package it.webred.ct.service.spprof.data.access;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.dao.localizza.SpProfLocalizzaDAO;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class SpProfLocalizzaServiceBean
 */
@Stateless
public class SpProfLocalizzaServiceBean extends SpProfBaseServiceBean implements SpProfLocalizzaService {

	@Autowired
    private SpProfLocalizzaDAO localizzaDAO;

	public List<CivicoDTO> getListaCivici(CivicoDTO datiToponimastici) throws SpProfException {
		
		try {
			return localizzaDAO.getListaCivici(datiToponimastici.getCivico(), datiToponimastici.getNome(), datiToponimastici.getStartRecord(), datiToponimastici.getNumRecord());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
		
	}
	
	public List<CivicoDTO> getListaCiviciByFP(ParticellaDTO foglio) throws SpProfException {
		
		try {
			return localizzaDAO.getListaCivicibyFP(foglio.getFoglio(), foglio.getParticella());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
		
	}

	public List<ParticellaDTO> getListaParticelle(ParticellaDTO foglio)
			throws SpProfException {
		try {
			return localizzaDAO.getListaParticelle(foglio.getFoglio(), foglio.getParticella(), foglio.getStartRecord(), foglio.getNumRecord());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public Long getListaCiviciCount(CivicoDTO datiToponimastici) throws SpProfException {
		
		try {
			return localizzaDAO.getListaCiviciCount(datiToponimastici.getCivico(), datiToponimastici.getNome());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
		
	}
	
	public Long getListaParticelleCount(ParticellaDTO foglio) throws SpProfException {
		
		try {
			return localizzaDAO.getListaParticelleCount(foglio.getFoglio(), foglio.getParticella());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
		
	}
	
}
