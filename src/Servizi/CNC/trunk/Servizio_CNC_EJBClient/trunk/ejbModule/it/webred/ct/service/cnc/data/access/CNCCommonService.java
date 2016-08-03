package it.webred.ct.service.cnc.data.access;

//import it.webred.ct.service.cnc.data.access.dto.CNCDTO;
import it.webred.ct.data.access.basic.cnc.CNCDTO;

import javax.ejb.Remote;

@Remote
public interface CNCCommonService {

	public String getAmbitoDescr(CNCDTO dto);
	
	public String getCodiceEntrataDescr(CNCDTO dto);
	
}
