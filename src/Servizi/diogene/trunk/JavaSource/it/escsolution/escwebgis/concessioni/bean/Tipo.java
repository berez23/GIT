package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

public class Tipo extends EscComboObject implements Serializable,EscComboInterface 
{
	private String codTipo;
	private String desTipo;
	
	public String getCode(){
		return codTipo;
	}
	public String getDescrizione(){
		return desTipo;
	}
	public Tipo() 
	{
	}
	public Tipo(String cod,String  des) 
	{
		codTipo = cod;
		desTipo = des;
	}
	public String toString() 
	{
		return desTipo;
	}
	/**
	 * @return
	 */
	public String getCodTipo() {
		return codTipo;
	}

	/**
	 * @return
	 */
	public String getDesTipo() {
		return desTipo;
	}

	/**
	 * @param string
	 */
	public void setCodTipo(String string) {
		codTipo = string;
	}

	/**
	 * @param string
	 */
	public void setDesTipo(String string) {
		desTipo = string;
	}

}
