package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class BodFornituraDocfaBean extends MasterItem{

	private static final long serialVersionUID = 22817818599116641L;
	private Long idForDocfa = null;
	private Long forFk = new Long(0);
	private Long istFk = new Long(0);
	private String pathFileName = "";

	public BodFornituraDocfaBean() {
		// TODO Auto-generated constructor stub
	}

	public Long getIdForDocfa() {
		return idForDocfa;
	}

	public void setIdForDocfa(Long idForDocfa) {
		this.idForDocfa = idForDocfa;
	}

	public Long getForFk() {
		return forFk;
	}

	public void setForFk(Long idFor) {
		this.forFk = idFor;
	}

	public Long getIstFk() {
		return istFk;
	}

	public void setIstFk(Long idIst) {
		this.istFk = idIst;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getPathFileName() {
		return pathFileName;
	}

	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}
	
	

}
