package it.webred.diogene.visualizzatore.model;

import static it.webred.utils.StringUtils.CRLF;
import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import it.webred.diogene.db.DcAttributes;
import it.webred.diogene.db.model.*;
import it.webred.diogene.metadata.beans.*;
import it.webred.diogene.visualizzatore.beans.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Element;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalla pagina di visualizzazione del diagramma ad albero 
 * della struttura progetti - classi, nonché dalle popup di gestione (inserimento - modifica) di singoli progetti 
 * o classi. Contiene inoltre i metodi statici per la restituzione di valori fissi utilizzati in queste pagine.
 * @author Filippo Mazzini
 * @version $Revision: 1.3 $ $Date: 2007/11/22 11:33:50 $
 */
public class DefinizioneEntitaModel {
	
	/**
	 *	Oggetto Configuration di Hibernate.
	 */
	Configuration c;
	/**
	 *	Oggetto SessionFactory di Hibernate.
	 */
	SessionFactory sf;
	/**
	 *	Costante di tipo String.
	 */
	public static String MOVE_UP = "MOVE_UP";
	/**
	 *	Costante di tipo String.
	 */
	public static String MOVE_DOWN = "MOVE_DOWN";
	/**
	 *	Costante di tipo String.
	 */
	public static String PROJECT = "PROJECT";
	/**
	 *	Costante di tipo String.
	 */
	public static String CLASS = "CLASS";
	/**
	 *	Costante di tipo String.
	 */
	public static String COLUMNS = "COLUMNS";
	/**
	 *	Costante di tipo String.
	 */
	public static String OPERATORS = "OPERATORS";
	/**
	 *	Costante di tipo String.
	 */
	public static String SQL = "sql";
	/**
	 *	Costante di tipo String.
	 */
	public static String VALORI_FISSI = "valori fissi";
	/**
	 *	Costante di tipo String.
	 */
	public static String VALORE_LIBERO = "valore libero";
	/**
	 *	Costante di tipo String.
	 */
	public static String SELECTED_ENTITY = "SELECTED ENTITY";
	/**
	 *	Costante di tipo String.
	 */
	public static String FORMAT_CLASSE = "FORMAT CLASSE";
	/**
	 *	Costante di tipo String.
	 */
	public static String FILTER_TITLE = "FILTER TITLE";
	/**
	 *	Costante di tipo String.
	 */
	public static String LIST_TITLE = "LIST TITLE";
	/**
	 *	Costante di tipo String.
	 */
	public static String DETAIL_TITLE = "DETAIL TITLE";
	/**
	 *	Costante di tipo String.
	 */
	public static String FILTER_AL = "FILTER AL";
	/**
	 *	Costante di tipo String.
	 */
	public static String LIST_AL = "LIST AL";
	/**
	 *	Costante di tipo String.
	 */
	public static String DETAIL_AL = "DETAIL AL";
	/**
	 * Costante che identifica l'ATTRIBUTE_SPEC delle colonne definite come chiave.
	 */
	public static final String KEY = DcAttributes.KEY.name();
	
	/**
	 * Costante di tipo HashMap.<p>
	 * Contiene le diciture utilizzate per la generazione dell'array di byte (sintassi XML) usato per l'inserimento 
	 * o la modifica del valore del campo FILTER (di tipo XMLTYPE) in tabella DV_FORMAT_CLASSE.
	 */
	public static HashMap<String, String> FILTER_XML_LABELS = new HashMap<String, String>();
	static {
		FILTER_XML_LABELS.put("VIEW", "view");
		FILTER_XML_LABELS.put("TYPE", "type");
		FILTER_XML_LABELS.put("TYPE_VALUE", "filter");
		FILTER_XML_LABELS.put("TITLE", "title");
		FILTER_XML_LABELS.put("COLUMNS", "columns");
		FILTER_XML_LABELS.put("COLUMN", "column");
		FILTER_XML_LABELS.put("DCCOLUMN", "dccolumn");
		FILTER_XML_LABELS.put("OPERATORS", "operators");
		FILTER_XML_LABELS.put("VALUES", "values");
		FILTER_XML_LABELS.put("VALUE", "value");
		FILTER_XML_LABELS.put("ID", "id");
		FILTER_XML_LABELS.put("DESCRIPTION", "description");
		FILTER_XML_LABELS.put("LISTVALUES", "listvalues");
		FILTER_XML_LABELS.put("REQUIRED", "required");
		FILTER_XML_LABELS.put("SQL", "sql");
	}
	/**
	 * Costante di tipo HashMap.<p>
	 * Contiene le diciture utilizzate per la generazione dell'array di byte (sintassi XML) usato per l'inserimento 
	 * o la modifica del valore del campo LIST (di tipo XMLTYPE) in tabella DV_FORMAT_CLASSE.
	 */
	public static HashMap<String, String> LIST_XML_LABELS = new HashMap<String, String>();
	static {
		LIST_XML_LABELS.put("VIEW", "view");
		LIST_XML_LABELS.put("TYPE", "type");
		LIST_XML_LABELS.put("TYPE_VALUE", "list");
		LIST_XML_LABELS.put("TITLE", "title");
		LIST_XML_LABELS.put("COLUMNS", "columns");
		LIST_XML_LABELS.put("COLUMN", "column");
		LIST_XML_LABELS.put("DCCOLUMN", "dccolumn");
		LIST_XML_LABELS.put("HEADER", "header");
		LIST_XML_LABELS.put("SCRIPT", "script");
		LIST_XML_LABELS.put("IMAGEURL", "imageurl");
		LIST_XML_LABELS.put("TESTURL", "testurl");
		LIST_XML_LABELS.put("PARAM", "param");
		//LIST_XML_LABELS.put("HIDDENKEY", "hiddenKey");
		//LIST_XML_LABELS.put("HIDDENKEY_VALUE", "true");
	}
	
	/**
	 * Costante di tipo HashMap.<p>
	 * Contiene le diciture utilizzate per la generazione dell'array di byte (sintassi XML) usato per l'inserimento 
	 * o la modifica del valore del campo DETAIL (di tipo XMLTYPE) in tabella DV_FORMAT_CLASSE.
	 */
	public static HashMap<String, String> DETAIL_XML_LABELS = new HashMap<String, String>();
	static {
		DETAIL_XML_LABELS.put("VIEW", "view");
		DETAIL_XML_LABELS.put("TYPE", "type");
		DETAIL_XML_LABELS.put("TYPE_VALUE", "detail");
		DETAIL_XML_LABELS.put("TITLE", "title");
		DETAIL_XML_LABELS.put("TABLE", "table");
		DETAIL_XML_LABELS.put("ROW", "row");
		DETAIL_XML_LABELS.put("COLUMN", "column");
		DETAIL_XML_LABELS.put("DCCOLUMN", "dccolumn");
		DETAIL_XML_LABELS.put("BREAK_VALUE", "<hr>");
	}
	
	/**
	 * HashMap con chiavi di tipo String e valori di tipo ArrayList di GenericValue.<p>
	 * Le chiavi sono uguali al nome della classe java che corrisponde al tipo SQL di una colonna selezionata per il 
	 * filtro.<p>
	 * Il valore corrispondente ad una chiave è la lista degli operatori ammessi per il tipo SQL a cui corrisponde il 
	 * nome della classe java che costituisce la chiave.
	 */
	public static HashMap<String, ArrayList<GenericValue>> typeOperators =
		new HashMap<String, ArrayList<GenericValue>>();
	
	static {
		String key = "default";
		ArrayList<GenericValue> values = new ArrayList<GenericValue>();
		values.add(new GenericValue("<", "minore"));
		values.add(new GenericValue(">", "maggiore"));
		values.add(new GenericValue("=", "uguale"));
		values.add(new GenericValue("<>", "diverso"));
		typeOperators.put(key, values);
		key = "java.lang.String";
		values = new ArrayList<GenericValue>();
		values.add(new GenericValue("=", "uguale"));
		values.add(new GenericValue("like", "like"));
		typeOperators.put(key, values);
		//todo eventuali altri tipi
	}
	
