package it.webred.ct.service.comma336.web.bean.util.suggestion;

import it.webred.ct.data.access.basic.common.dto.RicercaCivicoDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.service.comma336.web.Comma336BaseBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;


public class DocfaSuggestionBean extends Comma336BaseBean{

	private List<SelectItem> listaForniture;
	
	private List<SelectItem> listaCausali;
	
	private List<SelectItem> listaCiviciVia;
	
	private final List<SelectItem> listaTipoDichiarazione = Arrays.asList(
			new SelectItem("3", "Dich. variazione, per stralcio da categoria 'E'"),
			new SelectItem("4", "Dich. fabbricato urbano riguardante ex edificio rurale"),
			new SelectItem("5", "Dich. fabbricato mai dichiarato")
	);
	
	private static final Map<String, String> causali = new HashMap<String, String>() {
		
		private static final long serialVersionUID = 1L;

		{
			put("NC", "ACCATASTAMENTO");
			put("AFF", "DENUNCIA UNITA AFFERENTE");
			put("DIV", "DIVISIONE");
			put("FRZ", "FRAZIONAMENTO");
			put("FUS", "FUSIONE");
			put("AMP", "AMPLIAMENTO");
			put("DET", "DEMOLIZIONE TOTALE");
			put("DEP", "DEMOLIZIONE PARZIALE");
			put("VSI", "VARIAZIONE SPAZI INTERNI");
			put("RST", "RISTRUTTURAZIONE");
			put("FRF", "FRAZIONAMENTO E FUSIONE");
			put("VTO", "VARIAZIONE TOPONOMASTICA");
			put("UFU", "ULTIMAZIONE FABBRICATO URBANO");
			put("VDE", "VARIAZIONE DELLA DESTINAZIONE");
			put("VAR", "ALTRE VARIAZIONI");
			put("VRP", "PRESENTAZIONE PLANIMETRIA MANCANTE");
			put("VMI", "MODIFICA DI IDENTIFICATIVO");
		}
	};
	
	
	
	public void doCaricaCausali() {

		if (listaCausali == null) {
			listaCausali = new ArrayList<SelectItem>();
			try {

				Iterator<String> it = causali.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					SelectItem item = new SelectItem(key, key + " - "
							+ causali.get(key));
					listaCausali.add(item);
				}

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.docfa.causali.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}

	}
	
	public void doCaricaListaForniture(){
		if (listaForniture == null) {
			listaForniture = new ArrayList<SelectItem>();
			
			listaForniture.add(new SelectItem(null,"-Seleziona-"));
			
			try {

				RicercaOggettoDocfaDTO ro = new RicercaOggettoDocfaDTO();
				this.fillEnte(ro);
				List<Date> lista = docfaService.getListaForniture(ro);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat text = new SimpleDateFormat("MMM yyyy");
				
				for (Date f : lista) {
					SelectItem item = new SelectItem(sdf.format(f), text.format(f));
					listaForniture.add(item);
				}

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.docfa.forniture.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		
	}

	public void doCaricaListaCiviciVia(Object via){
		
		listaCiviciVia = new ArrayList<SelectItem>();
		try {
			RicercaCivicoDTO rc = new RicercaCivicoDTO();
			this.fillEnte(rc);
			rc.setDescrizioneVia((String)via);
			List<String> lista = docfaService.getSuggestionCiviciByViaDocfaUiu(rc);
			for (String civico : lista) {
				SelectItem item = new SelectItem(civico, civico);
				listaCiviciVia.add(item);
			}
		} catch (Throwable t) {
			super.addErrorMessage("suggestion.error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
	}
	
	public List<String> getSuggestionByVia(Object obj) {

		String via = (String) obj;

		List<String> result = new ArrayList<String>();

		if (via != null && !via.trim().equalsIgnoreCase("NULL") && via.length() >= 3) {
			try {

				RicercaCivicoDTO rc = new RicercaCivicoDTO();
				this.fillEnte(rc);
				rc.setDescrizioneVia(via);
				result = docfaService.getSuggestionVieDocfaUiu(rc);

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.docfa.via.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return result;
	}
	
	//Vale sia per soggetti giuridici che fisici, effettua la concatenazione di cognome e nome
	public List<String> getSuggestionDichiarante(Object obj) {

		String denominazione = (String) obj;

		List<String> result = new ArrayList<String>();

		if (denominazione != null && !denominazione.trim().equalsIgnoreCase("NULL")
				&& denominazione.length() >= 3) {
			try {

				RicercaSoggettoDTO rs = new RicercaSoggettoDTO();
				fillEnte(rs);
				rs.setDenom(denominazione);
				result = docfaService.getSuggestionDichiarante(rs);
			
			} catch (Throwable t) {
				super.addErrorMessage("suggestion.docfa.dichiarante.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return result;
	}

	public List<SelectItem> getListaForniture() {
		doCaricaListaForniture();
		return listaForniture;
	}

	public void setListaForniture(List<SelectItem> listaForniture) {
		this.listaForniture = listaForniture;
	}

	public void setListaCausali(List<SelectItem> listaCausali) {
		this.listaCausali = listaCausali;
	}

	public List<SelectItem> getListaCausali() {
		return listaCausali;
	}
	
	public static Map<String, String> getCausali() {
		return causali;
	}

	public List<SelectItem> getListaTipoDichiarazione() {
		return listaTipoDichiarazione;
	}
	
}