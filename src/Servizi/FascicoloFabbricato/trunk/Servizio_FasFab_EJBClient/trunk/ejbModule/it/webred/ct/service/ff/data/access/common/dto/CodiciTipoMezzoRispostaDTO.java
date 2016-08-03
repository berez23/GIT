package it.webred.ct.service.ff.data.access.common.dto;

import it.webred.ct.service.ff.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class CodiciTipoMezzoRispostaDTO extends CeTBaseObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private CodiciTipoMezzoRisposta codice;
	
	public void setCodice(CodiciTipoMezzoRisposta codice) {
		this.codice = codice;
	}
	public CodiciTipoMezzoRisposta getCodice() {
		return codice;
	}
	
}
