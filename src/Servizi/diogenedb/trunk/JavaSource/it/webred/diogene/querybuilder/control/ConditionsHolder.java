package it.webred.diogene.querybuilder.control;

/**
 * Questa interfaccia deve essere implementata
 * da qualsiasi <i>backing bean</i> voglia
 * utilizzare {@link it.webred.diogene.querybuilder.control.ConditionsController},
 * ovvero la <i>popup</i> di creazione delle relazioni / condizioni,
 * per creare relazioni / condizioni.
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public interface ConditionsHolder
{
	/**
	 * Salva la condizione passata come parametro.
	 * @param condition
	 */
	public void saveCondition(GenericCondition condition);
	/**
	 * Annulla le modifiche fatte alla condizione
	 * passata come parametro.
	 * @param condition
	 */
	public void undoConditionUpdate(GenericCondition condition);
}
