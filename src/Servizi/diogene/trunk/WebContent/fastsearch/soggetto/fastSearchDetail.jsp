<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.indice.fastsearch.soggetto.bean.FastSearchFinder"%>
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.webred.indice.fastsearch.bean.IndiceBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.webred.indice.fastsearch.soggetto.bean.SoggTotaleBean"%>
<%@page import="it.webred.indice.Fonte"%><html>

<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	HashMap<String, IndiceBean> oggettiMap = (HashMap) session.getAttribute("oggettiMap");
	HashMap<String, Fonte> fonteDescr = (HashMap) session.getAttribute("fonteDescr");
	SoggTotaleBean sogg = (SoggTotaleBean) session.getAttribute("soggIndiceUnico");
	EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	
	System.out.println("OggettiMap ["+oggettiMap+"]");
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
		
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>


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
	<%=funzionalita%>:&nbsp;Elenco Soggetti totali</span>
</div>

<br/>
<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>
	 <td class="extWinTDTitle" width="50%">
 			<span class="extWinTXTTitle">
 			<%
 				String descr1 = sogg.getNome() + " " + sogg.getCognome();
 				if ("G".equals(sogg.getTipoPersona()))
 					descr1 = sogg.getDenominaz();
 			%>
 	  			Soggetto: <%=descr1 %>
 	  		 </span>
 	  	</td>
	</tr>
</table>
<br/>
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
 			  <% if (ib != null) { 
 				  
 				  if(descr1!=null)
 					  descr1 = descr1.replaceAll("'", " ");
 				  else descr1 = "";
 				  
 				%>
		 			  <span class="TXTmainLabel">
 					 	<img src="../../images/<%=img%>" 
 							 onclick="eseguiCrossLinkInd('<%=ib.getIdUnico()%>', '<%=ib.getFonteId()%>', 1, '<%=descr1 %>')"
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
<input type='hidden' name="UC" value="500">
<input type='hidden' name="BACK" value="">
</table>
</form>

  
 <table>
 <tr><td>
 <!-- ACCESSO CRIF -->
  <form action="http://ws1201.sintesinet.com/wps/myportal/dcam/IKViewPortlet" id="frmIKAccess" method="post">
		<input type="hidden" name="uid" value="novaratest"/>
	
		<input type="hidden" name="enteid" value="<%=eu.getEnte()%>"/>
	
		<input type="hidden" name="ikcf" value="<%=sogg.getCodFisc()%>" maxlength="16" />
	
		<input type="submit" value="Accedi a Servizio CRIF" />
	</form>
	</td>
	<%
		System.out.println("Ricerca Permesso Visura Nazionale "+eu.getUtente().getName()+"|"+eu.getNomeIstanza()+"|"+DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI+"|"+ DecodificaPermessi.PERMESSO_TEMA_VISURA_NAZIONALE);
		if (GestionePermessi.autorizzato(eu.getUtente() , eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_TEMA_VISURA_NAZIONALE, true)) {
	%>
	<td>
	 <!-- ACCESSO VISURA NAZIONALE -->
	<form >
		<input type="submit" value="Accedi a Visura Nazionale" 
		onclick=" window.open('<%=request.getContextPath()%>/VisuraNazionale?OGGETTO_SEL_CF=<%=sogg.getCodFisc()%>&UC=126&ST=3&NONAV=1', 'PopUpSISTER', 'width=640,height=480,status=no,resizable=yes, scrollbars=yes')"/>
	</form>
	</td>
	<%} %>
	</tr></table>
<div id="wait" class="cursorWait" />
</body>
</html>
