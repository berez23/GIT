package it.webred.cs.csa.web.manbean.scheda.operatori;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsACasoOpeTipoOpePK;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsTbTipoOperatore;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.IOperatori;
import it.webred.cs.jsf.manbean.DatiValGestioneMan;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.dto.utility.KeyValuePairBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class OperatoriBean extends DatiValGestioneMan  implements IOperatori {

	protected AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
	protected AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");	
	protected AccessTableOperatoreSessionBeanRemote operatoreService = (AccessTableOperatoreSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableOperatoreSessionBean");
	protected AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
	protected AnagraficaService anagraficaService = (AnagraficaService) getEjb("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
	
	private Long idTipoOperatore;
	private Long idSettore;
	protected CsASoggetto soggetto;
	protected Long soggettoId;
	
	public void initialize(Long sId) {
	
		soggettoId = sId;

		if(soggettoId != null) {
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(soggettoId);
			
			soggetto = soggettoService.getSoggettoById(dto);
		
			List<CsTbTipoOperatore> lst = getTipiOperatore();
			if(lst!=null && lst.size()>0)
				idTipoOperatore = lst.get(0).getId();
			
			dto.setObj(soggetto.getCsACaso().getId());
			List<CsACasoOpeTipoOpe> lstCasoOper = casoService.getListaOperatoreTipoOpByCasoId(dto);
			lstComponents = new ArrayList<ValiditaCompBaseBean>();
			for(CsACasoOpeTipoOpe co : lstCasoOper){
				OperatoriComp b = new OperatoriComp();
				b.setDataFine(co.getId().getDataFineApp());
				b.setDataInizio(co.getDataInizioApp());
				
				AmAnagrafica operatoreAnagrafica = null;
				CsOOperatore oper = co.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore();
				if(oper != null) 
					operatoreAnagrafica = anagraficaService.findAnagraficaByUserName(oper.getUsername());
				
				String descrizione = "";
				if(operatoreAnagrafica!=null)
					descrizione = operatoreAnagrafica.getCognome()+" "+operatoreAnagrafica.getNome();
				b.setDescrizione(descrizione);
				b.setId(co.getCsOOperatoreTipoOperatore().getId());
				b.setTipo(co.getCsOOperatoreTipoOperatore().getCsTbTipoOperatore().getDescrizione());
				b.setSettore(co.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOSettore().getNome());
				b.setResponsabile(co.getFlagResponsabile());
				lstComponents.add(b);
				
			}
		}
	}
	
	@Override
	protected String getDescrizioneTipo(){
		List<CsTbTipoOperatore> lst = getTipiOperatore();
		if(idTipoOperatore!=null){
			int i=0;
			boolean trovato = false;
			while(i<lst.size() && !trovato){
				CsTbTipoOperatore to = lst.get(i);
				if(to.getId()==this.idTipoOperatore.longValue())
				   return to.getDescrizione();
				
				i++;
			}
		}
		return "";
	}
	
	private List<CsTbTipoOperatore> getTipiOperatore(){
		List<CsTbTipoOperatore> beanLst = new ArrayList<CsTbTipoOperatore>();
		try {
			CeTBaseObject cet = new CeTBaseObject();
			fillEnte(cet);
			beanLst = confService.getTipiOperatore(cet);
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		return beanLst;
	}
	
	@Override
	public List<SelectItem> getLstTipoOperatore() {
		ArrayList<SelectItem> lst = new ArrayList<SelectItem>();
		try {
			List<CsTbTipoOperatore> beanLst = this.getTipiOperatore();
			if (beanLst != null) {
				for (CsTbTipoOperatore s : beanLst) 
					lst.add(new SelectItem(s.getId(), s.getDescrizione()));
			}
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		return lst;
	}
	
	@Override
	public List<SelectItem> getLstSettori() {
		ArrayList<SelectItem> lst = new ArrayList<SelectItem>();
		try {
			OperatoreDTO dto = new OperatoreDTO();
			fillEnte(dto);
			List<CsOSettore> beanLst = operatoreService.findSettori(dto);
			if (beanLst != null) {
				for (CsOSettore s : beanLst) 
					lst.add(new SelectItem(s.getId(), s.getNome()));
			}
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		return lst;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<KeyValuePairBean> getLstItems() {
		
		lstItems = new ArrayList<KeyValuePairBean>();
		try {
			//filtro per tipo e settore
			BaseDTO bo = new BaseDTO();
			bo.setObj(this.idTipoOperatore);
			fillEnte(bo);
			List<CsOOperatoreTipoOperatore> lstOper = operatoreService.getOperatoriByTipoId(bo);
			if (lstOper != null) {
				for (CsOOperatoreTipoOperatore obj : lstOper) {
					AmAnagrafica operatoreAnagrafica = null;
					CsOOperatoreSettore operSett = obj.getCsOOperatoreSettore();
					if(operSett != null && operSett.getCsOSettore().getId().equals(this.idSettore)) {
						operatoreAnagrafica = anagraficaService.findAnagraficaByUserName(operSett.getCsOOperatore().getUsername());
					
						String descrizione = "c.f. "+operSett.getCsOOperatore().getUsername();
						if(operatoreAnagrafica!=null)
							descrizione = operatoreAnagrafica.getCognome()+" "+operatoreAnagrafica.getNome()+", "+ descrizione;
						lstItems.add(new KeyValuePairBean(obj.getId(), descrizione));
					}
				}
			}		
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		
		return lstItems;
	}
	
	@Override
	public void aggiungiSelezionato() {
		
		try{
			
			if(itemSelezionato != null && !"".equals(itemSelezionato)){

				String[] str = itemSelezionato.split("\\|");
				OperatoriComp comp = new OperatoriComp();
				comp.setId(new Long(str[0]));
				comp.setTipo(this.getDescrizioneTipo());
				comp.setDescrizione(str[1]);
				comp.setDataInizio(new Date());
				comp.setDataFine(DataModelCostanti.END_DATE);
				
				if(checkExists(comp))
					addWarning("L'elemento selezionato è già presente", null);
				else lstComponents.add(comp);
			} else
				addWarning("Seleziona un elemento", null);
			
		} catch (Exception e) {
			addError("Errore durante l'inserimento dell'elemento selezionato", null);
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void salva() {
		itemSelezionato = null;
		List<ValiditaCompBaseBean> lstTempActive = getActiveList();
		try {
			
			lstComponentsActive = lstTempActive;
			
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);	
			
			if(soggetto != null) {
				
				dto.setObj(soggetto.getCsACaso().getId());
				casoService.eliminaOperatoreTipoOpByCasoId(dto);
				
				for(ValiditaCompBaseBean comp: getLstComponents()) {
					CsACasoOpeTipoOpe cs = new CsACasoOpeTipoOpe();
					CsACasoOpeTipoOpePK csPK = new CsACasoOpeTipoOpePK();
					
					cs.setCsACaso(soggetto.getCsACaso());
					cs.setDataInizioApp(comp.getDataInizio());
					cs.setDataInizioSys(new Date());
					cs.setDtIns(new Date());
					cs.setUserIns(dto.getUserId());
					
					dto.setObj(comp.getId());
					cs.setCsOOperatoreTipoOperatore(operatoreService.getOperatoreTipoOpById(dto));
					
					csPK.setCasoId(soggetto.getCsACaso().getId());
					csPK.setOperatoreTipoOperatoreId(comp.getId());
					csPK.setDataFineApp(comp.getDataFine());
					cs.setId(csPK);
					
					dto.setObj(cs);
					casoService.salvaOperatoreCaso(dto);
					
				}
				RequestContext.getCurrentInstance().addCallbackParam("saved", true);
			}
			
			addInfoFromProperties("salva.ok");
		
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}	
	}
	

	public Long getIdTipoOperatore() {
		return idTipoOperatore;
	}

	public void setIdTipoOperatore(Long idTipoOperatore) {
		this.idTipoOperatore = idTipoOperatore;
	}

	public Long getIdSettore() {
		return idSettore;
	}

	public void setIdSettore(Long idSettore) {
		this.idSettore = idSettore;
	}

}
