/**
 * 
 */
package it.webred.diogene.sqldiagram;

import java.io.IOException;
import org.jdom.JDOMException;

/**
 * Per ogni tipo di DBMS occorrer&agrave; implementare
 * una factory da questa interfaccia. 
 * @see it.webred.diogene.sqldiagram.SqlGenerator#getConditionFactory()
 * @author Giulio Quaresima
 * @author Marco Riccardini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public interface ConditionFactory 
{
	/**
	 * Restituisce una condizione vuota.
	 */
	public Condition getCondition();
	/**
	 * Valida un XML da utilizzare per la costruzione di una condizione
	 * @param xml
	 * l'xml da validare
	 * @return
	 * true se il file xml è valido
	 */
	public boolean validateConditionXML(String xml); 
}
