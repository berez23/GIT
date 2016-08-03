package it.escsolution.escwebgis.imu.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.imu.bean.ConsulenzaBean;
import it.escsolution.escwebgis.imu.bean.SaldoImuFinder;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.data.access.basic.imu.SaldoImuService;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuBaseDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuConsulenzaDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuElaborazioneDTO;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ConsulenzaImuLogic extends EscLogic {
	
	public final static String LISTA= "LISTA@ConsulenzaImuLogic";
	public final static String FINDER = "FINDER124";
	public final static String IMU_CONS = "IMU_CONS@ConsulenzaImuLogic";
	public final static String IMU_ELAB = "IMU_ELAB@ConsulenzaImuLogic";
	 
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	
	private final static String SQL_SELECT_LISTA = "select * from ( select rownum as N, " +
			"(S.CODFISC||'|'||s.progressivo) as id, s.* " +
			"from (select codfisc, max(progressivo) prog from saldo_imu_storico group by codfisc) validi, saldo_imu_storico s " +
			"where validi.codfisc=S.CODFISC and validi.prog=s.progressivo and 1=? ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from ( "+
			"select rownum as N, (S.CODFISC||'|'||s.progressivo) as id, s.* from (select codfisc, max(progressivo) prog from saldo_imu_storico group by codfisc) validi, saldo_imu_storico s "+
			"where validi.codfisc=S.CODFISC and validi.prog=s.progressivo and 1=? ";
	

	public ConsulenzaImuLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, SaldoImuFinder finder) throws Exception {

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
					sql = sql + " and S.CODFISC||'|'||S.PROGRESSIVO in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by S.CODFISC) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						ConsulenzaBean v = new ConsulenzaBean();
						v.setCf(rs.getString("CODFISC"));
						v.setChiave(rs.getString("ID"));
						v.setDtConsulenza(rs.getDate("DT_INS"));
						v.setCognome(rs.getString("COGNOME"));
						v.setNome(rs.getString("NOME"));
						vct.add(v);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA, vct);
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
			log.error("Errore mCaricareLista: "+e.getMessage(),e);
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
		
		try {
			if(chiave != null && !chiave.equals("")) {
			
				String[] c = chiave.split("\\|");
				this.caricaConsulenzaImu(c[0],ht);
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
			log.error("Errore mCaricareDettaglio: "+e.getMessage(),e);
			throw e;
		}
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	public void caricaConsulenzaImu(String cf, Hashtable ht) throws NamingException{
		
		SaldoImuConsulenzaDTO consulenza = null;
		SaldoImuElaborazioneDTO datiElab = null;
		
		if(cf!=null){
			Connection conn = null;
			EnvUtente eu = this.getEnvUtente();
			String enteId = eu.getEnte();
			String userId = eu.getUtente().getUsername();
			String sessionId = eu.getUtente().getSessionId();
			
			//se il tema ha associata una fonte, allora lo visualizzo solo se ho il permesso su quella fonte (e se la fonte è abilitata per l'ente)
			boolean fontePermessa = GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Fonte:IMU",true);
			 if(fontePermessa && verificaFonteComune(eu.getEnte(), "IMU")){
					
				SaldoImuService imu= (SaldoImuService)getEjb("CT_Service", "CT_Service_Data_Access", "SaldoImuServiceBean");
				
				SaldoImuBaseDTO search = new SaldoImuBaseDTO();
				search.setEnteId(enteId);
				search.setUserId(userId);
				search.setSessionId(sessionId);
				search.setCodfisc(cf);
				
				consulenza = imu.getConsulenza(search);
				if(consulenza!=null)
					datiElab = imu.getJsonDTOUltimaElaborazione(search);
				 }
			 
		}
		
		if(consulenza!=null)
			ht.put(this.IMU_CONS, consulenza);
		
		if(datiElab!=null)
			ht.put(this.IMU_ELAB, datiElab);
		
	}
	
	public Hashtable mCaricareDettaglioFromOggetto(String chiave) throws Exception{
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
	
		try {
			if(chiave != null && !chiave.equals("")) {
				
				String[] c = chiave.split("\\|");
				this.caricaConsulenzaImu(c[0],ht);
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
			log.error("Errore mCaricareDettaglioFromOggetto: "+e.getMessage(),e);
			throw e;
		}
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		
	}
	
}
