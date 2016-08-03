package it.webred.ct.diagnostics.service.data.dao;

import it.webred.ct.diagnostics.service.data.dto.DiaFindKeysDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogoTipoInfo;

import java.util.HashMap;
import java.util.List;

public interface IDAOFindKeys {
	
	public  HashMap<Long, List<DiaValueKeysDTO[]>> getFindKeys(DiaFindKeysDTO keys) throws DIAServiceException;

	public HashMap<Long, List<DiaValueKeysDTO[]>> getFindKeysByFonteETipo(DiaFindKeysDTO keys) throws DIAServiceException;

	public List<DiaCatalogoTipoInfo> getDiaInfo(String beanClassName, String idFonte, String tipoFonte) throws DIAServiceException;
	
	public DiaCatalogoTipoInfo getDiaCatalogoTipoInfo(long id) throws DIAServiceException;
}
