package it.escsolution.escwebgis.tributiNew.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.docfa.bean.Docfa;
import it.escsolution.escwebgis.tributiNew.bean.ContribuentiOggettoNew;
import it.escsolution.escwebgis.tributiNew.bean.OggettiICINew;
import it.escsolution.escwebgis.tributiNew.bean.OggettiICINewFinder;
import it.webred.utils.GenericTuples;
import it.webred.utils.StringUtils;

public class TributiOggettiICINewLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public TributiOggettiICINewLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
		
	public final static String ICI_DOCFA_COLLEGATI = "ICI_DOCFA_COLLEGATI@TributiOggettiICINewLogic";
	public final static String ICI = "ICI";
	public final static String SOLO_ATT = "SOLO_ATT@TributiOggettiICINewLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
		"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA from (" +
		"select distinct SIT_T_ICI_OGGETTO.ID, " + 
		"NVL(SIT_T_ICI_OGGETTO.YEA_DEN,'-') AS YEA_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.NUM_DEN,'-') AS NUM_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_ICI_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_ICI_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_ICI_OGGETTO.CAT,'-') AS CAT, " +
		"NVL(SIT_T_ICI_OGGETTO.CLS,'-') AS CLS, " +
		"SIT_T_ICI_OGGETTO.FLG_POS3112 AS FLG_POS3112, " +
		"NVL(SIT_T_ICI_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
		"from SIT_T_ICI_OGGETTO, SIT_T_ICI_VIA where 1 = ? and  SIT_T_ICI_OGGETTO.ID_EXT_VIA = SIT_T_ICI_VIA.ID_EXT(+) ";

	private final static String SQL_SELECT_LISTA_DA_CIV = "select * from (" +
	"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA from (" +
	"select distinct ogg.ID, " + 
	"NVL(ogg.YEA_DEN,'-') AS YEA_DEN, " +
	"NVL(ogg.NUM_DEN,'-') AS NUM_DEN, " +
	"NVL(ogg.FOGLIO,'-') AS FOGLIO, " +
	"NVL(ogg.NUMERO,'-') AS NUMERO, " +
	"NVL(ogg.SUB,'-') AS SUB, " +
	"NVL(ogg.CAT,'-') AS CAT, " +
	"NVL(ogg.CLS,'-') AS CLS, " +
	"ogg.FLG_POS3112 AS FLG_POS3112, " +
	"NVL(ogg.PROVENIENZA,'-') AS PROVENIENZA " +
	"from SIT_T_ICI_OGGETTO ogg, SIT_T_ICI_VIA via where via.id=? AND via.id_ext=ogg.id_ext_via AND ogg.num_civ=?))";
	
	private final static String SQL_SELECT_LISTA_DA_VIA = "select * from (" +
	"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA from (" +
	"select distinct ogg.ID, " + 
	"NVL(ogg.YEA_DEN,'-') AS YEA_DEN, " +
	"NVL(ogg.NUM_DEN,'-') AS NUM_DEN, " +
	"NVL(ogg.FOGLIO,'-') AS FOGLIO, " +
	"NVL(ogg.NUMERO,'-') AS NUMERO, " +
	"NVL(ogg.SUB,'-') AS SUB, " +
	"NVL(ogg.CAT,'-') AS CAT, " +
	"NVL(ogg.CLS,'-') AS CLS, " +
	"ogg.FLG_POS3112 AS FLG_POS3112, " +
	"NVL(ogg.PROVENIENZA,'-') AS PROVENIENZA " +
	"from SIT_T_ICI_OGGETTO ogg, SIT_T_ICI_VIA via where via.id=? AND via.id_ext=ogg.id_ext_via))";
	
	private final static String SQL_SELECT_LISTA_DA_OGG = "select * from (" +
	"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA from (" +
	"select distinct SIT_T_ICI_OGGETTO.ID, " + 
	"NVL(SIT_T_ICI_OGGETTO.YEA_DEN,'-') AS YEA_DEN, " +
	"NVL(SIT_T_ICI_OGGETTO.NUM_DEN,'-') AS NUM_DEN, " +
	"NVL(SIT_T_ICI_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
	"NVL(SIT_T_ICI_OGGETTO.NUMERO,'-') AS NUMERO, " +
	"NVL(SIT_T_ICI_OGGETTO.SUB,'-') AS SUB, " +
	"NVL(SIT_T_ICI_OGGETTO.CAT,'-') AS CAT, " +
	"NVL(SIT_T_ICI_OGGETTO.CLS,'-') AS CLS, " +
	"SIT_T_ICI_OGGETTO.FLG_POS3112 AS FLG_POS3112, " +
	"NVL(SIT_T_ICI_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
	"from SIT_T_ICI_OGGETTO where ID=? ))";
	
	private final static String SQL_SELECT_COUNT_LISTA_DA_VIA = "select count(*) as conteggio  from (" +
	"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA from (" +
	"select distinct ogg.ID, " + 
	"NVL(ogg.YEA_DEN,'-') AS YEA_DEN, " +
	"NVL(ogg.NUM_DEN,'-') AS NUM_DEN, " +
	"NVL(ogg.FOGLIO,'-') AS FOGLIO, " +
	"NVL(ogg.NUMERO,'-') AS NUMERO, " +
	"NVL(ogg.SUB,'-') AS SUB, " +
	"NVL(ogg.CAT,'-') AS CAT, " +
	"NVL(ogg.CLS,'-') AS CLS, " +
	"ogg.FLG_POS3112 AS FLG_POS3112, " +
	"NVL(ogg.PROVENIENZA,'-') AS PROVENIENZA " +
	"from SIT_T_ICI_OGGETTO ogg, SIT_T_ICI_VIA via where via.id=? AND via.id_ext=ogg.id_ext_via))";

	private final static String SQL_SELECT_LISTA_SOGG = "select * from (" +
		"select ROWNUM as N, ID, YEA_DEN, NUM_DEN, FOGLIO, NUMERO, SUB, CAT, CLS, FLG_POS3112, PROVENIENZA, COD_FISC, COG_DENOM, NOME, PART_IVA from (" +
		"select distinct SIT_T_ICI_OGGETTO.ID, " + 
		"NVL(SIT_T_ICI_OGGETTO.YEA_DEN,'-') AS YEA_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.NUM_DEN,'-') AS NUM_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_ICI_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_ICI_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_ICI_OGGETTO.CAT,'-') AS CAT, " +
		"NVL(SIT_T_ICI_OGGETTO.CLS,'-') AS CLS, " +
		"SIT_T_ICI_OGGETTO.FLG_POS3112 AS FLG_POS3112, " +
		"NVL(SIT_T_ICI_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA, " +
		"v.COD_FISC, v.COG_DENOM, v.NOME, v.PART_IVA " +
		"FROM sit_t_ici_oggetto, v_t_ici_sogg_all v, SIT_T_ICI_VIA " +
		"where 1 = ? " +
		"and  SIT_T_ICI_OGGETTO.ID_EXT_VIA = SIT_T_ICI_VIA.ID_EXT(+) " +
		"and sit_t_ici_oggetto.id_ext = v.id_ext_ogg_ici ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from (" +
		"select distinct SIT_T_ICI_OGGETTO.ID, " + 
		"NVL(SIT_T_ICI_OGGETTO.YEA_DEN,'-') AS YEA_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.NUM_DEN,'-') AS NUM_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_ICI_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_ICI_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_ICI_OGGETTO.CAT,'-') AS CAT, " +
		"NVL(SIT_T_ICI_OGGETTO.CLS,'-') AS CLS, " +
		"SIT_T_ICI_OGGETTO.FLG_POS3112 AS FLG_POS3112, " +
		"NVL(SIT_T_ICI_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
		"from SIT_T_ICI_OGGETTO, SIT_T_ICI_VIA " +
		"where 1 = ? " +
		"and  SIT_T_ICI_OGGETTO.ID_EXT_VIA = SIT_T_ICI_VIA.ID_EXT(+) ";
	
	private final static String SQL_SELECT_COUNT_LISTA_SOGG = "select count(*) as conteggio from (" +
		"select distinct SIT_T_ICI_OGGETTO.ID, " + 
		"NVL(SIT_T_ICI_OGGETTO.YEA_DEN,'-') AS YEA_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.NUM_DEN,'-') AS NUM_DEN, " +
		"NVL(SIT_T_ICI_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_ICI_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_ICI_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_ICI_OGGETTO.CAT,'-') AS CAT, " +
		"NVL(SIT_T_ICI_OGGETTO.CLS,'-') AS CLS, " +
		"SIT_T_ICI_OGGETTO.FLG_POS3112 AS FLG_POS3112, " +
		"NVL(SIT_T_ICI_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA, " +
		"v.COD_FISC, v.COG_DENOM, v.NOME, v.PART_IVA " +
		"FROM sit_t_ici_oggetto, v_t_ici_sogg_all v, SIT_T_ICI_VIA where 1 = ? " +
		"and  SIT_T_ICI_OGGETTO.ID_EXT_VIA = SIT_T_ICI_VIA.ID_EXT(+) " +
		"and sit_t_ici_oggetto.id_ext = v.id_ext_ogg_ici ";
	
	private final static String SQL_SELECT_DETTAGLIO = "select SIT_T_ICI_OGGETTO.*, " + 
								"DECODE(SIT_T_ICI_VIA.DESCRIZIONE, NULL, SIT_T_ICI_OGGETTO.DES_IND, (DECODE(SIT_T_ICI_VIA.PREFISSO, NULL, '', '', '', SIT_T_ICI_VIA.PREFISSO || ' ') || SIT_T_ICI_VIA.DESCRIZIONE)) AS DESC_VIA, " +
								"SIT_T_ICI_VIA.ID AS ID_VIA "+
								"from SIT_T_ICI_OGGETTO, SIT_T_ICI_VIA " + 
								"where SIT_T_ICI_OGGETTO.ID = ? " +	
								"and SIT_T_ICI_OGGETTO.ID_EXT_VIA = SIT_T_ICI_VIA.ID_EXT(+)";
	
	
	private final static String SQL_SELECT_CONTRIB_OGG = "select * from (" +
								"select 'Contribuente' as titolo, " +
								"sit_t_ici_sogg.id_ext, " +
								"sit_t_ici_sogg.id_orig, " +
								"sit_t_ici_sogg.cog_denom, " +
								"sit_t_ici_sogg.nome, " +
								"sit_t_ici_sogg.cod_fisc, " +
								"sit_t_ici_sogg.part_iva, " +
								"sit_t_ici_sogg.dt_nsc, " +
								"sit_t_ici_oggetto.yea_den, " +
								"sit_t_ici_oggetto.yea_rif, " +
								"sit_t_ici_oggetto.num_den, " +
								"sit_t_ici_oggetto.num_riga, " +
								"sit_t_ici_oggetto.mesi_pos, " +
								"sit_t_ici_oggetto.prc_poss, " +
								"sit_t_ici_oggetto.val_imm, " +
								"sit_t_ici_oggetto.flg_pos3112, " +
								"sit_t_ici_oggetto.tip_den, " +
								"sit_t_ici_sogg.provenienza, " +
								"sit_t_ici_oggetto.id as ID_OGG, " +
								"sit_t_ici_sogg.id as ID_SOGG " +
								"from sit_t_ici_sogg, sit_t_ici_contrib, sit_t_ici_oggetto " +
								"where lpad(sit_t_ici_oggetto.foglio, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.numero, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.sub, 4, '0') = lpad(?, 4, '0') " +
								"and sit_t_ici_sogg.id_ext = sit_t_ici_contrib.id_ext_sogg " +
								"and sit_t_ici_oggetto.id_ext = sit_t_ici_contrib.id_ext_ogg_ici " +
								"union all " +
								"select 'Dichiarante' as titolo, " +
								"sit_t_ici_sogg.id_ext, " +
								"sit_t_ici_sogg.id_orig, " +
								"sit_t_ici_sogg.cog_denom, " +
								"sit_t_ici_sogg.nome, " +
								"sit_t_ici_sogg.cod_fisc, " +
								"sit_t_ici_sogg.part_iva, " +
								"sit_t_ici_sogg.dt_nsc, " +
								"sit_t_ici_oggetto.yea_den, " +
								"sit_t_ici_oggetto.yea_rif, " +
								"sit_t_ici_oggetto.num_den, " +
								"sit_t_ici_oggetto.num_riga, " +
								"sit_t_ici_oggetto.mesi_pos, " +
								"sit_t_ici_oggetto.prc_poss, " +
								"sit_t_ici_oggetto.val_imm, " +
								"sit_t_ici_oggetto.flg_pos3112, " +
								"sit_t_ici_oggetto.tip_den, " +
								"sit_t_ici_sogg.provenienza, " +
								"sit_t_ici_oggetto.id as ID_OGG, " +
								"sit_t_ici_sogg.id as ID_SOGG " +
								"from sit_t_ici_sogg, sit_t_ici_dich, sit_t_ici_oggetto " +
								"where lpad(sit_t_ici_oggetto.foglio, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.numero, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.sub, 4, '0') = lpad(?, 4, '0') " +
								"and sit_t_ici_sogg.id_ext = sit_t_ici_dich.id_ext_sogg " +
								"and sit_t_ici_oggetto.id_ext = sit_t_ici_dich.id_ext_ogg_ici " +
								"union all " +
								"select 'Contitolare' as titolo, " +
								"sit_t_ici_sogg.id_ext, " +
								"sit_t_ici_sogg.id_orig, " +
								"sit_t_ici_sogg.cog_denom, " +
								"sit_t_ici_sogg.nome, " +
								"sit_t_ici_sogg.cod_fisc, " +
								"sit_t_ici_sogg.part_iva, " +
								"sit_t_ici_sogg.dt_nsc, " +
								"sit_t_ici_oggetto.yea_den, " +
								"sit_t_ici_oggetto.yea_rif, " +
								"sit_t_ici_oggetto.num_den, " +
								"sit_t_ici_oggetto.num_riga, " +
								"sit_t_ici_oggetto.mesi_pos, " +
								"sit_t_ici_oggetto.prc_poss, " +
								"sit_t_ici_oggetto.val_imm, " +
								"sit_t_ici_oggetto.flg_pos3112, " +
								"sit_t_ici_oggetto.tip_den, " +
								"sit_t_ici_sogg.provenienza, " +
								"sit_t_ici_oggetto.id as ID_OGG, " +
								"sit_t_ici_sogg.id as ID_SOGG " +
								"from sit_t_ici_sogg, sit_t_ici_contit, sit_t_ici_oggetto " +
								"where lpad(sit_t_ici_oggetto.foglio, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.numero, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.sub, 4, '0') = lpad(?, 4, '0') " +
								"and sit_t_ici_sogg.id_ext = sit_t_ici_contit.id_ext_sogg " +
								"and sit_t_ici_oggetto.id_ext = sit_t_ici_contit.id_ext_ogg_ici " +
								"union all " +
								"select * from (" +
								"select sit_t_ici_ogg_ultsogg.tit_sogg as titolo, " +
								"sit_t_ici_sogg.id_ext, " +
								"sit_t_ici_sogg.id_orig, " +
								"sit_t_ici_sogg.cog_denom, " +
								"sit_t_ici_sogg.nome, " +	
								"sit_t_ici_sogg.cod_fisc, " +
								"sit_t_ici_sogg.part_iva, " +
								"sit_t_ici_sogg.dt_nsc, " +
								"sit_t_ici_oggetto.yea_den, " +
								"sit_t_ici_oggetto.yea_rif, " +
								"sit_t_ici_oggetto.num_den, " +
								"sit_t_ici_oggetto.num_riga, " +
								"sit_t_ici_oggetto.mesi_pos, " +
								"sit_t_ici_oggetto.prc_poss, " +
								"sit_t_ici_oggetto.val_imm, " +
								"sit_t_ici_oggetto.flg_pos3112, " +
								"sit_t_ici_oggetto.tip_den, " +
								"sit_t_ici_sogg.provenienza, " +
								"sit_t_ici_oggetto.id as ID_OGG, " +
								"sit_t_ici_sogg.id as ID_SOGG " +
								"from sit_t_ici_sogg, sit_t_ici_ult_sogg, sit_t_ici_ogg_ultsogg, sit_t_ici_oggetto " +
								"where lpad(sit_t_ici_oggetto.foglio, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.numero, 5, '0') = lpad(?, 5, '0') " +
								"and lpad(sit_t_ici_oggetto.sub, 4, '0') = lpad(?, 4, '0') " +
								"and sit_t_ici_ogg_ultsogg.id_ext_ici_ult_sogg = sit_t_ici_ult_sogg.id_ext " +
								"and sit_t_ici_sogg.id_ext = sit_t_ici_ult_sogg.id_ext_sogg " +
								"and sit_t_ici_oggetto.id_ext = sit_t_ici_ult_sogg.id_ext_ogg_ici " +
								"order by titolo)) " +
								"order by yea_den desc, decode(flg_pos3112, '1', 'SI', 'S', 'SI', 'NO') desc";
	
	private final static String SQL_ENTE = "SELECT * FROM SIT_ENTE";
	
	boolean soloAtt = false;
	
	
	public Hashtable mCaricareListaOggettiICICiv(String oggettoSel, OggettiICINewFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		java.sql.ResultSet rs = null;
		
		
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			
			
			this.initialize();
			
			StringTokenizer st = new StringTokenizer(oggettoSel, "|");
			String id = st.nextToken();
			String civ = st.nextToken();
			
			this.setString(1,id);
			this.setString(2,civ);
			
			prepareStatement(SQL_SELECT_LISTA_DA_CIV );
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ResultSet rsEnte = null;
			
			while (rs.next()){
				OggettiICINew ici = new OggettiICINew();
				String codEnte = "";
				try {
					rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
					while (rsEnte.next()) {
						codEnte = rsEnte.getString("codent");
					}
				}
				finally {
					if (rsEnte != null)
						rsEnte.close();
				}
				ici.setCodEnte(codEnte);
				ici.setChiave(rs.getString("ID"));
				ici.setFoglio(rs.getString("FOGLIO"));
				ici.setNumero(rs.getString("NUMERO"));
				ici.setSub(rs.getString("SUB"));
				ici.setCat(rs.getString("CAT"));
				ici.setCls(rs.getString("CLS"));
				ici.setProvenienza(rs.getString("PROVENIENZA"));
				ici.setNumDen(rs.getString("NUM_DEN"));
				ici.setYeaDen(rs.getString("YEA_DEN"));
				ici.setFlgPos3112(rs.getObject("FLG_POS3112") == null ? "NO" : 
				(rs.getString("FLG_POS3112").equalsIgnoreCase("1") || rs.getString("FLG_POS3112").equalsIgnoreCase("S") ? "SI" : "NO"));
					setEvidenziaAttuale(conn, ici);
						
				GenericTuples.T2<String,String> coord = null;
				try {
					coord = getLatitudeLongitude(ici.getFoglio(), Utils.fillUpZeroInFront(ici.getNumero(),5),ici.getCodEnte());
				} catch (Exception e) {}
				if (coord!=null) {
				  ici.setLatitudine(coord.firstObj);
				  ici.setLongitudine(coord.secondObj);
				}
				vct.add(ici);						
			}			
			
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_ICI",vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = oggettoSel;
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
			if (rs !=null)
				rs.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Hashtable mCaricareListaOggettiICIVia(String oggettoSel, OggettiICINewFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		java.sql.ResultSet rs = null;
		
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			
			/*for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
				if (i==0) sql= SQL_SELECT_COUNT_LISTA_DA_VIA;
				else if (i==1)*/
			
			
					sql= SQL_SELECT_LISTA_DA_VIA;
				
			this.initialize();
			
			
			this.setString(1,oggettoSel);
		/*	
			long limInf, limSup;
			limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
			limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
			
			if (i==1) sql = sql + "  where N > " + limInf + " and N <=" + limSup;*/
			
			prepareStatement(sql );
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			String codEnte = "";
			ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
			while (rsEnte.next()) {
				codEnte = rsEnte.getString("codent");
			}
			rsEnte.close();
			
			//if (i==1){
				while (rs.next()){
					OggettiICINew ici = new OggettiICINew();
					
					ici.setCodEnte(codEnte);
					ici.setChiave(rs.getString("ID"));
					ici.setFoglio(rs.getString("FOGLIO"));
					ici.setNumero(rs.getString("NUMERO"));
					ici.setSub(rs.getString("SUB"));
					ici.setCat(rs.getString("CAT"));
					ici.setCls(rs.getString("CLS"));
					ici.setProvenienza(rs.getString("PROVENIENZA"));
					ici.setNumDen(rs.getString("NUM_DEN"));
					ici.setYeaDen(rs.getString("YEA_DEN"));
					ici.setFlgPos3112(rs.getObject("FLG_POS3112") == null ? "NO" : 
					(rs.getString("FLG_POS3112").equalsIgnoreCase("1") || rs.getString("FLG_POS3112").equalsIgnoreCase("S") ? "SI" : "NO"));
						setEvidenziaAttuale(conn, ici);
							
					GenericTuples.T2<String,String> coord = null;
					try {
						coord = getLatitudeLongitude(ici.getFoglio(), Utils.fillUpZeroInFront(ici.getNumero(),5),ici.getCodEnte());
					} catch (Exception e) {}
					if (coord!=null) {
					  ici.setLatitudine(coord.firstObj);
					  ici.setLongitudine(coord.secondObj);
					}
					vct.add(ici);						
				}		
				/*
			}else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}*/
			
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_ICI",vct);
			
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			//finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setPagineTotali(1);
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = oggettoSel;
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
			if (rs !=null)
				rs.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Hashtable mCaricareListaOggettiICIOggetto(String oggettoSel, OggettiICINewFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		java.sql.ResultSet rs = null;
		
		
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			
			
			this.initialize();
			
			this.setString(1,oggettoSel);
			
			prepareStatement(SQL_SELECT_LISTA_DA_OGG );
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ResultSet rsEnte = null;
			
			while (rs.next()){
				OggettiICINew ici = new OggettiICINew();
				String codEnte = "";
				try {
					rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
					while (rsEnte.next()) {
						codEnte = rsEnte.getString("codent");
					}
				}
				finally {
					if (rsEnte != null)
						rsEnte.close();
				}
				ici.setCodEnte(codEnte);
				ici.setChiave(rs.getString("ID"));
				ici.setFoglio(rs.getString("FOGLIO"));
				ici.setNumero(rs.getString("NUMERO"));
				ici.setSub(rs.getString("SUB"));
				ici.setCat(rs.getString("CAT"));
				ici.setCls(rs.getString("CLS"));
				ici.setProvenienza(rs.getString("PROVENIENZA"));
				ici.setNumDen(rs.getString("NUM_DEN"));
				ici.setYeaDen(rs.getString("YEA_DEN"));
				ici.setFlgPos3112(rs.getObject("FLG_POS3112") == null ? "NO" : 
				(rs.getString("FLG_POS3112").equalsIgnoreCase("1") || rs.getString("FLG_POS3112").equalsIgnoreCase("S") ? "SI" : "NO"));
					setEvidenziaAttuale(conn, ici);
						
				GenericTuples.T2<String,String> coord = null;
				try {
					coord = getLatitudeLongitude(ici.getFoglio(), Utils.fillUpZeroInFront(ici.getNumero(),5),ici.getCodEnte());
				} catch (Exception e) {}
				if (coord!=null) {
				  ici.setLatitudine(coord.firstObj);
				  ici.setLongitudine(coord.secondObj);
				}
				vct.add(ici);						
			}			
			
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_ICI",vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = oggettoSel;
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
			if (rs !=null)
				rs.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Hashtable mCaricareListaOggettiICI(Vector listaPar, OggettiICINewFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		java.sql.ResultSet rs = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			
			
			boolean hasParametriSoggetto = hasParametriSoggetto(listaPar);
			
			for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
				if (i==0)
					sql = hasParametriSoggetto ? SQL_SELECT_COUNT_LISTA_SOGG : SQL_SELECT_COUNT_LISTA;
				else
					sql = hasParametriSoggetto ? SQL_SELECT_LISTA_SOGG : SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;
				
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					sql = sql + " and SIT_T_ICI_OGGETTO.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by LPAD(FOGLIO, 5, '0'), LPAD(NUMERO, 5, '0'), LPAD(SUB, 4, '0'), YEA_DEN DESC)) where N > " + limInf + " and N <=" + limSup;
				else if (i == 0) sql = sql + ")";

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						OggettiICINew ici = new OggettiICINew();
						String codEnte = "";
						ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
						while (rsEnte.next()) {
							codEnte = rsEnte.getString("codent");
						}
						ici.setCodEnte(codEnte);
						ici.setChiave(rs.getString("ID"));
						ici.setFoglio(rs.getString("FOGLIO"));
						ici.setNumero(rs.getString("NUMERO"));
						ici.setSub(rs.getString("SUB"));
						ici.setCat(rs.getString("CAT"));
						ici.setCls(rs.getString("CLS"));
						ici.setProvenienza(rs.getString("PROVENIENZA"));
						ici.setNumDen(rs.getString("NUM_DEN"));
						ici.setYeaDen(rs.getString("YEA_DEN"));
						ici.setFlgPos3112(rs.getObject("FLG_POS3112") == null ? "NO" : 
							(rs.getString("FLG_POS3112").equalsIgnoreCase("1") || rs.getString("FLG_POS3112").equalsIgnoreCase("S") ? "SI" : "NO"));
						setEvidenziaAttuale(conn, ici);
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(ici.getFoglio(), Utils.fillUpZeroInFront(ici.getNumero(),5),ici.getCodEnte());
						} catch (Exception e) {
						}
						if (coord!=null) {
							ici.setLatitudine(coord.firstObj);
							ici.setLongitudine(coord.secondObj);
						}
						
						vct.add(ici);						
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_ICI",vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER",finder);
			
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
			if (rs !=null)
				rs.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Hashtable mCaricareDettaglioOggettiICI(String chiave) throws Exception{
		
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		OggettiICINew ici = new OggettiICINew();
		try {
			if(chiave != null && !chiave.equals("")) {

				String codEnte = recuperaCodNazionale(null);
				
				conn = this.getConnection();
				
				this.initialize();
				String sql = SQL_SELECT_DETTAGLIO;
				
				this.setString(1,chiave);

				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
				ArrayList<ContribuentiOggettoNew> contrOggList = new ArrayList<ContribuentiOggettoNew>();

				if (rs.next()) {
					ici.setChiave(rs.getString("ID"));					
					ici.setCodEnte(codEnte);
					ici.setSez(rs.getObject("SEZ") == null ? "-" : (rs.getString("SEZ").trim().equals("") ? "-" : rs.getString("SEZ")));
					ici.setFoglio(rs.getObject("FOGLIO") == null ? "-" : rs.getString("FOGLIO"));
					ici.setNumero(rs.getObject("NUMERO") == null ? "-" : rs.getString("NUMERO"));
					ici.setSub(rs.getObject("SUB") == null ? "-" : rs.getString("SUB"));
					ici.setYeaDen(rs.getObject("YEA_DEN") == null ? "-" : rs.getString("YEA_DEN"));
					ici.setNumDen(rs.getObject("NUM_DEN") == null ? "-" : rs.getString("NUM_DEN"));
					ici.setCat(rs.getObject("CAT") == null ? "-" : rs.getString("CAT"));
					ici.setCls(rs.getObject("CLS") == null ? "-" : rs.getString("CLS"));
					ici.setValImm(rs.getObject("VAL_IMM") == null ? "-" : DF.format(rs.getDouble("VAL_IMM")));
					
					String flgAbiPri3112 = "NO";
					if (rs.getObject("FLG_ABI_PRI3112") != null && 
						(rs.getString("FLG_ABI_PRI3112").trim().equals("1") || rs.getString("FLG_ABI_PRI3112").trim().equalsIgnoreCase("S"))) {
						flgAbiPri3112 = "SI";
					}
					ici.setFlgAbiPri3112(flgAbiPri3112);
					
					String desInd = rs.getObject("DESC_VIA") == null ? null : rs.getString("DESC_VIA");
					String numCiv ="";
					String espCiv ="";
					if (desInd == null) {
						desInd = "-";
					} else {
						numCiv = rs.getObject("NUM_CIV") == null ? null : rs.getString("NUM_CIV");
						ici.setNumCiv(numCiv);
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							try {
								numCiv = "" + Integer.parseInt(numCiv.trim());
							} catch (Exception e) {}
							 espCiv = rs.getObject("ESP_CIV") == null ? null : rs.getString("ESP_CIV");
							 ici.setEspCiv(espCiv);
							if (espCiv != null && !espCiv.trim().equals("")) {
								numCiv += "/" + espCiv;
							}
							if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
								desInd += " " + numCiv;
							}
						}						
					}	
					
					ici.setDesInd(desInd);
									
					ici.setIdVia(rs.getObject("ID_VIA") == null ? "-" : rs.getString("ID_VIA"));
					
					GenericTuples.T2<String,String> coord = null;
					try {
						coord = getLatitudeLongitude(ici.getFoglio(), Utils.fillUpZeroInFront(ici.getNumero(),5),ici.getCodEnte());
					} catch (Exception e) {
					}
					if (coord!=null) {
						ici.setLatitudine(coord.firstObj);
						ici.setLongitudine(coord.secondObj);
					}
					
					if(ici.getFoglio() != null && !ici.getFoglio().trim().equals("") && !ici.getFoglio().trim().equals("-")
							&& ici.getNumero() != null && !ici.getNumero().trim().equals("") && !ici.getNumero().trim().equals("-")
							/*&& ici.getSub() != null && !ici.getSub().trim().equals("") && !ici.getSub().trim().equals("-") && !ici.getSub().trim().equals("0000")*/)
					{
						PreparedStatement stm2 =null;
						ResultSet rs2 =null;
						try
						{							
							setIndirizzoCatastale(ici, conn, stm2, rs2);
							
							sql = "SELECT DISTINCT S.PREFISSO || ' ' || S.NOME || ' ' || c.civico as indirizzo " +
							"                  FROM sitiuiu u, siticivi_uiu iu, siticivi c, SITIDSTR S " +
							"                 WHERE iu.pkid_uiu = u.pkid_uiu " +
							"                   AND iu.pkid_civi = c.pkid_civi " +
							"                   and c.pkid_stra=s.pkid_stra " +
							"                   AND EXISTS (SELECT 1  FROM SITICOMU C WHERE C.CODI_FISC_LUNA =? AND C.COD_NAZIONALE = U.COD_NAZIONALE) " +
							"                   AND u.foglio = ? " +
							"                   AND u.particella = ? " ;
							stm2 = conn.prepareStatement(sql);
							stm2.setString(1, codEnte);
							stm2.setInt(2, ici.getFoglio().equals("-") ? 0 : Integer.parseInt(ici.getFoglio()));
							stm2.setString(3, StringUtils.padding(ici.getNumero(), 5, '0', true));
							rs2 = stm2.executeQuery();
							while(rs2.next())
							{
								if(rs2.getString("indirizzo") != null)
									ici.setDesIndViario(ici.getDesIndViario() != null ? ici.getDesIndViario()+"<br>"+rs2.getString("indirizzo"):rs2.getString("indirizzo") );
							}
							if(ici.getDesIndViario() == null)
								ici.setDesIndViario("non trovato");
							
							stm2.cancel();
							
							//storico contribuenti oggetto
							sql = SQL_SELECT_CONTRIB_OGG;
							stm2 = conn.prepareStatement(sql);
							stm2.setString(1, ici.getFoglio().equals("-") ? "0" : ici.getFoglio());
							stm2.setString(2, ici.getNumero().equals("-") ? "0" : ici.getNumero());
							stm2.setString(3, ici.getSub().equals("-") ? "0" : ici.getSub());
							stm2.setString(4, ici.getFoglio().equals("-") ? "0" : ici.getFoglio());
							stm2.setString(5, ici.getNumero().equals("-") ? "0" : ici.getNumero());
							stm2.setString(6, ici.getSub().equals("-") ? "0" : ici.getSub());
							stm2.setString(7, ici.getFoglio().equals("-") ? "0" : ici.getFoglio());
							stm2.setString(8, ici.getNumero().equals("-") ? "0" : ici.getNumero());
							stm2.setString(9, ici.getSub().equals("-") ? "0" : ici.getSub());
							stm2.setString(10, ici.getFoglio().equals("-") ? "0" : ici.getFoglio());
							stm2.setString(11, ici.getNumero().equals("-") ? "0" : ici.getNumero());
							stm2.setString(12, ici.getSub().equals("-") ? "0" : ici.getSub());
							rs2 = stm2.executeQuery();
							while(rs2.next()) {
								ContribuentiOggettoNew contrOgg = new ContribuentiOggettoNew();
								contrOgg.setTitSogg(rs2.getObject("TITOLO") == null ? "-" : rs2.getString("TITOLO"));
								contrOgg.setChiave("ID_EXT|" + (rs2.getObject("ID_EXT") == null ? "-" : rs2.getString("ID_EXT")));
								String cogDenom = rs2.getObject("COG_DENOM") == null ? "" : rs2.getString("COG_DENOM");
								String nome = rs2.getObject("NOME") == null ? "" : rs2.getString("NOME");
								String nominativo = cogDenom.trim();
								if (!nome.trim().equals("")) {
									if (!nominativo.trim().equals("")) {
										nominativo += " ";
									}
									nominativo += nome.trim();
								}
								if (nominativo.trim().equals("")) {
									nominativo = "-";
								}
								contrOgg.setNominativo(nominativo);
								contrOgg.setNome(nome);
								contrOgg.setCognome(cogDenom);
								contrOgg.setCodiceFiscale( rs2.getObject("COD_FISC") == null ? "" : rs2.getString("COD_FISC"));
								contrOgg.setPartitaIva(rs2.getObject("PART_IVA") == null ? "" : rs2.getString("PART_IVA"));
								String dataNascita="";
								DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
								Date dataNascitaD=  rs2.getDate("DT_NSC");
								if (dataNascitaD!= null)
									dataNascita= df.format(dataNascitaD);
								contrOgg.setDataNascita(dataNascita);
								contrOgg.setYeaDen(rs2.getObject("YEA_DEN") == null ? "-" : rs2.getString("YEA_DEN"));
								contrOgg.setYeaRif(rs2.getObject("YEA_RIF") == null ? "-" : rs2.getString("YEA_RIF"));
								contrOgg.setNumDen(rs2.getObject("NUM_DEN") == null ? "-" : rs2.getString("NUM_DEN"));
								contrOgg.setNumRiga(rs2.getObject("NUM_RIGA") == null ? "-" : DF.format(rs2.getDouble("NUM_RIGA")));
								contrOgg.setMesiPos(rs2.getObject("MESI_POS") == null ? "-" : DF.format(rs2.getDouble("MESI_POS")));
								String prcPoss = rs2.getObject("PRC_POSS") == null ? "" : DF.format(rs2.getDouble("PRC_POSS"));
								if (prcPoss.trim().equals("")) {
									prcPoss = "-";
								} else {
									prcPoss = prcPoss.trim() + " %";
								}
								contrOgg.setPrcPoss(prcPoss);
								contrOgg.setValImm(rs2.getObject("VAL_IMM") == null ? "-" : DF.format(rs2.getDouble("VAL_IMM")));
								String flgPos3112 = "NO";
								if (rs2.getObject("FLG_POS3112") != null && 
									(rs2.getString("FLG_POS3112").trim().equals("1") || rs2.getString("FLG_POS3112").trim().equalsIgnoreCase("S"))) {
									flgPos3112 = "SI";
								}
								contrOgg.setFlgPos3112(flgPos3112);
								contrOgg.setTipDen(rs2.getObject("TIP_DEN") == null ? "-" : rs2.getString("TIP_DEN"));
								contrOgg.setProvenienza(rs2.getObject("PROVENIENZA") == null ? "-" : rs2.getString("PROVENIENZA"));
								contrOgg.setIdOgg(rs2.getString("ID_OGG"));
								contrOgg.setIdSogg(rs2.getString("ID_SOGG"));
								contrOggList.add(contrOgg);
							}
						}
						catch(Exception exp2)
						{
							log.error(exp2.getMessage(),exp2);
						}
						finally
						{
							try
							{
								if(stm2 != null)
									stm2.close();
								if(rs2 != null)
									rs2.close();
							}
							catch(Exception non){}
						}
						
					}
				}
				
				ht.put("ICI",ici);
				ht.put("CONTR_LIST", filter(contrOggList));
			}
			
			// docfa collegati
			if
			(
					(ici.getFoglio() != null && !ici.getFoglio().equals("") && !ici.getFoglio().equals("-")) &&
					(ici.getNumero() != null && !ici.getNumero().equals("") && !ici.getNumero().equals("-")) &&
					(ici.getSub() != null && !ici.getSub().equals("") && !ici.getSub().equals("-"))

			)
			{
				//cerco dati 
				sql = "SELECT protocollo_registrazione protocollo,  fornitura,categoria,classe,rendita_euro FROM DOCFA_DATI_CENSUARI U WHERE  "+
						"  u.foglio = lpad(?,4,'0') "+
						"  AND u.numero = lpad(?,5,'0') "+
						"  AND u.subalterno = lpad(?,4,'0')"+
						"  AND to_char(u.fornitura,'yyyymm')=substr(data_registrazione,0,6) ";
						

				this.initialize();
				this.setString(1,ici.getFoglio().trim());
				this.setString(2,ici.getNumero().trim());
				this.setString(3,ici.getSub().trim());
				prepareStatement(sql);
				java.sql.ResultSet rsDocfa = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				ArrayList docfaCollegati = new ArrayList();
				while (rsDocfa.next())
				{
					Docfa docfa = new Docfa();
					docfa.setProtocollo(tornaValoreRS(rsDocfa,"protocollo"));
					docfa.setFornitura(tornaValoreRS(rsDocfa,"fornitura","YYYY-MM-DD"));
					docfa.setCategoria(tornaValoreRS(rsDocfa,"categoria"));
					docfa.setClasse(tornaValoreRS(rsDocfa,"classe"));
					docfa.setRendita(tornaValoreRS(rsDocfa,"rendita_euro"));
					docfaCollegati.add(docfa);
				}
				ht.put(ICI_DOCFA_COLLEGATI,docfaCollegati);
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
		Vector listaParOgg = new Vector();
		Vector listaParSogg = new Vector();
		soloAtt = false;
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if ("SOLO_ATT".equalsIgnoreCase(el.getAttributeName())) {
				soloAtt = "Y".equalsIgnoreCase(el.getValore());
			} else {
				if (isParametroOggetto(el)) {
					listaParOgg.add(el);
				} else {
					listaParSogg.add(el);
				}
			}
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParOgg);
		indice = getCurrentParameterIndex();
		
		if (listaParSogg.size() > 0) {
			Vector listaParSoggQuery = new Vector();
			String[] titoliSogg = null;
			for (int i = 0; i < listaParSogg.size(); i++) {
				EscElementoFiltro el = (EscElementoFiltro)listaParSogg.get(i);
				String attName = el.getAttributeName();
				if ("TIT_SOGG".equalsIgnoreCase(attName)) {
					titoliSogg = el.getValori();
				} else {
					listaParSoggQuery.add(el);
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
				
				indice = getCurrentParameterIndex();
				addOr = true;
			}
			if (dic) {
				if (addOr) {
					sqlAdd += " OR ";
				}
				
				sqlAdd += "v.TIPO_SOGGETTO = 'Dichiarante'";
		
				indice = getCurrentParameterIndex();
				addOr = true;
			}
			if (ctt) {
				if (addOr) {
					sqlAdd += " OR ";
				}
				
				sqlAdd += "v.TIPO_SOGGETTO = 'Contitolare'";
				
				indice = getCurrentParameterIndex();
				addOr = true;
			}
			if (ult) {
				if (addOr) {
					sqlAdd += " OR ";
				}

				sqlAdd += "v.TIPO_SOGGETTO = 'Ulteriore Soggetto'";
				
				indice = getCurrentParameterIndex();
				addOr = true;
			}
			
			if (!"".equals(sqlAdd)) {
				sqlAdd = " and (" + sqlAdd + ")";
			}
			
			sqlAdd += super.elaboraFiltroMascheraRicercaParziale(indice, listaParSoggQuery);

			sql += sqlAdd;
		}
		
		if (soloAtt) {
			sql += " and SIT_T_ICI_OGGETTO.FLG_POS3112 in ('1', 'S') ";
			sql += "and lpad(SIT_T_ICI_OGGETTO.YEA_DEN, 4, '0') = ";
			sql += "(select max(lpad(yea_den, 4, '0')) from sit_t_ici_oggetto cfrogg ";
			sql += "where lpad(cfrogg.foglio, 5, '0') = lpad(sit_t_ici_oggetto.foglio, 5, '0') ";
			sql += "and lpad(cfrogg.numero, 5, '0') = lpad(sit_t_ici_oggetto.numero, 5, '0') ";
			sql += "and lpad(cfrogg.sub, 4, '0') = lpad(sit_t_ici_oggetto.sub, 4, '0'))";
		}
		
		return sql;
	}
	
	private boolean isParametroOggetto(EscElementoFiltro el) {
		String attName = el.getAttributeName();
		//problema degli zeri iniziali
		if ("FOGLIO".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_ICI_OGGETTO.FOGLIO, 5, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 5, '0', true));
			}
		} else if ("NUMERO".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_ICI_OGGETTO.NUMERO, 5, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 5, '0', true));
			}
		} else if ("SUB".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_ICI_OGGETTO.SUB, 4, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 4, '0', true));
			}
		}else if ("NUM_CIV".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_ICI_OGGETTO.NUM_CIV, 7, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 7, '0', true));
			}
		}
		return "YEA_DEN".equalsIgnoreCase(attName) ||
				"FOGLIO".equalsIgnoreCase(attName) ||
				"NUMERO".equalsIgnoreCase(attName) ||
				"SUB".equalsIgnoreCase(attName) ||
				"CAT".equalsIgnoreCase(attName) ||
				"CLS".equalsIgnoreCase(attName);
	}
	
	private boolean hasParametriSoggetto(Vector listaPar) {
		if (listaPar == null) {
			return false;
		}
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			String attName = el.getAttributeName();
			if ("TIT_SOGG".equalsIgnoreCase(attName)) {
				if (el.getValori() != null && el.getValori().length > 0) {
					return true;
				}
			} else {
				if (!"".equals(el.getValore())) {					
					if ("COD_FISC".equalsIgnoreCase(attName) ||
						"COG_DENOM".equalsIgnoreCase(attName) ||
						"NOME".equalsIgnoreCase(attName) ||
						"PART_IVA".equalsIgnoreCase(attName)) {
						return true;
					}
				}
			}			
		}
		return false;
	}
	
	private void setIndirizzoCatastale(OggettiICINew ici, Connection conn, PreparedStatement stm2, ResultSet rs2) throws Exception {
		
		//PRIMA RICERCA PER FOGLIO / PARTICELLA / SUBALTERNO
		//SE NON TROVATO NULLA, SECONDA RICERCA PER FOGLIO / PARTICELLA
		//SI PRENDE L'ULTIMO INDIRIZZO COLLEGATO ALL'IMMOBILE E SI ESCLUDONO DALLA QUERY LE UIU CESSATE
		ici.setDesIndCatastale(null);
		
		sql = "SELECT DISTINCT ind.load_id, t.descr || ' ' || ind.ind  || ' ' || remove_lead_zero (ind.civ1) AS indirizzo " +
		"           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t " +
		"          WHERE c.codi_fisc_luna = ? " +
		"            AND ID.codi_fisc_luna = c.codi_fisc_luna " +
		"            AND ID.sez = c.sezione_censuaria " +
		"            AND ID.foglio = LPAD (?, 4, '0') " +
		"            AND ID.mappale = LPAD (?, 5, '0') " +
		"            AND nvl(ID.sub,'0000') = LPAD (?, 4, '0') " +
		"            AND ind.codi_fisc_luna = c.codi_fisc_luna " +
		"            AND ind.sezione = ID.sezione " +
		"            AND ind.id_imm = ID.id_imm " +
		"            AND ind.progressivo = ID.progressivo " +
		"            AND t.pk_id = ind.toponimo " +
		"            AND NOT EXISTS (" +
		"			(SELECT 1 FROM sitiuiu " +
		"			WHERE sitiuiu.cod_nazionale = c.cod_nazionale " +
		"			and LPAD (sitiuiu.foglio, 4, '0')  = ID.foglio " +
		"			and LPAD (sitiuiu.particella, 5, '0') = ID.mappale " +
		"			and sitiuiu.sub = ' ' " +
		"			and LPAD (sitiuiu.unimm, 4, '0') = nvl(ID.sub,'0000') " +
		"			and ((data_inizio_val < sysdate and data_fine_val > sysdate) " +
		"			or not exists " +
		"			(SELECT 1 FROM sitiuiu b where b.pkid_uiu <> sitiuiu.pkid_uiu " + 
		"			and b.cod_nazionale = sitiuiu.cod_nazionale " +
		"			and b.foglio = sitiuiu.foglio " + 
		"			and b.particella = sitiuiu.particella " + 
		"			and b.sub = sitiuiu.sub " + 
		"			and b.unimm = sitiuiu.unimm " + 
		"			and (b.data_inizio_val < sysdate and b.data_fine_val > sysdate))) " + 
		"			and sitiuiu.ide_muta_fine is not null))" +		
		"			ORDER BY ind.load_id";
		stm2 = conn.prepareStatement(sql);
		stm2.setString(1, ici.getCodEnte());
		stm2.setString(2, ici.getFoglio());
		stm2.setString(3, ici.getNumero());
		stm2.setString(4, ici.getSub());
		rs2 =  stm2.executeQuery();
		while(rs2.next()) {
			//scorre tutto e prende l'ultimo (quello con load_id più alto, secondo l'order by)
			if(rs2.getString("indirizzo") != null) {
				ici.setDesIndCatastale(rs2.getString("indirizzo"));				
			}				
		}
		if(ici.getDesIndCatastale() == null) {
			stm2.cancel();
			sql = "SELECT DISTINCT ind.load_id, t.descr || ' ' || ind.ind  || ' ' || remove_lead_zero (ind.civ1) AS indirizzo " +
			"           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t " +
			"          WHERE c.codi_fisc_luna = ? " +
			"            AND ID.codi_fisc_luna = c.codi_fisc_luna " +
			"            AND ID.sez = c.sezione_censuaria " +
			"            AND ID.foglio = LPAD (?, 4, '0') " +
			"            AND ID.mappale = LPAD (?, 5, '0') " +
			"            AND ind.codi_fisc_luna = c.codi_fisc_luna " +
			"            AND ind.sezione = ID.sezione " +
			"            AND ind.id_imm = ID.id_imm " +
			"            AND ind.progressivo = ID.progressivo " +
			"            AND t.pk_id = ind.toponimo " +
			"            AND NOT EXISTS (" +
			"			(SELECT 1 FROM sitiuiu " +
			"			WHERE sitiuiu.cod_nazionale = c.cod_nazionale " +
			"			and LPAD (sitiuiu.foglio, 4, '0')  = ID.foglio " +
			"			and LPAD (sitiuiu.particella, 5, '0') = ID.mappale " +
			"			and sitiuiu.sub = ' ' " +
			"			and LPAD (sitiuiu.unimm, 4, '0') = nvl(ID.sub,'0000') " +
			"			and ((data_inizio_val < sysdate and data_fine_val > sysdate) " +
			"			or not exists " +
			"			(SELECT 1 FROM sitiuiu b where b.pkid_uiu <> sitiuiu.pkid_uiu " + 
			"			and b.cod_nazionale = sitiuiu.cod_nazionale " +
			"			and b.foglio = sitiuiu.foglio " + 
			"			and b.particella = sitiuiu.particella " + 
			"			and b.sub = sitiuiu.sub " + 
			"			and b.unimm = sitiuiu.unimm " + 
			"			and (b.data_inizio_val < sysdate and b.data_fine_val > sysdate))) " + 
			"			and sitiuiu.ide_muta_fine is not null))" +	
			"			ORDER BY ind.load_id";
			stm2 = conn.prepareStatement(sql);
			stm2.setString(1, ici.getCodEnte());
			stm2.setString(2, ici.getFoglio());
			stm2.setString(3, ici.getNumero());
			rs2 =  stm2.executeQuery();
			while(rs2.next()) {
				//scorre tutto e prende l'ultimo (quello con load_id più alto, secondo l'order by)
				if(rs2.getString("indirizzo") != null) {
					ici.setDesIndCatastale(rs2.getString("indirizzo"));				
				}			
			}
		}
		
		if(ici.getDesIndCatastale() == null)
			ici.setDesIndCatastale("non trovato");
		
		stm2.cancel();
	}
	
	public String recuperaCodNazionale(Connection conn) throws Exception {
		PreparedStatement pst = null;
    	ResultSet rs =null;
		String sql=null;
		String codEnt=null;
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}	
			sql=SQL_ENTE;
 		    pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()){
				codEnt=rs.getString("CODENT");
			}
			rs.close();
			pst.close();
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
		return codEnt;
	}	
	
	private void setEvidenziaAttuale(Connection conn, OggettiICINew oggettoICI) throws Exception {
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
	
	public Vector mCaricareListaOggettiICISoggetto(String codiceFiscaleDic, int annoImposta) throws Exception{

		Vector vct = new Vector();
	    sql = "";
		Connection conn = null;
		
		try {
			conn = this.getConnection();			
			java.sql.ResultSet rs = null;
			sql = SQL_SELECT_LISTA_SOGG;
			sql += " and COD_FISC = ?";
			sql += " and YEA_DEN = ?";
			sql = sql + " order by LPAD(FOGLIO, 5, '0'), LPAD(NUMERO, 5, '0'), LPAD(SUB, 4, '0'), YEA_DEN DESC))";			
			PreparedStatement pstmtOggettiICI = conn.prepareStatement(sql);
			int indice = 1;
			pstmtOggettiICI.setInt(indice, 1);
			indice++;
			pstmtOggettiICI.setString(indice, codiceFiscaleDic);
			indice++;
			pstmtOggettiICI.setString(indice, "" + annoImposta);
			indice++;
			rs = pstmtOggettiICI.executeQuery();
			
			while (rs.next()){
				OggettiICINew ici = new OggettiICINew();
				String codEnte = "";
				ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
				while (rsEnte.next()) {
					codEnte = rsEnte.getString("codent");
				}
				ici.setCodEnte(codEnte);
				ici.setChiave(rs.getString("ID"));
				ici.setFoglio(rs.getString("FOGLIO"));
				ici.setNumero(rs.getString("NUMERO"));
				ici.setSub(rs.getString("SUB"));
				ici.setCat(rs.getString("CAT"));
				ici.setCls(rs.getString("CLS"));
				ici.setProvenienza(rs.getString("PROVENIENZA"));
				ici.setNumDen(rs.getString("NUM_DEN"));
				ici.setYeaDen(rs.getString("YEA_DEN"));
				ici.setFlgPos3112(rs.getObject("FLG_POS3112") == null ? "NO" : 
					(rs.getString("FLG_POS3112").equalsIgnoreCase("1") || rs.getString("FLG_POS3112").equalsIgnoreCase("S") ? "SI" : "NO"));
				setEvidenziaAttuale(conn, ici);
				
				GenericTuples.T2<String,String> coord = null;
				try {
					coord = getLatitudeLongitude(ici.getFoglio(), Utils.fillUpZeroInFront(ici.getNumero(),5),ici.getCodEnte());
				} catch (Exception e) {
				}
				if (coord!=null) {
					ici.setLatitudine(coord.firstObj);
					ici.setLongitudine(coord.secondObj);
				}
				
				vct.add(ici);
			}

			return vct;
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
	
	public int getCountListaSoggetto(String codFiscaleDic, int annoImposta) throws Exception {
		
		Connection conn = null;
		int numOggettiICI = 0;
		try
		{
			conn = this.getConnection();
			String sql = SQL_SELECT_COUNT_LISTA_SOGG;
			sql += " and COD_FISC = ?";
			sql += " and YEA_DEN = ?";
			sql += ")";
			PreparedStatement pstmtOggettiICI = conn.prepareStatement(sql);
			int indice = 1;
			pstmtOggettiICI.setInt(indice, 1);
			indice++;
			pstmtOggettiICI.setString(indice, codFiscaleDic);
			indice++;
			pstmtOggettiICI.setString(indice, "" + annoImposta);
			indice++;
			ResultSet rsOggettiICI = pstmtOggettiICI.executeQuery();				
			while (rsOggettiICI.next()) {
				numOggettiICI = rsOggettiICI.getInt("conteggio");
			}
			return numOggettiICI;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
	}
	
	private ArrayList<ContribuentiOggettoNew> filter(ArrayList<ContribuentiOggettoNew> from) {
		java.util.ArrayList<ContribuentiOggettoNew> to = new java.util.ArrayList<ContribuentiOggettoNew>();
		for (ContribuentiOggettoNew con : from) {
			if (con.getTitSogg().trim().equalsIgnoreCase("CONTRIBUENTE")) {
				to.add(con);
			} else {
				boolean trovato = false;
				for (ContribuentiOggettoNew con1 : from) {
					if (con1.getTitSogg().trim().equalsIgnoreCase("CONTRIBUENTE")) {
						boolean equals = con.getNominativo().equals(con1.getNominativo()) &&
										con.getYeaDen().equals(con1.getYeaDen()) &&
										con.getYeaRif().equals(con1.getYeaRif()) &&
										con.getNumDen().equals(con1.getNumDen()) &&
										con.getNumRiga().equals(con1.getNumRiga()) &&
										con.getMesiPos().equals(con1.getMesiPos()) &&
										con.getPrcPoss().equals(con1.getPrcPoss()) &&
										con.getValImm().equals(con1.getValImm()) &&
										con.getFlgPos3112().equals(con1.getFlgPos3112()) &&
										con.getTipDen().equals(con1.getTipDen()) &&
										con.getProvenienza().equals(con1.getProvenienza());
						if (equals) {
							trovato = true;
						}						
					}					
					if (trovato) break;
				}
				if (!trovato) {
					to.add(con);
				}
			}
		}
		return to;
	}
	
}
