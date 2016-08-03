package it.webred.gitland.web.bean.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.IntParametro;
import it.webred.gitland.data.model.IntQuery;
import it.webred.gitland.data.model.Pratica;
import it.webred.gitland.ejb.dto.FiltroDTO;
import it.webred.gitland.web.bean.GitLandBaseBean;
import it.webred.gitland.web.statistiche.ColumnModel;
import it.webred.gitland.web.statistiche.FormField;
import it.webred.utils.CollectionsUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

import com.sun.org.apache.bcel.internal.generic.LUSHR;


@ManagedBean
@SessionScoped
public class StatisticheBean extends GitLandBaseBean {
	
	private String categoriaSearch="";
	private Long idCategoriaSearch=0l;
	private String descrizioneSearch="";
	private List<IntQuery> listaQuery;
	private IntQuery queryCurr=null;
	private DynaFormModel modelloForm; 
	private List<FormField> inputParams=new ArrayList<FormField>();
	private List<Object[]> listaRisultato;
	private List<Map<String,Object>> listaRighe;
	private List<ColumnModel> colonne;
	private List<SelectItem> listaTipologieItems = null;
	private List<SelectItem> listaCategorieItems = null;
	
	public StatisticheBean() {
	}//-------------------------------------------------------------------------
	
	public String statisticheShowLst(){
		String esito = "statisticheBean.statisticheShowLst";
		logger.info(StatisticheBean.class + ".statisticheShowLst");
		init();
		return esito;
	}//-------------------------------------------------------------------------
	
	private void init() {
		categoriaSearch="";
		descrizioneSearch="";
		idCategoriaSearch=0l;
		listaQuery=new ArrayList<IntQuery>();
		listaCategorieItems=null;
		listaRisultato= null;
	}

