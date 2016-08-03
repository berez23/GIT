package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CTR_ICI01 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CTR_ICI03")
public class DiaDettaglioDCtrIci03 extends SuperDia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CTR_ICI03_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CTR_ICI")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CTR_ICI03_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
        	
	private String denominazione;
	private String cognome;
	private String nome;
	
	@Column(name="COD_FISC")
	private String codFisc;
	
	@Column(name="PART_IVA")
	private String partiva;
	
	private String cuaa;
	
	  
    public DiaDettaglioDCtrIci03() {
    }

   
	public String getDenominazione() {
		return denominazione;
	}


	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getPartiva() {
		return partiva;
	}


	public void setPartiva(String partiva) {
		this.partiva = partiva;
	}


	public String getCuaa() {
		return cuaa;
	}


	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public DiaTestata getDiaTestata() {
		return this.diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}


	public String getCodFisc() {
		return codFisc;
	}


	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}
	
}