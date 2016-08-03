<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<html>
	<head>
  	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  	<link href="<%="http://"+request.getServerName()+":"+ request.getServerPort()%>/<%=request.getContextPath()%>/css/newstyle.css" rel="stylesheet" type="text/css" />
  	<title>AMProfiler - Modifica Utente</title>
  	<%String pathWebApp = "http://"+request.getServerName()+":"+ request.getServerPort() +  request.getContextPath();%>
	</head>
	
	<body>
		<script>
			var doLogout = <%=request.getParameter("doLogout") != null && new Boolean(request.getParameter("doLogout")).booleanValue()%>
			<% 
			  String ctxPath;
				if (request.getParameter("pwdScaduta") != null && (new Boolean(request.getParameter("pwdScaduta"))).booleanValue()) {
					  if (request.getParameter("pathApp") != null && !request.getParameter("pathApp").equals("")) {
						  ctxPath = request.getParameter("pathApp");
            } else {
            	  ctxPath = request.getContextPath();
        	  }
				} else {
					ctxPath = request.getContextPath();
				}
      %>

      if (doLogout) {
    	  if (opener && opener.opener) {
					opener.opener.location = "<%=ctxPath%>/Logout";
					opener.close();
					window.close();
				} else if (opener) {
					opener.location = "<%=ctxPath%>/Logout";
					window.close();
				} else {
					window.location = "<%=ctxPath%>/Logout";
				}
			}
      function creaGruppo() {
          var browser=navigator.appName;
          if(browser.indexOf("Microsoft") == -1)
            window.open("<%=pathWebApp%>/NuovoGruppo","Nuovo Gruppo","width=600,height=400,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");
          else
            window.open("<%=pathWebApp%>/NuovoGruppo","Nuovo_Gruppo","width=600,height=400,left=300,top=0,scrollbars=yes,resizable=yes,status=yes"); 
      }
		</script>		

		<div id="centrecontent"><!--centre content goes here --> 
			<br>
			<form name="userForm" method="POST" action="<%=pathWebApp%>/SalvaUtente?mode=<%=request.getAttribute("mode")%>" >
				<c:if test="${noPwd}">
					<font color="red" style="font-size: 18px; font-weight: bold;">
	  				Attenzione. Nuova password non inserita.
	  			</font>
		 		</c:if> 

				<c:if test="${pwdError}">
					<font color="red" style="font-size: 18px; font-weight: bold;">
	  				Attenzione.&nbsp;La conferma della nuova password non &egrave; valida: la <b>password</b> e la <b>conferma password</b> devono coincidere.
	  			</font>
		 		</c:if> 

				<c:if test="${salvaOk!=null}">
					<font color="red" style="font-size: 18px; font-weight: bold;">
						<% if (!(request.getParameter("soloMsg") != null && (new Boolean(request.getParameter("soloMsg"))).booleanValue())) {%>
		  			<c:out value="${salvaOk}"/>
			  		<% } else {%>
		  			<c:out value="Modifica password effettuata correttamente! Chiudi e riapri l'applicazione per rendere effettiva la variazione."/>
			  		<% } %>
			  	</font>
				</c:if>
		
				<% if (request.getParameter("pwdScaduta") != null && (new Boolean(request.getParameter("pwdScaduta"))).booleanValue()) {
					if (request.getAttribute("noPwd") == null && request.getAttribute("pwdError") == null && request.getAttribute("salvaOk") == null) {%>
					<div style="color: red; text-align: middle; font-size: 14px; font-weight: bold;">
		  				Password scaduta: inserire nuova password
		  			</div>
				<%}}%>
		
				<table width="90%" cellspacing="5" align="center">
  		    <tr>
  		      <th align="right">Username:</th>
  		      <%if (request.getAttribute("uN") != null && !"".equals(request.getAttribute("uN")))  {%>
  			      <td align="left">
                <input type="text" name="userName" readonly="readonly" value="<%=request.getAttribute("uN")%>"/>
              </td>
  		      <%} else {%>
  			      <td align="left"><input type="text" name="userName"></td>
  		      <%}%>
  		    </tr>

			    <% if (!(request.getParameter("soloMsg") != null && (new Boolean(request.getParameter("soloMsg"))).booleanValue())) {%>
    
          <% if ((request.getAttribute("showPwd") != null && (new Boolean(request.getAttribute("showPwd").toString())).booleanValue() )) {%>
			    <tr>
			      <th align="right">Nuova Password:</th>
			      <td align="left"><input type="password" name="password"></td>
			    </tr>

          <tr>
            <th align="right">Conferma Nuova Password:</th>
            <td align="left"><input type="password" name="password2"></td>
          </tr>
          <tr>
            <th align="right">Password di default valida:</th>
            <td align="left">
              <input type=checkbox name="flPwdValida" 
                     title="Se selezionato, assegna all'utente una password non scaduta; viceversa, l'utente dovrà, al primo accesso, effettuare subito la modifica dalla password.">
            </td>
          </tr>  
          <%}%>
          <% if ((request.getAttribute("showGroup") != null && (new Boolean(request.getAttribute("showGroup").toString())).booleanValue() )) {%>
          <tr><td><br/></td></tr>
          <tr>
            <th align="right">Gruppi:</th>
            <td align="left">
              <div id="listagruppi">
                <br/>
                <select NAME="gruppi" size="4" multiple ondblclick="document.permessiForm.submit()" >
                  <c:forEach var="gruppo" items="${gruppi}">
                    <c:if test="${gruppo.checked == true}">
                      <option selected value=" <c:out value="${gruppo.name}" />">
                        <c:out value="${gruppo.name}" />
                      </option>
                    </c:if>
                    <c:if test="${gruppo.checked != true}">
                      <option value=" <c:out value="${gruppo.name}" />">
                        <c:out value="${gruppo.name}" />
                      </option>
                    </c:if>
                  </c:forEach>
                </select>
              </div>
            </td>
          </tr>
          <tr>
            <th align="right"></th>
            <td align="left">
              <input type="button" name="nuovoGruppo" value="Nuovo Gruppo" onclick="creaGruppo();" />
            </td>
          </tr>
          <% }%>   
          <% }%>   

          <tr>
			      <td align="center" colspan="2">
			      	<% if (!(request.getParameter("soloMsg") != null && (new Boolean(request.getParameter("soloMsg"))).booleanValue())) {%>
			      		<input type="submit" name="submit" value="Salva">
			      	<% }%>
			      	<input type="button" value="Chiudi" onclick="window.close();">
			      </td>
			    </tr>
				</table>
				
        <% if ((request.getParameter("pwdScaduta") != null && new Boolean(request.getParameter("pwdScaduta")).booleanValue()) ||
						(request.getUserPrincipal()).getName().equals(request.getAttribute("uN"))) {%>
			    	<div style="color: red; text-align: middle; font-size: 14px; font-weight: bold;">
	  					Al termine dell'operazione, si verrà reindirizzati alla pagina di login.
	  				</div>
			    <% } %>
				
        <input name="pwdScaduta" type="hidden" value="<%=request.getParameter("pwdScaduta") == null ? false : request.getParameter("pwdScaduta")%>"></input>
        <input name="pathApp" type="hidden" value="<%=request.getParameter("pathApp") == null ? "" : request.getParameter("pathApp")%>"></input>
			</form>
		</div>
	</body>
</html>
