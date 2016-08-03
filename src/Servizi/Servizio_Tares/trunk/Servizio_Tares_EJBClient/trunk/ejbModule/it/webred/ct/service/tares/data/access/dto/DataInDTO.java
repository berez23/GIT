package it.webred.ct.service.tares.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DataInDTO extends CeTBaseObject implements Serializable{

	private static final long serialVersionUID = -3472556246576373621L;
	
	private ValutazioniSearchCriteria criteria;
	private StatisticheSearchCriteria criteriaStat;
	private TariffeSearchCriteria criteriaCoeff;
	private DiagnosticheSearchCriteria criteriaDiag;
	private SegnalazioniSearchCriteria criteriaSegnalazioni;
	private String nome;
	private Object obj = null;
	

	public ValutazioniSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(ValutazioniSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public StatisticheSearchCriteria getCriteriaStat() {
		return criteriaStat;
	}

	public void setCriteriaStat(StatisticheSearchCriteria criteriaStat) {
		this.criteriaStat = criteriaStat;
	}

	public TariffeSearchCriteria getCriteriaCoeff() {
		return criteriaCoeff;
	}

	public void setCriteriaCoeff(TariffeSearchCriteria criteriaCoeff) {
		this.criteriaCoeff = criteriaCoeff;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public DiagnosticheSearchCriteria getCriteriaDiag() {
		return criteriaDiag;
	}

	public void setCriteriaDiag(DiagnosticheSearchCriteria criteriaDiag) {
		this.criteriaDiag = criteriaDiag;
	}

	public SegnalazioniSearchCriteria getCriteriaSegnalazioni() {
		return criteriaSegnalazioni;
	}

	public void setCriteriaSegnalazioni(
			SegnalazioniSearchCriteria criteriaSegnalazioni) {
		this.criteriaSegnalazioni = criteriaSegnalazioni;
	}

	
}
