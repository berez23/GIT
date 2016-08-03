package it.webred.ct.service.spprof.web.user.bean.nuovo;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;

import java.util.List;

public interface DataProvider {
    
	public List<CivicoDTO> getViaByRange(int start, int rowNumber);
	
	public Long getViaCount();
	
	public List<ParticellaDTO> getParticelleByRange(int start, int rowNumber);
	
	public Long getParticelleCount();
	
	public List<ParticellaDTO> getParticelleByCivicoRange(int start, int rowNumber);
	
	public Long getParticelleByCivicoCount();
	
	public void setListaFogliRicerca(String fogli);
	
	public void setListaParticelleRicerca(String particelle);
	
	public void resetData();
}
