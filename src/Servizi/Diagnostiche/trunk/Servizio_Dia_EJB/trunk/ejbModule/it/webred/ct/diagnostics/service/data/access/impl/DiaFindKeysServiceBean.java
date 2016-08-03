package it.webred.ct.diagnostics.service.data.access.impl;

import it.webred.ct.diagnostics.service.data.access.DIABaseBean;
import it.webred.ct.diagnostics.service.data.access.DiaFindKeysService;
import it.webred.ct.diagnostics.service.data.dao.IDAOFindKeys;
import it.webred.ct.diagnostics.service.data.dto.DiaFindKeysDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogoTipoInfo;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class DiaFindKeysServiceBean
 */
@Stateless
public class DiaFindKeysServiceBean extends DIABaseBean implements DiaFindKeysService {
	
	@Autowired
	private IDAOFindKeys findKeysDAO;
	
    /**
     * Default constructor. 
     */
    public DiaFindKeysServiceBean() {
        // TODO Auto-generated constructor stub
    }

	public HashMap<Long, List<DiaValueKeysDTO[]>> getFindKeys(DiaFindKeysDTO keys) {
		try {
			
			return findKeysDAO.getFindKeys(keys);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}

	public HashMap<Long, List<DiaValueKeysDTO[]>> getFindKeysByFonteETipo(
			DiaFindKeysDTO keys) {
		try {
			
			return findKeysDAO.getFindKeysByFonteETipo(keys);
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}
	
	public List<DiaCatalogoTipoInfo> getDiaInfo(DiaFindKeysDTO keys) {
		try {
			
			return findKeysDAO.getDiaInfo(keys.getBeanClassName(),keys.getIdFonte(),keys.getTipoFonte());
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}
	
	public DiaCatalogoTipoInfo getDiaCatalogoTipoInfo(DiaFindKeysDTO keys) {
		try {
			
			return findKeysDAO.getDiaCatalogoTipoInfo(keys.getId().longValue());
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage(), t);
			throw new DIAServiceException(t);
		}
	}

}
