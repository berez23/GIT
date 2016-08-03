package it.webred.ct.data.access.basic.traffico;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.traffico.dao.TrafficoDAO;
import it.webred.ct.data.access.basic.traffico.dto.TrafficoSearchCriteria;
import it.webred.ct.data.model.traffico.SitTrffMulte;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class TrafficoServiceBean extends CTServiceBaseBean implements TrafficoService {

	@Autowired
	private TrafficoDAO trafficoDAO;

	@Override
	public List<SitTrffMulte> getListaMulteByCodFis(TrafficoDataIn dataIn)
			throws TrafficoServiceException {
		TrafficoSearchCriteria criteria = new TrafficoSearchCriteria();
		if(dataIn.getObj() != null)
			criteria.setCodFiscale((String) dataIn.getObj());
		if(dataIn.getObj2() != null)
			criteria.setNumVerbale((String) dataIn.getObj2());
		if(dataIn.getObj3() != null)
			criteria.setTarga((String) dataIn.getObj3());
		return trafficoDAO.getListaMulteByCriteria(criteria);
	}

	

}
