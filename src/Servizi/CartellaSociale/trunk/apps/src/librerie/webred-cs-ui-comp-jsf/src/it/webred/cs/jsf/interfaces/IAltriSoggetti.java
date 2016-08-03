package it.webred.cs.jsf.interfaces;

import java.util.List;

import it.webred.cs.data.model.CsASoggetto;

public interface IAltriSoggetti {
	
	public void setListaAltriSoggetti(List<CsASoggetto> listaAltriSoggetti);
	public String getLabelSoggetto();

}
