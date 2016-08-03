package it.webred.ct.service.comma340.web.catasto;

import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.data.access.basic.catasto.*;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CatastoSuggestionBean extends CatastoBaseBean{

	/**
	 * Restituisce la lista di vie il cui nome contiene la stringa passata come parametro
	 * 
	 * @param viaObj Nome completo o parziale della via/strada/piazza/etc. 
	 * @return Lista di oggetti Sitidstr che soddisfano la ricerca
	 */
	public List<Sitidstr> getListaVie(Object viaObj){
		String msg = "suggestion.listaVie";
		String codNazionale = this.getCurrentEnte();
		String via = (String)viaObj;
		List<Sitidstr> result = new ArrayList<Sitidstr>();
		
		try{
			if(codNazionale != null && via != null && !codNazionale.trim().equalsIgnoreCase("NULL") && !via.trim().equalsIgnoreCase("NULL")){
				if (via.length() >= 3) {
					CatastoService cs = super.getCatastoService();
					RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
					this.fillEnte(ro);
					ro.setCodEnte(codNazionale);
					ro.setVia(via);
					result = cs.getListaVie(ro);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		
		return result;
		
	}
	
	/**
	 * Restituisce la lista di civici che si trovano nella via precedentemente selezionata.
	 * Se civicoObj à valorizzato estrae solo quelli che iniziano con la stringa passata a parametro.
	 * Se civicoObj NON à valorizzato restituisce l'intera lista di civici della via
	 * 
	 * @param civicoObj Stringa iniziale del numero civico
	 * @return Lista di oggetti Siticivi che soddisfano la ricerca
	 */
	public List<Siticivi> getListaIndirizzi(Object civicoObj){
		String msg = "suggestion.listaIndirizzi";
		String codNazionale = this.getCurrentEnte();
		List<Siticivi> result = new ArrayList<Siticivi>();
		
		try{
			String civico = (String)civicoObj;
			if(codNazionale != null && !codNazionale.trim().equalsIgnoreCase("NULL")){
				String pkIdStraStr = this.getRequest().getParameter("frmMain:idVia");
				
				if(pkIdStraStr != null && !(pkIdStraStr).trim().isEmpty()){
					BigDecimal pkIdStra = new BigDecimal(pkIdStraStr);
					RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
					this.fillEnte(ro);
					if(civico!=null && !civico.trim().equalsIgnoreCase("NULL")) {
						//Ricerca i civici che iniziano con i caratteri passati a parametro						
						ro.setCodEnte(codNazionale);
						ro.setPkIdStra(pkIdStra);
						ro.setCivico(civico);
					}else{						
						//Ricerca Tutti i civici della via
						ro.setCodEnte(codNazionale);
						ro.setPkIdStra(pkIdStra);
					}
					result = super.getCatastoService().getListaIndirizzi(ro);
				}else
					super.addErrorMessage(msg+".warn.novia","");
			}
			
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
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
					RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
					this.fillEnte(rs);
					rs.setCognome(cognome);
					result = cs.getListaSoggettiF(rs);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
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
					RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
					this.fillEnte(rs);
					rs.setDenom(denom);
					result = cs.getListaSoggettiG(rs);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
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
					RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
					this.fillEnte(rs);
					rs.setCodFis(cF);
					result = cs.getSoggettoByCF(rs);	
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
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
					RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
					this.fillEnte(rs);
					rs.setPartIva(pI);
					ConsSoggTab sogg = cs.getSoggettoByPIVA(rs);
					result.add(sogg);
				}else{
					super.addErrorMessage(msg+".warn","");
				}
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param catObj Stringa di ricerca che dovrebbe essere contenuta nella descrizione della categoria catastale
	 * @return Lista delle categorie catastali degli immobili che corrispondono al criterio di ricerca. 
	 * 		   L'intera lista, se il parametro non à stato specificato.
	 */
	public List<Sitideco> getListaCategorieImmobile(Object catObj){
		String codCat = (String)catObj;
		String msg = "suggestion.listaCategorieImmobile";
		List<Sitideco> result = new ArrayList<Sitideco>();
		
		try{
			super.getLogger().debug("Ricerca Categorie Catastali ["+codCat+"]");
			CatastoService cs = super.getCatastoService();
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			this.fillEnte(ro);
			if(codCat!=null && !codCat.trim().equalsIgnoreCase("NULL") && !codCat.trim().isEmpty()){
				ro.setCodCategoria(codCat);				
			}
			result = cs.getListaCategorieImmobile(ro);
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		
		return result;
	}
	
}
