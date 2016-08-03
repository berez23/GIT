package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="respLocazioniDTO")
public class RespLocazioniDTO implements Serializable{

	private static final long serialVersionUID = -7145737143512430491L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<LocazioneDTO> lstObjs = null;
	
	public RespLocazioniDTO() {
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

	public ArrayList<LocazioneDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<LocazioneDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
