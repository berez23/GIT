package it.webred.ct.service.gestprep.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the S_SP_SOGGETTI database table.
 * 
 */
@Entity
@Table(name="S_SP_SOGGETTI")
public class GestPrepSoggetti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SOGG")
	private long idSogg;

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

    public GestPrepSoggetti() {
    }

	public long getIdSogg() {
		return this.idSogg;
	}

	public void setIdSogg(long idSogg) {
		this.idSogg = idSogg;
	}

	public String getCodComNas() {
		return this.codComNas;
	}

	public void setCodComNas(String codComNas) {
		this.codComNas = codComNas;
	}

	public String getCodFis() {
		return this.codFis;
	}

	public void setCodFis(String codFis) {
		this.codFis = codFis;
	}

	public String getCodTipSogg() {
		return this.codTipSogg;
	}

	public void setCodTipSogg(String codTipSogg) {
		this.codTipSogg = codTipSogg;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDenomSoc() {
		return this.denomSoc;
	}

	public void setDenomSoc(String denomSoc) {
		this.denomSoc = denomSoc;
	}

	public Date getDtNas() {
		return this.dtNas;
	}

	public void setDtNas(Date dtNas) {
		this.dtNas = dtNas;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getParIva() {
		return this.parIva;
	}

	public void setParIva(String parIva) {
		this.parIva = parIva;
	}

	public String getTelefono() {
		return this.telefono;
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