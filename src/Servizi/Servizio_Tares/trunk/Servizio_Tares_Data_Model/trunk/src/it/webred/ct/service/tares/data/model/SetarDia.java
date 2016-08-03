package it.webred.ct.service.tares.data.model;

import javax.persistence.*;


/**
 * The persistent class for the SETAR_DIA database table.
 * 
 */
@Entity
@Table(name="SETAR_DIA")
public class SetarDia extends BaseItem {
	
	private static final long serialVersionUID = 5129013499856004882L;

	@Id
	private Long id;
	
	private String descrizione;

	@Column(name="SQL_DETTAGLIO")
	private String sqlDettaglio;

	private String valore;

	public SetarDia() {
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getSqlDettaglio() {
		return this.sqlDettaglio;
	}

	public void setSqlDettaglio(String sqlDettaglio) {
		this.sqlDettaglio = sqlDettaglio;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}