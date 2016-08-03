package it.webred.ct.data.access.basic.concedilizie.dao;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieException;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.SoggettoConcessioneDTO;
import it.webred.ct.data.model.concedilizie.SitCConcessioni;

import java.util.List;

public interface ConcessioniEdilizieDAO {
	public List<SitCConcessioni> getConcessioniByFabbricato(RicercaOggettoCatDTO ro) throws ConcessioniEdilizieException; 
	public SitCConcessioni getConcessioneById(RicercaConcEdilizieDTO ro) throws ConcessioniEdilizieException; 
	public List<SoggettoConcessioneDTO> getSoggettiByConcessione(RicercaConcEdilizieDTO ro) throws ConcessioniEdilizieException;
	public String getStringaImmobiliByConcessione(RicercaConcEdilizieDTO ro) throws ConcessioniEdilizieException;
}
