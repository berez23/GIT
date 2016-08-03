package it.webred.cs.csa.web.manbean.listacatsociali;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableCatSocialeSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.CasoSearchCriteria;
import it.webred.cs.csa.ejb.dto.PaginationDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsRelSettoreCatsoc;
import it.webred.cs.jsf.bean.CasiCatSocialeBean;
import it.webred.cs.jsf.bean.CasiOperatoreBean;
import it.webred.cs.jsf.interfaces.IListaCatSociali;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.ejb.utility.ClientUtility;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


@ManagedBean
@ViewScoped
public class ListaCatSocialiBean extends CsUiCompBaseBean implements IListaCatSociali {

	private List<CasiCatSocialeBean> lstCasiCatSociale;
	private List<CasiOperatoreBean> lstCasiOperatore;
	private int idxSelected;
	
	private boolean visualCaricoLavoro;
	
	@PostConstruct
	public void init() {
		
		try {

			lstCasiCatSociale = new ArrayList<CasiCatSocialeBean>();
			AccessTableCatSocialeSessionBeanRemote catSocialeService = (AccessTableCatSocialeSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCatSocialeSessionBean");
			AccessTableSoggettoSessionBeanRemote soggettiService = (AccessTableSoggettoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");

			CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
			
			BaseDTO bDto = new BaseDTO();
			fillEnte(bDto);
			bDto.setObj(opSettore.getCsOSettore().getId());
			List<CsRelSettoreCatsoc> listaCatSociali = catSocialeService.findRelSettoreCatsocBySettore(bDto);
			
			CasoSearchCriteria searchCriteria = new CasoSearchCriteria();
			PaginationDTO dto = new PaginationDTO();
			fillEnte(dto);
			dto.setObj(searchCriteria);
						
			for(CsRelSettoreCatsoc settCatSoc: listaCatSociali) {
				
				CasiCatSocialeBean comp = new CasiCatSocialeBean();
				comp.setCatSociale(settCatSoc.getCsCCategoriaSociale());
				comp.setSettore(settCatSoc.getCsOSettore());
				
				searchCriteria.setWithResponsabile(false);
				searchCriteria.setIdLastIter(null);
				searchCriteria.setIdCatSociale(settCatSoc.getCsCCategoriaSociale().getId());
				Integer numCasi = soggettiService.getCasiPerCategoriaCount(dto);
				searchCriteria.setWithResponsabile(true);
				searchCriteria.setIdLastIter(null);
				Integer numCasiInCarico = soggettiService.getCasiPerCategoriaCount(dto);
				searchCriteria.setWithResponsabile(false);
				searchCriteria.setIdLastIter(DataModelCostanti.IterStatoInfo.CHIUSO);
				Integer numCasiChiusi = soggettiService.getCasiPerCategoriaCount(dto);
				
				comp.setCasi(numCasi);
				comp.setCasiInCarico(numCasiInCarico);
				comp.setCasiChiusi(numCasiChiusi);
				lstCasiCatSociale.add(comp);
			}
			
			visualCaricoLavoro = checkPermesso(DataModelCostanti.PermessiCaso.VISUALIZZAZIONE_CARICO_LAVORO);
			
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
	}
	
	public void caricaCaricoLavoro() {
		
		try {
			
			AccessTableOperatoreSessionBeanRemote opService = (AccessTableOperatoreSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableOperatoreSessionBean");
			AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
			AnagraficaService anagraficaService = (AnagraficaService) ClientUtility.getEjbInterface("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
			
			lstCasiOperatore = new ArrayList<CasiOperatoreBean>();
			CasiCatSocialeBean comp = lstCasiCatSociale.get(idxSelected);
			BaseDTO dto = new BaseDTO(); 
			fillEnte(dto);
			dto.setObj(comp.getCatSociale().getId());
			dto.setObj2(comp.getCatSociale().getId());
			//dto.setObj3(comp.getSettore().getId());
			List<CsOOperatore> listaOperatori = opService.getOperatoriByCatSociale(dto);
			
			for(CsOOperatore op: listaOperatori) {
				
				CasiOperatoreBean oComp = new CasiOperatoreBean();
				oComp.setOperatore(op);
				oComp.setAmAnagrafica(anagraficaService.findAnagraficaByUserName(op.getUsername()));
				
				dto.setObj(op.getId());
				oComp.setNumCasi(casoService.countCasiByResponsabileCatSociale(dto));
				lstCasiOperatore.add(oComp);
			}
			
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
	}


	public List<CasiCatSocialeBean> getLstCasiCatSociale() {
		return lstCasiCatSociale;
	}

	public void setLstCasiCatSociale(List<CasiCatSocialeBean> lstCasiCatSociale) {
		this.lstCasiCatSociale = lstCasiCatSociale;
	}

	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	public List<CasiOperatoreBean> getLstCasiOperatore() {
		return lstCasiOperatore;
	}

	public void setLstCasiOperatore(List<CasiOperatoreBean> lstCasiOperatore) {
		this.lstCasiOperatore = lstCasiOperatore;
	}

	public boolean isVisualCaricoLavoro() {
		return visualCaricoLavoro;
	}

	public void setVisualCaricoLavoro(boolean visualCaricoLavoro) {
		this.visualCaricoLavoro = visualCaricoLavoro;
	}
	
}
