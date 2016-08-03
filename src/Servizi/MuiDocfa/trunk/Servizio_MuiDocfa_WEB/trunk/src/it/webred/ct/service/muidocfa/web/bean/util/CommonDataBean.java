package it.webred.ct.service.muidocfa.web.bean.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.diagnostiche.DocfaAnomalie;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

import org.apache.log4j.Logger;

public class CommonDataBean extends MuiDocfaBaseBean {

	private List<SelectItem> listaCategorie;

	private List<SelectItem> listaCausali;
	
	private final List<SelectItem> listaPlusMinus = Arrays.asList(new SelectItem("+", "SI:aumento"), new SelectItem("-", "SI:diminuzione"), new SelectItem("0", "NO"));

	private final List<SelectItem> listaAugDim = Arrays.asList(new SelectItem("+", "Aumento"), new SelectItem("-", "Diminuzione"), new SelectItem("0", "Nessuna"));

	private final List<SelectItem> listaSiNo = Arrays.asList(new SelectItem("1", "SI"), new SelectItem("0", "NO"));
	
	private final List<SelectItem> listaTarPost = Arrays.asList(new SelectItem("2", "SI,dopo scadenza Tarsu"),new SelectItem("1", "SI,ante scadenza Tarsu"), new SelectItem("0", "NO"));
	
	private final List<SelectItem> listaTipoSoggetto = Arrays.asList(new SelectItem("P", "Persona Fisica "),new SelectItem("G", "Persona Giuridica"));

	
	private static final Map<String, String> causali = new HashMap<String, String>() {
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
	
	public void doCaricaCategorie() {

		if (listaCategorie == null) {
			listaCategorie = new ArrayList<SelectItem>();
			try {

				RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
				fillEnte(ro);
				List<Sitideco> lista = catastoService.getListaCategorieImmobile(ro);

				for (Sitideco cat : lista) {
					SelectItem item = new SelectItem(cat.getId().getCode(), cat
							.getId().getCode()
							+ " - " + cat.getDescription());
					listaCategorie.add(item);
				}

			} catch (Throwable t) {
				super.addErrorMessage("categorie.error", t.getMessage());
				t.printStackTrace();
			}
		}

	}

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
				super.addErrorMessage("causali.error", t.getMessage());
				t.printStackTrace();
			}
		}

	}

	public List<SelectItem> getListaCategorie() {
		this.doCaricaCategorie();
		return listaCategorie;
	}

	public void setListaCategorie(List<SelectItem> listaCategorie) {
		this.listaCategorie = listaCategorie;
	}

	public List<SelectItem> getListaCausali() {
		this.doCaricaCausali();
		return listaCausali;
	}

	public void setListaCausali(List<SelectItem> listaCausali) {
		this.listaCausali = listaCausali;
	}

	public static Map<String, String> getCausali() {
		return causali;
	}

	public List<SelectItem> getListaSiNo() {
		return listaSiNo;
	}
	
	public List<SelectItem> getListaTarPost() {
		return listaTarPost;
	}

	public List<SelectItem> getListaPlusMinus() {
		return listaPlusMinus;
	}

	public List<SelectItem> getListaAugDim() {
		return listaAugDim;
	}

	public List<SelectItem> getListaTipoSoggetto() {
		return listaTipoSoggetto;
	}

}
