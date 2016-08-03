<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page language="java"
import="java.util.*,it.escsolution.escwebgis.docfa.bean.*, java.text.DecimalFormat,
			it.escsolution.escwebgis.docfa.servlet.*,it.escsolution.escwebgis.docfa.logic.*, 
			it.escsolution.eiv.database.*,
			it.webred.ct.data.access.basic.docfa.dto.*, it.webred.ct.data.access.basic.catasto.dto.*,
			it.webred.ct.data.model.docfa.*, it.webred.ct.data.access.aggregator.elaborazioni.dto.* "%>

<html>
<head>
<title>Docfa - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>
function apriClasseAttesa(valore, vani, zona,foglio,particella,sub,rapportovalore,consistenza,categoria,classe, rapporto)
{
			var url = '<%= request.getContextPath() %>/Docfa?UC=43&popupClasseAtt=true&valore='+valore+'&vani='+vani+'&zona='+zona+'&foglio='+foglio+'&particella='+particella+'&sub='+sub+'&rapportovalore='+rapportovalore+'&consistenza='+consistenza+'&categoria='+categoria+'&classe='+classe+'&rapporto=' +rapporto;
			var finestra=window.open(url,"_dati","height=400,width=600,status=yes,resizable=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");			
			finestra.focus();
}
</script>
</head>

