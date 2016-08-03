package it.webred.ct.data.access.basic.catasto.dao;

import it.webred.ct.data.access.basic.catasto.dto.*;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Sitideco;

public class CatastoQueryBuilder{

	private CatastoSearchCriteria criteria;
	
	private String cod_nazionale;
	private Boolean nonANorma;
	private String foglio;
	private String particella;
	private String unimm;
	private String codCategoria;
	
	//Ricerca per indirizzo
	private String via;
	private String civico;
	
	//Ricerca per soggetto
	private String soggetto;
	
	private final String SQL_FIELDS_TO_SHOW = "" +
			"SITIUIU.PKID_UIU, " +
			"sitiuiu.COD_NAZIONALE AS FK_COMUNE , " +
			"sitiuiu.FOGLIO AS FOGLIO, " +
			"CAST(sitiuiu.PARTICELLA as varchar2(5)) AS PARTICELLA, "+
			"sitiuiu.SUB AS SUBALTERNO, " +
			"sitiuiu.UNIMM AS UNIMM, " +
			"sitiuiu.DATA_INIZIO_VAL as DATA_INIZIO_VAL, " +
			"sitiuiu.DATA_FINE_VAL as DATA_FINE_VAL, " +
			"sitiuiu.CATEGORIA  " ;
	
	/*private final  String SQL_SELECT_LISTA_UIU_BY_TITOLARITA = "select * " +
			"from ( (@SQL_LISTA_UIU_TITOLARITA_PROPRIETARI@) UNION (@SQL_LISTA_UIU_TITOLARITA_ALTRO@))";
	
	private final  String SQL_LISTA_UIU_TITOLARITA_PROPRIETARI = 
			"select distinct " + this.SQL_FIELDS_TO_SHOW +
			"from sitiuiu, cons_csui_tab, siticomu " +
			"where sitiuiu.COD_NAZIONALE = cons_csui_tab.COD_NAZIONALE " +
			"and sitiuiu.FOGLIO = cons_csui_tab.FOGLIO " +
			"and sitiuiu.PARTICELLA = cons_csui_tab.PARTICELLA " +
			"and sitiuiu.SUB = cons_csui_tab.SUB " +
			"and sitiuiu.UNIMM = cons_csui_tab.UNIMM " +
			"and cons_csui_tab.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
			"and cons_csui_tab.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
			"and siticomu.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
			"and cons_csui_tab.PK_CUAA = '@PK_CUAA_SOGGETTO@' " ;
	
	private final  String SQL_LISTA_UIU_TITOLARITA_ALTRO = 
			"select distinct " + this.SQL_FIELDS_TO_SHOW +
			"from sitiuiu, cons_urui_tab, siticomu  " +
			"where sitiuiu.COD_NAZIONALE = cons_urui_tab.COD_NAZIONALE " +
			"and sitiuiu.FOGLIO = cons_urui_tab.FOGLIO " +
			"and sitiuiu.PARTICELLA = cons_urui_tab.PARTICELLA " +
			"and sitiuiu.SUB = cons_urui_tab.SUB " +
			"and sitiuiu.UNIMM = cons_urui_tab.UNIMM " +
			"and cons_urui_tab.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
			"and  cons_urui_tab.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
			"and siticomu.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
			"and cons_urui_tab.PK_CUAA = '@PK_CUAA_SOGGETTO@' ";*/
	
	
	
	private final  String SQL_SELECT_LISTA_UIU_BY_TITOLARITA = 
		"SELECT DISTINCT " + this.SQL_FIELDS_TO_SHOW +
		"FROM sitiuiu, siticonduz_imm_all, siticomu  " +
		"WHERE siticonduz_imm_all.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
		"AND siticonduz_imm_all.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
		"AND siticonduz_imm_all.COD_NAZIONALE = siticomu.COD_NAZIONALE " +
		"AND sitiuiu.COD_NAZIONALE = SITICOMU.COD_NAZIONALE " +
		"AND sitiuiu.FOGLIO = siticonduz_imm_all.FOGLIO " +
		"AND sitiuiu.PARTICELLA = siticonduz_imm_all.PARTICELLA " +
		"AND sitiuiu.SUB = siticonduz_imm_all.SUB " +
		"AND sitiuiu.UNIMM = siticonduz_imm_all.UNIMM "+ 
		"AND siticonduz_imm_all.PK_CUAA = '@PK_CUAA_SOGGETTO@' ";
	
	
	private final String SQL_LISTA_UIU_BY_FPS = 
		"SELECT DISTINCT " + this.SQL_FIELDS_TO_SHOW +
		"from sitiuiu, sitideco, siticomu @SQL_REPTARSU_TAB " + 
		"where sitiuiu.CATEGORIA = sitideco.CODE " +
		"AND sitideco.TABLENAME='SITIUIU' " +
		"AND sitideco.FIELDNAME = 'CATEGORIA' " +
		"AND sitiuiu.cod_nazionale = SITICOMU.cod_nazionale ";
	
