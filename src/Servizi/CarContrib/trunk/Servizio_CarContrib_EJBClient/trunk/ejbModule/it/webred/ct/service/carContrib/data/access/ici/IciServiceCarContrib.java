package it.webred.ct.service.carContrib.data.access.ici;

import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.service.carContrib.data.access.cc.IndiceNonAllineatoException;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.ici.dto.DatiIciDTO;
import it.webred.ct.service.carContrib.data.access.tarsu.dto.DatiTarsuDTO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface IciServiceCarContrib {
	public SitTIciSogg getDatiIciSoggetto (RicercaSoggettoIciDTO rs );
	public List<DatiIciDTO> getDatiIci(RicercaSoggettoIciDTO rs, IndiciSoggettoDTO indSogg);
	public List<DatiImportiCncDTO> getVersamenti(RicercaSoggettoIciDTO rs, int annoRif);
	
	//metodi aggiunti per utilizzo indice correlazione
	public List<SitTIciSogg>  searchSoggettiCorrelatiIci(RicercaDTO dati) throws IndiceNonAllineatoException;//in alternativa a searchSoggettoIci CHE e' diventato private 
	

}
