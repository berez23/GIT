package it.webred.ct.data.access.basic.diagnostiche.tarsu.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.model.diagnostiche.*;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

public class UnitaImmobiliareTarDTO implements Serializable{
	
	private DocfaTarReportDTO docfaTarReportDTO;
	private List<DocfaTarReportDTO> listaDocfaPerImmobileDTO;
	private List<DocfaTarReportSogg> listaSoggetti;
	private List<IndirizzoDTO> listaIndirizziCatasto;
	private List<IndirizzoDTO> listaIndirizziTarsu;
	private List<DocfaDatiMetrici> listaDatiMetrici;
	private DocfaInParteDueH datiMetriciABC;
	private List<SitTTarOggettoDTO> listaOggettiTarAnte;
	private List<SitTTarOggettoDTO> listaOggettiTarPost;
	private List<PlanimetriaComma340DTO> listaPlanimetrieC340;
	private List<DocfaPlanimetrie> listaPlanimetrieDocfa;
	
	public DocfaTarReportDTO getDocfaTarReportDTO() {
		return docfaTarReportDTO;
	}
	public void setDocfaTarReportDTO(DocfaTarReportDTO docfaTarReportDTO) {
		this.docfaTarReportDTO = docfaTarReportDTO;
	}
	public List<DocfaTarReportDTO> getListaDocfaPerImmobileDTO() {
		return listaDocfaPerImmobileDTO;
	}
	public void setListaDocfaPerImmobileDTO(
			List<DocfaTarReportDTO> listaDocfaPerImmobileDTO) {
		this.listaDocfaPerImmobileDTO = listaDocfaPerImmobileDTO;
	}
	public List<DocfaTarReportSogg> getListaSoggetti() {
		return listaSoggetti;
	}
	public void setListaSoggetti(List<DocfaTarReportSogg> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}
	public List<IndirizzoDTO> getListaIndirizziCatasto() {
		return listaIndirizziCatasto;
	}
	public void setListaIndirizziCatasto(List<IndirizzoDTO> listaIndirizziCatasto) {
		this.listaIndirizziCatasto = listaIndirizziCatasto;
	}
	public List<DocfaDatiMetrici> getListaDatiMetrici() {
		return listaDatiMetrici;
	}
	public void setListaDatiMetrici(List<DocfaDatiMetrici> listaDatiMetrici) {
		this.listaDatiMetrici = listaDatiMetrici;
	}
	public DocfaInParteDueH getDatiMetriciABC() {
		return datiMetriciABC;
	}
	public void setDatiMetriciABC(DocfaInParteDueH datiMetriciABC) {
		this.datiMetriciABC = datiMetriciABC;
	}
	public List<IndirizzoDTO> getListaIndirizziTarsu() {
		return listaIndirizziTarsu;
	}
	public void setListaIndirizziTarsu(List<IndirizzoDTO> listaIndirizziTarsu) {
		for(int i=0; i<listaIndirizziTarsu.size(); i++){
			String civico = listaIndirizziTarsu.get(i).getCivico();
			if(civico!=null)
				listaIndirizziTarsu.get(i).setCivico(this.cleanLeftPad(civico,'0'));
		}
		this.listaIndirizziTarsu = listaIndirizziTarsu;
	}
	
	public List<SitTTarOggettoDTO> getListaOggettiTarAnte() {
		return listaOggettiTarAnte;
	}
	
	public void setListaOggettiTarAnte(List<SitTTarOggettoDTO> listaOggettiTarAnte) {
		for(int i=0; i<listaOggettiTarAnte.size(); i++){
			String civico = listaOggettiTarAnte.get(i).getSitTTarOggetto().getNumCiv();
			if(civico!=null)
				listaOggettiTarAnte.get(i).getSitTTarOggetto().setNumCiv(this.cleanLeftPad(civico,'0'));
		}
		this.listaOggettiTarAnte = listaOggettiTarAnte;
	}
	public List<SitTTarOggettoDTO> getListaOggettiTarPost() {
		return listaOggettiTarPost;
	}
	public void setListaOggettiTarPost(List<SitTTarOggettoDTO> listaOggettiTarPost) {
		for(int i=0; i<listaOggettiTarPost.size(); i++){
			String civico = listaOggettiTarPost.get(i).getSitTTarOggetto().getNumCiv();
			if(civico!=null)
				listaOggettiTarPost.get(i).getSitTTarOggetto().setNumCiv(this.cleanLeftPad(civico,'0'));
		}
		this.listaOggettiTarPost = listaOggettiTarPost;
	}
	public List<PlanimetriaComma340DTO> getListaPlanimetrieC340() {
		return listaPlanimetrieC340;
	}
	public void setListaPlanimetrieC340(
			List<PlanimetriaComma340DTO> listaPlanimetrieC340) {
		this.listaPlanimetrieC340 = listaPlanimetrieC340;
	}
	public List<DocfaPlanimetrie> getListaPlanimetrieDocfa() {
		return listaPlanimetrieDocfa;
	}
	public void setListaPlanimetrieDocfa(
			List<DocfaPlanimetrie> listaPlanimetrieDocfa) {
		this.listaPlanimetrieDocfa = listaPlanimetrieDocfa;
	}
	
	protected String cleanLeftPad(String s, char pad){
		if(s!= null){
			//s = s.trim();
			while (s.length() > 1 && s.charAt(0) == pad)
				s = s.substring(1);
		}
		return s;
	}
	
}
