package it.escsolution.escwebgis.versamenti.iciDM.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.versamenti.bean.VersComboObject;
import it.escsolution.escwebgis.versamenti.bean.VersFinder;
import it.escsolution.escwebgis.versamenti.logic.VersamentiAllLogic;
import it.webred.ct.data.access.basic.versamenti.iciDM.VersIciDmService;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDataIn;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.VersamentoIciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.ViolazioneIciDmDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class VersIciDmLogic extends EscLogic {
	
	private static final Logger log = Logger.getLogger(VersIciDmLogic.class.getName());
	
	public final static String FINDER = "FINDER130";

	public final static String VERSAMENTO = "VERSAMENTO@VersIciDmLogic";	
	public final static String VIOLAZIONE = "VIOLAZIONE@VersIciDmLogic";	
	public final static String LISTA_VERSAMENTI= "LISTA_VERSAMENTI@VersIciDmLogic";
	
	//Liste usate nel dettaglio di VersamentiAll
	public final static String LISTA_VERS_ICI_DM= "LISTA_VERSAMENTI_ICI_DM@VersIciDmLogic";
	public final static String LISTA_VIOL_ICI_DM= "LISTA_VIOLAZIONI_ICI_DM@VersIciDmLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
		"select * from (select 'VER@'||v.id as ID, V.CF_VERSANTE AS CODFISC, V.DT_VERSAMENTO, v.imp_versato, 'VER' as TIPO from sit_t_ici_dm_vers v "+
		"union all "+ 
		"select 'VIO@'||v.id as ID, V.CF_VERSANTE AS CODFISC, V.DT_VERSAMENTO, v.imp_versato, 'VIO' as TIPO from sit_t_ici_dm_violazione v) t " +
		"where 1=? ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( " +
			"select * from (select v.id, V.CF_VERSANTE AS CODFISC, V.DT_VERSAMENTO, v.imp_versato, 'V' as TIPO from sit_t_ici_dm_vers v "+
			"union all "+ 
			"select v.id, V.CF_VERSANTE AS CODFISC, V.DT_VERSAMENTO, v.imp_versato, 'D' as TIPO from sit_t_ici_dm_violazione v) t " +
			"where 1=? ";
	

	public VersIciDmLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, VersFinder finder) throws Exception {

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
					sql = sql + " and T.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.CODFISC, T.DT_VERSAMENTO) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						IciDmDTO dto = new IciDmDTO();
						dto.setId(rs.getString("ID"));
						dto.setCfVersante(rs.getString("CODFISC"));
						dto.setDtVersamento(rs.getDate("DT_VERSAMENTO"));
						dto.setImpVersato(rs.getBigDecimal("IMP_VERSATO"));
						dto.setTipoTab(rs.getString("TIPO"));
						vct.add(dto);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_VERSAMENTI, vct);
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
				VersIciDmService servizio= (VersIciDmService)getEjb("CT_Service","CT_Service_Data_Access" , "VersIciDmServiceBean");
				
				IciDmDataIn dataIn = new IciDmDataIn();
				dataIn.setEnteId(enteId);
				dataIn.setSessionId(sessionId);
				dataIn.setUserId(userId);
				
				String s[] = chiave.split("@");
				
				String tipo = null;
				if(s.length==2){
					tipo = s[0];
					chiave = s[1];
				}
				dataIn.setId(chiave);
				
				if(tipo==null || "VER".equals(tipo)){
					VersamentoIciDmDTO dettaglioVer = servizio.getVersamentoById(dataIn);
					if(dettaglioVer!=null)
						ht.put(VersIciDmLogic.VERSAMENTO, dettaglioVer);
				}
				if(tipo==null || "VIO".equals(tipo)){
					ViolazioneIciDmDTO dettaglioVio = servizio.getViolazioneById(dataIn);
					if(dettaglioVio!=null)
						ht.put(VersIciDmLogic.VIOLAZIONE, dettaglioVio);
				}
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
	
	public Hashtable mCaricareListeByCf(String cf) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try {
			if(cf != null && !cf.equals("")) {
				
				Context cont = new InitialContext();
				VersIciDmService servizio= (VersIciDmService)getEjb("CT_Service","CT_Service_Data_Access" , "VersIciDmServiceBean");
				
				IciDmDataIn dataIn = new IciDmDataIn();
				dataIn.setEnteId(enteId);
				dataIn.setSessionId(sessionId);
				dataIn.setUserId(userId);
				dataIn.setCf(cf);
				
				List<VersamentoIciDmDTO> lst1 = servizio.getListaVersamentiByCodFis(dataIn);	
				ht.put(VersIciDmLogic.LISTA_VERS_ICI_DM, lst1);
				
				List<ViolazioneIciDmDTO> lst2 = servizio.getListaViolazioniByCodFis(dataIn);
				ht.put(VersIciDmLogic.LISTA_VIOL_ICI_DM, lst2);
			
			}
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
