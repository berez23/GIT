package it.webred.ct.service.carContrib.web.beans;

import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteDTO;
import it.webred.ct.service.carContrib.web.Utility;
import it.webred.ct.service.carContrib.web.Utility.TipoRichiesta;

public class FiltroRichieste {

	private FiltroRichiesteDTO richiesta;
	private String descTipoProvenienza;
	private String descTipoRichiesta;
	
	public FiltroRichiesteDTO getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(FiltroRichiesteDTO richiesta) {
		this.richiesta = richiesta;
	}
	
	public String getDescTipoProvenienza() {
	
		if (richiesta==null || richiesta.getTipoProvenienza()==null)
			return "";
		
		if(richiesta.getTipoProvenienza().equals(Utility.TipoProvenienza.INTERNA.getTipoProvenienza()))
			return Utility.TipoProvenienza.INTERNA.toString();
		else
			return Utility.TipoProvenienza.WEB.toString();
		
	}
	public String getDescTipoRichiesta() {
		
		if (richiesta==null || richiesta.getTipoRichiesta()==null)
			return "";
		
		if (richiesta.getTipoRichiesta().equals(TipoRichiesta.CARTELLA.getTipoRichiesta()))
			return TipoRichiesta.CARTELLA.toString();
		else
			return "-";
	}
}
