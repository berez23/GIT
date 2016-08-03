package it.webred.ct.service.carContrib.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the S_CC_RISPOSTE database table.
 * 
 */
@Entity
@Table(name="S_CC_RISPOSTE")
public class Risposte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_RIS")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="rispSeq" )
	@SequenceGenerator(name="rispSeq", sequenceName="S_CC_RISP_SEQ")	
	private Long idRis;
	
	@Column(name="ID_RIC")
	private Long idRic;
	
	@Column(name="USER_NAME")
	private String userName;
	
    @Temporal( TemporalType.DATE)
	@Column(name="DT_RIS")
	private Date dtRis;

	@Column(name="COD_TIP_MEZ_RIS")
	private String codTipMezRis;
	
	@Column(name="DES_RIS")
	private String desRis;

	@Column(name="DES_NOT_USER")
	private String desNotUser;
	
	@Column(name="RESPINTO")
	private String respinto;

    public Risposte() {
    }

	public Long getIdRis() {
		return this.idRis;
	}

	public void setIdRis(Long idRis) {
		this.idRis = idRis;
	}

	public String getCodTipMezRis() {
		return this.codTipMezRis;
	}

	public void setCodTipMezRis(String codTipMezRis) {
		this.codTipMezRis = codTipMezRis;
	}

	public String getDesNotUser() {
		return this.desNotUser;
	}

	public void setDesNotUser(String desNotUser) {
		this.desNotUser = desNotUser;
	}

	public String getDesRis() {
		return this.desRis;
	}

	public void setDesRis(String desRis) {
		this.desRis = desRis;
	}

	public Date getDtRis() {
		return this.dtRis;
	}

	public void setDtRis(Date dtRis) {
		this.dtRis = dtRis;
	}

	public Long getIdRic() {
		return this.idRic;
	}

	public void setIdRic(Long idRic) {
		this.idRic = idRic;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRespinto() {
		return respinto;
	}
	public void setRespinto(String respinto) {
		this.respinto = respinto;
	}
}