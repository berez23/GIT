package it.escsolution.escwebgis.cened.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import it.escsolution.escwebgis.cened.bean.CertificazioniEnergeticheFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.webred.ct.data.model.datitecnicifabbricato.CertificazioneEnergetica;


public class CertificazioniEnergeticheLogic extends EscLogic {
	
	public final static String LISTA_CENED = "LISTA_CENED@CertificazioniEnergeticheLogic";
	public final static String FINDER = "FINDER135";
	public final static String CENED = "CENED@CertificazioniEnergeticheLogic";	
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
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
	"select * " +
	"from DATI_TEC_FABBR_CERT_ENER T where 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( select * from DATI_TEC_FABBR_CERT_ENER T where 1=? ";
	
	private final static String SQL_SELECT_DETTAGLIO = "SELECT * FROM DATI_TEC_FABBR_CERT_ENER WHERE ID = ?";

	public CertificazioniEnergeticheLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}//-------------------------------------------------------------------------
	
	private CertificazioneEnergetica getOggettoCertificazioneEnergetica(ResultSet rs, Connection conn) throws Exception{

		CertificazioneEnergetica ce = new CertificazioneEnergetica();
		
		ce.setAnnoCostruzione( rs.getString("ANNO_COSTRUZIONE")!=null?rs.getString("ANNO_COSTRUZIONE"):"" );
		ce.setBelfiore( rs.getString("BELFIORE")!=null?rs.getString("BELFIORE"):"" );
		ce.setCfPivaCertificatore( rs.getString("CF_PIVA_CERTIFICATORE")!=null?rs.getString("CF_PIVA_CERTIFICATORE"):"" );
		ce.setCfPivaProprietario( rs.getString("CF_PIVA_PROPRIETARIO")!=null?rs.getString("CF_PIVA_PROPRIETARIO"):"" );
		ce.setClasseEnergetica( rs.getString("CLASSE_ENERGETICA")!=null?rs.getString("CLASSE_ENERGETICA"):"" );
		ce.setCodiceIdentificativoPratica( rs.getString("CODICE_IDENTIFICATIVO_PRATICA")!=null?rs.getString("CODICE_IDENTIFICATIVO_PRATICA"):"" );
		ce.setComune( rs.getString("COMUNE")!=null?rs.getString("COMUNE"):"" );
		ce.setDataProt( rs.getDate("DATA_PROT") );
		ce.setDenomCertificatore( rs.getString("DENOM_CERTIFICATORE")!=null?rs.getString("DENOM_CERTIFICATORE"):"" );
		ce.setDenomProprietario( rs.getString("DENOM_PROPRIETARIO")!=null?rs.getString("DENOM_PROPRIETARIO"):"" );
		ce.setDestinazioneDiUso( rs.getString("DESTINAZIONE_DI_USO")!=null?rs.getString("DESTINAZIONE_DI_USO"):"" );
		ce.setDtScaValidita(  rs.getDate("DT_SCA_VALIDITA") );
		ce.setEdificioPubblico( rs.getString("EDIFICIO_PUBBLICO")!=null?rs.getString("EDIFICIO_PUBBLICO"):"" );
		ce.setEfer( rs.getBigDecimal("EFER") );
		ce.setEfGlobMediaAcquaCaldaSan( rs.getBigDecimal("EF_GLOB_MEDIA_ACQUA_CALDA_SAN") );
		ce.setEfGlobMediaRiscaldamento( rs.getBigDecimal("EF_GLOB_MEDIA_RISCALDAMENTO") );
		ce.setEghw( rs.getBigDecimal("EGHW") );
		ce.setEmissioni( rs.getBigDecimal("EMISSIONI") );
		ce.setEph( rs.getBigDecimal("EPH") );
		ce.setEpt( rs.getBigDecimal("EPT") );
		ce.setEpw( rs.getBigDecimal("EPW") );
		ce.setEtc( rs.getBigDecimal("ETC") );
		ce.setEth( rs.getBigDecimal("ETH") );
		ce.setFoglio( rs.getString("FOGLIO")!=null?rs.getString("FOGLIO"):"" );
		ce.setId( rs.getLong("ID") );
		ce.setIndirizzo( rs.getString("INDIRIZZO")!=null?rs.getString("INDIRIZZO"):"" );
		ce.setMotivazioneAce( rs.getString("MOTIVAZIONE_ACE")!=null?rs.getString("MOTIVAZIONE_ACE"):"" );
		ce.setNumeroRicambiOrari( rs.getString("NUMERO_RICAMBI_ORARI")!=null?rs.getString("NUMERO_RICAMBI_ORARI"):"" );
		ce.setNumProt( rs.getBigDecimal("NUM_PROT") );
		ce.setParticella( rs.getString("PARTICELLA")!=null?rs.getString("PARTICELLA"):"" );
		ce.setPotenzaGeneratore( rs.getString("POTENZA_GENERATORE")!=null?rs.getString("POTENZA_GENERATORE"):"" );
		ce.setProvincia( rs.getString("PROVINCIA")!=null?rs.getString("PROVINCIA"):"" );
		ce.setSezione( rs.getString("SEZIONE")!=null?rs.getString("SEZIONE"):"" );
		ce.setSubTutti( rs.getString("SUB_TUTTI")!=null?rs.getString("SUB_TUTTI"):"" );
		ce.setSuperficieAperturaSt( rs.getString("SUPERFICIE_APERTURA_ST")!=null?rs.getString("SUPERFICIE_APERTURA_ST"):"" );
		ce.setSuperficieCaptanteFv( rs.getString("SUPERFICIE_CAPTANTE_FV")!=null?rs.getString("SUPERFICIE_CAPTANTE_FV"):"" );
		ce.setSuperficieDisperdente( rs.getBigDecimal("SUPERFICIE_DISPERDENTE") );
		ce.setSuperficieLorda( rs.getBigDecimal("SUPERFICIE_LORDA") );
		ce.setSuperficieNetta( rs.getBigDecimal("SUPERFICIE_NETTA") );
		ce.setSuperficieVetrataOpaca( rs.getBigDecimal("SUPERFICIE_VETRATA_OPACA") );
		ce.setSupPanFvSupUtile( rs.getString("SUP_PAN_FV_SUP_UTILE")!=null?rs.getString("SUP_PAN_FV_SUP_UTILE"):"" );
		ce.setSupPanStSupUtile( rs.getString("SUP_PAN_ST_SUP_UTILE")!=null?rs.getString("SUP_PAN_ST_SUP_UTILE"):"" );
		ce.setTipologiaCombustibile( rs.getString("TIPOLOGIA_COMBUSTIBILE")!=null?rs.getString("TIPOLOGIA_COMBUSTIBILE"):"" );
		ce.setTipologiaGeneratore( rs.getString("TIPOLOGIA_GENERATORE")!=null?rs.getString("TIPOLOGIA_GENERATORE"):"" );
		ce.setTipologiaPannelloFv( rs.getString("TIPOLOGIA_PANNELLO_FV")!=null?rs.getString("TIPOLOGIA_PANNELLO_FV"):"" );
		ce.setTipologiaPannelloSt( rs.getString("TIPOLOGIA_PANNELLO_ST")!=null?rs.getString("TIPOLOGIA_PANNELLO_ST"):"" );
		ce.setTipologiaVentilazione( rs.getString("TIPOLOGIA_VENTILAZIONE")!=null?rs.getString("TIPOLOGIA_VENTILAZIONE"):"" );
		ce.setTrasmittanzaMediaBasamento( rs.getBigDecimal("TRASMITTANZA_MEDIA_BASAMENTO") );
		ce.setTrasmittanzaMediaCopertura( rs.getBigDecimal("TRASMITTANZA_MEDIA_COPERTURA") );
		ce.setTrasmittanzaMediaInvolucro( rs.getBigDecimal("TRASMITTANZA_MEDIA_INVOLUCRO") );
		ce.setTrasmittanzaMediaSerramento( rs.getBigDecimal("TRASMITTANZA_MEDIA_SERRAMENTO") );
		ce.setVolumeLordo( rs.getBigDecimal("VOLUME_LORDO") );
		ce.setVolumeNetto( rs.getBigDecimal("VOLUME_NETTO") );
		
		return ce;
	}//-------------------------------------------------------------------------
	
	public Hashtable mCaricareLista(Vector listaPar, CertificazioniEnergeticheFinder finder) throws Exception {

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
					sql = sql + " and DATI_TEC_FABBR_CERT_ENER.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.FOGLIO, T.PARTICELLA, T.SUB_TUTTI) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						CertificazioneEnergetica tes = getOggettoCertificazioneEnergetica(rs, conn);
						//tes.setChiave( "" + tes.getId() );
						vct.add(tes);					
					}
				}else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_CENED, vct);
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
	}//-------------------------------------------------------------------------
	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		CertificazioneEnergetica tes = new CertificazioneEnergetica();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				sql = SQL_SELECT_DETTAGLIO;
				
				this.setString(1, chiave);

				log.debug(sql);
				
				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					tes = getOggettoCertificazioneEnergetica(rs,conn);
				}
				
				ht.put(CENED, tes);
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
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception{
		String sql = "";
		Vector listaParTes = new Vector();
		Vector listaParDet = new Vector();
		
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if (isParametroDettaglio(el)) {
				listaParDet.add(el);
			} else {
				listaParTes.add(el);
			}
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParTes);
		indice = getCurrentParameterIndex();
		
		boolean trovatoParDet = false;
		for (int i = 0; i < listaParDet.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaParDet.get(i);
			if (!"".equals(el.getValore())) {
				trovatoParDet = true;
				break;
			}			
		}
		
		if (trovatoParDet) {			
//			String sqlAdd = " AND EXISTS (SELECT 1 FROM SIT_PUBBLICITA_PRAT_DETTAGLIO D WHERE T.ID_EXT = D.ID_EXT_TESTATA ";
			String sqlAdd = super.elaboraFiltroMascheraRicercaParziale(indice, listaParDet);
//			sqlAdd += ")";
			sql += sqlAdd;
		}
		
		return sql;
	}//-------------------------------------------------------------------------
	
	private boolean isParametroDettaglio(EscElementoFiltro el) {
		String attName = el.getAttributeName();
		
		return "VIA".equalsIgnoreCase(attName) ||
			   "CIVICO".equalsIgnoreCase(attName) ||
			   "DESC_CLS".equalsIgnoreCase(attName) ||
			   "DT_INIZIO_D".equalsIgnoreCase(attName) ||
			   "DT_FINE_D".equalsIgnoreCase(attName);
	}//-------------------------------------------------------------------------
	

}
