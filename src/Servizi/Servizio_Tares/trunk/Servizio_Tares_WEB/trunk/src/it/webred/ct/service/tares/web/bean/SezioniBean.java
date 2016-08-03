package it.webred.ct.service.tares.web.bean;

import java.io.Serializable;



import java.sql.Timestamp;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import it.doro.util.TimeControl;
import it.webred.ct.service.tares.data.access.SezioniService;
import it.webred.ct.service.tares.data.access.StatisticheService;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.access.dto.DiagnosticheSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarDia;
import it.webred.ct.service.tares.web.bean.TaresBaseBean;

import org.apache.log4j.Logger;

public class SezioniBean extends TaresBaseBean implements Serializable{

	private static final long serialVersionUID = -7653940663106363976L;

	protected SezioniService sezioniService = (SezioniService) getEjb("Servizio_Tares", "Servizio_Tares_EJB", "SezioniServiceBean");
	
//	@EJB(mappedName = "Servizio_Tares/StatisticheServiceBean/remote")
//	protected StatisticheService statisticheService;

	protected StatisticheService statisticheService = (StatisticheService) getEjb("Servizio_Tares", "Servizio_Tares_EJB", "StatisticheServiceBean");

	private static Logger logger = Logger.getLogger("tares.log");

	private String nome = "";
	private Date lastUpdateLandRegister = null;
	private Date lastUpdateSetar = null;
	private String testo = "";
//	private Boolean attiva = false;
	private String testoColor = "black";
	
	private List<SetarDia> lstDiagnostiche = null;
	private DiagnosticheSearchCriteria criteriaDiag = null;
	
	
	public void init(){
		lstDiagnostiche = new LinkedList<SetarDia>();
		criteriaDiag = new DiagnosticheSearchCriteria();
		lastUpdateLandRegister = null;
		lastUpdateSetar = null;
	}//-------------------------------------------------------------------------

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

//	public void doCercaSezioni() {
//		DataInDTO dataIn = new DataInDTO();
//		fillEnte(dataIn);
//		dataIn.setNome(nome);
//		
//		List<CataSezioni> lstCataSezioni = sezioniService.getSezioniByName(dataIn);
//		
//	}// ------------------------------------------------------------------------
	
