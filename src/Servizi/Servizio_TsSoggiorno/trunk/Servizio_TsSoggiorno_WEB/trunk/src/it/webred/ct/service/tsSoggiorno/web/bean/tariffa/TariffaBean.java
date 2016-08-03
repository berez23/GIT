package it.webred.ct.service.tsSoggiorno.web.bean.tariffa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import it.webred.ct.service.tsSoggiorno.data.access.DichiarazioneService;
import it.webred.ct.service.tsSoggiorno.data.access.TariffaService;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffaPK;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.anagrafica.AnagraficaBean;

public class TariffaBean extends TsSoggiornoBaseBean {

	protected TariffaService tariffaService = (TariffaService) getEjb("Servizio_TsSoggiorno", "Servizio_TsSoggiorno_EJB", "TariffaServiceBean");

	private Long idPeriodoSelezionato;
	private String idClasseSelezionato;
	private IsTariffa tariffa;
	private List<TariffaDTO> listaTariffe;
	private List<SelectItem> listaClassi = new ArrayList<SelectItem>();
	private List<SelectItem> listaPeriodi = new ArrayList<SelectItem>();

	public void doNewTariffa() {
		tariffa = new IsTariffa();
		IsTariffaPK id = new IsTariffaPK();
		tariffa.setId(id);
		loadPeriodi();
		loadClassi();
	}

	public void doLoadTariffe() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			listaTariffe = tariffaService.getListaTariffe(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doSaveTariffa() {
		try {

			boolean valido = true;
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(tariffa.getId().getFkIsPeriodoDa());
			dataIn.setId2(tariffa.getId().getFkIsClasse());
			//controllo se esiste una dichiarazione per il periodo/categoria
			List<IsDichiarazione> listaDich = super.getDichiarazioneService().getDichiarazioniByPeriodoDalClasse(dataIn);
			if(listaDich != null && listaDich.size() > 0){
				valido = false;
				super.addErrorMessage("tarif.dichiarazioniexist", "");
			}
			//controllo se esiste una tariffa per il periodo/categoria
			IsTariffa tarExist= tariffaService.getTariffaByPeriodoClasse(dataIn);
			if(tarExist != null){
				valido = false;
				super.addErrorMessage("tarif.exist", "");
			}
			
			if(valido){
				AnagraficaBean ana = (AnagraficaBean) getBeanReference("anagraficaBean");
				tariffa.setUsrIns(ana.getSoggetto().getCodfisc());
				tariffa.setDtIns(new Date());
				dataIn.setObj(tariffa);
				tariffaService.saveTariffa(dataIn);
	
				super.addInfoMessage("salvataggio.ok");
			}	
				
		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doDeleteTariffa() {
		try {

			boolean valido = true;
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(idPeriodoSelezionato);
			dataIn.setId2(idClasseSelezionato);
			//controllo se esiste una dichiarazione per il periodo/categoria
			List<IsDichiarazione> listaDich = super.getDichiarazioneService().getDichiarazioniByPeriodoDalClasse(dataIn);
			if(listaDich != null && listaDich.size() > 0){
				valido = false;
				super.addErrorMessage("tarif.dichiarazioniexist", "");
			}

			if(valido){
				tariffaService.deleteTariffa(dataIn);
				//reload
				doLoadTariffe();
				super.addInfoMessage("salvataggio.ok");
			}

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public List<TariffaDTO> getListaTariffe() {
		return listaTariffe;
	}

	public void setListaTariffe(List<TariffaDTO> listaTariffe) {
		this.listaTariffe = listaTariffe;
	}

	public Long getIdPeriodoSelezionato() {
		return idPeriodoSelezionato;
	}

	public void setIdPeriodoSelezionato(Long idPeriodoSelezionato) {
		this.idPeriodoSelezionato = idPeriodoSelezionato;
	}

	public String getIdClasseSelezionato() {
		return idClasseSelezionato;
	}

	public void setIdClasseSelezionato(String idClasseSelezionato) {
		this.idClasseSelezionato = idClasseSelezionato;
	}

	public IsTariffa getTariffa() {
		return tariffa;
	}

	public void setTariffa(IsTariffa tariffa) {
		this.tariffa = tariffa;
	}

	public List<SelectItem> getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List<SelectItem> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List<SelectItem> getListaPeriodi() {
		return listaPeriodi;
	}

	public void setListaPeriodi(List<SelectItem> listaPeriodi) {
		this.listaPeriodi = listaPeriodi;
	}

	private void loadClassi() {
		if (listaClassi == null || listaClassi.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsClasse> lista = super.getAnagraficaService().getClassi(dataIn);
			for (IsClasse c : lista) {
				listaClassi.add(new SelectItem(c.getCodice(), c
						.getDescrizione()));
			}
		}
	}

	private void loadPeriodi() {
		if (listaPeriodi == null || listaPeriodi.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsPeriodo> lista = super.getDichiarazioneService().getPeriodi(dataIn);
			for (IsPeriodo c : lista) {
				listaPeriodi.add(new SelectItem(c.getId(), c.getDescrizione()));
			}
		}
	}
}
