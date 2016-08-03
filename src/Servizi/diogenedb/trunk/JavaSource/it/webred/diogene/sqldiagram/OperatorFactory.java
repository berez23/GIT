/**
 * 
 */
package it.webred.diogene.sqldiagram;

import java.util.List;
import java.util.Locale;

/**
 * La factory degli operatori.
 * Questa interfaccia deve essee implementata da 
 * implementazioni specifiche dei DBMS, implementazioni 
 * che dovranno essere restituite da 
 * {@link it.webred.diogene.sqldiagram.SqlGenerator#getOperatorFactory()}
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public interface OperatorFactory 
{
	/**
	 * @return Tutti gli operatori relazionali.
	 */
	public List<RelationalOperator> getRelationalOperators();
	/**
	 * @return Tutti gli operatori logici.
	 */
	public List<LogicalOperator> getLogicalOperators();
	/**
	 * @return Tutti gli operatori di espressione
	 */
	public List<ValueExpressionOperator> getValueExpressionOperators();
		
	/**
	 * @param name
	 * @see RelationalOperator#getName()
	 * @return Un operatore relazionale, dato il nome univoco
	 */
	public RelationalOperator getOperator(String name);
	/**
	 * @param name
	 * @see LogicalOperator#getName()
	 * @return Un operatore logico, dato il nome univoco
	 */
	public LogicalOperator getLogicalOperator(String name);
	/**
	 * @return L'operatore logico di default
	 */
	public LogicalOperator getLogicalOperator();
	/**
	 * @param name
	 * @see ValueExpressionOperator#getName()
	 * @return Un operatore di espressione, dato il nome univoco
	 */
	public ValueExpressionOperator getValueExpressionOperator(String name);
	
	/**
	 * @param key
	 * L'operatore di cui si desidera la definizione
	 * @return
	 * Come:<br />
	 * <code>
	 * getDefinition(key, Locale.getDefault());
	 * </code>
	 * @see OperatorFactory#getDefinition(Operator, Locale)
	 */
	public String getDefinition(Operator key);
	/**
	 * @param key
	 * L'operatore di cui si desidera la definizione
	 * @param locale
	 * @return
	 * Un testo che definisca l'operatore passato,
	 * secondo il locale specificato, o <code>null</code>
	 * qualora non esista la definizione.
	 */
	public String getDefinition(Operator key, Locale locale);
	/**
	 * @param key
	 * L'operatore di cui si desidera il testo di aiuto
	 * @return
	 * Come<br />
	 * <code>
	 * getHelp(key, Locale.getDefault());
	 * </code>
	 * @see OperatorFactory#getHelp(Operator, Locale)
	 */
	public String getHelp(Operator key);
	/**
	 * @param key
	 * L'operatore di cui si desidera il testo di help
	 * @param locale
	 * @return
	 * Un testo di aiuto, ovvero una descrizione dettagliata
	 * del significato e della funzione dell'operatore,
	 * secondo il locale specificato
	 */
	public String getHelp(Operator key, Locale locale);
}