<body>
		<div class="TXTmainLabel">Controllo Classamento - Consistenza per Foglio (Zone Censuarie Disponibili)</div>
		<table align=center class="extWinTable" style="width: 100%" >
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio </span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Part.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Zona</span></td>	
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Sup.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cons</span></td>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita x 100 (A)</span></td>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita x 105 (B)</span></td>	
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Val.Comm. (C)</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">C/A</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">C/B</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Class.Compatibile</span></td>	
			</tr>
			<%
			
			DecimalFormat DF = new DecimalFormat("#0.00");
			List<ControlloClassamentoConsistenzaDTO> lst = (ArrayList<ControlloClassamentoConsistenzaDTO>) request.getAttribute("LISTA_CLASSAMENTO_ALTRE_ZC");
			
			for(ControlloClassamentoConsistenzaDTO dc : lst){
			%>
				<tr>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getFoglio()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getParticella()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getSubalterno()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData" style="font-weight:bold;text-align:center;"><%=dc.getZona()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getSuperficie()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getCategoria()%></span></td>
					<td class="extWinTDData">
					
					   <table>
		                	<tr>
		                	<td width="50%"><span class="extWinTXTData" style="color:<%=dc.getClasseRif() %>;"><%=dc.getClasse()%></span></td>
		                	<td width="50%">
		                		<% String noClasseMin = DocfaLogic.CLASSI_MIN_STR.get(new Integer(dc.getClasseMin()));
		                		if (noClasseMin != null) {
		                			String msg = "Impossibile effettuare il controllo classamento consistenza per classe minima uguale a " + noClasseMin;
		                			if (noClasseMin.equals("ERROR")) {
		                				msg = "Impossibile effettuare il controllo classamento consistenza: classe minima non gestita o errore nel controllo";
		                			}%>
		                			<span class="extWinTXTData" style="color:<%=dc.getClasseRif() %>;"><%=msg%></span></td>
		                		<%} else if(dc.getMostraClasseMaggiormenteFrequente())%>
	                				<span class="extWinTXTData">(<%=dc.getClasseMin()%>)</span>
		                	</td>
		                	</tr>
		                </table>
					</td>
					
					<td class="extWinTDData">
					  <table>
				                <tr>
				                <td width="20%" align="center">
				                   <span class="extWinTXTData" style="color:<%=dc.getColore() %>;"><%=dc.getConsistenza()%></span>
				                </td>
				                <td width="30%" align="right">
				                
				                <%if(dc.getSuperfMediaMax() >0){ %>
				                
				                	<span class="extWinTXTData">(</span>
				                
				                <%if(dc.getSuperfMediaMax() >0 && dc.getSuperfMediaMax().doubleValue()!= 0.85d) %>
				                	<span class="extWinTXTData"><%=DF.format(dc.getSuperfMediaMin())%> - </span>
				             	
				             	<%if(dc.getConsisAnomalia()> 0 && Double.compare(dc.getSuperfMediaMax(), 0.8d)==0) %>
				             		<span class="extWinTXTData"><%=DF.format(dc.getConsisAnomalia())%></span>
				            
				                <%if(dc.getSuperfMediaMax()> 0 && dc.getSuperfMediaMax().doubleValue()!= 0.85d) %>
				                	<span class="extWinTXTData"><%=DF.format(dc.getSuperfMediaMax())%></span>
				                	
				              
				                	<span class="extWinTXTData">)</span>
				                
				                <%} %>
				                
				             	</td>
				                </tr>
				                </table>
					 </td>
					 <td class="extWinTDData"><span class="extWinTXTData"><%=dc.getRendita()!=null ? DF.format(dc.getRendita()) :"-"%></span></td>
					 <td class="extWinTDData"><span class="extWinTXTData"><%=dc.getRenditaX100()!=null ?  DF.format(dc.getRenditaX100()) : "-"%></span></td>
					 <td class="extWinTDData"><span class="extWinTXTData"><%=dc.getRenditaX105()!=null ?  DF.format(dc.getRenditaX105()) : "-"%></span></td>
					 <td class="extWinTDData">
					  		<span class="extWinTXTData"><%=(dc.getClassamentoCompatibile().booleanValue()==true && dc.getValoreCommerciale()!=null) ? DF.format(dc.getValoreCommerciale()) :"-"%></span>
					 </td>
					 <td class="extWinTDData"><span class="extWinTXTData" style="color:<%=dc.getValComSuRen100()>dc.getRapporto() ? "red" : "black" %>;">
					 		<%=(dc.getClassamentoCompatibile().booleanValue() && dc.getValComSuRen100()!=null) ?  DF.format(dc.getValComSuRen100()) : "-"%></span>
					 </td>
					  <td class="extWinTDData"><span class="extWinTXTData" style="color:<%=dc.getValComSuRen105()>dc.getRapporto() ? "red" : "black" %>;">
					 		<%=(dc.getClassamentoCompatibile().booleanValue() && dc.getValComSuRen105()!=null) ?  DF.format(dc.getValComSuRen105()) : "-"%></span>
					 </td>
				 	 <td class="extWinTDData">
				 	 <span class="extWinTXTData">
						<%if(dc.getCategoria() != null && dc.getCategoria().toLowerCase().startsWith("a") && dc.getValoreCommerciale() != null && dc.getRenditaX100() != null && dc.getRenditaX100()>0){
								
							String valComm = dc.getValoreCommerciale()!=null ? DF.format(dc.getValoreCommerciale()) : "";
							String zona = dc.getZona()!=null ? dc.getZona() : "-";
							String valComSuRendX105 = dc.getValComSuRen105()!=null ? DF.format(dc.getValComSuRen105()) : "";
							
						%>						
						<a href="javascript:apriClasseAttesa('<%=valComm%>','<%=dc.getConsistenza()%>','<%=zona%>','<%=dc.getFoglio()%>','<%=dc.getParticella()%>','<%=dc.getSubalterno()%>','<%=valComSuRendX105%>','<%=dc.getConsistenza()%>','<%=dc.getCategoria()%>','<%=dc.getClasse()%>','<%=dc.getRapporto()%>');">visualizza</a>
						<% }%>
					 </td>
				           
				
				</tr>
			<%
			}
			if(request.getAttribute("tabellaNonTrovata") != null)
				out.println("<script>alert('"+request.getAttribute("tabellaNonTrovata")+"');</script>");
			%>
		</table>		
<br>
<br>
<br>


</body>
</html>


