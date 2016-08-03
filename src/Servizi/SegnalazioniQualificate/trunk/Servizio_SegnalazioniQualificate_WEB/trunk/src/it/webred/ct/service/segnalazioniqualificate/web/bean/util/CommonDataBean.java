package it.webred.ct.service.segnalazioniqualificate.web.bean.util;

import it.webred.ct.data.access.basic.common.dto.KeyValueDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.AmbitiAccertamentoSpecieDTO;
import it.webred.ct.data.model.segnalazionequalificata.SOfAmbitoAccertamento;
import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;
import it.webred.ct.service.segnalazioniqualificate.web.bean.gestione.GestionePraticaBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

public class CommonDataBean extends SegnalazioniQualificateBaseBean {

	private String siatelWebSite;
	
	private List<AmbitiAccertamentoSpecieDTO> ambitiAccertamento;
	
	private List<SelectItem> listaOperatori;
	
	private List<SelectItem> listaFontiComune;
	
	private List<SelectItem> listaAnno;
	
	private final List<SelectItem> listaFinalita = Arrays.asList(
			new SelectItem("TFL", "Tributi Fiscali Locali"),
			new SelectItem("TFE", "Tributi Fiscali Erariali")
		);
	
	private final List<SelectItem> listaStato = Arrays.asList(
			new SelectItem("0", "Istruttoria Comunale in corso"),
			new SelectItem("1", "Segnalazione in corso presso Agenzia delle Entrate"),
			new SelectItem("2", "Iter concluso")
		);
	
	private final List<SelectItem> listaAzioniChiusura = Arrays.asList(
			new SelectItem("DIRETTA AC", "Azione diretta amministrazione comunale"),
			new SelectItem("SEGNALAZIONE ADE", "Segnalazione qualificata presso Agenzia delle Entrate"),
			new SelectItem("NESSUNA", "Nessuna Azione")
		);
	
	private final List<SelectItem> listaStatoAdEAll = Arrays.asList(
			new SelectItem("ATTESA DI IMMISSIONE", "In Attesa di Immissione"),
			new SelectItem("IMMESSA", "Immessa"), 
			new SelectItem("INVIATA", "Inviata"),
			new SelectItem("PRESA IN CARICO", "Presa in carico"),
			new SelectItem("CONCLUSA", "Conclusa")
			);
	
	private List<SelectItem> listaStatoAdE;
	
	private final List<SelectItem> listaTipoSoggetto = Arrays.asList(
			new SelectItem("P", "Soggetto Fisico"),
			new SelectItem("G", "Soggetto Giuridico"));
	
	private final List<SelectItem> listaSiNo = Arrays.asList(
			new SelectItem("0", "NO"),
			new SelectItem("1", "SI")
			);
	
	public void doCaricaOperatori(){
		if (listaOperatori == null) {
			listaOperatori = new ArrayList<SelectItem>();
			try {
				List<String> lista = segnalazioneService.getListaOperatori(this.getInitRicercaParams());
				for (String oper : lista) {
					SelectItem item = new SelectItem(oper, oper);
					listaOperatori.add(item);
				}
			} catch (Throwable t) {
				super.addErrorMessage("operatori.error", t.getMessage());
				t.printStackTrace();
			}
		}
	}
	
	public void doCaricaListaAmbitiAccertamento(){
		if (ambitiAccertamento == null) {
			ambitiAccertamento = new ArrayList<AmbitiAccertamentoSpecieDTO>();
			try {
				List<SOfAmbitoAccertamento> lista = segnalazioneService.getAmbitiAccertamento(this.getInitRicercaParams());
				for (SOfAmbitoAccertamento a : lista) {
					SelectItem item = new SelectItem(a.getId(), a.getAmbito());
					addAmbitoSpecie(a.getSpecie(),item);
				}
			} catch (Throwable t) {
				super.addErrorMessage("ambiti.error", t.getMessage());
				t.printStackTrace();
			}
		}
	}
	
