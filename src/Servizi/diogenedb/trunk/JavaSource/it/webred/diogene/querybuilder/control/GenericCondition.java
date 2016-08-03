package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.TABLE;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.sqldiagram.Column;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.LiteralExpression;
import it.webred.diogene.sqldiagram.LogicalOperator;
import it.webred.diogene.sqldiagram.Operator;
import it.webred.diogene.sqldiagram.RelationalOperator;
import it.webred.diogene.sqldiagram.TableExpression;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.utils.ASetAsAKey;
import it.webred.utils.IdentitySet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 * Questa classe, con classi annidate, descrive una relazione
 * o una condizione (la differenza tra una relazione e una condizione &egrave; che
 * una relazione ha due e solo due entit&agrave; coninvolte, 
 * una condizione pu&ograve; averne da 1 a n). 
 * Una relazione / condizione
 * &egrave; composta da pi&ugrave; criteri di relazione,
 * qui espressi dalla classe annidata 
 * {@link it.webred.diogene.querybuilder.control.GenericCondition.RelationRow}
 * 
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class GenericCondition implements Cloneable, Switches
{
	private String conditionType;
	private Long id;
	private ArrayList<RelationRow> relations = null;
	private ValueExpressionsSource valueExpressionsSource;
	private ObjectEditor<ValueExpression> editingOperand;
	private ArrayList<DcEntityBean> entitiesInvolved;
	private IdentitySet<DcEntityBean> outerJoin;
	private boolean editable = true;
	private Date dtMod;

	/**
	*	
	*/
	private GenericCondition() {super();}
	/**
	*	@param valueExpressionsSource
	*/
	public GenericCondition(ValueExpressionsSource valueExpressionsSource)
	{
		this();
		setValueExpressionsSource(valueExpressionsSource);
	}
	
	
	/**
	 * Resitiuisce un clone di questa classe, 
	 * clonando anche tutti gli oggetti clonabili 
	 * contenuti.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GenericCondition clone() throws CloneNotSupportedException
	{
		GenericCondition clone = (GenericCondition) super.clone();
		if (getEditingOperand() != null)
			clone.setEditingOperand(getEditingOperand().clone());
		if (getEntitiesInvolved() != null)
			clone.setEntitiesInvolved((ArrayList<DcEntityBean>) getEntitiesInvolved().clone());
		if (getRelations() != null)
		{
			clone.setRelations((ArrayList<RelationRow>) getRelations().clone());
			for (int i = 0; i < clone.getRelations().size(); clone.getRelations().set(i, clone.getRelations().get(i++).clone(clone)));
		}
		return clone;
	}
	
	
	// CLASSES /////////////////////////////////////////
	/**
	 * Classe annidata che descrive un criterio di
	 * relazione, ovvero un {@link RelationalOperator}
	 * ed i suoi operandi, e permette di editarli. 
	 * @author Giulio Quaresima
	 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
	 */
	/**
	 * TODO Scrivi descrizione
	 * @author Giulio Quaresima
	 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
	 */
	public class RelationRow implements Cloneable, Switches
	{
		private GenericCondition riferimentoLogicoAllaVeraClasseContenitore;

		private int rowNum;
		private String operatorKey;
		private RelationalOperator operator;
		private ObjectEditor<ValueExpression> operand1;
		private ArrayList<AppendedValue> operand1AppendedValues;
		private boolean operand1Enabled = true;
		private ObjectEditor<ValueExpression> operand2;
		private ArrayList<AppendedValue> operand2AppendedValues;
		private boolean operand2Enabled = false;
		private ObjectEditor<ValueExpression> operand3;
		private ArrayList<AppendedValue> operand3AppendedValues;
		private boolean operand3Enabled = false;
		private boolean editable = GenericCondition.this.isEditable();
		
		public RelationRow()
		{
			super();
			setRiferimentoLogicoAllaVeraClasseContenitore(GenericCondition.this);
		}
		
		
		public class AppendedValue implements Cloneable, Switches
		{
			private ObjectEditor<ValueExpression> value;
			private String chainOperator;
			private boolean editable = true;
			private ArrayList<AppendedValue> reference;
			
			private AppendedValue()	{super();}
			public AppendedValue(ObjectEditor<ValueExpression> value, String chainOperator, boolean editable, ArrayList<AppendedValue> reference)
			{
				this();
				this.value = value;
				this.chainOperator = chainOperator;
				this.editable = editable;
				this.reference = reference;
			}
			
			public void edit(ActionEvent e)
			{
				this.getValue().setEditable(isEditable());
				getValue().edit();
				setEditingOperand(getValue());
			}
			public void deleteMe(ActionEvent e)
			{
				reference.remove(this);
				setEditingOperand(null);
			}
			

			/* (non-Javadoc)
			 * @see java.lang.Object#clone()
			 */
			@Override
			protected AppendedValue clone() throws CloneNotSupportedException
			{
				AppendedValue clone = (AppendedValue) super.clone();
				if (getValue() != null)
					clone.setValue(getValue().clone());
				return clone;
			}
			protected AppendedValue clone(ArrayList<AppendedValue> reference) throws CloneNotSupportedException
			{
				AppendedValue clone = clone();
				clone.setReference(reference);
				return clone;
			}
			
			
			public String getChainOperator()
			{
				return chainOperator;
			}
			public void setChainOperator(String chainOperator)
			{
				this.chainOperator = chainOperator;
			}
			public boolean isEditable()
			{
				return editable;
			}
			public void setEditable(boolean editable)
			{
				this.editable = editable;
			}
			public ObjectEditor<ValueExpression> getValue()
			{
				return value;
			}
			public void setValue(ObjectEditor<ValueExpression> value)
			{
				this.value = value;
			}
			public ArrayList<AppendedValue> getReference()
			{
				return reference;
			}
			public void setReference(ArrayList<AppendedValue> reference)
			{
				this.reference = reference;
			}
			public String getHint(){
				String hint="";
				if(value!=null)
					hint=ValueExpression.retrieveHint(value.getValue());
				else 
					hint=ValueExpression.retrieveHint(null);
				return hint;
			}
			
		}
		
		
		/**
		 * Resitiuisce un clone di questa classe, 
		 * clonando anche tutti gli oggetti clonabili 
		 * contenuti.
		 */
		@SuppressWarnings("unchecked")
		@Override
		public RelationRow clone() 
		throws CloneNotSupportedException
		{
			RelationRow clone = (RelationRow) super.clone();
			if (getOperator() != null)
				clone.setOperator(getOperator().clone());
			if (getOperand1() != null)
				clone.setOperand1(getOperand1().clone());
			if (getOperand2() != null)
				clone.setOperand2(getOperand2().clone());
			if (getOperand3() != null)
				clone.setOperand3(getOperand3().clone());
			clone.setOperand1AppendedValues((ArrayList<AppendedValue>) getOperand1AppendedValues().clone());
			clone.setOperand2AppendedValues((ArrayList<AppendedValue>) getOperand2AppendedValues().clone());
			clone.setOperand3AppendedValues((ArrayList<AppendedValue>) getOperand3AppendedValues().clone());
			for (int i = 0; i < clone.getOperand1AppendedValues().size(); clone.getOperand1AppendedValues().set(i, clone.getOperand1AppendedValues().get(i++).clone(clone.getOperand1AppendedValues())));
			for (int i = 0; i < clone.getOperand2AppendedValues().size(); clone.getOperand2AppendedValues().set(i, clone.getOperand2AppendedValues().get(i++).clone(clone.getOperand2AppendedValues())));
			for (int i = 0; i < clone.getOperand3AppendedValues().size(); clone.getOperand3AppendedValues().set(i, clone.getOperand3AppendedValues().get(i++).clone(clone.getOperand3AppendedValues())));
			return clone;
		}
		/**
		 * <b>ATTENZIONE!</b> Questo metodo &egrave; analogo
		 * al metodo {@link RelationRow#clone()}, ma si &egrave;
		 * resa necessaria la sua introduzione per il particolare
		 * comportamento delle classi annidate quando i loro
		 * riferimenti vengono clonati. 
		 * @param riferimentoLogicoAllaVeraClasseContenitore
		 * Passare il riferimento alla nuova classe esterna
		 * clonata.
		 * @return
		 * @throws CloneNotSupportedException
		 */
		public RelationRow clone(GenericCondition riferimentoLogicoAllaVeraClasseContenitore) 
		throws CloneNotSupportedException
		{
			RelationRow clone = clone();
			if (riferimentoLogicoAllaVeraClasseContenitore != null)
				clone.setRiferimentoLogicoAllaVeraClasseContenitore(riferimentoLogicoAllaVeraClasseContenitore);
			return clone;
		}
		
		public void appendNewValue2Operand1(ValueExpression value)
		{
			getOperand1AppendedValues().add(new AppendedValue(new ObjectEditor<ValueExpression>(value), null, true, getOperand1AppendedValues()));
		}
		public void appendNewValue2Operand1(ValueExpression value, String chainOperator)
		{
			getOperand1AppendedValues().add(new AppendedValue(new ObjectEditor<ValueExpression>(value), chainOperator, true, getOperand1AppendedValues()));
		}
		public void appendNewValue2Operand2(ValueExpression value)
		{
			getOperand2AppendedValues().add(new AppendedValue(new ObjectEditor<ValueExpression>(value), null, true, getOperand2AppendedValues()));
		}
		public void appendNewValue2Operand2(ValueExpression value, String chainOperator)
		{
			getOperand2AppendedValues().add(new AppendedValue(new ObjectEditor<ValueExpression>(value), chainOperator, true, getOperand2AppendedValues()));
		}
		public void appendNewValue2Operand3(ValueExpression value)
		{
			getOperand3AppendedValues().add(new AppendedValue(new ObjectEditor<ValueExpression>(value), null, true, getOperand3AppendedValues()));
		}
		public void appendNewValue2Operand3(ValueExpression value, String chainOperator)
		{
			getOperand3AppendedValues().add(new AppendedValue(new ObjectEditor<ValueExpression>(value), chainOperator, true, getOperand3AppendedValues()));
		}
		
		// LISTENERS /////////////////////////////////////
		public void appendNewValue2Operand1(ActionEvent e)
		{
			appendNewValue2Operand1(new LiteralExpression());
		}
		public void appendNewValue2Operand2(ActionEvent e)
		{
			appendNewValue2Operand2(new LiteralExpression());
		}
		public void appendNewValue2Operand3(ActionEvent e)
		{
			appendNewValue2Operand3(new LiteralExpression());
		}
		/**
		 * Metodo chiamato quando cambia l'operatore
		 * di relazione selezionato: oltre ad assegnare
		 * il nuovo valore all'operatore, vengono resi visibili
		 * soltanto il numero di operandi di cui questo
		 * operatore ha bisogno (a seconda, cio&egrave;, se
		 * l'operatore sia unario, binario o ternario).
		 * @param e
		 */
		public void chooseOperator(ValueChangeEvent e)
		{
			if (e != null && e.getNewValue() != null)
			{
				String key = e.getNewValue().toString();
				RelationalOperator operator = null;
				if (null != (operator = getValueExpressionsSource().getOF().getOperator(key)))
				{
					setOperand1Enabled(operator.getNumOfOperands() > 0);
					setOperand2Enabled(operator.getNumOfOperands() > 1);
					setOperand3Enabled(operator.getNumOfOperands() > 2);
					setOperator(operator);
				}				
			}
		}
		/**
		 * Metodo chiamato quando si preme il pulsante di
		 * editazione del primo operando: 
		 * @see {@link GenericCondition#setEditingOperand(ObjectEditor)}
		 * @param e
		 */
		public void editOperand1(ActionEvent e)
		{
			getRiferimentoLogicoAllaVeraClasseContenitore().setEditingOperand(getOperand1());
			getOperand1().setEditable(isEditable());
			getOperand1().edit();
		}
		/**
		 * Metodo chiamato quando si preme il pulsante di
		 * editazione del secondo operando: 
		 * @see {@link GenericCondition#setEditingOperand(ObjectEditor)}
		 * @param e
		 */
		public void editOperand2(ActionEvent e)
		{
			getRiferimentoLogicoAllaVeraClasseContenitore().setEditingOperand(getOperand2());
			getOperand2().setEditable(isEditable());
			getOperand2().edit();
		}
		/**
		 * Metodo chiamato quando si preme il pulsante di
		 * editazione del terzo operando: 
		 * @see {@link GenericCondition#setEditingOperand(ObjectEditor)}
		 * @param e
		 */
		public void editOperand3(ActionEvent e)
		{
			getRiferimentoLogicoAllaVeraClasseContenitore().setEditingOperand(getOperand3());
			getOperand3().setEditable(isEditable());
			getOperand3().edit();
		}
		/**
		 * Metodo chiamato per rimuovere questo criterio di
		 * relazione.
		 * @param e
		 */
		public void removeMe(ActionEvent e)
		{
			getRiferimentoLogicoAllaVeraClasseContenitore().
			getRelations().remove(getRowNum());
			getRiferimentoLogicoAllaVeraClasseContenitore().
			arrangeRowNums();
			getRiferimentoLogicoAllaVeraClasseContenitore().
			setEditingOperand(null);
		}
		
		
		// GETTERS AND SETTERS
		/**
		 * @return
		 */
		public int getRowNum()
		{
			return rowNum; 
		}
		/**
		 * @param rowNum
		 */
		public void setRowNum(int rowNum)
		{
			this.rowNum = rowNum;
		}
		/**
		 * @return
		 */
		public ObjectEditor<ValueExpression> getOperand1()
		{
			if (operand1 == null)
				setOperand1(new ObjectEditor<ValueExpression>(new Column()));
			return operand1;
		}
		/**
		 * @return
		 */
		public String getOperand1Hint(){
			String hint="";
			
			if(operand1!=null)
				hint=ValueExpression.retrieveHint(operand1.getValue());
			else 
				hint=ValueExpression.retrieveHint(null);
			return hint;
		}
		/**
		 * @return
		 */
		public String getOperand2Hint(){
			String hint="";
			if(operand2!=null)
				hint=ValueExpression.retrieveHint(operand2.getValue());
			else 
				hint=ValueExpression.retrieveHint(null);
			return hint;
		}
		/**
		 * @return
		 */
		public String getOperand3Hint(){
			String hint="";
			if(operand3!=null)
				hint=ValueExpression.retrieveHint(operand3.getValue());
			else 
				hint=ValueExpression.retrieveHint(null);
			return hint;
		}

		/**
		 * @param operand1
		 */
		public void setOperand1(ObjectEditor<ValueExpression> operand1)
		{
			this.operand1 = operand1;
		}

		/**
		 * @return
		 */
		public boolean isOperand1Enabled()
		{
			return operand1Enabled;
		}

		/**
		 * @param operand1Enabled
		 */
		public void setOperand1Enabled(boolean operand1Enabled)
		{
			this.operand1Enabled = operand1Enabled;
		}

		/**
		 * @return
		 */
		public ObjectEditor<ValueExpression> getOperand2()
		{
			if (operand2 == null)
				setOperand2(new ObjectEditor<ValueExpression>(new Column()));
			return operand2;
		}

		/**
		 * @param operand2
		 */
		public void setOperand2(ObjectEditor<ValueExpression> operand2)
		{
			this.operand2 = operand2;
		}

		/**
		 * @return
		 */
		public boolean isOperand2Enabled()
		{
			return operand2Enabled;
		}

		/**
		 * @param operand2Enabled
		 */
		public void setOperand2Enabled(boolean operand2Enabled)
		{
			this.operand2Enabled = operand2Enabled;
		}

		/**
		 * @return
		 */
		public ObjectEditor<ValueExpression> getOperand3()
		{
			if (operand3 == null)
				setOperand3(new ObjectEditor<ValueExpression>(new Column()));
			return operand3;
		}

		/**
		 * @param operand3
		 */
		public void setOperand3(ObjectEditor<ValueExpression> operand3)
		{
			this.operand3 = operand3;
		}

		/**
		 * @return
		 */
		public boolean isOperand3Enabled()
		{
			return operand3Enabled;
		}

		/**
		 * @param operand3Enabled
		 */
		public void setOperand3Enabled(boolean operand3Enabled)
		{
			this.operand3Enabled = operand3Enabled;
		}

		/**
		 * @return
		 */
		public RelationalOperator getOperator()
		{
			return operator;
		}

		/**
		 * @param operator
		 */
		public void setOperator(RelationalOperator operator)
		{
			this.operator = operator;
		}
		/**
		 * @return
		 */
		public String getOperatorKey()
		{
			return operatorKey;
		}
		/**
		 * @param operatorKey
		 */
		public void setOperatorKey(String operatorKey)
		{
			this.operatorKey = operatorKey;
		}


		public GenericCondition getRiferimentoLogicoAllaVeraClasseContenitore()
		{
			return riferimentoLogicoAllaVeraClasseContenitore;
		}


		public void setRiferimentoLogicoAllaVeraClasseContenitore(GenericCondition riferimentoLogicoAllaVeraClasseContenitore)
		{
			this.riferimentoLogicoAllaVeraClasseContenitore = riferimentoLogicoAllaVeraClasseContenitore;
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
		public ArrayList<AppendedValue> getOperand1AppendedValues()
		{
			if (operand1AppendedValues == null)
				operand1AppendedValues = new ArrayList<AppendedValue>();
			return operand1AppendedValues;
		}
		public void setOperand1AppendedValues(ArrayList<AppendedValue> operand1AppendedValues)
		{
			this.operand1AppendedValues = operand1AppendedValues;
		}
		public ArrayList<AppendedValue> getOperand2AppendedValues()
		{
			if (operand2AppendedValues == null)
				operand2AppendedValues = new ArrayList<AppendedValue>();
			return operand2AppendedValues;
		}
		public void setOperand2AppendedValues(ArrayList<AppendedValue> operand2AppendedValues)
		{
			this.operand2AppendedValues = operand2AppendedValues;
		}
		public ArrayList<AppendedValue> getOperand3AppendedValues()
		{
			if (operand3AppendedValues == null)
				operand3AppendedValues = new ArrayList<AppendedValue>();
			return operand3AppendedValues;
		}
		public void setOperand3AppendedValues(ArrayList<AppendedValue> operand3AppendedValues)
		{
			this.operand3AppendedValues = operand3AppendedValues;
		}
		
		/**
		 * Metodo per attivare/disattivare l'outerjoin della relazione impostando 
		 * il valore isOuter della TableExpression dell'operando appartenente alla
		 * entità latter della Relazione.
		 * 
		 * 
		 * tab.setOuter(!tab.isOuter())
		 * 
		 */
		public void toggleOuterJoin(Long id)
		{
			ValueExpression ve= getOperand1().getValue();
			if(ve instanceof Column){
				Column col =(Column)ve;
				TableExpression tab= (TableExpression)col.getTable();
				if(tab.getId().equals(id))tab.setOuter(!tab.isOuter());
			}
			ve= getOperand2().getValue();
			if(ve instanceof Column ){
				Column col =(Column)ve;
				TableExpression tab= (TableExpression)col.getTable();
				if(tab.getId().equals(id))tab.setOuter(!tab.isOuter());
			}
			
		}
	}

	
	
	/**
	 * 
	 */
	public void addRelation()
	{
		getRelations().add(new RelationRow());
		arrangeRowNums();
	}
	/**
	 * 
	 */
	private void arrangeRowNums()
	{
		for (int i = 0; i < getRelations().size(); getRelations().get(i).setRowNum(i++));
	}
	
	
	/**
	 * @return
	 */
	public Condition getCondition() 
	{
		Condition result = null;
		for (RelationRow item : getRelations())
		{
			if (result == null)
				result = getValueExpressionsSource().getCF().getCondition();
			RelationalOperator operator = item.getOperator();
			if (operator != null)
			{
				if (operator.getOperands().length > 0)
					operator.getOperands()[0] = item.getOperand1().getValue(); 
				if (operator.getOperands().length > 1)
					operator.getOperands()[1] = item.getOperand2().getValue(); 
				if (operator.getOperands().length > 2)
					operator.getOperands()[2] = item.getOperand3().getValue();
				result.addSimpleCondition(operator, getValueExpressionsSource().getOF().getLogicalOperator());
			}
		}
		return result;
	}
	public static GenericCondition initializeFromCondition(Condition condition, ValueExpressionsSource valueExpressionSource)
	{
		if (condition != null && valueExpressionSource != null)
		{
			GenericCondition result = new GenericCondition(valueExpressionSource);
			List<Operator> relations = condition.getRelationsList();
			//N.B. la lista è alternata LogicalOperator RelationaleOperator
			for (int i = 0; i < relations.size();)
			{
				@SuppressWarnings("unused") LogicalOperator logicalOp = null;
				RelationalOperator relatioOp = null;
				try
				{
					logicalOp = (LogicalOperator) relations.get(i++);
					relatioOp = (RelationalOperator) relations.get(i++);
				}
				catch (ClassCastException cce)
				{
					// TODO
					throw new RuntimeException("Evidentemente, c'è qualcosa che non va nella condizione passata", cce);
				}
				catch (IndexOutOfBoundsException iooe) 
				{
					// TODO
					throw new RuntimeException("Evidentemente, c'è qualcosa che non va nella condizione passata", iooe);
				}
				
				RelationRow relation = result.new RelationRow();
				relation.setOperator(relatioOp);
				relation.setOperatorKey(relatioOp.getName());
				if (relatioOp.getOperands().length > 0)
				{
					relation.setOperand1(new ObjectEditor<ValueExpression>(relatioOp.getOperands()[0]));
					relation.getOperand1().save();
					relation.setOperand1Enabled(true);
				}
				if (relatioOp.getOperands().length > 1)
				{
					relation.setOperand2(new ObjectEditor<ValueExpression>(relatioOp.getOperands()[1])); 
					relation.getOperand2().save();
					relation.setOperand2Enabled(true);
				}
				if (relatioOp.getOperands().length > 2)
				{
					relation.setOperand3(new ObjectEditor<ValueExpression>(relatioOp.getOperands()[2])); 
					relation.getOperand3().save();
					relation.setOperand3Enabled(true);
				}
				
				result.getRelations().add(relation);
			}
			return result;
		}
		return null;
	}
	/**
	 * @return
	 */
	public String getFormerEntityName()
	{
		if (getEntitiesInvolved() != null && getEntitiesInvolved().size() == 2)
			return getEntitiesInvolved().get(0).getName();
		return null;
	}
	/**
	 * @return
	 */
	public String getLatterEntityName()
	{
		if (getEntitiesInvolved() != null && getEntitiesInvolved().size() == 2)
			return getEntitiesInvolved().get(1).getName();
		return null;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getKey().toString();
	}
	/**
	 * @return
	 */
	public ASetAsAKey getKey()
	{
		ASetAsAKey<String> key = new ASetAsAKey<String>();
		if (getEntitiesInvolved() != null)
			for (DcEntityBean item : getEntitiesInvolved())
				key.add(TABLE + item.getId() + ALIAS + item.getAliasNum());
		return key;
	}
	public ASetAsAKey getOrderedKeyId()
	{
		ASetAsAKey<OrderedEntities> key = new ASetAsAKey<OrderedEntities>();
		int count=1;
		Long id=getId()!=null?getId():new Long(0);
		if (getEntitiesInvolved() != null)
			for (DcEntityBean item : getEntitiesInvolved())
			{
				key.add(new OrderedEntities( TABLE + item.getId() + ALIAS + id,count));
				count++;
			}
		return key;
	}
	
	public ASetAsAKey getOrderedKey()
	{
		ASetAsAKey<OrderedEntities> key = new ASetAsAKey<OrderedEntities>();
		int count=1;
		if (getEntitiesInvolved() != null)
			for (DcEntityBean item : getEntitiesInvolved())
			{
				key.add(new OrderedEntities( TABLE + item.getId() + ALIAS + item.getAliasNum(),count));
				count++;
			}
		return key;
	}


	// GETTERS AND SETTERS
	/**
	 * @see {@link ConditionsController#getValueExpressionEditor()}
	 * @return
	 * L'espressione correntemente in editaizone
	 */
	public ObjectEditor<ValueExpression> getEditingOperand()
	{
		return editingOperand;
	}

	/**
	 * @see {@link GenericCondition#getEditingOperand()}
	 * @param editingOperand
	 */
	public void setEditingOperand(ObjectEditor<ValueExpression> editingOperand)
	{
		this.editingOperand = editingOperand;
	}

	/**
	 * @return
	 */
	public ArrayList<RelationRow> getRelations()
	{
		if (relations == null)
			relations = new ArrayList<RelationRow>();
		return relations;
	}
	/**
	 * @param relations
	 */
	public void setRelations(ArrayList<RelationRow> relations)
	{
		this.relations = relations;
	}
	/**
	 * @return
	 */
	public ValueExpressionsSource getValueExpressionsSource()
	{
		return valueExpressionsSource;
	}

	/**
	 * @param valueExpressionsSource
	 */
	public void setValueExpressionsSource(ValueExpressionsSource valueExpressionsSource)
	{
		this.valueExpressionsSource = valueExpressionsSource;
	}


	/**
	 * Le entit&agrave; coinvolte in questa relazione / condizione.
	 * La differenza tra una relazione e una condizione &egrave; che
	 * una relazione ha due e solo due entit&agrave; coninvolte, 
	 * una condizione pu&ograve; averne da 1 a n. 
	 * @return
	 */
	public ArrayList<DcEntityBean> getEntitiesInvolved()
	{
		if (entitiesInvolved == null)
			entitiesInvolved = new ArrayList<DcEntityBean>();
		return entitiesInvolved;
	}


	/**
	 * @param entitiesInvolved
	 */
	public void setEntitiesInvolved(ArrayList<DcEntityBean> entitiesInvolved)
	{
		this.entitiesInvolved = entitiesInvolved;
	}
	public String getName()
	{
		return conditionType;
	}
	public void setConditionType(String conditionType)
	{
		this.conditionType = conditionType;
	}
	/**
	 * @return
	 * L'insieme degli indici di 
	 * {@link GenericCondition#getEntitiesInvolved()}
	 * corrispondenti a tabelle in outer join
	 */
	public IdentitySet<DcEntityBean> getOuterJoin()
	{
		if (outerJoin == null)
			outerJoin = new IdentitySet<DcEntityBean>();
		return outerJoin;
	}
	public void setOuterJoin(IdentitySet<DcEntityBean> outerJoin)
	{
		this.outerJoin = outerJoin;
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
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	/** 
	 * @return Se le entità di questa relazione sono in OuterJoin
	 * 
	 */
	public boolean isInOuterJoin()
	{
		boolean isOuter=false;
		for (DcEntityBean ent : getEntitiesInvolved())
		{
			if(getOuterJoin().contains(ent))
			{
				isOuter= true;
				break;
			}
		}
		return isOuter;
	}
	/**
	 *  Metodo per scambiare le entità che sono in left outer Join
	 * @throws Exception 
	 */
	public void swapOuterJoinEntities() throws Exception{
		if (getEntitiesInvolved() == null || getEntitiesInvolved().size() != 2 )
		{	
			throw new Exception("Errore : è stato chiamato il metodo swapOuterJoinEntities in un constesto non Valido!");
		}
		//l'entità nella posizione 0 è la former entity
		boolean isOuter=isInOuterJoin();
		if (isOuter)toggleOuterJoin();
		DcEntityBean tmp=  getEntitiesInvolved().get(0);
		getEntitiesInvolved().set(0,getEntitiesInvolved().get(1));
		getEntitiesInvolved().set(1,tmp);
		if (isOuter)toggleOuterJoin();
	}
	/**
	 *  Metodo per scambiare le entità che sono in left outer Join
	 */
	public void toggleOuterJoin(){
	
		if(isInOuterJoin())
		{
			getOuterJoin().remove(getEntitiesInvolved().get(1));
			for (RelationRow rel : getRelations())
			{
				rel.toggleOuterJoin(getEntitiesInvolved().get(1).getId());
			}
		}else{
			getOuterJoin().add(getEntitiesInvolved().get(1));
			for (RelationRow rel : getRelations())
			{
				rel.toggleOuterJoin(getEntitiesInvolved().get(1).getId());
			}
		}
	}
	public Date getDtMod()
	{
		return dtMod;
	}
	public void setDtMod(Date dtMod)
	{
		this.dtMod = dtMod;
	}
}
