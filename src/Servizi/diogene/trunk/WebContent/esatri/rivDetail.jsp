<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   it.escsolution.escwebgis.esatri.bean.Riversamento riv=(it.escsolution.escwebgis.esatri.bean.Riversamento)sessione.getAttribute(it.escsolution.escwebgis.esatri.logic.EsatriRiversamentiLogic.ESATRI_RIVERSAMENTO); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>
<%  it.escsolution.escwebgis.esatri.bean.RiversamentoFinder finder = null;

	if (sessione.getAttribute("FINDER37") !=null){
		if (((Object)sessione.getAttribute("FINDER37")).getClass() == new it.escsolution.escwebgis.esatri.bean.RiversamentoFinder().getClass()){
			finder = (it.escsolution.escwebgis.esatri.bean.RiversamentoFinder)sessione.getAttribute("FINDER37");
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
<title>Esatri Riversamenti - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/EsatriRiversamenti" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Riversamento</span>
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
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Anno Rif.Riscossioni</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getAnnoRiferimento()%></span></td>
			</tr>
		</table>
		</td>
		<td width=10></td>
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Data Scadenza</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getDataScadenza()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	
</table>
	
&nbsp;
	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Cod.Concessione</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getCodConcessione()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Progr.Invio</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getProgressivoInvio()%></span></td>
			</tr>
		</table>
		</td>
		
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	
	<tr>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Num.Quietanza</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getNumQuietanza()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Cod.Tesoreria</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getCodTesoreria()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	
</table>

&nbsp;
<!-- DATI -->	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Data Riversamento</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getDataRiv()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Tipo Riversamento</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getTipoRiv()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Importo Versato</span></td>
				<td class="TDviewTextBoxDX" style="width:100;"><span class="TXTviewTextBoxDX"><%=riv.getImportoVersato()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Importo Commissione</span></td>
				<td class="TDviewTextBoxDX" style="width:100;"><span class="TXTviewTextBoxDX"><%=riv.getImportoCommissione()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Num.Riscossioni</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getNumRiscossioni()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Tipo Riscossioni</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=riv.getTipoRisc()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</td>

</tr>
</table>


<!-- FINE solo dettaglio -->


<% if (riv != null){%>
<% String codice = riv.getPkRiversamento();%>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
<%}%>

<br><br><br>
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="37">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>