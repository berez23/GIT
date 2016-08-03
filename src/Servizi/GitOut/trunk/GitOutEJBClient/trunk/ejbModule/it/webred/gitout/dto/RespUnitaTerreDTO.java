package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respUnitaImmoDTO")
public class RespUnitaTerreDTO implements Serializable{

	private static final long serialVersionUID = -4206550578337962360L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<UnitaTerreDTO> lstObjs = null;

	public RespUnitaTerreDTO() {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ArrayList<UnitaTerreDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<UnitaTerreDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}
	
	

}
