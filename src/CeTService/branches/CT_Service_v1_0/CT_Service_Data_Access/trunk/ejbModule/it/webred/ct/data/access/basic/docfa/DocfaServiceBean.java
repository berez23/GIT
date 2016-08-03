package it.webred.ct.data.access.basic.docfa;

import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuServiceException;
import it.webred.ct.data.access.basic.tarsu.dto.DichiarazioniTarsuSearchCriteria;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SintesiDichiarazioneTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.common.SitEnte;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

import javax.persistence.Query;

@Stateless
public class DocfaServiceBean extends DocfaBaseService implements DocfaService {

	
	public List<DocfaDatiCensuari> getListaDocfaImmobile (ParametriCatastaliDTO params){
		
		List<DocfaDatiCensuari> docfa = new ArrayList<DocfaDatiCensuari>();
		
		
		String foglio = params.getFoglio();
		String particella = params.getParticella();
		String unimm = params.getSubalterno();
		
		logger.debug("RICERCA DOCFA [" +
				"Foglio: "+foglio+", " +
				"Particella: "+particella+", " +
				"Subalterno: "+unimm+"]");
		
		if
		(
				(foglio != null && !foglio.equals("")) &&
				(particella != null && !particella.equals("")) &&
				(unimm != null && !unimm.equals(""))
		)
		
		{
			try{
					//Esecuzione Query per estrazione dati docfa
					Query q1 = manager_diogene.createNamedQuery("DocfaDatiCensuari.getDocfaDatiCensuariByFPS");
					q1.setParameter("foglio", foglio);
					q1.setParameter("particella", particella);
					q1.setParameter("subalterno", unimm);
					docfa = q1.getResultList();
					logger.debug("Result size ["+docfa.size()+"]");
					
			}catch(Throwable t) {
				logger.error("", t);
				throw new DocfaServiceException(t);
			}
		
		}else{
			
			String message = "Parametri non validi per la ricerca DOCFA [" +
             				"Foglio: "+foglio+", " +
            				"Particella: "+particella+", " +
            				"Subalterno: "+unimm+"]";
			
			throw new DocfaServiceException(message);
		}
		
		return docfa;
	}
	
}
