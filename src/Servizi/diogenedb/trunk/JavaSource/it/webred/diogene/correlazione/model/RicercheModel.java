package it.webred.diogene.correlazione.model;

import it.webred.diogene.correlazione.backingbeans.RicercheBB;
import it.webred.diogene.correlazione.beans.Ricerca;
import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DcExpression;
import it.webred.diogene.db.model.DcTipoe;
import it.webred.diogene.db.model.DcTipoeColumn;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.metadata.beans.Table;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalla pagina di ricerca negli archivi consultabili.
 * @author Filippo Mazzini
 * @version $Revision: 1.8 $ $Date: 2007/12/19 09:16:37 $
 */
public class RicercheModel {

	/**
	 *	Oggetto Configuration di Hibernate.
	 */
	Configuration c;
	/**
	 *	Oggetto SessionFactory di Hibernate.
	 */
	SessionFactory sf;
	/**
	 *	Costante che specifica il charset da usare nella conversione da array di byte a String.
	 */
	private static final String UTF8_XML_ENCODING = "UTF-8";
	/**
	 *	Costante che specifica il formato delle date.
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	/**
	 *	Costante (con relativa inizializzazione) che specifica il formato dei campi numerici.
	 */
	private static final DecimalFormat df = new DecimalFormat();
	{
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		df.setGroupingUsed(false);
	}
	
	/**
	 * Carattere separatore per l'inserimento di eventuali parametri multipli (per BETWEEN, NOT BETWEEN, IN).
	 */
	public final static String separaParams = "§";
	
	/**
	 * Costante di classe SelectItem per l'operatore "è uguale a"
	 */
	public final static SelectItem UGUALE_A = new SelectItem("=", "è uguale a");
	/**
	 * Costante di classe SelectItem per l'operatore "è diverso da"
	 */
	public final static SelectItem DIVERSO_DA = new SelectItem("<>", "è diverso da");
	/**
	 * Costante di classe SelectItem per l'operatore "è compreso tra"
	 */
	public final static SelectItem COMPRESO_TRA = new SelectItem("BETWEEN AND", "è compreso tra");
	/**
	 * Costante di classe SelectItem per l'operatore "non è compreso tra"
	 */
	public final static SelectItem NON_COMPRESO_TRA = new SelectItem("NOT BETWEEN AND", "non è compreso tra");
	/**
	 * Costante di classe SelectItem per l'operatore "è maggiore di"
	 */
	public final static SelectItem MAGGIORE_DI = new SelectItem(">", "è maggiore di");
	/**
	 * Costante di classe SelectItem per l'operatore "è minore di"
	 */
	public final static SelectItem MINORE_DI = new SelectItem("<", "è minore di");
	/**
	 * Costante di classe SelectItem per l'operatore "comincia per"
	 */
	public final static SelectItem COMINCIA_PER = new SelectItem("LIKE ?*", "comincia per");
	/**
	 * Costante di classe SelectItem per l'operatore "finisce per"
	 */
	public final static SelectItem FINISCE_PER = new SelectItem("LIKE *?", "finisce per");
	/**
	 * Costante di classe SelectItem per l'operatore "contiene"
	 */
	public final static SelectItem CONTIENE = new SelectItem("LIKE *?*", "contiene");
	/**
	 * Costante di classe SelectItem per l'operatore "è vuoto"
	 */
	public final static SelectItem VUOTO = new SelectItem("IS NULL", "è vuoto");
	/**
	 * Costante di classe SelectItem per l'operatore "non è vuoto"
	 */
	public final static SelectItem NON_VUOTO = new SelectItem("IS NOT NULL", "non è vuoto");
	/**
	 * Costante di classe SelectItem per l'operatore "è contenuto in"
	 */
	public final static SelectItem CONTENUTO_IN = new SelectItem("IN ()", "è contenuto in");
	
	/**
	 * Costante di tipo ArrayList di oggetti SelectItem (con relativa inizializzazione), contenente la lista degli operatori, 
	 * da utilizzarsi per la valorizzazione (ad esempio) di una combo box in una pagina JSP.
	 */
	public final static ArrayList<SelectItem> operatori = new ArrayList<SelectItem>();
	static {
		operatori.add(UGUALE_A);
		operatori.add(DIVERSO_DA);
		operatori.add(COMPRESO_TRA);
		operatori.add(NON_COMPRESO_TRA);
		operatori.add(MAGGIORE_DI);
		operatori.add(MINORE_DI);
		operatori.add(COMINCIA_PER);
		operatori.add(FINISCE_PER);
		operatori.add(CONTIENE);
		operatori.add(VUOTO);
		operatori.add(NON_VUOTO);
		operatori.add(CONTENUTO_IN);
	}	

