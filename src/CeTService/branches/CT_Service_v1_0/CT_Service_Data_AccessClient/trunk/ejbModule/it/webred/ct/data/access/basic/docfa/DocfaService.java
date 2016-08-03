package it.webred.ct.data.access.basic.docfa;

import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.tarsu.dto.DichiarazioniTarsuSearchCriteria;
import it.webred.ct.data.access.basic.tarsu.dto.SintesiDichiarazioneTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DocfaService {
	
	
	public List<DocfaDatiCensuari> getListaDocfaImmobile(ParametriCatastaliDTO params);

}
