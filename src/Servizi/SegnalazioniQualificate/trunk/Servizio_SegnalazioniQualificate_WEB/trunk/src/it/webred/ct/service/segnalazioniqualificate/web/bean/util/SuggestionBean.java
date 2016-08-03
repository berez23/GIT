package it.webred.ct.service.segnalazioniqualificate.web.bean.util;

import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioniDataIn;
import it.webred.ct.data.model.catasto.Siticomu;
import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SuggestionBean extends SegnalazioniQualificateBaseBean {

	public List<Object> getSuggestionByAccCognome(Object obj) {
		return getSuggestionByCognome(obj, "ACC");
	}
	
	public List<Object> getSuggestionByResCognome(Object obj) {
		return getSuggestionByCognome(obj, "RES");
	}
	
	public List<Object> getSuggestionByAccNome(Object obj) {
		return getSuggestionByNome(obj, "ACC");
	}
	
	public List<Object> getSuggestionByResNome(Object obj) {
		return getSuggestionByNome(obj, "RES");
	}
	
	public List<Object> getSuggestionByAccCodFisc(Object obj) {
		return getSuggestionByCodFisc(obj, "ACC");
	}
	
	public List<Object> getSuggestionByResCodFisc(Object obj) {
		return getSuggestionByCodFisc(obj, "RES");
	}
	
	private List<Object> getSuggestionByCognome(Object obj1, String tipo) {

		String cognome = (String) obj1;
		List<Object> result = new ArrayList<Object>();

		if (cognome != null && !cognome.trim().equalsIgnoreCase("NULL")
				&& cognome.length() >= 3) {
			try {

				SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(cognome);
				dataIn.setObj2(tipo);
				result = segnalazioneService.getSuggestionCognomi(dataIn);
				
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}
	
	public List<Object> getSuggestionByAccDenominazione(Object obj) {

		String denom = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (denom != null && !denom.trim().equalsIgnoreCase("NULL") && denom.length() >= 3) {
			try {
				SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(denom);
				result = segnalazioneService.getSuggestionDenominazione(dataIn);

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}

	private List<Object> getSuggestionByNome(Object obj, String tipo) {

		String nome = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (nome != null && !nome.trim().equalsIgnoreCase("NULL") && nome.length() >= 3) {
			try {

				SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(nome);
				dataIn.setObj2(tipo);
				result = segnalazioneService.getSuggestionNomi(dataIn);

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}

	private List<Object> getSuggestionByCodFisc(Object obj, String tipo) {

		String codFisc = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (codFisc != null && !codFisc.trim().equalsIgnoreCase("NULL") && codFisc.length() >= 3) {
			try {
				SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(codFisc);
				dataIn.setObj2(tipo);
				result = segnalazioneService.getSuggestionCodFisc(dataIn);

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}
	
	public List<Siticomu> getSuggestionComune(Object obj) {

		String desc = (String) obj;

		List<Siticomu>  comuni = new ArrayList<Siticomu>();
		try {
			if (desc != null && !desc.trim().equalsIgnoreCase("NULL")) {
				//if(desc.length()>=2)
					comuni = catastoService.getListaSiticomu(desc);
			}	
		} catch (Throwable t) {
			super.addErrorMessage("suggestion.error", t.getMessage());
			t.printStackTrace();
		}
		return comuni;	
		
	}

}
