/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.logic;

import it.escsolution.escwebgis.catasto.bean.IntestatarioG;
import it.escsolution.escwebgis.catasto.bean.IntestatarioGFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoCatastoDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;

import java.sql.Connection;
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


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CatastoIntestatariGLogic extends EscLogic{
	private String appoggioDataSource;
	
	private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
		public CatastoIntestatariGLogic(EnvUtente eu) {
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
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
			"select ROWNUM as N,DENOMINAZIONE,FK_COMUNE,PARTITA_IVA,PK_CUAA,SEDE " +
			"from (select ROWnum as N," +
			"decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS DENOMINAZIONE," +
			"siticomu.COD_NAZIONALE AS FK_COMUNE," +
			"decode(cons_sogg_tab.CODI_PIVA,null,'-',cons_sogg_tab.CODI_PIVA) AS PARTITA_IVA," +
			"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS SEDE " +
			"from cons_sogg_tab,siticomu " +
			"WHERE 1=? " +
			"and cons_sogg_tab.FLAG_PERS_FISICA='G' " +
			//"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) ";

	private final static String SQL_SELECT_COUNT_LISTA=""+
			"select count(*) as conteggio  from cons_sogg_tab,siticomu " +
			"WHERE cons_sogg_tab.FLAG_PERS_FISICA='G' " +
			//"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
			"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) " +
			"and 1=? ";
	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from cons_sogg_tab WHERE cons_sogg_tab.FLAG_PERS_FISICA='G' and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') and 1=? ";

	private final static String SQL_SELECT_LISTA_INTESTATARIG_TERRENO = ""+
	"select decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS DENOMINAZIONE," +
	"sititrkc.COD_NAZIONALE AS FK_COMUNE," +
	"decode(cons_sogg_tab.CODI_PIVA,null,'-',cons_sogg_tab.CODI_PIVA) AS PARTITA_IVA," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"nvl(to_char(cons_cons_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl(to_char(cons_cons_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_cons_tab.PERC_POSS,null,'-',cons_cons_tab.PERC_POSS) AS QUOTA, " +
	"cons_deco_tab.description AS TITOLO " +
	"from cons_sogg_tab, cons_cons_tab, sititrkc, cons_deco_tab " +
	"WHERE cons_sogg_tab.PK_CUAA =  cons_cons_tab.PK_CUAA " +
	"and sititrkc.PARTICELLA = cons_cons_tab.PARTICELLA " +
	"and sititrkc.FOGLIO = cons_cons_tab.FOGLIO " +
	"and sititrkc.COD_NAZIONALE = cons_cons_tab.COD_NAZIONALE " +
	"and sititrkc.SUB = cons_cons_tab.SUB " +
	"and sititrkc.TRKC_ID = ? " +
	"and cons_sogg_tab.FLAG_PERS_FISICA = 'G' " +
	"and cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
	//"and (cons_cons_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_cons_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " ;
	//"AND cons_cons_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_cons_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy') " ;
	"AND( ( cons_cons_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_cons_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_cons_tab.data_inizio >= TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_cons_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))) " +
	"AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_cons_tab.tipo_documento ";

	private final static String SQL_SELECT_LISTA_ALTRIG_TERRENO = ""+
	"select decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS DENOMINAZIONE," +
	"sititrkc.COD_NAZIONALE AS FK_COMUNE," +
	"decode(cons_sogg_tab.CODI_PIVA,null,'-',cons_sogg_tab.CODI_PIVA) AS PARTITA_IVA," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"nvl(to_char(cons_ufre_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl(to_char(cons_ufre_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_ufre_tab.PERC_POSS,null,'-',cons_ufre_tab.PERC_POSS) AS QUOTA, " +
	"cons_deco_tab.description AS TITOLO " +
	"from cons_sogg_tab, cons_ufre_tab, sititrkc, cons_deco_tab " +
	"WHERE cons_sogg_tab.PK_CUAA =  cons_ufre_tab.PK_CUAA " +
	"and sititrkc.PARTICELLA = cons_ufre_tab.PARTICELLA " +
	"and sititrkc.FOGLIO = cons_ufre_tab.FOGLIO " +
	"and sititrkc.COD_NAZIONALE = cons_ufre_tab.COD_NAZIONALE " +
	"and sititrkc.SUB = cons_ufre_tab.SUB " +
	"and sititrkc.TRKC_ID = ? " +
	"and cons_sogg_tab.FLAG_PERS_FISICA = 'G' " +
	"and cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
	//"and (cons_ufre_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_ufre_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " ;
	//"AND cons_ufre_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_ufre_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy') " ;
	"AND( ( cons_ufre_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_ufre_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_ufre_tab.data_inizio >= TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_ufre_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy')))" +
	"AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_ufre_tab.tipo_documento ";


	private final static String SQL_SELECT_LISTA_INTESTATARIG_FABBRICATO = "" +
	"select decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS DENOMINAZIONE," +
	"siticomu.COD_NAZIONALE AS FK_COMUNE," +
	"decode(siticomu.NOME,null,'-',siticomu.NOME) AS SEDE," +
	"decode(cons_sogg_tab.CODI_PIVA,null,'-',cons_sogg_tab.CODI_PIVA) AS PARTITA_IVA," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"nvl(to_char(cons_csui_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl(to_char(cons_csui_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_csui_tab.PERC_POSS,null,'-',cons_csui_tab.PERC_POSS) AS QUOTA, " +
	"siticonduz_imm_all.QUOTA_NUM, " +
	"siticonduz_imm_all.QUOTA_DENOM, " +
	"siticonduz_imm_all.AFFIDABILITA, " +
	"cons_deco_tab.description AS TITOLO " +
	"from cons_csui_tab,cons_sogg_tab,siticomu, cons_deco_tab, siticonduz_imm_all " +
	"WHERE cons_csui_tab.COD_NAZIONALE = ? " +
	"and cons_csui_tab.FOGLIO = ? " +
	"and LPAD(cons_csui_tab.PARTICELLA,5,'0') = ? " +
	//"and DECODE(cons_csui_tab.SUB,' ','-',NVL(cons_csui_tab.SUB,'*'),'-',cons_csui_tab.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
	"and cons_csui_tab.UNIMM = ? " +
	//"and (cons_csui_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_csui_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " +
	//"AND cons_csui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_csui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy') " +
	"AND( ( cons_csui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_csui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_csui_tab.data_inizio >= TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_csui_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))) " +
	"and cons_csui_tab.PK_CUAA = cons_sogg_tab.PK_CUAA " +
	"and cons_sogg_tab.FLAG_PERS_FISICA='G' " +
	"and cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) " +	
	"and siticonduz_imm_all.COD_NAZIONALE = cons_csui_tab.COD_NAZIONALE " +
	"and siticonduz_imm_all.FOGLIO = cons_csui_tab.FOGLIO " +
	"and siticonduz_imm_all.PARTICELLA = cons_csui_tab.PARTICELLA " +
	"and siticonduz_imm_all.UNIMM = cons_csui_tab.UNIMM " +
	"and siticonduz_imm_all.PK_CUAA = cons_csui_tab.PK_CUAA " +
	"and NVL(siticonduz_imm_all.DATA_INIZIO, TO_DATE('01/01/1000', 'DD/MM/YYYY')) = NVL(cons_csui_tab.DATA_INIZIO, TO_DATE('01/01/1000', 'DD/MM/YYYY'))  " +
	"and NVL(siticonduz_imm_all.DATA_FINE, TO_DATE('31/12/9999', 'DD/MM/YYYY')) = NVL(cons_csui_tab.DATA_FINE, TO_DATE('31/12/9999', 'DD/MM/YYYY')) " +
	"and siticonduz_imm_all.ATTO_INI = cons_csui_tab.ATTO_INI " +
	"and siticonduz_imm_all.TIPO_TITOLO = cons_csui_tab.TIPO_TITOLO " +	
	"AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_csui_tab.tipo_documento ";

	private final static String SQL_SELECT_LISTA_ALTRIG_FABBRICATO = "" +
	"select decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS DENOMINAZIONE," +
	"siticomu.COD_NAZIONALE AS FK_COMUNE," +
	"decode(siticomu.NOME,null,'-',siticomu.NOME) AS SEDE," +
	"decode(cons_sogg_tab.CODI_PIVA,null,'-',cons_sogg_tab.CODI_PIVA) AS PARTITA_IVA," +
	"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
	"nvl(to_char(cons_urui_tab.DATA_INIZIO,'dd/mm/yyyy'),'-') AS DATA_INIZIO," +
	"nvl(to_char(cons_urui_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE," +
	"decode(cons_urui_tab.PERC_POSS,null,'-',cons_urui_tab.PERC_POSS) AS QUOTA " +
	"from cons_urui_tab,cons_sogg_tab,siticomu " +
	"WHERE cons_urui_tab.COD_NAZIONALE = ? " +
	"and cons_urui_tab.FOGLIO = ? " +
	"and LPAD(cons_urui_tab.PARTICELLA,5,'0') = ? " +
	//"and DECODE(cons_urui_tab.SUB,' ','-',NVL(cons_urui_tab.SUB,'*'),'-',cons_urui_tab.SUB) = decode (NVL(?,'*'),'*','-',' ','-',?) " +
	"and cons_urui_tab.UNIMM = ? " +
	//"and (cons_urui_tab.data_inizio between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy') " +
	//"or cons_urui_tab.data_fine between to_date (?, 'dd/mm/yyyy') AND to_date (?, 'dd/mm/yyyy')) " +
	//"AND cons_urui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
	//"AND cons_urui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy') " +
	"AND( ( cons_urui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') AND cons_urui_tab.data_fine <= TO_DATE (?, 'dd/mm/yyyy')) " +
	"OR (cons_urui_tab.data_inizio >= TO_DATE ('01/01/0001', 'dd/mm/yyyy') AND cons_urui_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))) " +
	"and cons_urui_tab.PK_CUAA = cons_sogg_tab.PK_CUAA " +
	"and cons_sogg_tab.FLAG_PERS_FISICA='G' " +
	"and cons_sogg_tab.DATA_FINE = TO_DATE('31-12-9999','DD-MM-YYYY') " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
	"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) ";


	public Hashtable mCaricareDettaglioIntestatarioG(String codIntG) throws Exception{
			// carico la lista dei terreni e la metto in un hashtable
			Hashtable ht = new Hashtable();
			Connection conn = null;
			// faccio la connessione al db
			try {
				this.setDatasource(JNDI_CATCOSPOLETO);
				conn = this.getConnection();

				this.initialize();

				String sql = "select decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS DENOMINAZIONE," +
						"siticomu.COD_NAZIONALE AS FK_COMUNE," +
						"decode(cons_sogg_tab.CODI_PIVA,null,'-',cons_sogg_tab.CODI_PIVA) AS PARTITA_IVA," +
						"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
						"decode(siticomu.NOME,null,'-',siticomu.NOME) AS SEDE, " +
						"nvl(to_char(cons_sogg_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE, " +
						"decode(cons_sogg_tab.PKID,null,'-',cons_sogg_tab.PKID) AS PKID " +
						"from cons_sogg_tab,siticomu " +
						"WHERE cons_sogg_tab.PK_CUAA = ? " +
						"AND cons_sogg_tab.FLAG_PERS_FISICA = 'G' " +
						"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
						"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) " +
						"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd')";

				int indice = 1;
				this.setString(indice,codIntG);
				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				IntestatarioG intG= new IntestatarioG();
				if (rs.next()){
					intG.setCodIntestatario(rs.getString("PK_CUAA"));
					intG.setDenominazione(rs.getString("DENOMINAZIONE"));
					intG.setPartitaIva(rs.getString("PARTITA_IVA"));
					intG.setSede(rs.getString("SEDE"));
					intG.setDataFine(rs.getString("DATA_FINE"));
					intG.setPkId(rs.getString("PKID"));
				}
				ht.put("INTESTATARIOG",intG);
				
				/*INIZIO AUDIT*/
				try {
					Object[] arguments = new Object[1];
					arguments[0] = codIntG;
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

	public Hashtable mCaricareDettaglioFromSoggetto(String codIntG) throws Exception{
		// carico la lista dei terreni e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Connection conn = null;
		// faccio la connessione al db
		try {
			this.setDatasource(JNDI_CATCOSPOLETO);
			conn = this.getConnection();

			this.initialize();

			String sql = "select decode(cons_sogg_tab.RAGI_SOCI,null,'-',cons_sogg_tab.RAGI_SOCI) AS DENOMINAZIONE," +
					"siticomu.COD_NAZIONALE AS FK_COMUNE," +
					"decode(cons_sogg_tab.CODI_PIVA,null,'-',cons_sogg_tab.CODI_PIVA) AS PARTITA_IVA," +
					"decode(cons_sogg_tab.PK_CUAA,null,'-',cons_sogg_tab.PK_CUAA) AS PK_CUAA," +
					"decode(siticomu.NOME,null,'-',siticomu.NOME) AS SEDE, " +
					"nvl(to_char(cons_sogg_tab.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE, " +
					"decode(cons_sogg_tab.PKID,null,'-',cons_sogg_tab.PKID) AS PKID " +
					"from cons_sogg_tab,siticomu " +
					"WHERE cons_sogg_tab.PKID = ? " +
					"AND cons_sogg_tab.FLAG_PERS_FISICA = 'G' " +
					"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
					"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+) " +
					"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd')";

			int indice = 1;
			this.setString(indice,codIntG);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			IntestatarioG intG= new IntestatarioG();
			if (rs.next()){
				intG.setCodIntestatario(rs.getString("PK_CUAA"));
				intG.setDenominazione(rs.getString("DENOMINAZIONE"));
				intG.setPartitaIva(rs.getString("PARTITA_IVA"));
				intG.setSede(rs.getString("SEDE"));
				intG.setDataFine(rs.getString("DATA_FINE"));
				intG.setPkId(rs.getString("PKID"));
			}
			ht.put("INTESTATARIOG",intG);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = codIntG;
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
		
		if(!pf && cc!=null){
			
			IntestatarioG intG = new IntestatarioG();
			intG.setCodIntestatario(cc.getPkCuaa()!=null ?  cc.getPkCuaa().toString() : "-");
			intG.setComune(s.getEnteId());
			intG.setDenominazione(cc.getRagiSoci()!=null ? cc.getRagiSoci() : "-");
			intG.setPartitaIva(cc.getCodiPiva()!=null ? cc.getCodiPiva() : "-");
			intG.setDataFine(s.getDataFinePossesso()!=null ? SDF.format(s.getDataFinePossesso()) : "-");
			intG.setDataInizio(s.getDataInizioPossesso()!=null ? SDF.format(s.getDataInizioPossesso()) : "-");
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
			intG.setQuota(quota);						
			intG.setTitolo(s.getTitolo()!=null ? s.getTitolo() : "-");
			vct.add(intG);
		}
	}
	
	ht.put("LISTA_INTESTATARIG",vct);
	
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

/*
public Hashtable mCaricareListaIntestatariPerTerreno(String particella) throws Exception{
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
					sql = SQL_SELECT_LISTA_INTESTATARIG_TERRENO;


					int indice = 1;
					this.initialize();
					//this.setString(indice,particella);
					//indice ++;
					String pkPart = (String)listParam.get(0);
					String dataI = (String)listParam.get(1);
					String dataF = (String)listParam.get(2);

					this.setString(indice,pkPart);
					this.setString(++indice,dataI);
					this.setString(++indice,dataF);
					this.setString(++indice,dataI);
					//this.setString(++indice,dataF);

					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					while (rs.next()){
						IntestatarioG intG = new IntestatarioG();
						intG.setCodIntestatario(rs.getString("PK_CUAA"));
						intG.setComune(rs.getString("FK_COMUNE"));
						intG.setDenominazione(rs.getString("DENOMINAZIONE"));
						intG.setPartitaIva(rs.getString("PARTITA_IVA"));
						intG.setDataFine(rs.getString("DATA_FINE"));
						intG.setDataInizio(rs.getString("DATA_INIZIO"));
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
						intG.setQuota(quota);						
						//intG.setTitolo("PROPRIETARIO");
						String titolo = rs.getString("TITOLO");
						intG.setTitolo(titolo!=null ? titolo : "PROPRIETARIO");
						vct.add(intG);
					}

					sql = SQL_SELECT_LISTA_ALTRIG_TERRENO;

					//this.initialize();
					//this.setString(1,particella);
					int indice2 =1;
					this.initialize();
					//this.setString(1,particella);
					this.setString(indice2,pkPart);
					this.setString(++indice2,dataI);
					this.setString(++indice2,dataF);
					this.setString(++indice2,dataI);
					//this.setString(++indice2,dataF);

					prepareStatement(sql);
					java.sql.ResultSet rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					while (rs2.next()){
						IntestatarioG intG = new IntestatarioG();
						intG.setCodIntestatario(rs2.getString("PK_CUAA"));
						intG.setComune(rs2.getString("FK_COMUNE"));
						intG.setDenominazione(rs2.getString("DENOMINAZIONE"));
						intG.setPartitaIva(rs2.getString("PARTITA_IVA"));
						intG.setDataFine(rs2.getString("DATA_FINE"));
						intG.setDataInizio(rs2.getString("DATA_INIZIO"));
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
						intG.setQuota(quota);
						//intG.setTitolo("ALTRO");
						String titolo = rs2.getString("TITOLO");
						intG.setTitolo(titolo!=null ? titolo : "ALTRO");
						vct.add(intG);
					}

					ht.put("LISTA_INTESTATARIG",vct);
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
						
						sql = SQL_SELECT_LISTA_INTESTATARIG_FABBRICATO;


						int indice = 0;
						this.initialize();
						if (listParam.size() ==7 && unimm != null)
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
						
						sql += " order by cons_csui_tab.data_fine desc";
						
						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						
						log.debug("DURATA LETTURA INTESTATARI G PER FABBRICATO MS " + (new Date().getTime() - startTime));

						while (rs.next()){
							IntestatarioG intG = new IntestatarioG();
							intG.setCodIntestatario(rs.getString("PK_CUAA"));
							intG.setComune(rs.getString("FK_COMUNE"));
							intG.setDenominazione(rs.getString("DENOMINAZIONE"));
							intG.setPartitaIva(rs.getString("PARTITA_IVA"));
							intG.setSede(rs.getString("SEDE"));
							intG.setDataInizio(rs.getString("DATA_INIZIO"));
							
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
							intG.setQuota(quota);
							
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
							intG.setQuotaNum(quotaNum);
							
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
							intG.setQuotaDenom(quotaDenom);
							
							if (quotaNum.equals("-") || quotaDenom.equals("-")) {
								intG.setQuota("-");
							} else {
								try {
									if (DF.parse(quotaNum).doubleValue() <= 0 || DF.parse(quotaDenom).doubleValue() <= 0) {
										intG.setQuota("-");
									}
								} catch (Exception e) {
									intG.setQuota("-");
								}
							}
							
							intG.setAffidabilita(rs.getObject("AFFIDABILITA") == null ? "-" : rs.getString("AFFIDABILITA"));
							
							String titolo = rs.getString("TITOLO");
							intG.setTitolo(titolo!=null ? titolo : "PROPRIETARIO");
							intG.setDataFine(rs.getString("DATA_FINE"));
							vct.add(intG);
						}

						startTime = new Date().getTime();
						
						sql = SQL_SELECT_LISTA_ALTRIG_FABBRICATO;


						int indice2 = 0;
						this.initialize();
						if (listParam.size() > 3 && unimm != null)
						{
							this.setString(++indice2,(String)listParam.get(0));
							this.setString(++indice2,(String)listParam.get(1));
							this.setString(++indice2,(String)listParam.get(2));
							String parametro_i = (String)listParam.get(3);
							//this.setString(++indice2,parametro_i);
							//this.setString(++indice2,parametro_i);
							this.setString(++indice2,unimm);
							String data_f = (String)listParam.get(5);
							String data_i = (String)listParam.get(6);
							this.setString(++indice2,data_i);
							this.setString(++indice2,data_f);
							this.setString(++indice2,data_i);
							//this.setString(++indice2,data_f);
						}
						else
						{
							// IN CASO NON ARRIVI ALCUNA CHIAVE, FORZO A NON TROVARE NULLA
							this.setString(++indice2, "-1");
							this.setString(++indice2, "-1");
							this.setString(++indice2, "-1");
							//this.setString(++indice2, "-1");
							//this.setString(++indice2, "-1");
							this.setString(++indice2, "-1");
							this.setString(++indice2, "31/12/9999");
							this.setString(++indice2, "31/12/9999");
							this.setString(++indice2, "31/12/9999");
							//this.setString(++indice2, "31/12/9999");
							sql += " and 0 = 1 ";
						}
						prepareStatement(sql);
						java.sql.ResultSet rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						
						log.debug("DURATA LETTURA ALTRI INTESTATARI G PER FABBRICATO MS " + (new Date().getTime() - startTime));

						while (rs2.next()){
							IntestatarioG intG = new IntestatarioG();
							intG.setCodIntestatario(rs2.getString("PK_CUAA"));
							intG.setComune(rs2.getString("FK_COMUNE"));
							intG.setDenominazione(rs2.getString("DENOMINAZIONE"));
							intG.setPartitaIva(rs2.getString("PARTITA_IVA"));
							intG.setSede(rs2.getString("SEDE"));
							intG.setDataInizio(rs2.getString("DATA_INIZIO"));
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
							intG.setQuota(quota);	
							intG.setTitolo("ALTRO");
							intG.setDataFine(rs2.getString("DATA_FINE"));
							vct.add(intG);
						}

						ht.put("LISTA_INTESTATARIG",vct);
						
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

	public Hashtable mCaricareListaIntestatariG2(Vector listaPar, IntestatarioGFinder GFinder) throws Exception{
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
				indice ++;prepareStatement(sql);
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
					if (GFinder.getKeyStr().equals("")){
						//if (i ==1)
							sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
					}
					else{
						//controllo provenienza chiamata
						String controllo = GFinder.getKeyStr();
						if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
							String ente = controllo.substring(0,controllo.indexOf("|"));
							ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
							String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
							sql = sql + " and cons_sogg_tab.PK_CUAA in (" +chiavi + ")" ;
						}else
							sql = sql + " and PK_CUAA in (" +GFinder.getKeyStr() + ")" ;
					}
					long limInf, limSup;
					limInf = (GFinder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
					limSup = GFinder.getPaginaAttuale() * RIGHE_PER_PAGINA;

					if (i ==1){
						sql = sql + " order by DENOMINAZIONE, PARTITA_IVA ";
						sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
					}

					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
							IntestatarioG intF = new IntestatarioG();
							intF.setCodIntestatario(rs.getString("PK_CUAA"));
							intF.setComune(rs.getString("FK_COMUNE"));
							intF.setDenominazione(rs.getString("DENOMINAZIONE"));
							intF.setPartitaIva(rs.getString("PARTITA_IVA"));
							intF.setSede(rs.getString("SEDE"));

							vct.add(intF);
						}

					}
					else{
						 if (rs.next()){
							 conteggio = rs.getString("conteggio");
						 }
					}
				}

				ht.put("LISTA_INTESTATARIG",vct);
				GFinder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
				// pagine totali
				GFinder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
				GFinder.setTotaleRecord(conteggione);
				GFinder.setRighePerPagina(RIGHE_PER_PAGINA);

				ht.put("FINDER",GFinder);
				
				/*INIZIO AUDIT*/
				try {
					Object[] arguments = new Object[2];
					arguments[0] = listaPar;
					arguments[1] = GFinder;
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
	
	public String mTrovaIntestatario(String pIva) throws Exception {
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
					
					if (pIva != null && !pIva.equals("")){
						sql= sql + " and codi_piva= ?";
					this.setString(1, pIva);
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

}

