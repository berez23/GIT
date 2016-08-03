package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_ICI203 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_ICI203")
public class DiaDettaglioDCfrIci203 extends it.webred.ct.diagnostics.service.data.SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_ICI2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_ICI2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;

	@Column(name="COD_FISCALE")
	private String codFiscale;

	private String cognome;

	private String cuaa;

	private String denominazione;

	private String nome;

	@Column(name="PART_IVA")
	private String partIva;

    public DiaDettaglioDCfrIci203() {
    }

	public String getCodFiscale() {
		return this.codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	
	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPartIva() {
		return this.partIva;
	}

	public void setPartIva(String partIva) {
		this.partIva = partIva;
	}

}