	public DynaFormModel getModelloForm() {
		if(modelloForm==null)costruisciModelloQueryCorrente();
		return modelloForm;
	}
	
	
	public String submitForm() {  
        FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();  
        boolean hasErrors = (sev != null && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));  
        
        
        RequestContext requestContext = RequestContext.getCurrentInstance();  
        requestContext.addCallbackParam("isValid", !hasErrors);  
        if(!hasErrors){
        	//La chiamata ad un ajax listener è diversa da una action e pertanto
			//se deve cambiare pagina va indicato il redirect
        	
			try {
				costruisciEdEseguiQuery();
				FacesContext.getCurrentInstance().getExternalContext().redirect("statisticaEsito.faces");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        return null;  
    }  
  	
	private void costruisciEdEseguiQuery() {
        Hashtable<String, Object> pars= new Hashtable<String, Object>();
        String query=queryCurr.getQuery();
        String where=queryCurr.getWhereDinamica();
        String orderBy=queryCurr.getOrderBy();
        String prepend=" AND ";
        boolean isHQL=!"S".equals(queryCurr.getQueryNativa());
        boolean isIndexMapping="I".equals(queryCurr.getTipoMapping());
        if(where==null)where="";
        if(where.toUpperCase().endsWith("WHERE")){
        	prepend=" ";
        }
        //preparo i parametri per la query
        for (FormField field : getFormFields()) {
        	//logger.info(field.getValue());
			Object valore=field.getValue();
			IntParametro par= field.getParametro();
			//Controllo che il parametro non sia ti tipo Azienda ed in quel caso lo valorizzo con il giusto valore
			if("azienda".equals(par.getDynaformType())){
				if(field.getAziendaSelezionata()!=null){
					valore=field.getAziendaSelezionata().getCodiceAsi();
				}
			}
			//Controllo che il parametro non sia ti tipo Azienda ed in quel caso lo valorizzo con il giusto valore
			if("pratica".equals(par.getDynaformType())){
				if(field.getPraticaSelezionata()!=null){
					valore=field.getPraticaSelezionata().getCodicePratica();
				}
			}
			//se il parametro è valorizzato lo aggiungo
			if(valore!=null){
				if(!FormField.DATA_TYPE_STRING.equals(par.getDataType()) || (FormField.DATA_TYPE_STRING.equals(par.getDataType()) && StringUtils.isNotEmpty((String)valore))){
					if(FormField.DATA_TYPE_STRING.equals(par.getDataType())){
						if(FormField.MATCH_TYPE_INIZIA.equals(par.getMatchType())){
							valore=valore.toString()+"%";
						}
						if(FormField.MATCH_TYPE_FINISCE.equals(par.getMatchType())){
							valore="%"+valore.toString();
						}
						if(FormField.MATCH_TYPE_CONTIENE.equals(par.getMatchType())){
							valore="%"+valore.toString()+"%";
						}
					}
					pars.put(par.getNomeParametro(), valore);
					//ed aggiungo anche l'eventuale aggiunta where se la where dinamica è valorizzata
					if(StringUtils.isNotBlank(par.getAggiuntaWhere()) && StringUtils.isNotBlank(where)){
						where+=prepend+par.getAggiuntaWhere();
						prepend=" AND ";
					}
					
				}
			}
		}
        if(StringUtils.isNotBlank(where)){
        	query+=" "+ where;
        }
        if(StringUtils.isNotBlank(orderBy)){
        	query+=" "+orderBy;
        }
        listaRisultato=getGitLandService().getList(query, pars, isHQL);
        if(listaRighe==null)listaRighe= new ArrayList<Map<String,Object>>(); 
        if(colonne==null)colonne= new ArrayList<ColumnModel>(); 
        listaRighe.clear();
        colonne.clear();
        String[] campi= queryCurr.getMappingRisultato().split("\\|");
        for (String campo : campi) {
        	String[] colonna=campo.split("=");
        	colonne.add(new ColumnModel(colonna[0], colonna[1]));
		}
        for (int i=0;i< listaRisultato.size();i++) {
        	Object[] elem=isIndexMapping?listaRisultato.get(i):new Object[]{listaRisultato.get(i)};
        	listaRighe.add(creaRiga(elem,campi,isHQL));
		}
        
	}

	private Map<String, Object> creaRiga(Object[] elem, String[] campi,
			boolean isHQL) {
        Map<String, Object> riga = new HashMap<String, Object>();
    		Object entity=elem[0];
    		for (String campo : campi) {
    			String proprieta=campo.split("=")[1];
				Object value=null;
		    	if(isHQL){
					try {
						value=PropertyUtils.getProperty(entity, proprieta);
					} catch (Exception e) {
						// errore nel recupero della colonna
						logger.error("Errore durante il recupero del valore proprietà ritornato dalla query hql");
						logger.error(e);
					}
		    	}else{
		    		int indice=-1;
		    		try {
			    		indice=Integer.parseInt(proprieta);
			    		value=elem[indice];
					} catch (Exception e) {
						// errore nel recupero della colonna
						logger.error("Errore durante il recupero del valore incizzato ritornato dalla query nativa");
						logger.error(e);
					}
		    		
		    	}
    			riga.put(proprieta, value);
			}
		return riga;
	}

	public List<FormField> getFormFields() {  
        if (modelloForm == null) {  
            return null;  
        }  
        List<FormField> fieldsProperties = new ArrayList<FormField>();  
        for (DynaFormControl dynaFormControl : modelloForm.getControls()) {  
        	if("it.webred.gitland.web.statistiche.FormField".equals(dynaFormControl.getData().getClass().getName()))fieldsProperties.add((FormField) dynaFormControl.getData());  
        }  
  
        return fieldsProperties;  
    }  
	
	private void costruisciModelloQueryCorrente() {
		// Costruzione del modello DynaForm per l'input dei parametri della query
		
	 	modelloForm = new DynaFormModel();  
		
	 	//Parto dal presupposto che riga e posizione sono not_null
	 	List<IntParametro> parametri= queryCurr.getParametri();
	 	
	 	Collections.sort(parametri, new Comparator<IntParametro>() {

			@Override
			public int compare(IntParametro o1, IntParametro o2) {
				int ret= o1.getRiga().compareTo(o2.getRiga());
				if(ret == 0){
					ret=o1.getPosizione().compareTo(o2.getPosizione());
				}
				return ret;
			}
		});
	 	Long rigaCorrente=null;
	 	DynaFormRow row = modelloForm.createRegularRow();
	 	for (IntParametro par : parametri) {
	 		if(!par.getRiga().equals(rigaCorrente)){
	 			rigaCorrente=par.getRiga();
	 			row = modelloForm.createRegularRow();
	 		}
	 		FormField field= new FormField(par);
	 		if(FormField.TIPO_ENTE_CORRENTE.equals(par.getTipo()))field.setValue(getEnte());
	 		if(FormField.TIPO_UTENTE_CORRENTE.equals(par.getTipo()))field.setValue(getUser().getUsername());
	 		int colSpan=par.getColSpan()!=null?par.getColSpan().intValue():1;
	 		int rowSpan=par.getRowSpan()!=null?par.getRowSpan().intValue():1;
	 		DynaFormLabel label = row.addLabel(par.getDescrizione(), 1,rowSpan);
	 		DynaFormControl control = row.addControl(field, par.getDynaformType(), colSpan,rowSpan);
	 		label.setForControl(control);
		}
	}

	public String statisticheSearch(){
		String esito = "statisticheBean.statisticheSearch";
		logger.info(StatisticheBean.class + ".statisticheSearch");
		listaQuery=new ArrayList<IntQuery>();
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		if (idCategoriaSearch>=0 ){
			FiltroDTO f = new FiltroDTO();
			f.setAttributo("categoria.id");
			f.setParametro("idCategoria");
			f.setOperatore("=");
			f.setValore( idCategoriaSearch);
			alFiltri.add(f);
		}
//		if (StringUtils.isNotBlank(categoriaSearch) ){
//			FiltroDTO f = new FiltroDTO();
//			f.setAttributo("categoria.descrizione");
//			f.setParametro("descrizioneCategoria");
//			f.setOperatore("LIKE");
//			f.setValore( "%" + categoriaSearch +"%");
//			alFiltri.add(f);
//		}
		if (StringUtils.isNotBlank(descrizioneSearch) ){
			FiltroDTO f = new FiltroDTO();
			f.setAttributo("descrizione");
			f.setParametro("descrizione");
			f.setOperatore("LIKE");
			f.setValore("%" + descrizioneSearch +"%");
			alFiltri.add(f);
		}
		if (alFiltri != null && alFiltri.size()>0){
			htQry.put("where", alFiltri);
		}
		htQry.put("orderBy", "id");
		listaQuery = gitLandService.getList(htQry, IntQuery.class);

		return esito;
	}//-------------------------------------------------------------------------

	public String onRowSelect(SelectEvent event){
		String esito = "statisticheBean.onRowSelect";
		logger.info(StatisticheBean.class + ".onRowSelect");
		//imposto la query corrente ed annullo l'eventuale precedente modello 
		queryCurr=(IntQuery) event.getObject();
		
		modelloForm=null;
		listaTipologieItems=null;
		try {
			//La chiamata ad un ajax listener è diversa da una action e pertanto
			//se deve cambiare pagina va indicato il redirect
			FacesContext.getCurrentInstance().getExternalContext().redirect("statisticaParamsLst.faces");
			
		} catch (Exception e) {
			logger.error(e);
		}

		return esito;
	}//-------------------------------------------------------------------------

	public List<Azienda> completeAziende(String filtro){
		logger.info(StatisticheBean.class + ".completeAziende: " + filtro);
		Hashtable<String, Object> pars= new Hashtable<String, Object>();
		String query="from Azienda a where belfiore=:belfiore ";
		pars.put("belfiore", getEnte());
		if(NumberUtils.isNumber( filtro)){
			query+="and a.codiceAsi = "+filtro;
		}else{
			query+="and a.ragioneSociale like '%"+filtro+"%'";
		}
		query+=" order by a.ragioneSociale";
		
		List lista=gitLandService.getList(query,pars,true);
		
		List<Azienda> listaAziendeFiltrata = new ArrayList<Azienda>(); 
		for (int i=0;i< lista.size();i++) {
			listaAziendeFiltrata.add((Azienda)lista.get(i));
		}
		return listaAziendeFiltrata;
	}//-------------------------------------------------------------------------
	
	public List<Pratica> completePratiche(String filtro){
		logger.info(StatisticheBean.class + ".completePratiche: " + filtro);
		Hashtable<String, Object> pars= new Hashtable<String, Object>();
		String query="from Pratica p where belfiore=:belfiore ";
		pars.put("belfiore", getEnte());
		query+="and p.codicePratica like '%"+filtro+"%'";
		query+=" order by p.protocolloNum, p.protocolloData";
		
		List lista=gitLandService.getList(query,pars,true);
		
		List<Pratica> listaPraticheFiltrata = new ArrayList<Pratica>(); 
		for (int i=0;i< lista.size();i++) {
			listaPraticheFiltrata.add((Pratica)lista.get(i));
		}
		return listaPraticheFiltrata;
	}//-------------------------------------------------------------------------
	
	private void loadTipologieItems(Boolean firstEmpty){
		
		String sql = "select cod_tipo, tipologia "
				+ "from tipologia "
				+ "order by cod_tipo, tipologia ";
		
		logger.info(sql);
		
		listaTipologieItems = new LinkedList<SelectItem>();
		if (firstEmpty){
			SelectItem si0 = new SelectItem();
			si0.setDescription("");
			si0.setLabel("");
			si0.setValue("");
			listaTipologieItems.add(si0);
		}
		List<Object[]> tipologieObjs = gitLandService.getList(sql, false);
		if (tipologieObjs != null && tipologieObjs.size()>0){
			Iterator<Object[]> itTipoObjs = tipologieObjs.iterator();
			while(itTipoObjs.hasNext()){
				Object[] objTipol = itTipoObjs.next();
				String cod = (String)objTipol[0];
				String des = (String)objTipol[1];

				SelectItem si = new SelectItem();
				si.setDescription(des);
				si.setLabel(cod + " - " + des);
				si.setValue(cod);
				listaTipologieItems.add(si);
				
			}
		}
	}//-------------------------------------------------------------------------

	private void loadCategorieItems(Boolean firstEmpty){
		
		String sql = "select id, descrizione "
				+ "from int_categoria "
				+ "order by descrizione";
		
		logger.info(sql);
		
		listaCategorieItems = new LinkedList<SelectItem>();
		if (firstEmpty){
			SelectItem si0 = new SelectItem();
			si0.setDescription("");
			si0.setLabel("");
			si0.setValue(-1l);
			listaCategorieItems.add(si0);
		}
		List<Object[]> categorieObjs = gitLandService.getList(sql, false);
		if (categorieObjs != null && categorieObjs.size()>0){
			Iterator<Object[]> itCatObjs = categorieObjs.iterator();
			while(itCatObjs.hasNext()){
				Object[] objTipol = itCatObjs.next();
				BigDecimal id = (BigDecimal)objTipol[0];
				String des = (String)objTipol[1];

				SelectItem si = new SelectItem();
				si.setDescription(des);
				si.setLabel(des);
				si.setValue(id);
				listaCategorieItems.add(si);
				
			}
		}
	}//-------------------------------------------------------------------------

	
	public String statisticaAvvio(){
		String esito = "statisticheBean.statisticaAvvio";
		logger.info(StatisticheBean.class + ".statisticaAvvio");
        FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();  
        boolean hasErrors = (sev != null && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));  
  
        for (FormField field : getFormFields()) {
			logger.info(field.getValue());
		}
        RequestContext requestContext = RequestContext.getCurrentInstance();  
        requestContext.addCallbackParam("isValid", !hasErrors);  
  

		return esito;
	}//-------------------------------------------------------------------------
	
