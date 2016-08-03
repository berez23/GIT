package it.webred.ct.service.tares.web.bean;

import java.util.LinkedList;


import java.util.List;

import javax.ejb.EJB;

import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.tares.data.access.StatisticheService;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.access.dto.DiagnosticheSearchCriteria;
import it.webred.ct.service.tares.data.access.dto.StatisticheSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarDia;
import it.webred.ct.service.tares.data.model.SetarStat;
import it.webred.ct.service.tares.web.bean.TaresBaseBean;
import it.webred.ct.service.tares.web.bean.export.ExportXLSBean;


import org.apache.log4j.Logger;

public class StatisticheBean extends TaresBaseBean {

	protected StatisticheService statisticheService = (StatisticheService) getEjb("Servizio_Tares", "Servizio_Tares_EJB", "StatisticheServiceBean");

	private List<SetarStat> lstStatistiche = null;
	private List<SetarDia> lstDiagnostiche = null;
	private StatisticheSearchCriteria criteriaStat = null;
	private DiagnosticheSearchCriteria criteriaDiag = null;
	
	private Long statNum = new Long(0);

	public void init(){
		lstStatistiche = new LinkedList<SetarStat>();
		lstDiagnostiche = new LinkedList<SetarDia>();
		criteriaStat = new StatisticheSearchCriteria();
		criteriaDiag = new DiagnosticheSearchCriteria();
		
		statNum = new Long(0);
	}//-------------------------------------------------------------------------
	
	public String goDiagnosticheElab() {
		logger.info(StatisticheBean.class + ".goDiagnosticheElab");
		
		init();
		
		doCercaDiagnostiche();
		
		return "statistiche.diagnosticheElab";
	}// ------------------------------------------------------------------------
	
	public void doCercaDiagnostiche() {
		logger.info(StatisticheBean.class + ".doCercaDiagnostiche");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCriteriaDiag(criteriaDiag);
		
		lstDiagnostiche = statisticheService.getDiagnostiche(dataIn);
		
	}// ------------------------------------------------------------------------

	public String goStatistichePerCategoria() {
		logger.info(StatisticheBean.class + ".goStatistichePerCategoria");
		
		init();
		
		doCercaStatistiche();
		
		return "statistiche.statistichePerCategoria";
	}// ------------------------------------------------------------------------	

	public void doCercaStatistiche() {
		logger.info(StatisticheBean.class + ".doCercaStatistiche");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCriteriaStat(criteriaStat);
		
		lstStatistiche = statisticheService.getStatistiche(dataIn);
		
	}// ------------------------------------------------------------------------
	
	public String goXls() {
		logger.info(StatisticheBean.class + ".goXls");

		StatisticheSearchCriteria criteriaXls = new StatisticheSearchCriteria();
		criteriaXls.setCategoria(criteriaStat.getCategoria());
		criteriaXls.setCategoriaNo(criteriaStat.getCategoriaNo());

		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCriteriaStat(criteriaXls);
		
		/*
		 * Recupero il path per scrivere il file temporaneamente 
		 */
		ParameterSearchCriteria paramCriteria = new ParameterSearchCriteria();
		paramCriteria.setKey("dir.files.dati");
		String pathXLS = doGetListaKeyValue(paramCriteria);
		
		/*
		 * Unica query
		 */
		try{
		
			List<SetarStat> lstSta = statisticheService.getStatistiche(dataIn);
			ExportXLSBean xls = new ExportXLSBean();
			xls.setPathXLS(pathXLS);
			xls.setListaStat(lstSta);
			/*
			 * Faccio iniziare dalla riga 1 non dalla zero perché ci sono 
			 * le intestazioni
			 */
			xls.setRowStart(1);
			xls.resultListTaresExportToXls("STAT");
			
			if (lstSta != null)
				statNum = new Long(lstSta.size());
			
			logger.info("Statistiche esportate: " + lstSta.size());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return "statistiche.xls";
	}// ------------------------------------------------------------------------
	
	public String doXls() {
		logger.info(StatisticheBean.class + ".doXls");

		DiagnosticheSearchCriteria criteriaXLS = new DiagnosticheSearchCriteria();
//		criteriaXls.setCategoria(criteriaDiag.getCategoria());
//		criteriaXls.setCategoriaNo(criteriaDiag.getCategoriaNo());

		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCriteriaDiag(criteriaXLS);
		
		/*
		 * Recupero il path per scrivere il file temporaneamente 
		 */
		ParameterSearchCriteria paramCriteria = new ParameterSearchCriteria();
		paramCriteria.setKey("dir.files.dati");
		String pathXLS = doGetListaKeyValue(paramCriteria);
		
		/*
		 * Unica query
		 */
		try{
		
			List<SetarDia> lstDia = statisticheService.getDiagnostiche(dataIn);
			ExportXLSBean xls = new ExportXLSBean();
			xls.setPathXLS(pathXLS);
			xls.setListaDia(lstDia);
			/*
			 * Faccio iniziare dalla riga 1 non dalla zero perché ci sono 
			 * le intestazioni
			 */
			xls.setRowStart(1);
			xls.resultListTaresExportToXls("DIA");
			
			if (lstDia != null)
				statNum = new Long(lstDia.size());
			
			logger.info("Diagnostiche esportate: " + lstDia.size());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return "diagnostiche.xls";
	}// ------------------------------------------------------------------------

	public List<SetarStat> getLstStatistiche() {
		return lstStatistiche;
	}

	public void setLstStatistiche(List<SetarStat> lstStatistiche) {
		this.lstStatistiche = lstStatistiche;
	}

	public StatisticheSearchCriteria getCriteriaStat() {
		return criteriaStat;
	}

	public void setCriteriaStat(StatisticheSearchCriteria criteriaStat) {
		this.criteriaStat = criteriaStat;
	}

	public Long getStatNum() {
		return statNum;
	}

	public void setStatNum(Long statNum) {
		this.statNum = statNum;
	}

	public List<SetarDia> getLstDiagnostiche() {
		return lstDiagnostiche;
	}

	public void setLstDiagnostiche(List<SetarDia> lstDiagnostiche) {
		this.lstDiagnostiche = lstDiagnostiche;
	}

	public DiagnosticheSearchCriteria getCriteriaDiag() {
		return criteriaDiag;
	}

	public void setCriteriaDiag(DiagnosticheSearchCriteria criteriaDiag) {
		this.criteriaDiag = criteriaDiag;
	}

}
