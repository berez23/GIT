<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql"%>
<%
String nome = "";
String tipo = "";
if(request.getParameter("nome") != null && !request.getParameter("nome").equals(""))
{
	nome = request.getParameter("nome");
	request.getSession().setAttribute("nomeCatalogoSql",nome);
}
else if(request.getSession().getAttribute("nomeCatalogoSql") != null)
	nome = request.getSession().getAttribute("nomeCatalogoSql").toString();

if(request.getParameter("tipo") != null && !request.getParameter("tipo").equals(""))
{
	tipo = request.getParameter("tipo").toUpperCase();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheCatalogoLogic"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="java.sql.SQLException"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="javax.servlet.jsp.jstl.sql.*"%>
<%@page import="org.apache.log4j.Logger"%>
<%!static Logger log = Logger.getLogger("diogene.log");%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>SqlExplorer</title>

<link href="css/screen.css" rel="stylesheet" type="text/css" />

<script language="JavaScript">

	function mostraTooltip(testo, spostaDaMano) {
		if (testo != "") {
			var x = event.clientX;
			var y = event.clientY;
			if (spostaDaMano) {
				x += 12;
			}
			document.getElementById("tooltip").style.left = x;
			document.getElementById("tooltip").style.top = y;
			document.getElementById("tooltip").innerHTML = testo;
			document.getElementById("tooltip").style.display = "block";
		} else {
			document.getElementById("tooltip").style.display = "none";
		}	
	}
	
	function nascondiTooltip() {
		document.getElementById("tooltip").style.display = "none";
	}

</script>

<style type="text/css">

	.tooltip {
		position: absolute;
		display: none;
		width: 250px;
		color: #000099;
		background-color: #D9D9D9;
		border: 2px white solid;
		padding: 2px;
		font-family: Verdana;
		font-size: 7pt;
		font-weight: bold;
	}

</style>

</head>

<body>
	
	<% if(tipo.equals("")) {
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource = (DataSource) cont.lookup(es.getDataSourceIntegrato());
		Connection cnn = theDataSource.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		try {
		
			st = cnn.createStatement();
			rs = st.executeQuery("SELECT IDCATALOGOSQL, NOME, DESCRIZIONE FROM CATALOGOSQL WHERE ABILITATO = 1  AND UPPER(RTRIM(LTRIM(NOME)))='"+nome.toUpperCase()+"' ORDER BY IDCATALOGOSQL");
			Result dati = ResultSupport.toResult(rs); 
			pageContext.setAttribute("dati", dati);
		
		} catch (SQLException e) {
			log.error("Errore caricamento catalogo SQL", e);
	
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (cnn != null)
					cnn.close();
			} catch (SQLException e) {
			}
		}
		%>
		<div style="width: 80%;margin-left: 10%; margin-right: 10%">
			 <display:table name="pageScope.dati.rows"  style="width: 100%" class="TXTmainLabel" >
			 	<display:column property="NOME" width="10%"  title="Nome Comando"  href="sqlRis.jsp?nuova=1" paramId="command"  paramProperty="IDCATALOGOSQL"  sortable="true" nowrap="true"/>
				<display:column property="DESCRIZIONE" title="Descrizione Comando" href="sqlRis.jsp?nuova=1" paramId="command"  paramProperty="IDCATALOGOSQL" sortable="true"/>
			 </display:table>
		</div>
	<% } else {%>
			<% DiagnosticheCatalogoLogic logic = new DiagnosticheCatalogoLogic();
			ArrayList<Hashtable<String, Object>> dati = logic.getListaDiagnostiche(nome, request);
			if (dati != null && dati.size() > 0) {				
				Hashtable<String, Object> primaRiga = (Hashtable<String, Object>)dati.get(0);
				ArrayList date = (ArrayList)primaRiga.get(DiagnosticheCatalogoLogic.DATE);%>
				<div id="tooltip" class="tooltip">					
				</div>
				<div style="width: 80%;margin-left: 10%; margin-right: 10%">
					<% Hashtable<String, String> titoli = new Hashtable<String, String>();
					titoli.put("D_CTR_DEM", "Diagnostiche di controllo - Demografia");
					//titoli.put("D_CTR_TRI", "Diagnostiche di controllo - Tributi");
					titoli.put("D_CTR_CAT", "Diagnostiche di controllo - Catasto");
					titoli.put("D_CTR_ALT", "Diagnostiche di controllo - Altri archivi");
					titoli.put("D_CTR_TRI2", "Diagnostiche di controllo - Tributi");
					titoli.put("D_CFR_DEM", "Diagnostiche di confronto - Demografia");
					titoli.put("D_CFR_TRI", "Diagnostiche di confronto - Tributi");
					titoli.put("D_CFR_CAT", "Diagnostiche di confronto - Catasto");
					titoli.put("D_CFR_ALT", "Diagnostiche di confronto - Altri archivi");
					//titoli.put("D_CFR_ICI", "Diagnostiche di confronto - ICI");
					//titoli.put("D_CFR_TAR", "Diagnostiche di confronto - TARSU");
					titoli.put("D_CFR_TOP", "Diagnostiche di confronto - Toponomastica");
					titoli.put("D_CFR_ICI2", "Diagnostiche di confronto - ICI");
					titoli.put("D_CFR_TAR2", "Diagnostiche di confronto - TARSU");
					titoli.put("S_STA_DEM", "Statistiche - Demografia");
					titoli.put("S_STA_TRI", "Statistiche - Tributi");
					titoli.put("S_STA_CAT", "Statistiche - Catasto");
					titoli.put("S_STA_ALT", "Statistiche - Altri archivi");
					titoli.put("L_LIS_DEM", "Liste - Demografia");
					titoli.put("L_LIS_TRI", "Liste - Tributi");
					titoli.put("L_LIS_CAT", "Liste - Catasto");
					titoli.put("L_LIS_ALT", "Liste - Altri archivi");%>
					<div style="width: 100%; padding-top: 15px; font-size: 10pt; font-weight: bold; color: #000099;">
						<%= titoli.get(nome) != null ? titoli.get(nome) : "" %>
					</div>
					<table style="width: 100%" class="TXTmainLabel">
						<thead>
							<tr>
								<th width="40%">
									Descrizione<%=tipo.equals("LIST") ? " lista" : (tipo.equals("STAT") ? " statistica" : " diagnostica")%>
								</th>
								<%for (int i = 0; i < date.size(); i++) {%>
									<th width="15%">
										<%= (String)date.get(i) %>
									</th>
								<% } %>
								<th>
									&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
							<% for (int i = 0; i < dati.size(); i++) {
								Hashtable<String, Object> riga = (Hashtable<String, Object>)dati.get(i);
								int conta = riga.get(DiagnosticheCatalogoLogic.CONTA) != null ? ((Integer)riga.get(DiagnosticheCatalogoLogic.CONTA)).intValue() : 0;%>
								<tr class="<%=(i % 2) == 0 ? "odd" : "even"%>">
									<td width="40%">
										<span onmouseover="mostraTooltip('<%= DiagnosticheCatalogoLogic.eliminaProblemaApiciJS((String)riga.get(DiagnosticheCatalogoLogic.TOOLTIPTEXT)) %>', <%= conta > 0 %>);" onmouseout="nascondiTooltip();">
										<% if (conta > 0) { %>
											<a href="diaExecList.jsp?nome=<%=nome%>&tipo=<%=tipo%>&id=<%=((Integer)riga.get(DiagnosticheCatalogoLogic.ID)).intValue()%>&descrizione=<%=(String)riga.get(DiagnosticheCatalogoLogic.DESCRIZIONE)%>">
												<%=(String)riga.get(DiagnosticheCatalogoLogic.DESCRIZIONE)%>
											</a>
										<% } else { %>
												<%=(String)riga.get(DiagnosticheCatalogoLogic.DESCRIZIONE)%>
										<% } %>
										</span>
									</td>
									<% if (conta > 0) { %>
										<%for (int j = 0; j < date.size(); j++) {%>
											<td width="15%">
												<% ArrayList ultime = (ArrayList)riga.get(DiagnosticheCatalogoLogic.ULTIME);
												if (ultime != null && ultime.size() > 0) {
													Hashtable ultima = (Hashtable)ultime.get(j);
													Iterator it = ultima.keySet().iterator();
													while (it.hasNext()) {
														String key = (String)it.next();
														if (key.equals("-") || key.equals("0")) {%>
															<%=key%>
														<% } else {%>
															<a href="<%=request.getContextPath()%>/DiagnosticheCatalogo?ST=99&PATH_FILE=<%=ultima.get(key)%>"><%=key%>
															</a>
												<%		}
													}
												}%>
											</td>
										<% } %>
										<td>
											<a href="diaExecList.jsp?nome=<%=nome%>&tipo=<%=tipo%>&id=<%=((Integer)riga.get(DiagnosticheCatalogoLogic.ID)).intValue()%>&descrizione=<%=(String)riga.get(DiagnosticheCatalogoLogic.DESCRIZIONE)%>">
												Tutte
											</a>
										</td>
									<% } else { %>
											<td colspan="<%= date.size() + 1 %>">
												nessuna esecuzione effettuata
											</td>
									<% } %>
								</tr>
							<% } %>
						</tbody>
					</table>
				</div>
			<% } else {%>
				<div style="width: 80%; margin-left: 10%; margin-right: 10%; padding-top: 15px; font-size: 10pt; font-weight: bold; color: #000099;">
			 		Nessuna<%=tipo.equals("LIST") ? " lista " : (tipo.equals("STAT") ? " statistica " : " diagnostica ")%>trovata
				</div>
			<% }%>
	<% }%>
</body>
</html>


<c:remove var="results" scope="session" />
<c:remove var="resultsSchema" scope="session" />