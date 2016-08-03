package it.webred.ct.service.comma340.web.catasto;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.*;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;


public class CatastoDataProviderImpl extends CatastoBaseBean implements CatastoDataProvider {
	
	private CatastoSearchCriteria criteria = new CatastoSearchCriteria();

	/**
	 * Recupera l'elenco di UIU che soddisfano i criteri di ricerca impostati, paginando.
	 * 
	 * @param start indice del primo record della lista
	 * @param rowNumber numero massimo di righe della lista dei risultati
	 * @param criteria criteri di ricerca
	 */
	public List<SintesiImmobileDTO> getDataByRange(int start, int rowNumber) {
		
		try {
		
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			this.fillEnte(ro);
			ro.setCriteria(criteria);
			ro.setStartm(start);
			ro.setNumberRecord(rowNumber);
			return  catastoService.getListaImmobili(ro);
		
		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			logger.error(t.getMessage(), t);
			return new ArrayList<SintesiImmobileDTO>();
		}
		
	}

	/**
	 * Calcola il numero di record in lista che che soddisfano i criteri di ricerca impostati.
	 * 
	 * @param criteria criteri di ricerca
	 */
	public Long getRecordCount() {
		try{
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			this.fillEnte(ro);
			ro.setCriteria(criteria);
			Long result = catastoService.getCatastoRecordCount(ro);
			return result;
		
		} catch (Throwable t) {
			//super.addErrorMessage("lista.error", t.getMessage());
			logger.error(t.getMessage(), t);
			return new Long(0);
		}
		
	}

	public void resetData() {
		criteria = new CatastoSearchCriteria();
		this.criteria.setCodNazionale(this.getCurrentEnte());
		this.criteria.setUiuAttuale(true);
	}
	
	public String goSearch() {
		resetData();
		return "c340.search";
	}
	
	public String goList() {
		return "c340.search";
	}
	
	
	public CatastoSearchCriteria getCriteria() {
		
		if (criteria.getCodNazionale() == null)
			this.criteria.setCodNazionale(this.getCurrentEnte());	
		
		if(this.criteria.getUiuAttuale()== null)
			this.criteria.setUiuAttuale(Boolean.TRUE);
		/*
		if(this.criteria.getCodCategoria()== null)
			this.criteria.setCodCategoria("");*/
		
		return criteria;
	}
	

	public void setCriteria(CatastoSearchCriteria criteria) {
				
		this.criteria = criteria;		
		
		if (this.criteria.getCodNazionale() == null) 
			this.criteria.setCodNazionale(this.getCurrentEnte());			
	
		/*if(this.criteria.getNonANorma()== null)
			this.criteria.setNonANorma(Boolean.FALSE);
		
		if(this.criteria.getCodCategoria()== null)
			this.criteria.setCodCategoria("");*/
	}
	
}
