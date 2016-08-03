package it.webred.diogene.sqldiagram;

public class Tuple {
	/**
	*	@param cond
	*	@param op
	*/
	public Tuple(Condition cond, LogicalOperator op) {this.cond = cond; this.op = op;}
	private Condition cond;
	private LogicalOperator op;
	
	/**
	 * Il metodo non chiama esclusivamente <code>super.clone()</code>,
	 * ma clona anche la condizione e l'operatore di
	 * cui &egrave; composta
	 * @see LogicalOperator#clone()
	 * @see Condition#clone()
	 */
	@Override
	protected Tuple clone() throws CloneNotSupportedException
	{
		return new Tuple(cond.clone(), op.clone());
	}
	
	//questi quattro metodi sono necessari per l'uso di XMLEncoder - Filippo Mazzini 23.10.07
	public Condition getCond() {
		return cond;
	}

	public void setCond(Condition cond) {
		this.cond = cond;
	}

	public LogicalOperator getOp() {
		return op;
	}

	public void setOp(LogicalOperator op) {
		this.op = op;
	}
	//fine Filippo Mazzini 23.10.07
}
