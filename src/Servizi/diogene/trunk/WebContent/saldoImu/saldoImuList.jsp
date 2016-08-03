<%@ page language="java" import="it.escsolution.escwebgis.imu.bean.*, it.escsolution.escwebgis.imu.logic.*,java.text.*" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<% HttpSession sessione = request.getSession(true);  %> 
<% java.util.Vector vct_lista = (java.util.Vector)sessione.getAttribute(ConsulenzaImuLogic.LISTA); %>
<% SaldoImuFinder finder = (SaldoImuFinder)sessione.getAttribute(ConsulenzaImuLogic.FINDER); 
   SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy"); 
%>
<%int js_back = 1;
	if (sessione.getAttribute("BACK_JS_COUNT")!= null)
		js_back = ((Integer)sessione.getAttribute("BACK_JS_COUNT")).intValue();%>
<% java.lang.String funzionalita=(java.lang.String)sessione.getAttribute("FUNZIONALITA"); %>		
<html>
	<head>
		<title>Consulenze Saldo Imu - Lista</title>
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
					popUpAperte[index] = window.open("", "PopUpImuDetail" + index, "width=900,height=700,status=yes,resizable=yes");
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
				popUpAperte[index] = window.open("", "PopUpImuDetail" + index, "width=900,height=700,status=yes,resizable=yes");
				return popUpAperte[index].name;
			}
		}
	
	
		<% if (vct_lista.size() > 0){
			ConsulenzaBean cons = (ConsulenzaBean)vct_lista.get(0);%>
		function vaiPrimo(){
		 	document.mainform.OGGETTO_SEL.value='<%=cons.getChiave()%>';
			document.mainform.RECORD_SEL.value = 1;
			document.mainform.ST.value = 3;
			document.mainform.submit();
			}
		<%}%>
	
	</script>

	<body onload="mettiST()">

		<form name="mainform" action="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/ConsulenzaImu" target="_parent" >

		<div align="center" class="extWinTDTitle">
			<span class="extWinTXTTitle">
				&nbsp;<%=funzionalita%>:&nbsp;Elenco Consulenze Saldo Imu
			</span>
		</div>
		
		&nbsp;
		
		
   		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COD.FISCALE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">COGNOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">NOME</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA CONSULENZA</span></td>
			</tr>
	
			<%
				ConsulenzaBean v = new ConsulenzaBean();
				java.util.Enumeration en = vct_lista.elements(); 
				int contatore = 1;
				long progressivoRecord = (finder.getPaginaAttuale()-1) * finder.getRighePerPagina() + 1; %>
			<% while (en.hasMoreElements()) {
		        v = (ConsulenzaBean)en.nextElement();%>

	    		<tr id="r<%=contatore%>">
	    
	    			<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getCf()!=null ? v.getCf() : "-"%></span>
					</td>
	    		
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getCognome()!=null ? v.getCognome() : "-"%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getNome()!=null ? v.getNome() : "-"%></span>
					</td>
					
					<td class="extWinTDData" onclick="vai('<%=v.getChiave()%>', '<%=progressivoRecord%>', true)" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;' >
						<span class="extWinTXTData"><%=v.getDtConsulenza()!=null ? SDF.format(v.getDtConsulenza()) : "-" %></span>
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
				<input type='hidden' name="UC" value="124">
				<input type='hidden' name="BACK" value="">
				<input type="hidden" name="BACK_JS_COUNT" value="<%=js_back%>">

		</table>
		</form>

		<div id="wait" class="cursorWait" />
	</body>
</html>