package it.webred.ct.data.access.basic.locazioni.dto;

import it.webred.ct.data.model.locazioni.LocazioneBPK;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;

public class RicercaLocazioniDTO extends CeTBaseObject implements Serializable{
	private static final long serialVersionUID = 1L;
	LocazioneBPK key ;
	private String codFis;
	private Date dtRif;
	
	
	public String getCodFis() {
		return codFis;
	}
	public void setCodFis(String codFis) {
		this.codFis = codFis;
	}
	public Date getDtRif() {
		return dtRif;
	}
	public void setDtRif(Date dtRif) {
		this.dtRif = dtRif;
	}
	public LocazioneBPK getKey() {
		return key;
	}
	public void setKey(LocazioneBPK key) {
		this.key = key;
	}



}
