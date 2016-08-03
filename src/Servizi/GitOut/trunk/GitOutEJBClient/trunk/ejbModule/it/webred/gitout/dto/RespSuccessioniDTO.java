package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respSuccessioniDTO")
public class RespSuccessioniDTO implements Serializable{

	private static final long serialVersionUID = 5051594240119638622L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<SuccessioneDTO> lstObjs = null;

	public RespSuccessioniDTO() {
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

	public ArrayList<SuccessioneDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<SuccessioneDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
