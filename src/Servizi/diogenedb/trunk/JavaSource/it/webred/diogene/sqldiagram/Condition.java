/**
 * 
 */
package it.webred.diogene.sqldiagram;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.Constants.OUTER;
import static it.webred.diogene.querybuilder.Constants.TABLE;
import static it.webred.utils.StringUtils.CRLF;

import it.webred.diogene.querybuilder.control.ConditionsController;
import it.webred.diogene.sqldiagram.ValueExpression.AppendedValueExpression;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Rappresenta una condizione. Una condizione rappresenta genericamente
 * un'entità SQL che può avere valore vero o falso.<br />
 * Ogni condizione può contenere una lista di condizioni semplici
 * o altre condizioni, concatenate mediante operatori logici
 * rappresentati dalla classe {@link it.webred.diogene.sqldiagram.LogicalOperator LogicalOperator}.<br />
 * Le condizioni semplici sono operatori di relazione, rappresntati
 * dalla classe RelationalOperator, mentre le condizioni rappresentano
 * questo oggetto. Per cui una condizione pu&ograve; rappresentare
 * un albero di condizioni, che trova la sua rappresentazione
 * in SQL mediante l'uso delle parentesi.
 * Una condizione che rispecchi il seguente SQL: <br />
 * <tt>
 * COGNOME = 'Rossi' AND <br />
 * NOME LIKE 'A%' AND <br /> 
 * ( <br /> 
 *   DATA_NASCITA > SYSDATE OR DATA_MORTE IS NOT NULL <br /> 
 * ) <br /> 
 * </tt>
 * conterr&agrave; tre condizioni concatenate da AND 
 * due condizioni semplici, una con l'operatore equal
 * e l'altra con l'operatore like, e una sottocondizione, 
 * quella tra parentesi, contenente a sua volta due condizioni semplici, 
 * concatenate dall'operatore logico OR, la prima con l'operatore
 * greater, la seconda con l'operatore isNotNull
 * @author Giulio Quaresima 
 * @author Marco Riccardini 
 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
 */
public class Condition extends it.webred.diogene.sqldiagram.Expression implements XMLRepresentable
{
	protected List<Tuple> conditions = null;
	protected RelationalOperator expressionOP = null;

	// ORDINAMENTO DEI GRUPPI: --> --> --> --> --> --> -->  [1                  (2   )][3                  (4   )][5            ] [6                   (7   )]
	final Pattern separeTableAndAlias = Pattern.compile("\\A(\\Q" + TABLE + "\\E(\\d+))(\\Q" + ALIAS + "\\E(\\d+))(" + OUTER + ")?(\\Q" + COLUMN + "\\E(\\d+))?\\z");
	
	/**
	*	Crea una condizione vuota
	*/
	public Condition() {}
	/**
	 * Crea una condizione {@link Condition#isSimpleCondition() semplice}  
	 * basata sulla relazione passata
	 * come parametro. 
	 * <br /><b>ATTENZIONE:</b> QUESTO COSTRUTTORE E' VOLUTAMENTE
	 * PRIVATO, E DEVE RESTARE TALE, per un utilizzo esclusivamente
	 * privato da parte di {@link Condition#addSimpleCondition(RelationalOperator, LogicalOperator) addSimpleCondition}
	 * e {@link Condition#clone() clone}
	 *	@param expressionOP
	 */
	private Condition(RelationalOperator expressionOP)
	{
		this.expressionOP = expressionOP;
	}

