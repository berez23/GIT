package it.webred.ct.service.tares.web.bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import it.doro.util.Character;
import it.doro.util.CreateZip;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.tares.data.access.SegnalazioniService;
import it.webred.ct.service.tares.data.access.ValutazioniService;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.access.dto.SegnalazioniSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarElab;
import it.webred.ct.service.tares.data.model.SetarSegnala1;
import it.webred.ct.service.tares.data.model.SetarSegnala2;
import it.webred.ct.service.tares.data.model.SetarSegnala3;
import it.webred.ct.service.tares.data.model.SetarSegnalazione;
import it.webred.ct.service.tares.srv.OpenPdfSrv;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class SegnalaBean extends TaresBaseBean {

	protected SegnalazioniService segnalazioniService = (SegnalazioniService) getEjb("Servizio_Tares", "Servizio_Tares_EJB", "SegnalazioniServiceBean");

	private SegnalazioniSearchCriteria criteriaSegnala = null;
	private ArrayList<SetarSegnalazione> lstSegnalazioni = null;
	private ArrayList<SetarSegnalazione> lstSegnalazione = null;
	private ArrayList<SetarSegnalazione> lstSegnalazioniEsportate = null;
	private ArrayList<SelectItem> lstAmbientiItems = null;
	private SetarSegnalazione segnalazioneCur = null;
	private SetarSegnala2 segnala2Cur = null;
	private SetarSegnala1 segnala1Cur = null;
	private String pathFornitura = "";
	private String fileName = "";

	private TimeZone defaultTimeZone = TimeZone.getDefault();

	public SegnalaBean() {

	}//-------------------------------------------------------------------------
	
	public void init(){
		criteriaSegnala = new SegnalazioniSearchCriteria();
		lstSegnalazioni = new ArrayList<SetarSegnalazione>();
		lstSegnalazione = new ArrayList<SetarSegnalazione>();
		lstSegnalazioniEsportate = new ArrayList<SetarSegnalazione>();
		segnalazioneCur = new SetarSegnalazione();
		segnala1Cur = new SetarSegnala1();
		segnala2Cur = new SetarSegnala2();
		lstAmbientiItems = new ArrayList<SelectItem>();
		pathFornitura = "";
	}//-------------------------------------------------------------------------
	
	public String goSegnalaShow() {
		logger.info(SegnalaBean.class + ".goSegnalaShow");
		
		init();
		/*
		 * Inserisco le UIU selezionate dalla pagine dei dati metrici nel DB sottoforma 
		 * di segnalazioni, ma prima di effettuare l'inser verifico che la UIU corrente 
		 * non sia già presente tra le segnalazioni NON ancora esportate e quindi 
		 * da processare
		 */
		/*
		 * Recupero UIU selezionate dalla maschera dei Dati Metrici e messe in sessione
		 * perché ancora non salvate
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		List<SetarElab> lstSegnala = (List<SetarElab>)context.getExternalContext().getSessionMap().get("lstSegnala");
        Integer numSegnala = (Integer)context.getExternalContext().getSessionMap().get("numSegnala");
        if (lstSegnala != null && lstSegnala.size() > 0){
        	for (SetarElab uiu : lstSegnala ){

        		/*
        		 * Informazioni Tipo record 1 del tracciato
        		 */
        		String foglio = uiu.getId().getFoglio().toString();
   				String particella = uiu.getId().getParticella();
				String subalterno = uiu.getId().getUnimm().toString();
				String sezioneCensuaria = uiu.getIdSezc()!=null?uiu.getIdSezc():"";
				
				/*
				 * Verifico se la UIU corrente è già stata inserita tra le segnalazioni
				 * da ESPORTARE  
				 */
				DataInDTO dataIn = new DataInDTO();
				fillEnte(dataIn);
				criteriaSegnala = new SegnalazioniSearchCriteria();
				criteriaSegnala.setEsportata(false);
    			criteriaSegnala.setFoglio(foglio);
    			criteriaSegnala.setNumero(particella);
    			criteriaSegnala.setSubalterno(subalterno);
				dataIn.setCriteriaSegnalazioni(criteriaSegnala);
				List<SetarSegnala1> lstCoordinateCorrenti = segnalazioniService.getSegnalazioni1(dataIn);
        		if (lstCoordinateCorrenti != null && lstCoordinateCorrenti.size()>0){
        			/*
        			 * UIU già presente tra quelle da processare per cui non inserisco 
        			 * nulla
        			 */
        		}else{
        			/*
        			 * UIU da inserire nella lista delle segnalazioni da esportare
        			 */
        			/*
	        		 * PROGRESSIVO: va fatta una verifica nello storico delle segnalazioni 
	        		 * esportate che ci permetta di capire se e quante segnalazioni 
	        		 * sono state fatte per questa UIU
	        		 */
	        		Integer progressivo = 0;
	        		dataIn = new DataInDTO();
	    			fillEnte(dataIn);
	    			criteriaSegnala.setFoglio(foglio);
	    			criteriaSegnala.setNumero(particella);
	    			criteriaSegnala.setSubalterno(subalterno);
	    			criteriaSegnala.setEsportata(true);
	    			dataIn.setCriteriaSegnalazioni(criteriaSegnala);
	        		List<SetarSegnala1> lstSegnala1 = segnalazioniService.getSegnalazioni1(dataIn);
	        		if (lstSegnala1 != null && lstSegnala1.size()>0){
	            		progressivo = lstSegnala1.size()+1;        			
	        		}else{
	        			progressivo++;
	        		}
	        		/*
	        		 * IDENTIFICATIVO IMMOBILE: recuperare dalla SITIUIU l'IDE_IMMO 
	        		 * attraverso fps + IDE_MUTA_FINE is null
	        		 *
	        		 */
	        		String identificativoImmobile = "0";
	        		criteriaSegnala.setEsportata(null);
	    			dataIn.setCriteriaSegnalazioni(criteriaSegnala);
	        		List<Object> lstUiu = segnalazioniService.getUiu(dataIn);
	        		if (lstUiu != null && lstUiu.size()>0){
	        			for (int i=0; i<lstUiu.size(); i++){
	        				Object[] sitiuiu = (Object[])lstUiu.get(i);
	        				BigDecimal ideUiu = (BigDecimal)sitiuiu[21];
	        				identificativoImmobile = ideUiu.toString();
	        			}
	        		}
	        		/*
	        		 * FOGLIO = 2
					 * AND PARTICELLA = '00084'
					 * AND UNIMM = 2
					 * AND IDE_MUTA_FINE IS NULL
	        		 */

	        		/*
	        		 * Inserisco nel DB le informazioni
	        		 */
	        		String codiceAmministrativo = uiu.getCodiFiscLuna();
	        		
	        		SetarSegnala1 seg1 = new SetarSegnala1();
	        		seg1.setCodiceAmministrativo(codiceAmministrativo);
	        		seg1.setDenominatore(uiu.getSub());
	        		seg1.setFoglio(foglio);
	        		seg1.setIdentificativoImmobile(identificativoImmobile);
	        		seg1.setNumero(particella);
	        		seg1.setProgressivo(progressivo.toString());
	        		seg1.setSezioneCensuaria(sezioneCensuaria);
	        		//seg1.setSezioneUrbana(sezioneUrbana);
	        		seg1.setSezioneUrbana("");
	        		seg1.setSubalterno(subalterno);
	        		BigDecimal superfTot = null;
	        		if (uiu.getSupCat() != null){
	        			superfTot = uiu.getSupCat().setScale(2, BigDecimal.ROUND_HALF_EVEN);
	        			seg1.setSuperficieTotaleCalcolata(false);
	        		}else if (uiu.getSupCatDpr13898() != null){
	        			superfTot = uiu.getSupCatDpr13898().setScale(2, BigDecimal.ROUND_HALF_EVEN);
	        			seg1.setSuperficieTotaleCalcolata(true);
	        		}
	        		BigDecimal superf = null;
	        		if (uiu.getSupCatTarsu() != null){
	        			superf = uiu.getSupCatTarsu().setScale(2, BigDecimal.ROUND_HALF_EVEN);
	        		}
	        		seg1.setSuperficieTotale( superfTot!=null?superfTot.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
	        		seg1.setSuperficie( superf!=null?superf.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
	        		seg1.setTipoRecord("1");
	        		/*
	        		 * inserisco segnala1
	        		 */
	        		dataIn.setObj(seg1);
	        		SetarSegnala1 ss1 = segnalazioniService.addSegnala1(dataIn);
	        		/*
	        		 * Informazioni Tipo record 2 del tracciato
	        		 */
	        		/*
	        		 * Recupero max 10 vani (= ambienti) dell'uiu corrente
	        		 */
	        		criteriaSegnala.setOffset(0);
	        		criteriaSegnala.setLimit(10);
	    			dataIn.setCriteriaSegnalazioni(criteriaSegnala);
	        		List<Object> lstVani = segnalazioniService.getVani(dataIn);
	        		if (lstVani != null && lstVani.size()>0){

	        			for (int index=0; index<lstVani.size(); index++){
	        				Object[] vano = (Object[])lstVani.get(index);
	        				BigDecimal h = (BigDecimal)vano[2];
	        				BigDecimal hMax = (BigDecimal)vano[3];
	        				String amb = (String)vano[0];
	        				BigDecimal sa = (BigDecimal)vano[1];
	        				
	        				SetarSegnala2 seg2 = new SetarSegnala2();
	        				seg2.setAltezza(h.toString());
	        				seg2.setAltezzaMax(hMax.toString());
	        				seg2.setAmbiente( amb );
	        				seg2.setCodiceAmministrativo(codiceAmministrativo);
	        				seg2.setIdentificativoImmobile(identificativoImmobile);
	        				seg2.setProgressivo(progressivo.toString());
	        				seg2.setSetarSegnala1(ss1);
	        				seg2.setSegnala1Id(ss1.getId());
	        				seg2.setSezioneCensuaria(sezioneCensuaria);
	        				seg2.setSuperficieAmbiente( sa.toString() );
	        				seg2.setTipoRecord("2");
	        				/*
	        				 * inserisco segnala2
	        				 */
	        				dataIn.setObj(seg2);
	    	        		SetarSegnala2 ss2 = segnalazioniService.addSegnala2(dataIn);
	        			}
	        		}
	        		/*
	        		 * Informazioni Tipo record 3 del tracciato
	        		 */
//	        		SetarSegnala3 seg3 = new SetarSegnala3();
//	        		seg3.setCodiceFiscale(new StringBuffer(16).toString());
//	        		seg3.setCognome(new StringBuffer(50).toString());
//	        		seg3.setEmail(new StringBuffer(200).toString());
//	        		seg3.setNome(new StringBuffer(50).toString());
//	        		seg3.setRuolo(new StringBuffer(200).toString());
//	        		seg3.setTelefono(new StringBuffer(20).toString());
	        		/*
	        		 * inserisco segnala3
	        		 */
	        		
        		}
        	}
        }
        
        loadSegnalazioniDaEsportare();
		
		return "segnala.goSegnalaShow";
	}// ------------------------------------------------------------------------
	
	public String doDelUiu() {
		logger.info(SegnalaBean.class + ".doDelUiu");
		
		if (lstSegnalazioni != null && lstSegnalazioni.size()>0){
			for (SetarSegnalazione segnalazione : lstSegnalazioni){
				if (segnalazione.getSel()){
					DataInDTO dataIn = new DataInDTO();
					fillEnte(dataIn);
					dataIn.setObj(segnalazione.getSegnala1());
					segnalazioniService.delSegnala1ById(dataIn);
					ArrayList<SetarSegnala2> alSegnala2 = segnalazione.getLstSegnala2();
					if (alSegnala2 != null && alSegnala2.size()>0){
						for (SetarSegnala2 ss2 : alSegnala2){
							dataIn.setObj(ss2);
							segnalazioniService.delSegnala2ById(dataIn);
						}
					}
					logger.info("UIU eliminata da DB: " + segnalazione.getSegnala1().getIdentificativoImmobile());
				}
			}
		}

		loadSegnalazioniDaEsportare();
		
		return "segnala.doDelUiu";
	}// ------------------------------------------------------------------------
	
	public String doDelVano() {
		logger.info(SegnalaBean.class + ".doDelVano");
		
		if (lstSegnalazioni != null && lstSegnalazioni.size()>0){
			for (SetarSegnalazione segnalazione : lstSegnalazioni){
					
					ArrayList<SetarSegnala2> alSegnala2 = segnalazione.getLstSegnala2();
					if (alSegnala2 != null && alSegnala2.size()>0){
						DataInDTO dataIn = new DataInDTO();
						fillEnte(dataIn);
						for (SetarSegnala2 ss2 : alSegnala2){
							if ( ss2.getSel()){
								dataIn.setObj(ss2);
								segnalazioniService.delSegnala2ById(dataIn);								
							}
						}
					}
					logger.info("VANO eliminata da DB: " + segnalazione.getSegnala1().getIdentificativoImmobile());
			}
		}

		loadSegnalazioniDaEsportare();
		
		return "segnala.doDelVano";
	}// ------------------------------------------------------------------------

	public String doVanoAddShow() {
		logger.info(SegnalaBean.class + ".doVanoAddShow");
		/*
		 * Recupero lista degli ambienti
		 */
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		SegnalazioniSearchCriteria criteriaSegnala = new SegnalazioniSearchCriteria();
		dataIn.setCriteriaSegnalazioni(criteriaSegnala);
		lstAmbientiItems = new ArrayList<SelectItem>();
		List<Object> listaAmbienti = segnalazioniService.getAmbienti(dataIn);
		if (listaAmbienti != null && listaAmbienti.size()>0){
			for (int index=0; index<listaAmbienti.size(); index++){
				Object obj = listaAmbienti.get(index);
				SelectItem si = new SelectItem();
				si.setDescription((String)obj);
				si.setLabel( (String)obj );
				si.setValue( (String)obj );
				
				lstAmbientiItems.add(si);
			}
		}
		/*
		 * Preparazione bean per aggiunta vano
		 */
		logger.info("Aggiunta vano IMMOBILE:" + segnalazioneCur.getSegnala1().getIdentificativoImmobile());
		segnala2Cur = new SetarSegnala2();
		segnala2Cur.setCodiceAmministrativo(segnalazioneCur.getSegnala1().getCodiceAmministrativo());
		segnala2Cur.setIdentificativoImmobile(segnalazioneCur.getSegnala1().getIdentificativoImmobile());
		segnala2Cur.setProgressivo(segnalazioneCur.getSegnala1().getProgressivo());
		segnala2Cur.setSetarSegnala1(segnalazioneCur.getSegnala1());
		segnala2Cur.setSegnala1Id(segnalazioneCur.getSegnala1().getId());
		segnala2Cur.setSezioneCensuaria(segnalazioneCur.getSegnala1().getSezioneCensuaria());
		segnala2Cur.setTipoRecord("2");
		
		return "segnala.doVanoAddShow";
	}// ------------------------------------------------------------------------
	
	public String doVanoAddSave() {
		logger.info(SegnalaBean.class + ".doVanoAddSave");
		
		if (segnala2Cur != null){
			if (segnala2Cur.getAltezza() != null && !segnala2Cur.getAltezza().equalsIgnoreCase("") && 
					segnala2Cur.getAltezzaMax() != null && !segnala2Cur.getAltezzaMax().equalsIgnoreCase("") && 
					segnala2Cur.getAmbiente() != null && !segnala2Cur.getAmbiente().equalsIgnoreCase("") && 
					segnala2Cur.getSuperficieAmbiente() != null && !segnala2Cur.getSuperficieAmbiente().equalsIgnoreCase("")){

				logger.info("Alt. Min: " + segnala2Cur.getAltezza());
				logger.info("Alt. Max: " + segnala2Cur.getAltezzaMax());
				logger.info("Ambiente: " + segnala2Cur.getAmbiente());
				logger.info("Sup. Amb: " + segnala2Cur.getSuperficieAmbiente());

				DataInDTO dataIn = new DataInDTO();
				fillEnte(dataIn);
				dataIn.setObj(segnala2Cur);
        		SetarSegnala2 ss2 = segnalazioniService.addSegnala2(dataIn);
    			super.addInfoMessage("operazione.success");
    			/*
    			 * ricarico la lista
    			 */
    			loadSegnalazioniDaEsportare();
    			/*
    			 * Preparo per nuovo inserimento vano
    			 */
    			segnala2Cur = new SetarSegnala2();
    			segnala2Cur.setCodiceAmministrativo(segnalazioneCur.getSegnala1().getCodiceAmministrativo());
    			segnala2Cur.setIdentificativoImmobile(segnalazioneCur.getSegnala1().getIdentificativoImmobile());
    			segnala2Cur.setProgressivo(segnalazioneCur.getSegnala1().getProgressivo());
    			segnala2Cur.setSetarSegnala1(segnalazioneCur.getSegnala1());
    			segnala2Cur.setSegnala1Id(segnalazioneCur.getSegnala1().getId());
    			segnala2Cur.setSezioneCensuaria(segnalazioneCur.getSegnala1().getSezioneCensuaria());
    			segnala2Cur.setTipoRecord("2");
			}else{
				super.addErrorMessage("valori.mancanti", "");
			}
		}else{
			super.addErrorMessage("valori.mancanti", "");			
		}
		
		
		return "segnala.doVanoAddSave";
	}// ------------------------------------------------------------------------
	
	public String goXls() {
		logger.info(SegnalaBean.class + ".goXls");
		
		return "segnala.xls";
	}// ------------------------------------------------------------------------
	
	public String doSegnalaSave() {
		logger.info(SegnalaBean.class + ".doSegnalaSave");
		Date adesso = new Date(System.currentTimeMillis());

		if (lstSegnalazioni != null && lstSegnalazioni.size()>0){
			for (SetarSegnalazione segnalazione : lstSegnalazioni){
				SetarSegnala1 ss1 = segnalazione.getSegnala1();
				ss1.setDataInserimento(adesso);
				DataInDTO dataIn1 = new DataInDTO();
				fillEnte(dataIn1);
				dataIn1.setObj(ss1);
				SetarSegnala1 s1 = segnalazioniService.updSegnala1(dataIn1);

				ArrayList<SetarSegnala2> lstSegnala2 = segnalazione.getLstSegnala2();
				if (lstSegnala2 != null && lstSegnala2.size()>0){
					for (SetarSegnala2 ss2 : lstSegnala2){
						ss2.setDataInserimento(adesso);
						ss2.setSegnala1Id(s1.getId());
						DataInDTO dataIn2 = new DataInDTO();
						fillEnte(dataIn2);
						dataIn2.setObj(ss2);
						segnalazioniService.updSegnala2(dataIn2);
					}					
				}

//				SetarSegnala3 ss3 = segnalazione.getSegnala3();
//				ss3.setDataInserimento(adesso);
//				ss3.setSegnala1Id(s1.getId());
//				dataIn.setObj(ss3);
//				segnalazioniService.addSegnala3(dataIn);
				
				super.addInfoMessage("operazione.success");
			}
		}
		
		return "segnala.doSegnalaSave";
	}//-------------------------------------------------------------------------
	
	public String goGeneraFile() {
		logger.info(SegnalaBean.class + ".goGeneraFile");
		
		Date adesso = new Date(System.currentTimeMillis());
		
		if (lstSegnalazioni != null && lstSegnalazioni.size()>0){
			boolean generaTxt = false;
			for (SetarSegnalazione segnalazione : lstSegnalazioni){
				
				if (segnalazione.getSel()){
					generaTxt = true;

					SetarSegnala1 ss1 = segnalazione.getSegnala1();
					ss1.setDataInserimento(adesso);
					DataInDTO dataIn1 = new DataInDTO();
					fillEnte(dataIn1);
					/*
					 * Premesso che il campo DIFFORME di SETAR_SEGNALA_1 deve essere 
					 * valorizzato solo per l'ultimo progressivo di ogni uiu:
					 * andiamo a verificare se lo stesso progressivo di ogni uiu 
					 * coinvolta nella attuale segnala_1 da esportare non � uguale 
					 * ad 1, questo significa che in passato ci sono state 
					 * altre segnala_1 pertanto vado a ripulire il campo DIFFORME
					 * della segnala_1 precedente perch� ora inseriremo una nuova 
					 * segnala_1 con il campo DIFFORME valorizzato
					 */
					String progressivoCorrente = ss1.getProgressivo();
					if (progressivoCorrente.equalsIgnoreCase("1")){
						/*
						 * Prima segnalazione pertanto non c'� alcuna SEGNALA_1.DIFFORME
						 * gi� esportata da ripulire
						 */
					}else{
						/*
						 * Recupero il max dei progressivi - 1 per questi fps con esportata=true
						 */
						Long pc = new Long(progressivoCorrente);
						pc--;
						SegnalazioniSearchCriteria criteriaSegnala = new SegnalazioniSearchCriteria();
						criteriaSegnala.setFoglio(ss1.getFoglio());
						criteriaSegnala.setNumero(ss1.getNumero());
						criteriaSegnala.setSubalterno(ss1.getSubalterno());
						criteriaSegnala.setEsportata(true);
						criteriaSegnala.setProgressivo(pc.toString());
						dataIn1.setCriteriaSegnalazioni(criteriaSegnala);
						/*
						 * recupero la segnala_1 precedente (=esportata) con il 
						 * maxProgressivo: la query seguente deve restituire un 
						 * solo record
						 */
						List<SetarSegnala1> listaSetarSegnala1 = segnalazioniService.getSegnalazioni1(dataIn1);
						SetarSegnala1 setarSegnala1 = null;
						if (listaSetarSegnala1 != null && listaSetarSegnala1.size()>0)
							setarSegnala1 = listaSetarSegnala1.get(0);
						/*
						 * pulisco il campo DIFFORME da qualsiasi contenuto
						 */
						setarSegnala1.setDifforme("");
						dataIn1.setObj(setarSegnala1);
						segnalazioniService.updSegnala1(dataIn1);
						
					}
					DataInDTO dataIn3 = new DataInDTO();
					fillEnte(dataIn3);
					/*
					 * XXX recupero della superficie totale dalla SITIUIU.SUP_CAT
					 * per determinare la difformit�/conformit� rispetto ai dati 
					 * segnalati
					 */
					String difforme = "CONFORME";
					criteriaSegnala = new SegnalazioniSearchCriteria();
					criteriaSegnala.setFoglio(ss1.getFoglio());
	    			criteriaSegnala.setNumero(ss1.getNumero());
	    			criteriaSegnala.setSubalterno(ss1.getSubalterno());
	    			dataIn3.setCriteriaSegnalazioni(criteriaSegnala);
	    			List<Object> lstUiu = segnalazioniService.getUiu(dataIn3);
	    			/*
	    			 * Questa query ha come clausola WHERE 1 = 1 and IDE_MUTA_FINE is null
	    			 * pertanto la lista che ne risulta in realt� dovrebbe essere 
	    			 * costituita da un solo record 
	    			 */
	    			if (lstUiu != null && lstUiu.size()>0){
	    				Object[] objUiu = (Object[])lstUiu.get(0);
	    				if (objUiu != null){
	    					BigDecimal supTotSeg = new BigDecimal(  ss1.getSuperficieTotale() );
	    					BigDecimal supTotCat = (BigDecimal)objUiu[11];
	    					if (supTotSeg.equals(supTotCat)){
	    						difforme = "CONFORME";
	    					}else{
	    						difforme = "DIFFORME";
	    					}
	    				}
	    			}else{
	    				difforme = "DIFFORME";
	    			}
					ss1.setDifforme(difforme);
					dataIn3.setObj(ss1);
					SetarSegnala1 s1 = segnalazioniService.updSegnala1(dataIn3);

					ArrayList<SetarSegnala2> lstSegnala2 = segnalazione.getLstSegnala2();
					if (lstSegnala2 != null && lstSegnala2.size()>0){
						for (SetarSegnala2 ss2 : lstSegnala2){
							ss2.setDataInserimento(adesso);
							ss2.setSegnala1Id(s1.getId());
							DataInDTO dataIn2 = new DataInDTO();
							fillEnte(dataIn2);
							dataIn2.setObj(ss2);
							segnalazioniService.updSegnala2(dataIn2);
						}					
					}

//					SetarSegnala3 ss3 = segnalazione.getSegnala3();
//					ss3.setDataInserimento(adesso);
//					ss3.setSegnala1Id(s1.getId());
//					dataIn.setObj(ss3);
//					segnalazioniService.addSegnala3(dataIn);
					
				}
			}
			
			if (generaTxt){
				createTXT();
				super.addInfoMessage("operazione.success");	
			}else
				super.addErrorMessage("selezione.assente", "Selezione Assente");
		}
		/*
		 * ripulisco tutte le variabili coinvolte ... anche quelle in sessione e 
		 * raggiungo la pagina dedicata alla gestione delle segnalazioni da esportare
		 */
		init();
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("lstSegnala");
		context.getExternalContext().getSessionMap().remove("htSegnala");
        context.getExternalContext().getSessionMap().remove("numSegnala");
        
        goSegnalaShow();
        
		return "segnala.goGeneraFile";
	}//-------------------------------------------------------------------------
	
	public String loadSegnalazioniDaEsportare(){
		logger.info(SegnalaBean.class + ".loadSegnalazioni");

		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		criteriaSegnala = new SegnalazioniSearchCriteria();
		criteriaSegnala.setEsportata(false);
		dataIn.setCriteriaSegnalazioni(criteriaSegnala);
		lstSegnalazioni = new ArrayList<SetarSegnalazione>();
		List<SetarSegnala1> lstSegnala1DaEsportare = segnalazioniService.getSegnalazioni1(dataIn);
		/*
		 * Recupero della lista delle segnalazioni in corso (= da esportare)  
		 */
		if (lstSegnala1DaEsportare != null && lstSegnala1DaEsportare.size()>0){
			for (SetarSegnala1 ss1 : lstSegnala1DaEsportare){
				criteriaSegnala = new SegnalazioniSearchCriteria();
				criteriaSegnala.setSegnala1Id(ss1.getId());
				dataIn.setCriteriaSegnalazioni(criteriaSegnala);
				List<SetarSegnala2> lstSegnala2Cur = segnalazioniService.getSegnalazioni2(dataIn);
				SetarSegnalazione ss = new SetarSegnalazione();
				ss.setSegnala1(ss1);
				ss.setLstSegnala2(new ArrayList<SetarSegnala2>(lstSegnala2Cur));

				lstSegnalazioni.add(ss);
				
			}
		}
		
		return "segnala.loadSegnalazioni";
	}//-------------------------------------------------------------------------
	
	public String createTXT() {
		logger.info(SegnalaBean.class + ".createTXT");
		/*
		 * Inizio scrittura file in dati_diogene/X000/Tares/AXXX_SUPERFICI_ZZZ.txt
		 */
		String nomeFile = "";
		String suffisso = ".txt";
		String cartella = "Tares";
		/*
		 * recupero il progressivo piu alto per determinare il nome del file
		 */
		DataInDTO dataIn1 = new DataInDTO();
		fillEnte(dataIn1);
		SegnalazioniSearchCriteria criteriaSegnala = new SegnalazioniSearchCriteria();
		dataIn1.setCriteriaSegnalazioni(criteriaSegnala);
		Long maxProgressivo = segnalazioniService.getSegnalazioniMaxProgressivo(dataIn1);
		if (maxProgressivo != null){
			maxProgressivo++;
		}else{
			maxProgressivo = new Long(1);
		}
		String ente = dataIn1.getEnteId();
		nomeFile += ente + "_SUPERFICI_";
		nomeFile += Character.fillUpZeroInFront(maxProgressivo.toString(), 3);
		/*
		 * Recupero il path per scrivere il file: il nome del file deve essere 
		 * costituito da AXXX_SUPERFICI_ZZZ
		 * AXXX: codice amministrativo
		 * 
		 */
		ParameterSearchCriteria paramCriteria = new ParameterSearchCriteria();
		paramCriteria.setKey("dir.files.datiDiogene");
		String pathFile = doGetListaKeyValue(paramCriteria); // C:/Dati_DIOGENE
		pathFile += "/" + ente + "";						 // C:/Dati_DIOGENE/AXXX/
		pathFile += "/" + cartella;						     // C:/Dati_DIOGENE/AXXX/Tares/
//		pathFile += "/" + nomeFile;							 // C:/Dati_DIOGENE/AXXX/Tares/AXXX_SUPERFICI_ZZZ
//		pathFile += suffisso;								 // C:/Dati_DIOGENE/AXXX/Tares/AXXX_SUPERFICI_ZZZ.txt

		logger.info("NOME FILE: " + pathFile);
		
		
		try{
			FileOutputStream out = new FileOutputStream(pathFile + "/" + nomeFile + suffisso);
			StringBuffer sb = new StringBuffer(250);
			
			SetarSegnalazione segnalazioneNew = new SetarSegnalazione();
			segnalazioneNew.setCodiceAmministrativo(ente);
			segnalazioneNew.setDataInserimento(new Date(System.currentTimeMillis()) );
			segnalazioneNew.setNomeFile(nomeFile + suffisso);
			segnalazioneNew.setNomeZip(nomeFile + ".zip");
			segnalazioneNew.setProgressivo(maxProgressivo);
			segnalazioneNew.setScaricato(false);
            dataIn1.setObj(segnalazioneNew);
            SetarSegnalazione segnalazioneCur = segnalazioniService.addSegnalazione(dataIn1);

			for (SetarSegnalazione s : lstSegnalazioni){
				
				if (s.getSel()){
		            
					SetarSegnala1 s1 = s.getSegnala1();
					sb.append("1|" + Character.checkNullString( s.getSegnala1().getCodiceAmministrativo() ) + "|" + Character.checkNullString( s.getSegnala1().getSezioneCensuaria() ) + "|" + Character.checkNullString( s.getSegnala1().getIdentificativoImmobile() ) + "|" + Character.checkNullString( s.getSegnala1().getProgressivo() ) + "|" + Character.checkNullString( s.getSegnala1().getTipoRecord() ) + "|" + Character.checkNullString( s.getSegnala1().getSezioneUrbana() ) + "|" + Character.checkNullString( s.getSegnala1().getFoglio() ) + "|" + Character.checkNullString( s.getSegnala1().getNumero() ) + "|" + Character.checkNullString( s.getSegnala1().getDenominatore() ) + "|" + Character.checkNullString( s.getSegnala1().getSubalterno() ) + "|" + Character.checkNullString( s.getSegnala1().getSuperficieTotale() ) + "|" + Character.checkNullString( s.getSegnala1().getSuperficie() ) + "|" + Character.checkNullString( s.getSegnala1().getNote() ) );
		            sb.append((char)13);
		            sb.append((char)10);

		            ArrayList<SetarSegnala2> alSs2 = s.getLstSegnala2();
		            for (SetarSegnala2 s2 : alSs2){
			            sb.append("2|" + Character.checkNullString( s2.getCodiceAmministrativo() ) + "|" + Character.checkNullString( s2.getSezioneCensuaria() ) + "|" + Character.checkNullString( s2.getIdentificativoImmobile() ) + "|" + Character.checkNullString( s2.getProgressivo() ) + "|" + Character.checkNullString( s2.getTipoRecord() ) + "|" + Character.checkNullString( s2.getAmbiente() ) + "|" + Character.checkNullString( s2.getSuperficieAmbiente() ) + "|" + Character.checkNullString( s2.getAltezza() ) + "|" + Character.checkNullString( s2.getAltezzaMax() ) );
			            sb.append((char)13);
			            sb.append((char)10);
			            
			            s2.setSegnalazioneId(segnalazioneCur.getId());
			            s2.setSegnala1Id(s1.getId());
			            dataIn1.setObj(s2);
			            segnalazioniService.updSegnala2(dataIn1);
		            }			
		            s1.setEsportata(true);
		            s1.setSegnalazioneId(segnalazioneCur.getId());
		            dataIn1.setObj(s1);
		            segnalazioniService.updSegnala1(dataIn1); 

				}
			}
           
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            /*
             * Zippare il file
             */
            CreateZip zipper = new CreateZip();
            File zipFile = new File(pathFile + "/" + nomeFile + ".zip");
            File[] listFiles = new File[1];
            File daZippare = new File(pathFile + "/" + nomeFile + suffisso);
            listFiles[0] = daZippare;
            zipper.createZip(zipFile, listFiles);
            
		}
		catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}

		return "segnala.createTXT";
	}//-------------------------------------------------------------------------
	
	public String goEsportateLst(){
		logger.info(SegnalaBean.class + ".goEsportateLst");
		
		init();
		/*
		 * 
		 */
		DataInDTO dataIn1 = new DataInDTO();
		fillEnte(dataIn1);
		String ente = dataIn1.getEnteId();
		ParameterSearchCriteria paramCriteria = new ParameterSearchCriteria();
		paramCriteria.setKey("dir.files.datiDiogene");
		pathFornitura = doGetListaKeyValue(paramCriteria); // C:/Dati_DIOGENE
		pathFornitura += "/" + ente.toUpperCase() + "";						 // C:/Dati_DIOGENE/AXXX/
		pathFornitura += "/Tares/";
		/*
		 * recupero l'elenco delle segnalazioni esportate
		 */

		SegnalazioniSearchCriteria criteriaSegnala = new SegnalazioniSearchCriteria();
		criteriaSegnala.setEsportata(true);
		dataIn1.setCriteriaSegnalazioni(criteriaSegnala);
		lstSegnalazioniEsportate = new ArrayList<SetarSegnalazione>( segnalazioniService.getSegnalazioni(dataIn1) );
		if (lstSegnalazioniEsportate != null && lstSegnalazioniEsportate.size()>0){
			
		}else{
			lstSegnalazioniEsportate = new ArrayList<SetarSegnalazione>();
		}
//		List<SetarSegnala1> lstSegnala1 = segnalazioniService.getSegnalazioni1(dataIn1);
//		if (lstSegnala1 != null && lstSegnala1.size()>0){
//			SetarSegnalazione segnalazione = null;
//			Hashtable<Long, SetarSegnalazione> htSegnalazioniEsportate = new Hashtable<Long, SetarSegnalazione>();
//			for (SetarSegnala1 segnala1 : lstSegnala1){
//				Long seqSegnalazione = segnala1.getSegnalazioneId();
//				if (htSegnalazioniEsportate.containsKey(seqSegnalazione)){
//					
//				}else{
////					criteriaSegnala = new SegnalazioniSearchCriteria();
////					criteriaSegnala.setId(seqSegnalazione);
////					dataIn1.setCriteriaSegnalazioni(criteriaSegnala);
//					segnalazione = new SetarSegnalazione();
//					segnalazione.setId(seqSegnalazione);
//					dataIn1.setObj(segnalazione);
//					segnalazione = segnalazioniService.getSegnalazione(dataIn1);
//					lstSegnalazioniEsportate.add(segnalazione);
//					htSegnalazioniEsportate.put(seqSegnalazione, segnalazione);
//				}
//			}
//		}
		
		return "segnala.goEsportateLst";
	}//-------------------------------------------------------------------------
	
	public String doValidateStatus(){
		logger.info(SegnalaBean.class + ".doValidateStatus");
		
		if (segnalazioneCur != null){
			segnalazioneCur.setScaricato(true);
			DataInDTO dataIn1 = new DataInDTO();
			fillEnte(dataIn1);
			dataIn1.setObj(segnalazioneCur);
			SetarSegnalazione ss = segnalazioniService.getSegnalazione(dataIn1);
			ss.setScaricato(true);
			dataIn1.setObj(ss);
			segnalazioniService.updSegnalazione(dataIn1);
		}
		
		return "segnala.doValidateStatus";
	}//-------------------------------------------------------------------------
	
	public void doDownload() {
		logger.info(OpenPdfSrv.class + ".doDownload");
		
		BufferedOutputStream  bos = null;
		BufferedInputStream bis = null;
		PrintWriter pw = null;
		//FileOutputStream fos = null;
		ServletOutputStream out = null;
		int DEFAULT_BUFFER_SIZE = 10240;
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
       ExternalContext externalContext = facesContext.getExternalContext();
       HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		try {
			File f = new File(fileName);
			
			if (f.exists()){
			
				bis = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);

				response.setContentType("application/" + getContentType());
				response
						.setHeader("Content-Length", String.valueOf(f.length()));
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + f.getName() + "\"");
				bos = new BufferedOutputStream(response.getOutputStream(),
						DEFAULT_BUFFER_SIZE);

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, length);
				}

				bos.flush();
				
			}else{
				logger.info("Impossibile trovare il file: " + fileName);
			}

			} catch (Throwable t) {
				
				t.printStackTrace();
			} finally {
				close(bos);
				close(bis);
			}

			facesContext.responseComplete();
			
	}//-------------------------------------------------------------------------
	
	public String getFileExt() {
		int dot = fileName.lastIndexOf(".");
		return fileName.substring(dot);
	}//-------------------------------------------------------------------------
	
	protected String getContentType() {

		String ct = "application/download";

		if (getFileExt().equalsIgnoreCase(".zip"))
			ct = "application/zip";

		return ct;
	}//-------------------------------------------------------------------------
	
	 private static void close(Closeable resource) {
	        if (resource != null) {
	          try {
		        resource.close();
		      } catch (IOException e) {
					logger.error(e.getMessage(), e);
		      }
		    }
	}//-------------------------------------------------------------------------

	 public String doSegnalazioneView(){
		 logger.info(SegnalaBean.class + ".doSegnalazioneView");
		 
		 if (segnalazioneCur != null){
				DataInDTO dataIn1 = new DataInDTO();
				fillEnte(dataIn1);
				dataIn1.setObj(segnalazioneCur);
				
				SegnalazioniSearchCriteria segnalaCriteria = new SegnalazioniSearchCriteria();
				segnalaCriteria.setSegnalazioneId(segnalazioneCur.getId());
				dataIn1.setCriteriaSegnalazioni(segnalaCriteria);
				List<SetarSegnala1> alSegnala1 = segnalazioniService.getSegnalazioni1(dataIn1);
				if (alSegnala1 != null && alSegnala1.size()>0){
					lstSegnalazione = new ArrayList<SetarSegnalazione>();
					for (SetarSegnala1 ss1 : alSegnala1){
						SetarSegnalazione ss = segnalazioniService.getSegnalazione(dataIn1);
						
						ss.setSegnala1(ss1);

						segnalaCriteria = new SegnalazioniSearchCriteria();
						segnalaCriteria.setSegnala1Id(ss1.getId());
						dataIn1.setCriteriaSegnalazioni(segnalaCriteria);
						List<SetarSegnala2> alSegnala2 = segnalazioniService.getSegnalazioni2(dataIn1);
						ss.setLstSegnala2(new ArrayList<SetarSegnala2>(alSegnala2));
						
						lstSegnalazione.add(ss);
						
					}
				}
		 }

		 return "segnala.doSegnalazioneView";
	 }//------------------------------------------------------------------------
	 
	 public String doSegnalazioneDel() {
			logger.info(SegnalaBean.class + ".doDelUiu");
			
			if (segnalazioneCur != null){
				
				DataInDTO dataIn = new DataInDTO();
				fillEnte(dataIn);
				SegnalazioniSearchCriteria criteria = new SegnalazioniSearchCriteria();
				criteria.setSegnalazioneId( segnalazioneCur.getId() );
				dataIn.setCriteriaSegnalazioni(criteria);
				
				segnalazioniService.delSegnalazioni1(dataIn);
				
				segnalazioniService.delSegnalazioni2(dataIn);
				
				dataIn.setObj(segnalazioneCur);
				segnalazioniService.delSegnalazioneById(dataIn);
				
			}
			
			goEsportateLst();
			
			return "segnala.doSegnalazioneDel";
		}// ------------------------------------------------------------------------
	
	public SegnalazioniSearchCriteria getCriteriaSegnala() {
		return criteriaSegnala;
	}

	public void setCriteriaSegnala(SegnalazioniSearchCriteria criteriaSegnala) {
		this.criteriaSegnala = criteriaSegnala;
	}

	public ArrayList<SetarSegnalazione> getLstSegnalazioni() {
		return lstSegnalazioni;
	}

	public void setLstSegnalazioni(ArrayList<SetarSegnalazione> lstSegnalazioni) {
		this.lstSegnalazioni = lstSegnalazioni;
	}

	public SetarSegnalazione getSegnalazioneCur() {
		return segnalazioneCur;
	}

	public void setSegnalazioneCur(SetarSegnalazione segnalazioneCur) {
		this.segnalazioneCur = segnalazioneCur;
	}

	public SetarSegnala2 getSegnala2Cur() {
		return segnala2Cur;
	}

	public void setSegnala2Cur(SetarSegnala2 segnala2Cur) {
		this.segnala2Cur = segnala2Cur;
	}


	public SetarSegnala1 getSegnala1Cur() {
		return segnala1Cur;
	}

	public void setSegnala1Cur(SetarSegnala1 segnala1Cur) {
		this.segnala1Cur = segnala1Cur;
	}

	public ArrayList<SetarSegnalazione> getLstSegnalazioniEsportate() {
		return lstSegnalazioniEsportate;
	}

	public void setLstSegnalazioniEsportate(
			ArrayList<SetarSegnalazione> lstSegnalazioniEsportate) {
		this.lstSegnalazioniEsportate = lstSegnalazioniEsportate;
	}

	public ArrayList<SelectItem> getLstAmbientiItems() {
		return lstAmbientiItems;
	}

	public void setLstAmbientiItems(ArrayList<SelectItem> lstAmbientiItems) {
		this.lstAmbientiItems = lstAmbientiItems;
	}

	public String getPathFornitura() {
		return pathFornitura;
	}

	public void setPathFornitura(String pathFornitura) {
		this.pathFornitura = pathFornitura;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public TimeZone getDefaultTimeZone() {
		return defaultTimeZone;
	}

	public void setDefaultTimeZone(TimeZone defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}

	public ArrayList<SetarSegnalazione> getLstSegnalazione() {
		return lstSegnalazione;
	}

	public void setLstSegnalazione(ArrayList<SetarSegnalazione> lstSegnalazione) {
		this.lstSegnalazione = lstSegnalazione;
	}




}
