package it.webred.fb.ejb.dto.locazione;


import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.ejb.dto.DatoSpec;

public class DatiTerreni extends DatiCatastali {

	private static final long serialVersionUID = 1L;
	
	private DmBTerreno dettaglio;

	public DatiTerreni(DatoSpec codInventario, DatoSpec codComune, DatoSpec sezione, DatoSpec foglio, DatoSpec mappale, DatoSpec provenienza){
		super(codInventario,null,codComune,sezione,foglio,mappale,provenienza);
	}

	public DmBTerreno getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(DmBTerreno dettaglio) {
		this.dettaglio = dettaglio;
	}
	

}
