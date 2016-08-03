package it.escsolution.escwebgis.impiantitermici.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author dan
 *
 */
public class ImpTer extends EscObject implements Serializable{

	private String occupante;
	private String tipo_via;
	private String pref;
	private String nome_via;
	private String civico;
	private String barrato;
	private String scala;
	private String piano;
	private String interno;
	private String gruppo;



	public ImpTer()
	{
		this.occupante="";
		this.tipo_via="";
		this.pref="";
		this.nome_via="";
		this.civico="";
		this.barrato="";
		this.scala="";
		this.piano="";
		this.interno="";
		this.gruppo="";

	}



	public String getBarrato()
	{
		return barrato;
	}



	public void setBarrato(String barrato)
	{
		this.barrato = barrato;
	}



	public String getCivico()
	{
		return civico;
	}



	public void setCivico(String civico)
	{
		this.civico = civico;
	}



	public String getGruppo()
	{
		return gruppo;
	}



	public void setGruppo(String gruppo)
	{
		this.gruppo = gruppo;
	}



	public String getInterno()
	{
		return interno;
	}



	public void setInterno(String interno)
	{
		this.interno = interno;
	}



	public String getNome_via()
	{
		return nome_via;
	}



	public void setNome_via(String nome_via)
	{
		this.nome_via = nome_via;
	}



	public String getOccupante()
	{
		return occupante;
	}



	public void setOccupante(String occupante)
	{
		this.occupante = occupante;
	}



	public String getPiano()
	{
		return piano;
	}



	public void setPiano(String piano)
	{
		this.piano = piano;
	}



	public String getPref()
	{
		return pref;
	}



	public void setPref(String pref)
	{
		this.pref = pref;
	}



	public String getScala()
	{
		return scala;
	}



	public void setScala(String scala)
	{
		this.scala = scala;
	}



	public String getTipo_via()
	{
		return tipo_via;
	}



	public void setTipo_via(String tipo_via)
	{
		this.tipo_via = tipo_via;
	}




}