	public String statisticaShowNew(){
		String esito = "statisticheBean.statisticaShowNew";
		logger.info(StatisticheBean.class + ".statisticaShowNew");

		return esito;
	}//-------------------------------------------------------------------------
	
	public String statisticaSaveNew(){
		String esito = "statisticheBean.statisticaSaveNew";
		logger.info(StatisticheBean.class + ".statisticaSaveNew");

		return esito;
	}//-------------------------------------------------------------------------
	
	public String statisticaSaveMod(){
		String esito = "statisticheBean.statisticaSaveMod";
		logger.info(StatisticheBean.class + ".statisticaSaveMod");

		return esito;
	}//-------------------------------------------------------------------------

	public String statisticaShowMod(){
		String esito = "statisticheBean.statisticaShowMod";
		logger.info(StatisticheBean.class + ".statisticaShowMod");

		return esito;
	}//-------------------------------------------------------------------------

	public String getOrientamento(){
		String ret="Portrait";
		if(queryCurr!=null && "O".equals(queryCurr.getOrientamento()))ret="Landscape";
		return ret;
	}
	public String getCategoriaSearch() {
		return categoriaSearch;
	}

	public void setCategoriaSearch(String categoriaSearch) {
		this.categoriaSearch = categoriaSearch;
	}

	public String getDescrizioneSearch() {
		return descrizioneSearch;
	}