	/**
	 * Aggiunge una sottocondizione cond, concatenandola alle precedenti
	 * sottocondizioni
	 * mediante l'operatore logico logicOp
	 * @param cond
	 * @param logicOp
	 * @return
	 */
	public Condition addCondition(Condition cond, LogicalOperator logicOp)
	{
		if (conditions == null)
			conditions = new ArrayList<Tuple>();
		conditions.add(new Tuple(cond, logicOp));
		return cond;
	}
	/**
	 * Accoda a questa Condition un'altra condition
	 * A differenza di {@link Condition#addCondition(Condition, LogicalOperator) addCondition},
	 * non aggiunge una sottocondizione (non crea un albero),
	 * ma fonde questa condizione e quella passata in un unica condizione
	 * @param cond
	 * @param logicOp
	 * @return
	 */
	public Condition mergeCondition(Condition cond)
	{
		if (cond != null && !cond.isEmpty())
		{
			if (conditions == null)
				conditions = new ArrayList<Tuple>();
			conditions.addAll(cond.conditions);			
		}
		return this;
	}
	
	/**
	 * Aggiunge una condizione semplice, ovvero non annidata, 
	 * rappresentata dall'operatore 
	 * di relazione op e dai suoi operandi, e la concatena alle precedenti
	 * mediante l'operatore logico logicOp
	 * @param op
	 * @param logicOp
	 */
	public void addSimpleCondition(RelationalOperator op, LogicalOperator logicOp)
	{
		if (conditions == null)
			conditions = new ArrayList<Tuple>();
		conditions.add(new Tuple(new Condition(op), logicOp));
	}
	
	/**
	 * @return
	 * Una lista alternata di
	 * {@link LogicalOperator} e di
	 * {@link RelationalOperator}.
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 * Se questa condizione contiene condizioni
	 * annidate
	 */
	public List<Operator> getRelationsList()
	throws NullPointerException, IllegalStateException
	{
		ArrayList<Operator> result = new ArrayList<Operator>();
		if (conditions != null)
			for (Tuple item : conditions)
			{
				if (!item.getCond().isSimpleCondition())
					throw new IllegalStateException("Questa condizione contiene anche condizioni annidate, non semplici");
				result.add(item.getOp());
				result.add(item.getCond().expressionOP);
			}
		return result;
	}
	
	/**
	 * @return
	 * <code>true</code> se questa &egrave; una condizione semplice,
	 * <code>false</code> altrimenti. Le condizioni semplici
	 * non possono essere istanziate dall'esterno, ma soltanto
	 * tramite il costruttore privato {@link Condition#Condition(RelationalOperator)}
	 */
	public boolean isSimpleCondition()
	{
		return expressionOP != null;
	}
	
	/**
	 * Restituisce la rappresentazione SQL di questa condizione
	 */
	@Override
	public String toString()
	{
		String result = "";
		//String CRLF = System.getProperty("line.separator");
		if (conditions != null)
		{
			boolean firstCycle = true;
			for (Tuple tuple : conditions)
			{
				if (!firstCycle)
					result += " " + tuple.getOp().toString();
				else
				{
					result += " " + tuple.getOp().showWhenFirst();
					firstCycle = false;
				}
				result += CRLF;
				
				if (tuple.getCond().isSimpleCondition())
					result += " " + tuple.getCond().toString();
				else
					result += " (" + tuple.getCond().toString() + ")";
				result += CRLF;
			}
		}		
		else if (isSimpleCondition())
			result += expressionOP.toString();
		
		return result;			
	}

	/**
	 * Crea una condizione a partire dalla sua rappresentazione
	 * XML, cos&igrave; come &egrave; specificata dal file
	 * condition.dtd
	 * @param xml
	 * @param cF
	 * @param oF
	 * @param eF
	 * @return
	 * @throws IllegalArgumentException
	 * Se l'XML rappresentato dalla stringa <code>xml</code> &egrave;
	 * malformato o <code>null</code>
	 */
	public static Condition createFromXml(String xml, SqlGenerator sqlGenerator)
	throws IllegalArgumentException
	{
		SAXBuilder builder = new SAXBuilder();
		Document xmlDoc = null;
		try {xmlDoc = builder.build(new StringReader(xml));}
		catch (JDOMException e) {throw new IllegalArgumentException("XML malformato o nullo");}
		catch (IOException e) {}
		return createFromXml(xmlDoc.getRootElement(), sqlGenerator);
	}

