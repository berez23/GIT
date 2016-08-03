package it.escsolution.escwebgis.acqua.logic;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.acqua.bean.AcquaCatasto;
import it.escsolution.escwebgis.acqua.bean.AcquaFinder;
import it.escsolution.escwebgis.acqua.bean.AcquaUtente;
import it.escsolution.escwebgis.acqua.bean.AcquaUtenze;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.enel.bean.EnelBean2;
import it.webred.utils.GenericTuples;

public class AcquaLogic extends EscLogic {
	
	public final static String LISTA_ACQUA = "LISTA_ACQUA@AcquaLogic";
	public final static String FINDER = "FINDER121";
	public final static String ACQUA = "ACQUA@AcquaLogic";
	public final static String ACQUA_UTENTE = "ACQUA_UTENTE@AcquaLogic";	
	public static final String ALTRE_UTENZE = "ALTRE_UTENZE@AcquaLogic";
	public static final String CATASTO = "CATASTO@AcquaLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat SDF2 = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
	"select DISTINCT z.ID, z.id_ext_utente, z.cod_servizio," +
                                "z.descr_categoria, z.qualifica_titolare," +
                                "z.tipologia, z.tipo_contratto, z.dt_utenza," +
                                "z.rag_soc_ubicazione,z.via_ubicazione," +
                                "z.civico_ubicazione,z.cap_ubicazione," +
                                "z.comune_ubicazione,z.tipologia_ui," +
                                "z.mesi_fatturazione,z.consumo_medio," +
                                "z.stacco,z.giro,z.fatturato," +
                                "t.cognome,t.nome,t.sesso," +
                                "t.dt_nascita,t.comune_nascita," +
                                "t.pr_nascita,t.cod_fiscale," +
                                "t.denominazione,t.part_iva," +
                                "t.via_residenza,t.civico_residenza," +
                                "t.cap_residenza,t.comune_residenza," +
                                "t.pr_residenza,t.telefono,t.fax_email " +
	"from sit_acqua_utenze Z LEFT JOIN sit_acqua_utente T ON z.id_ext_utente = t.id_ext where 1=? AND z.dt_fine_val is null AND t.dt_fine_val is null";
	
	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT (*) AS conteggio FROM (select * from sit_acqua_utenze z LEFT JOIN sit_acqua_utente t ON z.id_ext_utente = t.id_ext WHERE 1 = ? AND z.dt_fine_val is null AND t.dt_fine_val is null";
 	
	private final static String SQL_SELECT_DETTAGLIO_ALTRE_UTENZE = "SELECT * FROM sit_acqua_utenze WHERE ID_EXT_UTENTE = ? AND ID != ? AND dt_fine_val is null";
	
	private final static String SQL_SELECT_DETTAGLIO_CATASTO = "SELECT * FROM sit_acqua_catasto WHERE COD_SERVIZIO = ? AND dt_fine_val is null";
	
	private final static String SQL_SELECT_DETTAGLIO_FROM_OGGETTO_UTT="SELECT DISTINCT z.ID, z.id_ext_utente, z.cod_servizio," +
                                "z.descr_categoria, z.qualifica_titolare," +
                                "z.tipologia, z.tipo_contratto, z.dt_utenza," +
                                "z.rag_soc_ubicazione,z.via_ubicazione," +
                                "z.civico_ubicazione,z.cap_ubicazione," +
                                "z.comune_ubicazione,z.tipologia_ui," +
                                "z.mesi_fatturazione,z.consumo_medio," +
                                "z.stacco,z.giro,z.fatturato," +
                                "t.cognome,t.nome,t.sesso," +
                                "t.dt_nascita,t.comune_nascita," +
                                "t.pr_nascita,t.cod_fiscale," +
                                "t.denominazione,t.part_iva," +
                                "t.via_residenza,t.civico_residenza," +
                                "t.cap_residenza,t.comune_residenza," +
                                "t.pr_residenza,t.telefono,t.fax_email  FROM sit_acqua_utenze Z LEFT JOIN sit_acqua_utente T ON z.id_ext_utente = t.id_ext " +
														"WHERE t.ID = ? AND z.dt_fine_val is null AND t.dt_fine_val is null";
	private final static String SQL_SELECT_DETTAGLIO_FROM_OGGETTO_UTZ="SELECT DISTINCT z.ID, z.id_ext_utente, z.cod_servizio," +
                                "z.descr_categoria, z.qualifica_titolare," +
                                "z.tipologia, z.tipo_contratto, z.dt_utenza," +
                                "z.rag_soc_ubicazione,z.via_ubicazione," +
                                "z.civico_ubicazione,z.cap_ubicazione," +
                                "z.comune_ubicazione,z.tipologia_ui," +
                                "z.mesi_fatturazione,z.consumo_medio," +
                                "z.stacco,z.giro,z.fatturato," +
                                "t.cognome,t.nome,t.sesso," +
                                "t.dt_nascita,t.comune_nascita," +
                                "t.pr_nascita,t.cod_fiscale," +
                                "t.denominazione,t.part_iva," +
                                "t.via_residenza,t.civico_residenza," +
                                "t.cap_residenza,t.comune_residenza," +
                                "t.pr_residenza,t.telefono,t.fax_email FROM sit_acqua_utenze Z LEFT JOIN sit_acqua_utente T ON z.id_ext_utente = t.id_ext " +
			"WHERE z.ID = ? AND z.dt_fine_val is null AND t.dt_fine_val is null";
	private final static String SQL_SELECT_DETTAGLIO_FROM_OGGETTO_CAT="SELECT DISTINCT z.ID, z.id_ext_utente, z.cod_servizio," +
                                "z.descr_categoria, z.qualifica_titolare," +
                                "z.tipologia, z.tipo_contratto, z.dt_utenza," +
                                "z.rag_soc_ubicazione,z.via_ubicazione," +
                                "z.civico_ubicazione,z.cap_ubicazione," +
                                "z.comune_ubicazione,z.tipologia_ui," +
                                "z.mesi_fatturazione,z.consumo_medio," +
                                "z.stacco,z.giro,z.fatturato," +
                                "t.cognome,t.nome,t.sesso," +
                                "t.dt_nascita,t.comune_nascita," +
                                "t.pr_nascita,t.cod_fiscale," +
                                "t.denominazione,t.part_iva," +
                                "t.via_residenza,t.civico_residenza," +
                                "t.cap_residenza,t.comune_residenza," +
                                "t.pr_residenza,t.telefono,t.fax_email " +
						           "FROM sit_acqua_utenze z LEFT JOIN sit_acqua_utente t " +
						                "ON z.id_ext_utente = t.id_ext " +
						          "WHERE z.COD_SERVIZIO = (SELECT cod_servizio from sit_acqua_catasto where id= ? and dt_fine_val IS NULL) " +
						          "AND z.dt_fine_val IS NULL AND t.dt_fine_val IS NULL";
	
	public AcquaLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	private AcquaUtente getOggettoAcquaUtente(ResultSet rs, Connection conn) throws Exception{
		
		AcquaUtente m = new AcquaUtente();
		
		m.setCognome(rs.getString("COGNOME") == null ? "-" : rs.getString("COGNOME"));
		m.setNome(rs.getString("NOME") == null ? "-" : rs.getString("NOME"));
		m.setSesso(rs.getString("SESSO") == null ? "-" : rs.getString("SESSO"));
		m.setDtNascita(rs.getDate("DT_NASCITA") == null ? "-" : SDF2.format(rs.getDate("DT_NASCITA")));
		m.setComuneNascita(rs.getString("COMUNE_NASCITA") == null ? "-" : rs.getString("COMUNE_NASCITA"));
		m.setPrNascita(rs.getString("PR_NASCITA") == null ? "-" : rs.getString("PR_NASCITA"));
		m.setCodFiscale(rs.getString("COD_FISCALE") == null ? "-" : rs.getString("COD_FISCALE"));
		m.setDenominazione(rs.getString("DENOMINAZIONE") == null ? "-" : rs.getString("DENOMINAZIONE"));
		m.setPartIva(rs.getString("PART_IVA") == null ? "-" : rs.getString("PART_IVA"));
		m.setViaResidenza(rs.getString("VIA_RESIDENZA") == null ? "-" : rs.getString("VIA_RESIDENZA"));
		m.setCivicoResidenza(rs.getString("CIVICO_RESIDENZA") == null ? "-" : rs.getString("CIVICO_RESIDENZA"));
		m.setCapResidenza(rs.getString("CAP_RESIDENZA") == null ? "-" : rs.getString("CAP_RESIDENZA"));
		m.setComuneResidenza(rs.getString("COMUNE_RESIDENZA") == null ? "-" : rs.getString("COMUNE_RESIDENZA"));
		m.setPrResidenza(rs.getString("PR_RESIDENZA") == null ? "-" : rs.getString("PR_RESIDENZA"));
		m.setTelefono(rs.getString("TELEFONO") == null ? "-" : rs.getString("TELEFONO"));
		m.setFaxEmail(rs.getString("FAX_EMAIL") == null ? "-" : rs.getString("FAX_EMAIL"));
	
		return m;
		
	}
	
	private AcquaUtenze getOggettoAcquaUtenze(ResultSet rs, Connection conn, boolean addDenom) throws Exception{
		
		AcquaUtenze m = new AcquaUtenze();
		
		m.setId(rs.getString("ID"));
		m.setIdExtUtente(rs.getString("ID_EXT_UTENTE"));
		m.setDescrCategoria(rs.getString("DESCR_CATEGORIA") == null ? "-" : rs.getString("DESCR_CATEGORIA"));
		m.setCodServizio(rs.getString("COD_SERVIZIO") == null ? "-" : rs.getString("COD_SERVIZIO"));
		m.setQualificaTitolare(rs.getString("QUALIFICA_TITOLARE") == null ? "-" : rs.getString("QUALIFICA_TITOLARE"));
		m.setTipologia(rs.getString("TIPOLOGIA") == null ? "-" : rs.getString("TIPOLOGIA"));
		m.setTipoContratto(rs.getString("TIPO_CONTRATTO") == null ? "-" : rs.getString("TIPO_CONTRATTO"));
		m.setDtUtenza(rs.getDate("DT_UTENZA") == null ? "-" : SDF2.format(rs.getDate("DT_UTENZA")));
		m.setRagSocUbicazione(rs.getString("RAG_SOC_UBICAZIONE") == null ? "-" : rs.getString("RAG_SOC_UBICAZIONE"));
		m.setViaUbicazione(rs.getString("VIA_UBICAZIONE") == null ? "-" : rs.getString("VIA_UBICAZIONE"));
		m.setCivicoUbicazione(rs.getString("CIVICO_UBICAZIONE") == null ? "-" : rs.getString("CIVICO_UBICAZIONE"));
		m.setCapUbicazione(rs.getString("CAP_UBICAZIONE") == null ? "-" : rs.getString("CAP_UBICAZIONE"));
		m.setComuneUbicazione(rs.getString("COMUNE_UBICAZIONE") == null ? "-" : rs.getString("COMUNE_UBICAZIONE"));
		m.setTipologiaUi(rs.getString("TIPOLOGIA_UI") == null ? "-" : rs.getString("TIPOLOGIA_UI"));
		m.setMesiFatturazione(rs.getBigDecimal("MESI_FATTURAZIONE") == null ? "-" : DF.format(rs.getBigDecimal("MESI_FATTURAZIONE")));
		m.setConsumoMedio(rs.getBigDecimal("CONSUMO_MEDIO") == null ? "-" : DF.format(rs.getBigDecimal("CONSUMO_MEDIO")));
		m.setStacco(rs.getBigDecimal("STACCO") == null ? "-" : DF.format(rs.getBigDecimal("STACCO")));
		m.setGiro(rs.getBigDecimal("GIRO") == null ? "-" : DF.format(rs.getBigDecimal("GIRO")));
		m.setFatturato(rs.getBigDecimal("FATTURATO") == null ? "-" : DF.format(rs.getBigDecimal("FATTURATO")));
	
		if(addDenom){
			m.setDenominazione(rs.getString("DENOMINAZIONE") == null ?(rs.getString("COGNOME") == null ?"-":
				(rs.getString("COGNOME")+" "+(rs.getString("NOME") == null ?"":rs.getString("NOME")))): rs.getString("DENOMINAZIONE"));
		}
		return m;
		
	}
	
	private AcquaCatasto getOggettoAcquaCatasto(ResultSet rs, Connection conn) throws Exception{
		
		AcquaCatasto m = new AcquaCatasto();
		
		m.setId(rs.getString("ID"));
		m.setCodServizio(rs.getString("COD_SERVIZIO") == null ? "-" : rs.getString("COD_SERVIZIO"));
		m.setAssenzaDatiCat(rs.getString("ASSENZA_DATI_CAT") == null ? "-" : rs.getString("ASSENZA_DATI_CAT"));
		m.setSezione(rs.getString("SEZIONE") == null ? "-" : rs.getString("SEZIONE"));
		m.setFoglio(rs.getString("FOGLIO") == null ? "-" : rs.getString("FOGLIO"));
		m.setParticella(rs.getString("PARTICELLA") == null ? "-" : rs.getString("PARTICELLA"));
		m.setSubalterno(rs.getString("SUBALTERNO") == null ? "-" : rs.getString("SUBALTERNO"));
		m.setEstensionePart(rs.getDate("ESTENSIONE_PART") == null ? "-" :rs.getString("ESTENSIONE_PART"));
		m.setTipologiaPart(rs.getString("TIPOLOGIA_PART") == null ? "-" : rs.getString("TIPOLOGIA_PART"));
	
		GenericTuples.T2<String,String> coord = null;
		try {
			coord = getLatitudeLongitude(m.getFoglio(), Utils.fillUpZeroInFront(m.getParticella(),5),envUtente.getEnte());
		} catch (Exception e) {}
		if (coord!=null) {
		  m.setLatitudine(coord.firstObj);
		  m.setLongitudine(coord.secondObj);
		  m.setCodEnte(envUtente.getEnte());
		}
		
		return m;
		
	}
	
	public Hashtable mCaricareLista(Vector listaPar, AcquaFinder finder) throws Exception {

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
					sql = sql + " and SIT_ACQUA_UTENZE.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by Z.COD_SERVIZIO) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						AcquaUtenze m = getOggettoAcquaUtenze(rs,conn, true);
						vct.add(m);					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_ACQUA, vct);
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
	
	public Hashtable mCaricareDettaglio(String chiave, String progEs) throws Exception{
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		AcquaUtenze m = new AcquaUtenze();
		AcquaUtente m2 = new AcquaUtente();
		Vector<AcquaUtenze> altreUtenze = new Vector<AcquaUtenze>();
		Vector<AcquaCatasto> listaCatasto = new Vector<AcquaCatasto>();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = "";
				if("UTENTI".equals(progEs))
					sql = SQL_SELECT_DETTAGLIO_FROM_OGGETTO_UTT;
				else if("CATASTO".equals(progEs))
					sql = SQL_SELECT_DETTAGLIO_FROM_OGGETTO_CAT;
				else sql = SQL_SELECT_DETTAGLIO_FROM_OGGETTO_UTZ;
				
				this.setString(1, chiave);

				log.debug("SQL_SELECT_DETTAGLIO_FROM_OGGETTO ["+sql+"]");

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					m = getOggettoAcquaUtenze(rs, conn, false);
					m2 = getOggettoAcquaUtente(rs,conn);
				}
				
				//dati altre utenze
				if(m.getId() != null){
					
					this.initialize();
					sql = SQL_SELECT_DETTAGLIO_ALTRE_UTENZE;
					this.setString(1, m.getIdExtUtente());
					this.setString(2, m.getId());
					log.debug(sql);
					prepareStatement(sql);
					java.sql.ResultSet rs1 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs1.next()) {
						AcquaUtenze u = getOggettoAcquaUtenze(rs1,conn, false);
						altreUtenze.add(u);
					}
					ht.put(ALTRE_UTENZE, altreUtenze);
					
				}
				
				//dati catasto
				if(m.getId() != null){
					
					this.initialize();
					sql = SQL_SELECT_DETTAGLIO_CATASTO;
					this.setString(1, m.getCodServizio());
					log.debug(sql);
					prepareStatement(sql);
					java.sql.ResultSet rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs2.next()) {
						AcquaCatasto c = getOggettoAcquaCatasto(rs2,conn);
						listaCatasto.add(c);
					}
					ht.put(CATASTO, listaCatasto);
					
				}
				
				ht.put(ACQUA, m);
				ht.put(ACQUA_UTENTE, m2);
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
	
	
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception
	{
		String sql = "";
		Vector listaMul = new Vector();
		
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			listaMul.add(el);
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaMul);
		indice = getCurrentParameterIndex();
		
		boolean trovatoMul = false;
		for (int i = 0; i < listaMul.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaMul.get(i);
			if (!"".equals(el.getValore())) {
				trovatoMul = true;
				break;
			}			
		}
		
		return sql;
	}
	
}
