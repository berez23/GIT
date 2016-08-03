package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

public class TariffaDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private IsTariffa tariffa;
	private IsPeriodo periodo;
	private IsClasse classe;
	
	public IsTariffa getTariffa() {
		return tariffa;
	}
	public void setTariffa(IsTariffa tariffa) {
		this.tariffa = tariffa;
	}
	public IsPeriodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(IsPeriodo periodo) {
		this.periodo = periodo;
	}
	public IsClasse getClasse() {
		return classe;
	}
	public void setClasse(IsClasse classe) {
		this.classe = classe;
	}
}
