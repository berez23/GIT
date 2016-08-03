package it.webred.ct.data.access.basic.rette;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.rette.dao.RetteDAO;
import it.webred.ct.data.access.basic.traffico.dao.TrafficoDAO;
import it.webred.ct.data.access.basic.traffico.dto.TrafficoSearchCriteria;
import it.webred.ct.data.model.rette.SitRttBollette;
import it.webred.ct.data.model.rette.SitRttDettBollette;
import it.webred.ct.data.model.rette.SitRttRateBollette;
import it.webred.ct.data.model.traffico.SitTrffMulte;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class RetteServiceBean extends CTServiceBaseBean implements RetteService {

	@Autowired
	private RetteDAO retteDAO;

	@Override
	public List<SitRttBollette> getListaBolletteNonPagateByCodFis(
			RetteDataIn dataIn) throws RetteServiceException {
		return retteDAO.getListaBolletteNonPagateByCodFis((String) dataIn.getObj());
	}

	@Override
	public List<SitRttBollette> getListaBollettePagateByCodFis(
			RetteDataIn dataIn) throws RetteServiceException {
		return retteDAO.getListaBollettePagateByCodFis((String) dataIn.getObj());
	}

	@Override
	public List<SitRttDettBollette> getListaDettagliBollettaByCodBoll(
			RetteDataIn dataIn) throws RetteServiceException {
		return retteDAO.getListaDettaglioBolletteByCodBoll((String) dataIn.getObj());
	}

	@Override
	public List<SitRttRateBollette> getListaRateBollettaByCodBoll(
			RetteDataIn dataIn) throws RetteServiceException {
		return retteDAO.getListaRateBolletteByCodBoll((String) dataIn.getObj());
	}



	

}
