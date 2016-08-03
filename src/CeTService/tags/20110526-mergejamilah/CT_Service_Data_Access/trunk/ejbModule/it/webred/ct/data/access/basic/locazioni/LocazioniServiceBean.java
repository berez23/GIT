package it.webred.ct.data.access.basic.locazioni;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.locazioni.dao.LocazioniDAO;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;
@Stateless
public class LocazioniServiceBean extends CTServiceBaseBean implements	LocazioniService {
	@Autowired
	private LocazioniDAO  locazioniDAO;
	@Override
	public List<LocazioniA> getLocazioniByCF(RicercaLocazioniDTO rl) {
		return locazioniDAO.getLocazioniByCF(rl);
	}
	@Override
	public List<LocazioniA> getLocazioniOggByKey(RicercaLocazioniDTO rl) {
		return locazioniDAO.getLocazioniOggByKey(rl);
	}
	@Override
	public LocazioniB getLocazioneSoggByKey(RicercaLocazioniDTO rl) {
		return locazioniDAO.getLocazioneSoggByKey(rl);
	}

}
