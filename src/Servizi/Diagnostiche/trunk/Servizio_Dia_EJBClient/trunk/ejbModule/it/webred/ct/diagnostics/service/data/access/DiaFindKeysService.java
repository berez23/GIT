package it.webred.ct.diagnostics.service.data.access;

import it.webred.ct.diagnostics.service.data.dto.DiaFindKeysDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogoTipoInfo;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DiaFindKeysService {
		
	public HashMap<Long,List<DiaValueKeysDTO[]>> getFindKeys(DiaFindKeysDTO keys);
	
	public HashMap<Long,List<DiaValueKeysDTO[]>> getFindKeysByFonteETipo(DiaFindKeysDTO keys);
	
	public List<DiaCatalogoTipoInfo> getDiaInfo(DiaFindKeysDTO keys);
	
	public DiaCatalogoTipoInfo getDiaCatalogoTipoInfo(DiaFindKeysDTO keys);
		
}
