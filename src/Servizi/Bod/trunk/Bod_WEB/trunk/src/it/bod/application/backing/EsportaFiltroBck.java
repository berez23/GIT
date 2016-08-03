package it.bod.application.backing;

import it.bod.application.beans.BodConfrontoBean;
import it.bod.application.beans.BodEsitoBean;
import it.bod.application.beans.BodFornituraBean;
import it.bod.application.beans.BodFornituraDocfaBean;
import it.bod.application.beans.BodIstruttoriaBean;
import it.bod.application.beans.BodUploadBean;
import it.bod.application.beans.switches.BodUploadSwitch;
import it.bod.application.common.FilterItem;
import it.bod.application.common.Info;
import it.bod.application.common.MasterClass;
import it.bod.application.common.Schiavo;
import it.bod.business.service.BodLogicService;
import it.doro.tools.Character;
import it.doro.tools.Number;
import it.persistance.common.SqlHandler;
import it.webred.permessi.GestionePermessi;
import it.webred.rich.common.ComboBoxRch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.faces.component.html.HtmlInputText;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class EsportaFiltroBck  extends MasterClass{
	
	private static Logger logger = Logger.getLogger(EsportaFiltroBck.class.getName());
	
	private static final long serialVersionUID = 5126833467036849602L;
	
	private BodLogicService bodLogicService = null;
 	private List<BodFornituraBean> listaForniture = null;
	private List<BodIstruttoriaBean> listaIstruttorie = null;
	private List<Object> listaSegnalazioni = null;
	private List<Object> listaUiuCatasto = null;
	private List<Object> listaDatiCensuari = null;
	private List<BodIstruttoriaBean> elencoIstruttorie = null;
	private HtmlInputText txtProgressivo = null;
	private HtmlInputText txtNomeFile = null;
	private ComboBoxRch boxStatus = null;
	private Integer numIstruttorieDaZippare = 0;
	private String maxPro = "0";
	private String pathXml = "";
	private String pathFornitura = "";
	//private Boolean chkGenera = null;
	private Boolean success = new Boolean(false);
	private Boolean error = new Boolean(false);
	private Long currentFornitura = new Long(0);
	private Boolean isAutorizzatoEsporta = false;
	private Boolean esportaXls = false;
	private Boolean generaXls = true;
	
	private String codiceEnte;
	private String xlsPath = "";
	
	private BodUploadBean bodUpload = null;
	private BodUploadSwitch bodUploadSwitch = null;
	
	private BodEsitoBean esitoCur = null;
	private List<BodEsitoBean> listaEsiti = null;
	private List<BodConfrontoBean> listaConfronti = null;
	private BodConfrontoBean confrontoCur = null;
	private HtmlInputText txtProtocollo = null;
	private HtmlInputText txtFoglio = null;
	private HtmlInputText txtParticella = null;
	private HtmlInputText txtSubalterno = null;
	private HtmlInputText txtAnnoMese = null;
	private String txtIncConsistenza = null;
	private String txtIncClassamento = null;
	private String txtIncDestinazione = null;
	private String txtIncPlanimetria = null;
	private String cbxEsitoCollaudo = null;
	private ArrayList<SelectItem> aylIncOpz = null;
	private ArrayList<SelectItem> aylEsitiCollaudoOpz = null;
	
	public EsportaFiltroBck(){
		super.initializer();
		
	}//-------------------------------------------------------------------------
	
	protected String getRootPathEnte(){
		
		String pathDatiDiogeneEnte = super.getPathDatiDiogene()+ this.getEnteCorrente().toUpperCase()+ "/";
		//logger.info("PATH DATI DIOGENE ENTE: " + pathDatiDiogeneEnte);
		return pathDatiDiogeneEnte;
	}//-------------------------------------------------------------------------
	
	private String getEnteCorrente(){
		codiceEnte = super.getEnte().getBelfiore();
		return codiceEnte;
	}//-------------------------------------------------------------------------
	
	public String preparaFiltro(){
		String esito = "esportaFiltroBck.preparaFiltro";
		logger.info(esito);
		/*
		 * Imposta la variabile in sessione per il percorso delle immagini
		 */
//		FacesContext context = FacesContext.getCurrentInstance();
//		ExternalContext extContext = context.getExternalContext();
//		HttpServletRequest req = (HttpServletRequest)extContext.getRequest();
//		HttpSession jsfSession = (HttpSession)req.getSession(true);
//		jsfSession.setAttribute("ctxName", req.getContextPath());
		
		super.initializer(); //necessario per verifica dei permessi dell'utente
		isAutorizzatoEsporta = GestionePermessi.autorizzato(utente, nomeApplicazione, "Esporta dati Bod", "Esportazione completa");
		if (isAutorizzatoEsporta){
			init();			
		}else{
			esito = "failure";
			return esito;
		}
		
		esito = ricerca(); //per default si visualizzano tutte le forniture generate
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String importaEsitiShow(){
		String esito = "esportaFiltroBck.importaEsitiShow";
		
		bodUploadSwitch = new BodUploadSwitch();
		bodUploadSwitch.setAcceptedTypes("zip, ZIP");
		bodUploadSwitch.setMaxFileQuantity(5d);
		//bodUploadSwitch.setAutoUpload(true);
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public void uploadListener(UploadEvent event){
		/*
		 * Questo metodo viene invocato n volte in base al numero dei files aggiunti
		 * nel componente rich:fileUpload (=max 5 files)
		 */
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
			
			bodUpload = new BodUploadBean();
			bodUpload.setName(fileName);
			bodUpload.setSize(new Double(fileSize));
			
			File file = item.getFile();
			String absolutePath = file.getAbsolutePath();
			
			/*
			 * Cartelle di destinazione del file uploadato e zippato per saperne le 
			 * dimensioni e memorizzarle nel db 
			 */
			long istante = System.currentTimeMillis();
			String unzipPath = this.getPathDatiDiogene() + this.getEnteCorrente().toUpperCase() + "/Bod/";
			String zipPath = this.getPathLoad() + this.getEnteCorrente().toUpperCase() + "/Bod/";

			logger.info("File: " + absolutePath);
			
			bodUpload.setZipTemporaryPath(zipPath + fileName);			
			bodUpload.setAbsolutePathSource(absolutePath);
			/*
			 * controllo che lo zip non sia stato già uppato in passato
			 */
			File zipFile = new File(zipPath + fileName);
			if (!zipFile.exists()){
				/*
				 * Se non esiste ancora lo sposto dal percorso temporaneo di upload
				 */
				boolean eseguito = new File(absolutePath).renameTo(new File(zipPath + fileName));
				if (eseguito){
					String[] decompresso = Schiavo.unzipper(unzipPath, zipPath + fileName  );
					/*
					 * Leggere e inserire i dati contenuti nel file XML
					 */
					if (new Boolean(decompresso[0]) ){
						logger.info("File decompresso in: " + unzipPath + fileName);
						// inserimento valori presi da XML
						esitoCur = new BodEsitoBean();
						esitoCur.setDataUpload(new Date(System.currentTimeMillis()));
						esitoCur.setNomeFileEsito(unzipPath + decompresso[1]);
						esitoCur.setNomeFileZip(zipPath + fileName);
						/*
						 * Leggi XML
						 */
						readXMLfile();
						/*
						 * Insert nel db
						 */
						if (listaEsiti != null && listaEsiti.size()>0){
							/*
							 * - completare le informazioni: dataUpload, nomeFileEsito, nomeFileZip
							 * - inserire gli esiti recuperati dal file XML
							 */
							Date oggi = new Date(System.currentTimeMillis());
							for (int index=0; index<listaEsiti.size(); index++){
								BodEsitoBean iEsito = listaEsiti.get(index);
								iEsito.setDataUpload(oggi);
								iEsito.setNomeFileEsito(unzipPath + decompresso[1]);
								iEsito.setNomeFileZip(zipPath + fileName);
								
								bodLogicService.addItem(iEsito, BodEsitoBean.class);

							}
						}else{
							/*
							 * Non ci sono esiti da inserire
							 */
						}
					}else
						logger.info("Impossibile decomprimere: " + fileName);
				}else{
					logger.info("Impossibile spostare il file ZIP nella Load: " + absolutePath);
				}
			}else{
				logger.info("File già esistente nella LOAD: " + zipPath + fileName);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}//-------------------------------------------------------------------------
	
	private void init(){
		/*
		 * Inizializza filtri
		 */
		txtFoglio = new HtmlInputText();
		txtParticella = new HtmlInputText();
		txtSubalterno = new HtmlInputText();
		txtProtocollo = new HtmlInputText();
		txtProgressivo = new HtmlInputText();
		txtAnnoMese = new HtmlInputText();
		txtNomeFile = new HtmlInputText();
		boxStatus = new ComboBoxRch();
		//chkGenera = new Boolean(false);
		ArrayList<SelectItem> alStati = new ArrayList<SelectItem>();
		alStati.add(new SelectItem(Info.htFornitureStatus.get("0"), Info.htFornitureStatus.get("0")));
		alStati.add(new SelectItem(Info.htFornitureStatus.get("1"), Info.htFornitureStatus.get("1")));
		//boxStatus.setSelectItems(alStati);
		String suggestionsStati = Info.htFornitureStatus.get("0") + ", " + Info.htFornitureStatus.get("1");
		boxStatus.setSuggestions(suggestionsStati);
		boxStatus.setState("");
		
		/*
		 * Opzioni per incoerenze in maschera confronto
		 */
		
		aylEsitiCollaudoOpz = new ArrayList<SelectItem>(Info.aylEsitiCollaudoOpz);
		
		aylIncOpz = new ArrayList<SelectItem>();
		aylIncOpz.add( new SelectItem("", "") );
		aylIncOpz.add( new SelectItem("false", "NO") );
		aylIncOpz.add( new SelectItem("true", "SI") );
		generaXls = true;
		esportaXls = false;
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
		listaIstruttorie = bodLogicService.getList(htQry, BodIstruttoriaBean.class);
		numIstruttorieDaZippare = 0;
		if (listaIstruttorie != null && listaIstruttorie.size() > 0){
			numIstruttorieDaZippare = listaIstruttorie.size();
			//chkGenera = true;
		}else{
			//chkGenera = false;
		}
		
		
		error = false;
		success = false;
	}//-------------------------------------------------------------------------
	
	public String ricerca(){
		String esito = "esportaFiltroBck.ricerca";
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		String progressivo = (String)this.txtProgressivo.getValue();
		if (progressivo != null && !progressivo.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("progressivo");
			fi.setOperatore("=");
			fi.setParametro("progressivo");
			fi.setValore(new Long(progressivo));
			aryFilters.add(fi);
		}
		String nomeFile = (String)this.txtNomeFile.getValue();
		if (nomeFile != null && !nomeFile.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("fileName");
			fi.setOperatore("LIKE");
			fi.setParametro("fileName");
			fi.setValore(nomeFile.trim().toUpperCase());
			aryFilters.add(fi);
		}
		String status = boxStatus.getState();
		if (status != null && !status.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("status");
			fi.setOperatore("=");
			fi.setParametro("status");
			fi.setValore(status.trim().toUpperCase());
			aryFilters.add(fi);
		}
		Hashtable htQry = new Hashtable();
		if (aryFilters != null && aryFilters.size()>0)
			htQry.put("where", aryFilters);
		htQry.put("orderBy", "progressivo");		
		listaForniture = bodLogicService.getList(htQry, BodFornituraBean.class);
		
		init();
		
		return esito;
	}//-------------------------------------------------------------------------
	
	private void clean(){
		txtIncConsistenza = "";
		txtIncClassamento = "";
		txtIncDestinazione = "";
		txtIncPlanimetria = "";
	}//-------------------------------------------------------------------------
	
	public String cerca(){
		String esito = "esportaFiltroBck.cerca";
		/*
		 * //cerca segnalazioni di ogni uiu
		 */
		listaConfronti = new LinkedList<BodConfrontoBean>();
		generaXls = true;
		esportaXls = false;
		/*
		 * //cerca esiti
		 */
		BodEsitoBean infoEsitoBean = null;
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		String protocollo = (String)this.txtProtocollo.getValue();
		if (protocollo != null && !protocollo.trim().equalsIgnoreCase("")){
		//if (objSeg[0] != null && !objSeg[0].toString().trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("uiuProtocolloReg");
			fi.setOperatore("LIKE");
			fi.setParametro("uiuProtocolloReg");
			//fi.setValore( "%" + objSeg[0] + "%");
			fi.setValore( "%" + protocollo + "%");
			aryFilters.add(fi);
		}
		String foglio = (String)this.txtFoglio.getValue();
		if (foglio != null && !foglio.trim().equalsIgnoreCase("")){
		//if (objSeg[4] != null && !objSeg[4].toString().trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("uiuFoglio");
			fi.setOperatore("=");
			fi.setParametro("uiuFoglio");
			//fi.setValore( Character.fillUpZeroInFront( (String)objSeg[4], 4) );
			fi.setValore( Character.fillUpZeroInFront( foglio, 4) );
			aryFilters.add(fi);
		}
		String particella = (String)this.txtParticella.getValue();
		if (particella != null && !particella.trim().equalsIgnoreCase("")){
		//if (objSeg[5] != null && !objSeg[5].toString().trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("uiuNumero");
			fi.setOperatore("=");
			fi.setParametro("uiuNumero");
			//fi.setValore( Character.fillUpZeroInFront( objSeg[5].toString().trim().toUpperCase(), 5) );
			fi.setValore( Character.fillUpZeroInFront( particella.toString().trim().toUpperCase(), 5) );
			aryFilters.add(fi);
		}
		String subalterno = (String)this.txtSubalterno.getValue();
		if (subalterno != null && !subalterno.trim().equalsIgnoreCase("")){	
		//if (objSeg[6] != null && !objSeg[6].toString().trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("uiuSubalterno");
			fi.setOperatore("=");
			fi.setParametro("uiuSubalterno");
			//fi.setValore( Character.fillUpZeroInFront( objSeg[6].toString().trim().toUpperCase(), 4));
			fi.setValore( Character.fillUpZeroInFront( subalterno.toString().trim().toUpperCase(), 4));
			aryFilters.add(fi);
		}
		String annoMese = (String)this.txtAnnoMese.getValue();
		if (annoMese != null && !annoMese.trim().equalsIgnoreCase("")){
			//where += " AND nomeFileZip LIKE '%" + Character.checkNullString(annoMese) + "%' ";
			FilterItem fi = new FilterItem();
			fi.setAttributo("nomeFileZip");
			fi.setOperatore("LIKE");
			fi.setParametro("nomeFileZip");
			fi.setValore( "%" + Character.checkNullString(annoMese) + "%");
			aryFilters.add(fi);
		}
		if (txtIncConsistenza != null && !txtIncConsistenza.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("incConsistenza");
			fi.setOperatore("=");
			fi.setParametro("incConsistenza");
			fi.setValore( new Boolean(txtIncConsistenza) );
			aryFilters.add(fi);
		}
		if (txtIncClassamento != null && !txtIncClassamento.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("incClassamento");
			fi.setOperatore("=");
			fi.setParametro("incClassamento");
			fi.setValore( new Boolean(txtIncClassamento) );
			aryFilters.add(fi);
		}
		if (txtIncDestinazione != null && !txtIncDestinazione.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("incDestinazione");
			fi.setOperatore("=");
			fi.setParametro("incDestinazione");
			fi.setValore( new Boolean(txtIncDestinazione) );
			aryFilters.add(fi);
		}
		if (txtIncPlanimetria != null && !txtIncPlanimetria.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("incPlanimetria");
			fi.setOperatore("=");
			fi.setParametro("incPlanimetria");
			fi.setValore( new Boolean(txtIncPlanimetria) );
			aryFilters.add(fi);
		}
		if (cbxEsitoCollaudo != null && !cbxEsitoCollaudo.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setAttributo("esito");
			fi.setOperatore("=");
			fi.setParametro("esito");
			fi.setValore( cbxEsitoCollaudo );
			aryFilters.add(fi);
		}
		Hashtable htQry = new Hashtable();
		if (aryFilters != null && aryFilters.size()>0)
			htQry.put("where", aryFilters);
		htQry.put("orderBy", "uiuFoglio, uiuNumero, uiuSubalterno");	
		if (aryFilters != null && aryFilters.size()>0){
			listaEsiti = bodLogicService.getList(htQry, BodEsitoBean.class);
			if (listaEsiti != null && listaEsiti.size()>0){
				infoEsitoBean = listaEsiti.get(0);
			}
		}else
			listaEsiti = new LinkedList<BodEsitoBean>();
		
		if (listaEsiti != null && listaEsiti.size()>0){
			for (int j=0; j<listaEsiti.size();j++){
				BodConfrontoBean confronto = new BodConfrontoBean();
				
				BodEsitoBean bodEsito = listaEsiti.get(j);
				confronto.setEsito(bodEsito);
				/*
				 * recupero la segnalazione relativa ad ogni collaudo uppato ... se esistono
				 */
				/*
				 * qrySegnalazioniLista
				 * select * from bod_uiu bu left join bod_segnalazioni bs on bu.seg_fk = bs.id_seg where 1 = 1
				 */
				Properties prop = SqlHandler.loadProperties(propName);
				
				String sql = prop.getProperty("qrySegnalazioniLista");
				String where = "";
				String uiuProtocollo = "";
				if (bodEsito.getUiuProtocolloReg() != null && !bodEsito.getUiuProtocolloReg().trim().equalsIgnoreCase("")){
					int lastUnderscore = bodEsito.getUiuProtocolloReg().lastIndexOf('_');
					uiuProtocollo = bodEsito.getUiuProtocolloReg().substring(0, lastUnderscore);
					where += " AND protocollo LIKE '%" + uiuProtocollo + "%'";
				}
				String uiuFoglio = "";
				if (bodEsito.getUiuFoglio() != null && !bodEsito.getUiuFoglio().trim().equalsIgnoreCase("")){
					uiuFoglio = bodEsito.getUiuFoglio();
					where += " AND foglio = '" + Character.fillUpZeroInFront(uiuFoglio , 4) + "'";
				}
				String uiuParticella = "";
				if (bodEsito.getUiuNumero() != null && !bodEsito.getUiuNumero().trim().equalsIgnoreCase("")){
					uiuParticella = bodEsito.getUiuNumero();
					where += " AND particella = '" + Character.fillUpZeroInFront( uiuParticella, 5) + "'";
				}
				String uiuSubalterno = "";
				if (bodEsito.getUiuSubalterno() != null && !bodEsito.getUiuSubalterno().trim().equalsIgnoreCase("")){
					uiuSubalterno = bodEsito.getUiuSubalterno();
					where += " AND subalterno = '" + Character.fillUpZeroInFront( uiuSubalterno, 4) + "'";
				}
				htQry = new Hashtable();
				htQry.put("queryString", sql + where);
				htQry.put("orderBy", "foglio, particella, subalterno");
				listaSegnalazioni = bodLogicService.getList(htQry);
				if (listaSegnalazioni != null && listaSegnalazioni.size()==1){
					Object[] objSeg = (Object[])listaSegnalazioni.get(0);
					/*
					 * 0=bs.protocollo, 1=bs.esito_controllo, 2=bs.sopralluogo, 3=bu.sezione, 4=bu.foglio, 5=bu.particella, 
					 * 6=bu.subalterno, 7=bu.inc_consistenza, 8=bu.inc_destinazione, 9=bu.inc_planimetria, 10=bu.inc_classamento
					 * 11=nome_file 
					 */
					confronto.setSegnalazioniObjs(objSeg);
				}else{
					confronto.setSegnalazioniObjs(new Object[]{"","",null,"","","","",null,null,null,null,""});
					logger.info("XXX: SEGNALAZIONE RELATIVA AD ESITO NON ESISTENTE O NON SINGOLA");
				}
				/*
				 * qryRicercaDettaglioDatiCensuari
				 * cerca dati censuari DOCFA
				 */
				where = "";
				sql = prop.getProperty("qryRicercaDettaglioDatiCensuari");
				where = "";
				if (uiuProtocollo != null && !uiuProtocollo.toString().trim().equalsIgnoreCase("")){
					where += " AND cen.protocollo_registrazione LIKE '%" + uiuProtocollo.toString() + "%'";
				}
				if (uiuFoglio != null && !uiuFoglio.toString().trim().equalsIgnoreCase("")){
					where += " AND cen.foglio = '" + Character.fillUpZeroInFront( uiuFoglio.toString(), 4) + "'";
				}
				if (uiuParticella != null && !uiuParticella.toString().trim().equalsIgnoreCase("")){
					where += " AND cen.numero = '" + Character.fillUpZeroInFront( uiuParticella.toString(), 5) + "'";
				}
				if (uiuSubalterno != null && !uiuSubalterno.toString().trim().equalsIgnoreCase("")){
					where += " AND cen.subalterno = '" + Character.fillUpZeroInFront( uiuSubalterno.toString(), 4) + "'";
				}
				sql += where + " and to_char(to_date(cen.data_registrazione, 'yyyymmdd'),'yyyymm') = to_char(cen.fornitura,'yyyymm') order by fog, num, sub ";
				htQry = new Hashtable();
				htQry.put("queryString", sql);
				listaDatiCensuari = bodLogicService.getList(htQry);
				if (listaDatiCensuari != null && listaDatiCensuari.size()==1){
					confronto.setDatiCensuariObjs((Object[])listaDatiCensuari.get(0));
				/*
				 *  0=cen.foglio, 1=cen.numero, 2=cen.subalterno, 3=cen.zona, 4=cen.classe,
				 *  5=cen.categoria, 6=cen.consistenza, 7=cen.superficie, 8=cen.rendita_euro,
				 *  9=cen.data_registrazione, 10=cen.identificativo_immobile, 11=cen.presenza_graffati
				 */					
				}else{
					confronto.setDatiCensuariObjs(new String[]{"","","","","","","","","","","",""});
					logger.info("XXX: DATO CENSUARIO DOCFA RELATIVO AD ESITO CORRENTE NON ESISTENTE O NON SINGOLO");
				}
				/*
				 * qryCatastoLista
				 * cerca situazione a catasto
				 * 0=foglio, 1=particella, 2=unimm, 3=categoria, 4=consistenza, 5=sup_cat, 6=zona, 7=classe, 8=rendita 
				 */
				sql = prop.getProperty("qryCatastoLista");
				where = "";
				/*
				 * il protocollo nella SITIUIU non esiste
				 */
				if (uiuFoglio != null && !uiuFoglio.toString().trim().equalsIgnoreCase("")){
					where += " AND foglio = '" + uiuFoglio.toString() + "'";
				}
				if (uiuParticella != null && !uiuParticella.toString().trim().equalsIgnoreCase("")){
					where += " AND particella = '" + Character.fillUpZeroInFront( uiuParticella.toString(), 4) + "'";
				}
				if (uiuSubalterno != null && !uiuSubalterno.toString().trim().equalsIgnoreCase("")){
					where += " AND unimm = '" + uiuSubalterno.toString() + "'";
				}
				htQry = new Hashtable();
				htQry.put("queryString", sql + where);
				htQry.put("orderBy", "foglio, particella, unimm");
				Object[] infoCatastoObjs = null;
				listaUiuCatasto = bodLogicService.getList(htQry);
				if (listaUiuCatasto != null && listaUiuCatasto.size()==1){
					infoCatastoObjs = (Object[])listaUiuCatasto.get(0);
					confronto.setCatastoObjs(infoCatastoObjs);
				}else{
					confronto.setCatastoObjs(new Object[]{null,"",null,"",null,null,"","",null});
					logger.info("XXX: DATO A CATASTO RELATIVO AD ESITO CORRENTE NON ESISTENTE O NON SINGOLO");
				}
				/*
				 * Aggiunta in lista pubblica per visualizzare e 
				 * Confronto per catasto vero o falso 
				 */
				boolean infoRecepite = false;
				if (infoCatastoObjs != null && infoEsitoBean != null){
					if (infoEsitoBean.getDatClasse() != null && infoCatastoObjs[7] != null && infoEsitoBean.getDatClasse().trim().equalsIgnoreCase( (String)infoCatastoObjs[7] ) &&
						infoEsitoBean.getDatCategoria() != null && infoCatastoObjs[3] != null && infoEsitoBean.getDatCategoria().trim().equalsIgnoreCase( (String)infoCatastoObjs[3] ) && 	
						infoEsitoBean.getDatConsistenza() != null && infoCatastoObjs[4] != null && new BigDecimal (infoEsitoBean.getDatConsistenza()).equals( (BigDecimal)infoCatastoObjs[4] ) &&		
						infoEsitoBean.getDatRenditaInEuro() != null && infoCatastoObjs[8] != null && infoEsitoBean.getDatRenditaInEuro().equals( (BigDecimal)infoCatastoObjs[8] ) && 
						infoEsitoBean.getDatSuperficie() != null && infoCatastoObjs[5] != null && new BigDecimal (infoEsitoBean.getDatSuperficie()).equals( (BigDecimal)infoCatastoObjs[5] ) ){

						infoRecepite = true;
					}
				}

				confronto.setConfronto(new Boolean(infoRecepite) );

				listaConfronti.add(confronto);
			}
		}else{
			logger.info("xxx: ESITI/COLLAUDI ASSENTI");
		}
		
		init();
		/*
		 * Questa if deve restare dopo init
		 */
		if (listaConfronti != null && listaConfronti.size()>0){
			generaXls = false;
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String infoUiuCatastoShow(){
		String esito = "esportaFiltroBck.infoUiuCatastoShow";

		if (confrontoCur != null)
			System.currentTimeMillis();
		else
			esito = "failure";
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String generaFornitura(){
		String esito = "esportaFiltroBck.generaFornitura";
		/*
		 * Recupero il codice ente
		 */
		String sql = "SELECT " +
		"uk_belfiore " +
		"FROM " +
		"ewg_tab_comuni  ";
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> lstEnte = bodLogicService.getList(htQry);
		codiceEnte = Character.checkNullString((String)lstEnte.get(0));
		/*
		 * Recupero il progressivo piu alto delle forniture
		 */
		sql = "SELECT " +
		"MAX(PROGRESSIVO) as MAX_PROGRESSIVO " +
		"FROM " +
		"BOD_FORNITURE ";
		htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> lstMaxPro = bodLogicService.getList(htQry);
		if (lstMaxPro != null && lstMaxPro.get(0) != null) {
			maxPro = lstMaxPro.get(0).toString();
		} else {
			maxPro = null;
		}
		//maxPro = Character.checkNullString((String)lstMaxPro.get(0));
		if (maxPro != null && !maxPro.trim().equalsIgnoreCase("")){
			maxPro = "" + (Integer.parseInt(maxPro) + 1);
		}else{
			maxPro = "1";
		}

		return esito;
	}//-------------------------------------------------------------------------
	
	public void showIstruttorieInFornitura(){
		elencoIstruttorie = new ArrayList<BodIstruttoriaBean>();
		/*
		 * -Recupero la fornitura tramite l'id
		 * -Itero le forniture docfa per recuperare le istruttorie presenti
		 * -Inserisco nell'elenco da visualizzare i risultati
		 */
		BodFornituraBean bFornitura = (BodFornituraBean)bodLogicService.getItemById(currentFornitura, BodFornituraBean.class);
		if (bFornitura != null){
			Set<BodFornituraDocfaBean> setForDocfa = bFornitura.getFornitureDocfa();
			Iterator<BodFornituraDocfaBean> itForDocfa = setForDocfa.iterator();
			Hashtable<Long, BodFornituraDocfaBean> htForDocfa = new Hashtable<Long, BodFornituraDocfaBean>();
			while(itForDocfa.hasNext()){
				BodFornituraDocfaBean bForDocfa = itForDocfa.next();
				htForDocfa.put(bForDocfa.getIstFk(), bForDocfa);
			}
			Enumeration<Long> eIst = htForDocfa.keys();
			while(eIst.hasMoreElements()){
				Long seqIst = eIst.nextElement();
				elencoIstruttorie.add( (BodIstruttoriaBean)bodLogicService.getItemById(seqIst, BodIstruttoriaBean.class) );
			}
		}
	}//-------------------------------------------------------------------------
	
	public String confrontoShow(){
		String esito = "esportaFiltroBck.confrontoShow";
		init();
		clean();
		/* 
		 * Azzero anche una eventuale selezione effettuata in momenti precedenti
		 */
		listaConfronti = new LinkedList<BodConfrontoBean>();
		return esito;
	}//-------------------------------------------------------------------------	
	
	public void validateGenera(){
//		if (chkGenera != null && chkGenera){
//			logger.info("Da Falso a Vero Genera");
//		}else{
//			logger.info("Da Vero a Falso Genera");
//		}
	}//-------------------------------------------------------------------------
	
	public void validateStatus(){
		/*
		 * -Recupero la forniture selezionata
		 * -Download della fornitura
		 * -Aggiorno lo status a SCARICATA
		 */
		logger.info("Aggiornamento stato fornitura...");
		BodFornituraBean bFornitura = (BodFornituraBean)bodLogicService.getItemById(currentFornitura, BodFornituraBean.class);
		bFornitura.setStatus( Info.htFornitureStatus.get("1") );
		bodLogicService.updItem(bFornitura, BodFornituraBean.class);
		this.ricerca();
		logger.info("Stato fornitura aggiornato.");
	
	}//-------------------------------------------------------------------------
	
	public String nuovaFornitura(){
		String esito = "esportaFiltroBck.nuovaFornitura";
		
		String pathFornitura = this.getPathFornitura();
		String pathXml = this.getPathXml();
		
		//logger.info("Genera: " + chkGenera);
		if (listaIstruttorie != null && listaIstruttorie.size()>0){
			//BodIstruttoriaBean istruttoria = null;
			/*
			 * Genero il file XML della fornitura
			 */
			Hashtable htXml = new Hashtable();
			htXml.put("codiceEnte", codiceEnte);
			htXml.put("listaIstruttorie", listaIstruttorie);
			htXml.put("progressivo", maxPro);
			
			Hashtable htFornitura = Schiavo.createXMLfornitura(pathXml, htXml);
			String xmlFile = (String)htFornitura.get("xmlFile");
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
						if (bForDocfa != null && bForDocfa.getPathFileName() != null && !bForDocfa.getPathFileName().equalsIgnoreCase("")) {
							if (bForDocfa.getPathFileName().toLowerCase().endsWith(".xml")) {
								if (!trovatoXML) {
									trovatoXML = true;
									listaFilesForniture.add(bForDocfa.getPathFileName());
								}
							} else {
								listaFilesForniture.add(bForDocfa.getPathFileName());
							}	
						}													
					}
					
					String nomeFornitura = new File(xmlFile).getName();
					int end = nomeFornitura.indexOf('.');
					nomeFornitura = nomeFornitura.substring(0, end);
					logger.info("nuovaFornitura - NOME FORNITURA: " + nomeFornitura);
					
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
					if (istruttoriaBean != null){
						success = true;
						error = false;
					}else{
						success = false;
						error = true;
					}
				}
			}else{
				/*
				 * Impossibile generare il file XML
				 */
			}
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public boolean readXMLfile() throws Exception{
		boolean read = false;

		listaEsiti = new LinkedList<BodEsitoBean>();
		SAXBuilder builder = new SAXBuilder();
	    Document document = builder.build(new File(esitoCur.getNomeFileEsito()));
	    org.jdom.Element root = document.getRootElement();
	    List<?> children = root.getChildren();
	    Iterator<?> iterator = children.iterator();
	    boolean fornituraOK = false;
	    while(iterator.hasNext()){
	      Element item = (Element)iterator.next();
	      logger.info( item.getName() + ": " + item.getValue());
	      
	      if (item.getName().equalsIgnoreCase("COMUNE") && this.getEnteCorrente().toUpperCase().equalsIgnoreCase(item.getValue())){
	    	  fornituraOK = true;
	      }else{
	    	  logger.info("Forniture di altro ENTE");
	      }

	      if (fornituraOK){
	    	  Namespace ns =  Namespace.getNamespace("http://www.agenziaterritorio.it/EsitiSegnalazioniL80.xsd"); 
	    	  if ( item.getName().equalsIgnoreCase("IncoerenzeEsaminate") ) {
	    		  Element riepilogoTag = item.getChild("Riepilogo", ns);
	    		  String riepilogo = "";
	    		  if (riepilogoTag != null){
	    			  List tipoTags = riepilogoTag.getChildren("Tipo", ns);
	    			  if (tipoTags != null && tipoTags.size()>0){
	    				  Iterator<Element> itTipo = tipoTags.iterator();
	    				  while(itTipo.hasNext()){
	    					  Element tipoTag = itTipo.next();
	    					  //Elemento; Esito; Contatore;
	    					  String elemento = tipoTag.getChild("Elemento", ns)!=null?tipoTag.getChild("Elemento", ns).getValue():"";
	    					  String esito = tipoTag.getChild("Esito", ns)!=null?tipoTag.getChild("Esito", ns).getValue():"";
	    					  String contatore = tipoTag.getChild("Contatore", ns)!=null?tipoTag.getChild("Contatore", ns).getValue():"";
	    					  riepilogo += "EL:"+elemento+";"+"ES:"+esito+";"+"CO:"+contatore+"_"; 
	    				  }
	    			  }
	    		  }
	    		  
	    		  List uiuTags = item.getChildren("UIU", ns);
	    		  if (uiuTags != null && uiuTags.size()>0){
	    			  Iterator<Element> itUiu = uiuTags.iterator();
	    			  while(itUiu.hasNext()){
	    				  
	    				  BodEsitoBean iEsito = new BodEsitoBean();
	    				  iEsito.setDatRiepilogo(riepilogo);
	    				  
	    				  Element uiu = itUiu.next();
	    				  String protocolloRegistrazione = uiu.getChild("ProtocolloRegistrazione", ns)!=null?uiu.getChild("ProtocolloRegistrazione", ns).getValue():"";
	    				  iEsito.setUiuProtocolloReg(protocolloRegistrazione);
	    				  
	    				  if (uiu.getChild("IdentificativiUIU", ns) != null){
		    				  String sezioneUrbana = uiu.getChild("IdentificativiUIU", ns).getChild("SezioneUrbana", ns)!=null?uiu.getChild("IdentificativiUIU", ns).getChild("SezioneUrbana", ns).getValue():"";
		    				  String foglio = uiu.getChild("IdentificativiUIU", ns).getChild("Foglio", ns)!=null?uiu.getChild("IdentificativiUIU", ns).getChild("Foglio", ns).getValue():"";
		    				  String numero = uiu.getChild("IdentificativiUIU", ns).getChild("Numero", ns)!=null?uiu.getChild("IdentificativiUIU", ns).getChild("Numero", ns).getValue():"";
		    				  String denominatore = uiu.getChild("IdentificativiUIU", ns).getChild("Denominatore", ns)!=null?uiu.getChild("IdentificativiUIU", ns).getChild("Denominatore", ns).getValue():"";
		    				  String subalterno = uiu.getChild("IdentificativiUIU", ns).getChild("Subalterno", ns)!=null?uiu.getChild("IdentificativiUIU", ns).getChild("Subalterno", ns).getValue():"";
		    				  iEsito.setUiuSezioneUrbana(sezioneUrbana);
		    				  iEsito.setUiuFoglio(foglio);
		    				  iEsito.setUiuNumero(numero);
		    				  iEsito.setUiuDenominatore(denominatore);
		    				  iEsito.setUiuSubalterno(subalterno);	    					  
	    				  }
	    				  
	    				  if (uiu.getChild("Incoerenze", ns)!=null){
		    				  String incConsistenza = uiu.getChild("Incoerenze", ns).getChild("Consistenza", ns)!=null?uiu.getChild("Incoerenze", ns).getChild("Consistenza", ns).getValue():"";
		    				  String destinazione = uiu.getChild("Incoerenze", ns).getChild("Destinazione", ns)!=null?uiu.getChild("Incoerenze", ns).getChild("Destinazione", ns).getValue():"";
		    				  String classamento = uiu.getChild("Incoerenze", ns).getChild("Classamento", ns)!=null?uiu.getChild("Incoerenze", ns).getChild("Classamento", ns).getValue():"";
		    				  String planimetria = uiu.getChild("Incoerenze", ns).getChild("Planimetria", ns)!=null?uiu.getChild("Incoerenze", ns).getChild("Planimetria", ns).getValue():"";
		    				  iEsito.setIncConsistenza(new Boolean(incConsistenza) );
		    				  iEsito.setIncDestinazione(new Boolean(destinazione) );
		    				  iEsito.setIncClassamento(new Boolean(classamento) );
		    				  iEsito.setIncPlanimetria(new Boolean(planimetria) );	    					  
	    				  }
	    				  
	    				  String esito = uiu.getChild("Esito", ns)!=null?uiu.getChild("Esito", ns).getValue():"";
	    				  iEsito.setEsito(esito);
	    				  
	    				  if(uiu.getChild("DatiClassamento", ns)!=null){
		    				  String zona = uiu.getChild("DatiClassamento", ns).getChild("Zona", ns)!=null?uiu.getChild("DatiClassamento", ns).getChild("Zona", ns).getValue():"";
		    				  String categoria = uiu.getChild("DatiClassamento", ns).getChild("Categoria", ns)!=null?uiu.getChild("DatiClassamento", ns).getChild("Categoria", ns).getValue():"";
		    				  String classe = uiu.getChild("DatiClassamento", ns).getChild("Classe", ns)!=null?uiu.getChild("DatiClassamento", ns).getChild("Classe", ns).getValue():"";
		    				  String datConsistenza = uiu.getChild("DatiClassamento", ns).getChild("Consistenza", ns)!=null?uiu.getChild("DatiClassamento", ns).getChild("Consistenza", ns).getValue():"";
		    				  String flagClassamento = uiu.getChild("DatiClassamento", ns).getChild("FlagClassamento", ns)!=null?uiu.getChild("DatiClassamento", ns).getChild("FlagClassamento", ns).getValue():"";
		    				  String superficie = uiu.getChild("DatiClassamento", ns).getChild("Superficie", ns)!=null?uiu.getChild("DatiClassamento", ns).getChild("Superficie", ns).getValue():"";
		    				  String renditaInEuro = uiu.getChild("DatiClassamento", ns).getChild("RenditaInEuro", ns)!=null?uiu.getChild("DatiClassamento", ns).getChild("RenditaInEuro", ns).getValue():"";
		    				  iEsito.setDatZona(zona);
		    				  iEsito.setDatCategoria(categoria);
		    				  iEsito.setDatClasse(classe);
		    				  iEsito.setDatConsistenza(datConsistenza);
		    				  iEsito.setDatFlagClassamento(flagClassamento);
		    				  iEsito.setDatSuperficie(superficie);
		    				  iEsito.setDatRenditaInEuro( new BigDecimal(renditaInEuro) );	    					  
	    				  }
	    				  listaEsiti.add(iEsito);
	    			  }
	    		  }
	    	  }
	      }
	    }
	    
		return read;
	}//-------------------------------------------------------------------------
	
	public String esporta() throws Exception{
		String esito = "esportaFiltroBck.esporta";
		if (listaConfronti != null && listaConfronti.size()>0){
			String pathXlsTemplate = this.getRootPathEnte() + dirTemplateBod + "template_xlsDocfa/";
			String pathXlsTmp = this.getRootPathEnte() + this.TEMPORARY_FILES + "/";
			String msg = esportaDati(pathXlsTemplate, pathXlsTmp);
			if (msg != null && msg.trim().equalsIgnoreCase("true"))
				this.esportaXls = true;
			else
				this.esportaXls = false;			
		}else{
			logger.info("LISTA CONFRONTI VUOTA ");
		}

		return esito;
	}//-------------------------------------------------------------------------
	
	public String esportaDati(String pathModelloXls, String pathOutXls) throws Exception	{
		String msg = "true";
		try {
			String xlsName = System.currentTimeMillis() + "_modello_confronto_esiti.xls";
			String modelloXls = pathModelloXls + "/modello_confronto_esiti.xls";

			//controllo che i dati entrino nel foglio (<=65.000)
			Integer controllo = listaConfronti.size();
				
			if (controllo > 65500){
				msg = "ATTENZIONE - La selezione ha prodotto un risultato troppo grande per la rappresentazione su file Excel!";
			}else{
				//preparo il modello xls sul foglio 0 (report)
				POIFSFileSystem fs  =  new POIFSFileSystem(new FileInputStream( modelloXls ));
				HSSFWorkbook wb = new HSSFWorkbook(fs);

				wb = scriviSheet(wb); 
				
				// Write the output to a file
				File outDir = new File(pathOutXls);
				if(!outDir.exists())
					outDir.mkdirs();

				xlsPath = pathOutXls + "/" + xlsName;
				logger.info("PATH CONFRONTO ESITI: " + xlsPath);

				FileOutputStream fileOut = new FileOutputStream(xlsPath);
				wb.write(fileOut);
				fileOut.close();
			}
		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return msg;
	}//-------------------------------------------------------------------------
	
	private HSSFWorkbook scriviSheet(HSSFWorkbook wb) throws Exception{
					
		HSSFSheet sheet = wb.getSheet("COMPARAZIONE");
		/*
		 * Iniziamo dalla riga 3 perché l'indice parte da zero e le prime tre righe 
		 * sono occupate dall'intestazione
		 */
		int riga = 2;
		
		HSSFCellStyle cellStyleGreen = wb.createCellStyle();
		//cellStyleGreen.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyleGreen.setFillForegroundColor(HSSFColor.GREEN.index);
		cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		HSSFCellStyle cellStyleRed = wb.createCellStyle();
		cellStyleRed.setFillForegroundColor(HSSFColor.RED.index);
		cellStyleRed.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		Iterator<BodConfrontoBean>  itObjs = listaConfronti.iterator();
		while (itObjs.hasNext()){
			BodConfrontoBean confronto = itObjs.next();
			riga++;
			int numCell = 0;
			
			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
				
			HSSFCell cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			
			Object[] segnalazioniObjs = confronto.getSegnalazioniObjs();
			
			if (segnalazioniObjs != null){
				if ( segnalazioniObjs[0] != null)	//PROTOCOLLO
					cell.setCellValue( Character.checkNullString((String)segnalazioniObjs[0]) );	//PROTOCOLLO
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( segnalazioniObjs[7] != null)	//SEGNALAZIONI - INCOERENZE - CONS.
					cell.setCellValue( ( (BigDecimal)segnalazioniObjs[7] ).doubleValue() );	//SEGNALAZIONI - INCOERENZE - CONS.
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( segnalazioniObjs[8] != null )	//SEGNALAZIONI - INCOERENZE - DEST.
					cell.setCellValue( ( (BigDecimal)segnalazioniObjs[8] ).doubleValue() );	//SEGNALAZIONI - INCOERENZE - DEST.
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( segnalazioniObjs[10] != null)	//SEGNALAZIONI - INCOERENZE - CLAS.
					cell.setCellValue( ( (BigDecimal)segnalazioniObjs[10] ).doubleValue() );   //SEGNALAZIONI - INCOERENZE - CLAS.
	
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( segnalazioniObjs[9] != null)	//SEGNALAZIONI - INCOERENZE - PLAN.
					cell.setCellValue( ( (BigDecimal)segnalazioniObjs[9] ).doubleValue() );	//SEGNALAZIONI - INCOERENZE - PLAN.
		
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
	//			if ( objs[5] != null)	//OCCORRENZE
	//				cell.setCellValue( ( (BigDecimal)objs[5] ).doubleValue() );	//OCCORRENZE
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)segnalazioniObjs[1]) ));	//SEGNALAZIONI - ESITO
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( segnalazioniObjs[2] != null)	//SEGNALAZIONI - SOPR.
					cell.setCellValue( ( (BigDecimal)segnalazioniObjs[2] ).doubleValue() );	//SEGNALAZIONI - SOPR.
				
//				numCell++;
//				cell = row.getCell(numCell);
//				if (cell == null)
//					cell = row.createCell((short)numCell);
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)segnalazioniObjs[3]) ));	//SEGNALAZIONI - SEZ.
//				
//				numCell++;
//				cell = row.getCell(numCell);
//				if (cell == null)
//					cell = row.createCell((short)numCell);
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)segnalazioniObjs[4]) ));	//SEGNALAZIONI - FOG.
//				
//				numCell++;
//				cell = row.getCell(numCell);
//				if (cell == null)
//					cell = row.createCell((short)numCell);
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)segnalazioniObjs[5]) ));	//SEGNALAZIONI - PAR.
//				
//				numCell++;
//				cell = row.getCell(numCell);
//				if (cell == null)
//					cell = row.createCell((short)numCell);
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)segnalazioniObjs[6]) ));	//SEGNALAZIONI - SUB.
			}else{
				numCell=numCell+6;
			}
			
			Object[] datiCensuariObjs = confronto.getDatiCensuariObjs();
			if (datiCensuariObjs != null){
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)datiCensuariObjs[3]) ));	//DOCFA - ZONA
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)datiCensuariObjs[5]) ));	//DOCFA - CAT.
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)datiCensuariObjs[4]) ));	//DOCFA - CLA.
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)datiCensuariObjs[6]) ));	//DOCFA - CON.
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)datiCensuariObjs[7]) ));	//DOCFA - SUP.
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString((String)datiCensuariObjs[8]) ));	//DOCFA - REN.
			}else{
				numCell = numCell+6;
			}
			
			BodEsitoBean esito = confronto.getEsito();
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getUiuProtocolloReg() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getUiuProtocolloReg()) ));	//ESITI - PROTOCOLLE REGSTRAZIONE
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getUiuSezioneUrbana() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getUiuSezioneUrbana()) ));	//ESITI - SEZIONE
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getUiuFoglio() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getUiuFoglio()) ));	//ESITI - FOGLIO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getUiuNumero() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getUiuNumero()) ));	//ESITI - PARTICELLA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getUiuSubalterno() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getUiuSubalterno()) ));	//ESITI - SUBALTERNO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getIncConsistenza() != null)
				cell.setCellValue(new HSSFRichTextString( esito.getIncConsistenza().toString() ));	//ESITI - INCOERENZE - CONS.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getIncDestinazione() != null)
				cell.setCellValue(new HSSFRichTextString( esito.getIncDestinazione().toString() ));	//ESITI - INCOERENZE - DEST.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getIncClassamento() != null)
				cell.setCellValue(new HSSFRichTextString( esito.getIncClassamento().toString() ));	//ESITI - INCOERENZE - CLAS.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getIncPlanimetria() != null)
				cell.setCellValue(new HSSFRichTextString( esito.getIncPlanimetria().toString() ));	//ESITI - INCOERENZE - PLAN.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getEsito() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getEsito()) ));	//ESITI - ESITO
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getDatZona() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getDatZona()) ));	//ESITI - DATI CLASSAMENTO - ZONA
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getDatCategoria() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getDatCategoria()) ));	//ESITI - DATI CLASSAMENTO - CAT.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getDatClasse() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getDatClasse()) ));	//ESITI - DATI CLASSAMENTO - CLA.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getDatFlagClassamento() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getDatFlagClassamento()) ));	//ESITI - DATI CLASSAMENTO - FLAG CLA.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getDatConsistenza() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getDatConsistenza()) ));	//ESITI - DATI CLASSAMENTO - CON.
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if ( esito != null && esito.getDatSuperficie() != null)
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString(esito.getDatSuperficie()) ));	//ESITI - DATI CLASSAMENTO - SUP.
			
			/*
			 * XXX dobbiamo confrontare la rendita tra il valore in catsto e il valore 
			 * in collaudo per evidenziarla nel foglio XLS nel caso di diversità 
			 */

			Object[] catastoObjs = confronto.getCatastoObjs();
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (catastoObjs != null && catastoObjs[8] != null && new BigDecimal(0d).compareTo((BigDecimal)catastoObjs[8]) == -1 
					&& esito.getDatRenditaInEuro() != null && esito.getDatRenditaInEuro().compareTo(new BigDecimal(0d)) == 1 
					&& !esito.getDatRenditaInEuro().equals((BigDecimal)catastoObjs[8]) ){
				cell.setCellStyle(cellStyleRed);				
			}else{
				cell.setCellStyle(cellStyleGreen);
			}
			if ( esito != null && esito.getDatRenditaInEuro() != null)	//ESITI - DATI CLASSAMENTO - REN.
				cell.setCellValue( ( esito.getDatRenditaInEuro() ).doubleValue() );	//ESITI - DATI CLASSAMENTO - REN.
			
			
			if (catastoObjs != null){
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if (confronto.getConfronto()){
					cell.setCellStyle(cellStyleGreen);				
				}else{
					cell.setCellStyle(cellStyleRed);
				}
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( confronto.getConfronto().toString() ));	//CATASTO - CONFRONTO
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString( (String)catastoObjs[6] )));	//CATASTO - ZONA
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString( (String)catastoObjs[3] )));	//CATASTO - CAT.
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString( Character.checkNullString( (String)catastoObjs[7] )));	//CATASTO - CLA
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( catastoObjs[4] != null)	//CATASTO - CON
					cell.setCellValue(  ((BigDecimal)catastoObjs[4]).doubleValue() );	//CATASTO - CON
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( catastoObjs[5] != null)	//CATASTO - SUP
					cell.setCellValue( ( (BigDecimal)catastoObjs[5] ).doubleValue() );	//CATASTO - SUP
				
				numCell++;
				cell = row.getCell(numCell);
				if (cell == null)
					cell = row.createCell((short)numCell);
				if ( catastoObjs[8] != null)	//CATASTO - REN
					cell.setCellValue( ( (BigDecimal)catastoObjs[8] ).doubleValue() );	//CATASTO - REN
			}else{
				numCell = numCell+7;
			}
			
		}
			
		return wb;
	}//-------------------------------------------------------------------------

	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<BodFornituraBean> getListaForniture() {
		return listaForniture;
	}

	public void setListaForniture(List<BodFornituraBean> lstForniture) {
		this.listaForniture = lstForniture;
	}

	public HtmlInputText getTxtProgressivo() {
		return txtProgressivo;
	}

	public void setTxtProgressivo(HtmlInputText txtProgressivo) {
		this.txtProgressivo = txtProgressivo;
	}

	public HtmlInputText getTxtNomeFile() {
		return txtNomeFile;
	}

	public void setTxtNomeFile(HtmlInputText txtNomeFile) {
		this.txtNomeFile = txtNomeFile;
	}

	public ComboBoxRch getBoxStatus() {
		return boxStatus;
	}

	public void setBoxStatus(ComboBoxRch boxStatus) {
		this.boxStatus = boxStatus;
	}

	public List<BodIstruttoriaBean> getListaIstruttorie() {
		return listaIstruttorie;
	}

	public void setListaIstruttorie(List<BodIstruttoriaBean> listaIstruttorie) {
		this.listaIstruttorie = listaIstruttorie;
	}

	public Integer getNumIstruttorieDaZippare() {
		return numIstruttorieDaZippare;
	}

	public void setNumIstruttorieDaZippare(Integer numIstruttorieDaZippare) {
		this.numIstruttorieDaZippare = numIstruttorieDaZippare;
	}

	public String getCodiceEnte() {
		return codiceEnte;
	}

	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}


	public String getMaxPro() {
		return maxPro;
	}

	public void setMaxPro(String maxPro) {
		this.maxPro = maxPro;
	}


	public String getPathXml() {
		pathXml = this.getRootPathEnte() + "xmlBod/";
		File dir = new File(pathXml);
		if(!dir.exists())
			dir.mkdirs();
		logger.info("PATH xmlBod: " + pathXml);
		return pathXml;
	}

	public void setPathXml(String pathXml) {
		this.pathXml = pathXml;
	}

	public String getPathFornitura() {
		pathFornitura = this.getRootPathEnte() + "fornitureBod/";
		File dir = new File(pathFornitura);
		if(!dir.exists())
			dir.mkdirs();
		logger.info("PATH fornitureBod: " + pathFornitura);
		return pathFornitura;
	}

	public void setPathFornitura(String pathFornitura) {
		this.pathFornitura = pathFornitura;
	}