	private final  String SQL_LISTA_UIU_BY_INDIRIZZO = 
		"select distinct " + this.SQL_FIELDS_TO_SHOW +
		"from sitiuiu, sitideco, siticomu, siticivi_uiu " +
		"where sitiuiu.CATEGORIA = sitideco.CODE " +
		"and sitideco.TABLENAME='SITIUIU' " +
		"and sitideco.FIELDNAME = 'CATEGORIA' " +
		"AND sitiuiu.cod_nazionale = SITICOMU.cod_nazionale " +
		"AND siticivi_uiu.PKID_UIU = sitiuiu.PKID_UIU " +
		"and siticivi_uiu.DATA_FINE_VAL = TO_DATE('99991231', 'yyyymmdd') ";

	private final  String SQL_LISTA_PARTICELLE_BY_INDIRIZZO = 
		"select distinct siticomu.id_sezc, FOGLIO, cast(PARTICELLA as varchar2(5)) " +
		"from sitiuiu, siticomu, siticivi_uiu " +
		"where sitiuiu.cod_nazionale = SITICOMU.cod_nazionale " +
		"AND siticivi_uiu.PKID_UIU = sitiuiu.PKID_UIU " +
		"and siticivi_uiu.DATA_FINE_VAL = TO_DATE('99991231', 'yyyymmdd') ";
	
	private final  String SQL_SELECT_LISTA = 
		"SELECT * FROM ( " +
		" @SQL_SELECT_BASE_LISTA@ " +
		"ORDER BY foglio, particella, unimm)";


	private final  String SQL_SELECT_COUNT_LISTA = "SELECT count(*) as conteggio FROM ( @SQL_SELECT_BASE_LISTA@ )";
	
	
	private String SQL_LISTA_INDIRIZZI_BY_UNIMM = 
		"select " +
		"sitidstr.PREFISSO||' '||sitidstr.NOME AS INDIRIZZO, " +
		"siticivi.CIVICO " +
		"from siticivi_uiu,siticivi,sitidstr,siticomu " +
		"where siticivi_uiu.PKID_UIU = ? " +
		"and (SITICOMU.CODI_FISC_LUNA = ? OR siticomu.COD_NAZIONALE = ? )" +
		"and siticomu.cod_nazionale = siticivi.cod_nazionale " +
		"and siticivi_uiu.PKID_CIVI = siticivi.PKID_CIVI " +
		"and siticivi.PKID_STRA = sitidstr.PKID_STRA " +
		"and siticivi_uiu.DATA_FINE_VAL = TO_DATE('99991231', 'yyyymmdd') " +
		"and siticivi.DATA_FINE_VAL  = TO_DATE('99991231', 'yyyymmdd')  " +
		"and sitidstr.DATA_FINE_VAL  = TO_DATE('99991231', 'yyyymmdd') " +
		"order by INDIRIZZO, CIVICO ";
	
