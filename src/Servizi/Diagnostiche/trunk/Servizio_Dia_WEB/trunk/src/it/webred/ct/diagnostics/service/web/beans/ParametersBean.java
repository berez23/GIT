package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.Serializable;

public class ParametersBean extends UserBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long idDiaTestata;
	private String codCommand;
	
	public long getIdDiaTestata() {
		return idDiaTestata;
	}

	public void setIdDiaTestata(long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}

	public String getCodCommand() {
		return codCommand;
	}

	public void setCodCommand(String codCommand) {
		this.codCommand = codCommand;
	}

	public ParametersBean() {
		super();
		// TODO Auto-generated constructor stub
	}
}
