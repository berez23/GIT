package it.webred.ct.service.spprof.data.access;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SpProfLocalizzaService {

	public List<CivicoDTO> getListaCivici(CivicoDTO datiToponimastici) throws SpProfException;
	
	public List<CivicoDTO> getListaCiviciByFP(ParticellaDTO foglio) throws SpProfException;	

	public List<ParticellaDTO> getListaParticelle(ParticellaDTO foglio) throws SpProfException;
	
	public Long getListaCiviciCount(CivicoDTO datiToponimastici) throws SpProfException;
	
	public Long getListaParticelleCount(ParticellaDTO foglio) throws SpProfException;
}
