package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respPregeoDTO")
public class RespPregeoDTO implements Serializable{

	private static final long serialVersionUID = 5051594240119638622L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<PregeoDTO> lstObjs = null;

	public RespPregeoDTO() {
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

	public ArrayList<PregeoDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<PregeoDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}