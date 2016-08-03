<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.indice.fastsearch.fabbricato.bean.FastSearchFabbricatoFinder"%>
<%@page import="it.webred.indice.fastsearch.fabbricato.bean.FabbricatoTotaleBean" %>
<%@page import="java.util.List" %>
<%@page import="java.util.HashMap" %>

<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	java.util.Vector listaFabbricati =(java.util.Vector)sessione.getAttribute("indice.lista_fabbricati"); 
	
	FastSearchFabbricatoFinder finder = null;
	if (sessione.getAttribute("FINDER504")!= null){
		finder = (FastSearchFabbricatoFinder)sessione.getAttribute("FINDER504"); 
	}else 
		finder = new FastSearchFabbricatoFinder();
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
			popUpAperte[index] = window.open("", "PopUpFastSearchFabbricatoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpFastSearchFabbricatoDetail" + index, "width=640,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}


function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (listaFabbricati.size() > 0) {
 FabbricatoTotaleBean fabb = (FabbricatoTotaleBean)listaFabbricati.get(0);
 
 %>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%=fabb.getIdDwh()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/FastSearchFabbricati" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Elenco Fabbricati totali</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">FABBRICATI da Fonte di Riferimento</span></td>
		<td class="extWinTDTitle" colspan="5"><span class="extWinTXTTitle">FABBRICATI da Fonti Dati</span></td>
	
	</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SEZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SEZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">FONTE</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">TIPO</span></td>
	</tr>
	
	<% FabbricatoTotaleBean fabb = new FabbricatoTotaleBean(); %>
  <% java.util.Enumeration en = listaFabbricati.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>

	
 
  <% 
     while (en.hasMoreElements()) {
        fabb = (FabbricatoTotaleBean)en.nextElement();
        
 		List<List<FabbricatoTotaleBean>> mappa = fabb.getFabbAssociati();
        
       	String stileCellaTD = contatore%2==0 ? "extWinTDDataAlter" : "extWinTDData";
        
        int somma=0;
        for(List<FabbricatoTotaleBean> fabb_i : mappa){
			somma += fabb_i.size();
        }
        
        int rowspan1 = somma;
        
        boolean primoTotale = true;
        
        
		%>
    <tr id="r<%=contatore%>" style="">
		
		<td onclick="vai('<%=fabb.getIdDwh()%>', '<%=progressivoRecord%>', true)" class=<%=stileCellaTD%> onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan1%>>
		<span class="extWinTXTData">
		
		<%=fabb.getSezione() != null ? fabb.getSezione() : "-"%>
		
		</span></td>
		
		<td onclick="vai('<%=fabb.getIdDwh()%>', '<%=progressivoRecord%>', true)" class=<%=stileCellaTD%> onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan1%>>
		<span class="extWinTXTData">
		
		<%=fabb.getFoglio() != null ? fabb.getFoglio() : "-"%>
		
		</span></td>
		
		<td onclick="vai('<%=fabb.getIdDwh()%>', '<%=progressivoRecord%>', true)" class=<%=stileCellaTD%> onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'rowspan=<%=rowspan1%>>
		<span class="extWinTXTData">
		
		<%=fabb.getParticella() != null ? fabb.getParticella() : "-"%>
		
		</span></td>	
		
		<% 	
			
			for(List<FabbricatoTotaleBean> fabb_i : mappa){
				int rowspan2 = fabb_i.size();
				boolean primo = true;
				for(FabbricatoTotaleBean totale : fabb_i){ 
			%>	
				<%if(!primoTotale){%>
					<tr>
				<% }  %>
				<%if(primo){  %>
					<td class=<%=stileCellaTD%> rowspan=<%=rowspan2%>  >
						<span class="extWinTXTData">
							<%=totale.getSezione() != null ? totale.getSezione() : "-"%>
						</span>
					</td>
					<td class=<%=stileCellaTD%> rowspan=<%=rowspan2%>  >
						<span class="extWinTXTData">
						<%=totale.getFoglio() != null ? totale.getFoglio() : "-"%>
						</span>
					</td>
					<td class=<%=stileCellaTD%>  rowspan=<%=rowspan2%>  >
						<span class="extWinTXTData">
							<%=totale.getParticella() != null ? totale.getParticella() : "-"%>
						</span>
					</td>
					
				<% } %>
				
				<td class=<%=stileCellaTD%> >
				<span class="extWinTXTData">
					<%=totale.getDescrFonte() != null ? totale.getDescrFonte() : ", "%>
				</span>
				</td>
				<td class=<%=stileCellaTD%>  >
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
<input type='hidden' name="UC" value="504">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>