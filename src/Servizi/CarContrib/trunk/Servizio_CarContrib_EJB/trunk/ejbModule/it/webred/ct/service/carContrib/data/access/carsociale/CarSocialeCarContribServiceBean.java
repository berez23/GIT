package it.webred.ct.service.carContrib.data.access.carsociale;
import it.webred.ct.data.access.basic.cartellasociale.CartellaSocialeService;
import it.webred.ct.data.access.basic.cartellasociale.dto.*;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class CosapCarContribServiceBean
*/
@Stateless
public class CarSocialeCarContribServiceBean extends CarContribServiceBaseBean implements CarSocialeCarContribService {
	
	private static final long serialVersionUID = 1L;
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CartellaSocialeServiceBean")
	private CartellaSocialeService csService;
	
	
	public List<InterventoDTO> getInterventiByCF(RicercaCarSocDTO rs) {
		return csService.getInterventiByCF(rs);
	}

}
