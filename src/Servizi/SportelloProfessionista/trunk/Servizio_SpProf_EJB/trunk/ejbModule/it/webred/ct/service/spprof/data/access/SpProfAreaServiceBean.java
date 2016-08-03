package it.webred.ct.service.spprof.data.access;


import java.util.List;
import java.util.Map;

import it.webred.ct.service.spprof.data.access.dao.area.SpProfAreaDAO;
import it.webred.ct.service.spprof.data.access.dao.intervento.SpProfInterventoDAO;
import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfAreaDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpAreaLayer;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.data.model.SSpIntervento;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class SpProfAreaServiceBean
 */
@Stateless
public class SpProfAreaServiceBean extends SpProfBaseServiceBean implements SpProfAreaService {

	@Autowired
	private SpProfAreaDAO areaDAO;
	
	@Autowired
	private SpProfInterventoDAO interventoDAO;
	
	
	public List<SSpAreaPart> getListaAreaPart(SpProfDTO dto) throws SpProfException {
		try {		
			return areaDAO.getListaAreaPart((Long) dto.getObj());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public SSpAreaPart getAreaPartById(SpProfDTO dto) throws SpProfException {
		try {		
			return areaDAO.getAreaPartById((Long) dto.getObj());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public Long saveAreaFabb(SpProfDTO dto) throws SpProfException {
		try {		
			return areaDAO.saveAreaFabb(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public Long saveAreaPart(SpProfDTO dto) throws SpProfException {
		try {		
			return areaDAO.saveAreaPart(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	
	public List<SSpAreaLayer> getSSpAreaLayerFromHiddenKey(SpProfDTO dto) throws SpProfException {
		try {		
			return areaDAO.getSSpAreaLayerFromHiddenKey(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public Long saveAreaLayer(SpProfDTO dto) throws SpProfException {
		try {		
			return areaDAO.saveAreaLayer(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public Long saveAllArea(SpProfAreaDTO dto) throws SpProfException {
		Long ret = null;
		
		try {		
			super.logger.debug("ID Intervento inserito: "+dto.getFkSpIntervento());
			
			//chiamata DAO
			areaDAO.saveAllArea(dto);
			
			SpProfDTO intervento = new SpProfDTO();
			SSpIntervento obj = new SSpIntervento();
			obj.setStato("IN LAVORAZIONE");
			obj.setIdSpIntervento(dto.getFkSpIntervento());
			intervento.setObj(obj);
			
			ret = interventoDAO.update(intervento);
			super.logger.debug("ID Intervento aggiornato: "+ret);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
		
		return ret;
	}
	
	public void deleteAreaFabbByIntervento(SpProfDTO dto) throws SpProfException {
		try {		
			areaDAO.deleteAreaFabbByIntervento(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public void deleteAreaPartByIntervento(SpProfDTO dto) throws SpProfException {
		try {		
			areaDAO.deleteAreaPartByIntervento(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public void deleteAreaLayerByIntervento(SpProfDTO dto) throws SpProfException {
		try {		
			areaDAO.deleteAreaLayerByIntervento(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	/**
	 * Restituisce il nome del file zip
	 */
	public Map download(ProgettoShapeDTO dto) throws SpProfException {
		Map ret = new java.util.HashMap();
		
		try {
			
			Map params = areaDAO.getConnectionParams(dto.getEnteId());
			
			com.vividsolutions.jump.feature.FeatureDataset featureAreaFabb = 
						areaDAO.getFeature(params,"S_SP_AREA_FABB", null, dto.getFkIntervento());
			
			if(!featureAreaFabb.isEmpty()) {
				ret.put("S_SP_AREA_FABB", featureAreaFabb);	
			}
			
			
			com.vividsolutions.jump.feature.FeatureDataset featureAreaPart = 
						areaDAO.getFeature(params,"S_SP_AREA_PART", null, dto.getFkIntervento());
			
			if(!featureAreaPart.isEmpty()) {
				ret.put("S_SP_AREA_PART", featureAreaPart);	
			}
			
			
			com.vividsolutions.jump.feature.FeatureDataset featureAreaLayer = 
						areaDAO.getFeature(params,"S_SP_AREA_LAYER", new String[] {"DES_LAYER","SHAPE_TYPE"}
						   					,dto.getFkIntervento());
			
			if(!featureAreaLayer.isEmpty()) {
				ret.put("S_SP_AREA_LAYER", featureAreaLayer);		
			}
			
			
		}catch(Throwable t) {
			//throw new SpProfException(t);
			super.logger.error("Eccezione download: "+t.getMessage(),t);
		}
		
		return ret;
	}

    

}
