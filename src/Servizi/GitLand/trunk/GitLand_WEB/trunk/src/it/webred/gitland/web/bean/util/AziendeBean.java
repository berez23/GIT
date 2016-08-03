package it.webred.gitland.web.bean.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.Comune;
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
public class AziendeBean extends GitLandBaseBean {
	
	private List<AziendaView> listaAziende = null;
	private List<Comune> listaComuni = null;
	private List<Object[]> listaInsediamenti = null;
	private List<Object[]> listaObjs = null;
	private Azienda aziendaNew = null;
	private Comune comuneCur = null;
	private String ragioneSocialeSearch="";
	private String partitaIvaSearch="";
	private String codiceAsiSearch="";
	private Boolean eliminabile=false;
	public AziendeBean() {
		logger.info(AziendeBean.class + ".COSTRUTTORE");

	}//-------------------------------------------------------------------------
	
	public void init(){
		logger.info(AziendeBean.class + ".init");

		//HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		aziendaNew = new Azienda();
		comuneCur = new Comune();
		listaComuni = new ArrayList<Comune>();
		listaInsediamenti = new ArrayList<Object[]>();
		listaAziende= new ArrayList<AziendaView>();
		partitaIvaSearch="";
		ragioneSocialeSearch="";
		codiceAsiSearch="";
		
	}//-------------------------------------------------------------------------
	
	public String searchAziende(){
		String esito = "aziendeBean.searchAziende";
		logger.info(AziendeBean.class + ".searchAziende");
		//GitLandSessionBeanRemote gitLandService = getGitLandService();
		String query="select A.PK_AZIENDA, A.CODICE_ASI, A.RAGIONE_SOCIALE, A.RAPPR_LEGALE, A.COD_ATTIVITA, A.ATTIVITA, A.VIA_CIVICO,  " +
				"    C.NOME, A.P_IVA, A.COD_FISCALE, A.TELEFONO, A.FAX, A.EMAIL, A.WEB, A.AREA_ASI, A.NUM_ISC_CAM_COM  " +
				"from aziende a " +
				"left join comuni c on A.ISTATP=C.ISTATP and A.ISTATC=C.ISTATC " +
				"where a.belfiore='"+getEnte()+"' "; 

		
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		listaAziende = new ArrayList<AziendaView>();
		if (StringUtils.isNotBlank(ragioneSocialeSearch)){
			query+="and upper(A.RAGIONE_SOCIALE) like '"+ragioneSocialeSearch.toUpperCase() + "%' ";
		}
		if (StringUtils.isNotBlank(partitaIvaSearch)){
			query+="and A.P_IVA = '"+partitaIvaSearch + "' ";
		}
		
		if (StringUtils.isNotBlank(codiceAsiSearch)){
			query+="and A.CODICE_ASI = "+codiceAsiSearch + " ";
		}
		
		query+="order By A.RAGIONE_SOCIALE";
		
		List<Object[]> lista = gitLandService.getList(query, false);
		for (Object[] riga : lista) {
			AziendaView azi=new AziendaView(riga);
			listaAziende.add(azi);
		}
		return esito;
	}//-------------------------------------------------------------------------

