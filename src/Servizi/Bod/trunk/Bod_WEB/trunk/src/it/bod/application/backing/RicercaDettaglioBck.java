package it.bod.application.backing;

import it.bod.application.beans.BodAllegatiBean;


import it.bod.application.beans.BodBean;
import it.bod.application.beans.BodFabbricatoBean;
import it.bod.application.beans.BodFornituraBean;
import it.bod.application.beans.BodFornituraDocfaBean;
import it.bod.application.beans.BodIstruttoriaBean;
import it.bod.application.beans.BodModello662Bean;
import it.bod.application.beans.BodModello80Bean;
import it.bod.application.beans.BodSegnalazioneBean;
import it.bod.application.beans.BodUiuBean;
import it.bod.application.beans.BodUploadBean;
import it.bod.application.beans.ClassamentoCorpoFabbricaBean;
import it.bod.application.beans.Comma340Bean;
import it.bod.application.beans.ConcessioneBean;
import it.bod.application.beans.ControlloIncrociatoBean;
import it.bod.application.beans.EnteBean;
import it.bod.application.beans.GraffatoBean;
import it.bod.application.beans.PdfDocfaBean;
import it.bod.application.beans.PlanimetriaBean;
import it.bod.application.beans.StreetViewBean;
import it.bod.application.beans.TrasformazioneEdiliziaBean;
import it.bod.application.beans.VariazioneCensuaria;
import it.bod.application.beans.VerificaCatastoBean;
import it.bod.application.beans.VirtualEarthBean;
import it.bod.application.beans.switches.BodIstruttoriaSwitch;
import it.bod.application.beans.switches.BodSegnalazioneSwitch;
import it.bod.application.beans.switches.BodUploadSwitch;
import it.bod.application.common.FilterItem;
import it.bod.application.common.Info;
import it.bod.application.common.MasterClass;
import it.bod.application.common.Schiavo;
import it.bod.application.environment.BaseEnvironment;
import it.bod.business.service.BodLogicService;
import it.doro.tools.Character;
import it.doro.tools.Number;
import it.doro.tools.TimeControl;
import it.persistance.common.SqlHandler;
import it.webred.ct.data.access.aggregator.elaborazioni.ElaborazioniService;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.*;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.*;
import it.webred.ct.data.model.docfa.*;
import it.webred.permessi.GestionePermessi;
import it.webred.rich.common.CalendarBoxRch;
import it.webred.rich.common.ComboBoxRch;
import it.webred.util.common.ApplicationResources;
import it.webred.util.common.TiffUtill;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.model.types.LSTFAbstractType;
import org.apache.poi.hwpf.usermodel.CharacterProperties;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.richfaces.component.html.HtmlInputTextarea;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
//import demo.MediaData;

public class RicercaDettaglioBck  extends MasterClass{

	private static final long serialVersionUID = -4466513155832868009L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.RicercaDettaglioBck");
	
	
	
	private BodBean selBodBean = null;
	private ConcessioneBean selConcessione = null;
	//private ControlloClassamentoConsistenzaBean selClaCom = null;
	private ControlloClassamentoConsistenzaDTO selClaCom = null;
	private ClassamentoCorpoFabbricaBean selClaCorpoFabbrica = null;
	private Object[] selClaSub = null;
	private List<BodBean> lstDatGen = null;
	private PdfDocfaBean pdfDocfa = null; 
	private List<DocfaDatiGenerali> lstDatGenHelp = null;
	private List<DocfaIntestati> lstIntestati = null;
	private List<Object> lstDichiaranti = null;
	private List<DocfaInParteUnoDTO> lstParteUno = null;
	
	private List<ControlloIncrociatoBean> lstControlliIncrociati = null;
	private List<BeniNonCensDTO> lstBeniNonCensibili = null;
	private List<String> lstAnnotazioni = null;
	
	private List<Object> lstUiu = null;
	private List<Object> lstUiuAllProt = null;
	private List<Object> lstDatiCensuari = null;
	private Object[] selDatCen = null;
	private List<Object> lstSuperficiPerVano = null;
	
	private String tempPathDocC = "";
	private String tempPathDocB = "";
	private String tempFileDocC = "";
	//private String tempFileDocB = "";
	private List<PlanimetriaBean> lstImagesFiles = null;
	private List<PlanimetriaBean> lstImagesFilesDocfa = null;
	private List<PlanimetriaBean> lstImagesFilesC340 = null;

	//	private MediaData mediaData = null;
	private PlanimetriaBean currentPla = null;
	private String currentOperator = "";
	private VirtualEarthBean currentVirtualEarth = null;
	private StreetViewBean currentStreetView = null;
	private List<OperatoreDTO> lstOperatori =  null;
	
	private List<ControlloClassamentoConsistenzaDTO> lstClaCom = null;
	private List<ControlloClassamentoConsistenzaDTO> selLstClaZcDiverse = null;
	
	private List<ControlloClassamentoConsistenzaDTO> lstControlliClassCons = null;
	
	private List<ClassamentoCorpoFabbricaBean> lstClassCorpiFabbrica = null;
	private HtmlInputText txtRapporto = null;
	private List<Object> lstClaCorFab = null;
	private List<TrasformazioneEdiliziaBean> lstTrasfEdiliziaVariazioni = null;
	private List<TrasformazioneEdiliziaBean> lstTrasfEdiliziaSoppressioni = null;
	private List<TrasformazioneEdiliziaBean> lstTrasfEdiliziaCostituzioni = null;
	private Double totRenPerCoefSop = 0d;
	private Double totRenPerCoefCos = 0d;
	private String pdf = "";
	private String nomeAllegato = "";
	private String nomeAllegatoB = "";
	private List<Object> lstAscensori = null;
	private List<Object> lstBagni = null;
	private HtmlInputTextarea txtAnnotazioni = null;
	private HtmlSelectManyCheckbox cbxEsitiIstruttoria = null;
	private BodLogicService bodLogicService = null;

	private BodIstruttoriaBean bodIstruttoria = null;
	private BodIstruttoriaSwitch bodIstruttoriaSwitch = null;
	private HtmlSelectOneRadio rdoCoerente = null;
	private HtmlSelectOneRadio rdoIncoerente = null;
	private HtmlSelectOneRadio rdoAssenza = null;
	private ComboBoxRch cbxCoerenteComDoc = null;
	private BodSegnalazioneBean bodSegnalazione = null;
	private BodAllegatiBean bodAllegato = null;
	private Hashtable htIstruttoriaFabbricati = null;
	private List<BodFabbricatoBean> lstIstruttoriaFab = null;
	private List<BodUiuBean> lstIstruttoriaUiu = null;
	private CalendarBoxRch calDataFineLavori = null;
	private BodSegnalazioneSwitch bodSegnalazioneSwitch = null;
	private BodUploadBean bodUpload = null;
	private BodUploadBean bodUploadB = null;
	private BodUploadSwitch bodUploadSwitch = null;
	
	private TimeControl tc = new TimeControl();
	private Boolean isAutorizzatoRicerca = false;

	
	private String esitoControlloCoerente = null;
	private String esitoControlloComDoc = null;
	private String esitoControlloIncoerente = null;

	private String esitoControlloAssenza = null;	
	
	
	//campi pannello precompilazione modello B
	private Boolean flgAscensore=false;
	private Boolean flgResidenzialeMinore8=false; 
	private Boolean flgResidenzialeMaggiore8=false; 
	private String catResidenzialeMin8=null;
	private String catResidenzialeMagg8=null;
	private String classeResidenzialeMin8=null;
	private String classeResidenzialeMagg8=null;
	private Boolean flgUfficio=false; 
	private Boolean flgNegozio=false; 
	private Boolean flgLaboratorio=false; 
	private Boolean flgDeposito=false; 
	private Boolean flgBox=false; 
	private Boolean flgPauto=false; 
	private String classeUfficio=null;
	private String classeNegozio=null;
	private String classeLaboratorio=null;
	private String classeDeposito=null;
	private String classeBox=null;
	private String classePauto=null;
	private Boolean flgAllineamento=false; 
	
	//campi pannello precompilazione modello C
	
	private String oggettoDomanda=null;
	private String tipoIntervento=null;
	private String oggetto=null;
	private String numProgressivo=null;
	private String numProtocollo=null;
	private String note=null;
	private String settore=null;
	private String destinazioneZona=null;
	private String significativaPresenza=null;
	private String destinazioneUiu=null;
	private String categoriaProposta=null;
	private String classeProposta=null;
	private Boolean flgRichiesta662=false;
	private Boolean flgAllineamentoUiu=false; 
	private Boolean flgNonApplicabile336=false; 
	private Boolean flgAscensoreC=false; 
	private List<ConcessioneBean> concessioniLst = null;
	
	private String pathAllegati="";
	private String pathAllegatiSegnalazioni="";
	
	public static final String emptyComboItem = "";
	private String minimoAnniFornitura="";
	private String maxAnniFornitura="";
	
	private Boolean flgVisualizzaAllegato=false;
	private Boolean flgVisualizzaAllegatoC=false;
	
	private String msgPraticaStatus = "";
	private static final String MSG_PRATICA_NON_ESAMINATA = "Pratica NON ESAMINATA ";
	private static final String MSG_PRATICA_PRESA_IN_CARICO = "Pratica PRESA IN CARICO ";
	private static final String MSG_PRATICA_CHIUSA = "Pratica CHIUSA ";

	
	private String codiceEnte;
	private String descrizioneEnte;
	
	
	
	public RicercaDettaglioBck() {
		super.initializer();
		
		init();
	}//-------------------------------------------------------------------------
	
	public void init(){
		cbxCoerenteComDoc = new ComboBoxRch();
		rdoCoerente = new HtmlSelectOneRadio();
		rdoIncoerente = new HtmlSelectOneRadio();
		rdoAssenza = new HtmlSelectOneRadio();
		esitoControlloCoerente = "";
		esitoControlloComDoc = "";
		esitoControlloIncoerente = "";
		esitoControlloAssenza = "";
		
		
		/*
		 * Imposto la sessione e creo la cartella per contenere l'allegato scaricabile
		 * dal tab istruttoria
		 */
//		FacesContext context = FacesContext.getCurrentInstance();
//		ExternalContext extContext = context.getExternalContext();
//		req = (HttpServletRequest)extContext.getRequest();
//		res = (HttpServletResponse)extContext.getResponse();
//		jsfSession = (HttpSession)req.getSession(false);
//		sessionId = jsfSession.getId();
		
	}//-------------------------------------------------------------------------
	
	public StreetViewBean getCurrentStreetView() {
		return currentStreetView;
	}

	public void setCurrentStreetView(StreetViewBean currentStreetView) {
		this.currentStreetView = currentStreetView;
	}

	
	protected String getRootPathEnte(){
		
		String pathDatiDiogeneEnte = super.getPathDatiDiogene()+ this.getEnteCorrente().toUpperCase()+ "/";
		return pathDatiDiogeneEnte;
	}
	
	private String getEnteCorrente(){
	
		/*	Properties prop =  SqlHandler.loadProperties(propName);
			String sql = prop.getProperty("qryCodiceEnte");
			logger.info("SQL: " + sql);
			Hashtable htQry = new Hashtable();
			htQry.put("queryString", sql);
			List<Object> lstEnte = bodLogicService.getList(htQry);
			Object[] enteInfos = (Object[])lstEnte.get(0);
			codiceEnte = Character.checkNullString((String)enteInfos[0]);*/
			
		codiceEnte = super.getEnte().getBelfiore();
		
		return codiceEnte;
		
	}
	
	public String getPathTemplateBod() {
		
		if (pathTemplateBod==null)
			pathTemplateBod = this.getRootPathEnte() + super.dirTemplateBod;
		
		logger.info("PATH TEMPLATE BOD: " + pathTemplateBod);
		return pathTemplateBod;
	}
	
	
	public String getTempPathDocC() {
		logger.info("TempPathDocC: " + tempPathDocC);
		return tempPathDocC;
	}

	public void setTempPathDocC(String pathDoc) {
		this.tempPathDocC = pathDoc;
	}

	public BodIstruttoriaBean getBodIstruttoria() {
		return bodIstruttoria;
	}

	public void setBodIstruttoria(BodIstruttoriaBean bodIstruttoria) {
		this.bodIstruttoria = bodIstruttoria;
	}
	
	public String selectionConcessione() {
		String esito = "ricercaDettaglioBck.selectionConcessione";
		
		if (selConcessione != null){
			this.numProgressivo = selConcessione.getProgressivo();
			this.tipoIntervento = selConcessione.getTipoIntervento();
			this.oggetto = selConcessione.getOggetto();
			this.numProtocollo = selConcessione.getProtocollo();
		}else{
			esito = "failure";
		}
		
		return esito;
	}//-------------------------------------------------------------------------

