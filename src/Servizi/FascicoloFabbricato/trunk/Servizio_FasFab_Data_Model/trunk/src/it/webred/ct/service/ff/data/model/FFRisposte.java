package it.webred.ct.service.ff.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_FF_RISPOSTE database table.
 * 
 */
@Entity
@Table(name="S_FF_RISPOSTE")
public class FFRisposte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_RIS")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="rispSeq" )
	@SequenceGenerator(name="rispSeq", sequenceName="S_FF_RISP_SEQ")	
	private Long idRis;

	@Column(name="COD_TIP_MEZ_RIS")
	private String codTipMezRis;

	@Column(name="DES_NOT_USER")
	private String desNotUser;

	@Column(name="DES_RIS")
	private String desRis;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_RIS")
	private Date dtRis;

	@Column(name="ID_RIC")
	private BigDecimal idRic;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="RESPINTO")
	private String respinto;
	
    public FFRisposte() {
    }

	public long getIdRis() {
		return this.idRis;
	}

	public void setIdRis(long idRis) {
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

	public BigDecimal getIdRic() {
		return this.idRic;
	}

	public void setIdRic(BigDecimal idRic) {
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