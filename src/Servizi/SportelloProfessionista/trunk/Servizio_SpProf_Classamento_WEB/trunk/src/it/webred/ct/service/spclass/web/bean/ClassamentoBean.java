package it.webred.ct.service.spclass.web.bean;

import it.webred.ct.data.access.aggregator.elaborazioni.CalcoloValoriAttesiService;
import it.webred.ct.data.access.aggregator.elaborazioni.ElaborazioniService;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.DatiAttesiDTO;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.ZonaDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.FoglioMicrozonaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.service.spclass.web.SpProfClassamentoBaseBean;
import it.webred.ct.service.spclass.web.bean.util.CommonDataBean;
import it.webred.ct.service.spclass.web.bean.util.UserBean;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class ClassamentoBean extends SpProfClassamentoBaseBean {

	private static final long serialVersionUID = 1L;
	protected CommonDataBean common = (CommonDataBean)getBeanReference("commonDataBean");
	
	public ElaborazioniService elaborazioniService = (ElaborazioniService) getEjb("CT_Service", "CT_Service_Data_Access", "ElaborazioniServiceBean");
	
	public CalcoloValoriAttesiService calcoloValoriAttesiService = (CalcoloValoriAttesiService) getEjb("CT_Service", "CT_Service_Data_Access", "CalcoloValoriAttesiServiceBean");
	
	public DocfaService docfaService = (DocfaService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
	
	public CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
	protected DatiAttesiDTO datiAttesiDTO;
	protected ZonaDTO zonaDTO = new ZonaDTO();
	protected List<SelectItem> listaCategorieEdilizie;
	protected List<SelectItem> listaZC;
	protected List<SelectItem> listaFogli;

	private boolean visualizzaOutput = false;
	protected int numberOfRows = 0;
	
	public List<SelectItem> getListaCategorieEdilizie() {
		return listaCategorieEdilizie;
	}

	public void setListaCategorieEdilizie(List<SelectItem> listaCategorieEdilizie) {
		this.listaCategorieEdilizie = listaCategorieEdilizie;
	}

	public DatiAttesiDTO getDatiAttesiDTO() {
		return datiAttesiDTO;
	}

	public void setDatiAttesiDTO(DatiAttesiDTO datiAttesiDTO) {
		this.datiAttesiDTO = datiAttesiDTO;
	}

	public ZonaDTO getZonaDTO() {
		return zonaDTO;
	}

	public void setZonaDTO(ZonaDTO zonaDTO) {
		this.zonaDTO = zonaDTO;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isVisualizzaOutput() {
		return visualizzaOutput;
	}

	public void setVisualizzaOutput(boolean visualizzaOutput) {
		this.visualizzaOutput = visualizzaOutput;
	}
	
	public void hideOutPanel() {
		this.visualizzaOutput = false;
	}
	
	public void showOutPanel() {
		this.visualizzaOutput = true;
	}
	
	
	
	public void getMicrozona() {

		this.fillEnte(zonaDTO);
		
		List<FoglioMicrozonaDTO> lista = docfaService.getFoglioMicrozona(zonaDTO);
		
		if(lista!=null && lista.size()>0){
			zonaDTO.setNewMicrozona(lista.get(0).getNewMicrozona());
			zonaDTO.setOldMicrozona(lista.get(0).getOldMicrozona());
		}
		
		this.hideOutPanel();

	}

	public List<SelectItem> getListaFogli() {
		return listaFogli;
	}

	public void setListaFogli(List<SelectItem> listaFogli) {
		this.listaFogli = listaFogli;
	}
	
	public void setListaZC(List<SelectItem> listaZC) {
		this.listaZC = listaZC;
	}

	
	public List<SelectItem> getListaZC() {

		listaZC = new ArrayList<SelectItem>();
		String foglio = zonaDTO.getFoglio();
		RicercaOggettoDocfaDTO ro = new RicercaOggettoDocfaDTO();

		/*if (foglio != null) {
			ro.setFoglio((String) foglio);*/
			fillEnte(zonaDTO);
			
			List<FoglioMicrozonaDTO> list = docfaService.getFoglioMicrozona(zonaDTO);
			for (FoglioMicrozonaDTO dfm : list) {
				String zc = dfm.getZc();
				SelectItem si = new SelectItem(zc, zc);
				listaZC.add(si);
			}
			
		//}
		
		return listaZC;
	}
	
	public String init(){
		
		String esito = "success";
		
		datiAttesiDTO = new DatiAttesiDTO();
		
		zonaDTO = new ZonaDTO();
		this.fillEnte(zonaDTO);
		
		listaFogli = new ArrayList<SelectItem>();
		
		CeTBaseObject o = new CeTBaseObject();
		this.fillEnte(o);
		List<String> fogli = docfaService.getDocfaFogliMicrozona_ListaFogli(o);
		
		if (fogli != null){
		for (String f : fogli){
			SelectItem si = new SelectItem(f,f);
			listaFogli.add(si);
			}
		
		UserBean uBean = (UserBean) getBeanReference("userBean");
		if(uBean.getFoglioSpProf() != null)
			zonaDTO.setFoglio(uBean.getFoglioSpProf());
		else if (listaFogli.size()>0)
			 zonaDTO.setFoglio((String)listaFogli.get(0).getValue());
		}
		
		return esito;
		

	}

	public void calcola() {
		
		this.showOutPanel();
		this.getLogger().info("Foglio:" + zonaDTO.getFoglio());
		this.getLogger().info("CodCategoria:" + zonaDTO.getCodiceCategoriaEdilizia());
		this.getLogger().info("Mappale:" + zonaDTO.getMappale());
		this.getLogger().info("NewMicrozona:" + zonaDTO.getNewMicrozona());
		this.getLogger().info("OldMicrozona" + zonaDTO.getOldMicrozona());
		this.getLogger().info("Stato:" + zonaDTO.getStato());
		this.getLogger().info("TipoInterventp:" + zonaDTO.getTipoIntervento());
		this.getLogger().info("ZonaCensuaria:" + zonaDTO.getZonaCensuaria());
		this.getLogger().info("Consistenza:" + zonaDTO.getConsistenza());
		this.getLogger().info("Superficie:" + zonaDTO.getSuperficie());
		
	}


}
