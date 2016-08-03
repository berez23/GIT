<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vlista=(java.util.Vector)sessione.getAttribute(RetteLogic.LISTA_RETTE); %>
<%	java.lang.String ST = (java.lang.String)sessione.getAttribute("ST");%>
<% int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
	<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>	
<%@page import="it.escsolution.escwebgis.rette.logic.RetteLogic"%>
<%@page import="it.escsolution.escwebgis.rette.bean.RttBollette"%>
<html>
	<head>
		<title>Rette Scolastiche - Lista</title>
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
					popUpAperte[index] = window.open("", "RetteDetail" + index, "width=900,height=550,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "RetteDetail" + index, "width=900,height=550,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
		<%if (vlista.size() > 0){
			RttBollette con = (RttBollette)vlista.get(0);%>
			function vaiPrimo(){
			 	document.mainform.OGGETTO_SEL.value='<%=con.getChiave()%>';
				document.mainform.RECORD_SEL.value=1;
				document.mainform.ST.value = 3;
				document.mainform.submit();
			}
		<%}%>	
	
	</script>

	<body>

		<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
			&nbsp;<%=funzionalita%>:&nbsp;Lista Rette Scolastiche</span>
		</div>

		<%if(vlista.isEmpty()){%>
			<br/><br/>
			<div align="center">
				Non è stata trovata nessuna retta
			</div>
		<%}else{%>
		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/Rette" target="_parent" >
		
		&nbsp;
               
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Codice</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Data</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Oggetto</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Intestatario</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Importo</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">Importo pagato</span></td>
			</tr>
	
			<% RttBollette t = new RttBollette(); %>
			<% java.util.Enumeration en = vlista.elements(); %>
			<% int contatore = 1; %>
			<% int progressivoRecord = contatore; %>
			<% while (en.hasMoreElements()) {
		        t = (RttBollette)en.nextElement();
		       System.out.println("Chiave, Progressivo: " + t.getChiave() + ", " + progressivoRecord);   
		        %>

	    		<tr id="r<%=contatore%>">
					<td onclick="vai('<%=t.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getCodBolletta()!=null ? t.getCodBolletta() : "-" %></span>
					</td>
					<td onclick="vai('<%=t.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=t.getDataBolletta()!=null ? t.getDataBolletta() : "-"%></span>
					</td>
					<td onclick="vai('<%=t.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=t.getOggetto()!=null ? t.getOggetto() : "-"%></span>
					</td>
					<td onclick="vai('<%=t.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getDesIntestatario()!=null ? t.getDesIntestatario() : "-"%></span>
					</td>
					<td onclick="vai('<%=t.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getTotBolletta()!=null ? t.getTotBolletta() : "-"%></span>
					</td>
					<td onclick="vai('<%=t.getId()%>', '<%=progressivoRecord%>', true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=t.getTotPagato()!=null ? t.getTotPagato() : "-"%></span>
					</td>
				</tr>
	
				<% 
				contatore++;
			  	} 
			  	%>

				<input type='hidden' name="ST" value="">
				<input type='hidden' name="OGGETTO_SEL" value="">
				<input type='hidden' name="RECORD_SEL" value="">
				<input type='hidden' name="AZIONE" value="">
				<input type='hidden' name="UC" value="119">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>
		<%}%>
	</body>
</html>