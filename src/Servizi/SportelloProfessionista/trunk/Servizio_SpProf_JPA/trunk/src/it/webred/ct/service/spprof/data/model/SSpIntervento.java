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




/**
 * The persistent class for the S_SP_INTERVENTO database table.
 * 
 */
@Entity
@Table(name="S_SP_INTERVENTO")
public class SSpIntervento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SP_INTERVENTO")
	private Long idSpIntervento;
	
	@Column(name="N_POSTI_AUTO")
	private Long nPostiAuto;
	
	@Column(name="N_BOX_AUTO")
	private Long nBoxAuto;
	
	@Column(name="N_PASSI_CARRAI_PREV")
	private Long nPassiCarraiPrev;
	
	@Column(name="N_ACCESSI_CARRAI_PREV")
	private Long nAccessiCarraiPrev;
	
	@Column(name="C_CONCESSIONE_NUMERO")
	private String cConcessioneNumero;
	
	@Column(name="C_PROGRESSIVO_NUMERO")
	private String cProgressivoNumero;
	
	@Column(name="C_PROGRESSIVO_ANNO")
	private String cProgressivoAnno;
	
	@Temporal( TemporalType.DATE)
	@Column(name="C_PROTOCOLLO_DATA")
	private Date cProtocolloData;
	
	@Column(name="C_PROTOCOLLO_NUMERO")
	private String cProtocolloNumero;
	
	@Column(name="FK_SOGG")
	private Long fkSogg;
	
	private String note;
	
	@Column(name="USER_INS")
	private String userIns;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;
	
	@Column(name="USER_MOD")
	private String userMod;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;
	
	private String stato;

	public SSpIntervento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getIdSpIntervento() {
		return idSpIntervento;
	}

	public void setIdSpIntervento(Long idSpIntervento) {
		this.idSpIntervento = idSpIntervento;
	}

	public Long getnPostiAuto() {
		return nPostiAuto;
	}

	public void setnPostiAuto(Long nPostiAuto) {
		this.nPostiAuto = nPostiAuto;
	}

	public Long getnBoxAuto() {
		return nBoxAuto;
	}

	public void setnBoxAuto(Long nBoxAuto) {
		this.nBoxAuto = nBoxAuto;
	}

	public Long getnPassiCarraiPrev() {
		return nPassiCarraiPrev;
	}

	public void setnPassiCarraiPrev(Long nPassiCarraiPrev) {
		this.nPassiCarraiPrev = nPassiCarraiPrev;
	}

	public Long getnAccessiCarraiPrev() {
		return nAccessiCarraiPrev;
	}

	public void setnAccessiCarraiPrev(Long nAccessiCarraiPrev) {
		this.nAccessiCarraiPrev = nAccessiCarraiPrev;
	}

	public String getcConcessioneNumero() {
		return cConcessioneNumero;
	}

	public void setcConcessioneNumero(String cConcessioneNumero) {
		this.cConcessioneNumero = cConcessioneNumero;
	}

	public String getcProgressivoNumero() {
		return cProgressivoNumero;
	}

	public void setcProgressivoNumero(String cProgressivoNumero) {
		this.cProgressivoNumero = cProgressivoNumero;
	}

	public String getcProgressivoAnno() {
		return cProgressivoAnno;
	}

	public void setcProgressivoAnno(String cProgressivoAnno) {
		this.cProgressivoAnno = cProgressivoAnno;
	}

	public Date getcProtocolloData() {
		return cProtocolloData;
	}

	public void setcProtocolloData(Date cProtocolloData) {
		this.cProtocolloData = cProtocolloData;
	}

	public String getcProtocolloNumero() {
		return cProtocolloNumero;
	}

	public void setcProtocolloNumero(String cProtocolloNumero) {
		this.cProtocolloNumero = cProtocolloNumero;
	}

	public Long getFkSogg() {
		return fkSogg;
	}

	public void setFkSogg(Long fkSogg) {
		this.fkSogg = fkSogg;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUserIns() {
		return userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public Date getDtIns() {
		return dtIns;
	}

	public void setDtIns(Date dtIns) {
		this.dtIns = dtIns;
	}

	public String getUserMod() {
		return userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

	public Date getDtMod() {
		return dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	
	
	
}