	/**
	 * Crea una condizione a partire dalla sua rappresentazione
	 * XML, cos&igrave; come &egrave; specificata dal file
	 * condition.dtd
	 * @param xml
	 * @param cF
	 * @param oF
	 * @param eF
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Condition createFromXml(Element xml, SqlGenerator sqlGenerator)
	{
		if (!"condition".equals(xml.getName()))
			throw new IllegalArgumentException("Il root element deve essere <condition>");

		ConditionFactory cF = null;
		OperatorFactory oF = null;
		@SuppressWarnings("unused") ExpressionFactory eF = null;
		try
		{
			cF = sqlGenerator.getConditionFactory();
			oF = sqlGenerator.getOperatorFactory();
			eF = sqlGenerator.getExpressionFactory();			
		} 
		catch (Exception e) {throw new IllegalArgumentException("SqlGenerator passato non valido");}

		Condition result = cF.getCondition();
		for (Element item1 : (Iterable<? extends Element>) xml.getChildren())
		{
			if ("sql".equals(item1.getName()))
				continue;
			LogicalOperator lO = oF.getLogicalOperator(item1.getAttributeValue("logical_relation"));
			if ("operator".equals(item1.getName()))
			{
				RelationalOperator rO = oF.getOperator(item1.getChildText("name"));
				List<? extends Element> operands = item1.getChildren("operand");
				for (int i = 0; i < rO.getNumOfOperands(); i++)
					rO.getOperands()[i] = ValueExpression.createFromXml(operands.get(i), sqlGenerator);
				result.addSimpleCondition(rO, lO);
			}
			else if ("embedded_condition".equals(item1.getName()))
				result.addCondition(Condition.createFromXml(item1.getChild("condition"), sqlGenerator), lO);
			else
				throw new IllegalArgumentException();
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	public List<Element> getXml()
	{
		List<Element> result = new ArrayList<Element>();
		
		if (isSimpleCondition())
		{
			result.addAll(expressionOP.getXml());
		}
		else
		{
			Element condition = new Element("condition");
			Element sql = new Element("sql");
			CDATA cData = new CDATA(this.toString());
			sql.addContent(cData);
			condition.addContent(sql);
			if (conditions != null)
				for (Tuple item : conditions)			
				{
					if (item.getCond().isSimpleCondition())
					{
						Element operator = item.getCond().getXml().get(0);
						operator.setAttribute("logical_relation", item.getOp().getName());
						condition.addContent(operator);
					}
					else
					{
						Element embedded = new Element("embedded_condition");
						embedded.setAttribute("logical_relation", item.getOp().getName());
						for (Element item2 : item.getCond().getXml())
							embedded.addContent(item2);
						condition.addContent(embedded);
					}
				}
			else
				throw new IllegalStateException("");
			result.add(condition);
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getStringXml()
	 */
	public String getStringXml()
	{
		StringWriter result = new StringWriter();
		XMLOutputter out = new XMLOutputter();
		// out.setIndentSize(3); // DEPRECATO. SERVIVA SOLO PER MOTIVI ESTETICI
//		out.setLineSeparator(CRLF);
//		out.setNewlines(true);
		out.setFormat(Format.getPrettyFormat());
		try
		{
			out.output(getXml(), result);
		}
		catch (IOException e) {}
		return result.toString();
	}
	
	/**
	 * @return <code>true</code> se la condizione
	 * &egrave; stata creata ma &egrave; vuota,
	 * altrimenti <code>false</code>.
	 */
	public boolean isEmpty()
	{
		return conditions == null || conditions.isEmpty();
	}
	
