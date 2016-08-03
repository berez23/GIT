package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respElettricaFornituraDTO")
public class RespElettricheFornitureDTO implements Serializable{

	private static final long serialVersionUID = 2495955769806754583L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<ElettricaFornituraDTO> lstObjs = null;

	public RespElettricheFornitureDTO() {
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

	public ArrayList<ElettricaFornituraDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<ElettricaFornituraDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
