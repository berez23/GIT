package it.webred.diogene.metadata.model;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import static it.webred.utils.MetadataConstants.*;
import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import it.webred.diogene.db.*;
import it.webred.diogene.db.model.*;
import it.webred.diogene.db.model.am.AmApplication;
import it.webred.diogene.db.model.am.AmItem;
import it.webred.diogene.metadata.*;
import it.webred.diogene.metadata.beans.*;
import it.webred.diogene.querybuilder.RightsManager;
import it.webred.diogene.querybuilder.db.EntitiesDBManager;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.permessi.GestionePermessi;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalle pagine di mappatura tabelle e mappatura colonne.
 * @author Filippo Mazzini
 * @version $Revision: 1.6.2.1 $ $Date: 2012/01/26 16:59:39 $
 */
public class CaricaTabelleModel {

	/**
	 *	Oggetto MetadataFactory utilizzato per la connessione al DB di origine dati.
	 */
	MetadataFactory mf;
	/**
	 *	Oggetto MetadataManager, utilizzato per impostare la MetadataFactory per la connessione al DB di origine dati.
	 */
	MetadataManager mm;
	/**
	 *	Oggetto Configuration di Hibernate.
	 */
	Configuration c;
	/**
	 *	Oggetto SessionFactory di Hibernate.
	 */
	SessionFactory sf;
	/**
	 * Costante che identifica l'ATTRIBUTE_SPEC delle colonne definite come chiave.
	 */
	public static final String KEY = DcAttributes.KEY.name();
	/**
	 * Costante che identifica l'ATTRIBUTE_SPEC "NULLABLE".
	 */
	public static final String NULLABLE = DcAttributes.NULLABLE.name();
	/**
	 * Costante che indica che una tabella non è stata configurata, e quindi non ne sono state caricate colonne e chiavi.<p>
	 * Una tabella non configurata ha le Hashmap contenenti colonne e chiavi valorizzate con un solo elemento, che ha chiave = 
	 * NON_CONFIGURATA e valore null.
	 */
	public static final String NON_CONFIGURATA = "NON_CONFIGURATA";
	/**
	 * ArrayList contenente i nomi delle entità il salvataggio delle quali non è stato possibile per problemi di lock.
	 */
	private ArrayList<String> errEntities;

