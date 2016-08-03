<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%@page import="javax.naming.Context"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="it.escsolution.escwebgis.common.EscServlet"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="it.webred.cet.permission.CeTUser"%>

<html>
<head>
<% EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	EnvSource es = new EnvSource(eu.getEnte());%>
<title>Informazioni sul Catasto <% if (eu.getEnte().equals("F205")) {%> e sul PGT<% } %></title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>

<script type="text/javascript">
function openView(percorso){
	nuova=window.open(percorso,"windowdati","width=550, height=500, statusbar=no,scrollbars=no");
	nuova.focus();
}

</script>

<%
String layerName = ((String)request.getParameter("LayerName")).toUpperCase();
String pk_particelle = request.getParameter("OGGETTO_SEL");
%>

</head>

	<body>
			
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="extWinTDData"><span class="extWinTXTTitle">Informazioni sul Catasto</span>
					<div>
						<br/>
				    	<a href="javascript:openView('<%=request.getContextPath()%>/CatastoGauss?DATASOURCE=jdbc/dbIntegrato&ST=3&UC=12&origine=MAPPA&OGGETTO_SEL=<%=pk_particelle%>&LAYER=<%=layerName%>')">Catasto</a>
				        <br/>
					</div>			
				</td>
			</tr>
		</table>
		
		<% if (eu.getEnte().equals("F205")) {
		
			if (false) { %>
			<br/><br/>
			
			<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
				<tr>
					<td class="extWinTDData"><span class="extWinTXTTitle">Informazioni sul PGT</span>							
						<div>
							<%	
								DecimalFormat DF = new DecimalFormat();
								DF.setGroupingUsed(false);
								DecimalFormatSymbols dfs = new DecimalFormatSymbols();
								dfs.setDecimalSeparator(',');
								DF.setDecimalFormatSymbols(dfs);
								DF.setMaximumFractionDigits(2);
								
								ArrayList<HashMap<String, String>> datiPgt = new ArrayList<HashMap<String, String>>();
								ArrayList<HashMap<String, String>> datiAggPgt = new ArrayList<HashMap<String, String>>();
								ArrayList<HashMap<String, String>> datiR02 = new ArrayList<HashMap<String, String>>();
								ArrayList<HashMap<String, String>> datiS02 = new ArrayList<HashMap<String, String>>();
								Context cont= new InitialContext();

						  		javax.naming.Context datasourceContext= new javax.naming.InitialContext();
								DataSource theDataSource = null;
								theDataSource =(DataSource)datasourceContext.lookup(es.getDataSource());
								Connection cnn = theDataSource.getConnection();
								PreparedStatement pst = null;
								ResultSet rs = null;								
								try
								{
									String sql = "SELECT A.* " +
									"FROM CAT_S01_01 A, CAT_PARTICELLE_GAUSS B " +
									"WHERE SDO_RELATE (A.SHAPE, B.GEOMETRY,'MASK=ANYINTERACT') = 'TRUE' " +
									"AND B.PK_PARTICELLE = ? " +
									"AND UPPER(CODICE) <> 'NESSUN ATTRIBUTO' " +
									"AND NVL(TO_CHAR(A.DATA_FINE_VAL, 'DD/MM/YYYY'),'31/12/9999') = '31/12/9999'";
									
									pst = cnn.prepareStatement(sql);
									pst.setString(1, pk_particelle);
									rs = pst.executeQuery();
									while (rs.next()){
										HashMap<String, String> datoPgt = new HashMap<String, String>();
										datoPgt.put("TAVOLA", rs.getObject("TAVOLA") == null ?
												"-" : 
												(rs.getString("TAVOLA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TAVOLA")));
										datoPgt.put("QUAL_SER", rs.getObject("QUAL_SER") == null ?
												"-" : 
												(rs.getString("QUAL_SER").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("QUAL_SER")));
										datoPgt.put("TIPO_SER_1", rs.getObject("TIPO_SER_1") == null ?
												"-" : 
												(rs.getString("TIPO_SER_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_SER_1")));
										datoPgt.put("TIPO_SER_2", rs.getObject("TIPO_SER_2") == null ?
												"-" : 
												(rs.getString("TIPO_SER_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_SER_2")));
										datoPgt.put("TIPO_SER_3", rs.getObject("TIPO_SER_3") == null ?
												"-" : 
												(rs.getString("TIPO_SER_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_SER_3")));
										datoPgt.put("NIL", rs.getObject("NIL") == null ?
												"-" : 
												(rs.getString("NIL").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("NIL")));
										datoPgt.put("TIPO_NIL", rs.getObject("TIPO_NIL") == null ?
												"-" : 
												(rs.getString("TIPO_NIL").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_NIL")));											
										datoPgt.put("PARCHI", rs.getObject("PARCHI") == null ?
												"-" : 
												(rs.getString("PARCHI").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("PARCHI")));
										datoPgt.put("TIPO_PAR_1", rs.getObject("TIPO_PAR_1") == null ?
												"-" : 
												(rs.getString("TIPO_PAR_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PAR_1")));
										datoPgt.put("TIPO_PAR_2", rs.getObject("TIPO_PAR_2") == null ?
												"-" : 
												(rs.getString("TIPO_PAR_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PAR_2")));
										datoPgt.put("AREA", rs.getObject("AREA") == null ?
												"-" : 
												DF.format(rs.getDouble("AREA")));
										datiPgt.add(datoPgt);
									}
									
									sql = "SELECT A.* " +
									"FROM CAT_R04_08 A, CAT_PARTICELLE_GAUSS B " +
									"WHERE SDO_RELATE (A.SHAPE, B.GEOMETRY,'MASK=ANYINTERACT') = 'TRUE' " +
									"AND B.PK_PARTICELLE = ? " +
									"AND NVL(TO_CHAR(A.DATA_FINE_VAL, 'DD/MM/YYYY'),'31/12/9999') = '31/12/9999' " +
									"ORDER BY A.TAVOLA";
									
									pst = cnn.prepareStatement(sql);
									pst.setString(1, pk_particelle);
									rs = pst.executeQuery();
									while (rs.next()){
										HashMap<String, String> datoAggPgt = new HashMap<String, String>();
										datoAggPgt.put("TAVOLA", rs.getObject("TAVOLA") == null ?
												"-" : 
												(rs.getString("TAVOLA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TAVOLA")));
										datoAggPgt.put("CODICE", rs.getObject("CODICE") == null ?
												"-" : 
												(rs.getString("CODICE").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("CODICE")));
										String nomeCampo = rs.getObject("TIPO_CODICE") == null ?
														null : 
														(rs.getString("TIPO_CODICE").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString("TIPO_CODICE").trim());
										String label1 = null;
										String valoreCampo1 = null;
										String label2 = null;
										String valoreCampo2 = null;
										if (nomeCampo != null) {
											if (nomeCampo.equalsIgnoreCase("COMPL_EDI")) {
												label2 = "Complessi Edilizi";
											} else if (nomeCampo.equalsIgnoreCase("NAF")) {
												label2 = "Nuclei Antica Formazione";
											} else if (nomeCampo.equalsIgnoreCase("TIPO_INT_1")) {
												label1 = "Intervento";
												valoreCampo1 = rs.getObject("INTERVENTO") == null ?
														null : 
														(rs.getString("INTERVENTO").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString("INTERVENTO"));
												label2 = "Tipo Intervento 1";
											} else if (nomeCampo.equalsIgnoreCase("TIPO_INT_2")) {
												label1 = "Intervento";
												valoreCampo1 = rs.getObject("INTERVENTO") == null ?
														null : 
														(rs.getString("INTERVENTO").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString("INTERVENTO"));
												label2 = "Tipo Intervento 2";
											} else if (nomeCampo.equalsIgnoreCase("TIPO_VIN_1")) {
												label1 = "Vincoli";
												valoreCampo1 = rs.getObject("VINCOLI") == null ?
														null : 
														(rs.getString("VINCOLI").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString("VINCOLI"));
												label2 = "Tipo Vincoli 1";
											} else if (nomeCampo.equalsIgnoreCase("TIPO_VIN_2")) {
												label1 = "Vincoli";
												valoreCampo1 = rs.getObject("VINCOLI") == null ?
														null : 
														(rs.getString("VINCOLI").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString("VINCOLI"));
												label2 = "Tipo Vincoli 2";
											} else if (nomeCampo.equalsIgnoreCase("TIPO_NOR_1")) {
												label1 = "Norma";
												valoreCampo1 = rs.getObject("NORMA") == null ?
														null : 
														(rs.getString("NORMA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString("NORMA"));
												label2 = "Tipo Norma 1";
											} else if (nomeCampo.equalsIgnoreCase("TIPO_NOR_2")) {
												label1 = "Norma";
												valoreCampo1 = rs.getObject("NORMA") == null ?
														null : 
														(rs.getString("NORMA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString("NORMA"));
												label2 = "Tipo Norma 2";
											}
											valoreCampo2 = rs.getObject(nomeCampo) == null ?
														null : 
														(rs.getString(nomeCampo).trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? null : rs.getString(nomeCampo));
										}
										datoAggPgt.put("LABEL_1", label1);
										datoAggPgt.put("VALORE_CAMPO_1", valoreCampo1);
										datoAggPgt.put("LABEL_2", label2);
										datoAggPgt.put("VALORE_CAMPO_2", valoreCampo2);
										datiAggPgt.add(datoAggPgt);
									}
									
									sql = "SELECT A.* " +
									"FROM CAT_R02_03 A, CAT_PARTICELLE_GAUSS B " +
									"WHERE SDO_RELATE (A.SHAPE, B.GEOMETRY,'MASK=ANYINTERACT') = 'TRUE' " +
									"AND B.PK_PARTICELLE = ? " +
									"AND NVL(TO_CHAR(A.DATA_FINE_VAL, 'DD/MM/YYYY'),'31/12/9999') = '31/12/9999'";
									
									pst = cnn.prepareStatement(sql);
									pst.setString(1, pk_particelle);
									rs = pst.executeQuery();
									while (rs.next()){
										HashMap<String, String> datoR02 = new HashMap<String, String>();
										String dato = "";
										String dato1 = "";
										String dato2 = "";
										String dato3 = "";
										dato = rs.getObject("TUC") == null ?
													"-" : 
													(rs.getString("TUC").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TUC"));
										dato1 = "Tipo TUC: " + (rs.getObject("TIPO_TUC") == null ?
													"-" : 
													(rs.getString("TIPO_TUC").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_TUC")));
										datoR02.put("TUC", dato + "<br/>" + dato1 + "<br/>");
										dato = rs.getObject("TIP_INTERV") == null ?
												"-" : 
												(rs.getString("TIP_INTERV").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIP_INTERV"));
										dato1 = "Tipo Tip. Interv. 1: " + (rs.getObject("TIPO_INT_1") == null ?
												"-" : 
												(rs.getString("TIPO_INT_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INT_1")));
										dato2 = "Tipo Tip. Interv. 2: " + (rs.getObject("TIPO_INT_2") == null ?
												"-" : 
												(rs.getString("TIPO_INT_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INT_2")));
										datoR02.put("TIP_INTERV", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>");
										dato = rs.getObject("ADR") == null ?
												"-" : 
												(rs.getString("ADR").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("ADR"));
										dato1 = "Tipo ADR: " + (rs.getObject("TIPO_ADR") == null ?
												"-" : 
												(rs.getString("TIPO_ADR").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_ADR")));
										datoR02.put("ADR", dato + "<br/>" + dato1 + "<br/>");
										dato = rs.getObject("QRI_STORIC") == null ?
												"-" : 
												(rs.getString("QRI_STORIC").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("QRI_STORIC"));
										datoR02.put("QRI_STORIC", dato + "<br/>");										
										dato = rs.getObject("ARU") == null ?
												"-" : 
												(rs.getString("ARU").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("ARU"));
										dato1 = "Tipo ARU 1: " + (rs.getObject("TIPO_ARU_1") == null ?
												"-" : 
												(rs.getString("TIPO_ARU_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_ARU_1")));
										dato2 = "Tipo ARU 2: " + (rs.getObject("TIPO_ARU_2") == null ?
												"-" : 
												(rs.getString("TIPO_ARU_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_ARU_2")));
										dato3 = "Tipo ARU 3: " + (rs.getObject("TIPO_ARU_3") == null ?
												"-" : 
												(rs.getString("TIPO_ARU_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_ARU_3")));
										datoR02.put("ARU", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>" + dato3 + "<br/>");
										dato = rs.getObject("PARCHI") == null ?
												"-" : 
												(rs.getString("PARCHI").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("PARCHI"));
										dato1 = "Tipo Parchi 1: " + (rs.getObject("TIPO_PAR_1") == null ?
												"-" : 
												(rs.getString("TIPO_PAR_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PAR_1")));
										dato2 = "Tipo Parchi 2: " + (rs.getObject("TIPO_PAR_2") == null ?
												"-" : 
												(rs.getString("TIPO_PAR_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PAR_2")));
										datoR02.put("PARCHI", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>");
										dato = rs.getObject("NORMA") == null ?
												"-" : 
												(rs.getString("NORMA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("NORMA"));
										dato1 = "Tipo Norma 1: " + (rs.getObject("TIPO_NOR_1") == null ?
												"-" : 
												(rs.getString("TIPO_NOR_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_NOR_1")));
										dato2 = "Tipo Norma 2: " + (rs.getObject("TIPO_NOR_2") == null ?
												"-" : 
												(rs.getString("TIPO_NOR_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_NOR_2")));
										dato3 = "Tipo Norma 3: " + (rs.getObject("TIPO_NOR_3") == null ?
												"-" : 
												(rs.getString("TIPO_NOR_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_NOR_3")));
										datoR02.put("NORMA", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>" + dato3 + "<br/>");
										dato = rs.getObject("VERDE") == null ?
												"-" : 
												(rs.getString("VERDE").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("VERDE"));
										datoR02.put("VERDE", dato + "<br/>");
										dato = rs.getObject("PERT_IND") == null ?
												"-" : 
												(rs.getString("PERT_IND").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("PERT_IND"));
										dato1 = "Tipo Pert. Ind. 1: " + (rs.getObject("TIPO_PER_1") == null ?
												"-" : 
												(rs.getString("TIPO_PER_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PER_1")));
										dato2 = "Tipo Pert. Ind. 2: " + (rs.getObject("TIPO_PER_2") == null ?
												"-" : 
												(rs.getString("TIPO_PER_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PER_2")));
										dato3 = "Tipo Pert. Ind. 3: " + (rs.getObject("TIPO_PER_3") == null ?
												"-" : 
												(rs.getString("TIPO_PER_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PER_3")));
										datoR02.put("PERT_IND", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>" + dato3 + "<br/>");
										dato = rs.getObject("INFRASTRUT") == null ?
												"-" : 
												(rs.getString("INFRASTRUT").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("INFRASTRUT"));
										dato1 = "Tipo Infrastrut. 1: " + (rs.getObject("TIPO_INF_1") == null ?
												"-" : 
												(rs.getString("TIPO_INF_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INF_1")));
										dato2 = "Tipo Infrastrut. 2: " + (rs.getObject("TIPO_INF_2") == null ?
												"-" : 
												(rs.getString("TIPO_INF_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INF_2")));
										dato3 = "Tipo Infrastrut. 3: " + (rs.getObject("TIPO_INF_3") == null ?
												"-" : 
												(rs.getString("TIPO_INF_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INF_3")));
										datoR02.put("INFRASTRUT", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>" + dato3 + "<br/>");
										dato = rs.getObject("SERVIZI") == null ?
												"-" : 
												(rs.getString("SERVIZI").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("SERVIZI"));
										dato1 = "Tipo Servizi 1: " + (rs.getObject("TIPO_SER_1") == null ?
												"-" : 
												(rs.getString("TIPO_SER_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_SER_1")));
										dato2 = "Tipo Servizi 2: " + (rs.getObject("TIPO_SER_2") == null ?
												"-" : 
												(rs.getString("TIPO_SER_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_SER_2")));
										datoR02.put("SERVIZI", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>");
										dato = rs.getObject("TECNOLOGIC") == null ?
												"-" : 
												(rs.getString("TECNOLOGIC").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TECNOLOGIC"));
										dato1 = "Tipo Tecnol.: " + (rs.getObject("TIPO_TECNO") == null ?
												"-" : 
												(rs.getString("TIPO_TECNO").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_TECNO")));
										datoR02.put("TECNOLOGIC", dato + "<br/>" + dato1 + "<br/>");										
										datiR02.add(datoR02);
									}
									
									sql = "SELECT A.* " +
									"FROM CAT_S02_14 A, CAT_PARTICELLE_GAUSS B " +
									"WHERE SDO_RELATE (A.SHAPE, B.GEOMETRY,'MASK=ANYINTERACT') = 'TRUE' " +
									"AND B.PK_PARTICELLE = ? " +
									"AND NVL(TO_CHAR(A.DATA_FINE_VAL, 'DD/MM/YYYY'),'31/12/9999') = '31/12/9999'";
									
									pst = cnn.prepareStatement(sql);
									pst.setString(1, pk_particelle);
									rs = pst.executeQuery();
									while (rs.next()){
										HashMap<String, String> datoS02 = new HashMap<String, String>();
										String dato = "";
										String dato1 = "";
										String dato2 = "";
										String dato3 = "";
										dato = rs.getObject("TAVOLA") == null ?
													"-" : 
													(rs.getString("TAVOLA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TAVOLA"));										
										datoS02.put("TAVOLA", dato + "<br/>");
										dato = rs.getObject("PERT_IND") == null ?
												"-" : 
												(rs.getString("PERT_IND").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("PERT_IND"));
										dato1 = "Tipo Per. 1: " + (rs.getObject("TIPO_PER_1") == null ?
												"-" : 
												(rs.getString("TIPO_PER_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PER_1")));
										dato2 = "Tipo Per. 2: " + (rs.getObject("TIPO_PER_2") == null ?
												"-" : 
												(rs.getString("TIPO_PER_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PER_2")));
										dato3 = "Tipo Per. 3: " + (rs.getObject("TIPO_PER_3") == null ?
												"-" : 
												(rs.getString("TIPO_PER_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PER_3")));
										datoS02.put("PERT_IND", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>" + dato3 + "<br/>");
										dato = rs.getObject("VERDE") == null ?
												"-" : 
												(rs.getString("VERDE").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("VERDE"));
										datoS02.put("VERDE", dato + "<br/>");										
										dato = rs.getObject("MOBILITA") == null ?
												"-" : 
												(rs.getString("MOBILITA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("MOBILITA"));
										dato1 = "Tipo Mob. 1: " + (rs.getObject("TIPO_MOB_1") == null ?
												"-" : 
												(rs.getString("TIPO_MOB_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_MOB_1")));
										dato2 = "Tipo Mob. 2: " + (rs.getObject("TIPO_MOB_2") == null ?
												"-" : 
												(rs.getString("TIPO_MOB_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_MOB_2")));
										datoS02.put("MOBILITA", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>");
										dato = rs.getObject("INFRASTRUT") == null ?
												"-" : 
												(rs.getString("INFRASTRUT").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("INFRASTRUT"));
										dato1 = "Tipo Infrastrut. 1: " + (rs.getObject("TIPO_INF_1") == null ?
												"-" : 
												(rs.getString("TIPO_INF_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INF_1")));
										dato2 = "Tipo Infrastrut. 2: " + (rs.getObject("TIPO_INF_2") == null ?
												"-" : 
												(rs.getString("TIPO_INF_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INF_2")));
										dato3 = "Tipo Infrastrut. 3: " + (rs.getObject("TIPO_INF_3") == null ?
												"-" : 
												(rs.getString("TIPO_INF_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_INF_3")));
										datoS02.put("INFRASTRUT", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>" + dato3 + "<br/>");
										dato = rs.getObject("PARCHI") == null ?
												"-" : 
												(rs.getString("PARCHI").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("PARCHI"));
										dato1 = "Tipo Parchi 1: " + (rs.getObject("TIPO_PAR_1") == null ?
												"-" : 
												(rs.getString("TIPO_PAR_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PAR_1")));
										dato2 = "Tipo Parchi 2: " + (rs.getObject("TIPO_PAR_2") == null ?
												"-" : 
												(rs.getString("TIPO_PAR_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_PAR_2")));
										datoS02.put("PARCHI", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>");
										dato = rs.getObject("RETE_FER") == null ?
												"-" : 
												(rs.getString("RETE_FER").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("RETE_FER"));
										dato1 = "Tipo Fer. 1: " + (rs.getObject("TIPO_FER_1") == null ?
												"-" : 
												(rs.getString("TIPO_FER_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_FER_1")));
										datoS02.put("RETE_FER", dato + "<br/>" + dato1 + "<br/>");
										dato = rs.getObject("RETE_MET") == null ?
												"-" : 
												(rs.getString("RETE_MET").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("RETE_MET"));
										dato1 = "Tipo Met. 1: " + (rs.getObject("TIPO_MET_1") == null ?
												"-" : 
												(rs.getString("TIPO_MET_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_MET_1")));
										datoS02.put("RETE_MET", dato + "<br/>" + dato1 + "<br/>");
										dato = rs.getObject("RETE_LIN") == null ?
												"-" : 
												(rs.getString("RETE_LIN").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("RETE_LIN"));
										dato1 = "Tipo Lin. 1: " + (rs.getObject("TIPO_LIN_1") == null ?
												"-" : 
												(rs.getString("TIPO_LIN_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_LIN_1")));
										datoS02.put("RETE_LIN", dato + "<br/>" + dato1 + "<br/>");										
										dato = rs.getObject("RETE_TRA") == null ?
												"-" : 
												(rs.getString("RETE_TRA").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("RETE_TRA"));
										dato1 = "Tipo Tram: " + (rs.getObject("TIPO_TRAM") == null ?
												"-" : 
												(rs.getString("TIPO_TRAM").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_TRAM")));
										datoS02.put("RETE_TRA", dato + "<br/>" + dato1 + "<br/>");
										dato = rs.getObject("STAZIONI") == null ?
												"-" : 
												(rs.getString("STAZIONI").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("STAZIONI"));
										dato1 = "Tipo Staz.: " + (rs.getObject("TIPO_STAZ") == null ?
												"-" : 
												(rs.getString("TIPO_STAZ").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_STAZ")));
										datoS02.put("STAZIONI", dato + "<br/>" + dato1 + "<br/>");
										dato = rs.getObject("TRASPORTO") == null ?
												"-" : 
												(rs.getString("TRASPORTO").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TRASPORTO"));
										dato1 = "Tipo Tr. 1: " + (rs.getObject("TIPO_TR_1") == null ?
												"-" : 
												(rs.getString("TIPO_TR_1").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_TR_1")));
										dato2 = "Tipo Tr. 2: " + (rs.getObject("TIPO_TR_2") == null ?
												"-" : 
												(rs.getString("TIPO_TR_2").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_TR_2")));
										dato3 = "Tipo Tr. 3: " + (rs.getObject("TIPO_TR_3") == null ?
												"-" : 
												(rs.getString("TIPO_TR_3").trim().equalsIgnoreCase("NESSUN ATTRIBUTO") ? "-" : rs.getString("TIPO_TR_3")));
										datoS02.put("TRASPORTO", dato + "<br/>" + dato1 + "<br/>" + dato2 + "<br/>" + dato3 + "<br/>");
										datiS02.add(datoS02);
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
							<br/>							
							<% if (datiPgt.size() > 0) {%>
								<table class="extWinTable" style="width: 100%;">
									<tr>
										<td class="extWinTDData" colspan="11"><span class="extWinTXTTitle">Servizi Pubblici Comunali esistenti</span>						
									</tr>					
									<tr>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tavola</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Qualifica Servizio</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Servizio 1</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Servizio 2</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Servizio 3</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Nuclei Identità Locale</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo N.I.L.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Parchi</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Parco 1</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tipo Parco 2</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Area</span></td>
									</tr>
									<%for (Object obj : datiPgt) {
										HashMap datoPgt = (HashMap)obj;
									%>
										<tr>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("TAVOLA") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("QUAL_SER") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("TIPO_SER_1") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("TIPO_SER_2") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("TIPO_SER_3") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("NIL") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("TIPO_NIL") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("PARCHI") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("TIPO_PAR_1") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoPgt.get("TIPO_PAR_2") %></span>
											</td>
											<td class="extWinTDData" style="text-align: right;">
												<span class="extWinTXTData"><%= (String)datoPgt.get("AREA") %></span>
											</td>
										</tr>
									<% } %>
								</table>
								<br/>							
							<% } %>
							<% if (datiAggPgt.size() > 0) {%>
								<table class="extWinTable" style="width: 100%;">
									<tr>
										<td class="extWinTDData" colspan="8"><span class="extWinTXTTitle">Nuclei di antica formazione - Tipologie di intervento</span>						
									</tr>
									<% String corrTavola = "";
									for (Object obj : datiAggPgt) {
										HashMap datoAggPgt = (HashMap)obj;
										boolean scriviTavola = !((String)datoAggPgt.get("TAVOLA")).equalsIgnoreCase(corrTavola); 
										boolean scriviValore1 = datoAggPgt.get("LABEL_1") != null && datoAggPgt.get("VALORE_CAMPO_1") != null; 
										boolean scriviValore2 = datoAggPgt.get("LABEL_2") != null && datoAggPgt.get("VALORE_CAMPO_2") != null; %>
										<tr>
											<% if (scriviTavola) {%>
												<td class="extWinTDTitle" style="width: 10%">
													<span class="extWinTXTTitle">
														Tavola
													</span>
												</td>
												<td class="extWinTDData" style="width: 15%">
													<span class="extWinTXTData">
														<%= (String)datoAggPgt.get("TAVOLA") %>
													</span>
												</td>
											<% } else {%>
												<td class="extWinTDData" style="width: 25%" colspan="2">
													<span class="extWinTXTData">
														&nbsp;
													</span>
												</td>
											<% } %>											
											<td class="extWinTDTitle" style="width: 10%">
												<span class="extWinTXTTitle">
													Codice
												</span>
											</td>
											<td class="extWinTDData" style="width: 15%">
												<span class="extWinTXTData">
													<%= (String)datoAggPgt.get("CODICE") %>
												</span>
											</td>
											<% if (scriviValore1 || scriviValore2) {%>
												<td class="extWinTDTitle" style="width: 10%">
													<span class="extWinTXTTitle">
														<% if (scriviValore1) {%>
															<%= (String)datoAggPgt.get("LABEL_1") %>
														<% } else if (scriviValore2) {%>
															<%= (String)datoAggPgt.get("LABEL_2") %>
														<% }%>
													</span>
												</td>
												<td class="extWinTDData" style="width: 15%">
													<span class="extWinTXTData">
														<% if (scriviValore1) {%>
															<%= (String)datoAggPgt.get("VALORE_CAMPO_1") %>
														<% } else if (scriviValore2) {%>
															<%= (String)datoAggPgt.get("VALORE_CAMPO_2") %>
														<% }%>
													</span>
												</td>
											<% } else {%>
												<td class="extWinTDData" style="width: 25%" colspan="2">
													<span class="extWinTXTData">
														&nbsp;
													</span>
												</td>
											<% } %>											
											<% if (scriviValore1 && scriviValore2) {%>
												<td class="extWinTDTitle" style="width: 10%">
													<span class="extWinTXTTitle">
														<%= (String)datoAggPgt.get("LABEL_2") %>
													</span>
												</td>
												<td class="extWinTDData" style="width: 15%">
													<span class="extWinTXTData">
														<%= (String)datoAggPgt.get("VALORE_CAMPO_2") %>
													</span>
												</td>
											<% } else {%>
												<td class="extWinTDData" style="width: 25%" colspan="2">
													<span class="extWinTXTData">
														&nbsp;
													</span>
												</td>
											<% } %>
										</tr>
										<%corrTavola = (String)datoAggPgt.get("TAVOLA");
									} %>
								</table>
								<br/>
							<% }%>
							<% if (datiR02.size() > 0) {%>
								<table class="extWinTable" style="width: 100%;">
									<tr>
										<td class="extWinTDData" colspan="12"><span class="extWinTXTTitle">Indicazioni morfologiche</span>						
									</tr>				
									<tr>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">TUC</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tip. Interv.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">ADR</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Qri. Storic.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">ARU</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Parchi</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Norma</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Verde</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Pert. Ind.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Infrastrut.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Servizi</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tecnologic.</span></td>
									</tr>
									<%for (Object obj : datiR02) {
										HashMap datoR02 = (HashMap)obj;
									%>
										<tr>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("TUC") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("TIP_INTERV") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("ADR") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("QRI_STORIC") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("ARU") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("PARCHI") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("NORMA") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("VERDE") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("PERT_IND") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("INFRASTRUT") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("SERVIZI") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoR02.get("TECNOLOGIC") %></span>
											</td>
										</tr>
									<% } %>
								</table>
								<br/>							
							<% } %>
							<% if (datiS02.size() > 0) {%>
								<table class="extWinTable" style="width: 100%;">
									<tr>
										<td class="extWinTDData" colspan="12"><span class="extWinTXTTitle">Sistema del verde urbano e delle infrastrutture per la mobilità</span>						
									</tr>					
									<tr>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Tavola</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Pert. Ind.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Verde</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Mobilità</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Infrastrut.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Parchi</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Rete Fer.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Rete Met.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Rete Lin.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Rete Tra.</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Stazioni</span></td>
										<td class="extWinTDTitle"><span class="extWinTXTTitle">Trasporto</span></td>
									</tr>
									<%for (Object obj : datiS02) {
										HashMap datoS02 = (HashMap)obj;
									%>
										<tr>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("TAVOLA") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("PERT_IND") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("VERDE") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("MOBILITA") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("INFRASTRUT") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("PARCHI") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("RETE_FER") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("RETE_MET") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("RETE_LIN") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("RETE_TRA") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("STAZIONI") %></span>
											</td>
											<td class="extWinTDData">
												<span class="extWinTXTData"><%= (String)datoS02.get("TRASPORTO") %></span>
											</td>
										</tr>
									<% } %>
								</table>
								<br/>							
							<% } %>
							
							
							<% if (datiPgt.size() == 0 && datiAggPgt.size() == 0 && datiR02.size() == 0 && datiS02.size() == 0) {%>
								<span class="extWinTXTTitle">Nessun dato trovato</span>
								<br/>
							<% } %>						
							<br/>
						</div>			
					</td>
				</tr>
			</table>
		<%	} 
		} %>
	
	</body>

</html>
