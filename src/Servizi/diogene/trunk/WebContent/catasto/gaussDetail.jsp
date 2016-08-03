<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.catasto.bean.Gauss gauss=(it.escsolution.escwebgis.catasto.bean.Gauss)sessione.getAttribute("GAUSS"); 
	java.util.Vector vlistaGaussUnimm = null;
	if (gauss.getLayer().equals("FABBRICATI")){
		vlistaGaussUnimm =(java.util.Vector)sessione.getAttribute("GAUSS_UNIMM");
	}
%>
<%  it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.catasto.bean.GaussFinder finder = null;

	if (sessione.getAttribute("FINDER12") !=null){
		if (((Object)sessione.getAttribute("FINDER12")).getClass() == new it.escsolution.escwebgis.catasto.bean.GaussFinder().getClass()){
			finder = (it.escsolution.escwebgis.catasto.bean.GaussFinder)sessione.getAttribute("FINDER12");
			}
					
	}
	
	
int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();


java.util.Vector vctLink = null; 
if (sessione.getAttribute("LISTA_INTERFACCE") != null){
	vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
}
%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>


	

	
<html>
<head>
<title>Particelle - Dettaglio</title>
<LINK rel="stylesheet" href="<%= request.getContextPath() %>/styles/style.css" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/tab.css" type="text/css" >
<script src="<%=request.getContextPath()%>/ewg/Scripts/tabber.js" language="JavaScript"></script>
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function visualizzaTerreni(){
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoTerreni";		
	document.mainform.ST.value=3;
	document.mainform.EXT.value=1;
	document.mainform.UC.value=2;
	document.mainform.submit();
}
function visualizzaUimm(chiave){
	wait();
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoImmobili";		
	document.mainform.ST.value=3;
	document.mainform.EXT.value=1;
	document.mainform.UC.value=1;
	document.mainform.OGGETTO_SEL.value = chiave;
	document.mainform.submit();
}
</script>
<script>
function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body>

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/CatastoGauss" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Particelle </span>
</div>

&nbsp;

<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
<br/>

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getComune()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		

	<tr>		
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Sezione</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getSezione()%></span></td>
			</tr>
		</table>
		</td>
	</tr>

		
	<tr>		
		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Foglio</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getFoglio()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
		

		
	<tr>	

				
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Particella</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getParticella()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	

	
	
	<tr>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Area</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getPerimetro()%></span></td>
			</tr>
		</table>
		</td>
	
	</tr>
		
	<tr>	

	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Perimetro</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getPerimetro()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
	

	
	<tr>	

	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Data Fine Val.</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getDataFine()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
	
	<tr>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Layer</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getLayer()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	
	
	<%if (gauss.getLayer().equals("FABBRICATI")){ %>
	<tr>	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Indirizzo ICI</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getIndIci()%></span></td>
			</tr>
		</table>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Indirizzo Catasto</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getIndCata()%></span></td>
			</tr>
		</table>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:90;"><span class="TXTmainLabel">Indirizzo Viario</span></td>
				<td class="TDviewTextBox" style="width:250;"><span class="TXTviewTextBox"><%=gauss.getIndViarioRif()%></span></td>
			</tr>
		</table>				
		</td>			
	</tr>
	<%} %>
</table>
</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
</table>


<!-- FINE solo dettaglio -->

<br><br><br>
<% if (gauss.getLayer().equals("PARTICELLE") ){
   	  if ( !gauss.getParticellaTerreno().equals("") ){ 
   	 
   	  %>
		  <center><span class="TXTmainLabel"><a href="javascript:visualizzaTerreni()">Dettaglio Terreno</a></span></center>
<% 	  } 
}else{ %>

	<div class="tabber">
	
	<% if (vlistaGaussUnimm != null && vlistaGaussUnimm.size() >0 ){ %>
		<div class="tabbertab">
		<h2>Elenco UNITA' IMMOBILIARI </h2>
		 
		
		<table  class="viewExtTable" cellpadding="0" cellspacing="0">
		<tr>&nbsp;</tr>
			<tr class="extWinTXTTitle">Elenco Unità Immobiliari</tr>
		<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Sub.</span></td>		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Categoria</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Descrizione</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Titolari</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Classe</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Consistenza</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Rendita</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie catastale totale</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Superficie catastale ai fini TARSU</span></td>
		</tr>
		
		<%
		if (vlistaGaussUnimm != null){
			java.util.Enumeration en = vlistaGaussUnimm.elements(); 
			int contatore = 1; 
		%>
  		<% 	while (en.hasMoreElements()) {
        		it.escsolution.escwebgis.catasto.bean.Immobile unimm = (it.escsolution.escwebgis.catasto.bean.Immobile)en.nextElement();%>
    	
    	<tr id="r<%=contatore%>" onclick="visualizzaUimm('<%=unimm.getChiave()%>', '<%=contatore%>')">
		<td class="extWinTDData"   style='cursor: pointer;'>
		<span class="extWinTXTData" style="padding-left: 3px;"><%=unimm.getUnimm()%></span></td>		
		<td class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData" style="padding-left: 3px;"><%=unimm.getCodCategoria()%></span></td>
		<td class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData" style="padding-left: 3px;"><%=unimm.getCategoria()%></span></td>
		<td class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData" style="padding-left: 3px;"><%=unimm.getListaTitolari()%></span></td>
		<td class="extWinTDData" style='cursor: pointer;'>
		<span class="extWinTXTData" style="padding-left: 3px;"><%=unimm.getClasse()%></span></td>
		<td class="extWinTDData" style='cursor: pointer; text-align: right;'>
		<span class="extWinTXTData" style="padding-right: 3px;"><%=unimm.getVani()%></span></td>
		<td class="extWinTDData" style='cursor: pointer; text-align: right;'>
		<span class="extWinTXTData" style="padding-right: 3px;"><%=unimm.getRendita()%></span></td>
		<td class="extWinTDData" style='cursor: pointer; text-align: right;'>
		<span class="extWinTXTData" style="padding-right: 3px;"><%=unimm.getSuperficie()%></span></td>
		<td class="extWinTDData" style='cursor: pointer; text-align: right;'>
		<span class="extWinTXTData" style="padding-right: 3px;"><%=unimm.getDatiMetrici().getSupCatTarsu()%></span></td>
		</tr>
		<% 
				contatore++;
			} 
		}		
		%>
		</table>
		</div>
		<% } %>
		</div>
<% } %>


<br><br>



		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="12">
		<input type='hidden' name="OGGETTO_SEL" value="<%=gauss.getParticellaTerreno()%>">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>