package it.webred.ct.data.access.basic.concedilizie;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.SoggettoConcessioneDTO;
import it.webred.ct.data.model.concedilizie.SitCConcessioni;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ConcessioniEdilizieService {
	public List<SitCConcessioni> getConcessioniByFabbricato(RicercaOggettoCatDTO ro) ;
	public SitCConcessioni getConcessioneById(RicercaConcEdilizieDTO ro) ; 
	public List<SoggettoConcessioneDTO> getSoggettiByConcessione(RicercaConcEdilizieDTO ro) ;
	public String getStringaImmobiliByConcessione(RicercaConcEdilizieDTO ro) ;
	public List<ConcessioneDTO> getDatiConcessioniByFabbricato(RicercaOggettoCatDTO ro) ;
}
