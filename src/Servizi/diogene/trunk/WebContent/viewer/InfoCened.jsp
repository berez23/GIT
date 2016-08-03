<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%@page import="javax.naming.Context"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<html>
<head>
<title>Informazioni Certificazioni Energetiche</title>
<LINK rel="stylesheet"
	href="<%= request.getContextPath() %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>
<script>
function vai(codice){
	document.infoForm.KEYSTR.value=codice; // QUESTO PER SALTARE IN LISTA CON ST = 2
	document.infoForm.OGGETTO_SEL.value=codice; // QUESTO PER DETTAGLIO CON ST=3
	document.infoForm.ST.value = 3;
	document.infoForm.submit();
	}
</script>

</head>

<body>

 
<div align="center" class="extWinTDTitle"><span class="extWinTXTTitle">
	Lista dei Subalterni </span>
</div>

   <table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
	
	<tr>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CODICE IDENTIFICATIVO PRATICA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA PROT</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">NUM PROT</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SEZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">FOGLIO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">PARTICELLA</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">SUBALTERNO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">DATA FINE VAL</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">ANNO COSTRUZIONE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">INDIRIZZO</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">COMUNE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">MOTIVAZIONE ACE</span></td>
		<td class="extWinTDTitle"><span class="extWinTXTTitle">CLASSE ENERGETICA</span></td>
	</tr>
<%
    String pk = request.getParameter("pk");
	String oggettoSel = request.getParameter("OGGETTO_SEL");
	String foglio = request.getParameter("Foglio");
	String particella = request.getParameter("Particella");
	String layerName = request.getParameter("LayerName");

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
		Context cont= new InitialContext();
		//Context datasourceContext = (Context)cont.lookup("java:comp/env");
		DataSource theDataSource = null;
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		//theDataSource =(DataSource)cont.lookup("jdbc/Diogene_DS" + "_" + eu.getEnte());
		theDataSource = (DataSource)cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
		  
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
				String tabella = null;
				if ( layerName!=null && layerName.equals("CERTIFICAZIONI ENERGETICHE EDIFICI") ) 
					tabella = "DATI_TEC_FABBR_CERT_ENER";
				
				String query = "select ID, CODICE_IDENTIFICATIVO_PRATICA, DATA_PROT, NUM_PROT, SEZIONE, FOGLIO, PARTICELLA, SUB_TUTTI, DT_SCA_VALIDITA, ANNO_COSTRUZIONE, INDIRIZZO, COMUNE, MOTIVAZIONE_ACE, CLASSE_ENERGETICA from " + tabella + " "
					+" where TO_NUMBER_ALL(FOGLIO) = ? AND LPAD(PARTICELLA, 5, '0') = ? ";
				
				System.out.println("Eseguo "+ query);
				System.out.println("Parametri: foglio="+ foglio + "; particella="+ particella );
				pst = cnn.prepareStatement(query);
				pst.setString(1, foglio);
				pst.setString(2, particella);
				rs = pst.executeQuery();
				int contatore = 1; 
				while (rs.next()) {
					//trovato = true;
					%>
					<tr id="r<%=contatore%>">
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=rs.getString("CODICE_IDENTIFICATIVO_PRATICA")!=null?rs.getString("CODICE_IDENTIFICATIVO_PRATICA"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=rs.getDate("DATA_PROT")!=null?sdf.format(rs.getDate("DATA_PROT")):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=rs.getString("NUM_PROT")!=null?rs.getString("NUM_PROT"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
						<span class="extWinTXTData"><%=rs.getString("SEZIONE")!=null?rs.getString("SEZIONE"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("FOGLIO")!=null?rs.getString("FOGLIO"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("PARTICELLA")!=null?rs.getString("PARTICELLA"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("SUB_TUTTI")!=null?rs.getString("SUB_TUTTI"):""%></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("DT_SCA_VALIDITA")!=null?sdf.format(rs.getDate("DT_SCA_VALIDITA")):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("ANNO_COSTRUZIONE")!=null?rs.getString("ANNO_COSTRUZIONE"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("INDIRIZZO")!=null?rs.getString("INDIRIZZO"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("COMUNE")!=null?rs.getString("COMUNE"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("MOTIVAZIONE_ACE")!=null?rs.getString("MOTIVAZIONE_ACE"):"" %></span>
					</td>
					<td onclick="vai('<%=rs.getString("ID")%>','<%=contatore%>',true)" class="extWinTDData" onmouseover='chgtr(<%=contatore%>,1)' onmouseout='chgtr(<%=contatore%>,0)' style='cursor: pointer;'>
					    <span class="extWinTXTData"><%=rs.getString("CLASSE_ENERGETICA")!=null?rs.getString("CLASSE_ENERGETICA"):"" %></span>
					</td>
					</tr>
					<%			
					contatore++;
				}
					
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally
		{
			try { rs.close(); } catch (Exception ex) {}
			try { pst.close(); } catch (Exception ex) {}
			try { cnn.close(); } catch (Exception ex) {}
		}
		
	
%>
	</table>

			<form name="infoForm" method="post" action="<%=request.getContextPath()%>/CertificazioniEnergetiche" id="infoForm" target="_BLANK">
				<input type="hidden" name="DATASOURCE" value="<%=es.getDataSource()%>">
				<input type="hidden" name="ST" value="2">
				<input type="hidden" name="UC" value="135">
				<input type="hidden" name="KEYSTR" value="">
				<input type="hidden" name="OGGETTO_SEL" value="">
			</form>							
										



<div id="wait" class="cursorWait" />
		


</body>

</html>