	public void resetForm(){
		logger.info(AziendeBean.class + ".resetForm");
		
		init();
		
	}//-------------------------------------------------------------------------
	//<f:setPropertyActionListener value="#{cliente}" target="#{clientiAct.currentCli}"/>
	public String onRowSelect(SelectEvent event) throws IOException{
		logger.info(AziendeBean.class + ".onRowSelect");
		String esito = "aziendeBean.onRowSelect";
		
		AziendaView a = (AziendaView) event.getObject();
		caricaAzienda(a.getPkAzienda());
		
		if (aziendaNew != null && aziendaNew.getPkAzienda() != null && aziendaNew.getPkAzienda().longValue()>0){
			
			//La chiamata ad un ajax listener è diversa da una action e pertanto
			//se deve cambiare pagina va indicato il redirect
			FacesContext.getCurrentInstance().getExternalContext().redirect("aziendaMod.faces");
			
		}else{
			
			esito = "failure";

		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	private void caricaAzienda(Long pkAzienda) {
		aziendaNew = (Azienda)gitLandService.getItemById(pkAzienda, Azienda.class);

		String istatc = aziendaNew.getIstatc();
		String istatp = aziendaNew.getIstatp();
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		
		if (istatc != null && !istatc.trim().equalsIgnoreCase("") ){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("istatc");
			f1.setParametro("istatc");
			f1.setOperatore("=");
			f1.setValore( istatc );
			alFiltri.add(f1);
			
		}
		if (istatp != null && !istatp.trim().equalsIgnoreCase("") ){
			FiltroDTO f1 = new FiltroDTO();
			f1.setAttributo("istatp");
			f1.setParametro("istatp");
			f1.setOperatore("=");
			f1.setValore( istatp );
			alFiltri.add(f1);
			
		}
		if (alFiltri != null && alFiltri.size()>0){
			htQry.put("where", alFiltri);
			htQry.put("limit", 1);
			listaComuni = gitLandService.getList(htQry, Comune.class);
		}
		if (listaComuni != null && listaComuni.size()>0)
			comuneCur = (Comune)listaComuni.get(0);
		
		/*
		 * Valorizzo l'elenco storico degli insediamenti per questo lotto
		 */
		listaInsediamenti = new ArrayList<Object[]>();
		String sql="select al.codice_asi_lotto, al.codice_asi_azienda, al.data_fine_status, al.status, lotti.area, lotti.perimetro, lotti.indirizzo  " +
				"from aziende_lotti al  " +
				"left join aziende on al.codice_asi_azienda = aziende.codice_asi  " +
				"left join lotti  on al.codice_asi_lotto = lotti.codice_asi  " +
				"where  " +
				"al.codice_asi_azienda = '" + aziendaNew.getCodiceAsi() + "' and  " +
				"al.belfiore = '" + getEnte()+ "'  " +
				"and aziende.belfiore = '" + getEnte()+ "'  " +
				"and lotti.belfiore = '" + getEnte()+ "'  " +
				"order by al.data_fine_status desc, al.pk_azienda_lotto desc " ;


		logger.info(sql);
		
		listaInsediamenti = gitLandService.getList(sql, false);

		sql="select count(0) PraticheAttive  " +
				"from pratiche p  " +
				"where  " +
				"p.codice_asi_azienda = '" + aziendaNew.getCodiceAsi() + "' and  " +
				"p.belfiore = '" + getEnte()+ "' " ;
		//Controllo se ci sono insediamenti o pratiche per rendere eliminabile o meno la pratica
		eliminabile=listaInsediamenti.size()==0;
		List prati= gitLandService.getList(sql, false);
		if(!"0".equals(prati.get(0).toString())){
			eliminabile=false;
		}
		
		
	}

	public String aziendeShowLst(){
		logger.info(AziendeBean.class + ".aziendeShowLst");
		String esito = "aziendeBean.aziendeShowLst";
		
		init();
		
		listaAziende = new ArrayList<AziendaView>();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String aziendaShowNew(){
		String esito = "aziendeBean.aziendaShowNew";
		
		aziendaNew = new Azienda();
		comuneCur = new Comune();
		/*
		 * Calcolo il max codice ASI per il BELFIORE corrente
		 */
		//Select max(f.codice_Asi) from Aziende f where f.belfiore = 'F704' group by f.belfiore
		List<Object[]> listaMax = new ArrayList<Object[]>();
		String hql = "select max(AZIENDA.codiceAsi) from Azienda AZIENDA where AZIENDA.belfiore = '" + getEnte() + "' group by AZIENDA.belfiore";
		logger.info(hql);
		listaMax = gitLandService.getList(hql, true);
		Long codiceAsiMax = 0l;
		if (listaMax != null && listaMax.size() == 1){
			Object objMax = listaMax.get(0);
			codiceAsiMax = (Long)objMax;
		}
		aziendaNew.setCodiceAsi(codiceAsiMax.longValue()+1);
		aziendaNew.setBelfiore(getEnte());
		
		return esito;
	}//-------------------------------------------------------------------------
	

	public List<AziendaView> getListaAziende() {
		return listaAziende;
	}

	public void setListaAziende(List<AziendaView> listaAziende) {
		this.listaAziende = listaAziende;
	}

	public String aziendaSaveNew(){
		String esito="aziendeBean.aziendaSaveNew";
		GitLandSessionBeanRemote gitLandService = getGitLandService();
		if (aziendaNew != null){
			if (comuneCur != null && comuneCur.getIdComune()>0){
				Comune c = (Comune)gitLandService.getItemById( comuneCur.getIdComune() , Comune.class);
				if (c != null && c.getIdComune() != null && c.getIdComune().longValue()>0){
					aziendaNew.setIstatc(c.getIstatc());
					aziendaNew.setIstatp(c.getIstatp());
				}
			} 
			aziendaNew.setBelfiore(getEnte());
			gitLandService.addItem(aziendaNew);
			init();
		}
		return esito;

	}//-------------------------------------------------------------------------
	
	public String aziendaSaveMod(){
		logger.info(AziendeBean.class + ".aziendaSaveMod");
		String esito = "aziendeBean.aziendaSaveMod";

		GitLandSessionBeanRemote gitLandService = getGitLandService();
		if (aziendaNew != null){
			if (comuneCur != null && comuneCur.getIdComune()>0){
				Comune c = (Comune)gitLandService.getItemById( comuneCur.getIdComune() , Comune.class);
				if (c != null && c.getIdComune() != null && c.getIdComune().longValue()>0){
					aziendaNew.setIstatc(c.getIstatc());
					aziendaNew.setIstatp(c.getIstatp());
				}
			} 
			aziendaNew.setBelfiore(getEnte());
			gitLandService.updItem(aziendaNew);
			aziendaNew = new Azienda();
			comuneCur = new Comune();
			//listaAziende = new ArrayList<AziendaView>();
		}

		return esito;
	}//-------------------------------------------------------------------------

	public String aziendaSaveDel(){
		logger.info(AziendeBean.class + ".aziendaSaveDel");
		String esito = "aziendeBean.aziendaSaveDel";

		GitLandSessionBeanRemote gitLandService = getGitLandService();
		if (aziendaNew != null){
			gitLandService.delItemById(aziendaNew.getPkAzienda(),Azienda.class,false);
			init();
		}

		return esito;
	}//-------------------------------------------------------------------------

	
	public List<String> completeAziende(String filtro){
		logger.info(AziendeBean.class + ".completeAziende: " + filtro);
		String query="select ragione_sociale from AZIENDE where belfiore='"+getEnte()+"' and upper(ragione_sociale) like'%"+filtro.toUpperCase()+"%'";
		
		List aziende = gitLandService.getList(query,false);

		return aziende;
	}//-------------------------------------------------------------------------
	
	
	public String setComuneValues(SelectEvent event){
		logger.info(AziendeBean.class + ".setComuneValues");
		String esito = "aziendeBean.setComuneValues";

		Object item = event.getObject();
		FacesMessage msg = new FacesMessage("Selected", "Item:" + (Long)item);

		comuneCur = (Comune)gitLandService.getItemById( (Long)item, Comune.class );

		return esito;
	}//------------------------------------------------------------------------- 
	
	public List<Comune> completeComuni(String filtro){
		logger.info(AziendeBean.class + ".completeComuni: " + filtro);
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f0 = new FiltroDTO();
		f0.setAttributo("nome");
		f0.setParametro("nome");
		f0.setOperatore("LIKE");
		f0.setValore(filtro + "%");
		alFiltri.add(f0);
		htQry.put("where", alFiltri);
		htQry.put("limit", 5);
		htQry.put("orderBy", "nome");
		List<Comune> comuni = gitLandService.getList(htQry, Comune.class);

		return comuni;
	}//-------------------------------------------------------------------------

//	public void renderator(){
//		if (aziendaNew != null){
//			logger.info( aziendaNew.getIstatc() + "-" + aziendaNew.getIstatp() + "-" + aziendaNew.toString() );
//		}
//	}//-------------------------------------------------------------------------

	public List<Object[]> getListaObjs() {
		return listaObjs;
	}

	public void setListaObjs(List<Object[]> listaObjs) {
		this.listaObjs = listaObjs;
	}

	public Azienda getAziendaNew() {
		return aziendaNew;
	}

	public void setAziendaNew(Azienda aziendaNew) {
		this.aziendaNew = aziendaNew;
	}

	

	public Comune getComuneCur() {
		return comuneCur;
	}

	public void setComuneCur(Comune comuneCur) {
		this.comuneCur = comuneCur;
	}

	public List<Comune> getListaComuni() {
		return listaComuni;
	}

	public void setListaComuni(List<Comune> listaComuni) {
		this.listaComuni = listaComuni;
	}

	public List<Object[]> getListaInsediamenti() {
		return listaInsediamenti;
	}

	public void setListaInsediamenti(List<Object[]> listaInsediamenti) {
		this.listaInsediamenti = listaInsediamenti;
	}

	public String getRagioneSocialeSearch() {
		return ragioneSocialeSearch;
	}

	public void setRagioneSocialeSearch(String ragioneSocialeSearch) {
		this.ragioneSocialeSearch = ragioneSocialeSearch;
	}

	public String getPartitaIvaSearch() {
		return partitaIvaSearch;
	}

	public void setPartitaIvaSearch(String partitaIvaSearch) {
		this.partitaIvaSearch = partitaIvaSearch;
	}

	public String getCodiceAsiSearch() {
		return codiceAsiSearch;
	}

	public void setCodiceAsiSearch(String codiceAsiSearch) {
		this.codiceAsiSearch = codiceAsiSearch;
	}

	public Boolean getEliminabile() {
		return eliminabile;
	}

	public void setEliminabile(Boolean eliminabile) {
		this.eliminabile = eliminabile;
	}

}
