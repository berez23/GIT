package it.webred.ct.service.tares.web.bean.paging;

import java.io.FileOutputStream;



import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.List;

import javax.faces.context.FacesContext;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import it.doro.util.Character;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.tares.data.access.SegnalazioniService;
import it.webred.ct.service.tares.data.access.ValutazioniService;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.access.dto.SegnalazioniSearchCriteria;
import it.webred.ct.service.tares.data.access.dto.ValutazioniSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarElab;
import it.webred.ct.service.tares.data.model.SetarElabPK;
import it.webred.ct.service.tares.data.model.SetarSegnala1;
import it.webred.ct.service.tares.data.model.SetarSegnala2;
import it.webred.ct.service.tares.data.model.SetarSegnala3;
import it.webred.ct.service.tares.data.model.SetarSegnalazione;
import it.webred.ct.service.tares.web.bean.TaresBaseBean;
import it.webred.ct.service.tares.web.bean.export.ExportXLSBean;

import org.apache.log4j.Logger;

public class ValutazioniBean extends TaresBaseBean implements ValutazioniDataProvider {

	protected ValutazioniService valutazioniService = (ValutazioniService) getEjb("Servizio_Tares", "Servizio_Tares_EJB", "ValutazioniServiceBean");
	
	protected SegnalazioniService segnalazioniService = (SegnalazioniService) getEjb("Servizio_Tares", "Servizio_Tares_EJB", "SegnalazioniServiceBean");
	
	private Boolean renderTab = false;
	private Long elabNum = null;
	private SetarElab elabCur = null;
	private String fpsElab = null; 
	
	private ValutazioniSearchCriteria criteriaElab = null;
	private List<SetarElab> lstElaborazioni = null;
	private List<SetarSegnalazione> lstSegnalazioni = null;
	private List<Object> lstVaniCur = null;
	
	//private ArrayList<SelectItem> alDifformi = null;

	public void init(){
		lstElaborazioni = new LinkedList<SetarElab>();
		lstSegnalazioni = new ArrayList<SetarSegnalazione>();
		lstVaniCur = new LinkedList<Object>();
		criteriaElab = new ValutazioniSearchCriteria();
		renderTab = false;
		elabNum = new Long(0);
		//alDifformi = new ArrayList<SelectItem>();
	}//-------------------------------------------------------------------------
	
