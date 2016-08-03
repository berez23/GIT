package it.escsolution.escwebgis.f24.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.f24.bean.F24Annullamento;
import it.escsolution.escwebgis.f24.bean.F24ComboObject;
import it.escsolution.escwebgis.f24.bean.F24Finder;
import it.webred.ct.data.access.basic.f24.F24Service;
import it.webred.ct.data.access.basic.f24.dto.F24AnnullamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.F24VersamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.RicercaF24DTO;
import it.webred.ct.data.access.basic.f24.dto.TipoTributoDTO;

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

public class F24AnnullamentoLogic extends EscLogic {
	
	public final static String LISTA= "LISTA_ANNULLAMENTO@F24AnnullamentoLogic";
	public final static String FINDER = "FINDER123";
	public final static String ANNULLAMENTO ="F24ANNULLAMENTO@F24AnnullamentoLogic";	
	public static final String LISTA_VERSAMENTI = "F24VERSAMENTI@F24AnnullamentoLogic";	
	
	public final static String LISTA_ANN_F24_CF= "LISTA_ANNULLAMENTI_F24@VersamentiAllLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
	"select DISTINCT T.*, cod.descrizione desc_tributo  " +
	"from SIT_T_F24_ANNULLAMENTO T, sit_t_cod_tributo cod " +
	"where t.cod_tributo = cod.codice(+) and 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( " +
			"select DISTINCT T.*, cod.descrizione desc_tributo  " +
			"from SIT_T_F24_ANNULLAMENTO T, sit_t_cod_tributo cod " +
			"where t.cod_tributo = cod.codice(+) and 1=? ";

	
	
	
	public F24AnnullamentoLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, F24Finder finder) throws Exception {

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
					sql = sql + " and SIT_T_F24_ANNULLAMENTO.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by TO_NUMBER(T.ANNO_RIF), T.DT_RISCOSSIONE, T.CF, T.COD_TRIBUTO ) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						F24Annullamento v = new F24Annullamento();
						v.setChiave(rs.getString("ID"));
						v.setDtFornitura(SDF.format(rs.getDate("DT_FORNITURA")));
						v.setProgFornitura(rs.getString("PROG_FORNITURA"));
						v.setDtRipartizioneOrig(SDF.format(rs.getDate("DT_RIPART_ORIG")));
						v.setProgRipartizioneOrig(rs.getString("PROG_RIPART_ORIG"));
						v.setDtBonificoOrig(SDF.format(rs.getDate("DT_BONIFICO_ORIG")));
						v.setCodEnte(rs.getString("COD_ENTE_RD"));
						v.setCodFisc(rs.getString("CF"));
						v.setDtRiscossione(SDF.format(rs.getDate("DT_RISCOSSIONE")));
						v.setCodTributo(rs.getString("COD_TRIBUTO"));
						v.setDescTributo(rs.getString("DESC_TRIBUTO"));
						v.setAnnoRif(rs.getString("ANNO_RIF"));
						BigDecimal impCredito = rs.getBigDecimal("IMP_CREDITO").divide(new BigDecimal(100));
						v.setImpCredito(DF.format(impCredito.doubleValue()));
						BigDecimal impDebito = rs.getBigDecimal("IMP_DEBITO").divide(new BigDecimal(100));
						v.setImpDebito(DF.format(impDebito.doubleValue()));
						v.setTipoOperazione(rs.getString("TIPO_OPERAZIONE"));
						v.setDtOperazione(SDF.format(rs.getDate("DT_OPERAZIONE")));
						v.setTipoImposta(rs.getString("TIPO_IMPOSTA"));
						
						vct.add(v);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(this.LISTA, vct);
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
	
	public Vector<F24ComboObject> getListaTipoTributo() throws Exception {
		
		Vector<F24ComboObject> vec = new Vector<F24ComboObject>();
		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try {
							
				F24Service serviziof24= (F24Service)getEjb("CT_Service", "CT_Service_Data_Access", "F24ServiceBean");
				
				RicercaF24DTO rs = new RicercaF24DTO();
				rs.setEnteId(enteId);
				rs.setSessionId(sessionId);
				rs.setUserId(userId);
				
				List<TipoTributoDTO> lstTipiTributo = serviziof24.getListaDescTipoTributo(rs);
				F24ComboObject kv = new F24ComboObject("","Tutti");
				vec.add(kv);
				for(TipoTributoDTO dto: lstTipiTributo){
					kv = new F24ComboObject(dto.getDescrizione(),dto.getDescrizione());
					vec.add(kv);
				}
			
		}
		catch (Exception e) {
			throw e;
		}
		return vec;
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
				
				F24Service serviziof24= (F24Service)getEjb("CT_Service", "CT_Service_Data_Access", "F24ServiceBean");
				
				RicercaF24DTO rs = new RicercaF24DTO();
				rs.setEnteId(enteId);
				rs.setSessionId(sessionId);
				rs.setUserId(userId);
				rs.setId(chiave);
				
				F24AnnullamentoDTO dettaglio = serviziof24.getAnnullamentoById(rs);
				
				List<F24VersamentoDTO> listaVers = new ArrayList<F24VersamentoDTO>();
				
				if(dettaglio!=null){
					rs.setCodEnte(dettaglio.getCodEnteRd());
					rs.setDtRipartizione(dettaglio.getDtRipartizione());
					rs.setProgRipartizione(dettaglio.getProgRipartizione());
					rs.setDtBonifico(dettaglio.getDtBonifico());
					rs.setAnnoRif(dettaglio.getAnnoRif());
					rs.setCf(dettaglio.getCf());
					rs.setCodTributo(dettaglio.getCodTributo());
					rs.setDtRiscossione(dettaglio.getDtRiscossione());
					
					listaVers = serviziof24.getListaVersamentiByAnn(rs);
				
				}
				
				
				
				ht.put(F24AnnullamentoLogic.ANNULLAMENTO, dettaglio);
				ht.put(F24AnnullamentoLogic.LISTA_VERSAMENTI, listaVers);
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
	
	public Hashtable mCaricareDettaglioFromOggetto(String chiave) throws Exception{
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				F24Service serviziof24= (F24Service)getEjb("CT_Service", "CT_Service_Data_Access", "F24ServiceBean");
				
				RicercaF24DTO rs = new RicercaF24DTO();
				rs.setEnteId(enteId);
				rs.setSessionId(sessionId);
				rs.setUserId(userId);
				rs.setId(chiave);
				
				F24AnnullamentoDTO dettaglio = serviziof24.getAnnullamentoById(rs);
				
				ht.put(F24AnnullamentoLogic.ANNULLAMENTO, dettaglio);
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
	
	public Hashtable mCaricareListaAnnByCf(String cf) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try {
			if(cf != null && !cf.equals("")) {
				
				Context cont = new InitialContext();
				F24Service serviziof24= (F24Service) getEjb("CT_Service", "CT_Service_Data_Access","F24ServiceBean" ) ; 
				
				RicercaF24DTO rs = new RicercaF24DTO();
				rs.setEnteId(enteId);
				rs.setSessionId(sessionId);
				rs.setUserId(userId);
				rs.setCf(cf);
				
				List<F24AnnullamentoDTO> lst = serviziof24.getListaAnnullamentiByCF(rs);
				
				ht.put(this.LISTA_ANN_F24_CF, lst);
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