	private String SQL_LISTA_INDIRIZZI_BY_FP = 
		"SELECT DISTINCT " +
		"SITIDSTR.PREFISSO, SITIDSTR.NOME AS VIA, SITICIVI.CIVICO " +
		"FROM SITIUIU, SITICOMU, SITICIVI_UIU, SITIDSTR , SITICIVI " +
		"WHERE SITIUIU.COD_NAZIONALE = SITICOMU.COD_NAZIONALE " +
		"AND   SITICIVI_UIU.PKID_UIU = SITIUIU.PKID_UIU " +
		"AND   SITICIVI_UIU.PKID_CIVI = SITICIVI.PKID_CIVI " +
		"AND   SITICIVI.PKID_STRA = SITIDSTR.PKID_STRA " +
		"AND   SITICOMU.CODI_FISC_LUNA = ? " +
		"AND   (SITICOMU.ID_SEZC IS NULL  OR SITICOMU.ID_SEZC =?) " +
		"AND   SITIUIU.FOGLIO =  ? " +
		"AND   SITIUIU.PARTICELLA= LPAD(? ,5,'0') " +
		"AND   SITICIVI_UIU.DATA_INIZIO_VAL <= ? AND SITICIVI_UIU.DATA_FINE_VAL >= ?  ";
	
	/*private String SQL_LISTA_SOGGETTI_BY_UNIMM = "select distinct " +
		"NVL(FLAG_PERS_FISICA,'') AS FLAG, " +
		"TITOLO, " +
		"NVL(RAGI_SOCI,' ') AS COG_DENOM, " +
		"NVL(NOME,' ') AS NOME, " +
		"NVL(CF,'-') AS CF, " +
		"NVL(PIVA,'-') AS PIVA, " +
		"DATA_INIZIO_POS, "+
		"DATA_FINE_POS, " +
		"QUOTA, " +
		"DATA_FINE_VAL " +
		"FROM (" +
			"select distinct " +
			"CONS_SOGG_TAB.FLAG_PERS_FISICA, " +
			"'Proprietario' AS TITOLO, " +
			"CONS_SOGG_TAB.RAGI_SOCI," +
			"CONS_SOGG_TAB.NOME, " +
			"CONS_SOGG_TAB.CODI_FISC AS CF, " +
			"CONS_SOGG_TAB.CODI_PIVA AS PIVA, " +
			"cons_csui_tab.DATA_INIZIO as DATA_INIZIO_POS, " +
			"cons_csui_tab.DATA_FINE as DATA_FINE_POS, " +
			"cons_csui_tab.PERC_POSS AS QUOTA, " +
			"sitiuiu.DATA_FINE_VAL " +
			"from sitiuiu, cons_csui_tab, siticomu, CONS_SOGG_TAB " +
			"where sitiuiu.COD_NAZIONALE = cons_csui_tab.COD_NAZIONALE " +
			"and CONS_SOGG_TAB.DATA_FINE =  TO_DATE('99991231', 'yyyymmdd') " +
			"and sitiuiu.FOGLIO = cons_csui_tab.FOGLIO " +
			"and sitiuiu.PARTICELLA = cons_csui_tab.PARTICELLA " +
			"and sitiuiu.SUB = cons_csui_tab.SUB " +
			"and sitiuiu.UNIMM = cons_csui_tab.UNIMM " +
			"and cons_csui_tab.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
			"and cons_csui_tab.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
			"and siticomu.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
			"and cons_csui_tab.PK_CUAA = cons_sogg_tab.PK_CUAA " +
			"and SITIUIU.PKID_UIU = ? " +
			"AND (SITICOMU.CODI_FISC_LUNA = ? OR siticomu.COD_NAZIONALE = ? )" +
			"UNION " +
			"select distinct " +
			"CONS_SOGG_TAB.FLAG_PERS_FISICA, " +
			"'Altro' AS TITOLO, " +
			"CONS_SOGG_TAB.RAGI_SOCI," +
			"CONS_SOGG_TAB.NOME, " +
			"CONS_SOGG_TAB.CODI_FISC AS CF, " +
			"CONS_SOGG_TAB.CODI_PIVA AS PIVA, " +
			"cons_urui_tab.DATA_INIZIO as DATA_INIZIO_POS, " +
			"cons_urui_tab.DATA_FINE AS DATA_FINE_POS, " +
			"cons_urui_tab.PERC_POSS AS QUOTA, " +
			"sitiuiu.DATA_FINE_VAL " +
			"from sitiuiu, cons_urui_tab, siticomu , CONS_SOGG_TAB " +
			"where sitiuiu.COD_NAZIONALE = cons_urui_tab.COD_NAZIONALE " +
			"and CONS_SOGG_TAB.DATA_FINE =  TO_DATE('99991231', 'yyyymmdd') " +
			"and sitiuiu.FOGLIO = cons_urui_tab.FOGLIO " +
			"and sitiuiu.PARTICELLA = cons_urui_tab.PARTICELLA " +
			"and sitiuiu.SUB = cons_urui_tab.SUB " +
			"and sitiuiu.UNIMM = cons_urui_tab.UNIMM " +
			"and cons_urui_tab.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
			"and  cons_urui_tab.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
			"and siticomu.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
			"and cons_urui_tab.PK_CUAA = cons_sogg_tab.PK_CUAA " +
			"and SITIUIU.PKID_UIU = ? " +
			"AND (SITICOMU.CODI_FISC_LUNA = ? OR siticomu.COD_NAZIONALE = ? ))" +
		"ORDER BY data_fine_pos DESC,cog_denom, nome";*/
	
