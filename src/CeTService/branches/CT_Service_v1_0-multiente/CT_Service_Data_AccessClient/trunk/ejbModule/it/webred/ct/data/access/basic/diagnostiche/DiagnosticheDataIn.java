package it.webred.ct.data.access.basic.diagnostiche;

import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaIciTarsuDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DiagnosticheDataIn extends CeTBaseObject implements Serializable{

	private Object obj;
	private Object obj2;
	private RicercaDocfaReportDTO ricerca;
	private RicercaIciTarsuDTO ricercaIciTarsuDto;
	private RicercaOggettoDocfaDTO ricercaOggettoDocfaDTO;
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Object getObj2() {
		return obj2;
	}

	public void setObj2(Object obj2) {
		this.obj2 = obj2;
	}

	public RicercaDocfaReportDTO getRicerca() {
		return ricerca;
	}

	public void setRicerca(RicercaDocfaReportDTO ricerca) {
		this.ricerca = ricerca;
	}

	public RicercaIciTarsuDTO getRicercaIciTarsuDto() {
		return ricercaIciTarsuDto;
	}

	public void setRicercaIciTarsuDto(RicercaIciTarsuDTO ricercaIciTarsuDto) {
		this.ricercaIciTarsuDto = ricercaIciTarsuDto;
	}
	
}
