package it.webred.ct.service.spprof.web.ff.bean;

import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;

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
	
	public void setDescTipoProvenienza(String descTipoProvenienza) {
		this.descTipoProvenienza = descTipoProvenienza;
	}
	public String getDescTipoProvenienza() {
		if (richiesta==null || richiesta.getTipoProvenienza()==null)
			descTipoProvenienza =  "";
		else 
			descTipoProvenienza = richiesta.getDescTipoProvenienza();
		
		return descTipoProvenienza;
	}
	
	public void setDescTipoRichiesta(String descTipoRichiesta) {
		this.descTipoRichiesta = descTipoRichiesta;
	}
	public String getDescTipoRichiesta() {
		if (richiesta==null || richiesta.getTipoRichiesta()==null)
			descTipoRichiesta = "";
		else
			descTipoRichiesta = richiesta.getDescTipoRichiesta();
				
			
		return descTipoRichiesta;
	}
}
