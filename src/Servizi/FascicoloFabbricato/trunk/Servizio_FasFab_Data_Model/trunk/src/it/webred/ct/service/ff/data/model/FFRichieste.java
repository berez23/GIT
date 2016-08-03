package it.webred.ct.service.ff.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The persistent class for the S_FF_RICHIESTE database table.
 * 
 */
@Entity
@Table(name="S_FF_RICHIESTE")
public class FFRichieste implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_RIC")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="richSeq" )
	@SequenceGenerator(name="richSeq", sequenceName="S_FF_RICH_SEQ")
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


	@Column(name="E_MAIL")
	private String eMail;

	private String foglio;

	@Column(name="ID_SOGG_RIC")
	private Long idSoggRic;

	@Column(name="NOME_PDF")
	private String nomePdf;

	@Column(name="NUM_DOC_RICON")
	private String numDocRicon;

	@Column(name="NUM_PROT")
	private BigDecimal numProt;

	@Column(name="NUM_TEL")
	private String numTel;

	private String particella;

	private String sezione;

	@Column(name="USER_NAME_RIC")
	private String userNameRic;
	
    @Temporal( TemporalType.DATE)
	@Column(name="DT_RIF")
	private Date dtRif;
	
    @Temporal( TemporalType.DATE)
	@Column(name="DT_RIC")
	private Date dtRic;
    
    
    @Transient
    private String strDtRic;
    @Transient
    private String strDtRif;
    
    

    public FFRichieste() {
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

	public String getFoglio() {
		return this.foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public Long getIdSoggRic() {
		return this.idSoggRic;
	}

	public void setIdSoggRic(Long idSoggRic) {
		this.idSoggRic = idSoggRic;
	}

	public String getNomePdf() {
		return this.nomePdf;
	}

	public void setNomePdf(String nomePdf) {
		this.nomePdf = nomePdf;
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

	public String getNumTel() {
		return this.numTel;
	}

	public void setNumTel(String numTel) {
		this.numTel = numTel;
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

	public String getUserNameRic() {
		return this.userNameRic;
	}

	public void setUserNameRic(String userNameRic) {
		this.userNameRic = userNameRic;
	}

	public Date getDtRif() {
		return dtRif;
	}

	public void setDtRif(Date dtRif) {
		this.dtRif = dtRif;
	}

	public String getStrDtRic() {
		
		String strDtRic="";
		DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		
		if (dtRic != null){
			strDtRic= df.format(dtRic);
		}
				
		return strDtRic;
	}

	public void setStrDtRic(String strDtRic) {
		this.strDtRic = strDtRic;
	}

	public String getStrDtRif() {
		
		String strDtRif="";
		DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		
		if (dtRif != null){
			strDtRif= df.format(dtRif);
		}
		
		return strDtRif;
	}

	public void setStrDtRif(String strDtRif) {
		this.strDtRif = strDtRif;
	}
	
	

}