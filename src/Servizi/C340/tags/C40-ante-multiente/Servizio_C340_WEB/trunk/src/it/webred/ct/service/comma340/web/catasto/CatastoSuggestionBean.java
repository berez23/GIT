package it.webred.ct.service.comma340.web.catasto;

import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.data.access.basic.catasto.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class CatastoSuggestionBean extends CatastoBaseBean{

	/**
	 * Restituisce la lista di vie il cui nome contiene la stringa passata come parametro
	 * 
	 * @param viaObj Nome completo o parziale della via/strada/piazza/etc. 
	 * @return Lista di oggetti Sitidstr che soddisfano la ricerca
	 */
	public List<Sitidstr> getListaVie(Object viaObj){
		String msg = "suggestion.listaVie";
		String codNazionale = getCodNazionaleFromUser();
		String via = (String)viaObj;
		List<Sitidstr> result = new ArrayList<Sitidstr>();
		
		try{
			if(codNazionale != null && via != null && !codNazionale.trim().equalsIgnoreCase("NULL") && !via.trim().equalsIgnoreCase("NULL")){
				if (via.length() >= 3) {
					CatastoService cs = super.getCatastoService();
					result = cs.getListaVie(codNazionale, via);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		
		return result;
		
	}
	
	/**
	 * Restituisce la lista di civici che si trovano nella via precedentemente selezionata.
	 * Se civicoObj è valorizzato estrae solo quelli che iniziano con la stringa passata a parametro.
	 * Se civicoObj NON è valorizzato restituisce l'intera lista di civici della via
	 * 
	 * @param civicoObj Stringa iniziale del numero civico
	 * @return Lista di oggetti Siticivi che soddisfano la ricerca
	 */
	public List<Siticivi> getListaIndirizzi(Object civicoObj){
		String msg = "suggestion.listaIndirizzi";
		String codNazionale = getCodNazionaleFromUser();
		List<Siticivi> result = new ArrayList<Siticivi>();
		
		try{
			String civico = (String)civicoObj;
			if(codNazionale != null && !codNazionale.trim().equalsIgnoreCase("NULL")){
				String pkIdStraStr = this.getRequest().getParameter("frmMain:idVia");
				
				if(pkIdStraStr != null && !(pkIdStraStr).trim().isEmpty()){
					BigDecimal pkIdStra = new BigDecimal(pkIdStraStr);
					if(civico!=null && !civico.trim().equalsIgnoreCase("NULL"))
						//Ricerca i civici che iniziano con i caratteri passati a parametro
						result = super.getCatastoService().getListaIndirizzi(codNazionale, pkIdStra, civico);
					else{
						
						//Ricerca Tutti i civici della via
						result = super.getCatastoService().getListaIndirizzi(codNazionale, pkIdStra);
					}
				}else
					super.addErrorMessage(msg+".warn.novia","");
			}
			
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Restituisce la lista dei soggetti fisici registrati a catasto il cui Cognome (e Nome) inizia con
	 * la stringa passata a parametro.
	 * 
	 * @param cognomeObj Parametro per la ricerca, costituito almeno dai primi tre caratteri del Cognome
	 * @return Lista degli oggetti ConsSoggTab (soggetti fisici registrati a catasto) che soddisfano i criteri di ricerca.
	 */
	public List<ConsSoggTab> getListaSoggettiF(Object cognomeObj){	
		String msg = "suggestion.listaSoggettiF";
		String cognome = (String)cognomeObj;
		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		
		try{
		
			if (cognome != null && !cognome.trim().equalsIgnoreCase("NULL")){
				if(cognome.length() >= 3) {
					CatastoService cs = super.getCatastoService();
					result = cs.getListaSoggettiF(cognome);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Restituisce la lista dei soggetti giuridici registrati a catasto la cui 
	 * Denominazione / Ragione Sociale inizia con la stringa passata a parametro.
	 * 
	 * @param denomObj Parametro per la ricerca, costituito almeno dai primi tre caratteri della Denominazione / Ragione Sociale 
	 * @return Lista degli oggetti ConsSoggTab (soggetti giuridici registrati a catasto) che soddisfano i criteri di ricerca.
	 */
	public List<ConsSoggTab> getListaSoggettiG(Object denomObj){	
		String msg = "suggestion.listaSoggettiG";
		String denom = (String)denomObj;
		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		
		try{
			if (denom != null && !denom.trim().equalsIgnoreCase("NULL")){
				if(denom.length() >= 3) {
					CatastoService cs = super.getCatastoService();
					result = cs.getListaSoggettiG(denom);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
	
		
		return result;
	}
	
	/**
	 * Restituisce la lista dei soggetti fisici registrati a catasto il cui 
	 * Cod.fiscale inizia con la stringa passata a parametro.
	 * 
	 * @param cFObj Parametro per la ricerca, costituito almeno dai primi tre caratteri del Codice Fiscale
	 * @return Lista degli oggetti ConsSoggTab (soggetti fisici registrati a catasto) che soddisfano i criteri di ricerca.
	 */
	public List<ConsSoggTab> getSoggettoByCF(Object cFObj){
		String cF = (String)cFObj;
		String msg = "suggestion.listaSoggettiF.cf";
		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		
		try{
			if (cF != null && !cF.trim().equalsIgnoreCase("NULL")){
				if (cF.length() >=3) {
					CatastoService cs = super.getCatastoService();
					//ConsSoggTab sogg = cs.getSoggettoByCF(cF);
					//result.add(sogg);
					result = cs.getSoggettoByCF(cF);	
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Restituisce la lista dei soggetti giuridici registrati a catasto la cui 
	 * Partita IVA corrisponde esattamente alla stringa passata a parametro.
	 * 
	 * @param denomObj Parametro per la ricerca, costituito dal numero completo della partita IVA
	 * @return Lista degli oggetti ConsSoggTab (soggetti giuridici registrati a catasto) che soddisfano i criteri di ricerca.
	 */
	public List<ConsSoggTab> getSoggettoByPIVA(Object pIObj){
		String pI = (String)pIObj;
		String msg = "suggestion.listaSoggettiG.piva";
		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		try{
			if (pI != null && !pI.trim().equalsIgnoreCase("NULL")){
				if(pI.length() == 11) {
					CatastoService cs = super.getCatastoService();
					ConsSoggTab sogg = cs.getSoggettoByPIVA(pI);
					result.add(sogg);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param catObj Stringa di ricerca che dovrebbe essere contenuta nella descrizione della categoria catastale
	 * @return Lista delle categorie catastali degli immobili che corrispondono al criterio di ricerca. 
	 * 		   L'intera lista, se il parametro non è stato specificato.
	 */
	public List<Sitideco> getListaCategorieImmobile(Object catObj){
		String codCat = (String)catObj;
		String msg = "suggestion.listaCategorieImmobile";
		List<Sitideco> result = new ArrayList<Sitideco>();
		
		try{
			super.getLogger().debug("Ricerca Categorie Catastali ["+codCat+"]");
			CatastoService cs = super.getCatastoService();
			if(codCat!=null && !codCat.trim().equalsIgnoreCase("NULL") && !codCat.trim().isEmpty()){
				
				result = cs.getListaCategorieImmobile(codCat);
				
			}else{
				result = cs.getListaCategorieImmobile();
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
		
		return result;
	}
	
}
