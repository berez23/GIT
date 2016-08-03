package it.escsolution.escwebgis.common;

import it.escsolution.eiv.database.Fonte;
import it.escsolution.eiv.database.Fonti;
import it.escsolution.eiv.database.Link;
import it.webred.cet.permission.CeTUser;
import it.webred.utils.CollectionsUtils;
import it.webred.utils.DateFormat;
import it.webred.utils.StringUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class UpdateServlet extends EscServlet{

	private static final long serialVersionUID = 3324100703716659023L;
	
	private static final String SQL_SELECT_EXT_LINK = "SELECT * FROM CFG_DIOG_EXT_LINK ORDER BY NAME";
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Connection connDbTotale = null;
		Connection connDiogene = null;
		try {
			Context ctx = new InitialContext();
			if(ctx == null) 
				throw new Exception("Boom - No Context");
			String pagina = "./ewg/modValuesPopup.jsp";
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			DataSource dsDiog = (DataSource)ctx.lookup(es.getDataSource());
			if (ds != null && dsDiog != null) {
				connDbTotale = ds.getConnection();
				connDiogene = dsDiog.getConnection();
				if (connDbTotale != null && connDiogene != null)  {

					String seqTema = request.getParameter("hSeqTema");
					String provenienza = request.getParameter("hProvenienza");
					log.debug("Provenienza: " + request.getParameter("hProvenienza"));
					log.debug("hSeqTema: " + request.getParameter("hSeqTema"));
					if (provenienza != null && provenienza.trim().equalsIgnoreCase("Home.jsp")){
						pagina = "./ewg/modValuesPopup.jsp";
						String sql = "SELECT * FROM EWG_TEMA WHERE PK_TEMA = " + seqTema;
						log.debug("SQL: " + sql);
						PreparedStatement pstmt = connDbTotale.prepareStatement(sql);
						ResultSet rst = pstmt.executeQuery();
						Fonte fonte = null;
						while(rst.next()) {

							String descrizione = rst.getObject("NOME") == null ? "" : rst.getString("NOME").trim();
							String dataAgg = rst.getObject("DATA_AGG") == null ? "" : DateFormat.dateToString( rst.getDate("DATA_AGG"), "dd/MM/yyyy");
							String dataIni = rst.getObject("DATA_INI") == null ? "" : DateFormat.dateToString( rst.getDate("DATA_INI"), "dd/MM/yyyy");
							String sqlDataAgg = rst.getObject("SQL_DATA_AGG") == null ? "" : rst.getString("SQL_DATA_AGG").trim();
							String sqlDataIni = rst.getObject("SQL_DATA_INI") == null ? "" : rst.getString("SQL_DATA_INI").trim();
							String note = rst.getObject("NOTE") == null ? "" : rst.getString("NOTE").trim();
							
							fonte = new Fonte();
							fonte.setIdTema( rst.getObject("PK_TEMA")!=null?rst.getLong("PK_TEMA"):new Long(0) );
//							fonte.setIdProgetto(rst.getObject("FK_PROGETTO") == null ? null : new Integer(rst.getInt("FK_PROGETTO")));
//							if (rst.getObject("FK_PROGETTO") == null) {
//								fonte.setDescrizioneProgetto("");
//							} else {
//								if (rst.getInt("FK_PROGETTO") == 2) {
//									fonte.setDescrizioneProgetto("Archivi Interni");
//								} else {
//									fonte.setDescrizioneProgetto("Archivi Esterni");
//								}
//							}
							fonte.setDescrizioneFonte( descrizione );
							fonte.setDataAggiornamento( dataAgg );
							fonte.setDataInizio( dataIni );
							fonte.setSqlDataAgg( sqlDataAgg );
							fonte.setSqlDataIni( sqlDataIni );
							fonte.setNote( note );
						}
						if (fonte != null){
							request.setAttribute("fonte", fonte);
							request.setAttribute("hSeqTema", seqTema);
						}else
							fonte = new Fonte();
						
						rst.close();
						pstmt.close();
						
					}else if (provenienza != null && provenienza.trim().equalsIgnoreCase("modValuesPopup.jsp")){
						pagina = "./ewg/Home.jsp";
						String dataAgg = request.getParameter("txtDataAgg");
						String sqlDataAgg = request.getParameter("txtSqlDataAgg");
						String dataIni = request.getParameter("txtDataIni");
						String sqlDataIni = request.getParameter("txtSqlDataIni");
						String note = request.getParameter("txtNote");
						
						String sql = "UPDATE EWG_TEMA SET DATA_AGG=?, SQL_DATA_AGG=?, DATA_INI=?, SQL_DATA_INI=?, NOTE=? WHERE PK_TEMA = " + seqTema;
						log.debug("SQL: " + sql);
						PreparedStatement pstmt = connDbTotale.prepareStatement(sql);
						pstmt.setDate(1, DateFormat.stringToDate(dataAgg, "dd/MM/yyyy") ); 
						pstmt.setString(2, sqlDataAgg!=null?sqlDataAgg.trim():"" );
						pstmt.setDate(3, DateFormat.stringToDate(dataIni, "dd/MM/yyyy") ); 
						pstmt.setString(4, sqlDataIni!=null?sqlDataIni.trim():"" );
						pstmt.setString(5, note!=null?note.trim():"" ); 
						
						pstmt.executeUpdate();
						
						pstmt.close();						
					}else if (provenienza != null && provenienza.trim().equalsIgnoreCase("tabBar.jsp")){
						pagina = "./ewg/linkEsterniLst.jsp";
						/*
						 * Prepara lista link esterni esistenti nel DB
						 */
						ArrayList<Link> alLinks = new ArrayList<Link>();
						PreparedStatement pstmt = connDiogene.prepareStatement(SQL_SELECT_EXT_LINK);
						log.debug("SQL: " + SQL_SELECT_EXT_LINK);
						ResultSet rst = pstmt.executeQuery();						
						while(rst.next()) {
							Link link = new Link();
							
							link.setName(rst.getString("NAME"));
							link.setUrl(rst.getString("URL"));
							
							alLinks.add(link);
						}
						
						request.setAttribute("alLinks", alLinks);
						pstmt.close();	
					}else if (provenienza != null && provenienza.trim().equalsIgnoreCase("linkEsterniLst.jsp")){
						pagina = "./ewg/linkEsterniLst.jsp";
						/*
						 * recupera parametri di runtime
						 */
						String hAct = request.getParameter("hAct");

						if (hAct != null && !hAct.trim().equalsIgnoreCase("") && hAct.trim().equalsIgnoreCase("ADD")){
							String linkId = request.getParameter("linkId");
							String linkUrl = request.getParameter("linkUrl");
							/*
							 * faccio insert se i valori non sono nulli
							 */
							if ( !StringUtils.isEmpty(linkId) && !StringUtils.isEmpty(linkUrl) ){
								String sql = "INSERT INTO CFG_DIOG_EXT_LINK (NAME, URL) VALUES (?,?) ";
								PreparedStatement pstmt = connDiogene.prepareStatement(sql);
								pstmt.setString(1, linkId );
								pstmt.setString(2, linkUrl ); 
								
								pstmt.executeUpdate();
								
								pstmt.close();			
							}
							/*
							 * recupero la nuova lista di links
							 */
							ArrayList<Link> alLinks = new ArrayList<Link>();
							PreparedStatement pstmt = connDiogene.prepareStatement(SQL_SELECT_EXT_LINK);
							log.debug("SQL: " + SQL_SELECT_EXT_LINK);
							ResultSet rst = pstmt.executeQuery();						
							while(rst.next()) {
								Link link = new Link();
								
								link.setName(rst.getString("NAME"));
								link.setUrl(rst.getString("URL"));
								
								alLinks.add(link);
							}
							
							request.setAttribute("alLinks", alLinks);
							pstmt.close();	
							
						}else if (hAct != null && !hAct.trim().equalsIgnoreCase("") && hAct.trim().equalsIgnoreCase("DEL")){
							String linkId = request.getParameter("linkId");
							/*
							 * faccio delete se il nome non è nullo
							 */
							if ( !StringUtils.isEmpty(linkId) ){
								String sql = "DELETE FROM CFG_DIOG_EXT_LINK WHERE NAME = ? ";
								PreparedStatement pstmt = connDiogene.prepareStatement(sql);
								pstmt.setString(1, linkId );

								pstmt.executeUpdate();
								
								pstmt.close();			
							}
							/*
							 * recupero la nuova lista di links
							 */
							ArrayList<Link> alLinks = new ArrayList<Link>();
							PreparedStatement pstmt = connDiogene.prepareStatement(SQL_SELECT_EXT_LINK);
							log.debug("SQL: " + SQL_SELECT_EXT_LINK);
							ResultSet rst = pstmt.executeQuery();						
							while(rst.next()) {
								Link link = new Link();
								
								link.setName(rst.getString("NAME"));
								link.setUrl(rst.getString("URL"));
								
								alLinks.add(link);
							}
							
							request.setAttribute("alLinks", alLinks);
							pstmt.close();	
						}
					}

				}	
			}
			request.getRequestDispatcher(pagina).forward(request,response);
			
		}
		catch (it.escsolution.escwebgis.common.DiogeneException de)
		{
			log.debug(de);
			throw de;
		}
		catch (Exception ex)
		{
			log.debug(ex);
			log.error(ex.getMessage(),ex);
		}finally {
			try {
				if(connDbTotale != null)
					connDbTotale.close();

				if(connDiogene != null)
					connDiogene.close();
				
			} catch (SQLException e) {
				log.debug(e);
			}			
		}
	}

}
