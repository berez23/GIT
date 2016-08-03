package it.webred.ct.service.comma336.web.bean.util.suggestion;

import it.webred.ct.data.model.catasto.Siticomu;
import it.webred.ct.service.comma336.web.Comma336BaseBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

public class SuggestionBean extends Comma336BaseBean {
	
	private final List<SelectItem> listaAndrone = Arrays.asList(
			new SelectItem(0, "Angusto(0)"),
			new SelectItem(2, "Normale(2)"),
			new SelectItem(3, "Ampio(3)"));
	
	private final List<SelectItem> listaNumAbitazioniPiano = Arrays.asList(
			new SelectItem(0, "4 o più(0)"),
			new SelectItem(2, "3(2)"),
			new SelectItem(3, "Fino a 2(3)"));
	
	private final List<SelectItem> listaAvgSup = Arrays.asList(
			new SelectItem(0, "fino a 50-65 mq(0)"),
			new SelectItem(1, "intermedia(1)"),
			new SelectItem(2, "oltre i 100-120 mq(2)"));
	
	private final List<SelectItem> listaSiNo = Arrays.asList(
			new SelectItem("N", "No"),
			new SelectItem("S", "Si"));
	
	private final List<SelectItem> listaTotaleParziale = Arrays.asList(
			new SelectItem("N", "Nessuna"),
			new SelectItem("P", "Parziale"),
			new SelectItem("T", "Totale"));
	
	private final List<SelectItem> listaTotaleIParziale = Arrays.asList(
			new SelectItem("N", "Nessuna"),
			new SelectItem("P", "Parziale"),
			new SelectItem("I", "Parziale/Totale"),
			new SelectItem("T", "Totale"));

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

				/*SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(cognome);
				dataIn.setObj2(tipo);
				result = segnalazioneService.getSuggestionCognomi(dataIn);*/
				
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return result;
	}
	
	public List<Object> getSuggestionByAccDenominazione(Object obj) {

		String denom = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (denom != null && !denom.trim().equalsIgnoreCase("NULL") && denom.length() >= 3) {
			try {
				/*SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(denom);
				result = segnalazioneService.getSuggestionDenominazione(dataIn);
*/
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return result;
	}

	private List<Object> getSuggestionByNome(Object obj, String tipo) {

		String nome = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (nome != null && !nome.trim().equalsIgnoreCase("NULL") && nome.length() >= 3) {
			try {

				/*SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(nome);
				dataIn.setObj2(tipo);
				result = segnalazioneService.getSuggestionNomi(dataIn);*/

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return result;
	}

	private List<Object> getSuggestionByCodFisc(Object obj, String tipo) {

		String codFisc = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (codFisc != null && !codFisc.trim().equalsIgnoreCase("NULL") && codFisc.length() >= 3) {
			try {
				/*SegnalazioniDataIn dataIn = this.getInitRicercaParams();
				dataIn.setObj1(codFisc);
				dataIn.setObj2(tipo);
				result = segnalazioneService.getSuggestionCodFisc(dataIn);*/

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				logger.error(t.getMessage(), t);
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
					comuni = getCatastoService().getListaSiticomu(desc);
			}	
		} catch (Throwable t) {
			super.addErrorMessage("suggestion.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		return comuni;	
		
	}

	public List<SelectItem> getListaSiNo() {
		return listaSiNo;
	}

	public List<SelectItem> getListaTotaleParziale() {
		return listaTotaleParziale;
	}

	public List<SelectItem> getListaTotaleIParziale() {
		return listaTotaleIParziale;
	}

	public List<SelectItem> getListaAndrone() {
		return listaAndrone;
	}

	public List<SelectItem> getListaNumAbitazioniPiano() {
		return listaNumAbitazioniPiano;
	}

	public List<SelectItem> getListaAvgSup() {
		return listaAvgSup;
	}

}
