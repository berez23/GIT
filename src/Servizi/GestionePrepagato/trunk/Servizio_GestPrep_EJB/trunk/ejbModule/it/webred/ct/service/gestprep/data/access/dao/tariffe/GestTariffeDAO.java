package it.webred.ct.service.gestprep.data.access.dao.tariffe;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepTariffaVisura;


import java.util.List;

public interface GestTariffeDAO {

	public Long createTariffa(GestPrepDTO tariffaDTO) throws GestPrepDAOException ;
	

	public List<GestPrepTariffaVisura> getList(GestPrepDTO tariffaDTO) throws GestPrepDAOException ;
	
	public void updateTariffa(GestPrepDTO tariffaDTO) throws GestPrepDAOException ;
	
	public GestPrepTariffaVisura getTariffa(GestPrepDTO visuraDTO)  throws GestPrepDAOException ;

}
