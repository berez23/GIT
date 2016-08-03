package it.webred.ct.service.spprof.data.access;
import java.util.List;

import it.webred.ct.service.spprof.data.access.dto.InterventoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayer;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import javax.ejb.Remote;

@Remote
public interface SpProfInterventoService {
	
	public List<SSpIntervento> getIntervento(InterventoSearchCriteria criteria) throws SpProfException;
	
	public Long getCountIntervento(InterventoSearchCriteria criteria) throws SpProfException;
	
	public List<SSpIntervento> getInterventoByUser(SpProfDTO dto) throws SpProfException;
	
	public Long getCountInterventoByUser(SpProfDTO dto) throws SpProfException;
	
	public SSpIntervento getInterventoById(SpProfDTO dto) throws SpProfException;
	
	public Long save(SpProfDTO dto) throws SpProfException;
	
	public Long update(SpProfDTO dto) throws SpProfException;
	
	public void delete(SpProfDTO dto) throws SpProfException;
	
	public List<String> getStatiForSearch() throws SpProfException;

	public List<String> getConcNumeroForSearch() throws SpProfException;

	public List<SSpSoggetto> getSoggettiForSearch() throws SpProfException;

	public List<String> getProtDataForSearch() throws SpProfException;

	public List<String> getProtNumeroForSearch() throws SpProfException;
	
	public List<SSpInterventoLayer> getInterventoLayerByIntervento(SpProfDTO dto) throws SpProfException;
}
