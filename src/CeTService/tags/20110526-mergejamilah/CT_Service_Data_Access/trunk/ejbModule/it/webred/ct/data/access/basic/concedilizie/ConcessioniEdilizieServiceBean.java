package it.webred.ct.data.access.basic.concedilizie;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;
import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.concedilizie.dao.ConcessioniEdilizieDAO;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.SoggettoConcessioneDTO;
import it.webred.ct.data.model.concedilizie.SitCConcessioni;

@Stateless 
public class ConcessioniEdilizieServiceBean extends CTServiceBaseBean implements		ConcessioniEdilizieService {
	@Autowired
	private ConcessioniEdilizieDAO concEdilizieDAO;
	private static final long serialVersionUID = 1L;
	@Override
	public SitCConcessioni getConcessioneById(RicercaConcEdilizieDTO ro) {
		return concEdilizieDAO.getConcessioneById(ro);
	}
	@Override
	public List<SitCConcessioni> getConcessioniByFabbricato(RicercaOggettoCatDTO ro) {
		return concEdilizieDAO.getConcessioniByFabbricato(ro);
	}
	
	@Override
	public List<SoggettoConcessioneDTO> getSoggettiByConcessione(RicercaConcEdilizieDTO ro) {
		return concEdilizieDAO.getSoggettiByConcessione(ro);
	}
	@Override
	public String getStringaImmobiliByConcessione(RicercaConcEdilizieDTO ro) {
		return concEdilizieDAO.getStringaImmobiliByConcessione(ro);
	}
	@Override
	public List<ConcessioneDTO> getDatiConcessioniByFabbricato(RicercaOggettoCatDTO ro) {
		List<ConcessioneDTO> lista=new ArrayList<ConcessioneDTO>();
		
		List<SitCConcessioni> listaConc =  getConcessioniByFabbricato(ro);
		if(listaConc==null)
			return lista;
		
		ConcessioneDTO datiConc =null;
		RicercaConcEdilizieDTO rc = new RicercaConcEdilizieDTO();
		rc.setUserId(ro.getUserId());
		rc.setEnteId(ro.getEnteId());
		for (SitCConcessioni conc:listaConc) {
			datiConc = new ConcessioneDTO();
			rc.setIdExtConc(conc.getIdExt());
			datiConc.valorizzaDatiConc(conc, getSoggettiByConcessione(rc), getStringaImmobiliByConcessione(rc));
			lista.add(datiConc);
		}
		return lista;
	}
	
}
