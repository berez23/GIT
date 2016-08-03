<%@ page language="java" import="it.escsolution.escwebgis.f24.bean.*, it.escsolution.escwebgis.f24.logic.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vct_lista = (java.util.Vector)sessione.getAttribute(F24AnnullamentoLogic.LISTA); %>
<% F24Finder finder = (F24Finder)sessione.getAttribute(F24AnnullamentoLogic.FINDER); %>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<html>
	<head>
		<title>Annullamento F24 - Lista</title>
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
	
	
		<% if (vct_lista.size() > 0){
		F24Annullamento annullamento = (F24Annullamento)vct_lista.get(0);%>
		function vaiPrimo(){
		 	document.mainform.OGGETTO_SEL.value='<%=annullamento.getChiave()%>';
			document.mainform.RECORD_SEL.value = 1;
			document.mainform.ST.value = 3;
			document.mainform.submit();
			}
		<%}%>
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/F24Annullamento" target="_parent" >

		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Elenco Annullamenti F24
			</span>
		</div>
		
		&nbsp;
		
		
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO OPERAZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA OPERAZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO RIF.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA VERSAMENTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CONTRIBUENTE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMPOSTA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TIPO TRIBUTO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. CREDITO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">IMP. DEBITO</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">FORNITURA</span></td>
				<td class="extWinTDTitle" colspan="2"><span class="extWinTXTTitle">RIPARTIZIONE ORIG.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA BONIFICO ORIG.</span></td>
			</tr>
	
			<%
				F24Annullamento v = new F24Annullamento();
				java.util.Enumeration en = vct_lista.elements(); 
				int contatore = 1;
				long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
			<% while (en.hasMoreElements()) {
		        v = (F24Annullamento)en.nextElement();%>

	    		<tr id="r<%=contatore%>">
	    		    <td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%="A".equalsIgnoreCase(v.getTipoOperazione())? "Annullamento" : ("R".equalsIgnoreCase(v.getTipoOperazione()) ? "Ripristino" : v.getTipoOperazione())%></span>
					</td>
	    		
	    			<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getDtOperazione()%></span>
					</td>
	    		
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getAnnoRif()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getDtRiscossione()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getCodFisc()%></span>
					</td>
					
					<td class="extWinTDData" style="text-align:center;" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getTipoImposta()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getCodTributo()+" - "+v.getDescTributo()%></span>
					</td>
					
					<td style="text-align: right;" class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData">&euro; <%=v.getImpCredito()%></span>
					</td>
					
					<td style="text-align: right;" class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData">&euro; <%=v.getImpDebito()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getDtFornitura()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getProgFornitura()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getDtRipartizioneOrig()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getProgRipartizioneOrig()%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getDtBonificoOrig()%></span>
					</td>
					
				</tr>
	
				<% 
				contatore++; progressivoRecord ++;
			  	} 
			  	%>

				<input type='hidden' name="ST" value="">
				<input type='hidden' name="OGGETTO_SEL" value="">
				<input type='hidden' name="RECORD_SEL" value="">
				<input type='hidden' name="ACT_PAGE" value="<%=finder.getPaginaAttuale()%>">
				<input type='hidden' name="AZIONE" value="">
				<input type='hidden' name="UC" value="123">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>