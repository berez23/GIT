package it.webred.ct.service.tsSoggiorno.data.access;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.dao.TariffaDAO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class TariffaServiceBean extends TsSoggiornoServiceBaseBean implements
		TariffaService {
	/**
	 * 
	 */

	@Autowired
	private TariffaDAO tariffaDAO;

	public IsTariffa getTariffaByPeriodoClasse(DataInDTO dataIn) {
		return tariffaDAO.getTariffaByPeriodoClasse(dataIn.getId(), dataIn.getId2());
	}
	
	public List<TariffaDTO> getListaTariffe(DataInDTO dataIn) {
		return tariffaDAO.getListaTariffe();
	}
	
	public void saveTariffa(DataInDTO dataIn){
		tariffaDAO.saveTariffa((IsTariffa) dataIn.getObj());
	}
	
	public void deleteTariffa(DataInDTO dataIn){
		tariffaDAO.deleteTariffa(dataIn.getId(), dataIn.getId2());
	}
}