	public String selectionChanged() {
		String esito = "ricercaDettaglioBck.selectionChanged";
		
		super.initializer();
		
	if (minimoAnniFornitura== null || minimoAnniFornitura.equals("")){
			
			Properties prop = SqlHandler.loadProperties(propName);
			String sql = prop.getProperty("qryMinimoAnniFornitura");
			Hashtable<String,String> htQry = new Hashtable<String,String>();
			htQry.put("queryString", sql);
			List<Object> lstAnni = bodLogicService.getList(htQry);
			if (lstAnni != null && lstAnni.size()>0){
				minimoAnniFornitura= (String)lstAnni.get(0);
			}
		}
		
		if (maxAnniFornitura== null || maxAnniFornitura.equals("")){
					
					Properties prop = SqlHandler.loadProperties(propName);
					String sql = prop.getProperty("qryMassimoAnniFornitura");
					Hashtable<String,String> htQry = new Hashtable<String,String>();
					htQry.put("queryString", sql);
					List<Object> lstAnni = bodLogicService.getList(htQry);
					if (lstAnni != null && lstAnni.size()>0){
						maxAnniFornitura= (String)lstAnni.get(0);
					}
				}
		
		isAutorizzatoRicerca = GestionePermessi.autorizzato(utente, nomeApplicazione, "Ricerca Bod", "Accertato");
		if (isAutorizzatoRicerca){
			Properties prop = SqlHandler.loadProperties(propName);
			/*
			 * Recupero il codice ente
			 */
			String sql = null;
			Hashtable htQry = new Hashtable();
			EnteBean ente = super.getEnte();
			
			codiceEnte = Character.checkNullString( (String)ente.getBelfiore() );
			descrizioneEnte = Character.checkNullString( (String)ente.getDescrizione() );
			/*
			 * Dati Generali 
			 */
			
			//String condizioniBnc = "";
			String condizioniDatCen = "";
			String condizioniOpe = "";
			
			ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
			this.lstDatGen = new ArrayList<BodBean>();
			this.lstDatGenHelp = new ArrayList<DocfaDatiGenerali>();
			
			TimeControl tc = new TimeControl();
			if (selBodBean != null){
				this.lstDatGen.add(selBodBean);
				String[] mmAaaa = selBodBean.getFornitura().split("/");
				FilterItem fi = new FilterItem();
				fi.setAttributo("protocollo_reg");
				fi.setOperatore("=");
				fi.setParametro("protocollo_reg");
				fi.setValore(selBodBean.getProtocollo());
				aryFilters.add(fi);
				
				//condizioniBnc += "and bnc.protocollo_reg = '" + selBodBean.getProtocollo() + "' ";
				condizioniDatCen += "and cen.protocollo_registrazione = '" + selBodBean.getProtocollo() + "' ";
				condizioniOpe += "and ope.protocollo = '" + selBodBean.getProtocollo() + "' ";
				/*
				 * La fornitura in questa fase ha il seguente formato MM/yyyy,
				 * per fare la query hibernate ho invece bisogno di fornitura dal - al 
				 */
				fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_DAL_");
				fi.setParametro("fornitura_DAL");
				Timestamp dataFornituraDal = tc.fromFormatStringToTimestampMorning("01/" + mmAaaa[0] + "/" + mmAaaa[1]);
				fi.setValore(dataFornituraDal);
				aryFilters.add(fi);
				
				fi = new FilterItem();
				fi.setAttributo("fornitura");
				fi.setOperatore("_AL_");
				fi.setParametro("fornitura_AL");
				Timestamp dataFornituraAl = tc.fromFormatStringToTimestampEvening("01/" + mmAaaa[0] + "/" + mmAaaa[1]);
				dataFornituraAl = tc.getLastOfMonth(dataFornituraAl);
				fi.setValore(dataFornituraAl);
				aryFilters.add(fi);
		
				//condizioniBnc += "and bnc.fornitura =  to_date('" + selBodBean.getFornitura() + "','MM/yyyy') ";
				condizioniDatCen += "and cen.fornitura =  to_date('" + selBodBean.getFornitura() + "','MM/yyyy') ";
				condizioniOpe += "and ope.anno =  to_char(to_date('" + selBodBean.getFornitura() + "','MM/yyyy'), 'yyyy') ";
				
				/*
				 * Recupero l'istruttoria se esistente cosi da impostare lo status
				 * della pratica in alto sul dettaglio 
				 */
				htQry = new Hashtable();
				List<FilterItem> filters = new ArrayList<FilterItem>();
				FilterItem filter = new FilterItem();
				filter.setAttributo("protocollo");
				filter.setOperatore("=");
				filter.setParametro("protocollo");
				filter.setValore(selBodBean.getProtocollo());
				filters.add(filter);
				
				filter = new FilterItem();
				filter.setAttributo("fornitura");
				filter.setOperatore("_IL_");
				filter.setParametro("fornitura");
				filter.setValore(tc.fromFormatStringToDate("01/" + selBodBean.getFornitura(), "00:00"));
				filters.add(filter);
				
				htQry.put("where", filters);
				
				ArrayList<BodIstruttoriaBean> lst = (ArrayList<BodIstruttoriaBean>)bodLogicService.getList(htQry, BodIstruttoriaBean.class);
				
				if (lst != null && lst.size() >0){
					bodIstruttoria = (BodIstruttoriaBean)lst.get(0);
					if (bodIstruttoria == null){
						//bodIstruttoria = new BodIstruttoriaBean();
						/*
						 * Pratica non esaminata
						 */
						msgPraticaStatus = MSG_PRATICA_NON_ESAMINATA;
					}else{
						if (bodIstruttoria.getPresaInCarico()){
							/*
							 * Pratica presa in carico
							 */
							msgPraticaStatus = MSG_PRATICA_PRESA_IN_CARICO;
						}
						if (bodIstruttoria.getChiusa()){
							/*
							 * Pratica Chiusa
							 */
							msgPraticaStatus = MSG_PRATICA_CHIUSA;
						}
					}
				}else{
					/*
					 * Pratica non esaminata
					 */
					msgPraticaStatus = MSG_PRATICA_NON_ESAMINATA;					
				}
				
			}

			htQry = new Hashtable<String,String>();
	
			/*
			 * Lista Link PDF
			 */
			String[] fornitura = selBodBean.getFornitura().split("/");
			String nomeFile = selBodBean.getProtocollo() + ".pdf";
			String pathPdf = this.getRootPathEnte() + "docfa_pdf/" +  fornitura[1] + fornitura[0] + "/" + nomeFile;
			File filePdf = new File(pathPdf);
			pdfDocfa = new PdfDocfaBean();
			/*
			 * il protocollo file:// (con due slash) è volontario per farlo funzionare
			 * in ie7
			 */
			pdfDocfa.setLink(pathPdf);
			pdfDocfa.setNomeFile(nomeFile);
			pdfDocfa.setEsiste(filePdf.exists());

			Context cont;
			try {
				cont = new InitialContext();
			
				DocfaService docfaService= (DocfaService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
				
				SimpleDateFormat MMyyyy = new SimpleDateFormat("MM/yyyy");
				
				RicercaOggettoDocfaDTO rd = new RicercaOggettoDocfaDTO();
				rd.setEnteId(codiceEnte);
				rd.setUserId(this.getUtente().getName());
				rd.setProtocollo(selBodBean.getProtocollo());
				rd.setFornitura(MMyyyy.parse(selBodBean.getFornitura()));
				
				logger.info("Ricerca dati Docfa da CT_SERVICE...");
				DettaglioDocfaDTO dto = docfaService.getDettaglioDocfa(rd);
				
				//Lista Dati Generali
				lstDatGenHelp = docfaService.getDocfaDatiGenerali(rd);
				logger.info("Dati Generali ["+lstDatGenHelp.size()+"]");
				
				//Lista Annotazioni
				String annotazioni = dto.getsAnnotazioni();
				lstAnnotazioni = new ArrayList<String>();
				lstAnnotazioni.add(annotazioni);
				logger.info("Annotazioni ["+lstAnnotazioni.size()+"]");
				
				/*
				 * Lista Intestati
				 */
				lstIntestati = dto.getIntestati();
				logger.info("Lista Intestati ["+lstIntestati.size()+"]");
				
				/*
				 * Lista Dichiaranti
				 */
				
			    List<DocfaDichiaranti> lstDichiarantiDTO = dto.getDichiaranti();
			
				lstDichiaranti = new ArrayList<Object>();
				
				for (DocfaDichiaranti dich : lstDichiarantiDTO){
					
					Object[] objArr = new Object[5];
					
					objArr[0] = dich.getDicCognome();
					objArr[1] = dich.getDicNome();
					objArr[2] = dich.getDicResCom();
					
					String indirizzo= dich.getDicResIndir();
					String indirizzoNew= Character.pulisciVia(indirizzo);
					objArr[3]= indirizzoNew;
					
					String civico= dich.getDicResNumciv();
					String civicoNew="";
					
					if (civico != null){
	            		try{
	            			Integer civicoInt= Integer.valueOf(civico);
	            			civicoNew= String.valueOf(civicoInt);
	            		}catch(Exception e){
	            			civicoNew= civico;
	            		}
	        		}
					objArr[4]=civicoNew;
					
					lstDichiaranti.add(objArr);
				}
				logger.info("Lista Dichiaranti ["+lstDichiaranti.size()+"]");
				
				//Lista Uiu
				List<DocfaUiu> lstUiuDTO = dto.getListaUiu();
				lstUiu = new ArrayList<Object>();
					for (DocfaUiu uiu : lstUiuDTO){
						Object[] objArr= new Object[8];
						
						objArr[0] = uiu.getTipoOperazione();
						objArr[1] = uiu.getFoglio();
						objArr[2] = uiu.getNumero();
						objArr[3] = uiu.getSubalterno();
						
						String via= uiu.getIndirToponimo()!=null ? uiu.getIndirToponimo().trim() : null;
						
						objArr[4] = via+" "+uiu.getIndirNcivUno();
						
						String viaNew= Character.pulisciVia(via);
						objArr[5]= viaNew;
						
						String civico= uiu.getIndirNcivUno();
						String civicoNew="";
						
						if (civico != null){
		            		try{
		            			Integer civicoInt= Integer.valueOf(civico);
		            			civicoNew= String.valueOf(civicoInt);
		            		}catch(Exception e){
		            			civicoNew= civico;
		            		}
		        		}
						objArr[6]=civicoNew;
						
						String sezione = uiu.getSezione();
						objArr[7]=sezione;
						
						lstUiu.add(objArr);
					}
					logger.info("Lista Uiu ["+lstUiu.size()+"]");
					
					//Lista Parte Uno
					lstParteUno = docfaService.getListaDocfaInParteUno(rd);
					logger.info("Lista Parte Uno ["+lstParteUno.size()+"]");
					
					//Lista Beni non censibili
					lstBeniNonCensibili = docfaService.getListaBeniNonCensibili(rd);
					logger.info("Lista Beni Non Censibili ["+lstBeniNonCensibili.size()+"]");
					
					//ListaOperatori
					lstOperatori = docfaService.getListaOperatori(rd);
					logger.info("Lista Operatori ["+lstOperatori.size()+"]");
					
				
			} catch (Exception e1) {
				logger.error("Errore Recupero Dettaglio Docfa: " + e1.getMessage(),e1);
				
			}
		
			/*
			 * Lista Dati Censuari
			 */
//			sql = "SELECT DISTINCT " +
//				"cen.foglio fog, cen.numero num, cen.subalterno  sub, cen.zona zona, " +
//				"cen.classe cls, cen.categoria   cat, cen.consistenza cons, " +
//				"cen.superficie  sup, cen.rendita_euro rendita_eu, cen.data_registrazione data_registrazione, " +
//				"cen.identificativo_immobile identificativo_immobile, nvl(cen.presenza_graffati,'-') presenza_graffati " +
//				"FROM " +
//				"docfa_dati_censuari cen " +
//				"LEFT JOIN docfa_dati_metrici  met ON cen.identificativo_immobile = met.identificativo_immobile " +
//				"WHERE 1=1 ";
			
			sql = prop.getProperty("qryRicercaDettaglioDatiCensuari");
			sql += condizioniDatCen + " and to_char(to_date(cen.data_registrazione, 'yyyymmdd'),'yyyymm') = to_char(cen.fornitura,'yyyymm') order by fog, num, sub ";

			htQry = new Hashtable();
			htQry.put("queryString", sql);
			lstDatiCensuari = bodLogicService.getList(htQry);
	
		}else{
			esito = "failure";
		}
		
		return esito;
    }//-------------------------------------------------------------------------
	
	public String getBodBeanPrecedente(){
		String esito="ricercaDettaglioBck.getBodBeanPrecedente";
		
		String protocolloBean= selBodBean.getProtocollo();
		String fornituraBean= selBodBean.getFornitura();
		
		String meseFornitura= "";
		String annoFornitura="";
		if (fornituraBean != null){
			String[] arrFornitura= fornituraBean.split("/");
			meseFornitura=arrFornitura[0];
			annoFornitura=arrFornitura[1];
		}
		
		String protocollo= "";
		String fornitura= "";
		
		Properties prop = SqlHandler.loadProperties(propName);
		
		//verifico se esiste un altro Bean all'interno della stessa fornitura con protocollo minore
		String sql = prop.getProperty("qryCercaPrecSucc");
		sql += " WHERE protocollo_reg < '"+ protocolloBean+"' AND to_char(fornitura, 'mm')= '"+ meseFornitura+ "' and to_char(fornitura, 'yyyy')='"+ annoFornitura+"'  order by protocollo_reg desc";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List listaPrecedentiFornitura = bodLogicService.getList(htQry);
		
		if (listaPrecedentiFornitura != null && listaPrecedentiFornitura.size()>0){
			//prendo il primo della lista
			Object[] precFornitura= (Object[])listaPrecedentiFornitura.get(0);
			protocollo= (String)precFornitura[0]; 
			fornitura= (String)precFornitura[1];
			
			
			
			
		} else {
			// se non esiste un altro Bean all'interno della stessa fornitura con protocollo minore, allora verifico nella fornitura precedente finchà non ne trovo uno
			
			boolean trovato= false;
			
				while(trovato== false && Integer.valueOf(annoFornitura).intValue() >= Integer.valueOf(minimoAnniFornitura).intValue()){
				
				int meseInt= 0;
				int annoInt=0;
				if (meseFornitura!= null ){
					meseInt= Integer.valueOf(meseFornitura).intValue();
					if (meseInt != 1){
						meseFornitura= Character.fillUpZeroInFront(String.valueOf(meseInt-1),2);
					}else{
						meseFornitura="12";
						if (annoFornitura!= null){
							annoInt=Integer.valueOf(annoFornitura).intValue();
							annoFornitura= String.valueOf(annoInt-1);
						}
					}
				}
				
				listaPrecedentiFornitura= getListaProtocolliPrec(annoFornitura, meseFornitura);
				if (listaPrecedentiFornitura != null && listaPrecedentiFornitura.size()>0){
					Object[] precFornitura= (Object[])listaPrecedentiFornitura.get(0);
					protocollo= (String)precFornitura[0]; 
					fornitura= (String)precFornitura[1];
										
					trovato=true;
										
				}
			
			}
				if (trovato == false){
					
					protocollo= protocolloBean;
					fornitura= fornituraBean;
					
					
				}
		}
		
		
		
		
		
		String sqlCerca = prop.getProperty("qryCerca");
		
		
			//htQry.put("where", aryFilters);
			sqlCerca += " and  D_GEN.protocollo_reg = '"+protocollo+"' and to_char(D_GEN.fornitura,'mm/yyyy') = '" + fornitura +"'";
			
			
			htQry = new Hashtable();
			htQry.put("queryString", sqlCerca);
			
			List lstObj = bodLogicService.getList(htQry);
			
			
			if (lstObj != null && lstObj.size()>0){
				
				ArrayList<BodBean> listaBodBean=Schiavo.setLstBodFromLstObject(lstObj);
				BodBean bodBeanPrecedente= listaBodBean.get(0);
				this.selBodBean=bodBeanPrecedente;
			}
		
		esito= selectionChanged();
		return esito;
	}
	
	public String getBodBeanSuccessivo(){
		String esito="ricercaDettaglioBck.getBodBeanSuccessivo";
		
		String protocolloBean= selBodBean.getProtocollo();
		String fornituraBean= selBodBean.getFornitura();
		
		String meseFornitura= "";
		String annoFornitura="";
		if (fornituraBean != null){
			String[] arrFornitura= fornituraBean.split("/");
			meseFornitura=arrFornitura[0];
			annoFornitura=arrFornitura[1];
		}
		
		String protocollo= "";
		String fornitura= "";
		
		Properties prop = SqlHandler.loadProperties(propName);
		
		//verifico se esiste un altro Bean all'interno della stessa fornitura con protocollo minore
		String sql = prop.getProperty("qryCercaPrecSucc");
		sql += " WHERE protocollo_reg > '"+ protocolloBean+"' AND to_char(fornitura, 'mm')= '"+ meseFornitura+ "' and to_char(fornitura, 'yyyy')='"+ annoFornitura+"'  order by protocollo_reg ";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List listaSuccessiviFornitura = bodLogicService.getList(htQry);
		
		if (listaSuccessiviFornitura != null && listaSuccessiviFornitura.size()>0){
			//prendo il primo della lista
			Object[] succFornitura= (Object[])listaSuccessiviFornitura.get(0);
			protocollo= (String)succFornitura[0]; 
			fornitura= (String)succFornitura[1];
			
			
		} else {
			// se non esiste un altro Bean all'interno della stessa fornitura con protocollo maggiore, allora verifico nella fornitura successiva finchà non ne trovo uno
			
			boolean trovato= false;
			
				while(trovato== false && Integer.valueOf(annoFornitura).intValue() <= Integer.valueOf(maxAnniFornitura).intValue()){
				
				int meseInt= 0;
				int annoInt=0;
				if (meseFornitura!= null ){
					meseInt= Integer.valueOf(meseFornitura).intValue();
					if (meseInt != 12){
						meseFornitura= Character.fillUpZeroInFront(String.valueOf(meseInt+1),2);
					}else{
						meseFornitura="01";
						if (annoFornitura!= null){
							annoInt=Integer.valueOf(annoFornitura).intValue();
							annoFornitura= String.valueOf(annoInt+1);
						}
					}
				}
				
				listaSuccessiviFornitura= getListaProtocolliSucc(annoFornitura, meseFornitura);
				if (listaSuccessiviFornitura != null && listaSuccessiviFornitura.size()>0){
					Object[] succFornitura= (Object[])listaSuccessiviFornitura.get(0);
					protocollo= (String)succFornitura[0]; 

					 fornitura= (String)succFornitura[1];
					
					
					trovato=true;
										
				}
			
			}
				
				if (trovato == false){
					
					protocollo= protocolloBean;
					fornitura= fornituraBean;
				}
		}
		
	
		
		
		String sqlCerca = prop.getProperty("qryCerca");
		
		
			//htQry.put("where", aryFilters);
			sqlCerca += " and  D_GEN.protocollo_reg = '"+protocollo+"'  and to_char(D_GEN.fornitura,'mm/yyyy') = '" + fornitura +"'";
			
			
			htQry = new Hashtable();
			htQry.put("queryString", sqlCerca);
			
			List lstObj = bodLogicService.getList(htQry);
			
			
			if (lstObj != null && lstObj.size()>0){
				
				ArrayList<BodBean> listaBodBean=Schiavo.setLstBodFromLstObject(lstObj);
				BodBean bodBeanSuccessivo= listaBodBean.get(0);
				this.selBodBean=bodBeanSuccessivo;
			}
		
		esito= selectionChanged();
		return esito;
	}
	
	private List getListaProtocolliPrec(String anno, String mese){
		
		List listaPrecedenti= null;
		
		/*
		int meseInt= 0;
		int annoInt=0;
		if (mese!= null ){
			meseInt= Integer.valueOf(mese).intValue();
			if (meseInt != 1){
				mese= Character.fillUpZeroInFront(String.valueOf(meseInt-1),2);
			}else{
				mese="12";
				if (anno!= null){
					annoInt=Integer.valueOf(anno).intValue();
				}
			}
		}
		*/
		
		Properties prop = SqlHandler.loadProperties(propName);
		
		String sql = prop.getProperty("qryCercaPrecSucc");
		sql += " WHERE to_char(fornitura, 'mm') = '"+ mese+ "' and to_char(fornitura, 'yyyy') = '"+ anno+"' order by protocollo_reg desc";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		listaPrecedenti= bodLogicService.getList(htQry);
		
		return listaPrecedenti;
		
	}
	
	
private List getListaProtocolliSucc(String anno, String mese){
		
		List listaSuccessivi= null;
		
		/*
		int meseInt= 0;
		int annoInt=0;
		if (mese!= null ){
			meseInt= Integer.valueOf(mese).intValue();
			if (meseInt != 1){
				mese= Character.fillUpZeroInFront(String.valueOf(meseInt-1),2);
			}else{
				mese="12";
				if (anno!= null){
					annoInt=Integer.valueOf(anno).intValue();
				}
			}
		}
		*/
		
		Properties prop = SqlHandler.loadProperties(propName);
		
		String sql = prop.getProperty("qryCercaPrecSucc");
		sql += " WHERE to_char(fornitura, 'mm') = '"+ mese+ "' and to_char(fornitura, 'yyyy') = '"+ anno+"' order by protocollo_reg ";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		listaSuccessivi= bodLogicService.getList(htQry);
		
		return listaSuccessivi;
		
	}
	
	
	public void showClaCorFab(){
		String sql = "select distinct " +
		"u.unimm as sub, u.categoria categoria, u.classe classe " +
		"from " +
		"sitiuiu u, siticomu sc " +
		"where " +
		"sc.CODI_FISC_LUNA = (select uk_belfiore from ewg_tab_comuni) " +
		"and u.FOGLIO = '" + selClaCorpoFabbrica.getFoglio() + "' " +
		"and u.PARTICELLA = '" + selClaCorpoFabbrica.getParticella() + "' " +
		"and sc.COD_NAZIONALE = u.COD_NAZIONALE " +
		"and data_fine_val = to_date('99991231','yyyymmdd')  " +
		"order by unimm, categoria ";
		
		lstClaCorFab = new ArrayList<Object>();
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		lstClaCorFab = bodLogicService.getList(htQry);
		
	}//-------------------------------------------------------------------------
	
	public void showClassamentoCorpiFabbrica(){
		/*
		 * Se viene riscontrata una eccezione alla riga di codice che crea la stringa
		 * sql, vuol dire che è scaduta la sessione 
		 */
		String sql = "select distinct " +
		"u.unimm as sub, u.categoria categoria, u.classe classe " +
		"from " +
		"sitiuiu u, siticomu sc " +
		"where " +
		"sc.CODI_FISC_LUNA = (select uk_belfiore from ewg_tab_comuni) " +
		"and u.FOGLIO = '" + selClaSub[0] + "' " +
		"and u.PARTICELLA = '" + selClaSub[1] + "' " +
		"and sc.COD_NAZIONALE = u.COD_NAZIONALE " +
		"and data_fine_val = to_date('99991231','yyyymmdd')  " +
		"order by unimm, categoria ";
		
		lstClaCorFab = new ArrayList<Object>();
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		lstClaCorFab = bodLogicService.getList(htQry);
		
	}//-------------------------------------------------------------------------
	
	
	public void showClassamento(){
	
			Context cont;
				
		try{
			ElaborazioniDataIn dataIn = new ElaborazioniDataIn();
			
			if(this.txtRapporto!=null)
				selClaCom.setRapportoParam(this.txtRapporto.getValue());
			else
				selClaCom.setRapportoParam(null);
			
			dataIn.setObj1(selClaCom);
			dataIn.setEnteId(codiceEnte);
			
			cont = new InitialContext();
	

			ElaborazioniService elaborazioniService= (ElaborazioniService) getEjb("CT_Service", "CT_Service_Data_Access", "ElaborazioniServiceBean");
			
			ElaborazioniDataOut dataOut = elaborazioniService.getClassamentoCompatibile(dataIn);
			selClaCom = (ControlloClassamentoConsistenzaDTO)dataOut.getObj1();
			lstClaCom = (List<ControlloClassamentoConsistenzaDTO>)dataOut.getObj2();
			
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
	}
	
	
/*	public void showClassamento(){
	
		lstClaCom = new ArrayList<ControlloClassamentoConsistenzaBean>();
		Object rapportoParam = this.txtRapporto.getValue();
		String valoreCommerciale = Number.formatDouble(selClaCom.getValoreCommerciale());
		String consistenza = selClaCom.getConsistenza();
		String categoria = selClaCom.getCategoria();
		String classe = selClaCom.getClasse();
		String zona = selClaCom.getZona();
		String rapporto = Number.formatDouble(selClaCom.getRapporto());
		if (rapportoParam != null) {
			if (rapportoParam instanceof String) {
				rapporto = (String) rapportoParam;
			} else if (rapportoParam instanceof Long) {
				rapporto = String.valueOf((Long) rapportoParam);
			} else if (rapportoParam instanceof Double) {
				rapporto = Number.formatDouble((Double) rapportoParam);
			}
			Double hopeAvg = selClaCom.getValoreCommerciale()/ (105 *  Number.parsStringToDoubleZero(rapporto));
			Double hopeAvgPerVano = selClaCom.getValoreCommerciale()/ (105 * Number.parsStringToDoubleZero( selClaCom.getConsistenza() ) * Number.parsStringToDoubleZero(rapporto));
			if (hopeAvg != null && !hopeAvg.isNaN() && !hopeAvg.isInfinite())
				selClaCom.setMediaAttesa(hopeAvg);
			if (hopeAvgPerVano != null && !hopeAvgPerVano.isNaN() && !hopeAvgPerVano.isInfinite())
				selClaCom.setMediaAttesaPerVano(hopeAvgPerVano);
			selClaCom.setRapporto(Number.parsStringToDoubleZero(rapporto));
		}
		
		double consistenzaD= Number.parsStringToDouble(consistenza);
		
		//errore Monza: eliminati to_number e apici
		//String tnValoreCommerciale = "TO_NUMBER('"+valoreCommerciale.replaceAll(",", ".")+"')";
		//String tnConsistenza = "TO_NUMBER('"+ consistenza.replaceAll(",", ".") +"')";
		//String tnRapporto = "TO_NUMBER('"+ rapporto.replaceAll(",", ".") +"')";
		//ora le variabili tn fanno solo il replace:
		String tnValoreCommerciale = valoreCommerciale.replaceAll(",", ".");
		String tnConsistenza = consistenza.replaceAll(",", ".");
		String tnRapporto = rapporto.replaceAll(",", ".");
		
		String sql = "SELECT " +
				"zona, categoria, classe, tariffa_euro " +
				"FROM " +
				"docfa_classe " +
				"WHERE " +
				"tariffa_euro >= (" + tnValoreCommerciale + " / " + tnConsistenza + " * 0.9 / 105 / " + tnRapporto + ") " +
				"AND tariffa_euro <= (" + tnValoreCommerciale + " / " + tnConsistenza + " * 1.1 / 105 / " + tnRapporto + ") " +
				"AND (UPPER (categoria) LIKE 'A%' " +
				"AND UPPER (categoria) <> 'A10' " +
				"AND UPPER (categoria) <> 'A05' " +
				") " +
				"AND NVL (zona, '-1') = NVL (TO_NUMBER ('" + zona + "'), '-1') " +
				//"AND (categoria <> '" + categoria + "' OR classe <> TO_NUMBER ('" + classe + "'))  ";
				//a Monza esiste la classe U che darebbe errore, quindi:
				"AND (categoria <> '" + categoria + "' OR classe <> DECODE('" + classe + "', 'U', 'U', TO_NUMBER ('" + classe + "')))  ";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> listaClaCom = bodLogicService.getList(htQry);
		if (listaClaCom != null && listaClaCom.size()>0){
			ControlloClassamentoConsistenzaBean claCom = null;
			for (int i=0; i<listaClaCom.size(); i++){
				claCom = new ControlloClassamentoConsistenzaBean();
				Object[] objs = (Object[])listaClaCom.get(i);
				claCom.setZona((String)objs[0]);
				claCom.setCategoria((String)objs[1]);
				claCom.setClasse((String)objs[2]);
				claCom.setTariffa( ((BigDecimal)objs[3]).doubleValue() );
				
				claCom.setRendita(((BigDecimal)objs[3]).doubleValue() * consistenzaD );
				
				lstClaCom.add(claCom);
			}
		}

	}*///-------------------------------------------------------------------------

	public void showPlanimetry(){
				
		String sqlDatiCensuari = "SELECT DISTINCT " +
				"met.superficie supm,  met.ambiente ambiente, met.altezza altezza " +
				"FROM " +
				"DOCFA_DATI_CENSUARI cen " +
				"left join DOCFA_DATI_METRICI met " +
				"on cen.identificativo_immobile = met.identificativo_immobile " +
				"WHERE 1=1 " +
				"and cen.protocollo_registrazione = met.protocollo_registrazione " +
				"and to_char(to_date(cen.data_registrazione, 'yyyymmdd'), 'yyyymm') = to_char(cen.fornitura, 'yyyymm') " +
				"AND cen.data_registrazione = met.data_registrazione " +
				"AND cen.fornitura = met.fornitura " +
				"AND met.protocollo_registrazione  = '" + selBodBean.getProtocollo() + "' " +
				"AND met.FORNITURA =  TO_DATE('" + selBodBean.getFornitura() + "', 'MM/yyyy') " +
				"and cen.identificativo_immobile = '" + selDatCen[10] + "' " +
				"order by ambiente ";
 

		Hashtable<String,String> htQry = new Hashtable<String,String>();
		htQry.put("queryString", sqlDatiCensuari);
		lstSuperficiPerVano = bodLogicService.getList(htQry);
		
		String sqlImmaginiPlanimetrie = "SELECT DISTINCT " +
				"nome_plan, progressivo, formato, protocollo, fornitura, identificativo_immo " +
				"FROM " +
				"DOCFA_PLANIMETRIE " +
				"WHERE 1=1 " +
				"and fornitura= TO_DATE('" + selBodBean.getFornitura() + "', 'MM/yyyy') " + 
                " and protocollo = '" + selBodBean.getProtocollo() + "' " +
                "and (identificativo_immo = '" + selDatCen[10] + "' or identificativo_immo = 0) ";
		
		htQry = new Hashtable<String,String>();
		htQry.put("queryString", sqlImmaginiPlanimetrie);
		List lstImagesPlanimetries = bodLogicService.getList(htQry);
		lstImagesFiles = new ArrayList<PlanimetriaBean>();
		Iterator<Object[]> itImgPla = lstImagesPlanimetries.iterator();
		for (int i=0; i<lstImagesPlanimetries.size(); i++) {
			
			Object[] objs = (Object[])lstImagesPlanimetries.get(i);
			String nomeFile = Character.checkNullString((String)objs[0]);
			if (nomeFile != null && !nomeFile.equals(""))
			{
			PlanimetriaBean plan = new PlanimetriaBean();			
			BigDecimal progressivo = (BigDecimal)objs[1];
			String progr = "";
			if (progressivo != null)
				progr = progressivo.toString();
			while(progr.length() < 3 )
				progr = "0" + progr;
			nomeFile = nomeFile + "." + progr + ".tif";
			String[] fornitura = selBodBean.getFornitura().split("/");
			String pathFile =  this.getRootPathEnte() + "planimetrie/" + fornitura[1] + fornitura[0] + "/" + nomeFile;
			logger.info("Path Planimetria: " + pathFile);
			File f = new File(pathFile);
			plan.setDescrizione(nomeFile);
			if (f.exists()){
				plan.setEsiste(true);
			}
			plan.setProgressivo(progr);
			plan.setFile(f);
			BigDecimal formato = (BigDecimal)objs[2];
			plan.setFormato(formato == null ? 0 : formato.intValue());
//			plan.setIdImmobile( Character.checkNullString((String)objs[5]));
//			plan.setProtocollo( Character.checkNullString((String)objs[3]) );
//			plan.setFornitura( Character.checkNullString((String)objs[4]) );
			
			lstImagesFiles.add(plan);
			}
		}
	}//-------------------------------------------------------------------------
	
	public  void openPDF() throws IOException{
		logger.info("We are waiting for opening of: " + pdf);
//		FacesContext context = FacesContext.getCurrentInstance();
//		ExternalContext extContext = context.getExternalContext();
//		res = (HttpServletResponse)extContext.getResponse();
//
//		File pdfFile = new File(pdfDocfa.getLink());
//		InputStream isByte = new FileInputStream( pdfFile );
//		BufferedInputStream bis = new BufferedInputStream(isByte);
//        PrintWriter pw  =  res.getWriter();
//        int count = 0;
//        while ((count = bis.read()) >= 0){
//            pw.write(count);                    
//        }
//        bis.close();
//        pw.flush();
//        pw.close();
	
	}//-------------------------------------------------------------------------
	
	public  void loadDOC(OutputStream out, Object data) throws IOException{
//		File doc = new File(pathDoc);
//		InputStream isByte = new FileInputStream(doc);
//		byte[] b = new byte[1024];
//		int count = 0;
//       	while((count = isByte.read(b, 0, 1024)) != -1){
//        	out.write(b, 0, count);
//        }
        //logger.info("Visualizzazione DOC!!! " + tempPathDocC);
        logger.info("Visualizzazione DOC!!! " + tempPathDocC);
	}//-------------------------------------------------------------------------
	
	public  void openVirtualEarth() throws IOException{

		logger.info("VirtualEarth - We are waiting for opening of: " +
				"cod. Ente: " + currentVirtualEarth.getCodiceEnte() + " " +
				"lat: " + currentVirtualEarth.getLatitudine() + " " +
				"lon: " + currentVirtualEarth.getLongitudine());
	}//-------------------------------------------------------------------------
	
	public  void openStreetView() throws IOException{

		logger.info("StreetView - We are waiting for opening of: " +
				"cod. Ente: " + currentStreetView.getCodiceEnte() + " " +
				"lat: " + currentStreetView.getLatitudine() + " " +
				"lon: " + currentStreetView.getLongitudine());
	}//-------------------------------------------------------------------------
	
	
	public  void openDOC() throws Exception{
//		FacesContext context = FacesContext.getCurrentInstance();
//		ExternalContext extContext = context.getExternalContext();
//		req = (HttpServletRequest)extContext.getRequest();
//		res = (HttpServletResponse)extContext.getResponse();
//		
//		ServletOutputStream out = res.getOutputStream();
//		res.setContentType( "application/msword" );
//        res.setHeader( "Content-Disposition", "attachment; filename=allegato_segnalazione.doc" );
//
//        //When it is inline it opens the file,
//        //res.setHeader( "Content-Disposition", "inline; filename=" + downloadFileName );
//        
//        //In the following case, we get a File Download dialog, but it behaves differently
//        //res.setHeader( "Content-Type", "attachment; filename=" + downloadFileName );
//        
//        File doc = new File(pathDoc);
//		InputStream isByte = new FileInputStream(doc);
//		byte[] b = new byte[1024];
//		int count = 0;
//       	while((count = isByte.read(b, 0, 1024)) != -1){
//        	out.write(b, 0, count);
//        }
//       	req.setAttribute("file", pathDoc);
       	//logger.info("Visualizzazione DOC!!! " + tempPathDocC);   
        logger.info("Visualizzazione DOC!!! " + tempPathDocC);
//       	ServletContext sc = (ServletContext)extContext.getContext();
//       	RequestDispatcher rd = sc.getRequestDispatcher("/ShowReport.jsp");
//       	rd.forward(req, res);
       	
   	}//-------------------------------------------------------------------------

	public  void loadPDF(OutputStream out, Object data) throws IOException{
	    
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		InputStream isByte = null;
		try{
			if (pdf != null && !pdf.trim().equalsIgnoreCase("docfa")){
				if (currentPla != null){
					File image = currentPla.getFile();
					/* 
					Iterator it = lstImagesFiles.iterator();
					int i = 1;
					while (it.hasNext()) {
						File f = (File) it.next();
						if (i == new Integer(lstImagesFiles.size()).intValue()) {
							image = f;
							is = new FileInputStream(image);
							break;
						}
						i +=1;
					}
					*/
					is = new FileInputStream(image);
					List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is);
					baos =  TiffUtill.jpgsTopdf(jpgs, false, currentPla.getFormato());
					isByte = new ByteArrayInputStream(baos.toByteArray());
					//isByte = new ByteArrayInputStream(jpgs.get(0).toByteArray());
				}
			}else{
				File pdfFile = new File(pdfDocfa.getLink());
				isByte = new FileInputStream( pdfFile );
				//isByte = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream( pdfDocfa.getLink() );
				/*
				 * Ripulisco pdf perché altrimenti mi 
				 */
				pdf = "";
				//data = (File)pdfFile;
	            logger.info("Visualizzazione PDF!!! " + pdfDocfa.getLink());
			}
			if (isByte != null){

				int size = isByte.available();
				byte[] b = new byte[size];
				isByte.read(b);

				out.write(b);
				out.flush();

// http://seamframework.org/10453.lace http://www.icefaces.org/JForum/posts/list/6602.page MI0004613				
//				byte[] b = new byte[1024];
//				int count = 0;
//	           	//while( (count = isByte.read(b, 0, 1024) ) != -1 ){
//				while( isByte.read(b) != -1 ){
//					try{
//		            	//if (count >= 40){
//						logger.info("Index: " + count++ + " Available: " + isByte.available());
//		            	//}
//		            	//out.write(b, 0, count);
//						out.write(b);
//						out.wait(1000);
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//	            }				
			}
//			out.flush();
//          out.close();
            //logger.info("Visualizzazione PDF!!! " + pdfDocfa.getLink());

		}catch(FileNotFoundException fnfe){
			logger.error(fnfe.getMessage());
			fnfe.printStackTrace();
		}catch(IOException ioe){
			logger.error(ioe.getMessage());
			ioe.printStackTrace();
		}
		finally{
            if (is != null)
            	is.close();
            if (baos != null){
				baos.flush();
				baos.close();
            }
            if (isByte != null)
            	isByte.close();     
			//out.flush();
			//out.close();
		}
   	}//-------------------------------------------------------------------------
	
//	public void paint(OutputStream out, Object data) throws IOException{
//		
//        if (data instanceof MediaData) {
//            
//	        mediaData paintData = (MediaData) data;
//	        BufferedImage img = new BufferedImage(paintData.getWidth(), paintData.getHeight(), BufferedImage.TYPE_INT_RGB);
//	        Graphics2D graphics2D = img.createGraphics();
//	        graphics2D.setBackground(paintData.getBackground());
//	        graphics2D.setColor(paintData.getDrawColor());
//	        graphics2D.clearRect(0, 0, paintData.getWidth(), paintData.getHeight());
//	        graphics2D.drawLine(5, 5, paintData.getWidth()-5, paintData.getHeight()-5);
//	        graphics2D.drawChars(new String("RichFaces").toCharArray(),0,9,40,15);
//	        graphics2D.drawChars(new String("mediaOutput").toCharArray(),0,11,5,45);
//	        
//	        ImageIO.write(img,"jpeg", out);
//	        
//        }else{
//        	MediaData paintData = new MediaData();
//        	
//        	BufferedImage img = new BufferedImage(paintData.getWidth(), paintData.getHeight(), BufferedImage.TYPE_INT_RGB);
//	        Graphics2D graphics2D = img.createGraphics();
//	        graphics2D.setBackground(paintData.getBackground());
//	        graphics2D.setColor(paintData.getDrawColor());
//	        graphics2D.clearRect(0, 0, paintData.getWidth(), paintData.getHeight());
//	        graphics2D.drawLine(5, 5, paintData.getWidth()-5, paintData.getHeight()-5);
//	        graphics2D.drawChars(new String("RichFaces").toCharArray(),0,9,40,15);
//	        graphics2D.drawChars(new String("mediaOutput").toCharArray(),0,11,5,45);
//	        
//	        ImageIO.write(img, "jpeg", out);
//	        
//	        
//        }
//    }//-------------------------------------------------------------------------
	
	public void validateL662Flag(){
		if (bodIstruttoria != null && bodIstruttoria.getIdIst() != null){
			BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.getItemById(bodIstruttoria.getIdIst(), BodIstruttoriaBean.class);
			if (bIstruttoria != null && bIstruttoria.getOperatore() != null && utente != null && utente.getName() != null){
				if ( Schiavo.usersComparator(utente.getName().trim(), bIstruttoria.getOperatore().trim())){
					/*
					 * L'interfaccia web è collegata a bodIstruttoria che raccoglie 
					 * i suoi eventi, quindi la valutazione delle checkbox deve essere
					 * effettuata attraverso questo bean e non su quello recuperato dal 
					 * db
					 */
					if (bodIstruttoria.getEsitoIst662()){
						bIstruttoria.setEsitoIst662(true);
						bIstruttoria.setEsitoIstNA(false);
						logger.info("Da Falso a Vero L.662");
					}else{
						bIstruttoria.setEsitoIst662(false);
						logger.info("Da Vero a Falso L.662");
					}
					bIstruttoria.setAnnotazioni(bodIstruttoria.getAnnotazioni().trim());
					bodIstruttoria = (BodIstruttoriaBean)bodLogicService.updItem(bIstruttoria, BodIstruttoriaBean.class);
				}else{
					logger.info("XXX: Utente e Operatore sono diversi per cui non è possibile chiudere la pratica");
					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);				
				}
			}else{
				logger.info("XXX: Uno dei seguenti oggetti è nullo: bIstruttoria, bIstruttoria.getOperatore(), utente, utente.getName()");
				bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);				
			}	
		}			
	}//-------------------------------------------------------------------------
	
	public void validateL80Flag(){
		if (bodIstruttoria != null && bodIstruttoria.getIdIst() != null){
			BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.getItemById(bodIstruttoria.getIdIst(), BodIstruttoriaBean.class);
			if (bIstruttoria != null && bIstruttoria.getOperatore() != null && utente != null && utente.getName() != null){
				if ( Schiavo.usersComparator(utente.getName().trim(), bIstruttoria.getOperatore().trim())){
					/*
					 * L'interfaccia web è collegata a bodIstruttoria che raccoglie 
					 * i suoi eventi, quindi la valutazione delle checkbox deve essere
					 * effettuata attraverso questo bean e non su quello recuperato dal 
					 * db
					 */
					if (bodIstruttoria.getEsitoIst80()){
						bIstruttoria.setEsitoIst80(true);
						bIstruttoria.setEsitoIstNA(false);
						logger.info("Da Falso a Vero L.80");
					}else{
						bIstruttoria.setEsitoIst80(false);
						logger.info("Da Vero a Falso L.80");
					}
					bIstruttoria.setAnnotazioni(bodIstruttoria.getAnnotazioni().trim());
					bodIstruttoria = (BodIstruttoriaBean)bodLogicService.updItem(bIstruttoria, BodIstruttoriaBean.class);
				}else{
					logger.info("XXX: Utente e Operatore sono diversi per cui non è possibile chiudere la pratica");
					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);
				}
			}else{
				logger.info("XXX: Uno dei seguenti oggetti è nullo: bIstruttoria, bIstruttoria.getOperatore(), utente, utente.getName()");
				bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);				
			}
		}
	}//-------------------------------------------------------------------------
	
	public void validateL311Flag(){
		if (bodIstruttoria != null && bodIstruttoria.getIdIst() != null){
			BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.getItemById(bodIstruttoria.getIdIst(), BodIstruttoriaBean.class);
			if (bIstruttoria != null && bIstruttoria.getOperatore() != null && utente != null && utente.getName() != null){
				if ( Schiavo.usersComparator(utente.getName().trim(), bIstruttoria.getOperatore().trim())){
					/*
					 * L'interfaccia web è collegata a bodIstruttoria che raccoglie 
					 * i suoi eventi, quindi la valutazione delle checkbox deve essere
					 * effettuata attraverso questo bean e non su quello recuperato dal 
					 * db
					 */
					if (bodIstruttoria.getEsitoIst311()){
						bIstruttoria.setEsitoIst311(true);
						bIstruttoria.setEsitoIstNA(false);
						logger.info("Da Falso a Vero L.311");
					}else{
						bIstruttoria.setEsitoIst311(false);
						logger.info("Da Vero a Falso L.80");
					}
					bIstruttoria.setAnnotazioni(bodIstruttoria.getAnnotazioni().trim());
					bodIstruttoria = (BodIstruttoriaBean)bodLogicService.updItem(bIstruttoria, BodIstruttoriaBean.class);
				}else{
					logger.info("XXX: Utente e Operatore sono diversi per cui non è possibile chiudere la pratica");
					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);
				}
			}else{
				logger.info("XXX: Uno dei seguenti oggetti è nullo: bIstruttoria, bIstruttoria.getOperatore(), utente, utente.getName()");
				bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);				
			}
		}
	}//-------------------------------------------------------------------------
	
	public void validateNAFlag(){
		if (bodIstruttoria != null && bodIstruttoria.getIdIst() != null){
			BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.getItemById(bodIstruttoria.getIdIst(), BodIstruttoriaBean.class);
			if (bIstruttoria != null && bIstruttoria.getOperatore() != null && utente != null && utente.getName() != null){
				if ( Schiavo.usersComparator(utente.getName().trim(), bIstruttoria.getOperatore().trim())){
					/*
					 * L'interfaccia web è collegata a bodIstruttoria che raccoglie 
					 * i suoi eventi, quindi la valutazione delle checkbox deve essere
					 * effettuata attraverso questo bean e non su quello recuperato dal 
					 * db
					 */
					if (bodIstruttoria.getEsitoIstNA()){
						bIstruttoria.setEsitoIst311(false);
						bIstruttoria.setEsitoIst662(false);
						bIstruttoria.setEsitoIst80(false);
						bIstruttoria.setEsitoIstNA(true);
						logger.info("Da Falso a Vero Nessuna Anomalia");
					}else{
						bIstruttoria.setEsitoIstNA(false);
						logger.info("Da Vero a Falso Nessuna Anomalia");
					}
					bIstruttoria.setAnnotazioni(bodIstruttoria.getAnnotazioni().trim());
					bodIstruttoria = (BodIstruttoriaBean)bodLogicService.updItem(bIstruttoria, BodIstruttoriaBean.class);
				}else{
					logger.info("XXX: Utente e Operatore sono diversi per cui non è possibile chiudere la pratica");
					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);
				}
			}else{
				logger.info("XXX: Uno dei seguenti oggetti è nullo: bIstruttoria, bIstruttoria.getOperatore(), utente, utente.getName()");
				bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);				
			}
		}
	}//-------------------------------------------------------------------------
	
	public String salvaAnnotazioni(){
		String esito = "ricercaDettaglioBck.salvaAnnotazioni";
		
		if (bodIstruttoria != null && bodIstruttoria.getIdIst() != null){
			BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.getItemById(bodIstruttoria.getIdIst(), BodIstruttoriaBean.class);
			logger.info("XXX: bIstruttoria ha id_ist = " + bodIstruttoria.getIdIst());
			if (bIstruttoria != null && bIstruttoria.getOperatore() != null && utente != null && utente.getName() != null){

				String operatore = ""; 

				if (this.currentOperator != null && !this.currentOperator.trim().equalsIgnoreCase(""))
					operatore = this.currentOperator;
				else
					operatore = bIstruttoria.getOperatore();

				if (Schiavo.usersComparator( utente.getName().trim(), operatore.trim()) ){
					logger.info("XXX BISTRUTTORIA.ANNOTAZIONI: " + bIstruttoria.getAnnotazioni());
					logger.info("XXX BODISTRUTTORIA.ANNOTAZIONI: " + bodIstruttoria.getAnnotazioni());
					bIstruttoria.setAnnotazioni(bodIstruttoria.getAnnotazioni());
					bodIstruttoria = (BodIstruttoriaBean)bodLogicService.updItem(bIstruttoria, BodIstruttoriaBean.class);
				}else{
					logger.info("XXX: Utente e Operatore sono diversi per cui non è possibile salvare le annotazioni");
					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);				
				}
			}else{
				if (bIstruttoria == null)
					logger.info("XXX: bIstruttoria è nullo");
				if (bIstruttoria.getOperatore() == null)
					logger.info("XXX: bIstruttoria.getOperatore() è nullo");
				if (utente == null)
					logger.info("XXX: utente è nullo");
				if (utente.getName() == null)
					logger.info("XXX: utente.getName() è nullo");
				
				bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);				
			}
		}
		return esito;
	}//-------------------------------------------------------------------------	
	
	public String validatePresaInCaricoFlag(){
		String esito = "ricercaDettaglioBck.validatePresaInCaricoFlag";
		
		if (bodIstruttoria != null){
			if (!bodIstruttoria.getPresaInCarico()){
				/*
				 * Scrivo il record solo se la pratica non è già stata presa in 
				 * carico da qualcuno
				 */
				logger.info("Da Falso a Vero Presa In Carico");
				Hashtable htQry = new Hashtable();
				List<FilterItem> filters = new ArrayList<FilterItem>();
				FilterItem filter = new FilterItem();
				filter.setAttributo("protocollo");
				filter.setOperatore("=");
				filter.setParametro("protocollo");
				filter.setValore( (selBodBean!=null && selBodBean.getProtocollo()!=null)?selBodBean.getProtocollo():"" );
				filters.add(filter);
				
				filter = new FilterItem();
				filter.setAttributo("fornitura");
				filter.setOperatore("_IL_");
				filter.setParametro("fornitura");
				filter.setValore(tc.fromFormatStringToDate("01/" + selBodBean.getFornitura(), "00:00"));
				filters.add(filter);
				
				htQry.put("where", filters);
				
				ArrayList<BodIstruttoriaBean> lst = (ArrayList<BodIstruttoriaBean>)bodLogicService.getList(htQry, BodIstruttoriaBean.class);
				if (lst != null && lst.size() >0){
					/*
					 * già Presa in Carico (= il messaggio già è visibile con il
					 * nome dell'utente che ha in carico la pratica)
					 */
				}else{
					/*
					 * Insert in tabella 
					 */
					String operator = utente.getName();
					logger.info("XXX PRATICA PRESA IN CARICO DA: " + operator);
					this.currentOperator = operator;
					bodIstruttoria.setOperatore(operator);
					bodIstruttoria.setPresaInCarico(true);
					Long lastId = bodLogicService.addItem(bodIstruttoria, BodIstruttoriaBean.class);
					logger.info("XXX: Istruttoria presa in carico con id_ist = " + lastId);
					bodIstruttoria.setIdIst(lastId);
					/*
					 * Mostrare Msg di successo, disabilitare btn Prendi in Carico
					 * ed abilitare il resto del mondo
					 */
					bodIstruttoriaSwitch.setMostraMsgIstruttoriaSave(true);
					bodIstruttoriaSwitch.setDisabledPresaInCarico(true);
					bodIstruttoriaSwitch.setDisabledSaveButton(false);
					bodIstruttoriaSwitch.setOperatoreAutorizzato(true);
					bodIstruttoriaSwitch.setMostraOperatore(true);
				}
				msgPraticaStatus = MSG_PRATICA_PRESA_IN_CARICO;
			}else{
				logger.info("Da Vero a Falso Presa In Carico");
			}
		}
		return esito;
	}//-------------------------------------------------------------------------
	
