package it.webred.cs.csa.web.manbean.report;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.utils.JasperUtils;
import it.webred.cs.csa.web.manbean.report.dto.BasePdfDTO;
import it.webred.cs.csa.web.manbean.report.dto.HeaderPdfDTO;
import it.webred.cs.csa.web.manbean.report.dto.ReportPdfDTO;
import it.webred.cs.csa.web.manbean.report.filler.ReportCartellaFiller;
import it.webred.cs.csa.web.manbean.report.filler.ReportFoglioAmmFiller;
import it.webred.cs.csa.web.manbean.scheda.SchedaBean;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ejb.utility.ClientUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class ReportBean extends CsUiCompBaseBean {
	
	//tipi report
	private static final String REPORT_CARTELLA = "ReportCartella";
	private String tipoReport;
	
	//tipi subreport cartella
	private static final String ANAGRAFICA = "Anagrafica";
	private static final String PARENTI = "Parenti";
	private static final String DATISOCIALI = "Dati sociali";
	private static final String INVALIDITA = "Invalidita";
	private static final String DISABILITA = "Disabilita";
	private static final String TRIBUNALE = "Tribunale";
	private static final String OPERATORI = "Operatori";
	private static final String NOTE = "Note";
	
	private String modalWidgetVar = "wdgOpzioniStampaModal";
	private String modalId = "idOpzioniStampa";
	private CsASoggetto soggetto;
	private String denominazione;
	
	@ManagedProperty(value="#{schedaBean}")
	private SchedaBean schedaBean;
	
	private List<String> lstSubreportObbl;
	private List<String> lstSubreport;
	private List<String> lstReportOpz;
	private String[] selectedSubreport;
	private String[] selectedReportOpz;
	
	public void initializeStampaCartella(CsASoggetto cssoggetto) {
		if(cssoggetto != null) {
			
			tipoReport = REPORT_CARTELLA;
			soggetto = cssoggetto;
			denominazione = soggetto.getCsAAnagrafica().getCognome() + "_" + soggetto.getCsAAnagrafica().getNome();
			schedaBean.initialize(soggetto);
			
			lstSubreportObbl = new ArrayList<String>();
			lstSubreportObbl.add(ANAGRAFICA);
			
			lstSubreport = new ArrayList<String>();
			if(schedaBean.isRenderTabParenti())
				lstSubreport.add(PARENTI);
			if(schedaBean.isRenderTabDatiSociali())
				lstSubreport.add(DATISOCIALI);
			if(schedaBean.isRenderTabInvalidita())
				lstSubreport.add(INVALIDITA);
			if(schedaBean.isRenderTabDisabilita())
				lstSubreport.add(DISABILITA);
			if(schedaBean.isRenderTabTribunale())
				lstSubreport.add(TRIBUNALE);
			if(schedaBean.isRenderTabOperatori())
				lstSubreport.add(OPERATORI);
			if(schedaBean.isRenderTabNote())
				lstSubreport.add(NOTE);
			
			selectedSubreport = lstSubreport.toArray(new String[lstSubreport.size()]);
			lstReportOpz = new ArrayList<String>();
			lstReportOpz.add("Includi dati storici");		
			
			RequestContext.getCurrentInstance().execute(modalWidgetVar + ".show()");
			RequestContext.getCurrentInstance().update(modalId);
		} else addWarningFromProperties("seleziona.warning");
	}
	
	public void creaStampa() {
		
		if(REPORT_CARTELLA.equals(tipoReport))
			esportaCartella();
	}
	
	private void esportaCartella() {
		
		try { 
			
			ReportCartellaFiller filler = new ReportCartellaFiller();
			filler.setSchedaBean(schedaBean);
			filler.setSoggetto(soggetto);
			
			Map<String, String> mapSubreport = new HashMap<String, String>();
			for(int i = 0; i < selectedSubreport.length; i++) 
				mapSubreport.put(selectedSubreport[i], selectedSubreport[i]);
			
			//path report e subreport
			String reportPath =	getSession().getServletContext().getRealPath("/report");
			List<String> listaSubreport = new ArrayList<String>();
			listaSubreport.add(reportPath + "/subreport/cartella/anagrafica.jrxml");
			
			//parametri subreport
			HashMap<String, Object> map = new HashMap<String, Object>();
			logger.info(String.format("Crea  Mappa di parametri per i Jasper Reports"));
			map.put("anagrafica", new JRBeanCollectionDataSource(Arrays.asList(filler.fillAnagrafica())));
						
			if(mapSubreport.containsKey(PARENTI)) {
				listaSubreport.add(reportPath + "/subreport/cartella/parenti.jrxml");
				map.put("parenti", new JRBeanCollectionDataSource(filler.fillParenti()));
			}
			
			if(mapSubreport.containsKey(DATISOCIALI)) {
				listaSubreport.add(reportPath + "/subreport/cartella/datisociali.jrxml");
				map.put("datisociali", new JRBeanCollectionDataSource(Arrays.asList(filler.fillDatiSociali())));
			}
			
			if(mapSubreport.containsKey(INVALIDITA)) {
				listaSubreport.add(reportPath + "/subreport/cartella/invalidita.jrxml");
				map.put("invalidita", new JRBeanCollectionDataSource(Arrays.asList(filler.fillInvalidita())));
			}
			
			if(mapSubreport.containsKey(DISABILITA)) {
				listaSubreport.add(reportPath + "/subreport/cartella/disabilita.jrxml");
				map.put("disabilita", new JRBeanCollectionDataSource(Arrays.asList(filler.fillDisabilita())));
			}
			
			if(mapSubreport.containsKey(TRIBUNALE)) {
				listaSubreport.add(reportPath + "/subreport/cartella/tribunale.jrxml");
				map.put("tribunale", new JRBeanCollectionDataSource(Arrays.asList(filler.fillTribunale())));
			}
			
			if(mapSubreport.containsKey(OPERATORI)) {
				listaSubreport.add(reportPath + "/subreport/cartella/operatori.jrxml");
				map.put("operatori", new JRBeanCollectionDataSource(filler.fillOperatori()));
			}
			
			if(mapSubreport.containsKey(NOTE)) {
				listaSubreport.add(reportPath + "/subreport/cartella/note.jrxml");
				map.put("note", new JRBeanCollectionDataSource(Arrays.asList(filler.fillNote())));
			}
			
			fillCommonData(reportPath, listaSubreport, map);
			
			//parametri report
			ReportPdfDTO repPdf = new ReportPdfDTO();
			CsOOperatoreSettore opSett = getCurrentOpSettore();
			//repPdf.setPieDiPagina(getParametro(DataModelCostanti.ParamReport.SEZIONE, DataModelCostanti.ParamReport.PIEDIPAGINA));
			repPdf.setPieDiPagina(getCartellaPieDiPagina());
			
			JasperUtils jUtils = new JasperUtils();
			jUtils.esportaReport("Cartella_" + denominazione, reportPath + "/cartella.jrxml", listaSubreport, map, new JRBeanCollectionDataSource(Arrays.asList(repPdf)));
			
		} catch (Exception e) {
			addErrorFromProperties("report.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public void esportaFoglioAmm(Long idDiario, Long idIntervento) {
	
		try {
		
			soggetto = (CsASoggetto) getSession().getAttribute("soggetto");
			ReportFoglioAmmFiller filler = new ReportFoglioAmmFiller();
			filler.setDiarioId(idDiario);
			filler.setSoggetto(soggetto);

			denominazione = soggetto.getCsAAnagrafica().getCognome() + "_" + soggetto.getCsAAnagrafica().getNome();

			//path report e subreport
			String reportPath =	getSession().getServletContext().getRealPath("/report");
			List<String> listaSubreport = new ArrayList<String>();
			
			//parametri subreport
			HashMap<String, Object> map = new HashMap<String, Object>();
			logger.info(String.format("Crea  Mappa di parametri per i Jasper Reports"));
			
			listaSubreport.add(reportPath + "/subreport/foglioamm/tipoIntervento_" + filler.getIdTipoIntervento() + ".jrxml");
			BasePdfDTO tipoIntPdfDTO = filler.fillTipoIntervento();
			map.put("tipoIntervento", new JRBeanCollectionDataSource(Arrays.asList(tipoIntPdfDTO)));
			
			fillCommonData(reportPath, listaSubreport, map);
			
			JasperUtils jUtils = new JasperUtils();
			BasePdfDTO intPdfDTO = filler.fillIntervento();
			intPdfDTO.setShowCasellaContributo(tipoIntPdfDTO.getShowCasellaContributo());
			String tipoIntervento = filler.getIn().getCsRelSettCsocTipoInter().getCsRelCatsocTipoInter().getCsCTipoIntervento().getDescrizione();
			jUtils.esportaReport(tipoIntervento + "_" + denominazione, reportPath + "/foglioAmm.jrxml", listaSubreport, map, new JRBeanCollectionDataSource(Arrays.asList(intPdfDTO)));
			
		} catch (Exception e) {
			addErrorFromProperties("report.error");
			logger.error(e.getMessage(),e);
		}
		
	}
	
	private void fillCommonData(String reportPath, List<String> listaSubreport, HashMap<String, Object> map) {
		
		BaseDTO baseDto = new BaseDTO();
		fillEnte(baseDto);
		
		HeaderPdfDTO hPdf = new HeaderPdfDTO();
		CsOOperatoreSettore opSett = getCurrentOpSettore();
		
		//carico path dati
		ParameterService paramService = (ParameterService) getEjb("CT_Service","CT_Config_Manager" , "ParameterBaseService");
	    ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("dir.files.datiDiogene");
		criteria.setComune(null);
		criteria.setSection(null);
		
		AmKeyValueExt amKey = paramService.getAmKeyValueExt(criteria);
		if(amKey != null) {
			String dir =  amKey.getValueConf();
			String path = dir + baseDto.getEnteId() + this.dirLoghi;
			File logo = new File(path + "logo_bw.png");
			if(logo.exists())
				hPdf.setImagePath(path);
			else {
				logger.error("Attenzione: Il logo del report non è presente");
				hPdf.setImagePath(reportPath + "/logo/");
			}
		} else {
			logger.error("Attenzione: Il path per il recupero logo del report non è impostato");
			hPdf.setImagePath(reportPath + "/logo/");
		}
		
		hPdf.setNomeSettore(opSett.getCsOSettore().getNome());
		hPdf.setNomeUfficio(opSett.getCsOSettore().getNome2());
		hPdf.setNomeEnte(getNomeEnte());

		listaSubreport.add(reportPath + "/subreport/header.jrxml");
		map.put("header", new JRBeanCollectionDataSource(Arrays.asList(hPdf)));
		
	}

	private String getCartellaPieDiPagina() {
		String pie = "";
		
		CsOSettore sett = getCurrentOpSettore().getCsOSettore();
		pie += sett.getNome();
		if(sett.getNome2() != null)
			pie += (" - " + sett.getNome2());
		pie += "<br>";
		if(sett.getCsAAnaIndirizzo() != null) {
			pie += (sett.getCsAAnaIndirizzo().getIndirizzo() + " " + sett.getCsAAnaIndirizzo().getCivicoNumero());
			pie += (" - " + sett.getCsAAnaIndirizzo().getComDes());
			pie += "<br>";
		}
		if(sett.getOrario() != null) {
			pie += ("Orari: " + sett.getOrario());
			pie += "<br>";
		}
		if(sett.getEmail() != null) {
			pie += ("Email: " + sett.getEmail());
			pie += " ";
		}
		if(sett.getTel() != null)
			pie += ("Tel: " + sett.getTel());
		if(sett.getCell() != null)
			pie += (" - " + sett.getCell());
		return pie;
	}

	public SchedaBean getSchedaBean() {
		return schedaBean;
	}

	public void setSchedaBean(SchedaBean schedaBean) {
		this.schedaBean = schedaBean;
	}

	public List<String> getLstSubreportObbl() {
		return lstSubreportObbl;
	}

	public void setLstSubreportObbl(List<String> lstSubreportObbl) {
		this.lstSubreportObbl = lstSubreportObbl;
	}

	public List<String> getLstSubreport() {
		return lstSubreport;
	}

	public void setLstSubreport(List<String> lstSubreport) {
		this.lstSubreport = lstSubreport;
	}

	public List<String> getLstReportOpz() {
		return lstReportOpz;
	}

	public void setLstReportOpz(List<String> lstReportOpz) {
		this.lstReportOpz = lstReportOpz;
	}

	public String[] getSelectedSubreport() {
		return selectedSubreport;
	}

	public void setSelectedSubreport(String[] selectedSubreport) {
		this.selectedSubreport = selectedSubreport;
	}

	public String[] getSelectedReportOpz() {
		return selectedReportOpz;
	}

	public void setSelectedReportOpz(String[] selectedReportOpz) {
		this.selectedReportOpz = selectedReportOpz;
	}

	public String getModalWidgetVar() {
		return modalWidgetVar;
	}

	public String getModalId() {
		return modalId;
	}
	
}
