package it.webred.cs.csa.web.manbean.fascicolo.relazioni;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.InterventoDTO;
import it.webred.cs.csa.ejb.dto.RelazioneDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsCTipoIntervento;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.jsf.bean.DatiInterventoBean;
import it.webred.cs.jsf.interfaces.IRelazioni;
import it.webred.cs.jsf.manbean.SchedaPaiMan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

public class RelazioniBean extends FascicoloCompBaseBean implements IRelazioni{

	private List<RelazioneDTO> listaRelazioni;
	private int idxSelected;
	private CsDRelazione relazione;
	private String modalHeader;
	
	private Long idTipoInterventoSelezionato;
	private List<SelectItem> listaTipiIntervento;
	private List<CsCTipoIntervento> listaTipiInterventoAttivi;
	private List<DatiInterventoBean> listaFogliAmmCollegati;
	
	private SchedaPaiMan managerPai;
	private boolean disableNuovoPAI;
	
	@Override
	public void initializeData() {
		
		try{
			
			managerPai = new SchedaPaiMan();
			managerPai.setIdCaso(idCaso);
			
			InterventoDTO dto = new InterventoDTO();
			fillEnte(dto);
			dto.setCasoId(idCaso);
			listaRelazioni = diarioService.findRelazioniByCaso(dto);
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	public void eliminaPai(CsDPai pai){
	 try{	
		 managerPai.elimina(pai);
		 
		 RequestContext.getCurrentInstance().addCallbackParam("eliminato", true);		
		 addInfoFromProperties("elimina.ok");
		 
		 this.initializeData();

		} catch(Exception e) {
			addErrorFromProperties("elimina.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void nuovo() {
		
		modalHeader = "Nuova relazione";
		relazione = new CsDRelazione();
		listaTipiInterventoAttivi = new ArrayList<CsCTipoIntervento>();
		listaFogliAmmCollegati = new ArrayList<DatiInterventoBean>();
	}
	
	@Override
	public void carica() {
		
		try {
			modalHeader = "Modifica relazione";
			relazione = listaRelazioni.get(idxSelected).getRelazione();
			listaTipiInterventoAttivi = new ArrayList<CsCTipoIntervento>(); 
			listaTipiInterventoAttivi.addAll(relazione.getCsCTipoInterventos());
			
			listaFogliAmmCollegati = new ArrayList<DatiInterventoBean>();
			for(CsDDiario d : relazione.getCsDDiario().getCsDDiariPadre()){
				if(d.getCsFlgIntervento() != null) {
					DatiInterventoBean dib = new DatiInterventoBean(d.getCsFlgIntervento().getCsIIntervento(), idSoggetto);
					listaFogliAmmCollegati.add(dib);
				}
			}
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void salva() {
		
		if(!validaRelazione()) {
			return;
		}
		
		try{
			if(listaTipiInterventoAttivi != null)
				relazione.setCsCTipoInterventos(new HashSet<CsCTipoIntervento>(listaTipiInterventoAttivi));
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(relazione);
			
			if(relazione.getDiarioId() != null) {
				relazione.setUsrMod(dto.getUserId());
				relazione.setDtMod(new Date());
				diarioService.updateRelazione(dto);
			}
			else {
				CsDDiario csd = new CsDDiario();
		        CsACaso csa = new CsACaso();
		        CsTbTipoDiario cstd = new CsTbTipoDiario(); 
		        csa.setId(idCaso);
		        cstd.setId(new Long(DataModelCostanti.TipoDiario.RELAZIONE_ID)); 
		        csd.setCsACaso(csa);
		        csd.setCsTbTipoDiario(cstd);
		        CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
		        csd.setCsOOperatoreSettore(opSettore);
		        relazione.setCsDDiario(csd);
		        relazione.setUserIns(dto.getUserId());
		        relazione.setDtIns(new Date());
				
				diarioService.saveRelazione(dto);
			}
			
			addInfoFromProperties("salva.ok");
			RequestContext.getCurrentInstance().addCallbackParam("saved", true);
			
			initializeData();
			
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	private boolean validaRelazione() {
		
		boolean ok = true;
		
		if(relazione.getDataProposta() == null) {
			ok = false;
			addError("Il campo Data è obbligatorio", "");
		}
		
		if(relazione.getSituazioneAmb() == null || "".equals(relazione.getSituazioneAmb())) {
			ok = false;
			addError("Il campo Situazione ambientale è obbligatorio", "");
		}
		
		return ok;
	}
	
	@Override
	public List<SelectItem> getListaTipiIntervento()  {
		
		if(listaTipiIntervento == null) {
			try{
				
				listaTipiIntervento = new ArrayList<SelectItem>();
				InterventoDTO dto = new InterventoDTO();
				fillEnte(dto);
				dto.setIdCatsoc(catsocCorrente.getId());
		        CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				dto.setIdSettore(opSettore.getCsOSettore().getId());
				List<CsCTipoIntervento> lista = interventoService.findTipiInterventoSettoreCatSoc(dto);
				for(CsCTipoIntervento tipoInt: lista) {
					listaTipiIntervento.add(new SelectItem(tipoInt.getId() , tipoInt.getDescrizione()));
				}
			
			} catch (Exception e) {
				addErrorFromProperties("caricamento.error");
				logger.error(e.getMessage(),e);
			}
		}
		
		return listaTipiIntervento;
	}
	
	@Override
	public void aggiungiTipoIntervento() {
		
		try{
			
			if(listaTipiInterventoAttivi == null)
				listaTipiInterventoAttivi = new ArrayList<CsCTipoIntervento>();
			
			for(CsCTipoIntervento cs: listaTipiInterventoAttivi) {
				if(cs.getId().equals(idTipoInterventoSelezionato)) {
					addWarning("Tipo intervento: " + cs.getDescrizione() + " già presente", "");
					return;
				}
			}
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(String.valueOf(idTipoInterventoSelezionato));
			CsCTipoIntervento tipoInt = interventoService.getTipoInterventoById(dto);
			
			listaTipiInterventoAttivi.add(tipoInt);
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		
	}

	@Override
	public void eliminaTipoIntervento() {
		
		listaTipiInterventoAttivi.remove(idxSelected);
		
	}

	@Override
	public Long getIdCaso() {
		return idCaso;
	}

	@Override
	public void setIdCaso(Long idCaso) {
		this.idCaso = idCaso;
	}

	@Override
	public String getModalHeader() {
		return modalHeader;
	}

	public void setModalHeader(String modalHeader) {
		this.modalHeader = modalHeader;
	}

	@Override
	public CsDRelazione getRelazione() {
		return relazione;
	}

	public void setRelazione(CsDRelazione relazione) {
		this.relazione = relazione;
	}

	@Override
	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	@Override
	public Long getIdTipoInterventoSelezionato() {
		return idTipoInterventoSelezionato;
	}

	public void setIdTipoInterventoSelezionato(Long idTipoInterventoSelezionato) {
		this.idTipoInterventoSelezionato = idTipoInterventoSelezionato;
	}

	@Override
	public List<CsCTipoIntervento> getListaTipiInterventoAttivi() {
		return listaTipiInterventoAttivi;
	}

	public void setListaTipiInterventoAttivi(
			List<CsCTipoIntervento> listaTipiInterventoAttivi) {
		this.listaTipiInterventoAttivi = listaTipiInterventoAttivi;
	}

	public void setListaTipiIntervento(List<SelectItem> listaTipiIntervento) {
		this.listaTipiIntervento = listaTipiIntervento;
	}

	public SchedaPaiMan getManagerPai() {
		return managerPai;
	}

	public void setManagerPai(SchedaPaiMan managerPai) {
		this.managerPai = managerPai;
	}

	@Override
	public List<RelazioneDTO> getListaRelazioni() {
		return this.listaRelazioni;
	}

	public boolean isDisableNuovoPAI() {
		return disableNuovoPAI;
	}

	public void setDisableNuovoPAI(boolean disableNuovoPAI) {
		this.disableNuovoPAI = disableNuovoPAI;
	}

	public List<DatiInterventoBean> getListaFogliAmmCollegati() {
		return listaFogliAmmCollegati;
	}

	public void setListaFogliAmmCollegati(
			List<DatiInterventoBean> listaFogliAmmCollegati) {
		this.listaFogliAmmCollegati = listaFogliAmmCollegati;
	}

}
