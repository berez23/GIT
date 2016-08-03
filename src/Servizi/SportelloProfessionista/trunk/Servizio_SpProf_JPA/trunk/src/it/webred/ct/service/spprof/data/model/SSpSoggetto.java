package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the S_SP_SOGGETTI database table.
 * 
 */
@Entity
@Table(name="S_SP_SOGGETTI")
public class SSpSoggetto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SOGG")
	private long idSogg;
	
	private String username;

	@Column(name="COD_COM_NAS")
	private String codComNas;

	@Column(name="COD_FIS")
	private String codFis;

	@Column(name="COD_TIP_SOGG")
	private String codTipSogg;

	private String cognome;

	@Column(name="DENOM_SOC")
	private String denomSoc;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_NAS")
	private Date dtNas;

	private String email;

	private String nome;

	@Column(name="PAR_IVA")
	private String parIva;

	private String telefono;

	@Column(name="ID_QUAL")
	private Long idQualifica;
	
	@Transient
	private String descrQualifica;

	public SSpSoggetto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getIdSogg() {
		return idSogg;
	}

	public void setIdSogg(long idSogg) {
		this.idSogg = idSogg;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCodComNas() {
		return codComNas;
	}

	public void setCodComNas(String codComNas) {
		this.codComNas = codComNas;
	}

	public String getCodFis() {
		return codFis;
	}

	public void setCodFis(String codFis) {
		this.codFis = codFis;
	}

	public String getCodTipSogg() {
		return codTipSogg;
	}

	public void setCodTipSogg(String codTipSogg) {
		this.codTipSogg = codTipSogg;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDenomSoc() {
		return denomSoc;
	}

	public void setDenomSoc(String denomSoc) {
		this.denomSoc = denomSoc;
	}

	public Date getDtNas() {
		return dtNas;
	}

	public void setDtNas(Date dtNas) {
		this.dtNas = dtNas;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getParIva() {
		return parIva;
	}

	public void setParIva(String parIva) {
		this.parIva = parIva;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Long getIdQualifica() {
		return idQualifica;
	}

	public void setIdQualifica(Long idQualifica) {
		this.idQualifica = idQualifica;
	}

	public String getDescrQualifica() {
		return descrQualifica;
	}

	public void setDescrQualifica(String descrQualifica) {
		this.descrQualifica = descrQualifica;
	}
	
}
