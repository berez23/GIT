package it.webred.ct.data.access.basic.catasto.dto;

import it.webred.ct.data.model.docfa.*;
import it.webred.ct.data.model.catasto.*;
import it.webred.ct.data.access.basic.catasto.dto.*;

import java.io.Serializable;
import java.util.*;

public class DettaglioCatastaleImmobileDTO implements Serializable {
	
	private String sezione;
	private String descrizioneCategoria;
	private Sitiuiu immobile;
	private List<DataVariazioneImmobileDTO> altreDateValidita;
	private List<SoggettoDTO> soggetti;
	private List<IndirizzoDTO> localizzazioneSIT;
	private List<IndirizzoDTO> localizzazioneCatasto;
	private List<SitiediVani> dettaglioVaniComma340;
	private List<PlanimetriaComma340DTO> planimetrieComma340;
	private boolean nonANorma;    //Secondo le elaborazioni del comma340
	
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	
	public void setDescrizioneCategoria(String descrizioneCategoria) {
		this.descrizioneCategoria = descrizioneCategoria;
	}
	public String getDescrizioneCategoria() {
		return descrizioneCategoria;
	}
	public List<SoggettoDTO> getSoggetti() {
		return soggetti;
	}
	public List<DataVariazioneImmobileDTO> getAltreDateValidita() {
		return altreDateValidita;
	}
	public void setAltreDateValidita(
			List<DataVariazioneImmobileDTO> altreDateValidita) {
		this.altreDateValidita = altreDateValidita;
	}
	public void setSoggetti(List<SoggettoDTO> soggetti) {
		this.soggetti = soggetti;
	}
	
	public List<SitiediVani> getDettaglioVaniComma340() {
		return dettaglioVaniComma340;
	}
	public void setDettaglioVaniComma340(List<SitiediVani> dettaglioVaniComma340) {
		this.dettaglioVaniComma340 = dettaglioVaniComma340;
	}
	
	public Sitiuiu getImmobile() {
		return immobile;
	}
	public void setImmobile(Sitiuiu immobile) {
		this.immobile = immobile;
	}
	public List<IndirizzoDTO> getLocalizzazioneSIT() {
		return localizzazioneSIT;
	}
	public void setLocalizzazioneSIT(List<IndirizzoDTO> localizzazioneSIT) {
		this.localizzazioneSIT = localizzazioneSIT;
	}
	public List<IndirizzoDTO> getLocalizzazioneCatasto() {
		return localizzazioneCatasto;
	}
	public void setLocalizzazioneCatasto(List<IndirizzoDTO> localizzazioneCatasto) {
		this.localizzazioneCatasto = localizzazioneCatasto;
	}
	
	public void setNonANorma(boolean nonANorma) {
		this.nonANorma = nonANorma;
	}
	public boolean isNonANorma() {
		return nonANorma;
	}
	public void setPlanimetrieComma340(List<PlanimetriaComma340DTO> planimetrieComma340) {
		this.planimetrieComma340 = planimetrieComma340;
	}
	public List<PlanimetriaComma340DTO> getPlanimetrieComma340() {
		return planimetrieComma340;
	}
	
	
	

}
