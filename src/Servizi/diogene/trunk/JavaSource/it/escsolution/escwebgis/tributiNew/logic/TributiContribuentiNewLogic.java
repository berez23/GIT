package it.escsolution.escwebgis.tributiNew.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.imu.logic.ConsulenzaImuLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.escsolution.escwebgis.tributiNew.bean.ContribuentiNewFinder;
import it.escsolution.escwebgis.tributiNew.bean.OggettiICINew;
import it.escsolution.escwebgis.tributiNew.bean.OggettiTARSUNew;
import it.escsolution.escwebgis.tributiNew.bean.SoggettiTributiNew;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.ici.IciService;
import it.webred.ct.data.access.basic.ici.dto.RicercaViaIciDTO;
import it.webred.ct.data.access.basic.imu.SaldoImuService;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuBaseDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuConsulenzaDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuElaborazioneDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaViaTarsuDTO;
import it.webred.ct.data.model.ici.SitTIciVia;
import it.webred.ct.data.model.tarsu.SitTTarVia;

public class TributiContribuentiNewLogic extends EscLogic {

private String appoggioDataSource;
	
	public TributiContribuentiNewLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
		
	public final static String CONTRIBUENTI = "CONTRIBUENTI";
	
	private final static String SQL_SELECT_CONTRIB = "select distinct * from (select 'ICI' as tributo, sit_t_ici_sogg.*, " +
								"DECODE(SIT_T_ICI_VIA.DESCRIZIONE, NULL, SIT_T_ICI_SOGG.DES_IND_RES, (DECODE(SIT_T_ICI_VIA.PREFISSO, NULL, '', '', '', SIT_T_ICI_VIA.PREFISSO || ' ') || SIT_T_ICI_VIA.DESCRIZIONE)) AS DESC_VIA_RES " +							
								"from sit_t_ici_sogg, sit_t_ici_via " +
								"where SIT_T_ICI_SOGG.ID_EXT_VIA_RES = SIT_T_ICI_VIA.ID_EXT(+) " +
								"union all " +
								"select 'TARSU' as tributo, sit_t_tar_sogg.*, " +
								"DECODE(SIT_T_TAR_VIA.DESCRIZIONE, NULL, SIT_T_TAR_SOGG.DES_IND_RES, (DECODE(SIT_T_TAR_VIA.PREFISSO, NULL, '', '', '', SIT_T_TAR_VIA.PREFISSO || ' ') || SIT_T_TAR_VIA.DESCRIZIONE)) AS DESC_VIA_RES " +
								"from sit_t_tar_sogg, sit_t_tar_via " +
								"where SIT_T_TAR_SOGG.ID_EXT_VIA_RES = SIT_T_TAR_VIA.ID_EXT(+))";
	
	private final static String SQL_SELECT_CONTRIBUENTE_INDIRIZZO_ANAGRAFE = "SELECT trim(nvl(SIT_D_VIA.VIASEDIME,'')) VIASEDIME, " + 
	"trim(nvl(SIT_D_VIA.DESCRIZIONE,'')) VIADESCRIZIONE, " +
	"TO_NUMBER (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"numero\"]/@valore' ) )  || " +
	"DECODE(NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"barrato\"]/@valore' ), ''), '', '', '/' || NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"barrato\"]/@valore' ), ''))  || " +
	"DECODE(NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"subbarrato\"]/@valore' ), ''), '', '', '/' || NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"subbarrato\"]/@valore' ), ''))  AS CIVICO, " +
	"ENTE_S.DESCRIZIONE " +
	"FROM sit_d_persona, sit_comune, SIT_D_PERS_FAM, SIT_STATO, SIT_D_PERSONA_CIVICO , SIT_D_CIVICO, SIT_D_VIA, " +
	"sit_ente ENTE_S, " +
	"TABLE (XMLSEQUENCE (EXTRACT (nvl(civico_composto,'<civicocomposto/>'), 'civicocomposto'))) tab_civicocomposto, " +
	"SIT_COMUNE COM_MOR, SIT_COMUNE COM_IMM , SIT_COMUNE COM_EMI " +
	"WHERE sit_comune.id_ext (+) = sit_d_persona.ID_EXT_COMUNE_NASCITA " +
	"and sit_comune.DT_FINE_VAL is null " +
	"AND SIT_D_PERSONA.ID_EXT = SIT_D_PERS_FAM.ID_EXT_D_PERSONA (+) " +
	"AND SIT_D_PERS_FAM.DT_FINE_VAL IS NULL and sit_STATO.id_ext = sit_d_persona.ID_EXT_STATO " +
	"and sit_STATO.DT_FINE_VAL is null " +
	"AND SIT_D_PERSONA.ID_EXT = SIT_D_PERSONA_CIVICO.ID_EXT_D_PERSONA (+) " +
	"AND SIT_D_PERSONA_CIVICO.DT_FINE_VAL IS NULL AND SIT_D_PERSONA_CIVICO.ID_EXT_D_CIVICO = SIT_D_CIVICO.ID_EXT (+) " +
	"AND SIT_D_CIVICO.DT_FINE_VAL IS NULL AND SIT_D_CIVICO.ID_EXT_D_VIA = SIT_D_VIA.ID_EXT (+) " +
	"AND SIT_D_VIA.DT_FINE_VAL IS NULL AND COM_MOR.ID_EXT (+)  = SIT_D_PERSONA.ID_EXT_COMUNE_MOR " + 
	"AND COM_MOR.DT_FINE_VAL IS NULL AND COM_EMI.ID_EXT (+)  = SIT_D_PERSONA.ID_EXT_COMUNE_EMI " +
	"AND COM_EMI.DT_FINE_VAL IS NULL AND COM_IMM.ID_EXT (+) = SIT_D_PERSONA.ID_EXT_COMUNE_IMM " +
	"AND COM_IMM.DT_FINE_VAL IS NULL " +
	"AND sit_d_persona.CODFISC = ?";
	
	private final static String SQL_SELECT_CONTRIBUENTE_ESISTE_TAB_SIATEL = "select * from tabs where table_name = 'MI_SIATEL_P_FIS'";
	private final static String SQL_SELECT_CONTRIBUENTE_ESISTE_SYN_TAB_SIATEL = "select * from syn where synonym_name = 'MI_SIATEL_P_FIS'";
	private final static String SQL_SELECT_CONTRIBUENTE_INDIRIZZO_SIATEL = "select * from MI_SIATEL_P_FIS where COD_FISC_PERSONA = ?";
	
	private final static String SQL_SELECT_OGG_ICI = "select distinct sit_t_ici_oggetto.*, " +
													 "DECODE(SIT_T_ICI_VIA.DESCRIZIONE, NULL, SIT_T_ICI_OGGETTO.DES_IND, (DECODE(SIT_T_ICI_VIA.PREFISSO, NULL, '', '', '', SIT_T_ICI_VIA.PREFISSO || ' ') || SIT_T_ICI_VIA.DESCRIZIONE)) AS DESC_VIA " +
													 "from sit_t_ici_oggetto, sit_t_ici_via, v_t_ici_sogg_all v " +
													 "where SIT_T_ICI_OGGETTO.ID_EXT_VIA = SIT_T_ICI_VIA.ID_EXT(+) " +
													 "and sit_t_ici_oggetto.id_ext = v.id_ext_ogg_ici " +
													 "and v.";
	
	private final static String SQL_SELECT_OGG_TAR = "select distinct sit_t_tar_oggetto.*, " +
													 "DECODE(SIT_T_TAR_VIA.DESCRIZIONE, NULL, SIT_T_TAR_OGGETTO.DES_IND, (DECODE(SIT_T_TAR_VIA.PREFISSO, NULL, '', '', '', SIT_T_TAR_VIA.PREFISSO || ' ') || SIT_T_TAR_VIA.DESCRIZIONE)) AS DESC_VIA " +
													 "from sit_t_tar_oggetto, sit_t_tar_via, v_t_tar_sogg_all v " +
													 "where SIT_T_TAR_OGGETTO.ID_EXT_VIA = SIT_T_TAR_VIA.ID_EXT(+) " +
													 "and SIT_T_TAR_OGGETTO.ID_EXT = v.ID_EXT_OGG_RSU " +
													 "and v.";
	
