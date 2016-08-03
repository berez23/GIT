package it.webred.ct.service.tares.data.model;

import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the SETAR_BILANCIO_ANNO_CORR database table.
 * 
 */
@Entity
@Table(name="SETAR_BILANCIO_ANNO_CORR")
public class SetarBilancioAnnoCorr extends BaseItem {
	
	private static final long serialVersionUID = 1L;

	private String codice;

	private String descrizione;

	private Long id;

	private BigDecimal parteFissa;

	private BigDecimal parteVaria;

	private BigDecimal totale;

	public SetarBilancioAnnoCorr() {
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="PARTE_FISSA")
	public BigDecimal getParteFissa() {
		return this.parteFissa;
	}

	public void setParteFissa(BigDecimal parteFissa) {
		this.parteFissa = parteFissa;
	}

	@Column(name="PARTE_VARIA")
	public BigDecimal getParteVaria() {
		return this.parteVaria;
	}

	public void setParteVaria(BigDecimal parteVaria) {
		this.parteVaria = parteVaria;
	}

	public BigDecimal getTotale() {
		return this.totale;
	}

	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

}