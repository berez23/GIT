package it.webred.fb.ejb.dto.locazione;

import it.webred.fb.ejb.dto.Dato;
import it.webred.fb.ejb.dto.DatoSpec;

public class DatoDaProvenienza extends Dato {
	private static final long serialVersionUID = 1L;
	
	private DatoSpec provenienza = new DatoSpec();
	
    public DatoDaProvenienza (DatoSpec provenienza) {
    	this.provenienza = provenienza;
    }

	public DatoSpec getProvenienza() {

		return provenienza;
	}

	public void setProvenienza(DatoSpec provenienza) {
		this.provenienza = provenienza;
	}
}