	private final static String SQL_SELECT_CONTRIB_FROM_VIA="select * from (" +
			"select distinct 'ICI' as tributo, sit_t_ici_sogg.*, NVL(SIT_T_ici_VIA.DESCRIZIONE, SIT_T_ici_SOGG.DES_IND_RES) AS DESC_VIA_RES from sit_t_ici_sogg, sit_t_ici_via, v_t_ici_sogg_all v where SIT_T_ici_SOGG.ID_EXT_VIA_RES = SIT_T_ici_VIA.ID_EXT(+) and SIT_T_ici_SOGG.ID_EXT = v.ID_EXT  and (v.TIPO_SOGGETTO = 'Contribuente' OR v.TIPO_SOGGETTO = 'Dichiarante' OR v.TIPO_SOGGETTO = 'Contitolare' OR v.TIPO_SOGGETTO = 'Ulteriore Soggetto') and sit_t_ici_via.ID=? " +
			"union all" +
			"select distinct 'TARSU' as tributo, sit_t_tar_sogg.*, NVL(SIT_T_tar_VIA.DESCRIZIONE, SIT_T_tar_SOGG.DES_IND_RES) AS DESC_VIA_RES from sit_t_tar_sogg, sit_t_tar_via, v_t_tar_sogg_all v where SIT_T_tar_SOGG.ID_EXT_VIA_RES = SIT_T_tar_VIA.ID_EXT(+) and SIT_T_tar_SOGG.ID_EXT = v.ID_EXT  and (v.TIPO_SOGGETTO = 'Contribuente' OR v.TIPO_SOGGETTO = 'Dichiarante' OR v.TIPO_SOGGETTO = 'Ulteriore Soggetto') and sit_t_tar_via.ID=? " +
			") " +
			"where 1 = 1 " +
			"order by COG_DENOM, NOME, NVL(COD_FISC, PART_IVA)";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	private String verificaPIvaZero(String pIVA){
		
		String s ="-";
		
		try{
			if(!"-".equalsIgnoreCase(pIVA) && Long.parseLong(pIVA)==0)
				s ="-";
		}catch(NumberFormatException e){
			
			log.error("Errore non bloccante: Partita IVA ["+pIVA+"] non numerica.");
			s=pIVA;
		}
		
		return s;
	}
	
