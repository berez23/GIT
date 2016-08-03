<%@page import="it.webred.DecodificaPermessi"%>
<%@page import="it.webred.cet.permission.GestionePermessi"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.escsolution.escwebgis.common.EscLogic"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="java.io.*"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.net.URLEncoder"%>
<%!static Logger log = Logger.getLogger("diogene.log");%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
table
{
	font-family: sans-serif;
	border-collapse:collapse;
	background-color:white;
	font-size: 8pt;
	padding:8px;
	font-style:normal;
	font-weight:normal;
}

th, table Thead th
{
	border: 1px solid #C0C0C0;
	padding:5px;
	background-color: #4A75B5;
	color:white;
	font-style:normal;
	font-weight:bold;
}
table tr td
{
	text-align:left;
	font-size:8pt;
	vertical-align:bottom;
	padding-bottom:1px;
	padding-right:2px;
	border: 1px solid #C0C0C0;
}
</style>

<title>Diogene - Localizza</title>
</head>

<body style="background-color:#F3F2F2;">

<%

	EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	
	//costruzione url WebGis
	String urlBase = "/viewerjs/extra/viewerjs/map";
	int param = 0;
	String cn=request.getParameter("cn")!=null&&!request.getParameter("cn").trim().equals("")?request.getParameter("cn"):null;
	String fgl=request.getParameter("fgl")!=null&&!request.getParameter("fgl").trim().equals("")?request.getParameter("fgl"):null;
	String par=request.getParameter("par")!=null&&!request.getParameter("par").trim().equals("")?request.getParameter("par"):null;
	String sub=request.getParameter("sub")!=null&&!request.getParameter("sub").trim().equals("")?request.getParameter("sub"):null;
	String pkana=request.getParameter("pkana")!=null&&!request.getParameter("pkana").trim().equals("")?request.getParameter("pkana"):null;
	String pkciv=request.getParameter("pkciv")!=null&&!request.getParameter("pkciv").trim().equals("")?request.getParameter("pkciv"):null;
	String idEcogCiv=request.getParameter("idEcogCiv")!=null&&!request.getParameter("idEcogCiv").trim().equals("")?request.getParameter("idEcogCiv"):null;
	
	String via = null;
	String civico = null;
	boolean piusezioni = false;
	boolean nonpresente = false;
	
	if(idEcogCiv!=null){
		System.out.println("Salto in mappa civici ecografico");
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource = (DataSource) cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
	
		Statement st = null;
		ResultSet rs = null;
		
		try {
		
			st = cnn.createStatement();
			rs = st.executeQuery("select descr_strada as via, descrizione_civico as civico from ec_top_civici where id = " + idEcogCiv);
			
			while (rs.next()) {
				if(rs.getString("via") != null)
					via = rs.getString("via");
				if(rs.getString("civico") != null)
					civico = rs.getString("civico");
			}
		
		} catch (SQLException e) {
			log.error("Errore caricamento indirizzo ecografico", e);
	
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
	}
	
	
	if(pkana != null){
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource = (DataSource) cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
	
		Statement st = null;
		ResultSet rs = null;
		
		try {
			
	
			
			st = cnn.createStatement();
			rs = st.executeQuery("select s.prefisso || ' ' || s.nome as via, c.civico as civico " +
						         "from persona_civici_shape_v pcs, sit_d_persona sp, siticivi c, sitidstr s " +
						         "where sp.id_ext = '" + pkana + "' " +
						         "and sp.id = pcs.pk_anagrafe " +
						         "and pcs.pk_siticivi = c.pkid_civi " +
						         "and c.pkid_stra = s.pkid_stra");
			
			while (rs.next()) {
				if(rs.getString("via") != null)
					via = rs.getString("via");
				if(rs.getString("civico") != null)
					civico = rs.getString("civico");
			}
			
		} catch (SQLException e) {
			log.error("Errore caricamento indirizzo soggetto", e);
	
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
	}
	
	if(pkciv != null){
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource = (DataSource) cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
	
		Statement st = null;
		ResultSet rs = null;
		
		try {
			
	
			
			st = cnn.createStatement();
			rs = st.executeQuery("select distinct s.prefisso || ' ' || s.nome as via, c.civico as civico " +
			         "from siticivi c, sitidstr s " +
			         "where c.pkid_civi = " + pkciv + " " +
			         "and c.pkid_stra = s.pkid_stra");

			while (rs.next()) {
				if(rs.getString("via") != null)
					via = rs.getString("via");
				if(rs.getString("civico") != null)
					civico = rs.getString("civico");
			}
			
		} catch (SQLException e) {
			log.error("Errore caricamento indirizzo soggetto", e);
	
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
	}
	
	if(fgl != null){
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource = (DataSource) cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
	
		Statement st = null;
		ResultSet rs = null;
		
		try {
			
	
			
			st = cnn.createStatement();
			String sql = "select count(distinct cod_nazionale) as cnt " +
			         "from sitipart where foglio = " + fgl + (par!=null?(" and particella = lpad('"+par+"',5,'0')"):"") + (sub!=null?(" and sub = " + sub):"");
			rs = st.executeQuery(sql);

			int count = 0;
			while (rs.next()) {
				count = rs.getInt("cnt");
			}
			if(count > 1)
				piusezioni = true;
			if(count == 0)
				nonpresente = true;
			
		} catch (SQLException e) {
			log.error("Errore caricamento indirizzo soggetto", e);
	
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
	}
	
	CeTUser utente = (CeTUser) request.getSession().getAttribute("user");
	boolean viewFasCiv = GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_RES, true) ||
						GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_TIT, true) ||
						GestionePermessi.autorizzato(utente, eu.getNomeIstanza(), DecodificaPermessi.ITEM_FASCICOLO_CIVICO, DecodificaPermessi.PERMESSO_FAS_CIV_LISTA_UI_CONS, true);
	
	String qry_stringa = "";
	qry_stringa += "&ente=" + eu.getUtente().getCurrentEnte();
	qry_stringa += "&qryoper=equal";
	qry_stringa += "&viewFasCiv=" + viewFasCiv;
	if(fgl != null) qry_stringa += "&foglio="+ URLEncoder.encode(fgl,"UTF-8");
	if(par != null) qry_stringa += "&particella="+ URLEncoder.encode(par,"UTF-8");
	if(sub != null) qry_stringa += "&sub="+ URLEncoder.encode(sub,"UTF-8");
	if(via != null) qry_stringa += "&via="+ URLEncoder.encode(via.replaceAll("'", "''"),"UTF-8");
	if(civico != null) qry_stringa += "&civico="+civico;
	if(pkana != null) qry_stringa += "&pkana="+ URLEncoder.encode(pkana,"UTF-8");
	if(pkciv != null) qry_stringa += "&pkciv="+ URLEncoder.encode(pkciv,"UTF-8");
	if(idEcogCiv != null) qry_stringa += "&idEcogCiv="+ URLEncoder.encode(idEcogCiv,"UTF-8");
	if(!qry_stringa.contains("via") && !piusezioni) qry_stringa += "&cod_nazionale=A'OR'A'<'B";
	if(!qry_stringa.contains("via") && !qry_stringa.contains("foglio")) qry_stringa += "'OR'A'<'B";
	if(qry_stringa.length()>1) qry_stringa = "?"+qry_stringa.substring(1);
	
	String url = urlBase + qry_stringa;
	if(!piusezioni && !nonpresente)
		response.sendRedirect(url);
%>

<%=scriviSezioniTable(eu,fgl,par,sub,url,piusezioni)%>
<%=scriviSaltoMappaNonPresente(eu,fgl,par,sub,url,nonpresente)%>

<%!public String scriviSezioniTable(EnvUtente eu, String foglio, String par, String sub, String url, boolean piusezioni) {
		
		String retVal = "";
		if(piusezioni){
			
			retVal = "<br /><br /><br /><br /><br /><br /><center>Scegliere la sezione</center><br /><center><table>";
			retVal += "<tr>";
			retVal += "<th>SEZIONE</th>";
			retVal += "<th>SOTTOSEZIONE</th>";
			retVal += "<th>&nbsp;</th>";
			retVal += "</tr>";
	
			Statement st = null;
			ResultSet rs = null;
			Connection cnn = null;
			try {
				Context cont = new InitialContext();
				DataSource theDataSource = null;
				EnvSource es = new EnvSource(eu.getEnte());
				theDataSource = (DataSource) cont.lookup(es.getDataSource());
				
				cnn = theDataSource.getConnection();
				st = cnn.createStatement();
	
				st = cnn.createStatement();
				String sql = "SELECT DISTINCT c.cod_nazionale, c.ID_SEZC, c.SEZIONE2 " +
				           "FROM sitipart p, siticomu c " +
				           "WHERE p.cod_nazionale = c.cod_nazionale AND p.foglio = " + foglio + (par!=null?(" and p.particella = lpad('"+par+"',5,'0')"):"") + (sub!=null?(" and p.sub = " + sub):"");
				rs = st.executeQuery(sql);
				while (rs.next()) {
					retVal += "<tr>";
					retVal += "<td>" + (rs.getString("ID_SEZC") != null ? rs.getString("ID_SEZC") : "&nbsp;") + "</td>";
					retVal += "<td>" + (rs.getString("SEZIONE2") != null ? rs.getString("SEZIONE2") : "&nbsp;") + "</td>";
					retVal += "<td><A HREF="+url+ "&cod_nazionale="+ rs.getString("cod_nazionale") +"><img title='Vai in mappa' style='border: 0px;' src='../ewg/images/Localizza.gif'/></A></td>";
					retVal += "</tr>";
				}
	
				retVal += "</table></center>";
	
			} catch (SQLException e) {
			} catch (NamingException e) {
			}  finally {
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
		}
		
		return retVal;
	}%>
	
<%!public String scriviSaltoMappaNonPresente(EnvUtente eu, String foglio, String par, String sub, String url, boolean nonpresente) {
		
		String retVal = "";
		if(nonpresente){
			
			retVal = "<br /><br /><br /><br /><br /><br /><center><h3><font color='orange'>ATTENZIONE</font></h3></center><br />";
			retVal += "<br /><center>In cartografia non è presente il dettaglio con i seguenti dati:</center>";
			retVal += "<br /><center>Foglio "+ foglio +"</center>";
			if (par != null) retVal += "<br /><center>Particella "+ par +"</center>";
			if (sub != null) retVal += "<br /><center>Sub "+ sub +"</center>";
		}
		
		return retVal;
	}%>
	
</body>
</html>