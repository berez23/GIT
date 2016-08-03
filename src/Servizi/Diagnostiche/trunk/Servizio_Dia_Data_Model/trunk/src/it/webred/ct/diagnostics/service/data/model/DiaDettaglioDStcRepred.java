package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_STC_REPRED database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_STC_REPRED")
public class DiaDettaglioDStcRepred implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_STC_REPRED_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_STC_REPRED")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_STC_REPRED_ID_GENERATOR")
	private long id;

	private String belfiore;

	private BigDecimal contribuenti;

	private String fascia;

	@Column(name="FK_DIA_TESTATA")
	private BigDecimal fkDiaTestata;

	private BigDecimal imponibile;

	private BigDecimal imposta;

	private String modello;

	public DiaDettaglioDStcRepred() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBelfiore() {
		return this.belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	public BigDecimal getContribuenti() {
		return this.contribuenti;
	}

	public void setContribuenti(BigDecimal contribuenti) {
		this.contribuenti = contribuenti;
	}

	public String getFascia() {
		return this.fascia;
	}

	public void setFascia(String fascia) {
		this.fascia = fascia;
	}

	public BigDecimal getFkDiaTestata() {
		return this.fkDiaTestata;
	}

	public void setFkDiaTestata(BigDecimal fkDiaTestata) {
		this.fkDiaTestata = fkDiaTestata;
	}

	public BigDecimal getImponibile() {
		return this.imponibile;
	}

	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}

	public BigDecimal getImposta() {
		return this.imposta;
	}

	public void setImposta(BigDecimal imposta) {
		this.imposta = imposta;
	}

	public String getModello() {
		return this.modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

}