	private String SQL_LISTA_SOGGETTI_BY_UNIMM = 
		"SELECT DISTINCT " +
			"NVL(FLAG_PERS_FISICA,'') AS FLAG, " +
			"TITOLO, " +
			"NVL(RAGI_SOCI,' ') AS COG_DENOM, " +
			"NVL(NOME,' ') AS NOME, " +
			"NVL(CF,'-') AS CF, " +
			"NVL(PIVA,'-') AS PIVA, " +
			"DATA_INIZIO_POS, "+
			"DATA_FINE_POS, " +
			"QUOTA, " +
			"DATA_FINE_VAL " +
			"FROM (" +
				"SELECT DISTINCT " +
				"cons_sogg_tab.FLAG_PERS_FISICA, " +
				"cons_deco_tab.DESCRIPTION AS TITOLO, " +
				"cons_sogg_tab.RAGI_SOCI," +
				"cons_sogg_tab.NOME, " +
				"cons_sogg_tab.CODI_FISC AS CF, " +
				"cons_sogg_tab.CODI_PIVA AS PIVA, " +
				"siticonduz_imm_all.DATA_INIZIO as DATA_INIZIO_POS, " +
				"siticonduz_imm_all.DATA_FINE as DATA_FINE_POS, " +
				"siticonduz_imm_all.PERC_POSS AS QUOTA, " +
				"sitiuiu.DATA_FINE_VAL " +
				"FROM sitiuiu, siticonduz_imm_all, siticomu, cons_sogg_tab, cons_deco_tab " +
				"WHERE sitiuiu.COD_NAZIONALE = siticonduz_imm_all.COD_NAZIONALE " +
				"AND CONS_SOGG_TAB.DATA_FINE =  TO_DATE('99991231', 'yyyymmdd') " +
				"AND sitiuiu.FOGLIO = siticonduz_imm_all.FOGLIO " +
				"AND sitiuiu.PARTICELLA = siticonduz_imm_all.PARTICELLA " +
				"AND sitiuiu.SUB = siticonduz_imm_all.SUB " +
				"AND sitiuiu.UNIMM = siticonduz_imm_all.UNIMM " +
				"AND siticonduz_imm_all.DATA_INIZIO < sitiuiu.DATA_FINE_VAL " +
				"AND siticonduz_imm_all.DATA_FINE > sitiuiu.DATA_INIZIO_VAL " +
				"AND siticomu.COD_NAZIONALE = sitiuiu.COD_NAZIONALE " +
				"AND siticonduz_imm_all.PK_CUAA = cons_sogg_tab.PK_CUAA " +
				"AND siticonduz_imm_all.tipo_titolo = cons_deco_tab.code(+) " +
				"AND cons_deco_tab.tablename = 'CONS_ATTI_TAB' " +
				"AND cons_deco_tab.fieldname = 'TIPO_TITOLO' " +
				"and SITIUIU.PKID_UIU = ? " +
				"AND (SITICOMU.CODI_FISC_LUNA = ? OR siticomu.COD_NAZIONALE = ? ))" +
	"ORDER BY data_fine_pos DESC,cog_denom, nome";
	
