package it.escsolution.escwebgis.f24.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.f24.bean.F24ComboObject;
import it.escsolution.escwebgis.f24.bean.F24Finder;
import it.escsolution.escwebgis.f24.bean.F24Versamento;
import it.escsolution.escwebgis.versamenti.iciDM.logic.VersIciDmLogic;
import it.webred.ct.data.access.basic.f24.F24Service;
import it.webred.ct.data.access.basic.f24.dto.F24AnnullamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.F24VersamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.RicercaF24DTO;
import it.webred.ct.data.access.basic.f24.dto.TipoTributoDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.VersIciDmService;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDataIn;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.VersamentoIciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.ViolazioneIciDmDTO;
import it.webred.utils.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class F24VersamentiLogic extends EscLogic {
		
	public final static String LISTA_VERSAMENTI= "LISTA_VERSAMENTI@F24VersamentiLogic";
	public final static String FINDER = "FINDER122";
	public final static String VERSAMENTO = "F24VERSAMENTI@F24VersamentiLogic";	
	public final static String LISTA_ANNULLAMENTI ="F24ANNULLAMENTO@F24VersamentiLogic";
	public final static String SQL_TO_EXPORT ="SQL_TO_EXPORT@F24VersamentiLogic";
	//Liste usate nel dettaglio di VersamentiAll
	public final static String LISTA_VERS_F24_CF= "LISTA_VERSAMENTI_F24@VersamentiAllLogic";
	public final static String FILTRO_RICERCA_F24VERSAMENTI = "FILTRO_RICERCA_F24VERSAMENTI@F24VersamentiLogic";
	
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	private final static String SQL_SELECT_LISTA_PREFIX = "select * from ( select ROWNUM as N, Q.* from ( ";

	private final static String SQL_SELECT_COUNT_PREFIX = "select count(*) as conteggio FROM ( ";
	
//	private final static String SQL_SELECT_LISTA = "select * from (" +
//	"select ROWNUM as N, Q.* from (" +
//	"select DISTINCT T.*, cod.descrizione desc_tributo, sogg.descrizione desc_cf2  " +
//	"from SIT_T_F24_VERSAMENTI T, sit_t_cod_tributo cod, sit_t_f24_cod_sogg sogg " +
//	"where sogg.codice(+)=t.cod_id_cf2 AND t.cod_tributo = cod.codice(+) and 1=?";
//		
//	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( " +
//			"select DISTINCT T.*, cod.descrizione desc_tributo, sogg.descrizione desc_cf2  " +
//			"from SIT_T_F24_VERSAMENTI T, sit_t_cod_tributo cod, sit_t_f24_cod_sogg sogg " +
//			"where sogg.codice(+)=t.cod_id_cf2 AND t.cod_tributo = cod.codice(+) and 1=? ";
	
	private final static String SQL_SELECT = "select DISTINCT T.*, cod.descrizione desc_tributo, sogg.descrizione desc_cf2, "
			+ " decode(T.TIPO_IMPOSTA, 'I', 'ICI/IMU', 'O', 'TOSAP/COSAP', 'T', 'TARSU/TARIFFA', 'S', 'TASSA DI SCOPO', 'R', 'IMPOSTA DI SOGGIORNO', 'U', 'TASI', 'A', 'TARES', 'AC', 'ADDIZIONALE COMUNALE' ) as DESC_TIPO_IMPOSTA " +
			"from SIT_T_F24_VERSAMENTI T, sit_t_cod_tributo cod, sit_t_f24_cod_sogg sogg " +
			"where sogg.codice(+)=t.cod_id_cf2 AND t.cod_tributo = cod.codice(+) and 1=? ";

	private final static String SQL_ORDER_BY = " order by T.DT_BONIFICO, T.PROG_DELEGA, T.PROG_RIGA, TO_NUMBER(T.ANNO_RIF), T.CF, T.COD_TRIBUTO ";

	public F24VersamentiLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}//-------------------------------------------------------------------------
	
	
	public Hashtable mCaricareLista(Vector listaPar, F24Finder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		String sqlToExport = "";
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
			for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
//				if (i==0)
//					sql = SQL_SELECT_COUNT_PREFIX + SQL_SELECT;
//				else
//					sql = SQL_SELECT_LISTA_PREFIX + SQL_SELECT;
				sql = SQL_SELECT;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;
				
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					sql = sql + " and SIT_T_F24_VERSAMENTI.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) {
					sqlToExport = sql + SQL_ORDER_BY;
					sql = sql + SQL_ORDER_BY + " ) Q) where N > " + limInf + " and N <=" + limSup;
					sql = SQL_SELECT_LISTA_PREFIX + sql;
				}else{
					sql += ")";
					sql = SQL_SELECT_COUNT_PREFIX + sql;
				}
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						F24Versamento v = new F24Versamento();
						v.setChiave(rs.getString("ID"));
						v.setDtFornitura(SDF.format(rs.getDate("DT_FORNITURA")));
						v.setProgFornitura(rs.getString("PROG_FORNITURA"));
						v.setDtRipartizione(SDF.format(rs.getDate("DT_RIPARTIZIONE")));
						v.setProgRipartizione(rs.getString("PROG_RIPARTIZIONE"));
						v.setDtBonifico(SDF.format(rs.getDate("DT_BONIFICO")));
						String cf = rs.getString("CF");
						v.setCodFisc( StringUtils.isEmpty(cf)?cf:""  );
						String codEnteRd = rs.getString("COD_ENTE_RD"); 
						v.setCodEnte( StringUtils.isEmpty(codEnteRd)?codEnteRd:"" );
						v.setAnnoRif(rs.getString("ANNO_RIF"));
						BigDecimal impCredito = rs.getBigDecimal("IMP_CREDITO").divide(new BigDecimal(100));
						v.setImpCredito(DF.format(impCredito.doubleValue()));
						BigDecimal impDebito = rs.getBigDecimal("IMP_DEBITO").divide(new BigDecimal(100));
						v.setImpDebito(DF.format(impDebito.doubleValue()));
						v.setCodTributo(rs.getString("COD_TRIBUTO"));
						v.setDescTributo(rs.getString("DESC_TRIBUTO"));
						v.setTipoImposta(rs.getString("TIPO_IMPOSTA"));
						v.setAcconto(rs.getInt("ACCONTO"));
						v.setSaldo(rs.getInt("SALDO"));
						v.setRavvedimento(rs.getInt("RAVVEDIMENTO"));
						v.setCf2(rs.getString("CF2"));
						v.setCodCf2(rs.getString("COD_ID_CF2"));
						String descCf2 = rs.getString("DESC_CF2");
						v.setDescCodCf2( StringUtils.isEmpty(descCf2)?descCf2:"" );
						Date dtRiscossione = rs.getDate("DT_RISCOSSIONE");
						v.setDtRiscossione( dtRiscossione!=null?SDF.format(dtRiscossione):"" );
						v.setProgDelega(rs.getString("PROG_DELEGA"));
						v.setProgRiga(rs.getString("PROG_RIGA"));
						String tipoEnteRd = rs.getString("TIPO_ENTE_RD"); 
						v.setTipoEnte( StringUtils.isEmpty(tipoEnteRd)?tipoEnteRd:"" );
						String cab = rs.getString("CAB"); 
						v.setCab( StringUtils.isEmpty(cab)?cab:"" );
						BigDecimal detrazione = rs.getBigDecimal("DETRAZIONE");
						v.setDetrazione( detrazione!=null?DF.format(detrazione.divide(new BigDecimal(100)).doubleValue()):"0,00");
						vct.add(v);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(SQL_TO_EXPORT, sqlToExport);
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

				/*
				 * E' stata fatta questa modifica perchè la maschera di filtro diogene 
				 * dei versamenti F24 faceva match sulla descrizione del Tipo Tributo
				 * invece che usare il codice
				 */
				//List<TipoTributoDTO> lstTipiTributo = serviziof24.getListaDescTipoTributo(rs); 
				List<TipoTributoDTO> lstTipiTributo = serviziof24.getListaCodTipoTributo(rs); 
				F24ComboObject kv = new F24ComboObject("","Tutti");
				vec.add(kv);
				for(TipoTributoDTO dto: lstTipiTributo){
					kv = new F24ComboObject(dto.getCodice(),dto.getDescrizione());
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
				
				F24VersamentoDTO dettaglio = serviziof24.getVersamentoById(rs);
				
				List<F24AnnullamentoDTO> listaAnnullamenti = new ArrayList<F24AnnullamentoDTO>();
				
				if(dettaglio!=null){
					rs.setCodEnte(dettaglio.getCodEnteRd());
					rs.setDtRipartizione(dettaglio.getDtRipartizione());
					rs.setProgRipartizione(dettaglio.getProgRipartizione());
					rs.setDtBonifico(dettaglio.getDtBonifico());
					rs.setAnnoRif(dettaglio.getAnnoRif());
					rs.setCf(dettaglio.getCf());
					rs.setCodTributo(dettaglio.getCodTributo());
					rs.setDtRiscossione(dettaglio.getDtRiscossione());
					
					listaAnnullamenti = serviziof24.getListaAnnullamentiByVer(rs);
				
				}
				
				ht.put(F24VersamentiLogic.VERSAMENTO, dettaglio);
				ht.put(F24VersamentiLogic.LISTA_ANNULLAMENTI, listaAnnullamenti);
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
				
				F24VersamentoDTO dettaglio = serviziof24.getVersamentoById(rs);
				
				ht.put(F24VersamentiLogic.VERSAMENTO, dettaglio);
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
		
	}//-------------------------------------------------------------------------
	
	public Hashtable mCaricareListaVersByCf(String cf) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try {
			if(cf != null && !cf.equals("")) {
				
				Context cont = new InitialContext();
				F24Service serviziof24= (F24Service)  getEjb("CT_Service","CT_Service_Data_Access" , "F24ServiceBean");
						
				RicercaF24DTO rs = new RicercaF24DTO();
				rs.setEnteId(enteId);
				rs.setSessionId(sessionId);
				rs.setUserId(userId);
				rs.setCf(cf);
				
				List<F24VersamentoDTO> lst = serviziof24.getListaVersamentiByCF(rs);
				
				ht.put(this.LISTA_VERS_F24_CF, lst);
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
