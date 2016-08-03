package it.webred.ct.service.tares.data.access;



import it.webred.ct.service.tares.data.access.dao.TariffeDAO;

import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.model.SetarBilancioAnnoCorr;
import it.webred.ct.service.tares.data.model.SetarCoeff;
import it.webred.ct.service.tares.data.model.SetarConsuntivoAnnoPrec;
import it.webred.ct.service.tares.data.model.SetarTariffa;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class TariffeServiceBean extends TaresServiceBaseBean implements TariffeService {
	
	private static final long serialVersionUID = 2854484118076909217L;
	
	@Autowired
	private TariffeDAO tariffeDAO;
	
	public List<SetarCoeff> getCoeff(DataInDTO dataIn){
		
		return tariffeDAO.getCoeff(dataIn.getCriteriaCoeff());
	}//-------------------------------------------------------------------------
	
	public SetarCoeff updCoeff(DataInDTO dataIn){
		
		return tariffeDAO.updCoeff( (SetarCoeff)dataIn.getObj() );
	}//-------------------------------------------------------------------------
	
	public List<SetarTariffa> getTariffe(DataInDTO dataIn){
		
		return tariffeDAO.getTariffe(dataIn.getCriteriaCoeff());
	}//-------------------------------------------------------------------------
	
	public SetarTariffa updTariffa(DataInDTO dataIn){
		
		return tariffeDAO.updTariffa((SetarTariffa)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public void addTariffa(DataInDTO dataIn){
		
		tariffeDAO.addTariffa( (SetarTariffa) dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public List<SetarBilancioAnnoCorr> getBilancio(DataInDTO dataIn){
		
		return tariffeDAO.getBilancio(dataIn.getCriteriaCoeff());
	}//-------------------------------------------------------------------------
	
	public SetarBilancioAnnoCorr updBilancio(DataInDTO dataIn){
		
		return tariffeDAO.updBilancio( (SetarBilancioAnnoCorr)dataIn.getObj() );
	}//-------------------------------------------------------------------------
	
	public List<SetarConsuntivoAnnoPrec> getConsuntivo(DataInDTO dataIn){
		
		return tariffeDAO.getConsuntivo(dataIn.getCriteriaCoeff());
	}//-------------------------------------------------------------------------
	
	public SetarConsuntivoAnnoPrec updConsuntivo(DataInDTO dataIn){
		
		return tariffeDAO.updConsuntivo( (SetarConsuntivoAnnoPrec)dataIn.getObj() );
	}//-------------------------------------------------------------------------
	
	public List<Object> getClassiTarsu(DataInDTO dataIn){
		return tariffeDAO.getClassiTarsu(dataIn.getCriteriaCoeff());
	}//-------------------------------------------------------------------------
	
	public List<Object> getDistribuzioneSupTotTarsu(DataInDTO dataIn){
		return tariffeDAO.getDistribuzioneSupTotTarsu(dataIn.getCriteriaCoeff());
	}//-------------------------------------------------------------------------
	
	public List<Object> getDistribuzioneComponenti(DataInDTO dataIn){
		return tariffeDAO.getDistribuzioneComponenti(dataIn.getCriteriaCoeff());
	}//-------------------------------------------------------------------------
	
	
	
}