	private String SQL_LOCALIZZAZIONE_CATASTALE_IMMOBILE = 
		"SELECT DISTINCT  " +
		"t.descr || ' ' || ind.ind  indirizzo, " +
		" ind.civ1 AS civico  "+
	    " FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t  "+
		" WHERE (c.cod_nazionale = ? OR c.codi_fisc_luna = ?) "+
		" AND ID.codi_fisc_luna = c.codi_fisc_luna  "+
		" AND ID.sez = c.sezione_censuaria  "+
		" AND ID.foglio = LPAD (?, 4, '0')  "+
		" AND ID.mappale = LPAD (?, 5, '0')  "+
		" AND ID.SUB =  LPAD (?, 4, '0') "+
		" AND ind.codi_fisc_luna = c.codi_fisc_luna "+ 
		" AND ind.sezione = ID.sezione  "+
		" AND ind.id_imm = ID.id_imm  "+
		" AND ind.progressivo = ID.progressivo  "+
		" AND t.pk_id = ind.toponimo ";
	
	private String SQL_PLANIMETRIE_C340 = "SELECT nome_file_planimetrico, " +
		"decode(nvl(subalterno, ' '), ' ', '9999', subalterno) as subalterno " +
		"FROM sit_cat_planimetrie_c340 " +
		"WHERE foglio = lpad(?, 4, '0') " +
		"AND numero = lpad(?, 5, '0') " +
		"AND (subalterno = lpad(?, 4, '0') OR decode(nvl(subalterno, ' '), ' ', '9999', subalterno) = '9999') " +
		"AND codice_amministrativo= ? "+
		"ORDER BY subalterno";
	
	public String getSQL_PLANIMETRIE_C340() {
		return SQL_PLANIMETRIE_C340;
	}
	private String SQL_LISTA_SOGGETTI_TERRENO = 
		"SELECT  PK_CUAA, CC.DATA_INIZIO, CC.DATA_FINE, CC.PERC_POSS, CC.TIPO_TITOLO " +
		"FROM SITITRKC T ,CONS_CONS_TAB CC, SITICOMU C " +
         "WHERE  T.COD_NAZIONALE = CC.COD_NAZIONALE " +
                    "AND C.COD_NAZIONALE = T.COD_NAZIONALE " + 
                    "AND C.CODI_FISC_LUNA=? " +
                    "AND (C.ID_SEZC IS NULL OR C.ID_SEZC=?) " + 
                    "AND T.FOGLIO = CC.FOGLIO " +
                    "AND T.PARTICELLA = CC.PARTICELLA "+  
                    "AND T.SUB = CC.SUB " +
                    "AND CC.DATA_INIZIO < T.DATA_FINE " + 
                    "AND  CC.DATA_FINE > T.DATA_AGGI " +
                    "AND T.FOGLIO = ? " +
                    "AND LPAD(T.PARTICELLA, 5,'0') = LPAD(?, 5,'0') " +
                    "AND T.DATA_FINE = TO_DATE('31/12/9999', 'dd/mm/yyyy') "+
                    "@CC.DATA_FINE_COND " + //CONDIZ. DA SOSTITUIRE PER IMPOSTARE SOLO STORICI OPPURE SOLO ATTUALI
                  //"AND CC.DATA_FINE >=SYSDATE " +//--> TITOLARI ATTUALI 
                  //"AND CC.DATA_FINE <SYSDATE " +//--> TITOLARI STORICI    
                  "  ORDER BY CC.DATA_FINE DESC,CC.DATA_INIZIO ";

	public CatastoQueryBuilder(){}
	
	public CatastoQueryBuilder(CatastoSearchCriteria criteria) {
		this.criteria = criteria;
		
		cod_nazionale = criteria.getCodNazionale();
		
		nonANorma = criteria.getNonANorma();
		
		foglio = criteria.getFoglio();
		particella = criteria.getParticella();
		unimm = criteria.getUnimm();
		
		codCategoria = criteria.getCodCategoria();
		
		via = criteria.getIdVia();
		civico = criteria.getIdCivico();
		
		soggetto = criteria.getIdSoggetto();
		
	}
	
