<%@ page language="java"
	import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.docfa.logic.*,it.escsolution.escwebgis.catasto.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%
java.util.ArrayList listaDocfaDatiCensuari = (java.util.ArrayList) request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_CENSUARI);
Integer numPlan = (Integer)request.getAttribute(DocfaLogic.NUMERO_DI_PLANIMETRIE);
%>


<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.tributi.bean.OggettiTARSU"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<html>
<head>
<title>Docfa - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<body>


		
		<span class="extWinTXTTitle">Dettaglio superfici per vano (comma 340) Foglio: <%=request.getParameter("f")%> Particella: <%=request.getParameter("m")%> Sub.: <%=request.getParameter("s")%></span>
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Vano<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Piano</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Edificio</span></td>				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie<span class="extWinTXTTitle"></td>				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Ambiente</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza Min.<span class="extWinTXTTitle"></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza Max.<span class="extWinTXTTitle"></td>
			</tr>
			<% 
			ArrayList<OggettiTARSU> lvani = (ArrayList<OggettiTARSU>)request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_340);
			Iterator itv = lvani.iterator();			
			
			if(lvani == null || lvani.size() < 1)
			{
				out.println("<tr>");
				out.println("<td class=\"extWinTDData\" colspan=7 align=\"center\">Misurazioni comma 340 non presenti</td>");
				out.println("</tr>");
				
			}
			while(itv.hasNext())
			{
				OggettiTARSU vani = (OggettiTARSU)itv.next();
			%>
			<tr>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getVano()%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getPiano()%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getEdificio()%></span></td>				
				<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getSupVani()%></span></td>
				<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getAmbiente()%></span></td>				
				<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getAltezzaMin()%></span></td>			
				<td class="extWinTDData" ><span class="extWinTXTData"><%=vani.getAltezzaMax()%></span></td>
			</tr>
			<%}%>
		</table>
		
		<% 
	
	ArrayList planimetrieComma340 = (ArrayList)request.getAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_DOCFA);
	
	 if (planimetrieComma340 != null && planimetrieComma340.size() > 0) { 
	%>

			<br/>
			<center><span class="extWinTXTTitle">Planimetrie comma 340</span></center>
			<center><table width="70%" class="extWinTable" cellpadding="0" cellspacing="0">
			
	<%
		
			Iterator it = planimetrieComma340.iterator();
			boolean sub = false;
			boolean sub9999 = false;
			while(it.hasNext())
			{
				ArrayList link = (ArrayList)it.next();
			%>
			<% if (!((String)link.get(2)).equalsIgnoreCase("9999")) {
				if (!sub) {
					sub = true;%>
					<tr>
						<td class="extWinTDTitle" ><span class="extWinTXTTitle">Planimetrie catastali associate alla UIU</span>
						</td>
					</tr>
			<%	} 
			} else {
				if (!sub9999) {
					sub9999 = true;%>
					<tr>
						<td class="extWinTDTitle" ><span class="extWinTXTTitle">Planimetrie catastali associate al corpo di fabbrica</span>
						</td>
					</tr>
			<% 	}
			}%>
				<tr>					
					<td class="extWinTDData">
						<span class="extWinTXTData">
							<%=(String)link.get(0)%>
						</span>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<a style="font-size: 12px;" 
						href="<%= EscServlet.URL_PATH %>/CatastoPlanimetrieComma340?pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						PDF
						</a>
						&nbsp;
						<a style="font-size: 12px;" 
						href="<%= EscServlet.URL_PATH %>/CatastoPlanimetrieComma340?openJpg=true&pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						IMG
						</a>
					</td>
				</tr>
			<%}%>
			
			</table></center>
	<% } %>
	



</body>
</html>
