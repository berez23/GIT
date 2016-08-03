package it.webred.ct.service.spprof.data.access.dao.area;

import java.util.List;
import java.util.Map;


import it.webred.ct.service.spprof.data.access.dto.SpProfAreaDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpAreaFabb;
import it.webred.ct.service.spprof.data.model.SSpAreaLayer;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;

public interface SpProfAreaDAO {
	
	public Long saveAreaPart(SpProfDTO dto) throws SpProfDAOException;
	
	public Long saveAreaFabb(SpProfDTO dto) throws SpProfDAOException;
	
	public Long saveAreaLayer(SpProfDTO dto) throws SpProfDAOException;
	
	public void saveAllArea(SpProfAreaDTO dto) throws SpProfDAOException;
	
	public void deleteAreaFabbByIntervento(SpProfDTO dto) throws SpProfDAOException;
	
	public void deleteAreaPartByIntervento(SpProfDTO dto) throws SpProfDAOException;
	
	public void deleteAreaLayerByIntervento(SpProfDTO dto) throws SpProfDAOException;
	
	public List<SSpAreaLayer> getSSpAreaLayerFromHiddenKey(SpProfDTO dto) throws SpProfDAOException;
	
	
	public List<SSpAreaFabb> getListaAreaFabb(Long idIntervento) throws SpProfDAOException;
	
	public List<SSpAreaPart> getListaAreaPart(Long idIntervento) throws SpProfDAOException;
	
	public List<SSpAreaLayer> getListaAreaLayer(Long idIntervento) throws SpProfDAOException;
	
	public SSpAreaPart getAreaPartById(Long id) throws SpProfDAOException;
	
	
	public com.vividsolutions.jump.feature.FeatureDataset getFeature(Map params, String tablename, String[] otherFields, Long idIntervento)  throws SpProfDAOException;
	
	public Map getConnectionParams(String ente) throws SpProfDAOException;
}
