package it.webred.ct.service.ff.data.access.risposte.dto;

import java.io.Serializable;

import it.webred.ct.service.ff.data.model.FFRisposte;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class RispostaDTO extends CeTBaseObject implements Serializable {

	private FFRisposte risposta = new FFRisposte();

	public void setRisposta(FFRisposte risposta) {
		this.risposta = risposta;
	}

	public FFRisposte getRisposta() {
		return risposta;
	}
	
	
}
