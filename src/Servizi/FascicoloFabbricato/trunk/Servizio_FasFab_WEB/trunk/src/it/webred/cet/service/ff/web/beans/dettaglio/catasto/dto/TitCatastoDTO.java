package it.webred.cet.service.ff.web.beans.dettaglio.catasto.dto;

import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;

import java.io.Serializable;
import java.util.List;

public class TitCatastoDTO implements Serializable {

	private ConsSoggTab soggetto;
	private List<SiticonduzImmAll> titolarita;
	
	public ConsSoggTab getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(ConsSoggTab soggetto) {
		this.soggetto = soggetto;
	}
	public List<SiticonduzImmAll> getTitolarita() {
		return titolarita;
	}
	public void setTitolarita(List<SiticonduzImmAll> titolarita) {
		this.titolarita = titolarita;
	}
	
	
}
