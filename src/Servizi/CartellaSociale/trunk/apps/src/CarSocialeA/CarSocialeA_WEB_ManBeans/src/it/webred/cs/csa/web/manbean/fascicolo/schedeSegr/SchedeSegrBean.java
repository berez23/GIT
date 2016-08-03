package it.webred.cs.csa.web.manbean.fascicolo.schedeSegr;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSchedaSegrSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.InterventoDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.SchedaSegrDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.csa.web.manbean.schedaSegr.SchedaSegr;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsSsSchedaSegr;
import it.webred.cs.jsf.interfaces.IListaSchedeSegr;
import it.webred.cs.jsf.manbean.IterDialogMan;
import it.webred.cs.jsf.manbean.IterInfoStatoMan;
import it.webred.cs.jsf.manbean.SchedaPaiMan;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ejb.utility.ClientUtility;
import it.webred.ss.data.model.SsScheda;
import it.webred.ss.data.model.SsSchedaSegnalato;
import it.webred.ss.data.model.SsTipoScheda;
import it.webred.ss.ejb.client.SsSchedaSessionBeanRemote;
import it.webred.ss.ejb.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class SchedeSegrBean extends FascicoloCompBaseBean implements IListaSchedeSegr {

	private List<SchedaSegr> lazyListaSchedeSegrModel;
	private SchedaSegr schedaSelected;
	
	@ManagedProperty( value="#{iterDialogMan}")
	private IterDialogMan iterDialogMan;
	
	private AccessTableSchedaSegrSessionBeanRemote schedaSegrService = (AccessTableSchedaSegrSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSchedaSegrSessionBean");
	
	@Override
	public void initializeData() {
		
		try{
			
			lazyListaSchedeSegrModel = new ArrayList<SchedaSegr>();
			AccessTableSchedaSegrSessionBeanRemote schedaSegrService = (AccessTableSchedaSegrSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSchedaSegrSessionBean");
			SsSchedaSessionBeanRemote ssSchedaSegrService = (SsSchedaSessionBeanRemote) ClientUtility.getEjbInterface("SegretariatoSoc", "SegretariatoSoc_EJB", "SsSchedaSessionBean");
			AnagraficaService anagraficaService = (AnagraficaService) ClientUtility.getEjbInterface("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
			AccessTableIterStepSessionBeanRemote iterSessionBean = (AccessTableIterStepSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableIterStepSessionBean");
			
			SchedaSegrDTO dto = new SchedaSegrDTO();
			CsUiCompBaseBean.fillEnte(dto);
			dto.setIdAnagrafica(idSoggetto);
			
			List<CsSsSchedaSegr> list = schedaSegrService.getSchedeSegr(dto);
			
			BaseDTO bDto = new BaseDTO();
			CsUiCompBaseBean.fillEnte(bDto);
			for(CsSsSchedaSegr csScheda: list){
				bDto.setObj(csScheda.getId());
				SsScheda ssScheda = ssSchedaSegrService.readScheda(bDto);
        		bDto.setObj(ssScheda.getSegnalato());
        		SsSchedaSegnalato segnalato = ssSchedaSegrService.readSegnalatoById(bDto);
        		bDto.setObj(ssScheda.getTipo());
        		SsTipoScheda tipoScheda = ssSchedaSegrService.readTipoSchedaById(bDto);
        		SchedaSegr schedaSegr = new SchedaSegr(ssScheda, segnalato, tipoScheda);
        		
        		schedaSegr.setShowStatoCartella(false);
        		AmAnagrafica amAna = anagraficaService.findAnagraficaByUserName(ssScheda.getAccesso().getOperatore());
        		if(amAna != null) {
        			schedaSegr.setOperatore(amAna.getCognome() + " " + amAna.getNome());
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
        		
        		lazyListaSchedeSegrModel.add(schedaSegr);
			}
		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	public ActionListener getCloseDialog() {
	    return new ActionListener() {
	        @Override
	        public void processAction(ActionEvent event) throws AbortProcessingException {
	        	//loadListaCasi();
	        }
	    };
	}
	
	public IterDialogMan getIterDialogMan() {
		return iterDialogMan;
	}

	public void setIterDialogMan(IterDialogMan iterDialogMan) {
		this.iterDialogMan = iterDialogMan;
	}

	public List<SchedaSegr> getLazyListaSchedeSegrModel() {
		return lazyListaSchedeSegrModel;
	}

	public void setLazyListaSchedeSegrModel(
			List<SchedaSegr> lazyListaSchedeSegrModel) {
		this.lazyListaSchedeSegrModel = lazyListaSchedeSegrModel;
	}
	
	public boolean isRenderListaSchedeSegr() {
		return checkPermesso(DataModelCostanti.PermessiSchedeSegr.VISUALIZZA_SCHEDE_SEGR);
	}
	
	public boolean isRenderNuovaCartella() {
		return false;
	}
	
	public boolean isRenderCaricaCartella() {
		return false;
	}

	public AccessTableSchedaSegrSessionBeanRemote getSchedaSegrService() {
		return schedaSegrService;
	}

	public void setSchedaSegrService(
			AccessTableSchedaSegrSessionBeanRemote schedaSegrService) {
		this.schedaSegrService = schedaSegrService;
	}

	public SchedaSegr getSchedaSelected() {
		return schedaSelected;
	}

	public void setSchedaSelected(SchedaSegr schedaSelected) {
		this.schedaSelected = schedaSelected;
	}
	
}
