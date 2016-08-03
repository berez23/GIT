package it.webred.ct.data.access.basic.anagrafe.dao;

import it.webred.ct.data.access.basic.anagrafe.AnagrafeException;
import it.webred.ct.data.access.basic.anagrafe.dto.AnagraficaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaIndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaLuogoDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.SoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.AttrPersonaDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.model.anagrafe.SitComune;
import it.webred.ct.data.model.anagrafe.SitDCivicoV;
import it.webred.ct.data.model.anagrafe.SitDPersFam;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.anagrafe.SitDPersonaCivico;
import it.webred.ct.data.model.anagrafe.SitDVia;
import it.webred.ct.data.model.anagrafe.SitProvincia;

import java.util.List;
 
public interface AnagrafeDAO {
	public SitDPersona getPersonaById(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<SitDPersona> getListaPersoneByCF(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<SitDPersona> getListaPersoneByCFAllaData(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<SitDPersona> getListaPersoneByDatiAna(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<SitDPersona> getListaPersoneByDatiAnaAllaData(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<SitDPersona> getVariazioniPersonaByIdExt(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public AnagraficaDTO getPersonaFamigliaByCF(RicercaSoggettoAnagrafeDTO rs);
	public List<SitDPersonaCivico> getListaPersCivByIdExt(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<SitDCivicoV> getListaCiviciByIdExt(RicercaIndirizzoAnagrafeDTO ri) throws AnagrafeException;
	public List<SitDVia> getListaVieByIdExt(RicercaIndirizzoAnagrafeDTO ri) throws AnagrafeException;
	public List<SoggettoAnagrafeDTO> searchSoggetto(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	
	public SitDPersFam getPersFamByIdExtDPersona(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<ComponenteFamigliaDTO> getListaCompFamiglia(RicercaSoggettoAnagrafeDTO rs) throws AnagrafeException;
	public List<ComponenteFamigliaDTO> getResidentiAlCivico(RicercaIndirizzoAnagrafeDTO ri) throws AnagrafeException ;
	
	public IndirizzoAnagrafeDTO getIndirizzo(RicercaIndirizzoAnagrafeDTO ri) throws AnagrafeException ;
	
	public SitComune getComune(RicercaLuogoDTO rl) throws AnagrafeException;
	public SitProvincia getProvincia(RicercaLuogoDTO rl) throws AnagrafeException;
	List<AnagraficaDTO> getAnagrafeByCF(RicercaSoggettoDTO rs)
			throws AnagrafeException;
	
	
	public AttrPersonaDTO getAttributiPersonaByCF(RicercaSoggettoAnagrafeDTO rs);

}
 