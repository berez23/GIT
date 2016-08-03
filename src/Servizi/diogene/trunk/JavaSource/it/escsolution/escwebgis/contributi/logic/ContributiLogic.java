package it.escsolution.escwebgis.contributi.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.contributi.bean.Contributi;
import it.escsolution.escwebgis.contributi.bean.ContributiSib;

public class ContributiLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public ContributiLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}
	
	public static final String FINDER = "FINDER";
	public static final String CONTRIBUTI = ContributiLogic.class.getName() + "@CONTRIBUTIKEY";
	public static final String LISTA = "LISTA_CONTRIBUTI";
	public static final String DETTAGLIO = "DETTAGLIO_CONTRIBUTI";
	
	private final static String SQL_SELECT_DETTAGLIO_CONTR = "SELECT * FROM CONTRIBUTI_SIB " +
			"WHERE CODICE_FISCALE = ? " +
			"AND ANNO_RUOLO = ? " +
			"ORDER BY SETTORE, SERVIZIO, UFFICIO, PARTITA, N_IDENT";
	
	
	public Hashtable mCaricareDettaglio(String chiave, HttpServletRequest request) throws Exception {	
		Hashtable ht = new Hashtable();
		Connection conn = null;		
		try {
			conn = this.getConnection();
			this.initialize();
			
			String[] params = chiave.split("_");
			
			String sql = SQL_SELECT_DETTAGLIO_CONTR;			
			this.setString(1, params[0]);
			this.setString(2, params[1]);
			
			Contributi contr = new Contributi();
			
			if (contr.getDatiTestata() == null) {
				contr.setDatiTestata(new LinkedHashMap<String, String>());
			}
			contr.getDatiTestata().put("Codice Fiscale", params[0]);
			contr.getDatiTestata().put("Anno Ruolo", params[1]);			
			
			prepareStatement(sql);
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()) {
				ContributiSib contrSib = new ContributiSib();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) { //base 1
					String key = rsmd.getColumnName(i);
					Object value = rs.getObject(key);
					if (contrSib.getDati() == null) {
						contrSib.setDati(new LinkedHashMap<String, Object>());
					}
					contrSib.getDati().put(key, value);
				}
				if (contr.getContributiSib() == null) {
					contr.setContributiSib(new ArrayList<ContributiSib>());
				}
				contr.getContributiSib().add(contrSib);
			}
			
			ht.put(DETTAGLIO, contr);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = chiave;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

}