	protected String createQueryByImmobile(){
		
		String sqlUiu = this.SQL_LISTA_UIU_BY_FPS;
		sqlUiu += " AND ("+ addCondition("SITICOMU.CODI_FISC_LUNA","=",cod_nazionale)+ " OR "+ addCondition("siticomu.COD_NAZIONALE","=",cod_nazionale)+")";
		sqlUiu += this.getSQL_ImmobileCriteria();
		
		//Seleziona solo gli immobili non a norma
		if(nonANorma!=null && nonANorma){
			
			sqlUiu = sqlUiu.replace("@SQL_REPTARSU_TAB", ", sit_rep_tarsu ");
			//int flag = (nonANorma == Boolean.FALSE ? 1 : 0);
			//AND sitiuiu.pkid_uiu = sit_rep_tarsu.pkid_uiu " +
			sqlUiu += " AND sit_rep_tarsu.foglio = sitiuiu.foglio " +
					" AND sit_rep_tarsu.particella = sitiuiu.particella " +
					" AND sit_rep_tarsu.unimm = sitiuiu.unimm " +
					" AND sit_rep_tarsu.data_inizio_val = sitiuiu.data_inizio_val " +
					" AND sit_rep_tarsu.f_c340 = 0 ";
			
		}else sqlUiu = sqlUiu.replace("@SQL_REPTARSU_TAB", "");
			
		
		System.out.println("UIU-SQL [" + sqlUiu + "]");
	
		return sqlUiu;
	}
	
	protected String createQueryByIndirizzo(){
		
		String sqlUiu = this.SQL_LISTA_UIU_BY_INDIRIZZO;
		sqlUiu += " AND ("+ addCondition("SITICOMU.CODI_FISC_LUNA","=",cod_nazionale)+ " OR "+ addCondition("siticomu.COD_NAZIONALE","=",cod_nazionale)+")";
		sqlUiu += this.getSQL_IndirizzoCriteria();
		
		return sqlUiu;
	}
	protected String createQueryParticelleByIndirizzo(){
		
		String sqlUiu = this.SQL_LISTA_PARTICELLE_BY_INDIRIZZO;
		sqlUiu += " AND ("+ addCondition("SITICOMU.CODI_FISC_LUNA","=",cod_nazionale)+ " OR "+ addCondition("siticomu.COD_NAZIONALE","=",cod_nazionale)+")";
		sqlUiu += this.getSQL_IndirizzoCriteria();
		
		return sqlUiu;
	}
	
