package it.webred.ct.service.spprof.data.access.dao.intervento;

import java.util.List;

import it.webred.ct.service.spprof.data.access.dto.InterventoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfAreaDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayer;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

public interface SpProfInterventoDAO {
	
	public List<SSpIntervento> getIntervento(InterventoSearchCriteria criteria) throws SpProfDAOException;
	
	public Long getCountIntervento(InterventoSearchCriteria criteria) throws SpProfDAOException;
	
	public List<SSpIntervento> getInterventoByUser(String username, int start, int record) throws SpProfDAOException;
	
	public Long getCountInterventoByUser(String username) throws SpProfDAOException;
	
	public SSpIntervento save(SpProfDTO dto) throws SpProfDAOException;
	
	public Long update(SpProfDTO dto) throws SpProfDAOException;
	
	public SSpIntervento getIntervento(Long idIntervento) throws SpProfDAOException;
	
	public void delete(SpProfDTO dto) throws SpProfDAOException;
	
	//ricerca
	public List<String> getStatiForSearch() throws SpProfDAOException;
	
	public List<SSpSoggetto> getSoggettiForSearch() throws SpProfDAOException;
	
	public List<String> getConcNumeroForSearch() throws SpProfDAOException;
	
	public List<String> getProtDataForSearch() throws SpProfDAOException;
	
	public List<String> getProtNumeroForSearch() throws SpProfDAOException;
	
	//InterventoLayer
	public List<SSpInterventoLayer> getSSpInterventoLayerByFkSpIntervento(Long idIntervento) throws SpProfDAOException;
	
	public SSpInterventoLayer saveInterventoLayer(Long idIntervento,Long idPgt) throws SpProfDAOException;
	
	public void deleteInteventoLayer(SpProfDTO dto) throws SpProfDAOException;
}
