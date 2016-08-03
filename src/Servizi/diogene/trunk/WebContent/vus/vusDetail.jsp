<%@ page language="java" import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.vus.bean.*,it.escsolution.escwebgis.vus.servlet.*,it.escsolution.escwebgis.vus.logic.* "%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Vus vus = (Vus) sessione.getAttribute(VusLogic.VUS); %>
<%   
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");	
	 int st = new Integer(ST).intValue();%>

<%  VusFinder finder = null;

	if (sessione.getAttribute(VusServlet.NOMEFINDER) !=null){
		if (((Object)sessione.getAttribute(VusServlet.NOMEFINDER)).getClass() == new VusFinder().getClass()){
			finder = (VusFinder)sessione.getAttribute(VusServlet.NOMEFINDER);
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

	

	
<html>
<head>
<title>Utenti VUS - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function vaiPart(chiave){
	document.mainform.action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Vus";
	document.mainform.OGGETTO_SEL.value=chiave;
	document.mainform.ST.value = 4;
	document.mainform.EXT.value = 1;
	document.mainform.submit();
    }
    
function mettiST(){
	document.mainform.ST.value = 3;
}

function chgtr(row,flg)
{
if (flg==1)
	{
	document.getElementById("r"+row).style.backgroundColor = "#d7d7d7";
	}
else
	{
	document.all("r"+row).style.backgroundColor = "";
	}
}    
</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Vus" target="_parent">

<div align="center" class="extWinTDTitle">
	<span class="extWinTXTTitle">Dettaglio Utenti Forniture Idriche</span>
</div>
&nbsp;
<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan=2>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Codice Utenza</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getCodSer()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td colspan=2>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Nominativo</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox" ><%=vus.getRagiSoc()%></span></td>
			</tr>    
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Cod.Fiscale</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=(vus.getCodiFisc()==null?" ":vus.getCodiFisc())%></span></td>
			</tr>
		</table>
		</td>
		<td>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:80;"><span class="TXTmainLabel">P.Iva</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=(vus.getPartitaIva()==null?" ":vus.getPartitaIva())%></span></td>
			</tr>			
		</table>
		</td>
	</tr>	
</table>
<br><br>
<span class="TXTmainLabel">Fornitura</span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getViaUbi()%> <%=vus.getCivicoUbi()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td >	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getCapUbi()%> - <%=vus.getComuneUbi()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Cod.Categoria</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=vus.getCodCategoria()%></span></td>
				<td class="TDmainLabel" style="width:200;"><span class="TXTmainLabel">&nbsp;</span></td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Descrizione</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getDescCategoria()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Consumo Medio</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getConsumoMedio()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br><br>
<span class="TXTmainLabel">Recapito</span>
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td >
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Indirizzo</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getViaResi()+" "+vus.getCivicoResi()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td >
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:300;"><span class="TXTviewTextBox"><%=vus.getCapResi()+" - "+vus.getComuneResi()%></span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>		
		<td>	
		<table class="viewExtTable" >
			<tr>			
				<td class="TDmainLabel"  style="width:120;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=vus.getPrResi()%></span></td>
				<td class="TDmainLabel" style="width:200;"><span class="TXTmainLabel">&nbsp;</span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br><br>
<span class="TXTmainLabel">Dati Catastali</span>
<table width="50%" align="center" class="extWinTable" cellpadding="0" cellspacing="0">
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Foglio</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Particella</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">Subalterno </span></td>
	</tr>
	<% VusGasCatasto vusCata; %>
  <% ArrayList appoCata = vus.getDatiCatastali();int contatore = 0;%>
  <% for(int i=0;i<appoCata.size();i++) {
	  	contatore++;
        vusCata = (VusGasCatasto) appoCata.get(i);
        if (vusCata.getFoglio() != null){%>

    <tr id="r<%=contatore%>" onclick="vaiPart('<%=vusCata.getChiave()%>')">
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= (vusCata.getFoglio()==null?" ":vusCata.getFoglio()) %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= (vusCata.getParticella()==null?" ":vusCata.getParticella()) %></span></td>
		<td class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
		<span class="extWinTXTData"><%= (vusCata.getSubalterno()==null?" ":vusCata.getSubalterno()) %></span></td>
	</tr>
<% 	}
	} %>
<br><br>

<!-- FINE solo dettaglio -->
<% if (vus != null){%>
<% String codice = "";
   codice = vus.getChiave(); %>
   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
    <%}%>


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="42">
		<input type='hidden' name="EXT" value="">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>