package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the DIA_DETTAGLIO_D_CFR_ECOG1_V1 database table.
 * 
 */
@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_ECOG2")
public class DiaDettaglioDCfrEcog2 extends SuperDia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="DIA_DETTAGLIO_D_CFR_ECOG2_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_ECOG2")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIA_DETTAGLIO_D_CFR_ECOG2_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    @Column(name="COM_ID_STRA")   
    private String comIdStra;
    
    @Column(name="COM_ID_CIVICO")   
    private String comIdCivico;
    
    @Column(name="COM_SEDIME")    
    private String comSedime;
    
    @Column(name="COM_INDIRIZZO")
    private String comIndirizzo;
    
    @Column(name="COM_CIVICO")
    private String comCivico;
    
    @Column(name="COM_BARRATO")
    private String comBarrato;
    
    @Column(name="COM_DESCR_CIVICO")
    private String comDescrCivico;
    
    @Column(name="ECO_ID_STRA")   
    private String ecoIdStra;
    
    @Column(name="ECO_ID_CIVICO")   
    private String ecoIdCivico;
    
    @Column(name="ECO_SEDIME")    
    private String ecoSedime;
    
    @Column(name="ECO_INDIRIZZO")
    private String ecoIndirizzo;
    
    @Column(name="ECO_CIVICO")
    private String ecoCivico;
    
    @Column(name="ECO_BARRATO")
    private String ecoBarrato;
    
    @Column(name="ECO_DESCR_CIVICO")
    private String ecoDescrCivico;
    
    @Column(name="DISTANZA")
    private BigDecimal distanza;
   

	public DiaDettaglioDCfrEcog2() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}


	public DiaTestata getDiaTestata() {
		return diaTestata;
	}


	public String getComIdStra() {
		return comIdStra;
	}


	public String getComIdCivico() {
		return comIdCivico;
	}


	public String getComSedime() {
		return comSedime;
	}


	public String getComIndirizzo() {
		return comIndirizzo;
	}


	public String getComCivico() {
		return comCivico;
	}


	public String getComBarrato() {
		return comBarrato;
	}


	public String getComDescrCivico() {
		return comDescrCivico;
	}


	public String getEcoIdStra() {
		return ecoIdStra;
	}


	public String getEcoIdCivico() {
		return ecoIdCivico;
	}


	public String getEcoSedime() {
		return ecoSedime;
	}


	public String getEcoIndirizzo() {
		return ecoIndirizzo;
	}


	public String getEcoCivico() {
		return ecoCivico;
	}


	public String getEcoBarrato() {
		return ecoBarrato;
	}


	public String getEcoDescrCivico() {
		return ecoDescrCivico;
	}


	public BigDecimal getDistanza() {
		return distanza;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}


	public void setComIdStra(String comIdStra) {
		this.comIdStra = comIdStra;
	}


	public void setComIdCivico(String comIdCivico) {
		this.comIdCivico = comIdCivico;
	}


	public void setComSedime(String comSedime) {
		this.comSedime = comSedime;
	}


	public void setComIndirizzo(String comIndirizzo) {
		this.comIndirizzo = comIndirizzo;
	}


	public void setComCivico(String comCivico) {
		this.comCivico = comCivico;
	}


	public void setComBarrato(String comBarrato) {
		this.comBarrato = comBarrato;
	}


	public void setComDescrCivico(String comDescrCivico) {
		this.comDescrCivico = comDescrCivico;
	}


	public void setEcoIdStra(String ecoIdStra) {
		this.ecoIdStra = ecoIdStra;
	}


	public void setEcoIdCivico(String ecoIdCivico) {
		this.ecoIdCivico = ecoIdCivico;
	}


	public void setEcoSedime(String ecoSedime) {
		this.ecoSedime = ecoSedime;
	}


	public void setEcoIndirizzo(String ecoIndirizzo) {
		this.ecoIndirizzo = ecoIndirizzo;
	}


	public void setEcoCivico(String ecoCivico) {
		this.ecoCivico = ecoCivico;
	}


	public void setEcoBarrato(String ecoBarrato) {
		this.ecoBarrato = ecoBarrato;
	}


	public void setEcoDescrCivico(String ecoDescrCivico) {
		this.ecoDescrCivico = ecoDescrCivico;
	}


	public void setDistanza(BigDecimal distanza) {
		this.distanza = distanza;
	}

}
