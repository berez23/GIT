<%@ page language="java" %>
<%@page import="it.escsolution.escwebgis.demanio.logic.*"%>
<%@page import="it.escsolution.escwebgis.demanio.bean.*"%>
<%@page import="it.webred.fb.ejb.dto.*, java.util.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(DemanioLogic.LISTA_BENI); %>
<% it.escsolution.escwebgis.demanio.bean.DemanioFinder finder = (it.escsolution.escwebgis.demanio.bean.DemanioFinder)sessione.getAttribute(DemanioLogic.FINDER); %>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	

<html>
	<head>
		<title>Demanio Comunale - Lista</title>
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
					popUpAperte[index] = window.open("", "DemanioDetail" + index, "width=640,height=480,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "DemanioDetail" + index, "width=640,height=480,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
		<%if (vlista.size() > 0){
			BeneInListaDTO con = (BeneInListaDTO)vlista.get(0);%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=con.getBene().getId()%>';
				document.mainform.RECORD_SEL.value=1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>	
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Demanio" target="_parent" >

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Lista Beni</span>
		</div>
		
		&nbsp;
               
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cod. Inv.</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat. Inventario</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Bene</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Descrizione</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Note</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Indirizzi</span></td>
			</tr>
	
			<% BeneInListaDTO t = new BeneInListaDTO(); %>
			<% java.util.Enumeration en = vlista.elements(); %>
			<% int contatore = 1; %>
			<% long progressivoRecord = (finder.getPaginaAttuale()-1)*finder.getRighePerPagina()+1; %>
			<% while (en.hasMoreElements()) {
		        t = (BeneInListaDTO)en.nextElement();  
		     %>

	    		<tr id="r<%=contatore%>">
					<td onclick="vai('<%=t.getBene().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getBene().getDmBBeneInv().getCodInventario()!=null ? t.getBene().getDmBBeneInv().getCodInventario() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getBene().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getBene().getDmBBeneInv().getDesTipo()!=null ? t.getBene().getDmBBeneInv().getDesTipo() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getBene().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getBene().getDmBBeneInv().getDesCatInventariale()!=null ? t.getBene().getDmBBeneInv().getDesCatInventariale() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getBene().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getBene().getDesTipoBene()!=null ? t.getBene().getDesTipoBene() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getBene().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getBene().getDescrizione()!=null ? t.getBene().getDescrizione().trim() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getBene().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getBene().getNote()!=null ? t.getBene().getNote().trim() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getBene().getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;width:300px;'>
						<%List<IndirizzoDTO> indirizzi = t.getIndirizzi(); 
						  for(IndirizzoDTO i : indirizzi){%>
							<span class="extWinTXTData"><%=(i.getTipoVia()!=null ? i.getTipoVia()+" " : "") 
							+(i.getDescrVia()!=null ? i.getDescrVia() : "")
							+(i.getCivico()!=null ? ", "+i.getCivico() : "")
							+(i.getDesComune()!=null ? " "+i.getDesComune() : "")
							+(i.getProv()!=null ? " ("+i.getProv()+")" : "") %></span>
							<br/>
						<%} %>
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
				<input type='hidden' name="UC" value="132">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>