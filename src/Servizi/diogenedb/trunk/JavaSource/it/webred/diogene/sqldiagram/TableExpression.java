package it.webred.diogene.sqldiagram;

import java.io.Serializable;

/**
 * Questa classe descrive una tabella SQL, ovvero un nome SQL che
 * che potrebbe riferirsi a entit&agrave; come una tabella, una
 * vista, un sinonimo.
 * @see it.webred.diogene.sqldiagram.Table
 * @author Giulio Quaresima
 * @version $Revision: 1.4 $ $Date: 2008/01/24 14:24:42 $
 */
public class TableExpression extends Expression implements Serializable, Table
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 142789724060143988L;
	private String alias = null;
	private Long id = null;
	private boolean outer;
	
	/**
	* @param id vedi {@link TableExpression#setId(Long)}
	* @param exp vedi {@link Table#getExpression()}
	* @param des vedi {@link Expression#setExpression(String)}
	* @param alias vedi {@link Table#setAlias(String)}
	*/
	public TableExpression(Long id, String exp, String des, String alias)
	{
		super(exp, des);
		this.id = id;
		this.alias = alias;
	}
	
	/**
	 * @see Table#clone()
	 */
	@Override
	public TableExpression clone() throws CloneNotSupportedException
	{
		return (TableExpression) super.clone();
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Expression#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return super.getDescription();
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Expression#getExpression()
	 */
	@Override
	public String getExpression()
	{
		// TODO Auto-generated method stub
		return super.getExpression();
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Expression#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description)
	{
		// TODO Auto-generated method stub
		super.setDescription(description);
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Expression#setExpression(java.lang.String)
	 */
	@Override
	public void setExpression(String expression)
	{
		// TODO Auto-generated method stub
		super.setExpression(expression);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String result = "" + getExpression();
		if (getAlias() != null && !"".equals(getAlias().trim()))
			result += " " + getAlias();
		return result;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Table#getAlias()
	 */
	public String getAlias()
	{
		return alias;
	}

	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.Table#setAlias(java.lang.String)
	 */
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	/**
	 * @return L'id univoco di questa entit&agrave;,
	 * se presente, altrimenti <code>null</code>
	 */
	public Long getId()
	{
		return id;
	}
	/**
	 * In taluni casi pu&ograve; essere comodo avere un
	 * id numerico che contraddistingua univocamente
	 * una entit&agrave;, per cui si fornisce questa
	 * possibilit&agrave;
	 * @param id
	 * L'id univoco
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	public boolean isOuter()
	{
		return outer;
	}

	public void setOuter(boolean outer)
	{
		this.outer = outer;
	}
	
	//questi quattro metodi sono necessari per l'uso di XMLEncoder - Filippo Mazzini 22.10.07
	public String getDes() {
		return getDescription();
	}
	
	public void setDes(String des) {
		setDescription(des);
	}
	
	public String getExp() {
		return getExpression();
	}
	
	public void setExp(String exp) {
		setDescription(exp);
	}
	//fine Filippo Mazzini 22.10.07
	
}
