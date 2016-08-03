package it.webred.ct.service.spclass.web.bean;

import it.webred.ct.data.access.aggregator.elaborazioni.dto.ZonaDTO;
import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.docfa.dto.FoglioMicrozonaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.service.spclass.web.bean.util.UserBean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

public class ClassamentoUnoBean extends ClassamentoBean {

	private static final long serialVersionUID = 1L;
	
	private String flgAscensore;
	private boolean disabilitaAscensore = true;
	private boolean abilitaMappale = false;

	
	private List<SelectItem> listaMappali;


	public String init() {

		String esito = "classamentoUno";
		
		zonaDTO = new ZonaDTO();
		this.fillEnte(zonaDTO);
		
		//setto la lista delle categorie edilizie
		super.listaCategorieEdilizie = Arrays.asList(
				new SelectItem("1", "Abitazioni Civili (A1,A2,A7,A8)"), 
				new SelectItem("2", "Abitazioni Economiche (A3,A4)"));
		
		super.init();
				
		this.setFlgAscensore(common.getListaSiNo().get(0).getValue().toString());
		
		UserBean uBean = (UserBean) getBeanReference("userBean");
		if(uBean.getFoglioSpProf() != null)
			zonaDTO.setFoglio(uBean.getFoglioSpProf());
		else zonaDTO.setFoglio(this.getListaFogli().get(0).getValue().toString());
		zonaDTO.setTipoIntervento(common.getListaTipologiaIntervento().get(0).getValue().toString());
		zonaDTO.setCodiceCategoriaEdilizia(listaCategorieEdilizie.get(0).getValue().toString());
	
		getListaZC();
		getMicrozona();

		abilitaMappale = false;
	
		numberOfRows = 0;

		this.hideOutPanel();

		//resetForm();

		return esito;
	}

	public void resetForm() {

		// Parametri Form
		disabilitaAscensore = true;
		listaMappali = new ArrayList<SelectItem>();
		listaZC = new ArrayList<SelectItem>();

		// Parametri DTO
		zonaDTO.setFoglio("");
		zonaDTO.setZonaCensuaria("");
		this.setFlgAscensore(common.getListaSiNo().get(0).getValue().toString());
		zonaDTO.setCodiceCategoriaEdilizia(listaCategorieEdilizie.get(0).getValue().toString());
		zonaDTO.setTipoIntervento(common.getListaTipologiaIntervento().get(0).getValue().toString());
		zonaDTO.setMappale("");

		// Parametri Result
		zonaDTO.setNewMicrozona("");
		zonaDTO.setOldMicrozona("");

	}

	public void selectFoglio() {

		String tipoIntervento = zonaDTO.getTipoIntervento();

		getListaZC();
		getMicrozona();

		if (tipoIntervento != null && tipoIntervento.equals("2"))
			getListaMappali();

		this.hideOutPanel();
	}


// -----------------------------------------------------------------------------------------

	public void selectTipoIntervento() {

		String tipoIntervento = zonaDTO.getTipoIntervento();
		String catEdilizia = zonaDTO.getCodiceCategoriaEdilizia();

		if (tipoIntervento != null && tipoIntervento.equals("2")) {

			this.abilitaMappale = true;
			getListaMappali();

			// se abitazioni civili disabilito cbxAscensore
			if (catEdilizia != null && catEdilizia.equals("1"))
				this.disabilitaAscensore = true;
			else if (catEdilizia != null && catEdilizia.equals("2"))
				this.disabilitaAscensore = false;
		} else {
			this.abilitaMappale = false;
			listaMappali = new ArrayList<SelectItem>();
			zonaDTO.setMappale("");
		
			this.disabilitaAscensore = true;
		}

		this.hideOutPanel();

	}

	public void selectCategoriaEdilizia() {
		String catEdilizia = zonaDTO.getCodiceCategoriaEdilizia();
		String tipoIntervento = zonaDTO.getTipoIntervento();

		// se abitazioni civili disabilito cbxAscensore
		if (catEdilizia != null && catEdilizia.equals("1"))
			this.disabilitaAscensore = true;
		else if (catEdilizia != null && catEdilizia.equals("2")) {

			if (tipoIntervento != null && tipoIntervento.equals("2"))
				this.disabilitaAscensore = false;

			else
				this.disabilitaAscensore = true;
		}

		this.hideOutPanel();

	}

