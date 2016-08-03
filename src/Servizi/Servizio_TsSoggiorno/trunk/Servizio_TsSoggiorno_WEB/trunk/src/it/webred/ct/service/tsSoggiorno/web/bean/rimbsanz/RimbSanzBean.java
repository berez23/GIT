package it.webred.ct.service.tsSoggiorno.web.bean.rimbsanz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import it.webred.ct.service.tsSoggiorno.data.access.DichiarazioneService;
import it.webred.ct.service.tsSoggiorno.data.access.RimbSanzService;
import it.webred.ct.service.tsSoggiorno.data.access.TariffaService;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.StrutturaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffaPK;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.anagrafica.AnagraficaBean;
import it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione.DichiarazioneDataProvider;
import it.webred.ct.service.tsSoggiorno.web.bean.util.PageBean;

public class RimbSanzBean extends TsSoggiornoBaseBean implements
	RimbSanzDataProvider{

	protected RimbSanzService rimbsanzService = (RimbSanzService) getEjb("Servizio_TsSoggiorno", "Servizio_TsSoggiorno_EJB", "RimbSanzServiceBean");

	private Long idSelezionato;
	private BigDecimal valore;
	
	private String idSocieta;
	private String descSocieta;
	
	private BigDecimal idStruttura;
	private String idClasse;
	private BigDecimal idPeriodo;
	private String descrizione;
	private String dataIns;
	private String rimbsanz = "R";
	private RimbSanzSearchCriteria criteria = new RimbSanzSearchCriteria();
	private List<RimbSanzDTO> listaRimbSanz;
	private List<SelectItem> listaSocieta = new ArrayList<SelectItem>();
	private List<SelectItem> listaStrutture = new ArrayList<SelectItem>();
	private List<SelectItem> listaClassi = new ArrayList<SelectItem>();
	private List<SelectItem> listaPeriodi = new ArrayList<SelectItem>();
	
	private boolean flgSearch;

	public void doNewRimbSanz() {
		flgSearch=false;
		
		valore = null;
		idSocieta = null;
		idStruttura = null;
		idClasse = null;
		idPeriodo = null;
		loadPeriodi();
		if(listaPeriodi.size() > 0){
			idPeriodo = (BigDecimal) listaPeriodi.get(0).getValue();
		}
		
		listaSocieta = new ArrayList<SelectItem>();
		listaStrutture = new ArrayList<SelectItem>();
		listaClassi = new ArrayList<SelectItem>();
		
		
	  /*loadSocieta();
		if(listaSocieta.size() > 0){
			idSocieta = listaSocieta.get(0).getValue().toString();
		}
		doLoadStrutture();*/
		
	}
	
	public void doLoadRimbSanz(){
		
		flgSearch=true;
		
		valore = null;
		idSocieta = null;
		idStruttura = null;
		idClasse = null;
		idPeriodo = null;
		criteria = new RimbSanzSearchCriteria();
		
		loadPeriodi();
		loadSocieta();
		doLoadStrutture();
		loadClassi();
	}
	
	public List<RimbSanzDTO> getRimbSanzByRange(int start,
			int rowNumber) {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			criteria.setStart(start);
			criteria.setRowNumber(rowNumber);
			dataIn.setObj(criteria);
			return rimbsanzService.getRimbSanzByCriteria(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
		return new ArrayList<RimbSanzDTO>();
	}

	public int getRimbSanzCount() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setObj(criteria);
			return rimbsanzService.getRimbSanzCountByCriteria(dataIn)
					.intValue();

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
		return 0;
	}

	public void doSaveRimbSanz() {
		try {

			boolean valido = true;
			if(descrizione == null || "".equals(descrizione)){
				super.addErrorMessage("rimbsanz.required.descr", "");
			}
			
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			DichiarazioneSearchCriteria criteriaD = new DichiarazioneSearchCriteria();
			criteriaD.setIdClasse(idClasse);
			criteriaD.setIdStruttura(idStruttura.longValue());
			criteriaD.setIdPeriodo(idPeriodo.longValue());
			dataIn.setObj(criteriaD);
			List<DichiarazioneDTO> listaDich = super.getDichiarazioneService().getDichiarazioniByCriteria(dataIn);
			if(listaDich != null && listaDich.size() > 0){
				valido = false;
				super.addErrorMessage("rimbsanz.save.error", "");
			}
			
			if(valido){
				dataIn = new DataInDTO();
				fillEnte(dataIn);
				AnagraficaBean ana = (AnagraficaBean) getBeanReference("anagraficaBean");
				if("R".equals(rimbsanz)){
					IsRimborso r = new IsRimborso();
					r.setValore(valore);
					r.setFkIsStruttura(idStruttura);
					r.setFkIsClasse(idClasse);
					r.setFkIsPeriodo(idPeriodo);
					r.setDescrizione(descrizione);
					r.setUsrIns(ana.getSoggetto().getCodfisc());
					r.setDtIns(new Date());
					dataIn.setObj(r);
					
					rimbsanzService.saveRimborso(dataIn);
				}else {
					IsSanzione s = new IsSanzione();
					s.setValore(valore);
					s.setFkIsStruttura(idStruttura);
					s.setFkIsClasse(idClasse);
					s.setFkIsPeriodo(idPeriodo);
					s.setDescrizione(descrizione);
					s.setUsrIns(ana.getSoggetto().getCodfisc());
					s.setDtIns(new Date());
					dataIn.setObj(s);
					
					rimbsanzService.saveSanzione(dataIn);
				}
				doLoadRimbSanz();
				PageBean pBean = (PageBean) getBeanReference("pageBean");
				pBean.setPageCenter("/jsp/protected/content/rimbsanzLista.xhtml");
				
				super.addInfoMessage("salvataggio.ok");
			}
				
		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doDeleteRimbSanz() {
		try {

			boolean valido = true;
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			DichiarazioneSearchCriteria criteriaD = new DichiarazioneSearchCriteria();
			criteriaD.setIdClasse(idClasse);
			criteriaD.setIdStruttura(idStruttura.longValue());
			criteriaD.setIdPeriodo(idPeriodo.longValue());
			criteriaD.setDataIns(dataIns);
			dataIn.setObj(criteriaD);
			List<DichiarazioneDTO> listaDich = super.getDichiarazioneService().getDichiarazioniByCriteria(dataIn);
			if(listaDich != null && listaDich.size() > 0){
				valido = false;
				super.addErrorMessage("rimbsanz.delete.error", "");
			}
			
			if(valido){
				dataIn.setId(idSelezionato);
				if("R".equals(criteria.getRimbsanz()))
					rimbsanzService.deleteRimborso(dataIn);
				else rimbsanzService.deleteSanzione(dataIn); 
				super.addInfoMessage("salvataggio.ok");
			}

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public List<SelectItem> getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List<SelectItem> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List<SelectItem> getListaPeriodi() {
		return listaPeriodi;
	}

	public void setListaPeriodi(List<SelectItem> listaPeriodi) {
		this.listaPeriodi = listaPeriodi;
	}

	public Long getIdSelezionato() {
		return idSelezionato;
	}

	public void setIdSelezionato(Long idSelezionato) {
		this.idSelezionato = idSelezionato;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public String getIdSocieta() {
		return idSocieta;
	}

	public void setIdSocieta(String idSocieta) {
		this.idSocieta = idSocieta;
	}

	public BigDecimal getIdStruttura() {
		return idStruttura;
	}

	public void setIdStruttura(BigDecimal idStruttura) {
		this.idStruttura = idStruttura;
	}

	public String getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(String idClasse) {
		this.idClasse = idClasse;
	}

	public BigDecimal getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(BigDecimal idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getRimbsanz() {
		return rimbsanz;
	}

	public void setRimbsanz(String rimbsanz) {
		this.rimbsanz = rimbsanz;
	}

	public List<RimbSanzDTO> getListaRimbSanz() {
		return listaRimbSanz;
	}

	public void setListaRimbSanz(List<RimbSanzDTO> listaRimbSanz) {
		this.listaRimbSanz = listaRimbSanz;
	}

	public List<SelectItem> getListaSocieta() {
		return listaSocieta;
	}

	public void setListaSocieta(List<SelectItem> listaSocieta) {
		this.listaSocieta = listaSocieta;
	}

	public List<SelectItem> getListaStrutture() {
		return listaStrutture;
	}

	public void setListaStrutture(List<SelectItem> listaStrutture) {
		this.listaStrutture = listaStrutture;
	}

	public RimbSanzSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(RimbSanzSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	private void loadPeriodi() {
		if (listaPeriodi == null || listaPeriodi.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsPeriodo> lista = super.getDichiarazioneService().getPeriodi(dataIn);
			for (IsPeriodo c : lista) {
				listaPeriodi.add(new SelectItem(new BigDecimal(c.getId()), c.getDescrizione()));
			}
		}
	}
	
	private void loadSocieta() {
		listaSocieta = new ArrayList<SelectItem>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		List<SocietaDTO> lista = super.getAnagraficaService().getSocieta(dataIn);
		for (SocietaDTO s : lista) {
			listaSocieta.add(new SelectItem(new BigDecimal(s.getSocieta()
					.getId()), s.getSocieta().getRagSoc()));
		}
	}
	
	private void loadClassi() {
		if (listaClassi == null || listaClassi.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsClasse> lista = super.getAnagraficaService().getClassi(dataIn);
			for (IsClasse c : lista) {
				listaClassi.add(new SelectItem(c.getCodice(), c.getDescrizione()));
			}
		}
	}
	
	public void doLoadStrutture() {
		listaClassi = new ArrayList<SelectItem>();
		listaStrutture = new ArrayList<SelectItem>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		DichiarazioneSearchCriteria crit = new DichiarazioneSearchCriteria();
		if(idSocieta != null && !"".equals(idSocieta))
			crit.setIdSocieta(new Long(idSocieta));
		
		if(this.flgSearch || (!this.flgSearch && idSocieta != null && !"".equals(idSocieta))){
			dataIn.setObj(crit);
			List<StrutturaDTO> lista = super.getAnagraficaService()
					.getStrutturaByCriteria(dataIn);
			
			for (StrutturaDTO s : lista) {
				listaStrutture.add(new SelectItem(new BigDecimal(s.getStruttura()
						.getId()), s.getStruttura().getDescrizione()));
			}
			
		
			//Carico le classe di default
			if(listaStrutture.size() > 0 && !this.flgSearch){
				idStruttura = (BigDecimal) listaStrutture.get(0).getValue();
				doLoadClassiStruttura();
				if(listaClassi.size() > 0)
					idClasse = (String) listaClassi.get(0).getValue();
			}
		
		}
	}
	
	public void doLoadClassiStruttura() {
		listaClassi = new ArrayList<SelectItem>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setId(idStruttura.longValue());
		List<IsClassiStruttura> lista = super.getAnagraficaService()
				.getClassiByStruttura(dataIn);
		for (IsClassiStruttura s : lista) {
			listaClassi.add(new SelectItem(s.getId().getFkIsClasse(),s.getDescrizione()));
		}
	}

	public String getDescSocieta() {
		return descSocieta;
	}

	public void setDescSocieta(String descSocieta) {
		this.descSocieta = descSocieta;
	}
	


	
	public void changeSocietaActionListener(ActionEvent e){
		doLoadStrutture();
	}

	public void doCleanStruttureClassi(){
		idSocieta=null;
		idStruttura=null;
		idClasse=null;
		listaStrutture = new ArrayList<SelectItem>();
		listaClassi = new ArrayList<SelectItem>();
		
	}

	public boolean isFlgSearch() {
		return flgSearch;
	}

	public void setFlgSearch(boolean flgSearch) {
		this.flgSearch = flgSearch;
	}
	
}
