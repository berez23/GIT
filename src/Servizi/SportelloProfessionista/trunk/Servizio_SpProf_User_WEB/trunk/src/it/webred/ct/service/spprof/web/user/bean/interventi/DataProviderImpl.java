package it.webred.ct.service.spprof.web.user.bean.interventi;

import it.webred.ct.service.spprof.data.access.SpProfInterventoService;
import it.webred.ct.service.spprof.data.access.dto.InterventoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.util.UserBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

public class DataProviderImpl extends SpProfBaseBean implements DataProvider {

	public SpProfInterventoService spProfInterventoService = (SpProfInterventoService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfInterventoServiceBean");
	
	private InterventoSearchCriteria criteria = new InterventoSearchCriteria();
	
	private List<SelectItem> listaStati = new ArrayList<SelectItem>();
	private List<SelectItem> listaSoggetti = new ArrayList<SelectItem>();
	private List<SelectItem> listaConcNumero = new ArrayList<SelectItem>();
	private List<SelectItem> listaProtNumero = new ArrayList<SelectItem>();

	public Long getInterventiCount() {
		
		try {

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setUserId(getUsername());
			UserBean uBean = (UserBean) getBeanReference("userBean");
			if(uBean.getAdministrator()){
				criteria.setEnteId(getEnte());
				criteria.setUserId(getUsername());
				return spProfInterventoService.getCountIntervento(criteria);
			}
			else return spProfInterventoService.getCountInterventoByUser(dto);

		} catch (Throwable t) {
			super.addErrorMessage("interventi.error", t.getMessage());
			logger.error(t);
			return new Long(0);
		}
	}

	public List<SSpIntervento> getInterventiByRange(int start, int rowNumber) {

		try {

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setUserId(getUsername());
			dto.setStart(start);
			dto.setRecord(rowNumber);
			UserBean uBean = (UserBean) getBeanReference("userBean");
			if(uBean.getAdministrator()){
				criteria.setEnteId(getEnte());
				criteria.setUserId(getUsername());
				criteria.setStart(start);
				criteria.setRecord(rowNumber);
				return spProfInterventoService.getIntervento(criteria);
			}
			else return spProfInterventoService.getInterventoByUser(dto);

		} catch (Throwable t) {
			super.addErrorMessage("interventi.error", t.getMessage());
			logger.error(t);
			return new ArrayList<SSpIntervento>();
		}
	}

	public void resetData() {
		criteria = new InterventoSearchCriteria();
	}

	public InterventoSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(InterventoSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public List<SelectItem> getListaStati() {
		if(listaStati.size() == 0){
			List<String> st = spProfInterventoService.getStatiForSearch();
			for(String s: st)
				listaStati.add(new SelectItem(s, s));
		}
		return listaStati;
	}

	public void setListaStati(List<SelectItem> listaStati) {
		this.listaStati = listaStati;
	}

	public List<SelectItem> getListaSoggetti() {
		if(listaSoggetti.size() == 0){
			List<SSpSoggetto> so = spProfInterventoService.getSoggettiForSearch();
			for(SSpSoggetto s: so)
				listaSoggetti.add(new SelectItem(s.getIdSogg(), s.getCognome() + " " + s.getNome()));
		}
		return listaSoggetti;
	}

	public void setListaSoggetti(List<SelectItem> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}

	public List<SelectItem> getListaConcNumero() {
		if(listaConcNumero.size() == 0){
			List<String> st = spProfInterventoService.getConcNumeroForSearch();
			for(String s: st)
				listaConcNumero.add(new SelectItem(s, s));
		}
		return listaConcNumero;
	}

	public void setListaConcNumero(List<SelectItem> listaConcNumero) {
		this.listaConcNumero = listaConcNumero;
	}

	public List<SelectItem> getListaProtNumero() {
		if(listaProtNumero.size() == 0){
			List<String> st = spProfInterventoService.getProtNumeroForSearch();
			for(String s: st)
				listaProtNumero.add(new SelectItem(s, s));
		}
		return listaProtNumero;
	}

	public void setListaProtNumero(List<SelectItem> listaProtNumero) {
		this.listaProtNumero = listaProtNumero;
	}

}
