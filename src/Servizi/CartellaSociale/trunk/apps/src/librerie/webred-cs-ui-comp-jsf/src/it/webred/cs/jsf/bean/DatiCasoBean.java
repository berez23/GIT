package it.webred.cs.jsf.bean;

import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.jsf.manbean.IterInfoStatoMan;


public class DatiCasoBean {
	
	private CsASoggetto soggetto;
	private CsOOperatore operatore;
	private AmAnagrafica operatoreAnagrafica;
	private IterInfoStatoMan lastIterStepInfo;
	private String nInterventi;
	private String catSociale;
	
	public DatiCasoBean(){
	}
	
	public DatiCasoBean(CsASoggetto soggetto, CsOOperatore operatore, AmAnagrafica operatoreAnagrafica, IterInfoStatoMan lastIterStepInfo){
		this.soggetto = soggetto;
		this.operatore = operatore;
		this.operatoreAnagrafica = operatoreAnagrafica;
		this.lastIterStepInfo = lastIterStepInfo;
	}
	
	public CsASoggetto getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(CsASoggetto soggetto) {
		this.soggetto = soggetto;
	}
	public CsOOperatore getOperatore() {
		return operatore;
	}
	public void setOperatore(CsOOperatore operatore) {
		this.operatore = operatore;
	}

	public IterInfoStatoMan getLastIterStepInfo() {
		return lastIterStepInfo;
	}

	public void setLastIterStepInfo(IterInfoStatoMan lastIterStepInfo) {
		this.lastIterStepInfo = lastIterStepInfo;
	}

	public AmAnagrafica getOperatoreAnagrafica() {
		return operatoreAnagrafica;
	}

	public void setOperatoreAnagrafica(AmAnagrafica operatoreAnagrafica) {
		this.operatoreAnagrafica = operatoreAnagrafica;
	}

	public String getnInterventi() {
		return nInterventi;
	}

	public void setnInterventi(String nInterventi) {
		this.nInterventi = nInterventi;
	}

	public String getCatSociale() {
		return catSociale;
	}

	public void setCatSociale(String catSociale) {
		this.catSociale = catSociale;
	}

}
