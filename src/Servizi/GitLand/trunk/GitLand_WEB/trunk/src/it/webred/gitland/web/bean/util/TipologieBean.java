package it.webred.gitland.web.bean.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.Comune;
import it.webred.gitland.data.model.Tipologia;
import it.webred.gitland.ejb.client.GitLandSessionBeanRemote;
import it.webred.gitland.ejb.dto.FiltroDTO;
import it.webred.gitland.web.bean.GitLandBaseBean;
import it.webred.gitland.web.bean.items.AziendaView;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;











import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class TipologieBean extends GitLandBaseBean {
	
	private List<Tipologia> listaTipologie = null;
	private Tipologia tipologiaCur = null;
	private String codTipoSearch="";
	private String tipologiaSearch="";
	private Boolean eliminabile=false;
	public TipologieBean() {
		logger.info(TipologieBean.class + ".COSTRUTTORE");

	}//-------------------------------------------------------------------------
	
	public void init(){
		logger.info(TipologieBean.class + ".init");

		tipologiaCur = new Tipologia();
		listaTipologie= new ArrayList<Tipologia>();
		codTipoSearch="";
		tipologiaSearch="";
		
	}//-------------------------------------------------------------------------
	
	public String searchTipologie(){
		String esito = "tipologieBean.searchTipologie";
		logger.info(TipologieBean.class + ".searchTipologie");
		
		List<Tipologia> lista = new ArrayList<Tipologia>();
		
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();

		if (StringUtils.isNotBlank(codTipoSearch) ){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("codTipo");
			f1.setParametro("codTipo");
			f1.setOperatore("LIKE");
			f1.setValore( codTipoSearch+"%" );
			alFiltri.add(f1);
			
		}
		if (StringUtils.isNotBlank(tipologiaSearch)){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("tipologia");
			f1.setParametro("tipologia");
			f1.setOperatore("LIKE");
			f1.setValore( tipologiaSearch+"%");
			alFiltri.add(f1);
			
		}
		if (alFiltri != null && alFiltri.size()>0){
			htQry.put("where", alFiltri);
		}
		htQry.put("orderBy", "codTipo");
		lista = gitLandService.getList(htQry, Tipologia.class);
		/*
		 * Il codice contiene numeri interi anche se il campo è di tipo stringa
		 * per cui li ordino come numeri
		 */
		
		if (lista != null && lista.size()>0){
			Integer[] alCod = new Integer[lista.size()];
			Hashtable<Integer, Tipologia> htTipo = new Hashtable<Integer, Tipologia>();
			for (int index=0; index<lista.size(); index++){
				Tipologia t = lista.get(index);
				htTipo.put(new Integer(t.getCodTipo()), t);
				alCod[index] = new Integer(t.getCodTipo());
			}
			Arrays.sort(alCod);
			listaTipologie = new ArrayList<Tipologia>();
			for (int index=0; index<alCod.length; index++){
				listaTipologie.add(htTipo.get(alCod[index]));
			}
		}else
			listaTipologie = new ArrayList<Tipologia>();
		
		
		return esito;
	}//-------------------------------------------------------------------------

	public void resetForm(){
		logger.info(TipologieBean.class + ".resetForm");
		
		init();
		
	}//-------------------------------------------------------------------------
	//<f:setPropertyActionListener value="#{cliente}" target="#{clientiAct.currentCli}"/>
	public String onRowSelect(SelectEvent event) throws IOException{
		logger.info(TipologieBean.class + ".onRowSelect");
		String esito = "tipologieBean.onRowSelect";
		
		tipologiaCur = (Tipologia) event.getObject();
		
		if (tipologiaCur != null && tipologiaCur.getCodTipo() != null ){
			
			String sql="select count(0) PraticheAttive  " +
					"from pratiche p  " +
					"where  " +
					"p.cod_tipo = '" + tipologiaCur.getCodTipo() + "'";
			//Controllo se ci sono insediamenti o pratiche per rendere eliminabile o meno la pratica
			List prati= gitLandService.getList(sql, false);
			if(!"0".equals(prati.get(0).toString())){
				eliminabile=false;
			}else{
				eliminabile=true;
			}
			
			
			//La chiamata ad un ajax listener è diversa da una action e pertanto
			//se deve cambiare pagina va indicato il redirect
			FacesContext.getCurrentInstance().getExternalContext().redirect("tipologiaMod.faces");
			
		}else{
			
			esito = "failure";

		}
		
		return esito;
	}//-------------------------------------------------------------------------
	

	public String tipologieShowLst(){
		logger.info(TipologieBean.class + ".tipologieShowLst");
		String esito = "tipologieBean.tipologieShowLst";
		
		init();
		
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String tipologiaShowNew(){
		logger.info(TipologieBean.class + ".tipologiaShowNew");
		String esito = "tipologieBean.tipologiaShowNew";
		
		tipologiaCur = new Tipologia();
		return esito;
	}//-------------------------------------------------------------------------
	
	public String tipologiaSaveNew(){
		logger.info(TipologieBean.class + ".tipologiaSaveMod");
		String esito = "tipologieBean.tipologiaSaveNew";
		GitLandSessionBeanRemote gitLandService = getGitLandService();
		if (tipologiaCur != null){
			gitLandService.addItem(tipologiaCur);
		}
		init();
		return esito;
	}//-------------------------------------------------------------------------
	
	public String tipologiaSaveMod(){
		logger.info(TipologieBean.class + ".tipologiaSaveMod");
		String esito = "tipologieBean.tipologiaSaveMod";

		GitLandSessionBeanRemote gitLandService = getGitLandService();
		if (tipologiaCur != null){
			gitLandService.updItem(tipologiaCur);
		}
		init();

		return esito;
	}//-------------------------------------------------------------------------

	public String tipologiaSaveDel(){
		logger.info(TipologieBean.class + ".tipologiaSaveDel");
		String esito = "tipologieBean.tipologiaSaveDel";

		GitLandSessionBeanRemote gitLandService = getGitLandService();
		if (tipologiaCur != null){
			gitLandService.delItemById(tipologiaCur.getCodTipo(),Tipologia.class);
		}
		init(); 
		return esito;
	}//-------------------------------------------------------------------------

	public List<Tipologia> getListaTipologie() {
		return listaTipologie;
	}

	public void setListaTipologie(List<Tipologia> listaTipologie) {
		this.listaTipologie = listaTipologie;
	}

	public Tipologia getTipologiaCur() {
		return tipologiaCur;
	}

	public void setTipologiaCur(Tipologia tipologiaCur) {
		this.tipologiaCur = tipologiaCur;
	}

	public String getCodTipoSearch() {
		return codTipoSearch;
	}

	public void setCodTipoSearch(String codTipoSearch) {
		this.codTipoSearch = codTipoSearch;
	}

	public String getTipologiaSearch() {
		return tipologiaSearch;
	}

	public void setTipologiaSearch(String tipologiaSearch) {
		this.tipologiaSearch = tipologiaSearch;
	}

	public Boolean getEliminabile() {
		return eliminabile;
	}

	public void setEliminabile(Boolean eliminabile) {
		this.eliminabile = eliminabile;
	}

}
