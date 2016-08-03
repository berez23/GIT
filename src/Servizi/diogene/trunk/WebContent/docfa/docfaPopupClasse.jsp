<%@ page language="java"
	import="java.util.ArrayList,java.util.Iterator,it.escsolution.escwebgis.docfa.bean.*,it.escsolution.escwebgis.docfa.servlet.*,it.escsolution.escwebgis.docfa.logic.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<html>
<head>
<title>Docfa - Dettaglio</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
</head>

<body>
		<form>
		<div class="TXTmainLabel">
		<table align=center class="extWinTable" style="width: 60%;">
		  <tr>
		    <td class="TXTmainLabel">Foglio</td>
		    <td class="TXTviewTextBox"><%=request.getParameter("foglio")%></td>
		    <td class="TXTmainLabel">Particella</td>
		    <td class="TXTviewTextBox"><%=request.getParameter("particella")%></td>
		    <td class="TXTmainLabel">Sub.</td>
		    <td class="TXTviewTextBox"><%=request.getParameter("sub")%></td>
		  </tr>
		</table>
		  <table align=center class="extWinTable" style="width: 60%;">
		  <tr>
		    <td class="TXTmainLabel">Rapporto Val.Comm./Rend. DOCFA aggiornata. </td>
		    <td class="TXTviewTextBox"><%=request.getParameter("rapportovalore")%></td>
		  </tr>
		  <tr>
		    <td class="TXTmainLabel">Rendita media attesa:</td>
		    <td class="TXTviewTextBox"><%=request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_MEDIA_ATTESA)%></td>
		  </tr>
		  <tr>
		    <td colspan="2" class="TXTmainLabel">
		    		Valore di rapporto: <input type="text" name="rapporto" id="rapporto" value="<%=request.getParameter("rapporto")%>" size="2">
		    		<input class="TDviewTextBox" type="submit" name="ricalcola" value="Ricalcola" onclick="if(document.getElementById('rapporto').value != '3')if(!confirm('Ricalcolare i valori con il rapporto tra valore commerciale e rendita catastale modificato?')) return false;">
		     </td>
		    </tr>
		</table>
				
				
				<input type="hidden" name="valore" value="<%=request.getParameter("valore")%>">
				<input type="hidden" name="vani" value="<%=request.getParameter("vani")%>">
				<input type="hidden" name="zona" value="<%=request.getParameter("zona")%>">
				<input type="hidden" name="rapporto" value="<%=request.getParameter("rapporto")%>">
				<input type="hidden" name="foglio" value="<%=request.getParameter("foglio")%>">
				<input type="hidden" name="particella" value="<%=request.getParameter("particella")%>">
				<input type="hidden" name="sub" value="<%=request.getParameter("sub")%>">
				<input type="hidden" name="rapportovalore" value="<%=request.getParameter("rapportovalore")%>">	
				<input type="hidden" name="consistenza" value="<%=request.getParameter("consistenza")%>">	
				<input type="hidden" name="categoria" value="<%=request.getParameter("categoria")%>">	
				<input type="hidden" name="classe" value="<%=request.getParameter("classe")%>">	
				<input type="hidden" name="popupClasseAtt" value="true">	
							
		</div>
		</form>
		<div class="TXTmainLabel">Dati Categoria/Classe compatibile</div>
		<table align=center class="extWinTable" style="width: 100%" >
			<tr>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">ZONA </span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CATEGORIA</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE</span></td>
				<td class="extWinTDTitle"><span class="extWinTXTTitle">TARIFFA</span></td>	
			</tr>
			<%
			java.util.ArrayList listaDocfaDatiClasseAttesa = (java.util.ArrayList) request.getAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_CLASSE_ATTESTA);
			java.util.Iterator it  = listaDocfaDatiClasseAttesa.iterator();
			while (it.hasNext())
			{
				Docfa dc = (Docfa) it.next();
			%>
				<tr>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getZona()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getCategoria()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getClasse()%></span></td>
					<td class="extWinTDData"><span class="extWinTXTData"><%=dc.getRendita()%></span></td>
				</tr>
			<%
			}
			if(request.getAttribute("tabellaNonTrovata") != null)
				out.println("<script>alert('"+request.getAttribute("tabellaNonTrovata")+"');</script>");
			%>
		</table>		
<br>
<br>
<br>


</body>
</html>
