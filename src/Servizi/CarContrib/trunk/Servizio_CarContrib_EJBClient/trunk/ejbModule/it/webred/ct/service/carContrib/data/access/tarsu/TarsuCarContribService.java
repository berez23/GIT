package it.webred.ct.service.carContrib.data.access.tarsu;


import it.webred.ct.data.access.basic.tarsu.dto.RicercaSoggettoTarsuDTO;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.service.carContrib.data.access.cc.IndiceNonAllineatoException;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.tarsu.dto.DatiTarsuDTO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface TarsuCarContribService {
	public SitTTarSogg getDatiTarsuSoggetto (RicercaSoggettoTarsuDTO rs );
	public List<DatiTarsuDTO> getDatiTarsu(RicercaSoggettoTarsuDTO rs, IndiciSoggettoDTO indSogg);
	//public List<VersamentoDTO> getVersamentiOLD(RicercaSoggettoTarsuDTO rs, int annoRif, String[] codTipiTributo);
	public List<DatiImportiCncDTO> getVersamenti(RicercaSoggettoTarsuDTO rs, int annoRif,String[] codTipiTributo);
	
	//metodi aggiunti per utilizzo indice correlazione
	public List<SitTTarSogg>  searchSoggettiCorrelatiTarsu(RicercaDTO dati) throws IndiceNonAllineatoException;//in alternativa a searchSoggettoTARSU CHE è diventato private
	
	
}
