package it.webred.ct.service.tares.data.access;


import it.webred.ct.service.tares.data.access.dao.StatisticheDAO;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.model.SetarDia;
import it.webred.ct.service.tares.data.model.SetarStat;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class StatisticheServiceBean extends TaresServiceBaseBean implements StatisticheService {
	
	private static final long serialVersionUID = -3279427167954265192L;
	
	@Autowired
	private StatisticheDAO statisticheDAO;
	
	public List<SetarStat> getStatistiche(DataInDTO dataIn){
		
		return statisticheDAO.getStatistiche(dataIn.getCriteriaStat());
	}//-------------------------------------------------------------------------
	
	public Number getStatisticheCount(DataInDTO dataIn){
		
		return statisticheDAO.getStatisticheCount(dataIn.getCriteriaStat());
	}//-------------------------------------------------------------------------
	
	public List<SetarDia> getDiagnostiche(DataInDTO dataIn){
		
		return statisticheDAO.getDiagnostiche(dataIn.getCriteriaDiag());
	}//-------------------------------------------------------------------------


}
