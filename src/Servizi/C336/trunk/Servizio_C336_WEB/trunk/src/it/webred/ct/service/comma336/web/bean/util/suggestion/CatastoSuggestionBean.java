package it.webred.ct.service.comma336.web.bean.util.suggestion;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.service.comma336.web.Comma336BaseBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;


public class CatastoSuggestionBean extends Comma336BaseBean{
	
	private List<SelectItem> listaCategorie;
	
	private final List<SelectItem> listaTipoSoggetto = Arrays.asList(
			new SelectItem("P", "Soggetto Fisico"),
			new SelectItem("G", "Soggetto Giuridico"));
	
	
	public List<SelectItem> getListaTipoSoggetto() {
		return listaTipoSoggetto;
	}
	
	
	public List<SelectItem> getListaCategorie() {
		return listaCategorie;
	}


	public void setListaCategorie(List<SelectItem> listaCategorie) {
		this.listaCategorie = listaCategorie;
	}


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
				super.addErrorMessage("suggestion.catasto.categorie.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}

	}
	
	
	
	
	
	/**
	 * Restituisce la lista di vie il cui nome contiene la stringa passata come parametro
	 * 
	 * @param viaObj Nome completo o parziale della via/strada/piazza/etc. 
	 * @return Lista di oggetti Sitidstr che soddisfano la ricerca
	 */
	public List<Sitidstr> getListaVie(Object viaObj){
		String msg = "suggestion.catasto.listaVie";
		String codNazionale = this.getCurrentEnte();
		String via = (String)viaObj;
		List<Sitidstr> result = new ArrayList<Sitidstr>();
		
		try{
			if(codNazionale != null && via != null && !codNazionale.trim().equalsIgnoreCase("NULL") && !via.trim().equalsIgnoreCase("NULL")){
				if (via.length() >= 3) {
					CatastoService cs = getCatastoService();
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
	 * Se civicoObj è valorizzato estrae solo quelli che iniziano con la stringa passata a parametro.
	 * Se civicoObj NON è valorizzato restituisce l'intera lista di civici della via
	 * 
	 * @param civicoObj Stringa iniziale del numero civico
	 * @return Lista di oggetti Siticivi che soddisfano la ricerca
	 */
	public List<Siticivi> getListaIndirizzi(Object civicoObj){
		String msg = "suggestion.catasto.listaIndirizzi";
		String codNazionale = this.getCurrentEnte();
		List<Siticivi> result = new ArrayList<Siticivi>();
		
		try{
			String civico = (String)civicoObj;
			if(codNazionale != null && !codNazionale.trim().equalsIgnoreCase("NULL")){
				String pkIdStraStr = this.getRequest().getParameter("frmMain:idViaCatasto");
				//String pkIdStraStr = getModel().getCriteria().getCatOggetto().getIdVia();
				
				if(pkIdStraStr != null && pkIdStraStr.trim().length()> 0){
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
	public List<ConsSoggTab> getListaSoggettiF(Object cognomeNomeObj){	
		String msg = "suggestion.catasto.listaSoggettiF";
		String cognomeNome = (String)cognomeNomeObj;
		List<ConsSoggTab> result = new ArrayList<ConsSoggTab>();
		
		try{
		
			if (cognomeNome != null && !cognomeNome.trim().equalsIgnoreCase("NULL")){
				if(cognomeNome.length() >= 3) {
					CatastoService cs = super.getCatastoService();
					RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
					this.fillEnte(rs);
					rs.setCognome(cognomeNome);
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
		String msg = "suggestion.catasto.listaSoggettiG";
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
		String msg = "suggestion.catasto.listaSoggettiF.cf";
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
		String msg = "suggestion.catasto.listaSoggettiG.piva";
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
	 * 		   L'intera lista, se il parametro non è stato specificato.
	 */
	public List<Sitideco> getListaCategorieImmobile(Object catObj){
		String codCat = (String)catObj;
		String msg = "suggestion.catasto.listaCategorieImmobile";
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
