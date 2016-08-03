package it.webred.diogene.querybuilder.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DcEntityBean implements Serializable, Comparable<DcEntityBean>, Cloneable
{
	private static final long serialVersionUID = -3531995768081830169L;
	public static final short DEFAULT_ALIAS_NUM = 0;
	public static final short MAXIMUM_ALIAS_NUM = 64;
	public static final String ALIAS_PREFIX = "_";
	
	private Long id;
	private String name, desc, alias;
	private short aliasNum = DEFAULT_ALIAS_NUM;
	private HashSet<? extends DcColumnBean> columns;
	private Date dtMod;
	private boolean entityEditable=false;
	private boolean entityViewable=false;
	private boolean entityDeletable=false;
	private boolean entityExplictSQL=false;
	public int compareTo(DcEntityBean o)
	{
		return toString().compareTo(o.toString());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj != null)
			return toString().equals(obj.toString());
		return false;
	}
	@Override
	public String toString()
	{
		return "DC_ENTITY" + getId() + "ALIAS" + getAliasNum();
	}
	/**
	 * L'hash code &egrave; stato implementato
	 * per essere utilizzato in modifica all'interno
	 * del metodo {@link it.webred.diogene.querybuilder.db.EntitiesDBManager#updateUserEntity(UserEntityBean, Session, SqlGenerator)},
	 * per utilizzare questa classe come chiave in
	 * una mappa che possa garantire univocit&agrave; per mezzo dell'id
	 * e dell'alias.
	 * Questo hash code garantisce la propria univocit&agrave;
	 * alle seguenti due condizioni:
	 * <ol>
	 * 	<li>
	 * 		Gli id delle Entit&agrave; utente non siano mai pi&ugrave; 
	 * 		grandi di 67.108.864 Anche nel caso fossero pi&ugrave;,
	 * 		la probabilit&agrave; di hash uguali per entit&agrave; diverse
	 * 		&egrave; minore di 1/67.108.864
	 * 	</li>
	 * 	<li>
	 * 		L'alias non pu&ograve; essere maggiore di o uguale a
	 * 		{@link DcEntityBean#MAXIMUM_ALIAS_NUM}, cosa del resto impedita
	 * 		alla chiamata dei metodi {@link DcEntityBean#setAliasNum(short)}
	 * 		e {@link DcEntityBean#setAliasNum(String)}
	 * 	</li>
	 * </ol>
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return (int) (id * MAXIMUM_ALIAS_NUM) + getAliasNum();
	}
	@Override
	public DcEntityBean clone() throws CloneNotSupportedException
	{
		DcEntityBean clone = (DcEntityBean) super.clone();
		if (getColumns() != null)
			clone.setColumns((HashSet<? extends DcColumnBean>) getColumns().clone());
		return clone;
	}
	
	
	
	
	
	// GETTERS AND SETTERS ////////////////////////////////////////////
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
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
	 * @return il nome mostrato all'utente
	 */
	public String getName()
	{
		return (getAliasNum() == DEFAULT_ALIAS_NUM) ? name : (name + " (" + getAliasNum() + ")" );
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public HashSet<? extends DcColumnBean> getColumns()
	{
		return columns;
	}
	public void setColumns(HashSet<? extends DcColumnBean> columns)
	{
		this.columns = columns;
	}
	public String getAlias()
	{
		if (alias != null)
			return alias;
		if (getAliasNum() == DEFAULT_ALIAS_NUM)
			return null;
		return getSqlName() + ALIAS_PREFIX + getAliasNum();
	}
	public short getAliasNum()
	{
		return aliasNum;
	}
	private short getAliasNum(String alias)
	{
		if (alias != null && !alias.equals(getSqlName()))
		{
			Matcher m = Pattern.compile(".*\\Q" + ALIAS_PREFIX + "\\E(\\d+)\\z").matcher(alias.trim());
			if (m.find())
			{
				try 
				{
					return Short.parseShort(m.group(1));
				}
				catch (Throwable e) {}
			}
		}
		return DEFAULT_ALIAS_NUM;
	}
	/**
	 * @param aliasNum
	 * Il numero dell'alias
	 * @throws Exception
	 * Se l'alias passato &egrave; maggiore di o
	 * uguale a {@link DcEntityBean#MAXIMUM_ALIAS_NUM}
	 */
	public void setAliasNum(short aliasNum)
	throws Exception
	{
		if (aliasNum < 0)
			aliasNum *= -1;
		if (aliasNum >= MAXIMUM_ALIAS_NUM)
			throw new IllegalArgumentException("L'alias non puo' essere piu' grande di " + (MAXIMUM_ALIAS_NUM - 1) + " al fine di garantire l'univocit&agrave; dell'hash code");
		this.aliasNum = aliasNum;
	}
	/**
	 * @param alias
	 * Una stringa per l'alias.
	 * Se <code>null</code>, o se uguale al nome
	 * SQL, l'alias viene impostato a
	 * {@link DcEntityBean#DEFAULT_ALIAS_NUM}, 
	 * altrimenti l'alias cerca alla fine della
	 * stringa passata, per vedere se termina con
	 * la stringa {@link DcEntityBean#ALIAS_PREFIX}
	 * seguita da un numero: se &egrave; cos&igrave;,
	 * quel numero viene parsato per impostarlo come
	 * alias, altrimenti l'alias rimane quello di default
	 * {@link DcEntityBean#DEFAULT_ALIAS_NUM}
	 * @throws Exception
	 * Se l'alias passato &egrave; maggiore di o
	 * uguale a {@link DcEntityBean#MAXIMUM_ALIAS_NUM}
	 */
	public void setAliasNum(String alias)
	throws Exception
	{
		setAliasNum(getAliasNum(alias));
	}
	public abstract String getSqlName();
	protected abstract void setSqlName(String sqlName);

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public Date getDtMod()
	{
		return dtMod;
	}

	public void setDtMod(Date dtMod)
	{
		this.dtMod = dtMod;
	}

	public String getAliasRelation()
	{
		return "E_"+getId();
	}

	public boolean isEntityDeletable()
	{
		return entityDeletable;
	}

	public void setEntityDeletable(boolean entityDeletable)
	{
		this.entityDeletable = entityDeletable;
	}

	public boolean isEntityEditable()
	{
		return entityEditable;
	}

	public void setEntityEditable(boolean entityEditable)
	{
		this.entityEditable = entityEditable;
	}

	public boolean isEntityViewable()
	{
		return entityViewable;
	}

	public void setEntityViewable(boolean entityViewable)
	{
		this.entityViewable = entityViewable;
	}

	public boolean isEntityExplictSQL() {
		return entityExplictSQL;
	}

	public void setEntityExplictSQL(boolean entityExplictSQL) {
		this.entityExplictSQL = entityExplictSQL;
	}

}
