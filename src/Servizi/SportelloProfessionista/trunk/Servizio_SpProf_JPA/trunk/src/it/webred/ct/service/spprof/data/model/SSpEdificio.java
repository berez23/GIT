package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_SP_EDIFICIO database table.
 * 
 */
@Entity
@Table(name="S_SP_EDIFICIO")
public class SSpEdificio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FK_SP_CEDIFICATO")
	private Long fkSpCedificato;

	@Column(name="CIVICO_BARRATO")
	private String civicoBarrato;

	@Column(name="CIVICO_NUMERO")
	private String civicoNumero;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_SP_DEST_URB")
	private String fkSpDestUrb;

	@Column(name="FK_SP_DEST_URB_PREVAL")
	private String fkSpDestUrbPreval;

	@Column(name="FK_SP_TIPOLOGIA_EDI")
	private String fkSpTipologiaEdi;

	@Column(name="USER_MOD")
	private String userMod;

	@Column(name="CIVICO_CODICE")
	private String civicoCodice;

	@Column(name="VIA_NOME")
	private String viaNome;

	@Column(name="VIA_PREFISSO")
	private String viaPrefisso;

    public SSpEdificio() {
    }

	public Long getFkSpCedificato() {
		return this.fkSpCedificato;
	}

	public void setFkSpCedificato(Long fkSpCedificato) {
		this.fkSpCedificato = fkSpCedificato;
	}

	public String getCivicoBarrato() {
		return this.civicoBarrato;
	}

	public void setCivicoBarrato(String civicoBarrato) {
		this.civicoBarrato = civicoBarrato;
	}

	public String getCivicoNumero() {
		return this.civicoNumero;
	}

	public void setCivicoNumero(String civicoNumero) {
		this.civicoNumero = civicoNumero;
	}

	public Date getDtIns() {
		return this.dtIns;
	}

	public void setDtIns(Date dtIns) {
		this.dtIns = dtIns;
	}

	public Date getDtMod() {
		return this.dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

	public String getFkSpDestUrb() {
		return this.fkSpDestUrb;
	}

	public void setFkSpDestUrb(String fkSpDestUrb) {
		this.fkSpDestUrb = fkSpDestUrb;
	}

	public String getFkSpDestUrbPreval() {
		return this.fkSpDestUrbPreval;
	}

	public void setFkSpDestUrbPreval(String fkSpDestUrbPreval) {
		this.fkSpDestUrbPreval = fkSpDestUrbPreval;
	}

	public String getFkSpTipologiaEdi() {
		return this.fkSpTipologiaEdi;
	}

	public void setFkSpTipologiaEdi(String fkSpTipologiaEdi) {
		this.fkSpTipologiaEdi = fkSpTipologiaEdi;
	}

	public String getUserMod() {
		return this.userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

	public String getCivicoCodice() {
		return civicoCodice;
	}

	public void setCivicoCodice(String civicoCodice) {
		this.civicoCodice = civicoCodice;
	}

	public String getViaNome() {
		return this.viaNome;
	}

	public void setViaNome(String viaNome) {
		this.viaNome = viaNome;
	}

	public String getViaPrefisso() {
		return this.viaPrefisso;
	}

	public void setViaPrefisso(String viaPrefisso) {
		this.viaPrefisso = viaPrefisso;
	}

}