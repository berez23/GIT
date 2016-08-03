package it.escsolution.escwebgis.ruolo.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.ruolo.bean.RuoloFinder;
import it.webred.ct.data.access.basic.ruolo.RuoloDataIn;
import it.webred.ct.data.access.basic.ruolo.tarsu.RTarsuService;
import it.webred.ct.data.access.basic.ruolo.tarsu.dto.RuoloTarsuDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class RuoloTarsuLogic extends EscLogic {
	
	private static final Logger log = Logger.getLogger(RuoloTarsuLogic.class.getName());
	
	public final static String LISTA_RUOLI= "LISTA_RUOLI@RuoloTarsuLogic";
	public final static String FINDER = "FINDER128";
	public final static String RUOLO = "RUOLO@RuoloTarsuLogic";	
	
	public final static String LISTA_RUOLI_CF= "LISTA_RUOLI_CF@RuoloTarsuLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
	"select DISTINCT T.*  " +
	"from SIT_RUOLO_TARSU T " +
	"where 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( " +
			"select DISTINCT T.* " +
			"from SIT_RUOLO_TARSU T " +
			"where 1=? ";
	

	public RuoloTarsuLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, RuoloFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
			for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;
				
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					sql = sql + " and SIT_RUOLO_TARSU.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.CODFISC, T.ANNO, T.TIPO) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					
					EnvUtente eu = this.getEnvUtente();
					String enteId = eu.getEnte();
					String userId = eu.getUtente().getUsername();
					String sessionId = eu.getUtente().getSessionId();
					Context cont = new InitialContext();
					RTarsuService servizio= (RTarsuService)getEjb("CT_Service","CT_Service_Data_Access" , "RTarsuServiceBean"); 
					
					RuoloDataIn dataIn = new RuoloDataIn();
					dataIn.setEnteId(enteId);
					dataIn.setSessionId(sessionId);
					dataIn.setUserId(userId);
					dataIn.setRicercaVersamenti(false);
					
					while (rs.next()){
						String chiave = rs.getString("ID");
						dataIn.setId(chiave);
						RuoloTarsuDTO dettaglio = servizio.getRuoloById(dataIn);
						vct.add(dettaglio);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_RUOLI, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = listaPar;
				arguments[1] = finder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				Context cont = new InitialContext();
				
				RTarsuService servizio= (RTarsuService)getEjb("CT_Service","CT_Service_Data_Access" , "RTarsuServiceBean"); 
				
				RuoloDataIn rs = new RuoloDataIn();
				rs.setEnteId(enteId);
				rs.setSessionId(sessionId);
				rs.setUserId(userId);
				rs.setRicercaVersamenti(true);
				rs.setId(chiave);
				
				RuoloTarsuDTO dettaglio = servizio.getRuoloById(rs);
				
				ht.put(RuoloTarsuLogic.RUOLO, dettaglio);
				
			}
			
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
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	public Hashtable mCaricareDettaglioFromImm(String chiave) throws Exception{
		
		Hashtable ht = new Hashtable();
		ResultSet rs = null;
		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		RuoloTarsuDTO dettaglio = null;
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				Context cont = new InitialContext();
				
				String sql = "select r.id from sit_ruolo_tarsu r, sit_ruolo_tarsu_imm i " +
					         "where r.id_ext=i.id_ext_ruolo and i.id='"+chiave+"'";
				conn = this.getConnectionDiogene();
				rs = conn.createStatement().executeQuery(sql);
				String idR = null;
				if(rs.next()){
					idR = rs.getString("ID");
				
					RTarsuService servizio= (RTarsuService) getEjb("CT_Service","CT_Service_Data_Access" , "RTarsuServiceBean"); 
					RuoloDataIn dataIn = new RuoloDataIn();
					dataIn.setEnteId(enteId);
					dataIn.setSessionId(sessionId);
					dataIn.setUserId(userId);
					dataIn.setId(idR);
					
					dettaglio = servizio.getRuoloById(dataIn);
				}
				ht.put(RuoloTarsuLogic.RUOLO, dettaglio);
			}
			
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
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		
	}
	
	
	public Hashtable mCaricareListaRuoliByCF(String cf) throws Exception{
		
		Hashtable ht = new Hashtable();
		ResultSet rs = null;
		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		RuoloTarsuDTO dettaglio = null;
		
		try {
			if(cf != null && !cf.equals("")) {
				
				Context cont = new InitialContext();
				
				RTarsuService servizio= (RTarsuService)getEjb("CT_Service","CT_Service_Data_Access" , "RTarsuServiceBean"); 
				RuoloDataIn dataIn = new RuoloDataIn();
				dataIn.setEnteId(enteId);
				dataIn.setSessionId(sessionId);
				dataIn.setUserId(userId);
				dataIn.setRicercaVersamenti(true);
				dataIn.setCf(cf);
				
				List<RuoloTarsuDTO> lst = servizio.getListaRuoliByCodFis(dataIn);
				
				ht.put(RuoloTarsuLogic.LISTA_RUOLI_CF, lst);
			}
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = cf;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		
	}
	
}

