package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class CausaleBean extends MasterItem{

	private static final long serialVersionUID = 4594393614050986093L;
	
	private String cau_cod = "";
	private String cau_des = "";

	public CausaleBean() {
		
	}//-------------------------------------------------------------------------

	public String getCau_cod() {
		return cau_cod;
	}

	public void setCau_cod(String cau_cod) {
		this.cau_cod = cau_cod;
	}

	public String getCau_des() {
		return cau_des;
	}

	public void setCau_des(String cau_des) {
		this.cau_des = cau_des;
	}
	
	

}
