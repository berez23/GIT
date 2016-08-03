package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class BodConfrontoBean extends MasterItem {

	private static final long serialVersionUID = -8240478396425449348L;
	
	private Object[] segnalazioniObjs = null;
	private Object[] datiCensuariObjs = null;
	private BodEsitoBean esito = null;
	private Object[] catastoObjs = null;
	private Boolean confronto = false;

	public BodConfrontoBean() {

	}//-------------------------------------------------------------------------

	public Object[] getSegnalazioniObjs() {
		return segnalazioniObjs;
	}

	public void setSegnalazioniObjs(Object[] segnalazioniObjs) {
		this.segnalazioniObjs = segnalazioniObjs;
	}

	public Object[] getDatiCensuariObjs() {
		return datiCensuariObjs;
	}

	public void setDatiCensuariObjs(Object[] datiCensuariObjs) {
		this.datiCensuariObjs = datiCensuariObjs;
	}

	public BodEsitoBean getEsito() {
		return esito;
	}

	public void setEsito(BodEsitoBean esito) {
		this.esito = esito;
	}

	public Object[] getCatastoObjs() {
		return catastoObjs;
	}

	public void setCatastoObjs(Object[] catastoObjs) {
		this.catastoObjs = catastoObjs;
	}

	public Boolean getConfronto() {
		return confronto;
	}

	public void setConfronto(Boolean confronto) {
		this.confronto = confronto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