	public String doInfoUpdate(){
		logger.info(SezioniBean.class + ".doInfoUpdate");
		String esito = "sezioniBean.doInfoUpdate";
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		lastUpdateLandRegister = null;
		lastUpdateSetar = null;
		
		String strDataUltimoAggiornamento = "";
		try{
			Object[] objsDate  = sezioniService.getUpdateDate(dataIn);
			/*
			 * Considerare la possibilita che non torni valore oppure torni un valora 
			 * a casso 
			 */
			Timestamp dataAggiornamentoCatasto = (Timestamp)objsDate[1];
			lastUpdateLandRegister = new Date( dataAggiornamentoCatasto.getTime() );
			//lastUpdateLandRegister = TimeControl.parsItalyDate( dtAggCat );
			/*
			 * Recupero delle informazioni di diagnostica ed in particolare della 
			 * data ultimo aggiornamento da confrontare con la data attuale di aggiornamento 
			 * del catasto
			 */
			dataIn.setCriteriaDiag(new DiagnosticheSearchCriteria());
			/*
			 * la query restituisce una lista ordinata per il campo descrizione 
			 * quindi sarà meglio cercare questa etichetta "DATA ULTIMO AGGIORNAMENTO"
			 */
			
			lstDiagnostiche = statisticheService.getDiagnostiche(dataIn);
			if (lstDiagnostiche != null && lstDiagnostiche.size()>0){
				Iterator<SetarDia> itDia = lstDiagnostiche.iterator();
				while(itDia.hasNext()){
					SetarDia sd = itDia.next();
					/*
					 * in SETAR_DIA:
					 * 1: DATA ULTIMA ELABORAZIONE TARES
					 * 2: DATA ULTIMO AGGIORNAMENTO CATASTO
					 */
					if (sd.getId() == 1){
						strDataUltimoAggiornamento = sd.getValore();
					}
				}
			}
			if (strDataUltimoAggiornamento != null && !strDataUltimoAggiornamento.trim().equalsIgnoreCase("")){
				lastUpdateSetar = TimeControl.fromFormatStringToDate(strDataUltimoAggiornamento, "00:00"); 				
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info(e.getMessage() );
		}finally{
			if (lastUpdateLandRegister != null && lastUpdateSetar != null){
				/*
				 * vado a confrontare la data di elaborazione ultima con quella 
				 * di aggiornamento catasto
				 */
				if (lastUpdateLandRegister.after(lastUpdateSetar)){
					/*
					 * Il catasto è stato aggiornato dopo l'elaborazione TARES pertanto 
					 * deve essere eseguita una nuova elaborazione
					 */
					testo = "Eseguire nuova Elaborazione TARES!";
					testoColor = "red";
					//attiva = true;
				}else{
					/*
					 * Non è necessario effettuare l'elaborazione TARES
					 */
					testo = "Data Ultima Elaborazione TARES successiva ad Aggiornamento Catasto - NON è necessario eseguire Elaborazione TARES!";
					testoColor = "black";
					//attiva = false;
				}
			}else{
				/*
				 * Non si possono effettuare confronti quindi attivo il pulsante
				 */
				//attiva = false;
				if (lastUpdateLandRegister == null)
					testo += "Data Ultimo Aggiornamento Catasto non comprensibile!";
				if (lastUpdateSetar == null){
					testo += "Data Ultima Elaborazione non comprensibile!";
					//attiva = true;
				}
				testo = "Impossibile effettuare il confronto tra Data Ultimo Aggiornamento Catasto o Data Ultima Elaborazione!";
				testoColor = "red";
			}
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String doEseguiElaborazioni(){
		logger.info(SezioniBean.class + ".doEseguiElaborazioni");
		String esito = "sezioniBean.doEseguiElaborazioni";
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		/*
		 * Popolare SETAR_ELAB
		 */
		ArrayList<Object> lstExecElab = sezioniService.execElab(dataIn);
		logger.info("Stored procedure: SETAR_TEMP001_TEMP002");
		logger.info("Stored procedure: SETAR_SET_TEMP001_SUP_TARSU");
		logger.info("Stored procedure: SETAR_SET_TEMP001_SUP_VANI");

		ArrayList<Object> lstExecElab2 = sezioniService.execElab2(dataIn);
		logger.info("Stored procedure: SETAR_SET_TEMP001_SUP_X_CAT1");
		logger.info("Stored procedure: SETAR_SET_TEMP001_SUP_X_CAT2");
		/*
		 * Popolare SETAR_STAT
		 */
		ArrayList<Object> lstExecStat = sezioniService.execStat(dataIn);
		logger.info("Stored procedure: SETAR_TEMP003");
		/*
		 * Popolare SETAR_DIA
		 */
		ArrayList<Object> lstExecDia = sezioniService.execDia(dataIn);
		logger.info("Stored procedure: SETAR_SET_DIA");
		/*
		 * Trasferimento da tabelle TEMP a tabelle SETAR ed eliminazione delle TEMP
		 */
		ArrayList<Object> lstExecTrasf = sezioniService.execTrasf(dataIn);
		logger.info("Stored procedure: SETAR_FROM_TEMP_TO_SETAR");
		/*
		 * Aggiorno le info in prima pagina
		 */
//		doInfoUpdate();
		logger.info("Aggiornamento INFO della home page");
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public Date getLastUpdateLandRegister() {
		doInfoUpdate();
		return lastUpdateLandRegister;
	}

	public void setLastUpdateLandRegister(Date lastUpdateLandRegister) {
		this.lastUpdateLandRegister = lastUpdateLandRegister;
	}

//	public Boolean getAttiva() {
//		return attiva;
//	}
//
//	public void setAttiva(Boolean attiva) {
//		this.attiva = attiva;
//	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getTestoColor() {
		return testoColor;
	}

	public void setTestoColor(String testoColor) {
		this.testoColor = testoColor;
	}

	public Date getLastUpdateSetar() {
		return lastUpdateSetar;
	}

	public void setLastUpdateSetar(Date lastUpdateSetar) {
		this.lastUpdateSetar = lastUpdateSetar;
	}

	public List<SetarDia> getLstDiagnostiche() {
		return lstDiagnostiche;
	}

	public void setLstDiagnostiche(List<SetarDia> lstDiagnostiche) {
		this.lstDiagnostiche = lstDiagnostiche;
	}

}