//	public Boolean getChkGenera() {
//		return chkGenera;
//	}
//
//	public void setChkGenera(Boolean chkGenera) {
//		this.chkGenera = chkGenera;
//	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public Long getCurrentFornitura() {
		return currentFornitura;
	}

	public void setCurrentFornitura(Long currentFornitura) {
		this.currentFornitura = currentFornitura;
	}

	public List<BodIstruttoriaBean> getElencoIstruttorie() {
		return elencoIstruttorie;
	}

	public void setElencoIstruttorie(List<BodIstruttoriaBean> elencoIstruttorie) {
		this.elencoIstruttorie = elencoIstruttorie;
	}

	public BodUploadBean getBodUpload() {
		return bodUpload;
	}

	public void setBodUpload(BodUploadBean bodUpload) {
		this.bodUpload = bodUpload;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BodUploadSwitch getBodUploadSwitch() {
		return bodUploadSwitch;
	}

	public void setBodUploadSwitch(BodUploadSwitch bodUploadSwitch) {
		this.bodUploadSwitch = bodUploadSwitch;
	}

	public BodEsitoBean getEsitoCur() {
		return esitoCur;
	}

	public void setEsitoCur(BodEsitoBean esitoCur) {
		this.esitoCur = esitoCur;
	}

	public Boolean getIsAutorizzatoEsporta() {
		return isAutorizzatoEsporta;
	}

	public void setIsAutorizzatoEsporta(Boolean isAutorizzatoEsporta) {
		this.isAutorizzatoEsporta = isAutorizzatoEsporta;
	}

	public List<BodEsitoBean> getListaEsiti() {
		return listaEsiti;
	}

	public void setListaEsiti(List<BodEsitoBean> listaEsiti) {
		this.listaEsiti = listaEsiti;
	}

	public HtmlInputText getTxtFoglio() {
		return txtFoglio;
	}

	public void setTxtFoglio(HtmlInputText txtFoglio) {
		this.txtFoglio = txtFoglio;
	}

	public HtmlInputText getTxtParticella() {
		return txtParticella;
	}

	public void setTxtParticella(HtmlInputText txtParticella) {
		this.txtParticella = txtParticella;
	}

	public HtmlInputText getTxtSubalterno() {
		return txtSubalterno;
	}

	public void setTxtSubalterno(HtmlInputText txtSubalterno) {
		this.txtSubalterno = txtSubalterno;
	}

	public HtmlInputText getTxtProtocollo() {
		return txtProtocollo;
	}

	public void setTxtProtocollo(HtmlInputText txtProtocollo) {
		this.txtProtocollo = txtProtocollo;
	}

	public List<Object> getListaSegnalazioni() {
		return listaSegnalazioni;
	}

	public void setListaSegnalazioni(List<Object> listaSegnalazioni) {
		this.listaSegnalazioni = listaSegnalazioni;
	}

	public List<Object> getListaUiuCatasto() {
		return listaUiuCatasto;
	}

	public void setListaUiuCatasto(List<Object> listaUiuCatasto) {
		this.listaUiuCatasto = listaUiuCatasto;
	}

	public List<Object> getListaDatiCensuari() {
		return listaDatiCensuari;
	}

	public void setListaDatiCensuari(List<Object> listaDatiCensuari) {
		this.listaDatiCensuari = listaDatiCensuari;
	}

	public List<BodConfrontoBean> getListaConfronti() {
		return listaConfronti;
	}

	public void setListaConfronti(List<BodConfrontoBean> listaConfronti) {
		this.listaConfronti = listaConfronti;
	}

	public BodConfrontoBean getConfrontoCur() {
		return confrontoCur;
	}

	public void setConfrontoCur(BodConfrontoBean confrontoCur) {
		this.confrontoCur = confrontoCur;
	}

	public HtmlInputText getTxtAnnoMese() {
		return txtAnnoMese;
	}

	public void setTxtAnnoMese(HtmlInputText txtAnnoMese) {
		this.txtAnnoMese = txtAnnoMese;
	}

	public String getTxtIncConsistenza() {
		return txtIncConsistenza;
	}

	public void setTxtIncConsistenza(String txtIncConsistenza) {
		this.txtIncConsistenza = txtIncConsistenza;
	}

	public String getTxtIncClassamento() {
		return txtIncClassamento;
	}

	public void setTxtIncClassamento(String txtIncClassamento) {
		this.txtIncClassamento = txtIncClassamento;
	}

	public String getTxtIncDestinazione() {
		return txtIncDestinazione;
	}

	public void setTxtIncDestinazione(String txtIncDestinazione) {
		this.txtIncDestinazione = txtIncDestinazione;
	}

	public String getTxtIncPlanimetria() {
		return txtIncPlanimetria;
	}

	public void setTxtIncPlanimetria(String txtIncPlanimetria) {
		this.txtIncPlanimetria = txtIncPlanimetria;
	}

	public ArrayList<SelectItem> getAylIncOpz() {
		return aylIncOpz;
	}

	public void setAylIncOpz(ArrayList<SelectItem> aylIncOpz) {
		this.aylIncOpz = aylIncOpz;
	}

	public String getXlsPath() {
		return xlsPath;
	}

	public void setXlsPath(String xlsPath) {
		this.xlsPath = xlsPath;
	}

	public Boolean getEsportaXls() {
		return esportaXls;
	}

	public void setEsportaXls(Boolean esportaXls) {
		this.esportaXls = esportaXls;
	}

	public Boolean getGeneraXls() {
		return generaXls;
	}

	public void setGeneraXls(Boolean generaXls) {
		this.generaXls = generaXls;
	}

	public ArrayList<SelectItem> getAylEsitiCollaudoOpz() {
		return aylEsitiCollaudoOpz;
	}

	public void setAylEsitiCollaudoOpz(ArrayList<SelectItem> aylEsitiCollaudoOpz) {
		this.aylEsitiCollaudoOpz = aylEsitiCollaudoOpz;
	}

	public String getCbxEsitoCollaudo() {
		return cbxEsitoCollaudo;
	}

	public void setCbxEsitoCollaudo(String cbxEsitoCollaudo) {
		this.cbxEsitoCollaudo = cbxEsitoCollaudo;
	}

	
	
}
