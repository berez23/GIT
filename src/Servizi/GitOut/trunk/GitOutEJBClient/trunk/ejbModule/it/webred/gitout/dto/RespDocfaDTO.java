package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="respDocfaDTO")
public class RespDocfaDTO implements Serializable{
	
	private static final long serialVersionUID = -5648822660633558052L;
	
	private Boolean success = false;
	private String error = "";
	private ArrayList<DocfaDTO> lstObjs = null;

	public RespDocfaDTO() {
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

	public ArrayList<DocfaDTO> getLstObjs() {
		return lstObjs;
	}

	public void setLstObjs(ArrayList<DocfaDTO> lstObjs) {
		this.lstObjs = lstObjs;
	}

	
	
}
