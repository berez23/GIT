package it.webred.ct.service.comma340.web.pratica;

import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.service.comma340.data.access.C340GestionePraticheService;
import it.webred.ct.service.comma340.data.access.dto.C340PratDepositoPlanimDTO;
import it.webred.ct.service.comma340.data.access.dto.C340PratRettificaSupDTO;
import it.webred.ct.service.comma340.data.access.dto.DettaglioC340ImmobileDTO;
import it.webred.ct.service.comma340.data.access.dto.ModuloPraticaDTO;
import it.webred.ct.service.comma340.data.model.ente.CodiceBelfiore;
import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;
import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;
import it.webred.ct.service.comma340.data.model.pratica.C340PratRettificaSup;
import it.webred.ct.service.comma340.data.model.pratica.C340Pratica;
import it.webred.ct.service.comma340.web.Comma340BaseBean;
import it.webred.ct.service.comma340.web.common.Comma340DettaglioBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.ajax4jsf.component.html.HtmlAjaxSupport;

public class GestionePraticheBean extends GestionePraticheBaseBean {
	
	public final static String MODE_INSERIMENTO = "I";
	public final static String MODE_MODIFICA = "M";
	public final static String DESC_MODE_INSERIMENTO = "Inserimento";
	public final static String DESC_MODE_MODIFICA = "Modifica";
	
	public final static String TIPO_PLANIMETRIA = "P";
	public final static String TIPO_SUPERFICIE = "S";
	public final static String DESC_TIPO_PLANIMETRIA = "Pratica per deposito della planimetria catastale";
	public final static String DESC_TIPO_SUPERFICIE = "Pratica di istanza di rettifica della superficie";	
	
	public final static String INTESTATARIO = "INTESTATARIO";
	public final static String PROPRIETARIO = "PROPRIETARIO";
	public final static String ALTRO = "ALTRO";
	
	List<VisPraticaLista> listaPratiche;
	C340PratDepositoPlanimDTO praticaPlanimetria;
	C340PratRettificaSupDTO praticaSuperficie;
	
	String mode;
	String tipo;
	BigDecimal selIdUiu;

	String titoloPersonaP;
	String altroTitoloPersonaP;
	String titoloPersonaS;
	String altroTitoloPersonaS;
	
	DettaglioC340ImmobileDTO dettImmobile;
	
	boolean visPnlDatiPratica;
	boolean visBtnNuoPraPla;
	boolean visBtnNuoPraSup;
	boolean visPnlDatiAggP;
	boolean visPnlDatiAggS;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTitoloPersonaP() {
		if(titoloPersonaP == null){
			String titolo = praticaPlanimetria.getPratica().getTitoloDichiarante();
			titoloPersonaP = "";
			if(titolo!= null){
				if(titolo.equalsIgnoreCase(this.INTESTATARIO)||titolo.equalsIgnoreCase(this.PROPRIETARIO)){
					titoloPersonaP = titolo;
				}else{
					titoloPersonaP = ALTRO;
				}
			}
		}
		return titoloPersonaP;
	}

	public String getTitoloPersonaS() {
		if(titoloPersonaS == null){
			String titolo = praticaSuperficie.getPratica().getTitoloDichiarante();
			titoloPersonaS = "";
			if(titolo!= null){
				if(titolo.equalsIgnoreCase(this.INTESTATARIO)||titolo.equalsIgnoreCase(this.PROPRIETARIO)){
					titoloPersonaS = titolo;
				}else{
					titoloPersonaS = ALTRO;
				}
			}
		}
		return titoloPersonaS;
	}
	
	public String getAltroTitoloPersonaP() {
		if(altroTitoloPersonaP == null){
			String titolo = praticaPlanimetria.getPratica().getTitoloDichiarante();
			altroTitoloPersonaP = "";
			if(titolo!= null){
				if(!titolo.equalsIgnoreCase(this.INTESTATARIO) && !titolo.equalsIgnoreCase(this.PROPRIETARIO)){
					altroTitoloPersonaP = titolo;
				}
			}
		}
		return altroTitoloPersonaP;
	}
	
