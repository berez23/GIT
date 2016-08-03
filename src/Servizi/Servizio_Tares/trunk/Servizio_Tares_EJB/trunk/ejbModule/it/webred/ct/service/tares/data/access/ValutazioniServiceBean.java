package it.webred.ct.service.tares.data.access;


import it.webred.ct.service.tares.data.access.dao.ValutazioniDAO;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.model.SetarElab;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class ValutazioniServiceBean extends TaresServiceBaseBean implements ValutazioniService {
	
	private static final long serialVersionUID = -3279427167954265192L;
	
	@Autowired
	private ValutazioniDAO valutazioniDAO;
	
	public List<SetarElab> getElaborazioni(DataInDTO dataIn){
		
		return valutazioniDAO.getElaborazioni(dataIn.getCriteria());
	}//-------------------------------------------------------------------------
	
	public Number getElaborazioniCount(DataInDTO dataIn){
		
		return valutazioniDAO.getElaborazioniCount(dataIn.getCriteria());
	}//-------------------------------------------------------------------------
	
	public SetarElab getElaborazione(DataInDTO dataIn){

		return valutazioniDAO.getElaborazione((SetarElab)dataIn.getObj());
	}//-------------------------------------------------------------------------


}
