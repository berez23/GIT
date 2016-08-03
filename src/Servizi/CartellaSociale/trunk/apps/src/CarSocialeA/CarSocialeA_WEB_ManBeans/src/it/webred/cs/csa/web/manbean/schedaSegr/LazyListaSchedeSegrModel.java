package it.webred.cs.csa.web.manbean.schedaSegr;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSchedaSegrSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.SchedaSegrDTO;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsSsSchedaSegr;
import it.webred.cs.jsf.manbean.IterInfoStatoMan;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ejb.utility.ClientUtility;
import it.webred.ss.data.model.SsScheda;
import it.webred.ss.data.model.SsSchedaSegnalato;
import it.webred.ss.data.model.SsTipoScheda;
import it.webred.ss.ejb.client.SsSchedaSessionBeanRemote;
import it.webred.ss.ejb.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class LazyListaSchedeSegrModel extends LazyDataModel<SchedaSegr> {
     
	private static final long serialVersionUID = 1L;

	@Override
    public SchedaSegr getRowData(String rowKey) { 
		//TODO
        return null;
    }
 
    @Override
    public Object getRowKey(SchedaSegr schedaSegr) {
        return schedaSegr.getId();
    }
 
    @Override
    public List<SchedaSegr> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        List<SchedaSegr> data = new ArrayList<SchedaSegr>();
 
		SchedaSegrDTO dto = new SchedaSegrDTO();
		CsUiCompBaseBean.fillEnte(dto);
		dto.setFirst(first);
		dto.setPageSize(pageSize);
			
		try {
			
			AccessTableSchedaSegrSessionBeanRemote schedaSegrService = (AccessTableSchedaSegrSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSchedaSegrSessionBean");
			SsSchedaSessionBeanRemote ssSchedaSegrService = (SsSchedaSessionBeanRemote) ClientUtility.getEjbInterface("SegretariatoSoc", "SegretariatoSoc_EJB", "SsSchedaSessionBean");
			AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
			AnagraficaService anagraficaService = (AnagraficaService) ClientUtility.getEjbInterface("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
			AccessTableIterStepSessionBeanRemote iterSessionBean = (AccessTableIterStepSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableIterStepSessionBean");
			
			CsOOperatoreSettore opSettore = (CsOOperatoreSettore) CsUiCompBaseBean.getSession().getAttribute("operatoresettore");
			dto.setIdSettore(opSettore.getCsOSettore().getId());
			
			List<CsSsSchedaSegr> list = schedaSegrService.getSchedeSegr(dto);
			
			BaseDTO bDto = new BaseDTO();
			CsUiCompBaseBean.fillEnte(bDto);
			for(CsSsSchedaSegr csScheda: list){
				bDto.setObj(csScheda.getId());
				try {
					SsScheda ssScheda = ssSchedaSegrService.readScheda(bDto);
	        		bDto.setObj(ssScheda.getSegnalato());
	        		SsSchedaSegnalato segnalato = ssSchedaSegrService.readSegnalatoById(bDto);
	        		bDto.setObj(ssScheda.getTipo());
	        		SsTipoScheda tipoScheda = ssSchedaSegrService.readTipoSchedaById(bDto);
	        		SchedaSegr schedaSegr = new SchedaSegr(ssScheda, segnalato, tipoScheda);
	        		AmAnagrafica amAna = anagraficaService.findAnagraficaByUserName(ssScheda.getAccesso().getOperatore());
	        		if(amAna != null) {
	        			schedaSegr.setOperatore(amAna.getCognome() + " " + amAna.getNome());
	        		}
	        		
	        		//controllo per cod fiscale se esiste già la cartella
	        		if(csScheda.getCsASoggetto() == null && segnalato != null) {
		        		it.webred.cs.csa.ejb.dto.BaseDTO bDto2 = new it.webred.cs.csa.ejb.dto.BaseDTO();
		        		CsUiCompBaseBean.fillEnte(bDto2);
		        		bDto2.setObj(segnalato.getAnagrafica().getCf());
		        		CsASoggetto csSogg = soggettoService.getSoggettiByCF(bDto2);
		        		if(csSogg != null) {
		        			if(csScheda.getFlgEsistente() == null || !csScheda.getFlgEsistente()) {
		        				csScheda.setFlgEsistente(true);
	    						bDto2.setObj(csScheda);
	    						schedaSegrService.updateSchedaSegr(bDto2);
		        			}
		        			if(csScheda.getCsASoggetto() == null) {
	    						csScheda.setCsASoggetto(csSogg);
	    					}
		        		}
	        		}
	        		
	        		schedaSegr.setCsSsSchedaSegr(csScheda);

					// info stato caso
	        		if(schedaSegr.getCsSsSchedaSegr().getCsASoggetto() != null) {
	        			CsACaso caso = csScheda.getCsASoggetto().getCsACaso();
						IterInfoStatoMan casoInfo = new IterInfoStatoMan();
						
						IterDTO itDto = new IterDTO();
						CsUiCompBaseBean.fillEnte(itDto);
						itDto.setIdCaso(caso.getId());
						CsItStep itStep = iterSessionBean.getLastIterStepByCaso(itDto);
						
						if( itStep != null )
							casoInfo.initialize( itStep );
						schedaSegr.setLastIterStepInfo(casoInfo);
	        		}
	        		
	        		data.add(schedaSegr);
	        		
				} catch (Exception e) {
					CsUiCompBaseBean.logger.error("ID SCHEDA: " + csScheda.getId() + e.getMessage(), e);
				}
	        }
			
	        //rowCount
			Integer dataSize;
			dataSize = schedaSegrService.getSchedeSegrCount(dto);
			this.setRowCount(dataSize);
		
		} catch (Exception e) {
			CsUiCompBaseBean.addErrorFromProperties("caricamento.error");
			CsUiCompBaseBean.logger.error(e.getMessage(), e);
		}
		
		return data;
    }
	
}
