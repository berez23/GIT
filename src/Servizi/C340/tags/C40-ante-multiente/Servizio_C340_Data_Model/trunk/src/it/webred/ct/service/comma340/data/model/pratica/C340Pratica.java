package it.webred.ct.service.comma340.data.model.pratica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: C340Pratica
 *
 */
@MappedSuperclass
public class C340Pratica implements Serializable {

	
	private static final long serialVersionUID = 1L;	
	
	//Dati Pratica
	@Column(name="COMUNE_PRES_PRATICA")
	private String comunePresPratica;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DATA_PRES_PRATICA")
	private Date dataPresPratica;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DATA_ULT_MODIFICA")
	private Date dataUltimaModifica;
	
	//Dati soggetto Dichiarante
	
	@Column(name="COGNOME_DICH")
	private String cognomeDichiarante;
	
	@Column(name="NOME_DICH")
	private String nomeDichiarante;

	@Column(name="CF_DICH")
	private String cfDichiarante;
	
	@Column(name="COMUNE_DICH")
	private String comuneDichiarante;
	
	@Column(name="VIA_DICH")
	private String viaDichiarante;
	
	@Column(name="CIVICO_DICH")
	private String civicoDichiarante;
	
	@Column(name="TELEFONO_DICH")
	private String telefonoDichiarante;

	@Column(name="TITOLO_DICH")
	private String titoloDichiarante;
	
	//Dati Soggetto Giuridico
	@Column(name="DENOM_ENTE")
	private String denomEnte;

	@Column(name="PIVA_ENTE")
	private String pivaEnte;
	
	@Column(name="COMUNE_ENTE")
	private String comuneEnte;
	
	@Column(name="VIA_ENTE")
	private String viaEnte;

	@Column(name="CIVICO_ENTE")
	private String civicoEnte;
	
	@Column(name="TELEFONO_ENTE")
	private String telefonoEnte;
	
	@Column(name="TITOLO_ENTE")
	private String titoloEnte;
	
	//Dati UIU
	
	@Column(name="ID_UIU")
	private BigDecimal idUiu;
	
	@Column(name="COMUNE_UIU")
	private String comuneUiu;
	
	@Column(name="VIA_UIU")
	private String viaUiu;
	
	@Column(name="CIVICO_UIU")
	private String civicoUiu;
	
	@Column(name="COMUNE_NCEU")
	private String comuneNCEU;

	private BigDecimal foglio;

	private BigDecimal particella;

	private String sezione;

	private BigDecimal unimm;

	//Dati Richiesta da parte del Comune
	
	@Column(name="PROT_RICHIESTA")
	private String protRichiesta;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_RICHIESTA")
	private Date dataRichiesta;

	public String getComunePresPratica() {
		return comunePresPratica;
	}

	public void setComunePresPratica(String comunePresPratica) {
		this.comunePresPratica = comunePresPratica;
	}

	public Date getDataPresPratica() {
		return dataPresPratica;
	}

	public void setDataPresPratica(Date dataPresPratica) {
		this.dataPresPratica = dataPresPratica;
	}

	public Date getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public String getCognomeDichiarante() {
		return cognomeDichiarante;
	}

	public void setCognomeDichiarante(String cognomeDichiarante) {
		this.cognomeDichiarante = cognomeDichiarante;
	}

	public String getNomeDichiarante() {
		return nomeDichiarante;
	}

	public void setNomeDichiarante(String nomeDichiarante) {
		this.nomeDichiarante = nomeDichiarante;
	}

	public String getCfDichiarante() {
		return cfDichiarante;
	}

	public void setCfDichiarante(String cfDichiarante) {
		this.cfDichiarante = cfDichiarante;
	}

	public String getComuneDichiarante() {
		return comuneDichiarante;
	}

	public void setComuneDichiarante(String comuneDichiarante) {
		this.comuneDichiarante = comuneDichiarante;
	}

	public String getViaDichiarante() {
		return viaDichiarante;
	}

	public void setViaDichiarante(String viaDichiarante) {
		this.viaDichiarante = viaDichiarante;
	}

	public String getCivicoDichiarante() {
		return civicoDichiarante;
	}

	public void setCivicoDichiarante(String civicoDichiarante) {
		this.civicoDichiarante = civicoDichiarante;
	}

	public String getTelefonoDichiarante() {
		return telefonoDichiarante;
	}

	public void setTelefonoDichiarante(String telefonoDichiarante) {
		this.telefonoDichiarante = telefonoDichiarante;
	}

	public String getTitoloDichiarante() {
		return titoloDichiarante;
	}

	public void setTitoloDichiarante(String titoloDichiarante) {
		this.titoloDichiarante = titoloDichiarante;
	}

	public String getDenomEnte() {
		return denomEnte;
	}

	public void setDenomEnte(String denomEnte) {
		this.denomEnte = denomEnte;
	}

	public String getPivaEnte() {
		return pivaEnte;
	}

	public void setPivaEnte(String pivaEnte) {
		this.pivaEnte = pivaEnte;
	}

	public void setComuneEnte(String comuneEnte) {
		this.comuneEnte = comuneEnte;
	}

	public String getComuneEnte() {
		return comuneEnte;
	}

	public String getViaEnte() {
		return viaEnte;
	}

	public void setViaEnte(String viaEnte) {
		this.viaEnte = viaEnte;
	}

	public String getCivicoEnte() {
		return civicoEnte;
	}

	public void setCivicoEnte(String civicoEnte) {
		this.civicoEnte = civicoEnte;
	}

	public String getTelefonoEnte() {
		return telefonoEnte;
	}

	public void setTelefonoEnte(String telefonoEnte) {
		this.telefonoEnte = telefonoEnte;
	}

	public String getTitoloEnte() {
		return titoloEnte;
	}

	public void setTitoloEnte(String titoloEnte) {
		this.titoloEnte = titoloEnte;
	}

	public BigDecimal getIdUiu() {
		return idUiu;
	}

	public void setIdUiu(BigDecimal idUiu) {
		this.idUiu = idUiu;
	}

	public String getComuneUiu() {
		return comuneUiu;
	}

	public void setComuneUiu(String comuneUiu) {
		this.comuneUiu = comuneUiu;
	}

	public String getViaUiu() {
		return viaUiu;
	}

	public void setViaUiu(String viaUiu) {
		this.viaUiu = viaUiu;
	}

	public String getCivicoUiu() {
		return civicoUiu;
	}

	public void setCivicoUiu(String civicoUiu) {
		this.civicoUiu = civicoUiu;
	}

	public String getComuneNCEU() {
		return comuneNCEU;
	}

	public void setComuneNCEU(String comuneNCEU) {
		this.comuneNCEU = comuneNCEU;
	}

	public BigDecimal getFoglio() {
		return foglio;
	}

	public void setFoglio(BigDecimal foglio) {
		this.foglio = foglio;
	}

	public BigDecimal getParticella() {
		return particella;
	}

	public void setParticella(BigDecimal particella) {
		this.particella = particella;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public BigDecimal getUnimm() {
		return unimm;
	}

	public void setUnimm(BigDecimal unimm) {
		this.unimm = unimm;
	}

	public String getProtRichiesta() {
		return protRichiesta;
	}

	public void setProtRichiesta(String protRichiesta) {
		this.protRichiesta = protRichiesta;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public C340Pratica() {
		super();
	} 
	
   
}
