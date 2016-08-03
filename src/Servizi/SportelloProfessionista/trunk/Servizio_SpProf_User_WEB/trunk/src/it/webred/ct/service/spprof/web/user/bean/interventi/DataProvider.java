package it.webred.ct.service.spprof.web.user.bean.interventi;

import it.webred.ct.service.spprof.data.model.SSpIntervento;

import java.util.List;

public interface DataProvider {
    
	public List<SSpIntervento> getInterventiByRange(int start, int rowNumber);
	
	public Long getInterventiCount();
	
	public void resetData();
}
