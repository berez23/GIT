package it.webred.diogene.querybuilder.control;

import it.webred.utils.ASetAsAKey;

import java.util.HashMap;
import java.util.Map;

import javax.faces.model.DataModel;

/**
 * Contiene una lista di relazioni gi&agrave; create,
 * mappate con una chiave che dipende dalle entit&agrave; coinvolte
 * nelle relazioni.
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class RelationsBetweenEntities implements Switches
{
	Map<ASetAsAKey,ObjectEditor<GenericCondition>> relations;
	ObjectEditor<GenericCondition> filterCondition;
	boolean editable;
	
	public RelationsBetweenEntities() {super();}
	
	/**
	 * Salva (o sostituisce) la relazione associata alla
	 * chiave passata.
	 * @param key
	 * La chiave dipende dalle entit&agrave; coinvolte
	 * nella relazione.
	 * @param condition
	 * La relazione da salvare.
	 */
	public void saveRelation(ASetAsAKey key, ObjectEditor<GenericCondition> condition)
	{
		getRelations().put(key, condition);
	}

	/**
	 * @return La mappa delle relazioni. Non restituisce mai
	 * <code>null</code>. 
	 */
	public Map<ASetAsAKey,ObjectEditor<GenericCondition>> getRelations()
	{
		if (relations == null)
			relations = new HashMap<ASetAsAKey,ObjectEditor<GenericCondition>>();
		return relations;
	}

	/**
	 * Imposta la mappa delle relazioni. Se si passa
	 * <code>null</code>, si crea una nuova mappa vuota.
	 * @param relations
	 */
	public void setRelations(Map<ASetAsAKey,ObjectEditor<GenericCondition>> relations)
	{
		this.relations = relations;
	}
	
	/**
	 * @return Il <i>wrapper</i> che consente di gestire
	 * la lista delle relazioni mediante un <code>dataTable</code>
	 * di JSF.
	 * @see DataModelMapWrapper
	 */
	public DataModel getRelationsDataModel()
	{
		return new DataModelMapWrapper<ObjectEditor<GenericCondition>>(getRelations());
	}

	public ObjectEditor<GenericCondition> getFilterCondition()
	{
		return filterCondition;
	}

	public void setFilterCondition(ObjectEditor<GenericCondition> filterCondition)
	{
		this.filterCondition = filterCondition;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.Switches#isEditable()
	 */
	public boolean isEditable()
	{
		return editable;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.Switches#setEditable(boolean)
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
}
