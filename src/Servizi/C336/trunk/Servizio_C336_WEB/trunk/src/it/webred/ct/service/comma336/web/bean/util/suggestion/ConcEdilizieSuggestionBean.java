package it.webred.ct.service.comma336.web.bean.util.suggestion;

import it.webred.ct.data.access.basic.common.dto.RicercaCivicoDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;
import it.webred.ct.service.comma336.web.Comma336BaseBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class ConcEdilizieSuggestionBean extends Comma336BaseBean {

	private List<SelectItem> listaProgressivoAnno;
	
	private List<SelectItem> listaAnnoProtocollo;
	
	private List<SelectItem> listaTitoliSoggetto;
	
	private List<SelectItem> listaCiviciVia;
	
	public void doCaricaListaProgressivoAnno(){
		if (listaProgressivoAnno == null) {
			listaProgressivoAnno = new ArrayList<SelectItem>();
			try {
				RicercaConcEdilizieDTO rc = new RicercaConcEdilizieDTO();
				this.fillEnte(rc);
				List<String> lista = concEdilizieService.getListaProgressivoAnno(rc);
				for (String anno : lista) {
					SelectItem item = new SelectItem(anno, anno);
					listaProgressivoAnno.add(item);
				}
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.concEdi.progAnno.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
	}
	
	public void doCaricaListaTitoliSoggetto(){
		if (listaTitoliSoggetto == null) {
			listaTitoliSoggetto = new ArrayList<SelectItem>();
			try {
				RicercaConcEdilizieDTO rc = new RicercaConcEdilizieDTO();
				this.fillEnte(rc);
				List<String> lista = concEdilizieService.getListaTitoliSoggetto(rc);
				for (String anno : lista) {
					SelectItem item = new SelectItem(anno, anno);
					listaTitoliSoggetto.add(item);
				}
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.concEdi.titoliSoggetto.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
	}
	
	public void doCaricaListaAnnoProtocollo(){
		if (listaAnnoProtocollo == null) {
			listaAnnoProtocollo = new ArrayList<SelectItem>();
			try {
				RicercaConcEdilizieDTO rc = new RicercaConcEdilizieDTO();
				this.fillEnte(rc);
				List<String> lista = concEdilizieService.getListaProtocolloAnno(rc);
				for (String anno : lista) {
					SelectItem item = new SelectItem(anno, anno);
					listaAnnoProtocollo.add(item);
				}
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.concEdi.protAnno.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
	}
	
	public void doCaricaListaCiviciVia(Object via){
		
		listaCiviciVia = new ArrayList<SelectItem>();
		try {
			RicercaCivicoDTO rc = new RicercaCivicoDTO();
			this.fillEnte(rc);
			rc.setDescrizioneVia((String)via);
			List<String> lista = concEdilizieService.getSuggestionCiviciByVia(rc);
			for (String civico : lista) {
				SelectItem item = new SelectItem(civico, civico);
				listaCiviciVia.add(item);
			}
		} catch (Throwable t) {
			super.addErrorMessage("suggestion.concEdi.civici.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
	}
	
	public List<String> getSuggestionByVia(Object obj) {

		String via = (String) obj;

		List<String> result = new ArrayList<String>();

		if (via != null && !via.trim().equalsIgnoreCase("NULL") && via.length() >= 3) {
			try {

				RicercaCivicoDTO rc = new RicercaCivicoDTO();
				this.fillEnte(rc);
				rc.setDescrizioneVia(via);
				result = concEdilizieService.getSuggestionVie(rc);

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.concEdi.via.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return result;
	}
	
	//Vale sia per soggetti giuridici che fisici, effettua la concatenazione di cognome e nome
	public List<String> getSuggestionByDenominazione(Object obj) {

		String denominazione = (String) obj;

		List<String> result = new ArrayList<String>();

		if (denominazione != null && !denominazione.trim().equalsIgnoreCase("NULL")
				&& denominazione.length() >= 3) {
			try {

				RicercaSoggettoDTO rs = new RicercaSoggettoDTO();
				fillEnte(rs);
				rs.setDenom(denominazione);
				result = concEdilizieService.getSuggestionSoggetti(rs);
			
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.concEdi.soggetto.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return result;
	}

	
	public List<SelectItem> getListaProgressivoAnno() {
		doCaricaListaProgressivoAnno();
		return listaProgressivoAnno;
	}

	public void setListaProgressivoAnno(List<SelectItem> listaProgressivoAnno) {
		this.listaProgressivoAnno = listaProgressivoAnno;
	}

	public List<SelectItem> getListaAnnoProtocollo() {
		doCaricaListaAnnoProtocollo();
		return listaAnnoProtocollo;
	}

	public void setListaAnnoProtocollo(List<SelectItem> listaAnnoProtocollo) {
		this.listaAnnoProtocollo = listaAnnoProtocollo;
	}

	public List<SelectItem> getListaTitoliSoggetto() {
		doCaricaListaTitoliSoggetto();
		return listaTitoliSoggetto;
	}

	public void setListaTitoliSoggetto(List<SelectItem> listaTitoliSoggetto) {
		this.listaTitoliSoggetto = listaTitoliSoggetto;
	}

	public void setListaCiviciVia(List<SelectItem> listaCiviciVia) {
		this.listaCiviciVia = listaCiviciVia;
	}

	public List<SelectItem> getListaCiviciVia() {
		return listaCiviciVia;
	}

}
