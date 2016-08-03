package it.webred.ct.service.spprof.data.access.dao.localizza;

import java.util.List;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;

public interface SpProfLocalizzaDAO {
	
	
	public List<CivicoDTO> getListaCivici(String civico, String nome, int start, int record) throws SpProfDAOException;
	
	public List<CivicoDTO> getListaCivicibyFP(String foglio, String particella) throws SpProfDAOException;
	
	public List<ParticellaDTO> getListaParticelle(String foglio, String particella, int start, int record) throws SpProfDAOException;

	public Long getListaCiviciCount(String civico, String nome) throws SpProfDAOException;
	
	public Long getListaParticelleCount(String foglio,String particella) throws SpProfDAOException;
}
