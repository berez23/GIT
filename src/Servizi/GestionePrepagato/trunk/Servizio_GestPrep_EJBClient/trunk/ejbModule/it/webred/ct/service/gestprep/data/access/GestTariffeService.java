package it.webred.ct.service.gestprep.data.access;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepTariffaVisura;


import java.util.List;

import javax.ejb.Remote;

@Remote
public interface GestTariffeService {

	public Long createTariffa(GestPrepDTO tariffa);
	
	public boolean updateTariffa(GestPrepDTO tariffa);
	
	public List<GestPrepTariffaVisura> getTariffeList(GestPrepDTO tariffa);
	
	public GestPrepTariffaVisura getTariffa(GestPrepDTO tariffa);

}