	/**
	*	Costruttore che inizializza gli oggetti Configuration e SessionFactory.
	*/
	public CaricaTabelleModel() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}

	/**
	 * Data una stringa che rappresenta il nome del tipo di DB, imposta gli oggetti MetadataManager e (da esso) 
	 * MetadataFactory.
	 * @param dbTypeToDisplay String che rappresenta il nome del tipo di DB.
	 * @throws Exception Se si verifica una qualche eccezione durante la valorizzazione dei due oggetti.
	 */
	public void setMetadataFactory(String dbTypeToDisplay) throws Exception {
		if (dbTypeToDisplay == null || dbTypeToDisplay.equals("")) {
			mf = null;
			mm = null;
		} else {
			mm = new MetadataManager(dbTypeToDisplay);
			mf = mm.getMetadataFactory();
		}
	}

	/**
	 * Restituisce la lista dei nomi dei tipi di DB supportati dall'applicazione (dalla classe it.webred.utils.MetadataConstants).
	 * @return Un'ArrayList (di SelectItem) contenente la lista dei nomi dei tipi di DB supportati dall'applicazione.
	 */
	public ArrayList<SelectItem> getDbTypes() {
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		// i nomi dei db supportati sono presi dalla classe Constants
		List<String> list = getDBDescriptions();
		retVal.add(new SelectItem("")); // item vuoto
		for (String str : list) {
			retVal.add(new SelectItem(str));
		}
		return retVal;
	}

	/**
	 * Restituisce la lista dei nomi degli schemi presenti nel DB di origine.
	 * @return Un'ArrayList (di SelectItem) contenente la lista dei nomi degli schemi presenti nel DB di origine.
	 * @throws Exception Se si verifica una qualche eccezione SQL durante l'esecuzione del metodo.
	 */
	public ArrayList<SelectItem> getSchemas() throws Exception {
		return getSchemas(null, null, null);
	}

	/**
	 * Restituisce la lista dei nomi degli schemi presenti nel DB di origine per stringa di connessione, username e 
	 * password passati a parametro.
	 * @param connStr La stringa di connessione
	 * @param username L'username
	 * @param password La password
	 * @return Un'ArrayList (di SelectItem) contenente la lista dei nomi degli schemi presenti nel DB di origine.
	 * @throws Exception Se si verifica una qualche eccezione SQL durante l'esecuzione del metodo.
	 */
	public ArrayList<SelectItem> getSchemas(String connStr, String username, String password) throws Exception {
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		Connection conn = null;
		try {
			if ((connStr == null || connStr.equals(""))
					&& (username == null || username.equals(""))
					&& (password == null || password.equals(""))) {
				conn = getDefaultConn();
				setMetadataFactory(DataJdbcConnection.getDefaultMdc().getDbtype());
			} else {
				conn = mf.getConnection(connStr, username, password, true);
			}
			List<Schema> list = mf.getSchemas(conn);
			retVal.add(new SelectItem("")); // item vuoto
			for (Schema sch : list) {
				retVal.add(new SelectItem(sch.getTableSchema()));
			}
		}catch (Exception e){
			throw e;
		}finally{
			mf.closeConnection(conn);
		}
		return retVal;
	}

	/**
	 * Restituisce la lista dei nomi delle tabelle presenti nel DB di origine per catalog, nome schema (e nome 
	 * tabella e types) passati a parametro.
	 * @param catalog Il nome del catalog
	 * @param schemaPattern Il nome dello schema
	 * @param tableNamePattern Il nome della tabella
	 * @param types I types
	 * @return Un'ArrayList (di SelectItem) contenente la lista dei nomi delle tabelle presenti nel DB di origine.
	 * @throws Exception Se si verifica una qualche eccezione SQL durante l'esecuzione del metodo.
	 */
	public ArrayList<SelectItem> getTables(String catalog, String schemaPattern, String tableNamePattern,
											String[] types) throws Exception {
		return getTables(null, null, null, catalog, schemaPattern, tableNamePattern, types);
	}

	/**
	 * Restituisce la lista dei nomi delle tabelle presenti nel DB di origine per stringa di connessione, username, 
	 * password, catalog, nome schema (e nome tabella e types) passati a parametro.
	 * @param connStr La stringa di connessione
	 * @param username L'username
	 * @param password La password
	 * @param catalog Il nome del catalog
	 * @param schemaPattern Il nome dello schema
	 * @param tableNamePattern Il nome della tabella
	 * @param types I types
	 * @return Un'ArrayList (di SelectItem) contenente la lista dei nomi delle tabelle presenti nel DB di origine.
	 * @throws Exception Se si verifica una qualche eccezione SQL durante l'esecuzione del metodo.
	 */
	public ArrayList<SelectItem> getTables(String connStr, String username, String password, String catalog,
											String schemaPattern, String tableNamePattern, String[] types)
											throws Exception {
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		Connection conn = null;
		try {
			if ((connStr == null || connStr.equals(""))
					&& (username == null || username.equals(""))
					&& (password == null || password.equals(""))) {
				conn = getDefaultConn();
				setMetadataFactory(DataJdbcConnection.getDefaultMdc().getDbtype());
			} else {
				conn = mf.getConnection(connStr, username, password, true);
			}
			List<Table> list = mf.getTables(conn, catalog, schemaPattern, tableNamePattern, types);
			for (Table tab : list) {
				retVal.add(new SelectItem(tab.getName()));
			}
		}catch (Exception e){
			throw e;
		}finally{
			mf.closeConnection(conn);
		}
		return retVal;
	}

	/**
	 * Restituisce l'elenco delle entità (tabelle) inserite nel DB di destinazione, sotto forma di HashMap.
	 * @return Un'HashMap di chiavi String e valori Table, contenente l'elenco delle entità (tabelle) inserite 
	 * nel DB di destinazione.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public HashMap<String, Table> getDestinationTables() throws Exception {
		HashMap<String, Table> retVal = new HashMap<String, Table>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("select DC_ENTITY from DC_ENTITY in class DcEntity, DC_SCHEMA_ENTITY in class DcSchemaEntity where DC_ENTITY.id = DC_SCHEMA_ENTITY.dcEntity");
			DcEntity dcent = new DcEntity();
			q.setProperties(dcent);
			List dcents = q.list();
			Iterator it = dcents.iterator();
			Table tab;
			while (it.hasNext()) {
				DcEntity element = (DcEntity) it.next();
				tab = new Table();
				tab.setId(element.getId());
				tab.setName(element.getName());
				tab.setLongDesc(element.getLongDesc());
				tab.setOwner(element.getOwner());
				tab.setUpdated(false);
				tab.setDtMod(element.getDtMod());
				Set dcSchEnts = element.getDcSchemaEntities();
				Iterator itSchEnts = dcSchEnts.iterator();
				// è una relazione 1..1
				if (itSchEnts.hasNext()) {
					DcSchemaEntity dcSchEnt = (DcSchemaEntity) itSchEnts.next();
					tab.setSqlName(dcSchEnt.getSqlName());
				}
				// colonne
				//HashMap<String, Column> columns = getDestinationColumns(tab.getId());
				HashMap<String, Column> columns = new HashMap<String, Column>();
				columns.put(NON_CONFIGURATA, null);
				tab.setColumns(columns);
				// chiavi
				//HashMap<String, Key> keys = getKeys(tab.getId());
				HashMap<String, Key> keys = new HashMap<String, Key>();
				keys.put(NON_CONFIGURATA, null);
				tab.setKeys(keys);

				retVal.put(tab.getSqlName(), tab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}

	/**
	 * Dato un identificativo di tabella, restituisce l'elenco delle colonne inserite nel DB di destinazione, 
	 * sotto forma di HashMap.
	 * @param tableId L'identificativo della tabella
	 * @return Un'HashMap di chiavi String e valori Column, contenente l'elenco delle colonne inserite nel DB di 
	 * destinazione per la tabella il cui identificativo è passato a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public HashMap<String, Column> getDestinationColumns(Long tableId) throws Exception {
		HashMap<String, Column> retVal = new HashMap<String, Column>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("select DC_COLUMN from DC_COLUMN in class DcColumn, "
							+ "DC_ENTITY in class DcEntity, "
							+ "DC_EXPRESSION in class DcExpression "
							+ "where DC_EXPRESSION.id = DC_COLUMN.dcExpression "
							+ "and DC_ENTITY.id = DC_COLUMN.dcEntity "
							+ "and DC_COLUMN.dcEntity = :dcEntity");
			DcColumn dccol = new DcColumn();
			DcEntity dcent = new DcEntity();
			dcent.setId(tableId);
			dccol.setDcEntity(dcent);
			q.setProperties(dccol);
			List dccols = q.list();
			Iterator it = dccols.iterator();
			Column col;
			while (it.hasNext()) {
				DcColumn element = (DcColumn) it.next();
				col = new Column();
				DcExpression expr = element.getDcExpression();
				DcEntity ent = element.getDcEntity();
				col.setId(expr.getId());
				col.setName(getColumnNameFromExpression(expr.getExpression()));
				col.setAlias(expr.getAlias());
				col.setTableId(ent.getId());
				col.setLongDesc(element.getLongDesc());
				if (col.getLongDesc() == null || col.getLongDesc().equals(""))
					col.setLongDesc(col.getName()); //default
				col.setColType(expr.getColType());
				DcAttribute myDcAtt = getNullable(session, element.getId());
				col.setNullable(myDcAtt == null ? "unknown" : myDcAtt.getAttributeVal());
				retVal.put(col.getName(), col);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}

	/**
	 * Dato un identificativo di tabella, restituisce l'elenco delle colonne definite come chiave per essa nel DB 
	 * di destinazione, sotto forma di HashMap.
	 * @param tableId L'identificativo della tabella
	 * @return Un'HashMap di chiavi String e valori Key, contenente l'elenco delle colonne definite come chiave, nel 
	 * DB di destinazione, per la tabella il cui identificativo è passato a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public HashMap<String, Key> getKeys(Long tableId) throws Exception {
		HashMap<String, Key> retVal = new HashMap<String, Key>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("select DC_COLUMN from DC_COLUMN in class DcColumn, "
					+ "DC_ENTITY in class DcEntity, "
					+ "DC_EXPRESSION in class DcExpression "
					+ "where DC_EXPRESSION.id = DC_COLUMN.dcExpression "
					+ "and DC_ENTITY.id = DC_COLUMN.dcEntity "
					+ "and DC_COLUMN.dcEntity = :dcEntity");
			DcColumn dccol = new DcColumn();
			DcEntity dcent = new DcEntity();
			dcent.setId(tableId);
			dccol.setDcEntity(dcent);
			q.setProperties(dccol);
			List dcatts = q.list();
			Iterator it = dcatts.iterator();
			Key key;
			while (it.hasNext()) {
				Set atts = ((DcColumn)it.next()).getDcAttributes();
				Iterator attsIt = atts.iterator();
				while (attsIt.hasNext()) {
					DcAttribute element = (DcAttribute)attsIt.next();
					if (element.getAttributeSpec().equals(KEY)) {
						key = new Key();
						key.setId(element.getId());
						DcColumn col = element.getDcColumn();
						key.setColumnId(col.getId());
						key.setColumnName(getColumnNameFromExpression(col.getDcExpression().getExpression()));
						retVal.put(key.getColumnName(), key);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}

	/**
	 * Effettua il salvataggio (operazioni di inserimento, modifica e cancellazione) nel DB di destinazione 
	 * delle colonne e delle chiavi definite per la tabella il cui identificativo è passato a parametro.
	 * @param session La corrente sessione di Hibernate
	 * @param newColumns L'HashMap delle colonne definite per la tabella
	 * @param newDcAtts L'HashMap delle colonne definite come chiave per la tabella
	 * @param tableId L'identificativo della tabella
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void saveColumns(Session session, HashMap<String, Column> newColumns, HashMap<String, Key> newDcAtts, 
							Long tableId) throws Exception {
		HashMap<String, Column> oldColumns = getDestinationColumns(tableId);
		HashMap<String, Key> oldDcAtts = getKeys(tableId);
		// colonne
		Set<String> newKeys = newColumns.keySet();
		Set<String> oldKeys = oldColumns.keySet();
		for (String newKey : newKeys) {
			Column newColumn = newColumns.get(newKey);
			newColumn.setTableId(tableId);
			if (newColumn.getLongDesc() != null && newColumn.getLongDesc().equals(""))
				//newColumn.setLongDesc(null);
				newColumn.setLongDesc(newColumn.getName()); //default
			if (newColumn.getAlias() != null && newColumn.getAlias().equals(""))
				newColumn.setAlias(null);
			boolean isKey = newDcAtts.containsKey(newKey);
			Key dcAtt = isKey ? newDcAtts.get(newKey) : null;
			saveOrUpdateColumn(session, newColumn, dcAtt, tableId);
		}
		boolean trovata;
		for (String oldKey : oldKeys) {
			trovata = false;
			for (String newKey : newKeys) {
				if (newKey.equalsIgnoreCase(oldKey)) {
					trovata = true;
					break;
				}
			}
			if (!trovata) {
				deleteColumn(session, oldColumns.get(oldKey), oldDcAtts);
			}
		}
		// chiavi
		newKeys = newDcAtts.keySet();
		oldKeys = oldDcAtts.keySet();
		int count = 1; //l'ordine delle chiavi è in base 1
		for (String newKey : newKeys) {
			Key newDcAtt = newDcAtts.get(newKey);
			saveOrUpdateKey(session, newDcAtt, count);
			count++;
		}
		for (String oldKey : oldKeys) {
			trovata = false;
			for (String newKey : newKeys) {
				if (newKey.equalsIgnoreCase(oldKey)) {
					trovata = true;
					break;
				}
			}
			if (!trovata) {
				deleteKey(session, oldDcAtts.get(oldKey));
			}
		}
	}

	/**
	 Effettua il salvataggio (inserimento o modifica) nel DB di destinazione della colonna e della chiave passate a 
	 parametro.
	 * @param session La corrente sessione di Hibernate
	 * @param column La colonna da inserire o modificare
	 * @param key La chiave da inserire o modificare
	 * @param tableId L'identificativo della tabella
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void saveOrUpdateColumn(Session session, Column column, Key key, Long tableId) throws Exception {
		DcExpression dcExpression = new DcExpression();
		// inserimento o modifica in DC_EXPRESSION
		if (column.getId() != null) // cioè se sono in modifica: in inserimento lo assegna automaticamente
			dcExpression.setId(column.getId());
		dcExpression.setExpression(getExpressionFromColumn(session, column, tableId));
		dcExpression.setAlias(column.getAlias());
		dcExpression.setColType(column.getColType());
		session.saveOrUpdate(dcExpression);
		// inserimento o modifica in DC_COLUMN
		DcColumn dcColumn = new DcColumn();
		if (column.getId() != null) { // cioè se sono in modifica: devo trovare l'id del record di DC_COLUMN da modificare
			Query q = session.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.dcExpression=:dcExpression");
			DcColumn dcc = new DcColumn();
			dcc.setDcExpression(dcExpression);
			q.setProperties(dcc);
			List dccs = q.list();
			Iterator it = dccs.iterator();
			while (it.hasNext()) {
				/* N.B.: in questo caso (aggiornamento sia in tabella "madre" che in tabella "figlia")
				 * uso lo stesso oggetto valorizzato nell'iterazione per effettuare in seguito l'aggiornamento.
				 * Infatti viceversa (con un DcColumn diverso) otterrei una NonUniqueObjectException, in quanto
				 * la query di selezione precedente mi "bloccherebbe" l'id della DcColumn che intendo
				 * modificare in seguito. Verificare se è questa la soluzione migliore (Filippo 08.03.06) */
				dcColumn = (DcColumn) it.next();
			}
		}
		dcColumn.setDcExpression(dcExpression);
		DcEntity dcEntity = (DcEntity) session.load(DcEntity.class, column.getTableId());
		dcColumn.setDcEntity(dcEntity);
		dcColumn.setLongDesc(column.getLongDesc());
		session.saveOrUpdate(dcColumn);
		/* se la colonna è parte della chiave, nell'oggetto Key corrispondente va settata la FK_DC_COLUMN; potrebbe
		 * trattarsi infatti di una colonna "nuova" (aggiunta tramite l'applicazione e poi aggiunta alla chiave) */
		if (key != null)
			key.setColumnId(dcColumn.getId());
		// inserimento (se in modifica non è necessario) in DC_ATTRIBUTE per salvare il valore di NULLABLE
		if (column.getId() == null) {
			DcAttribute newDcAtt = new DcAttribute();
			newDcAtt.setDcColumn(dcColumn);
			newDcAtt.setAttributeSpec(NULLABLE);
			newDcAtt.setAttributeVal(column.getNullable());
			session.saveOrUpdate(newDcAtt);
		}
	}

	/**
	 * Effettua la cancellazione dal DB di destinazione della colonna passata a parametro.<p>
	 * Se la colonna è definita come chiave, la cancella anche dalla tabella delle chiavi.
	 * @param session La corrente sessione di Hibernate
	 * @param column La colonna da cancellare
	 * @param oldDcAtts HashMap che contiene l'elenco delle colonne definite come chiave per la tabella di cui fa parte 
	 * la colonna da cancellare
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void deleteColumn(Session session, Column column, HashMap<String, Key> oldDcAtts) throws Exception {
		Object dcExpression = session.load(DcExpression.class, column.getId());
		// cancello prima da DC_ATTRIBUTE e DC_COLUMN perché c'è la chiave esterna
		Query qCol = session.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.dcExpression=:dcExpression");
		DcColumn dcc = new DcColumn();
		dcc.setDcExpression((DcExpression) dcExpression);
		qCol.setProperties(dcc);
		List dccs = qCol.list();
		Iterator itCol = dccs.iterator();
		while (itCol.hasNext()) {
			DcColumn elementCol = (DcColumn) itCol.next();
			// cancello prima da DC_ATTRIBUTE perché è in chiave esterna con DC_COLUMN
			Query qDcAtt = session.createQuery("from DC_ATTRIBUTE in class DcAttribute where DC_ATTRIBUTE.dcColumn=:dcColumn and DC_ATTRIBUTE.attributeSpec=:attributeSpec");
			// per il valore di nullable:
			DcAttribute dcatt = new DcAttribute();
			dcatt.setDcColumn(elementCol);
			dcatt.setAttributeSpec(NULLABLE);
			qDcAtt.setProperties(dcatt);
			List dcatts = qDcAtt.list();
			Iterator itAtts = dcatts.iterator();
			while (itAtts.hasNext()) {
				DcAttribute elementKey = (DcAttribute) itAtts.next();
				session.delete(elementKey);
			}
			// se è chiave:
			dcatt = new DcAttribute();
			dcatt.setDcColumn(elementCol);
			dcatt.setAttributeSpec(KEY);
			qDcAtt.setProperties(dcatt);
			dcatts = qDcAtt.list();
			itAtts = dcatts.iterator();
			while (itAtts.hasNext()) {
				DcAttribute elementKey = (DcAttribute) itAtts.next();
				session.delete(elementKey);
				/* cancello, se presente, la colonna da oldDcAtts (chiavi) per evitare che venga chiamata due volte
				 * (con eccezione) la cancellazione della stessa colonna chiave */
				if (oldDcAtts.containsKey(column.getName()))
					oldDcAtts.remove(column.getName());
			}
			// ora cancello da DC_COLUMN
			session.delete(elementCol);
		}
		// ora cancello da DC_EXPRESSION
		session.delete(dcExpression);
	}

	/**
	 * Inserisce o modifica nel DB di destinazione la chiave passata a parametro.
	 * @param session La corrente sessione di Hibernate
	 * @param key La chiave da inserire o modificare
	 * @param sortOrder Il valore per l'ordinamento della chiave (ordinamento automatico in base 1)
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void saveOrUpdateKey(Session session, Key key, int sortOrder) throws Exception {
		DcAttribute dcAtt = new DcAttribute();
		if (key.getId() != null) // cioè se sono in modifica: in inserimento lo assegna automaticamente
			dcAtt.setId(key.getId());
		DcColumn dcCol = (DcColumn) session.load(DcColumn.class, key.getColumnId());
		dcAtt.setDcColumn(dcCol);
		dcAtt.setAttributeSpec(KEY);
		dcAtt.setAttributeVal("" + sortOrder);
		session.saveOrUpdate(dcAtt);
	}

	/**
	 * Cancella dal DB di destinazione la chiave passata a parametro.
	 * @param session La corrente sessione di Hibernate
	 * @param key La chiave da cancellare
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void deleteKey(Session session, Key key) throws Exception {
		session.delete(session.load(DcAttribute.class, key.getId()));
	}

	/**
	 * Verifica se nel DB di destinazione è definita la connessione di default al DB di origine.
	 * @return Un flag che indica se nel DB di destinazione è definita la connessione di default al DB di origine.
	 */
	public boolean hasDefaultConnection() {
		Connection testConn = null;
		try {
			testConn = getDefaultConn();
		} catch (Exception e) {
			// in questo caso non deve fare nulla, solo restituire false
			e.printStackTrace();
		} finally {
			boolean retVal = (testConn != null);
			try {
				if (testConn != null && !testConn.isClosed())
					testConn.close();
				return retVal;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Apre una Transaction Hibernate per la gestione delle operazioni di salvataggio delle entità definite nella 
	 * pagina di mappatura tabelle.<p>
	 * Effettua quindi, tramite la chiamata ai metodi privati specifici, il salvataggio, nel DB di destinazione, 
	 * degli oggetti Table contenuti nell'HashMap parametro; per ogni Table, con modalità analoghe, viene effettuato 
	 * il salvataggio delle colonne e delle chiavi definite nella pagina di mappatura colonne.<p>
	 * Al termine, viene effettuato il commit della transazione (o il rollback se si verifica una qualche eccezione).
	 * @param newTables HashMap di chiavi String e valori Table, contenente i dati da salvare nel DB di destinazione.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void saveTables(HashMap<String, Table> newTables) throws Exception {
		errEntities = new ArrayList<String>();
		HashMap<String, Table> oldTables = getDestinationTables();
		Session session = null;
		Transaction t = null;
		//Variabili per il salvataggio dei diritti
		RightsManager rm= new RightsManager();
		SessionFactory rsf= new Configuration().configure(EntitiesDBManager.RIGHTS_SESSION_FACTORY_CONF_FILE).buildSessionFactory();
		Session rightSession = null;
		Transaction rT = null;
		ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
		Principal principal = externalContext.getUserPrincipal();
		String context =externalContext.getRequestContextPath().substring(1);
		/** TODO: CAMBIARE GESTIONE PERMESSI 
		String ente=GestionePermessi.getEnteFromContext(principal,context);
		*/
		// Fine diritti
		try {
			//Diritti su AMPROFILE
			rightSession = rsf.openSession();
			rT = rightSession.beginTransaction();

			session = sf.openSession();
			t = session.beginTransaction();
			Set<String> newKeys = newTables.keySet();
			Set<String> oldKeys = oldTables.keySet();
			for (String newKey : newKeys) {
				Table newTable = newTables.get(newKey);
				if (newTable.getLongDesc() != null && newTable.getLongDesc().equals(""))
					newTable.setLongDesc(null);
				boolean isNewEntity=newTable.getId()==null;
				HashMap<String, Column> columnsTest = newTable.getColumns();
				boolean nonConfigurata = columnsTest.keySet().size() == 1 && columnsTest.containsKey(NON_CONFIGURATA) && columnsTest.get(NON_CONFIGURATA) == null;
				if (isNewEntity || !nonConfigurata) {
					saveOrUpdateTable(session, newTable);
				}				
				//Inserisco i diritti
				/* TODO : rifare gestioni permessi 
				if(isNewEntity){
					ByteArrayInputStream fis = (ByteArrayInputStream)externalContext.getResourceAsStream(EntitiesDBManager.APPLICATION_XML_RELATIVE_PATH);
					AmApplication application = (AmApplication) rm.decode(fis,newTable.getName(),ente,principal.getName());
					rm.saveOrUpdateAmApplication(application,rightSession,principal.getName());
				}
				*/
				//fine Diritti
			}
			boolean trovata;
			for (String oldKey : oldKeys) {
				trovata = false;
				for (String newKey : newKeys) {
					if (newKey.equalsIgnoreCase(oldKey)) {
						trovata = true;
						break;
					}
				}
				if (!trovata) {
					/* TODO CAMBIARE GESTIONE DIRITTI
					//Elimino i diritti
					Query qItem = rightSession.createQuery("from AmItem i where i.name=:name");
					qItem.setParameter("name",oldTables.get(oldKey).getName()+" ("+ente+")");
					AmItem amItem =(AmItem)qItem.uniqueResult();
					rm.deleteAmItem(amItem,rightSession);
					//fine  diritti
					*/
					deleteTable(session, oldTables.get(oldKey));
				}
			}
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack())
				t.commit();
			if (rT != null && !rT.wasCommitted() && !rT.wasRolledBack())
				rT.commit();
		} catch (Exception e) {
			if (t != null && !t.wasCommitted() && !t.wasRolledBack())
				t.rollback();
			if (rT != null && !rT.wasCommitted() && !rT.wasRolledBack())
				rT.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
			if (rightSession != null && rightSession.isOpen())
				rightSession.close();
		}
	}

	/**
	 * Inserisce o modifica nel DB di destinazione la tabella passata a parametro.
	 * @param session La corrente sessione di Hibernate
	 * @param table La tabella da inserire o modificare
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void saveOrUpdateTable(Session session, Table table) throws Exception {
		/* query preliminare per recuperare la data di sistema e verificare se l'entità è stata nel frattempo modificata 
		  da un altro utente */
		SQLQuery countQuery = session.createSQLQuery("select count(*), to_char(sysdate,'dd/mm/yyyy hh24:mi:ss') from DC_ENTITY where id=:id and dt_mod=:data");
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (table.getId() != null && table.getDtMod() != null) {
			//modifica
			countQuery.setParameter("id", table.getId());
			countQuery.setParameter("data", table.getDtMod());
		}else{
			//inserimento: in questo caso la count è ininfluente, serve solo la sysdate, metto parametri fittizi
			countQuery.setParameter("id", new Long(-1));
			countQuery.setParameter("data", sf.parse("01/01/1000 00:00:01"));
		}
		Object[] rst = (Object[])countQuery.list().iterator().next();
		int count = ((BigDecimal)rst[0]).intValue();
		java.util.Date sysDt = sf.parse((String)rst[1]);
		//inizio salvataggio
		DcEntity dcEntity = new DcEntity();
		if (table.getId() != null) {// cioè se sono in modifica
			dcEntity = (DcEntity)session.load(DcEntity.class, table.getId());
		}
		dcEntity.setName(table.getName());
		dcEntity.setLongDesc(table.getLongDesc());
		dcEntity.setOwner(table.getOwner()); //todo
		if (table.getId() == null) {// cioè se sono in inserimento
			dcEntity.setUserIns(null); //todo
			dcEntity.setDtIns(sysDt);
			dcEntity.setUserMod(null); //todo
			dcEntity.setDtMod(sysDt);
		}else{ //se sono in modifica
			//controllo se l'entità è stata nel frattempo modificata (se è stata modificata, count è uguale a 0)
			if (count == 0) {
				errEntities.add(dcEntity.getName());
				return;
			}
			if (table.isUpdated()) {
				dcEntity.setUserMod(null); //todo
				dcEntity.setDtMod(sysDt);
			}
		}
		session.saveOrUpdate(dcEntity);
		DcSchemaEntity dcSchemaEntity = null;
		if (table.getId() == null) {
			dcSchemaEntity = new DcSchemaEntity();
			dcSchemaEntity.setDcEntity(dcEntity);
		} else {
			Query q = session.createQuery("from DC_SCHEMA_ENTITY in class DcSchemaEntity where DC_SCHEMA_ENTITY.dcEntity=:dcEntity");
			DcSchemaEntity dcSchEntParam = new DcSchemaEntity();
			dcSchEntParam.setDcEntity(dcEntity);
			q.setProperties(dcSchEntParam);
			List list = q.list();
			Iterator it = list.iterator();
			// è una relazione 1..1
			if (it.hasNext()) {
				dcSchemaEntity = (DcSchemaEntity) it.next();
			}
		}
		dcSchemaEntity.setSqlName(table.getSqlName());
		session.saveOrUpdate(dcSchemaEntity);
		//serve per il salvataggio successivo di colonne e chiavi (se siamo in inserimento)
		if (table.getId() == null) {
			table.setId(dcEntity.getId());
		}
		//salvataggio colonne e chiavi
		saveColumns(session, table.getColumns(), table.getKeys(), table.getId());
	}

	/**
	 * Cancella dal DB di destinazione la tabella passata a parametro.
	 * @param session La corrente sessione di Hibernate
	 * @param table La tabella da cancellare
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */	
	private void deleteTable(Session session, Table table) throws Exception {
		Object dcEntity = session.load(DcEntity.class, table.getId());
		//cancello le colonne (e le chiavi) utilizzando saveColumns con due HashMap vuote
		saveColumns(session, new HashMap<String, Column>(), new HashMap<String, Key>(), table.getId());
		// cancello prima da DC_SCHEMA_ENTITY perché c'è la chiave esterna
		Query q = session.createQuery("from DC_SCHEMA_ENTITY in class DcSchemaEntity where DC_SCHEMA_ENTITY.dcEntity=:dcEntity");
		DcSchemaEntity dcse = new DcSchemaEntity();
		dcse.setDcEntity((DcEntity) dcEntity);
		q.setProperties(dcse);
		List dcses = q.list();
		Iterator it = dcses.iterator();
		while (it.hasNext()) {
			DcSchemaEntity element = (DcSchemaEntity) it.next();
			session.delete(element);
		}
		// ora cancello da DC_ENTITY
		session.delete(dcEntity);
	}
	
	/**
	 * Legge la tabella DC_ATTRIBUTE e restituisce un oggetto DcAttribute che rappresenta il record con FK_DC_COLUMN 
	 * uguale a dcColumnId parametro e ATTRIBUTE_SPEC uguale a "NULLABLE" (restituisce null se non è presente nessun 
	 * record per i parametri specificati).
	 * @param session La corrente sessione di Hibernate
	 * @param dcColumnId L'identificativo della DC_COLUMN della colonna in questione
	 * @return Un oggetto DcAttribute che rappresenta il record con FK_DC_COLUMN uguale a dcColumnId parametro e 
	 * ATTRIBUTE_SPEC uguale a "NULLABLE" (null se non presente).
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public DcAttribute getNullable(Session session, Long dcColumnId) throws Exception {
		DcAttribute retVal = null;
		Query q = session.createQuery("from DC_ATTRIBUTE in class DcAttribute where DC_ATTRIBUTE.dcColumn=:dcColumn and DC_ATTRIBUTE.attributeSpec=:attributeSpec");
		DcAttribute dcatt = new DcAttribute();
		DcColumn dccol = (DcColumn)session.load(DcColumn.class, dcColumnId);
		dcatt.setDcColumn(dccol);
		dcatt.setAttributeSpec(NULLABLE);
		q.setProperties(dcatt);
		List dcatts = q.list();
		Iterator it = dcatts.iterator();
		//dovrebbe esserci un solo record per i parametri specificati...
		if (it.hasNext()) {
			retVal = (DcAttribute)it.next();
		}
		return retVal;
	}

	/**
	 * Dati un oggetto Column e l'identificativo di un'entità (tabella), restituisce un array di byte appropriato 
	 * per l'inserimento del valore EXPRESSION (campo di tipo XMLTYPE) nella tabella DC_EXPRESSION del DB di 
	 * destinazione.
	 * @param session La corrente sessione di Hibernate
	 * @param column L'oggetto Column
	 * @param tableId L'identificativo della tabella
	 * @return L'array di byte per l'inserimento nel campo XMLTYPE
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	protected byte[] getExpressionFromColumn(Session session, Column column, Long tableId) throws Exception {
		String exp = column.getName();
		String des = column.getLongDesc();
		String alias = null;
		DcEntity dcEntity = (DcEntity) session.load(DcEntity.class, tableId);
		String tableExp = null;
		Set dcSchEnts = dcEntity.getDcSchemaEntities();
		Iterator itSchEnts = dcSchEnts.iterator();
		// è una relazione 1..1
		if (itSchEnts.hasNext()) {
			DcSchemaEntity dcSchEnt = (DcSchemaEntity) itSchEnts.next();
			tableExp = dcSchEnt.getSqlName();
		}
		String tableDes = dcEntity.getLongDesc();
		String tableAlias = null;
		it.webred.diogene.sqldiagram.Table table = new it.webred.diogene.sqldiagram.TableExpression(tableId, tableExp, tableDes, tableAlias);
		Class myClass = Class.forName(column.getColType());
		it.webred.diogene.sqldiagram.EnumsRepository.ValueType valueType = it.webred.diogene.sqldiagram.EnumsRepository.ValueType.mapJavaType2ValueType(myClass);
		it.webred.diogene.sqldiagram.Column sqlColumn = new it.webred.diogene.sqldiagram.Column(exp, des, alias, table, valueType);
		return ValueExpression.getOperandXmlString(sqlColumn).getBytes();
	}

	/**
	 * Dato un array di byte (ricavato dalla lettura del valore EXPRESSION, campo di tipo XMLTYPE, nella tabella 
	 * DC_EXPRESSION del DB di destinazione), restituisce una String da utilizzarsi per la visualizzazione della 
	 * descrizione della colonna corrispondente.
	 * @param expr L'array di byte ricavato dalla lettura del valore per il campo XMLTYPE
	 * @return La descrizione della colonna
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	protected String getColumnNameFromExpression(byte[] expr) throws Exception {
		String xml = new String(expr, UTF8_XML_ENCODING);
		SAXBuilder builder = new SAXBuilder();
		List content = builder.build(new StringReader(xml)).getContent();
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
						if (element2.getName().equals("field")) {
							List content3 = element2.getChildren();
							Iterator it3 = content3.iterator();
							while (it3.hasNext()) {
								Element element3 = (Element) it3.next();
								if (element3.getName().equals("name"))
									return element3.getText();
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Restituisce un oggetto Connection che rappresenta la connessione di default al DB.
	 * @return L'oggetto Connection che rappresenta la connessione di default al DB.
	 */
	protected Connection getDefaultConn() {
		try {
			return DataJdbcConnection.getConnectionn("DWH");
		}catch (Exception e) {
			System.out.println("Impossibile istanziare la connessione di default");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Restituisce il nome utente della connessione di default al DB.
	 * @return Il nome utente della connessione di default al DB.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	public String getDefaultConnUsername() throws Exception {
		Connection conn = null;
		String retVal = "";
		try {
			conn = getDefaultConn();
			retVal = conn.getMetaData().getUserName();
		} catch (Exception e) {
			//non deve fare nulla, solo restituire ""
			e.printStackTrace();
		} finally{
			mf.closeConnection(conn);
		}
		return retVal;
	}

	public ArrayList<String> getErrEntities() {
		return errEntities;
	}

	public void setErrEntities(ArrayList<String> errEntities) {
		this.errEntities = errEntities;
	}
	
}
