package it.webred.ct.service.tares.data.access;
import it.webred.ct.service.tares.data.access.dao.SezioniDAO;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.model.CataSezioni;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class SezioniServiceBean extends TaresServiceBaseBean implements SezioniService {
	
	@Autowired
	private SezioniDAO sezioniDAO;
	
//	public List<CataSezioni> getSezioniByName(DataInDTO dataIn){
//		return sezioniDAO.getSezioniByName(dataIn.getNome());
//	}//-------------------------------------------------------------------------

	public Object[] getUpdateDate(DataInDTO dataIn){
		return sezioniDAO.getUpdateDate(dataIn.getNome());
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execElab(DataInDTO dataIn){
		return sezioniDAO.execElab(dataIn.getEnteId());
	}//-------------------------------------------------------------------------

	public ArrayList<Object> execElab2(DataInDTO dataIn){
		return sezioniDAO.execElab2(dataIn.getEnteId());
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execStat(DataInDTO dataIn){
		return sezioniDAO.execStat(dataIn.getEnteId());
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execDia(DataInDTO dataIn){
		return sezioniDAO.execDia(dataIn.getEnteId());
	}//-------------------------------------------------------------------------
	
	public ArrayList<Object> execTrasf(DataInDTO dataIn){
		return sezioniDAO.execTrasf(dataIn.getEnteId());
	}//-------------------------------------------------------------------------

}
