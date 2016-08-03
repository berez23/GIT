package it.webred.ct.service.comma336.web.bean.pagination;

import it.webred.ct.data.access.aggregator.catastoDocfaConcedilizie.dto.SearchCriteriaDTO;
import it.webred.ct.service.comma336.data.access.dto.C336SearchResultDTO;
import it.webred.ct.service.comma336.web.Comma336BaseBean;
import it.webred.ct.service.comma336.web.bean.util.PageUiBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

public class DataProviderImpl extends Comma336BaseBean implements DataProvider {

	private SearchCriteriaDTO criteria = new SearchCriteriaDTO();
	
	private boolean disableFlag;
	
	private String tipoSoggetto = "P";
	
	private String descVia;
	private String descCivico;
	private String descCognomeNome;
	private String descDenominazione;
	private String cf;
	private String piva;
	
	private String pageIndietro;
	
	private boolean visibleFlagsInList=true;
	
	private List<String> listaCategorieSel;
	private List<String> listaCausaliSel;
	
	public List<C336SearchResultDTO> getReportByRange(int start, int rowNumber) {
		getLogger().debug("getReportByRange()");
		List<C336SearchResultDTO> list = new ArrayList<C336SearchResultDTO>();
		try {
			if (!validaInput(criteria.getTipoRicerca())) {
				return list;
			}
			criteria.setStartm(start);
			criteria.setNumberRecord(rowNumber);
			fillEnte(criteria);
			list = c336CommonService.search(criteria);	
			logger.debug("LISTA-N.ELE " + list.size());	
		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		return list;
	}

	public Long getReportCount() {
		logger.debug("getReportCount()");
		if (!validaInput(criteria.getTipoRicerca())) {
			return new Long(0);
		}
		fillEnte(criteria);
		Long result = c336CommonService.searchCountImmobili(criteria);
		return result;
	}
	
	public String goSearch() {
		resetData();
		return "comma336.search";
	}

	public void resetData() {
		getLogger().debug("resetData()");
		PageUiBean p = (PageUiBean)this.getBeanReference("pageUiBean");
		p.setPageResultList("/jsp/protected/empty.xhtml");
		resetRicercaCatasto();
		criteria = new SearchCriteriaDTO();
		criteria.getCatOggetto().setFaiRicercaInCatastoUrbano(true);
		criteria.setFaiRicercaInConEdi(false);
		criteria.setFaiRicercaInDocfa(false);
	}
			
	public void resetDataFabbrMaiDic() {
		PageUiBean p = (PageUiBean)this.getBeanReference("pageUiBean");
		p.setPageResultList("/jsp/protected/empty.xhtml");
		criteria = new SearchCriteriaDTO();
		criteria.setFaiRicercaInFabbrMaiDich(true);
	}
	
	public void resetDataFabbrExRurali() {
		PageUiBean p = (PageUiBean)this.getBeanReference("pageUiBean");
		p.setPageResultList("/jsp/protected/empty.xhtml");
		criteria = new SearchCriteriaDTO();
		criteria.setFaiRicercaInFabbrExRurali(true);
	}
	
	public SearchCriteriaDTO getCriteria() {
		return criteria;
	}

	public void setCriteria(SearchCriteriaDTO criteria) {
		this.criteria = criteria;
	}
	
	
	public void doConfermaCategorie() {
		criteria.getCatOggetto().setCodCategoria("");
		String nuovaCat = "";
		for (String cat : listaCategorieSel) {
			nuovaCat += "," + cat;
		}
		criteria.getCatOggetto().setCodCategoria(nuovaCat.replaceFirst(",", ""));
		getLogger().debug("Lista Categorie selezionate: " + criteria.getCatOggetto().getCodCategoria());
	}
	
	public void doConfermaCausali() {
		criteria.getDcfOggetto().setCausali("");
		String nuovaCau = "";
		for (String cau : listaCausaliSel) {
			nuovaCau += "," + cau;
		}
		
		criteria.getDcfOggetto().setCausali(nuovaCau.replaceFirst(",", ""));
		getLogger().debug("Lista Causali selezionate: " + criteria.getDcfOggetto().getCausali());
	}

	public List<String> getListaCategorieSel() {
		return listaCategorieSel;
	}

	public void setListaCategorieSel(List<String> listaCategorieSel) {
		this.listaCategorieSel = listaCategorieSel;
	}

	public List<String> getListaCausaliSel() {
		return listaCausaliSel;
	}

	public void setListaCausaliSel(List<String> listaCausaliSel) {
		this.listaCausaliSel = listaCausaliSel;
	}

	public boolean isVisibleFlagsInList() {
		return visibleFlagsInList;
	}

	public void setVisibleFlagsInList(boolean visibleFlagsInList) {
		this.visibleFlagsInList = visibleFlagsInList;
	}
	private boolean validaInput(String tipoRicerca) {
		if (criteria.getTipoRicerca() ==null || criteria.getTipoRicerca().equals("")){
			pageIndietro = "uiu";
			//criteria.getConcEdiOggetto().setTipoCatasto("URBANO");
			return true;
		}
		if (criteria.getTipoRicerca().equals("maiDic") ) {
			pageIndietro = "maiDich";
			if (criteria.getFaiRicercaInFabbrMaiDich() || criteria.getCatOggetto().getFaiRicercaInCatastoTerreni() || criteria.getCatOggetto().getFaiRicercaInCatastoUrbano())
				return true;
		}
		if (criteria.getTipoRicerca().equals("exRurali") ) {
			pageIndietro = "exRurali";
			if (criteria.getFaiRicercaInFabbrExRurali() || criteria.getCatOggetto().getFaiRicercaInCatastoTerreni() || criteria.getCatOggetto().getFaiRicercaInCatastoUrbano())
				return true;
		}
		super.addErrorMessage("richiesta.fabbMaiDic.validator", "");
		return false;
	}

	public void setPageIndietro(String pageIndietro) {
		this.pageIndietro = pageIndietro;
	}

	public String getPageIndietro() {
		//System.out.println("Page Indietro: "+pageIndietro);
		return pageIndietro;
	}
	
	public void changeTipoSoggetto(ValueChangeEvent ev){
		ev.getNewValue();
		logger.debug("--------------------------->IdSoggetto Prima: " + criteria.getCatOggetto().getIdSoggetto());
		clearSoggetto();
		logger.debug("--------------------------->IdSoggetto Dopo: " + criteria.getCatOggetto().getIdSoggetto());

	}
	
	public void clearSoggetto(){
		setDescCognomeNome("");
		descDenominazione="";
		cf="";
		piva="";
		criteria.getCatOggetto().setIdSoggetto(null);
	}
	
	public void clearIndirizzo(){
		descVia = null;
		descCivico = null;
	}
	
	public void resetRicercaCatasto(){
		clearSoggetto();
		clearIndirizzo();
	}
	
	public String getDescVia() {
		return descVia;
	}

	public void setDescVia(String descVia) {
		this.descVia = descVia;
		if(descVia!=null && descVia.length()==0){
			this.setDescCivico("");
			criteria.getCatOggetto().setIdVia("");
		}
	}

	public String getDescCivico() {
		return descCivico;
	}

	public void setDescCivico(String descCivico) {
		this.descCivico = descCivico;
		if(descCivico!=null && descCivico.length()==0){
			criteria.getCatOggetto().setIdCivico("");
		}
	}

	public String getDescDenominazione() {
		return descDenominazione;
	}

	public void setDescDenominazione(String descDenominazione) {
		this.descDenominazione = descDenominazione;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public void setDescCognomeNome(String descCognomeNome) {
		this.descCognomeNome = descCognomeNome;
	}

	public String getDescCognomeNome() {
		return descCognomeNome;
	}

	public void setDisableFlag(boolean disableFlag) {
		this.disableFlag = disableFlag;
	}

	public boolean isDisableFlag() {
		return disableFlag;
	}

	
	
}