//	public boolean validatePresaInCarico(String esito){
//		boolean presaInCarico = false;
//		if (bodIstruttoria != null){
//			if (bodIstruttoria.getPresaInCarico()){
//				bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL662(false);
//				bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL80(false);
//				bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL311(false);
//				bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoNA(false);				
//				presaInCarico = true;
//			}else{
//				if (esito.equals("L662")){
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL662(true);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL80(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL311(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoNA(false);
//				}else if (esito.equals("L80")){
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL662(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL80(true);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL311(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoNA(false);
//				}else if (esito.equals("L311")){
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL662(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL80(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL311(true);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoNA(false);
//				}else{
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL662(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL80(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoL311(false);
//					bodIstruttoriaSwitch.setMostraMsgNonPresaInCaricoNA(true);				
//				}	
//				bodIstruttoria.setEsitoIst311(false);
//				bodIstruttoria.setEsitoIst662(false);
//				bodIstruttoria.setEsitoIst80(false);
//				bodIstruttoria.setEsitoIstNA(false);
//			}
//		}
//		return presaInCarico;
//	}//-------------------------------------------------------------------------
	
//	public void validateChiudiFlag(){
//		/*
//		 * Controllo se l'utente che l'ha presa in carico è l'utente corrente perché
//		 * è il solo autorizzato a chiuderla 
//		 */
//		if ( Schiavo.usersComparator(utente.getName().trim(), bodIstruttoria.getOperatore().trim())){
//			/*
//			 * Stesso Utente
//			 */
//			bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(false);
//			if (bodIstruttoria.getChiusa()){
//				/*
//				 * Per accettare la chiusura della pratica devo controllare se è stata
//				 * presa in carico e gli esiti:
//				 * Nessuna Anomalia esclude la segnalazione di una delle leggi anomale
//				 * L.662 prevede la compilazione di un modello
//				 * L.80 prevede la compilazione della segnalazione, l'upload dell'allegato
//				 */
//				if (bodIstruttoria.getEsitoIstNA()){
//					bodIstruttoria.setEsitoIst311(false);
//					bodIstruttoria.setEsitoIst662(false);
//					bodIstruttoria.setEsitoIst80(false);
//				}else if ( bodIstruttoria.getEsitoIst311() || bodIstruttoria.getEsitoIst662() || bodIstruttoria.getEsitoIst80() ){
//					if (bodIstruttoria.getEsitoIst662()){
//						/*
//						 * Controllo della compilazione del modello
//						 */
//					}
//					if (bodIstruttoria.getEsitoIst80()){
//						boolean segnalazioneCompilata = false;
//						boolean uploadEseguito = false;
//						if ( bodSegnalazione != null && bodSegnalazione.getEsitoControllo() != null && !bodSegnalazione.getEsitoControllo().equalsIgnoreCase("")){
//							/*
//							 * Segnalazione compilata
//							 */
//							segnalazioneCompilata = true;
//						}else{
//							segnalazioneCompilata = false;
//						}
//						if ( bodSegnalazione != null && bodSegnalazione.getNomeFile() != null && !bodSegnalazione.getNomeFile().equalsIgnoreCase("")){
//							/*
//							 * Upload Allegato eseguito
//							 */
//							uploadEseguito = true;
//						}else{
//							uploadEseguito = false;
//						}
//						if (!segnalazioneCompilata || !uploadEseguito){
//							bodIstruttoria.setChiusa(false);
//							bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);
//						}
//					}
//				}else{
//					/*
//					 * Non ci sono flag spuntati quindi non si puo chiudere la pratica
//					 */
//					bodIstruttoria.setChiusa(false);
//					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);
//				}	
//				//logger.info("Da Falso a Vero Chiudi Pratica");
//				logger.info("Da Falso a Vero Chiudi Pratica");
//			}else{
//				//logger.info("Da Vero a Falso Chiudi Pratica");
//				logger.info("Da Vero a Falso Chiudi Pratica");
//			}			
//		}else{
//			/*
//			 * Utente Diverso e quindi non autorizzato a chiudere
//			 */
//			bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(true);
//		}
//	}//-------------------------------------------------------------------------
	
	public String loadTab3() throws IOException{
		String esito = "ricercaDettaglioBck.loadTab3";
		
		FacesContext context = FacesContext.getCurrentInstance();
		String serverAddress= ((HttpServletRequest)context.getExternalContext().getRequest()).getServerName();
		String serverPort= String.valueOf(((HttpServletRequest)context.getExternalContext().getRequest()).getServerPort());
		String dirAllegati=ApplicationResources.getMessageResourceString(context.getApplication().getMessageBundle(), "dirBodFiles", null, context.getViewRoot().getLocale());
		//String pathAllegati="http://"+ serverAddress+ ":"+ serverPort+"/"+ dirAllegati+"temporaryFiles/" ;
		String pathAllegati = this.getRootPathEnte()+ this.TEMPORARY_FILES + "/";
		this.pathAllegati= pathAllegati;
		
		isAutorizzatoRicerca = GestionePermessi.autorizzato(utente, nomeApplicazione, "Ricerca Bod", "Istruttoria");
		if (isAutorizzatoRicerca){
			//logger.info("Load Tab 3! prot: " + selBodBean.getProtocollo() + " forn: " + selBodBean.getFornitura());
			this.initTab3();
			/*
			 * Recupero il bean nel caso in cui sia stata già presa in carico la pratica
			 */
			Hashtable htQry = new Hashtable();
			List<FilterItem> filters = new ArrayList<FilterItem>();
			FilterItem filter = new FilterItem();
			filter.setAttributo("protocollo");
			filter.setOperatore("=");
			filter.setParametro("protocollo");
			filter.setValore(selBodBean.getProtocollo());
			filters.add(filter);
			
			filter = new FilterItem();
			filter.setAttributo("fornitura");
			filter.setOperatore("_IL_");
			filter.setParametro("fornitura");
			filter.setValore(tc.fromFormatStringToDate("01/" + selBodBean.getFornitura(), "00:00"));
			filters.add(filter);
			
			htQry.put("where", filters);
			
			ArrayList<BodIstruttoriaBean> lst = (ArrayList<BodIstruttoriaBean>)bodLogicService.getList(htQry, BodIstruttoriaBean.class);
			if (lst != null && lst.size() >0){
				bodIstruttoria = (BodIstruttoriaBean)lst.get(0);
				/*
				 * -In ogni caso, visto che la pratica esiste devo disabilitare 
				 * la presa In Carico.
				 */
				bodIstruttoriaSwitch.setDisabledPresaInCarico(true);
				/*
				 * La pratica che è chiusa rimane chiusa. Il controllo per l'utente 
				 * autorizzato a chiuderla verrà fatto nell'evento di chiusura.
				 * In ogni caso segnalazione e file upload non possono essere modificati
				 * quindi disabilito il pulsante SALVA in entrambe 
				 */
				if (bodIstruttoria.getChiusa()){
					bodIstruttoriaSwitch.setDisabledSaveButton(true);
					bodSegnalazioneSwitch.setDisabledButtonSave(true);
					bodUploadSwitch.setDisabledButtonSave(true);
				}
//				/*
//				 * Mostro l'operatore che ha preso in carico l'istruttoria
//				 */
				bodIstruttoriaSwitch.setMostraOperatore(true);
				/*
				 * recupero anche la segnalazione relativa all'istruttoria per 
				 * impostare il link dell'allegato già esistente 
				 */
				Set<BodSegnalazioneBean> segnalazioni = bodIstruttoria.getSegnalazioni();
				if (segnalazioni != null && segnalazioni.size()>0){
					bodSegnalazione = segnalazioni.iterator().next();
					bodSegnalazioneSwitch.setMostraLinkNuova(false);
					if (bodSegnalazione != null && bodSegnalazione.getNomeFile() != null && !bodSegnalazione.getNomeFile().trim().equalsIgnoreCase("")){
						int start = bodSegnalazione.getNomeFile().lastIndexOf("/");
						int end = bodSegnalazione.getNomeFile().length();
						nomeAllegato = bodSegnalazione.getNomeFile().substring(start, end);
						bodSegnalazioneSwitch.setMostraLinkAllegato(true);
					}
				}else{
					bodSegnalazioneSwitch.setMostraLinkNuova(true);
				}
				
				//recupero l'eventuale allegato B per  impostare il link dell'allegato già esistente 
				Set<BodAllegatiBean> allegati= bodIstruttoria.getAllegati();
				if (allegati!= null && allegati.size()>0){
					bodAllegato= allegati.iterator().next();
					if (bodAllegato != null && bodAllegato.getNomeFile() != null && !bodAllegato.getNomeFile().trim().equalsIgnoreCase("")){
						int start = bodAllegato.getNomeFile().lastIndexOf("/");
						int end = bodAllegato.getNomeFile().length();
						nomeAllegatoB = bodAllegato.getNomeFile().substring(start, end);
						bodSegnalazioneSwitch.setMostraLinkAllegatoB(true);
					}
				}
			}else{
				bodIstruttoria = new BodIstruttoriaBean();
				bodIstruttoria.setProtocollo(selBodBean.getProtocollo());
				bodIstruttoria.setFornitura(tc.fromFormatStringToDate("01/" + selBodBean.getFornitura(), "00:00"));
				bodSegnalazioneSwitch.setMostraLinkNuova(true);
				bodIstruttoriaSwitch.setDisabledPresaInCarico(false);
				bodIstruttoriaSwitch.setDisabledSaveButton(true);
			}
			/*
			 * Determino se l'utente corrente è autorizzato ad apportare modifiche
			 * oppure devo disabilitare tutto
			 */
			if ( Schiavo.usersComparator(utente.getName().trim(), bodIstruttoria.getOperatore().trim())){
				bodIstruttoriaSwitch.setOperatoreAutorizzato(true);
			}
			
			
			
		}else{
			esito = "failure";
		}

		return esito;
	}//-------------------------------------------------------------------------
	
	private void caricaAllegatoC() throws IOException{
		
		
		FacesContext context= FacesContext.getCurrentInstance();
		/*
		 * Preparo il file per allegato C partendo dal template
		 */
		File fileDoc = new File(this.getPathTemplateBod() + "template_allegati_segnalazioni/allegato_c.doc");
		FileInputStream fis = new FileInputStream(fileDoc);
		BufferedInputStream bis = new BufferedInputStream(fis);
		HWPFDocument doc = new HWPFDocument(fis);
		
		
		tempFileDocC = sessionId + "_allegato_c.doc";
		tempPathDocC = this.getRootPathEnte() + this.TEMPORARY_FILES +"/" + tempFileDocC;
		//logger.info(pathTempDoc);
		FileOutputStream docOutC = new FileOutputStream(tempPathDocC);
		
		
		//carico informazioni precompilate nel pannellino
        
        BodModello80Bean modello= this.getModello80();
        String oggettoDomanda="";
        String tipoIntervento = "";
    	String oggetto = "";
    	String numProgressivo = "";
    	String numProtocollo = "";
    	String note = "";
    	String settore = "";
    	String destinazioneZona = "";
    	String significativaPresenza = "";
    	String destinazioneUiu = "";
    	String categoriaProposta = "";
    	String classeProposta = "";
    	String annotazioni = "";
    	String richiesta662="";
    	String allineamentoUiu="";
    	String nonApplicabile336="";
    	String dotatoAscensore="";
        
        if (modello!= null){
        	
        	if (modello.getOggettoDomanda()!= null)
        		oggettoDomanda=modello.getOggettoDomanda();
        	if (modello.getTipoIntervento()!= null)
        		tipoIntervento = modello.getTipoIntervento();
        	if (modello.getOggetto()!= null)
        		oggetto = modello.getOggetto();
        	if (modello.getNumProgressivo()!= null)
        		numProgressivo = modello.getNumProgressivo();
        	if (modello.getNumProtocollo()!= null)
        		numProtocollo = modello.getNumProtocollo();
        	if (modello.getNote()!= null)
        		note = modello.getNote();
        	if (modello.getSettore()!= null)
        		settore = modello.getSettore();
        	if (modello.getDestinazioneZona()!= null)
        		destinazioneZona = modello.getDestinazioneZona();
        	if (modello.getSignificativaPresenza()!= null)
        		significativaPresenza = modello.getSignificativaPresenza();
        	if (modello.getDestinazioneUiu()!= null)
        		destinazioneUiu = modello.getDestinazioneUiu();
        	if (modello.getCategoriaProposta()!= null)
        		categoriaProposta = modello.getCategoriaProposta();
        	if (modello.getClasseProposta()!= null)
        		classeProposta = modello.getClasseProposta();
        	if (modello.getIstruttoria()!= null && modello.getIstruttoria().getAnnotazioni() != null)
        		annotazioni = modello.getIstruttoria().getAnnotazioni();
        	if (modello.getFlg662()!= null && modello.getFlg662().equals(new Boolean(true)))
        			richiesta662= ApplicationResources.getMessageResourceString(context.getApplication().getMessageBundle(), "testoFlg662", null, context.getViewRoot().getLocale());
        	if (modello.getFlgAllineamento()!= null && modello.getFlgAllineamento().equals(new Boolean(true)))
    				allineamentoUiu= ApplicationResources.getMessageResourceString(context.getApplication().getMessageBundle(), "testoFlgAllineamentoUiuC", null, context.getViewRoot().getLocale());
        	if (modello.getFlgNo336()!= null && modello.getFlgNo336().equals(new Boolean(true)))
    				nonApplicabile336= ApplicationResources.getMessageResourceString(context.getApplication().getMessageBundle(), "testoFlgNonApplicabile336", null, context.getViewRoot().getLocale());
        	if (modello.getFlgAscensore()!= null && modello.getFlgAscensore().equals(new Boolean(true)))
				dotatoAscensore= ApplicationResources.getMessageResourceString(context.getApplication().getMessageBundle(), "testoFlgAscensore", null, context.getViewRoot().getLocale());
    	
        }
        
        
//ricavo la zona censuaria
		String zonaCen = " ";
		String foglio = " ";
		 String zonaCenAll="";
		
		 if (lstDatiCensuari != null && lstDatiCensuari.size() > 0){
	        	List<String> lstZone= new ArrayList<String>();
                
               
                for (int index=0; index<lstDatiCensuari.size(); index++){
                    Object[] objs = (Object[])lstDatiCensuari.get(index);
                    zonaCen = (String)objs[3];
                   
                    	String zonaCenNew="";
                		
                		if (zonaCen != null){
                    		try{
                    			Integer zonaCenInt= Integer.valueOf(zonaCen);
                    			zonaCenNew= String.valueOf(zonaCenInt);
                    		}catch(Exception e){
                    			zonaCenNew= zonaCen;
                    		}
                		}
                		if (zonaCenNew!= null && !zonaCenNew.equals("") && !lstZone.contains(zonaCenNew)){ 	
                    	lstZone.add(zonaCenNew);
                    }
                }
                
                for (int z=0; z< lstZone.size(); z++){
                	if (z != lstZone.size() -1)
                		zonaCenAll= zonaCenAll + lstZone.get(z)+ " - ";
                	else
                		zonaCenAll= zonaCenAll + lstZone.get(z);
                }
		 }
		 
		//ricavo la microzona
 		String microzona="";
		 
		 if (lstDatiCensuari != null && lstDatiCensuari.size() > 0){
                
                Hashtable<String, String> htMz = new Hashtable<String, String>();
                for (int index=0; index<lstDatiCensuari.size(); index++){
                    Object[] objs = (Object[])lstDatiCensuari.get(index);
                    zonaCen = (String)objs[3];
                    foglio = (String)objs[0];
                    List<FoglioMicrozonaDTO> lstMz = this.getFoglioMicrozona(foglio, zonaCen);
                    if ( lstMz != null && lstMz.size()>0){
                       // Object[] microzonaArr= (Object[])lstMz.get(0);
                       //String mz = (String)microzonaArr[2];
                    	String mz = lstMz.get(0).getNewMicrozona();
                        htMz.put(mz, mz);
                    }
                }
                
                Enumeration<String> eMz = htMz.elements();
                while(eMz.hasMoreElements()){
                    microzona += eMz.nextElement() + ",";
                }
    	        
                if (!microzona.equals("")){
	                int lastVirgola= microzona.lastIndexOf(",");
	        		microzona= microzona.substring(0, lastVirgola);
                }
	        }
		 
		 //ricavo le coordinate catastali delle uiu non soppresse
		 Hashtable htMicroZona = new Hashtable();
         StringBuffer sb = new StringBuffer(100);
         StringBuffer sbCoord = new StringBuffer(100);
         if (lstUiu != null && lstUiu.size()>0){
        	List<Object> listUiuNonS = new ArrayList<Object>();
        	for (int index=0; index<lstUiu.size(); index++){
        		Object[] uiu = (Object[])lstUiu.get(index);
        		if (!((String)uiu[0]).trim().equals("S")){
        			listUiuNonS.add(uiu);
        		}
        	}
        	
        	ArrayList<String> subalterni = new ArrayList<String>();
        	ArrayList<String[]> coordCatastali = new ArrayList<String[]>();
        	for (int index=0; index<listUiuNonS.size(); index++){
        		Object[] uiu = (Object[])listUiuNonS.get(index);
        		
        		
        		String via= (String)uiu[5];
        		String civico= (String)uiu[6];
        		String civicoNew="";
        		
        		if (civico != null){
            		try{
            			Integer civicoInt = Integer.valueOf(civico);
            			civicoNew = String.valueOf(civicoInt);
            		}catch(Exception e){
            			civicoNew = civico;
            		}
        		}
        		
        		foglio= (String)uiu[1];
        		String foglioNew="";
        		
        		if (foglio != null){
            		try{
            			Integer foglioInt= Integer.valueOf(foglio);
            			foglioNew= String.valueOf(foglioInt);
            		}catch(Exception e){
            			foglioNew= foglio;
            		}
        		}
        		
        		String particella= (String)uiu[2];
        		String particellaNew="";
        		
        		if (particella != null){
            		try{
            			Integer particellaInt= Integer.valueOf(particella);
            			particellaNew= String.valueOf(particellaInt);
            		}catch(Exception e){
            			particellaNew= particella;
            		}
        		}
        		
        		String subalterno= (String)uiu[3];
        		String subalternoNew="";
        		
        		if (subalterno != null){
            		try{
            			Integer subalternoInt= Integer.valueOf(subalterno);
            			subalternoNew= String.valueOf(subalternoInt);
            		}catch(Exception e){
            			subalternoNew= subalterno;
            		}
        		}
        		
        		subalterni.add(subalternoNew);
        		String[] fms = new String[]{foglioNew, particellaNew, subalternoNew};
        		coordCatastali.add(fms);
        		
        		if (index == listUiuNonS.size() - 1) {
                    sb.append(" \r - "+Character.pulisciVia(via)  +" " + civicoNew +  " individuato al C.d.F. al Foglio " + foglioNew + ", Particella " + particellaNew + ", Sub. ");
                    for (int iSub = 0; iSub < subalterni.size(); iSub++) {
                    	if (iSub > 0) {
                    		sb.append(" - ");
                    	}
                    	sb.append(subalterni.get(iSub));
                    }
                    sb.append("\r");
        		} else {
        			Object[] uiuSucc = (Object[])listUiuNonS.get(index + 1);
        			if (!(((String)uiu[4]).trim().equals(((String)uiuSucc[4]).trim()) &&
        					((String)uiu[1]).trim().equals(((String)uiuSucc[1]).trim()) &&
        					((String)uiu[2]).trim().equals(((String)uiuSucc[2]).trim()))) {
        				sb.append(" \r - "+Character.pulisciVia(via)  +" " + civicoNew +  " individuato al C.d.F. al Foglio " + foglioNew + ", Particella " + particellaNew+ ", Sub. ");
                        for (int iSub = 0; iSub < subalterni.size(); iSub++) {
                        	if (iSub > 0) {
                        		sb.append(" - ");
                        	}
                        	sb.append(subalterni.get(iSub));
                        }
                        //sb.append("\r");
                        subalterni = new ArrayList<String>();
        			}
        		}
        	}
        	
        	 if (coordCatastali != null && coordCatastali.size() > 0){
      			for (int iCoo=0; iCoo<coordCatastali.size(); iCoo++){
      				String[] coord = coordCatastali.get(iCoo);
      				sbCoord.append(" \r - Foglio " + coord[0] + ", Particella " + coord[1] + ", Sub. " + coord[2]);
      			}
      		}
         }
         
        
		 
		Range range = doc.getRange();
		String plainText = range.text(); 
		StyleSheet styleSheet = doc.getStyleSheet();

	    for (int i=0; i<range.numParagraphs(); i++){
	        Paragraph p = range.getParagraph(i);
	        CharacterRun cr = p.getCharacterRun(0);
	        try{
	        String oldText = p.text();
	        logger.info(oldText);
	        
	        String newText = Character.replace(oldText, "#DATA_OGGI#", tc.shortFormat(new Timestamp(System.currentTimeMillis())));
		       
	        newText = Character.replace(newText, "#PROTOCOLLO#", selBodBean.getProtocollo());

	        newText = Character.replace(newText, "#FORNITURA_MMAAAA#", selBodBean.getFornitura());
	        
	        newText = Character.replace(newText, "#DATA_FORNITURA#", "01/" + selBodBean.getFornitura());
	         
	        String dataVariazione = selBodBean.getDataVariazione();
	        if (dataVariazione == null || dataVariazione.trim().equals("")) {
	        	dataVariazione = selBodBean.getFornitura();
	        }
	        newText = Character.replace(newText, "#DATA_VARIAZIONE#", dataVariazione);
	        
	        newText = Character.replace(newText, "#MICROZONA#", microzona);
    	        
	        newText = Character.replace(newText, "#ZONA#", zonaCenAll);       
	            
	        newText = Character.replace(newText, "#OGGETTODOMANDA#", oggettoDomanda);      
	            
	        newText = Character.replace(newText, "#TIPOINTERVENTO#", tipoIntervento);      
	            
	        newText = Character.replace(newText, "#OGGETTO#", oggetto);      
	            
	        newText = Character.replace(newText, "#NUMPROGRESSIVO#", numProgressivo);      
	            
	        newText = Character.replace(newText, "#NUMPROTOCOLLO#", numProtocollo);      
	            
	        newText = Character.replace(newText, "#NOTE#", note);      
	            
	        newText = Character.replace(newText, "#SETTORE#", settore);     
	            
	        newText = Character.replace(newText, "#DESTINAZIONEZONA#", destinazioneZona);     
	            
	        newText = Character.replace(newText, "#SIGNIFICATIVAPRESENZA#", significativaPresenza);     
	            
	        newText = Character.replace(newText, "#DOTEASCENSORE#", dotatoAscensore);   
	            
	        newText = Character.replace(newText, "#DESTINAZIONEUIU#", destinazioneUiu);   
	            
	        newText = Character.replace(newText, "#CATEGORIA#", categoriaProposta); 
	            
	        newText = Character.replace(newText, "#CLASSE#", classeProposta); 
	            
	        newText = Character.replace(newText, "#TESTOFLG662#", richiesta662);   
	            
	        newText = Character.replace(newText, "#TESTOALLINEAMENTOUIU#", allineamentoUiu);   
	            
	        newText = Character.replace(newText, "#TESTONONAPPLICABILE336#", nonApplicabile336);   
	            
	        newText = Character.replace(newText, "#INDIRIZZO_CIVICO_COORDINATECATASTALI#", sb.toString());
	        
	        newText = Character.replace(newText, "#DESCRIZIONE_ENTE#", this.descrizioneEnte);
	        
	        newText = Character.replace(newText, "#ANNOTAZIONI#", annotazioni);
	        
	        newText = Character.replace(newText, "#COORDINATECATASTALI#", sbCoord.toString());
	        
	        //logger.info(newText);
	        
		       /* if (cr.text().trim().startsWith("Immobile sito in Milano")){
	                StyleDescription paragraphStyle = styleSheet.getStyleDescription(p.getStyleIndex());
	                //String styleName = paragraphStyle.getName(); 
	                 CharacterProperties prop = cr.cloneProperties();
	                 
	                 CharacterRun run = p.insertAfter("\r" + sb.toString(), prop);
	            }
		        */
		        p.replaceText(oldText, newText, 0);
		        
	        }
	        
		    
	        catch(Exception e){
	        	logger.info(e.getMessage());
	        }   
	    }
	   
		
		doc.write(docOutC);
		docOutC.close();
		
	}
	
	private void caricaAllegatoB() throws IOException{
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		/*
		 * NOTA BENE: se bisogna aggiungere altre informazioni "custom" nel documento, occorre prevedere alla fine del documento template tanti spazi quanta è la lunghezza massima delle informazioni  che possono essere inserite , altrimenti viene generato un errore.
		 * 
		 * 
		 */
		
		/*
		 * Preparo il file per allegato B partendo dal template
		 */
		File fileDoc = new File(this.getPathTemplateBod() + "template_allegati_segnalazioni/allegato_b.doc");
		FileInputStream fis = new FileInputStream(fileDoc);
		BufferedInputStream bis = new BufferedInputStream(fis);
		HWPFDocument doc = new HWPFDocument(fis);
		Range range = doc.getRange();
		String plainText = range.text(); 
		StyleSheet styleSheet = doc.getStyleSheet();
		
		//carico informazioni sulle proposte di classamento
        
        BodModello662Bean modello= this.getModello662();
        String proposte="";
        String proposteAll="";
        String ascensore="";
        String allineamento= "";
        String coordinate="";
        
        
        List<String> listaProposte= new ArrayList<String>();
        
       
        if (modello != null){
        	
        	if (modello.getCatResMin8()!= null && !modello.getCatResMin8().equals("") && modello.getClasseResMin8()!= null && !modello.getClasseResMin8().equals("")){
        		proposte="-per le u.i.u. a destinazione \"residenziale\"  < 8 vani la categoria "+ modello.getCatResMin8() + " classe " + modello.getClasseResMin8() ;
        		listaProposte.add(proposte);
        	}
        	
        	if (modello.getCatResMag8()!= null && !modello.getCatResMag8().equals("") && modello.getClasseResMag8()!= null && !modello.getClasseResMag8().equals("")){
        		proposte="-per le u.i.u. a destinazione \"residenziale\"  >= 8 vani la categoria "+ modello.getCatResMag8() + " classe " + modello.getClasseResMag8() ;
        		listaProposte.add(proposte);
        	}
        	
        	if (modello.getClasseUff()!= null && !modello.getClasseUff().equals("")){
        		proposte= "-per le u.i.u. a destinazione \"ufficio\" la categoria A10"  + " classe " + modello.getClasseUff() ;
        		listaProposte.add(proposte);
        	}
        	
        	if (modello.getClasseNeg()!= null && !modello.getClasseNeg().equals("")){
        		proposte=  "-per le u.i.u. a destinazione \"negozio\" la categoria C01"  + " classe " + modello.getClasseNeg() ;
        		listaProposte.add(proposte);
        	}
        	
   

        	if (modello.getClasseLab()!= null && !modello.getClasseLab().equals("")){
        		proposte=  "-per le u.i.u. a destinazione \"laboratorio\" la categoria C03" + " classe " + modello.getClasseLab();
        		listaProposte.add(proposte);
        	}
        	
        	if (modello.getClasseDep()!= null && !modello.getClasseDep().equals("")){
        		proposte= "-per le u.i.u. a destinazione \"deposito\" la categoria C02" + " classe " + modello.getClasseDep() ;
        		listaProposte.add(proposte);
        	}
        	
        	if (modello.getClasseBox()!= null && !modello.getClasseBox().equals("")){
        		proposte= "-per le u.i.u. a destinazione \"box\" la categoria C06" + " classe " + modello.getClasseBox() ;
        		listaProposte.add(proposte);
        	}
        	
        	if (modello.getClassePAuto()!= null && !modello.getClassePAuto().equals("")){
        		proposte=  "-per le u.i.u. a destinazione \"posti auto\" la categoria C06" + " classe " + modello.getClassePAuto();
        		listaProposte.add(proposte);
        	}
        	
        	
        	
        	int numProposte= listaProposte.size();
        	
        	for(int j= 0; j< numProposte; j++){
        		
        		if (j != numProposte -1)
        			proposteAll= proposteAll + listaProposte.get(j) + ";\r ";
        		else
        			proposteAll= proposteAll + listaProposte.get(j) + ".\r ";
        		
        		
        	}
        	
        	 
 	        
 	        if (modello.getFlgAscensore()!= null &&  modello.getFlgAscensore().equals(new Boolean(true))){
 	        	
 	        	ascensore = " dotato di impianto ascensore,";
 	        }
 	        
 	        if (modello.getFlgAllineamento()!= null && modello.getFlgAllineamento().equals(new Boolean(true)))
 	        	allineamento=ApplicationResources.getMessageResourceString(context.getApplication().getMessageBundle(), "testoFlgAllineamentoUIU", null, context.getViewRoot().getLocale());
 	        	
 	        
 	        	
 	        	
        }
        
       
        String zonaCenAll="";
        
      //ricavo la zona censuaria
        
        if (lstDatiCensuari != null && lstDatiCensuari.size() > 0){
        	List<String> lstZone= new ArrayList<String>();
            String zonaCen = " ";
            
            for (int index=0; index<lstDatiCensuari.size(); index++){
                Object[] objs = (Object[])lstDatiCensuari.get(index);
                zonaCen = (String)objs[3];
                
                
                
                	
                	String zonaCenNew="";
            		
            		if (zonaCen != null){
                		try{
                			Integer zonaCenInt= Integer.valueOf(zonaCen);
                			zonaCenNew= String.valueOf(zonaCenInt);
                		}catch(Exception e){
                			zonaCenNew= zonaCen;
                		}
            		}
            	if (zonaCenNew!= null && !zonaCenNew.equals("") && !lstZone.contains(zonaCenNew)){ 	
                	lstZone.add(zonaCenNew);
                }
            }
            
            for (int z=0; z< lstZone.size(); z++){
            	if (z != lstZone.size() -1)
            	zonaCenAll= zonaCenAll + lstZone.get(z)+ " - ";
            	else
            		zonaCenAll= zonaCenAll + lstZone.get(z);
            }
            
                       
        }
        
        
        
        //carico l'indirizzo e le coordinate catastali
        
        Hashtable htMicroZona = new Hashtable();
        StringBuffer sb = new StringBuffer(100);	                                  
        if (lstUiu != null && lstUiu.size()>0){
        	List<Object> listUiuNonS = new ArrayList<Object>();
        	for (int index=0; index<lstUiu.size(); index++){
        		Object[] uiu = (Object[])lstUiu.get(index);
        		if (!((String)uiu[0]).trim().equals("S")){
        			listUiuNonS.add(uiu);
        		}
        	}
        	ArrayList<String> subalterni = new ArrayList<String>();
        	for (int index=0; index<listUiuNonS.size(); index++){
        		Object[] uiu = (Object[])listUiuNonS.get(index);
        		
        		String via= (String)uiu[5];
        		String civico= (String)uiu[6];
        		String civicoNew="";
        		
        		if (civico != null){
            		try{
            			Integer civicoInt= Integer.valueOf(civico);
            			civicoNew= String.valueOf(civicoInt);
            		}catch(Exception e){
            			civicoNew= civico;
            		}
        		}
        		
        		String foglio= (String)uiu[1];
        		String foglioNew="";
        		
        		if (foglio != null){
            		try{
            			Integer foglioInt= Integer.valueOf(foglio);
            			foglioNew= String.valueOf(foglioInt);
            		}catch(Exception e){
            			foglioNew= foglio;
            		}
        		}
        		
        		String particella= (String)uiu[2];
        		String particellaNew="";
        		
        		if (particella != null){
            		try{
            			Integer particellaInt= Integer.valueOf(particella);
            			particellaNew= String.valueOf(particellaInt);
            		}catch(Exception e){
            			particellaNew= particella;
            		}
        		}
        		
        		String subalterno= (String)uiu[3];
        		String subalternoNew="";
        		
        		if (subalterno != null){
            		try{
            			Integer subalternoInt= Integer.valueOf(subalterno);
            			subalternoNew= String.valueOf(subalternoInt);
            		}catch(Exception e){
            			subalternoNew= subalterno;
            		}
        		}
        		
        		subalterni.add(subalternoNew);
        		
        		//ricavo la microzona
        		String microzona="";
        		String zonaCensuaria = "";
        		
        		if (lstDatiCensuari != null && lstDatiCensuari.size() > 0){
	                
	                Hashtable<String, String> htMz = new Hashtable<String, String>();
	                for (int j=0; j<lstDatiCensuari.size(); j++){
	                    Object[] objs = (Object[])lstDatiCensuari.get(j);
	                   
	                    String foglioCen = (String)objs[0];
	                    String particellaCen= (String)objs[1];
	                    String subalternoCen= (String)objs[2];
	                    if (foglioCen!= null && foglioCen.equals(foglio) && particellaCen!= null && particellaCen.equals(particella) && subalternoCen!= null && subalternoCen.equals(subalterno)){
	                    	zonaCensuaria = (String)objs[3];
	                    	
	                    	String zonaCenNew="";
	                		
	                		if (zonaCensuaria != null){
	                    		try{
	                    			Integer zonaCenInt= Integer.valueOf(zonaCensuaria);
	                    			zonaCenNew= String.valueOf(zonaCenInt);
	                    		}catch(Exception e){
	                    			zonaCenNew= zonaCensuaria;
	                    		}
	                		}
	                    	
	                    	List<FoglioMicrozonaDTO> lstMz = this.getFoglioMicrozona(foglioNew, zonaCenNew);
    	                    if ( lstMz != null && lstMz.size()>0){
    	                       //Object[] microzonaArr = (Object[])lstMz.get(0);
    	                       // String mz = (String)microzonaArr[2];
    	                    	String mz = lstMz.get(0).getNewMicrozona();
    	                        htMz.put(mz, mz);
    	                    }
	                    }
	                }
	                
	                Enumeration<String> eMz = htMz.elements();
	                while(eMz.hasMoreElements()){
	                		microzona += eMz.nextElement() + ",";
	                }

        		}
        		 if (!microzona.equals("")){
		                int lastVirgola= microzona.lastIndexOf(",");
		        		microzona= microzona.substring(0, lastVirgola);
	                }
        		
        		if (index == listUiuNonS.size() - 1) {
                    sb.append(" \r - "+ Character.pulisciVia(via)  +" " + civicoNew + " individuato al N.C.E.U. al Foglio " + foglioNew + ", Particella " + particellaNew + ", Sub. ");
                    for (int iSub = 0; iSub < subalterni.size(); iSub++) {
                    	if (iSub > 0) {
                    		sb.append(" - ");
                    	}
                    	sb.append(subalterni.get(iSub));
                    }
                    sb.append(" - Microzona " + microzona+".");
                    sb.append("\r");
        		} else {
        			Object[] uiuSucc = (Object[])listUiuNonS.get(index + 1);
        			if (!(((String)uiu[4]).trim().equals(((String)uiuSucc[4]).trim()) &&
        					((String)uiu[1]).trim().equals(((String)uiuSucc[1]).trim()) &&
        					((String)uiu[2]).trim().equals(((String)uiuSucc[2]).trim()))) {
        				sb.append(" \r - "+Character.pulisciVia(via) +" " + civicoNew  + " individuato al N.C.E.U. al Foglio " + foglioNew + ", Particella " + particellaNew + ", Sub. ");
                        for (int iSub = 0; iSub < subalterni.size(); iSub++) {
                        	if (iSub > 0) {
                        		sb.append(" - ");
                        	}
                        	sb.append(subalterni.get(iSub));
                        }
                        sb.append(" - Microzona " + microzona+ ".");
                        //sb.append("\r");
                        subalterni = new ArrayList<String>();
        			}
        		}
        	}
        }

	    for (int i=0; i<range.numParagraphs(); i++){
	        Paragraph p = range.getParagraph(i);
	        CharacterRun cr = p.getCharacterRun(0);
	        String oldText="";
	        try{
	         oldText = p.text();
	        
	        logger.info(oldText);
	        String newText = Character.replace(oldText, "#DATA_OGGI#", tc.shortFormat(new Timestamp(System.currentTimeMillis())));
	       
	        newText = Character.replace(newText, "#DOTE#", ascensore);
	        
	        newText = Character.replace(newText, "#ALLINEAMENTO#", allineamento);
	        
	        
            newText = Character.replace(newText, "#ZONA#", zonaCenAll);                     
            
            
            if (cr.text().trim().startsWith("Immobile sito in Milano")){
                StyleDescription paragraphStyle = styleSheet.getStyleDescription(p.getStyleIndex());
                //String styleName = paragraphStyle.getName(); 
                CharacterProperties prop = cr.cloneProperties();
                
                CharacterRun run = p.insertAfter("\r" + sb.toString(), prop);
                
            }
            
	    
	        
            newText = Character.replace(newText, "#COORDINATECATASTALI#", sb.toString());
            
	        newText = Character.replace(newText, "#PROPOSTECLASSAMENTO#", proposteAll.trim());
	       
	        p.replaceText(oldText, newText, 0);
	    }
	        
	    
        catch(Exception e){
        	logger.info(e.getMessage());
        }
        
	    }
		String tempFileDocB = sessionId + "_allegato_b.doc";		
		tempPathDocB = this.getRootPathEnte() + this.TEMPORARY_FILES +"/" + tempFileDocB;
		FileOutputStream docOutB = new FileOutputStream(tempPathDocB);
		doc.write(docOutB);
		docOutB.close();
		
	}
	
	public void initTab3(){
		/*
		 * Informazioni di upload inizializate
		 */
		bodUploadSwitch = new BodUploadSwitch();
//		bodUpload = new BodUploadBean();
		/*
		 * Informazioni della segnalazione inizializzate
		 */
//		bodSegnalazione = new BodSegnalazioneBean();
//		bodSegnalazione.setCodiceEnte(codiceEnte);
//		bodSegnalazione.setProtocollo(selBodBean.getProtocollo());
		bodSegnalazioneSwitch = new BodSegnalazioneSwitch();
		/*
		 * Inizializzo i componenti dell'istruttoria prelevandoli dal db se è già 
		 * stata presa in carico
		 */
		txtAnnotazioni = new HtmlInputTextarea();
		cbxEsitiIstruttoria = new HtmlSelectManyCheckbox();
		bodIstruttoriaSwitch = new BodIstruttoriaSwitch();
		//bodIstruttoriaSwitch.setDisabledXmlLink(true);
	}//-------------------------------------------------------------------------
	
	public void radioControl(ActionEvent event) {
		String objId = ((HtmlSelectOneRadio)((HtmlAjaxSupport)event.getSource()).getParent()).getId().toString();
		logger.info("radioControl-obj "+objId);
		if (objId.equalsIgnoreCase("rdoCoerente")) {
			esitoControlloCoerente = "COERENTE";
			esitoControlloIncoerente = "";
			esitoControlloAssenza = "";
		} else if (objId.equalsIgnoreCase("rdoIncoerente")) {
			esitoControlloCoerente = "";
			esitoControlloComDoc = "";
			esitoControlloIncoerente = "INCOERENTE";
			esitoControlloAssenza = "";
		} else if (objId.equalsIgnoreCase("rdoAssenza")) {
			esitoControlloCoerente = "";
			esitoControlloComDoc = "";
			esitoControlloIncoerente = "";
			esitoControlloAssenza = "ASSENZA";
		}
		bodSegnalazioneSwitch.setDisabledButtonSave(false);
	}//-------------------------------------------------------------------------

	public void uploadListener(UploadEvent event){
		//logger.info("uploadListener");
		try{
			UploadItem item = event.getUploadItem();
			int start = item.getFileName().lastIndexOf('\\');
			int end =item.getFileName().length();
			String fileName = "";
			int fileSize = item.getFileSize();
			
			if (start != -1 && end != -1)
				fileName = item.getFileName().substring(start+1, end);
			else
				fileName = item.getFileName();
	//		int startEst = fileName.lastIndexOf('.');
	//		String est = fileName.substring(startEst);
			
			bodUpload = new BodUploadBean();
			bodUpload.setName(fileName);
			bodUpload.setSize(new Double(fileSize));
			
			String absolutePath = "";
			/*
			 * Cartelle di destinazione del file uploadato e zippato per saperne le 
			 * dimensioni e memorizzarle nel db 
			 */
			String zipTemporaryPath = this.getRootPathEnte() + this.TEMPORARY_FILES +"/" + sessionId;
	
			File file = item.getFile();
			absolutePath = file.getAbsolutePath();

			logger.info("File: " + absolutePath);
			
			bodUpload.setZipTemporaryPath(zipTemporaryPath + "/outFile.zip");			
			bodUpload.setAbsolutePathSource(absolutePath);
			/*
			 * Zip del file uploadato per calcolarne le dimensioni che mi serviranno
			 * per generare le forniture zip
			 */
			// definiamo l'output previsto che sarà un file in formato zip
			File zipFolder = new File(zipTemporaryPath);
			boolean creato = Schiavo.zipper(zipTemporaryPath, absolutePath, fileName);
			if (creato){
		    	File zipFile = new File(zipTemporaryPath + "/outFile.zip");
		    	bodUpload.setZipSize(new Double(zipFile.length()));
			    zipFile.delete();
			    zipFolder.delete();
			    
			    bodUploadSwitch.setFlgHideAddAllegatoC(true);
		    }
			    
			/*}else{
				if (fileSize <= 4194304)
					bodUploadSwitch.setMostraMsgTypeError(true);
				else
					bodUploadSwitch.setMostraMsgSizeError(true);
			}*/
			
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}//-------------------------------------------------------------------------
		
	public void uploadListenerB(UploadEvent event){
		//logger.info("uploadListener");
		try{
			UploadItem item = event.getUploadItem();
			int start = item.getFileName().lastIndexOf('\\');
			int end =item.getFileName().length();
			String fileName = "";
			int fileSize = item.getFileSize();
			
			if (start != -1 && end != -1)
				fileName = item.getFileName().substring(start+1, end);
			else
				fileName = item.getFileName();
	//		int startEst = fileName.lastIndexOf('.');
	//		String est = fileName.substring(startEst);
			
			bodUploadB = new BodUploadBean();
			bodUploadB.setName(fileName);
			bodUploadB.setSize(new Double(fileSize));
			
	//		if (fileSize <= 4718592 && (fileName.toLowerCase().endsWith("doc") || fileName.toLowerCase().endsWith("pdf")) ){
			String absolutePath = "";
			/*
			 * Cartelle di destinazione del file uploadato e zippato per saperne le 
			 * dimensioni e memorizzarle nel db (=questa informazione servirà poi alla
			 * routine di esportazione dei files XML/ZIP che dovranno essere inferiori
			 * a 5 Mb)
			 */
			String zipTemporaryPath = this.getRootPathEnte() + this.TEMPORARY_FILES +"/" + sessionId;
	
			File file = item.getFile();
			absolutePath = file.getAbsolutePath();

			logger.info("File: " + absolutePath);
			
			bodUploadB.setZipTemporaryPath(zipTemporaryPath + "/outFile.zip");			
			bodUploadB.setAbsolutePathSource(absolutePath);
			/*
			 * Zip del file uploadato per calcolarne le dimensioni che mi serviranno
			 * per generare le forniture zip
			 */
			// definiamo l'output previsto che sarà un file in formato zip
			File zipFolder = new File(zipTemporaryPath);
			boolean creato = Schiavo.zipper(zipTemporaryPath, absolutePath, fileName);
			if (creato){
		    	File zipFile = new File(zipTemporaryPath + "/outFile.zip");
		    	bodUploadB.setZipSize(new Double(zipFile.length()));
			    zipFile.delete();
			    zipFolder.delete();
			    
			    bodUploadSwitch.setFlgHideAddAllegatoB(true);
		    }
			    
			/*}else{
				if (fileSize <= 4194304)
					bodUploadSwitch.setMostraMsgTypeError(true);
				else
					bodUploadSwitch.setMostraMsgSizeError(true);
			}*/
			
			
			
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void showUpload(){
		//logger.info("showUpload");
		//bodUploadSwitch = new BodUploadSwitch();
		//bodUpload = new BodUploadBean();
		bodUploadSwitch.setAcceptedTypes("doc,pdf");
		bodUploadSwitch.setMaxFileQuantity(1d);
		/*
		 * Stabilito il limite massimo a 4,5 Mb
		 */
		bodUploadSwitch.setMaxFileSize(4718592d);
		bodUploadSwitch.setUseFlash(true);
		bodUploadSwitch.setAutoUpload(true);
		/*
		 * Se l'utente corrente non è autorizzato disabilito il pulsante di conferma
		 * per l'upload
		 */
		if ( !Schiavo.usersComparator(utente.getName().trim(), bodIstruttoria.getOperatore().trim())){
			bodUploadSwitch.setDisabledButtonSave(true);			
		}
		
		 /* Recupero le info sul file uploadato se esistente
		 */
		Set<BodSegnalazioneBean> segnalazioni = bodIstruttoria.getSegnalazioni();
		if (segnalazioni != null) {
			Iterator<BodSegnalazioneBean> itSeg = segnalazioni.iterator();
			while(itSeg.hasNext())
				bodSegnalazione = itSeg.next();
		}		
		
		bodUpload = new BodUploadBean();
		
		 bodUploadSwitch.setFlgHideAddAllegatoC(false);
		
		if (bodSegnalazione != null){
			
			if (bodSegnalazione.getNomeFile()!= null  && !bodSegnalazione.getNomeFile().equals("")){
				bodUpload.setSize(bodSegnalazione.getFileSize());	    
			    bodUpload.setZipSize(bodSegnalazione.getZipFileSize());
			    bodUpload.setName(bodSegnalazione.getNomeFile());
			    
			    bodUploadSwitch.setFlgHideAddAllegatoC(true);
			}
		}
		

	}//-------------------------------------------------------------------------
	
	
	public void showUploadB(){
		//logger.info("showUpload");
		//bodUploadSwitch = new BodUploadSwitch();
		//bodUpload = new BodUploadBean();
		bodUploadSwitch.setAcceptedTypes("doc,pdf");
		bodUploadSwitch.setMaxFileQuantity(1d);
		/*
		 * Stabilito il limite massimo a 4,5 Mb
		 */
		bodUploadSwitch.setMaxFileSize(4718592d);
		bodUploadSwitch.setUseFlash(true);
		bodUploadSwitch.setAutoUpload(true);
		/*
		 * Se l'utente corrente non è autorizzato disabilito il pulsante di conferma
		 * per l'upload
		 */
		if ( !Schiavo.usersComparator(utente.getName().trim(), bodIstruttoria.getOperatore().trim())){
			bodUploadSwitch.setDisabledButtonSave(true);			
		}
		
		 /* Recupero le info sul file uploadato se esistente
		 */
		Set<BodAllegatiBean> allegati = bodIstruttoria.getAllegati();
		if (allegati != null) {
			Iterator<BodAllegatiBean> itAll = allegati.iterator();
			while(itAll.hasNext())
				bodAllegato = itAll.next();
		}		
		
		bodUploadB = new BodUploadBean();
		
		bodUploadSwitch.setFlgHideAddAllegatoB(false);
		
		if (bodAllegato != null){
			
			if (bodAllegato.getNomeFile()!= null  && !bodAllegato.getNomeFile().equals("")){
				bodUploadB.setSize(bodAllegato.getFileSize());	    
			    bodUploadB.setZipSize(bodAllegato.getZipFileSize());
			    bodUploadB.setName(bodAllegato.getNomeFile());
			    
			    bodUploadSwitch.setFlgHideAddAllegatoB(true);
			}
		}
		

	}//-------------------------------------------------------------------------
	
	public void clearUploadData() throws Exception {
		//logger.info("clearUploadData");
		bodUpload = new BodUploadBean();
		
		//cancello il file dal file system e gli attributi relativi al file
			if ( bodSegnalazione!= null && bodSegnalazione.getNomeFile()!= null  && !bodSegnalazione.getNomeFile().equals("")){
				File fileUpload = new File(bodSegnalazione.getNomeFile());
		       	fileUpload.delete();
		       	
		       	bodSegnalazione.setNomeFile("");
		       	bodSegnalazione.setFileSize(0D);
		       	bodSegnalazione.setZipFileSize(0D);
				
		       	
		       	
				ArrayList<BodSegnalazioneBean> alSegnalazioni = new ArrayList<BodSegnalazioneBean>();
				alSegnalazioni.add(bodSegnalazione);
				bodIstruttoria.setSegnalazioni(new HashSet<BodSegnalazioneBean>(alSegnalazioni));
				Long istLastId = new Long(0);
				if (bodIstruttoria.getIdIst() != null && bodIstruttoria.getIdIst() > 0){
					BodIstruttoriaBean bib = (BodIstruttoriaBean)bodLogicService.updItem(bodIstruttoria, BodIstruttoriaBean.class);
					if (bib != null)
						istLastId = bib.getIdIst();
				}else{
					istLastId = bodLogicService.addItem(bodIstruttoria, BodIstruttoriaBean.class);	
				}
		    
			}
			
			bodUploadSwitch.setFlgHideAddAllegatoC(false);
			
			bodSegnalazioneSwitch.setMostraLinkAllegato(false);
		
	}//-------------------------------------------------------------------------
	
	public void clearUploadDataB() throws Exception {
		//logger.info("clearUploadData");
		bodUploadB = new BodUploadBean();
		
		
		//cancello il file dal file system e gli attributi relativi al file
		if ( bodAllegato!= null && bodAllegato.getNomeFile()!= null  && !bodAllegato.getNomeFile().equals("")){
			File fileUpload = new File(bodAllegato.getNomeFile());
	       	fileUpload.delete();
	       	
	       	bodAllegato.setNomeFile("");
	       	bodAllegato.setFileSize(0D);
	       	bodAllegato.setZipFileSize(0D);
			
	       	
	       	
			ArrayList<BodAllegatiBean> alAllegati = new ArrayList<BodAllegatiBean>();
			alAllegati.add(bodAllegato);
			bodIstruttoria.setAllegati(new HashSet<BodAllegatiBean>(alAllegati));
			Long istLastId = new Long(0);
			if (bodIstruttoria.getIdIst() != null && bodIstruttoria.getIdIst() > 0){
				BodIstruttoriaBean bib = (BodIstruttoriaBean)bodLogicService.updItem(bodIstruttoria, BodIstruttoriaBean.class);
				if (bib != null)
					istLastId = bib.getIdIst();
			}else{
				istLastId = bodLogicService.addItem(bodIstruttoria, BodIstruttoriaBean.class);	
			}
	    
		}
		
		bodUploadSwitch.setFlgHideAddAllegatoB(false);
		
		bodSegnalazioneSwitch.setMostraLinkAllegatoB(false);
	}//--
	
	public void showXmlManager(){
		/*
		 * XXX Recupero le segnalazione già inserite per questa istruttoria 
		 */
		calDataFineLavori = new CalendarBoxRch();
//		lstIstruttoriaFab = new ArrayList<BodFabbricatoBean>();
//		lstIstruttoriaUiu = new ArrayList<BodUiuBean>();
		htIstruttoriaFabbricati = new Hashtable();
		//bodSegnalazioneSwitch.setDisabledButtonSave(true);
		
		if (bodIstruttoria.getSegnalazioni() != null && bodIstruttoria.getSegnalazioni().size() > 0){
			Set<BodSegnalazioneBean> segnalazioni = bodIstruttoria.getSegnalazioni();
			Iterator<BodSegnalazioneBean> itSeg = segnalazioni.iterator();
			while(itSeg.hasNext()){
				bodSegnalazione = itSeg.next();
			}
			bodSegnalazione.setCodiceEnte(codiceEnte);
			bodSegnalazione.setProtocollo(selBodBean.getProtocollo());
			
			calDataFineLavori.setSelectedDate(bodSegnalazione.getDataFineLavori());
			
			/*
			 * Reimposto la lista dei fabbricati e delle uiu già esistenti anche
			 * se l'esito controllo è diverso da "INCOERENTE" cosi che si possa 
			 * sempre cambiare dall'uno all'altro radio button
			 */
			Set<BodUiuBean> setUiu = bodSegnalazione.getUius();
			Set<BodFabbricatoBean> setFab = bodSegnalazione.getFabbricati();
			lstIstruttoriaUiu = new ArrayList<BodUiuBean>(setUiu);
			lstIstruttoriaFab = new ArrayList<BodFabbricatoBean>(setFab);
		}
		
		esitoControlloCoerente = "";
		esitoControlloComDoc = "";
		esitoControlloIncoerente = "";
		esitoControlloAssenza = "";
		
		String esitoControllo = bodSegnalazione.getEsitoControllo();
		if (esitoControllo == null) {
			esitoControlloCoerente = "COERENTE";
		} else {
			if (esitoControllo.equalsIgnoreCase("COERENTE") || 
				esitoControllo.equalsIgnoreCase("COMPLETA") || 
				esitoControllo.equalsIgnoreCase("DOCUMENTALE")) {
				esitoControlloCoerente = "COERENTE";
			}
			if (esitoControllo.equalsIgnoreCase("COMPLETA") || 
				esitoControllo.equalsIgnoreCase("DOCUMENTALE")) {
				esitoControlloComDoc = esitoControllo;
			}
			if (esitoControllo.equalsIgnoreCase("INCOERENTE")) {
				esitoControlloIncoerente = esitoControllo;
			}
			if (esitoControllo.equalsIgnoreCase("ASSENZA")) {
				esitoControlloAssenza = esitoControllo;
			}
		}

	}//-------------------------------------------------------------------------
	
	public void showXmlManagerNew(){		
		/*
		 * Creo una nuova segnalazione
		 */
		calDataFineLavori = new CalendarBoxRch();
		lstIstruttoriaFab = new ArrayList<BodFabbricatoBean>();
		lstIstruttoriaUiu = new ArrayList<BodUiuBean>();
		htIstruttoriaFabbricati = new Hashtable();
		bodSegnalazioneSwitch.setDisabledButtonSave(false);

		bodSegnalazione = new BodSegnalazioneBean();
		bodSegnalazione.setCodiceEnte(codiceEnte);
		bodSegnalazione.setProtocollo(selBodBean.getProtocollo());
		/*
		 * Preparo la lista delle uiu distinguendo per foglio, particella e subalterno
		 * mentre la lista dei fabbricati distinti per foglio e particella
		 */
		BodUiuBean bodUiu = null;
		BodFabbricatoBean bodFabbricato = null;
		for (int ind = 0; ind < lstUiu.size(); ind++){
			bodUiu = new BodUiuBean();
			bodFabbricato = new BodFabbricatoBean();
			Object[] uiu = (Object[])lstUiu.get(ind);
			String controlloFab = uiu[1] + "_" + uiu[2];
			bodFabbricato.setFoglio( (String)uiu[1] );
			bodFabbricato.setParticella( (String)uiu[2] );
			bodFabbricato.setSezione((String)uiu[7]);
			logger.info("showXmlManagerNew - Sezione Fabbricato: " + bodFabbricato.getSezione());
			bodUiu.setFoglio( (String)uiu[1] );
			bodUiu.setParticella( (String)uiu[2] );
			bodUiu.setSubalterno( (String)uiu[3] );
			bodUiu.setSezione((String)uiu[7]);
			if (!htIstruttoriaFabbricati.containsKey(controlloFab)){
				htIstruttoriaFabbricati.put(controlloFab, bodFabbricato);
			}
			if ( uiu[0].toString().trim().equalsIgnoreCase("C") ||
				uiu[0].toString().trim().equalsIgnoreCase("V"))
				lstIstruttoriaUiu.add(bodUiu);
		}
		Enumeration<BodFabbricatoBean> eFab = htIstruttoriaFabbricati.elements();
		while(eFab.hasMoreElements()){
			lstIstruttoriaFab.add(eFab.nextElement());
		}
		
		esitoControlloCoerente = "COERENTE";
		esitoControlloComDoc = "";
		esitoControlloIncoerente = "";
		esitoControlloAssenza = "";

	}//-------------------------------------------------------------------------
	
	public String saveUpload() throws Exception{
		String esito = "ricercaDettaglioBck.saveUpload";
		/*
		 * Recupero le info sul file uploadato se esistente
		 */
		Set<BodSegnalazioneBean> segnalazioni = bodIstruttoria.getSegnalazioni();
		if (segnalazioni != null) {
			Iterator<BodSegnalazioneBean> itSeg = segnalazioni.iterator();
			while(itSeg.hasNext())
				bodSegnalazione = itSeg.next();
		}		
		/*
		 * Se non esiste la segnalazione ne istanzio una nuova impostando i valori
		 * principali
		 */
	    if (bodSegnalazione == null){
	    	bodSegnalazione = new BodSegnalazioneBean();
	    	bodSegnalazione.setCodiceEnte(codiceEnte);
	    	bodSegnalazione.setProtocollo(selBodBean.getProtocollo());
	    }
	    if (bodUpload != null){
		    bodSegnalazione.setFileSize(bodUpload.getSize());	    
		    bodSegnalazione.setNomeFile(bodUpload.getAbsolutePathSource());
		    bodSegnalazione.setZipFileSize(bodUpload.getZipSize());
	    }
		/*
		 * Memorizzo il file doc allegato se esiste
		 */
		if (bodUpload != null && !bodUpload.getAbsolutePathSource().trim().equalsIgnoreCase("")){
			/*
			 * Il file doc/pdf di cui è stato fatto l'upload verrà rinominato utilizzando
			 * le informazioni seguenti data fornitura e protocollo: anno_mese_protocollo.doc/pdf
			 */
			//individuo l'estensione
			String absolutePath = bodUpload.getAbsolutePathSource();
			int startEst = bodUpload.getName().lastIndexOf('.');
			String est = bodUpload.getName().substring(startEst);
			String mm = tc.getMonth(new Timestamp(bodIstruttoria.getFornitura().getTime()));
			String aaaa = tc.getYear(new Timestamp(bodIstruttoria.getFornitura().getTime()));
			String nomeFile =  aaaa + mm + "_" + bodIstruttoria.getProtocollo() + est;
			String fileNameAnnex = this.getPathAllegatiSegnalazioni() + "/" + nomeFile;
			
			
			OutputStream out = new FileOutputStream(fileNameAnnex);
			File fileUpload = new File(absolutePath);
			InputStream is = new FileInputStream(fileUpload);
			byte[] b = new byte[1024];
			int count = 0;
           	while((count = is.read(b, 0, 1024)) != -1){
            	out.write(b, 0, count);
            }
           	is.close();
           	out.flush();
           	out.close();
           	
           	bodSegnalazione.setNomeFile(fileNameAnnex);
           	fileUpload.delete();
           	
           	
    		ArrayList<BodSegnalazioneBean> alSegnalazioni = new ArrayList<BodSegnalazioneBean>();
    		alSegnalazioni.add(bodSegnalazione);
    		bodIstruttoria.setSegnalazioni(new HashSet<BodSegnalazioneBean>(alSegnalazioni));
    		Long istLastId = new Long(0);
    		if (bodIstruttoria.getIdIst() != null && bodIstruttoria.getIdIst() > 0){
    			BodIstruttoriaBean bib = (BodIstruttoriaBean)bodLogicService.updItem(bodIstruttoria, BodIstruttoriaBean.class);
    			if (bib != null)
    				istLastId = bib.getIdIst();
    		}else{
    			istLastId = bodLogicService.addItem(bodIstruttoria, BodIstruttoriaBean.class);	
    		}
    	    
    	    if (istLastId == null || istLastId == 0)
    	    	esito = "failure";
    	    else
    	    	bodSegnalazioneSwitch.setMostraLinkAllegato(true);
    	    
		}
		/*
		 * Cancello il file generato dal template dell'allegato per istruttoria
		 */
		File fileGenerateByTemplate = new File(tempPathDocC);
		fileGenerateByTemplate.delete();
		
		//clearUploadData();
		
		loadTab3();
	    
		return esito;
	}//-------------------------------------------------------------------------
	
	public String getPathAllegatiSegnalazioni(){
		this.pathAllegatiSegnalazioni = this.getRootPathEnte() + this.ALLEGATI_SEGNALAZIONI ;
		File f = new File(pathAllegatiSegnalazioni);
		if(!f.exists())
			f.mkdirs();
		
		return pathAllegatiSegnalazioni;
	}
	
	
	public String saveUploadB() throws Exception{
		String esito = "ricercaDettaglioBck.saveUploadB";
		/*
		 * Recupero le info sul file uploadato se esistente
		 */
		Set<BodAllegatiBean> allegati = bodIstruttoria.getAllegati();
		if (allegati != null) {
			Iterator<BodAllegatiBean> itAll = allegati.iterator();
			while(itAll.hasNext())
				bodAllegato = itAll.next();
		}		
		/*
		 * Se non esiste l'allegato ne istanzio una nuova impostando i valori
		 * principali
		 */
	    if (bodAllegato == null){
	    	bodAllegato = new BodAllegatiBean();
	    	bodAllegato.setCodiceEnte(codiceEnte);
	    	bodAllegato.setProtocollo(selBodBean.getProtocollo());
	    }
	    if (bodUploadB != null){
	    	bodAllegato.setFileSize(bodUploadB.getSize());	    
	    	bodAllegato.setNomeFile(bodUploadB.getAbsolutePathSource());
	    	bodAllegato.setZipFileSize(bodUploadB.getZipSize());
	    }
		/*
		 * Memorizzo il file doc allegato se esiste
		 */
		if (bodUploadB != null && !bodUploadB.getAbsolutePathSource().trim().equalsIgnoreCase("")){
			/*
			 * Il file doc/pdf di cui è stato fatto l'upload verrà rinominato utilizzando
			 * le informazioni seguenti data fornitura e protocollo: anno_mese_protocollo.doc/pdf
			 */
			//individuo l'estensione
			String absolutePath = bodUploadB.getAbsolutePathSource();
			int startEst = bodUploadB.getName().lastIndexOf('.');
			String est = bodUploadB.getName().substring(startEst);
			String mm = tc.getMonth(new Timestamp(bodIstruttoria.getFornitura().getTime()));
			String aaaa = tc.getYear(new Timestamp(bodIstruttoria.getFornitura().getTime()));
			//distinguo il nome mettendo un suffisso finale "_B"
			String fileNameAnnex =  this.getPathAllegatiSegnalazioni() + "/" + aaaa + "_" + mm + "_" + bodIstruttoria.getProtocollo() + "_B"+est;
			OutputStream out = new FileOutputStream(fileNameAnnex);
			File fileUpload = new File(absolutePath);
			InputStream is = new FileInputStream(fileUpload);
			byte[] b = new byte[1024];
			int count = 0;
           	while((count = is.read(b, 0, 1024)) != -1){
            	out.write(b, 0, count);
            }
           	is.close();
           	out.flush();
           	out.close();
           	
           	bodAllegato.setNomeFile(fileNameAnnex);
           	fileUpload.delete();
           	
           	
    		ArrayList<BodAllegatiBean> alAllegati = new ArrayList<BodAllegatiBean>();
    		alAllegati.add(bodAllegato);
    		bodIstruttoria.setAllegati(new HashSet<BodAllegatiBean>(alAllegati));
    		Long istLastId = new Long(0);
    		if (bodIstruttoria.getIdIst() != null && bodIstruttoria.getIdIst() > 0){
    			BodIstruttoriaBean bib = (BodIstruttoriaBean)bodLogicService.updItem(bodIstruttoria, BodIstruttoriaBean.class);
    			if (bib != null)
    				istLastId = bib.getIdIst();
    		}else{
    			istLastId = bodLogicService.addItem(bodIstruttoria, BodIstruttoriaBean.class);	
    		}
    	    
    	    if (istLastId == null || istLastId == 0)
    	    	esito = "failure";
    	    else
    	    	bodSegnalazioneSwitch.setMostraLinkAllegatoB(true);
    	    
		}
		/*
		 * Cancello il file generato dal template dell'allegato per istruttoria
		 */
		File fileGenerateByTemplate = new File(tempPathDocB);
		fileGenerateByTemplate.delete();
		
		//clearUploadDataB();
		
		loadTab3();
	    
		return esito;
	}
	
	public String saveSegnalazione() throws Exception{
		String esito = "ricercaDettaglioBck.saveSegnalazione";
		/*
		 * Recupero il radiobutton dell'esito
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		req = (HttpServletRequest)extContext.getRequest();
		String esitoControllo = null;
		
		if (esitoControlloCoerente != null && esitoControlloCoerente.trim().equalsIgnoreCase("COERENTE")){
			if (esitoControlloComDoc == null || 
				esitoControlloComDoc.equals("") || 
				esitoControlloComDoc.trim().equalsIgnoreCase(emptyComboItem)) {
				esitoControllo = null;
			} else {
				esitoControllo = esitoControlloComDoc.trim();
			}
			
			/*
			 * 22-08-2012: In seguito a segnalazione di Chiare, commentata la valorizzazione automatica di DatafineLavori, 
			 *          poiché il file xml non viene validato dal portale dell'agenzia se presente il tag Ultimazione Lavori
			*/
			
			//bodSegnalazione.setDataFineLavori(new Date());
			
		}else if (esitoControlloIncoerente != null && esitoControlloIncoerente.trim().equalsIgnoreCase("INCOERENTE")){
			esitoControllo = esitoControlloIncoerente;
			bodSegnalazione.setDataFineLavori(calDataFineLavori.getSelectedDate());
			
		}else if (esitoControlloAssenza != null && esitoControlloAssenza.trim().equalsIgnoreCase("ASSENZA")){
			esitoControllo = esitoControlloAssenza;
		}else{
			esitoControllo = null;
		}
		logger.info("saveSegnalazione-esitoControllo:"+esitoControllo);
		bodSegnalazione.setEsitoControllo(esitoControllo);
		
		/*
		 * recupero fabbricati e Uius
		 */
		Set<BodFabbricatoBean> fabbricati = new HashSet<BodFabbricatoBean>();
		Set<BodUiuBean> uius = new HashSet<BodUiuBean>();
		if (lstIstruttoriaFab != null)
		fabbricati = new HashSet<BodFabbricatoBean>(lstIstruttoriaFab);
		if (lstIstruttoriaUiu!= null)
		uius = new HashSet<BodUiuBean>(lstIstruttoriaUiu);
		
		bodSegnalazione.setFabbricati(fabbricati);
		bodSegnalazione.setUius(uius);
		
		/*
		 * Update delle informazioni
		 */
		ArrayList<BodSegnalazioneBean> alSegna = new ArrayList<BodSegnalazioneBean>();
		alSegna.add(bodSegnalazione);
		bodIstruttoria.setSegnalazioni(new HashSet<BodSegnalazioneBean>(alSegna));
		BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.updItem(bodIstruttoria, BodIstruttoriaBean.class);
		if (bIstruttoria == null)
			esito = "failure";
		
		loadTab3();
		
		return esito;

	}//-------------------------------------------------------------------------
	
	public void saveIstruttoria(){
		/*
		 * Recupero l'istruttoria direttamente dal db per effettuare gli ultimi 
		 * controlli prima di chiudere la pratica
		 */
		Hashtable htQry = new Hashtable();
		List<FilterItem> filters = new ArrayList<FilterItem>();
		FilterItem filter = new FilterItem();
		filter.setAttributo("protocollo");
		filter.setOperatore("=");
		filter.setParametro("protocollo");
		filter.setValore(selBodBean.getProtocollo());
		filters.add(filter);
		
		filter = new FilterItem();
		filter.setAttributo("fornitura");
		filter.setOperatore("_IL_");
		filter.setParametro("fornitura");
		filter.setValore(tc.fromFormatStringToDate("01/" + selBodBean.getFornitura(), "00:00"));
		filters.add(filter);
		
		htQry.put("where", filters);
		
		ArrayList<BodIstruttoriaBean> lst = (ArrayList<BodIstruttoriaBean>)bodLogicService.getList(htQry, BodIstruttoriaBean.class);
		if (lst != null && lst.size() >0){
			bodIstruttoria = (BodIstruttoriaBean)lst.get(0);
			/*
			 * Controllo se l'utente che l'ha presa in carico è l'utente corrente perché
			 * è il solo autorizzato a modificarla
			 */
			/*
			 * Controllo se, in base agli esiti selezionati, ho tutta la documentazione
			 * necessaria per chiudere la pratica 
			 */
			boolean chiudi = false;
			if ( Schiavo.usersComparator(utente.getName().trim(), bodIstruttoria.getOperatore().trim())){
				/*
				 * Stesso Utente
				 */
				
				/*
				 * Per accettare la chiusura della pratica devo controllare se è stata
				 * presa in carico e gli esiti:
				 * Nessuna Anomalia esclude informazioni aggiuntive
				 * L.662 prevede la compilazione della segnalazione senza ESITO e l'upload di un doc/pdf allegato
				 * L.80 prevede la compilazione della segnalazione con ESITO, l'upload di un doc/pdf allegato
				 * L. 311 esclude informazioni aggiuntive
				 */
				if (bodIstruttoria.getEsitoIstNA()){
					bodIstruttoria.setEsitoIst311(false);
					bodIstruttoria.setEsitoIst662(false);
					bodIstruttoria.setEsitoIst80(false);
					/*
					 * No segnalazioni e no fabbricati/uiu 
					 */
					bodIstruttoria.setSegnalazioni(new HashSet<BodSegnalazioneBean>());
					chiudi = true;
				}else if ( bodIstruttoria.getEsitoIst311() || bodIstruttoria.getEsitoIst662() || bodIstruttoria.getEsitoIst80() ){
					
					boolean esitoL80 = false;
					if (bodIstruttoria.getEsitoIst80()){
						/*
						 * Controllo esistenza segnalazione senza ESITO e upload doc/pdf
						 */
						Set<BodSegnalazioneBean> segnalazioni = bodIstruttoria.getSegnalazioni();
						if (segnalazioni != null && segnalazioni.size() >0 ){
							Iterator<BodSegnalazioneBean> itSeg = segnalazioni.iterator();
							BodSegnalazioneBean segnalazione = itSeg.next();
							if ( (segnalazione.getEsitoControllo() != null && !segnalazione.getEsitoControllo().trim().equalsIgnoreCase("")) && (segnalazione.getNomeFile() != null && !segnalazione.getNomeFile().trim().equalsIgnoreCase("")) ){
								esitoL80 = true;
							}else{
								bodIstruttoriaSwitch.setMostraMsgErrorChiusuraPraticaNoAllegato(true);
							}
						}else{
							bodIstruttoriaSwitch.setMostraMsgErrorChiusuraPraticaNoAllegato(true);
						}
					}
					
					boolean esitoL662 = false;
					if (bodIstruttoria.getEsitoIst662()){
						/*
						 * Controllo esistenza segnalazione senza ESITO e upload doc/pdf
						 */
						/*Set<BodSegnalazioneBean> segnalazioni = bodIstruttoria.getSegnalazioni();
						if (segnalazioni != null && segnalazioni.size() >0 ){
							Iterator<BodSegnalazioneBean> itSeg = segnalazioni.iterator();
							BodSegnalazioneBean segnalazione = itSeg.next();
							if ( (segnalazione.getEsitoControllo() == null || segnalazione.getEsitoControllo().trim().equalsIgnoreCase("")) && (segnalazione.getNomeFile() != null && !segnalazione.getNomeFile().trim().equalsIgnoreCase("")) ){
								esitoL662 = true;
							}
						}*/
						//corretto: come per 311
						//bodIstruttoria.setSegnalazioni(new HashSet<BodSegnalazioneBean>());
						esitoL662 = true;
						logger.info("ESITO 662 TRUE");
					}
					
					boolean esitoL311 = false;
					if (bodIstruttoria.getEsitoIst311()){
						/*
						 * No segnalazioni e no fabbricati/uiu 
						 */
						//bodIstruttoria.setSegnalazioni(new HashSet<BodSegnalazioneBean>());
						esitoL311 = true;
					}
					if ( (bodIstruttoria.getEsitoIst311() && esitoL311) || (bodIstruttoria.getEsitoIst662() && esitoL662)  ){
						chiudi = true;
					}
					
					if (  (bodIstruttoria.getEsitoIst80() && esitoL80) ){
						chiudi = true;
					} else if (bodIstruttoria.getEsitoIst80() && !esitoL80){
						chiudi = false;
					}
					
	
				}
			}else{
				/*
				 * Utente diverso
				 */
				bodIstruttoriaSwitch.setMostraMsgUtenteNonAutorizzato(true);
			}
			
			if (chiudi){
				bodIstruttoria.setChiusa(true);
				BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.updItem(bodIstruttoria, BodIstruttoriaBean.class);
				if (bIstruttoria != null){
					//msgSuccess
					bodIstruttoriaSwitch.setMostraMsgIstruttoriaSave(true);
					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(false);	
					bodIstruttoriaSwitch.setMostraMsgUtenteNonAutorizzato(false);
					bodIstruttoriaSwitch.setMostraMsgErrorChiusuraPraticaNoAllegato(false);
					bodIstruttoriaSwitch.setDisabledSaveButton(true);
					msgPraticaStatus = MSG_PRATICA_CHIUSA;
				}
				else{
					//msgError
					bodIstruttoriaSwitch.setMostraMsgIstruttoriaSave(false);
					bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(false);
					bodIstruttoriaSwitch.setDisabledSaveButton(false);
					bodIstruttoriaSwitch.setMostraMsgErrorChiusuraPratica(true);
					bodIstruttoriaSwitch.setMostraMsgErrorChiusuraPraticaNoAllegato(false);
				}						
			}else{
				//msgError
				bodIstruttoriaSwitch.setMostraMsgIstruttoriaSave(false);
				bodIstruttoriaSwitch.setMostraMsgChiudiPraticaError(false);
				bodIstruttoriaSwitch.setDisabledSaveButton(false);
				
				
			}
			
		}
		
			
		Set<BodFabbricatoBean> setFabbricati = null;
		if (lstIstruttoriaFab != null){
			setFabbricati = new HashSet<BodFabbricatoBean>(lstIstruttoriaFab);
			bodSegnalazione.setFabbricati(setFabbricati);
		}

		Set<BodUiuBean> setUius = null;
		if (lstIstruttoriaUiu != null){
			setUius = new HashSet<BodUiuBean>(lstIstruttoriaUiu);
			bodSegnalazione.setUius(setUius);
		}

		logger.info("Istruttoria salvata con successo!");
		/*
		 * Ad ogni salvataggio dell'istruttoria, genero, se possibile, una nuova fornitura.
		 */
		this.generaFornitura();
	}//-------------------------------------------------------------------------
	
	public void generaFornitura(){
		/*
		 * -Controllo se la quantita di file uploadati appartenenti alle istruttorie
		 * ancora da fornire è superiore ai 4,5 Mb
		 * -In caso positivo generare il file XML e comprimerlo con gli allegati  
		 */
		/*
		 * Calcolo del numero di pratiche non ancora inserite in una fornitura
		 */
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		FilterItem fi = new FilterItem();
		fi.setAttributo("generato");
		fi.setOperatore("=");
		fi.setParametro("generato");
		fi.setValore(false);
		aryFilters.add(fi);
		fi = new FilterItem();
		fi.setAttributo("esitoIst80");
		fi.setOperatore("=");
		fi.setParametro("esitoIst80");
		fi.setValore(true);	
		aryFilters.add(fi);
		fi = new FilterItem();
		fi.setAttributo("chiusa");
		fi.setOperatore("=");
		fi.setParametro("chiusa");
		fi.setValore(true);
		aryFilters.add(fi);
		
		Hashtable htQry = new Hashtable();
		htQry.put("where", aryFilters);
		List<BodIstruttoriaBean> listaIstruttorie = bodLogicService.getList(htQry, BodIstruttoriaBean.class);
		Double totByteZipFilesSize = 0d;
		if (listaIstruttorie != null && listaIstruttorie.size() > 0){
			/*
			 * Itero per sommare le dimensioni di allegati
			 */
			Iterator<BodIstruttoriaBean> itIst = listaIstruttorie.iterator();
			while(itIst.hasNext()){
				BodIstruttoriaBean bIstruttoria = itIst.next();
				Set<BodSegnalazioneBean> setSegnalazione = bIstruttoria.getSegnalazioni();
				if (setSegnalazione != null){
					Iterator<BodSegnalazioneBean> itSeg = setSegnalazione.iterator();
					while(itSeg.hasNext()){
						BodSegnalazioneBean bSegnalazione = itSeg.next();
						if (bSegnalazione != null){
							totByteZipFilesSize += bSegnalazione.getZipFileSize();
						}
					}
				}
			}
			Double totKbZipFileSize = totByteZipFilesSize / 1024;
			logger.info("generaFornitura - totKbZipFileSize "+totKbZipFileSize);
			if (totKbZipFileSize >= 4500){
				/*
				 * Recupero il progressivo piu alto delle forniture
				 */
				String sql = "SELECT " +
				"MAX(PROGRESSIVO) as MAX_PROGRESSIVO " +
				"FROM " +
				"BOD_FORNITURE ";
				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> lstMaxPro = bodLogicService.getList(htQry);
				Object max = lstMaxPro.get(0)!=null ? lstMaxPro.get(0): "" ;
				String maxPro = Character.checkNullString(max.toString());
				if (maxPro != null && !maxPro.trim().equalsIgnoreCase("")){
					maxPro = "" + (Integer.parseInt(maxPro) + 1);
				}else{
					maxPro = "1";
				}			
				String pathXml = this.getPathXml();
				String pathFornitura = this.getPathFornitura();
				/*
				 * Genero il file XML della fornitura
				 */
				Hashtable htXml = new Hashtable();
				htXml.put("codiceEnte", codiceEnte);
				htXml.put("listaIstruttorie", listaIstruttorie);
				htXml.put("progressivo", maxPro);
				
				Hashtable htFornitura = Schiavo.createXMLfornitura(pathXml, htXml);
				String xmlFile = (String)htFornitura.get("xmlFile");
				
				logger.info("generaFornitura - xml "+xmlFile);
				/*
				 * Se la stringa termina con .xml il processo è andato a buon fine
				 */
				if (xmlFile != null && xmlFile.toLowerCase().endsWith(".xml")){
					/*
					 * Generato il file XML e recuperati eventuali allegati devo zippare
					 * il tutto.
					 */
					Long lastIdFornitura = new Long(0);
					if (htFornitura.containsKey("setFornitureDocfa")){
						Set<BodFornituraDocfaBean> fornitureDocfa = (Set<BodFornituraDocfaBean>)htFornitura.get("setFornitureDocfa");
						Iterator<BodFornituraDocfaBean> itForDocfa = fornitureDocfa.iterator();
						List<String> listaFilesForniture = new ArrayList<String>();
						boolean trovatoXML = false;
						while(itForDocfa.hasNext()){
							BodFornituraDocfaBean bForDocfa = itForDocfa.next();
							if (bForDocfa != null && bForDocfa.getPathFileName() != null && !bForDocfa.getPathFileName().equalsIgnoreCase(""))
								if (bForDocfa.getPathFileName().toLowerCase().endsWith(".xml")) {
									if (!trovatoXML) {
										trovatoXML = true;
										listaFilesForniture.add(bForDocfa.getPathFileName());
									}
								} else {
									listaFilesForniture.add(bForDocfa.getPathFileName());
								}	
						}
						
						String nomeFornitura = new File(xmlFile).getName();
						int end = nomeFornitura.indexOf('.');
						nomeFornitura = nomeFornitura.substring(0, end);
						logger.info("generaFornitura - NOME FORNITURA: " + nomeFornitura);
						boolean compresso = Schiavo.ziplus(pathFornitura, listaFilesForniture, nomeFornitura);
						/*
						 * Inserisco nel db
						 */
						BodFornituraBean fornitura = new BodFornituraBean();
						fornitura.setProgressivo(new Long(maxPro));
						fornitura.setFileName(pathFornitura + nomeFornitura + ".zip");
						fornitura.setFileSize( new Double( new File(pathFornitura + nomeFornitura + ".zip").length() ));
						fornitura.setStatus("DA SCARICARE");
						fornitura.setFornitureDocfa(fornitureDocfa);
						//fornitura.setDescrizione(FilesHandler.fromPathToName(pathFornitura + nomeFornitura + ".zip"));
						fornitura.setDescrizione(nomeFornitura + ".zip");
						
						lastIdFornitura = bodLogicService.addItem(fornitura, BodFornituraBean.class);
						/*
						 * Sposto a true il flag generato dell'istruttoria
						 */
						BodIstruttoriaBean istruttoriaBean = null;
						if (lastIdFornitura > 0 ){
							for (int index=0; index<listaIstruttorie.size(); index++){
								BodIstruttoriaBean istruttoria = listaIstruttorie.get(index);
								BodIstruttoriaBean bIstruttoria = (BodIstruttoriaBean)bodLogicService.getItemById(istruttoria.getIdIst(), BodIstruttoriaBean.class);
								bIstruttoria.setGenerato(true);
								istruttoriaBean = (BodIstruttoriaBean)bodLogicService.updItem(bIstruttoria, BodIstruttoriaBean.class);
							}
						}
//						if (istruttoriaBean != null){
//							success = true;
//							error = false;
//						}else{
//							success = false;
//							error = true;
//						}
					}// FINE if (htFornitura.containsKey("setFornitureDocfa"))
				}else{
					/*
					 * Impossibile generare il file XML
					 */
				}// FINE else per if (xmlFile != null && xmlFile.toLowerCase().endsWith(".xml"))
			}// FINE if (totKbZipFileSize >= 4.5)
		}// FINE if (listaIstruttorie != null && listaIstruttorie.size() > 0)

	}//-------------------------------------------------------------------------
	
	public void switchOutcome(){
		/*
		 * Resetto gli oggetti che mi servono per mantenere in memoria la compilazione 
		 * dell'istruttoria aspettando che poi venga salvata
		 */
		calDataFineLavori = new CalendarBoxRch();
		lstIstruttoriaFab = new ArrayList<BodFabbricatoBean>();
		lstIstruttoriaUiu = new ArrayList<BodUiuBean>();
		htIstruttoriaFabbricati = new Hashtable();
		/*
		 * Logica di controllo
		 */
		if (rdoCoerente.getValue() != null && rdoCoerente.getValue().toString().trim().equalsIgnoreCase("COERENTE")){
			if (cbxCoerenteComDoc.getState() == null || cbxCoerenteComDoc.getState().trim().equalsIgnoreCase(emptyComboItem)) {
				bodSegnalazione.setEsitoControllo(null);
			} else {
				bodSegnalazione.setEsitoControllo(cbxCoerenteComDoc.getState().trim());
			}			
		}else if (rdoIncoerente.getValue() != null && rdoIncoerente.getValue().toString().trim().equalsIgnoreCase("INCOERENTE")){
			bodSegnalazione.setEsitoControllo("INCOERENTE");
			/*
			 * Preparo la lista delle uiu distinguendo per foglio, particella e subalterno
			 * mentre la lista dei fabbricati distinti per foglio e particella
			 */
			BodUiuBean bodUiu = null;
			BodFabbricatoBean bodFabbricato = null;
			for (int ind = 0; ind < lstUiu.size(); ind++){
				bodUiu = new BodUiuBean();
				bodFabbricato = new BodFabbricatoBean();
				Object[] uiu = (Object[])lstUiu.get(ind);
				String controlloFab = uiu[1] + "_" + uiu[2];
				bodFabbricato.setFoglio( (String)uiu[1] );
				bodFabbricato.setParticella( (String)uiu[2] );
				bodFabbricato.setSezione((String)uiu[7] );
				logger.info("switchOutcome - Sezione Fabbricato: " + bodFabbricato.getSezione());
				bodUiu.setFoglio( (String)uiu[1] );
				bodUiu.setParticella( (String)uiu[2] );
				bodUiu.setSubalterno( (String)uiu[3] );
				bodUiu.setSezione((String)uiu[7]);
				if (!htIstruttoriaFabbricati.containsKey(controlloFab)){
					htIstruttoriaFabbricati.put(controlloFab, bodFabbricato);
				}
				if ( uiu[0].toString().trim().equalsIgnoreCase("C") )
					lstIstruttoriaUiu.add(bodUiu);
			}
			Enumeration<BodFabbricatoBean> eFab = htIstruttoriaFabbricati.elements();
			while(eFab.hasMoreElements()){
				lstIstruttoriaFab.add(eFab.nextElement());
			}
		}else if (rdoAssenza.getValue() != null && rdoAssenza.getValue().toString().trim().equalsIgnoreCase("ASSENZA")){
			bodSegnalazione.setEsitoControllo("ASSENZA");
		}else{
			bodSegnalazione.setEsitoControllo(null);
			logger.info("ESITO DICHIARAZIONE NON SPECIFICATO");
		}
		
	}//-------------------------------------------------------------------------
	
	public String loadTab2(){
		String esito = "ricercaDettaglioBck.loadTab2";
		
		isAutorizzatoRicerca = GestionePermessi.autorizzato(utente, nomeApplicazione, "Ricerca Bod", "Dichiarato");
		if (isAutorizzatoRicerca){
			//logger.info("Load Tab 2! " + selBodBean.getProtocollo());
			logger.info("Load Tab 2! " + selBodBean.getProtocollo());
			/*
			 * Caricamento delle Liste (=PRG, Concessioni, Graffati, ...)
			 */
			/*
			 * Lista Controlli Incrociati
			 */
			lstControlliIncrociati = this.getControlliIncrociati();
			/*
			 * Lista Controlli Classamento - Consistenza
			 */
			
			lstControlliClassCons = this.getControlliClassamentoConsistenza();
			
			
			/*
			 * Lista Controllo Trasformazione Edilizia
			 */
					/*
					 * Elenco Variazioni
					 */
					lstTrasfEdiliziaVariazioni = this.getVariazioni();
					/*
					 * Elenco Soppressioni
					 */
					lstTrasfEdiliziaSoppressioni = this.getSoppressioni();
					/*
					 * Elenco Costituzioni 
					 */
					lstTrasfEdiliziaCostituzioni = this.getCostituzioni();
			/*
			 * Dotazioni Infrastrutturali
			 */
					/*
					 * Ascensori 
					 */
					lstAscensori = this.getAscensori();
					/*
					 * Accessori diretti (Bagni, Wc)
					 */
					lstBagni = this.getBagni();			
			/*
			 * XXX Elenco planimetrie C340		
			 */
			lstImagesFilesC340 = this.getPlanimetrieC340();
			/*
			 * XXX Elenco planimetrie DOCFA		
			 */
			lstImagesFilesDocfa = this.getPlanimetrieDocfa();


		}else{
			esito = "failure";
		}
				
		return esito;
	}//-------------------------------------------------------------------------
	
	private List getAscensori(){
		List<Object> lst = new ArrayList<Object>();
		String sql = "select distinct " +
		"protocollo, data, " +
		"substr(doc.riga_dettaglio,21,4) as foglio,  " +
		"substr(doc.riga_dettaglio,25,5) as particella,  " +
		"to_number_all(substr(doc.riga_dettaglio,34,4)) as subalterno,  " +
		"decode(to_number_all(substr(riga_dettaglio,255,1)),0,'No',1,'Si') as ascensore_uso_escl,   " +
		"decode(to_number_all(substr(riga_dettaglio,256,1)),0,'No',1,'Si') as ascensore,   " +
		"decode(to_number_all(substr(riga_dettaglio,257,1)),0,'No',1,'Si') as ascensore_di_servi,   " +
		"decode(to_number_all(substr(riga_dettaglio,258,1)),0,'No',1,'Si') as montacarichi,   " +
		"to_number_all(substr(riga_dettaglio,259,3)) as ascensori_nr   " +
		"from " +
		"doc_docfa_1_0 doc  " +
		"where substr(doc.riga_dettaglio,8,1) = 'U' " +
		"   and substr(doc.riga_dettaglio,17,1) = 'H' " +
		"and protocollo = '" + selBodBean.getProtocollo() + "' " +
		"and data = to_date('" + selBodBean.getFornitura() + "','MM/yyyy') ";

		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		lst = bodLogicService.getListCaronte(htQry);
//		if (listaAscensori != null && listaAscensori.size()>0){
//			for (int index=0; index<listaAscensori.size(); index++){
//				Object[] objs = (Object[])listaAscensori.get(index);
//				
//			}			
//		}
		
		return lst;
	}//-------------------------------------------------------------------------
	
	private List getBagni(){
		List<Object> lst = new ArrayList<Object>();
		String sql = "select distinct  " +
		"protocollo, data, " +
		"substr(doc.riga_dettaglio,21,4) as foglio,  " +
		"substr(doc.riga_dettaglio,25,5) as particella,  " +
		"to_number_all(substr(doc.riga_dettaglio,34,4)) as subalterno,  " +
		"(case  " +
		"    when ( substr(doc.riga_dettaglio,106,1) = '1' or substr(doc.riga_dettaglio,106,1) = '0')   " +
		"         then to_number_all(substr(riga_dettaglio,117,2))   " +
		"         else null  " +
		" end) as ab_accessori_dir_01_nr,  " +
		" (case  " +
		"    when ( substr(doc.riga_dettaglio,106,1) = '1' or substr(doc.riga_dettaglio,106,1) = '0')   " +
		"         then to_number_all(substr(riga_dettaglio,119,5))   " +
		"         else null  " +
		" end) as ab_accessori_dir_01_superf_uti " +
		" from doc_docfa_1_0 doc  " +
		" where substr(doc.riga_dettaglio,8,1) = 'U'      " +
		"   and substr(doc.riga_dettaglio,17,1) = 'H'    " +
		"and protocollo = '" + selBodBean.getProtocollo() + "' " +
		"and data = to_date('" + selBodBean.getFornitura() + "','MM/yyyy') ";

		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		lst = bodLogicService.getListCaronte(htQry);
//		if (listaAscensori != null && listaAscensori.size()>0){
//			for (int index=0; index<listaAscensori.size(); index++){
//				Object[] objs = (Object[])listaAscensori.get(index);
//				
//			}			
//		}
		
		return lst;
	}//-------------------------------------------------------------------------
	
	private List getPlanimetrieDocfa(){
		List<PlanimetriaBean> lstPlanHeader = new ArrayList<PlanimetriaBean>();
		List<PlanimetriaBean> lst = new ArrayList<PlanimetriaBean>();
		if (lstUiu != null && lstUiu.size()>0){
			/*
			 * recupero planimetrie docfa anche di protocolli passati o futuri per 
			 * ogni uiu
			 */
			for (int index=0; index<lstUiu.size(); index++){
				Object[] objUiu = (Object[])lstUiu.get(index);
				/*
				 * Lista UIU:
				 * - 0: tipo operazione
				 * - 1: foglio
				 * - 2: particella
				 * - 3: subalterno
				 * - 4: 
				 * - 5 e 6: indirizzo 
				 */
				PlanimetriaBean planHeader = new PlanimetriaBean();
				planHeader.setFoglio( (String)objUiu[1] );
				planHeader.setParticella( (String)objUiu[2] );
				planHeader.setSubalterno( (String)objUiu[3] );
				/*
				 * Dall DOCFA_UIU query1 
				 * select du.protocollo_reg, du.fornitura, du.tipo_operazione, du.foglio, du.numero, du.subalterno
				 *	from docfa_uiu du
				 *	where 
				 *	foglio = '0077' and numero = '00658' and subalterno = '0098'
				 *	order by du.protocollo_reg, du.fornitura, du.tipo_operazione
				 */
				ArrayList<PlanimetriaBean> alPlanTitle = new ArrayList<PlanimetriaBean>();
				Properties prop1 = SqlHandler.loadProperties(propName);
				String sql1 = prop1.getProperty("qryPlanUiu");
				sql1 += "and foglio = '" + objUiu[1] + "' and numero = '" + objUiu[2] + "' and subalterno = '" + objUiu[3] + "' " +
						"order by du.protocollo_reg, du.fornitura, du.tipo_operazione ";
				System.out.println(sql1);
				logger.info(sql1);
				Hashtable<String,String> htQry1 = new Hashtable<String,String>();
				htQry1.put("queryString", sql1);
				lstUiuAllProt = bodLogicService.getList(htQry1);
				if (lstUiuAllProt != null && lstUiuAllProt.size()>0){
					for (int j=0; j<lstUiuAllProt.size(); j++){
						Object[] objUiuAllProt = (Object[])lstUiuAllProt.get(j);	
						
						/*
						 * Dalla DOCFA_PLANIMETRIE query2
						 * SELECT dp.protocollo, dp.fornitura, ddc.foglio, ddc.numero, ddc.subalterno, dp.nome_plan, dp.progressivo, DP.IDENTIFICATIVO_IMMO, dp.formato, dp.scala
		from docfa_planimetrie dp
		left join docfa_dati_censuari ddc on dp.identificativo_immo = ddc.identificativo_immobile AND dp.protocollo = ddc.protocollo_registrazione and dp.fornitura = ddc.fornitura
		where
		ddc.foglio = '0077' and ddc.numero = '00658' and ddc.subalterno = '0098' and
		dp.protocollo = 'NO0054640' and dp.fornitura = to_date('01/04/2006', 'dd/mm/yyyy')
						 */
						//ArrayList<PlanimetriaBean> alPlanDetails = new ArrayList<PlanimetriaBean>();
						Properties prop2 = SqlHandler.loadProperties(propName);
						String sql2 = prop2.getProperty("qryPlanDocfa");
						sql2 += "and ddc.foglio = '" + objUiuAllProt[3] + "' and ddc.numero = '" + objUiuAllProt[4] + "' and ddc.subalterno = '" + objUiuAllProt[5] + "' " +
								"and dp.protocollo = '" + objUiuAllProt[0] + "' and dp.fornitura = to_date('" + TimeControl.parsItalyDate((Date)objUiuAllProt[1] ) + "', 'dd/mm/yyyy') ";
//						sql += "and fornitura = TO_DATE('" + selBodBean.getFornitura() + "', 'MM/yyyy') " +
//						" and protocollo = '" + selBodBean.getProtocollo() + "' " +
//		                " and (identificativo_immo = '" + uiDatCen[10] + "' or identificativo_immo = 0) ";
						System.out.println(sql2);
						logger.info(sql2);
						Hashtable<String,String> htQry2 = new Hashtable<String,String>();
						htQry2.put("queryString", sql2);
						List lstImagesPlanimetries = bodLogicService.getList(htQry2);
						/*
						 * Puo esistere piu di una planimetrria DOCFA per ogni 
						 * prot+forn+fogl+part+suba;
						 * AAA: Esistono anche planimetrie associate al solo
						 * prot+forn ma che non hanno valorizzati fog+par+sub ...
						 * in questo caso NON verranno mostrate le planimetrie 
						 */
						if (lstImagesPlanimetries != null && lstImagesPlanimetries.size()>0){
							for (int i=0; i<lstImagesPlanimetries.size(); i++) {
								Object[] objsPlan = (Object[])lstImagesPlanimetries.get(i);
								
								PlanimetriaBean planTitle = new PlanimetriaBean();
								planTitle.setProtocollo( (String)objUiuAllProt[0] );
								planTitle.setFornitura( TimeControl.parsItalyDate((Date)objUiuAllProt[1] ));
								planTitle.setTipoOperazione( (String)objUiuAllProt[2] );
								planTitle.setFoglio( Character.checkNullString((String)objUiuAllProt[3] ) );
								planTitle.setParticella(Character.checkNullString((String)objUiuAllProt[4] ));
								planTitle.setSubalterno(Character.checkNullString((String)objUiuAllProt[5] ));
								
								String nomeFile = Character.checkNullString((String)objsPlan[5]);
								if (nomeFile != null && !nomeFile.equals("")){
									BigDecimal progressivo = (BigDecimal)objsPlan[6];
									String progr = "";
									if (progressivo != null)
										progr = progressivo.toString();
									while(progr.length() < 3 )
										progr = "0" + progr;
									nomeFile = nomeFile + "." + progr + ".tif";
									//String[] fornitura =  TimeControl.formatDate( (Date)objsPlan[1]).split("/");
									Date fornitura =   (Date)objsPlan[1];
									String pathFile =  this.getRootPathEnte() + "planimetrie/" + TimeControl.getYear(new Timestamp(fornitura.getTime())) + TimeControl.getMonth( new Timestamp(fornitura.getTime()) ) + "/" + nomeFile;
									logger.info("Path Planimetria DOCFA: " + pathFile);
									File f = new File(pathFile);
									planTitle.setDescrizione(nomeFile);
									if (f.exists()){
										planTitle.setEsiste(true);
									}
									planTitle.setProgressivo(progr);
									planTitle.setFile(f);
									BigDecimal formato = (BigDecimal)objsPlan[8];
									planTitle.setFormato(formato == null ? 0 : formato.intValue());
									planTitle.setIdImmobile( Character.checkNullString( ( (BigDecimal)objsPlan[7]).toString() ));

									lst.add(planTitle);
									
									//alPlanTitle.add(planTitle);
								}
							}
							//planTitle.setOggetto(alPlanDetails);
						}else{
							/*
							 * Non ci sono planimetrie DOCFA per questi parametri 
							 * prot+forn+FPS quindi aggiungo la riga senza valorizzare
							 * le info riguardanti le planimetrie
							 */
							PlanimetriaBean planTitle = new PlanimetriaBean();
							planTitle.setProtocollo( (String)objUiuAllProt[0] );
							planTitle.setFornitura( TimeControl.parsItalyDate((Date)objUiuAllProt[1] ));
							planTitle.setTipoOperazione( (String)objUiuAllProt[2] );
							planTitle.setFoglio( Character.checkNullString((String)objUiuAllProt[3] ) );
							planTitle.setParticella(Character.checkNullString((String)objUiuAllProt[4] ));
							planTitle.setSubalterno(Character.checkNullString((String)objUiuAllProt[5] ));
							
							lst.add(planTitle);
							
							//alPlanTitle.add(planTitle);
						}
						
					}
				}
				planHeader.setOggetto(alPlanTitle);
				lstPlanHeader.add(planHeader);
			}
		}
		
		return lst;
	}//-------------------------------------------------------------------------
	

	private List getPlanimetrieC340(){
		List<PlanimetriaBean> lst = new ArrayList<PlanimetriaBean>();

		if (lstUiu != null && lstUiu.size()>0){
			/*
			 * recupero planimetrie C340 anche di protocolli passati o futuri per 
			 * ogni uiu
			 */
			for (int index=0; index<lstUiu.size(); index++){
				Object[] objUiu = (Object[])lstUiu.get(index);
				/*
				 * Lista UIU:
				 * - 0: tipo operazione
				 * - 1: foglio
				 * - 2: particella
				 * - 3: subalterno
				 * - 4: 
				 * - 5 e 6: indirizzo 
				 */
				Properties prop = SqlHandler.loadProperties(propName);
				String sql = prop.getProperty("qryPlanC340");
				sql += "AND LPAD (TRIM (FOGLIO), 4, '0') = LPAD (TRIM ('" + Character.checkNullString((String)objUiu[1]) + "'), 4, '0') " +
		        "AND LPAD (TRIM (NUMERO), 5, '0') = LPAD (TRIM ('" + Character.checkNullString((String)objUiu[2]) + "'), 5, '0') " +
		        "AND LPAD (TRIM (SUBALTERNO), 4, '0') = LPAD (TRIM ('" + Character.checkNullString((String)objUiu[3]) + "'), 4, '0') " +
		        //"AND SEZIONE_URBANA = '" + "" + "' " +
		        "ORDER BY NOME_FILE_PLANIMETRICO ";
				/*
				 * CAMPI TABELLA SIT_CAT_PLANIMETRIE_C340 (la chiave primaria di 
				 * questa tabella è costituita da fps:
				 * - CODICE_AMMINISTRATIVO,
		  		   - SEZIONE,
		  		   - IDENTIFICATIVO_IMMOBILE,
		           - SEZIONE_URBANA,
		           - FOGLIO,
		           - NUMERO,
		           - DENOMINATORE,
		           - SUBALTERNO,
		           - NOME_FILE_PLANIMETRICO,
		           - PROCESSID,
		           - RE_FLAG_ELABORATO,
		           - DT_EXP_DATO 
		           Accedendo alla tabella con le coordinate catastali andiamo a recuperare
		           il NOME_FILE_PLANIMETRICO che in realtà è senza estensione e senza progressivo
		           per cui nel file system potremmo trovare:
		           NOME_FILE_PLANIMETRICO.001
		           NOME_FILE_PLANIMETRICO.002 
				 */
				System.out.println(sql);
				logger.info(sql);
				Hashtable htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaPlanC340 = bodLogicService.getList(htQry);
				if (listaPlanC340 != null && listaPlanC340.size()>0){
					Iterator<Object> itPlanC340 = listaPlanC340.iterator();
					while(itPlanC340.hasNext()){
						Object[] objC340Tab = (Object[])itPlanC340.next();
						String pathFile =  this.getRootPathEnte() + "planimetrieComma340/";
						logger.info("Path Planimetria C340: " + pathFile);
						File dirPlanC340 = new File(pathFile);
						File filesDirPlanC340[] = dirPlanC340.listFiles();
						PlanimetriaBean planC340 = null;
						for( File filePlanC340 : filesDirPlanC340 ){		
						    String nomeFilePlanC340daFileSystem = filePlanC340.getName();
						    String nomeFilePlanC340daTabella = (String)objC340Tab[8]; 
						    if ( nomeFilePlanC340daFileSystem.startsWith( nomeFilePlanC340daTabella ) ){
						    	/*
						    	 * file trovato
						    	 */
						    	planC340 = new PlanimetriaBean();
						    	planC340.setFoglio( Character.checkNullString( (String)objUiu[1] ) );
						    	planC340.setParticella( Character.checkNullString( (String)objUiu[2] ) );
						    	planC340.setSubalterno( Character.checkNullString( (String)objUiu[3] ) );
								planC340.setDescrizione(nomeFilePlanC340daTabella);
								planC340.setEsiste(true);
								planC340.setFile(new File(pathFile + nomeFilePlanC340daFileSystem) );
								planC340.setFormato(4);
								planC340.setId(new Long(0));
								String progressivo = nomeFilePlanC340daFileSystem.substring(nomeFilePlanC340daFileSystem.lastIndexOf(".")+1, nomeFilePlanC340daFileSystem.length() );
								planC340.setProgressivo(progressivo);
								lst.add(planC340);
						    }
						}
						
					}
				}


			}
		}
		
		return lst;
	}//-------------------------------------------------------------------------

	
	private List getCostituzioni(){
		List<TrasformazioneEdiliziaBean> lst = new ArrayList<TrasformazioneEdiliziaBean>();
//		String sql = "select " +
//		"tipo_operazione as tipo, foglio, numero as particella, subalterno as sub  " +
//		"from " +
//		"docfa_uiu u " +
//		"where " +
//		"u.protocollo_reg = '" + selBodBean.getProtocollo() + "' " +
//		"and u.fornitura =  to_date('" + selBodBean.getFornitura() + "','MM/yyyy') " +
//		"and tipo_operazione = 'C' ";
//
//		Hashtable htQry = new Hashtable();
//		htQry.put("queryString", sql);
//		List<Object> lstCostituzioni = bodLogicService.getList(htQry);
		totRenPerCoefCos = 0d;
//		if (lstCostituzioni != null && lstCostituzioni.size()>0){
		TrasformazioneEdiliziaBean teb = null;
//			for (int index=0; index<lstCostituzioni.size(); index++){
//				Object[] objs = (Object[])lstCostituzioni.get(index);
				
		String sql = "select distinct " +
		"u.tipo_operazione as tipo, u.foglio, u.numero as particella, " +
		"u.subalterno as sub, ddc.categoria, ddc.classe, ddc.rendita_euro as rendita " +
		"from " +
		"docfa_uiu u, docfa_dati_censuari ddc " +
		"where " +
		"u.fornitura =  to_date('" + selBodBean.getFornitura() + "','MM/yyyy') " +
		"and u.protocollo_reg = '" + selBodBean.getProtocollo() + "' " +
		"and u.tipo_operazione='C' " +
		"and ddc.fornitura=U.FORNITURA " +
		"and ddc.protocollo_registrazione=u.protocollo_reg " +
		"and ddc.foglio = u.foglio " +
		"and ddc.numero = u.numero " +
		"and ddc.subalterno = u.subalterno " +
		"order by u.foglio, u.numero, u.subalterno ";

		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> lstCos = bodLogicService.getList(htQry);
		if (lstCos != null && lstCos.size() > 0){
			for (int ind = 0; ind < lstCos.size(); ind++){
				teb = new TrasformazioneEdiliziaBean();
				Object[] objects = (Object[])lstCos.get(ind);
				teb.setTipoOperazione( (String)objects[0] );
				teb.setFoglio( (String)objects[1] );
				teb.setParticella( (String)objects[2] );
				teb.setSubalterno( (String)objects[3] );
				String cat = (String)objects[4];
				teb.setCategoria(cat);
				Double rendita = Number.parsStringToDoubleZero((String)objects[6]);
				teb.setRendita(rendita.doubleValue());
				Double coeff = new Double("100");
				if (Info.htCoeffSoppressioni != null && cat != null && Info.htCoeffSoppressioni.get(cat) != null) {
					coeff = Number.parsStringToDoubleZero(Info.htCoeffSoppressioni.get(cat));
				}				
				teb.setCoefficiente(coeff);
				teb.setRenditaPerCoeff(rendita.doubleValue() * coeff);
				totRenPerCoefCos += rendita.doubleValue() * coeff;
				lst.add(teb);
			}
		}
			//}
		//}
		
		return lst;
	}//-------------------------------------------------------------------------
	
	private List getSoppressioni(){
		List<TrasformazioneEdiliziaBean> lst = new ArrayList<TrasformazioneEdiliziaBean>();
		String sql = "select " +
		"tipo_operazione as tipo, foglio, numero as particella, subalterno as sub  " +
		"from " +
		"docfa_uiu u " +
		"where " +
		"u.protocollo_reg = '" + selBodBean.getProtocollo() + "' " +
		"and u.fornitura =  to_date('" + selBodBean.getFornitura() + "','MM/yyyy') " +
		"and tipo_operazione='S' ";

		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> lstSoppressioni = bodLogicService.getList(htQry);
		totRenPerCoefSop = 0d;
		if (lstSoppressioni != null && lstSoppressioni.size()>0){
			TrasformazioneEdiliziaBean teb = null;
			for (int index=0; index<lstSoppressioni.size(); index++){
				teb = new TrasformazioneEdiliziaBean();
				Object[] objs = (Object[])lstSoppressioni.get(index);
				teb.setTipoOperazione( (String)objs[0] );
				teb.setFoglio( (String)objs[1] );
				teb.setParticella( (String)objs[2] );
				teb.setSubalterno( (String)objs[3] );
				
				sql = "select " +
				"su.categoria, su.classe, su.rendita, su.data_inizio_val, su.data_fine_val  " +
				"from " +
				"sitiuiu su " +
				"where " +
				"foglio = '" + Character.checkNullString(teb.getFoglio()) + "' " +
				"and particella = '" + Character.checkNullString(teb.getParticella()) + "' " +
				"and unimm = '" + Character.checkNullString(teb.getSubalterno()) + "' " +
				"and data_fine_val = (select max(data_fine_val) from sitiuiu  " +
				"where foglio = '" + Character.checkNullString(teb.getFoglio()) + "' " +
				"and particella = '" + Character.checkNullString(teb.getParticella()) + "' " +
				"and unimm = '" + Character.checkNullString(teb.getSubalterno()) + "' " +
				"and rendita <> 0) ";

				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> lstSop = bodLogicService.getList(htQry);
				if (lstSop != null && lstSop.size() > 0){
					for (int ind = 0; ind < lstSop.size(); ind++){
						Object[] objects = (Object[])lstSop.get(ind);
						String cat = (String)objects[0];
						teb.setCategoria(cat);
						BigDecimal rendita = (BigDecimal)objects[2];
						teb.setRendita(rendita.doubleValue());
						Double coeff = Number.parsStringToDoubleZero(Info.htCoeffSoppressioni.get(cat));
						teb.setCoefficiente(coeff);
						teb.setRenditaPerCoeff(rendita.doubleValue() * coeff);
						totRenPerCoefSop += rendita.doubleValue() * coeff;
					}
				}
				lst.add(teb);
			}
		}
		return lst;
	}//-------------------------------------------------------------------------
	
	private List getVariazioni(){
		List<TrasformazioneEdiliziaBean> lst = new ArrayList<TrasformazioneEdiliziaBean>();
		String sql = "select  " +
		"tipo_operazione as tipo, foglio as foglio, numero as particella, subalterno as sub " +
		"from  " +
		"docfa_uiu u " +
		"where  " +
		"u.protocollo_reg = '" + selBodBean.getProtocollo() + "' " +
		"and u.fornitura =  to_date('" + selBodBean.getFornitura() + "','MM/yyyy') " +
		"and tipo_operazione='V' " +
		"order by foglio, particella, sub ";

		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> lstVariazioni = bodLogicService.getList(htQry);
		if (lstVariazioni != null && lstVariazioni.size()>0){
			TrasformazioneEdiliziaBean te = null;
			for (int i=0; i<lstVariazioni.size(); i++){
				te = new TrasformazioneEdiliziaBean();
				Object[] objs = (Object[])lstVariazioni.get(i);
				te.setTipoOperazione( (String)objs[0] );
				te.setFoglio( (String)objs[1] );
				te.setParticella( (String)objs[2] );
				te.setSubalterno( (String)objs[3] );
				/*
				 * Recupero le date di variazione per questo protocollo e fornitura
				 */
				sql = "select " +
				"data_variazione " +
				"from " +
				"docfa_dati_generali ddg " +
				"where " +
				"ddg.protocollo_reg = '" + selBodBean.getProtocollo() + "' " +
				"and ddg.fornitura =  to_date('" + selBodBean.getFornitura() + "','MM/yyyy') ";

				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaDateVariazioni = bodLogicService.getList(htQry);
				List<Date> lstDateVar = new ArrayList<Date>();
				if (listaDateVariazioni != null && listaDateVariazioni.size()>0){
					Date d = null;
					for (int index=0; index<listaDateVariazioni.size(); index++){
						d = (Date)listaDateVariazioni.get(index);
						lstDateVar.add(d);
					}
				}
				te.setDateVariazioni(lstDateVar);
				/*
				 * Recupero delle Variazioni Censuarie
				 */
				sql = "select distinct " +
				"to_char(ui.data_inizio_val,'yyyymmdd'), to_char(ui.data_fine_val,'yyyymmdd'), " + //--non visualizzare queste due servono solo per la order
				"to_char(ui.data_inizio_val,'dd/mm/yyyy') as data_inizio_val, " +
				"to_char(ui.data_fine_val,'dd/mm/yyyy') as data_fine_val, ui.categoria, ui.classe, " +
				"ui.consistenza, ui.rendita, ui.sup_cat " +
				"from " +
				"sitiuiu ui, siticomu sc " +
				"where " +
				"sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
				"and ui.foglio = '" + te.getFoglio() + "' " +
				"and ui.particella = '" + te.getParticella() + "' " +
				"and ui.unimm = decode(trim('" + te.getSubalterno() + "'), '', 0, '" + te.getSubalterno() + "') " +
				"and to_char(ui.data_inizio_val,'yyyymmdd') <> to_char(ui.data_fine_val,'yyyymmdd')  " +
				"and sc.cod_nazionale = ui.cod_nazionale " +
				"order by to_char(ui.data_inizio_val,'yyyymmdd'), to_char(ui.data_fine_val,'yyyymmdd') ";

				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaVarCens = bodLogicService.getList(htQry);
				List<VariazioneCensuaria> lstVariazioniCensuarie = new ArrayList<VariazioneCensuaria>();
				if (listaVarCens != null && listaVarCens.size()>0){
					VariazioneCensuaria vc = null;
					for (int index=0; index<listaVarCens.size(); index++){
						vc = new VariazioneCensuaria();
						Object[] objects = (Object[])listaVarCens.get(index);
						vc = new VariazioneCensuaria();
						vc.setDataInizio( tc.fromFormatStringToDate((String)objects[2], "" ));
						vc.setDataFine( tc.fromFormatStringToDate((String)objects[3], "" ) );
						vc.setCategoria( (String)objects[4] );
						vc.setClasse( (String)objects[5] );
						vc.setConsistenza( (BigDecimal)objects[6] );
						vc.setRendita( (BigDecimal)objects[7] );
						vc.setSuperficie( (BigDecimal)objects[8] );
						
						lstVariazioniCensuarie.add(vc);
					}
				}
				te.setVariazioniCensuarie(lstVariazioniCensuarie);
				
				lst.add(te);
			}
		}
		
		return lst;
	}//-------------------------------------------------------------------------
	
	private List getCorpiFabbricaEntro200m(String foglio, String particella){
		String controllo = foglio + "_" + particella;
		List<ClassamentoCorpoFabbricaBean> lst = new ArrayList<ClassamentoCorpoFabbricaBean>();
//		Hashtable<String, String> ht = new Hashtable<String, String>();
//		if (!ht.containsKey(controllo)){
		ClassamentoCorpoFabbricaBean ccf = new ClassamentoCorpoFabbricaBean();
		ccf.setFoglio(foglio);
		ccf.setParticella(particella);
		String ente = this.getCodiceEnte();
		
		String sql = "select *  " +
		"from( " +
		" SELECT distinct  a.foglio, lpad(a.particella,5,'0') as particella " +
		"   FROM sitipart a " +
		"   where  sdo_relate (a.shape, (SELECT sdo_geom.sdo_buffer (a.shape, 200, 0.05) shape " +
		" FROM sitipart a " +
		"  where a.foglio = '" + foglio + "' " +
		"  AND a.particella = '" + particella + "' " +
		"  AND a.cod_nazionale = '" + ente + "' " + 
		"  AND a.data_fine_val = to_date('31/12/9999','dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		") res " +
		"where exists (" +
		" select 1 from sitiuiu u WHERE  u.foglio = res.foglio AND u.particella = res.particella AND u.data_fine_val = TO_DATE ('99991231', 'yyyymmdd') " +
		") " +
		"order by foglio, particella ";
		
//		" select count(*) from sitiuiu u where u.foglio = res.foglio and u.particella = res.particella and u.data_fine_val=to_date('99991231','yyyymmdd') " +
//		")>0 " +
//		"order by foglio, particella ";
		
		/*
		select *  
		from( 
		SELECT distinct  a.foglio, lpad(a.particella,5,'0') as particella 
		   FROM sitipart a 
		   where  sdo_relate (a.shape, (SELECT sdo_geom.sdo_buffer (a.shape, 200, 0.05) shape 
		 FROM sitipart a 
		  where a.foglio = '0274' 
		  AND a.particella = '00055' 
		  AND a.data_fine_val = to_date('31/12/9999','dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE') res
		where (select count(*) from sitiuiu u where u.foglio = res.foglio and u.particella = res.particella and u.data_fine_val=to_date('99991231','yyyymmdd'))>0    
		ORDER BY foglio, particella 
		*/
		
	
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> lstCf200m = bodLogicService.getList(htQry);
//		List<Object> lstCf200mNew= new ArrayList<Object>();
//			if (lstCf200m != null && lstCf200m.size()>0){
//				for (int i=0; i<lstCf200m.size(); i++){
//					Object[] objs = (Object[])lstCf200m.get(i);
//	
//				}
//			}
//		if (lstCf200m != null && lstCf200m.size()>0){
//
//			for (int i=0; i<lstCf200m.size(); i++){
//			Object[] objs = (Object[])lstCf200m.get(i);
//			String particellaArr= (String)objs[1];
//			
//			//if (!particellaArr.toUpperCase().contains("ST")){
//			try{
//				Integer.valueOf(particellaArr);
//				
//				lstCf200mNew.add(objs);
//			}catch(Exception e){
//				
//			}
//		}
//		}
		
		
//		ccf.setCorpiFabbrica(lstCf200mNew);
		ccf.setCorpiFabbrica(lstCf200m);
		lst.add(ccf);
		//}
		//ht.put(controllo, controllo);
		
		return lst;
	}//-------------------------------------------------------------------------
	
	
	private List getControlliClassamentoConsistenza(){
		List<ControlloClassamentoConsistenzaDTO> lst = new ArrayList<ControlloClassamentoConsistenzaDTO>();
		if (lstDatiCensuari != null && lstDatiCensuari.size() > 0){
				
			SimpleDateFormat MMyyyy = new SimpleDateFormat("MM/yyyy");
			
			try{
			
			RicercaOggettoDocfaDTO rod = new RicercaOggettoDocfaDTO();
			rod.setEnteId(codiceEnte);
			rod.setProtocollo(selBodBean.getProtocollo());
			rod.setFornitura(MMyyyy.parse(selBodBean.getFornitura()));
			/*
			 * usiamo il campo seguente per impostare il metodo di calcolo del valore
			 * commerciale deciso 
			 */
			rod.setTipoOperDocfa(this.getModalitaCalcoloValoreCommerciale());
			Context cont;
			
			cont = new InitialContext();
		
			ElaborazioniService elabService= (ElaborazioniService) getEjb("CT_Service", "CT_Service_Data_Access", "ElaborazioniServiceBean");

			
			
			lst = elabService.getControlliClassamentoConsistenzaDocfaUiu(rod);
			}catch(Exception e){
				logger.error("Errore getControlliClassamentoConsistenza "+e.getMessage(),e);
			}
			
		}
		return lst;
	}//-----------------------------
	
	
	//(SOSTITUITO DAL PRECEDENTE) 21 02 2013 - Cod.Intervento 93
	/*private List getControlliClassamentoConsistenza(){
		List<ControlloClassamentoConsistenzaBean> lst = new ArrayList<ControlloClassamentoConsistenzaBean>();
		if (lstDatiCensuari != null && lstDatiCensuari.size() > 0){
			ControlloClassamentoConsistenzaBean ccc = null;
			for (int index=0; index<lstDatiCensuari.size(); index++){
				ccc = new ControlloClassamentoConsistenzaBean();
				
				Object[] objs = (Object[])lstDatiCensuari.get(index);
				ccc.setFoglio(Character.checkNullString( (String)objs[0] ));
				ccc.setParticella(Character.checkNullString( (String)objs[1] ));
				ccc.setSubalterno(Character.checkNullString( (String)objs[2] ));
				ccc.setZona(Character.checkNullString( (String)objs[3] ));
				ccc.setCategoria(Character.checkNullString( (String)objs[5] ));
				ccc.setClasse(Character.checkNullString( (String)objs[4] ));
				ccc.setConsistenza(Character.checkNullString( (String)objs[6] ));
				String superf = Character.checkNullString( (String)objs[7] );
				Double superficie = Number.parsStringToDoubleZero(superf); 
				ccc.setSuperficie(superficie);
				if (ccc.getCategoria() != null && !ccc.getCategoria().trim().equalsIgnoreCase("")){
					
					 * Logica per anomalie di classe: preve il recupero della classe
					 * minima e nel caso di C06 devo diminuire di 3 unità
					 
					if (ccc.getCategoria().trim().toUpperCase().startsWith("C")
							|| ccc.getCategoria().trim().toUpperCase().equalsIgnoreCase("A10")) {
						
						 * Mostro la Classe Maggiormente Frequente
						 
						ccc.setMostraClasseMaggiormenteFrequente(true);

						String sql = "SELECT " +
						"classe_min " +
						"FROM " +
						"docfa_classi_min " +
						"WHERE " +
						"NVL (TO_NUMBER (zc), -1) = NVL ('" + Character.checkNullString(ccc.getZona()) + "', -1) " +
						"AND TO_NUMBER (foglio) = '" + Character.checkNullString(ccc.getFoglio()) + "' " +
						"AND categoria = '" + Character.checkNullString(ccc.getCategoria()) + "' ";
						Hashtable htQry = new Hashtable();
						htQry.put("queryString", sql);
						List<Object> lstClassiMinime = bodLogicService.getList(htQry);
						if (lstClassiMinime != null && lstClassiMinime.size()>0){
							String minClasse = (String)lstClassiMinime.get(0);
							int classeMin = Number.parsStringToInt(minClasse);
							if (ccc.getCategoria().trim().equalsIgnoreCase("C06")){
								classeMin -= 3;								
							}
							ccc.setClasseMin(classeMin);
							//(ccc.classe >= ccc.classeRif)?'black':'red'
							ccc.setClasseRif( (Number.parsStringToInt(ccc.getClasse()) >= ccc.getClasseMin() )?"black":"red" );
						}
					}else{
						//if ( (new Double(ccc.getClasse())).isNaN() )
						ccc.setClasseRif("black");
					}
					
					 * Calcolo la tolleranza (= 95% <= x <= 105%) per determinare 
					 * le anomalie di consistenza delle categorie diverse da C01, 
					 * C02, C03 alle quali invece è riservato un trattamento diverso
					 
					if (ccc.getCategoria().trim().equalsIgnoreCase("C01")
							|| ccc.getCategoria().trim().equalsIgnoreCase("C02")
							|| ccc.getCategoria().trim().equalsIgnoreCase("C03")  ) {
						
						 * L'anomalia di consistenza si verifica quando il rapporto 
						 * tra consistenza e superficie catastale è inferiore a 0,85
						 
						ccc.setSuperfMediaMin(0d);
						Double consistSuSuperfCatastale = Number.parsStringToDoubleZero(ccc.getConsistenza()) / ccc.getSuperficie();
						ccc.setConsisAnomalia(consistSuSuperfCatastale);
						ccc.setSuperfMediaMax(0.85d);
						if (ccc.getSuperfMediaMin() <= ccc.getConsisAnomalia() && ccc.getConsisAnomalia()>= ccc.getSuperfMediaMax())
							ccc.setColore("black");
						else
							ccc.setColore("red");

					} else if (Info.htSuperficiMedie.containsKey(ccc.getCategoria())){
						if (!ccc.getCategoria().trim().equalsIgnoreCase("A10")){
							ccc.setClassamentoCompatibile(true);							
						}
//						Double consistenzaMin = Number.parsStringToDouble(Info.htSuperficiMedie.get(ccc.getCategoria())) * 95 / 100;
//						ccc.setSuperfMediaMin(consistenzaMin);
//						Double consistenzaMax = Number.parsStringToDouble(Info.htSuperficiMedie.get(ccc.getCategoria())) * 105 / 100;
//						ccc.setSuperfMediaMax(consistenzaMax);
//						Double superfSuConsis = superficie	/ Number.parsStringToDoubleZero(ccc.getConsistenza());
//						ccc.setConsisAnomalia(superfSuConsis);
						Double consistenzaMin = ccc.getSuperficie() / Number.parsStringToDouble(Info.htSuperficiMedie.get(ccc.getCategoria())) * 0.95;
						ccc.setSuperfMediaMin(consistenzaMin);
						Double consistenzaMax = ccc.getSuperficie() / Number.parsStringToDouble(Info.htSuperficiMedie.get(ccc.getCategoria())) * 1.05;
						ccc.setSuperfMediaMax(consistenzaMax);
						ccc.setConsisAnomalia(Number.parsStringToDoubleZero(ccc.getConsistenza()));
						if (ccc.getConsisAnomalia() >= ccc.getSuperfMediaMin() && ccc.getConsisAnomalia() <= ccc.getSuperfMediaMax())
							ccc.setColore("black");
						else
							ccc.setColore("red");
					}
				}
				String rend = Character.checkNullString( (String)objs[8] );
				Double rendita = Number.parsStringToDoubleZero(rend); 
				ccc.setRendita(rendita);
				//ccc.setRendita100(Number.parsStringToDoubleZero(Number.formatDouble(rendita * 100)));
				Double r100 = rendita * 100;
				ccc.setRendita100(r100);
				Double r105 = rendita * 105;
				ccc.setRendita105(r105);
				
				 * Determinazione del valore comm.le
				 
				Double valCom = this.getValoreCommerciale(objs);
				ccc.setValoreCommerciale(valCom);
				ccc.setValComSuRen100(valCom/r100);
				ccc.setValComSuRen105(valCom/r105);
				
				 * Recupero il rapporto
				 
				Double rapporto= bodLogicService.getDocfaRapporto();
				
				ccc.setRapporto(rapporto);
				Double hopeAvg = ccc.getValoreCommerciale()/(105 * Number.parsStringToDoubleZero(ccc.getConsistenza()) * ccc.getRapporto());
				if (hopeAvg != null && !hopeAvg.isNaN() && !hopeAvg.isInfinite())
					ccc.setMediaAttesa(hopeAvg);
				
				lst.add(ccc);
			}
		}
		return lst;
	}//-------------------------------------------------------------------------
*/	
	private List<FoglioMicrozonaDTO> getFoglioMicrozona(String foglio, String zonaCens){
		List<FoglioMicrozonaDTO> lstMicrozone = new ArrayList<FoglioMicrozonaDTO>();
		Context cont;
		try{
		cont = new InitialContext();
		DocfaService docfaService= (DocfaService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
		
		ZonaDTO rd = new ZonaDTO();
		rd.setEnteId(codiceEnte);
		rd.setUserId(this.getUtente().getName());
		rd.setFoglio(Character.checkNullString( foglio ));
		rd.setZonaCensuaria(Character.checkNullString( zonaCens ));
		rd.setRicZcZonaOmi(true);
		
		lstMicrozone =  docfaService.getFoglioMicrozona(rd);
		
		}catch(Exception e){
			logger.error("getFoglioMicrozona "+ e.getMessage(),e);
		}
		
		return lstMicrozone;
	}//-------------------------------------------------------------------------
	
	private Double getValoreCommerciale(Object[] objs){
		Double valCom = new Double(0);
		try{
			/*
			 * Recupero del flag di nuova costituzione
			 */
			String sql = "SELECT DISTINCT " +
			"FLAG_NC  " +
			"from " +
			"DOCFA_DATI_GENERALI G " +
			"where " +
			"G.FORNITURA = to_date('" + selBodBean.getFornitura() + "', 'MM/yyyy')  " +
			"AND G.PROTOCOLLO_REG = '" + selBodBean.getProtocollo() + "' ";
			Hashtable htQry = new Hashtable();
			htQry.put("queryString", sql);
			/*
			 * In realtà è un solo record e un solo campo
			 */
			List<Object> lstFlagsNuovaCostituzione = bodLogicService.getList(htQry);
			if (lstFlagsNuovaCostituzione != null && lstFlagsNuovaCostituzione.size()> 0){
				/*
				 * Se non ho il risultato non posso calcolare il valore commerciale
				 */
				String flag = ((BigDecimal)lstFlagsNuovaCostituzione.get(0)).toString();
				/*
				 * Recupero della microzona
				 */
				List<FoglioMicrozonaDTO> lstMicrozone = this.getFoglioMicrozona( (String)objs[0],  (String)objs[3] );
				if (lstMicrozone != null && lstMicrozone.size() > 0){
					//Object[] microzona = (Object[])lstMicrozone.get(0);
					FoglioMicrozonaDTO microzona = lstMicrozone.get(0);
					//if (microzona != null && objs != null && microzona[2] != null && objs[5] != null){	
					if (microzona != null && objs != null && microzona.getNewMicrozona() != null && objs[5] != null){	
						
						String newMicrozona = microzona.getNewMicrozona();
						
						/*
						 * Recupero valore medio SE I PARAMETRI PER LA QUERY SONO OK
						 */
						sql = "SELECT " +
						"V.VAL_MED " +
						"from " +
						"DOCFA_VALORI V " +
						"where " +
						"TO_NUMBER(V.MICROZONA)  = '" + newMicrozona + "' " +
						"AND V.TIPOLOGIA_EDILIZIA = '" + Info.htCategorieCatastali.get((String)objs[5]) + "' " +
						"AND V.STATO = '" + Info.htFlagNuovaCostituzione.get(flag.trim()) + "' ";
						htQry = new Hashtable();
						htQry.put("queryString", sql);
						List<Object> lstValoriMedi = bodLogicService.getList(htQry);
						if (lstValoriMedi != null && lstValoriMedi.size() > 0){
							BigDecimal valMed = (BigDecimal)lstValoriMedi.get(0);
							/*
							 * superficie * val_med = val_comm.le 
							 */
							String superf = Character.checkNullString( (String)objs[7] );
							Double superficie = Number.parsStringToDoubleZero(superf); 
							//valCom = Number.parsStringToDoubleZero(Number.formatDouble(valMed.doubleValue() * superficie));
							valCom = valMed.doubleValue() * superficie;
						}else
							logger.warn("Valore Medio mancante");
					}else
						logger.warn("Parametri = microzona: " + microzona.getNewMicrozona() + "; categoria catastale: " + (String)objs[5] + "; Flag Nuova Costituzione: " + flag);
				}else
					logger.warn("Microzona mancante");
			}else
				logger.warn("Flag di nuova costituzione mancante");
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return valCom;
	}//-------------------------------------------------------------------------
	
	private List getControlliIncrociati(){
		List<ControlloIncrociatoBean> lst = new ArrayList<ControlloIncrociatoBean>();
		lstClassCorpiFabbrica = new ArrayList<ClassamentoCorpoFabbricaBean>();
		if (lstUiu != null && lstUiu.size()>0){
			ControlloIncrociatoBean cib = null;
			Hashtable<String, String> htDistinct = new Hashtable<String, String>();
			for (int index=0; index<lstUiu.size(); index++){
				Object[] uiu = (Object[])lstUiu.get(index);
				cib = new ControlloIncrociatoBean();
				cib.setProtocollo_reg(selBodBean.getProtocollo());
				cib.setFornitura(tc.fromFormatStringToDate("01/" + selBodBean.getFornitura(), null));
				cib.setCodiceEnte(codiceEnte);
				cib.setTipo_operazione(Character.checkNullString((String)uiu[0]));
				cib.setFoglio(Character.checkNullString((String)uiu[1]));
				cib.setParticella(Character.checkNullString((String)uiu[2]));
				cib.setSubalterno(Character.checkNullString((String)uiu[3]));
				//cib.setIndir_toponimo(Character.checkNullString((String)uiu[4]));
				
				String via= (String)uiu[5];
				String viaNew= Character.pulisciVia(via);
				String civico="";
				
				if (uiu[6] != null){
            		try{
            			Integer civicoInt= Integer.valueOf((String)uiu[6]);
            			civico= String.valueOf(civicoInt);
            		}catch(Exception e){
            			civico= (String)uiu[6];
            		}
        		}
				
				
				cib.setIndir_toponimo(viaNew + " " + civico);
				/*
				 * Lista Verifica Catasto
				 */
				String sql = "select distinct  " +
				"ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
				"ui.data_fine_val, ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
				"ui.interno, ui.piano,  " +
				"t.descr ||' '||uind.ind as indirizzo, uind.civ1  " +
				"from  " +
				"sitiuiu ui " +
				"left join siticomu sc " +
				"on ui.cod_nazionale=sc.cod_nazionale " +
				"left join load_cat_uiu_id i " +
				"on sc.codi_fisc_luna=i.codi_fisc_luna " +
				//"left join load_cat_uiu_id i  " +
				//"on ui.cod_nazionale=i.codi_fisc_luna " +
				"and lpad(ui.foglio,4,'0')=i.foglio   " +
				"and ui.particella= i.mappale " +
				"and decode(ui.unimm,0,' ',lpad(ui.unimm,4,'0'))= i.sub " +
				"left join load_cat_uiu_ind uind " +
				"on i.codi_fisc_luna=uind.codi_fisc_luna  " +
				"and i.sezione=uind.sezione " +
				"and i.progressivo=uind.progressivo " +
				"and i.id_imm=uind.id_imm " +
				"left join cat_d_toponimi t  " +
				"on uind.toponimo = t.pk_id  " +
				"where ui.foglio = '" + cib.getFoglio() + "' " +
				"and ui.particella = '" + cib.getParticella() + "'  " +
				"and ui.unimm = '" + cib.getSubalterno() + "'  " +
				"and uind.seq = 0 " +
				"AND uIND.PROGRESSIVO = (SELECT MAX(PROGRESSIVO) FROM load_cat_uiu_ind IND22 WHERE IND22.ID_IMM = uIND.ID_IMM AND IND22.CODI_FISC_LUNA = uIND.CODI_FISC_LUNA AND ind22.SEZIONE = uind.SEZIONE AND ind22.todelete ='0' )" +
				"order by data_inizio_val DESC, data_fine_val DESC, foglio, particella, sub, unimm";
				
				Hashtable htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaVerCat = bodLogicService.getList(htQry);
				List<VerificaCatastoBean> lstVerificaCatasto = new ArrayList<VerificaCatastoBean>();
				if (listaVerCat != null && listaVerCat.size()>0){
					VerificaCatastoBean vcb = null;
					for (int ind = 0; ind < listaVerCat.size(); ind++){
						vcb = new VerificaCatastoBean();
						Object[] objs = (Object[])listaVerCat.get(ind);
						vcb.setFoglio(Number.parsBigDecimalToDoubleZero((BigDecimal)objs[0]) );
						vcb.setParticella("" + (java.lang.String)objs[1]);
						vcb.setSubalterno(Number.parsBigDecimalToDoubleZero((BigDecimal)objs[3]) );
						//vcb.setDataInizioVal(  tc.fromFormatStringToDate(Character.checkNullString((String)objs[4]), "00:00") );
						vcb.setDataInizioVal( (Date)objs[4] );
						vcb.setDataFineVal( (Date)objs[5] );
						vcb.setCategoria(Character.checkNullString((String)objs[6]));
						vcb.setClasse(Character.checkNullString((String)objs[7]));
						vcb.setConsistenza( Number.parsBigDecimalToDoubleZero((BigDecimal)objs[8]) );
						vcb.setRendita( Number.parsBigDecimalToDoubleZero((BigDecimal)objs[9]) );
						vcb.setSuperficie( Number.parsBigDecimalToDoubleZero((BigDecimal)objs[10]) );
						vcb.setZona(Character.checkNullString((String)objs[11]));
						vcb.setEdificio(Character.checkNullString((String)objs[12]));
						vcb.setScala(Character.checkNullString((String)objs[13]));
						vcb.setInterno(Character.checkNullString((String)objs[14]));
						vcb.setPiano(Character.checkNullString((String)objs[15]));
						vcb.setIndirizzo(Character.pulisciVia(Character.checkNullString((String)objs[16])));
						 civico="";
						
						
						
						if (objs[17]!= null ) {
							try{
		            			Integer civicoInt= Integer.valueOf((String)objs[17]);
		            			civico= String.valueOf(civicoInt);
		            		}catch(Exception e){
		            			civico= (String)objs[17];
		            		}
						}
								 
						vcb.setCivico(civico);
						
						lstVerificaCatasto.add(vcb);
					}
				}
				cib.setVerificaCatasto(lstVerificaCatasto);
				/*
				 * Lista PRG
				 */
				
				BaseEnvironment environment = new BaseEnvironment();
				environment.setBodLogicService(bodLogicService);
				cib.setPrgs(environment.getPRG((String)uiu[1], (String)uiu[2], codiceEnte));
				/*
				 * Lista Concessioni Tab. Accertato: anche se viene ripetuta per ogni
				 * unità immobiliare, l'elenco delle concessioni è sempre lo stesso 
				 * perché si accede alla banca dati con foglio e particella
				 */
//				sql = "select o.foglio, o.particella, o.subalterno, C.PROTOCOLLO_NUMERO, "+
//                "TO_CHAR (c.protocollo_data, 'dd/mm/yyyy') data_protocollof, "+
//                "p.codice_fiscale, p.denominazione , "+
//                "cp.TITOLO tipo_soggetto "+
//                "FROM SIT_C_CONCESSIONI c, "+
//                "SIT_C_PERSONA p, "+
//                "SIT_C_CONC_PERSONA cp, "+
//                "SIT_C_CONCESSIONI_CATASTO o "+
//                "WHERE 1 = 1 "+
//                "AND p.ID_EXT = cp.ID_EXT_C_PERSONA "+
//                "AND o.ID_EXT_C_CONCESSIONI = cp.ID_EXT_C_CONCESSIONI "+
//                "AND cp.ID_EXT_C_CONCESSIONI = c.ID_EXT "+
//                "AND LPAD (TRIM (o.foglio), 4, '0') = LPAD (TRIM ('" + uiu[1] + "'), 4, '0') "+
//                "AND LPAD (TRIM (o.particella), 5, '0') = "+
//                "                                    LPAD (TRIM ('" + uiu[2] + "'), 5, '0') "+
//                "AND C.DT_FINE_VAL IS NULL "+
//                "AND P.DT_FINE_VAL IS NULL "+
//                "AND CP.DT_FINE_VAL IS NULL "+
//                "AND O.DT_FINE_VAL IS NULL "+
//                "ORDER BY o.foglio, o.particella, o.subalterno, c.PROTOCOLLO_DATA";
				
				Properties prop = SqlHandler.loadProperties(propName);
				sql = prop.getProperty("qryConcessioni");
				sql += "AND LPAD (TRIM (o.foglio), 4, '0') = LPAD (TRIM ('" + uiu[1] + "'), 4, '0') " +
	            "AND LPAD (TRIM (o.particella), 5, '0') = LPAD (TRIM ('" + uiu[2] + "'), 5, '0') " +
	            //"AND LPAD (TRIM (o.subalterno), 5, '0') = LPAD (TRIM ('" + uiu[3] + "'), 5, '0') " +
	            "ORDER BY o.foglio, o.particella, o.subalterno, data_protocollof ";
				
				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaConcessioni = bodLogicService.getList(htQry);
				List<ConcessioneBean> lstConcessioni = new ArrayList<ConcessioneBean>();
				if (listaConcessioni != null && listaConcessioni.size()>0){
					//logger.info("Num. Concessioni: " + listaConcessioni.size());
					logger.info("Num. Concessioni: " + listaConcessioni.size());
					ConcessioneBean concessione = null;
					for (int i=0; i<listaConcessioni.size(); i++){
						concessione = new ConcessioneBean();
						Object[] objs = (Object[])listaConcessioni.get(i);
						concessione.setFoglio(Character.checkNullString((String)objs[0]));
						concessione.setParticella(Character.checkNullString((String)objs[1]));
						concessione.setSubalterno(Character.checkNullString((String)objs[2]));
						concessione.setConcessione(Character.checkNullString((String)objs[3]));
						concessione.setProtocollo(Character.checkNullString((String)objs[4]));
						concessione.setProgressivo(Character.checkNullString((String)objs[5]));
						concessione.setOggetto(Character.checkNullString((String)objs[6]));
						concessione.setTipoIntervento(Character.checkNullString((String)objs[7]));
						concessione.setDataRilascio(Character.checkNullString((String)objs[8]));
						concessione.setDataFineLavori(Character.checkNullString((String)objs[9]));
						concessione.setPosizione(Character.checkNullString((String)objs[10]));
						concessione.setDataProtocollo(Character.checkNullString((String)objs[11]));
						concessione.setCodiceFiscale(Character.checkNullString((String)objs[12]));
						concessione.setPartitaIva("");
						String den = Character.checkNullString((String)objs[13]);
						String cog = Character.checkNullString((String)objs[14]);
						String nom = Character.checkNullString((String)objs[15]);
						if (den.trim().equalsIgnoreCase(""))
							den = cog + " " + nom;
						concessione.setDenominazione(den);
						concessione.setCognome(cog);
						concessione.setNome(nom);
						concessione.setTipoSoggetto(Character.checkNullString((String)objs[16]));
						
						lstConcessioni.add(concessione);
					}
				}
				cib.setConcessioni(lstConcessioni);
				/*
				 * Lista Graffati
				 */
				sql = "SELECT DISTINCT " +
				"foglio, mappale as part, sub  " +
				"FROM " +
				"load_cat_uiu_id  " +
				"WHERE " +
				"codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
				"and id_imm in " +
				"(SELECT distinct id_imm FROM load_cat_uiu_id  " +
				"WHERE " +
				"codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
				"and foglio= '" + uiu[1] + "' " +
				"and mappale= '" + uiu[2] + "'  " +
				"and sub= '" + uiu[3] + "') " +
				"minus select '" + uiu[1] + "' as foglio, '" + uiu[2] + "' as part, '" + uiu[3] + "' as sub from dual " +
				"order by foglio, part ";
				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaGraffati = bodLogicService.getList(htQry);
				List<GraffatoBean> lstGraffati = new ArrayList<GraffatoBean>();
				if (listaGraffati != null && listaGraffati.size()>0){
					//logger.info("Num. Graffati: " + listaGraffati.size());
					logger.info("Num. Graffati: " + listaGraffati.size());
					GraffatoBean graffato = null;
					for (int i=0; i<listaGraffati.size(); i++){
						graffato = new GraffatoBean();
						Object[] objs = (Object[])listaGraffati.get(i);
						graffato.setFoglio(Character.checkNullString((String)objs[0]));
						graffato.setParticella(Character.checkNullString((String)objs[1]));
						graffato.setSubalterno(Character.checkNullString((String)objs[2]));
						
						lstGraffati.add(graffato);
					}
				}
				cib.setGraffati(lstGraffati);
				/*
				 * Lista Comma 340
				 */
				sql = "SELECT " +
				"vano, piano, edificio, ambiente, supe_ambiente, altezzamin, altezzamax  " +
				"FROM " +
				"sitiedi_vani " +
				"WHERE " +
				"foglio = to_number('" + uiu[1] + "')  " +
				"AND particella = '" + uiu[2] + "'  " +
				"AND unimm = to_number(decode('" + Character.checkNullString((String)uiu[3]) + "', '', 0, '" + uiu[3] + "')) " +
				"and data_fine_val=to_date('99991231','yyyymmdd') " +
				"order by ambiente  ";
				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaCommi = bodLogicService.getList(htQry);
				List<Comma340Bean> lstCommi = new ArrayList<Comma340Bean>();
				if (listaCommi != null && listaCommi.size()>0){
					//logger.info("Num. Commi: " + listaCommi.size());
					logger.info("Num. Commi: " + listaCommi.size());
					Comma340Bean comma = null;
					for (int i=0; i<listaCommi.size(); i++){
						comma = new Comma340Bean();
						Object[] objs = (Object[])listaCommi.get(i);
						comma.setVano(((BigDecimal)objs[0]).toString());
						comma.setPiano(Character.checkNullString((String)objs[1]));
						comma.setEdificio(Character.checkNullString((String)objs[2]));
						comma.setAmbiente(Character.checkNullString((String)objs[3]));
						comma.setSuperficie(((BigDecimal)objs[4]).toString());
						comma.setAltezzaMin(((BigDecimal)objs[5]).toString());
						comma.setAltezzaMax(((BigDecimal)objs[6]).toString());
						
						lstCommi.add(comma);
					}
				}
				cib.setCommi(lstCommi);
				
				/*
				 * Lista Vincoli
				 */
				
				cib.setVincoli(environment.getVincoli((String)uiu[1],(String) uiu[2], codiceEnte));
				/*
				 * Lista Controlli Classamenti Corpi di Fabbrica: 
				 * si evita che il recupero di questa informazioni sia fatto piu
				 * di una volta per ogni coppia foglio_particella
				 */
				String controllo = Character.checkNullString((String)uiu[1]) + "_" + Character.checkNullString((String)uiu[2]);
				if (!htDistinct.containsKey(controllo)){
					List<ClassamentoCorpoFabbricaBean> listaCCF = this.getCorpiFabbricaEntro200m(Character.checkNullString((String)uiu[1]), Character.checkNullString((String)uiu[2]));
					lstClassCorpiFabbrica.addAll(listaCCF);
					htDistinct.put(controllo, controllo);
				}

				/*
				 * Recupero delle coordinate per il virtual hearth 3d e StreetView
				 */
				sql = "SELECT  " +
				"t.y lat, t.x lon " +
				"FROM  " +
				"sitipart_3d p,  " +
				//"all_sdo_geom_metadata m, " +
				"siticomu sc, " +
				//"TABLE (sdo_util.getvertices (sdo_cs.transform (p.shape_pp, m.diminfo, 8307))) t " +
				"TABLE(SDO_UTIL.getvertices ( SDO_CS.transform (p.shape_pp, MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 1313328, 2820083, 0.0050),MDSYS.SDO_DIM_ELEMENT('Y', 3930191, 5220322.5, 0.0050)) , 8307))) t " +
				"WHERE  " +
				//"m.table_name = 'SITIPART_3D' AND m.column_name = 'SHAPE_PP' AND " +
				" P.COD_NAZIONALE = SC.COD_NAZIONALE " +
				"AND SC.CODI_FISC_LUNA = '" + cib.getCodiceEnte() + "' " +
				"AND P.FOGLIO = '" + cib.getFoglio() + "' " +
				"AND P.PARTICELLA = '" + cib.getParticella() + "' ";
				htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaCoordinate =  bodLogicService.getList(htQry);
				if (listaCoordinate != null && listaCoordinate.size()>0){
					Object[] coord = (Object[])listaCoordinate.get(0);
					Double latitudine = Number.parsBigDecimalToDoubleZero((BigDecimal)coord[0]);
					Double longitudine = Number.parsBigDecimalToDoubleZero((BigDecimal)coord[1]);
					
					VirtualEarthBean veb = new VirtualEarthBean();
					veb.setCodiceEnte(cib.getCodiceEnte());
					veb.setLatitudine(latitudine);
					veb.setLongitudine(longitudine);
					cib.setVirtualEarth(veb);
					
					StreetViewBean sw = new StreetViewBean();
					sw.setCodiceEnte(cib.getCodiceEnte());
					sw.setLatitudine(latitudine);
					sw.setLongitudine(longitudine);
					cib.setStreetView(sw);

				}
				
				lst.add(cib);
			}
		}
		return lst;
	}//-------------------------------------------------------------------------
	

	
	public String precaricaAllegato662() throws Exception{
		
		String esito = "ricercaDettaglioBck.precaricaAllegato662";
		
		flgVisualizzaAllegato=false;
		
		if (bodIstruttoria != null && bodIstruttoria.getIdIst() != null){
		
	
			
				BodModello662Bean modello=this.getModello662();
				
				if (modello != null){
				
					
					
					
						
						if (modello.getFlgAscensore()!= null){
							this.setFlgAscensore(modello.getFlgAscensore());
						}
						
						if (modello.getCatResMag8()!= null && !modello.getCatResMag8().equals("") && modello.getClasseResMag8()!= null && !modello.getClasseResMag8().equals("")){
							this.setFlgResidenzialeMaggiore8(true);
							this.setCatResidenzialeMagg8(modello.getCatResMag8());
							this.setClasseResidenzialeMagg8(modello.getClasseResMag8());
						}
						else{
							this.setFlgResidenzialeMaggiore8(false);
						}
						
						if (modello.getCatResMin8()!= null && !modello.getCatResMin8().equals("") && modello.getClasseResMin8()!= null && !modello.getClasseResMin8().equals("")){
							this.setFlgResidenzialeMinore8(true);
							this.setCatResidenzialeMin8(modello.getCatResMin8());
							this.setClasseResidenzialeMin8(modello.getClasseResMin8());
						}
						else{
							this.setFlgResidenzialeMinore8(false);
						}
						
						if (modello.getClasseBox()!= null && !modello.getClasseBox().equals("")){
							this.setClasseBox(modello.getClasseBox());
							this.setFlgBox(true);
						}else{
							this.setClasseBox("");
							this.setFlgBox(false);
						}
						
						if (modello.getClasseDep()!= null && !modello.getClasseDep().equals("")){
							this.setClasseDeposito(modello.getClasseDep());
							this.setFlgDeposito(true);
						}else{
							this.setClasseDeposito("");
							this.setFlgDeposito(false);
						}
						
						if (modello.getClasseLab()!= null && !modello.getClasseLab().equals("")){
							this.setClasseLaboratorio(modello.getClasseLab());
							this.setFlgLaboratorio(true);
						}else{
							this.setClasseLaboratorio("");
							this.setFlgLaboratorio(false);
						}
						
						if (modello.getClasseNeg()!= null && !modello.getClasseNeg().equals("")){
							this.setClasseNegozio(modello.getClasseNeg());
							this.setFlgNegozio(true);
						}else{
							this.setClasseNegozio("");
							this.setFlgNegozio(false);
						}
						
						if (modello.getClassePAuto()!= null && !modello.getClassePAuto().equals("")){
							this.setClassePauto(modello.getClassePAuto());
							this.setFlgPauto(true);
						}else{
							this.setClassePauto("");
							this.setFlgPauto(false);
						}
						
						if (modello.getClasseUff()!= null && !modello.getClasseUff().equals("")){
							this.setClasseUfficio(modello.getClasseUff());
							this.setFlgUfficio(true);
						}else{
							this.setClasseUfficio("");
							this.setFlgUfficio(false);
						}
						
						if (modello.getFlgAllineamento()!= null){
							this.setFlgAllineamento(modello.getFlgAllineamento());
						}
				
					
			
		}else{
			
			// precarico il valore  del flag ascensore in base alle informazioni del docfa
			boolean flgAscensore=true;
			
			lstAscensori = this.getAscensori();
	        if (lstAscensori != null && lstAscensori.size()>0){
	        	Iterator it = lstAscensori.iterator();
	        	while(it.hasNext()){
	        		Object[] obj = (Object[])it.next();
	        		String asc = (String)obj[6];
	        		if (asc != null && asc.trim().equalsIgnoreCase("No"))
	        			flgAscensore=false;
	        	}
	        }else{
	        	flgAscensore=false;
	        }
			
			this.setFlgAscensore(flgAscensore);
			
			this.setCatResidenzialeMin8("");
			this.setClasseResidenzialeMin8("");
			this.setFlgResidenzialeMinore8(false);
			this.setCatResidenzialeMagg8("");
			this.setClasseResidenzialeMagg8("");
			this.setFlgResidenzialeMaggiore8(false);
			this.setClasseBox("");
			this.setFlgBox(false);
			this.setClasseDeposito("");
			this.setFlgDeposito(false);
			this.setClasseLaboratorio("");
			this.setFlgLaboratorio(false);
			this.setClasseNegozio("");
			this.setFlgNegozio(false);
			this.setClassePauto("");
			this.setFlgPauto(false);
			this.setClasseUfficio("");
			this.setFlgUfficio(false);
			this.setFlgAllineamento(false);
		}
			
		
		
		}
		return esito;
			
		
	}
	
	public String precaricaAllegato80() throws Exception{
		
		String esito = "ricercaDettaglioBck.precaricaAllegato80";
		
		flgVisualizzaAllegatoC=false;
		
		if (bodIstruttoria != null && bodIstruttoria.getIdIst() != null){
			
			/*
			 * Determino foglio e particella della uiu per recuperare la lista delle 
			 * concessioni da presentare in fase di compilazione del modulo delle 
			 * segnalazioni
			 */
			String fgl = "";
			String prl = ""; 
			concessioniLst = new LinkedList<ConcessioneBean>();
			if (lstUiu != null && lstUiu.size()>0){
				Object[] uiu = (Object[])lstUiu.get(0);
				fgl = (String)uiu[1];
				prl = (String)uiu[2]; 
				
				Properties prop = SqlHandler.loadProperties(propName);
				String sql = prop.getProperty("qryConcessioniIstruttoria");

				/*
				 * XXX anche se venisse ripetuta per ogni unità immobiliare, l'elenco 
				 * delle concessioni è sempre lo stesso perché si accede alla banca 
				 * dati con foglio e particella
				 */
				
				sql += "AND LPAD (TRIM (o.foglio), 4, '0') = LPAD (TRIM ('" + fgl + "'), 4, '0') " +
	            "AND LPAD (TRIM (o.particella), 5, '0') = LPAD (TRIM ('" + prl + "'), 5, '0') " +
	            "ORDER BY c.protocollo_data DESC ";
				Hashtable htQry = new Hashtable();
				htQry.put("queryString", sql);
				List<Object> listaConcessioni = bodLogicService.getList(htQry);
				List<ConcessioneBean> lstConcessioni = new LinkedList<ConcessioneBean>();
				Hashtable<String, ConcessioneBean> htConcDaIns = new Hashtable<String, ConcessioneBean>();
				if (listaConcessioni != null && listaConcessioni.size()>0){
					logger.info("XXX Num. Concessioni: " + listaConcessioni.size());
					ConcessioneBean concessione = null;
					ConcessioneBean bConcessione = null;
					for (int i=0; i<listaConcessioni.size(); i++){
						concessione = new ConcessioneBean();
						bConcessione = new ConcessioneBean();
						Object[] objs = (Object[])listaConcessioni.get(i);
				
//						concessione.setFoglio(Character.checkNullString((String)objs[0]));
//						bConcessione.setFoglio(Character.checkNullString((String)objs[0]));
//						concessione.setParticella(Character.checkNullString((String)objs[1]));
//						bConcessione.setParticella(Character.checkNullString((String)objs[1]));
//						concessione.setSubalterno(Character.checkNullString((String)objs[2]));
//						bConcessione.setSubalterno(Character.checkNullString((String)objs[2]));
						concessione.setConcessione(Character.checkNullString((String)objs[0]));
						bConcessione.setConcessione(Character.checkNullString((String)objs[0]));
						concessione.setProtocollo(Character.checkNullString((String)objs[1]));
						bConcessione.setProtocollo(Character.checkNullString((String)objs[1]));
						concessione.setProgressivo(Character.checkNullString((String)objs[2]));
						bConcessione.setProgressivo(Character.checkNullString((String)objs[2]));
						concessione.setOggetto(Character.checkNullString((String)objs[3]));
						bConcessione.setOggetto(Character.checkNullString((String)objs[3]));
						concessione.setTipoIntervento(Character.checkNullString((String)objs[4]));
						bConcessione.setTipoIntervento(Character.checkNullString((String)objs[4]));
						concessione.setDataRilascio(Character.checkNullString((String)objs[5]));
						bConcessione.setDataRilascio(Character.checkNullString((String)objs[5]));
						concessione.setDataFineLavori(Character.checkNullString((String)objs[6]));
						bConcessione.setDataFineLavori(Character.checkNullString((String)objs[6]));
						concessione.setPosizione(Character.checkNullString((String)objs[7]));
						bConcessione.setPosizione(Character.checkNullString((String)objs[7]));
						concessione.setDataProtocollo(Character.checkNullString((String)objs[8]));
						bConcessione.setDataProtocollo(Character.checkNullString((String)objs[8]));
						concessione.setCodiceFiscale(Character.checkNullString((String)objs[9]));
						bConcessione.setCodiceFiscale(Character.checkNullString((String)objs[9]));
						concessione.setPartitaIva("");
						bConcessione.setPartitaIva("");
						String den = Character.checkNullString((String)objs[10]);
						String cog = Character.checkNullString((String)objs[11]);
						String nom = Character.checkNullString((String)objs[12]);
						if (den.trim().equalsIgnoreCase(""))
							den = cog + " " + nom;
						concessione.setDenominazione(den);
						bConcessione.setDenominazione(Character.checkNullString((String)objs[13]) + ": " + den);
						concessione.setCognome(cog);
						bConcessione.setCognome(cog);
						concessione.setNome(nom);
						bConcessione.setNome(nom);
						concessione.setTipoSoggetto(Character.checkNullString((String)objs[13]));
						bConcessione.setTipoSoggetto(Character.checkNullString((String)objs[13]));

						lstConcessioni.add(concessione);
						/*
						 * Effettuo la distinct accorpando nella denominazione i due campi
						 * seguenti: tipoSoggetto + denominazione
						 */
						String key = Character.checkNullString((String)objs[1]) + "_" + Character.checkNullString((String)objs[2]);
						if (htConcDaIns.containsKey(key)){
							ConcessioneBean iConcessione = htConcDaIns.get(key);
							String nominativo = bConcessione.getDenominazione();
							bConcessione.setDenominazione( nominativo += "; " + iConcessione.getDenominazione() );
						}
						htConcDaIns.put(key, bConcessione);
					}
				}
				/*
				 * Dall'elenco delle concessioni con i soggetti accorpati e ordinati
				 * con data protocollo decrescente
				 */
				Hashtable<String, ConcessioneBean> htConcIns = new Hashtable<String, ConcessioneBean>();
				if (lstConcessioni != null && lstConcessioni.size()>0){
					ConcessioneBean concessione = null;
					ConcessioneBean bConcessione = null;
					for (int i=0; i<lstConcessioni.size(); i++){
						concessione = lstConcessioni.get(i);
						if (concessione != null){
							String key = concessione.getProtocollo() + "_" + concessione.getProgressivo();
							if (htConcIns.containsKey(key)){
								/*
								 * Ho già aggiunto la concessione con i soggetti 
								 * accorpati in lista
								 */
							}else{
								/*
								 * La concessione è da aggiungere alla lista da 
								 * mostrare, quindi la recupero dalla htConcDaIns
								 */
								bConcessione = htConcDaIns.get(key);
								htConcIns.put(key, bConcessione);
								concessioniLst.add(bConcessione);
							}
						}
					}
				}else{
					logger.info("XXX: Non ci sono concessioni da mostrare per compilare il DOC");
				}
			}

			
			BodModello80Bean modello=this.getModello80();
				
				if (modello != null){
				
					if (modello.getFlgAscensore()!= null){
						this.setFlgAscensoreC(modello.getFlgAscensore());
					}
					if (modello.getFlg662()!= null){
						this.setFlgRichiesta662(modello.getFlg662());
					}
					if (modello.getFlgAllineamento()!= null){
						this.setFlgAllineamentoUiu(modello.getFlgAllineamento());
					}
					if (modello.getFlgNo336()!= null){
						this.setFlgNonApplicabile336(modello.getFlgNo336());
					}
					if (modello.getOggettoDomanda()!= null)
						this.setOggettoDomanda(modello.getOggettoDomanda());
					if (modello.getTipoIntervento()!= null)
		        		this.setTipoIntervento(modello.getTipoIntervento());
		        	if (modello.getOggetto()!= null)
		        		this.setOggetto(modello.getOggetto());
		        	if (modello.getNumProgressivo()!= null)
		        		this.setNumProgressivo(modello.getNumProgressivo());
		        	if (modello.getNumProtocollo()!= null)
		        		this.setNumProtocollo(modello.getNumProtocollo());
		        	if (modello.getNote()!= null)
		        		this.setNote(modello.getNote());
		        	if (modello.getSettore()!= null)
		        		this.setSettore(modello.getSettore());
		        	if (modello.getDestinazioneZona()!= null)
		        		this.setDestinazioneZona(modello.getDestinazioneZona());
		        	if (modello.getSignificativaPresenza()!= null)
		        		this.setSignificativaPresenza(modello.getSignificativaPresenza());
		        	if (modello.getDestinazioneUiu()!= null)
		        		this.setDestinazioneUiu(modello.getDestinazioneUiu());
		        	if (modello.getCategoriaProposta()!= null)
		        		this.setCategoriaProposta(modello.getCategoriaProposta());
		        	if (modello.getClasseProposta()!= null)
		        		this.setClasseProposta(modello.getClasseProposta());
					
					
				}else{
					
					
						this.setFlgAscensoreC(false);
					
						this.setFlgRichiesta662(false);
					
						this.setFlgAllineamentoUiu(false);
					
						this.setFlgNonApplicabile336(false);
					
						this.setOggettoDomanda("");
					
		        		this.setTipoIntervento("");
		        	
		        		this.setOggetto("");
		        	
		        		this.setNumProgressivo("");
		        	
		        		this.setNumProtocollo("");
		        	
		        		this.setNote("");
		        	
		        		this.setSettore("");
		        	
		        		this.setDestinazioneZona("");
		        	
		        		this.setSignificativaPresenza("");
		        	
		        		this.setDestinazioneUiu("");
		        	
		        		this.setCategoriaProposta("");
		        		
		        		this.setClasseProposta("");
					
				}
				
		}
		
		return esito;
		
}
	
	private BodModello662Bean getModello662(){
		BodModello662Bean modello=null;
		
		Hashtable htQry = new Hashtable();
		
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		
		FilterItem fi = new FilterItem();
		fi.setOperatore("=");
		fi.setParametro("idIst");
		fi.setAttributo("istruttoria.idIst");
		fi.setValore(bodIstruttoria.getIdIst());
		aryFilters.add(fi);
		
	
		htQry.put("where", aryFilters);
		

		List lstObj = bodLogicService.getList(htQry, BodModello662Bean.class );
		
		if (lstObj!= null && lstObj.size()>0){
			
				 modello=(BodModello662Bean) lstObj.get(0);
		}
		
		return modello;
	}
	
	private BodModello80Bean getModello80(){
		BodModello80Bean modello=null;
		
		Hashtable htQry = new Hashtable();
		
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		
		FilterItem fi = new FilterItem();
		fi.setOperatore("=");
		fi.setParametro("idIst");
		fi.setAttributo("istruttoria.idIst");
		fi.setValore(bodIstruttoria.getIdIst());
		aryFilters.add(fi);
		
	
		htQry.put("where", aryFilters);
		

		List lstObj = bodLogicService.getList(htQry, BodModello80Bean.class );
		
		if (lstObj!= null && lstObj.size()>0){
			
				 modello=(BodModello80Bean) lstObj.get(0);
		}
		
		return modello;
	}
	
	public void selectFlagModello662(ActionEvent event) {
		
		String objId = ((HtmlSelectBooleanCheckbox)((HtmlAjaxSupport)event.getSource()).getParent()).getId().toString();
		if (objId != null){
			if (objId.equalsIgnoreCase("flgResidenzialeMinore8")){
				if (this.flgResidenzialeMinore8 == false){
					this.classeResidenzialeMin8="";
					this.catResidenzialeMin8="";
				}
			}
			else if (objId.equalsIgnoreCase("flgResidenzialeMaggiore8")){
				if (this.flgResidenzialeMaggiore8 == false){
					this.classeResidenzialeMagg8="";
					this.catResidenzialeMagg8="";
				}
			}
			else if (objId.equalsIgnoreCase("flgUfficio")){
				if (this.flgUfficio == false){
					this.classeUfficio="";
					}
			}
			else if (objId.equalsIgnoreCase("flgNegozio")){
				if (this.flgNegozio == false){
					this.classeNegozio="";
					}
			}
			else if (objId.equalsIgnoreCase("flgLaboratorio")){
				if (this.flgLaboratorio == false){
					this.classeLaboratorio="";
					}
			}
			else if (objId.equalsIgnoreCase("flgDeposito")){
				if (this.flgDeposito == false){
					this.classeDeposito="";
					}
			}
			else if (objId.equalsIgnoreCase("flgBox")){
				if (this.flgBox == false){
					this.classeBox="";
					}
			}
			else if (objId.equalsIgnoreCase("flgPauto")){
				if (this.flgPauto == false){
					this.classePauto="";
					}
			}
		}
	}
	
	public String saveAllegato662() throws Exception{
		String esito = "ricercaDettaglioBck.saveAllegato662";
		
		BodModello662Bean modello= null;
		
		
		if (bodIstruttoria != null ){
			
			//verifico se sono in modifica 
			if ( bodIstruttoria.getModello662() != null){
				
				Long idMod= bodIstruttoria.getModello662().getIdMod();
				 modello= (BodModello662Bean)bodLogicService.getItemById(idMod, BodModello662Bean.class);
				
			}else {
				modello= new BodModello662Bean();
				modello.setIstruttoria(bodIstruttoria);
			}
			modello.setDescrizione(this.descrizioneEnte);
			modello.setFlgAscensore(this.flgAscensore);
			modello.setFlgAllineamento(this.flgAllineamento);
			modello.setCatResMag8(this.catResidenzialeMagg8);
			modello.setClasseResMag8(this.classeResidenzialeMagg8);
			modello.setCatResMin8(this.catResidenzialeMin8);
			modello.setClasseResMin8(this.classeResidenzialeMin8);		
			modello.setClasseBox(this.classeBox);
			modello.setClasseDep(this.classeDeposito);
			modello.setClasseLab(this.classeLaboratorio);
			modello.setClasseNeg(this.classeNegozio);
			modello.setClassePAuto(this.classePauto);
			modello.setClasseUff(this.classeUfficio);
			
		}
		
		if ( bodIstruttoria.getModello662() != null){
			bodLogicService.updItem(modello, BodModello662Bean.class);
		}
		else{
			bodLogicService.addItem(modello, BodModello662Bean.class);
		}
		
		//setto il modello nell'istruttoria
		
		bodIstruttoria.setModello662(modello);
		
		flgVisualizzaAllegato=true;
		
		/*
		 * Preparo il file per allegato  B partendo dal template
		 */
		caricaAllegatoB();
				
		return esito;
	}
	
	
	public String saveAllegato80() throws Exception{
		String esito = "ricercaDettaglioBck.saveAllegato80";
		
		BodModello80Bean modello= null;
		
		
		if (bodIstruttoria != null ){
			
			//verifico se sono in modifica 
			if ( bodIstruttoria.getModello80() != null){
				Long idMod= bodIstruttoria.getModello80().getIdMod();
				modello= (BodModello80Bean)bodLogicService.getItemById(idMod, BodModello80Bean.class);
			}else {
				modello= new BodModello80Bean();
				modello.setIstruttoria(bodIstruttoria);
			}
			modello.setDescrizione(this.descrizioneEnte);
			modello.setFlgAscensore(this.flgAscensoreC);
			modello.setFlgAllineamento(this.flgAllineamentoUiu);
			modello.setFlg662(this.flgRichiesta662);
			modello.setFlgNo336(this.flgNonApplicabile336);
			
			modello.setOggettoDomanda(this.getOggettoDomanda());
			modello.setTipoIntervento(this.getTipoIntervento());
			modello.setOggetto(this.getOggetto());
			modello.setNumProgressivo(this.getNumProgressivo());
			modello.setNumProtocollo(this.getNumProtocollo());
			modello.setNote(this.getNote());
			modello.setSettore(this.getSettore());
			modello.setDestinazioneZona(this.getDestinazioneZona());
			modello.setSignificativaPresenza(this.getSignificativaPresenza());
			modello.setDestinazioneUiu(this.getDestinazioneUiu());
			modello.setCategoriaProposta(this.getCategoriaProposta());
			modello.setClasseProposta(this.getClasseProposta());
			
		}

		logger.info("CURRENT FK IST:" + modello.getIstruttoria().getIdIst() + " PROTOCOLLO: " + modello.getIstruttoria().getProtocollo() + " FORNITURA: " +  modello.getIstruttoria().getFornitura());
		logger.info("CURRENT PK MOD:" + modello.getIdMod() + " PROTOCOLLO: " + modello.getNumProtocollo());
		
		if (bodIstruttoria.getModello80() != null){
			bodLogicService.updItem(modello, BodModello80Bean.class);
		}
		else{
			bodLogicService.addItem(modello, BodModello80Bean.class);
		}
		
		//setto il modello nell'istruttoria
		
		bodIstruttoria.setModello80(modello);
		
		flgVisualizzaAllegatoC=true;
		
		/*
		 * Preparo il file per allegato  C  partendo dal template
		 */
		caricaAllegatoC();
				
		return esito;
	}

	public BodBean getSelBodBean() {
		return selBodBean;
	}

	public void setSelBodBean(BodBean selBodBean) {
		this.selBodBean = selBodBean;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List getLstDatGen() {
		return lstDatGen;
	}

	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}

	public void setLstDatGen(List<BodBean> lstDatGen) {
		this.lstDatGen = lstDatGen;
	}

	public List<DocfaDatiGenerali> getLstDatGenHelp() {
		return lstDatGenHelp;
	}

	public void setLstDatGenHelp(List<DocfaDatiGenerali> lstDatGenHelp) {
		this.lstDatGenHelp = lstDatGenHelp;
	}

	public List<DocfaIntestati> getLstIntestati() {
		return lstIntestati;
	}

	public void setLstIntestati(List<DocfaIntestati> lstIntestati) {
		this.lstIntestati = lstIntestati;
	}

	public List<Object> getLstDichiaranti() {
		return lstDichiaranti;
	}

	public void setLstDichiaranti(List<Object> lstDichiaranti) {
		this.lstDichiaranti = lstDichiaranti;
	}

	public List<Object> getLstUiu() {
		return lstUiu;
	}

	public void setLstUiu(List<Object> lstUiu) {
		this.lstUiu = lstUiu;
	}

	public List<BeniNonCensDTO> getLstBeniNonCensibili() {
		return lstBeniNonCensibili;
	}

	public void setLstBeniNonCensibili(List<BeniNonCensDTO> lstBeniNonCensibili) {
		this.lstBeniNonCensibili = lstBeniNonCensibili;
	}

	public List<String> getLstAnnotazioni() {
		return lstAnnotazioni;
	}

	public void setLstAnnotazioni(List<String> lstAnnotazioni) {
		this.lstAnnotazioni = lstAnnotazioni;
	}

	public String getCodiceEnte() {
		return codiceEnte;
	}

	public void setCodiceEnte(String codEnte) {
		this.codiceEnte = codEnte;
	}

	public List<Object> getLstDatiCensuari() {
		return lstDatiCensuari;
	}

	public void setLstDatiCensuari(List<Object> lstDatiCensuari) {
		this.lstDatiCensuari = lstDatiCensuari;
	}

	public Object[] getSelDatCen() {
		return selDatCen;
	}

	public void setSelDatCen(Object[] selDatCen) {
		this.selDatCen = selDatCen;
	}

	public List<Object> getLstSuperficiPerVano() {
		return lstSuperficiPerVano;
	}

	public void setLstSuperficiPerVano(List<Object> lstPlan) {
		this.lstSuperficiPerVano = lstPlan;
	}

	

//	public MediaData getMediaData() {
//		return mediaData;
//	}
//
//	public void setMediaData(MediaData mediaData) {
//		this.mediaData = mediaData;
//	}

	public List<PlanimetriaBean> getLstImagesFiles() {
		return lstImagesFiles;
	}

	public void setLstImagesFiles(List<PlanimetriaBean> lstImagesFiles) {
		this.lstImagesFiles = lstImagesFiles;
	}

	public PlanimetriaBean getCurrentPla() {
		return currentPla;
	}

	public void setCurrentPla(PlanimetriaBean currentPla) {
		this.currentPla = currentPla;
	}

	public List<DocfaInParteUnoDTO> getLstParteUno() {
		return lstParteUno;
	}

	public void setLstParteUno(List<DocfaInParteUnoDTO> lstParteUno) {
		this.lstParteUno = lstParteUno;
	}

	public List<OperatoreDTO> getLstOperatori() {
		return lstOperatori;
	}

	public void setLstOperatori(List<OperatoreDTO> lstOperatori) {
		this.lstOperatori = lstOperatori;
	}

	public List<ControlloIncrociatoBean> getLstControlliIncrociati() {
		return lstControlliIncrociati;
	}

	public void setLstControlliIncrociati(
			List<ControlloIncrociatoBean> lstControlliIncrociati) {
		this.lstControlliIncrociati = lstControlliIncrociati;
	}
	
	public List<ControlloClassamentoConsistenzaDTO> getLstControlliClassCons() {
		return lstControlliClassCons;
	}

	public void setLstControlliClassCons(
			List<ControlloClassamentoConsistenzaDTO> lstControlliClassCons) {
		this.lstControlliClassCons = lstControlliClassCons;
	}
	
	public ControlloClassamentoConsistenzaDTO getSelClaCom() {
		return selClaCom;
	}

	public void setSelClaCom(ControlloClassamentoConsistenzaDTO selClaCom) {
		this.selClaCom = selClaCom;
	}
	
	public List<ControlloClassamentoConsistenzaDTO> getLstClaCom() {
		return lstClaCom;
	}

	public void setLstClaCom(List<ControlloClassamentoConsistenzaDTO> lstClaCom) {
		this.lstClaCom = lstClaCom;
	}

	public HtmlInputText getTxtRapporto() {
		return txtRapporto;
	}

	public void setTxtRapporto(HtmlInputText txtRapporto) {
		this.txtRapporto = txtRapporto;
	}

	public List<ClassamentoCorpoFabbricaBean> getLstClassCorpiFabbrica() {
		return lstClassCorpiFabbrica;
	}

	public void setLstClassCorpiFabbrica(
			List<ClassamentoCorpoFabbricaBean> lstClassCorpiFabbrica) {
		this.lstClassCorpiFabbrica = lstClassCorpiFabbrica;
	}

	public ClassamentoCorpoFabbricaBean getSelClaCorpoFabbrica() {
		return selClaCorpoFabbrica;
	}

	public void setSelClaCorpoFabbrica(
			ClassamentoCorpoFabbricaBean selClaCorpiFabbrica) {
		this.selClaCorpoFabbrica = selClaCorpiFabbrica;
	}

	public List<TrasformazioneEdiliziaBean> getLstTrasfEdiliziaVariazioni() {
		return lstTrasfEdiliziaVariazioni;
	}

	public void setLstTrasfEdiliziaVariazioni(
			List<TrasformazioneEdiliziaBean> lstTrasfEdilizia) {
		this.lstTrasfEdiliziaVariazioni = lstTrasfEdilizia;
	}

	public List<TrasformazioneEdiliziaBean> getLstTrasfEdiliziaSoppressioni() {
		return lstTrasfEdiliziaSoppressioni;
	}

	public void setLstTrasfEdiliziaSoppressioni(
			List<TrasformazioneEdiliziaBean> lstTrasfEdiliziaSoppressioni) {
		this.lstTrasfEdiliziaSoppressioni = lstTrasfEdiliziaSoppressioni;
	}

	public List<TrasformazioneEdiliziaBean> getLstTrasfEdiliziaCostituzioni() {
		return lstTrasfEdiliziaCostituzioni;
	}

	public void setLstTrasfEdiliziaCostituzioni(
			List<TrasformazioneEdiliziaBean> lstTrasfEdiliziaCostituzioni) {
		this.lstTrasfEdiliziaCostituzioni = lstTrasfEdiliziaCostituzioni;
	}

	public Double getTotRenPerCoefSop() {
		return totRenPerCoefSop;
	}

	public void setTotRenPerCoefSop(Double totRenPerCoefSop) {
		this.totRenPerCoefSop = totRenPerCoefSop;
	}

	public Double getTotRenPerCoefCos() {
		return totRenPerCoefCos;
	}

	public void setTotRenPerCoefCos(Double totRenPerCoefCos) {
		this.totRenPerCoefCos = totRenPerCoefCos;
	}

	public VirtualEarthBean getCurrentVirtualEarth() {
		return currentVirtualEarth;
	}

	public void setCurrentVirtualEarth(VirtualEarthBean currentVirtualEarth) {
		this.currentVirtualEarth = currentVirtualEarth;
	}

	public PdfDocfaBean getPdfDocfa() {
		return pdfDocfa;
	}

	public void setPdfDocfa(PdfDocfaBean pdfDocfa) {
		this.pdfDocfa = pdfDocfa;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public List<Object> getLstAscensori() {
		return lstAscensori;
	}

	public void setLstAscensori(List<Object> lstAscensori) {
		this.lstAscensori = lstAscensori;
	}

	public List<Object> getLstBagni() {
		return lstBagni;
	}

	public void setLstBagni(List<Object> lstBagni) {
		this.lstBagni = lstBagni;
	}

	public TimeControl getTc() {
		return tc;
	}

	public void setTc(TimeControl tc) {
		this.tc = tc;
	}

	public Object[] getSelClaSub() {
		return selClaSub;
	}

	public void setSelClaSub(Object[] selClaSub) {
		this.selClaSub = selClaSub;
	}

	public List<Object> getLstClaCorFab() {
		return lstClaCorFab;
	}

	public void setLstClaCorFab(List<Object> lstClaCorFab) {
		this.lstClaCorFab = lstClaCorFab;
	}

	public HtmlInputTextarea getTxtAnnotazioni() {
		return txtAnnotazioni;
	}

	public void setTxtAnnotazioni(HtmlInputTextarea txtAnnotazioni) {
		this.txtAnnotazioni = txtAnnotazioni;
	}

	public HtmlSelectManyCheckbox getCbxEsitiIstruttoria() {
		return cbxEsitiIstruttoria;
	}

	public void setCbxEsitiIstruttoria(HtmlSelectManyCheckbox cbxEsitiIstruttoria) {
		this.cbxEsitiIstruttoria = cbxEsitiIstruttoria;
	}

	public HtmlSelectOneRadio getRdoCoerente() {
		return rdoCoerente;
	}

	public void setRdoCoerente(HtmlSelectOneRadio rdoCoerente) {
		this.rdoCoerente = rdoCoerente;
	}

	public HtmlSelectOneRadio getRdoIncoerente() {
		return rdoIncoerente;
	}

	public void setRdoIncoerente(HtmlSelectOneRadio rdoIncoerente) {
		this.rdoIncoerente = rdoIncoerente;
	}

	public HtmlSelectOneRadio getRdoAssenza() {
		return rdoAssenza;
	}

	public void setRdoAssenza(HtmlSelectOneRadio rdoAssenza) {
		this.rdoAssenza = rdoAssenza;
	}	
	
	public BodSegnalazioneBean getBodSegnalazione() {
		return bodSegnalazione;
	}

	public void setBodSegnalazione(BodSegnalazioneBean bodSegnalazione) {
		this.bodSegnalazione = bodSegnalazione;
	}

	public ComboBoxRch getCbxCoerenteComDoc() {
		return cbxCoerenteComDoc;
	}

	public void setCbxCoerenteComDoc(ComboBoxRch cbxCoerenteComDoc) {
		this.cbxCoerenteComDoc = cbxCoerenteComDoc;
	}

	public List<BodFabbricatoBean> getLstIstruttoriaFab() {
		return lstIstruttoriaFab;
	}

	public void setLstIstruttoriaFab(List<BodFabbricatoBean> lstBodFabbricati) {
		this.lstIstruttoriaFab = lstBodFabbricati;
	}

	public List<BodUiuBean> getLstIstruttoriaUiu() {
		return lstIstruttoriaUiu;
	}

	public void setLstIstruttoriaUiu(List<BodUiuBean> lstBodUiu) {
		this.lstIstruttoriaUiu = lstBodUiu;
	}

	public CalendarBoxRch getCalDataFineLavori() {
		return calDataFineLavori;
	}

	public void setCalDataFineLavori(CalendarBoxRch calDataFineLavori) {
		this.calDataFineLavori = calDataFineLavori;
	}

	public BodIstruttoriaSwitch getBodIstruttoriaSwitch() {
		return bodIstruttoriaSwitch;
	}

	public void setBodIstruttoriaSwitch(BodIstruttoriaSwitch istruttoriaPag) {
		this.bodIstruttoriaSwitch = istruttoriaPag;
	}

	public BodSegnalazioneSwitch getBodSegnalazioneSwitch() {
		return bodSegnalazioneSwitch;
	}

	public void setBodSegnalazioneSwitch(BodSegnalazioneSwitch segnalazionePag) {
		this.bodSegnalazioneSwitch = segnalazionePag;
	}

	public BodUploadBean getBodUpload() {
		return bodUpload;
	}

	public void setBodUpload(BodUploadBean bodUpload) {
		this.bodUpload = bodUpload;
	}

	public BodUploadSwitch getBodUploadSwitch() {
		return bodUploadSwitch;
	}

	public void setBodUploadSwitch(BodUploadSwitch bodUploadSwitch) {
		this.bodUploadSwitch = bodUploadSwitch;
	}

	public String getTempFileDocC() {
		return tempFileDocC;
	}

	public void setTempFileDocC(String nameTempDoc) {
		this.tempFileDocC = nameTempDoc;
	}

	/*public String getTempFileDocB() {
		return tempFileDocB;
	}

	public void setTempFileDocB(String tempFileDocB) {
		this.tempFileDocB = tempFileDocB;
	}*/

	public String getTempPathDocB() {
		logger.info("TempPathDocB: " + tempPathDocB);
		return tempPathDocB;
	}

	public void setTempPathDocB(String tempPathDocB) {
		this.tempPathDocB = tempPathDocB;
	}

	public Boolean getIsAutorizzatoRicerca() {
		return isAutorizzatoRicerca;
	}

	public void setIsAutorizzatoRicerca(Boolean isAutorizzatoRicerca) {
		this.isAutorizzatoRicerca = isAutorizzatoRicerca;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	public void setNomeAllegato(String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
	}

	public String getEmptyComboItem() {
		return emptyComboItem;
	}

	public String getEsitoControlloCoerente() {
		return esitoControlloCoerente;
	}

	public void setEsitoControlloCoerente(String esitoControlloCoerente) {
		this.esitoControlloCoerente = esitoControlloCoerente;
	}

	public String getEsitoControlloComDoc() {
		return esitoControlloComDoc;
	}

	public void setEsitoControlloComDoc(String esitoControlloComDoc) {
		this.esitoControlloComDoc = esitoControlloComDoc;
	}

	public String getEsitoControlloIncoerente() {
		return esitoControlloIncoerente;
	}

	public void setEsitoControlloIncoerente(String esitoControlloIncoerente) {
		this.esitoControlloIncoerente = esitoControlloIncoerente;
	}

	public String getEsitoControlloAssenza() {
		return esitoControlloAssenza;
	}

	public void setEsitoControlloAssenza(String esitoControlloAssenza) {
		this.esitoControlloAssenza = esitoControlloAssenza;
	}

	public Boolean getFlgAscensore() {
		return flgAscensore;
	}

	public void setFlgAscensore(Boolean flgAscensore) {
		this.flgAscensore = flgAscensore;
	}

	public Boolean getFlgResidenzialeMinore8() {
		return flgResidenzialeMinore8;
	}

	public void setFlgResidenzialeMinore8(Boolean flgResidenzialeMinore8) {
		this.flgResidenzialeMinore8 = flgResidenzialeMinore8;
	}

	public Boolean getFlgResidenzialeMaggiore8() {
		return flgResidenzialeMaggiore8;
	}

	public void setFlgResidenzialeMaggiore8(Boolean flgResidenzialeMaggiore8) {
		this.flgResidenzialeMaggiore8 = flgResidenzialeMaggiore8;
	}

	public String getCatResidenzialeMin8() {
		return catResidenzialeMin8;
	}

	public void setCatResidenzialeMin8(String catResidenzialeMin8) {
		this.catResidenzialeMin8 = catResidenzialeMin8;
	}

	public String getCatResidenzialeMagg8() {
		return catResidenzialeMagg8;
	}

	public void setCatResidenzialeMagg8(String catResidenzialeMagg8) {
		this.catResidenzialeMagg8 = catResidenzialeMagg8;
	}

	public String getClasseResidenzialeMin8() {
		return classeResidenzialeMin8;
	}

	public void setClasseResidenzialeMin8(String classeResidenzialeMin8) {
		this.classeResidenzialeMin8 = classeResidenzialeMin8;
	}

	public String getClasseResidenzialeMagg8() {
		return classeResidenzialeMagg8;
	}

	public void setClasseResidenzialeMagg8(String classeResidenzialeMagg8) {
		this.classeResidenzialeMagg8 = classeResidenzialeMagg8;
	}

	public Boolean getFlgUfficio() {
		return flgUfficio;
	}

	public void setFlgUfficio(Boolean flgUfficio) {
		this.flgUfficio = flgUfficio;
	}

	public Boolean getFlgNegozio() {
		return flgNegozio;
	}

	public void setFlgNegozio(Boolean flgNegozio) {
		this.flgNegozio = flgNegozio;
	}

	public Boolean getFlgLaboratorio() {
		return flgLaboratorio;
	}

	public void setFlgLaboratorio(Boolean flgLaboratorio) {
		this.flgLaboratorio = flgLaboratorio;
	}

	public Boolean getFlgDeposito() {
		return flgDeposito;
	}

	public void setFlgDeposito(Boolean flgDeposito) {
		this.flgDeposito = flgDeposito;
	}

	public Boolean getFlgBox() {
		return flgBox;
	}

	public void setFlgBox(Boolean flgBox) {
		this.flgBox = flgBox;
	}

	public Boolean getFlgPauto() {
		return flgPauto;
	}

	public void setFlgPauto(Boolean flgPauto) {
		this.flgPauto = flgPauto;
	}

	public String getClasseUfficio() {
		return classeUfficio;
	}

	public void setClasseUfficio(String classeUfficio) {
		this.classeUfficio = classeUfficio;
	}

	public String getClasseNegozio() {
		return classeNegozio;
	}

	public void setClasseNegozio(String classeNegozio) {
		this.classeNegozio = classeNegozio;
	}

	public String getClasseLaboratorio() {
		return classeLaboratorio;
	}

	public void setClasseLaboratorio(String classeLaboratorio) {
		this.classeLaboratorio = classeLaboratorio;
	}

	public String getClasseDeposito() {
		return classeDeposito;
	}

	public void setClasseDeposito(String classeDeposito) {
		this.classeDeposito = classeDeposito;
	}

	public String getClasseBox() {
		return classeBox;
	}

	public void setClasseBox(String classeBox) {
		this.classeBox = classeBox;
	}

	public String getClassePauto() {
		return classePauto;
	}

	public void setClassePauto(String classePauto) {
		this.classePauto = classePauto;
	}

	public Boolean getFlgAllineamento() {
		return flgAllineamento;
	}

	public void setFlgAllineamento(Boolean flgAllineamento) {
		this.flgAllineamento = flgAllineamento;
	}

	public String getPathAllegati() {
		return pathAllegati;
	}

	public void setPathAllegati(String pathAllegati) {
		this.pathAllegati = pathAllegati;
	}

	public Boolean getFlgVisualizzaAllegato() {
		return flgVisualizzaAllegato;
	}

	public void setFlgVisualizzaAllegato(Boolean flgVisualizzaAllegato) {
		this.flgVisualizzaAllegato = flgVisualizzaAllegato;
	}

	public BodUploadBean getBodUploadB() {
		return bodUploadB;
	}

	public void setBodUploadB(BodUploadBean bodUploadB) {
		this.bodUploadB = bodUploadB;
	}

	public BodAllegatiBean getBodAllegato() {
		return bodAllegato;
	}

	public void setBodAllegato(BodAllegatiBean bodAllegato) {
		this.bodAllegato = bodAllegato;
	}

	public String getNomeAllegatoB() {
		return nomeAllegatoB;
	}

	public void setNomeAllegatoB(String nomeAllegatoB) {
		this.nomeAllegatoB = nomeAllegatoB;
	}

	public String getMinimoAnniFornitura() {
		return minimoAnniFornitura;
	}

	public void setMinimoAnniFornitura(String minimoAnniFornitura) {
		this.minimoAnniFornitura = minimoAnniFornitura;
	}

	public String getMaxAnniFornitura() {
		return maxAnniFornitura;
	}

	public void setMaxAnniFornitura(String maxAnniFornitura) {
		this.maxAnniFornitura = maxAnniFornitura;
	}

	public String getOggettoDomanda() {
		return oggettoDomanda;
	}

	public void setOggettoDomanda(String oggettoDomanda) {
		this.oggettoDomanda = oggettoDomanda;
	}

	public String getTipoIntervento() {
		return tipoIntervento;
	}

	public void setTipoIntervento(String tipoIntervento) {
		this.tipoIntervento = tipoIntervento;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getNumProgressivo() {
		return numProgressivo;
	}

	public void setNumProgressivo(String numProgressivo) {
		this.numProgressivo = numProgressivo;
	}

	public String getNumProtocollo() {
		return numProtocollo;
	}

	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSettore() {
		return settore;
	}

	public void setSettore(String settore) {
		this.settore = settore;
	}

	public String getDestinazioneZona() {
		return destinazioneZona;
	}

	public void setDestinazioneZona(String destinazioneZona) {
		this.destinazioneZona = destinazioneZona;
	}

	public String getSignificativaPresenza() {
		return significativaPresenza;
	}

	public void setSignificativaPresenza(String significativaPresenza) {
		this.significativaPresenza = significativaPresenza;
	}

	public String getDestinazioneUiu() {
		return destinazioneUiu;
	}

	public void setDestinazioneUiu(String destinazioneUiu) {
		this.destinazioneUiu = destinazioneUiu;
	}

	public String getCategoriaProposta() {
		return categoriaProposta;
	}

	public void setCategoriaProposta(String categoriaProposta) {
		this.categoriaProposta = categoriaProposta;
	}

	public Boolean getFlgRichiesta662() {
		return flgRichiesta662;
	}

	public void setFlgRichiesta662(Boolean flgRichiesta662) {
		this.flgRichiesta662 = flgRichiesta662;
	}

	public Boolean getFlgAllineamentoUiu() {
		return flgAllineamentoUiu;
	}

	public void setFlgAllineamentoUiu(Boolean flgAllineamentoUiu) {
		this.flgAllineamentoUiu = flgAllineamentoUiu;
	}

	public Boolean getFlgNonApplicabile336() {
		return flgNonApplicabile336;
	}

	public void setFlgNonApplicabile336(Boolean flgNonApplicabile336) {
		this.flgNonApplicabile336 = flgNonApplicabile336;
	}

	public Boolean getFlgAscensoreC() {
		return flgAscensoreC;
	}

	public void setFlgAscensoreC(Boolean flgAscensoreC) {
		this.flgAscensoreC = flgAscensoreC;
	}

	public Boolean getFlgVisualizzaAllegatoC() {
		return flgVisualizzaAllegatoC;
	}

	public void setFlgVisualizzaAllegatoC(Boolean flgVisualizzaAllegatoC) {
		this.flgVisualizzaAllegatoC = flgVisualizzaAllegatoC;
	}

	public String getClasseProposta() {
		return classeProposta;
	}

	public void setClasseProposta(String classeProposta) {
		this.classeProposta = classeProposta;
	}

	public String getMsgPraticaStatus() {
		return msgPraticaStatus;
	}

	public void setMsgPraticaStatus(String msgPraticaStatus) {
		this.msgPraticaStatus = msgPraticaStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDescrizioneEnte() {
		return descrizioneEnte;
	}

	public void setDescrizioneEnte(String descrizioneEnte) {
		this.descrizioneEnte = descrizioneEnte;
	}

	public String getCurrentOperator() {
		return currentOperator;
	}

	public void setCurrentOperator(String currentOperator) {
		this.currentOperator = currentOperator;
	}

	public List<ConcessioneBean> getConcessioniLst() {
		return concessioniLst;
	}

	public void setConcessioniLst(List<ConcessioneBean> concessioniLst) {
		this.concessioniLst = concessioniLst;
	}

	public ConcessioneBean getSelConcessione() {
		return selConcessione;
	}

	public void setSelConcessione(ConcessioneBean selConcessione) {
		this.selConcessione = selConcessione;
	}

	public void setPathAllegatiSegnalazioni(String pathAllegatiSegnalazioni) {
		this.pathAllegatiSegnalazioni = pathAllegatiSegnalazioni;
	}
	
	public String getPathXml() {
		String pathXml = this.getRootPathEnte() + "xmlBod/";
		File dir = new File(pathXml);
		if(!dir.exists())
			dir.mkdirs();
		return pathXml;
	}

	public String getPathFornitura() {
		String pathFornitura = this.getRootPathEnte() + "fornitureBod/";
		File dir = new File(pathFornitura);
		if(!dir.exists())
			dir.mkdirs();
		return pathFornitura;
	}

	public List<ControlloClassamentoConsistenzaDTO> getSelLstClaZcDiverse() {
		return selLstClaZcDiverse;
	}

	public void setSelLstClaZcDiverse(List<ControlloClassamentoConsistenzaDTO> selLstClaZcDiverse) {
		this.selLstClaZcDiverse = selLstClaZcDiverse;
	}


	public List<PlanimetriaBean> getLstImagesFilesC340() {
		return lstImagesFilesC340;
	}

	public void setLstImagesFilesC340(List<PlanimetriaBean> lstImagesFilesC340) {
		this.lstImagesFilesC340 = lstImagesFilesC340;
	}

	public List<PlanimetriaBean> getLstImagesFilesDocfa() {
		return lstImagesFilesDocfa;
	}

	public void setLstImagesFilesDocfa(List<PlanimetriaBean> lstImagesFilesDocfa) {
		this.lstImagesFilesDocfa = lstImagesFilesDocfa;
	}

	public List<Object> getLstUiuAllProt() {
		return lstUiuAllProt;
	}

	public void setLstUiuAllProt(List<Object> lstUiuAllProt) {
		this.lstUiuAllProt = lstUiuAllProt;
	}

	
	
}
