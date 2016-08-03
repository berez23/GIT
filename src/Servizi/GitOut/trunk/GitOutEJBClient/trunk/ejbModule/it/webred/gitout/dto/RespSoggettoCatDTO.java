package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respUnitaImmoDTO")
public class RespSoggettoCatDTO implements Serializable{

	private static final long serialVersionUID = -1737360154376370771L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<SoggettoCatDTO> lstObjs = null;

	public RespSoggettoCatDTO() {
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

	public ArrayList<SoggettoCatDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<SoggettoCatDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
