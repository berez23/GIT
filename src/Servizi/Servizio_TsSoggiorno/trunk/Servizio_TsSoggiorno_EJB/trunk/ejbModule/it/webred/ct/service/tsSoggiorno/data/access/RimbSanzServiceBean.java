package it.webred.ct.service.tsSoggiorno.data.access;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.dao.RimbSanzDAO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class RimbSanzServiceBean extends TsSoggiornoServiceBaseBean implements
		RimbSanzService {
	/**
	 * 
	 */

	@Autowired
	private RimbSanzDAO rimbsanzDAO;

	public List<RimbSanzDTO> getRimbSanzByCriteria(DataInDTO dataIn){
		return rimbsanzDAO.getRimbSanzByCriteria((RimbSanzSearchCriteria) dataIn.getObj());
	}
	
	public Long getRimbSanzCountByCriteria(DataInDTO dataIn){
		return rimbsanzDAO.getRimbSanzCountByCriteria((RimbSanzSearchCriteria) dataIn.getObj());
	}
	
	public void saveRimborso(DataInDTO dataIn){
		rimbsanzDAO.saveRimborso((IsRimborso) dataIn.getObj());
	}
	
	public void deleteRimborso(DataInDTO dataIn){
		rimbsanzDAO.deleteRimborso(dataIn.getId());
	}
	
	public void saveSanzione(DataInDTO dataIn){
		rimbsanzDAO.saveSanzione((IsSanzione) dataIn.getObj());
	}
	
	public void deleteSanzione(DataInDTO dataIn){
		rimbsanzDAO.deleteSanzione(dataIn.getId());
	}
}
