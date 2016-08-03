package it.bod.application.backing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import it.bod.application.common.FilterItem;
import it.bod.application.common.MasterClass;
import it.bod.business.service.BodLogicService;
import it.doro.tools.TimeControl;
import it.webred.permessi.GestionePermessi;
import it.webred.rich.common.CalendarBoxRch;
import it.webred.rich.common.ComboBoxRch;

public class StatisticaFiltroBck extends MasterClass{

	private static final long serialVersionUID = 8349647779999744644L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.StatisticaFiltroBck");
	
	private BodLogicService bodLogicService = null;
	private CalendarBoxRch txtFornituraResDal = null;
	private CalendarBoxRch txtFornituraResAl = null;
	private CalendarBoxRch txtFornituraNonResDal = null;
	private CalendarBoxRch txtFornituraNonResAl = null;
	private Boolean rdoDiagnosticaNonRes = false;
	private Boolean rdoDiagnosticaRes = false;
	private String fornituraDal = null;
	private String fornituraAl = null;
	private String currentDiagno = "";
	private TimeControl tc = new TimeControl();
	private List<Object> lstDiagnostiche = null;
	private List<Object> lstCategorie = null;
	private Integer numberOfRows = 0;
	private ComboBoxRch cbxCategorie = null;
	private Boolean isAutorizzatoStatistica = false;
	private Boolean newSearch = false;

	public StatisticaFiltroBck() {
		super.initializer();
	}
	
	public void init(){
		txtFornituraResDal = new CalendarBoxRch();
		txtFornituraResAl = new CalendarBoxRch();
		cbxCategorie = new ComboBoxRch();
		cbxCategorie.setState("Tutte");
		cbxCategorie.setDefaultMessage("Tutte");
		txtFornituraNonResDal = new CalendarBoxRch();
		txtFornituraNonResAl = new CalendarBoxRch();
		newSearch = false;
	}//-------------------------------------------------------------------------
	
