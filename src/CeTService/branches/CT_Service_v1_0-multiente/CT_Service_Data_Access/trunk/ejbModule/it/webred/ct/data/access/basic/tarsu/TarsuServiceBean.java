package it.webred.ct.data.access.basic.tarsu;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.tarsu.dao.TarsuDAO;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.OggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuParCatDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaSoggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaViaTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SintesiDichiarazioneTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.data.model.tarsu.SitTTarVia;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class TarsuServiceBean implements TarsuService {
 	  
	@Autowired 
	private TarsuDAO tarsuDAO;
	 
	//ADATTATI TUTTI I METODI AL MULTIENTE, ANCHE QUELLI NON USATI DIRETTAMENTE DA COMMA 340
	//PER EVENTUALI PROBLEMI DI COMPATIBILITÀ RECUPERARE UNA VERSIONE PRECEDENTE DALLA HISTORY
	//(IN OGNI CASO SI DOVRÀ POI PROCEDERE ALL'ADATTAMENTO AL MULTIENTE) - FILIPPO 18.11.10

	
	public List<SitTTarSogg> getListaSoggettiF(RicercaSoggettoTarsuDTO rs) {
		return tarsuDAO.getListaSoggettiF(rs);		
	}
	
	public List<SitTTarSogg> getListaSoggettiG(RicercaSoggettoTarsuDTO rs) {		
		return tarsuDAO.getListaSoggettiG(rs);	
	}
	
	public List<SitTTarSogg> getSoggettoByCF(RicercaSoggettoTarsuDTO rs) {		
		return tarsuDAO.getSoggettoByCF(rs);		
	}
	
	public List<SitTTarSogg> getSoggettoByPIVA(RicercaSoggettoTarsuDTO rs) {		
		return tarsuDAO.getSoggettoByPIVA(rs);
	}

	public Long getDichTarsuRecordCount(RicercaOggettoTarsuDTO ro) {		
		return tarsuDAO.getDichTarsuRecordCount(ro);
	}

	public List<SintesiDichiarazioneTarsuDTO> getListaDichiarazioniTarsu(RicercaOggettoTarsuDTO ro) {
		return tarsuDAO.getListaDichiarazioniTarsu(ro);
	}

	public List<SoggettoTarsuDTO> getListaSoggettiDichiarazioneTarsu(RicercaOggettoTarsuDTO ro) {		
		return tarsuDAO.getListaSoggettiDichiarazioneTarsu(ro);
	}
	
	public List<SitTTarOggetto> getListaDichiarazioniTarsu(RicercaOggettoTarsuParCatDTO ro) {
		return tarsuDAO.getListaDichiarazioniTarsu(ro);
	}
	
	public List<InformativaTarsuDTO> getListaInformativaTarsu(RicercaOggettoTarsuDTO ro) {		
		return tarsuDAO.getListaInformativaTarsu(ro);
	}	
	
	public InformativaTarsuDTO getInformativaTarsu(RicercaOggettoTarsuDTO ro) {
		return tarsuDAO.getInformativaTarsu(ro);
	}

	@Override
	public List<SitTTarSogg> searchSoggetto(RicercaSoggettoTarsuDTO rs) {
		List<SitTTarSogg> lista=null;
		lista = tarsuDAO.searchSoggetto(rs);
		return lista;
	}

	@Override
	public SitTTarSogg getSoggettoById(RicercaSoggettoTarsuDTO rs) {
		return tarsuDAO.getSoggettoById(rs);
	}

	@Override
	public List<OggettoTarsuDTO> getListaOggettiByIdSogg(RicercaSoggettoTarsuDTO rs) {
		return tarsuDAO.getListaOggettiByIdSogg(rs);
	}

	@Override
	public SitTTarVia getViaByIdExt(RicercaViaTarsuDTO rv) {
		return tarsuDAO.getViaByIdExt(rv);
	}

	@Override
	public List<SitTTarOggetto> getListaOggettiByFabbricato(RicercaOggettoDTO ro) {
		return tarsuDAO.getListaOggettiByFabbricato(ro);
	}

	@Override
	public List<SitTTarOggetto> getListaOggettiByUI(RicercaOggettoDTO ro) {
		return tarsuDAO.getListaOggettiByUI(ro);
	}
	
	public List<SitTTarOggetto> getListaOggettiAiCiviciTarsu(RicercaOggettoTarsuDTO ro) {
		return tarsuDAO.getListaOggettiAiCiviciTarsu(ro);
	}
	
	
}
