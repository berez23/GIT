package it.webred.ct.service.comma336.data.access;

import java.util.List;

import it.webred.ct.data.access.aggregator.catastoDocfaConcedilizie.dto.SearchCriteriaDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.service.comma336.data.access.dto.*;

import javax.ejb.Remote;

@Remote
public interface C336CommonService {
	
		public List<C336SearchResultDTO> search(SearchCriteriaDTO ro);
	
		public List<C336SearchResultDTO> searchImmobili(SearchCriteriaDTO ro);
		
		public Long searchCountImmobili(SearchCriteriaDTO ro);
		
		public DettInfoGeneraliUiuDTO getDettInfoGeneraliImmobile(RicercaOggettoDTO ro);
		
		public DettInfoGeneraliPartDTO getDettInfoGeneraliParticella(RicercaOggettoDTO ro);
					
		public List<C336SearchResultDTO> searchFabbricatiMaiDich(SearchCriteriaDTO ro);
				
		public List<C336SearchResultDTO> searchFabbricatiExRurali(SearchCriteriaDTO ro);
	
		public DettGestionePraticaDTO getDatiGestionePratica(RicercaOggettoDTO ro);
		
		public DettGestionePraticaDTO getDatiBasePratica(RicercaOggettoDTO ro);

		public List<VerificheControlliPartDTO> getVerificheControlliFabbricato(RicercaOggettoDTO ricUiu);
}
