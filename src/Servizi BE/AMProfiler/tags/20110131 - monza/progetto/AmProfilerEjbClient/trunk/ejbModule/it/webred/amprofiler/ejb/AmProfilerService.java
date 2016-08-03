package it.webred.amprofiler.ejb;

import it.webred.amprofiler.model.Anagrafica;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.model.AmSection;

import java.util.List;

/**
 * @author Pietro Bartoccioni
 *
 */
public interface AmProfilerService {
	
	/** Ricerca le Sezioni(AmSection) partendo dal Nome Applicazione, dall'id della Fonte alla quale sono associate,
	 *  nel caso che non venga fornito nessuno dei due parametri(entrambi = null) le sezioni globali al sistema.
	 *  
	 * @param applicationName Nome applicazione della quale recuperare le sezioni.
	 * @param idFonte Id della Fonte della quale recuperare le sezioni.
	 * @return
	 */
	public List<AmSection> findAmSections(String applicationName, Integer idFonte) ;

	/** Ricerca le Sezioni(AmSection) partendo dal Nome Applicazione alla quale sono associate.
	 *  
	 * @param applicationName Nome applicazione della quale recuperare le sezioni.
	 * @return
	 */
	public List<AmSection> findAmSections(String applicationName);

	/** Ricerca le Sezioni(AmSection) partendo dall'id della Fonte alla quale sono associate.
	 *  
	 * @param idFonte Id della Fonte della quale recuperare le sezioni.
	 * @return
	 */
	public List<AmSection> findAmSections(Integer idFonte) ;

	/** Recupera le Sezioni(AmSection) globali ossia tutte quelle non associate ne a fonti ne ad applicazione.
	 *  
	 * @return
	 */
	public List<AmSection> findGlobalAmSections() ;

	/** Recupera una sezione partendo dalla sua chiave primaria (id)
	 * @param id Chiave primaria sezione da recuperare.
	 * @return
	 */
	AmSection findAmSectionById( Integer id);
	
	/** Recupera l'anagrafica partendo dallo User Name alla quale è collegata.
	 * @param userName User name dell'anagrafica da recuperare.
	 * @return
	 */
	Anagrafica findAnagraficaByUserName(String userName);

	/** Recupera l'anagrafica partendo dal suo codice fiscale.
	 * @param id Id dell'anagrafica da recuperare
	 * @return
	 */
	Anagrafica findAnagraficaById(Integer id);

	/** Recupera l'anagrafica partendo dal suo codice fiscale.
	 * @param codiceFiscale Codice fiscale dell'anagrafica da recuperare
	 * @return
	 */
	List<Anagrafica>  findAnagraficaByCodiceFiscale(String userName, String codiceFiscale);

	/** Crea e rende persistente una nuova sezione(AmSection) comprensiva della lista delle sue chiave/valore (AnKeyValue)
	 * eventualmente associandola ad una applicazione, ad una istanza, ad un comune in una determinata istanza oppure a nessuno dei due(Sezione globale).
	 * Anche le istanze della lista chiave/valore vengono create.
	 * @param amSection Istanza del pojo della sezione con la lista delle istanze delle sue AnKeyValue valorizzata
	 * @param amApplicationName Nome dell'applicazione alla quale la sezione appartiene.
	 * @param amIdFonte id della Fonte alla quale la sezione appartiene.
	 * @return
	 */
	boolean createAmSection(AmSection amSection, String amApplicationName, Integer amIdFonte);

	/** Elimina al sezione e tutte le relative istanze chiave/valore
	 * @param id L'id della sezione da eliminare.
	 */
	void deleteAmSection(Integer id);

	/**Aggiorna la sezione rendendo persistenti le modifiche effettuate sull'istanza della sezione(pojo)
	 * @param amSection L'istanza della sezione(pojo) da aggiornare.
	 */
	void updateAmSection(AmSection amSection);

	List<AmKeyValueExt> findAmKeyValueExtByInstance(String instanceName) ;

	/** Ricerca i parametri(AmKeyValueExt) partendo dal Nome Instance al quale sono associati +
	 *  i parametri associati con overw_type 99 all'Applicazione.
	 *  
	 * @param instanceName Nome instance della quale recuperare le sezioni.
	 * @return
	 */
	
	List<AmKeyValueExt> findAmKeyValueExtByInstanceComune(String comune, String instanceName) ;

	/** Ricerca i parametri(AmKeyValueExt) partendo dal codice Belfiore del comune al quale sono associati + 
	 * i parametri della funzione findAmKeyValueExtByInstance.
	 *
	 * @param comune Codice belfiore del comune.
	 * @param instanceName Nome instance della quale recuperare le sezioni.
	 * @return
	 */
	
	List<AmKeyValueExt> findAmKeyValueExtByFonte(Integer idFonte) ;

	/** Ricerca i parametri(AmKeyValueExt) con overw_type 99 partendo dall'Id della Fonte al quale sono associati
	 *
	 * @param idFonte Id della fonte.
	 * @return
	 */
	
	List<AmKeyValueExt> findAmKeyValueExtByFonteComune(String comune, Integer idFonte) ;

	/** Ricerca i parametri(AmKeyValueExt) partendo dal codice Belfiore del comune al quale sono associati + 
	 * i parametri della funzione findAmKeyValueExtByFonte.
	 * 
	 * @param comune Codice belfiore del comune.
	 * @param idFonte Id della fonte.
	 * @return
	 */
	
	List<AmKeyValueExt> findAmKeyValueExtByComune(String comune) ;

	/** Ricerca i parametri(AmKeyValueExt) ottenuti dalla funzione findAmKeyValueExtByFonteComune e findAmKeyValueExtByInstanceComune
	 * usando tutte le instance legate a quel comune e tutte le fonti legate a quel comune.
	 * 
	 * @param comune Codice belfiore del comune.
	 * @return
	 */
	
	AmKeyValueExt getAmKeyValueExtByKeyFonteComune(String key, String comune, String fonte);
	
	/** Ricerca il parametro(AmKeyValueExt) ottenuti dal nome parametro, dal belfiore e dal codice fonte 
	 * 
	 * @param comune Codice belfiore del comune.
	 * @param fonte Codice dela fonte.
	 * @param key nome parametro.
	 * @return
	 */
	
}
