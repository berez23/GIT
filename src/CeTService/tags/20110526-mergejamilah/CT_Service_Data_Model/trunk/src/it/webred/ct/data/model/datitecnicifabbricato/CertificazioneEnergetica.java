package it.webred.ct.data.model.datitecnicifabbricato;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The persistent class for the DATI_TEC_FABBR_CERT_ENER database table.
 * 
 */
@Entity
@Table(name="DATI_TEC_FABBR_CERT_ENER")
public class CertificazioneEnergetica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name="CF_PIVA_CERTIFICATORE")
	private String cfPivaCertificatore;

	@Column(name="CF_PIVA_PROPRIETARIO")
	private String cfPivaProprietario;

	@Column(name="CLASSE_ENERGETICA")
	private String classeEnergetica;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_PROT")
	private Date dataProt;
    
    @Transient
    private String dataProtStr;

	@Column(name="DENOM_CERTIFICATORE")
	private String denomCertificatore;

	@Column(name="DENOM_PROPRIETARIO")
	private String denomProprietario;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_SCA_VALIDITA")
	private Date dtScaValidita;
    
    @Transient
    private String dtScaValiditaStr;

	private BigDecimal emissioni;

	private String foglio;

	@Column(name="NUM_PROT")
	private BigDecimal numProt;

	private String particella;

	private String sezione;

	@Column(name="SUB_TUTTI")
	private String subTutti;

	@Column(name="UBIC_INTERNO")
	private String ubicInterno;

	@Column(name="UBIC_PIANO")
	private String ubicPiano;

	@Column(name="UBIC_SCALA")
	private String ubicScala;

	@Column(name="UBIC_SITICIVI_PKID_CIVI")
	private BigDecimal ubicSiticiviPkidCivi;

    public CertificazioneEnergetica() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCfPivaCertificatore() {
		return this.cfPivaCertificatore;
	}

	public void setCfPivaCertificatore(String cfPivaCertificatore) {
		this.cfPivaCertificatore = cfPivaCertificatore;
	}

	public String getCfPivaProprietario() {
		return this.cfPivaProprietario;
	}

	public void setCfPivaProprietario(String cfPivaProprietario) {
		this.cfPivaProprietario = cfPivaProprietario;
	}

	public String getClasseEnergetica() {
		return this.classeEnergetica;
	}

	public void setClasseEnergetica(String classeEnergetica) {
		this.classeEnergetica = classeEnergetica;
	}

	public Date getDataProt() {
		return this.dataProt;
	}

	public void setDataProt(Date dataProt) {
		this.dataProt = dataProt;
	}

	public String getDataProtStr() {
		
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
		return sdf.format(dataProt);		
	}

	public String getDenomCertificatore() {
		return this.denomCertificatore;
	}

	public void setDenomCertificatore(String denomCertificatore) {
		this.denomCertificatore = denomCertificatore;
	}

	public String getDenomProprietario() {
		return this.denomProprietario;
	}

	public void setDenomProprietario(String denomProprietario) {
		this.denomProprietario = denomProprietario;
	}

	public Date getDtScaValidita() {
		return this.dtScaValidita;
	}

	public void setDtScaValidita(Date dtScaValidita) {
		this.dtScaValidita = dtScaValidita;
	}

	public String getDtScaValiditaStr() {
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
		return sdf.format(dtScaValidita);
	}

	public BigDecimal getEmissioni() {
		return this.emissioni;
	}

	public void setEmissioni(BigDecimal emissioni) {
		this.emissioni = emissioni;
	}

	public String getFoglio() {
		return this.foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public BigDecimal getNumProt() {
		return this.numProt;
	}

	public void setNumProt(BigDecimal numProt) {
		this.numProt = numProt;
	}

	public String getParticella() {
		return this.particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSezione() {
		return this.sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getSubTutti() {
		return this.subTutti;
	}

	public void setSubTutti(String subTutti) {
		this.subTutti = subTutti;
	}

	public String getUbicInterno() {
		return this.ubicInterno;
	}

	public void setUbicInterno(String ubicInterno) {
		this.ubicInterno = ubicInterno;
	}

	public String getUbicPiano() {
		return this.ubicPiano;
	}

	public void setUbicPiano(String ubicPiano) {
		this.ubicPiano = ubicPiano;
	}

	public String getUbicScala() {
		return this.ubicScala;
	}

	public void setUbicScala(String ubicScala) {
		this.ubicScala = ubicScala;
	}

	public BigDecimal getUbicSiticiviPkidCivi() {
		return this.ubicSiticiviPkidCivi;
	}

	public void setUbicSiticiviPkidCivi(BigDecimal ubicSiticiviPkidCivi) {
		this.ubicSiticiviPkidCivi = ubicSiticiviPkidCivi;
	}

}