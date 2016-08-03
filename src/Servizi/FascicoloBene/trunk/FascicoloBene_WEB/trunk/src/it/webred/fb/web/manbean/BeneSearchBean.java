package it.webred.fb.web.manbean;

import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.BeneInListaDTO;
import it.webred.fb.ejb.dto.DatoSpec;
import it.webred.fb.ejb.dto.IndirizzoDTO;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.RicercaBeneDTO;
import it.webred.fb.web.bean.FascicoloBeneBaseBean;
import it.webred.fb.web.bean.util.NavigationBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.SelectableDataModel;

@ManagedBean
@SessionScoped
public class BeneSearchBean extends FascicoloBeneBaseBean implements SelectableDataModel{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	private String idBene;
	private Integer maxResult = 15;
	private RicercaBeneDTO searchDto;
	private List<BeneInListaDTO> listaBeni;
	private String activeIndex;
	
	private List<KeyValueDTO> lstCivici;
	
	public BeneSearchBean(){
		initRicerca();
	}
	
	public void initRicerca(){
		searchDto = new RicercaBeneDTO();
		searchDto.setRicercaInventario(true);
		lstCivici = new ArrayList<KeyValueDTO>();
		activeIndex=null;
		idBene=null;
	}
	
	public void onViaSelect(SelectEvent event) {
	        this.caricaListaCivici(null);
	    }
	