	public List<SelectItem> getListaMappali() {

		listaMappali = new ArrayList<SelectItem>();
		String foglio = zonaDTO.getFoglio();

		if (foglio != null && !foglio.equals("")) {
			
			CatastoSearchCriteria criteria = new CatastoSearchCriteria();
			criteria.setFoglio(foglio);
			criteria.setUiuAttuale(true);
			
			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			roc.setCriteria(criteria);
			this.fillEnte(roc);
			List<ParticellaKeyDTO> lista = catastoService.getListaParticelle(roc);

			if (lista != null) {
				for (ParticellaKeyDTO p : lista) {
					String particella = p.getParticella();
					SelectItem si = new SelectItem(particella, particella);
					listaMappali.add(si);
				}
			}
		}

		UserBean uBean = (UserBean) getBeanReference("userBean");
		if(uBean.getPartSpProf() != null)
			zonaDTO.setMappale(uBean.getPartSpProf());
		else if (listaMappali.size() > 0)
			zonaDTO.setMappale((String) listaMappali.get(0).getValue());
		
		return listaMappali;
	}

	public void calcola() {

		super.calcola();

		this.datiAttesiDTO = calcoloValoriAttesiService.getCalcolo1(zonaDTO);

		if (datiAttesiDTO.getClassiMaxCategoria() == null)
			this.addErrorMessage("msgNoUiCategorie", "");

		this.numberOfRows = datiAttesiDTO.getClassamenti().size();

	}

	public void validateConsistenza(FacesContext context,
			UIComponent component, Object numero) throws ValidatorException {
	/*	Double d = null;

		String numeroS = (String) numero;

		int i = numeroS.indexOf(",");

		if (numeroS.indexOf(",") > 0)
			numeroS = numeroS.replace(",", ".");

		// verifico se è un numero
		try {
			d = Double.valueOf(numeroS);
		} catch (NumberFormatException e) {
			this.addErrorMessage("msgValoreNumerico", e.toString());
		}

		if (d.doubleValue() <= 1) {

			Object[] params = new Object[1];
			params[0] = new Integer(1);
			this.addErrorMessage("msgValoreMaggioreDi", "");
		}*/

		if((Long)numero <=0.0){
			Object[] params = new Object[1];
			params[0] = new Integer(0);
			this.addErrorMessage("msgValoreMaggioreDi", "");
		}
	}

	public void validateSuperficie(FacesContext context, UIComponent component,
			Object numero) throws ValidatorException {
/*		Double d = null;

		String numeroS = (String) numero;

		int i = numeroS.indexOf(",");

		if (numeroS.indexOf(",") > 0)
			numeroS = numeroS.replace(",", ".");

		// verifico se è un numero
		try {
			d = Double.valueOf(numeroS);
		} catch (NumberFormatException e) {
			this.addErrorMessage("msgValoreNumerico", e.toString());
		}
		
		if (d.doubleValue() <= 0) {

			Object[] params = new Object[1];
			params[0] = new Integer(0);

			this.addErrorMessage("msgValoreMaggioreDi", "");
		}*/
		
		if((Long)numero <=0.0){
			Object[] params = new Object[1];
			params[0] = new Integer(0);
			this.addErrorMessage("msgValoreMaggioreDi", "");
		}
	}


	public void setListaMappali(List<SelectItem> listaMappali) {
		this.listaMappali = listaMappali;
	}

	public boolean isAbilitaMappale() {
		return abilitaMappale;
	}

	public void setAbilitaMappale(boolean abilitaMappale) {
		this.abilitaMappale = abilitaMappale;
	}

	public boolean isDisabilitaAscensore() {
		return disabilitaAscensore;
	}

	public void setDisabilitaAscensore(boolean disabilitaAscensore) {
		this.disabilitaAscensore = disabilitaAscensore;
	}

	public void setFlgAscensore(String flgAscensore) {
		this.flgAscensore = flgAscensore;
		zonaDTO.setFlgAscensore("SI".equalsIgnoreCase(flgAscensore)? true : false);
	}

	public String getFlgAscensore() {
		return flgAscensore;
	}

}