	/**
	*	Costruttore che inizializza gli oggetti Configuration e SessionFactory.
	*/
	public DefinizioneEntitaModel() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}
	
	/**
	 * Dato il nome di una classe java (o la stringa "default") restituisce la lista degli operatori corrispondente 
	 * nell'HashMap typeOperators.
	 * @param className Nome di una classe java (o la stringa "default")
	 * @return La lista degli operatori corrispondente nell'HashMap typeOperators
	 */
	private ArrayList<GenericValue> getTypeOperators(String className) {
		boolean containsKey = typeOperators.containsKey(className);
		return containsKey ? typeOperators.get(className) : typeOperators.get("default");
	}
	
	/**
	 * Restituisce l'ArrayList di SelectItem da utilizzare per la valorizzazione della combo box tipo valori.
	 * @return L'ArrayList di SelectItem da utilizzare per la valorizzazione della combo box tipo valori
	 */
	public static ArrayList<SelectItem> getValueTypes() {
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		retVal.add(new SelectItem(SQL));
		retVal.add(new SelectItem(VALORI_FISSI));
		retVal.add(new SelectItem(VALORE_LIBERO));
		return retVal;
	}
	
	/**
	 * Metodo ricorsivo per la costruzione dell'albero di visualizzazione della struttura progetti - classi.<p>
	 * Per ogni elemento trovato (progetti o classi a seconda del parametro level) chiama se stesso al livello 
	 * immediatamente successivo.
	 * @param proClas ArrayList di ProgettoClasse che costituisce l'elenco degli elementi inclusi nell'albero
	 * @param id Identificativo del progetto o della classe di cui bisogna trovare la lista delle classi incluse al
	 * livello immediatamente successivo (se level è uguale a -2 si parte dall'esterno e quindi si caricano i progetti).
	 * @param level Il livello (-2 per caricare i progetti, -1 e successivi per le classi).
	 * @param session La corrente sessione di Hibernate
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void getAllProjectsClasses(ArrayList<ProgettoClasse> proClas, long id, short level, Session session) throws Exception {
		try {
			Query q = null;
			List list = null;
			Iterator it = null;
			ProgettoClasse proCla = null;
			if (level == -2) {
				//progetti
				q = session.createQuery("from DV_PROGETTO in class DvProgetto order by SORT_ORDER");
				DvProgetto dvpro = new DvProgetto();
				q.setProperties(dvpro);
			} else {
				DvClasse dvcla = new DvClasse();
				if (level == -1) {
					//classi del livello più esterno
					q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null order by SORT_ORDER");
					DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, new Long(id));
					dvcla.setDvProgetto(dvpro);
				} else {
					//altre classi
					q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse order by SORT_ORDER");
					DvClasse dvclaFk = (DvClasse)session.load(DvClasse.class, new Long(id));
					dvcla.setDvClasse(dvclaFk);
				}
				q.setProperties(dvcla);
			}
			list = q.list();
			it = list.iterator();
			while (it.hasNext()) {
				proCla = new ProgettoClasse();
				if (level == -2) {
					DvProgetto element = (DvProgetto)it.next();					
					proCla.setId(element.getId());
					proCla.setName(element.getName());
					proCla.setSortOrder(new Short(element.getSortOrder().shortValue()));
				} else {
					DvClasse element = (DvClasse)it.next();
					proCla.setId(element.getId());
					proCla.setName(element.getName());
					proCla.setSortOrder(new Short(element.getSortOrder().shortValue()));
				}
				proCla.setLevel(new Short((short)(level + 1)));					
				proCla.setExpanded(false);
				proCla.setIconPath("/visualizzatore/img/expand.gif");
				setIconPath(proCla, session);
				proClas.add(proCla);
				//chiamata ricorsiva per costruire l'intero albero
				getAllProjectsClasses(proClas, proCla.getId().longValue(), proCla.getLevel().shortValue(), session);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/** 
	 * Metodo ricorsivo per la definizione della lista delle classi da cancellare alla cancellazione di un progetto o 
	 * di una classe.<p>
	 * Per ogni elemento trovato chiama se stesso al livello immediatamente successivo.
	 * @param proClas ArrayList di ProgettoClasse che costituisce l'elenco delle classi da cancellare contestualmente 
	 * alla cancellazione di un progetto o di una classe.
	 * @param id Identificativo del progetto o della classe di cui bisogna trovare la lista delle classi incluse al
	 * livello immediatamente successivo (se level è uguale a -1 cerco le classi incluse in un progetto, altrimenti 
	 * le classi incluse in una classe).
	 * @param level Il livello (-1 se ricerca per progetto, 0 e successivi se ricerca per classi).
	 * @param session La corrente sessione di Hibernate
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void readProClasToDelete(ArrayList<ProgettoClasse> proClas, long id, short level, Session session) throws Exception {
		try {
			ProgettoClasse proCla = new ProgettoClasse();
			if (level == -1) {
				//progetto
				DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, new Long(id));
				proCla.setId(dvpro.getId());
				proCla.setName(dvpro.getName()); //serve più che altro per debug
			} else {
				//classe
				DvClasse dvcla = (DvClasse)session.load(DvClasse.class, new Long(id));
				proCla.setId(dvcla.getId());
				proCla.setName(dvcla.getName()); //serve più che altro per debug
			}
			proCla.setLevel(new Short(level));
			proClas.add(proCla);
			Query q = null;
			List list = null;
			Iterator it = null;
			DvClasse dvcla = new DvClasse();
			if (level == -1) {
				//classi per progetto
				q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null");
				DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, new Long(id));
				dvcla.setDvProgetto(dvpro);
			} else {
				//classi per classe
				q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse");
				DvClasse dvclaFk = (DvClasse)session.load(DvClasse.class, new Long(id));
				dvcla.setDvClasse(dvclaFk);
			}
			q.setProperties(dvcla);
			list = q.list();
			it = list.iterator();
			while (it.hasNext()) {
				DvClasse element = (DvClasse)it.next();
				//chiamata ricorsiva per costruire l'intero albero degli oggetti da cancellare
				readProClasToDelete(proClas, element.getId(), (short)(level + 1), session);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Dato un identificativo, restituisce l'oggetto Progetto corrispondente.
	 * @param id Identificativo del progetto
	 * @return L'oggetto Progetto corrispondente
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public Progetto getProject(Long id) throws Exception {
		Progetto retVal = new Progetto();
		Session session = null;
		try {
			session = sf.openSession();
			DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, id);
			retVal.setId(dvpro.getId());
			retVal.setName(dvpro.getName());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
		return retVal;
	}
	
	/**
	 * Dato un identificativo, restituisce l'oggetto Classe corrispondente.
	 * @param id Identificativo della classe
	 * @return L'oggetto Classe corrispondente
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public Classe getClazz(Long id) throws Exception {
		Classe retVal = new Classe();
		Session session = null;
		try {
			session = sf.openSession();
			DvClasse dvcla = (DvClasse)session.load(DvClasse.class, id);
			retVal.setId(dvcla.getId());
			retVal.setName(dvcla.getName());
			retVal.setDvClasse(dvcla.getDvClasse() == null ? null : dvcla.getDvClasse().getId());
			retVal.setDvClasseName(dvcla.getDvClasse() == null ? null : dvcla.getDvClasse().getName());
			retVal.setDvProgetto(dvcla.getDvProgetto() == null ? null : dvcla.getDvProgetto().getId());
			retVal.setDvProgettoName(dvcla.getDvProgetto() == null ? null : dvcla.getDvProgetto().getName());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
		return retVal;
	}
	
	/**
	 * Salva nel DB (inserimento o modifica) i dati contenuti nell'oggetto Progetto parametro.<p>
	 * Il salvataggio avviene tramite una Transaction Hibernate (apertura, e in chiusura commit o rollback a seconda 
	 * dell'esito dell'operazione).
	 * @param project L'oggetto Progetto contenente i dati da salvare
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void saveProject(Progetto project) throws Exception {
		Session session = null;
		Transaction t = null;
		try {
			session = sf.openSession();
			t = session.beginTransaction();
			DvProgetto dvpro = new DvProgetto();
			if (project.getId() != null) {
				//modifica
				dvpro = (DvProgetto)session.load(DvProgetto.class, project.getId());
			}else{
				//inserimento
				dvpro.setSortOrder(new Long(getNextSortOrder(PROJECT, session, null).shortValue()));
			}
			dvpro.setName(project.getName());
			session.saveOrUpdate(dvpro);
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.commit();
		} catch (Exception e) {
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
	}
	
	/**
	 * Salva nel DB (inserimento o modifica) i dati contenuti nell'oggetto Classe parametro.<p>
	 * Il salvataggio avviene tramite una Transaction Hibernate (apertura, e in chiusura commit o rollback a seconda 
	 * dell'esito dell'operazione).
	 * @param clazz L'oggetto Classe contenente i dati da salvare
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void saveClazz(Classe clazz) throws Exception {
		Session session = null;
		Transaction t = null;
		try {
			session = sf.openSession();
			t = session.beginTransaction();
			DvClasse dvcla = new DvClasse();
			if (clazz.getId() != null) {
				//modifica
				dvcla = (DvClasse)session.load(DvClasse.class, clazz.getId());
			}else{
				//inserimento
				if (clazz.getDvClasse() != null) {
					DvClasse dvclafk = (DvClasse)session.load(DvClasse.class, clazz.getDvClasse());
					dvcla.setDvClasse(dvclafk);
				}
				if (clazz.getDvProgetto() != null) {
					DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, clazz.getDvProgetto());
					dvcla.setDvProgetto(dvpro);
				}
				dvcla.setSortOrder(new Long(getNextSortOrder(CLASS, session, dvcla).shortValue()));
			}
			dvcla.setName(clazz.getName());
			session.saveOrUpdate(dvcla);
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.commit();
		} catch (Exception e) {
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
	}
	
	/**
	 * Salva nel DB (inserimento o modifica) i dati contenuti negli oggetti Classe e FormatClasse parametri.<p>
	 * Il salvataggio avviene tramite una Transaction Hibernate (apertura, e in chiusura commit o rollback a seconda 
	 * dell'esito dell'operazione).
	 * @param clazz L'oggetto Classe contenente i dati da salvare
	 * @param fc L'oggetto FormatClasse contenente i dati da salvare
	 */
	public void saveClazzAndFormat(Classe clazz, FormatClasse fc) {
		Session session = null;
		Transaction t = null;
		try {
			// questa parte, prima della transazione, serve a verificare in seguito se ho cambiato entità (dvUserEntity)
			Long oldDvUserEntityId = null;
			if (clazz.getId() != null) { // modifica
				HashMap<String, ? extends Object> selEntForCla = getSelectedEntityAndFormatClasse(clazz.getId());
				oldDvUserEntityId = ((FormatClasse)selEntForCla.get(DefinizioneEntitaModel.FORMAT_CLASSE)).getDvUserEntity();
			}
			// inizio transazione		
			session = sf.openSession();
			t = session.beginTransaction();
			//inserimento o modifica in DV_CLASSE
			DvClasse dvcla = new DvClasse();
			if (clazz.getId() != null) {
				//modifica
				dvcla = (DvClasse)session.load(DvClasse.class, clazz.getId());
			}else{
				//inserimento
				if (clazz.getDvClasse() != null) {
					DvClasse dvclafk = (DvClasse)session.load(DvClasse.class, clazz.getDvClasse());
					dvcla.setDvClasse(dvclafk);
				}
				if (clazz.getDvProgetto() != null) {
					DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, clazz.getDvProgetto());
					dvcla.setDvProgetto(dvpro);
				}
				dvcla.setSortOrder(new Long(getNextSortOrder(CLASS, session, dvcla).shortValue()));
			}
			dvcla.setName(clazz.getName());
			session.saveOrUpdate(dvcla);
			//inserimento o modifica in DV_FORMAT_CLASSE
			if (fc.getDvUserEntity() != null) {
				DvFormatClasse newFc = getRecordFromFormatClasse(session, dvcla.getId(), fc);
				//se sono in modifica
				if (clazz.getId() != null) {
					DvFormatClasseId dvforclaid = new DvFormatClasseId();
					dvforclaid.setFkDvClasse(clazz.getId().longValue());
					dvforclaid.setFkDvUserEntity(oldDvUserEntityId);
					DvFormatClasse oldFc = (DvFormatClasse)session.load(DvFormatClasse.class, dvforclaid);
					/* se ho cambiato entità (dvUserEntity) devo cancellare il vecchio record di DV_FORMAT_CLASSE
					 e poi inserire il nuovo (dvUserEntity fa parte della chiave)*/
					if (newFc.getId().getFkDvUserEntity().longValue() != oldFc.getId().getFkDvUserEntity().longValue())
						session.delete(oldFc);
				}
				session.saveOrUpdate(newFc);
			}
			//link
			saveLink(session, dvcla.getId(), fc.getLink());
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.commit();
		} catch (Exception e) {
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
	}
	
	/**
	 * Restituisce il primo numerico libero per essere assegnato come valore di ordinamento.
	 * @param mode Indica se si tratta del salvataggio di un progetto o di un classe
	 * @param session La corrente sessione di Hibernate
	 * @param dvcla Classe contenitore (oppure null)
	 * @return Il primo numerico libero (ordinamento da assegnare al progetto o alla classe)
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private Short getNextSortOrder(String mode, Session session, DvClasse dvcla) throws Exception {
		Short retVal = new Short((short)0);
		Query q = null;
		if (mode.equalsIgnoreCase(PROJECT)) {
			q = session.createQuery("select max(DV_PROGETTO.sortOrder) from DV_PROGETTO in class DvProgetto");
			DvProgetto dvpro = new DvProgetto();
			q.setProperties(dvpro);
		}else if (mode.equalsIgnoreCase(CLASS)) {
			if (dvcla.getDvClasse() == null) {
				//classe di primo livello
				q = session.createQuery("select max(DV_CLASSE.sortOrder) from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse is null and DV_CLASSE.dvProgetto=:dvProgetto");
			} else {
				q = session.createQuery("select max(DV_CLASSE.sortOrder) from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse");
			}
			q.setProperties(dvcla);
		}
		List list = q.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Long element = (Long)it.next();
			if (element != null)
				retVal = new Short(element.shortValue());
		}
		if (retVal.shortValue() < 999) {
			retVal = new Short((short)(retVal.shortValue() + 1));
		}
		return retVal;
	}

	/**
	 * Restituisce un'HashMap contenente due ArrayList di ProgettoClasse, il primo contenente i dati necessari per la 
	 * visualizzazione della struttura ad albero, il secondo contenente l'intera struttura progetti - classi (dal 
	 * confronto delle due liste viene generato il diagramma). 
	 * @param oldProjectsClasses Il precedente (ove presente) ArrayList di ProgettoClasse come visualizzato nel 
	 * diagramma
	 * @param allProjectsClasses L'intera struttura progetti - classi
	 * @param readDBForAllProCla Indica se bisogna leggere (se true) il DB per la costruzione della struttura o se 
	 * l'ArrayList generale (allProjectsClasses) è già stata valorizzata e bisogna solo rileggere i dati in memoria
	 * @return Un'HashMap contenente due ArrayList di ProgettoClasse (struttura visualizzata e struttura completa 
	 * dell'albero).
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public HashMap<String,ArrayList<ProgettoClasse>> getProjectsClasses(ArrayList<ProgettoClasse> oldProjectsClasses, ArrayList<ProgettoClasse> allProjectsClasses, boolean readDBForAllProCla) throws Exception {
		HashMap<String,ArrayList<ProgettoClasse>> retVal = new HashMap<String,ArrayList<ProgettoClasse>>();
		ArrayList<ProgettoClasse> newProjectsClasses = new ArrayList<ProgettoClasse>();
		Session session = null;
		try {
			session = sf.openSession();
			if (readDBForAllProCla) {
				allProjectsClasses = new ArrayList<ProgettoClasse>();
				getAllProjectsClasses(allProjectsClasses, -1, (short)-2, session);
			}
			boolean add = true;
			short level = -2;
			for (ProgettoClasse apc : allProjectsClasses) {
				if (oldProjectsClasses == null || oldProjectsClasses.size() == 0) {
					if (apc.getLevel() == (short)-1) {
						apc.setExpanded(false);
						apc.setIconPath("/visualizzatore/img/expand.gif");
						setIconPath(apc, session);
						newProjectsClasses.add(apc);
					}
				}else{					
					if (!add && apc.getLevel().shortValue() <= level) {
						add = true;
						level = -2;
					}
					if (add) {
						for (ProgettoClasse opc : oldProjectsClasses) {
							if (opc.getId().longValue() == apc.getId().longValue() && opc.getLevel().shortValue() == apc.getLevel().shortValue()) {
								apc.setExpanded(opc.isExpanded());
								break;
							}
						}
						apc.setIconPath(apc.isExpanded() ? "/visualizzatore/img/collapse.gif" : "/visualizzatore/img/expand.gif");
						if (!apc.isExpanded()) {
							add = false;
							level = apc.getLevel().shortValue();
						}
						setIconPath(apc, session);
						newProjectsClasses.add(apc);
					}					
				}
			}
			setRenderedButtons(newProjectsClasses, allProjectsClasses, session);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
		retVal.put("newProjectsClasses", newProjectsClasses);
		retVal.put("allProjectsClasses", allProjectsClasses);
		return retVal;			
	}
	
	/** 
	 * Imposta il percorso dell'icona (+, - , o nessuna) che nel diagramma ad albero affianca la descrizione di un 
	 * progetto o di una classe. 
	 * @param pc Il ProgettoClasse di cui va impostata l'icona
	 * @param session La corrente sessione di Hibernate
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void setIconPath(ProgettoClasse pc, Session session) throws Exception {
		boolean paramNull = (session == null);
		if (paramNull) {
			session = sf.openSession();
		}
		short level = pc.getLevel().shortValue();
		Query q = null;
		DvClasse dvcla = new DvClasse();			
		if (level == -1) {
			q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null order by SORT_ORDER");
			DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, pc.getId());
			dvcla.setDvProgetto(dvpro);
		} else {
			q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse order by SORT_ORDER");
			DvClasse dvclaFk = (DvClasse)session.load(DvClasse.class, pc.getId());
			dvcla.setDvClasse(dvclaFk);
		}
		q.setProperties(dvcla);
		List dvclas = q.list();
		Iterator it = dvclas.iterator();
		if (!it.hasNext()) {
			pc.setIconPath("");
		}
		if (paramNull && session != null && session.isOpen()) session.close();
	}
	
	/**
	 * Gestisce le azioni "Muovi su" e "Muovi già" (modifica dell'ordinamento di un progetto o di una classe).
	 * Il salvataggio avviene tramite una Transaction Hibernate (apertura, e in chiusura commit o rollback a seconda 
	 * dell'esito dell'operazione).
	 * @param id Identificativo dl progetto o della classe (a seconda del valore di level)
	 * @param level Se -1 si tratta di un progetto, altrimenti di una classe
	 * @param mode Indica di quale delle due azioni ("Muovi su" o "Muovi già") si tratta
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void move(long id, short level, String mode) throws Exception {
		Session session = null;
		Transaction t = null;
		try {
			session = sf.openSession();
			t = session.beginTransaction();
			int amount = (mode.equalsIgnoreCase(MOVE_UP) ? -1 : (mode.equalsIgnoreCase(MOVE_DOWN) ? 1 : 0));
			if (level == -1) {
				//progetto
				DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, new Long(id));
				Long oldSortOrder = dvpro.getSortOrder();
				Long newSortOrder = new Long(dvpro.getSortOrder().longValue() + amount);
				Query q = session.createQuery("from DV_PROGETTO in class DvProgetto where DV_PROGETTO.sortOrder=:sortOrder"); 
				DvProgetto dvproParam = new DvProgetto();
				dvproParam.setSortOrder(newSortOrder);
				q.setProperties(dvproParam);
				List list = q.list();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					DvProgetto dvproToChange = (DvProgetto)it.next();
					dvproToChange.setSortOrder(oldSortOrder);
					session.saveOrUpdate(dvproToChange);
				}
				dvpro.setSortOrder(newSortOrder);
				session.saveOrUpdate(dvpro);
			}else{
				//classe
				DvClasse dvcla = (DvClasse)session.load(DvClasse.class, new Long(id));
				Long oldSortOrder = dvcla.getSortOrder();
				Long newSortOrder = new Long(dvcla.getSortOrder().longValue() + amount);
				Query q = null;
				DvClasse dvclaParam = new DvClasse();
				dvclaParam.setSortOrder(newSortOrder);
				if (level == 0) {
					q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null and DV_CLASSE.sortOrder=:sortOrder");
					DvProgetto dvproFk = ((DvClasse)session.load(DvClasse.class, new Long(id))).getDvProgetto();
					dvclaParam.setDvProgetto(dvproFk);
				} else {
					q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse and DV_CLASSE.sortOrder=:sortOrder");
					DvClasse dvclaFk = ((DvClasse)session.load(DvClasse.class, new Long(id))).getDvClasse();
					dvclaParam.setDvClasse(dvclaFk);
				} 
				q.setProperties(dvclaParam);
				List list = q.list();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					DvClasse dvclaToChange = (DvClasse)it.next();
					dvclaToChange.setSortOrder(oldSortOrder);
					session.saveOrUpdate(dvclaToChange);
				}
				dvcla.setSortOrder(newSortOrder);
				session.saveOrUpdate(dvcla);
			}
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.commit();
		} catch (Exception e) {
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
	}
	
	/**
	 * Opera la cancellazione di un  progetto o di una classe.<p>
	 * Il salvataggio avviene tramite una Transaction Hibernate (apertura, e in chiusura commit o rollback a seconda 
	 * dell'esito dell'operazione).<p>
	 * La cancellazione avviene in due fasi: chiamata a metodo ricorsivo readProClasToDelete() per la costruzione di 
	 * un albero degli elementi da cancellare, quindi cancellazione effettiva, dal livello più interno al livello più 
	 * esterno per la presenza di chiavi esterne (tra classe e classe contenitore e tra classe e progetto).<p>
	 * A seguito della cancellazione viene inoltre reimpostato l'ordinamento degli eventuali elementi successivi.
	 * @param id Identificativo dl progetto o della classe (a seconda del valore di level) da cancellare
	 * @param level Se -1 si tratta di un progetto, altrimenti di una classe
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void delete(long id, short level) throws Exception {
		Session session = null;
		Transaction t = null;
		try {
			session = sf.openSession();
			t = session.beginTransaction();
			ArrayList<ProgettoClasse> proClasToDelete = new ArrayList<ProgettoClasse>();
			//carico gli oggetti da cancellare (chiamata a metodo ricorsivo)
			readProClasToDelete(proClasToDelete, id, level, session);
			//trovo qual è il livello più esterno e quello più interno
			short maxLevel = -2;
			short minLevel = -2;
			for (ProgettoClasse pc : proClasToDelete) {
				short pcLevel = pc.getLevel().shortValue();
				if (pcLevel > maxLevel) maxLevel = pcLevel;
				if (pcLevel < minLevel) minLevel = pcLevel;
			}
			//cancello dal livello più interno al livello più esterno a causa della presenza delle chiavi esterne
			short myLevel = maxLevel;
			while (myLevel >= minLevel) {
				for (int i = 0; i < proClasToDelete.size(); i++) {
					ProgettoClasse pc = (ProgettoClasse)proClasToDelete.get(i);
					short pcLevel = pc.getLevel().shortValue();
					if (pcLevel == myLevel) {
						if (myLevel == -1) {
							DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, pc.getId());
							//reimposto i sortOrder degli elementi successivi
							setNextSortOrders(dvpro, session);
							//cancello il progetto da DV_PROGETTO
							session.delete(dvpro);
						}else{
							//verifico se la classe è terminale e cancello gli eventuali record in DV_FORMAT_CLASSE
							Query q = session.createQuery("from DV_FORMAT_CLASSE in class DvFormatClasse where DV_FORMAT_CLASSE.dvClasse=:dvClasse");
							DvFormatClasse dvforcla = new DvFormatClasse();
							DvClasse dvcla = (DvClasse)session.load(DvClasse.class, pc.getId());	
							dvforcla.setDvClasse(dvcla);
							q.setProperties(dvforcla);
							List list = q.list();
							Iterator it = list.iterator();
							while (it.hasNext()) {
								session.delete((DvFormatClasse)it.next());
							}
							//reimposto i sortOrder degli elementi successivi
							setNextSortOrders(dvcla, session);
							//cancello la classe da DV_CLASSE
							session.delete(dvcla);
						}
						proClasToDelete.remove(i);
						i--;
					}
				}
				myLevel--;
			}			
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.commit();
		} catch (Exception e) {
			if (t != null && !t.wasCommitted() && !t.wasRolledBack()) t.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
	}
	
	/**
	 * Metodo di utilità chiamato da delete() per la reimpostazione dell'ordinamento degli elementi a seguito di una 
	 * cancellazione.
	 * @param pc L'oggetto DvProgetto o DvClasse di cui va reimpostato l'ordinamento
	 * @param session La corrente sessione di Hibernate
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void setNextSortOrders(Object pc, Session session) throws Exception {
		Query q = null;
		if (pc instanceof DvProgetto) {
			q = session.createQuery("from DV_PROGETTO in class DvProgetto where DV_PROGETTO.sortOrder>:sortOrder");
			q.setProperties((DvProgetto)pc);
		} else if (pc instanceof DvClasse) {
			DvClasse dvcla = (DvClasse)pc;
			if (dvcla.getDvClasse() == null) {
				//classe del livello più esterno
				q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null and DV_CLASSE.sortOrder>:sortOrder");
			}else{
				//altre classi
				q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse and DV_CLASSE.sortOrder>:sortOrder");
			}
			q.setProperties(dvcla);
		}
		List list = q.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			if (pc instanceof DvProgetto) {
				DvProgetto element = (DvProgetto)it.next();
				element.setSortOrder(new Long(element.getSortOrder().longValue() - 1));
				session.saveOrUpdate(element);
			} else if (pc instanceof DvClasse) {
				DvClasse element = (DvClasse)it.next();
				element.setSortOrder(new Long(element.getSortOrder().longValue() - 1));
				session.saveOrUpdate(element);
			}
		}
	}
	
	/**
	 * Verifica se il nome assegnato ad un nuovo progetto o a una nuova classe è disponibile o è già stato utilizzato 
	 * (a quel livello).
	 * @param pc L'oggetto Progetto o Classe d cui va verificato il nome
	 * @return Flag che indica se il nome assegnato al nuovo progetto o alla nuova classe è disponibile
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public boolean isNameFree(Object pc) throws Exception {
		Session session = null;
		Query q = null;
		try {
			session = sf.openSession();
			if (pc instanceof Progetto) {
				Progetto pro = (Progetto)pc;
				if (pro.getId() == null)
					q = session.createQuery("from DV_PROGETTO in class DvProgetto where DV_PROGETTO.name=:name");
				else
					q = session.createQuery("from DV_PROGETTO in class DvProgetto where DV_PROGETTO.name=:name and DV_PROGETTO.id<>:id");
				DvProgetto dvpro = new DvProgetto();
				dvpro.setId(pro.getId());
				dvpro.setName(pro.getName());
				q.setProperties(dvpro);
			} else if (pc instanceof Classe) {
				Classe cla = (Classe)pc;
				if (cla.getDvClasse() == null) {
					//classe del livello più esterno					
					if (cla.getId() == null)
						q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null and DV_CLASSE.name=:name");
					else
						q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null and DV_CLASSE.name=:name and DV_CLASSE.id<>:id");				
				}else{
					//altre classi
					if (cla.getId() == null)
						q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse and DV_CLASSE.name=:name");
					else
						q = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse and DV_CLASSE.name=:name and DV_CLASSE.id<>:id");			
				}
				DvClasse dvcla = new DvClasse();
				if (cla.getDvClasse() == null) {
					DvProgetto dvpro = (DvProgetto)session.load(DvProgetto.class, cla.getDvProgetto());
					dvcla.setDvProgetto(dvpro);
				}else{
					DvClasse dvclafk = (DvClasse)session.load(DvClasse.class, cla.getDvClasse());
					dvcla.setDvClasse(dvclafk);
				}
				dvcla.setId(cla.getId());
				dvcla.setName(cla.getName());
				q.setProperties(dvcla);
			}
			List list = q.list();
			Iterator it = list.iterator();
			return !it.hasNext();
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}
	}
	
	/**
	 * Imposta il valore dei flag che indicano se e quali pulsanti funzionali devono essere visualizzati a fianco di 
	 * ogni singolo progetto o classe nell'albero.
	 * @param al La lista degli elementi visualizzati nel diagramma ad albero
	 * @param allAl La lista completa degli elementi (visualizzati e non)
	 * @param session La corrente sessione di Hibernate
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void setRenderedButtons(ArrayList<ProgettoClasse> al, ArrayList<ProgettoClasse> allAl, Session session) throws Exception {
		for (ProgettoClasse pc : al) {
			Query qUp = null;
			Query qDown = null;
			if (pc.getLevel().shortValue() == -1) {
				qUp = session.createQuery("from DV_PROGETTO in class DvProgetto where DV_PROGETTO.sortOrder<:sortOrder");
				qDown = session.createQuery("from DV_PROGETTO in class DvProgetto where DV_PROGETTO.sortOrder>:sortOrder");
				DvProgetto dvpro = new DvProgetto();
				dvpro.setSortOrder(new Long(pc.getSortOrder().longValue()));
				qUp.setProperties(dvpro);
				qDown.setProperties(dvpro);
			} else {
				DvClasse dvcla = new DvClasse();
				if (pc.getLevel().shortValue() == 0) {
					qUp = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null and DV_CLASSE.sortOrder<:sortOrder");
					qDown = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvProgetto=:dvProgetto and DV_CLASSE.dvClasse is null and DV_CLASSE.sortOrder>:sortOrder");
					dvcla.setDvProgetto(((DvClasse)session.load(DvClasse.class, pc.getId())).getDvProgetto());
				} else {
					qUp = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse and DV_CLASSE.sortOrder<:sortOrder");
					qDown = session.createQuery("from DV_CLASSE in class DvClasse where DV_CLASSE.dvClasse=:dvClasse and DV_CLASSE.sortOrder>:sortOrder");
					dvcla.setDvClasse(((DvClasse)session.load(DvClasse.class, pc.getId())).getDvClasse());
				}				
				dvcla.setSortOrder(new Long(pc.getSortOrder().longValue()));
				qUp.setProperties(dvcla);
				qDown.setProperties(dvcla);
			}
			List listUp = qUp.list();
			Iterator it = listUp.iterator();
			pc.setUpRendered(it.hasNext());
			List listDown = qDown.list();
			it = listDown.iterator();
			pc.setDownRendered(it.hasNext());
			if (pc.getLevel().shortValue() == -1) {
				pc.setNewRendered(true);
			}else{
				Query qFormat = session.createQuery("from DV_FORMAT_CLASSE in class DvFormatClasse where DV_FORMAT_CLASSE.dvClasse=:dvClasse");
				DvFormatClasse dvforcla = new DvFormatClasse();
				DvClasse dvcla = (DvClasse)session.load(DvClasse.class, pc.getId());	
				dvforcla.setDvClasse(dvcla);
				qFormat.setProperties(dvforcla);
				List listFormat = qFormat.list();
				Iterator itFormat = listFormat.iterator();
				pc.setNewRendered(!itFormat.hasNext());
			}
			pc.setPopupName(pc.isNewRendered() ? "Folder.jsp" : "Classe.jsp");
			pc.setDeleteRendered(getDeleteRendered(pc, allAl, session));
		}
	}
	
	/**
	 * Metodo che verifica se è possibile la cancellazione di un progetto o di una classe; lo è se la classe, o tutte 
	 * le classi contenute nella classe (se contenitore) o nel progetto che si intende cancellare, sono libere da link, 
	 * cioè se non esistono record in DV_LINK_CLASSE con FK_DV_CLASSE1 o FK_DV_CLASSE2 uguale all'identificativo della 
	 * classe considerata.<p>
	 * L'icona contenente il link utilizzato per la cancellazione viene visualizzata o meno a seconda del valore 
	 * restituito da questo metodo.
	 * @param pc L'oggetto ProgettoClasse di cui va impostato il valore del campo deleteRendered.
	 * @param allAl La lista completa degli elementi (visualizzati nel diagramma ad albero e non)
	 * @param session La corrente sessione di Hibernate
	 * @return Flag che indica se è possibile la cancellazione del progetto o della classe
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private boolean getDeleteRendered(ProgettoClasse pc, ArrayList<ProgettoClasse> allAl, Session session) throws Exception {
		int index = 0;
		int startIndex = -1;
		int endIndex = -1;
		for (ProgettoClasse myPc : allAl) {
			if (myPc.getId().longValue() == pc.getId().longValue() && myPc.getLevel().shortValue() == pc.getLevel().shortValue())
				startIndex = index;
			if (startIndex > -1 && myPc.getId().longValue() != pc.getId().longValue() && myPc.getLevel().shortValue() == pc.getLevel().shortValue()) {
				endIndex = index;
				break;
			}
			index++;
		}
		if (endIndex == -1) //per l'ultimo elemento
			// come se ci fosse un elemento "virtuale" dello stesso livello fuori dall'albero
			endIndex = index;  
		boolean retVal = true;
		for (int i = startIndex; i < endIndex; i++) {
			ProgettoClasse myPc = allAl.get(i);
			if (myPc.getLevel().shortValue() > -1) {
				retVal = !(hasClassLinks(session, myPc.getId(), true) || hasClassLinks(session, myPc.getId(), false));
				if (!retVal)
					break;
			}
		}
		return retVal;
	}
	
	/**
	 * Restituisce l'elenco delle entità per la costruzione della combo box di selezione preliminare 
	 * dell'entità nella popup di gestione delle classi.
	 * @return Un'HashMap di chiavi String e valori Table che costituisce l'elenco delle entità
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public HashMap<String, Table> getEntities() throws Exception {
		HashMap<String, Table> retVal = new HashMap<String, Table>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("select DC_ENTITY from DC_ENTITY in class DcEntity, " +
					"DV_USER_ENTITY in class DvUserEntity " +
					"where DC_ENTITY.id = DV_USER_ENTITY.dcEntity");
			DcEntity dcent = new DcEntity();
			q.setProperties(dcent);
			List dcents = q.list();
			Iterator it = dcents.iterator();
			Table tab;
			while (it.hasNext()) {
				DcEntity element = (DcEntity)it.next();
				//l'entità viene aggiunta all'elenco solo se ha delle chiavi
				if (hasDcEntityKeys(session, element)) {
					tab = new Table();
					tab.setId(element.getId());
					tab.setName(element.getName());
					tab.setLongDesc(element.getLongDesc());
					tab.setOwner(element.getOwner());
					retVal.put(tab.getName(), tab);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}		
		return retVal;
	}
	
	/**
	 * Verifica se la DcEntity parametro ha delle chiavi definite (cioè, se ha almeno una DC_COLUMN definita come 
	 * chiave in DC_ATTRIBUTE).
	 * @param session La corrente sessione di Hibernate
	 * @param dcEnt Un oggetto DcEntity
	 * @return Un flag che indica se la DcEntity parametro ha delle chiavi definite.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private boolean hasDcEntityKeys(Session session, DcEntity dcEnt) throws Exception {
		boolean retVal = false;
		Set dcCols = dcEnt.getDcColumns();
		Iterator itCols = dcCols.iterator();
		while (itCols.hasNext()) {
			DcColumn dcCol = (DcColumn)itCols.next();
			Query q = session.createQuery("from DC_ATTRIBUTE in class DcAttribute " +
											"where DC_ATTRIBUTE.dcColumn = :dcColumn " +
											"and DC_ATTRIBUTE.attributeSpec = :attributeSpec");
			DcAttribute dcAtt = new DcAttribute();
			dcAtt.setDcColumn(dcCol);
			dcAtt.setAttributeSpec(KEY);
			q.setProperties(dcAtt);
			List dcAtts = q.list();
			Iterator itAtts = dcAtts.iterator();
			retVal = itAtts.hasNext();
			if (retVal)
				break;
		}
		return retVal;
	}

	/**
	 * Restituisce, dato un identificativo di entità, l'elenco delle colonne disponibili per essere eventualmente 
	 * aggiunte al filtro (nella popup di gestione classi).<p>
	 * Inoltre, per ogni colonna disponibile, restituisce l'elenco degli operatori disponibili per il filtro. Questo 
	 * elenco viene valorizzato sulla base del tipo java della colonna in questione.
	 * @param entityId L'identificativo dell'entità
	 * @return Un'HashMap di chiavi String e valori Object che contiene le colonne disponibili per l'entità 
	 * selezionata (primo elemento) e l'elenco degli operatori (HashMap che costituisce il secondo elemento 
	 * dell'HashMap di ritorno) per ogni colonna.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public HashMap<String, ? extends Object> getColumnsForFilter(Long entityId) throws Exception {
		HashMap<String, Object> retVal = new HashMap<String, Object>();
		ArrayList<SelectItem> retValCols = new ArrayList<SelectItem>();
		HashMap<String, ArrayList<GenericValue>> retValOps = new HashMap<String, ArrayList<GenericValue>>();
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
			dcent.setId(entityId);
			dccol.setDcEntity(dcent);
			q.setProperties(dccol);
			List dccols = q.list();
			Iterator it = dccols.iterator();
			while (it.hasNext()) {
				DcColumn element = (DcColumn) it.next();
				retValCols.add(new SelectItem(element.getLongDesc()));
				retValOps.put(element.getLongDesc(), getTypeOperators(element.getDcExpression().getColType()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		retVal.put(COLUMNS, retValCols);
		retVal.put(OPERATORS, retValOps);
		return retVal;
	}
	
	/** Dato l'identificativo di una classe, recupera il nome dell'entità associata (per chiave esterna) a 
	 * quella classe.<p>
	 * Inoltre, restituisce i dati recuperati in DV_FORMAT_CLASSE per l'impostazione di filtro, lista e dettaglio.
	 * @param clazzId L'identificativo della classe
	 * @return Un'HashMap di chiavi String e valori Object. Il primo elemento dell'HashMap è il nome dell'entità 
	 * associata alla classe, il secondo è un oggetto FormatClasse valorizzato a partire dai dati recuperati in 
	 * DV_FORMAT_CLASSE; il terzo e quarto elemento sono rispettivamente il titolo del filtro e il titolo della 
	 * lista.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public HashMap<String, ? extends Object> getSelectedEntityAndFormatClasse(Long clazzId) throws Exception {
		HashMap<String, Object> retVal = new HashMap<String, Object>();
		String retValSelEnt = "";
		String filterTitle = "";
		String listTitle = "";
		String detailTitle = "";
		FormatClasse retValFc = new FormatClasse();
		retValFc.setDvClasse(clazzId);
		Session session = null;
		try {
			session = sf.openSession();			
			DvClasse dvcla = (DvClasse)session.load(DvClasse.class, clazzId);
			Set dvForClas = dvcla.getDvFormatClasses();
			Iterator itForClas = dvForClas.iterator();
			// una relazione 1..1
			if (itForClas.hasNext()) {
				DvFormatClasse dvforcla = (DvFormatClasse)itForClas.next();
				DvUserEntity dvuseent = (DvUserEntity)session.load(DvUserEntity.class, dvforcla.getId().getFkDvUserEntity());
				DcEntity dcent = dvuseent.getDcEntity();
				retValSelEnt = dcent.getName();
				//imposto l'oggetto FormatClasse
				retValFc.setDvUserEntity(dvuseent.getId());
				//filtro
				HashMap<String, Object> filterData = getFilterALFromXML(session, dcent.getId(), dvforcla.getFilter());
				ArrayList<Filter> filterAL = getFilterArrayList((ArrayList)filterData.get(FILTER_AL));
				retValFc.setFilter(filterAL);
				filterTitle = (String)filterData.get(FILTER_TITLE);
				HashMap<String, Object> fcListData = getListALFromXML(session, dcent.getId(), dvforcla.getList());
				ArrayList<FcList> fcListAL = getFcListArrayList((ArrayList)fcListData.get(LIST_AL));
				retValFc.setList(fcListAL);
				listTitle = (String)fcListData.get(LIST_TITLE);
				//dettaglio
				HashMap<String, Object> detailData = getDetailALFromXML(session, dcent.getId(), dvforcla.getDetail());
				ArrayList<DetailGroup> detailAL = getDetailGroupArrayList((ArrayList)detailData.get(DETAIL_AL));
				retValFc.setDetail(detailAL);
				detailTitle = (String)detailData.get(DETAIL_TITLE);
				//link
				retValFc.setLink(getLinkAL(session, clazzId, dcent.getId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen()) session.close();
		}		
		retVal.put(SELECTED_ENTITY, retValSelEnt);
		retVal.put(FORMAT_CLASSE, retValFc);
		retVal.put(FILTER_TITLE, filterTitle);
		retVal.put(LIST_TITLE, listTitle);
		retVal.put(DETAIL_TITLE, detailTitle);
		return retVal;
	}
	
	/**
	 * Dato l'identificativo di un'entità, restituisce l'identificativo della user entity corrispondente 
	 * (si tratta di una relazione 1..1). 
	 * @param entityId L'identificativo dell'entità
	 * @return L'identificativo della user entity corrispondente
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public Long getUserEntityIdFromEntityId(Long entityId) throws Exception {
		Long retVal = null;
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("from DV_USER_ENTITY in class DvUserEntity where DV_USER_ENTITY.dcEntity=:dcEntity");
			DvUserEntity dvuseent = new DvUserEntity();
			DcEntity dcent = (DcEntity)session.load(DcEntity.class, entityId);
			dvuseent.setDcEntity(dcent);
			q.setProperties(dvuseent);
			List dvuseents = q.list();
			Iterator it = dvuseents.iterator();
			// una relazione 1..1
			if (it.hasNext()) {
				DvUserEntity element = (DvUserEntity)it.next();
				retVal = element.getId();
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
	 * Dati l'identificativo di un'entità e la descrizione estesa di una colonna, restituisce l'identificativo della 
	 * colonna.
	 * @param paramSession La corrente sessione di Hibernate (se null viene aperta una nuova sessione)
	 * @param entityId L'identificativo dell'entità
	 * @param longDesc La descrizione estesa della colonna
	 * @return L'identificativo della colonna di cui la descrizione estesa è passata a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public Long getColumnIdFromLongDesc(Session paramSession, Long entityId, String longDesc) throws Exception {
		Long retVal = null;
		Session session = null;
		boolean closeSession = paramSession == null;
		try {
			session = paramSession == null ? sf.openSession() : paramSession;
			Query q = session.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.dcEntity=:dcEntity " +
											"AND DC_COLUMN.longDesc=:longDesc");
			DcColumn dccol = new DcColumn();
			DcEntity dcent = (DcEntity)session.load(DcEntity.class, entityId);
			dccol.setDcEntity(dcent);
			dccol.setLongDesc(longDesc);
			q.setProperties(dccol);
			List dccols = q.list();
			Iterator it = dccols.iterator();
			//in it ci dovrebbe essere un solo elemento...
			if (it.hasNext()) {
				DcColumn element = (DcColumn)it.next();
				retVal = element.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}
	
	/**
	 * Dato l'identificativo di una colonna, restituisce la descrizione estesa della colonna.
	 * @param paramSession La corrente sessione di Hibernate (se null viene aperta una nuova sessione)
	 * @param columnId L'identificativo della colonna
	 * @return La descrizione estesa della colonna di cui l'identificativo è passato a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public String getColumnLongDescFromId(Session paramSession, Long columnId) throws Exception {
		String retVal = null;
		Session session = null;
		boolean closeSession = paramSession == null;
		try {
			session = paramSession == null ? sf.openSession() : paramSession;
			DcColumn dccol = (DcColumn)session.load(DcColumn.class, columnId);
			retVal = dccol.getLongDesc();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}
	
	/**
	 * Restituisce, dati l'identificativo di un'entità e la descrizione estesa di una colonna, l'elenco degli 
	 * operatori disponibili per il filtro (per la colonna in questione).
	 * @param entityId L'identificativo dell'entità
	 * @param colLongDesc La descrizione estesa della colonna
	 * @return Un'ArrayList di GenericValue che costituisce l'elenco degli operatori disponibili per il filtro, 
	 * per la colonna in questione.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private ArrayList<GenericValue> getOperatorsForColumn(Long entityId, String colLongDesc) throws Exception {
		ArrayList<GenericValue> retVal = new ArrayList<GenericValue>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("select DC_COLUMN from DC_COLUMN in class DcColumn, "
							+ "DC_ENTITY in class DcEntity, "
							+ "DC_EXPRESSION in class DcExpression "
							+ "where DC_EXPRESSION.id = DC_COLUMN.dcExpression "
							+ "and DC_ENTITY.id = DC_COLUMN.dcEntity "
							+ "and DC_COLUMN.longDesc = :longDesc "
							+ "and DC_COLUMN.dcEntity = :dcEntity");
			DcColumn dccol = new DcColumn();
			DcEntity dcent = new DcEntity();
			dcent.setId(entityId);
			dccol.setDcEntity(dcent);
			dccol.setLongDesc(colLongDesc);
			q.setProperties(dccol);
			List dccols = q.list();
			Iterator it = dccols.iterator();
			//in it ci dovrebbe essere un solo elemento...
			if (it.hasNext()) {
				DcColumn element = (DcColumn)it.next();
				retVal = getTypeOperators(element.getDcExpression().getColType());
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
	 * Restituisce un oggetto DvFormatClasse pronto per l'inserimento o la modifica in DV_FORMAT_CLASSE, valorizzando 
	 * (chiamata a getFilterXMLFromFormatClasse(), getListXMLFromFormatClasse() e getDetailXMLFromFormatClasse()) 
	 * anche i campi di DvFormatClasse corrispondenti a campi XMLTYPE del DB.
	 * @param session La corrente sessione di Hibernate
	 * @param clazzId L'identificativo della classe
	 * @param fc L'oggetto FormatClasse che contiene i dati di partenza
	 * @return Un oggetto DvFormatClasse da utilizzarsi per l'inserimento o la modifica in DV_FORMAT_CLASSE
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private DvFormatClasse getRecordFromFormatClasse(Session session, Long clazzId, FormatClasse fc) throws Exception {
		if (fc == null) return null;
		DvFormatClasse retVal = new DvFormatClasse();
		DvFormatClasseId id = new DvFormatClasseId();
		id.setFkDvClasse(clazzId.longValue());
		id.setFkDvUserEntity(fc.getDvUserEntity());
		retVal.setId(id);
		Long dcEntityId = ((DvUserEntity)session.load(DvUserEntity.class, id.getFkDvUserEntity())).getDcEntity().getId();
		retVal.setFilter(getFilterXMLFromFormatClasse(session, dcEntityId, fc));
		retVal.setList(getListXMLFromFormatClasse(session, dcEntityId, fc));
		retVal.setDetail(getDetailXMLFromFormatClasse(session, dcEntityId, fc));
		return retVal;
	}
	
	/**
	 * Restituisce un array di byte per l'inserimento o la modifica del campo FILTER (XMLTYPE) del DB.
	 * @param session La corrente sessione di Hibernate
	 * @param dcEntityId L'identificativo dell'entità selezionata
	 * @param fc L'oggetto FormatClasse che contiene i dati da inserire o modificare nel DB
	 * @return Un array di byte da utilizzarsi per l'inserimento o la modifica del campo FILTER (XMLTYPE) del DB
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private byte[] getFilterXMLFromFormatClasse(Session session, Long dcEntityId, FormatClasse fc) throws Exception {
		StringWriter sw = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		byte[] retVal = new byte[0];
//		out.setIndent("   ");
//		out.setLineSeparator(CRLF);
//		out.setNewlines(true);
		out.setFormat(Format.getPrettyFormat());

		try {
			Element view = new Element(FILTER_XML_LABELS.get("VIEW"));
			view.setAttribute(new Attribute(FILTER_XML_LABELS.get("TYPE"), FILTER_XML_LABELS.get("TYPE_VALUE")));
			view.setAttribute(new Attribute(FILTER_XML_LABELS.get("TITLE"), fc.getFilterTitle()));
			Element columns = new Element(FILTER_XML_LABELS.get("COLUMNS"));
			ArrayList<Filter> filterAL = fc.getFilter();
			for (Filter f : filterAL) {
				Element column = new Element(FILTER_XML_LABELS.get("COLUMN"));
				String dcColumnName = f.getDcColumnName();
				column.setAttribute(new Attribute(FILTER_XML_LABELS.get("DCCOLUMN"), getColumnIdFromLongDesc(session, dcEntityId, dcColumnName).toString()));
				//operatori
				ArrayList<GenericValue> ops = f.getOperatorsGV();
				String[] selOps = f.getSelOperators();
				if (selOps != null && selOps.length > 0) {
					ArrayList<GenericValue> selOpsArr = new ArrayList<GenericValue>();
					Element operators = new Element(FILTER_XML_LABELS.get("OPERATORS"));
					Element values = new Element(FILTER_XML_LABELS.get("VALUES"));
					String selOpForDef = f.getSelectedOperatorForDefault() == null ? "" : f.getSelectedOperatorForDefault();
					for (String selOp : selOps) {
						for (GenericValue op : ops) {
							if (selOp.equals(op.getValueDesc())) {
								if (selOp.equals(selOpForDef)) {
									selOpsArr.add(0, op);
								} else {
									selOpsArr.add(op);
								}
								break;
							}
						}
					}
					for (GenericValue op : selOpsArr) {
						Element value = new Element(FILTER_XML_LABELS.get("VALUE"));
						value.setAttribute(new Attribute(FILTER_XML_LABELS.get("ID"), op.getIdAsString()));
						value.setAttribute(new Attribute(FILTER_XML_LABELS.get("DESCRIPTION"), op.getValueDesc()));
						values.addContent(value);
					}
					operators.addContent(values);
					column.addContent(operators);
				}
				//valori
				String valueType = f.getSelectedValueType() == null ? "" : f.getSelectedValueType();
				if (valueType.equals(SQL) || valueType.equals(VALORI_FISSI)) {
					String mandatoryStr = "" + f.isMandatory();
					Element listValues = new Element(FILTER_XML_LABELS.get("LISTVALUES"));
					listValues.setAttribute(new Attribute(FILTER_XML_LABELS.get("REQUIRED"), mandatoryStr));
					if (valueType.equals(SQL)) {
						String sqlCommand = f.getSqlCommand();
						if (sqlCommand != null && !sqlCommand.equals("")) {
							Element sql = new Element(FILTER_XML_LABELS.get("SQL"));
							sql.setText(sqlCommand);
							listValues.addContent(sql);	
						}					
					} else if (valueType.equals(VALORI_FISSI)){
						ArrayList<GenericValue> myValues = f.getValuesGV();
						if (myValues != null && myValues.size() > 0) {
							Element values = new Element(FILTER_XML_LABELS.get("VALUES"));
							for (GenericValue myValue : myValues) {
								Element value = new Element(FILTER_XML_LABELS.get("VALUE"));
								value.setAttribute(new Attribute(FILTER_XML_LABELS.get("ID"), myValue.getIdAsString()));
								value.setAttribute(new Attribute(FILTER_XML_LABELS.get("DESCRIPTION"), myValue.getValueDesc()));
								values.addContent(value);
							}
							listValues.addContent(values);
						}
					}
					column.addContent(listValues);
				}
				columns.addContent(column);
			}
			view.addContent(columns);
			out.output(view, sw);
		} catch (IOException e) {
			//non fa nulla, restituisce l'array di byte vuoto
			e.printStackTrace();
			return retVal;
		}
		retVal = sw.toString().getBytes();
		//per debug
		System.out.print(sw.toString());
		return retVal;
	}
	
	/**
	 * Restituisce un array di byte per l'inserimento o la modifica del campo LIST (XMLTYPE) del DB.
	 * @param session La corrente sessione di Hibernate
	 * @param dcEntityId L'identificativo dell'entità selezionata
	 * @param fc L'oggetto FormatClasse che contiene i dati da inserire o modificare nel DB
	 * @return Un array di byte da utilizzarsi per l'inserimento o la modifica del campo LIST (XMLTYPE) del DB
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private byte[] getListXMLFromFormatClasse(Session session, Long dcEntityId, FormatClasse fc) throws Exception {
		StringWriter sw = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		byte[] retVal = new byte[0];
//		out.setIndent("   ");
//		out.setLineSeparator(CRLF);
//		out.setNewlines(true);
		out.setFormat(Format.getPrettyFormat());
		try {
			Element view = new Element(LIST_XML_LABELS.get("VIEW"));
			view.setAttribute(new Attribute(LIST_XML_LABELS.get("TYPE"), LIST_XML_LABELS.get("TYPE_VALUE")));
			view.setAttribute(new Attribute(LIST_XML_LABELS.get("TITLE"), fc.getListTitle()));
			Element columns = new Element(LIST_XML_LABELS.get("COLUMNS"));
			ArrayList<FcList> listAL = fc.getList();
			for (FcList fcl : listAL) {
				Element column = new Element(LIST_XML_LABELS.get("COLUMN"));
				if (!fcl.isJs()) {
					String dcColumnName = fcl.getSelectedColumn();
					column.setAttribute(new Attribute(LIST_XML_LABELS.get("DCCOLUMN"), getColumnIdFromLongDesc(session, dcEntityId, dcColumnName).toString()));
				}else{
					column.setAttribute(new Attribute(LIST_XML_LABELS.get("HEADER"), fcl.getHeader()));
					column.setAttribute(new Attribute(LIST_XML_LABELS.get("SCRIPT"), fcl.getScript()));
					column.setAttribute(new Attribute(LIST_XML_LABELS.get("IMAGEURL"), fcl.getImageurl()));
					column.setAttribute(new Attribute(LIST_XML_LABELS.get("TESTURL"), fcl.getTesturl()));
					ArrayList<SelectItem> params = fcl.getParams();
					if (params != null) {
						for (SelectItem si : params) {
							Element param = new Element(LIST_XML_LABELS.get("PARAM"));
							String dcColumnName = si.getValue().toString();
							param.setAttribute(new Attribute(LIST_XML_LABELS.get("DCCOLUMN"), getColumnIdFromLongDesc(session, dcEntityId, dcColumnName).toString()));
							column.addContent(param);
						}
					}
				}
				columns.addContent(column);
			}
			/* codice non utilizzato
			ArrayList<Long> hiddenKeys = getHiddenKeys(session, dcEntityId, listAL);
			for (Long hiddenKey : hiddenKeys) {
				Element column = new Element(LIST_XML_LABELS.get("COLUMN"));
				column.setAttribute(new Attribute(LIST_XML_LABELS.get("DCCOLUMN"), "" + hiddenKey.longValue()));
				column.setAttribute(new Attribute(LIST_XML_LABELS.get("HIDDENKEY"), LIST_XML_LABELS.get("HIDDENKEY_VALUE")));
				columns.addContent(column);
			}
			*/
			view.addContent(columns);
			out.output(view, sw);
		} catch (IOException e) {
			//non fa nulla, restituisce l'array di byte vuoto
			e.printStackTrace();
			return retVal;
		}
		retVal = sw.toString().getBytes();
		//per debug
		System.out.print(sw.toString());
		return retVal;
	}
	
	/**
	 * Restituisce un array di byte per l'inserimento o la modifica del campo DETAIL (XMLTYPE) del DB.
	 * @param session La corrente sessione di Hibernate
	 * @param dcEntityId L'identificativo dell'entità selezionata
	 * @param fc L'oggetto FormatClasse che contiene i dati da inserire o modificare nel DB
	 * @return Un array di byte da utilizzarsi per l'inserimento o la modifica del campo DETAIL (XMLTYPE) del DB
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private byte[] getDetailXMLFromFormatClasse(Session session, Long dcEntityId, FormatClasse fc) throws Exception {
		StringWriter sw = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		byte[] retVal = new byte[0];
//		out.setIndent("   ");
//		out.setLineSeparator(CRLF);
//		out.setNewlines(true);
		out.setFormat(Format.getPrettyFormat());
		try {
			Element view = new Element(DETAIL_XML_LABELS.get("VIEW"));
			view.setAttribute(new Attribute(DETAIL_XML_LABELS.get("TYPE"), DETAIL_XML_LABELS.get("TYPE_VALUE")));
			view.setAttribute(new Attribute(DETAIL_XML_LABELS.get("TITLE"), fc.getDetailTitle()));
			ArrayList<DetailGroup> detailAL = fc.getDetail();
			for (DetailGroup dg : detailAL) {
				Element table = new Element(DETAIL_XML_LABELS.get("TABLE"));
				if (dg.isTable()) {
					ArrayList<Detail> groupDetailAL = dg.getDetailAL();
					String tableTitle = groupDetailAL == null || groupDetailAL.size() == 0 ? "" : groupDetailAL.get(0).getTitle();
					table.setAttribute(new Attribute(DETAIL_XML_LABELS.get("TITLE"), tableTitle));
					for (Detail d : groupDetailAL) {
						if (!d.isFirst()) {
							Element row = new Element(DETAIL_XML_LABELS.get("ROW"));
							Element column1 = new Element(DETAIL_XML_LABELS.get("COLUMN"));
							String strColumn1 = d.getSelectedColumn1();
							if (strColumn1 != null && !strColumn1.equals("")) {
								column1.setAttribute(new Attribute(DETAIL_XML_LABELS.get("DCCOLUMN"), getColumnIdFromLongDesc(session, dcEntityId, strColumn1).toString()));
							}
							row.addContent(column1);
							Element column2 = new Element(DETAIL_XML_LABELS.get("COLUMN"));
							String strColumn2 = d.getSelectedColumn2();
							if (strColumn2 != null && !strColumn2.equals("")) {
								column2.setAttribute(new Attribute(DETAIL_XML_LABELS.get("DCCOLUMN"), getColumnIdFromLongDesc(session, dcEntityId, strColumn2).toString()));
							}
							row.addContent(column2);
							Element column3 = new Element(DETAIL_XML_LABELS.get("COLUMN"));
							String strColumn3 = d.getSelectedColumn3();
							if (strColumn3 != null && !strColumn3.equals("")) {
								column3.setAttribute(new Attribute(DETAIL_XML_LABELS.get("DCCOLUMN"), getColumnIdFromLongDesc(session, dcEntityId, strColumn3).toString()));
							}
							row.addContent(column3);
							table.addContent(row);
						}
					}
				}else{
					table.setAttribute(new Attribute(DETAIL_XML_LABELS.get("TITLE"), ""));
					Element row = new Element(DETAIL_XML_LABELS.get("ROW"));
					Element column = new Element(DETAIL_XML_LABELS.get("COLUMN"));
					CDATA cdata = new CDATA(DETAIL_XML_LABELS.get("BREAK_VALUE"));
					column.addContent(cdata);					
					row.addContent(column);
					table.addContent(row);
				}
				view.addContent(table);
			}
			out.output(view, sw);
		} catch (IOException e) {
			//non fa nulla, restituisce l'array di byte vuoto
			e.printStackTrace();
			return retVal;
		}
		retVal = sw.toString().getBytes();
		//per debug
		System.out.print(sw.toString());
		return retVal;
	}
	
	/**
	 * Dati l'identificativo di un'entità e un array di byte (valore del campo FILTER in DV_FORMAT_CLASSE) 
	 * restituisce l'ArrayList di oggetti Filter corrispondente (per la visualizzazione nella popup).<p>
	 * Inoltre, restituisce il titolo del filtro.
	 * @param session La corrente sessione di Hibernate
	 * @param entityId L'identificativo dell'entità
	 * @param byteArr L'array di byte che rappresenta il valore del campo FILTER in DV_FORMAT_CLASSE
	 * @return Un'HashMap di chiavi String e valori Object. Il primo elemento dell'HashMap è l'ArrayList di oggetti 
	 * Filter corrispondente ai parametri passati, il secondo è un oggetto String che rappresenta il titolo del 
	 * filtro.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */	
	private HashMap<String, Object> getFilterALFromXML(Session session, Long entityId, byte[] byteArr) throws Exception {
		HashMap<String, Object> retVal = new HashMap<String, Object>();
		ArrayList<Filter> filterAL = new ArrayList<Filter>();
		String filterTitle = new String();
		String xml = new String(byteArr, UTF8_XML_ENCODING);
		SAXBuilder builder = new SAXBuilder();
		List content = builder.build(new StringReader(xml)).getContent();
		Iterator it = content.iterator();
		while (it.hasNext()) {
			Element view = (Element)it.next();
			filterTitle = view.getAttributeValue(FILTER_XML_LABELS.get("TITLE"));
			List viewChildren = view.getChildren();
			Iterator viewIt = viewChildren.iterator();
			while (viewIt.hasNext()) {
				Element columns = (Element)viewIt.next();
				List columnsChildren = columns.getChildren();
				Iterator columnsIt = columnsChildren.iterator();
				while (columnsIt.hasNext()) {
					Element column = (Element)columnsIt.next();
					Filter f = new Filter();
					Long dcColumnId = new Long(column.getAttributeValue(FILTER_XML_LABELS.get("DCCOLUMN")));
					String dcColumn = getColumnLongDescFromId(session, dcColumnId);
					f.setDcColumn(dcColumnId);
					f.setDcColumnName(dcColumn);
					f.setOperatorsGV(getOperatorsForColumn(entityId, dcColumn));
					f.setSelectedValueType(VALORE_LIBERO); //default
					f.setOldSelectedValueType(VALORE_LIBERO);
					List columnChildren = column.getChildren();
					Iterator columnIt = columnChildren.iterator();
					while (columnIt.hasNext()) {
						Element columnElement = (Element)columnIt.next();
						if (columnElement.getName().equals(FILTER_XML_LABELS.get("OPERATORS"))) {
							ArrayList<GenericValue> selOpsAL = new ArrayList<GenericValue>();
							List values = columnElement.getChildren();
							Iterator valuesIt = values.iterator();
							while (valuesIt.hasNext()) {
								Element valuesElement = (Element)valuesIt.next();
								List singleValues = valuesElement.getChildren();
								Iterator singleValuesIt = singleValues.iterator();
								while (singleValuesIt.hasNext()) {
									Element singleValuesElement = (Element)singleValuesIt.next();
									String id = singleValuesElement.getAttributeValue(FILTER_XML_LABELS.get("ID"));
									String description = singleValuesElement.getAttributeValue(FILTER_XML_LABELS.get("DESCRIPTION"));
									GenericValue gv = new GenericValue(id, description);
									selOpsAL.add(gv);
								}
							}
							String[] selOps = new String[selOpsAL.size()];
							int count = 0;
							for (GenericValue gv : f.getOperatorsGV()) {
								for (GenericValue gvSel : selOpsAL) {
									if (gv.getValueDesc().equals(gvSel.getValueDesc())) {
										selOps[count] = gv.getValueDesc();
										count++;
										break;
									}
								}
							}
							f.setSelOperators(selOps);
							f.setOldSelOperators(selOps);
							ArrayList<SelectItem> opsForDef = new ArrayList<SelectItem>();
							for (String selOp : f.getSelOperators()) {
								opsForDef.add(new SelectItem(selOp));
							}
							f.setOperatorsForDefault(opsForDef);
							f.setSelectedOperatorForDefault(selOpsAL.get(0).getValueDesc());
						} else if (columnElement.getName().equals(FILTER_XML_LABELS.get("LISTVALUES"))) {
							String mandatory = columnElement.getAttributeValue(FILTER_XML_LABELS.get("REQUIRED"));
							f.setMandatory(mandatory != null && mandatory.equals("true") ? true : false);
							List columnElementChildren = columnElement.getChildren();
							Iterator columnElementChildrenIt = columnElementChildren.iterator();
							while (columnElementChildrenIt.hasNext()) {
								Element childrenElement = (Element)columnElementChildrenIt.next();
								f.setSqlCommand(""); //default
								f.setValues(new ArrayList<SelectItem>()); //default
								if (childrenElement.getName().equals(FILTER_XML_LABELS.get("SQL"))) {
									f.setSelectedValueType(SQL);
									f.setOldSelectedValueType(SQL);
									f.setSqlCommand(childrenElement.getText());
								}else if (childrenElement.getName().equals(FILTER_XML_LABELS.get("VALUES"))) {
									f.setSelectedValueType(VALORI_FISSI);
									f.setOldSelectedValueType(VALORI_FISSI);
									ArrayList<GenericValue> valuesAL = new ArrayList<GenericValue>();
									List singleValues = childrenElement.getChildren();
									Iterator singleValuesIt = singleValues.iterator();
									while (singleValuesIt.hasNext()) {
										Element singleValuesElement = (Element)singleValuesIt.next();
										String id = singleValuesElement.getAttributeValue(FILTER_XML_LABELS.get("ID"));
										String description = singleValuesElement.getAttributeValue(FILTER_XML_LABELS.get("DESCRIPTION"));
										GenericValue gv = new GenericValue(id, description);
										valuesAL.add(gv);
									}									
									f.setValuesGV(valuesAL);
								}
							}
							if (f.getSelectedValueType().equals(SQL)) {
								f.setRenderCheckbox(true);
								f.setRenderTextarea(true);
								f.setRenderList(false);
							} else if (f.getSelectedValueType().equals(VALORI_FISSI)) {
								f.setRenderCheckbox(true);
								f.setRenderTextarea(false);
								f.setRenderList(true);
							} else if (f.getSelectedValueType().equals(VALORE_LIBERO)) {
								f.setRenderCheckbox(false);
								f.setRenderTextarea(false);
								f.setRenderList(false);
							}
						}
					}					
					filterAL.add(f);
				}
			}
		}
		retVal.put(FILTER_AL, filterAL);
		retVal.put(FILTER_TITLE, filterTitle);
		return retVal;
	}
	
	/**
	 * Dati l'identificativo di un'entità e un array di byte (valore del campo LIST in DV_FORMAT_CLASSE) 
	 * restituisce l'ArrayList di oggetti FcList corrispondente (per la visualizzazione nella popup).<p>
	 * Inoltre, restituisce il titolo della lista.
	 * @param session La corrente sessione di Hibernate
	 * @param entityId L'identificativo dell'entità
	 * @param byteArr L'array di byte che rappresenta il valore del campo LIST in DV_FORMAT_CLASSE
	 * @return Un'HashMap di chiavi String e valori Object. Il primo elemento dell'HashMap è l'ArrayList di oggetti 
	 * FcList corrispondente ai parametri passati, il secondo è un oggetto String che rappresenta il titolo della 
	 * lista.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private HashMap<String, Object> getListALFromXML(Session session, Long entityId, byte[] byteArr) throws Exception {
		HashMap<String, Object> retVal = new HashMap<String, Object>();
		ArrayList<FcList> listAL = new ArrayList<FcList>();
		String listTitle = new String();
		String xml = new String(byteArr, "UTF-8");
		SAXBuilder builder = new SAXBuilder();
		List content = builder.build(new StringReader(xml)).getContent();
		Iterator it = content.iterator();
		while (it.hasNext()) {
			Element view = (Element)it.next();
			listTitle = view.getAttributeValue(LIST_XML_LABELS.get("TITLE"));
			List viewChildren = view.getChildren();
			Iterator viewIt = viewChildren.iterator();
			while (viewIt.hasNext()) {
				Element columns = (Element)viewIt.next();
				List columnsChildren = columns.getChildren();
				Iterator columnsIt = columnsChildren.iterator();
				int index = 0;
				while (columnsIt.hasNext()) {
					Element column = (Element)columnsIt.next();
					//controllo non più utilizzato
					/*if (column.getAttribute(LIST_XML_LABELS.get("DCCOLUMN")) == null ||
							column.getAttribute(LIST_XML_LABELS.get("HIDDENKEY")) == null || 
							!column.getAttributeValue(LIST_XML_LABELS.get("HIDDENKEY")).equals(LIST_XML_LABELS.get("HIDDENKEY_VALUE"))) {*/
						FcList fcl = new FcList();
						fcl.setIndex(index);
						if (column.getAttribute(LIST_XML_LABELS.get("DCCOLUMN")) != null) {
							//colonna
							Long columnId = new Long(column.getAttributeValue(LIST_XML_LABELS.get("DCCOLUMN")));
							fcl.setSelectedColumn(getColumnLongDescFromId(session, columnId));
							fcl.setJs(false);
							fcl.setHeader("");
							fcl.setScript("");
							fcl.setImageurl("");
							fcl.setTesturl("");
							fcl.setParams(new ArrayList<SelectItem>());
							fcl.setSelectedParam("");
						}else{
							//javascript
							fcl.setSelectedColumn("");
							fcl.setJs(true);
							fcl.setHeader(column.getAttributeValue(LIST_XML_LABELS.get("HEADER")));
							fcl.setScript(column.getAttributeValue(LIST_XML_LABELS.get("SCRIPT")));
							fcl.setImageurl(column.getAttributeValue(LIST_XML_LABELS.get("IMAGEURL")));
							fcl.setTesturl(column.getAttributeValue(LIST_XML_LABELS.get("TESTURL")));
							ArrayList<SelectItem> params = new ArrayList<SelectItem>(); 
							List columnChildren = column.getChildren();
							Iterator columnIt = columnChildren.iterator();
							while (columnIt.hasNext()) {
								Element param = (Element)columnIt.next();
								Long columnId = new Long(param.getAttributeValue(LIST_XML_LABELS.get("DCCOLUMN")));
								String dcColumnName = getColumnLongDescFromId(session, columnId);
								params.add(new SelectItem(dcColumnName));
							}
							fcl.setParams(params);
							fcl.setSelectedParam("");
						}										
						listAL.add(fcl);
						index++;
					//}
				}
			}
		}
		retVal.put(LIST_AL, listAL);
		retVal.put(LIST_TITLE, listTitle);
		return retVal;
	}
	
	/**
	 * Dati l'identificativo di un'entità e un array di byte (valore del campo DETAIL in DV_FORMAT_CLASSE) 
	 * restituisce l'ArrayList di oggetti DetailGroup corrispondente (per la visualizzazione nella popup).<p>
	 * Inoltre, restituisce il titolo del dettaglio.
	 * @param session La corrente sessione di Hibernate
	 * @param entityId L'identificativo dell'entità
	 * @param byteArr L'array di byte che rappresenta il valore del campo DETAIL in DV_FORMAT_CLASSE
	 * @return Un'HashMap di chiavi String e valori Object. Il primo elemento dell'HashMap è l'ArrayList di oggetti 
	 * DetailGroup corrispondente ai parametri passati, il secondo è un oggetto String che rappresenta il titolo del 
	 * dettaglio.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private HashMap<String, Object> getDetailALFromXML(Session session, Long entityId, byte[] byteArr) throws Exception {
		HashMap<String, Object> retVal = new HashMap<String, Object>();
		ArrayList<DetailGroup> detailAL = new ArrayList<DetailGroup>();
		String detailTitle = new String();
		String xml = new String(byteArr, "UTF-8");
		SAXBuilder builder = new SAXBuilder();
		List content = builder.build(new StringReader(xml)).getContent();
		Iterator it = content.iterator();
		while (it.hasNext()) {
			Element view = (Element)it.next();
			detailTitle = view.getAttributeValue(DETAIL_XML_LABELS.get("TITLE"));
			List viewChildren = view.getChildren();
			Iterator viewIt = viewChildren.iterator();
			int groupIndex = 0;
			while (viewIt.hasNext()) {
				Element table = (Element)viewIt.next();
				DetailGroup dg = new DetailGroup();
				dg.setIndex(groupIndex);
				ArrayList<Detail> groupDetailAL = new ArrayList<Detail>();
				List tableChildren = table.getChildren();
				Iterator tableIt = tableChildren.iterator();
				boolean isTable = false;
				int rowIndex = 0;
				while (tableIt.hasNext()) {
					Element row = (Element)tableIt.next();
					List rowChildren = row.getChildren();
					Iterator rowIt = rowChildren.iterator();
					Detail d = new Detail();
					d.setFirst(false); //la riga con il titolo verrà aggiunta dopo
					d.setTitle("");
					d.setSelectedColumn1("");
					d.setSelectedColumn2("");
					d.setSelectedColumn3("");
					int columnIndex = 0;
					while (rowIt.hasNext()) {
						Element column = (Element)rowIt.next();
						if (rowIndex == 0 && columnIndex == 0) {
							Object contObj = (column.getContent() != null && column.getContent().iterator().hasNext()) ?
											column.getContent().iterator().next() :
											null;
							contObj = (contObj != null && contObj.getClass().equals(CDATA.class)) ? contObj : null;
							contObj = (contObj != null && ((CDATA)contObj).getText().equals(DETAIL_XML_LABELS.get("BREAK_VALUE"))) ?
										contObj :
										null;
							isTable = (contObj == null);
							dg.setTable(isTable);
						}
						if (isTable) {
							String attrValue = column.getAttributeValue(DETAIL_XML_LABELS.get("DCCOLUMN"));
							if (attrValue != null && !attrValue.equals("")) {
								Long columnId = new Long(attrValue);
								String columnDesc = getColumnLongDescFromId(session, columnId);
								if (columnIndex == 0)
									d.setSelectedColumn1(columnDesc);
								else if (columnIndex == 1)
									d.setSelectedColumn2(columnDesc);
								else if (columnIndex == 2)
									d.setSelectedColumn3(columnDesc);
							}
						}
						columnIndex++;
					}
					d.setTable(isTable);
					if (isTable && rowIndex == 0)
						rowIndex = 1; //se si tratta di una tabella la prima riga serve per visualizzare il titolo
					d.setIndex(rowIndex);
					groupDetailAL.add(d);
					rowIndex++;
				}
				//se non ci sono righe si tratta di una tabella (vuota)
				dg.setTable(dg.isTable() || rowIndex == 0);
				//se si tratta di una tabella aggiungo la prima riga per visualizzare il titolo
				if (dg.isTable()) {
					Detail d = new Detail();
					d.setIndex(0);
					d.setFirst(true);
					d.setTable(true);
					d.setTitle(table.getAttributeValue(DETAIL_XML_LABELS.get("TITLE")));
					d.setSelectedColumn1("");
					d.setSelectedColumn2("");
					d.setSelectedColumn3("");
					groupDetailAL.add(0, d);
				}
				dg.setDetailAL(groupDetailAL);
				detailAL.add(dg);
				groupIndex++;
			}
		}		
		retVal.put(DETAIL_AL, detailAL);
		retVal.put(DETAIL_TITLE, detailTitle);
		return retVal;
	}
	
	/**
	 * Dati l'identificativo di una classe e l'identificativo di una DC_ENTITY, restituisce un'ArrayList di oggetti 
	 * Link, che rappresenta la lista dei possibili link per la classe che si sta inserendo o modificando.
	 * @param paramSession La corrente sessione di Hibernate (se null viene aperta una nuova sessione)
	 * @param clazzId L'identificativo della classe che si sta inserendo (se clazzId è uguale a null) o modificando
	 * @param dcentId L'identificativo della DC_ENTITY selezionata nella popup di gestione classi
	 * @return Un'ArrayList di oggetti Link, che rappresenta la lista dei possibili link per la classe che si sta 
	 * inserendo o modificando.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<Link> getLinkAL(Session paramSession, Long clazzId, Long dcentId) throws Exception {
		ArrayList<Link> retVal = new ArrayList<Link>();
		ArrayList<DcEntityRel> dcEntRelArr1 = new ArrayList<DcEntityRel>();
		ArrayList<DcEntityRel> dcEntRelArr2 = new ArrayList<DcEntityRel>();
		Session session = null;
		boolean closeSession = paramSession == null;
		try {
			
			session = paramSession == null ? sf.openSession() : paramSession;
			//leggo DC_ENTITY_REL per FK_DC_ENTITY1 = dcentId parametro e prendo tutte le FK_DC_ENTITY2
			Query q = session.createQuery("from DC_ENTITY_REL in class DcEntityRel where DC_ENTITY_REL.dcEntityByFkDcEntity1 = :dcEntityByFkDcEntity1");
			DcEntityRel dcentrel = new DcEntityRel();
			DcEntity dcent = new DcEntity();
			dcent.setId(dcentId);
			dcentrel.setDcEntityByFkDcEntity1(dcent);
			q.setProperties(dcentrel);
			List dcentrels = q.list();
			Iterator it = dcentrels.iterator();
			while (it.hasNext()) {
				dcEntRelArr2.add(((DcEntityRel)it.next()));
			}
			
			/* MARCORIC 9 MARZO 2007
			 * HO STABILITO CHE I LINK POSSIBILI SONO SOLO QUELLI NEI QUALI NELLA RELAZIONE LA MIA ENTITY OCCUPA LA PARTE DESTRA
			 * dUNQUE HO TOLTO LA PARTE SOTTO
			//leggo DC_ENTITY_REL per FK_DC_ENTITY2 = dcentId parametro e prendo tutte le FK_DC_ENTITY1
			q = session.createQuery("from DC_ENTITY_REL in class DcEntityRel where DC_ENTITY_REL.dcEntityByFkDcEntity2 = :dcEntityByFkDcEntity2");
			dcentrel = new DcEntityRel();
			dcent = new DcEntity();
			dcent.setId(dcentId);
			dcentrel.setDcEntityByFkDcEntity2(dcent);
			q.setProperties(dcentrel);
			dcentrels = q.list();
			it = dcentrels.iterator();
			while (it.hasNext()) {
				dcEntRelArr1.add(((DcEntityRel)it.next()));
			}
			 */

			//valorizzo l'ArrayList links
			ArrayList<Link> links = new ArrayList<Link>();
			ArrayList<DcEntity> dcEnts = new ArrayList<DcEntity>();
			ArrayList<Long> dcEntRelIds = new ArrayList<Long>();
			for (DcEntityRel myDcEntRel : dcEntRelArr2) {
				dcEnts.add(myDcEntRel.getDcEntityByFkDcEntity2());
				dcEntRelIds.add(myDcEntRel.getId());
			}
			for (DcEntityRel myDcEntRel : dcEntRelArr1) {
				dcEnts.add(myDcEntRel.getDcEntityByFkDcEntity1());
				dcEntRelIds.add(myDcEntRel.getId());
			}
			int count = 0;
			for (DcEntity myDcEnt : dcEnts) {
				//per ogni FK_DC_ENTITY1 e FK_DC_ENTITY2 cerco tutte le classi collegate, e i relativi progetti
				Query qUseEnts = session.createQuery("from DV_USER_ENTITY in class DvUserEntity where DV_USER_ENTITY.dcEntity = :dcEntity");
				DvUserEntity dvuseent = new DvUserEntity();
				dvuseent.setDcEntity(myDcEnt);
				qUseEnts.setProperties(dvuseent);
				List dvuseents = qUseEnts.list();
				Iterator itUseEnts = dvuseents.iterator();
				while (itUseEnts.hasNext()) {
					DvUserEntity myDvUseEnt = (DvUserEntity)itUseEnts.next();
					//cerco in DV_FORMAT_CLASSE
					Query qForClas = session.createQuery("from DV_FORMAT_CLASSE in class DvFormatClasse where DV_FORMAT_CLASSE.dvUserEntity = :dvUserEntity");
					DvFormatClasse dvforcla = new DvFormatClasse();
					dvforcla.setDvUserEntity(myDvUseEnt);
					qForClas.setProperties(dvforcla);
					List dvforclas = qForClas.list();
					Iterator itForClas = dvforclas.iterator();
					while (itForClas.hasNext()) {
						//cerco id e nome classe, e id e nome progetto relativi
						DvFormatClasse myDvForCla = (DvFormatClasse)itForClas.next();
						DvClasse dvCla = myDvForCla.getDvClasse();
						DvProgetto dvPro = dvCla == null ? null : dvCla.getDvProgetto();
						Link link = new Link();
						link.setProjectId(dvPro == null ? null : dvPro.getId());
						link.setProjectName(dvPro == null ? "" : dvPro.getName());
						link.setClassId(dvCla == null ? null : dvCla.getId());
						link.setClassName(dvCla == null ? "" : dvCla.getName());
						link.setLinkId(null); // si verifica dopo
						link.setLinkName(""); //si verifica dopo
						link.setDcEntityRelId(dcEntRelIds.get(count));
						link.setSelected(false); //si verifica dopo
						link.setDeletingAllKeys(false);
						links.add(link);
					}
				}
				count++;
			}
			//valorizzo retVal eliminando eventuali doppioni (verificare se è possibile che ci siano)
			for (int i = 0; i < links.size(); i++) {
				Link linkToAdd = links.get(i);
				boolean add = true;
				for (int j = 0; j < retVal.size(); j++) {
					Link addedLink = retVal.get(j);
					if (addedLink.getProjectId().longValue() == linkToAdd.getProjectId().longValue() && 
						addedLink.getClassId().longValue() == linkToAdd.getClassId().longValue()) {
						add = false;
					}
				}
				if (add) {
					retVal.add(linkToAdd);
				}
			}
			//ora (se sono in modifica di una classe) scorro retVal e verifico, per ogni link disponibile, se è già stato inserito o meno
			if (clazzId != null) {
				for (Link myLink : retVal) {
					Query qLink = session.createQuery("from DV_LINK in class DvLink where DV_LINK.dcEntityRel = :dcEntityRel");
					DvLink dvlin = new DvLink();
					DcEntityRel dcentrelforlink = (DcEntityRel)session.load(DcEntityRel.class, myLink.getDcEntityRelId());
					dvlin.setDcEntityRel(dcentrelforlink);
					qLink.setProperties(dvlin);
					List dvlinks = qLink.list();
					Iterator itLinks = dvlinks.iterator();
					while (itLinks.hasNext()) {
						DvLink myDvLink = (DvLink)itLinks.next();
						Set myDvLinkClasses = myDvLink.getDvLinkClasses();
						Iterator itLinkClasses = myDvLinkClasses.iterator();
						while (itLinkClasses.hasNext()) {
							DvLinkClasse myDvLinkClasse = (DvLinkClasse)itLinkClasses.next();
							if (myDvLinkClasse.getDvClasseByFkDvClasse1().getId().longValue() == clazzId.longValue() &&
								myDvLinkClasse.getDvClasseByFkDvClasse2().getId().longValue() == myLink.getClassId().longValue()) {
								// se il link è già stato inserito valorizzo alcuni campi dell'oggetto Link corrente
								myLink.setLinkId(myDvLinkClasse.getId());
								myLink.setLinkName(myDvLinkClasse.getName());
								myLink.setSelected(true);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen())
				session.close();
		}		
		return retVal;
	}
	
	/**
	 * Effettua il salvataggio (inserimento, modifica e/o cancellazione) dei link tramite inserimento, modifica e/o 
	 * cancellazione in tabella DV_LINK_CLASSE e operazioni nelle tabelle ad essa collegate da relazioni di chiave 
	 * esterna.
	 * @param session La corrente sessione di Hibernate
	 * @param clazzId L'identificativo della classe inserita o modificata, per la quale si definiscono i link
	 * @param linkAL ArrayList di oggetti Link, che contiene l'insieme dei link da inserire, modificare o cancellare
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private void saveLink(Session session, Long clazzId, ArrayList<Link> linkAL) throws Exception {
		/* valorizzo preliminarmente un'ArrayList per gestire alla fine delle operazioni l'eventuale cancellazione 
		 * delle chiavi relative ai link cancellati */
		// al momento questo ArrayList sarà sempre vuoto in quanto la confirm nella jsp è stata tolta...
		ArrayList<DvLink> linksDeletingAllKeys = new ArrayList<DvLink>();
		for (Link link : linkAL) {
			if (link.getLinkId() != null && !link.isSelected() && link.isDeletingAllKeys()) {
				DvLink dvLink = ((DvLinkClasse)session.load(DvLinkClasse.class, link.getLinkId())).getDvLink();
				if (dvLink != null) {
					boolean add = true;
					for (DvLink myDvLink : linksDeletingAllKeys) {
						if (dvLink.getId().longValue() == myDvLink.getId().longValue()) {
							add = false;
							break;
						}	
					}
					if (add) {
						linksDeletingAllKeys.add(dvLink);
					}
				}
			}
		}
		for (Link link : linkAL) {
			if (link.isSelected()) {
				if (link.getLinkId() == null) {
					//inserimento
					Long dcEntityRelId = link.getDcEntityRelId();
					DvLink dvLin = getDvLinkFromDcEntityRelId(session, dcEntityRelId);
					if (dvLin == null) {
						//inserimento in DV_LINK
						dvLin = new DvLink();
						dvLin.setDcEntityRel((DcEntityRel)session.load(DcEntityRel.class, dcEntityRelId));
						session.saveOrUpdate(dvLin);
					}
					//inserimento in DV_LINK_CLASSE
					DvLinkClasse dvLinCla = new DvLinkClasse();
					dvLinCla.setDvClasseByFkDvClasse1((DvClasse)session.load(DvClasse.class, clazzId));
					dvLinCla.setDvClasseByFkDvClasse2((DvClasse)session.load(DvClasse.class, link.getClassId()));
					dvLinCla.setDvLink(dvLin);
					dvLinCla.setName(link.getLinkName() != null && link.getLinkName().equals("") ? null : link.getLinkName());
					session.saveOrUpdate(dvLinCla);					
				}else{
					//modifica (viene modificato solo il valore di NAME in DV_LINK_CLASSE)
					DvLinkClasse dvLinCla = (DvLinkClasse)session.load(DvLinkClasse.class, link.getLinkId());
					dvLinCla.setName(link.getLinkName() != null && link.getLinkName().equals("") ? null : link.getLinkName());
					session.saveOrUpdate(dvLinCla);
				}
			}else{
				if (link.getLinkId() != null) {
					//cancellazione in DV_LINK_CLASSE
					session.delete(session.load(DvLinkClasse.class, link.getLinkId()));
				}
			}
		}
		// al momento linksDeletingAllKeys sarà sempre vuoto in quanto la confirm nella jsp è stata tolta...
		// 21-11-2007 : non si fa più questa cosa visto che le tabelle dvkey e keypair sono state abbandonate
		// 				per problemi prestazionali
		/**
		for (DvLink myDvLink : linksDeletingAllKeys) {
			//cancellazione chiavi:
			//se non ci sono altri record in DV_LINK_CLASSE con FK_DV_LINK uguale a FK_DV_LINK del link cancellato...
			if (!hasDvLinkClasses(session, myDvLink)) {
				//cancellazione in DV_KEY_PAIR per FK_DV_LINK uguale a FK_DV_LINK del link cancellato
				ArrayList<DvKey> dvKeys = deleteKeyPairs(session, myDvLink);
				//cancellazione in DV_LINK per ID uguale a FK_DV_LINK del link cancellato
				session.delete(myDvLink);
				//cancellazione in DV_KEY dei record che non sono più referenziati in DV_KEY_PAIR da FK_DV_KEY1 e FK_DV_KEY2
				deleteKeysFromKeyPairs(session, dvKeys);
			}
		}
		*/
	}
	
	/**
	 * Dato l'identificativo di una DC_ENTITY_REL, restituisce un oggetto DvLink corrispondente al record di 
	 * DV_LINK con FK_DC_ENTITY_REL uguale all'identificativo passato a parametro.<p>
	 * Se in DV_LINK non c'è un record con FK_DC_ENTITY_REL uguale a dcEntityRelId restituisce null.
	 * @param paramSession La corrente sessione di Hibernate (se null viene aperta una nuova sessione)
	 * @param dcEntityRelId L'identificativo della DC_ENTITY_REL
	 * @return Un oggetto DvLink corrispondente al record di DV_LINK con FK_DC_ENTITY_REL uguale all'identificativo 
	 * passato a parametro (null se in DV_LINK non c'è un record con FK_DC_ENTITY_REL uguale a dcEntityRelId).
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private DvLink getDvLinkFromDcEntityRelId(Session paramSession, Long dcEntityRelId) throws Exception {
		if (dcEntityRelId == null)
			return null;
		DvLink retVal = null;
		Session session = null;
		boolean closeSession = paramSession == null;
		try {
			session = paramSession == null ? sf.openSession() : paramSession;
			Query q = session.createQuery("from DV_LINK in class DvLink where DV_LINK.dcEntityRel=:dcEntityRel");
			DvLink dvlin = new DvLink();
			DcEntityRel dcentrel = (DcEntityRel)session.load(DcEntityRel.class, dcEntityRelId);
			dvlin.setDcEntityRel(dcentrel);
			q.setProperties(dvlin);
			List dvlins = q.list();
			Iterator it = dvlins.iterator();
			if (it.hasNext()) {
				// una relazione 1..1
				retVal = (DvLink)it.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}
	
	/**
	 * Dato un oggetto DvLink, verifica se in DV_LINK_CLASSE ci sono record con FK_DV_LINK uguale all'identificativo 
	 * del DvLink passato a parametro.
	 * @param paramSession La corrente sessione di Hibernate (se null viene aperta una nuova sessione)
	 * @param dvLink L'oggetto DvLink
	 * @return Un flag che indica se in DV_LINK_CLASSE ci sono record con FK_DV_LINK uguale all'identificativo 
	 * del DvLink passato a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private boolean hasDvLinkClasses(Session paramSession, DvLink dvLink) throws Exception {
		if (dvLink == null || dvLink.getId() == null)
			return false;
		boolean retVal = true;
		Session session = null;
		boolean closeSession = paramSession == null;
		try {
			session = paramSession == null ? sf.openSession() : paramSession;
			Query q = session.createQuery("from DV_LINK_CLASSE in class DvLinkClasse where DV_LINK_CLASSE.dvLink=:dvLink");
			DvLinkClasse dvlincla = new DvLinkClasse();
			dvlincla.setDvLink(dvLink);
			q.setProperties(dvlincla);
			List dvlinclas = q.list();
			Iterator it = dvlinclas.iterator();
			retVal = it.hasNext();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}
	
	/**
	 * Dato l'identificativo di una classe, restituisce un flag che indica se la classe ha dei link (cioè, se ci 
	 * sono record in DV_LINK_CLASSE con FK_DV_CLASSE1 uguale all'identificativo della classe) se primary = true, 
	 * oppure se la classe fa parte di link (cioè, se ci sono record in DV_LINK_CLASSE con FK_DV_CLASSE2 uguale 
	 * all'identificativo della classe) se primary = false.
	 * @param paramSession La corrente sessione di Hibernate (se null viene aperta una nuova sessione)
	 * @param clazzId L'identificativo della classe
	 * @param primary Flag che indica se l'identificativo passato a parametro deve essere cercato in DV_LINK_CLASSE 
	 * per FK_DV_CLASSE1 o per FK_DV_CLASSE2.
	 * @return Un flag che indica se la classe ha dei link
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public boolean hasClassLinks(Session paramSession, Long clazzId, boolean primary) throws Exception {
		if (clazzId == null)
			return false;
		boolean retVal = false;
		Session session = null;
		boolean closeSession = paramSession == null;
		try {
			session = paramSession == null ? sf.openSession() : paramSession;
			Query q = null;
			if (primary) 
				q = session.createQuery("from DV_LINK_CLASSE in class DvLinkClasse where DV_LINK_CLASSE.dvClasseByFkDvClasse1=:dvClasseByFkDvClasse1");
			else
				q = session.createQuery("from DV_LINK_CLASSE in class DvLinkClasse where DV_LINK_CLASSE.dvClasseByFkDvClasse2=:dvClasseByFkDvClasse2");
			DvLinkClasse dvlincla = new DvLinkClasse();
			DvClasse dvcla = (DvClasse)session.load(DvClasse.class, clazzId);
			if (primary)
				dvlincla.setDvClasseByFkDvClasse1(dvcla);
			else
				dvlincla.setDvClasseByFkDvClasse2(dvcla);
			q.setProperties(dvlincla);
			List dvlinclas = q.list();
			Iterator it = dvlinclas.iterator();
			retVal = it.hasNext();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (closeSession && session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}
	
	/**
	 * A seguito della cancellazione di un link, prepara, se prevista, la cancellazione del corrispondente record in 
	 * tabella DV_LINK, tramite la cancellazione dei record in DV_KEY_PAIR che hanno FK_DV_LINK uguale all'ID del 
	 * record di DV_LINK da cancellare.<p>
	 * Restituisce un'ArrayList di oggetti DvKey. Questi rappresentano i record di DV_KEY che dovranno essere 
	 * a loro volta cancellati, qualora non siano più referenziati, a seguito delle cancellazioni in DV_KEY_PAIR, in 
	 * tale tabella da FK_DV_KEY1 e FK_DV_KEY2.
	 * @param session La corrente sessione di Hibernate
	 * @param dvLink L'oggetto DvLink che rappresenta il record di DV_LINK che dovrà essere cancellato.
	 * @return Un'ArrayList di oggetti DvKey, che rappresentano i record di DV_KEY che dovranno essere 
	 * a loro volta cancellati, qualora non siano più referenziati, a seguito delle cancellazioni in DV_KEY_PAIR, in 
	 * tale tabella da FK_DV_KEY1 e FK_DV_KEY2.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	// 22-11-2007 tolto il metodo per inutlizzo
/**
	private ArrayList<DvKey> deleteKeyPairs(Session session, DvLink dvLink) throws Exception {
		ArrayList<DvKey> retVal = new ArrayList<DvKey>();
		Query q = session.createQuery("from DV_KEY_PAIR in class DvKeyPair where DV_KEY_PAIR.dvLink=:dvLink");
		DvKeyPair dvkeypai = new DvKeyPair();
		dvkeypai.setDvLink(dvLink);
		q.setProperties(dvkeypai);
		List dvkeypais = q.list();
		Iterator it = dvkeypais.iterator();
		while (it.hasNext()) {
			DvKeyPair element = (DvKeyPair)it.next();
			DvKey dvKey1 = element.getDvKeyByFkDvKey1();
			DvKey dvKey2 = element.getDvKeyByFkDvKey2();
			//aggiungo se necessario gli oggetti DvKey corrispondenti alle chiavi esterne dei record da cancellare 
			//  all'ArrayList che verrà restituito dal metodo 
			boolean add = true;
			for (DvKey myDvKey : retVal) {
				if (dvKey1.getId().longValue() == myDvKey.getId().longValue()) {
					add = false;
					break;
				}	
			}
			if (add) {
				retVal.add(dvKey1);
			}
			add = true;
			for (DvKey myDvKey : retVal) {
				if (dvKey2.getId().longValue() == myDvKey.getId().longValue()) {
					add = false;
					break;
				}	
			}
			if (add) {
				retVal.add(dvKey2);
			}
			//cancellazione in DV_KEY_PAIR
			session.delete(element);
		}
		return retVal;
	}	
*/
	/* METODO NON UTILIZZATO
	 * Verifica se tra le colonne aggiunte alla lista ci sono tutti i campi chiave dell'entità selezionata.<p>
	 * Restituisce un'ArrayList di oggetti Long che rappresentano gli identificativi dei campi chiave eventualmente 
	 * non aggiunti alla lista.
	 * @param session La corrente sessione di Hibernate
	 * @param dcEntityId L'identificativo dell'entità selezionata
	 * @param listAL Un'ArrayList di oggetti FcList che rappresentano gli elementi disponibili per la lista
	 * @return Un'ArrayList di oggetti Long che rappresentano gli identificativi dei campi chiave eventualmente 
	 * non aggiunti alla lista.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	/*private ArrayList<Long> getHiddenKeys(Session session, Long dcEntityId, ArrayList<FcList> listAL) throws Exception {
		ArrayList<Long> retVal = new ArrayList<Long>();
		if (listAL == null)
			listAL = new ArrayList<FcList>(); //deve comunque procedere...
		ArrayList<Long> columnIds = new ArrayList<Long>();
		for (FcList fcl : listAL) {
			if (!fcl.isJs()) {
				String dcColumnName = fcl.getSelectedColumn();
				columnIds.add(getColumnIdFromLongDesc(session, dcEntityId, dcColumnName));
			}
		}
		ArrayList<Long> keyIds = new ArrayList<Long>();
		Set entityColumns = ((DcEntity)session.load(DcEntity.class, dcEntityId)).getDcColumns();
		Iterator entityColumnsIt = entityColumns.iterator();
		while (entityColumnsIt.hasNext()) {
			DcColumn myDcCol = (DcColumn)entityColumnsIt.next();
			Query q = session.createQuery("from DC_ATTRIBUTE in class DcAttribute " +
					"where DC_ATTRIBUTE.dcColumn=:dcColumn " +
					"and DC_ATTRIBUTE.attributeSpec=:attributeSpec");
			DcAttribute dcatt = new DcAttribute();
			dcatt.setDcColumn(myDcCol);
			dcatt.setAttributeSpec(KEY);
			q.setProperties(dcatt);
			if (q.list().iterator().hasNext())
				keyIds.add(myDcCol.getId());
		}
		for (Long keyId : keyIds) {
			boolean trovato = false;
			for (Long columnId : columnIds) {
				if (columnId.longValue() == keyId.longValue()) {
					trovato = true;
					break;
				}
			}
			if (!trovato) {
				retVal.add(keyId);
			}
		}
		return retVal;
	}*/
	
	/**
	 * Verifica che i record di DV_KEY rappresentati dagli oggetti DvKey dell'ArrayList passata a parametro non siano 
	 * più referenziati in DV_KEY_PAIR da FK_DV_KEY1 e FK_DV_KEY2. In caso affermativo effettua la cancellazione in 
	 * DV_KEY.
	 * @param session La corrente sessione di Hibernate
	 * @param dvKeys ArrayList di oggetti DvKey che rappresentano i record di DV_KEY eventualmente da cancellare.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	// 22-11-2007 tolto il metodo per inutilizzo
/**
	private void deleteKeysFromKeyPairs(Session session, ArrayList<DvKey> dvKeys) throws Exception {
		for (DvKey myDvKey : dvKeys) {
			boolean isKey1 = false;
			boolean isKey2 = false;
			//verifica in DV_KEY_PAIR per FK_DV_KEY1 uguale all'identificativo dell'oggetto DvKey corrente
			Query q = session.createQuery("from DV_KEY_PAIR in class DvKeyPair where DV_KEY_PAIR.dvKeyByFkDvKey1=:dvKeyByFkDvKey1");
			DvKeyPair dvkeypai = new DvKeyPair();
			dvkeypai.setDvKeyByFkDvKey1(myDvKey);
			q.setProperties(dvkeypai);
			List dvkeypais = q.list();
			Iterator it = dvkeypais.iterator();
			isKey1 = it.hasNext();
			//verifica in DV_KEY_PAIR per FK_DV_KEY2 uguale all'identificativo dell'oggetto DvKey corrente
			q = session.createQuery("from DV_KEY_PAIR in class DvKeyPair where DV_KEY_PAIR.dvKeyByFkDvKey2=:dvKeyByFkDvKey2");
			dvkeypai = new DvKeyPair();
			dvkeypai.setDvKeyByFkDvKey2(myDvKey);
			q.setProperties(dvkeypai);
			dvkeypais = q.list();
			it = dvkeypais.iterator();
			isKey2 = it.hasNext();
			//cancellazione in DV_KEY
			if (!isKey1 && !isKey2)
				session.delete(myDvKey);
		}
	}
*/
	/**
	 * Metodo di utilità per evitare le eccezioni di type safety nell'uso dei Generics di Java 5.0.<p>
	 * Data un'ArrayList (generica), restituisce la corrispondente ArrayList di Filter.
	 * @param al ArrayList generica
	 * @return La corrispondente ArrayList di Filter
	 */
	private ArrayList<Filter> getFilterArrayList(ArrayList al) {
		if (al == null) return null;
		ArrayList<Filter> retVal = new ArrayList<Filter>();
		if (al.size() == 0) return retVal;
		for (Object obj : al) {
			//si dà per scontato che il casting non dia eccezioni
			retVal.add((Filter)obj);
		}
		return retVal;
	}
	
	/**
	 * Metodo di utilità per evitare le eccezioni di type safety nell'uso dei Generics di Java 5.0.<p>
	 * Data un'ArrayList (generica), restituisce la corrispondente ArrayList di FcList.
	 * @param al ArrayList generica
	 * @return La corrispondente ArrayList di FcList
	 */
	private ArrayList<FcList> getFcListArrayList(ArrayList al) {
		if (al == null) return null;
		ArrayList<FcList> retVal = new ArrayList<FcList>();
		if (al.size() == 0) return retVal;
		for (Object obj : al) {
			//si dà per scontato che il casting non dia eccezioni
			retVal.add((FcList)obj);
		}
		return retVal;
	}
	
	/**
	 * Metodo di utilità per evitare le eccezioni di type safety nell'uso dei Generics di Java 5.0.<p>
	 * Data un'ArrayList (generica), restituisce la corrispondente ArrayList di DetailGroup.
	 * @param al ArrayList generica
	 * @return La corrispondente ArrayList di DetailGroup
	 */
	private ArrayList<DetailGroup> getDetailGroupArrayList(ArrayList al) {
		if (al == null) return null;
		ArrayList<DetailGroup> retVal = new ArrayList<DetailGroup>();
		if (al.size() == 0) return retVal;
		for (Object obj : al) {
			//si dà per scontato che il casting non dia eccezioni
			retVal.add((DetailGroup)obj);
		}
		return retVal;
	}
	
}