	/**
	*	Costruttore che inizializza gli oggetti Configuration e SessionFactory.
	*/
	public RicercheModel() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}
	
	/**
	 * Metodo che effettua la ricerca negli archivi consultabili selezionati, valorizzando il parametro ricerca con i 
	 * risultati trovati (campo "risultati", ArrayList tridimensionale di Object).
	 * @param ricerca L'oggetto di classe Ricerca che contiene i parametri di ricerca e che deve essere valorizzato con i 
	 * risultati della ricerca.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	public void getRisultati(Ricerca ricerca) throws Exception {
		ArrayList<ArrayList<ArrayList<Object>>> risultati = new ArrayList<ArrayList<ArrayList<Object>>>();
		Session session = null;
		Connection conn = null;
		try {
			session = sf.openSession();
			//effettuo la connessione al DWH
			conn = DataJdbcConnection.getConnectionn("DWH");
			ricerca.setRecordCount(new ArrayList<Integer>());
			for (Table archivio : ricerca.getArchivi()) {
				DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, archivio.getId());
				DvUserEntity dvUserEntity = null; 
				Set dvUserEntities = dcEntity.getDvUserEntities();
				Iterator itUserEntities = dvUserEntities.iterator();
				while (itUserEntities.hasNext()) {
					dvUserEntity = (DvUserEntity)itUserEntities.next();
				}
				//recupero l'istruzione SQL
				String xmlSqlStatement = new String(dvUserEntity.getSqlStatementBk(), UTF8_XML_ENCODING);
				String sqlStatement = "";
				SAXBuilder builder = new SAXBuilder();
				List content = builder.build(new StringReader(xmlSqlStatement)).getContent();
				Iterator it = content.iterator();
				while (it.hasNext()) {
					Element element = (Element) it.next();
					if (element.getText() != null && !element.getText().equals("")) {
						sqlStatement = "SELECT * FROM (" + element.getText() + ")";
					}else{
						//recupero il valore del campo SQL_STATEMENT
						String xmlSqlStatement1 = new String(dvUserEntity.getSqlStatement(), UTF8_XML_ENCODING);
						SAXBuilder builder1 = new SAXBuilder();
						List content1 = builder1.build(new StringReader(xmlSqlStatement1)).getContent();
						Iterator it1 = content1.iterator();
						while (it1.hasNext()) {
							Element element1 = (Element) it1.next();
							sqlStatement = "SELECT * FROM (" + element1.getText() + ")";
						}
					}
				}
				//costruisco la where da aggiungere all'istruzione SQL, secondo i parametri specificati
				String[] wheres = new String[ricerca.getTipiEtichetta().size()];
				//recupero anche i tipi delle colonne
				String[] colTypes = new String[ricerca.getTipiEtichetta().size()];
				int conta = 0;
				for (TipoEtichetta tipoEtichetta : ricerca.getTipiEtichetta()) {
					String where = "";
					String colType = "";
					DcTipoe dcTipoe = (DcTipoe)session.load(DcTipoe.class, tipoEtichetta.getId());
					Set dcTipoeColumns = dcTipoe.getDcTipoeColumns();
					Set dcColumns = dcEntity.getDcColumns();
					Iterator itTipoeColumns = dcTipoeColumns.iterator();
					while (itTipoeColumns.hasNext() && where.equals("")) {
						DcTipoeColumn dcTipoeColumn = (DcTipoeColumn)itTipoeColumns.next();
						Iterator itColumns = dcColumns.iterator();
						while (itColumns.hasNext() && where.equals("")) {
							DcColumn dcColumn = (DcColumn)itColumns.next();
							if (dcColumn.getId().longValue() == dcTipoeColumn.getDcColumn().getId().longValue()) {
								DcExpression dcExpression = dcColumn.getDcExpression();
								colType = dcExpression.getColType();						
								if (dcExpression.getAlias() != null && !dcExpression.getAlias().equals("")) {
									where = dcExpression.getAlias();
								} else {
									where = getColName(dcColumn);
								}
							}
						}
					}
					colTypes[conta] = colType;
					/*Object obj = null;
					try {
						obj = Class.forName(colTypes[conta]).newInstance();
					}catch (java.lang.InstantiationException ie) {
						//si verifica con java.sql.Date e con java.math.BigDecimal
						if (colTypes[conta].equals("java.math.BigDecimal")) {
							//ci sarebbe una nuova eccezione...
							obj = new BigDecimal(0);
						}else{
							obj = Class.forName(colTypes[conta]).getSuperclass().newInstance();
						}
					}
					if (obj instanceof String) {
						where = "UPPER(" + where + ")";
					}*/			
					where = getWhere(colTypes[conta], where, tipoEtichetta);
					wheres[conta] = where;
					conta++;
				}
				conta = 0;
				for (String where : wheres) {
					if (conta == 0) {
						sqlStatement += " WHERE ";
					}else{
						sqlStatement += " AND ";
					}
					sqlStatement += where;					
					conta++;
				}
				//imposto il valore da aggiungere all'ArrayList recordCount (numero di righe)
				String sqlStatementToCount = "SELECT COUNT(*) AS CONTA FROM (" + sqlStatement + ")";
				PreparedStatement stToCount = null;
				ResultSet rsToCount = null;				
				try {
					stToCount = conn.prepareStatement(sqlStatementToCount);
					conta = 0;
					for (String colType : colTypes) {
						TipoEtichetta tipoEtichetta = ricerca.getTipiEtichetta().get(conta);
						conta = setParam(stToCount, conta, colType, tipoEtichetta);
					}
					rsToCount = stToCount.executeQuery();
					Integer rowCount = null;
					while (rsToCount.next()) {
						rowCount = new Integer(rsToCount.getObject("CONTA") == null ? new Integer(0) : rsToCount.getInt("CONTA"));
					}
					ricerca.getRecordCount().add(rowCount);
				}catch (Exception e) {
					//eccezione non bloccante, viene aggiunto 0 e il ciclo continua
					e.printStackTrace();
					ricerca.getRecordCount().add(new Integer(0)); //aggiungo 0
				}finally{
					if (rsToCount != null)
						rsToCount.close();
					if (stToCount != null)
						stToCount.close();
				}
				//costruisco ed eseguo la query
				PreparedStatement st = null;
				ResultSet rs = null;
				sqlStatement = "SELECT * FROM (SELECT ROWNUM, SEL.* FROM (" + sqlStatement + ") SEL) WHERE ROWNUM >= ? AND ROWNUM <= ?";
				try {
					st = conn.prepareStatement(sqlStatement);
					conta = 0;
					for (String colType : colTypes) {
						TipoEtichetta tipoEtichetta = ricerca.getTipiEtichetta().get(conta);
						conta =	setParam(st, conta, colType, tipoEtichetta);
					}
					//valori per parametri ROWNUM
					conta++;
					st.setInt(conta, 1);
					conta++;
					st.setInt(conta, RicercheBB.righePerPagina);
					//esecuzione della query
					rs = st.executeQuery();
					ArrayList<ArrayList<Object>> dati = new ArrayList<ArrayList<Object>>();
					ResultSetMetaData rsmd = rs.getMetaData();
					//intestazioni			
					ArrayList<Object> intestazioni = new ArrayList<Object>();
					//parto dal secondo elemento (indice 1) perché il primo è il ROWNUM e non va visualizzato
					for (int i = 1; i < rsmd.getColumnCount(); i++) {
						String intestazione = getIntestazioneFromAlias(dcEntity, rsmd.getColumnName(i + 1));
						intestazioni.add(intestazione);
					}
					dati.add(intestazioni);				
					//dati
					while (rs.next()) {
						ArrayList<Object> rigaDati = new ArrayList<Object>();
						//parto dal secondo elemento (indice 1) perché il primo è il ROWNUM e non va visualizzato
						for (int i = 1; i < rsmd.getColumnCount(); i++) {
							rigaDati.add(rs.getObject(i + 1));
						}
						dati.add(rigaDati);
					}
					risultati.add(dati);					
				}catch (Exception e) {
					//eccezione non bloccante, viene aggiunto un elemento vuoto e il ciclo continua
					e.printStackTrace();
					risultati.add(new ArrayList<ArrayList<Object>>()); //aggiungo un elemento vuoto
				}finally{
					if (rs != null)
						rs.close();
					if (st != null)
						st.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		ricerca.setRisultati(risultati);
	}
	
	
	/**
	 * Metodo che valorizza il campo "risultatiVis" (array tridimensionale di String), utilizzato per la 
	 * visualizzazione dei dati di una ricerca nella pagina, nel parametro ricerca, a partire dal valore del 
	 * campo "risultati" (array tridimensionale di Object).
	 * @param ricerca L'oggetto di classe Ricerca di cui deve essere valorizzato il campo "risultatiVis".
	 */
	public void getRisultatiVis(Ricerca ricerca) {
		ArrayList<ArrayList<ArrayList<Object>>> risultati = ricerca.getRisultati();
		ArrayList<ArrayList<ArrayList<String>>> risultatiVis = new ArrayList<ArrayList<ArrayList<String>>>();
		if (risultati == null) {
			return;
		}
		for (ArrayList<ArrayList<Object>> al1 : risultati) {
			ArrayList<ArrayList<String>> al1Str = new ArrayList<ArrayList<String>>();
			for (ArrayList<Object> al2 : al1) {
				ArrayList<String> al2Str = new ArrayList<String>();
				for(Object obj : al2) {
					String str = "-";
					if (obj != null) {
						if (obj instanceof Date) {
							str = sdf.format((Date)obj);
						} else if (obj instanceof Number) {
							str = df.format(((Number)obj).doubleValue());
						//inserire eventuali altri tipi
						} else{
							str = obj.toString();
						}
					}
					al2Str.add(str);
				}
				al1Str.add(al2Str);
			}
			risultatiVis.add(al1Str);
		}
		ricerca.setRisultatiVis(risultatiVis);
	}
	
	/**
	 * Metodo che effettua la ricerca nel DB a seguito di paginazione; per l'archivio consultabile corrente modifica 
	 * nell'oggetto ricerca passato a parametro i dati da visualizzare, a seconda della pagina selezionata.
	 * @param ricerca L'oggetto ricerca i cui dati sono da modificare (per l'archivio corrente) a seguito di paginazione.
	 * @param idxArchivio L'indice dell'archivio corrente (in base 0).
	 * @param rigaDa La riga iniziale da mostrare nella popup (primo parametro ROWNUM della query).
	 * @param rigaA La riga finale da mostrare nella popup (secondo parametro ROWNUM della query).
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void getRisultatiPag(Ricerca ricerca, Integer idxArchivio, int rigaDa, int rigaA) throws Exception {
		Session session = null;
		Connection conn = null;
		try {
			session = sf.openSession();
			//effettuo la connessione al DWH
			conn = DataJdbcConnection.getConnectionn("DWH");
			Table archivio = ricerca.getArchivi().get(idxArchivio.intValue());
			DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, archivio.getId());
			DvUserEntity dvUserEntity = null; 
			Set dvUserEntities = dcEntity.getDvUserEntities();
			Iterator itUserEntities = dvUserEntities.iterator();
			while (itUserEntities.hasNext()) {
				dvUserEntity = (DvUserEntity)itUserEntities.next();
			}
			//recupero l'istruzione SQL			
			String xmlSqlStatement = new String(dvUserEntity.getSqlStatementBk(), UTF8_XML_ENCODING);
			String sqlStatement = "";
			SAXBuilder builder = new SAXBuilder();
			List content = builder.build(new StringReader(xmlSqlStatement)).getContent();
			Iterator it = content.iterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				if (element.getText() != null && !element.getText().equals("")) {
					sqlStatement = "SELECT * FROM (" + element.getText() + ")";
				}else{
					//recupero il valore del campo SQL_STATEMENT
					String xmlSqlStatement1 = new String(dvUserEntity.getSqlStatement(), UTF8_XML_ENCODING);
					SAXBuilder builder1 = new SAXBuilder();
					List content1 = builder1.build(new StringReader(xmlSqlStatement1)).getContent();
					Iterator it1 = content1.iterator();
					while (it1.hasNext()) {
						Element element1 = (Element) it1.next();
						sqlStatement = "SELECT * FROM (" + element1.getText() + ")";
					}
				}
			}
			//costruisco la where da aggiungere all'istruzione SQL, secondo i parametri specificati
			String[] wheres = new String[ricerca.getTipiEtichetta().size()];
			//recupero anche i tipi delle colonne
			String[] colTypes = new String[ricerca.getTipiEtichetta().size()];
			int conta = 0;
			for (TipoEtichetta tipoEtichetta : ricerca.getTipiEtichetta()) {
				String where = "";
				String colType = "";
				DcTipoe dcTipoe = (DcTipoe)session.load(DcTipoe.class, tipoEtichetta.getId());
				Set dcTipoeColumns = dcTipoe.getDcTipoeColumns();
				Set dcColumns = dcEntity.getDcColumns();
				Iterator itTipoeColumns = dcTipoeColumns.iterator();
				while (itTipoeColumns.hasNext() && where.equals("")) {
					DcTipoeColumn dcTipoeColumn = (DcTipoeColumn)itTipoeColumns.next();
					Iterator itColumns = dcColumns.iterator();
					while (itColumns.hasNext() && where.equals("")) {
						DcColumn dcColumn = (DcColumn)itColumns.next();
						if (dcColumn.getId().longValue() == dcTipoeColumn.getDcColumn().getId().longValue()) {
							DcExpression dcExpression = dcColumn.getDcExpression();
							colType = dcExpression.getColType();						
							if (dcExpression.getAlias() != null && !dcExpression.getAlias().equals("")) {
								where = dcExpression.getAlias();
							} else {
								where = getColName(dcColumn);
							}
						}
					}
				}
				colTypes[conta] = colType;
				/*Object obj = null;
				try {
					obj = Class.forName(colTypes[conta]).newInstance();
				}catch (java.lang.InstantiationException ie) {
					//si verifica con java.sql.Date e con java.math.BigDecimal
					if (colTypes[conta].equals("java.math.BigDecimal")) {
						//ci sarebbe una nuova eccezione...
						obj = new BigDecimal(0);
					}else{
						obj = Class.forName(colTypes[conta]).getSuperclass().newInstance();
					}
				}
				if (obj instanceof String) {
					where = "UPPER(" + where + ")";
				}*/
				where = getWhere(colTypes[conta], where, tipoEtichetta);
				wheres[conta] = where;
				conta++;
			}
			conta = 0;
			for (String where : wheres) {
				if (conta == 0) {
					sqlStatement += " WHERE ";
				}else{
					sqlStatement += " AND ";
				}
				sqlStatement += where;
				conta++;
			}
			//costruisco ed eseguo la query
			PreparedStatement st = null;
			ResultSet rs = null;
			sqlStatement = "SELECT * FROM (SELECT ROWNUM ROWNUM_, SEL.* FROM (" + sqlStatement + ") SEL) WHERE ROWNUM_ >= ? AND ROWNUM_ <= ?";
			try {
				st = conn.prepareStatement(sqlStatement);
				conta = 0;
				for (String colType : colTypes) {
					TipoEtichetta tipoEtichetta = ricerca.getTipiEtichetta().get(conta);
					conta = setParam(st, conta, colType, tipoEtichetta);
				}
				//valori per parametri ROWNUM
				conta++;
				st.setInt(conta, rigaDa);
				conta++;
				st.setInt(conta, rigaA);
				//esecuzione della query
				rs = st.executeQuery();
				ArrayList<ArrayList<Object>> dati = new ArrayList<ArrayList<Object>>();
				ResultSetMetaData rsmd = rs.getMetaData();
				//intestazioni			
				ArrayList<Object> intestazioni = new ArrayList<Object>();
				//parto dal secondo elemento (indice 1) perché il primo è il ROWNUM e non va visualizzato
				for (int i = 1; i < rsmd.getColumnCount(); i++) {
					String intestazione = getIntestazioneFromAlias(dcEntity, rsmd.getColumnName(i + 1));
					intestazioni.add(intestazione);
				}
				dati.add(intestazioni);				
				//dati
				while (rs.next()) {
					ArrayList<Object> rigaDati = new ArrayList<Object>();
					//parto dal secondo elemento (indice 1) perché il primo è il ROWNUM e non va visualizzato
					for (int i = 1; i < rsmd.getColumnCount(); i++) {
						rigaDati.add(rs.getObject(i + 1));
					}
					dati.add(rigaDati);
				}
				ricerca.getRisultati().set(idxArchivio.intValue(), dati);
			}catch (Exception e) {
				//eccezione non bloccante
				e.printStackTrace();
				ricerca.getRisultati().set(idxArchivio.intValue(), new ArrayList<ArrayList<Object>>()); //aggiungo un elemento vuoto
			}finally{
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	/**
	 * Metodo privato che, dato l'alias di una colonna, ne restituisce la corrispondente intestazione (per la 
	 * visualizzazione nella popup).
	 * @param dcEntity L'entità a cui la colonna appartiene.
	 * @param alias L'alias della colonna.
	 * @return Una String che rappresenta l'intestazione della colonna, da utilizzarsi per la visualizzazione 
	 * nella popup.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private String getIntestazioneFromAlias(DcEntity dcEntity, String alias) throws Exception {
		if (alias == null)
			return "";
		String retVal = alias; //default
		Set dcColumns = dcEntity.getDcColumns();
		Iterator itColumns = dcColumns.iterator();
		while (itColumns.hasNext()) {
			DcColumn dcColumn = (DcColumn)itColumns.next();
			if (dcColumn.getDcExpression().getAlias() != null && dcColumn.getDcExpression().getAlias().equals(alias)) {
				//verificare se è corretta questa forma
				//return dcColumn.getLongDesc() + " (" + getColName(dcColumn) + ")";
				return dcColumn.getLongDesc();
			}
		}
		return retVal;
	}
	
	/**
	 * Metodo privato che, dato un oggetto di classe DcColumn, ne restituisce il nome sotto forma di String di 
	 * tipo NOME_TABELLA.NOME_COLONNA.
	 * @param dcColumn La colonna di cui si ricerca il nome.
	 * @return Una String che rappresenta, sotto forma NOME_TABELLA.NOME_COLONNA, il nome della colonna.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private String getColName(DcColumn dcColumn) throws Exception {
		String retVal = "";
		DcExpression dcExpression = dcColumn.getDcExpression();
		String expression = new String(dcExpression.getExpression(), UTF8_XML_ENCODING);
		SAXBuilder builder = new SAXBuilder();
		List content = builder.build(new StringReader(expression)).getContent();
		Iterator it = content.iterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			List content1 = element.getChildren();
			Iterator it1 = content1.iterator();
			while (it1.hasNext()) {
				Element element1 = (Element) it1.next();
				if (element1.getName().equals("column")) {
					List content2 = element1.getChildren();
					Iterator it2 = content2.iterator();
					while (it2.hasNext()) {
						Element element2 = (Element) it2.next();
						if (element2.getName().equals("table") || element2.getName().equals("field")) {
							List content3 = element2.getChildren();
							Iterator it3 = content3.iterator();
							while (it3.hasNext()) {
								Element element3 = (Element) it3.next();
								if (element3.getName().equals("name")) {
									if (element2.getName().equals("field")) {
										retVal += ".";
									}
									retVal += element3.getText();
								}															
							}
						}
					}
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Restituisce la lista degli operatori, sotto forma di ArrayList di oggetti SelectItem, per la valorizzazione (ad esempio) 
	 * di una combo box in una pagina JSP.
	 * @return Una ArrayList di oggetti SelectItem, contenente la lista degli operatori.
	 */
	public ArrayList<SelectItem> getOperatori() {
		return operatori;
	}
	
	//TODO commento
	private String getWhere(String colType, String where, TipoEtichetta tipoEtichetta) throws Exception {
		String dxRetVal = "";
		String operatore = tipoEtichetta.getOperatore();
		if (operatore.equals(UGUALE_A.getValue().toString()) ||
			operatore.equals(DIVERSO_DA.getValue().toString()) ||
			operatore.equals(MAGGIORE_DI.getValue().toString()) ||
			operatore.equals(MINORE_DI.getValue().toString())) {
			dxRetVal = " " + operatore + " ?";
		} else if (operatore.equals(COMPRESO_TRA.getValue().toString())) {
			dxRetVal = " BETWEEN ? AND ?";
		} else if (operatore.equals(NON_COMPRESO_TRA.getValue().toString())) {
			dxRetVal = " NOT BETWEEN ? AND ?";
		} else if (operatore.equals(COMINCIA_PER.getValue().toString()) ||
				operatore.equals(FINISCE_PER.getValue().toString()) ||
				operatore.equals(CONTIENE.getValue().toString())) {
			Object obj = null;
			try {
				obj = Class.forName(colType).newInstance();
			}catch (java.lang.InstantiationException ie) {
				//può lasciare null...
			}
			if (obj == null || !(obj instanceof String)) {
				where = "TO_CHAR(" + where + ")";
			}
			dxRetVal = " LIKE ?";
		} else if (operatore.equals(VUOTO.getValue().toString()) ||
				operatore.equals(NON_VUOTO.getValue().toString())) {
			dxRetVal = " " + operatore;
		} else if (operatore.equals(CONTENUTO_IN.getValue().toString())) {
			String chiave = tipoEtichetta.getChiave();
			int numParams = chiave.split(separaParams).length;
			dxRetVal = " IN (";
			for (int i = 0; i < numParams; i++) {
				dxRetVal += ("?" + ((i < numParams - 1) ? ", " : ""));
			}
			dxRetVal += ")";
		}
		if (dxRetVal.equals("")) {
			dxRetVal = " " + UGUALE_A.getValue().toString() + " ?";
		}
		return where + dxRetVal;
	}
	
	//TODO commento
	private int setParam(PreparedStatement st, int conta, String colType, TipoEtichetta tipoEtichetta) throws Exception {
		String chiave = tipoEtichetta.getChiave();
		String operatore = tipoEtichetta.getOperatore();
		if (operatore.equals(UGUALE_A.getValue().toString()) ||
				operatore.equals(DIVERSO_DA.getValue().toString()) ||
				operatore.equals(MAGGIORE_DI.getValue().toString()) ||
				operatore.equals(MINORE_DI.getValue().toString())) {
			Object obj = null;
			try {
				obj = Class.forName(colType).newInstance();
			}catch (java.lang.InstantiationException ie) {
				//si verifica con java.sql.Date e con java.math.BigDecimal
				if (colType.equals("java.math.BigDecimal")) {
					//ci sarebbe una nuova eccezione...
					obj = new BigDecimal(0);
				}else{
					obj = Class.forName(colType).getSuperclass().newInstance();
				}
			}
			conta++;
			if (obj instanceof Date) {
				st.setDate(conta, new java.sql.Date(sdf.parse(chiave).getTime()));
			} else if (obj instanceof Number) {
				st.setDouble(conta, df.parse(chiave).doubleValue());
			//inserire eventuali altri tipi
			} else{
				//st.setString(conta, chiave.toUpperCase());
				st.setString(conta, chiave);
			}
		} else if (operatore.equals(COMPRESO_TRA.getValue().toString()) ||
				operatore.equals(NON_COMPRESO_TRA.getValue().toString()) ||
				operatore.equals(CONTENUTO_IN.getValue().toString())) {
			String[] params = chiave.split(separaParams);
			if (operatore.equals(COMPRESO_TRA.getValue().toString()) ||
				operatore.equals(NON_COMPRESO_TRA.getValue().toString())) {
				if (params.length == 1) {
					String param = new String(params[0]);
					params = new String[2];
					params[0] = new String(param);
					params[1] = new String(param);
				}
			}
			int numParams = operatore.equals(CONTENUTO_IN.getValue().toString()) ?
							params.length :
							2;
			conta += numParams;
			for (int i = 0; i < numParams; i++) {
				Object obj = null;
				try {
					obj = Class.forName(colType).newInstance();
				}catch (java.lang.InstantiationException ie) {
					//si verifica con java.sql.Date e con java.math.BigDecimal
					if (colType.equals("java.math.BigDecimal")) {
						//ci sarebbe una nuova eccezione...
						obj = new BigDecimal(0);
					}else{
						obj = Class.forName(colType).getSuperclass().newInstance();
					}
				}
				if (obj instanceof Date) {
					st.setDate(conta, new java.sql.Date(sdf.parse(params[i]).getTime()));
				} else if (obj instanceof Number) {
					st.setDouble(conta, df.parse(params[i]).doubleValue());
				//inserire eventuali altri tipi
				} else{
					//st.setString(conta, params[i].toUpperCase());
					st.setString(conta, params[i]);
				}
			}
		} else if (operatore.equals(COMINCIA_PER.getValue().toString())) {
			conta++;
			st.setString(conta, chiave + "%");
		} else if (operatore.equals(FINISCE_PER.getValue().toString())) {
			conta++;
			st.setString(conta, "%" + chiave);
		} else if (operatore.equals(CONTIENE.getValue().toString())) {
			conta++;
			st.setString(conta, "%" + chiave + "%");
		} else if (operatore.equals(VUOTO.getValue().toString()) ||
				operatore.equals(NON_VUOTO.getValue().toString())) {
			//non deve fare nulla...
		}
		return conta;
	}
	
}
