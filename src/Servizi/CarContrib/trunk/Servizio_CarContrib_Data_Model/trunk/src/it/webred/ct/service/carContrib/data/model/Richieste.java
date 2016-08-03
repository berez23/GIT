package it.webred.ct.service.carContrib.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_CC_RICHIESTE database table.
 * 
 */
@Entity
@Table(name="S_CC_RICHIESTE")
public class Richieste implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_RIC")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="richSeq" )
	@SequenceGenerator(name="richSeq", sequenceName="S_CC_RICH_SEQ")
	private Long idRic;

	@Column(name="COD_TIP_DOC_RICON")
	private String codTipDocRicon;

	@Column(name="COD_TIP_PROVEN")
	private String codTipProven;

	@Column(name="COD_TIP_RIC")
	private String codTipRic;

	@Column(name="DES_ALTRO_TIPO")
	private String desAltroTipo;

	@Column(name="DES_NOT_RIC")
	private String desNotRic;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_EMI_DOC_RICON")
	private Date dtEmiDocRicon;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_PROT")
	private Date dtProt;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="DT_RIC")
	private Date dtRic;

	@Column(name="E_MAIL")
	private String eMail;

	@Column(name="ID_SOGG_CAR")
	private Long idSoggCar;
	
	@Column(name="USER_NAME_RIC")
	private String userNameRic;

	@Column(name="ID_SOGG_RIC")
	private Long idSoggRic;

	@Column(name="NUM_DOC_RICON")
	private String numDocRicon;

	@Column(name="NUM_PROT")
	private BigDecimal numProt;

	// Nome file pdf creato senza estensione
	@Column(name="NOME_PDF")
	private String nomePdf;

	@Column(name="NUM_TEL")
	private String numTel;
	
	@Column(name="COD_TIP_MEZ_RIS")
	private String codTipMezRis;
	
	@Column(name="FLG_STORICO")
	private Integer flgStorico;
	
    public Richieste() {
    }

	public Long getIdRic() {
		return this.idRic;
	}

	public void setIdRic(Long idRic) {
		this.idRic = idRic;
	}

	public String getCodTipDocRicon() {
		return this.codTipDocRicon;
	}

	public void setCodTipDocRicon(String codTipDocRicon) {
		this.codTipDocRicon = codTipDocRicon;
	}

	public String getCodTipProven() {
		return this.codTipProven;
	}

	public void setCodTipProven(String codTipProven) {
		this.codTipProven = codTipProven;
	}

	public String getCodTipRic() {
		return this.codTipRic;
	}

	public void setCodTipRic(String codTipRic) {
		this.codTipRic = codTipRic;
	}

	public String getDesAltroTipo() {
		return this.desAltroTipo;
	}

	public void setDesAltroTipo(String desAltroTipo) {
		this.desAltroTipo = desAltroTipo;
	}

	public String getDesNotRic() {
		return this.desNotRic;
	}

	public void setDesNotRic(String desNotRic) {
		this.desNotRic = desNotRic;
	}

	public Date getDtEmiDocRicon() {
		return this.dtEmiDocRicon;
	}

	public void setDtEmiDocRicon(Date dtEmiDocRicon) {
		this.dtEmiDocRicon = dtEmiDocRicon;
	}

	public Date getDtProt() {
		return this.dtProt;
	}

	public void setDtProt(Date dtProt) {
		this.dtProt = dtProt;
	}

	public Date getDtRic() {
		return this.dtRic;
	}

	public void setDtRic(Date dtRic) {
		this.dtRic = dtRic;
	}

	public String getEMail() {
		return this.eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public Long getIdSoggCar() {
		return this.idSoggCar;
	}

	public void setIdSoggCar(Long idSoggCar) {
		this.idSoggCar = idSoggCar;
	}

	public Long getIdSoggRic() {
		return this.idSoggRic;
	}

	public void setIdSoggRic(Long idSoggRic) {
		this.idSoggRic = idSoggRic;
	}

	public String getNumDocRicon() {
		return this.numDocRicon;
	}

	public void setNumDocRicon(String numDocRicon) {
		this.numDocRicon = numDocRicon;
	}

	public BigDecimal getNumProt() {
		return this.numProt;
	}

	public void setNumProt(BigDecimal numProt) {
		this.numProt = numProt;
	}
	/*
	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	*/
	public String getUserNameRic() {
		return userNameRic;
	}

	public void setUserNameRic(String userNameRic) {
		this.userNameRic = userNameRic;
	}

	public void setNomePdf(String nomePdf) {
		this.nomePdf = nomePdf;
	}

	public String getNomePdf() {
		return nomePdf;
	}

	public String getNumTel() {
		return numTel;
	}

	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}
	
	public String getCodTipMezRis() {
		return this.codTipMezRis;
	}

	public void setCodTipMezRis(String codTipMezRis) {
		this.codTipMezRis = codTipMezRis;
	}

	public Integer getFlgStorico() {
		return flgStorico;
	}

	public void setFlgStorico(Integer flgStorico) {
		this.flgStorico = flgStorico;
	}
}