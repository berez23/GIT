package it.webred.cs.csa.ejb.dto;

import java.util.Date;

import it.webred.cs.data.model.CsOOperatoreSettore;


/**
 * @author Alessandro Feriani
 *
 */
public class SchedaBarthelDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long schedaMultidimDiarioId;
	private Date dataDiValutazione;
	private String jsonString;
	private CsOOperatoreSettore opSettore;
	private String versione;
	private String descrizione;
	
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public CsOOperatoreSettore getOpSettore() {
		return opSettore;
	}
	public void setOpSettore(CsOOperatoreSettore opSettore) {
		this.opSettore = opSettore;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSchedaMultidimDiarioId() {
		return schedaMultidimDiarioId;
	}
	public void setSchedaMultidimDiarioId(Long schedaMultidimDiarioId) {
		this.schedaMultidimDiarioId = schedaMultidimDiarioId;
	}
	public Date getDataDiValutazione() {
		return dataDiValutazione;
	}
	public void setDataDiValutazione(Date dataDiValutazione) {
		this.dataDiValutazione = dataDiValutazione;
	}
	public String getVersione() {
		return versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
