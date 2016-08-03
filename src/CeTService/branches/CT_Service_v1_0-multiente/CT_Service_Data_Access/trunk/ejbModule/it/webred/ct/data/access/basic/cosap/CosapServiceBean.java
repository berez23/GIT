package it.webred.ct.data.access.basic.cosap;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.cosap.dao.CosapDAO;
import it.webred.ct.data.access.basic.cosap.dto.RicercaSoggettoCosapDTO;
import it.webred.ct.data.model.cosap.SitTCosapContrib;
import it.webred.ct.data.model.cosap.SitTCosapTassa;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;


@Stateless
public class CosapServiceBean extends CTServiceBaseBean implements CosapService {
	@Autowired 
	private CosapDAO cosapDAO;
	
	@Override
	public List<SitTCosapContrib> searchSoggetto(RicercaSoggettoCosapDTO rs) throws CosapServiceException {
		return cosapDAO.searchSoggetto(rs);
	}

	@Override
	public List<SitTCosapTassa> getDatiOggettiBySogg(RicercaSoggettoCosapDTO rs) {
		if (rs.getIdSoggCosap()!=null && !rs.getIdSoggCosap().equalsIgnoreCase(""))
			return cosapDAO.getDatiOggettiByIdSogg(rs) ;
		else
			return null;
	}

	@Override
	public SitTCosapContrib getDatiSoggettoById(RicercaSoggettoCosapDTO rs) {
		if (rs.getIdSoggCosap()!=null && !rs.getIdSoggCosap().equalsIgnoreCase(""))
			return cosapDAO.getDatiSoggettoById(rs);
		else
			return null;
	}

	

}