	public String getAltroTitoloPersonaS() {
		if(altroTitoloPersonaS == null){
			String titolo = praticaSuperficie.getPratica().getTitoloDichiarante();
			altroTitoloPersonaS = "";
			if(titolo!= null){
				if(!titolo.equalsIgnoreCase(this.INTESTATARIO) && !titolo.equalsIgnoreCase(this.PROPRIETARIO)){
					altroTitoloPersonaS = titolo;
				}
			}
		}
		return altroTitoloPersonaS;
	}
	
	public void setTitoloPersonaP(String titoloPersona) {
		this.titoloPersonaP = titoloPersona;
		if(!titoloPersonaP.equalsIgnoreCase(this.ALTRO)){
			praticaPlanimetria.getPratica().setTitoloDichiarante(titoloPersonaP);
		}
	}

	public void setTitoloPersonaS(String titoloPersona) {
		this.titoloPersonaS = titoloPersona;
		if(!titoloPersonaS.equalsIgnoreCase(this.ALTRO)){
			praticaSuperficie.getPratica().setTitoloDichiarante(titoloPersonaS);
		}
	}
	
	public void setAltroTitoloPersonaP(String altroTitoloPersona) {
		this.altroTitoloPersonaP = altroTitoloPersona;
		if(titoloPersonaP.equalsIgnoreCase(this.ALTRO)){
			if(altroTitoloPersonaP!=null && !altroTitoloPersonaP.equalsIgnoreCase(""))
				praticaPlanimetria.getPratica().setTitoloDichiarante(altroTitoloPersonaP);
			else
				praticaPlanimetria.getPratica().setTitoloDichiarante(titoloPersonaP);
		}
	}

	public void setAltroTitoloPersonaS(String altroTitoloPersona) {
		this.altroTitoloPersonaS = altroTitoloPersona;
		if(titoloPersonaS.equalsIgnoreCase(this.ALTRO)){
			if(altroTitoloPersonaS!=null && !altroTitoloPersonaS.equalsIgnoreCase(""))
				praticaSuperficie.getPratica().setTitoloDichiarante(altroTitoloPersonaS);
			else
				praticaSuperficie.getPratica().setTitoloDichiarante(titoloPersonaS);
		}
	}

	public BigDecimal getSelIdUiu() {
		return selIdUiu;
	}

	public void setSelIdUiu(BigDecimal selIdUiu) {
		this.selIdUiu = selIdUiu;
	}

	public DettaglioC340ImmobileDTO getDettImmobile() {
		return dettImmobile;
	}

	public void setDettImmobile(DettaglioC340ImmobileDTO dettImmobile) {
		this.dettImmobile = dettImmobile;
	}

	public boolean isVisPnlDatiPratica() {
		return visPnlDatiPratica;
	}

	public void setVisPnlDatiPratica(boolean visPnlDatiPratica) {
		this.visPnlDatiPratica = visPnlDatiPratica;
	}

	public boolean isVisBtnNuoPraPla() {
		return visBtnNuoPraPla;
	}

	public void setVisBtnNuoPraPla(boolean visBtnNuoPraPla) {
		this.visBtnNuoPraPla = visBtnNuoPraPla;
	}

	public boolean isVisBtnNuoPraSup() {
		return visBtnNuoPraSup;
	}

	public void setVisBtnNuoPraSup(boolean visBtnNuoPraSup) {
		this.visBtnNuoPraSup = visBtnNuoPraSup;
	}

	public boolean isVisPnlDatiAggP() {
		return visPnlDatiAggP;
	}

	public void setVisPnlDatiAggP(boolean visPnlDatiAggP) {
		this.visPnlDatiAggP = visPnlDatiAggP;
	}

