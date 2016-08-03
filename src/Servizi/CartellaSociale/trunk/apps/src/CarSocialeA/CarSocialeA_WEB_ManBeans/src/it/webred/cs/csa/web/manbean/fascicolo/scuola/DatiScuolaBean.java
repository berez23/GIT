package it.webred.cs.csa.web.manbean.fascicolo.scuola;

import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDScuola;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsTbScuola;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.data.model.CsTbTipoScuola;
import it.webred.cs.jsf.interfaces.IDatiScuola;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

public class DatiScuolaBean extends FascicoloCompBaseBean implements IDatiScuola {

	private List<CsDScuola> listaScuole;
	private List<SelectItem> listaAnni;
	private List<SelectItem> listaTipi;
	private List<SelectItem> listaNomi;
	private List<SelectItem> listaGradi;
	private List<SelectItem> listaProgetti;
	private int idxSelected;
	private CsDScuola scuola;
	private boolean renderScuole;
		
	AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");
	
	@Override
	public void initializeData() {
		
		try{
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(idCaso);
			listaScuole = diarioService.findScuoleByCaso(dto);

		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void nuovo() {
		
		scuola = new CsDScuola();
		scuola.setCsTbScuola(new CsTbScuola());
		scuola.setCsTbTipoScuola(new CsTbTipoScuola());
		listaNomi = new ArrayList<SelectItem>();
		listaNomi.add(new SelectItem("", "-> Scegli"));
		
	}
	
	@Override
	public void carica() {
		
		scuola = listaScuole.get(idxSelected);
		listaNomi.add(new SelectItem(scuola.getCsTbScuola().getId(), scuola.getCsTbScuola().getDescrizione()));
		
	}
	
	@Override
	public void salva() {
		
		try{
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(scuola);
			
			if(scuola.getDiarioId() != null) {
				
				//modifica
				scuola.setUsrMod(dto.getUserId());
				scuola.setDtMod(new Date());
				diarioService.updateScuola(dto);
			}
			else {
				
				//nuovo
				if(!validaScuola()) {
					return;
				}
				
				CsDDiario csd = new CsDDiario();
		        CsACaso csa = new CsACaso();
		        CsTbTipoDiario cstd = new CsTbTipoDiario(); 
		        csa.setId(idCaso);
		        cstd.setId(new Long(DataModelCostanti.TipoDiario.DATI_SCUOLA_ID)); 
		        csd.setCsACaso(csa);
		        csd.setCsTbTipoDiario(cstd);
		        CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
		        csd.setCsOOperatoreSettore(opSettore);
		        scuola.setCsDDiario(csd);
		        scuola.setUserIns(dto.getUserId());
		        scuola.setDtIns(new Date());
				
				csd = diarioService.saveScuola(dto);

			}
			
			initializeData();
			
			addInfoFromProperties("salva.ok");
			RequestContext.getCurrentInstance().addCallbackParam("saved", true);
			
		} catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	public void elimina() {
		
		try {
		
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(scuola);
			diarioService.deleteScuola(dto);
		
			initializeData();
			
			addInfoFromProperties("elimina.ok");
		
		} catch (Exception e) {
			addErrorFromProperties("elimina.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	private boolean validaScuola() {
		
		boolean ok = true;
		
		/*if(diarioDocsMan.getuFileMan().getDocumentiUploaded().isEmpty()) {
			ok = false;
			addError("Aggiungere un documento", "");
		}*/
		
		return ok;
	}
	
	public void aggiornaNomi() {
		renderScuole = false;
		if(scuola.getAnnoScolastico() != null && !"".equals(scuola.getAnnoScolastico())
				&& scuola.getCsTbTipoScuola().getId() != null) {
			listaNomi = new ArrayList<SelectItem>();
			listaNomi.add(new SelectItem("", "-> Scegli"));
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(scuola.getAnnoScolastico());
			dto.setObj2(scuola.getCsTbTipoScuola().getId());
			List<CsTbScuola> lstCs = confService.getScuoleByAnnoTipo(dto);
			for(CsTbScuola cs: lstCs) 
				listaNomi.add(new SelectItem(cs.getId(), cs.getDescrizione()));
			renderScuole = true;
		}
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
	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	@Override
	public List<CsDScuola> getListaScuole() {
		return listaScuole;
	}

	public void setListaScuole(List<CsDScuola> listaScuole) {
		this.listaScuole = listaScuole;
	}

	@Override
	public List<SelectItem> getListaAnni() {
		if(listaAnni == null) {
			listaAnni = new ArrayList<SelectItem>();
			listaAnni.add(new SelectItem("", "-> Scegli"));
			Map<String, CsTbScuola> map = new HashMap<String, CsTbScuola>();
			CeTBaseObject cet = new CeTBaseObject();
			fillEnte(cet);
			List<CsTbScuola> lstCs = confService.getScuole(cet);
			for(CsTbScuola cs: lstCs) {
				if(!map.containsKey(cs.getAnnoScolastico())) {
					listaAnni.add(new SelectItem(cs.getAnnoScolastico(), cs.getAnnoScolastico()));
					map.put(cs.getAnnoScolastico(), cs);
				}
			}
		}
		return listaAnni;
	}

	public void setListaAnni(List<SelectItem> listaAnni) {
		this.listaAnni = listaAnni;
	}

	@Override
	public List<SelectItem> getListaTipi() {
		if(listaTipi == null) {
			listaTipi = new ArrayList<SelectItem>();
			listaTipi.add(new SelectItem("", "-> Scegli"));
			CeTBaseObject cet = new CeTBaseObject();
			fillEnte(cet);
			List<CsTbTipoScuola> lstCs = confService.getTipoScuole(cet);
			for(CsTbTipoScuola cs: lstCs) 
				listaTipi.add(new SelectItem(cs.getId(), cs.getDescrizione()));
		}
		return listaTipi;
	}

	public void setListaTipi(List<SelectItem> listaTipi) {
		this.listaTipi = listaTipi;
	}

	@Override
	public List<SelectItem> getListaNomi() {
		return listaNomi;
	}

	public void setListaNomi(List<SelectItem> listaNomi) {
		this.listaNomi = listaNomi;
	}

	@Override
	public List<SelectItem> getListaGradi() {
		if(listaGradi == null) {
			listaGradi = new ArrayList<SelectItem>();
			listaGradi.add(new SelectItem("", "-> Scegli"));
			listaGradi.add(new SelectItem("1", "1"));
			listaGradi.add(new SelectItem("2", "2"));
			listaGradi.add(new SelectItem("3", "3"));
			listaGradi.add(new SelectItem("4", "4"));
			listaGradi.add(new SelectItem("5", "5"));
		}
		return listaGradi;
	}

	public void setListaGradi(List<SelectItem> listaGradi) {
		this.listaGradi = listaGradi;
	}

	@Override
	public List<SelectItem> getListaProgetti() {
		if(listaProgetti == null) {
			listaProgetti = new ArrayList<SelectItem>();
			listaProgetti.add(new SelectItem("Legge 68/99", "Legge 68/99"));
			listaProgetti.add(new SelectItem("Finalizzazione assunzione", "Finalizzazione assunzione"));
			listaProgetti.add(new SelectItem("Laboratorio carcere", "Laboratorio carcere"));
			listaProgetti.add(new SelectItem("Altro", "Altro"));
		}
		return listaProgetti;
	}

	public void setListaProgetti(List<SelectItem> listaProgetti) {
		this.listaProgetti = listaProgetti;
	}

	@Override
	public CsDScuola getScuola() {
		return scuola;
	}

	public void setScuola(CsDScuola scuola) {
		this.scuola = scuola;
	}

	@Override
	public boolean isRenderScuole() {
		return renderScuole;
	}

	public void setRenderScuole(boolean renderScuole) {
		this.renderScuole = renderScuole;
	}

}
