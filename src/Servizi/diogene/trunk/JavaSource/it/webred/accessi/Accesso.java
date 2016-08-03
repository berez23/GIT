package it.webred.accessi;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscServlet;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.GestionePermessi;
import it.webred.utils.DateFormat;

import org.apache.log4j.Logger;

public class Accesso {


	private static final Logger log = Logger.getLogger("diogene.log");

	/**
	 * @param messaggio
	 * @param eu
	 * @param ab
	 * @return boolean
	 * 
	 * Nel caso il permesso venga accordato viene prodotto un log che registra l'acesso alla risorsa
	 */
	public static boolean isAutorizzato(EnvUtente eu, AccessoBean ab ) {
		boolean gp = GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), ab.getItem(), ab.getPermesso());
		log.info("AUTORIZZAZIONE=" + eu.getUtente().getName() + "-" + eu.getNomeApplicazione() + "-" +  ab.getItem() + "-" + ab.getPermesso() + "=" + gp);
		return gp;
	}
	
	public static void registraLog(String messaggio, EnvUtente eu, String nomeRisorsa, String tipoLog, String parametri ) {
		// non loggo le query di infrastruttura
		if (nomeRisorsa.equals("it.escsolution.escwebgis.common.EscLogic"))
			return;

		Connection conn = null;
		try {
			EnvSource es = new EnvSource(eu.getEnte());
			//conn = getConnection(EscServlet.defaultDataSource + "_" + es.getDataSourceId());
			Context ctx = new InitialContext();
			if (ctx == null)
				throw new Exception("Boom - No Context");
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			conn = ds.getConnection();
			CallableStatement cs = conn.prepareCall("Insert into sit_log_accessi (CUR_TIME,UTENTE, NOME_RISORSA,MESSAGGIO,TIPO_LOG,PARAMETRI) values (?,?,?,?,?,?)");
			cs.setString(1, DateFormat.dateToString(new Date(System.currentTimeMillis()),"yyyyMMddHHmmss"));
			cs.setString(2, eu.getUtente().getName());
			cs.setString(3, nomeRisorsa);
			cs.setString(4, messaggio);
			cs.setString(5, tipoLog);
			cs.setString(6, parametri);
			cs.execute();
		} catch (Exception e) {
			log.error("Errore nella registrazione del log accessi:" + e.getMessage());
		} finally {
			if (conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		
	}
	
	
	private static Connection getConnection(String datasource) throws NamingException, SQLException
	{
		Context cont = new InitialContext();
		//Context datasourceContext = (Context) cont.lookup("java:comp/env");
		//DataSource theDataSource = null;
		//theDataSource = (DataSource) datasourceContext.lookup(datasource);
		DataSource theDataSource = (DataSource)cont.lookup("java:" + datasource);

		return theDataSource.getConnection();
	}
	
	
}
