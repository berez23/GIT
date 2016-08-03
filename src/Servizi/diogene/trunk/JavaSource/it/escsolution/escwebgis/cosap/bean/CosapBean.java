package it.escsolution.escwebgis.cosap.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Vector;

public class CosapBean extends EscObject implements Serializable {
	
	private ElementoLista datiContribuente;
	private Vector<Autorizzazione> autorizazioni = new Vector<Autorizzazione>();
	private Vector<Diffide> diffide = new Vector<Diffide>();
	private Vector<OccAbu> occupazioniAbusive = new Vector<OccAbu>();
	private Vector<InvitoPagamento> invitiPagamento = new Vector<InvitoPagamento>();
	private Vector<Verbali> verbali = new Vector<Verbali>();
	private Vector<Versamenti> versamenti = new Vector<Versamenti>(); 
	

	public Vector<Versamenti> getVersamenti() {
		return versamenti;
	}	
	public void addVersamenti(Versamenti v) {
		versamenti.add(v);
	}
	public Vector<Verbali> getVerbali() {
		return verbali;
	}
	public void addVerbali(Verbali v) {
		verbali.add(v);
	}
	public void setAutorizazioni(Vector<Autorizzazione> autorizazioni) {
		this.autorizazioni = autorizazioni;
	}
	public ElementoLista getDatiContribuente() {
		return datiContribuente;
	}
	public void setDatiContribuente(ElementoLista datiContribuente) {
		this.datiContribuente = datiContribuente;
	}
	public Vector getAutorizazioni() {
		return autorizazioni;
	}

	
	public void addAutorizzazione(Autorizzazione a) {
		autorizazioni.add(a);
	}
	

	public Vector getDiffide() {
		return diffide;
	}

	
	
	public void addDiffide(Diffide d) {
		diffide.add(d);
	}
	
	
	public Vector<OccAbu> getOccupazioniAbusive() {
		return occupazioniAbusive;
	}
	
	public void addOccupazioniAbusive(OccAbu o) {
		occupazioniAbusive.add(o);
	}

	public Vector<InvitoPagamento> getInvitiPagamento() {
		return invitiPagamento;
	}
	public void addInvitiPagamento(InvitoPagamento i) {
		invitiPagamento.add(i);
	}
	
	
	public String getChiave() {
		return datiContribuente.getChiave();
	}

}
