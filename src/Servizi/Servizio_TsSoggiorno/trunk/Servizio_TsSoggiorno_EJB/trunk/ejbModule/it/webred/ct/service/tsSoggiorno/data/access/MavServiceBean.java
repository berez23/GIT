package it.webred.ct.service.tsSoggiorno.data.access;

import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.dao.MavDAO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.VersamentoDTO;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class MavServiceBean extends TsSoggiornoServiceBaseBean implements
		MavService {
	

	@Autowired
	private MavDAO mavDAO;

	public List<VersamentoDTO> getListaVersamentiMav(DataInDTO dataIn) {
		return mavDAO.getVersamentiMavByCodFiscale(dataIn.getCodFiscale());
	}
	
	public List<VersamentoDTO> getListaVersamentiMavPeriodo(DataInDTO dataIn) {
		return mavDAO.getVersamentiMavByCodFiscalePeriodo(dataIn);
	}

}
