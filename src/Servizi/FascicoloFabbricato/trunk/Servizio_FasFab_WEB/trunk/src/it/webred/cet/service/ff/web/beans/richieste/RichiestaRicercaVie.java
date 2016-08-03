package it.webred.cet.service.ff.web.beans.richieste;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;

@SessionScoped
public class RichiestaRicercaVie extends FFBaseBean implements Serializable {
	
	private String nomeVia;
	private CatastoService catastoService;
	private List<SelectItem> vieItemList = new ArrayList<SelectItem>();
	private String idVia = "0";
	private String idCiv = "0";
	private String msg = "Inserisci i criteri di ricerca";
	private String msgCiv = "Inserisci i criteri di ricerca";
	private List<SelectItem> civItemList = new ArrayList<SelectItem>();
	private List<ParticellaKeyDTO> partList = new ArrayList<ParticellaKeyDTO>();
	
	
	public String getNomeVia() {
		return nomeVia;
	}
	
	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}
	
	public CatastoService getCatastoService() {
		return catastoService;
	}
	
	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}
	
	public void reset() {
		nomeVia = null;
		msg = "Inserisci i criteri di ricerca";
		msgCiv = "Inserisci i criteri di ricerca";
		vieItemList = new ArrayList<SelectItem>();
		vieItemList.add(new SelectItem("0", msg));
		idVia = "0";
		civItemList = new ArrayList<SelectItem>();
		civItemList.add(new SelectItem("0", msgCiv));
		idCiv = "0";
		partList = new ArrayList<ParticellaKeyDTO>();		
	}
	
	public void ricercaVie() {
		logger.debug("Ricerca vie ["+nomeVia+"]");
		
		resetRicercaVie();
		
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		ro.setVia(nomeVia);
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		ro.setCodEnte(super.getEnte());
		
		List<Sitidstr> vieList = catastoService.getListaVie(ro);
		
		for (Sitidstr via : vieList) {
			//logger.debug("Via ["+via+"]");
			SelectItem item = new SelectItem();
			item.setValue(via.getPkidStra());
			
			item.setLabel(via.getPrefisso() + " " + via.getNome());
			vieItemList.add(item);
		}
		
	}
	
	private void resetRicercaVie() {
		msg = "Seleziona la via";
		msgCiv = "Inserisci i criteri di ricerca";	
		vieItemList = new ArrayList<SelectItem>();
		vieItemList.add(new SelectItem("0", msg));
		idVia = "0";
		civItemList = new ArrayList<SelectItem>();
		civItemList.add(new SelectItem("0", msgCiv));
		idCiv = "0";
		partList = new ArrayList<ParticellaKeyDTO>();
	}
	
	private void resetRicercaCivici() {
		msgCiv = "Seleziona il civico";
		civItemList = new ArrayList<SelectItem>();
		civItemList.add(new SelectItem("0", msgCiv));
		idCiv = "0";
		partList = new ArrayList<ParticellaKeyDTO>();
	}
	
	public void valueChanged(ValueChangeEvent event) {
		//logger.debug("Change event");
		String idComp = event.getComponent().getId();
		//logger.debug("ID Comp ["+idComp+"]");
		
		partList = new ArrayList<ParticellaKeyDTO>();
		
		if (idComp.equals("viaList")) {
			logger.debug("Via list changed. Reload civici. ["+event.getNewValue()+"]");
			
			resetRicercaCivici();
			
			BigDecimal pkIdStra = new BigDecimal(event.getNewValue() == null || event.getNewValue().equals("") ? "-1" : (String) event.getNewValue());
			loadCivici(pkIdStra);			
			idVia = "" + pkIdStra.longValue();
		}
		else if (idComp.equals("civList")) {
			// Recupero la particella
			getParticellaList(event.getNewValue() == null || event.getNewValue().equals("") ? "-1" : (String) event.getNewValue());
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
		
		for (Siticivi civ : result) {
			SelectItem item = new SelectItem();
			item.setValue(civ.getPkidCivi());
			item.setLabel(civ.getCivico());			
			civItemList.add(item);
		}
	}


	public List<SelectItem> getVieItemList() {
		//logger.debug("Vie List ["+vieItemList+"]");
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

	public String getMsgCiv() {
		return msgCiv;
	}

	public void setMsgCiv(String msgCiv) {
		this.msgCiv = msgCiv;
	}

	public List<SelectItem> getCivItemList() {
		return civItemList;
	}

	public void setCivItemList(List<SelectItem> civItemList) {
		this.civItemList = civItemList;
	}

	public List<ParticellaKeyDTO> getPartList() {
		return partList;
	}

	public void setPartList(List<ParticellaKeyDTO> partList) {
		this.partList = partList;
	}
	
	

}