	public void setDescrizioneSearch(String descrizioneSearch) {
		this.descrizioneSearch = descrizioneSearch;
	}

	public List<IntQuery> getListaQuery() {
		return listaQuery;
	}

	public void setListaQuery(List<IntQuery> listaQuery) {
		this.listaQuery = listaQuery;
	}

	public IntQuery getQueryCurr() {
		return queryCurr;
	}

	public void setQueryCurr(IntQuery queryCurr) {
		this.queryCurr = queryCurr;
	}

	public List<Object[]> getListaRisultato() {
		return listaRisultato;
	}

	public void setListaRisultato(List<Object[]> listaRisultato) {
		this.listaRisultato = listaRisultato;
	}

	public List<ColumnModel> getColonne() {
		return colonne;
	}

	public void setColonne(List<ColumnModel> colonne) {
		this.colonne = colonne;
	}

	public List<Map<String, Object>> getListaRighe() {
		return listaRighe;
	}

	public void setListaRighe(List<Map<String, Object>> listaRighe) {
		this.listaRighe = listaRighe;
	}

	public List<SelectItem> getListaTipologieItems() {
		if(listaTipologieItems==null)loadTipologieItems(true);
		return listaTipologieItems;
	}

	public void setListaTipologieItems(List<SelectItem> listaTipologieItems) {
		this.listaTipologieItems = listaTipologieItems;
	}

	public Long getIdCategoriaSearch() {
		return idCategoriaSearch;
	}

	public void setIdCategoriaSearch(Long idCategoriaSearch) {
		this.idCategoriaSearch = idCategoriaSearch;
	}

	public List<SelectItem> getListaCategorieItems() {
		if(listaCategorieItems==null)loadCategorieItems(true);
		return listaCategorieItems;
	}

	public void setListaCategorieItems(List<SelectItem> listaCategorieItems) {
		this.listaCategorieItems = listaCategorieItems;
	}

}
