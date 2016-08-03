package it.webred.ct.service.tares.data.model;

import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the SETAR_CONSUNTIVO_ANNO_PREC database table.
 * 
 */
@Entity
@Table(name="SETAR_CONSUNTIVO_ANNO_PREC")
public class SetarConsuntivoAnnoPrec extends BaseItem {

	private static final long serialVersionUID = 1L;

	private String codice;
	private String descrizione;
	private Long id;
	private BigDecimal per;
	private String val;

	public SetarConsuntivoAnnoPrec() {
	}//-------------------------------------------------------------------------

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

	public BigDecimal getPer() {
		return this.per;
	}

	public void setPer(BigDecimal per) {
		this.per = per;
	}

	public String getVal() {
		return this.val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}