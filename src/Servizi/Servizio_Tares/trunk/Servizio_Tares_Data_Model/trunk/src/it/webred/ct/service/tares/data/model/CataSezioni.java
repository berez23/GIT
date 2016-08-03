package it.webred.ct.service.tares.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CATA_SEZIONI database table.
 * 
 */
@Entity
@Table(name="CATA_SEZIONI")
public class CataSezioni implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_SEZIONE")
	private Long idSezione;

	@Column(name="COD_SEZIONE")
	private String codSezione;

	@Temporal(TemporalType.DATE)
	private Date fine;

	@Column(name="FLAG_CATA")
	private BigDecimal flagCata;

	@Column(name="ID_COMUNE")
	private BigDecimal idComune;

	@Temporal(TemporalType.DATE)
	private Date inizio;

	private String nome;

	private String sezione;

	@Column(name="UBICAZIONE_GEOGRAFICA")
	private String ubicazioneGeografica;

	public CataSezioni() {
	}

	public Long getIdSezione() {
		return this.idSezione;
	}

	public void setIdSezione(Long idSezione) {
		this.idSezione = idSezione;
	}

	public String getCodSezione() {
		return this.codSezione;
	}

	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}

	public Date getFine() {
		return this.fine;
	}

	public void setFine(Date fine) {
		this.fine = fine;
	}

	public BigDecimal getFlagCata() {
		return this.flagCata;
	}

	public void setFlagCata(BigDecimal flagCata) {
		this.flagCata = flagCata;
	}

	public BigDecimal getIdComune() {
		return this.idComune;
	}

	public void setIdComune(BigDecimal idComune) {
		this.idComune = idComune;
	}

	public Date getInizio() {
		return this.inizio;
	}

	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSezione() {
		return this.sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getUbicazioneGeografica() {
		return this.ubicazioneGeografica;
	}

	public void setUbicazioneGeografica(String ubicazioneGeografica) {
		this.ubicazioneGeografica = ubicazioneGeografica;
	}

}