	public String goDatiMetriciUiu() {
		logger.info(ValutazioniBean.class + ".goDatiMetriciUiu");
		
		init();
		/*
		 * reimposto i valori correnti in sessione
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		Hashtable<String, SetarElab> htSegnala = (Hashtable<String, SetarElab>)context.getExternalContext().getSessionMap().get("htSegnala");
		List<SetarElab> lstSegnala = (List<SetarElab>)context.getExternalContext().getSessionMap().get("lstSegnala");
        Integer numSegnala = (Integer)context.getExternalContext().getSessionMap().get("numSegnala");
        if (lstSegnala != null && lstSegnala.size() > 0){
        	
        }else{
        	lstSegnala = new LinkedList<SetarElab>();
        	htSegnala = new Hashtable<String, SetarElab>();
        	numSegnala = 0;
        }		
        context.getExternalContext().getSessionMap().put("lstSegnala", lstSegnala);
        context.getExternalContext().getSessionMap().put("htSegnala", htSegnala);
        context.getExternalContext().getSessionMap().put("numSegnala", numSegnala);
		
		return "valutazioni.datiMetriciUiu";
	}// ------------------------------------------------------------------------
	
	public String goRecordDetail(){
		String esito = "valutazioni.goRecordDetail";
		logger.info(ValutazioniBean.class + ".goRecordDetail");
		
		lstSegnalazioni = new ArrayList<SetarSegnalazione>();
		
		//if (elabCur != null){
		if (fpsElab != null){
			//foglio_particella_unimm_segnalazioniIds;
			String[] fpss = fpsElab.split("_");
			/*
			 * 0: foglio
			 * 1: particella
			 * 2: unimm
			 * 3: segnalazioni separate dal ";2
			 */
			String foglio = fpss[0];
			String particella = fpss[1];
			String unimm = fpss[2];
			String segnalazioniIds = fpss[3];
			
			SetarElab elaborazione = new SetarElab();
			SetarElabPK elaborazionePk = new SetarElabPK();
			elaborazionePk.setFoglio(new BigDecimal(foglio));
			elaborazionePk.setParticella(particella);
			elaborazionePk.setUnimm(new BigDecimal(unimm));
			elaborazione.setId(elaborazionePk);
			DataInDTO dataIn3 = new DataInDTO();
			fillEnte(dataIn3);
			dataIn3.setObj(elaborazione);
			elabCur = valutazioniService.getElaborazione(dataIn3);
			
			logger.info("Segnalazioni uiu corrente: " + segnalazioniIds);
			String[] arySegnalazioniIds = segnalazioniIds.split(";");
			if (arySegnalazioniIds != null && arySegnalazioniIds.length>0){
				/*
				 *Per ogni id segnalazione recupero il record dalla tabella SETAR_SEGNALAZIONI,
				 *tutti gli immobili coinvolti nella segnalazione da SETAR_SEGNALA_1 
				 *e per ogni immobile i suoi vani da SETAR_SEGNALA_2 e le persone 
				 *SETAR_SEGNALA_3 
				 */
				for (int index=0; index<arySegnalazioniIds.length; index++){
					String segnalazioneId = arySegnalazioniIds[index];
					DataInDTO dataIn = new DataInDTO();
					fillEnte(dataIn);
					/*
					 * SETAR_SEGNALAZIONI
					 */
					SegnalazioniSearchCriteria criteriaSegnala = new SegnalazioniSearchCriteria();
					criteriaSegnala.setSegnalazioneId(new Long( segnalazioneId) );
					dataIn.setCriteriaSegnalazioni(criteriaSegnala);
					
					SetarSegnalazione setarSegnalazione = new SetarSegnalazione();
					setarSegnalazione.setId(new Long( segnalazioneId) );
					dataIn.setObj(setarSegnalazione);
					SetarSegnalazione ss = segnalazioniService.getSegnalazione(dataIn);
					/*
					 * SETAR_SEGNALA_1: accedere con fps 
					 */
					criteriaSegnala = new SegnalazioniSearchCriteria();
					criteriaSegnala.setFoglio(foglio);
					criteriaSegnala.setNumero(particella);
					criteriaSegnala.setSubalterno(unimm);
					dataIn.setCriteriaSegnalazioni(criteriaSegnala);
					List<SetarSegnala1> lstSs1 = segnalazioniService.getSegnalazioni1(dataIn);
					/*
					 * Anche se nel filtro non � stata impostata la data fine validit�
					 * non importa perch� la TARES/TARSU � cariacta sempre completa
					 */
					lstVaniCur = segnalazioniService.getVani(dataIn);
					/*
					 * XXX ordine di recuero query: AMBIENTE, SUPE_AMBIENTE, ALTEZZAMIN, ALTEZZAMAX
					 */
					for (SetarSegnala1 ss1 : lstSs1) {
						ss.setSegnala1(ss1);

						criteriaSegnala = new SegnalazioniSearchCriteria();
						criteriaSegnala.setSegnala1Id(ss1.getId());
						dataIn.setCriteriaSegnalazioni(criteriaSegnala);
						List<SetarSegnala2> lstSs2 = segnalazioniService.getSegnalazioni2(dataIn);
						if (lstSs2 != null)
							ss.setLstSegnala2(new ArrayList<SetarSegnala2>(lstSs2));
						else
							ss.setLstSegnala2(new ArrayList<SetarSegnala2>());
						
//						List<SetarSegnala3> lstSs3 = segnalazioniService.getSegnalazioni3(dataIn);
						ss.setSegnala3(new SetarSegnala3());
						
						lstSegnalazioni.add(ss);
						
					}
				}
			}

		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String doResetSegnala() throws Exception{
		logger.info(ValutazioniBean.class + ".doResetSegnala");

		/*
		 * ripulisco dalla sessione le variabili di interesse
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		Hashtable<String, SetarElab> htSegnala = new Hashtable<String, SetarElab>();
		List<SetarElab> lstSegnala = new LinkedList<SetarElab>();
        Integer numSegnala = new Integer(0);
        context.getExternalContext().getSessionMap().put("lstSegnala", lstSegnala);
        context.getExternalContext().getSessionMap().put("htSegnala", htSegnala);
        context.getExternalContext().getSessionMap().put("numSegnala", numSegnala);
        
		return "valutazioni.doResetSegnala";
	}//-------------------------------------------------------------------------
	
	public String doAddSegnala() throws Exception{
		logger.info(ValutazioniBean.class + ".doAddSegnala");
		/*
		 * recupero dalla sessione la lista delle segnalazioni gia selezionate, se esistente
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		Hashtable<String, SetarElab> htSegnala = (Hashtable<String, SetarElab>)context.getExternalContext().getSessionMap().get("htSegnala");
		List<SetarElab> lstSegnala = (List<SetarElab>)context.getExternalContext().getSessionMap().get("lstSegnala");
        Integer numSegnala = (Integer)context.getExternalContext().getSessionMap().get("numSegnala");
        if (lstSegnala != null && lstSegnala.size() > 0){
        	
        }else{
        	lstSegnala = new LinkedList<SetarElab>();
        	htSegnala = new Hashtable<String, SetarElab>();
        	numSegnala = 0;
        }

        if (lstElaborazioni != null && lstElaborazioni.size()>0){
            for (SetarElab uiu : lstElaborazioni ){
            	if (uiu != null && uiu.getSel()){
            		String f = uiu.getId().getFoglio()!=null?uiu.getId().getFoglio().toString():"";
            		String p = Character.checkNullString( uiu.getId().getParticella() );
            		String s = uiu.getId().getUnimm()!=null?uiu.getId().getUnimm().toString():"";
            		String key = f + "_" + p + "_" + s;
            		final MessageDigest md = MessageDigest.getInstance("MD5");
            		md.update(key.getBytes());
            		final byte[] digest = md.digest();
            		String md5 = Character.toHexString(digest);
            		if (!htSegnala.containsKey(md5)){
                		htSegnala.put(md5, uiu);
                		lstSegnala.add(uiu);
                		logger.info("UIU accodata alle segnalazioni: KEY = " + key + " MD5 = " + md5);
            		}else{
                		logger.info("UIU già presente nelle selezione: KEY = " + key + " MD5 = " + md5);
            		}
            	}
            }
        }
        numSegnala = lstSegnala.size();
        /*
         * metto in sessione l'elaborazione
         */
        context.getExternalContext().getSessionMap().put("lstSegnala", lstSegnala);
        context.getExternalContext().getSessionMap().put("htSegnala", htSegnala);
        context.getExternalContext().getSessionMap().put("numSegnala", numSegnala);
		
		return "valutazioni.doAddSegnala";
	}//-------------------------------------------------------------------------
	
	public String goXls() {
		logger.info(ValutazioniBean.class + ".goXls");
		
		ValutazioniSearchCriteria criteriaXls = new ValutazioniSearchCriteria();
		criteriaXls.setCategoria(criteriaElab.getCategoria());
		criteriaXls.setClasse(criteriaElab.getClasse());
		criteriaXls.setDeltaDich80Al(criteriaElab.getDeltaDich80Al());
		criteriaXls.setDeltaDich80Dal(criteriaElab.getDeltaDich80Dal());
		criteriaXls.setDeltaSupCatCalcAl(criteriaElab.getDeltaSupCatCalcAl());
		criteriaXls.setDeltaSupCatCalcDal(criteriaElab.getDeltaSupCatCalcDal());
		criteriaXls.setDeltaSupCatTarsuCalcAl(criteriaElab.getDeltaSupCatTarsuCalcAl());
		criteriaXls.setDeltaSupCatTarsuCalcDal(criteriaElab.getDeltaSupCatTarsuCalcDal());
		criteriaXls.setFoglio(criteriaElab.getFoglio());
		criteriaXls.setIdSezc(criteriaElab.getIdSezc());
		criteriaXls.setParticella(criteriaElab.getParticella());
		criteriaXls.setRenditaAl(criteriaElab.getRenditaAl());
		criteriaXls.setRenditaDal(criteriaElab.getRenditaDal());
		criteriaXls.setStatoCatasto(criteriaElab.getStatoCatasto());
		criteriaXls.setSub(criteriaElab.getSub());
		criteriaXls.setUnimm(criteriaElab.getUnimm());
		criteriaXls.setVanoAl(criteriaElab.getVanoAl());
		criteriaXls.setVanoDal(criteriaElab.getVanoDal());
		criteriaXls.setSupCatTarsuDal(criteriaElab.getSupCatTarsuDal());
		criteriaXls.setSupCatTarsuAl(criteriaElab.getSupCatTarsuAl());
	
		if (elabNum != null && elabNum > 0){
			
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			int offset = 0; 
			int limit = 10000;
			int loops = 0;
			
			criteriaXls.setLimit(limit);
			criteriaXls.setOffset(offset);
			dataIn.setCriteria(criteriaXls);
			/*
			 * Recupero il path per scrivere il file temporaneamente 
			 */
			ParameterSearchCriteria paramCriteria = new ParameterSearchCriteria();
			paramCriteria.setKey("dir.files.dati");
			String pathXLS = doGetListaKeyValue(paramCriteria);
			
			if (elabNum < limit){
				/*
				 * Unica query
				 */
				try{
					limit = elabNum.intValue();
					
					List<SetarElab> lstEla = valutazioniService.getElaborazioni(dataIn);
					ExportXLSBean xls = new ExportXLSBean();
					xls.setPathXLS(pathXLS);
					xls.setLista(lstEla);
					/*
					 * Faccio iniziare dalla riga 1 non dalla zero perché ci sono 
					 * le intestazioni
					 */
					xls.setRowStart(1);
					xls.resultListTaresExportToXls("ELAB");
					
					logger.info("Elaborazioni esportate: " + lstEla.size());
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}else{
				/*
				 * Preparo il file per l'esportazione
				 */
				double val = Math.random();
				
				String filePath = pathXLS + "/ReportTares@" + val + ".xls";
				try{
					FileOutputStream file = new FileOutputStream(filePath);
					WorkbookSettings wbSettings = new WorkbookSettings();
	
					wbSettings.setLocale(new Locale("en", "EN"));
	
					WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
					workbook.createSheet("Report", 0);
					WritableSheet excelSheet = workbook.getSheet(0);
					ExportXLSBean xls = new ExportXLSBean();
					xls.createTaresLabel(excelSheet);
					xls.setPathXLS(pathXLS);
					/*
					 * Determino il numero di queries da fare 
					 */
					double dbl = elabNum / 10000;
					loops = (int)dbl + 1;
					int parziale = 10000;
					for(int index=0; index<loops; index++){
						/*
						 * Se primo giro faccio iniziare dalla riga 1 non dalla 
						 * zero perché ci sono le intestazioni
						 */
						xls.setRowStart(offset+1);
						List<SetarElab> lstEla = valutazioniService.getElaborazioni(dataIn);
						xls.setLista(lstEla);						
						xls.createTaresContent(excelSheet);
						/*
						 * imposto i Valori per la prossima query nel caso venga 
						 * eseguita perché puo anche essere che questa sia l'ultima
						 */
						offset = offset + limit;
						parziale = parziale + limit;
						if (parziale > elabNum)
							limit = elabNum.intValue() - offset;
						criteriaXls.setLimit(limit);
						criteriaXls.setOffset(offset);
						dataIn.setCriteria(criteriaXls);
						
						logger.info("Elaborazioni esportate: " + lstEla.size());
					}
					
					workbook.write();
					workbook.close();
					file.flush();
					xls.close(file);
					xls.showXls(filePath);
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
			
		}
		
		return "valutazioni.xls";
	}// ------------------------------------------------------------------------

	public List<SetarElab> doCercaElaborazioni(int offset, int limit){
		logger.info( ValutazioniBean.class + ".doCercaElaborazioni" );
		List<SetarElab> lstEla = new LinkedList<SetarElab>();
		if (!renderTab){
			renderTab = true;
		}else{
			DataInDTO dataIn = new DataInDTO();
			
			fillEnte(dataIn);
	        criteriaElab.setLimit(limit);
	        criteriaElab.setOffset(offset);
			dataIn.setCriteria(criteriaElab);
//			elabCnt = valutazioniService.getElaborazioniCount(dataIn);
			/*
			 * Gestione paginazione per select
			 */
			lstEla = valutazioniService.getElaborazioni(dataIn);
			/*
			 * riempire gli attributi di segnalazione e/o esportazione segnalazioni
			 */
			if (lstEla != null && lstEla.size()>0){
				SegnalazioniSearchCriteria criteriaSegnala = new SegnalazioniSearchCriteria();
				for (SetarElab iEla : lstEla){
					criteriaSegnala.setFoglio(iEla.getId().getFoglio().toString());
					criteriaSegnala.setNumero(iEla.getId().getParticella());
					criteriaSegnala.setSubalterno(iEla.getId().getUnimm().toString());
					dataIn.setCriteriaSegnalazioni(criteriaSegnala);
					List<SetarSegnala1> lstSs1 = segnalazioniService.getSegnalazioni1(dataIn);
					/*
					 * L'esito di questa query potrebbe essere multiriga, perché 
					 * la segnalazione di una UIU puo avvenire anche piu di una 
					 * volta, in ogni caso però la segnalazione non ancora esportata
					 * sarà sempre UNICA  
					 */
					boolean segnalata = false;
					boolean esportata = false;
					boolean difforme = false;
					String segnalazioniIds = "";
					if (lstSs1 != null && lstSs1.size()>0){
						Date dataInserimentoMax = null; 
						for (SetarSegnala1 ss1 : lstSs1){
							if (ss1.getEsportata()){
								if (dataInserimentoMax != null){
									/*
									 * non � il primo giro
									 */
									if (ss1.getDataInserimento().after(dataInserimentoMax)){
										/*
										 * la segnalazione corrente � piu recente
										 */
										dataInserimentoMax = ss1.getDataInserimento();
										if (ss1.getDifforme() != null && ss1.getDifforme().equalsIgnoreCase("DIFFORME"))
											difforme = true;
										else
											difforme = false;
									}else{
										/*
										 * la segnalazione corrente � antecedente
										 * quindi devo fare nulla 
										 */
									}
								}else{
									/*
									 * � il primo giro
									 */
									dataInserimentoMax = ss1.getDataInserimento();
									if (ss1.getDifforme() != null && ss1.getDifforme().equalsIgnoreCase("DIFFORME"))
										difforme = true;
									else 
										difforme = false;
								}
								esportata = true;
								segnalazioniIds += ss1.getSegnalazioneId() + ";";
							}
						}
						/*
						 * se nessuna segnalazione è stata esportata sarà sicurmante 
						 * segnalata
						 */
						if (!esportata)
							segnalata = true;
					}
					iEla.setSegnalata(segnalata);
					iEla.setEsportata(esportata);
					iEla.setDifforme(difforme);
					iEla.setSegnalazioniIds(segnalazioniIds);
				}
			}
			
			lstElaborazioni = new LinkedList<SetarElab>(lstEla);
		}
		
//		System.out.println("Elaborazioni n. " + lstElaborazioni.size() + "; Num. righe per pag: " + itemsPerPage + "; num. pag: " + pagesVar);
//		sezioniService.getSezioniByName(dataIn);
		
		return lstEla;
	}//-------------------------------------------------------------------------
	
	public Long doCountElaborazioni(){
		logger.info(ValutazioniBean.class + ".doCountElaborazioni");
		Long elabCnt = new Long(0);
		if (!renderTab){
			
		}else{
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setCriteria(criteriaElab);
			elabCnt = valutazioniService.getElaborazioniCount(dataIn).longValue();
			elabNum = elabCnt;
			
			logger.info("Elaborazioni cercate: " + elabCnt);
		}
		/*
		 * se � stato applicato il filtro di difformita a runtime si deve aggironare
		 * il numero di righe in base all'esito del filtro
		 */
		if (criteriaElab != null && criteriaElab.getDifformita() != null && !criteriaElab.getDifformita().trim().equalsIgnoreCase("")){
			elabNum = elabCnt = new Long(lstElaborazioni.size() );
		}
		
		/*
		 * Gestione paginazione per select
		 */
//		lstElaborazioni = valutazioniService.getElaborazioni(dataIn);
		
//		System.out.println("Elaborazioni n. " + lstElaborazioni.size() + "; Num. righe per pag: " + itemsPerPage + "; num. pag: " + pagesVar);
//		sezioniService.getSezioniByName(dataIn);
		return elabCnt;
	}//-------------------------------------------------------------------------
	
	public ValutazioniSearchCriteria getCriteriaElab() {
		return criteriaElab;
	}

	public void setCriteriaElab(ValutazioniSearchCriteria criteriaElab) {
		this.criteriaElab = criteriaElab;
	}

	public List<SetarElab> getLstElaborazioni() {
		return lstElaborazioni;
	}

	public void setLstElaborazioni(List<SetarElab> lstElaborazioni) {
		this.lstElaborazioni = lstElaborazioni;
	}

	public Boolean getRenderTab() {
		return renderTab;
	}

	public void setRenderTab(Boolean renderTab) {
		this.renderTab = renderTab;
	}

	public Long getElabNum() {
		return elabNum;
	}

	public void setElabNum(Long elabNum) {
		this.elabNum = elabNum;
	}

	public SetarElab getElabCur() {
		return elabCur;
	}

	public void setElabCur(SetarElab elabCur) {
		this.elabCur = elabCur;
	}

	public List<SetarSegnalazione> getLstSegnalazioni() {
		return lstSegnalazioni;
	}

	public void setLstSegnalazioni(List<SetarSegnalazione> lstSegnalazioni) {
		this.lstSegnalazioni = lstSegnalazioni;
	}

	public List<Object> getLstVaniCur() {
		return lstVaniCur;
	}

	public void setLstVaniCur(List<Object> lstVani) {
		this.lstVaniCur = lstVani;
	}

	public String getFpsElab() {
		return fpsElab;
	}

	public void setFpsElab(String indice) {
		this.fpsElab = indice;
	}



//	public ArrayList<SelectItem> getAlDifformi() {
//		return alDifformi;
//	}
//
//	public void setAlDifformi(ArrayList<SelectItem> alDifformi) {
//		this.alDifformi = alDifformi;
//	}


	
}
