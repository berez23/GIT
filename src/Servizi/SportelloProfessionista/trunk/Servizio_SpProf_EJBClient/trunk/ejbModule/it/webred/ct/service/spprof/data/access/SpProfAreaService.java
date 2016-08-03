package it.webred.ct.service.spprof.data.access;
import java.util.List;
import java.util.Map;


import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfAreaDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpAreaLayer;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;

import javax.ejb.Remote;

@Remote
public interface SpProfAreaService {
	
	public Long saveAreaPart(SpProfDTO dto) throws SpProfException;
	
	public Long saveAreaFabb(SpProfDTO dto) throws SpProfException;
	
	public List<SSpAreaPart> getListaAreaPart(SpProfDTO dto) throws SpProfException;
	
	public SSpAreaPart getAreaPartById(SpProfDTO dto) throws SpProfException;
	
	public Long saveAreaLayer(SpProfDTO dto) throws SpProfException;

	public List<SSpAreaLayer> getSSpAreaLayerFromHiddenKey(SpProfDTO dto) throws SpProfException;
	
	public void deleteAreaFabbByIntervento(SpProfDTO dto) throws SpProfException;
	
	public void deleteAreaPartByIntervento(SpProfDTO dto) throws SpProfException;
		
	public void deleteAreaLayerByIntervento(SpProfDTO dto) throws SpProfException;

	/**
	 * Il metodo effettua gli inserimenti in maniera globale
	 * restituendo l'id dell'intervento
	 * 
	 * @param dto
	 * @return
	 * @throws SpProfException
	 */
	public Long saveAllArea(SpProfAreaDTO dto) throws SpProfException;
	
	public Map download(ProgettoShapeDTO dto) throws SpProfException;
	
}
