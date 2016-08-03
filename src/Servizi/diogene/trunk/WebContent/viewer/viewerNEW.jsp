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
<%@page import="java.sql.SQLException"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.net.URLEncoder"%>
<%!static Logger log = Logger.getLogger("diogene.log");%>

<html>

<%
	EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
	
	//costruzione url WebGis
	String urlBase = "http://miami:8888/siticatasto/extra/viewerjs/map";
	int param = 0;
	String cn=request.getParameter("cn")!=null&&!request.getParameter("cn").trim().equals("")?request.getParameter("cn"):null;
	String fgl=request.getParameter("fgl")!=null&&!request.getParameter("fgl").trim().equals("")?request.getParameter("fgl"):null;
	String par=request.getParameter("par")!=null&&!request.getParameter("par").trim().equals("")?request.getParameter("par"):null;
	String sub=request.getParameter("sub")!=null&&!request.getParameter("sub").trim().equals("")?request.getParameter("sub"):null;
	String pkana=request.getParameter("pkana")!=null&&!request.getParameter("pkana").trim().equals("")?request.getParameter("pkana"):null;
	String pkciv=request.getParameter("pkciv")!=null&&!request.getParameter("pkciv").trim().equals("")?request.getParameter("pkciv"):null;
	
	String via = null;
	String civico = null;
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
			rs = st.executeQuery("select v.VIASEDIME, v.DESCRIZIONE " +
					"from sit_d_persona_civico pc, sit_d_via v, sit_d_civico c " +
					"where pc.ID_EXT_D_PERSONA = '" + pkana + "' " +
					"and pc.ID_EXT_D_CIVICO = c.ID_EXT " +
					"and c.ID_EXT_D_VIA = v.ID_EXT " +
					"and pc.DT_FINE_VAL is null");
			
			while (rs.next()) {
				via = rs.getString("VIASEDIME");
				via += rs.getString("DESCRIZIONE");
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
			rs = st.executeQuery("SELECT DISTINCT sedime, indirizzo, LTRIM(civ_liv1_a,'0') as civico " +
			           "FROM sit_matrice_civici_t c, sit_civico_unico cu, sit_via_unico vu " +
		               "where c.id_dwh_a = '" + pkciv + "' " +
		               "and c.FK_CIVICO = cu.ID_CIVICO " +
		               "and cu.FK_VIA = vu.ID_VIA");
			
			while (rs.next()) {
				via = (rs.getString("sedime") == null || " ".equals(rs.getString("sedime")))?
						rs.getString("indirizzo"): rs.getString("sedime") + " " + rs.getString("indirizzo");
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
	
	String qry_stringa = "";
	if(cn != null) qry_stringa += "&cod_nazionale=F205";//+URLEncoder.encode(cn,"UTF-8");
		else qry_stringa += "&cod_nazionale=" + eu.getUtente().getCurrentEnte();
	if(fgl != null) qry_stringa += "&foglio="+ URLEncoder.encode(fgl,"UTF-8") ;
	if(par != null) qry_stringa += "&particella="+ URLEncoder.encode(par,"UTF-8") ;
	if(sub != null) qry_stringa += "&sub="+ URLEncoder.encode(sub,"UTF-8") ;
	if(via != null && !"nullnull".equals(via) && !"null".equals(via)) qry_stringa += "&via="+ URLEncoder.encode(via.replaceAll("'", "''"),"UTF-8");
	if(civico != null && !"null".equals(civico)) qry_stringa += "&civico="+civico;
	if(qry_stringa.length()>1) qry_stringa = "?"+qry_stringa.substring(1);
	
	String url = urlBase + qry_stringa;
	response.sendRedirect(url);
%>