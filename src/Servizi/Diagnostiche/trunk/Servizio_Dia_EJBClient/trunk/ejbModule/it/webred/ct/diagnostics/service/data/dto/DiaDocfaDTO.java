package it.webred.ct.diagnostics.service.data.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;

public class DiaDocfaDTO extends CeTBaseObject implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private int start;
	private int maxrows;
	private String categoria;
	private Date fornituraDa;
	private Date fornituraA;
	
	public DiaDocfaDTO(String enteId, String userId) {
		super();
		setEnteId(enteId);
		setUserId(userId);
	}
	
	/**
	 * Ritorna la fornitura da nel formato MM/AAAA
	 * @return
	 */
	public Date getFornituraDa() {
		return fornituraDa;
	}

	public void setFornituraDa(Date fornituraDa) {
		this.fornituraDa = fornituraDa;
	}
	
	/**
	 * Ritorna la fornitura a nel formato MM/AAAA
	 * @return
	 */
	public Date getFornituraA() {
		return fornituraA;
	}

	public void setFornituraA(Date fornituraA) {
		this.fornituraA = fornituraA;
	}
	
	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getMaxrows() {
		return maxrows;
	}


	public void setMaxrows(int maxrows) {
		this.maxrows = maxrows;
	}
	
	
}
