package it.webred.ct.service.carContrib.data.access.common.dto;

import java.io.Serializable;

public class MessaggioDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codMsg; //"W"=warniNG; "E"=bloccante
	private String desMsg;
	
	public MessaggioDTO() {
		codMsg="OK";
		desMsg="";
	}

	public String getCodMsg() {
		return codMsg;
	}

	public void setCodMsg(String codMsg) {
		this.codMsg = codMsg;
	}

	public String getDesMsg() {
		return desMsg;
	}

	public void setDesMsg(String desMsg) {
		this.desMsg = desMsg;
	}
	
	

}
