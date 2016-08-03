
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(BolliVeicoliLogic.LISTA_BOLLI_VEICOLI); %>
<% BolliVeicoliFinder finder = (BolliVeicoliFinder)sessione.getAttribute(BolliVeicoliLogic.FINDER); %>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	
<%@page import="it.escsolution.escwebgis.bolli.logic.BolliVeicoliLogic"%>
<%@page import="it.escsolution.escwebgis.bolli.bean.BolliVeicoliFinder"%>
<%@page import="it.webred.ct.data.model.bolliVeicoli.BolloVeicolo"%>
<html>
	<head>
		<title>Bolli Veicoli - Lista</title>
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
					popUpAperte[index] = window.open("", "BolliDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "BolliDetail" + index, "width=640,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
		<%if (vlista.size() > 0){
			BolloVeicolo con = (BolloVeicolo)vlista.get(0);%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=con.getChiave()%>';
				document.mainform.RECORD_SEL.value=1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>	
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/BolliVeicoli" target="_parent" >

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Lista Bolli Veicoli</span>
		</div>
		
		&nbsp;
               
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			<tr>
				<td class="extWinTDTitle" colspan="15"><span class="extWinTXTTitle">DATI BOLLI VEICOLI</span></td>
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TARGA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">USO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DESTINAZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">PORTATA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CC</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ALIMENTAZIONE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">MASSA RIMORCHIABILE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">N. POSTI</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">KW</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA PRIMA IMM.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">N. ASSI</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">SIGLA EURO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TELAIO</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">RAGIONE SOCIALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME/NOME</span></td>
			</tr>
	
			<% BolloVeicolo bollo = new BolloVeicolo(); %>
			<% java.util.Enumeration en = vlista.elements(); %>
			<% 
			int contatore = 1; 
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			%>
			<% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
			<% while (en.hasMoreElements()) {
		        bollo = (BolloVeicolo)en.nextElement();
		       System.out.println("Chiave, Progressivo: " + bollo.getChiave() + ", " + progressivoRecord);
		       
		        %>

	    		<tr id="r<%=contatore%>">
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getTarga()!=null ? bollo.getTarga() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getUso()!=null ? bollo.getUso() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getDestinazione()!=null ? bollo.getDestinazione() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getPortata()!=null ? bollo.getPortata() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getCilindrata()!=null ? bollo.getCilindrata() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getAlimentazione()!=null ? bollo.getAlimentazione() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getMassaRimorchiabile()!=null ? bollo.getMassaRimorchiabile() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getNumeroPosti()!=null ? bollo.getNumeroPosti() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getKw()!=null ? bollo.getKw() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%= bollo.getDtPrimaImmatricolazione()!=null? sdf.format(bollo.getDtPrimaImmatricolazione()):"-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getNumeroAssi()!=null ? bollo.getNumeroAssi() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getCodSiglaEuro()!=null ? bollo.getCodSiglaEuro() : "-" %></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getCodiceTelaio()!=null ? bollo.getCodiceTelaio() : "-"%></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getRagioneSociale()!=null ? bollo.getRagioneSociale() : "-"%></span>
					</td>
					<td onclick="vai('<%=bollo.getChiave()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=bollo.getCognome()!=null ? bollo.getCognome() : "-" %><%= bollo.getNome()!= null ? bollo.getNome() : "-" %></span>
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
				<input type='hidden' name="UC" value="137">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>