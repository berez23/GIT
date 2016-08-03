package it.webred.ct.service.comma340.web.catasto;

import java.util.List;

import it.webred.ct.data.access.basic.catasto.*;
import it.webred.ct.data.access.basic.catasto.dto.*;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;


public class CatastoDataProviderImpl extends CatastoBaseBean implements CatastoDataProvider {

	/**
	 * Recupera l'elenco di UIU che soddisfano i criteri di ricerca impostati, paginando.
	 * 
	 * @param start indice del primo record della lista
	 * @param rowNumber numero massimo di righe della lista dei risultati
	 * @param criteria criteri di ricerca
	 */
	public List<SintesiImmobileDTO> getDataByRange(int start, int rowNumber,
			CatastoSearchCriteria criteria) {
		
		List<SintesiImmobileDTO> result = getCatastoService().getListaImmobili(criteria, start, rowNumber);
		return result;
	}

	/**
	 * Calcola il numero di record in lista che che soddisfano i criteri di ricerca impostati.
	 * 
	 * @param criteria criteri di ricerca
	 */
	public long getRecordCount(CatastoSearchCriteria criteria) {
		Long result = getCatastoService().getCatastoRecordCount(criteria);
		return result;
	}

	public void resetData() {
		
	}
	
}