	protected String createQueryBySoggetto(){ //RIVEDERE
		String pk_cuaa_soggetto = soggetto;
		
		/*String sqlIdSoggetti = this.SQL_SELECT_ID_SOGGETTI_BASE;
		sqlIdSoggetti += " AND ("+ addCondition("SITICOMU.CODI_FISC_LUNA","=",cod_nazionale)+ " OR "+ addCondition("siticomu.COD_NAZIONALE","=",cod_nazionale)+")";
		sqlIdSoggetti += this.getSQL_SFCriteria() + this.getSQL_SGCriteria();*/
		
		/*String sqlUiuProprietari = this.SQL_LISTA_UIU_TITOLARITA_PROPRIETARI;
		sqlUiuProprietari = sqlUiuProprietari.replaceAll("@PK_CUAA_SOGGETTO@",pk_cuaa_soggetto);
		sqlUiuProprietari += " AND ("+ addCondition("SITICOMU.CODI_FISC_LUNA","=",cod_nazionale)+ " OR "+ addCondition("siticomu.COD_NAZIONALE","=",cod_nazionale)+")";
		
		String sqlUiuAltro = this.SQL_LISTA_UIU_TITOLARITA_ALTRO;
		sqlUiuAltro = sqlUiuAltro.replaceAll("@PK_CUAA_SOGGETTO@",pk_cuaa_soggetto);
		sqlUiuAltro += " AND ("+ addCondition("SITICOMU.CODI_FISC_LUNA","=",cod_nazionale)+ " OR "+ addCondition("siticomu.COD_NAZIONALE","=",cod_nazionale)+")";
		
		String sqlUiu = this.SQL_SELECT_LISTA_UIU_BY_TITOLARITA;
		sqlUiu = sqlUiu.replaceAll("@SQL_LISTA_UIU_TITOLARITA_PROPRIETARI@", sqlUiuProprietari);
		sqlUiu = sqlUiu.replaceAll("@SQL_LISTA_UIU_TITOLARITA_ALTRO@", sqlUiuAltro);*/
		
		String sqlUiu = this.SQL_SELECT_LISTA_UIU_BY_TITOLARITA;
		sqlUiu = sqlUiu.replaceAll("@PK_CUAA_SOGGETTO@",pk_cuaa_soggetto);
		sqlUiu += " AND ("+ addCondition("SITICOMU.CODI_FISC_LUNA","=",cod_nazionale)+ " OR "+ addCondition("siticomu.COD_NAZIONALE","=",cod_nazionale)+")";
		
		
		return sqlUiu;
	}
	
	
	public String createQuery(boolean isCount) {
		
		String sql = null;
		String sqlUiu = null;
		
		if(isSearchByImmobile())
			sqlUiu= this.createQueryByImmobile();
		
		if(isSearchByIndirizzo())
			sqlUiu= this.createQueryByIndirizzo();
		
		if(isSearchBySoggetto())
			sqlUiu= this.createQueryBySoggetto();
		
		//Elenca tutte le unità immobiliari
		//if(sqlUiu.isEmpty())
		//	sqlUiu= this.createQueryByFPS();
		
		if(sqlUiu != null){
			if (isCount){
				sql = this.SQL_SELECT_COUNT_LISTA;
				sql = sql.replace("@SQL_SELECT_BASE_LISTA@", sqlUiu);
			}else{
				sql = this.SQL_SELECT_LISTA;
				sql = sql.replace("@SQL_SELECT_BASE_LISTA@", sqlUiu);
			}
		}
		
		return sql;
	}
	//ricerca le particelle e non le unità immobiliari
	public String createQueryParticelle(boolean isCount) {
		
		String sql = null;
		String sqlPart = null;
		
		if(isSearchByIndirizzo())
			sqlPart= this.createQueryParticelleByIndirizzo();
		
		return sqlPart;
	}
	
	private String getSQL_ImmobileCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (foglio == null  || foglio.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("sitiuiu.foglio","=",foglio));
		
		sqlCriteria = (particella == null || particella.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + "sitiuiu.particella = LPAD ('" + particella + "', 5, '0')");
		
		sqlCriteria = (unimm == null || unimm.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("sitiuiu.unimm","=",unimm));

		sqlCriteria = (codCategoria == null || codCategoria.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("sitideco.code","=",codCategoria));
		
