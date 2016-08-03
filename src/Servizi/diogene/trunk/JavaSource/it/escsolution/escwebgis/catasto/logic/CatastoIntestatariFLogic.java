/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.logic;

import it.escsolution.escwebgis.catasto.bean.CatastoPerSoggetto;
import it.escsolution.escwebgis.catasto.bean.IntestatarioF;
import it.escsolution.escwebgis.catasto.bean.IntestatarioFFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoCatastoDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CatastoIntestatariFLogic extends EscLogic{
	private String appoggioDataSource;
	private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static final String VISURA_NAZIONALE = "VISURA_NAZIONALE";
	
	public CatastoIntestatariFLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DF.setDecimalFormatSymbols(dfs);
		DF.setMaximumFractionDigits(2);
	}
	
	private final static String SQL_SELECT_LISTA= "select * from (" +
			"select rownum as N, cognome, nome,CODICE_FISCALE,PK_CUAA,DATA_NASCITA,LUOGO,SESSO " +
			"from (select distinct " +
			"decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
			"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
			"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS LUOGO," +
			"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
			"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
			"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO " +
			"from cons_sogg_tab,siticomu " +
			"WHERE 1=? " +
			"and cons_sogg_tab.FLAG_PERS_FISICA ='P' " +
			//"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) ";

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio from (" +
			"select rownum as N, cognome, nome,CODICE_FISCALE,PK_CUAA,DATA_NASCITA,LUOGO,SESSO " +
			"from (select distinct " +
			"decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
			"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
			"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS LUOGO," +
			"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
			"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
			"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO " +
			"from cons_sogg_tab,siticomu " +
			"WHERE 1=? " +
			"and cons_sogg_tab.FLAG_PERS_FISICA ='P' " +
			//"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) ";

	private final static String SQL_SELECT_LISTA_FROM_SOGGETTO= "select * from (" +
	"select rownum as N, cognome, nome,CODICE_FISCALE,PK_CUAA,DATA_NASCITA,LUOGO,SESSO " +
	"from ( select ROWnum as N," +
	"decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
	"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
	"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
	"decode(siticomu.NOME,null,'-',siticomu.NOME) AS LUOGO," +
	"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO " +
	"from cons_sogg_tab,siticomu " +
	"WHERE cons_sogg_tab.pkid =? " +
	"and cons_sogg_tab.FLAG_PERS_FISICA ='P' " +
	//"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) ))";
	
	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from cons_sogg_tab, siticomu WHERE cons_sogg_tab.FLAG_PERS_FISICA='P' and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') AND SUBSTR (cons_sogg_tab.comu_nasc, 1, 3) = siticomu.istatp(+) AND SUBSTR (cons_sogg_tab.comu_nasc, 4, 3) = siticomu.istatc(+) and 1=? " ;

	private final static String SQL_SELECT_LISTA_INTESTATARIF_TERRENO = ""+
	"select decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
	"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
	"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
	"sititrkc.COD_NAZIONALE AS FK_COMUNE," +
	"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO," +
	"nvl(to_char(cons_cons_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl(to_char(cons_cons_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_cons_tab.PERC_POSS,null,'-',cons_cons_tab.PERC_POSS) AS QUOTA, " +
	"cons_deco_tab.description AS TITOLO " +
	"from cons_sogg_tab, cons_cons_tab, sititrkc, cons_deco_tab " +
	"WHERE sititrkc.TRKC_ID = ? " +
	"and cons_sogg_tab.PK_CUAA =  cons_cons_tab.PK_CUAA " +
	"and sititrkc.COD_NAZIONALE = cons_cons_tab.COD_NAZIONALE " +
	"and sititrkc.FOGLIO = cons_cons_tab.FOGLIO " +
	"and sititrkc.PARTICELLA = cons_cons_tab.PARTICELLA " +
	"and sititrkc.SUB = cons_cons_tab.SUB " +
	"and cons_sogg_tab.FLAG_PERS_FISICA= 'P' " +
	" AND cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
	//"and (cons_cons_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_cons_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " ;
	//"AND cons_cons_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_cons_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"AND( ( cons_cons_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_cons_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_cons_tab.data_inizio = TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_cons_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy')))" +
	"AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_cons_tab.tipo_documento ";

	private final static String SQL_SELECT_LISTA_ALTRIF_TERRENO = ""+
	"select decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
	"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
	"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
	"sititrkc.COD_NAZIONALE AS FK_COMUNE," +
	"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO," +
	"nvl(to_char(cons_ufre_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl(to_char(cons_ufre_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_ufre_tab.PERC_POSS,null,'-',cons_ufre_tab.PERC_POSS) AS QUOTA, " +
	"cons_deco_tab.description AS TITOLO " +
	"from cons_sogg_tab, cons_ufre_tab, sititrkc, cons_deco_tab " +
	"WHERE sititrkc.TRKC_ID = ? " +
	"and cons_sogg_tab.PK_CUAA =  cons_ufre_tab.PK_CUAA " +
	"and sititrkc.COD_NAZIONALE = cons_ufre_tab.COD_NAZIONALE " +
	"and sititrkc.FOGLIO = cons_ufre_tab.FOGLIO " +
	"and sititrkc.PARTICELLA = cons_ufre_tab.PARTICELLA " +
	"and sititrkc.SUB = cons_ufre_tab.SUB " +
	"and cons_sogg_tab.FLAG_PERS_FISICA= 'P' " +
	"AND cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
	//"and (cons_ufre_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_ufre_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " ;
	//"AND cons_ufre_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_ufre_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy') " ;
	"AND( ( cons_ufre_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_ufre_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_ufre_tab.data_inizio = TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_ufre_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))) " +
	"AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_ufre_tab.tipo_documento ";



	private final static String SQL_SELECT_LISTA_INTESTATARIF_FABBRICATO = "" +
	"select decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
	"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
	"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
	"decode(siticomu.COD_NAZIONALE,null,'-',siticomu.COD_NAZIONALE) AS FK_COMUNE," +
	"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE_NASCITA," +
	"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO," +
	"nvl(to_char(cons_csui_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl (to_char (cons_csui_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_csui_tab.PERC_POSS,null,'-',cons_csui_tab.PERC_POSS) AS QUOTA " +
	"from cons_csui_tab,cons_sogg_tab,siticomu " +
	"where cons_csui_tab.COD_NAZIONALE = ? " +
	"and cons_csui_tab.FOGLIO = ? " +
	"and cons_csui_tab.PARTICELLA = ? " +
	"and DECODE(cons_csui_tab.SUB,' ','-',NVL(cons_csui_tab.SUB,'*'),'-',cons_csui_tab.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
	"and cons_csui_tab.UNIMM = ? " +
	//"and (cons_csui_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_csui_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " +
	//"AND cons_csui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_csui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy') " +
	"AND( ( cons_csui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_csui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_csui_tab.data_inizio = TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_csui_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))) " +
	"and cons_csui_tab.PK_CUAA = cons_sogg_tab.PK_CUAA " +
	"and cons_sogg_tab.FLAG_PERS_FISICA='P' " +
	"AND cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) ";

	private final static String SQL_SELECT_LISTA_ALTRIF_FABBRICATO = "" +
	"select decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
	"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
	"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
	"decode(siticomu.COD_NAZIONALE,null,'-',siticomu.COD_NAZIONALE) AS FK_COMUNE," +
	"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE_NASCITA," +
	"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO," +
	"nvl(to_char(cons_urui_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl(to_char(cons_urui_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_urui_tab.PERC_POSS,null,'-',cons_urui_tab.PERC_POSS) AS QUOTA " +
	"from cons_urui_tab,cons_sogg_tab,siticomu " +
	"where cons_urui_tab.COD_NAZIONALE = ? " +
	"and cons_urui_tab.FOGLIO = ? " +
	"and cons_urui_tab.PARTICELLA = ? " +
	"and DECODE(cons_urui_tab.SUB,' ','-',NVL(cons_urui_tab.SUB,'*'),'-',cons_urui_tab.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
	"and cons_urui_tab.UNIMM = ? " +
	//"and (cons_urui_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_urui_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " +
	//"AND cons_urui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_urui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy') " +
	"AND( ( cons_urui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_urui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_urui_tab.data_inizio = TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_urui_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))) " +
	"and cons_urui_tab.PK_CUAA = cons_sogg_tab.PK_CUAA " +
	"and cons_sogg_tab.FLAG_PERS_FISICA='P' " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) ";

	private final static String SQL_SELECT_LISTA_INTESTATARIF_FABBRICATO_NUOVO = "SELECT DECODE (cons_sogg_tab.codi_fisc," +
	"               NULL, '-'," +
	"               cons_sogg_tab.codi_fisc" +
	"              ) AS codice_fiscale," +
	"       DECODE (cons_sogg_tab.ragi_soci," +
	"               NULL, '-'," +
	"               cons_sogg_tab.ragi_soci" +
	"              ) AS cognome," +
	"       NVL (TO_CHAR (cons_sogg_tab.data_nasc, 'dd/mm/yyyy')," +
	"            '-'" +
	"           ) AS data_nascita," +
	"       DECODE (siticomu.cod_nazionale," +
	"               NULL, '-'," +
	"               siticomu.cod_nazionale" +
	"              ) AS fk_comune," +
	"       DECODE (siticomu.nome, NULL, '-', siticomu.nome) AS comune_nascita," +
	"       DECODE (cons_sogg_tab.nome, NULL, '-', cons_sogg_tab.nome) AS nome," +
	"       DECODE (cons_sogg_tab.pk_cuaa," +
	"               NULL, '-'," +
	"               cons_sogg_tab.pk_cuaa" +
	"              ) AS pk_cuaa," +
	"       DECODE (cons_sogg_tab.sesso, NULL, '-', cons_sogg_tab.sesso) AS sesso," +
	"       NVL (TO_CHAR (siticonduz_imm_all.data_inizio, 'dd/mm/yyyy')," +
	"            '-'" +
	"           ) AS data_inizio," +
	"       NVL (TO_CHAR (siticonduz_imm_all.data_fine, 'dd/mm/yyyy')," +
	"            '-') AS data_fine," +
	"       siticonduz_imm_all.perc_poss AS QUOTA," +
	"       siticonduz_imm_all.quota_num AS QUOTA_NUM," +
	"       siticonduz_imm_all.quota_denom AS QUOTA_DENOM," +
	"       siticonduz_imm_all.affidabilita AS AFFIDABILITA," +
	"		NVL(cons_deco_tab.description, 'PROPRIETARIO') as DESC_TIPO_DOCUMENTO" +
	"  FROM siticonduz_imm_all, cons_sogg_tab, siticomu, cons_deco_tab" +
	" WHERE siticonduz_imm_all.cod_nazionale = ?" +
	"   AND siticonduz_imm_all.foglio = ?" +
	"   AND LPAD(siticonduz_imm_all.particella,5,'0') = ?" +
	//"   AND siticonduz_imm_all.sub = ?" +
	//"   AND siticonduz_imm_all.sub = ?" +
	"   AND siticonduz_imm_all.unimm = ?" +
	"   AND (   (    siticonduz_imm_all.data_inizio >=" +
	"                                          TO_DATE (nvl(?,'01/01/0001'), 'dd/mm/yyyy')" +
	"            AND siticonduz_imm_all.data_fine <=" +
	"                                          TO_DATE (?, 'dd/mm/yyyy')" +
	"           )" +
	"        OR (    siticonduz_imm_all.data_inizio >=" +
	"                                          TO_DATE ('01/01/0001', 'dd/mm/yyyy')" +
	"            AND siticonduz_imm_all.data_fine >=" +
	"                                          TO_DATE (nvl(?,'01/01/0001'), 'dd/mm/yyyy')" +
	"           )" +
	"       )" +
	"   AND siticonduz_imm_all.pk_cuaa = cons_sogg_tab.pk_cuaa" +
	"   AND cons_sogg_tab.flag_pers_fisica = 'P'" +
	"   AND cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY')" +
	"   AND SUBSTR (cons_sogg_tab.comu_nasc, 1, 3) = siticomu.istatp(+)" +
	"   AND SUBSTR (cons_sogg_tab.comu_nasc, 4, 3) = siticomu.istatc(+)" +
	"	AND siticonduz_imm_all.tipo_documento = cons_deco_tab.code (+)" +
	"	AND cons_deco_tab.tablename = 'CONS_ATTI_TAB'" + 
	"	AND cons_deco_tab.fieldname = 'TIPO_DOCUMENTO'";
	
	
	//vecchia query
	/*private final static String SQL_SELECT_LISTA_CATASTO_PER_SOGGETTO = "SELECT " +
	"       NVL (TO_CHAR(siticonduz_imm_all.foglio)," +
	"            '-'" +
	"       ) AS foglio," +
	"       NVL (siticonduz_imm_all.particella," +
	"            '-'" +
	"       ) AS particella," +
	"       NVL (LTRIM(RTRIM(siticonduz_imm_all.sub))," +
	"            '-'" +
	"       ) AS subalterno," +
	"       NVL (LTRIM(RTRIM(siticonduz_imm_all.unimm))," +
	"            '-'" +
	"       ) AS unimm," +
	"       NVL (TO_CHAR (siticonduz_imm_all.data_inizio, 'dd/mm/yyyy')," +
	"            '-'" +
	"           ) AS data_inizio," +
	"       NVL (TO_CHAR (siticonduz_imm_all.data_fine, 'dd/mm/yyyy')," +
	"            '-'" + 
	"			) AS data_fine," +
	"       NVL (TO_CHAR(siticonduz_imm_all.perc_poss)," +
	"            '-'" + 
	"			) AS quota," +
	"		sitiuiu.categoria," +
	"       NVL (sitiuiu.classe," +
	"            '-'" + 
	"			) AS classe," +
	"       NVL (TO_CHAR(sitiuiu.rendita)," +
	"            '-'" + 
	"			) AS rendita" +
	" FROM siticonduz_imm_all, cons_sogg_tab, siticomu, sitiuiu" +
	" WHERE cons_sogg_tab.codi_fisc = ?" +
	"	AND (siticonduz_imm_all.data_inizio IS NULL OR siticonduz_imm_all.data_inizio = TO_DATE('01/01/0001','DD/MM/YYYY') OR siticonduz_imm_all.data_inizio <= TO_DATE(?, 'dd/MM/yyyy')) " +
	"	AND (siticonduz_imm_all.data_fine IS NULL OR siticonduz_imm_all.data_fine = TO_DATE('31/12/9999','DD/MM/YYYY') OR siticonduz_imm_all.data_fine >= TO_DATE(?, 'dd/MM/yyyy')) " +
	"   AND siticonduz_imm_all.tipo_titolo = 1" +
	"   AND siticonduz_imm_all.pk_cuaa = cons_sogg_tab.pk_cuaa" +
	"   AND cons_sogg_tab.flag_pers_fisica = 'P'" +
	"   AND cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY')" +
	"   AND SUBSTR (cons_sogg_tab.comu_nasc, 1, 3) = siticomu.istatp(+)" +
	"   AND SUBSTR (cons_sogg_tab.comu_nasc, 4, 3) = siticomu.istatc(+)" +
	"	AND sitiuiu.data_fine_val = to_date('99991231','yyyymmdd') " +
	"	AND sitiuiu.cod_nazionale = siticonduz_imm_all.cod_nazionale (+) " +
	"	AND sitiuiu.data_fine_val = siticonduz_imm_all.data_fine (+) " +
	"	AND sitiuiu.foglio = siticonduz_imm_all.foglio (+) " +
	"	AND sitiuiu.particella = siticonduz_imm_all.particella (+) " +
	"	AND sitiuiu.sub = siticonduz_imm_all.sub (+) " +
	"	AND sitiuiu.unimm = siticonduz_imm_all.unimm (+) ";*/
	
	private final static String SQL_SELECT_LISTA_CATASTO_PER_SOGGETTO = "SELECT " +
								"NVL (siticomu.id_sezc, '-') AS sezione, " +
								"NVL (TO_CHAR (siticonduz_imm_all.foglio), '-') AS foglio, " +
								"NVL (siticonduz_imm_all.particella, '-') AS particella, " +
								"NVL (LTRIM (RTRIM (siticonduz_imm_all.sub)), '-') AS subalterno, " +
								"NVL (LTRIM (RTRIM (siticonduz_imm_all.unimm)), '-') AS unimm, " +
								"NVL (TO_CHAR (siticonduz_imm_all.data_inizio, 'dd/mm/yyyy'), '-') AS data_inizio, " +
								"NVL (TO_CHAR (siticonduz_imm_all.data_fine, 'dd/mm/yyyy'), '-') AS data_fine, " +
								"siticonduz_imm_all.perc_poss AS QUOTA, " +
								"sitiuiu.categoria, NVL (sitiuiu.classe, '-') AS classe, " +
								"NVL (TO_CHAR (sitiuiu.rendita), '-') AS rendita " +
								"FROM siticonduz_imm_all, sitiuiu, siticomu " +
								"WHERE siticonduz_imm_all.cuaa = ? " +
								"AND (siticonduz_imm_all.data_inizio IS NULL " +
								"OR siticonduz_imm_all.data_inizio = TO_DATE ('01/01/0001', 'DD/MM/YYYY') " +
								"OR siticonduz_imm_all.data_inizio <= TO_DATE (?, 'dd/MM/yyyy')) " +
								"AND (siticonduz_imm_all.data_fine IS NULL " +
								"OR siticonduz_imm_all.data_fine = TO_DATE ('31/12/9999', 'DD/MM/YYYY') " +
								"OR siticonduz_imm_all.data_fine >= TO_DATE (?, 'dd/MM/yyyy')) " +
								"AND siticonduz_imm_all.tipo_titolo = 1 " +
								"AND sitiuiu.data_fine_val = TO_DATE ('99991231', 'yyyymmdd') " +
								"AND sitiuiu.cod_nazionale = siticomu.cod_nazionale " +
								"AND siticomu.cod_nazionale = siticonduz_imm_all.cod_nazionale " +
								// PER VELOCIZZARE 3/3/2012"AND sitiuiu.data_fine_val = siticonduz_imm_all.data_fine(+) " +
								"AND sitiuiu.foglio = siticonduz_imm_all.foglio(+) " +
								"AND sitiuiu.particella = siticonduz_imm_all.particella(+) " +
								"AND sitiuiu.sub = siticonduz_imm_all.sub(+) " +
								"AND sitiuiu.unimm = siticonduz_imm_all.unimm(+) " +
								"ORDER BY foglio, particella, unimm";
	
	private final static String SQL_SELECT_LISTA_CATASTO_PER_SOGGETTO_COUNT = "SELECT /*+ ordered use_nl(siticonduz_imm_all) */ COUNT (*) AS conta " +
								"FROM siticonduz_imm_all, sitiuiu, siticomu " +
								"WHERE siticonduz_imm_all.cuaa = ? " +
								"AND (siticonduz_imm_all.data_inizio IS NULL " +
								"OR siticonduz_imm_all.data_inizio = TO_DATE ('01/01/0001', 'DD/MM/YYYY') " +
								"OR siticonduz_imm_all.data_inizio <= TO_DATE (?, 'dd/MM/yyyy')) " +
								"AND (siticonduz_imm_all.data_fine IS NULL " +
								"OR siticonduz_imm_all.data_fine = TO_DATE ('31/12/9999', 'DD/MM/YYYY') " +
								"OR siticonduz_imm_all.data_fine >= TO_DATE (?, 'dd/MM/yyyy')) " +
								"AND siticonduz_imm_all.tipo_titolo = 1 " +
								"AND sitiuiu.data_fine_val = TO_DATE ('99991231', 'yyyymmdd') " +
								"AND sitiuiu.cod_nazionale = siticomu.cod_nazionale " +
								"AND siticomu.cod_nazionale = siticonduz_imm_all.cod_nazionale " +
								// PER VELOCIZZARE 3/3/2012 "AND sitiuiu.data_fine_val = siticonduz_imm_all.data_fine(+) " +
								"AND sitiuiu.foglio = siticonduz_imm_all.foglio(+) " +
								"AND sitiuiu.particella = siticonduz_imm_all.particella(+) " +
								"AND sitiuiu.sub = siticonduz_imm_all.sub(+) " +
								"AND sitiuiu.unimm = siticonduz_imm_all.unimm(+)";

	public Hashtable mCaricareDettaglioIntestatarioF(String codIntF) throws Exception{
		return mCaricareDettaglioIntestatarioF(codIntF, "0");
	}
	
	public Hashtable mCaricareDettaglioIntestatarioF(String codIntF, String ext) throws Exception{
				// carico la lista dei terreni e la metto in un hashtable
				Hashtable ht = new Hashtable();
				Connection conn = null;
				// faccio la connessione al db
				try {
					this.setDatasource(JNDI_CATCOSPOLETO);

					conn = this.getConnection();

					this.initialize();
					String sql = "select decode(cons_sogg_tab.CODI_FISC,null,'-',cons_sogg_tab.CODI_FISC) AS CODICE_FISCALE," +
							"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS COGNOME," +
							"nvl(to_char(cons_sogg_tab.DATA_NASC,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
							"siticomu.COD_NAZIONALE AS FK_COMUNE," +
							"decode(siticomu.NOME,null,'-',siticomu.NOME) AS LUOGO," +
							"decode(cons_sogg_tab.NOME,null,'-',cons_sogg_tab.NOME) AS NOME," +
							"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
							"decode(cons_sogg_tab.SESSO,null,'-',cons_sogg_tab.SESSO) AS SESSO, " +
							"nvl(to_char(cons_sogg_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE, " +
							"decode(cons_sogg_tab.PKID,null,'-',cons_sogg_tab.PKID) AS PKID " +
							"from cons_sogg_tab,siticomu " +
							"WHERE %1% = ? " +
							"AND cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
							"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
							"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) " +
							//"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd')";
							"order by cons_sogg_tab.DATA_FINE desc";

					int indice = 1;
					if ("1".equals(ext))
						sql = sql.replaceAll("%1%", "cons_sogg_tab.PKID");
					else 
						sql = sql.replaceAll("%1%", "cons_sogg_tab.PK_CUAA");
					
					this.setString(indice,codIntF);
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					ArrayList<IntestatarioF> intsF = new ArrayList<IntestatarioF>();
					while (rs.next()){
						IntestatarioF intF = new IntestatarioF();
						intF.setCodFiscale(rs.getString("CODICE_FISCALE"));
						intF.setCodIntestatario(rs.getString("PK_CUAA"));
						intF.setCognome(rs.getString("COGNOME"));
						intF.setDataNascita(rs.getString("DATA_NASCITA"));
						intF.setLuogo(rs.getString("LUOGO"));
						intF.setNome(rs.getString("NOME"));
						intF.setSesso(rs.getString("SESSO"));
						intF.setDataFine(rs.getString("DATA_FINE"));
						intF.setPkId(rs.getString("PKID"));
						intsF.add(intF);
					}
					ht.put("INTESTATARIOF_STO",intsF);
					ht.put("INTESTATARIOF",intsF.size() > 0 ? intsF.get(0) : new IntestatarioF());
					
					/*INIZIO AUDIT*/
					try {
						Object[] arguments = new Object[2];
						arguments[0] = codIntF;
						arguments[1] = ext;
						writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
					} catch (Throwable e) {				
						log.debug("ERRORE nella scrittura dell'audit", e);
					}
					/*FINE AUDIT*/
					
					return ht;
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage(),e);
					throw e;
				}
				finally
				{
					if (conn != null && !conn.isClosed())
						conn.close();
				}

		}

