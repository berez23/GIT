package it.bod.application.beans;

import java.util.Set;

import it.bod.application.common.MasterItem;

public class BodFornituraBean extends MasterItem{

	private static final long serialVersionUID = 5338652380326548158L;
	
	private Long idFor = null;
	private String fileName = "";
	private Double fileSize = null;
	private String status = "";
	private Set<BodFornituraDocfaBean> fornitureDocfa = null;
	private Long progressivo = new Long(0);
	
	public BodFornituraBean() {
		// TODO Auto-generated constructor stub
	}

	public Long getIdFor() {
		return idFor;
	}

	public void setIdFor(Long idFor) {
		this.idFor = idFor;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fornituraFileName) {
		this.fileName = fornituraFileName;
	}

	public Double getFileSize() {
		return fileSize;
	}

	public void setFileSize(Double fornituraFileSize) {
		this.fileSize = fornituraFileSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Set<BodFornituraDocfaBean> getFornitureDocfa() {
		return fornitureDocfa;
	}

	public void setFornitureDocfa(Set<BodFornituraDocfaBean> fornitureDocfa) {
		this.fornitureDocfa = fornitureDocfa;
	}

	public Long getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Long progressivo) {
		this.progressivo = progressivo;
	}
	
	

}
