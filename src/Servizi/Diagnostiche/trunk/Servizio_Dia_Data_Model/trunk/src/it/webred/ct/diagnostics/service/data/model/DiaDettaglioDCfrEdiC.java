package it.webred.ct.diagnostics.service.data.model;

import it.webred.ct.diagnostics.service.data.SuperDia;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="DIA_DETTAGLIO_D_CFR_EDIC")
public class DiaDettaglioDCfrEdiC extends SuperDia implements Serializable {
	
	@Id
	@SequenceGenerator(name="SEQ_DIA_DETTAGLIO_D_CFR_EDIC_ID_GENERATOR", sequenceName="SEQ_DIA_DETTAGLIO_D_CFR_EDIC")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DIA_DETTAGLIO_D_CFR_EDIC_ID_GENERATOR")
	private Long id;
	
	//bi-directional many-to-one association to DiaTestata
    @ManyToOne
	@JoinColumn(name="FK_DIA_TESTATA")
	private DiaTestata diaTestata;
    
    @Column(name="ID_CONCESSIONE")
    private String idConcessione;
    
    @Column(name="PROTOCOLLO_DATA")
    private Date protocolloData;
    
    @Column(name="PROTOCOLLO_NUMERO")
    private String protocolloNumero;
    
    @Column(name="PROGRESSIVO_NUMERO")
    private String progressivoNumero;
    
    @Column(name="PROGRESSIVO_ANNO")
    private String progressivoAnno;
    
    @Column(name="tipo_intervento")
    private String tipoIntervento;
    
    private String oggetto;
    private String sezione;
    private String foglio;
    private String particella; 
    private String indirizzo;
    private String civico;
    
	public DiaDettaglioDCfrEdiC() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiaTestata getDiaTestata() {
		return diaTestata;
	}

	public void setDiaTestata(DiaTestata diaTestata) {
		this.diaTestata = diaTestata;
	}

	public String getIdConcessione() {
		return idConcessione;
	}

	public void setIdConcessione(String idConcessione) {
		this.idConcessione = idConcessione;
	}

	public Date getProtocolloData() {
		return protocolloData;
	}

	public void setProtocolloData(Date protocolloData) {
		this.protocolloData = protocolloData;
	}

	public String getProtocolloNumero() {
		return protocolloNumero;
	}

	public void setProtocolloNumero(String protocolloNumero) {
		this.protocolloNumero = protocolloNumero;
	}

	public String getProgressivoNumero() {
		return progressivoNumero;
	}

	public void setProgressivoNumero(String progressivoNumero) {
		this.progressivoNumero = progressivoNumero;
	}

	public String getProgressivoAnno() {
		return progressivoAnno;
	}

	public void setProgressivoAnno(String progressivoAnno) {
		this.progressivoAnno = progressivoAnno;
	}

	public String getTipoIntervento() {
		return tipoIntervento;
	}

	public void setTipoIntervento(String tipoIntervento) {
		this.tipoIntervento = tipoIntervento;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}


}
