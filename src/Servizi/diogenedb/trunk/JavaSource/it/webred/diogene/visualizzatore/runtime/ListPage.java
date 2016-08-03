package it.webred.diogene.visualizzatore.runtime;

import java.util.LinkedHashSet;

/**
 * @author Dan Petre
 * 
 */
public class ListPage extends Page
{
	private String						sql;
	private LinkedHashSet<ListElement>	listElement;
	private String[]					idDcColumnKey;
	private String 						aliasInizioValidita;
	private String 						aliasFineValidita;
	private String 						aliasExtKey;
	

	public LinkedHashSet<ListElement> getListElement()
	{
		return listElement;
	}

	public void setListElement(LinkedHashSet<ListElement> listElement)
	{
		this.listElement = listElement;
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	/*
	 * Return array of allias key in column
	 */
	public String[] getIdDcColumnKey()
	{
		return idDcColumnKey;
	}

	public void setIdDcColumnKey(String[] idDcColumnKey)
	{
		this.idDcColumnKey = idDcColumnKey;
	}

	public String getAliasExtKey()
	{
		return aliasExtKey;
	}

	public void setAliasExtKey(String aliasExtKey)
	{
		this.aliasExtKey = aliasExtKey;
	}

	public String getAliasFineValidita()
	{
		return aliasFineValidita;
	}

	public void setAliasFineValidita(String aliasFineValidita)
	{
		this.aliasFineValidita = aliasFineValidita;
	}

	public String getAliasInizioValidita()
	{
		return aliasInizioValidita;
	}

	public void setAliasInizioValidita(String aliasInizioValidita)
	{
		this.aliasInizioValidita = aliasInizioValidita;
	}

}