	public String preparaFiltro(){
		String esito = "statisticaFiltroBck.preparaFiltro";
		isAutorizzatoStatistica = GestionePermessi.autorizzato(utente, nomeApplicazione, "Statistiche Bod", "Esegui Statistiche");
		if (isAutorizzatoStatistica){
			init();			
		}else{
			esito = "failure";
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String ricerca(){
		String esito = "statisticaFiltroBck.ricerca";
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		String condizioni = "";
		String tabella = "";
		String tabCategorie = "";
		newSearch = true;
		if (rdoDiagnosticaRes){
			tabella = "DOCFA_DIAGNOSTICHE";
			tabCategorie = "DOCFA_REPORT";
			currentDiagno = "Residenziali";
			Date dataFornituraDal = this.txtFornituraResDal.getSelectedDate();
			if (dataFornituraDal != null){
				fornituraDal = sdf.format(dataFornituraDal);
				FilterItem fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_DAL_");
				fi.setParametro(":fornitura");
				fi.setValore(fornituraDal);
				aryFilters.add(fi);
				condizioni += "and DD.fornitura >= to_date('" + fornituraDal + "' ,'dd/mm/yyyy') ";
			}
			Date dataFornituraAl = this.txtFornituraResAl.getSelectedDate();
			if (dataFornituraAl != null){
				fornituraAl = sdf.format(dataFornituraAl);
				FilterItem fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_AL_");
				fi.setParametro(":fornitura");
				fi.setValore(fornituraAl);
				aryFilters.add(fi);
				condizioni += "and DD.fornitura <= to_date('" + fornituraAl + "' ,'dd/mm/yyyy') ";
			}
		}else if (rdoDiagnosticaNonRes){
			tabella = "DOCFA_DIAGNOSTICHE_NORES";
			tabCategorie = "DOCFA_REPORT_NORES";
			currentDiagno = "Non Residenziali";
			Date dataFornituraDal = this.txtFornituraNonResDal.getSelectedDate();
			if (dataFornituraDal != null){
				fornituraDal = sdf.format(dataFornituraDal);
				FilterItem fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_DAL_");
				fi.setParametro(":fornitura");
				fi.setValore(fornituraDal);
				aryFilters.add(fi);
				condizioni += "and DD.fornitura >= to_date('" + fornituraDal + "' ,'dd/mm/yyyy') ";
			}
			Date dataFornituraAl = this.txtFornituraNonResAl.getSelectedDate();
			if (dataFornituraAl != null){
				fornituraAl = sdf.format(dataFornituraAl);
				FilterItem fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_AL_");
				fi.setParametro(":fornitura");
				fi.setValore(fornituraAl);
				aryFilters.add(fi);
				condizioni += "and DD.fornitura <= to_date('" + fornituraAl + "' ,'dd/mm/yyyy') ";
			}
		}else{
			tabella = "DOCFA_DIAGNOSTICHE";
			tabCategorie = "DOCFA_REPORT";
			currentDiagno = "Residenziali";
			Date dataFornituraDal = this.txtFornituraResDal.getSelectedDate();
			if (dataFornituraDal != null){
				fornituraDal = sdf.format(dataFornituraDal);
				FilterItem fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_DAL_");
				fi.setParametro(":fornitura");
				fi.setValore(fornituraDal);
				aryFilters.add(fi);
				condizioni += "and DD.fornitura >= to_date('" + fornituraDal + "' ,'dd/mm/yyyy') ";
			}
			Date dataFornituraAl = this.txtFornituraResAl.getSelectedDate();
			if (dataFornituraAl != null){
				fornituraAl = sdf.format(dataFornituraAl);
				FilterItem fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_AL_");
				fi.setParametro(":fornitura");
				fi.setValore(fornituraAl);
				aryFilters.add(fi);
				condizioni += "and DD.fornitura <= to_date('" + fornituraAl + "' ,'dd/mm/yyyy') ";
			}
		}
		
		String sql = "select " +
				"FORNITURA, NREC_DATI_CENSUARI, NREC_DATAREG_NO_COER, NREC_TIPO_OPER_NO_UNICO, " +
				"NREC_TIPO_OPER_NULL, NREC_TIPO_OPER_CESS, NREC_DOCFA_OK, NREC_UIU " +
				"FROM " + tabella + " DD " +
				"WHERE 1=1 " + condizioni + " order by fornitura";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		lstDiagnostiche = bodLogicService.getList(htQry);
		if (lstDiagnostiche != null)
			numberOfRows = lstDiagnostiche.size(); 
		/*
		 * Recupero la lista delle categorie
		 */
		sql = "SELECT DISTINCT CAT FROM " + tabCategorie + " ORDER BY CAT";

		htQry = new Hashtable();
		htQry.put("queryString", sql);
		lstCategorie = bodLogicService.getList(htQry);
		if (lstCategorie != null){
			cbxCategorie = new ComboBoxRch();
			cbxCategorie.setState("Tutte");
			cbxCategorie.setDefaultMessage("Tutte");
			ArrayList<SelectItem> alSi = new ArrayList<SelectItem>();
			SelectItem si = null;
			Iterator<Object> itSi = lstCategorie.iterator();
			while(itSi.hasNext()){
				Object obj = itSi.next();
				si = new SelectItem();
				si.setDescription(obj.toString());
				si.setLabel(obj.toString());
				si.setValue(obj.toString());
				si.setDescription(obj.toString());
				alSi.add(si);
			}
			cbxCategorie.setSelectItems(alSi);
		}
		return esito;
	}//-------------------------------------------------------------------------
	
	public String validateDiagnoRes(){
		String esito = "statisticaFiltroBck.validateDiagnoRes";
		
		if (rdoDiagnosticaRes){
			logger.info("Da Falso a Vero Residenziali");
			
			/*
			 * Resetto la checkbox e le calendarbox dei Non residenziali
			 */
			rdoDiagnosticaNonRes = false;
			txtFornituraNonResDal = new CalendarBoxRch();
			txtFornituraNonResAl = new CalendarBoxRch();
		}else{
			logger.info("Da Vero a Falso Residenziali");
		}	
		return esito;
	}//-------------------------------------------------------------------------
	
	public String validateDiagnoNonRes(){
		String esito = "statisticaFiltroBck.validateDiagnoNonRes";
		if (rdoDiagnosticaNonRes){
			/*
			 * Resetto la checkbox e le calendarbox dei residenziali
			 */
			rdoDiagnosticaRes = false;
			txtFornituraResDal = new CalendarBoxRch();
			txtFornituraResAl = new CalendarBoxRch();
			logger.info("Da Falso a Vero Non Residenziali");
		}else{
			logger.info("Da Vero a Falso Non Residenziali");
		}	
		return esito;
	}//-------------------------------------------------------------------------

	public CalendarBoxRch getTxtFornituraResDal() {
		return txtFornituraResDal;
	}

	public void setTxtFornituraResDal(CalendarBoxRch txtFornituraDal) {
		this.txtFornituraResDal = txtFornituraDal;
	}

	public CalendarBoxRch getTxtFornituraResAl() {
		return txtFornituraResAl;
	}

	public void setTxtFornituraResAl(CalendarBoxRch txtFornituraAl) {
		this.txtFornituraResAl = txtFornituraAl;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}

	public List<Object> getLstDiagnostiche() {
		return lstDiagnostiche;
	}

	public void setLstDiagnostiche(List<Object> lstDiagnostiche) {
		this.lstDiagnostiche = lstDiagnostiche;
	}

	public Integer getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(Integer numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public List<Object> getLstCategorie() {
		return lstCategorie;
	}

	public void setLstCategorie(List<Object> lstCategorie) {
		this.lstCategorie = lstCategorie;
	}

	public ComboBoxRch getCbxCategorie() {
		return cbxCategorie;
	}

	public void setCbxCategorie(ComboBoxRch cbxCategorie) {
		this.cbxCategorie = cbxCategorie;
	}

	public String getFornituraDal() {
		return fornituraDal;
	}

	public void setFornituraDal(String dataFornituraDal) {
		this.fornituraDal = dataFornituraDal;
	}

	public String getFornituraAl() {
		return fornituraAl;
	}

	public void setFornituraAl(String dataFornituraAl) {
		this.fornituraAl = dataFornituraAl;
	}

	public CalendarBoxRch getTxtFornituraNonResDal() {
		return txtFornituraNonResDal;
	}

	public void setTxtFornituraNonResDal(CalendarBoxRch txtFornituraNonResDal) {
		this.txtFornituraNonResDal = txtFornituraNonResDal;
	}

	public CalendarBoxRch getTxtFornituraNonResAl() {
		return txtFornituraNonResAl;
	}

	public void setTxtFornituraNonResAl(CalendarBoxRch txtFornituraNonResAl) {
		this.txtFornituraNonResAl = txtFornituraNonResAl;
	}

	public Boolean getRdoDiagnosticaNonRes() {
		return rdoDiagnosticaNonRes;
	}

	public void setRdoDiagnosticaNonRes(Boolean rdoDiagnosticaNonRes) {
		this.rdoDiagnosticaNonRes = rdoDiagnosticaNonRes;
	}

	public Boolean getRdoDiagnosticaRes() {
		return rdoDiagnosticaRes;
	}

	public void setRdoDiagnosticaRes(Boolean rdoDiagnosticaRes) {
		this.rdoDiagnosticaRes = rdoDiagnosticaRes;
	}

	public String getCurrentDiagno() {
		return currentDiagno;
	}

	public void setCurrentDiagno(String currentDiagno) {
		this.currentDiagno = currentDiagno;
	}

	public Boolean getNewSearch() {
		return newSearch;
	}

	public void setNewSearch(Boolean xlsLinkato) {
		this.newSearch = xlsLinkato;
	}

}
