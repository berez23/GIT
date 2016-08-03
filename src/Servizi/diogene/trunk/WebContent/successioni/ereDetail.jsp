<%@ page language="java" import="it.escsolution.escwebgis.successioni.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%   HttpSession sessione = request.getSession(true);  %> 
<%   Erede def=(Erede)sessione.getAttribute("EREDE"); %>

<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%  it.escsolution.escwebgis.successioni.bean.EredeFinder finder = null;
	if (sessione.getAttribute("FINDER18") !=null){
		if (((Object)sessione.getAttribute("FINDER18")).getClass() == new EredeFinder().getClass()){
			finder = (it.escsolution.escwebgis.successioni.bean.EredeFinder)sessione.getAttribute("FINDER18");
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
<% String codice = "";
 if (def != null){

   codice = def.getUfficio()+"|"+def.getAnno()+"|"+def.getVolume()+"|"+def.getNumero()+"|"+def.getSottonumero()+"|"+def.getComune()+"|"+def.getProgressivo()+"|"+def.getProgressivoErede();
   
    }%>
	
<html>  
<head>
<title>Successioni Eredi - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>



function mettiST(){
	document.mainform.ST.value = 3;
}

function visualizzaEredita() {
	
	var url = '<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/SuccessioniEredi?ST=4&EXT=1&UC=18&OGGETTO_SEL=<%=codice%>';
	var finestra=window.open(url,"","height=400,width=600,status=yes,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no");			
	finestra.focus();
}

</script>
<body onload="mettiST()">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	&nbsp;<%=funzionalita%>:&nbsp;Dati Erede</span>
</div>

&nbsp;
<input type="Image" ID="UserCommand8"  align="MIDDLE" src="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif"  border="0" vspace="0" hspace="0"  alt = "Stampa"  
						onMouseOver="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Over.gif')" 
						onMouseOut="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" 
						onMouseDown="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print_Down.gif')" 
						onMouseUp="setbutton('UserCommand8','<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/print.gif')" onClick="Stampa()">
	<br/>
<jsp:include page="../frame/iFrameLinkIndice.jsp"></jsp:include>

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/SuccessioniEredi" target="_parent">





<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width=210;"><span class="TXTmainLabel">Codice Fiscale</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=def.getCodFiscale()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Denominazione</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getDenominazione()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
				
		<td>		
		</td>
	</tr>
	
</table>
	
&nbsp;
	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Nato a </span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getComuneNascita()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
	
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Provincia</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getProvNascita()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Nato il</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getDataNascita()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sesso</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getSesso()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	
</table>

&nbsp;
<!-- DATI PRATICA -->	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		
		<td colspan='3'>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100%;"><span class="TXTmainLabel">Dati Pratica </span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Ufficio</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getUfficio()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Anno</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getAnno()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Volume</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getVolume()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Numero</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getNumero()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Sottonumero</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getSottonumero()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getComune()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Progressivo</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getProgressivo()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Progressivo Erede</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=def.getProgressivoErede()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	
</table>

</td>

</tr>
</table>



   <input type='hidden' name="OGGETTO_SEL" value="<%=codice%>">
  

<br><br><br>
<% if (st == 3 || st == 33 || st == 101){%>
<center>
<span class="TXTmainLabel">
	<a href="javascript:visualizzaEredita()">Visualizza Eredita'</a>
</span>
</center>

<%}%>
<br><br>  


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="18">
		<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

</form>


<div id="wait" class="cursorWait" />
</body>
</html>