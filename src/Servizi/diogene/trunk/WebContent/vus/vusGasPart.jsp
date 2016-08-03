<%@ page language="java" import="it.escsolution.escwebgis.vus.servlet.*,it.escsolution.escwebgis.vus.bean.*,it.escsolution.escwebgis.vus.logic.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   VusGasCatasto vus = (VusGasCatasto) sessione.getAttribute(VusGasLogic.VUS_GAS_CATA); %>
<%  
	VusGasFinder finder = null;
	
	if (sessione.getAttribute(VusGasServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(VusGasServlet.NOMEFINDER)).getClass() == new VusGasFinder().getClass()){
			finder = (VusGasFinder)sessione.getAttribute(VusGasServlet.NOMEFINDER);
			}
					
	}
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();
		
	java.util.Vector vctLink = null; 
	if (sessione.getAttribute("LISTA_INTERFACCE") != null){
		vctLink = ((java.util.Vector)sessione.getAttribute("LISTA_INTERFACCE")); 
	}
%>

<html>
<head>
<title>Utenti VUS Gas - Dettaglio Dati Catastali</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<script>

function chgtr(row,flg)
		{
		if (flg==1)
			{
			document.all("r"+row).style.backgroundColor = "#d7d7d7";
			}
		else
			{
			document.all("r"+row).style.backgroundColor = "";
			}
		}

function mettiST(){
	document.mainform.ST.value = 2;
}
</script>
<body onload="mettiST()">

<form name="mainform" target="_parent" action="">
<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">Dettaglio Dato Catastale Utenza Gas</span>
</div>
&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top;">
	<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Codice Utenza</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getCodServizio()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan=2>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Foglio</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getFoglio()%></span></td>
			</tr>    
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan=2>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Particella</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getParticella()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Subalterno</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=(vus.getSubalterno()==null?" ":vus.getSubalterno())%></span></td>
			</tr>			
		</table>
		</td>
	</tr>	
</table>
<br>
<br>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Codice Anagrafico</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getCodAnagrafico()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Ragione Sociale</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getRagSociale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Cod. Com. Ammin</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getCodComAmmin()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Des. Com. Ammin</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getDesComAmmin()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan=2>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Loc. Ubicazione Fornitura</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getUbicFornitDesLoc()%></span></td>
			</tr>    
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan=2>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Via Ubicazione Fornitura</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getUbicFornitDesVia()%> <%=vus.getUbicFornitCivico() %></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Comune Catastale</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getCodComuneCatastale()%> - <%=vus.getComuneCatastale()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Sottotipologia Stato</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=(vus.getSottoTipoStato()==null?" ":vus.getSottoTipoStato())%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Dati Ines Incompleti</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=(vus.getDatiInesIncomp()==null?" ":vus.getDatiInesIncomp())%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Tipo Catasto</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getTipoCatasto()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Sezione Urbana</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=(vus.getSezioneUrbana()==null?" ":vus.getSezioneUrbana())%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Particella Sistema Tavolare</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=(vus.getParticellaSistTavolare()==null?" ":vus.getParticellaSistTavolare())%></span></td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Tipo Particella</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=(vus.getTipoParticella()==null?" ":vus.getTipoParticella())%></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</td>
<% if (vctLink != null && vctLink.size() > 0) { %>
<td class="iFrameLink">
	<jsp:include page="../frame/iFrameLink.jsp"></jsp:include>
</td>
<% } %>
</tr>
	
<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="<%=vus.getChiave()%>">
<input type='hidden' name="RECORD_SEL" value="">
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="44">
<input type='hidden' name="EXT" value="">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>