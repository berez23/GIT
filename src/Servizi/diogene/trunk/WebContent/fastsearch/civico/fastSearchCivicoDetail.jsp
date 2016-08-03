<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.indice.fastsearch.soggetto.bean.FastSearchFinder"%>
<%@page import="it.webred.indice.fastsearch.civico.bean.CivicoTotaleBean" %>


<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	HashMap<String, IndiceBean> oggettiMap = (HashMap) session.getAttribute("oggettiMap");
	HashMap<String, Fonte> fonteDescr = (HashMap) session.getAttribute("fonteDescr");
	CivicoTotaleBean civico = (CivicoTotaleBean) session.getAttribute("civicoIndiceUnico");
	
	System.out.println("OggettiMap ["+oggettiMap+"]");
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
		
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>


<%@page import="java.util.HashMap"%>
<%@page import="it.webred.indice.fastsearch.bean.IndiceBean"%>
<%@page import="java.util.Iterator"%>

<head>
<title>Indice correlazione</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script language="Javascript">
	function eseguiCrossLinkInd(oggettoSel, fonte,tipo, descOrig){
		    targ = window.open('<%=request.getContextPath()%>/indice/wait.jsp', 'PopUpContrDetail_Corr', 'width=640,height=480,status=no,resizable=yes, scrollbars=yes');
			document.corrForm.target=targ.name;
			document.corrForm.fonte.value=fonte;
			//document.corrForm.progr.value=progr;
			document.corrForm.tipo.value=tipo;
			document.corrForm.idOrig.value=oggettoSel;
			document.corrForm.descOrig.value=descOrig;
			document.corrForm.submit();					
	}
</script>
<body>



<form name="corrForm"	  action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/IndiceCorrServlet" 
	  method="POST">

 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Elenco Civici totali</span>
</div>

<br/>
<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>
	 <td class="extWinTDTitle" width="50%">
 			<span class="extWinTXTTitle">
 	  			Indirizzo: <%=civico.getSedime() %> &nbsp; <%=civico.getIndirizzo() %> &nbsp; <%=civico.getCivico() %> 
 	  		 </span>
 	  	</td>
	</tr>
</table>

<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
 	  <tr>
 	  	<td class="extWinTDTitle" width="90%">
 			<span class="extWinTXTTitle">
 	  			Nome fonte
 	  		 </span>
 	  	</td>
 	  	<td class="extWinTDTitle" width="10%">
 			<span class="extWinTXTTitle">
 	  			Presente
 	  		 </span>
 	  	 </td>
 	  </tr>
 		<%
 		   Iterator<String> keys = fonteDescr.keySet().iterator();
 		   while (keys.hasNext()) {
 			   String key = keys.next();
 			   //String descr = fonteDescr.get(key);
 			   if (key.indexOf("_") == -1) {
 			   Fonte f = fonteDescr.get(key);
 			   IndiceBean ib = oggettiMap.get(f.getId());
 			   String img = "no.gif";
 			   int count = 0;
 			   
 			   if (ib != null) {
 				   img = "ok.gif";
 				   count = ib.getNumero();
 			   }
 			   
 		%>
 		<tr>
 		<td class="extWinTDData" width="90%">
 			<span class="extWinTXTData">
 				<%=f.getDescr()%>
 			</span>
 		</td>
 		<td class="extWinTDData">
 			<span class="extWinTXTData">
 			  <% if (ib != null) { %>
		 			  <span class="TXTmainLabel">
 							<img src="../../images/<%=img%>" 
 							
 							onclick="eseguiCrossLinkInd('<%=ib.getIdUnico()%>', '<%=ib.getFonteId()%>', 3, '<%=civico.getSedime() %> &nbsp; <%=civico.getIndirizzo() %> &nbsp; <%=civico.getCivico() %>')"
 							onMouseOver="javascript:this.style.cursor='pointer';" onMouseOut="javascript:this.style.cursor='default';" />
 					</span>
 			<% } else { %>
 					<img src="../../images/<%=img%>"  />
		  <% } %>

 			</span>
 		</td>
 		</tr>
 		<%
 			   }
 		   }
 		
 		%>
 	
 	

</table>
               


<input type='hidden' name="ST" value="3">
<input type="hidden" id="fonte" name="fonte" value="" />
<input type="hidden" id="progr" name="progr" value="" />
<input type="hidden" id="tipo" name="tipo" value="" />
<input type="hidden" id="idOrig" name="idOrig" value="" />
<input type="hidden" id="descOrig" name="descOrig" value="" />
<input type='hidden' name="action" value="1">
<input type='hidden' name="AZIONE" value="1">
<input type='hidden' name="UC" value="502">
<input type='hidden' name="BACK" value="">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>

<%@page import="it.webred.indice.Fonte"%></html>
