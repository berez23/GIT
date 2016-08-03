/*
 * Created on 7-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import it.escsolution.escwebgis.common.interfacce.InterfacciaObject;
import it.webred.GlobalParameters;
import it.webred.accessi.Accesso;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmFonteComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.common.dto.KeyValueDTO;
import it.webred.ct.support.audit.AuditDBWriter;
import it.webred.utils.GenericTuples;
import it.webred.utils.gis.Coordinates;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class EscLogic extends EnvBase
{

	/*Nomi delle directory in cui si trovano i file, da comporre con la radice associata a ciascun ente*/
	private final String dirPdfDocfa = "docfa_pdf";
	private final String dirPlanimetrieComma340 = "planimetrieComma340";
	private final String dirStoricoConcessioni = "concessioniStorico";
	private final String dirModelloXls = "modelloXlsDocfaReport";
	private final String dirConcessioniVisure = "concessioniVisure";
	private final String dirPlanimetrie = "planimetrie";
	private final String dirCartografiaStorica = "cartografiaStorica";
	private final String dirPregeo = "pregeo";
	
	private String dirPregeoEnte = null;
	private String dirPdfDocfaEnte = null;
	private String dirPlanimetrieComma340Ente = null;
	private String dirStoricoConcessioniEnte = null;
	private String dirModelloXlsEnte = null;
	private String dirConcessioniVisureEnte = null;
	private String dirPlanimetrieEnte = null;
	private String dirCartografiaStoricaEnte = null;
	
	private String statement;

	private java.util.Vector vecParams;

	private PreparedStatement m_statementObject;

	private final static Object TAPPO = new Object();

	protected String sql;

	private String datasource;
	
	protected EnvUtente envUtente;
	
	protected AuditDBWriter auditWriter;

	protected final String JNDI_CATCOSPOLETO = "jdbc/dbIntegrato"; // CATCOSPOLETO
	protected final String JNDI_DIOGENE = "jdbc/Diogene_DS"; // diogene non + usato: sostituito da es.getDataSource()
	protected final String JNDI_DBTOTALE = "jdbc/dbIntegrato"; // dbtotale
	protected final String JNDI_SITISPOLETO = "jdbc/CP"; // SITISPOLETO

	public static String URL_3D_PROSPECTIVE = null;
	
	public int RIGHE_PER_PAGINA = GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA_DEF;

	// VISTE HARD-CODED
	protected final static String VISTA_CIVICI = "select " + "	civ.PKID_CIVI as PK_SEQU_CIVICO, " + "	com.COD_NAZIONALE || LPAD(NVL(civ.PKID_STRA, '00000000'), 8, 0) || LPAD(NVL(civ.CIVICO, '0000000'), 7, 0) || '000000000' as UK_CIVICO, " + "	com.COD_NAZIONALE as COD_NAZIONALE, " + "	com.CODI_FISC_LUNA as CODI_FISC_LUNA, " + "	com.NOME as COMUNE, " + "	'#Località' as LOCALITA, "
			+ "	str.PKID_STRA AS PKID_STRA, " + "	str.PREFISSO || ' ' || str.NOME as DESCR_VIA, " + "	civ.CIVICO as DESCRIZIONE_CIVICO, " + "	'#SERIE' AS SERIE, " + "	'#DESTINAZIONE_USO' AS DESTINAZIONE_USO, " + "	'#TIPO_ACCESSO' AS TIPO_ACCESSO, " + "	'#ACCESSIBILITA' AS ACCESSIBILITA, " + "	uiu.FOGLIO AS FOGLI_CATASTALE, " + "	uiu.PARTICELLA AS PARTICELLA_CATASTALE, "
			+ "	civ.DATA_INIZIO_VAL AS DATA_ATTIVAZIONE, " + "	'#NUM_PROCEDIMENTO' AS NUM_PROCEDIMENTO, " + "	'#EX_CIVICO' AS EX_CIVICO, " + "	'#NOTE' AS NOTE, " + "	'#SEZIONE_CENSIMENTO' AS SEZIONE_CENSIMENTO, " + "	0.0 AS XCENTROID, " + "	0.0 AS YCENTROID, " + "	0.0 AS FWIDTH, " + "	0.0 AS FHEIGHT " + "from " + "	SITICIVI civ, " + "	SITIDSTR str, " + "	SITICOMU com, " + "	SITICIVI_UIU ciuiu, "
			+ "	SITIUIU uiu " + "WHERE " + "	str.COD_NAZIONALE = com.COD_NAZIONALE " + "	AND " + "	civ.PKID_STRA (+) = str.PKID_STRA " + "	AND " + "	ciuiu.PKID_CIVI (+) = civ.PKID_CIVI " + "	AND " + "	uiu.PKID_UIU (+) = ciuiu.PKID_UIU " + "	AND " + "	civ.DATA_FINE_VAL = TO_DATE('99991231', 'YYYYMMDD') ";

	protected final static String TOP_STRADE = "SELECT " + "	com.COD_NAZIONALE as COD_NAZIONALE, com.CODI_FISC_LUNA as CODI_FISC_LUNA, " + "	str.PREFISSO AS SEDIME, " + "	str.NOME AS NOME_VIA, " + "	str.NOME AS ORDINAMENTO, " + "	com.CODI_FISC_LUNA AS FK_COMUNI_BELF, " + "	str.PKID_STRA AS UK_STRADA, " + "	str.NUMERO AS NUMERO " + "FROM " + "	SITIDSTR str, " + "	SITICOMU com " + "WHERE " + "	com.COD_NAZIONALE = str.COD_NAZIONALE "
			+ "	AND " + "	str.DATA_FINE_VAL = TO_DATE('99991231', 'YYYYMMDD') ";

	protected static final String CAT_PARTICELLE_GAUSS = "SELECT shape AS geometry, area_part AS area, UPPER(cod_nazionale) AS comune, " + "       DECODE (particella, " + "               'STRAD', 'STRADA', " + "               'ACQUA', 'ACQUA', " + "               'PARTICELLE' " + "              ) AS layer, " + "       particella, 0 AS perimeter, "
			+ "       'javascript:aprischeda(''/escwebgis/CatastoGauss?DATASOURCE=jdbc/dbIntegrato'||chr(38)||'ST=3'||chr(38)||'UC=12'||chr(38)||'OGGETTO_SEL=' || se_row_id || ''')' AS url, " + "       UPPER(cod_nazionale) AS fk_comune, " + "          cod_nazionale " + "       || LPAD (foglio, 5, '0') " + "       || particella "
			+ "       || LPAD (NVL (TRIM (sub), '0000'), 4, '0') AS fk_par_catastali, " + "       se_row_id AS pk_particelle, NULL AS label_posizione, " + "       NULL AS label_altezza, NULL AS label_rotazione, NULL AS centroide, " + "       NULL AS mapsheet, NULL AS label_x, NULL AS label_y, foglio,sub,data_fine_val,data_inizio_val " + "  FROM sitipart " //+ " WHERE data_fine_val = TO_DATE ('99991231', 'YYYYMMDD') "
			+ "UNION ALL " + "SELECT shape AS geometry, area_colt AS area, UPPER(cod_nazionale) AS comune, " + "       'FABBRICATI' AS layer, particella, 0 AS perimeter, " + "       'javascript:aprischeda(''/escwebgis/CatastoGauss?DATASOURCE=jdbc/dbIntegrato'||chr(38)||'ST=3'||chr(38)||'UC=12'||chr(38)||'OGGETTO_SEL=' || se_row_id || ''')' AS url, " + "       UPPER(cod_nazionale) AS fk_comune, "
			+ "          cod_nazionale " + "       || LPAD (foglio, 5, '0') " + "       || particella " + "       || LPAD (NVL (TRIM (sub), '0000'), 4, '0') AS fk_par_catastali, " + "       se_row_id AS pk_particelle, NULL AS label_posizione, " + "       NULL AS label_altezza, NULL AS label_rotazione, NULL AS centroide, " + "       NULL AS mapsheet, NULL AS label_x, NULL AS label_y, foglio,sub,data_fine_val,data_inizio_val "
			+ "  FROM sitisuolo " + " WHERE "//data_fine_val = TO_DATE ('99991231', 'YYYYMMDD') " 
			//+ "   AND "
			+" (cod_util = 100 OR (cod_util = 5 AND cod_coltura = 660)) ";

	protected static final String CONS_PROP_TOT = "SELECT cod_nazionale, foglio, particella,pk_cuaa,sub,data_inizio,perc_poss,pkid_cons as PK_PROP " + "  FROM cons_cons_tab " + "  WHERE data_fine = TO_DATE ('99991231', 'YYYYMMDD') " + "UNION ALL " + "SELECT cod_nazionale, foglio, particella,pk_cuaa,sub,data_inizio,perc_poss,pkid_csui as PK_PROP " + "  FROM cons_csui_tab "
			+ "  WHERE data_fine = TO_DATE ('99991231', 'YYYYMMDD') ";

	protected static final String CONS_SOGG_TOT = "SELECT " + "A.PK_CUAA,A.CUAA, " + "B.CODI_FISC, B.CODI_PIVA, B.RAGI_SOCI, B.NOME, " + "B.SESSO, B.DATA_NASC, B.DATA_MORTE, B.COMU_NASC, " + "B.RESI_VIA, B.RESI_COMU, " + "B.NATURA_GIURIDICA, B.FLAG_PERS_FISICA, " + "B.CODI_FISC_RAPP " + "FROM CONS_SOGG_TAB B,CONS_AZIE_TAB A " + "WHERE " + "A.PK_CUAA=B.PK_CUAA AND "
			+ "B.DATA_FINE=TO_DATE('99991231', 'YYYYMMDD') ";

	private static final String sqlLatLong = "SELECT DISTINCT t.y lat, t.x lon" 
    +"    FROM sitipart_3d p, siticomu c, "
    +"    TABLE (sdo_util.getvertices (sdo_cs.transform (p.shape_pp, "
    +"                                                  MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 1313328, 2820083, 0.0050),MDSYS.SDO_DIM_ELEMENT('Y', 3930191, 5220322.5, 0.0050)), "
    +"                                                 8307 "
    +"                                                ) "
    +"                              ) "
    +"        ) t "
    +"    WHERE (C.COD_NAZIONALE = ? OR C.CODI_FISC_LUNA= ? )" 
    +"    AND P.COD_NAZIONALE = C.CODI_FISC_LUNA " 
    +"    AND P.FOGLIO = ? "
    +"    AND P.PARTICELLA = ?";
	
	
	private static final String sqlLatLongFromXY = "select t.y lat, t.x lon from  TABLE (sdo_util.getvertices (sdo_cs.transform ( mdsys.sdo_geometry(2001, ?, mdsys.SDO_POINT_TYPE (?, ?, NULL), NULL, NULL) ,  " 
          +"  MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 1313328, 2820083, 0.0050),MDSYS.SDO_DIM_ELEMENT('Y', 3930191, 5220322.5, 0.0050)), "
          +"  8307  " 
          +" ) " 
          +") " 
          +") t"; 
	

	/**
	 * 
	 */
	public EscLogic(EnvUtente eu)
	{
		super();
		this.datasource = eu.getDataSource();
		envUtente = eu;
		auditWriter = new AuditDBWriter();
		if (URL_3D_PROSPECTIVE==null)
			URL_3D_PROSPECTIVE = getProperty("URL_3D_PROSPECTIVE", it.escsolution.escwebgis.common.EscLogic.class);
		if (GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA.get(eu.getEnte()) != null) {
			RIGHE_PER_PAGINA = (Integer)GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA.get(eu.getEnte()).intValue();
		}
	}

	protected Connection getConnectionDiogene() throws NamingException, SQLException
	{
		Context cont = new InitialContext();
		EnvSource es = new EnvSource(this.getEnvUtente().getEnte());
		DataSource theDataSource = (DataSource)cont.lookup(es.getDataSource());

		return theDataSource.getConnection();
	}
	
	protected Connection getConnection() throws NamingException, SQLException
	{
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		if (this.datasource.equals(""))
		{
			EnvSource es = new EnvSource(this.getEnvUtente().getEnte());
			theDataSource = (DataSource)cont.lookup(es.getDataSourceIntegrato());
		}
		else{
			EnvSource es = new EnvSource(this.getEnvUtente().getEnte());
			theDataSource = (DataSource)cont.lookup(es.getDataSource());
		}

		return theDataSource.getConnection();
	}

	protected Connection getDefaultConnection() throws NamingException, SQLException
	{
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(this.getEnvUtente().getEnte());
		theDataSource = (DataSource)cont.lookup(es.getDataSource());
		
		return theDataSource.getConnection();
	}	
	
	protected void setDate(int parameterIndex, java.util.Date x) throws Exception
	{
		addParameterToVector(parameterIndex, x);
	}
	
	/**
	 * Restituisce l'indice corrente per l'inserimento di un parametro
	 * del preparedStatement con <code>setInt</code> o <code>setString</code>
	 * o <code>setNumber</code> 
	 */ 
	protected int getCurrentParameterIndex() throws NullPointerException
	{
		if (vecParams == null)
			throw new NullPointerException("Richiamare prima initialize()!");
		return vecParams.size() + 1;
	}
	protected void setInt(int parameterIndex, int x) throws Exception
	{
		addParameterToVector(parameterIndex, new Integer(x));
	}

	protected void setNull(int parameterIndex) throws Exception
	{
		addParameterToVector(parameterIndex, null);
	}
	
	protected void setDouble(int parameterIndex, double x) throws Exception
	{
		addParameterToVector(parameterIndex, new Double(x));
	}
	
	/**
	 * Imposta un oggetto java.lang.String nel Vector contenente i parametri
	 * dello statement. Data di creazione: (31/05/2001 12.25.48)
	 * 
	 * @param int
	 *            parameterIndex Il primo parametro  1, il secondo  2, ...
	 * @param java.lang.String
	 *            x Il valore del parametro
	 */
	protected void setString(int parameterIndex, String x) throws Exception
	{
		addParameterToVector(parameterIndex, x);
	}

	protected void setNumber(int parameterIndex, Number n) throws Exception
	{
		addParameterToVector(parameterIndex, n);
	}

	/**
	 * Verifica che sia stato impostato lo statement SQL da eseguire attraverso
	 * l'invocazione del metodo <code>prepareStatement</code>, e che siano
	 * stati impostati tutti i parametri necessari attraverso l'invocazione dei
	 * metodi <code>setXXX</code>. Data di creazione: (31/05/2001 14.00.34)
	 * 
	 * @param index
	 *            int
	 */
	private void validateParameters() throws Exception
	{

		if (statement == null)
		{
			throw new Exception("Prima di eseguire la query occorre invocare il metodo prepareStatement(String) e successivamente impostare tutti i parametri necessari invocando i metodi setXXX().");
		}

		if (vecParams == null)
		{
			throw new Exception("Prima di eseguire la query occorre invocare il metodo prepareStatement(String) e successivamente impostare tutti i parametri necessari invocando i metodi setXXX().");
		}

		java.util.Enumeration e = vecParams.elements();

		Object param = null;
		int i = 1;

		log.debug("Verifica dello statement prima della sua esecuzione:");
		log.debug("Statement: " + statement);
		log.debug("Parametri ricevuti: ");
		while (e.hasMoreElements())
		{
			param = e.nextElement();
			log.debug("Parametro " + (i++) + ": " + param);
			if (TAPPO.equals(param))
			{
				throw new Exception("Non sono stati forniti tutti i parametri per eseguire lo statement.");
			}
		}

	}

	private void addParameterToVector(int index, Object x) throws Exception
	{

		if (index <= 0)
		{
			throw new Exception("E' stato fornito un valore non valido per il parametro parameterIndex al metodo MerTeCAccessBean.setXXX(): " + index);
		}

		index--;

		try
		{
			vecParams.setElementAt(x, index);
		}
		catch (ArrayIndexOutOfBoundsException ex)
		{

			while (vecParams.size() < index)
			{
				vecParams.addElement(TAPPO);
			}
			vecParams.addElement(x);
		}

	}

	protected void initialize(){
		vecParams = new java.util.Vector();
	}

	protected ResultSet executeQuery(Connection conn, String nomeClass, EnvUtente envUtente) throws Exception, java.sql.SQLException
	{
		/*
		 * try { CallableStatement cs = conn.prepareCall("ALTER SESSION SET
		 * OPTIMIZER_MODE=RULE"); cs.execute(); } catch (Exception e) {
		 */
		try {
			validateParameters();
			ResultSet rs = executeQuery(statement, vecParams, conn);
			String logString = statement;
			
			try {
				java.util.Enumeration e = vecParams.elements();
				Object param = null;
				String listaParametri = "|";
				int i = 1;
				while (e.hasMoreElements())
				{
					param = e.nextElement();
					listaParametri += param + "|";
					
					if (TAPPO.equals(param))
					{
						Accesso.registraLog("Non sono stati forniti tutti i parametri per eseguire lo statement", envUtente, nomeClass, "SQL",null);
						throw new Exception("Non sono stati forniti tutti i parametri per eseguire lo statement");
					}
				}
				Accesso.registraLog(statement, envUtente, nomeClass,"SQL",listaParametri);
	
			} catch (Exception e) {
				log.error("ERRORE nella registrazione del log");
			}
			clearStatement();
			return rs;

		} catch (Exception e) {
			log.debug(sql);
			log.error("Errore esecuzione query",e);
			throw new DiogeneException(e);
		}
		
	}

	private void clearStatement()
	{
		statement = null;
		vecParams.removeAllElements();
		vecParams = null;
	}

	protected ResultSet executeQuery(String sql, Vector params, Connection conn) throws SQLException, Exception
	{
		log.debug("Esecuzione statement:" +sql);
		sql = _prepareSQL(sql, params, conn);
		ResultSet rs;
		if (params.size() > 0)
		{
			rs = m_statementObject.executeQuery();
		}
		else
			throw new Exception("TIPO DI QUERY SENZA PARAMETRI NON GESTITA");
		return rs;
	}

	private final String _prepareSQL(String sql, Vector params, Connection conn) throws SQLException, Exception
	{
		if (sql == null || sql.length() == 0)
			throw new Exception("Impossibile eseguire lo statement, statement non definito.");
		sql = sql.trim();
		log.debug("esecuzione dello statement, " + sql);
		int index = -1;
		int count = 0;
		do
		{
			index = sql.indexOf("?", index + 1);
			if (index == -1)
				break;
			count++;
		}
		while (true);
		if (count != params.size())
			throw new Exception("Impossibile eseguire lo statement, il numero dei parametri dichiarati non corrisponde al numero dei parametri valorizzati.");
		log.debug("verifica dei parametri dello statement eseguita");
		sql = unquoteQuestionMArk(sql);
		count = 0;
		Statement stmt;
		if (params.size() == 0)
			stmt = conn.createStatement();
		else
			stmt = conn.prepareStatement(sql);
		log.debug("preparazione dello statement eseguita");
		if (stmt instanceof PreparedStatement)
		{
			PreparedStatement pstm = (PreparedStatement) stmt;
			for (Enumeration enm_params = params.elements(); enm_params.hasMoreElements();)
			{
				Object obj = enm_params.nextElement();
				if (obj == null)
					pstm.setNull(++count, 1);
				else if (obj instanceof String)
					pstm.setString(++count, (String) obj);
				else if (obj instanceof Number)
					pstm.setDouble(++count, ((Number) obj).doubleValue());
				else if (obj instanceof Date)
					pstm.setDate(++count, (Date) obj);
				else if (obj instanceof Timestamp)
					pstm.setTimestamp(++count, (Timestamp) obj);
				else if (obj instanceof java.util.Date)
					pstm.setTimestamp(++count, new Timestamp(((java.util.Date) obj).getTime()));
				else if (obj instanceof byte[])
					pstm.setBytes(++count, (byte[]) obj);
				else if (obj instanceof ByteArrayInputStream)
					pstm.setBinaryStream(++count, (ByteArrayInputStream) obj, ((ByteArrayInputStream) obj).available());
				else
					pstm.setObject(++count, obj);
			}

			log.debug("valorizzazione parametri dello statement eseguita");
			m_statementObject = pstm;
		}
		// m_statementObject = pstm;//conn.prepareStatement(sql);
		// m_statementStack.push(m_statementObject);
		return sql;
	}

	protected void prepareStatement(String sql)
	{
		statement = sql;
	}

	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception
	{
		if (listaPar == null)
			return sql;
		Enumeration en = listaPar.elements();
		while (en.hasMoreElements())
		{
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if (filtro.getValore()!=null && !filtro.getValore().trim().equals("") )
			{	
				if( filtro.getTipo().equals("O"))
					continue;
				if (filtro.getTipo().equals("N") || filtro.getTipo().equals("S") || filtro.getTipo().equals("F") )
					if (filtro.getOperatore().toLowerCase().trim().equals("like"))
					{
						if (filtro.getTipo().equals("S")){
							sql = sql + " and UPPER(" + filtro.getCampoFiltrato().toUpperCase() + ") LIKE '%'||?||'%'";
						}else{
							sql = sql + " and " + filtro.getCampoFiltrato().toUpperCase() + " LIKE '%'||?||'%'";
						}
						
					}
					else if (filtro.getOperatore().toLowerCase().trim().equals("<>"))
					{
						sql = sql + " and (" + filtro.getCampoFiltrato().toUpperCase() + " IS NULL OR " + filtro.getCampoFiltrato().toUpperCase() + " " + filtro.getOperatore() + " ? )";
					}
					else if (filtro.getOperatore().toLowerCase().trim().equals("null"))
					{
						sql = sql + " and (" + filtro.getCampoFiltrato().toUpperCase() + " IS NULL and  1= ? )";
					}
					else if (filtro.getOperatore().trim().equals("notNull"))
					{
						sql = sql + " and (" + filtro.getCampoFiltrato().toUpperCase() + " IS not NULL and  1= ? )";
					}
					else
					{
					/*	if (filtro.getTipo().equals("S")){
							sql = sql + " and UPPER(" + filtro.getCampoFiltrato().toUpperCase() + ") " + filtro.getOperatore() + " ? ";
						}else{*/
							sql = sql + " and (" + filtro.getCampoFiltrato().toUpperCase() + " " + filtro.getOperatore() + " ? )";
						//}
					}
				else if (filtro.getTipo().equals("NN"))
				{
					// null or not null
					sql = sql + " and " + filtro.getCampoFiltrato().toUpperCase() + "  ";
				}
				else
				{
					// date
					sql = sql + " and " + filtro.getCampoFiltrato().toUpperCase() + " " + filtro.getOperatore();
				}
				if (filtro.getTipo().equals("N"))
					this.setInt(indice, new Integer(filtro.getValore()).intValue());
				else if (filtro.getTipo().equals("F"))
					this.setNumber(indice, new Double(filtro.getValore()));
				else if (filtro.getTipo().equals("S"))
					this.setString(indice, filtro.getValore().toUpperCase());
				else if (filtro.getTipo().equals("NN")){
					sql = sql + filtro.getValore().toUpperCase()+ "  and 1 = ? ";
					this.setInt(indice, 1);
				}
				else
				{
					sql = sql + " to_date(?,'dd/mm/yyyy') ";
					this.setString(indice, filtro.getValore().toUpperCase());
				}
				indice++;
			}
		}
		return sql;
	}
	
	
	protected EscElementoFiltro getParametroRicerca(Vector listaPar, String nomeParam){
		EscElementoFiltro  elemento = null;
		Enumeration en = listaPar.elements();
		while (en.hasMoreElements())
		{
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if(filtro.getAttributeName().equals(nomeParam))
				elemento = filtro;
			
		}
		
		return elemento;
	}
	
	protected Vector aggiornaValoreParametro(Vector listaPar, String nomeParam, EscElementoFiltro param){
		Vector lst = new Vector(listaPar.size());
		Enumeration en = listaPar.elements();
		while (en.hasMoreElements())
		{
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if(!filtro.getAttributeName().equals(nomeParam)){
				lst.add(filtro);
			}else{
				lst.add(param);
			}
				
		}
		return lst;
	}
	
	
	/*
	  identico a elaboraFiltroMascheraRicerca, con la differenza che i parametri di ricerca non vengono direttamente
	 aggiunti a sql ma vanno a costituire una stringa a parte
	 */
	protected String elaboraFiltroMascheraRicercaParziale(int indice, Vector listaPar) throws NumberFormatException, Exception
	{
		String sqlAdd = "";
		if (listaPar == null)
			return sqlAdd;
		Enumeration en = listaPar.elements();
		while (en.hasMoreElements())
		{
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if (filtro.getValore()!=null && !filtro.getValore().trim().equals(""))
			{	
				if( filtro.getTipo().equals("O"))
					continue;
				if (filtro.getTipo().equals("N") || filtro.getTipo().equals("S") || filtro.getTipo().equals("F") )
					if (filtro.getOperatore().toLowerCase().trim().equals("like"))
					{
						if (filtro.getTipo().equals("S")){
							sqlAdd += " and UPPER(" + filtro.getCampoFiltrato().toUpperCase() + ") LIKE '%'||?||'%'";
						}else{
							sqlAdd += " and " + filtro.getCampoFiltrato().toUpperCase() + " LIKE '%'||?||'%'";
						}
						
					}
					else if (filtro.getOperatore().toLowerCase().trim().equals("<>"))
					{
						sqlAdd += " and (" + filtro.getCampoFiltrato().toUpperCase() + " IS NULL OR " + filtro.getCampoFiltrato().toUpperCase() + " " + filtro.getOperatore() + " ? )";
					}
					else if (filtro.getOperatore().toLowerCase().trim().equals("null"))
					{
						sqlAdd += " and (" + filtro.getCampoFiltrato().toUpperCase() + " IS NULL and  1= ? )";
					}
					else if (filtro.getOperatore().trim().equals("notNull"))
					{
						sqlAdd += " and (" + filtro.getCampoFiltrato().toUpperCase() + " IS not NULL and  1= ? )";
					}
					else
					{
						sqlAdd += " and (" + filtro.getCampoFiltrato().toUpperCase() + " " + filtro.getOperatore() + " ? )";
					}
				else if (filtro.getTipo().equals("NN"))
				{
					// null or not null
					sqlAdd += " and " + filtro.getCampoFiltrato().toUpperCase() + "  ";
				}
				else
				{
					// date
					sqlAdd += " and " + filtro.getCampoFiltrato().toUpperCase() + " " + filtro.getOperatore();
				}
				if (filtro.getTipo().equals("N"))
					this.setInt(indice, new Integer(filtro.getValore()).intValue());
				else if (filtro.getTipo().equals("F"))
					this.setNumber(indice, new Double(filtro.getValore()));
				else if (filtro.getTipo().equals("S"))
					this.setString(indice, filtro.getValore().toUpperCase());
				else if (filtro.getTipo().equals("NN")){
					sqlAdd += filtro.getValore().toUpperCase()+ "  and 1 = ? ";
					this.setInt(indice, 1);
				}
				else
				{
					sqlAdd += " to_date(?,'dd/mm/yyyy') ";
					this.setString(indice, filtro.getValore().toUpperCase());
				}
				indice++;
			}
		}
		return sqlAdd;
	}

	public Vector leggiVettoreCross(String tema, String tablename) throws Exception
	{
		String sql = "select 'select ' || ewg_collegamenti.EXT_KEYCOLUMN || ' from ' || ewg_collegamenti.EXT_TABLENAME || ',' || ewg_classe.TABLENAME " + " || ' where ' || ewg_collegamenti.EXT_JOIN || ' and ' || ewg_classe.TABLENAME || '.' ||ewg_classe.KEYCOLUMN  as SQL, " + " ewg_collegamenti.url as URL, " + " ewg_collegamenti.EXT_NROW, ewg_collegamenti.DESCRIZIONE "
				+ " from ewg_classe, ewg_tema, ewg_collegamenti " + " where ewg_tema.PK_TEMA = ewg_classe.FK_TEMA " + " and ewg_collegamenti.FK_CLASSE = ewg_classe.PK_CLASSE" + " and ewg_tema.nome = ?" + " and tablename = ? "
				+ "ORDER BY ewg_collegamenti.ORDINAMENTO";

		Connection conn = this.getConnection();
		int indice = 1;
		this.initialize();
		this.setString(indice, tema);
		indice++;
		this.setString(indice, tablename);
		prepareStatement(sql);

		java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		Vector rVect = new Vector();
		while (rs.next())
		{
			InterfacciaObject interfaccia = new InterfacciaObject();
			String url = "SERVLET=" + rs.getString("URL");
			url = url + "&QUERY=" + rs.getString("sql") + "&NROW=" + rs.getString("EXT_NROW");
			// url = url.substring(1,30);
			interfaccia.setLink(URLEncoder.encode(url, "US-ASCII"));
			interfaccia.setDescrizione(rs.getString("DESCRIZIONE"));
			rVect.add(interfaccia);
		}
		conn.close();
		return rVect;
	}

	protected static String quoteQuestionMark(String str)
	{
		return str.replaceAll("\\?", "§");
	}

	
	protected static String unquoteQuestionMArk(String str)
	{
		return str.replaceAll("§", "?");
	}

	protected static String tornaValoreRS(ResultSet rs, String colonna) throws Exception
	{
		return tornaValoreRS(rs, colonna, null);
	}

	protected static String tornaValoreRS(ResultSet rs, String colonna, String tipo) throws Exception
	{
		try
		{
			String s = null;
			s = rs.getString(colonna);

			if (s == null && tipo != null)
				if (tipo.equals("VUOTO"))
					s = "";
			if (s == null)
				return s = "-";

			if (tipo != null)
				if (tipo.equals("NUM"))
				{
					s = new Integer(s).toString();
				}
				else if (tipo.equals("DOUBLE"))
				{
					s = new Double(s).toString();
				}
				else if(tipo.equals("EURO"))
				{
					NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
					nf.setMinimumFractionDigits(2);
					nf.setMaximumFractionDigits(2);
					s = nf.format(new Double(s));
				}
				else if (tipo.equalsIgnoreCase("YYMMDD"))
				{
					s = s.substring(4) + "/" + s.substring(2, 4) + "/" + s.substring(0, 2);
				}
				else if (tipo.equalsIgnoreCase("YYYY/MM/DD"))
				{
					s = s.substring(8) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
				}
				else if (tipo.equalsIgnoreCase("YYYY-MM-DD"))
				{
					s = s.substring(8,10) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
				}
				else if (tipo.equalsIgnoreCase("DDMMYYYY"))
				{
					s = s.substring(0, 2) + "/" + s.substring(2, 4) + "/" + s.substring(4, 8);
				}
				else if (tipo.equalsIgnoreCase("YYYYMMDD"))
				{
					s = s.substring(6, 8) + "/" + s.substring(4, 6) + "/" + s.substring(0, 4);
				}			
				else if (tipo.equalsIgnoreCase("FLAG"))
				{
					if (s.equals("0"))
						s = "NO";
					else
						s = "SI";
				}
			return s;
		}
		catch (Exception e)
		{
			return "-";
		}
	}
	
	protected String getProperty(String propName, Class c) {
		String fileName =  c.getName()+".properties";
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        Properties props = new Properties();        
        try {
        	props.load(is);
        } catch (Exception e) {
        	log.error("errore recupero proprietà:" +propName);
			return null;
		}
        
        String p = props.getProperty(propName);
        return p;
	}
	    
	protected String getProperty(String propName) {
        String fileName =  this.getClass().getName()+".properties";
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        Properties props = new Properties();        
        try {
        	props.load(is);
        } catch (Exception e) {
        	log.error("errore recupero proprietà:" +propName);
			return null;
		}
        
        String p = props.getProperty(propName);
        return p;
	}
	
	public EnvUtente getEnvUtente() {
		return envUtente;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	
	
	
	public GenericTuples.T2<String, String> getLatitudeLongitudeFromXY(String x, String y) throws Exception {
		Connection conn = null;
		java.sql.ResultSet rs = null;
		HashMap ht = new LinkedHashMap();
		if (x!=null && y!=null){
		
		try {
			log.info("getLatitudeLongitudeFromXY - SQL["+sqlLatLongFromXY+"]" );
			Double xd = new Double(x);
			log.info("X:" + x);
			Double yd = new Double(y);
			log.info("Y:" + y);
			Double sysCoordinate = new Double(Coordinates.getCoordSystem(xd,yd));
			log.info("Sistema di Coordinate:" + sysCoordinate);
			
			conn = this.getConnection();
			this.initialize();
			this.setDouble(1,sysCoordinate);
			this.setDouble(2,xd);
			this.setDouble(3,yd);
			
			prepareStatement(sqlLatLongFromXY);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next()) {
				return new GenericTuples.T2<String, String>(rs.getString(1), rs.getString(2));
			}
			return new GenericTuples.T2<String, String>("0","0");
		} catch (Exception e) {
			throw e;
		} 
		finally
		{
			if (rs !=null)
				rs.close();
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
		}
		return new GenericTuples.T2<String, String>("0","0");
	}
	
	public GenericTuples.T2<String, String> getLatitudeLongitude(String foglio, String particella, String codEnte) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		HashMap ht = new LinkedHashMap();
		if (foglio!= null && !foglio.equals("-") && particella!= null && !particella.equals("0000-") &&  !foglio.trim().equals("") && particella!= null && !particella.equals("    0")){
		
		try {
			conn = this.getConnection();
			this.initialize();
			this.setString(1,codEnte);
			this.setString(2,codEnte);
			this.setString(3,foglio);
			this.setString(4,particella);
	
			prepareStatement(sqlLatLong);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next()) {
				return new GenericTuples.T2<String, String>(rs.getString(1), rs.getString(2));
			}
			return new GenericTuples.T2<String, String>("0","0");
		} catch (Exception e) {
			throw e;
		} 
		finally
		{
			if (rs !=null)
				rs.close();
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
		}
		return new GenericTuples.T2<String, String>("0","0");
	}
	
	public HashMap caricaIdStorici(String oggettoSel) throws Exception {
		Connection conn = null;
		java.sql.ResultSet rs = null;
		HashMap ht = new LinkedHashMap();
		try {
			conn = this.getConnection();
			this.initialize();
			String sql = getProperty("sql.SELECT_ID_STORICI");
			this.setString(1,oggettoSel);
	
			log.debug("caricaIsStorici SQL["+sql+"]"); 
			log.debug("caricaIsStorici oggettoSel["+oggettoSel+"]"); 		
			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()) {
				String dtIni = it.webred.utils.DateFormat.dateToString(rs.getDate("DT_INIZIO_VAL"), "dd/MM/yyyy");
				String dtFine = it.webred.utils.DateFormat.dateToString(rs.getDate("DT_FINE_VAL"), "dd/MM/yyyy");
				ht.put(rs.getString("ID"), dtIni + " - " + dtFine);
				
				log.debug("Storico: ["+rs.getString("ID") + " " +dtIni + " "+ dtFine +"]"); 		
			}
			return ht;
		} catch (Exception e) {
			throw e;
		} 
		finally
		{
			DbUtils.close(conn);
		}
	}

	private List<KeyValueDTO> cercaIndirizzi(String s, String tablename, String field)
	{
		List ris = new ArrayList();
		if(s == null)
			return ris;
		s = s.toUpperCase().trim();
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = this.getConnection();
			String sql = "select distinct " + field + " from " + tablename +" v where   "+field+" LIKE '%' || ? || '%' ";
			if (hasTableDtFineVal(conn, tablename)) {
				sql += " AND V.DT_FINE_VAL IS NULL ";
			}
			sql	+= " ORDER BY " + field;
			this.initialize();
			
			//log.debug("SQL EscLogic - cercaIndirizzi["+sql+"]");
			
			this.setString(1, s);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				String res = rs.getString(1);		
				KeyValueDTO result = new KeyValueDTO(res,res);
				ris.add(result);
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
		}
		finally
		{
			if (rs != null)
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}			
			if (conn != null)
				try
				{
					if (!conn.isClosed())
						conn.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}
			
		}
		return ris;
		
	}
	
	
	public List<KeyValueDTO> cercaIndirizzi(IndirizzoBean params, String tablename)
	{
		List ris = new ArrayList();
		
		if(params==null)
			return ris;
		
		String colIndirizzo = params.getColIndirizzo();
		String colSedime = params.getColSedime();
		
		String indirizzo = params.getIndirizzo();
		String sedime = params.getSedime();
		
		if(colIndirizzo==null || indirizzo==null)
			return ris;
		
		if(colSedime!=null && sedime!=null && !sedime.equalsIgnoreCase("")){
			
			//TODO:Ricerca con sedime
			indirizzo = indirizzo.toUpperCase().trim();
			
			Connection conn = null;
			ResultSet rs = null;
			try
			{
				conn = this.getConnection();
				String sql = "select distinct (" + colSedime + "||' '||" + colIndirizzo +") AS DESCR, " +colSedime +","+ colIndirizzo + 
						" from " + tablename +" v " +
						" where " + colIndirizzo + " LIKE '%' || ? || '%' ";
						if(sedime!=null && !sedime.equalsIgnoreCase("")){
							sedime = sedime.toUpperCase().trim();
							sql += " AND "+ colSedime + " = '"+sedime+"' ";
						}
						if (hasTableDtFineVal(conn, tablename)) {
							sql += " AND V.DT_FINE_VAL IS NULL ";
						}
						sql += " ORDER BY (" + colSedime + "||" + colIndirizzo +") ";
				this.initialize();
				
				//log.debug("SQL EscLogic - cercaIndirizziConSedime["+sql+"]");

				prepareStatement(sql);
				this.setString(1, indirizzo);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next())
				{
					KeyValueDTO result = new KeyValueDTO(rs.getString(colIndirizzo), rs.getString("DESCR").trim());
					ris.add(result);
				}
			}
			catch(Exception e)
			{
				log.error(e.getMessage(),e);
			}
			finally
			{
				if (rs != null)
					try
					{
						rs.close();
					}
					catch (SQLException e)
					{
						log.error(e.getMessage(),e);
					}			
				if (conn != null)
					try
					{
						if (!conn.isClosed())
							conn.close();
					}
					catch (SQLException e)
					{
						log.error(e.getMessage(),e);
					}
				
			}
			
		}else{
			 ris = this.cercaIndirizzi(indirizzo, tablename, colIndirizzo);
		}
		

		return ris;
		
	}
	
	private boolean hasTableDtFineVal(Connection conn, String tablename) {
		boolean retVal = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT 1 FROM " + tablename + " WHERE DT_FINE_VAL IS NULL";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {}
			retVal = true;
		} catch (Exception e) {}
		finally {
			if (rs != null) {
				try {
					rs.close();
				}
				catch (SQLException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				}
				catch (SQLException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return retVal;
	}
	
	public  String round(String val, int places) 
    {
    	return round(new Double(val.replaceAll("[,]", ".")).doubleValue(),places);
    }
    public  String round(double val, int places) 
    {
    	if(new Double(val).isInfinite())
    		return new Double(val).toString();
    	long factor = (long)Math.pow(10,places);

    	// Shift the decimal the correct number of places
    	// to the right.
    	val = val * factor;

    	// Round to the nearest integer.
    	long tmp = Math.round(val);

    	// Shift the decimal the correct number of places
    	// back to the left.
    	String s =  new Double((double)tmp / factor).toString().replace('.', ',');
    
    	if(s.indexOf(",")>0)
    	{
    		if(s.indexOf(",") == s.length()-2)
    			s+="0";
    	}
    	else
    	{
    		s+=",00";
    	}
    	return s;
    }
    protected String elaboraOrderByMascheraRicerca(Vector listaPar) 
    	throws NumberFormatException, Exception	{
    	String sqlAdd = "";
		if (listaPar == null)
			return sqlAdd;
		Enumeration en = listaPar.elements();
		while (en.hasMoreElements()){
			EscElementoFiltro filtro = (EscElementoFiltro) en.nextElement();
			if (filtro.getValore()!=null && !filtro.getValore().trim().equals("")){
				if (filtro.getTipo().equals("O")) {
					if (sqlAdd.equals(""))
						sqlAdd+= " ORDER BY ";
					else
						sqlAdd += " , ";
					sqlAdd += filtro.getValore().trim();
					if (filtro.getOperatore().toLowerCase().trim().equals("desc"))
						sqlAdd+= " DESC ";
					else
						sqlAdd+= " ";
						
				}
			}
		}
			
    	return sqlAdd;
	}
    
	protected boolean verificaFonteComune(String comune, String codFonte) throws NamingException{
		ComuneService comuneService= (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
		AmFonteComune am = comuneService.getFonteComuneByComuneCodiceFonte(comune, codFonte);
		return (am!=null)? true : false;
	}
    
    public String getDirPregeoEnte() {
		if(this.dirPregeoEnte == null){
			this.dirPregeoEnte = envUtente.getEnte() + File.separatorChar + this.dirPregeo;
		}
			
		return dirPregeoEnte;
	}

	public String getDirPdfDocfaEnte() {
		if(this.dirPdfDocfaEnte == null){
			this.dirPdfDocfaEnte = envUtente.getEnte() + File.separatorChar + this.dirPdfDocfa;
		}
			
		return dirPdfDocfaEnte;
	}

	public String getDirPlanimetrieComma340Ente() {
		if(this.dirPlanimetrieComma340Ente == null){
			this.dirPlanimetrieComma340Ente = envUtente.getEnte() + File.separatorChar + this.dirPlanimetrieComma340;
		}
			
		return dirPlanimetrieComma340Ente;
	}

	public String getDirStoricoConcessioniEnte() {
		if(this.dirStoricoConcessioniEnte == null){
			this.dirStoricoConcessioniEnte = envUtente.getEnte() + File.separatorChar + this.dirStoricoConcessioni;
		}
		return dirStoricoConcessioniEnte;
	}

	public String getDirModelloXlsEnte() {
		if(this.dirModelloXlsEnte == null)
			this.dirModelloXlsEnte = envUtente.getEnte() + File.separatorChar + this.dirModelloXls;
		return dirModelloXlsEnte;
	}

	public String getDirConcessioniVisureEnte() {
		if(this.dirConcessioniVisureEnte == null)
			this.dirConcessioniVisureEnte = envUtente.getEnte() + File.separatorChar + this.dirConcessioniVisure;
		return dirConcessioniVisureEnte;
	}

	public String getDirPlanimetrieEnte() {
		if(this.dirPlanimetrieEnte == null)
			this.dirPlanimetrieEnte = envUtente.getEnte() + File.separatorChar + this.dirPlanimetrie;
		return dirPlanimetrieEnte;
	}

	public String getDirCartografiaStoricaEnte() {
		
		if(this.dirCartografiaStoricaEnte == null)
			this.dirCartografiaStoricaEnte = envUtente.getEnte() + File.separatorChar + this.dirCartografiaStorica;
		
		return dirCartografiaStoricaEnte;
	}
	
	public void writeAudit(StackTraceElement stackEl, Object[] arguments, Object result){
		try {
			CeTUser cetUser = getEnvUtente().getUtente();
			auditWriter.auditMethod(cetUser.getCurrentEnte(),cetUser.getUsername(), cetUser.getSessionId(),
					stackEl.getClassName(), stackEl.getMethodName(), arguments, result, null);
		} catch (Throwable e) {				
			log.debug("ERRORE nella scrittura dell'audit", e);
		}
	}
	
}