		return sqlCriteria;
	}
	
	private String getSQL_IndirizzoCriteria() {
		
		String sqlCriteria = "";
		boolean isCivico = (civico != null && !civico.trim().equals("")); 
		
		if(isCivico)
		  sqlCriteria = addOperator(sqlCriteria) + addCondition("SITICIVI_UIU.PKID_CIVI", "=",civico);
		  
		else{
			  sqlCriteria = "and SITICIVI_UIU.PKID_CIVI IN (" +
			  		"select " +
			  		"siticivi.PKID_CIVI as PK_CIVICO " +
			  		"from siticivi " +
			  		"where siticivi.DATA_FINE_VAL (+) = TO_DATE('99991231', 'yyyymmdd') " +
			  		"and SITICIVI.COD_NAZIONALE = '"+cod_nazionale+"' " ;
			  		
			  sqlCriteria = (via == null  || via.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("SITICIVI.PKID_STRA", "=", via));
		      sqlCriteria += ") ";
		  }
		return sqlCriteria;
	}
	
	/*private String getSQL_SFCriteria() {
		
		//Ricerca per soggetto fisico titolare
		String sqlCriteria = "";
		
		sqlCriteria = (soggettof == null || soggettof.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("cons_sogg_tab.PK_CUAA","=",soggettof));
		
		if(!sqlCriteria.isEmpty()){
			sqlCriteria = (cfSoggetto == null || cfSoggetto.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("cons_sogg_tab.CODI_FISC","=",cfSoggetto));
			sqlCriteria = "(SELECT CONS_SOGG_TAB.PK_CUAA FROM CONS_SOGG_TAB WHERE DATA_FINE = TO_DATE('99991231', 'yyyymmdd') "+ sqlCriteria +")";
		}
		//Aggiunge la condizione sulla persona fisica
		if(!sqlCriteria.isEmpty())
			sqlCriteria += addOperator(sqlCriteria)+ addCondition("cons_sogg_tab.FLAG_PERS_FISICA","=","P");
		
		return sqlCriteria;
	}
	
	private String getSQL_SGCriteria() {
		//Ricerca per soggetto giuridico titolare
		String sqlCriteria = "";
		
		sqlCriteria = (soggettog == null || soggettog.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("ccons_sogg_tab.PK_CUAA","=",soggettog));
		
		if(sqlCriteria.isEmpty()){
		sqlCriteria = (pivaSoggetto == null || pivaSoggetto.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("cons_sogg_tab.CODI_PIVA","=",pivaSoggetto));
		sqlCriteria = "(SELECT CONS_SOGG_TAB.PK_CUAA FROM CONS_SOGG_TAB WHERE DATA_FINE = TO_DATE('99991231', 'yyyymmdd') "+ sqlCriteria +")";
		}
		
		//Aggiunge la condizione sulla persona giuridica
		if(!sqlCriteria.isEmpty())
			sqlCriteria += addOperator(sqlCriteria)+ addCondition("cons_sogg_tab.FLAG_PERS_FISICA","=","G");
		
		return sqlCriteria;
	}*/
	
	protected String addCondition(String field, String operator, String param) {
    	
		String criteria = "";
		operator = operator.trim();
		param = param.trim();
		
		if (operator.equals("="))
    	    criteria += " ("+ field +") "+ operator +" '" + param.toUpperCase() + "' " ;    	
    	else if (operator.equalsIgnoreCase("LIKE"))  	    
    		criteria += " ("+ field +") "+ operator +" '%' || '"+ param.toUpperCase()+"' || '%' " ;   
		
		return criteria;
    }
	
	protected String addOperator(String criteria) {
    	if (criteria == null)
    	    return criteria;    	
    	else    	    
    	    return criteria += " AND ";
    }
	
	protected boolean isSearchByImmobile(){
		boolean result = false;
		
		boolean isFoglio = (foglio != null && !foglio.equals(""));    // Parametro impostato
		boolean isParticella = (particella != null && !particella.equals(""));    // Parametro impostato
		boolean isUnimm = (unimm != null && !unimm.equals(""));    // Parametro impostato
		boolean isNonANorma = (nonANorma != null && nonANorma);
		boolean isCategoria = (codCategoria!=null && !codCategoria.equals(""));
		
		result = isFoglio || isParticella || isUnimm || isNonANorma || isCategoria;
		
		return result;
	}
	
	protected boolean isSearchByIndirizzo(){
		boolean result = false;
		
		boolean isVia = (via != null && !via.equals(""));    // Parametro impostato
		boolean isCivico = (civico != null && !civico.equals(""));    // Parametro impostato
		
		result = isVia || isCivico;
		return result;
	}
	
	protected boolean isSearchBySoggetto(){
		boolean result = false;
		
		result = (soggetto!= null && !soggetto.equals(""));
		
		return result;
	}

	public String getSQL_LISTA_INDIRIZZI_BY_UNIMM() {
		return SQL_LISTA_INDIRIZZI_BY_UNIMM;
	}

	public String getSQL_LISTA_SOGGETTI_BY_UNIMM() {
		return SQL_LISTA_SOGGETTI_BY_UNIMM;
	}

	public String getSQL_LOCALIZZAZIONE_CATASTALE_IMMOBILE() {
		return SQL_LOCALIZZAZIONE_CATASTALE_IMMOBILE;
	}
    
	public String getSQL_LISTA_INDIRIZZI_BY_FP() {
		return SQL_LISTA_INDIRIZZI_BY_FP;
	}
	
	public String getSQL_LISTA_SOGGETTI_TERRENO(boolean attuali) {
		String sql=SQL_LISTA_SOGGETTI_TERRENO;
		if (attuali)
			sql= sql.replace("@CC.DATA_FINE_COND", "AND CC.DATA_FINE >=SYSDATE ");
		else	
			sql = sql.replace("@CC.DATA_FINE_COND", "AND CC.DATA_FINE <SYSDATE ");
		return sql;
	}
}