	public boolean isVisPnlDatiAggS() {
		return visPnlDatiAggS;
	}

	public void setVisPnlDatiAggS(boolean visPnlDatiAggS) {
		this.visPnlDatiAggS = visPnlDatiAggS;
	}

	public C340PratDepositoPlanimDTO getPraticaPlanimetria() {
		return praticaPlanimetria;
	}

	public void setPraticaPlanimetria(C340PratDepositoPlanimDTO praticaPlanimetria) {
		this.praticaPlanimetria = praticaPlanimetria;
	}

	public C340PratRettificaSupDTO getPraticaSuperficie() {
		return praticaSuperficie;
	}

	public void setPraticaSuperficie(C340PratRettificaSupDTO praticaSuperficie) {
		this.praticaSuperficie = praticaSuperficie;
	}

	public String apriGestionePratiche() {
		String esito = "gestionePraticheBean.apriGestionePratiche";
		mode = null;
		tipo = null;
		selIdUiu = null;
		dettImmobile = null;		
		fillPratiche();
		return esito;
	}
	
	public String nuovaPraticaPlanimetria() {
		String esito = "success";		
		mode = MODE_INSERIMENTO;
		tipo = TIPO_PLANIMETRIA;
		visPnlDatiPratica = true;
		visPnlDatiAggP = false;
		visPnlDatiAggS = false;
		inizializzaDatiPratica(praticaPlanimetria.getPratica());
		return esito;
	}
	
	public String nuovaPraticaSuperficie() {
		String esito = "success";		
		mode = MODE_INSERIMENTO;
		tipo = TIPO_SUPERFICIE;
		visPnlDatiPratica = true;
		visPnlDatiAggP = false;
		visPnlDatiAggS = false;
		inizializzaDatiPratica(praticaSuperficie.getPratica());
		return esito;
	}
	
	public String apriModificaPraticaPlanimetria() {
		String esito = "success";	
		String msg = "pratica.planimetria.modifica.apri";
		mode = MODE_MODIFICA;
		tipo = TIPO_PLANIMETRIA;
		visPnlDatiPratica = true;	
		
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			praticaPlanimetria = praSer.getPraticaPlanimetria(selIdUiu);	
			showDatiAggP(this.getTitoloPersonaP(), false);
			//showDatiAggP(praticaPlanimetria.getPratica().getTitoloDichiarante(), false);
			//super.addInfoMessage(msg);
			
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		return esito;
	}
	
	public String apriModificaPraticaSuperficie() {
		String esito = "success";		
		String msg = "pratica.superficie.modifica.apri";
		mode = MODE_MODIFICA;
		tipo = TIPO_SUPERFICIE;
		visPnlDatiPratica = true;
		
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			praticaSuperficie = praSer.getPraticaSuperficie(selIdUiu);		
			showDatiAggS(this.getTitoloPersonaS(), false);
			//showDatiAggS(praticaSuperficie.getPratica().getTitoloDichiarante(), false);
			//super.addInfoMessage(msg);
			
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		return esito;
	}
	
