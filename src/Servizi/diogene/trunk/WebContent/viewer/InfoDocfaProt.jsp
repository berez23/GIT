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
<title>Informazioni Docfa Particella</title>
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
	Lista Docfa su Particella </span>
</div>

   
<%
    String pk = request.getParameter("pk");
	

		Context cont= new InitialContext();
		DataSource theDataSource = null;
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource = (DataSource)cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
		  
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
				String tabella = null;
				String q = ""; 
				if (request.getParameter("LAYER").equals("STA02: UNITA ABITATIVE CON DOCFA SOTTOCLASSATE")){ 
					tabella = "CAT_UNI_ABI_SOTTOCLASSATE";
					q = "select forn, prot, round(sum(rendita_dic_van)/count(*),3) as vm_rendita_dic_van, "
							+" round(sum(rendita_att_van)/count(*),3) as vm_rendita_att_van "
							+" from ( "
							+" select prot, to_char(forn,'yyyymmdd') forn "
							+" , to_number_all(rendita_docfa)/to_number_all(consistenza) as rendita_dic_van"
							+" , to_number_all(val_comm)/to_number_all(consistenza)/3/100 as rendita_att_van"
							+" from docfa_report dr, " + tabella + " l"
							+" where dr.foglio=l.foglio"
							+" and dr.part=l.particella"
							+" and l.SE_ROW_ID = ?"
							+" order by prot"
							+" )"
							+" group by prot, forn"; 
					
					%>
					<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Valore Medio Rendita Dichiarata / Vano </span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Valore Medio Rendita Attesa / Vano </span></td>
					</tr>
					<%
					String query = q;
					System.out.println("Eseguo "+ query);
					System.out.println("Parametri: "+ pk );
					pst = cnn.prepareStatement(query);
					pst.setString(1, pk);
					rs = pst.executeQuery();
						while (rs.next()) {
							//trovato = true;
							%>
							<tr onclick="vai('<%=rs.getString("prot") + "|" +  rs.getString("forn")%>')">
							<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= rs.getString("prot") %></span></td>
							<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= rs.getString("vm_rendita_dic_van") %></span></td>
							<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= rs.getString("vm_rendita_att_van") %></span></td>
							</tr>
							<%			
						}
					%>
					</table>
					<%
					
				}
				if (request.getParameter("LAYER").equals("STA01: UNITA ABITATIVE CON DOCFA")){ 
					tabella = "CAT_UNITA_ABITATIVE_DOCFA";
					q = "select forn, prot, round(sum(rendita_dic_van)/count(*),3) as vm_rendita_dic_van, "
							+" round(sum(rendita_att_van)/count(*),3) as vm_rendita_att_van "
							+" from ( "
							+" select prot, to_char(forn,'yyyymmdd') forn "
							+" , to_number_all(rendita_docfa)/to_number_all(consistenza) as rendita_dic_van"
							+" , to_number_all(val_comm)/to_number_all(consistenza)/3/100 as rendita_att_van"
							+" from docfa_report dr, " + tabella + " l"
							+" where dr.foglio=l.foglio"
							+" and dr.part=l.particella"
							+" and l.SE_ROW_ID = ?"
							+" order by prot"
							+" )"
							+" group by prot, forn"; 
					
					%>
					<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Valore Medio Rendita Dichiarata / Vano </span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Valore Medio Rendita Attesa / Vano </span></td>
					</tr>
					<%
					String query = q;
					System.out.println("Eseguo "+ query);
					System.out.println("Parametri: "+ pk );
					pst = cnn.prepareStatement(query);
					pst.setString(1, pk);
					rs = pst.executeQuery();
						while (rs.next()) {
							//trovato = true;
							%>
							<tr onclick="vai('<%=rs.getString("prot") + "|" +  rs.getString("forn")%>')">
							<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= rs.getString("prot") %></span></td>
							<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= rs.getString("vm_rendita_dic_van") %></span></td>
							<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= rs.getString("vm_rendita_att_van") %></span></td>
							</tr>
							<%			
						}
					%>
					</table>
					<%
					
				}
				if (request.getParameter("LAYER").equals("STA05: DOCFA SU PARTICELLA")){ 
					tabella = "CAT_DOCFA_SU_PARTICELLA";
					/* questa è la query che genera il catalogo e da cui deriva l'interrogazione mappa				
					sql = "create table "  + nomeSchemaDiogene + "." + NOME_TABELLA + " as "
							+ "select se_row_id, c.*, sp.shape, '-         ' as codifica from "
							+ "(select foglio, numero, count(*) occorrenze from "
							+ "(select distinct foglio, numero, protocollo_reg "
							+ "from "  + nomeSchemaDiogene + ".docfa_uiu "
							+ "where tipo_operazione <> 'S' "
							+ "order by foglio, numero) "
							+ "group by foglio, numero) c "
							+ "inner join "  + nomeSchemaDiogene + ".sitipart sp "
							+ "on c.FOGLIO = lpad(to_char(sp.foglio), 4, '0') "
							+ "and c.NUMERO = sp.particella "
							+ "and sp.data_fine_val = to_date('31/12/9999', 'dd/MM/yyyy' ) ";
					*/			
					
					q = " select distinct protocollo_reg as prot, TO_CHAR(fornitura, 'MM/YYYY') as meseFornitura, TO_CHAR(fornitura,   'YYYYMMDD') as forn, prop_categoria"
							+" from docfa_uiu du "
							+" right join CAT_DOCFA_SU_PARTICELLA dp "
							+" on du.foglio = dp.foglio and DU.NUMERO = DP.NUMERO "
							+" where dp.se_row_id = ? "
							+" order by forn, prot, prop_categoria";

					%>
					<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
					<tr>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Fornitura </span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Protocollo</span></td>
						<td class="extWinTDTitle"><span class="extWinTXTTitle">Cat. Costituita</span></td>
					</tr>
					<%
					String query = q;
					System.out.println("Eseguo "+ query);
					System.out.println("Parametri: "+ pk );
					pst = cnn.prepareStatement(query);
					pst.setString(1, pk);
					rs = pst.executeQuery();
					String chiave = "";
					String mese = "";
					String fornitura = "";
					String protocollo = "";
					String categoria = "";
						while (rs.next()) {
							//trovato = true;
							
							String meseCorrente = rs.getString("meseFornitura");
							String fornituraCorrente = rs.getString("forn");
							String protocolloCorrente = rs.getString("prot");
							String categoriaCorrente = rs.getString("prop_categoria");
							String chiaveCorrente = protocolloCorrente + "|" +  fornituraCorrente;
							if (chiave.trim().equalsIgnoreCase("")){
								//primo giro
								mese = meseCorrente;
								protocollo = protocolloCorrente;
								categoria = categoriaCorrente + ", ";
								chiave = chiaveCorrente;
							}else{
								if (chiave.trim().equalsIgnoreCase(chiaveCorrente.trim())){
									//non è ancora il punto di rottura per cui concateno la categoria se non è già stato fatto
									if (categoria != null && categoriaCorrente != null && categoria.indexOf(categoriaCorrente) == -1 )
										categoria += categoriaCorrente + ", ";
								}else{
									//punto di rottura per cui genero html con le info raccolte
									%>
									<tr onclick="vai('<%=chiave%>')">
									<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= mese %></span></td>
									<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= protocollo %></span></td>
									<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= categoria %></span></td>
									</tr>
									<%	
									mese = meseCorrente;
									protocollo = protocolloCorrente;
									categoria = categoriaCorrente + ", ";
									chiave = chiaveCorrente;
								}
							}
						}
						%>
						<tr onclick="vai('<%=chiave%>')">
						<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= mese %></span></td>
						<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= protocollo %></span></td>
						<td class="extWinTDData" style='cursor: pointer;'><span class="extWinTXTData"><%= categoria %></span></td>
						</tr>
						<%	
					%>
					</table>
					<%

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


			<form name="infoForm" method="post" action="<%=request.getContextPath()%>/Docfa" id="infoForm" target="_BLANK">
				<input type="hidden" name="DATASOURCE" value="<%=es.getDataSource()%>">
				<input type="hidden" name="ST" value="2">
				<input type="hidden" name="UC" value="43">
				<input type="hidden" name="KEYSTR" value="">
				<input type="hidden" name="OGGETTO_SEL" value="">
			</form>							
										



<div id="wait" class="cursorWait" />
		


</body>

</html>
