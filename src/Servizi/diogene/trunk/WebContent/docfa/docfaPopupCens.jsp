<%@ page language="java"
	import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.docfa.bean.*,it.escsolution.escwebgis.docfa.logic.*,it.escsolution.escwebgis.catasto.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%
java.util.ArrayList listaDocfaDatiCensuari = (java.util.ArrayList) request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_CENSUARI);
Integer numPlan = (Integer)request.getAttribute(DocfaLogic.NUMERO_DI_PLANIMETRIE);
String codice = request.getParameter("codice");
%>


<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.tributi.bean.OggettiTARSU"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<html>
<head>
<title>Docfa - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<body>
	
		<table align=center class="extWinTable" style="width: 100%">
			<tr class="extWinTXTTitle">Dettaglio superfici per vano (DOCFA)</tr>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Superfice Metr.</span></td>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Ambiente</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Altezza</span></td>
			</tr>
			<%
			
			String  protocollo = codice.substring(0,codice.indexOf("|"));
			String fornitura = codice.substring(codice.indexOf("|")+1);

			java.util.Iterator it  = listaDocfaDatiCensuari.iterator();
				while (it.hasNext())
				{
					Docfa dc = (Docfa) it.next();
					%>
			<tr>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getSubalterno()%></span></td>
				
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getSuperficeMetrici()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getAmbiente()%></span></td>
				<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getAltezza()%></span></td>
			</tr>
			<%
				}
			%>
		</table>
		

<br>
		
		
		<%
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		if (GestionePermessi.autorizzato(eu.getUtente() , eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE,true)) {
		if (numPlan == null ||
				(numPlan).intValue() == 0 ) {
		%>
		<table align=center class="extWinTable" style="width: 100%">
			<tr>
				<td class="extWinTDData" >
				<h1>
					Planimetrie non presenti
				</h1>
				</td>
			</tr>
		</table>				
		<%
		} else {
			if(request.getAttribute(DocfaLogic.PLANIMETRIE_SENZA_IMM_SOLO_REQUEST) != null)
				out.println("<span class=\"extWinTXTTitle\"  style=\"text-align: center;color: red;\"><center>ATTENZIONE: questo protocollo ha "+request.getAttribute(DocfaLogic.PLANIMETRIE_SENZA_IMM_SOLO_REQUEST)+" planimetria/e senza IMMOBILE</center></span><br>");
		%>

			<DIV STYLE="overflow: auto; 
	         border-left: 1px gray solid; border-bottom: 1px gray solid; 
	         padding:0px; margin: 0px">
			<table align=center class="extWinTable" style="width: 100%">
					<%
					for (int i=1;i<=numPlan;i++) {
					%>
					<tr>
						<td class="extWinTDData">
							<span class="extWinTXTData" style="font-size: 14px;">
								Scarica Planimetria n. <%=i%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="DocfaImmaginiPlanimetrie?protocollo=<%=protocollo%>&fornitura=<%=fornitura%>&numImmagine=<%=i%>&idimmo=<%=request.getParameter("im")%>&idFunz=1">
								pdf</a>
								 &nbsp;
								<a href="DocfaImmaginiPlanimetrie?protocollo=<%=protocollo%>&fornitura=<%=fornitura%>&numImmagine=<%=i%>&idimmo=<%=request.getParameter("im")%>&idFunz=1&openJpg=true">
								img</a>
								<%
									if (GestionePermessi.autorizzato(eu.getUtente() , eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_ELIMINA_WATERMARK, true)) {
								%>
										&nbsp;
										<a href="DocfaImmaginiPlanimetrie?protocollo=<%=protocollo%>&fornitura=<%=fornitura%>&numImmagine=<%=i%>&idimmo=<%=request.getParameter("im")%>&idFunz=1&openJpg=true&watermark=no">
										img (senza watermark)</a>
								<%
									}
								%>
							</span>
						</td>
					</tr>
					<%
					}
					%>
			</table>				
			</div>
		<%
		}
	}
	%>

<br>
		
		
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr class="extWinTXTTitle" >Dettaglio superfici per vano (comma 340) Foglio: <%=request.getParameter("f")%> Particella: <%=request.getParameter("m")%> Sub.: <%=request.getParameter("s")%></tr>
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
	
	ArrayList planimetrieComma340 = (ArrayList)request.getAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_DOCFA_CENS); 
	 if (planimetrieComma340 != null && planimetrieComma340.size() > 0) { 
	%>

			<br/>
			
			<table width="70%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr class="extWinTXTTitle">Planimetrie comma 340</tr>
	<%
			Iterator ite = planimetrieComma340.iterator();
			boolean sub = false;
			boolean sub9999 = false;
			while(ite.hasNext())
			{
				ArrayList link = (ArrayList)ite.next();
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
						href="<%= request.getContextPath() %>/CatastoPlanimetrieComma340?pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						PDF
						</a>
						&nbsp;
						<a style="font-size: 12px;" 
						href="<%= request.getContextPath() %>/CatastoPlanimetrieComma340?openJpg=true&pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
						IMG
						</a>
						<%
							if (GestionePermessi.autorizzato(eu.getUtente() , eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_ELIMINA_WATERMARK, true)) {
						%>
								&nbsp;
								<a style="font-size: 12px;" 
								href="<%= request.getContextPath() %>/CatastoPlanimetrieComma340?openJpg=true&watermark=no&pathCompleto=<%=(String)link.get(1)%>&fileName=<%=(String)link.get(0)%>&formato=<%=(String)link.get(3)%>">
								IMG (SENZA WATERMARK)
								</a>								
						<%
							}
						%>
					</td>
				</tr>
			<%}%>
			</table>
	<% }%>
	

</body>
</html>
