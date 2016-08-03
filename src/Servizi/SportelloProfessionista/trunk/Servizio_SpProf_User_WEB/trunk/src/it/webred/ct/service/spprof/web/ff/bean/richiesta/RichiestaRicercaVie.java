package it.webred.ct.service.spprof.web.ff.bean.richiesta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.service.ff.data.access.common.FFCommonService;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;

public class RichiestaRicercaVie extends SpProfBaseBean implements Serializable {
	
	private CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
	private String nomeVia;
	private String civ;
	private List<SelectItem> vieItemList = new ArrayList<SelectItem>();
	private String idVia;
	private String idCiv;
	private String msg="Inserisci i criteri di ricerca";
	private List<SelectItem> civItemList = new ArrayList<SelectItem>();
	private boolean civActive = false;
	private boolean btnActive = false;
	private String nomeViaSel;
	private String numCivSel;
	private List<ParticellaKeyDTO> partList = new ArrayList<ParticellaKeyDTO>();
	
	
	public String getNomeVia() {
		return nomeVia;
	}
	
	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}
	
	public String getCiv() {
		return civ;
	}
	
	public void setCiv(String civ) {
		this.civ = civ;
	}
	
	public CatastoService getCatastoService() {
		return catastoService;
	}
	
	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}
	
	public void ricercaVie() {
		System.out.println("Ricerca vie ["+nomeVia+"]");
	
		this.nomeViaSel = null;
		this.numCivSel = null;
		this.idVia = null;
		this.idCiv = null;
		
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		ro.setVia(nomeVia);
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		ro.setCodEnte(super.getEnte());
		
		List<Sitidstr> vieList = catastoService.getListaVie(ro);
		
		resetData();
		
		for (Sitidstr via : vieList) {
			//System.out.println("Via ["+via+"]");
			SelectItem item = new SelectItem();
			item.setValue(via.getPkidStra());
			
			item.setLabel(via.getPrefisso() + " " + via.getNome());
			vieItemList.add(item);
		}
		

		msg = "Seleziona la via";
		civItemList = new ArrayList<SelectItem>();
	}
	
	private void resetData() {
		vieItemList = new ArrayList<SelectItem>();
		partList = new ArrayList<ParticellaKeyDTO>();
	}
	
	public void valueChanged(ValueChangeEvent event) {
		//System.out.println("Change event");
		String idComp = event.getComponent().getId();
		//System.out.println("ID Comp ["+idComp+"]");
		
		partList = new ArrayList<ParticellaKeyDTO>();
		
		if (idComp.equals("viaList")) {
			System.out.println("Via list changed. Reload civici. ["+event.getNewValue()+"]");
			BigDecimal pkIdStra = new BigDecimal((String) event.getNewValue());
			loadCivici(pkIdStra);
			
			idVia = "" + pkIdStra.longValue();
			
			
			
		}
		else if (idComp.equals("civList")) {
			// Recupero la particella
			getParticellaList((String) event.getNewValue());
		}
	}
	
	
	private void getParticellaList(String pkIdCiv) {
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		CatastoSearchCriteria criteria = new CatastoSearchCriteria();
		criteria.setIdCivico(pkIdCiv);
		criteria.setCodNazionale(super.getEnte());
		ro.setCriteria(criteria);
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		ro.setCodEnte(super.getEnte());
		
		partList = catastoService.getListaParticelle(ro);
		
	}
	
	private void loadCivici(BigDecimal pkIdStra) {
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		ro.setPkIdStra(pkIdStra);
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		ro.setCodEnte(super.getEnte());

		List<Siticivi> result = catastoService.getListaIndirizzi(ro);
		
		civItemList = new ArrayList<SelectItem>();
		
		for (Siticivi civ : result) {
			SelectItem item = new SelectItem();
			item.setValue(civ.getPkidCivi());
			item.setLabel(civ.getCivico());			
			civItemList.add(item);
		}

	}


	public List<SelectItem> getVieItemList() {
		//System.out.println("Vie List ["+vieItemList+"]");
		return vieItemList;
	}

	public void setVieItemList(List<SelectItem> vieItemList) {
		this.vieItemList = vieItemList;
	}
	

	public String getIdVia() {
		return idVia;
	}

	public void setIdVia(String idVia) {
		this.idVia = idVia;
	}

	public String getIdCiv() {
		return idCiv;
	}

	public void setIdCiv(String idCiv) {
		this.idCiv = idCiv;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<SelectItem> getCivItemList() {
		return civItemList;
	}

	public void setCivItemList(List<SelectItem> civItemList) {
		this.civItemList = civItemList;
	}

	public boolean isCivActive() {
		return civActive;
	}

	public void setCivActive(boolean civActive) {
		this.civActive = civActive;
	}

	public boolean isBtnActive() {
		return btnActive;
	}

	public void setBtnActive(boolean btnActive) {
		this.btnActive = btnActive;
	}

	public String getNomeViaSel() {
		return nomeViaSel;
	}

	public void setNomeViaSel(String nomeViaSel) {
		this.nomeViaSel = nomeViaSel;
	}

	public String getNumCivSel() {
		return numCivSel;
	}

	public void setNumCivSel(String numCivSel) {
		this.numCivSel = numCivSel;
	}

	public List<ParticellaKeyDTO> getPartList() {
		return partList;
	}

	public void setPartList(List<ParticellaKeyDTO> partList) {
		this.partList = partList;
	}
	
	

}
