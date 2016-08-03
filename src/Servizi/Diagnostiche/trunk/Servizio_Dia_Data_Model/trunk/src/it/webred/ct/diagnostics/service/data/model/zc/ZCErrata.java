package it.webred.ct.diagnostics.service.data.model.zc;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ZCErrata {
	
	@Id
	private Long zonadic;
	
	private Long zona;
	
	private Long foglio;
	
	private String oldmz;
	
	private String newmz;
	
	private Long occorrenze;

	public ZCErrata() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getZonadic() {
		return zonadic;
	}

	public void setZonadic(Long zonadic) {
		this.zonadic = zonadic;
	}

	public Long getZona() {
		return zona;
	}

	public void setZona(Long zona) {
		this.zona = zona;
	}

	public Long getFoglio() {
		return foglio;
	}

	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}

	public String getOldmz() {
		return oldmz;
	}

	public void setOldmz(String oldmz) {
		this.oldmz = oldmz;
	}

	public String getNewmz() {
		return newmz;
	}

	public void setNewmz(String newmz) {
		this.newmz = newmz;
	}

	public Long getOccorrenze() {
		return occorrenze;
	}

	public void setOccorrenze(Long occorrenze) {
		this.occorrenze = occorrenze;
	}

	
	
}
