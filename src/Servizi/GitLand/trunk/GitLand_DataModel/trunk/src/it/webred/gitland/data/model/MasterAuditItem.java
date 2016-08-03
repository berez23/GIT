package it.webred.gitland.data.model;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.auth.callback.SecurityAssociationHandler;

@MappedSuperclass
public class MasterAuditItem extends MasterItem {
	
	
	@Column(name="UTENTE_CREAZIONE")
	String utenteCreazione ="";
	
	@Column(name="DATA_CREAZIONE")
	Date dataCreazione =null;
	
	@Column(name="UTENTE_MODIFICA")
	String utenteModifica ="";
	
	@Column(name="DATA_MODIFICA")
	Date dataModifica =null;
	
	@PrePersist
	void inserisciDatiAudit(){
		Principal per= SecurityContextAssociation.getPrincipal();
		utenteCreazione= per.getName();
		dataCreazione=new Date();
	}
	
	@PreUpdate
	void aggiornaDatiAudit(){
		Principal per= SecurityContextAssociation.getPrincipal();
		utenteModifica= per.getName();
		dataModifica=new Date();
	}
	
	public MasterAuditItem() {
	}//-------------------------------------------------------------------------

	public String getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public String getUtenteModifica() {
		return utenteModifica;
	}

	public void setUtenteModifica(String utenteModifica) {
		this.utenteModifica = utenteModifica;
	}

	public Date getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}
}
