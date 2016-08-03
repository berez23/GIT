package it.webred.ct.service.ff.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the S_FF_SOGGETTI database table.
 * 
 */
@Entity
@Table(name="S_FF_SOGGETTI")
public class FFSoggetti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_SOGG")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="soggSeq" )
	@SequenceGenerator(name="soggSeq", sequenceName="S_FF_SOGG_SEQ")
	private Long idSogg;

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

	private String nome;

	@Column(name="PAR_IVA")
	private String parIva;

    public FFSoggetti() {
    }

	public Long getIdSogg() {
		return this.idSogg;
	}

	public void setIdSogg(Long idSogg) {
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

}