	private void inserisciPraticaPlanimetria() {
		String msg = "pratica.planimetria.crea";
		
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			praSer.creaPraticaDepositoPlanimetria(praticaPlanimetria);
			super.addInfoMessage(msg);
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	private void inserisciPraticaSuperficie() {
		String msg = "pratica.superficie.crea";
		
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			praSer.creaPraticaRettificaSuperficie(praticaSuperficie);
			super.addInfoMessage(msg);
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private void modificaPraticaPlanimetria() {
		String msg = "pratica.planimetria.modifica";
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			praSer.modificaPraticaDepositoPlanimetria(praticaPlanimetria);
			super.addInfoMessage(msg);
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private void modificaPraticaSuperficie() {
		String msg = "pratica.superficie.modifica";
		
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			praSer.modificaPraticaRettificaSuperficie(praticaSuperficie);
			super.addInfoMessage(msg);
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	public String cancellaPraticaPlanimetria() {
		String esito = "success";	
		String msg = "pratica.planimetria.cancella";	
		boolean cancellaPratica;
		
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			
			//Cancella gli allegati
			List<C340PratAllegato> listaAllegati = praSer.getListaAllegatiPratica(praticaPlanimetria.getPratica().getId());
			GestioneAllegatiBean bean = new GestioneAllegatiBean();
			if(listaAllegati.size()> 0)
					cancellaPratica = bean.cancellaListaAllegati(listaAllegati, praSer);
				else
					cancellaPratica = true;
				
			if(cancellaPratica){
				//Cancella la pratica
				praSer.cancellaPraticaDepositoPlanimetria(praticaPlanimetria.getPratica().getId());
				super.addInfoMessage(msg);
			}else
				super.addErrorMessage(msg+".error", "Allegati presenti, impossibile rimuovere la pratica.");
			
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		fillPratiche();
		return esito;
	}
	
	public String cancellaPraticaSuperficie() {
		String esito = "success";		
		String msg = "pratica.superficie.cancella";
		boolean cancellaPratica;
		
		try{
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			
			//Cancella gli allegati
			List<C340PratAllegato> listaAllegati = praSer.getListaAllegatiPratica(praticaSuperficie.getPratica().getId());
			GestioneAllegatiBean bean = new GestioneAllegatiBean();
			if(listaAllegati.size()> 0)
				cancellaPratica = bean.cancellaListaAllegati(listaAllegati, praSer);
			else
				cancellaPratica = true;
			
			if(cancellaPratica){
				//Cancella la pratica
				praSer.cancellaPraticaRettificaSuperficie(praticaSuperficie.getPratica().getId());
				super.addInfoMessage(msg);
			}else
				super.addErrorMessage(msg+".error", "Allegati presenti, impossibile rimuovere la pratica.");
		
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		
		fillPratiche();
		return esito;
	}
		
	public String salva() {
		String esito = "success";		
		System.out.println(praticaPlanimetria.getPratica().getComuneEnte()+" "+praticaPlanimetria.getDescComuneSedeEnte());
		if (mode.equalsIgnoreCase(MODE_INSERIMENTO)) {
			if (tipo.equalsIgnoreCase(TIPO_PLANIMETRIA)) {
				inserisciPraticaPlanimetria();
			} else if (tipo.equalsIgnoreCase(TIPO_SUPERFICIE)) {
				inserisciPraticaSuperficie();
			}
		} else if (mode.equalsIgnoreCase(MODE_MODIFICA)) {
			if (tipo.equalsIgnoreCase(TIPO_PLANIMETRIA)) {
				modificaPraticaPlanimetria();
			} else if (tipo.equalsIgnoreCase(TIPO_SUPERFICIE)) {
				modificaPraticaSuperficie();
			}
		}
		fillPratiche();
		return esito;
	}
	
	public String annulla() {
		String esito = "success";		
		fillPratiche();
		return esito;
	}
	
	
	
	public List<CodiceBelfiore> getListaComuni(Object descObj){
		String desc = (String)descObj;
		String msg = "pratica.suggestion.lista.comuni";
		List<CodiceBelfiore> result = new ArrayList<CodiceBelfiore>();
		
		try{
			if (desc != null && !desc.trim().equalsIgnoreCase("NULL")) {
				if(desc.length()>=2){
					C340GestionePraticheService cs = super.getC340GestionePraticheService();
					result = cs.getListaComuni(desc);
				}else{
					
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		return result;
	}
	
	
	
	public List<VisPraticaLista> getListaPratiche() {
		return listaPratiche;
	}

	public void setListaPratiche(List<VisPraticaLista> listaPratiche) {
		this.listaPratiche = listaPratiche;
	}

	private void fillPratiche() {
		String msg = "pratica.lista";
		
		try{
			visPnlDatiPratica = false;
			visBtnNuoPraPla = false;
			visBtnNuoPraSup = false;
			visPnlDatiAggP = false;
			visPnlDatiAggS = false;
			Comma340DettaglioBean dettaglio = (Comma340DettaglioBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comma340DettaglioBean");
			selIdUiu = new BigDecimal(dettaglio.getSelIdUiu());
			dettImmobile = dettaglio.getDettImmobile();
			listaPratiche = new ArrayList<VisPraticaLista>();
			praticaPlanimetria = null;
			praticaSuperficie = null;
			C340GestionePraticheService praSer = super.getC340GestionePraticheService();
			praticaPlanimetria = praSer.getPraticaPlanimetria(selIdUiu);
			if (praticaPlanimetria != null) {
				listaPratiche.add(getPraticaLista(praticaPlanimetria.getPratica().getId(), praticaPlanimetria, praticaPlanimetria.getPratica(), TIPO_PLANIMETRIA));
			} else {
				visBtnNuoPraPla = true;
				inizializzaPraticaPlanimetria();
			}
			praticaSuperficie = praSer.getPraticaSuperficie(selIdUiu);
			if (praticaSuperficie != null) {
				listaPratiche.add(getPraticaLista(praticaSuperficie.getPratica().getId(), praticaSuperficie, praticaSuperficie.getPratica(), TIPO_SUPERFICIE));
			} else {
				visBtnNuoPraSup = true;
				inizializzaPraticaSuperficie();
			}	
			//super.addInfoMessage(msg);
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private VisPraticaLista getPraticaLista(Long id, ModuloPraticaDTO modPra, C340Pratica pratica, String tipo) {
		VisPraticaLista praticaLista = new VisPraticaLista();

		praticaLista.setId(id.toString());
		praticaLista.setIdUiu(pratica.getIdUiu().toString());
		praticaLista.setTipo(tipo);
		String dichiarante = pratica.getCognomeDichiarante() == null ? "" : pratica.getCognomeDichiarante().trim();
		if (pratica.getNomeDichiarante() != null) {
			if (!dichiarante.equals("")) {
				dichiarante += " ";
			}
			dichiarante += pratica.getNomeDichiarante().trim();
		}
		praticaLista.setDichiarante(dichiarante);
		praticaLista.setCf(pratica.getCfDichiarante());
		String indDich = pratica.getViaDichiarante() == null ? "" : pratica.getViaDichiarante().trim();
		if (pratica.getCivicoDichiarante() != null) {
			if (!indDich.equals("")) {
				indDich += ", ";
			}
			indDich += pratica.getCivicoDichiarante().trim();
		}
		if (modPra.getDescComuneResidenzaDichiarante() != null) {
			if (!indDich.equals("")) {
				indDich += " - ";
			}
			indDich += modPra.getDescComuneResidenzaDichiarante().trim();
		}
		praticaLista.setIndDich(indDich);
		String indImm = pratica.getViaUiu() == null ? "" : pratica.getViaUiu().trim();
		if (pratica.getCivicoUiu() != null) {
			if (!indImm.equals("")) {
				indImm += ", ";
			}
			indImm += pratica.getCivicoUiu().trim();
		}
		if (modPra.getDescComuneImmmobile() != null) {
			if (!indImm.equals("")) {
				indImm += " - ";
			}
			indImm += modPra.getDescComuneImmmobile().trim();
		}
		praticaLista.setIndImm(indImm);
		String dtProt = pratica.getDataRichiesta() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(pratica.getDataRichiesta());
		praticaLista.setDtProt(dtProt);
		
		return praticaLista;
	}
	
	public String getTitoloPannello() {
		String titolo = "";
		
		if (mode != null && mode.equals(MODE_INSERIMENTO))
			titolo = DESC_MODE_INSERIMENTO;
		if (mode != null && mode.equals(MODE_MODIFICA))
			titolo = DESC_MODE_MODIFICA;
		if (tipo != null && tipo.equals(TIPO_PLANIMETRIA))
			titolo += " " + DESC_TIPO_PLANIMETRIA;
		if (tipo != null && tipo.equals(TIPO_SUPERFICIE))
			titolo += " " + DESC_TIPO_SUPERFICIE;
		
		return titolo;
	}
	
	public void clearComuneDichiarante(ValueChangeEvent event){
		String value = ((HtmlInputText)event.getSource()).getValue().toString();
		System.out.println("COMUNE DICHIARANTE ["+value+"]");
		if(value.isEmpty()){
			if(tipo.equals(TIPO_PLANIMETRIA)){
				praticaPlanimetria.getPratica().setComuneDichiarante("");
				praticaPlanimetria.setDescComuneResidenzaDichiarante("");
				System.out.println("COMUNE DICHIARANTE PRAT["+praticaPlanimetria.getPratica().getComuneDichiarante()+"]");
			}else{
				praticaSuperficie.getPratica().setComuneDichiarante("");
				praticaSuperficie.setDescComuneResidenzaDichiarante("");
			}
				
		}
			
	}
	
	public void clearComuneNCEU(ValueChangeEvent event){
		String value = ((HtmlInputText)event.getSource()).getValue().toString();
		if(value.isEmpty()){
			if(tipo.equals(TIPO_PLANIMETRIA)){
				praticaPlanimetria.getPratica().setComuneNCEU("");
				praticaPlanimetria.setDescComuneNCEU("");
			}else{
				praticaSuperficie.getPratica().setComuneNCEU("");
				praticaSuperficie.setDescComuneNCEU("");
			}	
		}	
	}
	
	public void clearComuneEnte(ValueChangeEvent event){
		String value = ((HtmlInputText)event.getSource()).getValue().toString();
		if(value.isEmpty()){
			if(tipo.equals(TIPO_PLANIMETRIA)){
				praticaPlanimetria.getPratica().setComuneEnte("");
				praticaPlanimetria.setDescComuneSedeEnte("");
			}else{
				praticaSuperficie.getPratica().setComuneEnte("");
				praticaSuperficie.setDescComuneSedeEnte("");
			}	
		}	
	}
	
	public void clearComuneImmobile(ValueChangeEvent event){
		String value = ((HtmlInputText)event.getSource()).getValue().toString();
		if(value.isEmpty()){
			if(tipo.equals(TIPO_PLANIMETRIA)){
				praticaPlanimetria.getPratica().setComuneUiu("");
				praticaPlanimetria.setDescComuneImmmobile("");
			}else{
				praticaSuperficie.getPratica().setComuneUiu("");
				praticaSuperficie.setDescComuneImmmobile("");
			}	
		}	
	}
	
	public void resetComuneEnte(ValueChangeEvent event){
		String value = ((HtmlSelectOneRadio)event.getSource()).getValue().toString();
		System.out.println("COMUNE ENTE["+value+"]");
		if(value!=null){
			if(!value.equalsIgnoreCase(ALTRO)){
				if(tipo.equals(TIPO_PLANIMETRIA)){
					praticaPlanimetria.getPratica().setComuneEnte("");
					praticaPlanimetria.setDescComuneSedeEnte("");
					System.out.println("COMUNE ENTE["+praticaPlanimetria.getPratica().getComuneEnte()+"]");
				}else{
					praticaSuperficie.getPratica().setComuneEnte("");
					praticaSuperficie.setDescComuneSedeEnte("");
				}	
			}
		}	
	}
	
	public void selQualitaP(ActionEvent event) {
		String strVal = ((HtmlSelectOneRadio)((HtmlAjaxSupport)event.getSource()).getParent()).getValue().toString();
		showDatiAggP(strVal, true);
	}
	
	private void showDatiAggP(String strVal, boolean cancellaDati) {
		if(strVal!=null){
			visPnlDatiAggP = strVal.equalsIgnoreCase(ALTRO);
			if (cancellaDati) {
				C340PratDepositoPlanim pratica = praticaPlanimetria.getPratica();
				this.setAltroTitoloPersonaP(null);
				pratica.setDenomEnte(null);
				pratica.setPivaEnte(null);
				pratica.setViaEnte(null);
				pratica.setCivicoEnte(null);
				pratica.setComuneEnte("");
				praticaPlanimetria.setDescComuneSedeEnte(null);
				pratica.setTelefonoEnte(null);
				pratica.setTitoloEnte(null);
				praticaPlanimetria.setPratica(pratica);
			}		
		}
	}
	
	public void selQualitaS(ActionEvent event) {
		String strVal = ((HtmlSelectOneRadio)((HtmlAjaxSupport)event.getSource()).getParent()).getValue().toString();
		showDatiAggS(strVal, true);
	}
	
	private void showDatiAggS(String strVal, boolean cancellaDati) {
		if(strVal!=null){
			visPnlDatiAggS = strVal.equalsIgnoreCase(ALTRO);
			if (cancellaDati) {
				C340PratRettificaSup pratica = praticaSuperficie.getPratica();
				this.setAltroTitoloPersonaS(null);
				pratica.setDenomEnte(null);
				pratica.setPivaEnte(null);
				pratica.setViaEnte(null);
				pratica.setCivicoEnte(null);
				pratica.setComuneEnte("");
				praticaSuperficie.setDescComuneSedeEnte(null);
				pratica.setTelefonoEnte(null);
				pratica.setTitoloEnte(null);
				praticaSuperficie.setPratica(pratica);
			}	
		}
	}
	
	private void inizializzaPraticaPlanimetria() {
		praticaPlanimetria = new C340PratDepositoPlanimDTO();
		praticaPlanimetria.setPratica(new C340PratDepositoPlanim());
		C340PratDepositoPlanim pratica = praticaPlanimetria.getPratica();
		this.setTitoloPersonaP("");
		this.setAltroTitoloPersonaP("");
		inizializzaDatiPratica(pratica);
	}
	
	private void inizializzaPraticaSuperficie() {
		praticaSuperficie = new C340PratRettificaSupDTO();
		praticaSuperficie.setPratica(new C340PratRettificaSup());
		C340PratRettificaSup pratica = praticaSuperficie.getPratica();
		this.setTitoloPersonaS("");
		this.setAltroTitoloPersonaS("");
		inizializzaDatiPratica(pratica);
	}
	
	private void inizializzaDatiPratica(C340Pratica pratica) {
		//comune presentazione pratica
		pratica.setComunePresPratica(getCodNazionaleFromUser());
		//dati immobile
		pratica.setIdUiu(selIdUiu);
		//TODO INDIRIZZO
		//TODO COMUNE
		pratica.setFoglio(new BigDecimal(dettImmobile.getCatasto().getImmobile().getId().getFoglio()));
		pratica.setParticella(new BigDecimal(dettImmobile.getCatasto().getImmobile().getId().getParticella()));
		pratica.setUnimm(new BigDecimal(dettImmobile.getCatasto().getImmobile().getId().getUnimm()));
		
		//dati proprietario
		List<SoggettoDTO> soggetti = dettImmobile.getCatasto().getSoggetti();
		SoggettoDTO soggetto = null;
		for (SoggettoDTO mySoggetto : soggetti) {
			if (mySoggetto.getTitolo().equalsIgnoreCase(PROPRIETARIO)) {
				soggetto = mySoggetto;
				break;
			}
		}
		if (soggetto != null) {
			pratica.setCognomeDichiarante(soggetto.getCognomeSoggetto());
			pratica.setNomeDichiarante(soggetto.getNomeSoggetto());
			pratica.setCfDichiarante(soggetto.getCfSoggetto());
			pratica.setTitoloDichiarante(PROPRIETARIO);
		}
	}
	
}
