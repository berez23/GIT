/*
 * Created on 10-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.toponomastica.logic;

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscComboObject;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.toponomastica.bean.Civico;
import it.escsolution.escwebgis.toponomastica.bean.CivicoFinder;
import it.escsolution.escwebgis.toponomastica.bean.RiferimentiCatastali;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ToponomasticaCiviciLogic extends EscLogic {

	private String appoggioDataSource;

	public static final String LISTA_SEDIME = ToponomasticaCiviciLogic.class.getName() + "@LISTA_SEDIME";

	public static final String LISTA_CIVICI = ToponomasticaCiviciLogic.class.getName() + "@LISTA_CIVICI";
	public static final String LISTA_RIFERIMENTI_CATASTALI = ToponomasticaCiviciLogic.class.getName() + "@LISTA_RIFERIMENTI_CATASTALI";
	public static final String LISTA_CIVICI_STRADA = ToponomasticaCiviciLogic.class.getName() + "@LISTA_CIVICI_STRADA";
	public static final String FINDER = ToponomasticaCiviciLogic.class.getName() + "@FINDER";
	public static final String CIVICO = ToponomasticaCiviciLogic.class.getName() + "@CIVICO";
	public static final String CIVICIARIO = ToponomasticaCiviciLogic.class.getName() + "@CIVICIARIO";

	public ToponomasticaCiviciLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}


	private final static String SQL_SELECT_LISTA =
		"select " +
		"  * " +
		"from " +
		"  ( " +
		"  select " +
		"    ROWNUM AS N, " +
		"    nvl(UK_CIVICO, ' - ') as UK_CIVICO, " +
		"    nvl(SEDIME, ' - ') as SEDIME, " +
		"    nvl(NOME_VIA, ' - ') as NOME_VIA, " +
		"    nvl(DESCRIZIONE_CIVICO, ' - ') as DESCRIZIONE_CIVICO ," +
		"    pk_sequ_civico, " +
		"      COD_NAZIONALE, CODI_FISC_LUNA " +
		//" 	FOGLIO_CATASTALE, PARTICELLA_CATASTALE"+
		"  from " +
		"    ( " +
		"	 select distinct " +
		"      VC.UK_CIVICO, " +
		"      VC.SEDIME, " +
		"      VC.NOME_VIA, " +
		"      VC.DESCRIZIONE_CIVICO, " +
		"      VC.pk_sequ_civico, " +
		"      VC.COD_NAZIONALE, " +
		"      COM.CODI_FISC_LUNA " +
		//" 		VCU.FOGLIO_CATASTALE, VCU.PARTICELLA_CATASTALE " +
		"    from " +
		"      VISTA_CIVICI VC, SITICOMU COM ,VISTA_CIVICI_UIU VCU" +
		"    where " +
		"      VC.UK_CIVICO is not null " +
		"      and VC.COD_NAZIONALE = COM.COD_NAZIONALE " +
		"      and VC.PK_SEQU_CIVICO = VCU.PK_SEQU_CIVICO (+) "+
		"      and " +
		"      1 = ? " +
		"";

	private final static String SQL_SELECT_DETAIL = "select distinct 	VC.SEDIME, 	VC.NOME_VIA, 	VC.DESCRIZIONE_CIVICO, 	VC.DATA_ATTIVAZIONE, 	FOGLIO_CATASTALE, 	PARTICELLA_CATASTALE,   VC.PK_SEQU_CIVICO,	UNITA_IMMOBILIARE,       VC.COD_NAZIONALE  "+
									"from 	VISTA_CIVICI VC, VISTA_CIVICI_UIU VCU "+
									"where 	VC.UK_CIVICO LIKE  ? "+
									"AND VC.PK_SEQU_CIVICO = VCU.PK_SEQU_CIVICO (+) "+
									"order by 	FOGLIO_CATASTALE, 	PARTICELLA_CATASTALE, 	UNITA_IMMOBILIARE ";
	
	private final static String SQL_SELECT_DETAIL_FROM_CIVICO = 
			"select distinct 	VC.SEDIME, 	VC.NOME_VIA, 	VC.DESCRIZIONE_CIVICO, 	VC.DATA_ATTIVAZIONE, 	FOGLIO_CATASTALE, 	" +
			"PARTICELLA_CATASTALE,   VC.PK_SEQU_CIVICO,	UNITA_IMMOBILIARE,       VC.COD_NAZIONALE  "+
			"from 	VISTA_CIVICI VC, VISTA_CIVICI_UIU VCU "+
			"where 	VC.PK_SEQU_CIVICO =  ? "+
			"AND VC.PK_SEQU_CIVICO = VCU.PK_SEQU_CIVICO (+) "+
			"order by 	FOGLIO_CATASTALE, 	PARTICELLA_CATASTALE, 	UNITA_IMMOBILIARE ";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from (" + SQL_SELECT_LISTA;

    private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	


	/**
	 * @author Giulio Quaresima
	 */
	public Map mCaricareDatiFormRicerca(String utente) throws Exception
	{
		Map result = new HashMap();

		Set listaSedime = new LinkedHashSet();
		result.put(LISTA_SEDIME, listaSedime);
		Set listaComuni = new LinkedHashSet();
		result.put("LISTA_COMUNI", listaComuni);
		listaSedime.add(
				new EscComboObject()
				{
					public String getCode(){
						return "";
					}
					public String getDescrizione(){
						return "Qualsiasi";
					}
				});

		Connection conn = null;
		try
		{
			conn = super.getConnection();
			super.initialize();
			super.prepareStatement("select distinct prefisso from sitidstr, siticomu where sitidstr.cod_nazionale = siticomu.cod_nazionale and siticomu.codi_fisc_luna in (select distinct uk_belfiore from ewg_tab_comuni) and 1 = ? order by prefisso");
			super.setInt(1, 1);
			ResultSet rs = super.executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				final String key = rs.getString(1);
				if (key != null)
					listaSedime.add(
							new EscComboObject()
							{
								public String getCode(){
									return key;
								}
								public String getDescrizione(){
									return key;
								}
							});
			}

			Vector vctComuni = new ComuniLogic(super.envUtente).getListaComuniUtente(utente);
			listaComuni.addAll(vctComuni);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}


		return result;
	}

	public Hashtable mCaricareListaCivici(Vector listaPar, CivicoFinder finder) throws Exception{
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		java.sql.ResultSet rs = null;
		try {
			this.setDatasource(JNDI_SITISPOLETO);
			conn = this.getConnection();
			//sql = SQL_SELECT_COUNT_ALL;
			int indice = 1;
			//this.initialize();
			//this.setInt(indice,1);
			//indice ++;
			//prepareStatement(sql);
			//if (rs.next()){
			//		conteggione = rs.getLong("conteggio");
			//}
			conteggione = 0;

			for (int i=0;i<=1;i++){
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
					sql = sql + "and vc.UK_CIVICO in (" +finder.getKeyStr() + ") " ;
				}


				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				// LISTA
				if (i == 1)
				{
					sql += "order by NOME_VIA, DESCRIZIONE_CIVICO ";
					sql += ")) where N > " + limInf + " and N <=" + limSup;
				}
				// COUNT FILTRATI
				else
					sql += ")))";

				prepareStatement(sql) ;
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						Civico civ = new Civico();
						civ.setChiave(rs.getString(2));
						civ.setSedime(rs.getString(3));
						civ.setStrada(rs.getString(4));
						civ.setDescrCivico(rs.getString(5));
						civ.setPk_sequ_civico(rs.getString(6));
						civ.setCod_nazionale(rs.getString(7));
						//civ.setFoglio(String.valueOf(rs.getString(9)));
						//civ.setParticella(rs.getString(10));
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(civ.getFoglio(), Utils.fillUpZeroInFront(civ.getParticella(),5),civ.getCod_nazionale());
						} catch (Exception e) {
						}
						if (coord!=null) {
							civ.setLatitudine(coord.firstObj);
							civ.setLongitudine(coord.secondObj);
						}
						vct.add(civ);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(LISTA_CIVICI,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER,finder);
			
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
			if (rs != null)
			{
				rs.close();
				rs = null;
			}
			if (conn != null)
			{
				if (!conn.isClosed())
					conn.close();
				conn = null;
			}
		}
	}



	/**
	 * @author Giulio Quaresima
	 */
	public Hashtable mCaricareDettaglioCivici(String chiave, int st) throws Exception
	{
		Hashtable ht = new Hashtable();

		Connection conn = null;
		Connection connDiogene = getConnectionDiogene();
		try
		{

			this.setDatasource(JNDI_SITISPOLETO);
			conn = this.getConnection();
			this.initialize();
			this.setString(1, chiave);
			String sql="";
			
			if (st == 103)
				sql= SQL_SELECT_DETAIL_FROM_CIVICO;
			else
				sql=SQL_SELECT_DETAIL;
				
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Civico civ = new Civico();
			civ.setChiave(chiave);
			ArrayList<RiferimentiCatastali> listaRifCatastali = new ArrayList<RiferimentiCatastali>();
			ht.put(CIVICO, civ);
			ht.put(LISTA_RIFERIMENTI_CATASTALI, listaRifCatastali);
			if (rs.next()) {
				civ.setChiave(chiave);
				civ.setSedime(super.tornaValoreRS(rs, "SEDIME", "VUOTO"));
				civ.setStrada(super.tornaValoreRS(rs, "NOME_VIA"));
				civ.setDescrCivico(super.tornaValoreRS(rs, "DESCRIZIONE_CIVICO"));
				civ.setDataAttivazione(super.tornaValoreRS(rs, "DATA_ATTIVAZIONE"));
				civ.setParticella(super.tornaValoreRS(rs, "PARTICELLA_CATASTALE"));
				civ.setFoglio(super.tornaValoreRS(rs, "FOGLIO_CATASTALE"));

				civ.setPk_sequ_civico(super.tornaValoreRS(rs, "PK_SEQU_CIVICO"));
				/*QueryRunner run = new QueryRunner();
				String sqlCiviciario = getProperty("sql.SELECT_CIVICI_ARIO");
				ArrayList<Object[]> civiciario = (ArrayList<Object[]>) run.query(connDiogene,sqlCiviciario, civ.getPk_sequ_civico(), new ArrayListHandler());
				ht.put(CIVICIARIO, civiciario);
				*/
				civ.setCod_nazionale(super.tornaValoreRS(rs, "COD_NAZIONALE"));
				
				/*GenericTuples.T2<String,String> coord = null;
				try {
					coord = getLatitudeLongitude(civ.getFoglio(), Utils.fillUpZeroInFront(civ.getParticella(),5),civ.getCod_nazionale());
				} catch (Exception e) {
				}
				if (coord!=null) {
					civ.setLatitudine(coord.firstObj);
					civ.setLongitudine(coord.secondObj);
				}
				else{
					civ.setLatitudine("0");
					civ.setLongitudine("0");
				}*/
				/*RiferimentiCatastali rifCat = new RiferimentiCatastali();
				String virgola = "";
				String identificaRottura = null;
				StringBuffer unimm = new StringBuffer();
				do
				{
					if (!(rifCat.getFoglio() + rifCat.getParticella()).equals(identificaRottura) || identificaRottura==null)
					{
						rifCat.setFoglio(rs.getInt("FOGLIO_CATASTALE"));
						rifCat.setParticella(rs.getString("PARTICELLA_CATASTALE"));
						rifCat.setUnimm(new String(unimm));
						identificaRottura = rifCat.getFoglio() + rifCat.getParticella();
						unimm = new StringBuffer();
						virgola = "";
						listaRifCatastali.add(rifCat);				
						rifCat = new RiferimentiCatastali();
					}
					unimm.append(virgola);
					unimm.append(rs.getInt("UNITA_IMMOBILIARE"));
					virgola = ", ";						
				}
				while (rs.next());*/
				//sostituito da:
				int foglioPrec = -1;
				String particellaPrec = "";
				String unimmCorrente = "";
				do {
					if (rs.getObject("FOGLIO_CATASTALE") != null && rs.getObject("PARTICELLA_CATASTALE") != null) {
						int foglio = rs.getInt("FOGLIO_CATASTALE");
						String particella = rs.getString("PARTICELLA_CATASTALE");					
						if (foglioPrec != -1 && !particellaPrec.equals("") & 
							(foglio != foglioPrec || !particella.equals(particellaPrec))) {
							//punto di rottura
							RiferimentiCatastali rifCat = new RiferimentiCatastali();
							rifCat.setFoglio(foglioPrec);
							rifCat.setParticella(particellaPrec);
							rifCat.setUnimm(unimmCorrente);
							
							GenericTuples.T2<String,String> coord = null;
							try {
								coord = getLatitudeLongitude(String.valueOf(rifCat.getFoglio()), Utils.fillUpZeroInFront(rifCat.getParticella(),5),civ.getCod_nazionale());
							} catch (Exception e) {
							}
							if (coord!=null) {
								rifCat.setLatitudine(coord.firstObj);
								rifCat.setLongitudine(coord.secondObj);
							}
							else{
								rifCat.setLatitudine("0");
								rifCat.setLongitudine("0");
							}
							
							listaRifCatastali.add(rifCat);
							unimmCorrente = "";
						}
						foglioPrec = foglio;
						particellaPrec = particella;
						unimmCorrente = unimmCorrente.equals("") ? "" : unimmCorrente + ", ";
						unimmCorrente += rs.getInt("UNITA_IMMOBILIARE");
					}					
				}		
				while (rs.next());
				if (foglioPrec != -1 && !particellaPrec.equals("") & !unimmCorrente.equals("")) {
					//ultimo elemento
					RiferimentiCatastali rifCat = new RiferimentiCatastali();
					rifCat.setFoglio(foglioPrec);
					rifCat.setParticella(particellaPrec);
					rifCat.setUnimm(unimmCorrente);
					
					GenericTuples.T2<String,String> coord = null;
					try {
						coord = getLatitudeLongitude(String.valueOf(rifCat.getFoglio()), Utils.fillUpZeroInFront(rifCat.getParticella(),5),civ.getCod_nazionale());
					} catch (Exception e) {
					}
					if (coord!=null) {
						rifCat.setLatitudine(coord.firstObj);
						rifCat.setLongitudine(coord.secondObj);
					}
					else{
						rifCat.setLatitudine("0");
						rifCat.setLongitudine("0");
					}
					listaRifCatastali.add(rifCat);
				}
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
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
			if (connDiogene != null && !connDiogene.isClosed())
				connDiogene.close();
		}
	}

//////////////////////TEST MB
	/**
	 * @author MB
	 */
	public Hashtable mCaricareListaCiviciStrada(String chiave) throws Exception
	{
		Hashtable ht = new Hashtable();
		String sql = "select c.PK_SEQU_CIVICO,c.COD_NAZIONALE,c.UK_CIVICO,c.DESCRIZIONE_CIVICO,c.PKID_STRA,c.DESCR_VIA," +
					 "c.DATA_ATTIVAZIONE, co.DESCRIZIONE  " +
					 "from vista_civici c, ewg_tab_comuni co, siticomu com " +
					 "where c.PKID_STRA= ? " +
					 "and com.COD_NAZIONALE = c.COD_NAZIONALE " +
					 "and com.CODI_FISC_LUNA = co.UK_BELFIORE " +
					 "order by c.descr_via,c.descrizione_civico";

		Connection conn = null;
		try
		{
			this.setDatasource(JNDI_SITISPOLETO);
			conn = this.getConnection();
			this.initialize();
			this.setString(1, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());


			Vector listaCivStra = new Vector();
			while (rs.next()) {
				Civico civ = new Civico();
				civ.setChiave(rs.getString("UK_CIVICO"));
				civ.setComune(rs.getString("DESCRIZIONE"));
				civ.setCod_nazionale(rs.getString("COD_NAZIONALE"));
				civ.setCodCivico(rs.getString("PK_SEQU_CIVICO"));
				civ.setDescrCivico(rs.getString("DESCRIZIONE_CIVICO"));
				civ.setStrada(rs.getString("DESCR_VIA"));
				civ.setDataAttivazione(rs.getString("DATA_ATTIVAZIONE"));

				listaCivStra.add(civ);
			}

			ht.put(LISTA_CIVICI_STRADA,listaCivStra);
			
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
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

//////////////////////FINE TEST MB

	/*
	public JavaBeanGlobalVar mCaricareDatiGrafici(String chiave) throws Exception
	{
		Connection conn = null;
		try
		{
			this.datasource = JNDI_SITISPOLETO;
			conn = this.getConnection();
			this.initialize();
			String sql = "select Xcentroid, ycentroid, fwidth, fheight " +
			" from (" + VISTA_CIVICI + ") vista_civici" +
			" where vista_civici.PK_SEQU_CIVICO = ?";


			int indice = 1;
			this.setString(indice,chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn);

			JavaBeanGlobalVar beanGlobale = null;
			if (rs.next()){
				beanGlobale = new JavaBeanGlobalVar(this.getEnvUtente());
				beanGlobale.setXCentroid(rs.getDouble("Xcentroid"));
				beanGlobale.setYCentroid(rs.getDouble("Ycentroid"));
				beanGlobale.setFWidth(rs.getDouble("fwidth"));
				beanGlobale.setFHeight(rs.getDouble("fheight"));
			}
			if (conn != null && !conn.isClosed())
				conn.close();
			return beanGlobale;

		}
		catch (Exception e) {
			if (conn != null && !conn.isClosed())
				conn.close();
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	public Hashtable mCaricareListaCiviciPerStrade(String chiave) throws Exception
	{
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		String sql = "";
		String conteggio = "";
		long conteggione = 0;

		Connection conn = null;
		try
		{
			this.datasource = JNDI_SITISPOLETO;
			conn = this.getConnection();
			sql = SQL_SELECT_CIVICI_PER_STRADE;

			int indice = 1;
			this.initialize();
			this.setString(indice,chiave);
			indice ++;

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn);

			while (rs.next())
			{
				Civico civ = new Civico();
				civ.setChiave(rs.getString("CHIAVE"));
				vct.add(civ);
			}
			if (conn != null && !conn.isClosed())
				conn.close();

			ht.put("LISTA_CIVICI",vct);
			return ht;
		}
		catch (Exception e)
		{
			if (conn != null && !conn.isClosed())
				conn.close();
			log.error(e.getMessage(),e);
			throw e;
		}
	}
	*/
}



















