<%@ page language="java" import="it.escsolution.escwebgis.istat.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%   HttpSession sessione = request.getSession(true);  %> 
<%   Istat ist=(Istat)sessione.getAttribute("ISTAT"); %>
<%   java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
	 int st = new Integer(ST).intValue();%>

<%  it.escsolution.escwebgis.istat.bean.IstatFinder finder = null;

	if (sessione.getAttribute("FINDER13") !=null){
		if (((Object)sessione.getAttribute("FINDER13")).getClass() == new IstatFinder().getClass()){
			finder = (it.escsolution.escwebgis.istat.bean.IstatFinder)sessione.getAttribute("FINDER13");
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
<title>Istat Istat - Dettaglio</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>
<script>

function mettiST(){
	document.mainform.ST.value = 3;
}

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/IstatIstat" target="_parent">

<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Dati Istat</span>
</div>

&nbsp;

<table style="background-color: white; width: 100%;">
<tr style="background-color: white;">
<td style="background-color: white; vertical-align: top; text-align: center; width: 51%;">

<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	
	<tr>
		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Nome Sezione</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ist.getNomeSezione()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Comune</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ist.getComune()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Numero Sezione </span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ist.getNumeroSezione()%></span></td>
			</tr>
		</table>
		</td>
				
	</tr>
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	
	<tr>
		
		<td colspan=3>		
		<table class="viewExtTable" >
			<tr>	
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Nome Località</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ist.getNomeLocalita()%></span></td>
			</tr>			
		</table>
		</td>
	</tr>
		
</table>
	
&nbsp;
	
<table align=center cellpadding="0" cellspacing="0" class="editExtTable" style="background-color: #C0C0C0;">
	<tr>

		<td colspan=3>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:210;"><span class="TXTmainLabel">Residenti Totali</span></td>
				<td class="TDviewTextBox" style="width:210;"><span class="TXTviewTextBox"><%=ist.getPopResidTotale()%></span></td>
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
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Residenti Uomini</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ist.getPopResidMaschi()%></span></td>
			</tr>
		</table>
		</td>
		
		<td width=10></td>
		
		<td>
		<table class="viewExtTable" >
			<tr>
				<td class="TDmainLabel"  style="width:100;"><span class="TXTmainLabel">Residenti Donne</span></td>
				<td class="TDviewTextBox" style="width:100;"><span class="TXTviewTextBox"><%=ist.getPopResidFemmine()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Totali</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazTotali()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Occupate</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazOccupate()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Non Occupate</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazNonOccupate()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Costruite prima del 1919</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazAnte1919()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Costruite tra il 1919 e il 1945</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazTra1919e1945()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Costruite tra il 1946 e il 1960</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazTra1946e1960()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Costruite tra il 1961 e il 1971</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazTra1961e1971()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Costruite tra il 1972 e il 1981</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazTra1972e1981()%></span></td>
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
				<td class="TDmainLabel"  style="width:300;"><span class="TXTmainLabel">Abitazioni Costruite dopo il 1981</span></td>
				<td class="TDviewTextBox" style="width:120;"><span class="TXTviewTextBox"><%=ist.getAbitazDopo1981()%></span></td>
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
</table>

<!-- FINE solo dettaglio -->


		
<% if(finder != null){%>
	<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<% }else{%>
	<input type='hidden' name="ACT_PAGE" value="">
<% }%>

		<input type='hidden' name="AZIONE" value="">
		<input type='hidden' name="ST" value="">
		<input type='hidden' name="UC" value="13">
		<input type='hidden' name="BACK" value="">
		<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
		


</form>


<div id="wait" class="cursorWait" />
</body>
</html>