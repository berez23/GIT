package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DOCFA_DIAGNOSTICHE database table.
 * 
 */
@Entity
@Table(name="DOCFA_DIAGNOSTICHE")
public class DocfaResidenziale implements Serializable {


private static final long serialVersionUID = 1L;
	
	@Id
	@Temporal( TemporalType.DATE)
    private Date fornitura ;
    
	@Column(name="NREC_DATI_CENSUARI")
	private Long nRecDatiCensuari;
	
	@Column(name="RES_NO_RES")
	private Long resNoRes;
	
	@Column(name="NREC_DATAREG_NO_COER")
	private Long nRecDataregNoCoer;
	
	@Column(name="NREC_TIPO_OPER_NO_UNICO")
	private Long nRecTipoOperNoUnico;
	
	@Column(name="NREC_TIPO_OPER_NULL")
	private Long nRecTipoOperNull;
	
	@Column(name="NREC_TIPO_OPER_CESS")
	private Long nRecTipoOperCess;
	
	@Column(name="NREC_DOCFA_OK")
	private Long nRecDocfaOk;
	
	@Column(name="NREC_UIU")
	private Long nRecUiu;		 
	
	public DocfaResidenziale() {
		super();		
	}

	public Date getFornitura() {
		return fornitura;
	}

	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}

	public Long getnRecDatiCensuari() {
		return nRecDatiCensuari;
	}

	public void setnRecDatiCensuari(Long nRecDatiCensuari) {
		this.nRecDatiCensuari = nRecDatiCensuari;
	}

	public Long getResNoRes() {
		return resNoRes;
	}

	public void setResNoRes(Long resNoRes) {
		this.resNoRes = resNoRes;
	}

	public Long getnRecDataregNoCoer() {
		return nRecDataregNoCoer;
	}

	public void setnRecDataregNoCoer(Long nRecDataregNoCoer) {
		this.nRecDataregNoCoer = nRecDataregNoCoer;
	}

	public Long getnRecTipoOperNoUnico() {
		return nRecTipoOperNoUnico;
	}

	public void setnRecTipoOperNoUnico(Long nRecTipoOperNoUnico) {
		this.nRecTipoOperNoUnico = nRecTipoOperNoUnico;
	}

	public Long getnRecTipoOperNull() {
		return nRecTipoOperNull;
	}

	public void setnRecTipoOperNull(Long nRecTipoOperNull) {
		this.nRecTipoOperNull = nRecTipoOperNull;
	}

	public Long getnRecTipoOperCess() {
		return nRecTipoOperCess;
	}

	public void setnRecTipoOperCess(Long nRecTipoOperCess) {
		this.nRecTipoOperCess = nRecTipoOperCess;
	}

	public Long getnRecDocfaOk() {
		return nRecDocfaOk;
	}

	public void setnRecDocfaOk(Long nRecDocfaOk) {
		this.nRecDocfaOk = nRecDocfaOk;
	}

	public Long getnRecUiu() {
		return nRecUiu;
	}

	public void setnRecUiu(Long nRecUiu) {
		this.nRecUiu = nRecUiu;
	}	  
    
}
