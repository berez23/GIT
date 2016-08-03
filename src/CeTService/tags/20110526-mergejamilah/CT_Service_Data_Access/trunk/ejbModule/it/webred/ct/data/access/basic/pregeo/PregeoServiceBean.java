package it.webred.ct.data.access.basic.pregeo;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.pregeo.PregeoService;
import it.webred.ct.data.access.basic.pregeo.dao.PregeoDAO;
import it.webred.ct.data.access.basic.pregeo.dto.RicercaPregeoDTO;
import it.webred.ct.data.model.pregeo.PregeoInfo;

@Stateless
public class PregeoServiceBean extends CTServiceBaseBean implements PregeoService {
	@Autowired
	private PregeoDAO pregeoDAO;
	@Override
	public List<PregeoInfo> getDatiPregeo(RicercaPregeoDTO rp) {
		List<PregeoInfo> listaPregeo = new ArrayList<PregeoInfo>();
		if (rp.getFoglio()!=null && rp.getFoglio().longValue() != 0 &&
			rp.getParticella() != null && !rp.getParticella().equals("")	)
			listaPregeo= pregeoDAO.getDatiPregeoByFabbricato(rp);
		return listaPregeo;
	}

}
