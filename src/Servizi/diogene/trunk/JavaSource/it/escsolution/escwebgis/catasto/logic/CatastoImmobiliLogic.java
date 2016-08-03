	/*
	 * Created on 9-apr-2004
	 *
	 * To change the template for this generated file go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	package it.escsolution.escwebgis.catasto.logic;

import it.escsolution.escwebgis.catasto.bean.Categoria;
import it.escsolution.escwebgis.catasto.bean.DatiMetrici;
import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.catasto.bean.ImmobiliFinder;
import it.escsolution.escwebgis.catasto.bean.StatisticaCategorie;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.docfa.bean.Docfa;
import it.escsolution.escwebgis.pregeo.logic.PregeoLogic;
import it.escsolution.escwebgis.toponomastica.bean.Civico;
import it.escsolution.escwebgis.tributi.bean.OggettiTARSU;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.utils.GenericTuples;
import it.webred.utils.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbutils.DbUtils;

	/**
	 * @author Administrator
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class CatastoImmobiliLogic extends EscLogic{

		public static final String LISTA_CIVICI = CatastoImmobiliLogic.class.getName() + "@LISTA_CIVICI";
		public static final String LISTA_CIVICI_CAT = CatastoImmobiliLogic.class.getName() + "@LISTA_CIVICI_CAT";
		public static final String UNIMM = CatastoImmobiliLogic.class.getName() +  "@UNIMM";
		public static final String SOGGETTO = "CATASTO_PER_SOGGETTO";
		public final static String DOCFA_COLLEGATI = "DOCFA_COLLEGATI@CatastoImmobiliLogic";
		public final static String DETTAGLIO_VANI_340 = "DETTAGLIO_VANI_340@CatastoImmobiliLogic";		
		public final static String SOLO_ATT = "SOLO_ATT@CatastoImmobiliLogic";
		public final static String LISTA_UIU_DERIVATE = "LISTA_UIU_DERIVATE@CatastoImmobiliLogic";
		public final static String LISTA_UIU_DERIVANTE = "LISTA_UIU_DERIVANTE@CatastoImmobiliLogic";
		public final static String LISTA_UIU_STORICO = "LISTA_UIU_STORICO@CatastoImmobiliLogic";
		public final static String ALBERO_IMMOBILI_DERIVATI = "ALBERO_IMMOBILI_DERIVATI@CatastoImmobiliLogic";

		public final static String LISTA_UIU_GRAFFATI = "LISTA_UIU_GRAFFATI@CatastoImmobiliLogic";
		public final static String ULTIMA_DICH_TARSU = "ULTIMA_DICH_TARSU@CatastoImmobiliLogic";
		
		private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		private String DATA_MIN = "01/01/0001";
		private String DATA_MAX = "31/12/9999";
		
		private final String CONDIZIONE_SITICOMU = "(siticomu.COD_NAZIONALE = ?) ";
		//private final String CONDIZIONE_SITICOMU = "(siticomu.COD_NAZIONALE = ? OR siticomu.CODI_FISC_LUNA)";
		
		private static final DecimalFormat DF_CAT = new DecimalFormat();
		static {
			DF_CAT.setGroupingUsed(false);
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator(',');
			DF_CAT.setDecimalFormatSymbols(dfs);
			DF_CAT.setMaximumFractionDigits(2);
		}
		
		boolean soloAtt = false;
		
		public CatastoImmobiliLogic(EnvUtente eu) {
			super(eu);
		}
		/*private final static String SQL_SELECT_LISTA = "select * from ( " +
				"select ROWNUM as N,PKID_UIU,FK_COMUNE,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO," +
				"UNIMM,CATEGORIA,DESCATEGORIA,DATA_FINE_VAL,DATA_INIZIO_VAL " +
				"from (" +
				"select ROWnum as N," +
				"sitiuiu.PKID_UIU," +
				"sitiuiu.COD_NAZIONALE AS FK_COMUNE," +
				"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
				"decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
				"sitiuiu.PARTICELLA," +
				"decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) AS SUBALTERNO," +
				"decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) AS UNIMM," +
				"decode(sitiuiu.CATEGORIA,null,'-',sitiuiu.CATEGORIA) AS CATEGORIA," +
				"sitideco.DESCRIPTION AS DESCATEGORIA, " +
				"to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy')as DATA_FINE_VAL,  " +
				"to_char(sitiuiu.DATA_INIZIO_VAL, 'dd/mm/yyyy')as DATA_INIZIO_VAL  " +
				"from sitiuiu, sitideco, siticomu " +
				//"where sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd') " +
				"where sitiuiu.CATEGORIA = sitideco.CODE " +
				"and sitideco.TABLENAME='SITIUIU' " +
				"and sitideco.FIELDNAME = 'CATEGORIA' " +
				"AND sitiuiu.cod_nazionale = SITICOMU.cod_nazionale " + 
				"and 1 = ?";*/
		
		private final static String SQL_SELECT_LISTA = 
				"select * from (select ROWNUM as N, FK_COMUNE,CODI_FISC_LUNA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO,UNIMM,GRAFFATO from " +
				" (" +
				"select distinct sitiuiu.COD_NAZIONALE AS FK_COMUNE," +
				"siticomu.CODI_FISC_LUNA," +
				"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
				"LPAD(TO_CHAR(sitiuiu.FOGLIO), 4, '0') AS FOGLIO," +
				"sitiuiu.PARTICELLA," +
				"sitiuiu.SUB AS SUBALTERNO," +
				"LPAD(TO_CHAR(sitiuiu.UNIMM), 4, '0') AS UNIMM," +
				"'N' AS GRAFFATO " +
				"from sitiuiu, sitideco, siticomu " +
				//"where sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd') " +
				"where sitiuiu.CATEGORIA = sitideco.CODE " +
				"and sitideco.TABLENAME='SITIUIU' " +
				"and sitideco.FIELDNAME = 'CATEGORIA' " +
				"AND sitiuiu.cod_nazionale = SITICOMU.cod_nazionale " + 
				"and 1 = ?";
		
		// count sbagliata
		/*private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio" +
				" from sitiuiu" +
				//" where sitiuiu.COD_NAZIONALE = 'F205'" +
				" WHERE sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd')" +
				" and 1 = ? ";
		*/
		private final static String SQL_SELECT_COUNT_LISTA ="SELECT count(*) as conteggio " +
		"from (" +
		"select distinct sitiuiu.COD_NAZIONALE AS FK_COMUNE," +
		"siticomu.CODI_FISC_LUNA," +
		"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
		"LPAD(TO_CHAR(sitiuiu.FOGLIO), 4, '0') AS FOGLIO," +
		"sitiuiu.PARTICELLA," +
		"sitiuiu.SUB AS SUBALTERNO," +
		"LPAD(TO_CHAR(sitiuiu.UNIMM), 4, '0') AS UNIMM," +
		"'N' AS GRAFFATO " +
		"from sitiuiu, sitideco, siticomu " +
		//"where sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd') " +
		"where sitiuiu.CATEGORIA = sitideco.CODE " +
		"and sitideco.TABLENAME='SITIUIU' " +
		"and sitideco.FIELDNAME = 'CATEGORIA' " +
		"AND sitiuiu.cod_nazionale = SITICOMU.cod_nazionale " + 
		"and 1 = ?";


		private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio" +
		" from sitiuiu " +
		//" where sitiuiu.COD_NAZIONALE = 'F205'" +
		" where sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd')" +
		" and 1 = ? ";
		
		private final static String SQL_SELECT_COORD_CAT_FROM_VIA="select id.FOGLIO, id.MAPPALE, id.SUB from load_cat_uiu_id ID," +
				" siticomu c,  load_cat_uiu_ind ind where" +
				"  ID.codi_fisc_luna = c.codi_fisc_luna and   ID.sez = c.sezione_censuaria  AND ind.codi_fisc_luna = c.codi_fisc_luna " +
				"   AND ind.sezione = ID.sezione   AND ind.id_imm = ID.id_imm   AND ind.progressivo = ID.progressivo " +
				"  and ind.ID_IMM= ? and ind.PROGRESSIVO= ? and ind.SEQ=? and ind.codi_fisc_luna = ? and ind.sezione=?  ";

		private final static String SQL_SELECT_LISTA_FROM_VIA = 
		"select * from (select ROWNUM as N, FK_COMUNE,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO,UNIMM from " +
		" ( " +
		"select distinct FK_COMUNE,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO," +
		"UNIMM  " +
		"from (" +
		"select sitiuiu.COD_NAZIONALE AS FK_COMUNE," +
		"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
		"decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
		"sitiuiu.PARTICELLA," +
		"decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) AS SUBALTERNO," +
		"decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) AS UNIMM " +
		"from sitiuiu, sitideco, siticomu " +
		//"where sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd') " +
		"where sitiuiu.CATEGORIA = sitideco.CODE " +
		"and sitideco.TABLENAME='SITIUIU' " +
		"and sitideco.FIELDNAME = 'CATEGORIA' " +
		"AND sitiuiu.cod_nazionale = SITICOMU.cod_nazionale " + 
		"and sitiuiu.FOGLIO = ? " +
		"and sitiuiu.PARTICELLA = ? " +
		"and (DECODE(sitiuiu.SUB,' ','-',NVL(sitiuiu.SUB,'*'),'-',sitiuiu.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
		"or sitiuiu.UNIMM = ?) )))";
		
		private final static String SQL_SELECT_LISTA_FROM_ID = 
		"select * from (select ROWNUM as N, FK_COMUNE,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO,UNIMM from " +
		" ( " +
		"select distinct FK_COMUNE,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO," +
		"UNIMM  " +
		"from (" +
		"select sitiuiu.COD_NAZIONALE AS FK_COMUNE," +
		"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
		"decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
		"sitiuiu.PARTICELLA," +
		"decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) AS SUBALTERNO," +
		"decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) AS UNIMM " +
		"from sitiuiu, sitideco, siticomu " +
		//"where sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd') " +
		"where sitiuiu.CATEGORIA = sitideco.CODE " +
		"and sitideco.TABLENAME='SITIUIU' " +
		"and sitideco.FIELDNAME = 'CATEGORIA' " +
		"AND sitiuiu.cod_nazionale = SITICOMU.cod_nazionale " + 
		"and sitiuiu.PKID_UIU = ?  )))";
		
		private static final String SQL_UIU_GRAFFATE = 
			"SELECT DISTINCT C.COD_NAZIONALE AS FK_COMUNE, " +
			"C.CODI_FISC_LUNA, " +
			"DECODE(SEZ, NULL, '-', ' ', '-', SEZ) AS SEZIONE, " +
			"FOGLIO, " +
			"MAPPALE, " +
			"' ' AS SUBALTERNO, " +
			"SUB AS UNIMM, " +
			"GRAFFATO " +
			"FROM LOAD_CAT_UIU_ID LC, SITICOMU C " +
			"WHERE C.SEZIONE_CENSUARIA = LC.SEZ and c.codi_fisc_luna = lc.codi_fisc_luna " +
			"AND GRAFFATO = 'Y' ";
		
		//Unità principale, collegata alla graffata selezionata
		private static final String SQL_UIU_GRAFFATA = 
			"SELECT DISTINCT c.cod_nazionale FK_COMUNE, SEZ, FOGLIO, MAPPALE, SUB " +
			"FROM LOAD_CAT_UIU_ID lc , siticomu c " +
			"WHERE LC.SEZ = C.SEZIONE_CENSUARIA and c.codi_fisc_luna = lc.codi_fisc_luna " +
			"AND GRAFFATO = 'N' " +
			"AND ID_IMM IN (" +
			"SELECT ID_IMM " +
			"FROM LOAD_CAT_UIU_ID lc , siticomu c " +
			"WHERE LC.SEZ = C.SEZIONE_CENSUARIA and c.codi_fisc_luna = lc.codi_fisc_luna " +
			"AND c.COD_NAZIONALE = ?  " +
			"AND FOGLIO = ? " +
			"AND MAPPALE = ? " +
			"AND SUB = ? " +
			"AND GRAFFATO = 'Y')";
		
		//Lista unità graffate alla principale
		private static final String SQL_UIU_PRINC_GRAFFATE = 
			"SELECT DISTINCT c.cod_nazionale FK_COMUNE, SEZ, FOGLIO, MAPPALE, SUB " +
			"FROM LOAD_CAT_UIU_ID lc , siticomu c " +
			"WHERE LC.SEZ = C.SEZIONE_CENSUARIA and c.codi_fisc_luna = lc.codi_fisc_luna " +
			"AND GRAFFATO = 'Y' " +
			"AND ID_IMM IN ( " +
			"SELECT ID_IMM " +
			"FROM LOAD_CAT_UIU_ID lc , siticomu c " +
			"WHERE LC.SEZ = C.SEZIONE_CENSUARIA and c.codi_fisc_luna = lc.codi_fisc_luna " +
			"AND c.cod_nazionale = ? "+
			"AND FOGLIO = ? " +
			"AND MAPPALE = ? " +
			"AND SUB = ? " +
			"AND GRAFFATO = 'N') ";


		public Hashtable mCaricareDatiFormRicerca(String utente) throws NamingException, SQLException{
			// carico la lista delle categorie e le metto in un hashtable
			Hashtable ht = new Hashtable();
			
			EnvUtente eu = this.getEnvUtente();
			String enteId = eu.getEnte();
			String userId = eu.getUtente().getUsername();
			String sessionId = eu.getUtente().getSessionId();
			
			try
			{	
				CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
				RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
				rc.setEnteId(enteId);
				rc.setUserId(userId);
				rc.setSessionId(sessionId);
		
				ht.put("LISTA_CATEGORIE",mCaricareCategorie(catastoService,rc));
				ht.put("LISTA_COMUNI",new ComuniLogic(this.envUtente).getListaComuniUtente(utente));
			}
			catch (SQLException e)
			{
				log.error(e.getMessage(),e);
				throw e;
			}

			return ht;
		}

	private Vector mCaricareCategorie(CatastoService service, RicercaOggettoCatDTO ro) throws SQLException{
		Vector vct = new Vector();
		
		List<Sitideco> lista = service.getListaCategorieImmobile(ro);
			
		vct.add(new Categoria("","Tutte"));
		for (Sitideco deco : lista)
			vct.add(new Categoria(deco.getId().getCode(),deco.getId().getCode() + " - "+deco.getDescription()));
		
		return vct;
	}

	public Hashtable mCaricareDettaglioImmobile(String chiave, String chiaveGraf, String pathPlanimetrieComma340) throws Exception{
		//salvo la chiave composta per usi successivi
		String chiaveParam = new String(chiave);
		
		Hashtable ht = new Hashtable();
		
		// costruisco la chiave per la query
		List<String> listParam = Arrays.asList(chiave.split("\\|"));

		Connection conn = null;
		try {
			this.setDatasource(JNDI_CATCOSPOLETO);

			conn = this.getConnection();
			this.initialize();
			
			long startTime = new Date().getTime();

			String sql = "select " +
		//	"decode(sitiuiu.COD_NAZIONALE,null,'-',sitiuiu.COD_NAZIONALE) AS FK_COMUNE," +
		//	"sitiuiu.ANNOTAZIONI AS ANNOTAZIONI," +
		//	"sitiuiu.PARTICELLA AS PARTICELLA," +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
		//	"decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
		//	"decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) as SUBALTERNO," +
		//	"decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) as UNIMM," +
		//	"decode(sitiuiu.consistenza,null,'-',sitiuiu.consistenza) AS VANI," +
		//	"decode(sitiuiu.SUP_CAT,null,'-',sitiuiu.SUP_CAT) AS SUPERFICIE," +
		//	"decode(sitiuiu.ZONA,null,'-',sitiuiu.ZONA)as ZONA," +
		//	"decode(sitiuiu.MICROZONA,null,'-',sitiuiu.MICROZONA) AS MICROZONA," +
		//	"decode(sitiuiu.RENDITA,null,'-',sitiuiu.RENDITA) AS RENDITA," +
		//	"decode(sitiuiu.CLASSE,null,'-',sitiuiu.CLASSE) AS CLASSE," +
		//	"decode(sitiuiu.PARTITA,null,'-',sitiuiu.PARTITA) as PARTITA," +
		//	"decode(sitiuiu.PIANO,null,'-',sitiuiu.PIANO) as PIANO," +
	//		"decode(sitiuiu.CATEGORIA,null,'-',sitiuiu.CATEGORIA) AS CATEGORIA," +
		//	"sitideco.DESCRIPTION AS desCategoria," +
			"sitiuiu.PKID_UIU," +
			"siticivi.CIVICO," +
			"sitidstr.PREFISSO||' '||sitidstr.NOME AS INDIRIZZO," +
			"siticivi.PKID_CIVI as PK_CIVICO," +
			"sitidstr.PKID_STRA as PK_INDIRIZZO " +
		//	"to_char(sitiuiu.DATA_INIZIO_VAL, 'dd/mm/yyyy')as DATA_INIZIO_VAL, " +
		//	"to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy')as DATA_FINE_VAL, " +
		//	"sitiuiu.IDE_MUTA_INI as IDE_MUTA_INI, "+
		//	"sitiuiu.IDE_MUTA_FINE as IDE_MUTA_FINE "+
			"from sitiuiu,siticivi_uiu,siticivi,sitidstr,siticomu " +
			"where "+ CONDIZIONE_SITICOMU +
			"and sitiuiu.FOGLIO = ? " +
			"and sitiuiu.PARTICELLA = ? " +
			"and decode (NVL(sitiuiu.SUB,'*'),'*','-',' ','-',sitiuiu.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
			"and sitiuiu.UNIMM = ? " +
			//"and sitideco.TABLENAME = 'SITIUIU' " +
			//"and sitideco.FIELDNAME = 'CATEGORIA' " +
			//"and sitideco.CODE = sitiuiu.CATEGORIA " +
			"and to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy') = ? " +
			//"and sitiuiu.DATA_FINE_VAL = TO_DATE('99991231', 'yyyymmdd') " +
			"AND siticivi_uiu.PKID_UIU (+) = sitiuiu.PKID_UIU " +
			"and siticivi_uiu.PKID_CIVI = siticivi.PKID_CIVI (+) " +
			"and sitidstr.PKID_STRA (+) = siticivi.PKID_STRA " +
			"and siticivi_uiu.DATA_FINE_VAL (+) = TO_DATE('99991231', 'yyyymmdd') " +
			"and siticivi.DATA_FINE_VAL (+) = TO_DATE('99991231', 'yyyymmdd') " +
			"and sitidstr.DATA_FINE_VAL (+) = TO_DATE('99991231', 'yyyymmdd') " +
			"and sitiuiu.COD_NAZIONALE = siticomu.COD_NAZIONALE " + 
			"order by INDIRIZZO, CIVICO " +
			"";


			int indice = 0;
			this.initialize();
			if (listParam.size() > 3)
			{
				this.setString(++indice,(String)listParam.get(0));
				//this.setString(++indice,(String)listParam.get(0));
				this.setString(++indice,(String)listParam.get(1));
				this.setString(++indice,(String)listParam.get(2));
				this.setString(++indice,(String)listParam.get(3));
				this.setString(++indice,(String)listParam.get(3));
				this.setString(++indice,(String)listParam.get(4));
				this.setString(++indice,(String)listParam.get(5));
			}
			else
			{
				// IN CASO NON ARRIVI ALCUNA CHIAVE, FORZO A NON TROVARE NULLA
				this.setString(++indice, "-1");
				//this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				this.setString(++indice, "-1");
				sql = sql.replaceFirst("order by INDIRIZZO, CIVICO\\s*$", "and 0 = 1 order by INDIRIZZO, CIVICO ");
			}
			
			String fog = "";
			String par = "";

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Immobile imm = new Immobile();
			imm.setChiaveGraffato(chiaveGraf);
			ht.put(UNIMM,imm);
			ArrayList listaCivici = new ArrayList();
			ArrayList listaCiviciCat = new ArrayList();
			ht.put(LISTA_CIVICI, listaCivici);
			ht.put(LISTA_CIVICI_CAT, listaCiviciCat);
			if (rs.next()){
				//imm.setNumero(rs.getString("PARTICELLA"));
				//imm.setFoglio(rs.getString("FOGLIO"));
				imm.setSezione(rs.getString("SEZIONE"));
				//imm.setSubalterno(rs.getString("SUBALTERNO"));
				//imm.setUnimm(rs.getString("UNIMM"));
				fog = (String)listParam.get(1);
				par = (String)listParam.get(2);
				imm.setComune((String)listParam.get(0));
				imm.setNumero((String)listParam.get(2));
				imm.setFoglio((String)listParam.get(1));
				imm.setSubalterno((String)listParam.get(3));
				imm.setUnimm((String)listParam.get(4));
				/*
				imm.setCategoria(rs.getString("desCategoria"));
				imm.setCodCategoria(rs.getString("CATEGORIA"));
				imm.setComune(rs.getString("FK_COMUNE"));
				imm.setPartita(rs.getString("PARTITA"));
				imm.setRendita(rs.getString("RENDITA"));
				imm.setMicrozona(rs.getString("MICROZONA"));
				imm.setSuperficie(rs.getString("SUPERFICIE"));
				imm.setVani(rs.getString("VANI"));
				imm.setPiano(rs.getString("PIANO"));
				imm.setZona(rs.getString("ZONA"));
				imm.setClasse(rs.getString("CLASSE"));*/
				imm.setPkId(rs.getString("PKID_UIU"));
				imm.setPkCivico(rs.getString("PK_CIVICO"));
				imm.setPkIndirizzo(rs.getString("PK_INDIRIZZO"));
				/*imm.setDataFineVal(rs.getString("DATA_FINE_VAL"));
				imm.setDataInizioVal(rs.getString("DATA_INIZIO_VAL"));
				imm.setAnnotazione(rs.getString("ANNOTAZIONI") != null ? rs.getString("ANNOTAZIONI"): "");
				imm.setIdeMutaIni(rs.getInt("IDE_MUTA_INI"));
				imm.setIdeMutaFine(rs.getInt("IDE_MUTA_FINE"));*/
				
				GenericTuples.T2<String,String> coord = getLatitudeLongitude(imm.getFoglio(), imm.getNumero(), imm.getComune());
				if (coord!=null) {
					imm.setLatitudine(coord.firstObj);
					imm.setLongitudine(coord.secondObj);
				}
				
				do
				{
					if (rs.getString("pk_civico")!=null) {
						Civico civ = new Civico();
						civ.setStrada(rs.getString("INDIRIZZO"));
						civ.setNumero(rs.getString("CIVICO"));
						listaCivici.add(civ);
					}
				} while (rs.next());
				
				log.debug("DURATA CARICAMENTO DATI IMMOBILE MS " + (new Date().getTime() - startTime));
				
				startTime = new Date().getTime();
				// LETTURA DEI CIVICI A CATASTO PER INTEGRARE LE INFORMAZIONI PER QUELLE 
				// UIU CHE SONO CESSATE E NELLA PRIMA LISTA NON DA RISULTATI
				int index = 0;
				this.initialize();
				this.setString(++index,(String)listParam.get(0) );
				//this.setString(++index,(String)listParam.get(0) );
				this.setString(++index,(String)listParam.get(1) );
				this.setString(++index,(String)listParam.get(2) );
				// salto parametro sub con spazio  e vado diretto al dato
				this.setString(++index,(String)listParam.get(4) );

				sql = /*"SELECT DISTINCT  t.descr || ' ' || ind.ind  indirizzo, remove_lead_zero (ind.civ1) AS civico, " +
						"ind.id_imm, ind.progressivo, ind.seq, ind.codi_fisc_luna, ind.sezione  "+
	            " FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t  "+
					" WHERE (c.cod_nazionale = ? or c.codi_fisc_luna = ?)"+
					" AND ID.codi_fisc_luna = c.codi_fisc_luna  "+
					" AND ID.sez = c.sezione_censuaria  "+
					" AND ID.foglio = LPAD (?, 4, '0')  "+
					" AND ID.mappale = LPAD (?, 5, '0')  "+
					" AND ID.SUB =  LPAD (?, 4, '0') "+
					" AND ind.codi_fisc_luna = c.codi_fisc_luna "+ 
					" AND ind.sezione = ID.sezione  "+
					" AND ind.id_imm = ID.id_imm  "+
					" AND ind.progressivo = ID.progressivo  "+
					" AND t.pk_id = ind.toponimo " +
					"AND IND.PROGRESSIVO = (SELECT MAX(PROGRESSIVO) " +
					"FROM load_cat_uiu_ind IND22 " +
					"WHERE IND22.ID_IMM = IND.ID_IMM " +
					"AND IND22.CODI_FISC_LUNA = IND.CODI_FISC_LUNA  " +
					"AND ind22.SEZIONE = ind.SEZIONE " +
					"AND ind22.todelete ='0' )"; 
				*/
				"SELECT DISTINCT TRIM(t.descr || ' ' || ind.ind)  indirizzo, remove_lead_zero (ind.civ1) AS civico , ind.id_imm, ind.progressivo, ind.seq, ind.codi_fisc_luna, ind.sezione "+  
				"FROM SITICOMU,CAT_D_TOPONIMI T,LOAD_CAT_UIU_ID LID INNER JOIN LOAD_CAT_UIU_IND IND "+
				"ON (IND.SEZIONE = LID.SEZIONE AND IND.ID_IMM = LID.ID_IMM  "+
				"    AND IND.PROGRESSIVO = LID.PROGRESSIVO AND IND.SEQ = LID.SEQ)  "+
				"WHERE LID.CODI_FISC_LUNA = siticomu.CODI_FISC_LUNA  "+
				"AND IND.CODI_FISC_LUNA = siticomu.CODI_FISC_LUNA  "+
				"AND LID.SEZ = SITICOMU.SEZIONE_CENSUARIA  "+
				"AND T.PK_ID = IND.TOPONIMO  "+
				"   AND IND.TODELETE ='0' "+
				"   AND IND.SEQ = (SELECT MAX(SEQ) FROM load_cat_uiu_ind IND21  "+
				"                   WHERE IND21.ID_IMM = IND.ID_IMM  "+
				"                     AND IND21.CODI_FISC_LUNA = IND.CODI_FISC_LUNA  "+
				"                     AND IND21.PROGRESSIVO = IND.PROGRESSIVO  "+
				"                     AND ind21.SEZIONE = ind.SEZIONE  "+
				"                     AND ind21.todelete ='0')  "+
				"AND LID.SEQ = (SELECT MAX(SEQ) FROM load_cat_uiu_id LID2  "+
				"                   WHERE LID2.ID_IMM = LID.ID_IMM  "+
				"                     AND LID2.CODI_FISC_LUNA = LID.CODI_FISC_LUNA  "+
				"                     AND LID2.PROGRESSIVO = LID.PROGRESSIVO  "+
				"                     AND LID2.SEZIONE = LID.SEZIONE  "+
				"                     AND lid2.foglio = lid.foglio  "+
				"                     AND lid2.mappale = lid.mappale  "+
				"                     AND lid2.sub = lid.sub)  "+
				"AND IND.PROGRESSIVO = (SELECT MAX(PROGRESSIVO) FROM load_cat_uiu_ind IND22  "+
				"                           WHERE IND22.ID_IMM = IND.ID_IMM  "+
				"                             AND IND22.CODI_FISC_LUNA = IND.CODI_FISC_LUNA  "+
				"                             AND ind22.SEZIONE = ind.SEZIONE  "+
				"                             AND ind22.todelete ='0' )   "+
				"AND (SITICOMU.cod_nazionale = ? )" +
				"AND LID.foglio = LPAD(?, 4, '0')   "+
				"AND LID.mappale = LPAD(?, 5, '0')   "+
				"AND LID.SUB =  LPAD(?, 4, '0')  ";
				
				
				prepareStatement(sql);
				java.sql.ResultSet rscat = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rscat.next()){
					Civico civ = new Civico();
					civ.setStrada(rscat.getString("INDIRIZZO"));
					civ.setNumero(rscat.getString("CIVICO"));
					civ.setIdImm(rscat.getInt("ID_IMM"));
					civ.setProgressivo(rscat.getInt("PROGRESSIVO"));
					civ.setSeq(rscat.getInt("SEQ"));
					civ.setCodiFiscLuna(rscat.getString("CODI_FISC_LUNA"));
					civ.setSezione(rscat.getString("SEZIONE"));
					listaCiviciCat.add(civ);
				}
				
				log.debug("DURATA LETTURA CIVICI CATASTO MS " + (new Date().getTime() - startTime));

			}
		
			startTime = new Date().getTime();
			
			//Storico Immobili
			ArrayList<Immobile> listaImmobiliStorico= new ArrayList<Immobile>();
			
			listaImmobiliStorico= this.getListaStoricoSubalterno(listParam);
			
			//setto ide_muta_ini dell'immobile di cui faccio il dettaglio come l'ide_muta_ini dell'immobile ultimo della lista, oppure del penultimo etc.., se non valorizzato (primo in ordine di tempo)
			//setto ide_muta_fine dell'immobile di cui faccio il dettaglio come l'ide_muta_fine dell'immobile primo della lista, oppure del secondo etc.., se non valorizzato (ultimo in ordine di tempo)
			
			Integer ideMutaFine=new Integer(0);
			Integer ideMutaIni=new Integer(0);
			
			if (listaImmobiliStorico!= null && listaImmobiliStorico.size()>0){
				
				for (int i= 0; i< listaImmobiliStorico.size(); i++ ){
					Immobile immobile= listaImmobiliStorico.get(i);
					if (immobile.getIdeMutaFine()!= null && !immobile.getIdeMutaFine().equals(new Integer(0))){
						ideMutaFine= immobile.getIdeMutaFine();
						break;
					}
				}
				for (int i= listaImmobiliStorico.size() -1; i>-1; i-- ){
					Immobile immobile= listaImmobiliStorico.get(i);
					if (immobile.getIdeMutaIni()!= null && !immobile.getIdeMutaIni().equals(new Integer(0))){
						ideMutaIni= immobile.getIdeMutaIni();
						break;
					}
				}
				imm.setIdeMutaIni(ideMutaIni);
				imm.setIdeMutaFine(ideMutaFine);
				
				//setto data fine validità come data fine del primo della lista
				//setto data inziio validità come data inizio dell'ultimo
				
				//nella chiave per i crosslink metto entrambe le date del primo della lista
				String keyDataFineVal = listaImmobiliStorico.get(0).getDataFineVal();
				String keyDataInizioVal = listaImmobiliStorico.get(listaImmobiliStorico.size() -1).getDataInizioVal();
				
				imm.setDataFineVal(keyDataFineVal);
				imm.setDataInizioVal(keyDataInizioVal);
				
				String datesForKey = (keyDataFineVal.equalsIgnoreCase("ATTUALE") ? this.DATA_MAX : keyDataFineVal) +
				"|" + keyDataInizioVal;
				imm.setDatesForKey(datesForKey);
				
			}
			
			ht.put(this.LISTA_UIU_STORICO, listaImmobiliStorico);
			
			log.debug("DURATA LETTURA STORICO IMMOBILI MS " + (new Date().getTime() - startTime));
			
			startTime = new Date().getTime();
			// cerco le uiu derivate 
			if (imm.getDataFineVal()!= null && (!imm.getDataFineVal().equals(this.DATA_MAX) && !imm.getDataFineVal().equalsIgnoreCase("ATTUALE"))){
				if (imm.getIdeMutaFine() != null){
					
					ArrayList<Immobile> listaImmobiliDerivati= this.getImmobiliDerivati(imm, listParam);
				
					ht.put(this.LISTA_UIU_DERIVATE, listaImmobiliDerivati);
				
				}
			}
			log.debug("DURATA LETTURA UIU DERIVATE MS " + (new Date().getTime() - startTime));			
			
			startTime = new Date().getTime();
			// cerco le uiu da cui deriva quella in oggetto 
			if (imm.getDataInizioVal()!= null && !imm.getDataInizioVal().equals(this.DATA_MIN)){
			
				if (imm.getIdeMutaIni() != null){
				
					ArrayList<Immobile> listaImmobiliDerivanti= this.getImmobiliDerivanti(imm, listParam);
					
					ht.put(this.LISTA_UIU_DERIVANTE, listaImmobiliDerivanti);
				
				}
			}
			log.debug("DURATA LETTURA UIU DA CUI DERIVA UIU IN OGGETTO MS " + (new Date().getTime() - startTime));
			
			// docfa collegati e superfici comma 340
			if
			(
					(imm.getFoglio() != null && !imm.getFoglio().equals("") && !imm.getFoglio().equals("-")) &&
					(imm.getNumero() != null && !imm.getNumero().equals("") && !imm.getNumero().equals("-")) &&
					(imm.getUnimm() != null && !imm.getUnimm().equals("") && !imm.getUnimm().equals("-"))

			)
			{
					startTime = new Date().getTime();
					ArrayList docfaCollegati = this.getDatiDocfa(imm,conn);
					ht.put(DOCFA_COLLEGATI, docfaCollegati);
					log.debug("DURATA LETTURA DOCFA COLLEGATI MS " + (new Date().getTime() - startTime));
					
					startTime = new Date().getTime();
					ArrayList dettaglioVani = this.getSuperficiComma340(imm);
					ht.put(DETTAGLIO_VANI_340, dettaglioVani);
					log.debug("DURATA LETTURA SUPERFICI COMMA 340 MS " + (new Date().getTime() - startTime));
					
					startTime = new Date().getTime();
					calcolaVanoMedioDPR138(imm.getDatiMetrici(), listaImmobiliStorico);
					log.debug("DURATA CALCOLO VANO MEDIO DPR 138 MS " + (new Date().getTime() - startTime));
					
					startTime = new Date().getTime();
					String supTarsuABC = getSupTarsuABC(dettaglioVani);
					imm.getDatiMetrici().setSupTarsuABC(supTarsuABC);
					
					InformativaTarsuDTO dichTarsu = this.getUltimaDichTarsu(imm);
					if(dichTarsu!=null)
					ht.put(ULTIMA_DICH_TARSU, dichTarsu);
					log.debug("DURATA LETTURA DICH. TARSU MS " + (new Date().getTime() - startTime));
			}
			
			
			/*
			 * Recupero le informazioni pregeo ipotizzando che il foglio e la particella
			 * dell'immobile coincidano con quelli del terreno	
			 */
			startTime = new Date().getTime();
			String pathPregeo = pathPlanimetrieComma340 + File.separatorChar + this.getDirPregeoEnte();
			PregeoLogic pLogic = new PregeoLogic(super.getEnvUtente());
			ht.put("PREGEO", pLogic.getListaPregeoFabbricato(fog,par,pathPregeo,false));
			log.debug("DURATA LETTURA INFORMAZIONI PREGEO MS " + (new Date().getTime() - startTime));
			
			//lista unità graffate nel caso di unità principale di graffate
			startTime = new Date().getTime();
			ArrayList<Immobile> listaGraffati = new ArrayList<Immobile>();
			listaGraffati = getListaGraffati(conn, imm, chiaveParam);
			imm.setPrincGraffati(listaGraffati.size() > 0);
			ht.put(this.LISTA_UIU_GRAFFATI, listaGraffati);
			log.debug("DURATA LETTURA UNITA' GRAFFATE MS " + (new Date().getTime() - startTime));
			
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
	finally
	{
		if (conn != null)
		{
			if (!conn.isClosed())
				conn.close();
			conn = null;
		}
	}
	}
	
	private ArrayList<Docfa> getDatiDocfa(Immobile imm, Connection conn) throws Exception{
		//cerco dati 
		ResultSet rsDocfa  =null;
		ArrayList<Docfa>  docfaCollegati = new ArrayList<Docfa> ();
		try {
			/*sql =   "SELECT protocollo_registrazione protocollo,  fornitura,categoria,classe,rendita_euro " +
            "FROM DOCFA_DATI_CENSUARI U WHERE  "+
            "  u.foglio = lpad(?,4,'0') "+
            "  AND lpad(u.numero,5,'0') = lpad(?,5,'0') "+
            "  AND nvl(trim(u.subalterno),'0000') = lpad(?,4,'0')"+
            "  AND to_char(u.fornitura,'yyyymm')=substr(data_registrazione,0,6) ";*/
			sql = "SELECT DISTINCT protocollo_reg AS protocollo, fornitura, prop_categoria categoria, prop_classe classe, " +
			"TO_NUMBER (prop_rendita_euro_cata) / 100 rendita_euro " +
            "FROM docfa_uiu du " +
            "WHERE du.foglio = LPAD(?, 4, '0') " +
            "AND LPAD(du.numero, 5, '0') = LPAD(?, 5, '0') " + 
            "AND NVL(TRIM(du.subalterno), '0000') = LPAD(?, 4, '0')";
								
			this.initialize();
			this.setString(1,imm.getFoglio().trim());
			this.setString(2,imm.getNumero().trim());
			this.setString(3,imm.getUnimm().trim());
			prepareStatement(sql);
			rsDocfa = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
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
		} finally {
			DbUtils.close(rsDocfa);
		}
		
		return docfaCollegati;
	}
	
	private String selectFieldFromSitiediUiuExt(Immobile imm, String field) throws Exception{
		
		ResultSet rs =null;
		Connection conn = null;
		String sub = imm.getUnimm();	
		String value = null;
		//Potrebbe non esserci la colonna, select a parte
		try 	{
			sql = "SELECT "+field+" from sitiedi_uiu_ext  "+
				  "WHERE cod_nazionale = ? AND foglio = ? AND lpad(trim(particella),5,'0') = lpad(?,5,'0') AND unimm = ? and data_fine_val = to_date('31-12-9999','dd-mm-yyyy')";

			this.initialize();
			this.setString(1,imm.getComune());
			this.setString(2,imm.getFoglio());
			this.setString(3,imm.getNumero());
			this.setString(4, (sub == null || sub.trim().equals("") || sub.equals("-")) ? "0": sub);
			prepareStatement(sql);
			conn = this.getConnection();

			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next())		
				value =  rs.getString(field);
		} catch (Exception sse) {
			log.debug("COLONNA MANCANTE",sse);
		} finally {
			DbUtils.close(rs);
			DbUtils.close(conn);
		}
		return value;
	}
	
	private void impostaValSitiEdiUiuExt(Immobile imm) throws Exception{
			
		DatiMetrici dm = imm.getDatiMetrici();
		dm.setCodStatoCat(selectFieldFromSitiediUiuExt(imm,"STATO_CATASTO"));
		dm.setSupCatTarsu(selectFieldFromSitiediUiuExt(imm,"SUP_CAT_TARSU"));
		dm.setSupDPR138(selectFieldFromSitiediUiuExt(imm,"TARES_SUP_TOTALE"));	
		
		imm.setDatiMetrici(dm);
	}
	
	private void calcolaVanoMedioDPR138(DatiMetrici dm , ArrayList<Immobile> listaImmobiliStorico){
		
		BigDecimal vanoMedio = BigDecimal.ZERO;
		
		if(listaImmobiliStorico!=null && listaImmobiliStorico.size()>0){
			Immobile immS = listaImmobiliStorico.get(0);
			if(immS.getCodCategoria().startsWith("A")){
				log.debug("CALCOLO VANO MEDIO DPR 138 PER L'IMMOBILE FOGLIO: " +
							immS.getFoglio() + " - PARTICELLA: " + 
							immS.getNumero() + " - UNIMM: " + 
							immS.getUnimm());
				String vani = immS.getVani();
				if (vani != null) vani = vani.replace(",", ".");
				String sup = dm.getSupDPR138() != null ? dm.getSupDPR138().replace(",", ".") : "";
				if(!"".equals(sup) && vani!=null && !"-".equals(vani)){
					try {
						vanoMedio = (new BigDecimal(sup)).divide(new BigDecimal(vani), 2, RoundingMode.HALF_UP);
					} catch (Exception e) {
						log.debug("ERRORE NEL CALCOLO VANO MEDIO DPR 138: IL DATO SARA' VALORIZZATO UGUALE A 0", e);
						vanoMedio = (new BigDecimal("0"));
					}					
					dm.setVanoMedioDPR138(vanoMedio.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
				}
			}
		}
	}
	
	public InformativaTarsuDTO getUltimaDichTarsu(Immobile imm) throws Exception{
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		Context cont = new InitialContext();	
		
		TarsuService tarService  = (TarsuService)  getEjb("CT_Service", "CT_Service_Data_Access","TarsuServiceBean" ) ;
		
		RicercaOggettoTarsuDTO ro = new RicercaOggettoTarsuDTO();
		ro.setEnteId(enteId);
		ro.setUserId(userId);
		ro.setSessionId(sessionId);
	
		if(imm.getSezione()!=null && !imm.getSezione().equals("-"))
			ro.setSezione(imm.getSezione().trim());
		else
			ro.setSezione("");
		ro.setFoglio(imm.getFoglio());
		ro.setParticella(imm.getNumero());
		ro.setUnimm(imm.getUnimm());

		List<InformativaTarsuDTO> lista = tarService.getListaInformativaTarsu(ro);
		
		InformativaTarsuDTO last = null;
		if(lista!=null && lista.size()>0){
			int i = 1;
			last = lista.get(0);
			boolean trovato = last.getOggettoTarsu().getDatFin()==null;
			while(i<lista.size() && !trovato){
				InformativaTarsuDTO xx = lista.get(i);
				SitTTarOggetto o = xx.getOggettoTarsu();
				if(o.getDatFin()==null || o.getDatFin().after(last.getOggettoTarsu().getDatFin())){
					last = xx;
				}else if(o.getDatFin().equals(last.getOggettoTarsu().getDatFin()) && o.getDatIni().after(last.getOggettoTarsu().getDatIni())){
					last = xx;
				}
				
				i++;
			}
		
		}
		return last;
	}
	
	
	public ArrayList getSuperficiComma340 (Immobile imm) throws Exception
	{
		String foglio = imm.getFoglio();
		String particella = imm.getNumero();
		String subalterno = imm.getUnimm();
	
		ArrayList dettaglioVani = new ArrayList();
		
		ResultSet rsVani =null;
		Connection conn = null;
		try 	{
			sql = "SELECT vano, piano, edificio, ambiente, supe_ambiente, altezzamin, altezzamax "+
			"		FROM sitiedi_vani "+
			"		WHERE foglio = ? AND particella = lpad(?,5,'0') AND unimm = ? and data_fine_val = to_date('31-12-9999','dd-mm-yyyy')";

			this.initialize();
			this.setString(1,foglio);
			this.setString(2,particella);
			this.setString(3, (subalterno == null || subalterno.trim().equals("") || subalterno.equals("-")) ? "0":subalterno);
			prepareStatement(sql);
			conn = this.getConnection();

			rsVani = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		
			while (rsVani.next())
			{
				OggettiTARSU vani = new OggettiTARSU();
				vani.setVano(tornaValoreRS(rsVani,"vano"));
				vani.setPiano(tornaValoreRS(rsVani,"piano"));
				vani.setEdificio(tornaValoreRS(rsVani,"edificio"));
				vani.setAmbiente(tornaValoreRS(rsVani,"ambiente"));
				vani.setSupVani(tornaValoreRS(rsVani,"supe_ambiente"));
				vani.setAltezzaMin(tornaValoreRS(rsVani,"altezzamin"));
				vani.setAltezzaMax(tornaValoreRS(rsVani,"altezzamax"));
				dettaglioVani.add(vani);
			}		
		} catch (Exception e) {
			throw e;
		} finally {
			DbUtils.close(rsVani);
			DbUtils.close(conn);
		}

		this.impostaValSitiEdiUiuExt(imm);
		
		return dettaglioVani;

	}
	public Hashtable mCaricareListaImmobiliPerTitolarita(String titolarita) throws Exception{
		// String foglio,String particella,String subalterno,String comune,String partita, String zona,String categoria, String classe,String vani, String superficie,String rendita)
		// carico la lista dei comuni e le metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		String sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db

		HashMap<String,Immobile> temp = new HashMap();

		try {
			this.setDatasource(JNDI_CATCOSPOLETO);

			conn = this.getConnection();

				//ricavo propietari
				sql = "" +
			      "select distinct " +
			      //"cons_csui_tab.PKID_CSUI AS FK_TITOLARITA, " +
			      "sitiuiu.COD_NAZIONALE|| LPAD (sitiuiu.FOGLIO, 5, '0')" +
			      "|| sitiuiu.PARTICELLA|| LPAD (NVL (TRIM (sitiuiu.SUB), '0000'), 4, '0') AS FK_PAR_CATASTALI," +
			      //"sitipart.SE_ROW_ID AS PK_PARTICELLA," +
			      "decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			      "decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
			      "decode(sitiuiu.PARTICELLA,null,'-',sitiuiu.PARTICELLA) AS NUMERO," +
			      "sitiuiu.COD_NAZIONALE AS FK_COMUNE , " +
			      "sitiuiu.SUB AS SUBALTERNO," +
			      "sitiuiu.UNIMM AS UNIMM," +
			      //"decode(sitiuiu.PARTITA,null,'-',sitiuiu.PARTITA) as PARTITA ," +
			      //"decode(sitiuiu.ZONA,null,'-',sitiuiu.ZONA)as ZONA ," +
			      "decode(sitiuiu.CATEGORIA,null,'-',sitiuiu.CATEGORIA) AS CATEGORIA," +
			      //"sitiuiu.CLASSE," +
			      //"sitiuiu.RENDITA," +
			      //"sitiuiu.SUP_CAT as SUPERFICIE ," +
			      //"sitiuiu.CONSISTENZA, " +
			      "to_char(cons_csui_tab.DATA_FINE, 'dd/mm/yyyy')as DATA_FINE_POS, " +
			      "cons_csui_tab.data_fine AS data_fine_pos_date, " +
			      "to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy')as DATA_FINE_VAL, " +
			      "sitiuiu.DATA_FINE_VAL as DATA_FINE_VAL_DATE, " +
			      "to_char(sitiuiu.DATA_INIZIO_VAL, 'dd/mm/yyyy')as DATA_INIZIO_VAL, " +
			      "cons_deco_tab.description AS TITOLO, " +
			      "cons_csui_tab.perc_poss PERC_POSS " +
				  "from sitiuiu, cons_csui_tab, siticomu, cons_deco_tab " +
			      //",sitipart " +
			      "where cons_csui_tab.PK_CUAA = ? " +
			      "and sitiuiu.COD_NAZIONALE = cons_csui_tab.COD_NAZIONALE " +
			      "and sitiuiu.FOGLIO = cons_csui_tab.FOGLIO " +
			      "and sitiuiu.PARTICELLA = cons_csui_tab.PARTICELLA " +
			      "and sitiuiu.SUB = cons_csui_tab.SUB " +
			      "and sitiuiu.UNIMM = cons_csui_tab.UNIMM " +
			      //"and sitipart.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
			      //"and sitipart.FOGLIO = sitiuiu.FOGLIO " +
			      //"and sitipart.PARTICELLA = sitiuiu.PARTICELLA " +
			      //"and sitipart.SUB = sitiuiu.SUB " +
			      //"and ( cons_csui_tab.DATA_INIZIO between sitiuiu.DATA_INIZIO_VAL and sitiuiu.DATA_FINE_VAL " +
				  //"or cons_csui_tab.DATA_FINE between sitiuiu.DATA_INIZIO_VAL and sitiuiu.DATA_FINE_VAL ) " +
				  "and cons_csui_tab.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
				  "and cons_csui_tab.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
				  "and siticomu.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
				  "AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_csui_tab.tipo_documento " +
			      "order by TO_NUMBER(FOGLIO), NUMERO, SUBALTERNO, TO_NUMBER(UNIMM), DATA_FINE_VAL_DATE DESC,  data_fine_pos_date DESC";

				this.initialize();
				this.setString(1,titolarita);

				
			java.sql.ResultSet rs = null;
			try {
				log.debug("mCaricareListaImmobiliPerTitolarita-sql1["+sql+"]");
				log.debug("param["+titolarita+"]");
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next()){
					Immobile imm = new Immobile();
					imm.setSezione(rs.getString("SEZIONE"));
					imm.setFoglio(rs.getString("FOGLIO"));
					imm.setNumero(rs.getString("NUMERO"));
					imm.setSubalterno(rs.getString("SUBALTERNO"));
					imm.setUnimm(rs.getString("UNIMM"));
					//imm.setParticella(rs.getString("PK_PARTICELLA"));
					imm.setComune(rs.getString("FK_COMUNE"));
					//imm.setPartita(rs.getString("PARTITA"));
					//imm.setRendita(rs.getString("RENDITA"));
					//imm.setSuperficie(rs.getString("SUPERFICIE"));
					//imm.setVani(rs.getString("CONSISTENZA"));
					//imm.setZona(rs.getString("ZONA"));
					String titolo = rs.getString("TITOLO");
					imm.setTitolo(titolo!=null ? titolo : "PROPRIETARIO");
					imm.setCodCategoria(rs.getString("CATEGORIA"));
					imm.setDataFineVal(rs.getString("DATA_FINE_VAL"));
					imm.setDataInizioVal(rs.getString("DATA_INIZIO_VAL"));
					imm.setDataFinePos(rs.getString("DATA_FINE_POS"));
					BigDecimal pp = rs.getBigDecimal("PERC_POSS");
					BigDecimal percPoss = new BigDecimal(0);
					if (pp != null){
						imm.setPercPoss(pp);
						percPoss = pp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					}

					
					String key = imm.getComune()+"|" +
								 imm.getFoglio()+"|" +
								 imm.getNumero()+"|" +
								 imm.getUnimm()+"|" +
								 imm.getCategoria()+"|"+
								 imm.getTitolo()+"|"+
								 imm.getDataFinePos()+"|"+percPoss;
					
					if(!temp.containsKey(key)){
						vct.add(imm);
						temp.put(key, imm);
						//log.debug("Nuova Chiave:" + key);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				DbUtils.close(rs);
			}

				
				
				//ricavo altre titolarità cons_urui_tab
				String sql2 = "" +
			      "select distinct " +
			      //"cons_urui_tab.PKID_URUI AS FK_TITOLARITA, " +
			      "sitiuiu.COD_NAZIONALE|| LPAD (sitiuiu.FOGLIO, 5, '0')" +
			      "|| sitiuiu.PARTICELLA|| LPAD (NVL (TRIM (sitiuiu.SUB), '0000'), 4, '0') AS FK_PAR_CATASTALI," +
			      //"sitipart.SE_ROW_ID AS PK_PARTICELLA," +
			      "decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			      "decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
			      "decode(sitiuiu.PARTICELLA,null,'-',sitiuiu.PARTICELLA) AS NUMERO," +
			      "sitiuiu.COD_NAZIONALE AS FK_COMUNE , " +
			      "sitiuiu.SUB AS SUBALTERNO," +
			      "sitiuiu.UNIMM AS UNIMM," +
			      //"decode(sitiuiu.PARTITA,null,'-',sitiuiu.PARTITA) as PARTITA ," +
			      //"decode(sitiuiu.ZONA,null,'-',sitiuiu.ZONA)as ZONA ," +
			      "decode(sitiuiu.CATEGORIA,null,'-',sitiuiu.CATEGORIA) AS CATEGORIA," +
			      //"sitiuiu.CLASSE," +
			      //"sitiuiu.RENDITA," +
			      //"sitiuiu.SUP_CAT as SUPERFICIE ," +
			      //"sitiuiu.CONSISTENZA, " +
			      "to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy')as DATA_FINE_VAL, " +
			      "sitiuiu.DATA_FINE_VAL as DATA_FINE_VAL_DATE, " +
			      "to_char(sitiuiu.DATA_INIZIO_VAL, 'dd/mm/yyyy')as DATA_INIZIO_VAL, " +
			      "to_char(cons_urui_tab.DATA_FINE, 'dd/mm/yyyy')as DATA_FINE_POS, " +
			      "cons_urui_tab.data_fine AS data_fine_pos_date, " +
			      "cons_deco_tab.description AS TITOLO, " +
			      "cons_urui_tab.perc_poss PERC_POSS " +
			      "from sitiuiu, cons_urui_tab, siticomu, cons_deco_tab " +
			      //",sitipart " +
			      "where cons_urui_tab.PK_CUAA = ? " +
			      "and sitiuiu.COD_NAZIONALE = cons_urui_tab.COD_NAZIONALE " +
			      "and sitiuiu.FOGLIO = cons_urui_tab.FOGLIO " +
			      "and sitiuiu.PARTICELLA = cons_urui_tab.PARTICELLA " +
			      "and sitiuiu.SUB = cons_urui_tab.SUB " +
			      "and sitiuiu.UNIMM = cons_urui_tab.UNIMM " +
			      //"and sitipart.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
			      //"and sitipart.FOGLIO = sitiuiu.FOGLIO " +
			      //"and sitipart.PARTICELLA = sitiuiu.PARTICELLA " +
			      //"and sitipart.SUB = sitiuiu.SUB " +
			      //"and ( cons_urui_tab.DATA_INIZIO between sitiuiu.DATA_INIZIO_VAL and sitiuiu.DATA_FINE_VAL -1 " +
				  //"or cons_urui_tab.DATA_FINE between sitiuiu.DATA_INIZIO_VAL and sitiuiu.DATA_FINE_VAL -1 ) " +
			      "and cons_urui_tab.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
				  "and  cons_urui_tab.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
				  "and siticomu.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
				  "AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_urui_tab.tipo_documento " +
				  "order by TO_NUMBER(FOGLIO), NUMERO, SUBALTERNO, TO_NUMBER(UNIMM), DATA_FINE_VAL_DATE DESC, DATA_FINE_POS_DATE DESC";

				this.initialize();
				this.setString(1,titolarita);

				
				log.debug("mCaricareListaImmobiliPerTitolarita-sql2["+sql2+"]");
				log.debug("param["+titolarita+"]");
				
				
				java.sql.ResultSet rs2 = null;
				try {
					prepareStatement(sql2);
					rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs2.next()){
						Immobile imm = new Immobile();
						//imm.setChiave(rs.getString("FK_PAR_CATASTALI")); // la chiave
						imm.setSezione(rs2.getString("SEZIONE"));
						imm.setFoglio(rs2.getString("FOGLIO"));
						imm.setNumero(rs2.getString("NUMERO"));
						imm.setSubalterno(rs2.getString("SUBALTERNO"));
						imm.setUnimm(rs2.getString("UNIMM"));
						//imm.setParticella(rs2.getString("PK_PARTICELLA"));
						imm.setComune(rs2.getString("FK_COMUNE"));
						//imm.setPartita(rs2.getString("PARTITA"));
						//imm.setRendita(rs2.getString("RENDITA"));
						//imm.setSuperficie(rs2.getString("SUPERFICIE"));
						//imm.setVani(rs2.getString("CONSISTENZA"));
						//imm.setZona(rs2.getString("ZONA"));
						//String key = imm.getComune()+"|"+imm.getFoglio()+"|"+imm.getNumero()+"|"+imm.getSubalterno()+"|"+imm.getUnimm();
						//imm.setChiave(key);
						String titolo = rs2.getString("TITOLO");
						imm.setTitolo(titolo!=null ? titolo : "ALTRO");
						imm.setCodCategoria(rs2.getString("CATEGORIA"));
						imm.setDataFineVal(rs2.getString("DATA_FINE_VAL"));
						imm.setDataInizioVal(rs2.getString("DATA_INIZIO_VAL"));
						imm.setDataFinePos(rs2.getString("DATA_FINE_POS"));
						BigDecimal pp = rs2.getBigDecimal("PERC_POSS");
						BigDecimal percPoss = new BigDecimal(0);
						if (pp != null){
							imm.setPercPoss(pp);
							percPoss = pp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
						}
						
						String key = imm.getComune()+"|" +
									 imm.getFoglio()+"|" +
									 imm.getNumero()+"|" +
									 imm.getUnimm()+"|" +
									 imm.getCategoria()+"|"+
									 imm.getTitolo()+"|"+
									 imm.getDataFinePos()+"|"+percPoss;
						
						
						if(!temp.containsKey(key)){
							vct.add(imm);
							temp.put(key, imm);
						}
						
					}					
				} catch (Exception e) {
					throw e;
				} finally {
					DbUtils.close(rs);
				}


			ht.put("LISTA_IMMOBILI",vct);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = titolarita;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/

			return ht;
		}
		catch (Exception e) {
			log.error("mCaricareListaImmobiliPerTitolarita",e);
			throw e;
		}
		finally
		{
			DbUtils.close(conn);
		}

	}


	public Hashtable mCaricareListaImmobili2(Vector listaPar, ImmobiliFinder finder) throws Exception{
		// String foglio,String particella,String subalterno,String comune,String partita, String zona,String categoria, String classe,String vani, String superficie,String rendita)
		// carico la lista dei comuni e le metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		// faccio la connessione al db
		Connection conn = null;
		try {
			this.setDatasource(JNDI_CATCOSPOLETO);

			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			/*
			sql = SQL_SELECT_COUNT_ALL;
			int indice = 1;
			this.initialize();
			this.setInt(indice,1);
			indice ++;
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn);
			if (rs.next()){
				conteggione = rs.getLong("conteggio");
			}
			*/



			for (int i=0;i<=1;i++){
				// il primo ciclo faccio la count
				if (i==0)
					 sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					//if (!finder.getKeyStr().equals("")){
					sql = sql + " and sitiuiu.PKID_UIU in (" +finder.getKeyStr() + ")" ;
					//this.setString(indice,finder.getTitolarita());
					//indice ++;
				}

				//criterio di data fine validit = 31/12/9999
				//sql = sql + " AND DATA_FINE_VAL = to_date('99991231', 'yyyymmdd')";
				//criterio comune = milano
				//sql = sql + " AND COD_NAZIONALE = 'F205'";




				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;				
				if (i == 0) sql = sql + " ) "; 
				if (i ==1) sql = sql + " order by foglio,particella,unimm)  ) where N > " + limInf + " and N <=" + limSup;


				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					HashMap<String, GenericTuples.T2<String,String>> hmCoord = new HashMap<String, GenericTuples.T2<String,String>>();
					while (rs.next()){
						Immobile imm = new Immobile();
						String foglio = rs.getString("FOGLIO");
						try {
							foglio = "" + Integer.parseInt(foglio);
						} catch (Exception e) {}
						String unimm = rs.getString("UNIMM");
						try {
							unimm = "" + Integer.parseInt(unimm);
						} catch (Exception e) {}
						String key = rs.getString("FK_COMUNE")+"|"+foglio+"|"+rs.getString("PARTICELLA")+"|"+rs.getString("SUBALTERNO")+"|"+unimm;
						String graffato = rs.getObject("GRAFFATO") == null ? "N" : rs.getString("GRAFFATO");					
						imm.setComune(rs.getString("FK_COMUNE"));
						imm.setSezione(rs.getObject("SEZIONE") == null ? "-" : rs.getString("SEZIONE"));
						imm.setFoglio(foglio == null ? "-" : foglio);
						imm.setParticella(rs.getObject("PARTICELLA") == null ? "-" : rs.getString("PARTICELLA"));
						imm.setNumero(rs.getObject("PARTICELLA") == null ? "-" : rs.getString("PARTICELLA"));
						imm.setUnimm(unimm == null || unimm.equals(" ")? "-" : unimm);
						imm.setSubalterno(rs.getObject("SUBALTERNO") == null ? "-" : rs.getString("SUBALTERNO"));
						//imm.setCategoria(rs.getString("DESCATEGORIA"));
						//imm.setCodCategoria(rs.getString("CATEGORIA"));
						//imm.setDataFineVal(tornaValoreRS(rs,"DATA_FINE_VAL","YYYY-MM-DD"));
						//imm.setDataFineVal(rs.getString("DATA_FINE_VAL"));
						//imm.setDataInizioVal(rs.getString("DATA_INIZIO_VAL"));
						imm.setGraffato(graffato);
						
						
						this.setDateValiditaImmobile(conn, imm);
						this.setStatoImmobile(imm);
						
						setChiave(conn, imm, key, graffato);
						//setPrincGraffati(conn, imm, key);
						imm.setPrincGraffati(this.getListaGraffati(conn,imm,key).size()>0);
						
						String codiFiscLuna = rs.getString("CODI_FISC_LUNA");
						String keyCoord = imm.getFoglio() + "|" + imm.getParticella() + "|" + codiFiscLuna;
						if (hmCoord.get(keyCoord) == null) {
							GenericTuples.T2<String,String> coord1 = null;
							try {
								coord1 = getLatitudeLongitude(imm.getFoglio(), imm.getParticella(), codiFiscLuna);
							} catch (Exception e) {
							}
							hmCoord.put(keyCoord, coord1);
						}
						
						GenericTuples.T2<String,String> coord = hmCoord.get(keyCoord);
						if (coord!=null) {
							imm.setLatitudine(coord.firstObj);
							imm.setLongitudine(coord.secondObj);
						}
						
						if (imm.getDataFineVal() != null && imm.getDataFineVal().equals("31/12/9999")){
							imm.setEvidenza(true);
						
						}
						
						
						
						vct.add(imm);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_IMMOBILI",vct);
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
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Hashtable mCaricareListaImmobiliFromVia(String idVia, ImmobiliFinder finder) throws Exception{
		// String foglio,String particella,String subalterno,String comune,String partita, String zona,String categoria, String classe,String vani, String superficie,String rendita)
		// carico la lista dei comuni e le metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		// faccio la connessione al db
		Connection conn = null;
		try {
			this.setDatasource(JNDI_CATCOSPOLETO);

			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			java.sql.ResultSet rsCat = null;
			
			
					sql = SQL_SELECT_COORD_CAT_FROM_VIA;

				indice = 0;
				this.initialize();
				
				
				String[] chiaviArr= idVia.split("\\|",-1);
				
				String idImm=chiaviArr[0];
				String progressivo=chiaviArr[1];
				String seq=chiaviArr[2];
				String codFiscLuna=chiaviArr[3];
				String sezione=chiaviArr[4];

				
					this.setString(++indice,idImm);
					this.setString(++indice,progressivo);
					this.setString(++indice,seq);
					this.setString(++indice,codFiscLuna);
					this.setString(++indice,sezione);


				prepareStatement(sql);
				rsCat = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
				ArrayList listaCoordinateCat= new ArrayList();
				
				
				while (rsCat.next()){
					
					String[] arrCoordinateCat= new String[3];
					String foglio= rsCat.getString("FOGLIO");
					String mappale= rsCat.getString("MAPPALE");
					String sub= rsCat.getString("SUB");
					
					arrCoordinateCat[0]=foglio;
					arrCoordinateCat[1]=mappale;
					arrCoordinateCat[2]=sub;
					
					listaCoordinateCat.add(arrCoordinateCat);
					
				}
				
				for (int i=0; i<listaCoordinateCat.size(); i++){
					
					String[] arrCoordinateCat=(String[])listaCoordinateCat.get(i);
					
					sql = SQL_SELECT_LISTA_FROM_VIA;
					
					this.initialize();
					 indice=0;
					
					 if (arrCoordinateCat[0] != null && !arrCoordinateCat[0].equals("") && !arrCoordinateCat[0].equals(" "))
						 this.setNumber(++indice,Integer.valueOf(arrCoordinateCat[0]));
					 else
						 this.setNumber(++indice,0);
					 
					this.setString(++indice,arrCoordinateCat[1]);
					this.setString(++indice,arrCoordinateCat[2]);
					this.setString(++indice,arrCoordinateCat[2]);
					
					if (arrCoordinateCat[2] != null && !arrCoordinateCat[2].equals("") && !arrCoordinateCat[2].equals(" "))
						 this.setNumber(++indice,Integer.valueOf(arrCoordinateCat[2]));
					 else
						 this.setNumber(++indice,0);
					
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					
					while (rs.next()){
						Immobile imm = new Immobile();
						//imm.setChiave(rs.getString("PKID_UIU")); // la chiave						
						imm.setComune(rs.getString("FK_COMUNE"));
						imm.setSezione(rs.getString("SEZIONE"));
						imm.setFoglio(rs.getString("FOGLIO"));
						imm.setParticella(rs.getString("PARTICELLA"));
						imm.setNumero(rs.getString("PARTICELLA"));
						imm.setUnimm(rs.getString("UNIMM"));
						imm.setSubalterno(rs.getString("SUBALTERNO"));
						//imm.setCategoria(rs.getString("DESCATEGORIA"));
						//imm.setCodCategoria(rs.getString("CATEGORIA"));
						//imm.setDataFineVal(tornaValoreRS(rs,"DATA_FINE_VAL","YYYY-MM-DD"));
						//imm.setDataFineVal(rs.getString("DATA_FINE_VAL"));
						//imm.setDataInizioVal(rs.getString("DATA_INIZIO_VAL"));
						
						
						this.setDateValiditaImmobile(conn, imm);
						this.setStatoImmobile(imm);
						
						String key = rs.getString("FK_COMUNE")+"|"+rs.getString("FOGLIO")+"|"+rs.getString("PARTICELLA")+"|"+rs.getString("SUBALTERNO")+"|"+rs.getString("UNIMM");
						key += "|"+(imm.getDataFineVal() == null || imm.getDataFineVal().equalsIgnoreCase("ATTUALE") ? "31/12/9999" : imm.getDataFineVal()) +
						"|"+imm.getDataInizioVal();
						imm.setChiave(key);
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(imm.getFoglio(), imm.getParticella(), imm.getComune());
						} catch (Exception e) {
						}
						if (coord!=null) {
							imm.setLatitudine(coord.firstObj);
							imm.setLongitudine(coord.secondObj);
						}
						
						if (imm.getDataFineVal().equals("31/12/9999")){
							imm.setEvidenza(true);
						
						}
						
						
						vct.add(imm);
					}
				
				}
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_IMMOBILI",vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = idVia;
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
	
	public Hashtable mCaricareDettaglioImmobiliFromId(String pkId, String pathDatiDiogene) throws Exception{
		// String foglio,String particella,String subalterno,String comune,String partita, String zona,String categoria, String classe,String vani, String superficie,String rendita)
		// carico la lista dei comuni e le metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = SQL_SELECT_LISTA_FROM_ID;
		
		// faccio la connessione al db
		Connection conn = null;
		try {
			this.setDatasource(JNDI_CATCOSPOLETO);

			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
					this.initialize();
					this.setString(indice, pkId);
					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					
					String key = "";
					while (rs.next()){
						Immobile imm = new Immobile();
						//imm.setChiave(rs.getString("PKID_UIU")); // la chiave
						//key = rs.getString("FK_COMUNE")+"|"+rs.getString("FOGLIO")+"|"+rs.getString("PARTICELLA")+"|"+rs.getString("SUBALTERNO")+"|"+rs.getString("UNIMM");
						
						imm.setChiave(key);
						imm.setComune(rs.getString("FK_COMUNE"));
						imm.setSezione(rs.getString("SEZIONE"));
						imm.setFoglio(rs.getString("FOGLIO"));
						imm.setParticella(rs.getString("PARTICELLA"));
						imm.setNumero(rs.getString("PARTICELLA"));
						imm.setUnimm(rs.getString("UNIMM"));
						imm.setSubalterno(rs.getString("SUBALTERNO"));
												
						
						this.setDateValiditaImmobile(conn, imm);
						
						key= imm.getChiave();
						/*
						 * 
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(imm.getFoglio(), imm.getParticella(), imm.getComune());
						} catch (Exception e) {
						}
						if (coord!=null) {
							imm.setLatitudine(coord.firstObj);
							imm.setLongitudine(coord.secondObj);
						}
						
						if (imm.getDataFineVal().equals("31/12/9999")){
							imm.setEvidenza(true);
						
						}
						
						
						
						vct.add(imm);
						*/
						
						
					}
				
				
		/*	ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_IMMOBILI",vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			return ht;*/
					
					return mCaricareDettaglioImmobile( key,  "", pathDatiDiogene);
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
	
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception
	{
		String sql = "";
		Vector listaParNew= new Vector();
		soloAtt = false;
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if ("SOLO_ATT".equalsIgnoreCase(el.getAttributeName())) {
				soloAtt = "Y".equalsIgnoreCase(el.getValore());
			} else {
				listaParNew.add(el);				
			}
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParNew);
		
		if (soloAtt) {
			sql = sql + " and DATA_FINE_VAL= to_date('31/12/9999','dd/MM/yyyy') ";
		}
		
		boolean allCat = false;
		for (int i = 0; i < listaParNew.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaParNew.get(i);
			if ("CATEGORIA".equalsIgnoreCase(el.getAttributeName())) {
				String valore = el.getValore() == null ? "" : el.getValore().trim();
				allCat = "".equals(valore);				
			}
		}
		if (allCat) {
			for (int i = 0; i < listaParNew.size(); i++) {
				EscElementoFiltro el = (EscElementoFiltro)listaParNew.get(i);				
				String valore = el.getValore();
				if (valore != null && !valore.trim().equals("")) {
					if (!el.getTipo().equals("O")) {
						indice++;
					}
				}
			}
			sql += " UNION ALL ";
			sql += SQL_UIU_GRAFFATE;
			for (int i = 0; i < listaParNew.size(); i++) {
				EscElementoFiltro el = (EscElementoFiltro)listaParNew.get(i);				
				String valore = el.getValore();
				if (valore != null && !valore.trim().equals("")) {					
					if ("Comune".equalsIgnoreCase(el.getAttributeName())) {
						sql += " AND LC.CODI_FISC_LUNA = ? ";
						setString(indice, valore);
						indice++;
					}
					if ("FOGLIO".equalsIgnoreCase(el.getAttributeName())) {
						sql += " AND LC.FOGLIO = ? ";
						setString(indice, StringUtils.padding(valore, 4, '0', true));
						indice++;
					}
					if ("PARTICELLA".equalsIgnoreCase(el.getAttributeName())) {
						sql += " AND LC.MAPPALE = ? ";
						setString(indice, StringUtils.padding(valore, 5, '0', true));
						indice++;
					}
					if ("SUBALTERNO".equalsIgnoreCase(el.getAttributeName())) {
						sql += " AND LC.SUB = ? ";
						setString(indice, StringUtils.padding(valore, 4, '0', true));
						indice++;
					}
					if ("ANNOTAZIONI".equalsIgnoreCase(el.getAttributeName())) {
						sql += " AND EXISTS (SELECT 1 FROM SITIUIU UIU, SITICOMU C WHERE ";
						sql += " TO_CHAR(UIU.FOGLIO) = LC.FOGLIO ";
						sql += " AND UIU.PARTICELLA = LC.MAPPALE ";
						sql += " AND UIU.SUB = LC.SUB ";
						sql += " AND C.COD_NAZIONALE = UIU.COD_NAZIONALE ";
						sql += " AND C.CODI_FISC_LUNA = LC.CODI_FISC_LUNA ";
						if (el.getOperatore().toLowerCase().trim().equals("like")) {
							sql += " AND UIU.ANNOTAZIONI LIKE '%'||?||'%') ";
						} else if (el.getOperatore().trim().equals("=")) {
							sql += " AND UIU.ANNOTAZIONI = ?) ";
						}						
						setString(indice, valore);
						indice++;
					}
				}				
			}
		}
		

		return sql;
	}

	

	private ArrayList<Immobile> getListaStoricoSubalterno(List<String> listParam) throws Exception{
	

		Connection conn= null;
		
		ArrayList<Immobile> listaImmobiliStorico= new ArrayList<Immobile>();
		
		try{
			
			
			String sqlUiuStorico ="select " +
			"decode(sitiuiu.COD_NAZIONALE,null,'-',sitiuiu.COD_NAZIONALE) AS FK_COMUNE, " +
			"sitiuiu.PARTICELLA AS PARTICELLA," +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			"decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
			"decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) as SUBALTERNO," +
			"decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) as UNIMM," +
			"decode(sitiuiu.CATEGORIA,null,'-',sitiuiu.CATEGORIA) AS CATEGORIA," +
			"sitideco.DESCRIPTION AS desCategoria," +
			"decode(sitiuiu.consistenza,null,'-',sitiuiu.consistenza) AS VANI," +
			"decode(sitiuiu.SUP_CAT,null,'-',sitiuiu.SUP_CAT) AS SUPERFICIE," +
			"decode(sitiuiu.ZONA,null,'-',sitiuiu.ZONA)as ZONA," +
			"decode(sitiuiu.MICROZONA,null,'-',sitiuiu.MICROZONA) AS MICROZONA," +
			"decode(sitiuiu.RENDITA,null,'-',sitiuiu.RENDITA) AS RENDITA," +
			"decode(sitiuiu.CLASSE,null,'-',sitiuiu.CLASSE) AS CLASSE," +
			"decode(sitiuiu.PARTITA,null,'-',sitiuiu.PARTITA) as PARTITA," +
			"decode(sitiuiu.PIANO,null,'-',sitiuiu.PIANO) as PIANO," +
			"sitiuiu.ANNOTAZIONI AS ANNOTAZIONI, " +
			"to_char(sitiuiu.DATA_INIZIO_VAL, 'dd/mm/yyyy')as DATA_INIZIO_VAL, " +
			"to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy')as DATA_FINE_VAL, " +
			"sitiuiu.IDE_MUTA_INI as IDE_MUTA_INI , "+
			"sitiuiu.IDE_MUTA_FINE as IDE_MUTA_FINE, "+
			"decode(lcu.protocollo_notif,null,'-',lcu.protocollo_notif) as PROT_NOTIFICA, " +
			"decode(lcu.data_notifica,null,'-',TO_CHAR(TO_DATE(lcu.data_notifica,'ddMMyyyy'),'dd/MM/yyyy')) as DT_NOTIFICA, " +
			"decode(lcu.cod_cau_atto_ini,null,'-',lcu.cod_cau_atto_ini) AS COD_ATTO_INI, " +
			"decode(lcu.des_atto_ini,null,'-',lcu.des_atto_ini) as DES_ATTO_INI, " +
			"decode(lcu.cod_cau_atto_fin,null,'-',lcu.cod_cau_atto_fin) as COD_ATTO_FINE, " +
			"decode(lcu.des_atto_fin,null,'-',lcu.des_atto_fin) as DES_ATTO_FINE, " +
			"lcu.fl_classamento as FLAG_CLASSAMENTO " +
			"from sitiuiu,sitideco,siticomu, load_cat_uiu_id id, load_cat_uiu lcu " +
			"where "+CONDIZIONE_SITICOMU +
			"and sitiuiu.FOGLIO = ? " +
			"and sitiuiu.PARTICELLA = ? " +
			"and decode (NVL(sitiuiu.SUB,'*'),'*','-',' ','-',sitiuiu.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
			"and sitiuiu.UNIMM = ? " +
			"and sitideco.TABLENAME = 'SITIUIU' " +
			"and sitideco.FIELDNAME = 'CATEGORIA' " +
			"and sitideco.CODE = sitiuiu.CATEGORIA " +
			"and sitiuiu.COD_NAZIONALE = siticomu.COD_NAZIONALE " +
			"and id.id_imm = lcu.id_imm and id.progressivo = lcu.progressivo " +
			"and id.seq = lcu.seq " +
			"and id.sezione=lcu.sezione " +
			"and id.codi_fisc_luna = lcu.codi_fisc_luna " +
			"and id.codi_fisc_luna = siticomu.codi_fisc_luna " +
			//"and siticomu.codi_fisc_luna=sitiuiu.cod_nazionale " +
			"and id.sez = siticomu.sezione_censuaria "+
			"and LPAD(id.foglio,4,'0') = LPAD(sitiuiu.foglio,4,'0') " +
			"and LPAD(id.mappale,5,'0') = LPAD(sitiuiu.particella,5,'0') " +
			"and LPAD(DECODE(id.sub,null,'0',' ','0',id.sub),4,'0') = LPAD(sitiuiu.unimm,4,'0') " +
			"and NVL(lcu.id_mut_ini,-1)=NVL(sitiuiu.ide_muta_ini,-1) " +
			"and NVL(lcu.id_mut_fin,-1)=NVL(sitiuiu.ide_muta_fine,-1) " +			
			"order by sitiuiu.DATA_INIZIO_VAL desc, sitiuiu.DATA_FINE_VAL desc";
	
			int index = 0;
			this.initialize();
			this.setString(++index,(String)listParam.get(0));
			
			/*NB Rimossa la condizione doppia su siticomu perchè sui comuni tipo CHIARI aventi uno stesso immobile su due sezioni diverse, se cerco l'immobile avente cod_nazionale = codi_fisc_luna='C618'
			con la (siticomu.COD_NAZIONALE = ? OR siticomu.CODI_FISC_LUNA= ?) becco nello storico non solo gli immobili della sezione di interesse ma anche delle altre (N884) --> vd esempio FPS = 9-151-1 */
			
			//this.setString(++index,(String)listParam.get(0)); 
			
			this.setString(++index,(String)listParam.get(1));
			this.setString(++index,(String)listParam.get(2));
			this.setString(++index,(String)listParam.get(3));
		
			this.setString(++index,(String)listParam.get(3));
			this.setString(++index,(String)listParam.get(4));
				
			conn= this.getConnection();
			prepareStatement(sqlUiuStorico);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
		
			
			while (rs.next()){
				Immobile imm= new Immobile();
			
				imm.setNumero(rs.getString("PARTICELLA"));
				imm.setFoglio(rs.getString("FOGLIO"));
				imm.setSezione(rs.getString("SEZIONE"));
				imm.setSubalterno(rs.getString("SUBALTERNO"));
				imm.setUnimm(rs.getString("UNIMM"));
				imm.setCategoria(rs.getString("desCategoria"));
				imm.setCodCategoria(rs.getString("CATEGORIA"));
				imm.setComune(rs.getString("FK_COMUNE"));
				imm.setPartita(rs.getString("PARTITA"));
				imm.setRendita(rs.getString("RENDITA"));
				imm.setMicrozona(rs.getString("MICROZONA"));
				imm.setSuperficie(rs.getString("SUPERFICIE"));
				imm.setVani(rs.getString("VANI"));
				imm.setPiano(rs.getString("PIANO"));
				imm.setZona(rs.getString("ZONA"));
				imm.setClasse(rs.getString("CLASSE"));
				imm.setDataFineVal(rs.getString("DATA_FINE_VAL"));
				
				if (imm.getDataFineVal()!= null && imm.getDataFineVal().equals("31/12/9999"))
					imm.setDataFineVal("ATTUALE");
				
				imm.setDataInizioVal(rs.getString("DATA_INIZIO_VAL"));
				imm.setAnnotazione(rs.getString("ANNOTAZIONI") != null ? rs.getString("ANNOTAZIONI"): "");
				imm.setIdeMutaIni(rs.getInt("IDE_MUTA_INI"));
				imm.setIdeMutaFine(rs.getInt("IDE_MUTA_FINE"));
				
				imm.setProtNotifica(rs.getString("PROT_NOTIFICA"));
				imm.setDataNotifica(rs.getString("DT_NOTIFICA"));
				imm.setCodAttoIni(rs.getString("COD_ATTO_INI"));
				imm.setDescAttoIni(rs.getString("DES_ATTO_INI"));
				imm.setCodAttoFine(rs.getString("COD_ATTO_FINE"));
				imm.setDescAttoFine(rs.getString("DES_ATTO_FINE"));
				imm.setCodClassamento(rs.getString("FLAG_CLASSAMENTO"));
				
				listaImmobiliStorico.add(imm);
				
			}
			if (listaImmobiliStorico.size() == 0) {
				throw new Exception("LISTA STORICO SUBALTERNO NON VALORIZZATA, IMPOSSIBILE PROCEDERE CON LA VISUALIZZAZIONE DEL DETTAGLIO DELL'IMMOBILE");
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null)
			{
				if (!conn.isClosed())
					conn.close();
				conn = null;
			}
		}
	
		return listaImmobiliStorico;
			
		
	}

	private void loadInfoAttoUiu(Immobile imm) throws Exception{
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(enteId);
		rc.setUserId(userId);
		rc.setSessionId(sessionId);
		rc.setFoglio(imm.getFoglio());
		rc.setParticella(imm.getNumero());
		rc.setSub(imm.getSubalterno());
		rc.setUnimm(imm.getUnimm());
		
		
		
	}

	
	private void setStatoImmobile(Immobile imm){
		
		String dataFineVal= imm.getDataFineVal();
		
		if (dataFineVal == null) {
			imm.setStato("-");
			return;
		}
		
		if (dataFineVal.equals("31/12/9999")){
			
			imm.setStato("ATTUALE");
		}
		else
			imm.setStato("CESSATO IL " + imm.getDataFineVal() );
		
	}

	private ArrayList<Immobile> getImmobiliDerivanti(Immobile imm,  List<String> listParam) throws Exception{
		
		
	Connection conn= null;
	ArrayList<Immobile> listaImmobiliDerivanti= new ArrayList<Immobile>();
	
	try{
		
		
		
		String sqlUiuDerivante ="select " +
		"decode(sitiuiu.COD_NAZIONALE,null,'-',sitiuiu.COD_NAZIONALE) AS FK_COMUNE, " +
		"sitiuiu.PARTICELLA AS PARTICELLA," +
		"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
		"decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
		"decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) as SUBALTERNO," +
		"decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) as UNIMM," +
		"decode(sitiuiu.CATEGORIA,null,'-',sitiuiu.CATEGORIA) AS CATEGORIA," +
		"sitideco.DESCRIPTION AS desCategoria," +
		"(select to_char(min(u.DATA_INIZIO_VAL),'dd/mm/yyyy') from sitiuiu u where u.COD_NAZIONALE= sitiuiu.COD_NAZIONALE " +
		"and  decode(u.FOGLIO,null,'-',u.FOGLIO) = decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) " +
		"and u.PARTICELLA= sitiuiu.PARTICELLA and decode(u.SUB,null,'-',u.SUB)= decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) "+
		" and decode(u.UNIMM,null,'-',u.UNIMM)= decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) ) as DATA_INIZIO_VAL, "+
		"(select to_char(max(u.DATA_FINE_VAL),'dd/mm/yyyy') from sitiuiu u where u.COD_NAZIONALE= sitiuiu.COD_NAZIONALE " +
		"and  decode(u.FOGLIO,null,'-',u.FOGLIO) = decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) " +
		"and u.PARTICELLA= sitiuiu.PARTICELLA and decode(u.SUB,null,'-',u.SUB)= decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) "+
		" and decode(u.UNIMM,null,'-',u.UNIMM)= decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) ) as DATA_FINE_VAL, "+
		"sitiuiu.IDE_MUTA_INI as IDE_MUTA_INI ,"+
		"sitiuiu.IDE_MUTA_FINE as IDE_MUTA_FINE "+
		"from sitiuiu,sitideco,siticomu " +
		"where "+CONDIZIONE_SITICOMU +
		//"and sitiuiu.FOGLIO = ? " +
		//"and sitiuiu.PARTICELLA = ? " +
		//"and decode (NVL(sitiuiu.SUB,'*'),'*','-',' ','-',sitiuiu.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
		//"and sitiuiu.UNIMM = ? " +
		"and sitideco.TABLENAME = 'SITIUIU' " +
		"and sitideco.FIELDNAME = 'CATEGORIA' " +
		"and sitideco.CODE = sitiuiu.CATEGORIA " +
		//"and to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy') = ? " +
		"and sitiuiu.COD_NAZIONALE = siticomu.COD_NAZIONALE " + 
		"and sitiuiu.IDE_MUTA_FINE = ? ";
		
		log.debug("getImmobiliDerivanti SQL["+sql+"]");
		log.debug("getImmobiliDerivanti PARAMS[ideMutaIni:"+imm.getIdeMutaIni()+"]");
	
		int index = 0;
		this.initialize();
			this.setString(++index,(String)listParam.get(0));
			//this.setString(++index,(String)listParam.get(0));
			this.setInt(++index,imm.getIdeMutaIni());
		
		
			conn= this.getConnection();
			prepareStatement(sqlUiuDerivante);
			java.sql.ResultSet rsUiuDerivante = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		
		
		
		while (rsUiuDerivante.next()){
			Immobile immDerivante= new Immobile();
		
			immDerivante.setComune(rsUiuDerivante.getString("FK_COMUNE"));
			immDerivante.setNumero(rsUiuDerivante.getString("PARTICELLA"));
			immDerivante.setFoglio(rsUiuDerivante.getString("FOGLIO"));
			immDerivante.setSezione(rsUiuDerivante.getString("SEZIONE"));
			immDerivante.setSubalterno(rsUiuDerivante.getString("SUBALTERNO"));
			immDerivante.setUnimm(rsUiuDerivante.getString("UNIMM"));
			immDerivante.setCategoria(rsUiuDerivante.getString("desCategoria"));
			immDerivante.setCodCategoria(rsUiuDerivante.getString("CATEGORIA"));
			immDerivante.setDataFineVal(rsUiuDerivante.getString("DATA_FINE_VAL"));
			immDerivante.setDataInizioVal(rsUiuDerivante.getString("DATA_INIZIO_VAL"));
			immDerivante.setIdeMutaIni(rsUiuDerivante.getInt("IDE_MUTA_INI"));
			immDerivante.setIdeMutaFine(rsUiuDerivante.getInt("IDE_MUTA_FINE"));
			
				if (!(immDerivante.getFoglio().equals(imm.getFoglio()) && immDerivante.getParticella().equals(imm.getParticella()) && immDerivante.getSubalterno().equals(imm.getSubalterno()) &&  immDerivante.getUnimm().equals(imm.getUnimm()))){
					this.setStatoImmobile( immDerivante);
					listaImmobiliDerivanti.add(immDerivante);
				}
			}
		
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null)
			{
				if (!conn.isClosed())
					conn.close();
				conn = null;
			}
		}
		return listaImmobiliDerivanti;
	}
	
	
	

	private ArrayList<Immobile> getImmobiliDerivati(Immobile imm,  List<String> listParam) throws Exception{
	
	
		Connection conn= null;
		
		ArrayList<Immobile> listaImmobiliDerivati= new ArrayList<Immobile>();
		
		try{
		
		
		String sqlUiuDerivate ="select " +
		"decode(sitiuiu.COD_NAZIONALE,null,'-',sitiuiu.COD_NAZIONALE) AS FK_COMUNE, " +
		"sitiuiu.PARTICELLA AS PARTICELLA," +
		"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
		"decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) AS FOGLIO," +
		"decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) as SUBALTERNO," +
		"decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) as UNIMM," +
		"decode(sitiuiu.CATEGORIA,null,'-',sitiuiu.CATEGORIA) AS CATEGORIA," +
		"sitideco.DESCRIPTION AS desCategoria," +
		"(select to_char(min(u.DATA_INIZIO_VAL),'dd/mm/yyyy') from sitiuiu u where u.COD_NAZIONALE= sitiuiu.COD_NAZIONALE " +
		"and  decode(u.FOGLIO,null,'-',u.FOGLIO) = decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) " +
		"and u.PARTICELLA= sitiuiu.PARTICELLA and decode(u.SUB,null,'-',u.SUB)= decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) "+
		" and decode(u.UNIMM,null,'-',u.UNIMM)= decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) ) as DATA_INIZIO_VAL, "+
		"(select to_char(max(u.DATA_FINE_VAL),'dd/mm/yyyy') from sitiuiu u where u.COD_NAZIONALE= sitiuiu.COD_NAZIONALE " +
		"and  decode(u.FOGLIO,null,'-',u.FOGLIO) = decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) " +
		"and u.PARTICELLA= sitiuiu.PARTICELLA and decode(u.SUB,null,'-',u.SUB)= decode(sitiuiu.SUB,null,'-',sitiuiu.SUB) "+
		" and decode(u.UNIMM,null,'-',u.UNIMM)= decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) ) as DATA_FINE_VAL, "+
		"sitiuiu.IDE_MUTA_INI as IDE_MUTA_INI ,"+
		"sitiuiu.IDE_MUTA_FINE as IDE_MUTA_FINE "+
		"from sitiuiu,sitideco,siticomu " +
		"where "+CONDIZIONE_SITICOMU +
		//"and sitiuiu.FOGLIO = ? " +
		//"and sitiuiu.PARTICELLA = ? " +
		//"and decode (NVL(sitiuiu.SUB,'*'),'*','-',' ','-',sitiuiu.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
		//"and sitiuiu.UNIMM = ? " +
		"and sitideco.TABLENAME = 'SITIUIU' " +
		"and sitideco.FIELDNAME = 'CATEGORIA' " +
		"and sitideco.CODE = sitiuiu.CATEGORIA " +
		//"and to_char(sitiuiu.DATA_FINE_VAL, 'dd/mm/yyyy') = ? " +
		"and sitiuiu.COD_NAZIONALE = siticomu.COD_NAZIONALE " + 
		"and sitiuiu.IDE_MUTA_INI = ? " +
		//clausola aggiunta per evitare doppioni (caso del comune di Chiari), cioè:
		//scarta le uiu con ide_muta_fine not null che ne hanno un'altra uguale (stessi sez-f-p-s) con stesso ide_muta_ini ed ide_muta_fine null
		"and not exists (select 1 from sitiuiu b " +
		"where b.IDE_MUTA_FINE IS NULL " +
		"and sitiuiu.IDE_MUTA_FINE IS NOT NULL " +
		"and sitiuiu.IDE_MUTA_INI = b.IDE_MUTA_INI " +
		"and decode(sitiuiu.COD_NAZIONALE,null,'-',sitiuiu.COD_NAZIONALE) = decode(b.COD_NAZIONALE,null,'-',b.COD_NAZIONALE) " +
		"and sitiuiu.PARTICELLA = b.PARTICELLA " +
		"and decode(sitiuiu.FOGLIO,null,'-',sitiuiu.FOGLIO) = decode(b.FOGLIO,null,'-',b.FOGLIO) " +
		"and decode(sitiuiu.SUB,null,'-',sitiuiu.SUB)  = decode(b.SUB,null,'-',b.SUB) " +
		"and decode(sitiuiu.UNIMM,null,'-',sitiuiu.UNIMM) = decode(b.UNIMM,null,'-',b.UNIMM)) " +
		//aggiunto order by
		"order by SEZIONE, FOGLIO, PARTICELLA, LPAD(UNIMM, 5, '0')";
		
		log.debug("getImmobiliDerivati SQL["+sqlUiuDerivate+"]");
		log.debug("getImmobiliDerivati PARAMS[ideMutaFine:"+imm.getIdeMutaFine()+"]");

		int index = 0;
		this.initialize();
		this.setString(++index,(String)listParam.get(0));
	  //this.setString(++index,(String)listParam.get(0));
		this.setInt(++index,imm.getIdeMutaFine());
		
		
		conn= this.getConnection();
		prepareStatement(sqlUiuDerivate);
		java.sql.ResultSet rsUiuDerivate = executeQuery(conn, this.getClass().getName(), getEnvUtente());
	
		
		
		while (rsUiuDerivate.next()){
			Immobile immDerivato= new Immobile();
		
			immDerivato.setComune(rsUiuDerivate.getString("FK_COMUNE"));
			immDerivato.setNumero(rsUiuDerivate.getString("PARTICELLA"));
			immDerivato.setFoglio(rsUiuDerivate.getString("FOGLIO"));
			immDerivato.setSezione(rsUiuDerivate.getString("SEZIONE"));
			immDerivato.setSubalterno(rsUiuDerivate.getString("SUBALTERNO"));
			immDerivato.setUnimm(rsUiuDerivate.getString("UNIMM"));
			immDerivato.setCategoria(rsUiuDerivate.getString("desCategoria"));
			immDerivato.setCodCategoria(rsUiuDerivate.getString("CATEGORIA"));
			immDerivato.setDataFineVal(rsUiuDerivate.getString("DATA_FINE_VAL"));
			immDerivato.setDataInizioVal(rsUiuDerivate.getString("DATA_INIZIO_VAL"));
			immDerivato.setIdeMutaIni(rsUiuDerivate.getInt("IDE_MUTA_INI"));
			immDerivato.setIdeMutaFine(rsUiuDerivate.getInt("IDE_MUTA_FINE"));
			
			if (!(immDerivato.getFoglio().equals(imm.getFoglio()) && immDerivato.getParticella().equals(imm.getParticella()) && immDerivato.getSubalterno().equals(imm.getSubalterno()) &&  immDerivato.getUnimm().equals(imm.getUnimm()))){
			
				this.setStatoImmobile( immDerivato);
				
				
				listaImmobiliDerivati.add(immDerivato);
			}
		
		}
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null)
			{
				if (!conn.isClosed())
					conn.close();
				conn = null;
			}
		}
		return listaImmobiliDerivati;
		
		
	}
	
	
	
	private void setChiave(Connection conn, Immobile imm, String key, String graffato) throws Exception {
		boolean isGraffato = graffato != null && graffato.equalsIgnoreCase("Y");
		
		if (isGraffato) {
			imm.setChiaveGraffato(key);
			
			String[] keyFields = key.split("\\|",-1);
			String codNazionale = keyFields[0];
			String foglio = StringUtils.padding(keyFields[1], 4, '0', true);
			String particella = StringUtils.padding(keyFields[2], 5, '0', true);
			String subalterno = keyFields[3];
			String unimm = (keyFields[4].equals(" ")||keyFields[4].equals("0"))? " " : StringUtils.padding(keyFields[4], 4, '0', true);
			
			String sql = SQL_UIU_GRAFFATA;
			PreparedStatement ps = conn.prepareStatement(sql);
			int index = 0;
			ps.setString(++index, codNazionale);
			ps.setString(++index, foglio);
			ps.setString(++index, particella);
			ps.setString(++index, unimm);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				codNazionale = rs.getString("FK_COMUNE");
				foglio = rs.getString("FOGLIO");
				particella = rs.getString("MAPPALE");
				unimm = rs.getString("SUB");
			}
			rs.close();
			ps.close();
			
			try {
				foglio = "" + Integer.parseInt(foglio);
			} catch (Exception e) {
				foglio = "0";
			}
			try {
				unimm = "" + Integer.parseInt(unimm);
			} catch (Exception e) {
				unimm = "0";
			}			
			
			Immobile imm1 = new Immobile();
			imm1.setComune(codNazionale);
			imm1.setFoglio(foglio);
			imm1.setNumero(particella);
			imm1.setSubalterno(subalterno);
			imm1.setUnimm(unimm);
			this.setDateValiditaImmobile(conn, imm1);
			
			key = codNazionale+"|"+foglio+"|"+particella+"|"+subalterno+"|"+unimm;
			key += "|"+(imm1.getDataFineVal() == null || imm1.getDataFineVal().equalsIgnoreCase("ATTUALE") ? "31/12/9999" : imm1.getDataFineVal()) +
			"|"+imm1.getDataInizioVal();
		} else {
			imm.setChiaveGraffato("");
			if (key.endsWith("| ")) {
				key = key.substring(0, key.length() - 1) + "0";
			}
			key += "|"+(imm.getDataFineVal() == null || imm.getDataFineVal().equalsIgnoreCase("ATTUALE") ? "31/12/9999" : imm.getDataFineVal()) +
			"|"+imm.getDataInizioVal();
		}
		imm.setChiave(key);
	}
	
/*	private void setPrincGraffati(Connection conn, Immobile imm, String key) throws Exception {		
		String[] keyFields = key.split("\\|",-1);
		String codNazionale = keyFields[0];
		String foglio = StringUtils.padding(keyFields[1], 4, '0', true);
		String particella = StringUtils.padding(keyFields[2], 5, '0', true);
		String unimm = keyFields[4].equals(" ")? keyFields[4] : StringUtils.padding(keyFields[4], 4, '0', true);
		
		String sql = "SELECT COUNT(1) AS CONTA FROM (" + SQL_UIU_PRINC_GRAFFATE + ")";
		PreparedStatement ps = conn.prepareStatement(sql);
		int index = 0;
		ps.setString(++index, codNazionale);
		ps.setString(++index, foglio);
		ps.setString(++index, particella);
		ps.setString(++index, unimm);
		ResultSet rs = ps.executeQuery();
		int conta = 0;
		while (rs.next()) {
			conta = rs.getObject("CONTA") == null ? 0 : rs.getInt("CONTA");
		}
		rs.close();
		ps.close();
		
		imm.setPrincGraffati(conta > 0);
	}*/
	
	private ArrayList<Immobile> getListaGraffati(Connection conn, Immobile imm, String key) throws Exception {		
		ArrayList<Immobile> listaGraffati = new ArrayList<Immobile>();
		
		String[] keyFields = key.split("\\|",-1);
		String codNazionale = keyFields[0];
		String foglio = StringUtils.padding(keyFields[1], 4, '0', true);
		String particella = StringUtils.padding(keyFields[2], 5, '0', true);
		String unimm = (keyFields[4].equals(" ")||keyFields[4].equals("0"))? " " : StringUtils.padding(keyFields[4], 4, '0', true);
		
		String sql = SQL_UIU_PRINC_GRAFFATE;
		int index = 0;
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(++index, codNazionale);
		ps.setString(++index, foglio);
		ps.setString(++index, particella);
		ps.setString(++index, unimm);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Immobile immobile = new Immobile();
			immobile.setSezione(rs.getObject("SEZ") == null || rs.getString("SEZ").trim().equals("") ? "-" : rs.getString("SEZ"));
			immobile.setFoglio(rs.getObject("FOGLIO") == null || rs.getString("FOGLIO").trim().equals("") ? "-" : rs.getString("FOGLIO"));
			immobile.setParticella(rs.getObject("MAPPALE") == null || rs.getString("MAPPALE").trim().equals("") ? "-" : rs.getString("MAPPALE"));
			immobile.setUnimm(rs.getObject("SUB") == null || rs.getString("SUB").trim().equals("") ? "-" : rs.getString("SUB"));
			listaGraffati.add(immobile);
		}
		rs.close();
		ps.close();
		
		return listaGraffati;
	}

	private String getSupTarsuABC(ArrayList dettaglioVani) {
		if (dettaglioVani == null || dettaglioVani.size() == 0) {
			return "0";
		}
		double sup = 0;
		Iterator it = dettaglioVani.iterator();
		while (it.hasNext()) {
			OggettiTARSU vani = (OggettiTARSU)it.next();
			String ambiente = vani.getAmbiente();
			if (ambiente.equalsIgnoreCase("A") || ambiente.equalsIgnoreCase("B") || ambiente.equalsIgnoreCase("C")) {
				int mySup = 0;
				String supVani = vani.getSupVani();
				try {
					if (supVani != null && !supVani.equals("")) {
						mySup = Integer.parseInt(supVani);
					}
				} catch (Exception e) {}
				sup += mySup;
			}
		}
		sup *= 0.8;
		return DF_CAT.format(sup);
	}
	
	public Vector<StatisticaCategorie> caricaListaCategorieParticella(String idCatalogo, HttpServletRequest request) throws Exception {
		
		Vector<StatisticaCategorie> vct = new Vector<StatisticaCategorie>();
		
		
		String sql = "select b.* from cat_categorie_cat a, cat_categorie_cat b where a.se_row_id=? " +
					 "and a.foglio=b.foglio and  a.particella =b.particella order by b.categoria ";
		
		Connection conn = null;

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			conn =  this.getConnectionDiogene();

			log.info("caricaListaCategorieParticella - SQL[" + sql + "]");
			log.info("caricaListaCategorieParticella - IdCatalogo[" + idCatalogo+"]");
			pst = conn.prepareStatement(sql);
			pst.setString(1, idCatalogo);
			rs = pst.executeQuery();
			
			String foglio = "";
			while(rs.next()){
				StatisticaCategorie stat = new StatisticaCategorie();
				stat.setFoglio(rs.getString("foglio"));
				stat.setParticella(rs.getString("particella"));
				stat.setOccorrenze(rs.getInt("occorrenze"));
				stat.setCategoria(rs.getString("categoria"));
				
				vct.add(stat);
			}
				
		}catch (Exception e)
			{
				log.error(e.getMessage(),e);
				throw e;
			}
			finally
			{
				if (rs!=null)
					rs.close();
				
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		
		return vct;
	}

	private void setDateValiditaImmobile(Connection conn, Immobile imm) throws Exception{
		
		Date[] minmax = {null,null};
		
		if(imm.getGraffato()!=null && "Y".equals(imm.getGraffato())){
			
	/*		
	 * 		String unimm = (imm.getUnimm().equals(" ")||imm.getUnimm().equals("0")? " "): StringUtils.padding(imm.getUnimm(), 4, '0', true);
			
			String sql = 
					"select min(TO_DATE(NVL(DATA_EFFIC_INI,'00010101'),'yyyyMMdd')) minData , max(TO_DATE(NVL(DATA_EFFIC_FIN,'99991231'),'yyyyMMdd')) maxData " +
					"from load_cat_uiu lcu, load_cat_uiu_id lid, SITICOMU C " +
					"where lid.id_imm = lcu.id_imm and C.CODI_FISC_LUNA=LID.CODI_FISC_LUNA and C.SEZIONE_CENSUARIA=LID.SEZ AND " +
					"C.COD_NAZIONALE = ? and foglio = LPAD(?,4,'0') and mappale = LPAD(? ,5,'0') and sub = ? ";
			
			int index = 0;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(++index, imm.getComune());
			ps.setString(++index, imm.getFoglio());
			ps.setString(++index, imm.getParticella());
			ps.setString(++index, unimm);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				minmax[0] = rs.getDate(1);
				minmax[1] = rs.getDate(2);
			}
		*/
			
			imm.setDataInizioVal(minmax[0]!=null ? SDF.format(minmax[0]) : null);
			imm.setDataFineVal(minmax[1]!=null ? SDF.format(minmax[1]) : null);
			
		}else{
			
			EnvUtente eu = this.getEnvUtente();
			String enteId = eu.getEnte();
			String userId = eu.getUtente().getUsername();
			String sessionId = eu.getUtente().getSessionId();
			
			CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
			RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
			rc.setEnteId(enteId);
			rc.setUserId(userId);
			rc.setSessionId(sessionId);
			rc.setCodEnte(imm.getComune());
			rc.setFoglio(imm.getFoglio());
			rc.setParticella(imm.getNumero());
			rc.setSub(imm.getSubalterno());
			rc.setUnimm(imm.getUnimm());
			
			minmax = catastoService.getMinMaxDateValUiu(rc);
			
			imm.setDataInizioVal(minmax[0]!=null ? SDF.format(minmax[0]) : this.DATA_MIN);
			imm.setDataFineVal(minmax[1]!=null ? SDF.format(minmax[1]) : this.DATA_MIN);
			
		}
		
		
		this.setStatoImmobile(imm);
		
	}
	
}

