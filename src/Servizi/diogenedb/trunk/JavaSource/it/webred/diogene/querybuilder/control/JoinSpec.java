package it.webred.diogene.querybuilder.control;

import java.io.Serializable;

import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.sqldiagram.Condition;

public class JoinSpec implements Switches , Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7779736735336696833L;
	private Long id;
	private Condition joinCondition;
	private DcEntityBean formerTable;
	private DcEntityBean latterTable;
	private boolean outerJoin;
	private boolean editable;
	
	public JoinSpec() {super();}
	public JoinSpec(Long id, Condition joinCondition, DcEntityBean formerTable, DcEntityBean latterTable, boolean outerJoin, boolean editable)
	throws IllegalArgumentException, NullPointerException
	{
		this();
		setId(id);
		setJoinCondition(joinCondition);
		setFormerTable(formerTable);
		setLatterTable(latterTable);
		setOuterJoin(outerJoin);
		setEditable(editable);
	}
	public JoinSpec(GenericCondition condition)
	throws IllegalArgumentException, NullPointerException
	{
		this(condition.getId(), condition.getCondition(), condition.getEntitiesInvolved().get(0), condition.getEntitiesInvolved().get(1), condition.getOuterJoin().contains(condition.getEntitiesInvolved().get(1)), condition.isEditable());
		if (condition.getEntitiesInvolved().size() > 2)
			throw new IllegalArgumentException("Una join \u00E8 una relaizione tra due tabelle, ma la condizione passata risulta coinvolgere " + condition.getEntitiesInvolved().size() + " tabelle");
	}
	
	public boolean isOuterJoin()
	{
		return outerJoin;
	}
	public void setOuterJoin(boolean outerJoin)
	{
		this.outerJoin = outerJoin;
	}
	public Condition getJoinCondition()
	{
		return joinCondition;
	}
	public void setJoinCondition(Condition joinCondition)
	{
		this.joinCondition = joinCondition;
	}
	public DcEntityBean getFormerTable()
	{
		return formerTable;
	}
	private void setFormerTable(DcEntityBean formerTable)
	{
		this.formerTable = formerTable;
	}
	public DcEntityBean getLatterTable()
	{
		return latterTable;
	}
	private void setLatterTable(DcEntityBean latterTable)
	{
		this.latterTable = latterTable;
	}
	/**
	 * Utile per le modifiche
	 * @return
	 * L'id nel DB, o <code>null</code> se si tratta di
	 * una relazione nuova.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Utile per le modifiche.
	 * @param id
	 * L'id nel DB.
	 */
	public void setId(Long id)
	{
		this.id = id;
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
