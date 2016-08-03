package it.webred.ct.service.muidocfa.web.bean.util;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

import java.util.ArrayList;
import java.util.List;

public class SuggestionBean extends MuiDocfaBaseBean {

	private String tipo;
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Object> getSuggestionByVia(Object obj) {

		String viaDocfa = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (viaDocfa != null && !viaDocfa.trim().equalsIgnoreCase("NULL")
				&& viaDocfa.length() >= 3) {
			try {

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				dataIn.setObj(viaDocfa);
				fillEnte(dataIn);
				
				if("ICI".equalsIgnoreCase(tipo))
					result = docfaIciReportService.getSuggestionVie(dataIn);
				else if("TAR".equalsIgnoreCase(tipo))
					result = docfaTarReportService.getSuggestionVie(dataIn);

				logger.info("Suggestion Vie "+tipo+" Result Size["+ result.size()+"]");
				
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}

	public List<Object> getSuggestionByCognome(Object obj) {

		String cognome = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (cognome != null && !cognome.trim().equalsIgnoreCase("NULL")
				&& cognome.length() >= 3) {
			try {

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				dataIn.setObj(cognome);
				fillEnte(dataIn);
				
				if("ICI".equalsIgnoreCase(tipo))
					result = docfaIciReportService.getSuggestionCognomi(dataIn);
				else if("TAR".equalsIgnoreCase(tipo))
					result = docfaTarReportService.getSuggestionCognomi(dataIn);
			
				logger.info("Suggestion Cognomi "+tipo+" Result Size["+ result.size()+"]");
				
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}

	public List<Object> getSuggestionByNome(Object obj) {

		String nome = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (nome != null && !nome.trim().equalsIgnoreCase("NULL")
				&& nome.length() >= 3) {
			try {

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				dataIn.setObj(nome);
				fillEnte(dataIn);
			
				if("ICI".equalsIgnoreCase(tipo))
					result = docfaIciReportService.getSuggestionNomi(dataIn);
				else if("TAR".equalsIgnoreCase(tipo))
					result = docfaTarReportService.getSuggestionNomi(dataIn);
			
				logger.info("Suggestion Nomi "+tipo+" Result Size["+ result.size()+"]");
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}

	public List<Object> getSuggestionByCodFisc(Object obj) {

		String codFisc = (String) obj;

		List<Object> result = new ArrayList<Object>();

		if (codFisc != null && !codFisc.trim().equalsIgnoreCase("NULL")
				&& codFisc.length() >= 3) {
			try {

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				dataIn.setObj(codFisc);
				fillEnte(dataIn);
				
				if("ICI".equalsIgnoreCase(tipo))
					result = docfaIciReportService.getSuggestionCodFisc(dataIn);
				else if("TAR".equalsIgnoreCase(tipo))
					result = docfaTarReportService.getSuggestionCodFisc(dataIn);
			
				logger.info("Suggestion Cod.Fisc. "+tipo+" Result Size["+ result.size()+"]");
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return result;
	}

}
