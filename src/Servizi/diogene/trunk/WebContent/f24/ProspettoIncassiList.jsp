<%@ page language="java" import="it.escsolution.escwebgis.f24.bean.*, java.util.*, it.escsolution.escwebgis.f24.logic.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% String condizione = (String)sessione.getAttribute("CONDIZIONE"); %>
<% String totIncassi = (String)sessione.getAttribute(ProspettoIncassiLogic.TOT_INCASSI); %>
<% Vector vct_lista = (Vector)sessione.getAttribute(ProspettoIncassiLogic.LISTA_INCASSI); %>
<% F24Finder finder = (F24Finder)sessione.getAttribute(ProspettoIncassiLogic.FINDER); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<html>
	<head>
		<title>Prospetto Incassi per Codice Tributo</title>
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
	
	
		function vai(chiave, record_cliccato){
			wait();
			document.mainform.OGGETTO_SEL.value=chiave;
			document.mainform.RECORD_SEL.value=record_cliccato;
			document.mainform.ST.value = 3;
			document.mainform.submit();
		}
	
		function mettiST(){
			document.mainform.ST.value = 2;
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
						document.mainform.ST.value = 33;
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
					popUpAperte[index] = window.open("", "PopUpF24AnnullamentoDetail" + index, "width=900,height=480,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpF24AnnullamentoDetail" + index, "width=900,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ProspettoIncassi" target="_parent" >

		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Incassi per anno di riferimento e codice tributo&nbsp;<%=condizione%>
			</span>
		</div>
		
		&nbsp;
		
		
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO IMPOSTA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO RIF.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">VERSATO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM.FABBRICATI</span></td>
			</tr>
	
			<%
				ProspettoIncassi v = new ProspettoIncassi();
				java.util.Enumeration en = vct_lista.elements(); 
				int contatore = 1;
				String prevAnno = "";
				boolean blu = true;
				long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
			<% while (en.hasMoreElements()) {
		        v = (ProspettoIncassi)en.nextElement();
		        if(contatore>1 && !prevAnno.equals(v.getAnnoRif())){
		        	blu = !blu;
		        }
		        	prevAnno = v.getAnnoRif();
		        
		        %>

	    		<tr id="r<%=contatore%>"  style='cursor: pointer;' >
	    		    <td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align:center;">
						<span class="extWinTXTData"><%=v.getDescTipoImposta()%></span>
					</td>
	    		
	    			<td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align:center;">
						<span class="extWinTXTData"><%=v.getAnnoRif()%></span>
					</td>
					
					<td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %> >
						<span class="extWinTXTData"><%=v.getCodTributo() + (v.getDescTributo()!=null ? " - "+v.getDescTributo() : "")%></span>
					</td>
					
				    <td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align:right;">
						<span class="extWinTXTData">&euro; <%=v.getVersato()%></span>
					</td>
					
					<td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align:right;">
						<span class="extWinTXTData"><%=v.getFabbricati()%></span>
					</td>
					
				</tr>
	
				<% 
				contatore++; progressivoRecord ++;
			  	} 
				blu=!blu;
			  	%>
			  	
			  	<tr>
			  		<td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %>  colspan="3" style="text-align:left;">
			  		<span class="extWinTXTData" style="font-weight:bold;font-size:15px;">TOTALE</span>
			  		</td>
			  		<td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align:right;">
			  		<span class="extWinTXTData" style="font-weight:bold;font-size:15px;">&euro; <%=totIncassi%></span>
			  		</td>
			  		<td class=<%= blu ? "extWinTDData" : "extWinTDDataAlter" %> style="text-align:right;">
			  		
			  		</td>
			  	</tr>
			  	
				</table>
				
				
			<input type='hidden' name="ST" value="">
			<input type='hidden' name="OGGETTO_SEL" value="">
			<input type='hidden' name="RECORD_SEL" value="">
			<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
			<input type='hidden' name="AZIONE" value="">
			<input type='hidden' name="UC" value="125">
			<input type='hidden' name="BACK" value="">
			<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>