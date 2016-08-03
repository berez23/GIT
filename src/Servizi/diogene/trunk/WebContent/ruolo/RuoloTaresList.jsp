<%@ page language="java" import="java.math.BigDecimal, java.util.*,java.text.*,it.escsolution.escwebgis.ruolo.bean.*, it.escsolution.escwebgis.ruolo.logic.*, it.webred.ct.data.access.basic.ruolo.tares.dto.RuoloTaresDTO" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vct_lista_ruoli = (java.util.Vector)sessione.getAttribute(RuoloTaresLogic.LISTA_RUOLI); %>
<% RuoloFinder finder = (RuoloFinder)sessione.getAttribute(RuoloTaresLogic.FINDER); %>
<% DecimalFormat DF = new DecimalFormat("#0.00"); int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<% String zero = DF.format(BigDecimal.ZERO.doubleValue()); %>
<html>
	<head>
		<title>Ruoli Tares - Lista</title>
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
						document.mainform.ST.value = 40;
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
					popUpAperte[index] = window.open("", "PopUpRuoloTaresList" + index, "width=1000,height=600,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpRuoloTaresList" + index, "width=1000,height=600,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
		
				
		function mettiST(){
			document.mainform.ST.value = 2;
		}
	
	
		<% if (vct_lista_ruoli.size() > 0){
		RuoloTaresDTO ruolo = (RuoloTaresDTO)vct_lista_ruoli.get(0);%>
		function vaiPrimo(){
		 	document.mainform.OGGETTO_SEL.value='<%=ruolo.getRuolo().getId()%>';
			document.mainform.RECORD_SEL.value = 1;
			document.mainform.ST.value = 3;
			document.mainform.submit();
			}
		<%}%>
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/RuoloTares" target="_parent" >

		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Elenco Ruoli Tares
			</span>
		</div>
		
		&nbsp;
		
		
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
		
			<tr>
				
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Anno</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>

				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod.Fisc.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Nominativo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Comune</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Prov.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Stato Estero</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tot.Netto</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Add.IVA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Trib.Prov</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Totale</span></td>
			</tr>
	
			<%
				RuoloTaresDTO v = new RuoloTaresDTO();
				java.util.Enumeration en = vct_lista_ruoli.elements(); 
				int contatore = 1;
				long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
			<%  boolean cls = false;
				String oldxx = null;
				String tipo = "extWinTDData";
				while (en.hasMoreElements()) {
		        v = (RuoloTaresDTO)en.nextElement();
		        
		        tipo = "extWinTDData";
		        
		        %>

	    		<tr id="r<%=contatore%>" style="cursor: pointer;" onclick="vai('<%=v.getRuolo().getId()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)'>
	    			<td class=<%=tipo%> style="text-align:center; " >
						<span class="extWinTXTData"><%=v.getRuolo().getAnno()%></span>
					</td>
					<td class=<%=tipo%> >
						<span class="extWinTXTData"><%="S".equals(v.getRuolo().getTipo()) ? "Conguaglio" : "Principale"%></span>
					</td>
					<td class=<%=tipo%> style="text-align:center;" >
						<span class="extWinTXTData"><%=v.getRuolo().getCodfisc()%></span>
					</td>
					<td class=<%=tipo%> style="text-align:left;" >
						<span class="extWinTXTData"><%=v.getRuolo().getNominativoContrib()%></span>
					</td>
					<td class=<%=tipo%> style="text-align:left;" >
						<span class="extWinTXTData"><%=v.getRuolo().getIndirizzo()!=null ? v.getRuolo().getIndirizzo() : ""%></span>
					</td>
					<td class=<%=tipo%> >
						<span class="extWinTXTData"><%=v.getRuolo().getComune()!=null ? v.getRuolo().getComune() : ""%></span>
					</td>
					<td class=<%=tipo%> >
						<span class="extWinTXTData"><%=v.getRuolo().getProv()!=null ? v.getRuolo().getProv() : ""%></span>
					</td>
					<td class=<%=tipo%> style="text-align:left;" >
						<span class="extWinTXTData"><%=v.getRuolo().getEstero()!=null ? v.getRuolo().getEstero() : ""%></span>
					</td>
					<td class=<%=tipo%> style="text-align:right;" >
						<span class="extWinTXTData">&euro; <%=v.getRuolo().getTotNetto()!=null ? DF.format(v.getRuolo().getTotNetto().doubleValue()) : zero %></span>
					</td>
					<td class=<%=tipo%> style="text-align:right;" >
						<span class="extWinTXTData">&euro; <%=v.getRuolo().getAddIva()!=null ? DF.format(v.getRuolo().getAddIva().doubleValue())  : zero %></span>
					</td>
					<td class=<%=tipo%> style="text-align:right;" >
						<span class="extWinTXTData">&euro; <%=v.getRuolo().getTribProv()!=null ? DF.format(v.getRuolo().getTribProv().doubleValue())  : zero %></span>
					</td>
					<td class=<%=tipo%> style="text-align:right;" >
						<span class="extWinTXTData">&euro; <%=v.getRuolo().getTotale()!=null ? DF.format(v.getRuolo().getTotale().doubleValue())  : zero %></span>
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
				<input type='hidden' name="UC" value="129">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>