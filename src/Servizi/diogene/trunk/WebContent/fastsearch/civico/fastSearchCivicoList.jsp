<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.indice.fastsearch.civico.bean.FastSearchCivicoFinder"%>
<%@page import="it.webred.indice.fastsearch.civico.bean.CivicoTotaleBean" %>
<%@page import="java.util.List" %>
<%@page import="java.util.HashMap" %>

<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	java.util.Vector listaCivico =(java.util.Vector)sessione.getAttribute("indice.lista_civico"); 
	
	FastSearchCivicoFinder finder = null;
	if (sessione.getAttribute("FINDER502")!= null){
		finder = (FastSearchCivicoFinder)sessione.getAttribute("FINDER502"); 
	}else 
		finder = new FastSearchCivicoFinder();
	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");
%>
<%   it.escsolution.escwebgis.common.PulsantiNavigazione pulsanti = (it.escsolution.escwebgis.common.PulsantiNavigazione)sessione.getAttribute("PULSANTI");%>

<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
		
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>

<html>
<head>
<title>Indice correlazione - Lista</title>
<LINK rel="stylesheet" href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css" type="text/css">
<script src="../../ewg/Scripts/dynamic.js" language="JavaScript"></script>
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



function vai(codice, record_cliccato){
	wait();
	document.mainform.OGGETTO_SEL.value=codice;
	document.mainform.RECORD_SEL.value=record_cliccato;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}


function vai(codice, record_cliccato, isPopUp)
{
	try
	{
		document.mainform.OGGETTO_SEL.value = codice;
		document.mainform.RECORD_SEL.value = record_cliccato;
		if (isPopUp)
		{
			targ = apriPopUp(record_cliccato);
			
			if (targ)
			{
				document.mainform.ST.value = 3;
				document.mainform.target = targ;
				document.mainform.submit();
				document.mainform.ST.value = 2;
				document.mainform.target = "_parent";
			}
		}
		else
		{
			wait();
			document.mainform.ST.value = 3;
			document.mainform.target = "_parent";
			document.mainform.submit();
		}
	}
	catch (e)
	{
		//alert(e);
	}
}

var popUpAperte = new Array();
function apriPopUp(index)
{
	if (popUpAperte[index])
	{
		if (popUpAperte[index].closed)
		{
			popUpAperte[index] = window.open("", "PopUpFastSearchCivicoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
			popUpAperte[index].focus();
			return popUpAperte[index].name;
		}
		else
		{
			popUpAperte[index].focus();
			return false;
		}
	}
	else
	{
		popUpAperte[index] = window.open("", "PopUpFastSearchCivicoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}


function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (listaCivico.size() > 0) {
 CivicoTotaleBean civico = (CivicoTotaleBean)listaCivico.get(0);
 
 %>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=civico.getIdDwh()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/FastSearchCivici" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Elenco Civici totali</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle" width="20%"><span class="extWinTXTTitle">INDIRIZZO da Fonte di Riferimento</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">INDIRIZZO da Fonti Dati</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">Fonte</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">Tipo</span></td>
	</tr>
	
  <% CivicoTotaleBean civico = new CivicoTotaleBean(); %>
  <% java.util.Enumeration en = listaCivico.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>

	
 
  <% 
     while (en.hasMoreElements()) {
        civico = (CivicoTotaleBean)en.nextElement();
        
       List<List<CivicoTotaleBean>> mappa = civico.getCiviciAssociati();
        
        int somma=0;
        for(List<CivicoTotaleBean> civ_i : mappa){
			somma += civ_i.size();
        }
        
        int rowspan1 = somma;
        
        boolean primoTotale = true;
        
    	String stileCellaTD = contatore%2==0 ? "extWinTDDataAlter" : "extWinTDData";
        
        
		%>
    <tr id="r<%=contatore%>" style="">
		
		<td onclick="vai('<%=civico.getIdDwh()%>', '<%=progressivoRecord%>', true)" class=<%=stileCellaTD%> onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan1%>>
		<span class="extWinTXTData">
		
		<%=civico.getSedime() != null ? civico.getSedime() + " ": ""%>
		<%=civico.getIndirizzo() != null ? civico.getIndirizzo() + " ": ""%>
		<%=civico.getCivico() != null ? civico.getCivico() + " ": ""%>
		
		</span></td>
		
				
		<% 	
			
			for(List<CivicoTotaleBean> civ_i : mappa){
				int rowspan2 = civ_i.size();
				boolean primo = true;
				for(CivicoTotaleBean totale : civ_i){ 
			%>	
				<%if(!primoTotale){%>
					<tr>
				<% }  %>
				<%if(primo){  %>
					<td class=<%=stileCellaTD%> width="33%" rowspan=<%=rowspan2%>  >
					<span class="extWinTXTData">
						<%=totale.getSedime() != null ? totale.getSedime() + " ": ""%>
						<%=totale.getIndirizzo() != null ? totale.getIndirizzo() : ""%>
						<%=totale.getCivico() != null ? totale.getCivico() + " ": ""%>
					</span>
					</td>
				<% } %>
				
				<td class=<%=stileCellaTD%> width="33%" >
				<span class="extWinTXTData">
					<%=totale.getDescrFonte() != null ? totale.getDescrFonte() : ", "%>
				</span>
				</td>
				<td class=<%=stileCellaTD%> width="33%">
				<span class="extWinTXTData">
					<%=totale.getDescrTipoInfo() != null ? totale.getDescrTipoInfo() : ""%>
				</span>
				</td>
				</tr>
			<% primo=false; 
			   primoTotale=false; } 
			} 
		
	contatore++;
	progressivoRecord ++;

	} %>

<input type='hidden' name="ST" value="">
<input type='hidden' name="OGGETTO_SEL" value="">
<input type='hidden' name="RECORD_SEL" value="">
<% if (finder != null){%>
<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
<%}%>
<input type='hidden' name="AZIONE" value="">
<input type='hidden' name="UC" value="502">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>