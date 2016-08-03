package it.webred.ct.data.access.basic.segnalazionequalificata.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import it.webred.ct.data.model.segnalazionequalificata.*;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class PraticaSegnalazioneDTO extends CeTBaseObject{
	private static final long serialVersionUID = 1L;
	
	private SOfPratica pratica;
	private List<String> fonti;
	private List<SOfAmbitoAccertamento> ambitiAccertamento;
	private List<SOfPratAllegato> allegati;
	private String descAccComuNasc;
	private String accStatoDecoded;
	private String accFinalitaDecoded;

	
	
	public PraticaSegnalazioneDTO(){
	}

	public SOfPratica getPratica() {
		return pratica;
	}

	public void setPratica(SOfPratica pratica) {
		this.pratica = pratica;
	}

	public List<String> getFonti() {
		return fonti;
	}

	public void setFonti(List<String> fonti) {
		this.fonti = fonti;
	}

	public List<SOfAmbitoAccertamento> getAmbitiAccertamento() {
		return ambitiAccertamento;
	}

	public void setAmbitiAccertamento(List<SOfAmbitoAccertamento> ambitiAccertamento) {
		this.ambitiAccertamento = ambitiAccertamento;
	}

	public List<SOfPratAllegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<SOfPratAllegato> allegati) {
		this.allegati = allegati;
	}

	public void setDescAccComuNasc(String descAccComuNasc) {
		this.descAccComuNasc = descAccComuNasc;
	}

	public String getDescAccComuNasc() {
		return descAccComuNasc;
	}

	public String getAccStatoDecoded(){
		return getAccStatoDecoded(pratica.getAccStato());
	}
	
	public String getAccStatoDecoded(String stato){
		String desc = "";
		if("0".equalsIgnoreCase(stato))
			desc = "Istruttoria Comunale in corso";
		else if("1".equalsIgnoreCase(stato))
			desc = "Segnalazione in corso presso Agenzia delle Entrate";
		else if ("2".equalsIgnoreCase(stato))
			desc = "Iter concluso";
		return desc;
	}
	
	public String getAccFinalitaDecoded(){
		return getAccFinalitaDecoded(pratica.getAccFinalita());
	}
	
	public String getAccFinalitaDecoded(String f){
		String desc = "";
		if("TFL".equalsIgnoreCase(f))
			desc = "Tributi fiscali locali";
		else if("TFE".equalsIgnoreCase(f))
			desc = "Tributi fiscali erariali";
		return desc;
	}
	

}
