package it.webred.servlet;

//import it.escsolution.escwebgis.common.EnvSource;
//import it.escsolution.escwebgis.common.EnvUtente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class GlobalParameters
{
//	public static int		RIGHE_CONFIGURATE_PER_PAGINA_DEF	= 10;
//	public static boolean	SALTA_LISTA_CON_UNO_DEF				= false;
//	
//	public static HashMap<String, Integer> RIGHE_CONFIGURATE_PER_PAGINA	= new HashMap<String, Integer>();
//	public static HashMap<String, Boolean> SALTA_LISTA_CON_UNO			= new HashMap<String, Boolean>();
//	public static HashMap<String, Boolean> DONE_CONFIG					= new HashMap<String, Boolean>();
//	public static HashMap<String, HashMap<String, String>> sitTabParams = new HashMap<String, HashMap<String, String>>();
//	
//	private static final Logger log = Logger.getLogger("diogene.log");
//
//	public static void loadWebredConfig(String ente)
//	{
//		if (DONE_CONFIG.get(ente) != null && DONE_CONFIG.get(ente).booleanValue())
//			return;
//		
//		log.debug("Inizio caricamento parametri configurazione");
//		Connection con = null;
//		ResultSet rs = null;
//		Statement stm = null;
//		try
//		{
//
//			// faccio la connessione al db
//			Context cont = new InitialContext();
//			EnvSource es = new EnvSource(ente);
//			DataSource theDataSource = (DataSource)cont.lookup(es.getDataSourceIntegrato());
//			con = theDataSource.getConnection();
//			stm = con.createStatement();
//			rs = stm.executeQuery("SELECT NOME, VALORE FROM WEBRED_CONFIGURAZIONE");
//			while(rs.next())
//			{
//				String nome = rs.getString("NOME");
//				if(nome != null && nome.equalsIgnoreCase("RIGHE_CONFIGURATE_PER_PAGINA"))
//					RIGHE_CONFIGURATE_PER_PAGINA.put(ente, new Integer(Integer.parseInt(rs.getString("VALORE"))));
//				else if(nome != null && nome.equalsIgnoreCase("SALTA_LISTA_CON_UNO"))
//					SALTA_LISTA_CON_UNO.put(ente, new Boolean(rs.getString("VALORE") != null && rs.getString("VALORE").equalsIgnoreCase("TRUE")?true:false));
//			}
//			DONE_CONFIG.put(ente, new Boolean(true));			
//		}
//		catch (Exception ex)
//		{
//		}
//		finally
//		{
//			try
//			{
//				if (stm != null)
//					stm.close();
//				if (rs != null)
//					rs.close();
//				if (con != null)
//					con.close();
//			}
//			catch (SQLException e)
//			{
//			}
//		}
//	}
//
//	
//	public static String getParameter(EnvUtente eu, String nomeP) throws Exception  
//	{
//		log.debug("Richiesto parametro utente " + eu.getUtente().getName() + " - " + nomeP);
//		Connection con = null;
//		ResultSet rs = null;
//		PreparedStatement stm = null;
//
//		
//		String utente = eu.getUtente().getName();
//		String nomeApplicazione = eu.getNomeApplicazione();
//		try
//		{
//
//			String ente = eu.getEnte();
//			//non serve recuperare UK_BELFIORE da EWG_TAB_COMUNI_UTENTI... è già in eu...
//			/*
//			// faccio la connessione al db
//			Context cont = new InitialContext();
//			Context datasourceContext = (Context) cont.lookup("java:comp/env");
//			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/dbIntegrato" + "_" + eu.getEnte());
//			con = theDataSource.getConnection();
//			stm = con.prepareStatement("SELECT * FROM EWG_TAB_COMUNI_UTENTI WHERE UTENTE = ? AND APPLICAZIONE =?");
//			stm.setString(1, utente);
//			stm.setString(2, nomeApplicazione);
//			rs = stm.executeQuery();
//			if (rs.next())
//				ente =  rs.getString("UK_BELFIORE");
//				
//			if (ente==null)
//				throw new Exception("Impossibile reperire ente per utente");
//			*/
//			
//			if (sitTabParams == null || sitTabParams.size() == 0 || sitTabParams.get(ente) == null) {
//				loadSitTabParams(eu);
//			}
//			
//			return sitTabParams.get(ente).get(nomeP);
//			
//		}
//		catch (Exception ex)
//		{
//			throw new Exception(ex);
//		}
//		finally
//		{
//			try
//			{
//				if (stm != null)
//					stm.close();
//				if (rs != null)
//					rs.close();
//				if (con != null)
//					con.close();
//			}
//			catch (SQLException e)
//			{
//			}
//		}
//	}
//	
//	private static void loadSitTabParams(EnvUtente eu) {
//		
//		sitTabParams = new HashMap<String, HashMap<String, String>>();
//		
//		Connection con = null;
//		ResultSet rs = null;
//		Statement stm = null;
//		try
//		{
//			// faccio la connessione al db
//			Context cont = new InitialContext();
//			EnvSource es = new EnvSource(eu.getEnte());
//			DataSource theDataSource = (DataSource)cont.lookup(es.getDataSource());
//			con = theDataSource.getConnection();
//			stm = con.createStatement();
//			rs = stm.executeQuery("SELECT SIT_ENTE.CODENT CODENT, NOME, VALORE FROM SIT_TAB_PARAMS LEFT JOIN SIT_ENTE ON ID_ENTE = ID");
//			while(rs.next())
//			{
//				String nome = rs.getString("NOME");
//				String valore = rs.getString("VALORE");
//				String ente = rs.getString("CODENT");
//				
//				if (sitTabParams.get(ente) == null) 
//				{
//					sitTabParams.put(ente, new HashMap<String, String>());
//				}
//				if (nome!=null)
//					sitTabParams.get(ente).put(nome, valore);
//			}
//		}
//		catch (Exception ex)
//		{
//			log.error("Errori durante il caricamento di parametri" , ex);
//		}
//		finally
//		{
//			try
//			{
//				if (stm != null)
//					stm.close();
//				if (rs != null)
//					rs.close();
//				if (con != null)
//					con.close();
//			}
//			catch (SQLException e)
//			{
//			}
//		}
//	}
//	
}