	public Hashtable mCaricareListaContribuenti(Vector listaPar, ContribuentiNewFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			java.sql.ResultSet rs = null;
			
			int indice = 1;

			this.initialize();
			this.setInt(indice,1);
			indice ++;
			
			boolean accorpCFPI = false;
			if (listaPar != null) {
				for (int i = 0; i < listaPar.size(); i++) {
					EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
					if ("ACCORP_CF_PI".equalsIgnoreCase(el.getAttributeName()) &&
						"Y".equalsIgnoreCase(el.getValore())) {
						accorpCFPI = true;
						break;
					}
				}
			}
			
			if (finder.getKeyStr().equals("")){
				sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				sql += " order by COG_DENOM, NOME, NVL(COD_FISC, PART_IVA)";
			} else{
				
				
				//controllo provenienza chiamata
				String controllo = finder.getKeyStr();
				String appo ="";
				if (  controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
					
						if (controllo.indexOf("ICI")>0){
							controllo=controllo.substring(controllo.indexOf("|")+1);
							//String ente = controllo.substring(0,controllo.indexOf("|"));
							//ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
							String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
							sql = SQL_SELECT_CONTRIB + " where 1 = ? and ID  in (" +chiavi + ") AND TRIBUTO ='ICI'";
						}else if (controllo.indexOf("TARSU")>0){
							controllo=controllo.substring(controllo.indexOf("|")+1);
							//String ente = controllo.substring(0,controllo.indexOf("|"));
							//ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
							String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
							sql = SQL_SELECT_CONTRIB + " where 1 = ? and ID  in (" +chiavi + ") AND TRIBUTO ='TARSU'";
						}
							
				}else{
					sql = SQL_SELECT_CONTRIB + " where 1 = ? and ID  in (" +finder.getKeyStr() + ") order by COG_DENOM, NOME, NVL(COD_FISC, PART_IVA)" ;
				}
			}
			
			long limInf, limSup;
			limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
			limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			ArrayList<SoggettiTributiNew> rsSoggetti = new ArrayList<SoggettiTributiNew>();
			
			while (rs.next()) {
				SoggettiTributiNew soggetto = new SoggettiTributiNew();
				soggetto.setIdExt(rs.getObject("ID_EXT") == null ? "-" : rs.getString("ID_EXT"));
				soggetto.setIdOrig(rs.getObject("ID_ORIG") == null ? "-" : rs.getString("ID_ORIG"));
				//ID_ORIG_SOGG_U = 0 equivale a null
				soggetto.setIdOrigSoggU((rs.getObject("ID_ORIG_SOGG_U") == null || rs.getString("ID_ORIG_SOGG_U").equals("0"))
										? "-" 
										: rs.getString("ID_ORIG_SOGG_U"));
				soggetto.setTipSogg(decTipSogg(rs.getObject("TIP_SOGG")));
				soggetto.setCodFisc(rs.getObject("COD_FISC") == null ? "-" : rs.getString("COD_FISC"));
				soggetto.setPartIva(rs.getObject("PART_IVA") == null ? "-" : rs.getString("PART_IVA"));
				soggetto.setCogDenom(rs.getObject("COG_DENOM") == null ? "-" : rs.getString("COG_DENOM"));
				soggetto.setNome(rs.getObject("NOME") == null ? "-" : rs.getString("NOME"));
				String desIndRes = rs.getObject("DESC_VIA_RES") == null ? null : rs.getString("DESC_VIA_RES");
				if (desIndRes == null) {
					desIndRes = "-";
				} else {
					String numCiv = rs.getObject("NUM_CIV_RES") == null ? null : rs.getString("NUM_CIV_RES");
					if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
						try {
							numCiv = "" + Integer.parseInt(numCiv.trim());
						} catch (Exception e) {}
						String espCiv = rs.getObject("ESP_CIV_RES") == null ? null : rs.getString("ESP_CIV_RES");
						if (espCiv != null && !espCiv.trim().equals("")) {
							numCiv += "/" + espCiv;
						}
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							desIndRes += " " + numCiv;
						}
					} else {
						numCiv = rs.getObject("NUM_CIV_EXT") == null ? null : rs.getString("NUM_CIV_EXT");
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							try {
								numCiv = "" + Integer.parseInt(numCiv.trim());
							} catch (Exception e) {}
							if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
								desIndRes += " " + numCiv;
							}
						}
					}
				}
				soggetto.setDesIndRes(desIndRes);
				soggetto.setDesComRes(rs.getString("DES_COM_RES")!=null ? rs.getString("DES_COM_RES") : "-");
				soggetto.setTributo(rs.getObject("TRIBUTO") == null ? "-" : rs.getString("TRIBUTO"));
				soggetto.setProvenienza(rs.getObject("PROVENIENZA") == null ? "-" : rs.getString("PROVENIENZA"));					
				
				rsSoggetti.add(soggetto);		
			}
			
			//ACCORPAMENTI
			
			if (!accorpCFPI) {
				// per ID_ORIG_SOGG_U se presente
				ArrayList<SoggettiTributiNew> rsSoggettiAccorpUnico = new ArrayList<SoggettiTributiNew>();
				for (SoggettiTributiNew soggetto : rsSoggetti) {
					boolean trovato = false;
					for (SoggettiTributiNew soggettoAccorp : rsSoggettiAccorpUnico) {
						if (!soggetto.getIdOrigSoggU().equals("-") &&
						!soggettoAccorp.getIdOrigSoggU().equals("-") &&
						soggetto.getIdOrigSoggU().equals(soggettoAccorp.getIdOrigSoggU())) {
							accorpaSoggettiInLista(soggettoAccorp, soggetto);
							soggettoAccorp.setChiave("ID_ORIG_SOGG_U|" + soggetto.getIdOrigSoggU());
							soggettoAccorp.setAccorpamento(true);
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						rsSoggettiAccorpUnico.add(soggetto);
					}
				}
				rsSoggetti = rsSoggettiAccorpUnico;
				
				// per ID_EXT
				ArrayList<SoggettiTributiNew> rsSoggettiAccorpExt = new ArrayList<SoggettiTributiNew>();
				for (SoggettiTributiNew soggetto : rsSoggetti) {
					boolean trovato = false;
					for (SoggettiTributiNew soggettoAccorp : rsSoggettiAccorpExt) {
						if (!soggetto.getIdExt().equals("-") &&
						!soggettoAccorp.getIdExt().equals("-") &&
						soggetto.getIdExt().equals(soggettoAccorp.getIdExt()) &&
						!soggetto.getProvenienza().equals("-") &&
						!soggettoAccorp.getProvenienza().equals("-") &&
						soggetto.getProvenienza().equals(soggettoAccorp.getProvenienza())) {
							accorpaSoggettiInLista(soggettoAccorp, soggetto);
							soggettoAccorp.setChiave("ID_EXT|" + soggetto.getIdExt());
							soggettoAccorp.setAccorpamento(true);
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						rsSoggettiAccorpExt.add(soggetto);
					}
				}
				rsSoggetti = rsSoggettiAccorpExt;
			}			
			
			//se richiesto, per codice fiscale / partita IVA
			if (accorpCFPI) {
				ArrayList<SoggettiTributiNew> rsSoggettiAccorpCFPI = new ArrayList<SoggettiTributiNew>();
				for (SoggettiTributiNew soggetto : rsSoggetti) {
					boolean trovato = false;
					for (SoggettiTributiNew soggettoAccorp : rsSoggettiAccorpCFPI) {

						String pIVASoggAccorp = this.verificaPIvaZero(soggettoAccorp.getPartIva());
						String pIVASogg = this.verificaPIvaZero(soggetto.getPartIva());

						boolean isPI = !"-".equalsIgnoreCase(pIVASoggAccorp) && Long.parseLong(pIVASoggAccorp) != 0
								&& !"-".equalsIgnoreCase(pIVASogg) && Long.parseLong(pIVASogg) != 0;
						boolean eqCond1 = isPI ? !soggetto.getPartIva().equals("-") : !soggetto.getCodFisc().equals("-");
						boolean eqCond2 = isPI ? !soggettoAccorp.getPartIva().equals("-") : !soggettoAccorp.getCodFisc().equals("-");
						boolean eqCond3 = isPI ? 
											soggetto.getPartIva().equals(soggettoAccorp.getPartIva()) : 
											soggetto.getCodFisc().equals(soggettoAccorp.getCodFisc());
						
						if (eqCond1 && eqCond2 && eqCond3) {
							accorpaSoggettiInLista(soggettoAccorp, soggetto);
							soggettoAccorp.setChiave(isPI ? ("PART_IVA|" + soggetto.getPartIva()) : ("COD_FISC|" + soggetto.getCodFisc()));
							soggettoAccorp.setAccorpamento(true);
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						rsSoggettiAccorpCFPI.add(soggetto);
					}
				}
				rsSoggetti = rsSoggettiAccorpCFPI;
			}
			
			for (SoggettiTributiNew soggetto : rsSoggetti) {
				//valorizzazione del campo chiave, se non già valorizzato da eventuali accorpamenti
				if (soggetto.getChiave() == null) {
					if (!soggetto.getIdOrigSoggU().equals("-")) {
						soggetto.setChiave("ID_ORIG_SOGG_U|" + soggetto.getIdOrigSoggU());
					} else if (!soggetto.getIdExt().equals("-")) {
						soggetto.setChiave("ID_EXT|" + soggetto.getIdExt());
					}
					//n.b. codice fiscale / partita IVA diventano chiave solo se usati per un accorpamento
				}
				//interventi su altri campi
				if (!soggetto.isAccorpamento()) {
					String cognome = soggetto.getCogDenom();
					String nome = soggetto.getNome();
					String denominazione = soggetto.getCogDenom();
					if("".equalsIgnoreCase(nome) || "-".equalsIgnoreCase(nome)){
						cognome = "-";
						nome = "-";
					}else {
						denominazione = "-";
					}
					soggetto.setCogDenom(cognome);
					soggetto.setNome(nome);
					soggetto.setDenominazione(denominazione);
					String pIVA = this.verificaPIvaZero(soggetto.getPartIva());
					soggetto.setPartIva(pIVA);
				} else {
					soggetto.setIdOrig(getHtmlFromArrayList(soggetto.getIdOrigs(), soggetto.getIdOrig()));
					soggetto.setTipSogg(getHtmlFromArrayList(soggetto.getTipSoggs(), soggetto.getTipSogg()));
					soggetto.setCodFisc(getHtmlFromArrayList(soggetto.getCodFiscs(), soggetto.getCodFisc()));
					soggetto.setPartIva(getHtmlFromArrayList(soggetto.getPartIvas(), soggetto.getPartIva()));
					soggetto.setCogDenom(getHtmlFromArrayList(soggetto.getCogDenoms(), soggetto.getCogDenom()));
					soggetto.setNome(getHtmlFromArrayList(soggetto.getNomes(), soggetto.getNome()));
					soggetto.setDenominazione(getHtmlFromArrayList(soggetto.getDenominaziones(), soggetto.getDenominazione()));
					soggetto.setDesIndRes(getHtmlFromArrayList(soggetto.getDesIndRess(), soggetto.getDesIndRes()));
					soggetto.setDesComRes(getHtmlFromArrayList(soggetto.getDesComRess(), soggetto.getDesComRes()));
					soggetto.setTributo(getHtmlFromArrayList(soggetto.getTributos(), soggetto.getTributo()));
					soggetto.setProvenienza(getHtmlFromArrayList(soggetto.getProvenienzas(), soggetto.getProvenienza()));
				}
			}
			
			conteggio = "" + rsSoggetti.size();
			
			int conta = 0;

			for (SoggettiTributiNew soggetto : rsSoggetti) {
				if (conta >= limInf && conta < limSup) {
					vct.add(soggetto);
				}
				conta++;
			}
			
			ht.put("LISTA_CONTRIBUENTI", vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER", finder);
			
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
	
	public Hashtable mCaricareListaContribuentiFromSoggetto(String idSoggetto, ContribuentiNewFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			java.sql.ResultSet rs = null;
			
			int indice = 1;

			this.initialize();
			
			sql = SQL_SELECT_CONTRIB  + " where ID= ?";
			this.setString(indice,idSoggetto);
			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			ArrayList<SoggettiTributiNew> rsSoggetti = new ArrayList<SoggettiTributiNew>();
			
			while (rs.next()) {
				SoggettiTributiNew soggetto = new SoggettiTributiNew();
				soggetto.setIdExt(rs.getObject("ID_EXT") == null ? "-" : rs.getString("ID_EXT"));
				soggetto.setIdOrig(rs.getObject("ID_ORIG") == null ? "-" : rs.getString("ID_ORIG"));
				//ID_ORIG_SOGG_U = 0 equivale a null
				soggetto.setIdOrigSoggU((rs.getObject("ID_ORIG_SOGG_U") == null || rs.getString("ID_ORIG_SOGG_U").equals("0"))
										? "-" 
										: rs.getString("ID_ORIG_SOGG_U"));				
				soggetto.setTipSogg(decTipSogg(rs.getObject("TIP_SOGG")));
				soggetto.setCodFisc(rs.getObject("COD_FISC") == null ? "-" : rs.getString("COD_FISC"));
				soggetto.setPartIva(rs.getObject("PART_IVA") == null ? "-" : rs.getString("PART_IVA"));
				soggetto.setCogDenom(rs.getObject("COG_DENOM") == null ? "-" : rs.getString("COG_DENOM"));
				soggetto.setNome(rs.getObject("NOME") == null ? "-" : rs.getString("NOME"));
				String desIndRes = rs.getObject("DESC_VIA_RES") == null ? null : rs.getString("DESC_VIA_RES");
				if (desIndRes == null) {
					desIndRes = "-";
				} else {
					String numCiv = rs.getObject("NUM_CIV_RES") == null ? null : rs.getString("NUM_CIV_RES");
					if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
						try {
							numCiv = "" + Integer.parseInt(numCiv.trim());
						} catch (Exception e) {}
						String espCiv = rs.getObject("ESP_CIV_RES") == null ? null : rs.getString("ESP_CIV_RES");
						if (espCiv != null && !espCiv.trim().equals("")) {
							numCiv += "/" + espCiv;
						}
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							desIndRes += " " + numCiv;
						}
					}						
				}
				soggetto.setDesIndRes(desIndRes);
				soggetto.setDesComRes(rs.getString("DES_COM_RES")!=null ? rs.getString("DES_COM_RES") : "-");
				soggetto.setTributo(rs.getObject("TRIBUTO") == null ? "-" : rs.getString("TRIBUTO"));
				soggetto.setProvenienza(rs.getObject("PROVENIENZA") == null ? "-" : rs.getString("PROVENIENZA"));					
				
				rsSoggetti.add(soggetto);		
			}
			
			//ACCORPAMENTI
			
			//if (!accorpCFPI) {
				// per ID_ORIG_SOGG_U se presente
				ArrayList<SoggettiTributiNew> rsSoggettiAccorpUnico = new ArrayList<SoggettiTributiNew>();
				for (SoggettiTributiNew soggetto : rsSoggetti) {
					boolean trovato = false;
					for (SoggettiTributiNew soggettoAccorp : rsSoggettiAccorpUnico) {
						if (!soggetto.getIdOrigSoggU().equals("-") &&
						!soggettoAccorp.getIdOrigSoggU().equals("-") &&
						soggetto.getIdOrigSoggU().equals(soggettoAccorp.getIdOrigSoggU())) {
							accorpaSoggettiInLista(soggettoAccorp, soggetto);
							soggettoAccorp.setChiave("ID_ORIG_SOGG_U|" + soggetto.getIdOrigSoggU());
							soggettoAccorp.setAccorpamento(true);
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						rsSoggettiAccorpUnico.add(soggetto);
					}
				}
				rsSoggetti = rsSoggettiAccorpUnico;
				
				// per ID_EXT
				ArrayList<SoggettiTributiNew> rsSoggettiAccorpExt = new ArrayList<SoggettiTributiNew>();
				for (SoggettiTributiNew soggetto : rsSoggetti) {
					boolean trovato = false;
					for (SoggettiTributiNew soggettoAccorp : rsSoggettiAccorpExt) {
						if (!soggetto.getIdExt().equals("-") &&
						!soggettoAccorp.getIdExt().equals("-") &&
						soggetto.getIdExt().equals(soggettoAccorp.getIdExt()) &&
						!soggetto.getProvenienza().equals("-") &&
						!soggettoAccorp.getProvenienza().equals("-") &&
						soggetto.getProvenienza().equals(soggettoAccorp.getProvenienza())) {
							accorpaSoggettiInLista(soggettoAccorp, soggetto);
							soggettoAccorp.setChiave("ID_EXT|" + soggetto.getIdExt());
							soggettoAccorp.setAccorpamento(true);
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						rsSoggettiAccorpExt.add(soggetto);
					}
				}
				rsSoggetti = rsSoggettiAccorpExt;
			/*}			
			
			//se richiesto, per codice fiscale / partita IVA
			if (accorpCFPI) {
				ArrayList<SoggettiTributiNew> rsSoggettiAccorpCFPI = new ArrayList<SoggettiTributiNew>();
				for (SoggettiTributiNew soggetto : rsSoggetti) {
					boolean trovato = false;
					for (SoggettiTributiNew soggettoAccorp : rsSoggettiAccorpCFPI) {

						String pIVASoggAccorp = soggettoAccorp.getPartIva();
						String pIVASogg = soggetto.getPartIva();
						try {
							if (!"-".equalsIgnoreCase(pIVASoggAccorp) && Long.parseLong(pIVASoggAccorp) == 0){
								pIVASoggAccorp = "-";
							}
							if (!"-".equalsIgnoreCase(pIVASogg) && Long.parseLong(pIVASogg) == 0){
								pIVASogg = "-";
							}
						} catch (Exception e) {}
						
						boolean isPI = !"-".equalsIgnoreCase(pIVASoggAccorp) && Long.parseLong(pIVASoggAccorp) != 0
								&& !"-".equalsIgnoreCase(pIVASogg) && Long.parseLong(pIVASogg) != 0;
						boolean eqCond1 = isPI ? !soggetto.getPartIva().equals("-") : !soggetto.getCodFisc().equals("-");
						boolean eqCond2 = isPI ? !soggettoAccorp.getPartIva().equals("-") : !soggettoAccorp.getCodFisc().equals("-");
						boolean eqCond3 = isPI ? 
											soggetto.getPartIva().equals(soggettoAccorp.getPartIva()) : 
											soggetto.getCodFisc().equals(soggettoAccorp.getCodFisc());
						
						if (eqCond1 && eqCond2 && eqCond3) {
							accorpaSoggettiInLista(soggettoAccorp, soggetto);
							soggettoAccorp.setChiave(isPI ? ("PART_IVA|" + soggetto.getPartIva()) : ("COD_FISC|" + soggetto.getCodFisc()));
							soggettoAccorp.setAccorpamento(true);
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						rsSoggettiAccorpCFPI.add(soggetto);
					}
				}
				rsSoggetti = rsSoggettiAccorpCFPI;
			}
			*/
				
			for (SoggettiTributiNew soggetto : rsSoggetti) {
				//valorizzazione del campo chiave, se non già valorizzato da eventuali accorpamenti
				if (soggetto.getChiave() == null) {
					if (!soggetto.getIdOrigSoggU().equals("-")) {
						soggetto.setChiave("ID_ORIG_SOGG_U|" + soggetto.getIdOrigSoggU());
					} else if (!soggetto.getIdExt().equals("-")) {
						soggetto.setChiave("ID_EXT|" + soggetto.getIdExt());
					}
					//n.b. codice fiscale / partita IVA diventano chiave solo se usati per un accorpamento
				}
				//interventi su altri campi
				if (!soggetto.isAccorpamento()) {
					String cognome = soggetto.getCogDenom();
					String nome = soggetto.getNome();
					String denominazione = soggetto.getCogDenom();
					if("".equalsIgnoreCase(nome) || "-".equalsIgnoreCase(nome)){
						cognome = "-";
						nome = "-";
					}else {
						denominazione = "-";
					}
					soggetto.setCogDenom(cognome);
					soggetto.setNome(nome);
					soggetto.setDenominazione(denominazione);
					String pIVA = this.verificaPIvaZero(soggetto.getPartIva());
					soggetto.setPartIva(pIVA);
				} else {
					soggetto.setIdOrig(getHtmlFromArrayList(soggetto.getIdOrigs(), soggetto.getIdOrig()));
					soggetto.setTipSogg(getHtmlFromArrayList(soggetto.getTipSoggs(), soggetto.getTipSogg()));
					soggetto.setCodFisc(getHtmlFromArrayList(soggetto.getCodFiscs(), soggetto.getCodFisc()));
					soggetto.setPartIva(getHtmlFromArrayList(soggetto.getPartIvas(), soggetto.getPartIva()));
					soggetto.setCogDenom(getHtmlFromArrayList(soggetto.getCogDenoms(), soggetto.getCogDenom()));
					soggetto.setNome(getHtmlFromArrayList(soggetto.getNomes(), soggetto.getNome()));
					soggetto.setDenominazione(getHtmlFromArrayList(soggetto.getDenominaziones(), soggetto.getDenominazione()));
					soggetto.setDesIndRes(getHtmlFromArrayList(soggetto.getDesIndRess(), soggetto.getDesIndRes()));
					soggetto.setDesComRes(getHtmlFromArrayList(soggetto.getDesComRess(),soggetto.getDesComRes()));
					soggetto.setTributo(getHtmlFromArrayList(soggetto.getTributos(), soggetto.getTributo()));
					soggetto.setProvenienza(getHtmlFromArrayList(soggetto.getProvenienzas(), soggetto.getProvenienza()));
				}
			
					vct.add(soggetto);
				
			}
			
			ht.put("LISTA_CONTRIBUENTI", vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER", finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = idSoggetto;
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
	
	private void accorpaSoggettiInLista(SoggettiTributiNew soggettoAccorp, SoggettiTributiNew soggetto) {
		if (soggettoAccorp.getIdOrigs() == null) {
			soggettoAccorp.setIdOrigs(new ArrayList<String>());
			soggettoAccorp.getIdOrigs().add(soggettoAccorp.getTributo().toUpperCase() + ": " + soggettoAccorp.getIdOrig());
		}
		soggettoAccorp.getIdOrigs().add(soggetto.getTributo().toUpperCase() + ": " + soggetto.getIdOrig());
		
		if (soggettoAccorp.getTipSoggs() == null) {
			soggettoAccorp.setTipSoggs(new ArrayList<String>());
			soggettoAccorp.getTipSoggs().add(soggettoAccorp.getTributo().toUpperCase() + ": " + soggettoAccorp.getTipSogg());
		}
		soggettoAccorp.getTipSoggs().add(soggetto.getTributo().toUpperCase() + ": " + soggetto.getTipSogg());
		
		if (soggettoAccorp.getCodFiscs() == null) {
			soggettoAccorp.setCodFiscs(new ArrayList<String>());
			soggettoAccorp.getCodFiscs().add(soggettoAccorp.getTributo().toUpperCase() + ": " + soggettoAccorp.getCodFisc());
		}
		soggettoAccorp.getCodFiscs().add(soggetto.getTributo().toUpperCase() + ": " + soggetto.getCodFisc());
		
		String pIVASoggAccorp = this.verificaPIvaZero(soggettoAccorp.getPartIva());
		String pIVASogg = this.verificaPIvaZero(soggetto.getPartIva());
		try {
			if (soggettoAccorp.getPartIvas() == null) {
				soggettoAccorp.setPartIvas(new ArrayList<String>());
				soggettoAccorp.getPartIvas().add(soggettoAccorp.getTributo().toUpperCase() + ": " + pIVASoggAccorp);
			}
			soggettoAccorp.getPartIvas().add(soggetto.getTributo().toUpperCase() + ": " + pIVASogg);
		} catch (Exception e) {}

		String cognomeSoggAccorp = soggettoAccorp.getCogDenom();
		String nomeSoggAccorp = soggettoAccorp.getNome();
		String denominazioneSoggAccorp = soggettoAccorp.getCogDenom();
		if("".equalsIgnoreCase(nomeSoggAccorp) || "-".equalsIgnoreCase(nomeSoggAccorp)){
			cognomeSoggAccorp = "-";
			nomeSoggAccorp = "-";
		}else {
			denominazioneSoggAccorp = "-";
		}
		
		String cognomeSogg = soggetto.getCogDenom();
		String nomeSogg = soggetto.getNome();
		String denominazioneSogg = soggetto.getCogDenom();
		if("".equalsIgnoreCase(nomeSogg) || "-".equalsIgnoreCase(nomeSogg)){
			cognomeSogg = "-";
			nomeSogg = "-";
		}else {
			denominazioneSogg = "-";
		}
		
		if (soggettoAccorp.getCogDenoms() == null) {
			soggettoAccorp.setCogDenoms(new ArrayList<String>());
			soggettoAccorp.getCogDenoms().add(soggettoAccorp.getTributo().toUpperCase() + ": " + cognomeSoggAccorp);
		}
		soggettoAccorp.getCogDenoms().add(soggetto.getTributo().toUpperCase() + ": " + cognomeSogg);
		
		if (soggettoAccorp.getNomes() == null) {
			soggettoAccorp.setNomes(new ArrayList<String>());
			soggettoAccorp.getNomes().add(soggettoAccorp.getTributo().toUpperCase() + ": " + nomeSoggAccorp);
		}
		soggettoAccorp.getNomes().add(soggetto.getTributo().toUpperCase() + ": " + nomeSogg);
		
		if (soggettoAccorp.getDenominaziones() == null) {
			soggettoAccorp.setDenominaziones(new ArrayList<String>());
			soggettoAccorp.getDenominaziones().add(soggettoAccorp.getTributo().toUpperCase() + ": " + denominazioneSoggAccorp);
		}
		soggettoAccorp.getDenominaziones().add(soggetto.getTributo().toUpperCase() + ": " + denominazioneSogg);
		
		if (soggettoAccorp.getDesIndRess() == null) {
			soggettoAccorp.setDesIndRess(new ArrayList<String>());
			soggettoAccorp.getDesIndRess().add(soggettoAccorp.getTributo().toUpperCase() + ": " + soggettoAccorp.getDesIndRes());
		}
		soggettoAccorp.getDesIndRess().add(soggetto.getTributo().toUpperCase() + ": " + soggetto.getDesIndRes());
		
		if (soggettoAccorp.getTributos() == null) {
			soggettoAccorp.setTributos(new ArrayList<String>());
			soggettoAccorp.getTributos().add(soggettoAccorp.getTributo());
		}
		soggettoAccorp.getTributos().add(soggetto.getTributo());
		
		if (soggettoAccorp.getProvenienzas() == null) {
			soggettoAccorp.setProvenienzas(new ArrayList<String>());
			soggettoAccorp.getProvenienzas().add(soggettoAccorp.getTributo().toUpperCase() + ": " + soggettoAccorp.getProvenienza());
		}
		soggettoAccorp.getProvenienzas().add(soggetto.getTributo().toUpperCase() + ": " + soggetto.getProvenienza());
	}
	
	private String getHtmlFromArrayList(ArrayList<String> al, String str) {
		if (str == null || str.equals("")) {
			str = "-";
		}
		if (al != null && al.size() > 0) {
			str = "";
			
			ArrayList<String> alNew = new ArrayList<String>();
			for (String alStr : al) {
				boolean trovato = false;
				for (String alNewStr : alNew) {
					if (alStr.equalsIgnoreCase(alNewStr)) {
						trovato = true;
						break;
					}
				}
				if (!trovato) {
					alNew.add(alStr);
				}
			}
			
			al = alNew;
			
			if (al != null && al.size() > 0) {
				if (al.size() == 1) {
					al.set(0, al.get(0).substring(al.get(0).indexOf(": ") + ": ".length()));
				} else {
					ArrayList<String> alCfr = al;
					boolean trovato = false;
					for (String alStr : al) {					
						for (String alCfrStr : alCfr) {
							if (!alCfrStr.substring(alCfrStr.indexOf(": ") + ": ".length()).equalsIgnoreCase(alStr.substring(alStr.indexOf(": ") + ": ".length()))) {
								trovato = true;
							}
						}
					}
					if (!trovato) {
						String strToAdd = al.get(0).substring(al.get(0).indexOf(": ") + ": ".length());
						al = new ArrayList<String>();
						al.add(strToAdd);
					}
				}
				
				for (String alStr : al) {
					str += alStr + "<br>";
				}
			}
		}
		return str;
	}
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception
	{
		boolean isICI = false;
		boolean isTARSU = false;
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if ("TIP_TRIB".equalsIgnoreCase(el.getAttributeName())) {
				if ("".equalsIgnoreCase(el.getValore())) {
					isICI = true;
					isTARSU = true;
				} else if ("I".equalsIgnoreCase(el.getValore())) {
					isICI = true;
					isTARSU = false;
				} else if ("T".equalsIgnoreCase(el.getValore())) {
					isICI = false;
					isTARSU = true;
				}
				break;
			}
		}
		
		Vector listaParSql = new Vector();
		Vector listaAltriPar = new Vector();
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if (isParametroSql(el)) {
				listaParSql.add(el);
			} else {
				listaAltriPar.add(el);
			}
		}
		
		String sql = "";
		
		boolean noOgg = false;
		String[] titoliSogg = null;
		for (int i = 0; i < listaAltriPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaAltriPar.get(i);
			String attName = el.getAttributeName();
			if ("TIT_SOGG".equalsIgnoreCase(attName)) {
				titoliSogg = el.getValori();
				break;
			}
		}
		noOgg = (titoliSogg == null || titoliSogg.length == 0);
		
		if (noOgg) {
			//vengono ricercati i contribuenti senza oggetti collegati
			sql = SQL_SELECT_CONTRIB + " Q WHERE 1=1 ";
			if (isICI) {
				if (!isTARSU) {
					sql += " AND TRIBUTO = 'ICI' ";
				}
				sql += " AND NOT EXISTS (SELECT 1 FROM V_T_ICI_SOGG_ALL V WHERE Q.ID_EXT = V.ID_EXT)";
			}
			if (isTARSU) {
				if (!isICI) {
					sql += " AND TRIBUTO = 'TARSU' ";
				}
				sql += " AND NOT EXISTS (SELECT 1 FROM V_T_TAR_SOGG_ALL V WHERE Q.ID_EXT = V.ID_EXT)";
			}
		} else {
			if (isICI) {
				if (isTARSU) {
					sql = getSelectICITARSU("ici", listaAltriPar) +
					" union all " +
					getSelectICITARSU("tar", listaAltriPar);
				} else {
					sql = getSelectICITARSU("ici", listaAltriPar);
				}
			} else if (isTARSU) {
				sql = getSelectICITARSU("tar", listaAltriPar);
			}
		}		
		
		sql = "select * from (" + sql + ") where 1 = ?" + super.elaboraFiltroMascheraRicerca(indice, listaParSql);
		
		return sql;
	}
	
	private boolean isParametroSql(EscElementoFiltro el) {
		String attName = el.getAttributeName();
		return "TIP_SOGG".equalsIgnoreCase(attName) ||
				"COD_FISC".equalsIgnoreCase(attName) ||
				"PART_IVA".equalsIgnoreCase(attName) ||
				"COG_DENOM".equalsIgnoreCase(attName) ||
				"NOME".equalsIgnoreCase(attName);
	}
	
	private String getSelectICITARSU(String trib, Vector listaPar) {
		String sql = "select distinct '" + 
					("tar".equalsIgnoreCase(trib) ? "TARSU" : "ICI") +
					"' as tributo, sit_t_" + trib + "_sogg.*, " +
					"NVL(SIT_T_" + trib + "_VIA.DESCRIZIONE, SIT_T_" + trib + "_SOGG.DES_IND_RES) AS DESC_VIA_RES " +
					"from sit_t_" + trib + "_sogg, sit_t_" + trib + "_via, v_t_" + trib + "_sogg_all v " +
					"where SIT_T_" + trib + "_SOGG.ID_EXT_VIA_RES = SIT_T_" + trib + "_VIA.ID_EXT(+) " +
					"and SIT_T_" + trib + "_SOGG.ID_EXT = v.ID_EXT ";
		
		String[] titoliSogg = null;
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			String attName = el.getAttributeName();
			if ("TIT_SOGG".equalsIgnoreCase(attName)) {
				titoliSogg = el.getValori();
				break;
			}
		}
		
		boolean cnt = false;
		boolean dic = false;
		boolean ctt = false;
		boolean ult = false;
		if (titoliSogg == null || titoliSogg.length == 0) {
			//non si aggiungono clausole
			//cnt = true;
			//dic = true;
			//ctt = true;
			//ult = true;
			//modificato: caso gestito a monte nel metodo chiamante (dove vengono ricercati i contribuenti senza oggetti collegati)
		} else {
			for (String titoloSogg : titoliSogg) {
				if ("CNT".equalsIgnoreCase(titoloSogg)) {
					cnt = true;
				}
				if ("DIC".equalsIgnoreCase(titoloSogg)) {
					dic = true;
				}
				if ("CTT".equalsIgnoreCase(titoloSogg)) {
					ctt = true;
				}
				if ("ULT".equalsIgnoreCase(titoloSogg)) {
					ult = true;
				}
			}
		}
		
		String sqlAdd = "";
		boolean addOr = false;
		if (cnt) {
			if (addOr) {
				sqlAdd += " OR ";
			}
			
			sqlAdd += "v.TIPO_SOGGETTO = 'Contribuente'";

			addOr = true;
		}
		if (dic) {
			if (addOr) {
				sqlAdd += " OR ";
			}
			
			sqlAdd += "v.TIPO_SOGGETTO = 'Dichiarante'";

			addOr = true;
		}
		if ("ici".equalsIgnoreCase(trib) && ctt) {
			if (addOr) {
				sqlAdd += " OR ";
			}
			
			sqlAdd += "v.TIPO_SOGGETTO = 'Contitolare'";
			
			addOr = true;
		}
		if (ult) {
			if (addOr) {
				sqlAdd += " OR ";
			}

			sqlAdd += "v.TIPO_SOGGETTO = 'Ulteriore Soggetto'";

			addOr = true;
		}
		if (!"".equals(sqlAdd)) {
			sqlAdd = " and (" + sqlAdd + ")";
		}
		sql += sqlAdd;
		
		return sql;
	}
	
	private String decTipSogg(Object tipSogg) {
		if (tipSogg != null && tipSogg instanceof String) {
			String strTipSogg = (String)tipSogg;
			if ("F".equalsIgnoreCase(strTipSogg)) {
				return "Persona Fisica";
			} else if ("G".equalsIgnoreCase(strTipSogg)) {
				return "Persona Giuridica";
			}
		}
		return "-";
	}
	
	public Hashtable mCaricareDettaglioContribuenti(String chiaveComposta) throws Exception {
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		ArrayList<SoggettiTributiNew> datiSoggetto = new ArrayList<SoggettiTributiNew>();
		
		try {
			if(chiaveComposta != null && !chiaveComposta.equals("") && chiaveComposta.indexOf("|") > -1) {
				
				String[] arrChiave = chiaveComposta.split("\\|");
				String campoChiave = arrChiave[0];
				String chiave = arrChiave[1];

				conn = this.getConnection();
				this.initialize();
				String sql = SQL_SELECT_CONTRIB;
				sql += " WHERE " + campoChiave + " = ?";		
				
				this.setString(1, chiave);
				if (campoChiave.equalsIgnoreCase("ID_EXT") && chiave.indexOf("@") > -1) {
					sql += " AND PROVENIENZA = ?";
					String provenienza = chiave.substring(0, chiave.indexOf("@"));
					this.setString(2, provenienza);
				}

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				boolean trovatoCrossLink = false;

				while (rs.next()) {
					
					//oggetto vuoto con sola chiave (serve per i crosslink)
					//prende l'id del primo oggetto trovato
					if (!trovatoCrossLink) {
						SoggettiTributiNew datoSoggettoCrossLink = new SoggettiTributiNew();
						datoSoggettoCrossLink.setChiave(rs.getString("ID"));
						ht.put("CONTRIBUENTI", datoSoggettoCrossLink);
						trovatoCrossLink = true;
					}					
					
					SoggettiTributiNew datoSoggetto = new SoggettiTributiNew();
					datoSoggetto.setId(rs.getObject("ID") == null ? "-" : rs.getString("ID"));
					datoSoggetto.setTributo(rs.getObject("TRIBUTO") == null ? "-" : rs.getString("TRIBUTO"));
					datoSoggetto.setIdOrig(rs.getObject("ID_ORIG") == null ? "-" : rs.getString("ID_ORIG"));					
					//ID_ORIG_SOGG_U = 0 equivale a null
					datoSoggetto.setIdOrigSoggU((rs.getObject("ID_ORIG_SOGG_U") == null || rs.getString("ID_ORIG_SOGG_U").equals("0"))
											? "-" 
											: rs.getString("ID_ORIG_SOGG_U"));					
					datoSoggetto.setTipSogg(decTipSogg(rs.getObject("TIP_SOGG")));
					datoSoggetto.setCodFisc(rs.getObject("COD_FISC") == null ? "-" : rs.getString("COD_FISC"));
					String pIVA = rs.getObject("PART_IVA") == null ? "-" : rs.getString("PART_IVA");
					pIVA = this.verificaPIvaZero(pIVA);
					datoSoggetto.setPartIva(pIVA);
					String cognome = rs.getObject("COG_DENOM") == null ? "-" : rs.getString("COG_DENOM");
					String nome = rs.getObject("NOME") == null ? "-" : rs.getString("NOME");
					String denominazione = rs.getObject("COG_DENOM") == null ? "-" : rs.getString("COG_DENOM");
					if("".equalsIgnoreCase(nome) || "-".equalsIgnoreCase(nome)){
						cognome = "-";
						nome = "-";
					}else {
						denominazione = "-";
					}
					datoSoggetto.setCogDenom(cognome);
					datoSoggetto.setNome(nome);
					datoSoggetto.setDenominazione(denominazione);
					datoSoggetto.setSesso(rs.getObject("SESSO") == null ? "-" : rs.getString("SESSO"));
					datoSoggetto.setDtNsc(rs.getObject("DT_NSC") == null ? "-" : SDF.format(rs.getDate("DT_NSC")));
					datoSoggetto.setDesComNsc(rs.getObject("DES_COM_NSC") == null ? "-" : rs.getString("DES_COM_NSC"));
					datoSoggetto.setProvenienza(rs.getObject("PROVENIENZA") == null ? "-" : rs.getString("PROVENIENZA"));
					String desIndRes = rs.getObject("DESC_VIA_RES") == null ? null : rs.getString("DESC_VIA_RES");
					if (desIndRes == null) {
						desIndRes = "-";
					} else {
						String numCiv = rs.getObject("NUM_CIV_RES") == null ? null : rs.getString("NUM_CIV_RES");
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							try {
								numCiv = "" + Integer.parseInt(numCiv.trim());
							} catch (Exception e) {}
							String espCiv = rs.getObject("ESP_CIV_RES") == null ? null : rs.getString("ESP_CIV_RES");
							if (espCiv != null && !espCiv.trim().equals("")) {
								numCiv += "/" + espCiv;
							}
							if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
								desIndRes += " " + numCiv;
							}
						} else {
							numCiv = rs.getObject("NUM_CIV_EXT") == null ? null : rs.getString("NUM_CIV_EXT");
							if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
								try {
									numCiv = "" + Integer.parseInt(numCiv.trim());
								} catch (Exception e) {}
								if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
									desIndRes += " " + numCiv;
								}
							}
						}			
					}
					datoSoggetto.setDesIndRes(desIndRes);
					datoSoggetto.setDesComRes(rs.getString("DES_COM_RES"));
					ArrayList<String> alAnagrafe = getIndirizzoAnagrafe(conn, datoSoggetto.getCodFisc());
					datoSoggetto.setIndResAna(alAnagrafe.get(0).trim().equals("") ? "-" : alAnagrafe.get(0).trim());
					datoSoggetto.setComResAna(alAnagrafe.get(1).trim().equals("") ? "-" : alAnagrafe.get(1).trim());
					ArrayList<String> alSiatel = getIndirizzoSiatel(conn, datoSoggetto.getCodFisc());
					datoSoggetto.setIndSiatel(alSiatel.get(0).trim().equals("") ? "-" : alSiatel.get(0).trim());
					datoSoggetto.setComSiatel(alSiatel.get(1).trim().equals("") ? "-" : alSiatel.get(1).trim());
					datoSoggetto.setScalaRes(rs.getObject("SCALA_RES") == null ? "-" : rs.getString("SCALA_RES"));
					datoSoggetto.setPianoRes(rs.getObject("PIANO_RES") == null ? "-" : rs.getString("PIANO_RES"));
					datoSoggetto.setInternoRes(rs.getObject("INTERNO_RES") == null ? "-" : rs.getString("INTERNO_RES"));
					datiSoggetto.add(datoSoggetto);
				}
				
				//lista oggetti ICI				
				sql = SQL_SELECT_OGG_ICI + campoChiave + " = ? ";
				this.initialize();
				this.setString(1, chiave);
				if (campoChiave.equalsIgnoreCase("ID_EXT") && chiave.indexOf("@") > -1) {
					sql += " AND v.provenienza = ? ";
					String provenienza = chiave.substring(0, chiave.indexOf("@"));
					this.setString(2, provenienza);
				}
				sql += "ORDER BY YEA_DEN DESC, FOGLIO, NUMERO, SUB";

				prepareStatement(sql);
				ResultSet rsICI = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				ArrayList<OggettiICINew> oggettiICI = new ArrayList<OggettiICINew>();
				while (rsICI.next()) {
					OggettiICINew oggettoICI = new OggettiICINew();
					
					oggettoICI.setChiave(rsICI.getString("ID"));
					oggettoICI.setSez(rsICI.getObject("SEZ") == null ? "-" : rsICI.getString("SEZ"));
					oggettoICI.setFoglio(rsICI.getObject("FOGLIO") == null ? "-" : rsICI.getString("FOGLIO"));
					oggettoICI.setNumero(rsICI.getObject("NUMERO") == null ? "-" : rsICI.getString("NUMERO"));
					oggettoICI.setSub(rsICI.getObject("SUB") == null ? "-" : rsICI.getString("SUB"));
					String desInd = rsICI.getObject("DESC_VIA") == null ? null : rsICI.getString("DESC_VIA");
					if (desInd == null) {
						desInd = "-";
					} else {
						String numCiv = rsICI.getObject("NUM_CIV") == null ? null : rsICI.getString("NUM_CIV");
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							try {
								numCiv = "" + Integer.parseInt(numCiv.trim());
							} catch (Exception e) {}
							String espCiv = rsICI.getObject("ESP_CIV") == null ? null : rsICI.getString("ESP_CIV");
							if (espCiv != null && !espCiv.trim().equals("")) {
								numCiv += "/" + espCiv;
							}
							if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
								desInd += " " + numCiv;
							}
						}						
					}
					oggettoICI.setDesInd(desInd);
					oggettoICI.setCat(rsICI.getObject("CAT") == null ? "-" : rsICI.getString("CAT"));
					oggettoICI.setProvenienza(rsICI.getObject("PROVENIENZA") == null ? "-" : rsICI.getString("PROVENIENZA"));
					oggettoICI.setYeaDen(rsICI.getObject("YEA_DEN") == null ? "-" : rsICI.getString("YEA_DEN"));
					oggettoICI.setNumDen(rsICI.getObject("NUM_DEN") == null ? "-" : rsICI.getString("NUM_DEN"));
					oggettoICI.setValImm(rsICI.getObject("VAL_IMM") == null ? "-" : DF.format(rsICI.getDouble("VAL_IMM")));
					oggettoICI.setFlgPos3112(rsICI.getObject("FLG_POS3112") == null ? "NO" : 
						(rsICI.getString("FLG_POS3112").equalsIgnoreCase("1") || rsICI.getString("FLG_POS3112").equalsIgnoreCase("S") ? "SI" : "NO"));
					setEvidenziaICIAttuale(conn, oggettoICI);
					
					oggettiICI.add(oggettoICI);
				}
				
				//lista oggetti TARSU
				sql = SQL_SELECT_OGG_TAR + campoChiave + " = ? ";
				this.initialize();
				this.setString(1, chiave);
				if (campoChiave.equalsIgnoreCase("ID_EXT") && chiave.indexOf("@") > -1) {
					sql += " AND v.provenienza = ? ";
					String provenienza = chiave.substring(0, chiave.indexOf("@"));
					this.setString(2, provenienza);
				}
				sql += "ORDER BY DAT_FIN DESC";

				prepareStatement(sql);
				ResultSet rsTARSU = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				ArrayList<OggettiTARSUNew> oggettiTARSU = new ArrayList<OggettiTARSUNew>();
				while (rsTARSU.next()) {
					OggettiTARSUNew oggettoTARSU = new OggettiTARSUNew();

					oggettoTARSU.setChiave(rsTARSU.getString("ID"));
					oggettoTARSU.setSez(rsTARSU.getObject("SEZ") == null ? "-" : rsTARSU.getString("SEZ"));
					oggettoTARSU.setFoglio(rsTARSU.getObject("FOGLIO") == null ? "-" : rsTARSU.getString("FOGLIO"));
					oggettoTARSU.setNumero(rsTARSU.getObject("NUMERO") == null ? "-" : rsTARSU.getString("NUMERO"));
					oggettoTARSU.setSub(rsTARSU.getObject("SUB") == null ? "-" : rsTARSU.getString("SUB"));
					String desInd = rsTARSU.getObject("DESC_VIA") == null ? null : rsTARSU.getString("DESC_VIA");
					if (desInd == null) {
						desInd = "-";
					} else {
						String numCiv = rsTARSU.getObject("NUM_CIV") == null ? null : rsTARSU.getString("NUM_CIV");
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							try {
								numCiv = "" + Integer.parseInt(numCiv.trim());
							} catch (Exception e) {}
							String espCiv = rsTARSU.getObject("ESP_CIV") == null ? null : rsTARSU.getString("ESP_CIV");
							if (espCiv != null && !espCiv.trim().equals("")) {
								numCiv += "/" + espCiv;
							}
							if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
								desInd += " " + numCiv;
							}
						}						
					}
					oggettoTARSU.setDesInd(desInd);
					oggettoTARSU.setSupTot(rsTARSU.getObject("SUP_TOT") == null ? "-" : DF.format(rsTARSU.getDouble("SUP_TOT")));
					oggettoTARSU.setDatIni(rsTARSU.getObject("DAT_INI") == null ? "-" : SDF.format(rsTARSU.getDate("DAT_INI")));
					oggettoTARSU.setDatFin(rsTARSU.getObject("DAT_FIN") == null ? "ATTUALE" : 
						(SDF.format(rsTARSU.getDate("DAT_FIN")).equals("31/12/9999") ? "ATTUALE" : SDF.format(rsTARSU.getDate("DAT_FIN"))));
					oggettoTARSU.setProvenienza(rsTARSU.getObject("PROVENIENZA") == null ? "-" : rsTARSU.getString("PROVENIENZA"));
					setEvidenziaTARSUAttuale(conn, oggettoTARSU);
					
					oggettiTARSU.add(oggettoTARSU);
				}
				
				//Recupero la consulenza imu, se presente per il soggetto
				boolean trovato = false;
				int i = 0;
				String cf = null;
				while(!trovato && datiSoggetto.size()>0){
					cf = datiSoggetto.get(i).getCodFisc();
					if(cf!=null)
						trovato = true;
				}
				
				ConsulenzaImuLogic saldoImuLogic = new ConsulenzaImuLogic(this.envUtente);
				saldoImuLogic.caricaConsulenzaImu(cf,ht);
				
				ht.put("CONTR", datiSoggetto);
				ht.put("ICI_LIST", oggettiICI);
				ht.put("TARSU_LIST", oggettiTARSU);
			
			}
		
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = chiaveComposta;
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
	
	/*private void caricaConsulenzaImu(String cf, Hashtable ht) throws NamingException{
		
		SaldoImuConsulenzaDTO consulenza = null;
		SaldoImuElaborazioneDTO datiElab = null;
		
		if(cf!=null){
			Connection conn = null;
			EnvUtente eu = this.getEnvUtente();
			String enteId = eu.getEnte();
			String userId = eu.getUtente().getUsername();
			String sessionId = eu.getUtente().getSessionId();
			
			//TODO:Gestione permessi visualizzazione (abilitazione ente)
			
			Context cont = new InitialContext();
			SaldoImuService imu= (SaldoImuService)getEjb("CT_Service","CT_Service_Data_Access" , "SaldoImuServiceBean");  
			
			SaldoImuBaseDTO search = new SaldoImuBaseDTO();
			search.setEnteId(enteId);
			search.setUserId(userId);
			search.setSessionId(sessionId);
			search.setCodfisc(cf);
			
			consulenza = imu.getConsulenza(search);
			if(consulenza!=null)
				datiElab = imu.getJsonDTOUltimaElaborazione(search);
		}
		
		ht.put(this.IMU_CONS, consulenza);
		ht.put(this.IMU_ELAB, datiElab);
		
	}*/
	
	
	private ArrayList<String> getIndirizzoAnagrafe(Connection conn, String codFiscale) throws Exception {
		ArrayList<String> alAnagrafe = new ArrayList<String>();
		alAnagrafe.add(""); //default
		alAnagrafe.add(""); //default
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_INDIRIZZO_ANAGRAFE);
			pstmt.setString(1, codFiscale.trim());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				alAnagrafe = new ArrayList<String>();
				String viaSedime = rs.getObject("VIASEDIME") == null ? "" : rs.getString("VIASEDIME").trim();
				String viaDescrizione = rs.getObject("VIADESCRIZIONE") == null ? "" : rs.getString("VIADESCRIZIONE").trim();
				String civico = rs.getObject("CIVICO") == null ? "" : rs.getString("CIVICO").trim();
				String anagrafe = viaSedime;
				if (!viaDescrizione.equals("")) {
					if (!anagrafe.equals("")) {
						anagrafe += " ";
					}
					anagrafe += viaDescrizione;
				}
				if (!civico.equals("")) {
					if (!anagrafe.equals("")) {
						anagrafe += ", ";
					}
					anagrafe += civico;
				}				
				alAnagrafe.add(anagrafe);
				alAnagrafe.add(rs.getObject("DESCRIZIONE") == null ? "" : rs.getString("DESCRIZIONE").trim());
			}
			return alAnagrafe;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
	}
	
	private ArrayList<String> getIndirizzoSiatel(Connection conn, String codFiscale) throws Exception {
		ArrayList<String> alSiatel = new ArrayList<String>();
		alSiatel.add(""); //default
		alSiatel.add(""); //default
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			//verifico prima se esiste la tabella, o il sinonimo, MI_SIATEL_P_FIS
			pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_ESISTE_TAB_SIATEL);
			boolean tabSiatel = pstmt.executeQuery().next();
			if (!tabSiatel) {
				pstmt.cancel();
				pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_ESISTE_SYN_TAB_SIATEL);
				tabSiatel = pstmt.executeQuery().next();
			}
			pstmt.cancel();
			if (tabSiatel) {
				pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_INDIRIZZO_SIATEL);
				pstmt.setString(1, codFiscale.trim());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					alSiatel = new ArrayList<String>();
					alSiatel.add(rs.getObject("INDIRIZZO") == null ? "" : rs.getString("INDIRIZZO").trim());
					alSiatel.add(rs.getObject("COMUNE_RESIDENZA") == null ? "" : rs.getString("COMUNE_RESIDENZA").trim());
				}
			}
			return alSiatel;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
	}
	
	private void setEvidenziaICIAttuale(Connection conn, OggettiICINew oggettoICI) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int maxAnno = -1;
		try {
			String sqlICIAttuale = "SELECT YEA_DEN " +
						"FROM SIT_T_ICI_OGGETTO " +
						"WHERE LPAD(FOGLIO, 5, '0') = ? AND LPAD(NUMERO, 5, '0') = ? AND LPAD(SUB, 4, '0')= ?";
			pstmt = conn.prepareStatement(sqlICIAttuale);
			pstmt.setString(1, oggettoICI.getFoglio());
			pstmt.setString(2, oggettoICI.getNumero());
			pstmt.setString(3, oggettoICI.getSub());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String anno = rs.getObject("YEA_DEN") == null ? "-1" : rs.getString("YEA_DEN");
				int intAnno = -1;				
				try {
					intAnno = Integer.parseInt(anno);					
				} catch (Exception e) {}
				
				if (intAnno > maxAnno) {
					maxAnno = intAnno;
				}
			}
			oggettoICI.setEvidenzia(oggettoICI.getYeaDen().equalsIgnoreCase("" + maxAnno) &&
									oggettoICI.getFlgPos3112().equalsIgnoreCase("SI"));
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
	}
	
	private void setEvidenziaTARSUAttuale(Connection conn, OggettiTARSUNew oggettoTARSU) throws Exception {
		oggettoTARSU.setEvidenzia(oggettoTARSU.getDatFin().equalsIgnoreCase("ATTUALE"));
	}
	
	
	public String getDescViaByIdExt(String codVia, String tipo) throws Exception {
		String desc = null;
		Context cont;
		
		
		try {
		EnvUtente eu = super.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
				
		if("ICI".equals(tipo)){
			IciService iciService = (IciService)getEjb("CT_Service", "CT_Service_Data_Access", "IciServiceBean");
			
			RicercaViaIciDTO rv = new RicercaViaIciDTO();
			rv.setEnteId(enteId);
			rv.setUserId(userId);
			SitTIciVia dati = new SitTIciVia();
			dati.setIdExt(codVia);
			rv.setDatiVia(dati);
			
			SitTIciVia via = iciService.getViaByIdExt(rv);
			
		    desc = "-";
		    if(via != null && via.getDescrizione()!=null){
		    	desc = via.getPrefisso()!=null ? via.getPrefisso()+" ":"";
		    	desc+= via.getDescrizione();
		    }
			
			
		}else{
			TarsuService tarsuService = (TarsuService)getEjb("CT_Service", "CT_Service_Data_Access", "TarsuServiceBean");
			
			RicercaViaTarsuDTO rv = new RicercaViaTarsuDTO();
			rv.setEnteId(enteId);
			rv.setUserId(userId);
			SitTTarVia dati = new SitTTarVia();
			dati.setIdExt(codVia);
			rv.setDatiVia(dati);
			
			SitTTarVia via = tarsuService.getViaByIdExt(rv);
			
			desc = "-";
		    if(via!=null && via.getDescrizione()!=null){
		    	desc = via.getPrefisso()!=null ? via.getPrefisso()+" ":"";
		    	desc+= via.getDescrizione();
		    }
			
		}
		
		} catch (Exception e) {
			log.error("getDescViaByIdExt", e);
			throw new Exception("Impossibile reperire la descrizione della via " + codVia, e);
		}	
		
		return desc;
		
		
	}
	
}