	private void addAmbitoSpecie(String s, SelectItem i){
		boolean trovato = false;
		for(AmbitiAccertamentoSpecieDTO as : ambitiAccertamento){
			if(as.getSpecie().equalsIgnoreCase(s)){
				as.getAmbiti().add(i);
				trovato = true;
			}
		}
		if(!trovato){
			List<SelectItem> ambiti = new ArrayList<SelectItem>();
			ambiti.add(i);
			AmbitiAccertamentoSpecieDTO as = new AmbitiAccertamentoSpecieDTO(s,ambiti);
			ambitiAccertamento.add(as);	
		}
		
	}
	
	public void doCaricaListaFontiComune(){
		
		if (listaFontiComune == null) {
			listaFontiComune = new ArrayList<SelectItem>();
			try {
				List<KeyValueDTO> listaFontiComuneKV = comuneService.getListaFonteByComune(this.getCurrentEnte(), false);
				for(KeyValueDTO kv : listaFontiComuneKV){
					SelectItem item = new SelectItem(kv.getKey(),kv.getValue());
					listaFontiComune.add(item);
				}
			} catch (Throwable t) {
				super.addErrorMessage("fonti.comune.error", t.getMessage());
				t.printStackTrace();
			}
		}
	}

	public List<SelectItem> getListaOperatori() {
		if(listaOperatori==null)
			this.doCaricaOperatori();
		return listaOperatori;
	}

	public void setListaOperatori(List<SelectItem> listaOperatori) {
		this.listaOperatori = listaOperatori;
	}


	public List<AmbitiAccertamentoSpecieDTO> getAmbitiAccertamento() {
		this.doCaricaListaAmbitiAccertamento();
		return ambitiAccertamento;
	}

	public List<SelectItem> getListaFontiComune() {
		if(listaFontiComune==null)
			this.doCaricaListaFontiComune();
		return listaFontiComune;
	}

	public List<SelectItem> getListaTipoSoggetto() {
		return listaTipoSoggetto;
	}

	public List<SelectItem> getListaFinalita() {
		return listaFinalita;
	}

	public List<SelectItem> getListaStato() {
		return listaStato;
	}

	public List<SelectItem> getListaAzioniChiusura() {
		return listaAzioniChiusura;
	}

	public List<SelectItem> getListaStatoAdEAll() {
		return listaStatoAdEAll;
	}
	
	public List<SelectItem> getListaStatoAdE(){
		GestionePraticaBean gpb = (GestionePraticaBean)this.getBeanReference("gestionePraticaBean");
		listaStatoAdE = new ArrayList<SelectItem>();
		String adeStato = gpb.getDto().getPratica().getAdeStato();
		boolean skip = true;
		if(adeStato!=null && adeStato.trim().length()!=0){
			for(SelectItem s : listaStatoAdEAll){
				if(adeStato.equalsIgnoreCase((String)s.getValue()))
					skip = false;
				
				if(!skip)
					listaStatoAdE.add(s);
			}
		}else{
			listaStatoAdE = listaStatoAdEAll;
		}
		return listaStatoAdE;
	}

	public List<SelectItem> getListaSiNo() {
		return listaSiNo;
	}

	public String getSiatelWebSite() {
		if(siatelWebSite == null)
			siatelWebSite = super.getSiatelWebSite();
		return siatelWebSite;
	}

	private List<SelectItem> popolaSelectList(HashMap<String,String> map){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()){
			String key = i.next();
			SelectItem item = new SelectItem(key,map.get(key));
			lista.add(item);
		}
		return lista;
	}

	public void setListaAnno(List<SelectItem> listaAnno) {
		this.listaAnno = listaAnno;
	}

	public List<SelectItem> getListaAnno() {
		int iniAnno = 1990;
		listaAnno = new ArrayList<SelectItem>();
		int currAnno = super.getCurrentYear();
		getLogger().info("Generazione anni fino a: "+ currAnno);
		for(int anno=iniAnno; anno<=currAnno; anno++){
			String sAnno = Integer.toString(anno);
			getLogger().info(anno);
			SelectItem i = new SelectItem(sAnno, sAnno);
			listaAnno.add(i);
		}
		return listaAnno;
	}
	
	
	
}
