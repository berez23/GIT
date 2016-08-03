<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="it.webred.AMProfiler.beans.CodificaPermessi"%>
<%@page import="it.webred.AMProfiler.servlet.BaseAction"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet,org.displaytag.tags.TableTag"%>
<%@page import="org.apache.commons.beanutils.RowSetDynaClass"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.webred.permessi.GestionePermessi"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="it.webred.AMProfiler.servlet.SalvaUtente"%>
<%@page import="it.webred.AMProfiler.servlet.CaricaUtenti"%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="<%="http://"+request.getServerName()+":"+ request.getServerPort()%>/<%=request.getContextPath()%>/css/newstyle.css" rel="stylesheet" type="text/css" />
    <title>AMProfiler - Gestione Utenti</title>
    <%String pathWebApp = "http://"+request.getServerName()+":"+ request.getServerPort() +  request.getContextPath();%>
    <style type="text/css">
    	.imgLnk A:link {text-decoration: none;}
    	.imgLnk A:visited {text-decoration: none;}
    	.imgLnk A:active {text-decoration: none;}
    	.imgLnk A:hover {text-decoration: none;}
    	.lnkImg {border: 1px solid #808080;}
    </style>
  </head>

  <script type="text/javascript">
     function creaUtente() {
       var browser=navigator.appName;
       if(browser.indexOf("Microsoft") == -1)
         window.open("<%=pathWebApp%>/SalvaUtente?mode=new","Modifica Utente","width=600,height=400,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");
       else
         window.open("<%=pathWebApp%>/SalvaUtente?mode=new","Modifica_Utente","width=600,height=400,left=300,top=0,scrollbars=yes,resizable=yes,status=yes"); 
     }

     function updUtente(uN) {		
       var browser=navigator.appName;
       if(browser.indexOf("Microsoft") == -1)
       	window.open("<%=pathWebApp%>/SalvaUtente?mode=vis&userName=" + uN,"Modifica Utente","width=600,height=400,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");
       else
       	window.open("<%=pathWebApp%>/SalvaUtente?mode=vis&userName=" + uN,"Modifica_Utente","width=600,height=400,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");	
  	 }

  	 function getUpdPwd(myDate) {
  		var numMillisecondsVal = <%=SalvaUtente.numGiorniVal%> * 24 * 60 * 60 * 1000;
  		var dataUpd = new Date(myDate.substring(0, 4), myDate.substring(4, 6) - 1, myDate.substring(6, 8));
  		var dataScad = new Date();
  		dataScad.setTime(dataUpd.getTime() + numMillisecondsVal);
  		var numMillisecondsAnteScad = <%=SalvaUtente.numGiorniAnteScad%> * 24 * 60 * 60 * 1000;
  		var dataAnteScad = new Date();
  		dataAnteScad.setTime(dataScad.getTime() - numMillisecondsAnteScad);
  		var dataOggi = new Date();
  		var pathWebApp = "<%=pathWebApp%>";
  		var retVal = "<img class='lnkImg' src='" + pathWebApp + "/img/sem_verde.gif' title='password valida: scadenza " + formattaData(dataScad) + "' />";
  		if (dataOggi.getTime() > dataAnteScad.getTime()) {
  			retVal = "<img class='lnkImg' src='" + pathWebApp + "/img/sem_giallo.gif' title='password in scadenza (" + formattaData(dataScad) + ")' />";
  		}
  		if (dataOggi.getTime() > dataScad.getTime()) {
  			retVal = "<img class='lnkImg' src='" + pathWebApp + "/img/sem_rosso.gif' title='password scaduta' />";
  		}
  		return retVal;
  	}
   
  	function formattaData(myDate) {
  		var giorno = myDate.getDate();
  		if (giorno < 10) {
  			giorno = "0" + giorno;
  		}
  		var mese = myDate.getMonth() + 1;
  		if (mese < 10) {
  			mese = "0" + mese;
  		}
  		var anno = myDate.getYear();
  		return giorno + "/" + mese + "/" + anno;		
  	}
  </script>

  <body>

  <div id="centrecontent"> 
    <br>
    <form name="userForm" method="POST" action="<%=pathWebApp%>/SalvaUtente?mode=new" >
      <div align="left" style="padding-left: 10px">
        <input type="button" name="nuovoUtente" value="Nuovo Utente" onclick="creaUtente()"/>
      </div>
    </form>
  </div>
  
  <div id="listautenti" align="left" style="padding-left: 10px"><!--centre content goes here --> 
    <br>
    <font color="red">
      <c:if test="${giaPresente}">
        <c:out value="Nome utente già presente!"></c:out>
      </c:if>
      <c:if test="${noCancellazioneUtente}">
        <c:out value="${msgCancellazioneUtente}"></c:out>
      </c:if>
    </font>
  </div>

  <div align="left" style="padding-left: 10px">
    <display:table id="row" name="${userList}" pagesize="10" class="griglia">
      <display:column title="Nome Utente">
      	<c:if test="${row.DISABLE_CAUSE != 'CANCELLED'}">
     			<c:out value="${row.name}"></c:out>
     		</c:if>
    		<c:if test="${row.DISABLE_CAUSE == 'CANCELLED'}">
    			<font color="red"><c:out value="${row.name}"></c:out></font>
    		</c:if>
      </display:column>
      <display:column title="&nbsp;">
    		<script type="text/javascript">
    			document.write(getUpdPwd("${row.dt_upd_pwd}"));
    		</script>
    	</display:column>
  
  	<%if (GestionePermessi.autorizzato(request.getUserPrincipal(), CodificaPermessi.NOME_APP, CodificaPermessi.ITEM_MAPPING, CodificaPermessi.CANCELLA_UTENTE, true)){%>
  		<display:column title="Gestione">
  			<c:if test="${row.DISABLE_CAUSE != 'CANCELLED'}">
  				<a class="imgLnk" style="text-decoration: none" href="javascript:void(0);" onclick="updUtente('${row.name}')"> 
  					<img class="lnkImg" src="<%=pathWebApp%>/img/edit.gif" title="modifica utente"/>
  				</a>
  				<a class="imgLnk" href="<%=pathWebApp%>/CancellaUtente?userName=${row.name}"> 
  					<img class="lnkImg" src="<%=pathWebApp%>/img/deleteRed.gif" title="cancella"/>
  				</a>
  			</c:if>
  			<c:if test="${row.DISABLE_CAUSE == 'CANCELLED'}">
  				<a class="imgLnk" href="<%=pathWebApp%>/CancellaUtente?ripristina=yes&userName=${row.name}"> 
  					<img class="lnkImg" src="<%=pathWebApp%>/img/ok.gif" title="ripristina"/>
  				</a>
  			</c:if>
  		</display:column>
  	<%}%>
    
    </display:table>
  </div>

</body>
</html>