public Hashtable mCaricareDettaglioFromSoggetto(String codIntF) throws Exception{
	
	return mCaricareDettaglioIntestatarioF(codIntF, "1");
}

private Vector mCaricareQualita(Connection conn) throws SQLException{
	// carico la lista terreni
	Vector vct = new Vector();

	String sql = "select descrizione from CAT_TAB_CT_QUALITA";

	Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	while (rs.next()){
		vct.add(rs.getString("descrizione"));
	}

	return vct;
}
public Hashtable mCaricareDettaglioIntestatarioFPerParticella(String codIntF) throws Exception{
			// carico la lista dei terreni e la metto in un hashtable
			Hashtable ht = new Hashtable();
			Connection conn = null;

			// faccio la connessione al db
			try {
				conn = this.getConnection();

				this.initialize();
				String sql = "select decode(cat_intestatari_fisica.CODICE_FISCALE,null,'-',cat_intestatari_fisica.CODICE_FISCALE) AS CODICE_FISCALE," +
				"decode(cat_intestatari_fisica.COGNOME,null,'-',cat_intestatari_fisica.COGNOME) AS COGNOME," +
				//"decode(cat_intestatari_fisica.DATA_NASCITA,null,'-',cat_intestatari_fisica.DATA_NASCITA) AS DATA_NASCITA," +
				"nvl(to_char(cat_intestatari_fisica.DATA_NASCITA,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
				"cat_intestatari_fisica.FK_COMUNE, decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE," +
				"decode(cat_intestatari_fisica.LUOGO,null,'-',cat_intestatari_fisica.LUOGO) AS LUOGO," +
				"decode(cat_intestatari_fisica.NOME,null,'-',cat_intestatari_fisica.NOME) AS NOME," +
				"decode(cat_intestatari_fisica.PK_INTESTATARIO,null,'-',cat_intestatari_fisica.PK_INTESTATARIO) AS PK_INTESTATARIO," +
				"decode(cat_intestatari_fisica.SESSO,null,'-',cat_intestatari_fisica.SESSO) AS SESSO " +
				"from cat_intestatari_fisica, ewg_tab_comuni WHERE PK_INTESTATARIO=? AND ewg_tab_comuni.UK_BELFIORE = cat_intestatari_fisica.FK_COMUNE";

				int indice = 1;
				this.setString(indice,codIntF);
				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				IntestatarioF intF = new IntestatarioF();
				if (rs.next()){
					intF.setCodFiscale(rs.getString("CODICE_FISCALE"));
					intF.setCodIntestatario(rs.getString("PK_INTESTATARIO"));
					intF.setCognome(rs.getString("COGNOME"));
					intF.setComune(rs.getString("COMUNE"));
					intF.setDataNascita(rs.getString("DATA_NASCITA"));
					intF.setLuogo(rs.getString("LUOGO"));
					intF.setNome(rs.getString("NOME"));
					intF.setSesso(rs.getString("SESSO"));
				}
				ht.put("INTESTATARIOF",intF);
				
				/*INIZIO AUDIT*/
				try {
					Object[] arguments = new Object[1];
					arguments[0] = codIntF;
					writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
				} catch (Throwable e) {				
					log.debug("ERRORE nella scrittura dell'audit", e);
				}
				/*FINE AUDIT*/
				
				return ht;
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);
				throw e;
			}
			finally
			{
				if (conn != null && !conn.isClosed())
					conn.close();
			}

	}

