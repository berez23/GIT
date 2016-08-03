package it.webred.ct.service.tsSoggiorno.web.bean.versamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import it.webred.ct.service.tsSoggiorno.data.access.DichiarazioneService;
import it.webred.ct.service.tsSoggiorno.data.access.MavService;
import it.webred.ct.service.tsSoggiorno.data.access.TariffaService;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.VersamentoDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffaPK;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.anagrafica.AnagraficaBean;

public class VersamentoBean extends TsSoggiornoBaseBean {

	protected MavService mavService = (MavService) getEjb("Servizio_TsSoggiorno", "Servizio_TsSoggiorno_EJB", "MavServiceBean");

	private Long idPeriodoSelezionato;
	private String codFiscSelezionato;
	private String pIvaSelezionato;
	private IsTariffa tariffa;
	private List<VersamentoDTO> listaVersamenti;
	
	
	public void doLoadVersamentiSocieta() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			
			if(codFiscSelezionato!=null && codFiscSelezionato.length()>0)
				dataIn.setCodFiscale(codFiscSelezionato);
			else
				dataIn.setCodFiscale(pIvaSelezionato);
			
			listaVersamenti = mavService.getListaVersamentiMav(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}


	public MavService getMavService() {
		return mavService;
	}


	public void setMavService(MavService mavService) {
		this.mavService = mavService;
	}


	public Long getIdPeriodoSelezionato() {
		return idPeriodoSelezionato;
	}


	public void setIdPeriodoSelezionato(Long idPeriodoSelezionato) {
		this.idPeriodoSelezionato = idPeriodoSelezionato;
	}


	public String getCodFiscSelezionato() {
		return codFiscSelezionato;
	}


	public void setCodFiscSelezionato(String codFiscSelezionato) {
		this.codFiscSelezionato = codFiscSelezionato;
	}


	public IsTariffa getTariffa() {
		return tariffa;
	}


	public void setTariffa(IsTariffa tariffa) {
		this.tariffa = tariffa;
	}


	public List<VersamentoDTO> getListaVersamenti() {
		return listaVersamenti;
	}


	public void setListaVersamenti(List<VersamentoDTO> listaVersamenti) {
		this.listaVersamenti = listaVersamenti;
	}


	public String getpIvaSelezionato() {
		return pIvaSelezionato;
	}


	public void setpIvaSelezionato(String pIvaSelezionato) {
		this.pIvaSelezionato = pIvaSelezionato;
	}

	
}
