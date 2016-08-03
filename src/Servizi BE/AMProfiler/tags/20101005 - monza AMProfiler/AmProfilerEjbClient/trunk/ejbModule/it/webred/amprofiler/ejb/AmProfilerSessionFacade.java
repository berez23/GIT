package it.webred.amprofiler.ejb;

import it.webred.amprofiler.model.AmSection;
import it.webred.amprofiler.model.Anagrafica;

import java.util.List;

/**
 * @author Pietro Bartoccioni
 *
 */
public interface AmProfilerSessionFacade {
	
	/** Ricerca le Sezioni(AmSection) partendo dal Nome Applicazione o dal Nome Comune al quale sono associate,
	 *  nel caso che non venga fornito nessuno dei due parametri(entrambi = null) le sezioni globali al sistema.
	 *  
	 * @param applicationName Nome applicazione della quale recuperare le sezioni(nel caso che
	 * 	 vengono passati entrambi i parametri viene preso in considerazione solo questo).
	 * @param amComuneName Nome del comune del quale recuperare le sezioni.
	 * @return
	 */
	public List<AmSection> findAmSections(String applicationName,String amComuneName) ;

	/** Recupera una sezione partendo dalla sua chiave primaria (id)
	 * @param id Chiave primaria sezione da recuperare.
	 * @return
	 */
	AmSection findAmSectionById(Integer id);
	
	/** Recupera l'anagrafica partendo dallo User Name alla quale è collegata.
	 * @param userName User name dell'anagrafica da recuperare.
	 * @return
	 */
	Anagrafica findAnagraficaByUserName(String userName);

	/** Recupera l'anagrafica partendo dal suo codice fiscale.
	 * @param codiceFiscale Codice fiscale dell'anagrafica da recuperare
	 * @return
	 */
	Anagrafica findAnagraficaById(Integer id);

	/** Recupera l'anagrafica partendo dal suo codice fiscale.
	 * @param codiceFiscale Codice fiscale dell'anagrafica da recuperare
	 * @return
	 */
	List<Anagrafica>  findAnagraficaByCodiceFiscale(String codiceFiscale);

	/** Crea e rende persistente una nuova sezione(AmSection) comprensiva della lista delle sue chiave/valore (AnKeyValue)
	 * eventualmente associandola ad una applicazione, ad un comune oppure a nessuno dei due(Sezione globale).
	 * Anche le istanze della lista chiave/valore vengono create.
	 * @param amSection Istanza del pojo della sezione con la lista delle istanze delle sue AnKeyValue valorizzata
	 * @param amApplicationName Nome dell'applicazione alla quale associare la sezione.
	 * @param amComuneName Nome del comune al quale associare la sezione.
	 * @return
	 */
	boolean createAmSection(AmSection amSection, String amApplicationName, String amComuneName);

	/** Elimina al sezione e tutte le relative istanze chiave/valore
	 * @param id L'id della sezione da eliminare.
	 */
	void deleteAmSection(Integer id);

	/**Aggiorna la sezione rendendo persistenti le modifiche effettuate sull'istanza della sezione(pojo)
	 * @param amSection L'istanza della sezione(pojo) da aggiornare.
	 */
	void updateAmSection(AmSection amSection); 
}