public Hashtable mCaricareListaIntestatariPerTerreno(String particella) throws Exception{
	
	Hashtable ht = new Hashtable();
	Vector vct = new Vector();
	
	//recupero le chiavi di ricerca
	List<String> listParam = new ArrayList<String>();
	if (particella.indexOf('|') > 0) {
		listParam = Arrays.asList(particella.split("\\|"));
	}
	
	EnvUtente eu = this.getEnvUtente();
	String enteId = eu.getEnte();
	String userId = eu.getUtente().getUsername();
	String sessionId = eu.getUtente().getSessionId();
	
	CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
	RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
	rc.setEnteId(enteId);
	rc.setUserId(userId);
	rc.setSessionId(sessionId);
	if (listParam != null && listParam.size()>0){
		for (int j=0; j<listParam.size(); j++){
			if (j==1)
				rc.setFoglio(listParam.get(j));
			if (j==2)
				rc.setParticella(listParam.get(j));
			if (j==3)
				rc.setUnimm(listParam.get(j));
			if (j==5)
				rc.setDtInizioRif(SDF.parse(listParam.get(j)));
			if (j==4)
				rc.setDtFineRif(SDF.parse(listParam.get(j)));
		}		
	}
	
	
	
	
	List<SoggettoCatastoDTO> lstSogg = catastoService.getListaSoggettiTerrByFPSDataRange(rc);
	
	for(SoggettoCatastoDTO s : lstSogg){
		
		String flag = s.getConsSoggTab().getFlagPersFisica();
		boolean pf = flag!=null && "P".equalsIgnoreCase(flag) ? true : false;
		ConsSoggTab cc = s.getConsSoggTab();
		
		if(pf && cc!=null){
			
			IntestatarioF intF = new IntestatarioF();
			intF.setCodFiscale(cc.getCodiFisc()!=null ? cc.getCodiFisc() : "-");
			intF.setCodIntestatario(cc.getPkCuaa()!=null ?  cc.getPkCuaa().toString() : "-");
			intF.setCognome(cc.getRagiSoci()!=null ? cc.getRagiSoci() : "-");
			intF.setNome(cc.getNome()!=null ? cc.getNome() : "-");
			intF.setComune(s.getEnteId());
			intF.setDataNascita(cc.getDataNasc()!=null ? SDF.format(cc.getDataNasc()) : "-");
			intF.setSesso(cc.getSesso()!=null ? cc.getSesso()  : "-");
			intF.setDataFine(s.getDataFinePossesso()!=null ? SDF.format(s.getDataFinePossesso()) : "-");
			intF.setDataInizio(s.getDataInizioPossesso()!=null ? SDF.format(s.getDataInizioPossesso()) : "-");
			String quota = "-";
			if (s.getPercPoss() != null) {
				try {
					quota = DF.format(s.getPercPoss());
				} catch (Exception e) {
					quota = s.getPercPoss().toString().replace(",", ".");
					quota = quota.indexOf(".") == -1 || quota.length() < (quota.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? quota : quota.substring(0, quota.indexOf(".") + DF.getMaximumFractionDigits() + 2);
					quota = DF.format(new Double(quota));
				}
			}
			intF.setQuota(quota);						
			intF.setTitolo(s.getTitolo()!=null ? s.getTitolo() : "-");
			rc.setSub(rc.getUnimm());
			rc.setPkCuaa(new BigDecimal(intF.getCodIntestatario()));
			setRegimeSoggCollegatoTerreno(intF, rc);
			vct.add(intF);
			
		}
	}
	
	ht.put("LISTA_INTESTATARIF",vct);
	
	/*INIZIO AUDIT*/
	try {
		Object[] arguments = new Object[1];
		arguments[0] = particella;
		writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
	} catch (Throwable e) {				
		log.debug("ERRORE nella scrittura dell'audit", e);
	}
	/*FINE AUDIT*/
	
	return ht;
	
}

/*	public Hashtable mCaricareListaIntestatariPerTerreno(String particella) throws Exception{
			// carico la lista dei terreni e la metto in un hashtable
			Hashtable ht = new Hashtable();
			Vector vct = new Vector();
			String sql = "";
			String conteggio = "";
			long conteggione = 0;
			Connection conn = null;

			//recupero le chiavi di ricerca
			ArrayList listParam = new ArrayList();
			while (particella.indexOf('|') > 0) {
				listParam.add(particella.substring(0,particella.indexOf('|')));
				particella = particella.substring(particella.indexOf('|')+1);
			}
			listParam.add(particella);

			// faccio la connessione al db
					try {
						this.setDatasource(JNDI_CATCOSPOLETO);

						conn = this.getConnection();
						sql = SQL_SELECT_LISTA_INTESTATARIF_TERRENO;


						int indice = 1;
						this.initialize();
						String pkPart = (String)listParam.get(0);
						String dataI = (String)listParam.get(1);
						String dataF = (String)listParam.get(2);

						this.setString(indice,pkPart);
						this.setString(++indice,dataI);
						this.setString(++indice,dataF);
						this.setString(++indice,dataI);
						//this.setString(++indice,dataF);

						//this.setString(indice,particella);
						//indice ++;
							
						log.debug("TerreniInt1: " + sql);
						
						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						while (rs.next()){
							IntestatarioF intF = new IntestatarioF();
							intF.setCodFiscale(rs.getString("CODICE_FISCALE"));
							intF.setCodIntestatario(rs.getString("PK_CUAA"));
							intF.setCognome(rs.getString("COGNOME"));
							intF.setComune(rs.getString("FK_COMUNE"));
							intF.setDataNascita(rs.getString("DATA_NASCITA"));
							intF.setNome(rs.getString("NOME"));
							intF.setSesso(rs.getString("SESSO"));
							intF.setDataInizio(rs.getString("DATA_INIZIO"));							
							String quota = "-";
							if (rs.getObject("QUOTA") != null) {
								try {
									quota = DF.format(rs.getDouble("QUOTA"));
								} catch (Exception e) {
									quota = rs.getString("QUOTA").replace(",", ".");
									quota = quota.indexOf(".") == -1 || quota.length() < (quota.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? quota : quota.substring(0, quota.indexOf(".") + DF.getMaximumFractionDigits() + 2);
									quota = DF.format(new Double(quota));
								}
							}
							intF.setQuota(quota);						
							//intF.setTitolo("PROPRIETARIO");
							String titolo = rs.getString("TITOLO");
							intF.setTitolo(titolo!=null ? titolo : "PROPRIETARIO");
							intF.setDataFine(rs.getString("DATA_FINE"));
							vct.add(intF);
						}

						sql = SQL_SELECT_LISTA_ALTRIF_TERRENO;


						int indice2 =1;
						this.initialize();
						//this.setString(1,particella);
						this.setString(indice2,pkPart);
						this.setString(++indice2,dataI);
						this.setString(++indice2,dataF);
						this.setString(++indice2,dataI);
						//this.setString(++indice2,dataF);
						log.debug("TerreniInt2: " + sql);
						prepareStatement(sql);
						java.sql.ResultSet rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						while (rs2.next()){
							IntestatarioF intF = new IntestatarioF();
							intF.setCodFiscale(rs2.getString("CODICE_FISCALE"));
							intF.setCodIntestatario(rs2.getString("PK_CUAA"));
							intF.setCognome(rs2.getString("COGNOME"));
							intF.setComune(rs2.getString("FK_COMUNE"));
							intF.setDataNascita(rs2.getString("DATA_NASCITA"));
							intF.setNome(rs2.getString("NOME"));
							intF.setSesso(rs2.getString("SESSO"));
							intF.setDataInizio(rs2.getString("DATA_INIZIO"));							
							String quota = "-";
							if (rs2.getObject("QUOTA") != null) {
								try {
									quota = DF.format(rs2.getDouble("QUOTA"));
								} catch (Exception e) {
									quota = rs2.getString("QUOTA").replace(",", ".");
									quota = quota.indexOf(".") == -1 || quota.length() < (quota.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? quota : quota.substring(0, quota.indexOf(".") + DF.getMaximumFractionDigits() + 2);
									quota = DF.format(new Double(quota));
								}
							}
							intF.setQuota(quota);						
							//intF.setTitolo("ALTRO");
							String titolo = rs2.getString("TITOLO");
							intF.setTitolo(titolo!=null ? titolo : "ALTRO");
							intF.setDataFine(rs2.getString("DATA_FINE"));
							vct.add(intF);
						}

						ht.put("LISTA_INTESTATARIF",vct);
						return ht;
					}
				catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage(),e);
					throw e;
				}
				finally
				{
					if (conn != null && !conn.isClosed())
						conn.close();
				}

		}*/
	
	public Hashtable mCaricareListaIntestatariPerFabbricato(String chiave) throws Exception{
			// carico la lista dei terreni e la metto in un hashtable
			Hashtable ht = new Hashtable();
			Vector vct = new Vector();
			String sql = "";
			String conteggio = "";
			long conteggione = 0;

			//recupero le chiavi di ricerca
			List<String> listParam = Arrays.asList(chiave.split("\\|"));

			String unimm = null;
			if (listParam.size() > 4)
			{
				unimm = (String)listParam.get(4);
				if (!unimm.equals("") && !unimm.equals("0")){
					
					while (unimm.length()<4){
						unimm = "0"+unimm;
					}
				}
			}
			Connection conn = null;
			// faccio la connessione al db
					try {
						this.setDatasource(JNDI_CATCOSPOLETO);

						conn = this.getConnection();
						
						long startTime = new Date().getTime();
						
						sql = SQL_SELECT_LISTA_INTESTATARIF_FABBRICATO_NUOVO;


						int indice = 0;
						this.initialize();
						if (listParam.size() == 7 && unimm != null)
						{
							this.setString(++indice,(String)listParam.get(0));
							this.setString(++indice,(String)listParam.get(1));
							this.setString(++indice,(String)listParam.get(2));
							String parametro_i = (String)listParam.get(3);
							//this.setString(++indice,parametro_i);
							//this.setString(++indice,parametro_i);
							this.setString(++indice,unimm);
							String data_f = (String)listParam.get(5);
							String data_i = (String)listParam.get(6);
							this.setString(++indice,data_i);
							this.setString(++indice,data_f);
							this.setString(++indice,data_i);
							//this.setString(++indice,data_f);
						}
						else
						{
							// IN CASO NON ARRIVI ALCUNA CHIAVE, FORZO A NON TROVARE NULLA
							this.setString(++indice, "-1");
							this.setString(++indice, "-1");
							this.setString(++indice, "-1");
							//this.setString(++indice, "-1");
							//this.setString(++indice, "-1");
							this.setString(++indice, "-1");
							this.setString(++indice, "31/12/9999");
							this.setString(++indice, "31/12/9999");
							this.setString(++indice, "31/12/9999");
							//this.setString(++indice, "31/12/9999");
							sql += " and 0 = 1 ";
						}

						sql += " order by siticonduz_imm_all.data_fine desc";
						
						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						log.debug("DURATA LETTURA INTESTATARI F PER FABBRICATO MS " + (new Date().getTime() - startTime));
						
						while (rs.next()){
							IntestatarioF intF = new IntestatarioF();
							intF.setCodFiscale(rs.getString("CODICE_FISCALE"));
							intF.setCodIntestatario(rs.getString("PK_CUAA"));
							intF.setCognome(rs.getString("COGNOME"));
							intF.setComune(rs.getString("FK_COMUNE"));
							intF.setDataNascita(rs.getString("DATA_NASCITA"));
							intF.setLuogo(rs.getString("COMUNE_NASCITA"));
							intF.setNome(rs.getString("NOME"));
							intF.setSesso(rs.getString("SESSO"));
							intF.setDataInizio(rs.getString("DATA_INIZIO"));
							
							String quota = "-";
							if (rs.getObject("QUOTA") != null) {
								try {
									quota = DF.format(rs.getDouble("QUOTA"));
								} catch (Exception e) {
									quota = rs.getString("QUOTA").replace(",", ".");
									quota = quota.indexOf(".") == -1 || quota.length() < (quota.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? quota : quota.substring(0, quota.indexOf(".") + DF.getMaximumFractionDigits() + 2);
									quota = DF.format(new Double(quota));
								}
							}
							intF.setQuota(quota);
							
							String quotaNum = "-";
							if (rs.getObject("QUOTA_NUM") != null) {
								try {
									quotaNum = DF.format(rs.getDouble("QUOTA_NUM"));
								} catch (Exception e) {
									quotaNum = rs.getString("QUOTA_NUM").replace(",", ".");
									quotaNum = quotaNum.indexOf(".") == -1 || quotaNum.length() < (quotaNum.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? quotaNum : quotaNum.substring(0, quotaNum.indexOf(".") + DF.getMaximumFractionDigits() + 2);
									quotaNum = DF.format(new Double(quotaNum));
								}
							}
							intF.setQuotaNum(quotaNum);
							
							String quotaDenom = "-";
							if (rs.getObject("QUOTA_DENOM") != null) {
								try {
									quotaDenom = DF.format(rs.getDouble("QUOTA_DENOM"));
								} catch (Exception e) {
									quotaDenom = rs.getString("QUOTA_DENOM").replace(",", ".");
									quotaDenom = quotaDenom.indexOf(".") == -1 || quotaDenom.length() < (quotaDenom.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? quotaDenom : quotaDenom.substring(0, quotaDenom.indexOf(".") + DF.getMaximumFractionDigits() + 2);
									quotaDenom = DF.format(new Double(quotaDenom));
								}
							}
							intF.setQuotaDenom(quotaDenom);
							
							if (quotaNum.equals("-") || quotaDenom.equals("-")) {
								intF.setQuota("-");
							} else {
								try {
									if (DF.parse(quotaNum).doubleValue() <= 0 || DF.parse(quotaDenom).doubleValue() <= 0) {
										intF.setQuota("-");
									}
								} catch (Exception e) {
									intF.setQuota("-");
								}
							}
							
							intF.setAffidabilita(rs.getObject("AFFIDABILITA") == null ? "-" : rs.getString("AFFIDABILITA"));
							
							//intF.setTitolo("PROPRIETARIO");
							intF.setTitolo(rs.getString("DESC_TIPO_DOCUMENTO"));
							intF.setDataFine(rs.getString("DATA_FINE"));
							startTime = new Date().getTime();
							setRegimeSoggCollegatoImmobile(intF, (String)listParam.get(1), (String)listParam.get(2), (String)listParam.get(3), (String)listParam.get(4));
							log.debug("DURATA LETTURA REGIME SOGGETTO F COLLEGATO IMMOBILE MS " + (new Date().getTime() - startTime));
							vct.add(intF);
						}
					
						ht.put("LISTA_INTESTATARIF",vct);
						
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
					if (conn != null && !conn.isClosed())
						conn.close();
				}

		}
	public Hashtable mCaricareListaIntestatariF(Vector listaPar,IntestatarioFFinder FFinder) throws Exception{
		// carico la lista dei terreni e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
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
			if (FFinder.getKeyStr().equals("")){
				sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
			}
			else{
				//controllo provenienza chiamata
				String controllo = FFinder.getKeyStr();
				if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
					String ente = controllo.substring(0,controllo.indexOf("|"));
					ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
					String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
					sql = sql + " and cons_sogg_tab.PK_CUAA in (" +chiavi + ")" ;
					//sql = sql + " and siticomu.COD_NAZIONALE = '" +ente + "'" ;
				}else
					sql = sql + " and PK_CUAA in (" +FFinder.getKeyStr() + ")" ;

			}
			long limInf, limSup;
			limInf = (FFinder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
			limSup = FFinder.getPaginaAttuale() * RIGHE_PER_PAGINA;
			//sql = sql + " ORDER BY COGNOME,NOME ";
			//if (i ==1) sql = sql + " )) where N > " + limInf + " and N <=" + limSup;
			if (i ==1){
				sql = sql + " ORDER BY COGNOME,NOME ";
				sql = sql + " )) where N > " + limInf + " and N <=" + limSup;
			} else {
				sql = sql + " ))";
			}

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			if (i ==1) {
					while (rs.next()){

						IntestatarioF intF = new IntestatarioF();
						intF.setCodFiscale(rs.getString("CODICE_FISCALE"));
						intF.setCodIntestatario(rs.getString("PK_CUAA"));
						intF.setCognome(rs.getString("COGNOME"));
						//intF.setComune(rs.getString("FK_COMUNE"));
						intF.setDataNascita(rs.getString("DATA_NASCITA"));
						intF.setLuogo(rs.getString("LUOGO"));
						intF.setNome(rs.getString("NOME"));
						intF.setSesso(rs.getString("SESSO"));

						vct.add(intF);
					}
			}

			else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}//fine FOR
			ht.put("LISTA_INTESTATARIF",vct);
			FFinder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			FFinder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			FFinder.setTotaleRecord(conteggione);
			FFinder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER",FFinder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = listaPar;
				arguments[1] = FFinder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Hashtable mCaricareListaIntestatariFromSoggetto(String idSoggetto,IntestatarioFFinder FFinder) throws Exception{
		// carico la lista dei terreni e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		
		Connection conn = null;
		// faccio la connessione al db
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

			
					sql = SQL_SELECT_LISTA_FROM_SOGGETTO;

					indice = 1;
					this.initialize();
					this.setString(indice,idSoggetto);
					



			

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			
					while (rs.next()){

						IntestatarioF intF = new IntestatarioF();
						intF.setCodFiscale(rs.getString("CODICE_FISCALE"));
						intF.setCodIntestatario(rs.getString("PK_CUAA"));
						intF.setCognome(rs.getString("COGNOME"));
						//intF.setComune(rs.getString("FK_COMUNE"));
						intF.setDataNascita(rs.getString("DATA_NASCITA"));
						intF.setLuogo(rs.getString("LUOGO"));
						intF.setNome(rs.getString("NOME"));
						intF.setSesso(rs.getString("SESSO"));

						vct.add(intF);
					}
			

			
			ht.put("LISTA_INTESTATARIF",vct);
			FFinder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			FFinder.setPagineTotali(1);
			FFinder.setTotaleRecord(vct.size());
			FFinder.setRighePerPagina(vct.size());

			ht.put("FINDER",FFinder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = idSoggetto;
				arguments[1] = FFinder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Vector mCaricareCatastoPerSoggetto(String codFiscaleDic, int annoImposta) throws Exception {
		// carico la lista dei terreni e la metto in un hashtable
		Vector vct = new Vector();
		String sql = "";
		Connection conn = null;
		// faccio la connessione al db
				try {
					this.setDatasource(JNDI_CATCOSPOLETO);

					conn = this.getConnection();
					sql = SQL_SELECT_LISTA_CATASTO_PER_SOGGETTO;
					int indice = 0;
					this.initialize();
					this.setString(++indice, codFiscaleDic);
					this.setString(++indice, "31/12/" + annoImposta);
					this.setString(++indice, "01/01/" + annoImposta);
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs.next()){
						CatastoPerSoggetto cps = new CatastoPerSoggetto();
						cps.setSezione(super.tornaValoreRS(rs, "SEZIONE"));
						cps.setFoglio(super.tornaValoreRS(rs, "FOGLIO"));
						cps.setParticella(super.tornaValoreRS(rs, "PARTICELLA"));
						cps.setSubalterno(super.tornaValoreRS(rs, "SUBALTERNO"));
						cps.setUnimm(super.tornaValoreRS(rs, "UNIMM"));
						cps.setDataInizio(rs.getString("DATA_INIZIO"));
						cps.setDataFine(rs.getString("DATA_FINE"));
						String quota = "-";
						if (rs.getObject("QUOTA") != null) {
							try {
								quota = DF.format(rs.getDouble("QUOTA"));
							} catch (Exception e) {
								quota = rs.getString("QUOTA").replace(",", ".");
								quota = quota.indexOf(".") == -1 || quota.length() < (quota.indexOf(".") + DF.getMaximumFractionDigits() + 2) ? quota : quota.substring(0, quota.indexOf(".") + DF.getMaximumFractionDigits() + 2);
								quota = DF.format(new Double(quota));
							}
							quota += " %";
						}
						cps.setQuota(quota);
						cps.setCategoria(super.tornaValoreRS(rs, "CATEGORIA"));
						cps.setClasse(super.tornaValoreRS(rs, "CLASSE"));
						cps.setRendita(super.tornaValoreRS(rs, "RENDITA"));						
						vct.add(cps);
					}

					return vct;
				}
			catch (Exception e) {
				// TODO Auto-generated catch block
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
		int numCatasto = 0;
		try
		{
			conn = this.getConnection();
			//PreparedStatement pstmtCatasto = conn.prepareStatement("SELECT COUNT (*) AS CONTA FROM (" + SQL_SELECT_LISTA_CATASTO_PER_SOGGETTO + ")");
			PreparedStatement pstmtCatasto = conn.prepareStatement(SQL_SELECT_LISTA_CATASTO_PER_SOGGETTO_COUNT);
			int indice = 0;
			pstmtCatasto.setString(++indice, codFiscaleDic);
			log.debug("Param. 1: " + codFiscaleDic);
			pstmtCatasto.setString(++indice, "31/12/" + annoImposta);
			log.debug("Param. 2: " + "31/12/" + annoImposta);
			pstmtCatasto.setString(++indice, "01/01/" + annoImposta);
			log.debug("Param. 3: " + "01/01/" + annoImposta);
			log.debug("SQL: " + SQL_SELECT_LISTA_CATASTO_PER_SOGGETTO_COUNT);
			ResultSet rsCatasto = pstmtCatasto.executeQuery();				
			while (rsCatasto.next()) {
				numCatasto = rsCatasto.getInt("CONTA");
			}
			return numCatasto;
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
	
	public String mTrovaIntestatario(String codFiscale, String nome, String cognome, String dataNascita) throws Exception {
		// carico la lista dei terreni e la metto in un hashtable
		String codIntestatario="";
		String sql = "";
		Connection conn = null;
		// faccio la connessione al db
				try {
					this.setDatasource(JNDI_CATCOSPOLETO);

					conn = this.getConnection();
					sql = "select * from cons_sogg_tab where 1=1 ";
					int indice = 0;
					this.initialize();
					
					if (codFiscale != null && !codFiscale.equals("")){
						sql= sql + " and codi_fisc= ?";
					this.setString(1, codFiscale);
					}
					else{
						if (nome != null && !nome.equals("") && cognome != null && !cognome.equals("")){
						sql= sql + " and nome=? and ragi_soci=? ";
						this.setString(1, nome);
						this.setString(2, cognome);
						if (dataNascita != null && !dataNascita.equals("") )
							sql= sql+ " and data_nasc= to_date(?,'dd/MM/yyyy')";
							this.setString(3, dataNascita);
						}
					}
					
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs.next()){
						 codIntestatario= rs.getString("PK_CUAA");
					}

					return codIntestatario;
				}
			catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);
				throw e;
			}
			finally
			{
				if (conn != null && !conn.isClosed())
					conn.close();
			}
	}
	
	private void setRegimeSoggCollegatoImmobile(IntestatarioF intF, String foglio, String particella, String sub, String unimm) throws Exception {
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(enteId);
		rc.setCodEnte(enteId);
		rc.setUserId(userId);
		rc.setSessionId(sessionId);
		rc.setFoglio(foglio);
		rc.setParticella(particella);
		rc.setSub(sub);
		rc.setUnimm(unimm);
		rc.setPkCuaa(new BigDecimal(intF.getCodIntestatario()));
		
		String regime = catastoService.getRegimeImmobili(rc);
		String descRegime = null;
		if (regime == null || regime.equals("")) {
			regime = "-";
			descRegime = "-";
		} else if (regime.equalsIgnoreCase("C")) {
			descRegime = "COMUNIONE";
		} else if (regime.equalsIgnoreCase("P")) {
			descRegime = "BENE PERSONALE";
		} else if (regime.equalsIgnoreCase("S")) {
			descRegime = "IN SEPARAZIONE";
		} else if (regime.equalsIgnoreCase("D")) {
			descRegime = "IN COMUNIONE DE RESIDUO";
		}
		intF.setRegime(regime);
		intF.setDescRegime(descRegime);
		
		String soggCollegato = catastoService.getSoggettoCollegatoImmobili(rc);
		intF.setSoggCollegato(soggCollegato);
		
	}
	
	private void setRegimeSoggCollegatoTerreno(IntestatarioF intF, RicercaOggettoCatDTO rc) throws Exception {		
		
		CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
		String regime = catastoService.getRegimeTerreni(rc);
		String descRegime = null;
		if (regime == null || regime.equals("")) {
			regime = "-";
			descRegime = "-";
		} else if (regime.equalsIgnoreCase("C")) {
			descRegime = "COMUNIONE";
		} else if (regime.equalsIgnoreCase("P")) {
			descRegime = "BENE PERSONALE";
		} else if (regime.equalsIgnoreCase("S")) {
			descRegime = "IN SEPARAZIONE";
		} else if (regime.equalsIgnoreCase("D")) {
			descRegime = "IN COMUNIONE DE RESIDUO";
		}
		intF.setRegime(regime);
		intF.setDescRegime(descRegime);
		
		String soggCollegato = catastoService.getSoggettoCollegatoTerreni(rc);
		intF.setSoggCollegato(soggCollegato);		
	}	

}


