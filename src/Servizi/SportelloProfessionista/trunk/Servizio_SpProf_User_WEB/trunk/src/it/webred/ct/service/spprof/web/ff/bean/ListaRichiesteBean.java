package it.webred.ct.service.spprof.web.ff.bean;

import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

public class ListaRichiesteBean extends SpProfBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private GestRichiestaService richiestaService = (GestRichiestaService) getEjb("Servizio_FasFab", "Servizio_FasFab_EJB", "GestRichiestaServiceBean");
	
	private List<FiltroRichieste> lista;
	
	@PostConstruct
	public void init(){
		
		lista = new ArrayList<FiltroRichieste>();
		
		//Imposto la ricerca per il solo utente correntemente autenticato
		FFFiltroRichiesteSearchCriteria criteria = new FFFiltroRichiesteSearchCriteria();
		criteria.setEnteId(this.getEnte());
		criteria.setUserGesRic(this.getUsername());
		
		Long numberRecord = richiestaService.getRecordCount(criteria);
		List<FiltroRichiesteDTO> richieste = richiestaService.filtraRichieste(criteria,0,numberRecord.intValue());
		for(FiltroRichiesteDTO rich:richieste)
		{
			FiltroRichieste e = new FiltroRichieste();
			e.setRichiesta(rich);
			lista.add(e);
		}
		
	}

	public List<FiltroRichieste> getLista() {
		return lista;
	}

	public void setLista(List<FiltroRichieste> lista) {
		this.lista = lista;
	}
}
