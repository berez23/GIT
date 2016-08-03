package it.webred.cs.csa.web.manbean.fascicolo.isee;

import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDIsee;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.jsf.interfaces.IIsee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.xml.crypto.Data;

import org.primefaces.context.RequestContext;

public class IseeBean extends FascicoloCompBaseBean implements IIsee {
		
	private List<CsDIsee> listaIsee;
	private CsDIsee isee;
	private int idxSelected;
	private List<SelectItem> listaTipologie;
	private List<SelectItem> listaAnni;
	
	AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");
	
	@Override
	public void initializeData() {
		
		try{
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(idCaso);
			listaIsee = diarioService.findIseeByCaso(dto);

		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		
	}

	@Override
	public void nuovo() {
		
		isee = new CsDIsee();
		
	}
	
	@Override
	public void carica() {
		
		isee = listaIsee.get(idxSelected);
		
	}
	
	public void handleChangeDataISEE(javax.faces.event.AjaxBehaviorEvent event) {
		Date data = ((Date)((javax.faces.component.UIInput)event.getComponent()).getValue());
		if(data != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(data);
			c.add(Calendar.YEAR, 1);
			isee.setDataScadenzaIsee(c.getTime());
		}
	}
	
	@Override
	public void salva() {
		
		try{
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(isee);
			
			if(isee.getDiarioId() != null) {
				
				//modifica
				isee.setUsrMod(dto.getUserId());
				isee.setDtMod(new Date());
				diarioService.updateIsee(dto);
			}
			else {
				
				//nuovo
				if(!validaIsee()) {
					return;
				}
				
				CsDDiario csd = new CsDDiario();
		        CsACaso csa = new CsACaso();
		        CsTbTipoDiario cstd = new CsTbTipoDiario(); 
		        csa.setId(idCaso);
		        cstd.setId(new Long(DataModelCostanti.TipoDiario.ISEE_ID)); 
		        csd.setCsACaso(csa);
		        csd.setCsTbTipoDiario(cstd);
		        CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
		        csd.setCsOOperatoreSettore(opSettore);
		        isee.setCsDDiario(csd);
		        isee.setUserIns(dto.getUserId());
		        isee.setDtIns(new Date());
				
				csd = diarioService.saveIsee(dto);

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
			dto.setObj(isee);
			diarioService.deleteIsee(dto);
		
			initializeData();
			
			addInfoFromProperties("elimina.ok");
		
		} catch (Exception e) {
			addErrorFromProperties("elimina.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	private boolean validaIsee() {
		
		boolean ok = true;
		
		if(isee.getAnnoRif() == null || "".equals(isee.getAnnoRif())) {
			ok = false;
			addError("Anno è un campo obbligatorio", "");
		}
		
		if(isee.getTipologia() == null || "".equals(isee.getTipologia())) {
			ok = false;
			addError("Tipologia è un campo obbligatorio", "");
		}
		
		if(isee.getDataIsee() == null) {
			ok = false;
			addError("Data dichiarazione ISEE è un campo obbligatorio", "");
		}
		
		if(isee.getDataScadenzaIsee() == null) {
			ok = false;
			addError("Data scadenza ISEE è un campo obbligatorio", "");
		}
		
		if(isee.getIse() == null || isee.getIse().intValue() == 0) {
			ok = false;
			addError("ISE è un campo obbligatorio", "");
		}
		
		if(isee.getIsee() == null || isee.getIsee().intValue() == 0) {
			ok = false;
			addError("ISEE è un campo obbligatorio", "");
		}
		
		if(isee.getScalaEquivalenza() == null || isee.getScalaEquivalenza().intValue() == 0) {
			ok = false;
			addError("Scala equivalenza è un campo obbligatorio", "");
		}
		
		return ok;
	}
	
	public List<CsDIsee> getListaIsee() {
		return listaIsee;
	}

	public void setListaIsee(List<CsDIsee> listaIsee) {
		this.listaIsee = listaIsee;
	}

	public List<SelectItem> getListaTipologie() {
		if(listaTipologie == null) {
			listaTipologie = new ArrayList<SelectItem>();
			listaTipologie.add(new SelectItem("", "-> Scegli"));
			listaTipologie.add(new SelectItem("Familiare", "Familiare"));
			listaTipologie.add(new SelectItem("Individuale", "Individuale"));
		}
		return listaTipologie;
	}

	public void setListaTipologie(List<SelectItem> listaTipologie) {
		this.listaTipologie = listaTipologie;
	}

	public CsDIsee getIsee() {
		return isee;
	}

	public void setIsee(CsDIsee isee) {
		this.isee = isee;
	}

	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	public List<SelectItem> getListaAnni() {
		if(listaAnni == null) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			listaAnni = new ArrayList<SelectItem>();
			listaAnni.add(new SelectItem("", "-> Scegli"));
			listaAnni.add(new SelectItem(String.valueOf(year), String.valueOf(year)));
			listaAnni.add(new SelectItem(String.valueOf(year-1), String.valueOf(year-1)));
			listaAnni.add(new SelectItem(String.valueOf(year-2), String.valueOf(year-2)));
			listaAnni.add(new SelectItem(String.valueOf(year-3), String.valueOf(year-3)));
			listaAnni.add(new SelectItem(String.valueOf(year-4), String.valueOf(year-4)));
			listaAnni.add(new SelectItem(String.valueOf(year-5), String.valueOf(year-5)));
		}
		return listaAnni;
	}

	public void setListaAnni(List<SelectItem> listaAnni) {
		this.listaAnni = listaAnni;
	};

}
