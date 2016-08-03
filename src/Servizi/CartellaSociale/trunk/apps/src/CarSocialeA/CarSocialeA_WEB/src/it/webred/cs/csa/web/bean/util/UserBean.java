package it.webred.cs.csa.web.bean.util;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.ejb.perm.LoginBeanService;
import it.webred.amprofiler.ejb.user.UserService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.amprofiler.model.AmUser;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.cs.csa.web.manbean.report.dto.HeaderPdfDTO;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsRelSettoreCatsoc;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.dto.utility.KeyValuePairBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean
@SessionScoped
public class UserBean extends CsUiCompBaseBean {
	
	private String username;
	private String denominazione;
	private String ente;
	
	private Long idOrganizzazione;
	private Long idSettore;
	private List<SelectItem> listaOrganizzazioni;
	private List<SelectItem> listaSettori;
	private List<CsOOperatoreSettore> listaCsOOperatoreSettore;
	private List<KeyValuePairBean<String, Boolean>> listaInfoSettore;
	private Map<Long, List<SelectItem>> mappaOrgSettori;

	private AccessTableOperatoreSessionBeanRemote opService = (AccessTableOperatoreSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableOperatoreSessionBean");
	
	private AnagraficaService anagraficaService = (AnagraficaService) getEjb("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
	
	private UserService userService = (UserService) getEjb("AmProfiler", "AmProfilerEjb", "UserServiceBean");
	
	private LoginBeanService loginService = (LoginBeanService) getEjb("AmProfiler", "AmProfilerEjb", "LoginBean");
	
	private ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	@PostConstruct
	public void init() {
		caricaSettori();
	}
	
	public String getUsername() {
		
		if (getRequest().getUserPrincipal()==null || getRequest().getUserPrincipal().getName()==null)
			return "";
		
		username = getRequest().getUserPrincipal().getName();
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEnte() {
		
		String es = getRequest().getParameter("es");
		if(es != null && !es.equals("") && getUser() != null){
			AmComune am = comuneService.getComune(getUser().getCurrentEnte());
			ente = am != null? am.getDescrizione(): "";
			ente = ente.substring(0,1).toUpperCase() + ente.substring(1).toLowerCase();
		}
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}

	public List<SelectItem> getListaSettori() {
		
		//inizializzo settori e gruppi
		if(listaSettori == null)
			caricaSettori();

		return listaSettori;
	}
	
	public List<SelectItem> getListaOrganizzazioni() {
		
		//inizializzo settori e gruppi
		if(listaOrganizzazioni == null)
			caricaSettori();

		return listaOrganizzazioni;
	}
	
	public void caricaSettori() {
		
		try {
			String username = getUsername();
			OperatoreDTO opDto = new OperatoreDTO();
			fillEnte(opDto);
			opDto.setUsername(username);
			CsOOperatore operatore = opService.findOperatoreByUsername(opDto);
			if(operatore != null){
				
				//carico lista organizzazioni e settori dell'utente
				listaSettori = new ArrayList<SelectItem>();
				listaOrganizzazioni = new ArrayList<SelectItem>();
				mappaOrgSettori = new HashMap<Long, List<SelectItem>>();
				Map<Long, CsOOrganizzazione> mappaOrg = new HashMap<Long, CsOOrganizzazione>();
				listaCsOOperatoreSettore = new ArrayList<CsOOperatoreSettore>(operatore.getCsOOperatoreSettores());
				if(listaCsOOperatoreSettore.size() > 0) {
					List<Long> tmpSett = new ArrayList<Long>();
					for(CsOOperatoreSettore os: listaCsOOperatoreSettore){
						CsOSettore settore = os.getCsOSettore();
						List<SelectItem> listaSett;
						
						if(mappaOrgSettori.containsKey(settore.getCsOOrganizzazione().getId()))
							listaSett = mappaOrgSettori.get(settore.getCsOOrganizzazione().getId());
						else {
							listaSett = new ArrayList<SelectItem>();
							mappaOrg.put(settore.getCsOOrganizzazione().getId(), settore.getCsOOrganizzazione());
						}
						
						if(!tmpSett.contains(settore.getId())){
							listaSett.add(new SelectItem(settore.getId(), settore.getNome()));
							tmpSett.add(settore.getId());
						}
						mappaOrgSettori.put(settore.getCsOOrganizzazione().getId(), listaSett);
					}
					
					idOrganizzazione = null;
					for(CsOOrganizzazione org: mappaOrg.values()) {
						listaOrganizzazioni.add(new SelectItem(org.getId(), org.getNome()));
						if(org.getBelfiore() != null && org.getBelfiore().equals(opDto.getEnteId())) 
							idOrganizzazione = org.getId();
					}
					if(idOrganizzazione == null) 
						idOrganizzazione = (Long) listaOrganizzazioni.get(0).getValue();
					
					listaSettori = mappaOrgSettori.get(idOrganizzazione);
				
					// carico permessi
					if(!listaSettori.isEmpty())
						idSettore = (Long) listaSettori.get(0).getValue();
					for(CsOOperatoreSettore opsettore: listaCsOOperatoreSettore){
						if(idSettore.equals(opsettore.getCsOSettore().getId())){
							getSession().setAttribute("operatoresettore", opsettore);
							setPermessiFromGruppo(opsettore);
							break;
						}
					}
				}
				
				
				/* 
				boolean trovato = false;
				int os=0;
				while(!trovato && os<listaCsOOperatoreSettore.size()) {
					CsOOperatoreSettore opSettore = listaCsOOperatoreSettore.get(os);
					if(opSettore.getCsOSettore().getCsOOrganizzazione().getId()==idOrganizzazione){
						getSession().setAttribute("operatoresettore", opSettore);
						setPermessiFromGruppo(opSettore);
					}
					os++;
				}*/
				
				AmUser amUser = userService.getUserByName(username);
				if(amUser.getAmGroups() != null)
					getSession().setAttribute("gruppi", amUser.getAmGroups());
			}
		} catch(Exception e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public void setListaSettori(List<SelectItem> listaSettori) {
		this.listaSettori = listaSettori;
	}
	
	public void handleChangeSettore(AjaxBehaviorEvent event) {
			
		Long id = (Long) ((javax.faces.component.UIInput)event.getComponent()).getValue();
		for(CsOOperatoreSettore opsettore: listaCsOOperatoreSettore){
			if(id.equals(opsettore.getCsOSettore().getId())){
				idSettore = id;
				getSession().setAttribute("operatoresettore", opsettore);
				setPermessiFromGruppo(opsettore);
				break;
			}
		}
		
		try {
			getSession().setAttribute("navigationHistory", "");
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento alla Homepage");
		}
	}
	
	public void handleChangeOrganizzazione(AjaxBehaviorEvent event) {
		
		Long id = (Long) ((javax.faces.component.UIInput)event.getComponent()).getValue();
		listaSettori = mappaOrgSettori.get(id);
		if(!listaSettori.isEmpty())
			idSettore = (Long) listaSettori.get(0).getValue();
		for(CsOOperatoreSettore opsettore: listaCsOOperatoreSettore){
			if(idSettore.equals(opsettore.getCsOSettore().getId())){
				getSession().setAttribute("operatoresettore", opsettore);
				setPermessiFromGruppo(opsettore);
				break;
			}
		}
		
		try {
			getSession().setAttribute("navigationHistory", "");
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento alla Homepage");
		}
	}
	
	private void setPermessiFromGruppo(CsOOperatoreSettore opsettore) {
		
		//listaInfoSettore descrive i permessi e categorie soc derivanti dal settore scelto
		listaInfoSettore = new ArrayList<KeyValuePairBean<String, Boolean>>();
		if(opsettore.getCsOSettore().getCsRelSettoreCatsocs() != null &&
				!opsettore.getCsOSettore().getCsRelSettoreCatsocs().isEmpty()) {
			listaInfoSettore.add(new KeyValuePairBean<String, Boolean>("CATEGORIE SOCIALI", true));
			for(CsRelSettoreCatsoc settCatsoc: opsettore.getCsOSettore().getCsRelSettoreCatsocs()) {
				listaInfoSettore.add(new KeyValuePairBean<String, Boolean>(settCatsoc.getCsCCategoriaSociale().getTooltip(), false));
			}
		}
		
		String gruppi = opsettore.getAmGroup();
		if(gruppi != null) {
			String[] arrGruppi = gruppi.split("\\|");
			List<String> listaGruppi = new ArrayList<String>(Arrays.asList(arrGruppi));
			HashMap<String, String> permessiGruppoSettore = new HashMap<String, String>();

			for(String gruppo: listaGruppi) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> permessi = loginService.getPermissionsByGroup(gruppo, getUser().getCurrentEnte());
				permessiGruppoSettore.putAll(permessi);
				
				listaInfoSettore.add(new KeyValuePairBean<String, Boolean>("Permessi Gruppo: " + gruppo, true));
				Map<String, String> permessiTreeMap = new TreeMap<String, String>(permessi);
				for(String permesso: permessiTreeMap.values()) {
					int idx = permesso.lastIndexOf("@-@");
					listaInfoSettore.add(new KeyValuePairBean<String, Boolean>(permesso.substring(idx+3), false));
				}
			}
			getSession().setAttribute("permessiGruppoSettore", permessiGruppoSettore);

		} else getSession().setAttribute("permessiGruppoSettore", null);
	}

	public Long getIdSettore() {
		return idSettore;
	}

	public void setIdSettore(Long idSettore) {
		this.idSettore = idSettore;
	}

	public Long getIdOrganizzazione() {
		return idOrganizzazione;
	}

	public void setIdOrganizzazione(Long idOrganizzazione) {
		this.idOrganizzazione = idOrganizzazione;
	}

	public String getDenominazione() {
		if(denominazione == null) {
			String username = getUsername();
			AmAnagrafica amAna = anagraficaService.findAnagraficaByUserName(username);
			denominazione = amAna.getCognome() + " " + amAna.getNome();
		}
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public List<KeyValuePairBean<String, Boolean>> getListaInfoSettore() {
		return listaInfoSettore;
	}

	public void setListaInfoSettore(List<KeyValuePairBean<String, Boolean>> listaInfoSettore) {
		this.listaInfoSettore = listaInfoSettore;
	}

	

	
	
	
}
