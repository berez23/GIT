package it.webred.cs.csa.web.manbean.listacasi;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.CasoSearchCriteria;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.PaginationDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsASoggettoCategoriaSoc;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.jsf.bean.DatiCasoBean;
import it.webred.cs.jsf.manbean.IterInfoStatoMan;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ejb.utility.ClientUtility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class LazyListaCasiModel extends LazyDataModel<DatiCasoBean> {
     
	private static final long serialVersionUID = 1L;

	@Override
    public DatiCasoBean getRowData(String rowKey) { 
		//TODO
        return null;
    }
 
    @Override
    public Object getRowKey(DatiCasoBean datiCaso) {
        return datiCaso.getSoggetto().getAnagraficaId();
    }
 
    @Override
    public List<DatiCasoBean> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        List<DatiCasoBean> data = new ArrayList<DatiCasoBean>();
 
		PaginationDTO dto = new PaginationDTO();
		CsUiCompBaseBean.fillEnte(dto);
		CasoSearchCriteria searchCriteria = new CasoSearchCriteria();
		CsOOperatoreSettore opSettore = (CsOOperatoreSettore) CsUiCompBaseBean.getSession().getAttribute("operatoresettore");
		if(opSettore != null) {
			//DEFAULT: filtro solo i casi dove sono presente come tipo operatore e con il settore scelto o quelli segnalatimi
			searchCriteria.setUsername(dto.getUserId());
			searchCriteria.setIdOperatore(opSettore.getCsOOperatore().getId());
			searchCriteria.setIdSettore(opSettore.getCsOSettore().getId());
			searchCriteria.setIdOrganizzazione(opSettore.getCsOSettore().getCsOOrganizzazione().getId());
			//PERMESSO CASI SETTORE: 
			if(CsUiCompBaseBean.checkPermesso(DataModelCostanti.PermessiCaso.VISUALIZZAZIONE_CASI_SETTORE))
				searchCriteria.setPermessoCasiSettore(true);
			//PERMESSO CASI ORGANIZZAZIONE: 
			if(CsUiCompBaseBean.checkPermesso(DataModelCostanti.PermessiCaso.VISUALIZZAZIONE_CASI_ORG))
				searchCriteria.setPermessoCasiOrganizzazione(true);
		}
		String filterSoggetto = (String) filters.get("soggetto");
		String filterDataNascita = (String) filters.get("dataNascita");
		String filterCF = (String) filters.get("codiceFiscale");
		String filterAssSociale = (String) filters.get("assistenteSociale");
		if(filterSoggetto != null)
			searchCriteria.setDenominazione(filterSoggetto);
		if(filterDataNascita != null)
			searchCriteria.setDataNascita(filterDataNascita);
		if(filterCF != null)
			searchCriteria.setCodiceFiscale(filterCF);
		//se devo filtrare per ass sociale eseguo manualmente la paginazione
		if(filterAssSociale == null) {
			dto.setFirst(first);
			dto.setPageSize(pageSize);
		}
		dto.setObj(searchCriteria);
			
		try {
			
			AccessTableSoggettoSessionBeanRemote soggettiService = (AccessTableSoggettoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
			AccessTableIterStepSessionBeanRemote iterSessionBean = (AccessTableIterStepSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableIterStepSessionBean");
			AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
			AnagraficaService anagraficaService = (AnagraficaService) ClientUtility.getEjbInterface("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
			
			List<CsASoggetto> list = soggettiService.getCasiSoggetto(dto);
			
			for(CsASoggetto sogg: list){
	            
				CsACaso caso = sogg.getCsACaso();
				IterInfoStatoMan casoInfo = new IterInfoStatoMan();
				
				IterDTO itDto = new IterDTO();
				CsUiCompBaseBean.fillEnte(itDto);
				itDto.setIdCaso(caso.getId());
				CsItStep itStep = iterSessionBean.getLastIterStepByCaso(itDto);
				
				if( itStep != null )
					casoInfo.initialize( itStep );
				
				BaseDTO bDto = new BaseDTO();
				CsUiCompBaseBean.fillEnte(bDto);
				bDto.setObj(sogg.getCsACaso().getId());
				CsACasoOpeTipoOpe casoOpTipoOp = casoService.findResponsabile(bDto);
				AmAnagrafica operatoreAnagrafica = null;
				CsOOperatore operatore = null;
				if(casoOpTipoOp != null) {
					operatore = casoOpTipoOp.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore();
					operatoreAnagrafica = anagraficaService.findAnagraficaByUserName(operatore.getUsername());
				}
				
				DatiCasoBean bean = new DatiCasoBean( sogg, operatore, operatoreAnagrafica, casoInfo);
				bean.setnInterventi("1");//test TODO
				
				String catSoc = "";
				bDto.setObj(sogg.getAnagraficaId());
				List<CsASoggettoCategoriaSoc> listaSoggCat = soggettiService.getSoggettoCategorieBySoggetto(bDto);
				for(CsASoggettoCategoriaSoc soggCat: listaSoggCat) {
					if(soggCat.getId().getDataFineApp().after(new Date()))
						catSoc += ", " + soggCat.getCsCCategoriaSociale().getTooltip();
				}
				if(catSoc.length() > 1)
					bean.setCatSociale(catSoc.substring(2));
				
				//filter
				boolean match = true;
				if(filterAssSociale != null && !(operatoreAnagrafica.getCognome() + " " + operatoreAnagrafica.getNome()).toUpperCase().contains(filterAssSociale.toUpperCase()))
					match = false;
	            if(match) {
	                data.add(bean);
	            }
	        }
			
	        //rowCount
			Integer dataSize;
			if(filterAssSociale != null)
				dataSize = data.size();
			else 
				dataSize = soggettiService.getCasiSoggettoCount(dto);
			this.setRowCount(dataSize);
			
			//paginate
	        if(filterAssSociale != null && dataSize > pageSize) {
	            try {
	                return data.subList(first, first + pageSize);
	            }
	            catch(IndexOutOfBoundsException e) {
	                return data.subList(first, first + (dataSize % pageSize));
	            }
	        }
	        else return data;
		
		} catch (Exception e) {
			CsUiCompBaseBean.addErrorFromProperties("caricamento.error");
			CsUiCompBaseBean.logger.error(e.getMessage(), e);
		}
		
		return data;
    }
	
}
