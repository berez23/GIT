package it.webred.ct.data.access.basic.anagrafe;

import it.webred.ct.data.access.basic.anagrafe.dto.AnagraficaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.DatiPersonaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaIndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaLuogoDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.SoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.AttrPersonaDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.model.anagrafe.SitComune;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.anagrafe.SitProvincia;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AnagrafeService {
	public SitDPersona getPersonaById(RicercaSoggettoAnagrafeDTO rs);

	public DatiPersonaDTO getDatiPersonaById(RicercaSoggettoAnagrafeDTO rs);

	public List<SitDPersona> getListaPersoneByCF(RicercaSoggettoAnagrafeDTO rs);

	public List<SitDPersona> getListaPersoneByDatiAna(
			RicercaSoggettoAnagrafeDTO rs);

	public String getIdPersonaByCF(RicercaSoggettoAnagrafeDTO rs);

	public List<SitDPersona> getVariazioniPersonaByIdExt(
			RicercaSoggettoAnagrafeDTO rs);

	public AnagraficaDTO getPersonaFamigliaByCF(RicercaSoggettoAnagrafeDTO rs);
	
	public IndirizzoAnagrafeDTO getIndirizzoPersona(
			RicercaSoggettoAnagrafeDTO rs);

	public List<SoggettoAnagrafeDTO> searchSoggetto(
			RicercaSoggettoAnagrafeDTO rs);

	public List<ComponenteFamigliaDTO> getListaCompFamiglia(
			RicercaSoggettoAnagrafeDTO rs);

	public List<ComponenteFamigliaDTO> getResidentiAlCivico(
			RicercaIndirizzoAnagrafeDTO ri);

	//
	public IndirizzoAnagrafeDTO getIndirizzo(RicercaIndirizzoAnagrafeDTO ri);

	List<AnagraficaDTO> getAnagrafeByCF(RicercaSoggettoDTO rs)
			throws AnagrafeException;

	// comuni e provincie
	public SitComune getComune(RicercaLuogoDTO rl);

	public SitProvincia getProvincia(RicercaLuogoDTO rl);
	
	public AttrPersonaDTO getAttributiPersonaByCF(RicercaSoggettoAnagrafeDTO rs);

}