	@SuppressWarnings("static-access")
	public List<KeyValueDTO> caricaListaCivici(String query){
		lstCivici = new ArrayList<KeyValueDTO>();
		RicercaBeneDTO b = new RicercaBeneDTO();
		this.fillUserData(b);
		
		try {
			if(searchDto.getVia().getCodice()!=null){
				b.getVia().setCodice(searchDto.getVia().getCodice());
				b.getCivico().setCodice(query);
			    
				DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
						ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
				
				List<String> list = beneService.getListaCivici(b);
				
				for(String i: list){
					KeyValueDTO si = new KeyValueDTO();			
					si.setCodice(i);
					si.setDescrizione(i);
					lstCivici.add(si);
				}
			}
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
		ordinaListaCivici(lstCivici);
		return lstCivici;
	}
	
	private void ordinaListaCivici(List<KeyValueDTO> lst) {
		if (lst == null || lst.size() == 0) {
			return;
		}
		Comparator<KeyValueDTO> comp = new Comparator<KeyValueDTO>() {
			public int compare(KeyValueDTO o1, KeyValueDTO o2) {
				return compCivici.compare(o1.getDescrizione(), o2.getDescrizione());				
			}
		};		
		Collections.sort(lst, comp);
	}
	
	public List<SelectItem> getLstTipo(){
		List<SelectItem> lst = new ArrayList<SelectItem>();
		
		BaseDTO b = new BaseDTO();
		fillUserData(b);
		try {
			
			DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			List<KeyValueDTO> list = beneService.getListaTipo(b);
			for(KeyValueDTO i: list){
				SelectItem si = new SelectItem();			
				si.setLabel(i.getDescrizione());
				si.setValue(i.getCodice());
				lst.add(si);
			}
			
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
		return lst;
	}
	
	public List<SelectItem> getLstTipoDirittoReale(){
		List<SelectItem> lst = new ArrayList<SelectItem>();
		
		BaseDTO b = new BaseDTO();
		fillUserData(b);
		try {
			
			DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			List<String> list = beneService.getListaTipoDirittoReale(b);
			for(String i: list){
				SelectItem si = new SelectItem();			
				si.setLabel(i);
				si.setValue(i);
				lst.add(si);
			}
			
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
		return lst;
	}
	
	public List<KeyValueDTO> getLstComuni(String query){
		List<KeyValueDTO> list = new ArrayList<KeyValueDTO>();
		
		RicercaBeneDTO rsDto = new RicercaBeneDTO();
		fillUserData(rsDto);
		rsDto.setRicercaIndirizzo(searchDto.isRicercaIndirizzo());
		rsDto.setRicercaCatasto(searchDto.isRicercaCatasto());
		rsDto.getComune().setDescrizione(query);
		rsDto.setMaxResult(maxResult);
			try {
				
				DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
						ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
				
				list = beneService.getListaComuni(rsDto);
				
			} catch (NamingException e) {
				addError("general", "caricamento.error");
				logger.error(e.getMessage(), e);
			}
		return list;
	}
	
	private List<SelectItem> elabora(List<KeyValueDTO> dto){
		List<SelectItem> lst = new ArrayList<SelectItem>();
		
		for(KeyValueDTO i: dto){
			SelectItem si = new SelectItem();			
			si.setLabel(i.getDescrizione());
			si.setValue(i.getCodice());
			lst.add(si);
		}
		
		return lst;
	}
	
	
	public List<SelectItem> getLstCatInventario(){
		List<SelectItem> lst = new ArrayList<SelectItem>();
		
		BaseDTO b = new BaseDTO();
		fillUserData(b);
		try {
			
			DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			List<KeyValueDTO> list = beneService.getListaCatInventario(b);
			lst = this.elabora(list);
			
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
		return lst;
	}
	
	public List<KeyValueDTO> getLstFascicoli(String query){
		List<KeyValueDTO> listFasc = new ArrayList<KeyValueDTO>();
		
		RicercaBeneDTO rsDto = new RicercaBeneDTO();
		fillUserData(rsDto);
		rsDto.setDesFascicolo(query);
		rsDto.setMaxResult(maxResult);
		try {
			
			DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			listFasc = beneService.getLstFascicoli(rsDto);
			
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
		return listFasc;
	}//-------------------------------------------------------------------------
	
	public void onFascicoloSelect(SelectEvent event) {
		String codFas = (String)event.getObject();
		if (codFas != null){

			List<KeyValueDTO> listFasc = new ArrayList<KeyValueDTO>();
			
			RicercaBeneDTO rsDto = new RicercaBeneDTO();
			fillUserData(rsDto);
			rsDto.setCodFascicolo(codFas);
			rsDto.setDesFascicolo("");
			rsDto.setMaxResult(1);
			try {
				
				DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
						ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
				
				listFasc = beneService.getLstFascicoli(rsDto);
				if (listFasc != null && listFasc.size()>0){
					KeyValueDTO kv = listFasc.get(0);
					searchDto.setCodFascicolo(kv.getCodice());
					searchDto.setDesFascicolo(kv.getDescrizione());
				}
				
			} catch (NamingException e) {
				addError("general", "caricamento.error");
				logger.error(e.getMessage(), e);
			}
			
		}

	}//-------------------------------------------------------------------------
	
	public List<KeyValueDTO> getLstIndirizzi(String query) {
		
		List<KeyValueDTO> lstIndirizzi = new ArrayList<KeyValueDTO>();
		RicercaBeneDTO rsDto = new RicercaBeneDTO();
		fillUserData(rsDto);
		KeyValueDTO comune = searchDto.getComuneInd();
		if(comune!=null && comune.getCodice()!=null)
			rsDto.getComuneInd().setCodice(comune.getCodice());
		rsDto.getVia().setDescrizione(query);
		rsDto.setMaxResult(maxResult);
		try {
			
			DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			lstIndirizzi = beneService.getListaVieByDesc(rsDto);
			//listAutocomplete = this.elabora(list);
			
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
		return lstIndirizzi;
	}
	
	@SuppressWarnings("static-access")
	public void searchListaBeni(){
		listaBeni=null;
		try {
			if(!searchDto.isEmpty()){
				listaBeni = new ArrayList<BeneInListaDTO>();
				DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
						ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
				
				this.fillUserData(this.searchDto);
				listaBeni = beneService.searchBene(this.searchDto);
				if(this.searchDto.isRicercaIndirizzo()) {
					ordinaIndirizziListaBeni(listaBeni);
					ordinaListaBeniPerIndirizzo(listaBeni);			
				}
			}else{
				if(this.searchDto.isRicercaCatasto())
					addWarning("Ricerca interrotta","Impostare foglio");
				if(this.searchDto.isRicercaIndirizzo())
					addWarning("Ricerca interrotta","Impostare via e/o civico");
			}
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
	}
	
	private void ordinaIndirizziListaBeni(List<BeneInListaDTO> lst) {
		if (lst == null || lst.size() == 0) {
			return;
		}
		for (BeneInListaDTO bene: lst) {
			List<IndirizzoDTO> lstInd = bene.getIndirizzi();			
			Collections.sort(lstInd, compIndirizzi);
		}
	}
	
	private void ordinaListaBeniPerIndirizzo(List<BeneInListaDTO> lst) {
		if (lst == null || lst.size() == 0) {
			return;
		}
		Comparator<BeneInListaDTO> comp = new Comparator<BeneInListaDTO>() {
			public int compare(BeneInListaDTO o1, BeneInListaDTO o2) {
				if (o1 == null || o1.getIndirizzi() == null || o1.getIndirizzi().size() == 0) {
					if (o2 == null || o2.getIndirizzi() == null || o2.getIndirizzi().size() == 0)
						return 0;
					else
						return -1;
				}
				else if (o2 == null || o2.getIndirizzi() == null || o2.getIndirizzi().size() == 0) {
					return 1;
				}
				else {
					return compIndirizzi.compare(o1.getIndirizzi().get(0), o2.getIndirizzi().get(0));
				}
			}
		};
		Collections.sort(lst, comp);
	}

	public void tabChangeListener(TabChangeEvent event){
		this.searchDto = new RicercaBeneDTO();
		String title = event.getTab().getTitle();
		if(title.equals("Toponomastica"))
			searchDto.setRicercaIndirizzo(true);
		else if(title.equals("Catasto"))
			searchDto.setRicercaCatasto(true);
		else if(title.equals("Inventario"))
			searchDto.setRicercaInventario(true);	
		
		listaBeni = null;
	}	
	
	public void onRowSelect(SelectEvent event) {
        Long sel = ((BeneInListaDTO) event.getObject()).getBene().getId();
        selDettaglio(sel, false);        
    }
	
	@SuppressWarnings("unchecked")
	public void onRowSelectColl(SelectEvent event) {
		LinkedList<DatoSpec> dato = ((Map.Entry<String, LinkedList<DatoSpec>>)event.getObject()).getValue();
		String chiave = dato.get(0).getDato();
        selDettaglio(chiave, true);
    }
	
	@SuppressWarnings("static-access")
	private void selDettaglio(Object sel, boolean byChiave) {
		NavigationBean nb = new NavigationBean();
        try {
        DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
				ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
        BaseDTO b = new BaseDTO();
        b.setObj(sel);
        this.fillUserData(b);
        
        
		getSession().setAttribute("dettaglioBene", byChiave ? beneService.getBeneByChiave(b) : beneService.getDettaglioBeneById(b));
        
        //Passaggio dell'elemento selezionato
        nb.goDettaglioBene();
        
        } catch (NamingException e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
	}
	
	public void resetForm(){
		if(searchDto.isRicercaCatasto()){
			searchDto = new RicercaBeneDTO();
			searchDto.setRicercaCatasto(true);
		}else if(searchDto.isRicercaInventario()){
			searchDto = new RicercaBeneDTO();
			searchDto.setRicercaInventario(true);
		}else if(searchDto.isRicercaIndirizzo()){
			lstCivici = new ArrayList<KeyValueDTO>();
			searchDto = new RicercaBeneDTO();
			searchDto.setRicercaIndirizzo(true);
		}	
		searchDto.setCodFascicolo("");
		searchDto.setDesFascicolo("");
		
		listaBeni = null;
	}
	
	
	public void resetViaCivico(){
		searchDto.setTipoVia(null);
		searchDto.setVia(new KeyValueDTO());
		searchDto.setCivico(null);
	}
	

	public Integer getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}


	public RicercaBeneDTO getSearchDto() {
		return searchDto;
	}


	public void setSearchDto(RicercaBeneDTO searchDto) {
		this.searchDto = searchDto;
	}
	
	public List<BeneInListaDTO> getListaBeni() {
		return listaBeni;
	}

	public void setListaBeni(List<BeneInListaDTO> listaBeni) {
		this.listaBeni = listaBeni;
	}

	public List<KeyValueDTO> getLstCivici() {
		return lstCivici;
	}

	public void setLstCivici(List<KeyValueDTO> lstCivici) {
		this.lstCivici = lstCivici;
	}

	public String getIdBene() {
		return idBene;
	}
	
	public void setIdBene(String idBene) {
		this.idBene = idBene;
	}

	@Override
	public Object getRowData(String arg0) {
		return null;
	}

	@Override
	public Object getRowKey(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(String activeIndex) {
		this.activeIndex = activeIndex;
	}
	
	protected Comparator<IndirizzoDTO> compIndirizzi = new Comparator<IndirizzoDTO>() {
		public int compare(IndirizzoDTO o1, IndirizzoDTO o2) {
			if (o1 == null || o1.getCodVia() == null || o1.getCodVia().equals("") || o1.getDescrVia() == null || o1.getDescrVia().equals("")) {
				if (o2 == null || o2.getCodVia() == null || o2.getCodVia().equals("") || o2.getDescrVia() == null || o2.getDescrVia().equals(""))
					return 0;
				else
					return -1;
			}
			else if (o2 == null || o2.getCodVia() == null || o2.getCodVia().equals("") || o2.getDescrVia() == null || o2.getDescrVia().equals("")) {
				return 1;
			}
			else {
				String cod1 = o1.getCodVia();
				String cod2 = o2.getCodVia();
				String codViaSel = searchDto.getVia().getCodice();
				if (cod1.equals(codViaSel)) {
					if (cod2.equals(codViaSel))
						return compCivici.compare(o1.getCivico(), o2.getCivico());
					else
						return -1;
				}
				else if (cod2.equals(codViaSel)) {
					return 1;
				}
				else {
					int compare = o1.getDescrVia().compareTo(o2.getDescrVia());
					if (compare != 0) {
						return compare;
					} else {
						return compCivici.compare(o1.getCivico(), o2.getCivico());
					}
				}
			}
		}
	};
	
	protected Comparator<String> compCivici = new Comparator<String>() {
		public int compare(String o1, String o2) {
			if (o1 == null) {
				if (o2 == null)
					return 0;
				else
					return -1;
			}
			else if (o2 == null) {
				return 1;
			}
			else {
				o1 = o1.replace(" ", "").replace("-", "/");
				o2 = o2.replace(" ", "").replace("-", "/");
				StringBuffer sb1 = new StringBuffer();
				boolean trovato = false;
				for (char c : o1.toCharArray()) {
					if (!trovato) {
						if ("1234567890".indexOf(c) == -1) {
							trovato = true;
							if (c != '/') {
								sb1.append('/');
							}
						}
					}
					sb1.append(c);
				}
				o1 = sb1.toString();
				StringBuffer sb2 = new StringBuffer();
				trovato = false;
				for (char c : o2.toCharArray()) {
					if (!trovato) {
						if ("1234567890".indexOf(c) == -1) {
							trovato = true;
							if (c != '/') {
								sb2.append('/');
							}
						}
					}
					sb2.append(c);
				}
				o2 = sb2.toString();
				
				String prima1 = "0";
				if (!o1.equals("")) {
					if (o1.indexOf("/") == -1) {
						prima1 = o1;
					} else {
						if (!o1.startsWith("/")) {
							prima1 = o1.substring(0, o1.indexOf("/"));
						}
					}
				}
				int civ1 = 0;
				try {
					civ1 = Integer.parseInt(prima1);
				} catch (Exception e) {}
				
				String prima2 = "0";
				if (!o2.equals("")) {
					if (o2.indexOf("/") == -1) {
						prima2 = o2;
					} else {
						if (!o2.startsWith("/")) {
							prima2 = o2.substring(0, o2.indexOf("/"));
						}
					}
				}
				int civ2 = 0;
				try {
					civ2 = Integer.parseInt(prima2);
				} catch (Exception e) {}					
				
				int compare = new Integer(civ1).compareTo(new Integer(civ2));
				if (compare != 0) {
					return compare;
				} else {
					String seconda1 = "";
					if (!o1.equals("")) {
						if (o1.indexOf("/") > -1) {
							if (!o1.endsWith("/")) {
								seconda1 = o1.substring(o1.indexOf("/") + 1);
							}								
						}
					}
					String seconda2 = "";
					if (!o2.equals("")) {
						if (o2.indexOf("/") > -1) {
							if (!o2.endsWith("/")) {
								seconda2 = o2.substring(o2.indexOf("/") + 1);
							}								
						}
					}						
					if (seconda1.equals("")) {
						if (seconda2.equals(""))
							return 0;
						else
							return -1;
					}
					else if (seconda2.equals("")) {
						return 1;
					}
					else {
						return seconda1.compareTo(seconda2);
					}
				}
			}
		}
	};

}
