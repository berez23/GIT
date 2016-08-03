<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.indice.fastsearch.oggetto.bean.FastSearchOggettoFinder"%>
<%@page import="it.webred.indice.fastsearch.oggetto.bean.OggettoTotaleBean" %>
<%@page import="java.util.List" %>
<%@page import="java.util.HashMap" %>

<%   HttpSession sessione = request.getSession(true);  %> 
<%  

	java.util.Vector listaOgg =(java.util.Vector)sessione.getAttribute("indice.lista_oggetti"); 
	
	FastSearchOggettoFinder finder = null;
	if (sessione.getAttribute("FINDER503")!= null){
		finder = (FastSearchOggettoFinder)sessione.getAttribute("FINDER503"); 
	}else 
		finder = new FastSearchOggettoFinder();
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
			popUpAperte[index] = window.open("", "PopUpAnaDwhDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
		popUpAperte[index] = window.open("", "PopUpAnaDwhDetail" + index, "width=640,height=480,status=yes,resizable=yes");
		return popUpAperte[index].name;
	}
}


function mettiST(){
	document.mainform.ST.value = 2;
}


<% if (listaOgg.size() > 0) {
 OggettoTotaleBean ogg = (OggettoTotaleBean)listaOgg.get(0);
 
 %>
function vaiPrimo(){
 	document.mainform.OGGETTO_SEL.value='<%= ogg.getIdDwh()%>';
	document.mainform.RECORD_SEL.value=1;
	document.mainform.ST.value = 3;
	document.mainform.submit();
	}
<%}%>

</script>
<body onload="mettiST()">

<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/FastSearchOggetti" target="_parent" >
 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	<%=funzionalita%>:&nbsp;Elenco Oggetti totali</span>
</div>
		
&nbsp;
               
   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		<tr>
		
		<td class="extWinTDTitle" colspan="3"><span class="extWinTXTTitle">FABBRICATI da Fonte di Riferimento</span></td>
		<td class="extWinTDTitle" colspan="5"><span class="extWinTXTTitle">FABBRICATI da Fonti Dati</span></td>
	
	</tr>
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
		
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">FONTE</span></td>
		<td class="extWinTDTitle" ><span class="extWinTXTTitle">TIPO</span></td>
	</tr>
	
	<% OggettoTotaleBean ogg = new OggettoTotaleBean(); %>
  <% java.util.Enumeration en = listaOgg.elements(); int contatore = 1; %>
  <% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>

	
 
  <% 
     while (en.hasMoreElements()) {
        ogg = (OggettoTotaleBean)en.nextElement();
        
 		List<List<OggettoTotaleBean>> mappa = ogg.getOggettiAssociati();
        
       	String stileCellaTD = contatore%2==0 ? "extWinTDDataAlter" : "extWinTDData";
        
        int somma=0;
        for(List<OggettoTotaleBean> ogg_i : mappa){
			somma += ogg_i.size();
        }
        
        int rowspan1 = somma;
        
        boolean primoTotale = true;
        
        
		%>
    <tr id="r<%=contatore%>" style="">
		
		<td onclick="vai('<%=ogg.getIdDwh()%>', '<%=progressivoRecord%>', true)" class=<%=stileCellaTD%> onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan1%>>
		<span class="extWinTXTData">
		
		<%=ogg.getFoglio() != null ? ogg.getFoglio() : "-"%>
		
		</span></td>
		
		<td onclick="vai('<%=ogg.getIdDwh()%>', '<%=progressivoRecord%>', true)" class=<%=stileCellaTD%> onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan1%>>
		<span class="extWinTXTData">
		
		<%=ogg.getParticella() != null ? ogg.getParticella() : "-"%>
		
		</span></td>	
		<td onclick="vai('<%=ogg.getIdDwh()%>', '<%=progressivoRecord%>', true)" class=<%=stileCellaTD%> onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' rowspan=<%=rowspan1%>>
		<span class="extWinTXTData">
		
		<%=ogg.getSub() != null ? ogg.getSub() : ""%>
		
		</span></td>
		
		<% 	
			
			for(List<OggettoTotaleBean> ogg_i : mappa){
				int rowspan2 = ogg_i.size();
				boolean primo = true;
				for(OggettoTotaleBean totale : ogg_i){ 
			%>	
				<%if(!primoTotale){%>
					<tr>
				<% }  %>
				<%if(primo){  %>
					
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
					<td class=<%=stileCellaTD%> rowspan=<%=rowspan2%>  >
						<span class="extWinTXTData">
							<%=totale.getSub() != null ? totale.getSub() : "-"%>
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
<input type='hidden' name="UC" value="503">
<input type='hidden' name="EXT" value="<%=pulsanti.getExt()%>">
<input type='hidden' name="EXT" value="<%=pulsanti.isPrimo()%>">
<input type='hidden' name="BACK" value="">
<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">
</table>
</form>
<div id="wait" class="cursorWait" />
</body>
</html>