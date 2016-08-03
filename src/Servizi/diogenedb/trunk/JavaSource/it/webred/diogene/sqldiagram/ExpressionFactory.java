/**
 * 
 */
package it.webred.diogene.sqldiagram;

import java.util.List;
import java.util.Locale;

import static it.webred.diogene.sqldiagram.EnumsRepository.*;

/**
 * Per ogni tipo di DBMS occorrer&agrave; implementare
 * una factory da questa interfaccia. 
 * @see it.webred.diogene.sqldiagram.SqlGenerator#getExpressionFactory()
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public interface ExpressionFactory 
{
	/**
	 * Restituisce un espressione SQL, dato il nome
	 * @param expression
	 * @return
	 */
	public ValueExpression getExpression(String expression);
	/**
	 * Restituisce la lista di tutte le espressioni esistenti
	 * @return
	 */
	public List<ValueExpression> getExpressions();
	/**
	 * @return il predicato di default
	 */
	public Predicate getPredicate();
	/**
	 * @param expression
	 * Nome del predicato
	 * @return il predicato corrispondente
	 */
	public Predicate getPredicate(String expression);
	/**
	 * @return la lista di tutti i predicati
	 */
	public List<Predicate> getPredicates();
	/**
	 * Valida una stringa per essere inserita come input
	 * di un valore letterale del tipo specificato
	 * @param input
	 * @param type
	 * @return
	 */
	public boolean validate(String input, ValueType type);
	/**
	 * Quota una stringa in modo che non abbia caratteri pericolosi
	 * @param input
	 * @return
	 */
	public String quoteString(String input);
	
	/**
	 * Uguale a:<br />
	 * <div>
	 * getDefinition(key, Locale.getDefault());
	 * </div>
	 * @param key
	 * @return
	 * @see ExpressionFactory#getDefinition(ValueExpression, Locale)
	 */
	public String getDefinition(Expression key);
	
	/**
	 * Restituisce un testo di definizione per
	 * l'espressione specificata, o null se
	 * la definizione per l'espressione non esiste, secondo
	 * il locale passato
	 * @param key
	 * @return
	 */
	public String getDefinition(Expression key, Locale locale);
	
	/**
	 * Uguale a:<br />
	 * <div>
	 * getDefinition(key, Locale.getDefault());
	 * </div>
	 * @param key
	 * @return
	 * @see ExpressionFactory#getDefinition(ValueType, Locale)
	 */
	public String getDefinition(ValueType key);
	
	/**
	 * Restituisce un testo di definizione per
	 * il valuetype specificato, o null se
	 * la definizione per il valuetype non esiste, secondo
	 * il locale passato
	 * @param key
	 * @return
	 */
	public String getDefinition(ValueType key, Locale locale);
	
	/**
	 * 
	 * Uguale a:<br />
	 * <div>
	 * getHelp(key, Locale.getDefault());
	 * </div>
	 * @param key
	 * @return
	 */
	public String getHelp(Expression key);
	
	/**
	 * Restituisce un testo di aiuto, ovvero una descrizione
	 * pi&ugrave; estesa, per l'espressione passata, secondo
	 * il locale specificato. Ci&ograve;
	 * pu&ograve; essere utile soprattutto per le 
	 * {@link Function Function}, che necessitano di un help 
	 * per il loro utilizzo da parte dell'utente.
	 * @param key
	 * @param locale
	 * @return
	 */
	public String getHelp(Expression key, Locale locale);
}
