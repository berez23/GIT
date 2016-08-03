package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respCompravenditeDTO")
public class RespCompravenditeDTO implements Serializable{

	private static final long serialVersionUID = -5496459317579527308L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<CompravenditaDTO> lstObjs = null;

	public RespCompravenditeDTO() {
	}//-------------------------------------------------------------------------

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public ArrayList<CompravenditaDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<CompravenditaDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