	/** 
	 * Il metodo non chiama esclusivamente <code>super.clone()</code>,
	 * ma clona anche tutte le condizioni e le sottocondizioni
	 * contenute in questa condizione, tutti gli oggetti di cui 
	 * &egrave; composta.
	 * @see RelationalOperator#clone()
	 * @see Tuple#clone()
	 */
	@Override
	public Condition clone() throws CloneNotSupportedException
	{
		Condition clone = null;
		if (isSimpleCondition())
			clone = new Condition(expressionOP.clone());
		else
		{
			clone = new Condition();
			if (conditions != null)
				for (Tuple item : conditions)
					clone.addCondition(item.clone());
		}
		return clone;
	}
	/**
	 * Questo metodo viene chiamato soltanto dal metodo
	 * {@link Condition#clone() clone}
	 * @param tupla
	 */
	private void addCondition(Tuple tupla)
	{
		if (conditions == null)
			conditions = new ArrayList<Tuple>();
		conditions.add(tupla);
	}

	/**
	 * Aggiorna gli alias delle colonne contenute(a tutti i livelli) nella Condition 
	 * 
	 * 
	 * 
	 * @param id
	 * @param newAlias
	 * @param newAliasId
	 * @return
	 */
	public void changeNestedColumnAlias(Long id , String newAlias, short newAliasNum)
	{
		changeNestedColumnAlias(this,id,newAlias,newAliasNum);
	}
	private void changeNestedColumnAlias(Condition cond, Long id , String newAlias, short newAliasNum)
	{
		if(cond.isSimpleCondition()){
			for (ValueExpression ve: cond.expressionOP.getOperands()){
				 setNewAlias(ve , id, newAlias,newAliasNum);
			}
		}else{
			for (Tuple t : cond.conditions ){
				changeNestedColumnAlias(t.getCond(),id,newAlias,newAliasNum);
			}
		}
	}
	private void setNewAlias(Function f , Long id , String newAlias, short newAliasNum){
		for (ValueExpression ve :f.getArguments().getExpressions()){
			setNewAlias(ve,id,newAlias,newAliasNum);
		}
	}
	private void setNewAlias(ValueExpression ve , Long id , String newAlias, short newAliasNum){
		if(ve instanceof Column ){
			setNewAlias((Column)ve, id,newAlias,newAliasNum);
		}else if(ve instanceof Function ){
			setNewAlias((Function)ve, id,newAlias,newAliasNum);
		}
		for (AppendedValueExpression ave: ve.getAppendedValueExpressions()){
			if(ave.getExpression() instanceof Column ){
				setNewAlias((Column)ave.getExpression(), id,newAlias,newAliasNum);
			}else if(ave.getExpression() instanceof Function ){
				setNewAlias((Function)ave.getExpression(), id,newAlias,newAliasNum);
			}
		}
	}
	private void setNewAlias(Column col , Long id , String newAlias, short newAliasNum){
		Table tab = col.getTable();
		if ( tab instanceof TableExpression){
			TableExpression te = (TableExpression)tab;
			if((te.getId()!= null) && te.getId().equals(id)){
				te.setAlias(newAlias);
				//Cambio anche la UIKey della Column
				Matcher m = separeTableAndAlias.matcher(col.getUIKey());
				if(m.find()){
					
					String newUIKey = m.group(1)+ALIAS+newAliasNum;
					if(m.group(5)!=null)newUIKey +=m.group(5);
					newUIKey +=m.group(6);
					col.setUIKey(newUIKey);
				}
				
			}
		}
	}
	
	//questi quattro metodi sono necessari per l'uso di XMLEncoder - Filippo Mazzini 23.10.07
	public List<Tuple> getConditions() {
		return conditions;
	}
	public void setConditions(List<Tuple> conditions) {
		this.conditions = conditions;
	}
	public RelationalOperator getExpressionOP() {
		return expressionOP;
	}
	public void setExpressionOP(RelationalOperator expressionOP) {
		this.expressionOP = expressionOP;
	}
	//fine Filippo Mazzini 23.10.07
	
	
	
}
