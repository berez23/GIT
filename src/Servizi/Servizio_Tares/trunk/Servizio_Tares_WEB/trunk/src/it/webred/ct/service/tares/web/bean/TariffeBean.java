package it.webred.ct.service.tares.web.bean;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;



import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import it.doro.util.Character;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.tares.data.access.TariffeService;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.access.dto.TariffeSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarBilancioAnnoCorr;
import it.webred.ct.service.tares.data.model.SetarCoeff;
import it.webred.ct.service.tares.data.model.SetarConsuntivoAnnoPrec;
import it.webred.ct.service.tares.data.model.SetarTariffa;
import it.webred.ct.service.tares.web.bean.TaresBaseBean;
import it.webred.ct.service.tares.web.bean.export.ExportXLSBean;
import it.webred.ct.service.tares.web.tarsu.ClasseTarsu;
import it.webred.ct.service.tares.web.tarsu.SupTotAbit;

import org.apache.log4j.Logger;

public class TariffeBean extends TaresBaseBean {

	protected TariffeService tariffeService = (TariffeService) getEjb("Servizio_Tares", "Servizio_Tares_EJB", "TariffeServiceBean");

	private List<SetarCoeff> lstCoeff = null;
	private List<SetarBilancioAnnoCorr> lstBilancio = null;
	private List<SetarConsuntivoAnnoPrec> lstConsuntivo = null;
	private List<SetarTariffa> lstTariffe = null;
	private List<SetarTariffeElabUteDom> lstTariffeUD = null;
	private List<SetarTariffeElabUteNonDom> lstTariffeUND = null;
	private List<ClasseTarsu> lstClassiTarsu = null;
	private List<SupTotAbit> lstSupTotAbit = null;
	private Boolean offOnApplyCls = true;
	private Boolean offOnApply = true;
	private Long cnt = new Long(0);
	private TariffeSearchCriteria criteriaCoeff = null;
	private String bilancioCF = "0";
	private String bilancioCV = "0";
//	private String occupantiNonResidenti = "0";
	private String abitanti = "";
	private BigDecimal totNumEntita = new BigDecimal(0);
	private BigDecimal totSupTot = new BigDecimal(0);
	private BigDecimal totGettito = new BigDecimal(0);
	private Long statNum = new Long(0);
	private String codiceVoceBilancio = "";
	private String codiceVoceConsuntivo = "";
	private Double quf = 0d;
	private Double quv = 0d;
	private Double supTotAbitPerKaSum = 0d;
	private Double numNucleiFamPerKbSum = 0d;
	
	private Double qupf = 0d;
	private Double qupv = 0d;
	private Double supTotCatPerKcSum = 0d;
	private Double supTotCatPerKdSum = 0d;
	
	
	@PostConstruct
	public void init(){
		lstCoeff = new LinkedList<SetarCoeff>();
		lstBilancio = new LinkedList<SetarBilancioAnnoCorr>();
		lstConsuntivo = new LinkedList<SetarConsuntivoAnnoPrec>();
		criteriaCoeff = new TariffeSearchCriteria();
		lstTariffe = new LinkedList<SetarTariffa>();
		lstTariffeUD = new LinkedList<SetarTariffeElabUteDom>();
		lstTariffeUND = new LinkedList<SetarTariffeElabUteNonDom>();
		lstClassiTarsu = new LinkedList<ClasseTarsu>();
		lstSupTotAbit = new LinkedList<SupTotAbit>();
		offOnApplyCls = true;
		offOnApply = true;
		cnt = new Long(0);
		bilancioCF = "0";
		bilancioCV = "0";
		totGettito = new BigDecimal(0); 
//		occupantiNonResidenti = "0";
		totNumEntita = new BigDecimal(0);
		totSupTot = new BigDecimal(0);
		statNum = new Long(0);
		codiceVoceConsuntivo = "";
		codiceVoceBilancio = "";
		quf = 0d;
		quv = 0d;
		supTotAbitPerKaSum = 0d;
		numNucleiFamPerKbSum = 0d;
		abitanti = "";
	}//-------------------------------------------------------------------------
	
	public String goElaborazioneUD() {
		logger.info(TariffeBean.class + ".goElaborazioneUD");
		
		init();
		
		//addInfoMessage(messageKey) messageKey = resources.properties
		offOnApplyCls = true;
		offOnApply = true;
		/*
		 * Recupero la lista delle voci delle utenze domestiche
		 */
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		criteriaCoeff.setUtenzaTipo("UD");
		
		dataIn.setCriteriaCoeff(criteriaCoeff);
		
		lstTariffe = tariffeService.getTariffe(dataIn);
			
		/*
		 * se lstTariffe non è vuota vuol dire che l'elaborazione è già stata fatta
		 * pertanto mostro i valori utilizzati in passato, altrimenti inserisco 
		 * le voci per le UD senza valori
		 */
		if (lstTariffe != null && lstTariffe.size()>0){
			Iterator<SetarTariffa> itST = lstTariffe.iterator();
			totNumEntita = new BigDecimal(0);
			totSupTot = new BigDecimal(0);
			while(itST.hasNext()){
				SetarTariffa st = itST.next();
				bilancioCF = st.getCfBilancio()!=null?st.getCfBilancio().toString():"0";
				bilancioCV = st.getCvBilancio()!=null?st.getCvBilancio().toString():"0";
//				occupantiNonResidenti = st.getOccupantiNonResidenti()!=null?st.getOccupantiNonResidenti().toString():"0";

//				if (st.getVoce().getId() != 108){
					totNumEntita = totNumEntita.add( st.getNumEntita()!=null?st.getNumEntita(): new BigDecimal(0) ) ;
					totSupTot = totSupTot.add(st.getSupTot()!=null?st.getSupTot(): new BigDecimal(0) );		
					abitanti = st.getAbitanti()!=null?st.getAbitanti():"OVER";
					criteriaCoeff.setGeo( st.getGeo()!= null?st.getGeo():"CENTRO") ;
//				}
			}
		}
		/*
		 * Se i parametri bilancioCF, bilancioCV non sono
		 * ancora valorizzati perche è la prima esecuzione allora li valorizzo
		 * recuperando i valori dal bilancio anno corrente e consuntivo anno precedente
		 * 
		 * Recupero i valori da suggerire come parametri direttamente dal bilancio 
		 * anno corrente (codice UD) e dal consuntivo anno precedente (codice ONR)  
		 */	
		criteriaCoeff.setCodice("UD");
		List<SetarBilancioAnnoCorr> lstBilanci = tariffeService.getBilancio(dataIn);
		if (lstBilanci != null && lstBilanci.size()>0){
			SetarBilancioAnnoCorr sbac = lstBilanci.get(0);
			if (sbac != null ){
				if (bilancioCF.equalsIgnoreCase("0"))
					bilancioCF = sbac.getParteFissa().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
				if (bilancioCV.equalsIgnoreCase("0"))
					bilancioCV = sbac.getParteVaria().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
				
				totGettito = new BigDecimal(bilancioCF).add( new BigDecimal(bilancioCV) ).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			}
		}
//		criteriaCoeff.setCodice("ONR");
//		List<SetarConsuntivoAnnoPrec> lstConsuntivi = tariffeService.getConsuntivo(dataIn);
//		if (lstConsuntivi != null && lstConsuntivi.size()>0){
//			SetarConsuntivoAnnoPrec scap = lstConsuntivi.get(0);
//			if (scap != null ){
//				if (occupantiNonResidenti.equalsIgnoreCase("0"))
//					occupantiNonResidenti = scap.getVal();
//			}
//		}
		/*
		 * Ripulisco il codice perché questo stesso bean verrà utilizzato in altri 
		 * metodi
		 */
		criteriaCoeff.setCodice("");
		
 
		return "tariffe.elaborazioneUD";
	}// ------------------------------------------------------------------------
	
	public String goElaborazioneUND() {
		logger.info(TariffeBean.class + ".goElaborazioneUND");
		
		init();
		
		//addInfoMessage(messageKey) messageKey = resources.properties

		/*
		 * Recupero la lista delle voci delle utenze NON domestiche per Comuni 
		 * sopra i 5000 abitanti ... ovvero tutte le 30 voci (=i comuni sotto 5000
		 * abitanti hanno 21 voci 
		 */
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		if (criteriaCoeff != null){
			criteriaCoeff.setUtenzaTipo("UND");
			
			dataIn.setCriteriaCoeff(criteriaCoeff);
			lstTariffe = tariffeService.getTariffe(dataIn);
			
		}
	
		/*
		 * imposto i totali e gli importi del bilancio fisso e variabile
		 */
		if (lstTariffe != null && lstTariffe.size()>0){
			Iterator<SetarTariffa> itST = lstTariffe.iterator();
			totNumEntita = new BigDecimal(0);
			totSupTot = new BigDecimal(0);
			while(itST.hasNext()){
				SetarTariffa st = itST.next();
				bilancioCF = st.getCfBilancio()!=null?st.getCfBilancio().toString():"0";
				bilancioCV = st.getCvBilancio()!=null?st.getCvBilancio().toString():"0";

				totNumEntita = totNumEntita.add(st.getNumEntita());
				totSupTot = totSupTot.add(st.getSupTot());		
				/*
				 * Se già valorizzata imposto la zona geografica
				 */
				criteriaCoeff.setGeo( st.getGeo()!= null?st.getGeo():"CENTRO") ;
				/*
				 * Se già valorizzata imposto il numero abitanti altrimenti sarà 
				 * OVER.
				 * Nel caso di di UNDER le voci non coinvolte saranno disabilitate
				 */
				abitanti = st.getAbitanti()!=null?st.getAbitanti():"OVER";
			}
		}
		/*
		 *
		 * Se i parametri bilancioCF, bilancioCV  non sono
		 * ancora valorizzati perche è la prima esecuzione allora li valorizzo
		 * recuperando i valori dal bilancio anno corrente 
		 * 
		 * Recupero i valori da suggerire come parametri direttamente dal bilancio 
		 * anno corrente (codice UND)  
		 */			
		if (criteriaCoeff != null){
			criteriaCoeff.setCodice("UND");
			List<SetarBilancioAnnoCorr> lstBilanci = tariffeService.getBilancio(dataIn);
			if (lstBilanci != null && lstBilanci.size()>0){
				SetarBilancioAnnoCorr sbac = lstBilanci.get(0);
				if (sbac != null ){
					if (bilancioCF.equalsIgnoreCase("0"))
						bilancioCF = sbac.getParteFissa().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
					if (bilancioCV.equalsIgnoreCase("0"))
						bilancioCV = sbac.getParteVaria().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
					
					totGettito = new BigDecimal(bilancioCF).add( new BigDecimal(bilancioCV) ).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					
				}
			}
			criteriaCoeff.setCodice("");			
		}
 
		return "tariffe.elaborazioneUND";
	}// ------------------------------------------------------------------------
	
	public String goCalcolaUD() {
		logger.info(TariffeBean.class + ".goCalcolaUD");

		List<SetarTariffeElabUteDom> lstTarUD = new LinkedList<SetarTariffeElabUteDom>();
		
		if (lstTariffe != null && lstTariffe.size()>0){
			/*
			 * recupero i coeff per la zona geografica scelta relativi alle UD
			 */
			doCercaCoeffUD();
			
			quf = 0d;
			quv = 0d;
			supTotAbitPerKaSum = 0d;
			numNucleiFamPerKbSum = 0d;
			totGettito = new BigDecimal(0);
			
			/*
			 * Comincio a popolare il bean del prospetto
			 */
			Iterator<SetarTariffa> itTar = lstTariffe.iterator();
			while(itTar.hasNext()){
				SetarTariffa st = itTar.next();
				/*
				 * Memorizzo il valore per recuperarlo alla prossima elaborazione
				 */
				bilancioCF = Character.checkNullString(bilancioCF).replace(",", ".");
				bilancioCV = Character.checkNullString(bilancioCV).replace(",", ".");
//				occupantiNonResidenti = Character.checkNullString(occupantiNonResidenti).replace(",", ".");
				st.setCfBilancio(new BigDecimal(bilancioCF));
				st.setCvBilancio(new BigDecimal(bilancioCV));
//				st.setOccupantiNonResidenti(new BigDecimal(occupantiNonResidenti));
				st.setAbitanti( Character.checkNullString(abitanti) );
				st.setGeo(criteriaCoeff.getGeo());
				
				DataInDTO dataIn = new DataInDTO();
				fillEnte(dataIn);
				dataIn.setObj(st);
				
				SetarTariffa setarTariffa = tariffeService.updTariffa(dataIn);
				
				SetarTariffeElabUteDom steud = new SetarTariffeElabUteDom();
				

				steud.setCfBilancio(new BigDecimal(bilancioCF) );
				steud.setCvBilancio(new BigDecimal(bilancioCV) );
//				steud.setOccupantiNonResidenti(new BigDecimal(occupantiNonResidenti));
				steud.setAbitanti(Character.checkNullString(abitanti));
				steud.setGeo(criteriaCoeff.getGeo());
				steud.setNumEntita(setarTariffa.getNumEntita());
				steud.setSupTot(setarTariffa.getSupTot());

				double quoteFam = setarTariffa.getNumEntita().doubleValue() / totNumEntita.doubleValue() * 100;
				steud.setQuoteFam( new BigDecimal( quoteFam ));
				double superfMedAbit = 0d;
				if (setarTariffa.getNumEntita().doubleValue()>0d){
					superfMedAbit = setarTariffa.getSupTot().doubleValue()/setarTariffa.getNumEntita().doubleValue();
				}
				steud.setSuperfMedAbit( new BigDecimal(superfMedAbit));
				double coeffKa = 0d;
				double coeffKb = 0d;
				if (lstCoeff != null && lstCoeff.size()>0){
					Iterator<SetarCoeff> itCoeff = lstCoeff.iterator();
					while(itCoeff.hasNext()){
						SetarCoeff coeff = itCoeff.next();
						if (coeff.getCoeff().equalsIgnoreCase("KA")){
							
							if (coeff.getVoce().getId().longValue() == setarTariffa.getVoce().getId().longValue()){
								if (abitanti.equalsIgnoreCase("UNDER") && coeff.getAbit().equalsIgnoreCase(abitanti)){
									/*
									 * <= 5000
									 */
									/*
									 * IL coeff KA è valorizzato solo nella colonna 
									 * AdHoc quindi non c'è bisogno di verificare 
									 * la scelta nella colonna SELEZIONE del coeff
									 */
									coeffKa = coeff.getAdhoc().doubleValue();
									steud.setSelKa("UNDER");
								}else if (abitanti.equalsIgnoreCase("OVER") && coeff.getAbit().equalsIgnoreCase(abitanti) ){
									/*
									 *  > 5000
									 */
									/*
									 * IL coeff KA è valorizzato solo nella colonna 
									 * AdHoc quindi non c'è bisogno di verificare 
									 * la scelta nella colonna SELEZIONE del coeff
									 */
									coeffKa = coeff.getAdhoc().doubleValue();
									steud.setSelKa("OVER");
								}
							}							
						}
						if (coeff.getCoeff().equalsIgnoreCase("KB")){
							/*
							 * Non c'è distinzione per abitanti o per zona geografica
							 */
							if (coeff.getVoce().getId().longValue() == setarTariffa.getVoce().getId().longValue()){
								if (coeff.getSelezione().equalsIgnoreCase("MIN")){
									coeffKb = coeff.getMin().doubleValue();
									steud.setSelKb("MIN");
								}
								else if (coeff.getSelezione().equalsIgnoreCase("MED")){
									coeffKb = coeff.getMed().doubleValue();
									steud.setSelKb("MED");
								}
								else if (coeff.getSelezione().equalsIgnoreCase("MAX")){
									coeffKb = coeff.getMax().doubleValue();
									steud.setSelKb("MAX");
								}
								else if (coeff.getSelezione().equalsIgnoreCase("ADHOC")){
									coeffKb = coeff.getAdhoc().doubleValue();
									steud.setSelKb("ADHOC");
								}
							}
						}
					}
				}
				steud.setCoeffKa( new BigDecimal(coeffKa) );
				steud.setCoeffKb( new BigDecimal(coeffKb) );
								
				double supTotAbitPerKa = setarTariffa.getSupTot().doubleValue() * coeffKa; 
				steud.setSupTotAbitPerKa( new BigDecimal( supTotAbitPerKa) );
				steud.setId(setarTariffa.getId());
				supTotAbitPerKaSum+=supTotAbitPerKa;

				double numNucleiFamPerKb = setarTariffa.getNumEntita().doubleValue() * coeffKb;
				steud.setNumNucleiFamPerKb(new BigDecimal(numNucleiFamPerKb));				
				numNucleiFamPerKbSum+=numNucleiFamPerKb;
				
				steud.setVoce(setarTariffa.getVoce());
				
				lstTarUD.add(steud);
			}

			if (lstTarUD != null && lstTarUD.size()>0){
				Iterator<SetarTariffeElabUteDom> itSTE = lstTarUD.iterator(); 
				while(itSTE.hasNext()){
					SetarTariffeElabUteDom stedu = itSTE.next();
					quf = (stedu.getCfBilancio().doubleValue() * 100) / (supTotAbitPerKaSum * 100);
					stedu.setQuf(new BigDecimal( quf));
					double gettitoQF = quf * stedu.getSupTotAbitPerKa().doubleValue();
					stedu.setGettitoQF(new BigDecimal( gettitoQF));
					double qufPerKa = quf * stedu.getCoeffKa().doubleValue();
					stedu.setQufPerKa(new BigDecimal(qufPerKa) );
					double quotaFissaMedia = stedu.getQufPerKa().doubleValue() * stedu.getSuperfMedAbit().doubleValue();
					stedu.setQuotaFissaMedia(new BigDecimal(quotaFissaMedia));
					
					quv = (stedu.getCvBilancio().doubleValue() * 100) / (numNucleiFamPerKbSum * 100);
					stedu.setQuv(new BigDecimal(quv));
					double gettitoQV = quv * stedu.getNumNucleiFamPerKb().doubleValue();
					stedu.setGettitoQV(new BigDecimal(gettitoQV) );
					double quvPerKb = quv * stedu.getCoeffKb().doubleValue();
					stedu.setQuvPerKb(new BigDecimal(quvPerKb) );
					double quotaVariabilePerPersona = 0d;
					if (stedu.getVoce().getId() == 101)
						quotaVariabilePerPersona = stedu.getQuvPerKb().doubleValue() / 1;
					else if (stedu.getVoce().getId() == 102)
						quotaVariabilePerPersona = stedu.getQuvPerKb().doubleValue() / 2;
					else if (stedu.getVoce().getId() == 103)
						quotaVariabilePerPersona = stedu.getQuvPerKb().doubleValue() / 3;
					else if (stedu.getVoce().getId() == 104)
						quotaVariabilePerPersona = stedu.getQuvPerKb().doubleValue() / 4;
					else if (stedu.getVoce().getId() == 105)
						quotaVariabilePerPersona = stedu.getQuvPerKb().doubleValue() / 5;
					else if (stedu.getVoce().getId() == 106)
						quotaVariabilePerPersona = stedu.getQuvPerKb().doubleValue() / 6;
					else if (stedu.getVoce().getId() == 107){
//						quotaVariabilePerPersona = stedu.getQuvPerKb().doubleValue() / stedu.getOccupantiNonResidenti().doubleValue();						
					}
					stedu.setQuotaVariabilePerPersona(new BigDecimal( quotaVariabilePerPersona) );
					
					double tariffaTot = (stedu.getSuperfMedAbit().doubleValue() * stedu.getQufPerKa().doubleValue()) + stedu.getQuvPerKb().doubleValue();  
					stedu.setTariffaTot(new BigDecimal(tariffaTot) );
				
					double gettitoTot = stedu.getGettitoQF().doubleValue() + stedu.getGettitoQV().doubleValue();
					stedu.setGettitoTot(new BigDecimal(gettitoTot));	
					
					totGettito = totGettito.add(new BigDecimal(gettitoTot) );
					
					double tariffaMed = stedu.getQuotaFissaMedia().doubleValue() + stedu.getQuvPerKb().doubleValue();
					stedu.setTariffaMed(new BigDecimal(tariffaMed) );
					
					lstTariffeUD.add(stedu);
				}
			}
		}

		return "tariffe.calcolaUD";
	}// ------------------------------------------------------------------------
	
	public String goCalcolaUND() {
		logger.info(TariffeBean.class + ".goCalcolaUND");

		List<SetarTariffeElabUteNonDom> lstTarUND = new LinkedList<SetarTariffeElabUteNonDom>();
		if (lstTariffe != null && lstTariffe.size()>0){
			/*
			 * recupero i coeff per la zona geografica scelta relativi alle UND
			 */
			doCercaCoeffUND();
			/*
			 * Comincio a popolare il bean del prospetto
			 */
			Iterator<SetarTariffa> itTar = lstTariffe.iterator();

			qupf = 0d;
			qupv = 0d;
			supTotCatPerKcSum = 0d;
			supTotCatPerKdSum = 0d;
			totGettito = new BigDecimal(0);
			
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			
			while(itTar.hasNext()){
				SetarTariffa st = itTar.next();
				/*
				 * Memorizzo il valore per recuperarlo alla prossima elaborazione
				 */
				bilancioCF = Character.checkNullString(bilancioCF).replace(",", ".");
				bilancioCV = Character.checkNullString(bilancioCV).replace(",", ".");
				st.setCfBilancio(new BigDecimal(bilancioCF));
				st.setCvBilancio(new BigDecimal(bilancioCV));
				st.setAbitanti(abitanti);
				st.setGeo(criteriaCoeff.getGeo());
				dataIn.setObj(st);
				SetarTariffa setarTariffa = tariffeService.updTariffa(dataIn);
				
				SetarTariffeElabUteNonDom steund = new SetarTariffeElabUteNonDom();

				steund.setCfBilancio(new BigDecimal(bilancioCF) );
				steund.setCvBilancio(new BigDecimal(bilancioCV) );
				steund.setGeo(criteriaCoeff.getGeo());
				steund.setNumEntita(setarTariffa.getNumEntita());
				steund.setSupTot(setarTariffa.getSupTot());
				steund.setAbitanti(abitanti);

				double quoteAtt = setarTariffa.getSupTot().doubleValue() / totSupTot.doubleValue() * 100;
				steund.setQuoteAtt( new BigDecimal( quoteAtt ));
				
				double superfMedLoc = 0d;
				if (setarTariffa.getNumEntita().doubleValue()>0d){
					superfMedLoc = setarTariffa.getSupTot().doubleValue()/setarTariffa.getNumEntita().doubleValue();
				}
				steund.setSuperfMedLoc( new BigDecimal(superfMedLoc));
				double coeffKc = 0d;
				double coeffKd = 0d;
				if (lstCoeff != null && lstCoeff.size()>0){
					Iterator<SetarCoeff> itCoeff = lstCoeff.iterator();
					while(itCoeff.hasNext()){
						SetarCoeff coeff = itCoeff.next();
						if (coeff.getCoeff().equalsIgnoreCase("KC")){
							if (coeff.getVoce().getId().longValue() == setarTariffa.getVoce().getId().longValue()){
								if (abitanti.equalsIgnoreCase("UNDER") && coeff.getAbit().equalsIgnoreCase(abitanti)){
									/*
									 * <= 5000 abitanti
									 */
									if (coeff.getSelezione().equalsIgnoreCase("MIN")){
										coeffKc = coeff.getMin().doubleValue();
										steund.setSelKc("MIN");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MED")){
										coeffKc = coeff.getMed().doubleValue();
										steund.setSelKc("MED");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MAX")){
										coeffKc = coeff.getMax().doubleValue();
										steund.setSelKc("MAX");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("ADHOC")){
										coeffKc = coeff.getAdhoc().doubleValue();
										steund.setSelKc("ADHOC");
									}
								}
								/*
								 * Per chiarezza viene implementato anche il ramo 
								 * else ripetendo il codice interno (si sarebbe potuto evitare) 
								 */
								else if (abitanti.equalsIgnoreCase("OVER") && coeff.getAbit().equalsIgnoreCase(abitanti)){
									/*
									 * > 5000 abitanti
									 */
									if (coeff.getSelezione().equalsIgnoreCase("MIN")){
										coeffKc = coeff.getMin().doubleValue();
										steund.setSelKc("MIN");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MED")){
										coeffKc = coeff.getMed().doubleValue();
										steund.setSelKc("MED");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MAX")){
										coeffKc = coeff.getMax().doubleValue();
										steund.setSelKc("MAX");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("ADHOC")){
										coeffKc = coeff.getAdhoc().doubleValue();
										steund.setSelKc("ADHOC");
									}
								}
							}							
						}
						if (coeff.getCoeff().equalsIgnoreCase("KD")){
							if (coeff.getVoce().getId().longValue() == setarTariffa.getVoce().getId().longValue()){
								if (abitanti.equalsIgnoreCase("UNDER") && coeff.getAbit().equalsIgnoreCase(abitanti)){
									/*
									 * <= 5000 abitanti
									 */
									if (coeff.getSelezione().equalsIgnoreCase("MIN")){
										coeffKd = coeff.getMin().doubleValue();
										steund.setSelKd("MIN");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MED")){
										coeffKd = coeff.getMed().doubleValue();
										steund.setSelKd("MED");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MAX")){
										coeffKd = coeff.getMax().doubleValue();
										steund.setSelKd("MAX");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("ADHOC")){
										coeffKd = coeff.getAdhoc().doubleValue();
										steund.setSelKd("ADHOC");
									}
								}
								/*
								 * Per chiarezza viene implementato anche il ramo 
								 * else ripetendo il codice interno (si sarebbe potuto evitare) 
								 */
								else if (abitanti.equalsIgnoreCase("OVER") && coeff.getAbit().equalsIgnoreCase(abitanti)){
									/*
									 * > 5000 abitanti
									 */
									if (coeff.getSelezione().equalsIgnoreCase("MIN")){
										coeffKd = coeff.getMin().doubleValue();
										steund.setSelKd("MIN");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MED")){
										coeffKd = coeff.getMed().doubleValue();
										steund.setSelKd("MED");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("MAX")){
										coeffKd = coeff.getMax().doubleValue();
										steund.setSelKd("MAX");
									}
									else if (coeff.getSelezione().equalsIgnoreCase("ADHOC")){
										coeffKd = coeff.getAdhoc().doubleValue();
										steund.setSelKd("ADHOC");
									}
								}
							}
						}
					}
				}
				steund.setCoeffKc( new BigDecimal(coeffKc) );
				steund.setCoeffKd( new BigDecimal(coeffKd) );
								
				double supTotCatPerKc = setarTariffa.getSupTot().doubleValue() * coeffKc; 
				steund.setSupTotCatPerKc( new BigDecimal( supTotCatPerKc) );
				steund.setId(setarTariffa.getId());
				supTotCatPerKcSum+=supTotCatPerKc;

				double supTotCatPerKd = setarTariffa.getSupTot().doubleValue() * coeffKd; 
				steund.setSupTotCatPerKd( new BigDecimal( supTotCatPerKd) );
				steund.setId(setarTariffa.getId());
				supTotCatPerKdSum+=supTotCatPerKd;
				
				steund.setVoce(setarTariffa.getVoce());
				
				lstTarUND.add(steund);
			}

			if (lstTarUND != null && lstTarUND.size()>0){
				Iterator<SetarTariffeElabUteNonDom> itSTE = lstTarUND.iterator(); 
				while(itSTE.hasNext()){
					SetarTariffeElabUteNonDom steund = itSTE.next();
					qupf = (steund.getCfBilancio().doubleValue() * 100) / (supTotCatPerKcSum * 100);
					steund.setQuf(new BigDecimal( qupf));
					double gettitoQF = qupf * steund.getSupTotCatPerKc().doubleValue();
					steund.setGettitoQF(new BigDecimal( gettitoQF));
					double quotaFissaMedia = steund.getQuf().doubleValue() * steund.getCoeffKc().doubleValue();
					steund.setQufPerKc(new BigDecimal(quotaFissaMedia));
					
					qupv = (steund.getCvBilancio().doubleValue() * 100) / (supTotCatPerKdSum * 100);
					steund.setQuv(new BigDecimal(qupv));
					double gettitoQV = qupv * steund.getSupTotCatPerKd().doubleValue();
					steund.setGettitoQV(new BigDecimal(gettitoQV) );
					double quotaVariabile =  steund.getQuv().doubleValue() * steund.getCoeffKd().doubleValue();
					steund.setQuvPerKd(new BigDecimal(quotaVariabile) );
					
					double tariffaTot = steund.getQufPerKc().doubleValue() + steund.getQuvPerKd().doubleValue();  
					steund.setTariffaTot(new BigDecimal(tariffaTot) );
				
					double gettitoTot = steund.getGettitoQF().doubleValue() + steund.getGettitoQV().doubleValue();
					steund.setGettitoTot(new BigDecimal(gettitoTot));	
					
					totGettito = totGettito.add(new BigDecimal(gettitoTot));
					
					lstTariffeUND.add(steund);
				}
			}
			
			
			
//			/*
//			 * Devo aggiornare il valore SUPCATKD nel consuntivo anno precedente
//			 * per la gestione del bilancio e del consuntivo stesso   
//			 */
//			dataIn = new DataInDTO();
//			fillEnte(dataIn);
//			TariffeSearchCriteria criteriaConsuntivo = new TariffeSearchCriteria();
//			criteriaConsuntivo.setUtenzaTipo("UND");
//			criteriaConsuntivo.setCodice("SUPCATKD");
//			dataIn.setCriteriaCoeff(criteriaConsuntivo);
//			
//			lstConsuntivo = tariffeService.getConsuntivo(dataIn);
//			if (lstConsuntivo != null && lstConsuntivo.size()>0){
//				SetarConsuntivoAnnoPrec sc = lstConsuntivo.get(0);
//				sc.setVal( new BigDecimal(supTotCatPerKdSum).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
//				
//				dataIn.setObj(sc);
//				tariffeService.updConsuntivo(dataIn);
//				
//			}

			
		}

		return "tariffe.calcolaUND";
	}// ------------------------------------------------------------------------
	
	public String goGestioneCoeffUD() {
		logger.info(TariffeBean.class + ".goGestioneCoeffUD");
		
		init();
		
		return "tariffe.gestioneCoeffUD";
	}// ------------------------------------------------------------------------	
	
	public String goGestioneCoeffUND() {
		logger.info(TariffeBean.class + ".goGestioneCoeffUND");
		
		init();
		
		return "tariffe.gestioneCoeffUND";
	}// ------------------------------------------------------------------------	
	
	public String goXlsUD() {
		logger.info(TariffeBean.class + ".goXlsUD");
		/*
		 * Recupero il path per scrivere il file temporaneamente 
		 */
		ParameterSearchCriteria paramCriteria = new ParameterSearchCriteria();
		paramCriteria.setKey("dir.files.dati");
		String pathXLS = doGetListaKeyValue(paramCriteria);
		
		try{
			ExportXLSBean xls = new ExportXLSBean();
			xls.setPathXLS(pathXLS);
			xls.setListaTariUD(lstTariffeUD);
			/*
			 * Faccio iniziare dalla riga 1 non dalla zero perché ci sono 
			 * le intestazioni
			 */
			xls.setRowStart(1);
			xls.resultListTaresExportToXls("TARI_UD");
			
			if (lstTariffeUD != null)
				statNum = new Long(lstTariffeUD.size());
			
			logger.info("Utenze Domestiche esportate: " + lstTariffeUD.size());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return "tariffe.xlsUD";
	}//-------------------------------------------------------------------------
	
	public String goXlsUND() {
		logger.info(TariffeBean.class + ".goXlsUND");
		/*
		 * Recupero il path per scrivere il file temporaneamente 
		 */
		ParameterSearchCriteria paramCriteria = new ParameterSearchCriteria();
		paramCriteria.setKey("dir.files.dati");
		String pathXLS = doGetListaKeyValue(paramCriteria);
		
		try{
			ExportXLSBean xls = new ExportXLSBean();
			xls.setPathXLS(pathXLS);
			xls.setListaTariUND(lstTariffeUND);
			/*
			 * Faccio iniziare dalla riga 1 non dalla zero perché ci sono 
			 * le intestazioni
			 */
			xls.setRowStart(1);
			xls.resultListTaresExportToXls("TARI_UND");
			
			if (lstTariffeUND != null)
				statNum = new Long(lstTariffeUND.size());
			
			logger.info("Utenze NON Domestiche esportate: " + lstTariffeUND.size());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "tariffe.xlsUND";
	}//-------------------------------------------------------------------------
	
	public void doCercaCoeffUD() {
		logger.info(TariffeBean.class + ".doCercaCoeffUD");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		if (criteriaCoeff != null){
			criteriaCoeff.setUtenzaTipo("UD");
			/*
			 * Nel caso KA c'è distinzione per zona geografica e numero abitanti
			 */
			
			/*
			 * Nel caso di coeff KB non c'è distinzione per zone geografiche e numero 
			 * abitanti ma non è necessario resettare i due filtri geo e abit perché
			 * nella abella la colonna GEO è valorizzata con SUD CENTRO NORD
			 * mentre la colonna ABIT = UNDER OVER e la query è eseguita con la LIKE 
			 */
//			if (criteriaCoeff.getCoeff().equals("KB")){
//				criteriaCoeff.setGeo("");
//				criteriaCoeff.setAbit("");
//			}

			dataIn.setCriteriaCoeff(criteriaCoeff);
			
			lstCoeff = tariffeService.getCoeff(dataIn);
			if (lstCoeff != null)
				cnt = new Long(lstCoeff.size());
		}
		
	}// ------------------------------------------------------------------------
	
	public void onBlurUD() {
		logger.info(TariffeBean.class + ".onBlurUD");
		
		totNumEntita = new BigDecimal(0);
		totSupTot = new BigDecimal(0);
		if (lstTariffe != null && lstTariffe.size()>0){
			Iterator<SetarTariffa> itTar = lstTariffe.iterator();
			while(itTar.hasNext()){
				SetarTariffa st = itTar.next();
				if (st.getVoce().getId() != 108){
					//logger.info("NE: " + st.getNumEntita().toString());
					totNumEntita = totNumEntita.add(st.getNumEntita() );
					//logger.info("ST: " + st.getSupTot().toString());
					totSupTot = totSupTot.add(st.getSupTot());
				}
			}
		}
//		logger.info("TOT NE: " + totNumEntita.toString());
//		logger.info("TOT ST: " + totSupTot.toString());
		
	}// ------------------------------------------------------------------------
	
	public void onBlurUND() {
		logger.info(TariffeBean.class + ".onBlurUND");
		
		totNumEntita = new BigDecimal(0);
		totSupTot = new BigDecimal(0);
		if (lstTariffe != null && lstTariffe.size()>0){
			Iterator<SetarTariffa> itTar = lstTariffe.iterator();
			while(itTar.hasNext()){
				SetarTariffa st = itTar.next();
				totNumEntita = totNumEntita.add(st.getNumEntita() );
				totSupTot = totSupTot.add(st.getSupTot());
			}
		}
		
	}// ------------------------------------------------------------------------
	
	public void onChangeAbitantiUND(){
		logger.info(TariffeBean.class + ".onChangeAbitantiUND");
		/*
		 * recupero tutte le voci in ogni caso, ma azzero i valori numEntita e SupTot
		 * delle seguenti voci perché non devono incidere sui totali (vengono impostate a readonly lato client):
		 * tar.voce.id=='2' or tar.voce.id=='3' or tar.voce.id=='10' or tar.voce.id=='15' or 
		 * tar.voce.id=='16' or tar.voce.id=='17' or tar.voce.id=='23' or tar.voce.id=='28' or 
		 * tar.voce.id=='29'
		 * nel caso in cui l'utente ha scelto UNDER  
		 */
		totNumEntita = new BigDecimal(0);
		totSupTot = new BigDecimal(0);
		
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		if (criteriaCoeff != null){
			criteriaCoeff.setUtenzaTipo("UND");
			criteriaCoeff.setGeo("");
			dataIn.setCriteriaCoeff(criteriaCoeff);
			lstTariffe = tariffeService.getTariffe(dataIn);
		}
		if (lstTariffe != null && lstTariffe.size()>0){
			Iterator<SetarTariffa> itTar = lstTariffe.iterator();
			while(itTar.hasNext()){
				SetarTariffa st = itTar.next();
				if (abitanti != null && abitanti.equalsIgnoreCase("UNDER")){
					if (st != null && st.getVoce() != null && st.getVoce().getAbit() != null && st.getVoce().getAbit().equalsIgnoreCase("UNDER")){
						
					}else{
						st.setNumEntita(new BigDecimal(0));
						st.setSupTot(new BigDecimal(0));
					}
				}
				totNumEntita = totNumEntita.add(st.getNumEntita() );
				totSupTot = totSupTot.add(st.getSupTot());
			}
		}

	}//-------------------------------------------------------------------------
	
	public void doAggiornaCoeffUD() {
		logger.info(TariffeBean.class + ".doAggiornaCoeffUD");
		
		if (lstCoeff != null && lstCoeff.size()>0){
			Iterator<SetarCoeff> itCoeff = lstCoeff.iterator();
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			while(itCoeff.hasNext()){
				SetarCoeff c = itCoeff.next();
				BigDecimal zero = new BigDecimal(0.0);
				/*
				 * a.compareTo(b);  // returns (-1 if a < b), (0 if a == b), (1 if a > b)
 					a.signum(); // returns (-1 if a < 0), (0 if a == 0), (1 if a > 0)
				 */
				if (c.getAdhoc() != null){
//					if (c.getAdhoc().compareTo( zero ) == 1 ){
//						
//					}
				}else{
					c.setAdhoc(new BigDecimal(0.0));
				}
				if (c.getAdhoc() != null){
					
				}else{
					c.setSelezione("");
				}
				dataIn.setObj(c);
				tariffeService.updCoeff(dataIn);
			}
		}
		
	}// ------------------------------------------------------------------------
	
	public void doAggiornaCoeffUND() {
		logger.info(TariffeBean.class + ".doAggiornaCoeffUND");
		
		if (lstCoeff != null && lstCoeff.size()>0){
			Iterator<SetarCoeff> itCoeff = lstCoeff.iterator();
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			while(itCoeff.hasNext()){
				SetarCoeff c = itCoeff.next();
				BigDecimal zero = new BigDecimal(0.0);
				/*
				 * a.compareTo(b);  // returns (-1 if a < b), (0 if a == b), (1 if a > b)
 					a.signum(); // returns (-1 if a < 0), (0 if a == 0), (1 if a > 0)
				 */
				if (c.getAdhoc() != null){
//					if (c.getAdhoc().compareTo( zero ) == 1 ){
//						
//					}
				}else{
					c.setAdhoc(new BigDecimal(0.0));
				}
				if (c.getAdhoc() != null){
					
				}else{
					c.setSelezione("");
				}
				dataIn.setObj(c);
				tariffeService.updCoeff(dataIn);
			}
		}
		
	}// ------------------------------------------------------------------------
	
	public void doCercaCoeffUND() {
		logger.info(TariffeBean.class + ".doCercaCoeffUND");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		if (criteriaCoeff != null){
			criteriaCoeff.setUtenzaTipo("UND");

			dataIn.setCriteriaCoeff(criteriaCoeff);
			
			lstCoeff = tariffeService.getCoeff(dataIn);
			if (lstCoeff != null)
				cnt = new Long(lstCoeff.size());
		}
		
	}// ------------------------------------------------------------------------
	
	public String goBilancioAC(){
		logger.info(TariffeBean.class + ".goBilancioAC");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);

		criteriaCoeff = new TariffeSearchCriteria();
		//if (criteriaCoeff != null){

			dataIn.setCriteriaCoeff(criteriaCoeff);
			
			lstBilancio = tariffeService.getBilancio(dataIn);
			if (lstBilancio != null)
				cnt = new Long(lstBilancio.size());
		//}
		/*
		 * Recupero il valore della percentuale utenze domestiche parte variabile 
		 * dal consuntivo anno precedente (UD) e lo valorizzo se ancora non 
		 * è mai stato fatto in bilancio anno corrente  
		 */

		TariffeSearchCriteria tsc = new TariffeSearchCriteria();
		tsc.setCodice("UD");
		dataIn.setCriteriaCoeff(tsc);
		
		lstConsuntivo = tariffeService.getConsuntivo(dataIn);
		if (lstConsuntivo != null && lstBilancio != null){
			/*
			 * imposto il valore in caso non sia ancora valorizzato
			 */
			SetarConsuntivoAnnoPrec scap = lstConsuntivo.get(0);
			Double undperap = 0d;
			for (SetarBilancioAnnoCorr sbac : lstBilancio){
				if (sbac.getCodice().equalsIgnoreCase("UDPERAP")){
					if (sbac.getParteVaria() != null){
						
					}else{
						sbac.setParteVaria(scap.getPer());
						undperap = 100 - scap.getPer().doubleValue();
					}
					if (sbac.getParteFissa()!= null){
						
					}else{
						sbac.setParteFissa(scap.getPer());
						undperap = 100 - scap.getPer().doubleValue();
					}
				}
				if (sbac.getCodice().equalsIgnoreCase("UNDPERAP")){
					if (sbac.getParteVaria() != null){
						
					}else{
						sbac.setParteVaria(new BigDecimal(undperap).setScale(2, BigDecimal.ROUND_HALF_EVEN));							
					}
					if (sbac.getParteFissa() != null){
						
					}else{
						sbac.setParteFissa(new BigDecimal(undperap).setScale(2, BigDecimal.ROUND_HALF_EVEN));							
					}
				}
			}
		}

		
		return "tariffe.bilancioAC";
	}//-------------------------------------------------------------------------
	
	public void onBlurBilAC() {
		logger.info(TariffeBean.class + ".onBlurBilAC");
		
		if (lstBilancio != null && lstBilancio.size()>0){
			Iterator<SetarBilancioAnnoCorr> itBil = lstBilancio.iterator();
			
			while(itBil.hasNext()){
				SetarBilancioAnnoCorr st = itBil.next();
				
				if (codiceVoceBilancio.equalsIgnoreCase("CV")){
					/*
					 * La chiamata arriva dopo la modifica del totale dei costi 
					 * vari parte variabile
					 */
					if (st.getCodice().equalsIgnoreCase("CV")){
						Double pvCostiVariEur = st.getParteVaria().doubleValue();
						Double pfCostiVariEur = st.getParteFissa().doubleValue();
						Double totCostiVariEur = pvCostiVariEur + pfCostiVariEur;
						st.setTotale(new BigDecimal( totCostiVariEur) );
					}
				}else if (codiceVoceBilancio.equalsIgnoreCase("CSL") || codiceVoceBilancio.equalsIgnoreCase("CARC") || codiceVoceBilancio.equalsIgnoreCase("CGG") || codiceVoceBilancio.equalsIgnoreCase("CCD") || codiceVoceBilancio.equalsIgnoreCase("AC") || codiceVoceBilancio.equalsIgnoreCase("CK") ){
					/*
					 * Chiamata proveniente dalla parte fissa dei costi
					 */
					if (st.getCodice().equalsIgnoreCase("CSL") || st.getCodice().equalsIgnoreCase("CARC") || st.getCodice().equalsIgnoreCase("CGG") || st.getCodice().equalsIgnoreCase("CCD") || st.getCodice().equalsIgnoreCase("AC") || st.getCodice().equalsIgnoreCase("CK")){
						 st.setParteFissa(st.getTotale());
					}
				}else if (codiceVoceBilancio.equalsIgnoreCase("CRT") || codiceVoceBilancio.equalsIgnoreCase("CTS") || codiceVoceBilancio.equalsIgnoreCase("CRD") || codiceVoceBilancio.equalsIgnoreCase("CTR") ){
					/*
					 * Chiamata proveniente dalla parte variabile dei costi
					 */
					if (st.getCodice().equalsIgnoreCase("CRT") || st.getCodice().equalsIgnoreCase("CTS") || st.getCodice().equalsIgnoreCase("CRD") || st.getCodice().equalsIgnoreCase("CTR") ){
						st.setParteVaria(st.getTotale());
					}
				}else if (codiceVoceBilancio.equalsIgnoreCase("COPERTURA") || codiceVoceBilancio.equalsIgnoreCase("APR") || codiceVoceBilancio.equalsIgnoreCase("MEAP") || codiceVoceBilancio.equalsIgnoreCase("UDPERAP")){
					/*
					 * dobbiamo aggiornare comunque tutto
					 */
				}
			}
			/*
			 * Ricalcola il tutto
			 */
			aggiornaBilAC();
			
		}

	}// ------------------------------------------------------------------------
	
	public void aggiornaBilAC() {
		logger.info(TariffeBean.class + ".aggiornaBilAC");
		if (lstBilancio != null && lstBilancio.size()>0){
			Iterator<SetarBilancioAnnoCorr> itBil = lstBilancio.iterator();
			Double totParteFissaEur = 0d;
			Double totParteVariaEur = 0d;
			Double totCostiEur = 0d;
			Double totParteFissaPer = 0d;
			Double totParteVariaPer = 0d;
			Double totCoperturaPer = 0d;
			Double totPrevisioneEntrataEur = 0d;
			Double previsioneEntrataParteFissaEur = 0d;
			Double previsioneEntrataParteVariaEur = 0d;
			Double agevolazioniParteFissaEur = 0d;
			Double agevolazioniParteVariaEur = 0d;
			Double totAgevolazioniEur = 0d;
			Double maggioriEntrateAnnoPrecedenteParteFissaEur = 0d;
			Double maggioriEntrateAnnoPrecedenteParteVariaEur = 0d;
			Double totMaggioriEntrateAnnoPrecedenteEur = 0d;
			Double totEntrataTeoricaEur = 0d;
			Double entrataTeoricaParteFissaEur = 0d;
			Double entrataTeoricaParteVariaEur = 0d;

			Double totUteDomAnnoPrecedentePer = 0d;
			Double uteDomParteFissaAnnoPrecedentePer = 0d;
			Double uteDomParteVariaAnnoPrecedentePer = 0d;
			Double totUteDomEur = 0d;
			Double uteDomParteFissaEur = 0d;
			Double uteDomParteVariaEur = 0d;
			Double totUteDomAnnoCorrentePer = 0d;
			Double uteDomParteFissaAnnoCorrentePer = 0d;
			Double uteDomParteVariaAnnoCorrentePer = 0d;
			
			Double totUteNonDomAnnoPrecedentePer = 0d;
			Double uteNonDomParteFissaAnnoPrecedentePer = 0d;
			Double uteNonDomParteVariaAnnoPrecedentePer = 0d;
			Double totUteNonDomEur = 0d;
			Double uteNonDomParteFissaEur = 0d;
			Double uteNonDomParteVariaEur = 0d;
			Double totUteNonDomAnnoCorrentePer = 0d;
			Double uteNonDomParteFissaAnnoCorrentePer = 0d;
			Double uteNonDomParteVariaAnnoCorrentePer = 0d;
			
			/*
			 * Primo giro calcolo i totali
			 * Secondo giro valorizzo le voci corrispondenti ai totali stessi
			 */
			
			while(itBil.hasNext()){
				SetarBilancioAnnoCorr st = itBil.next();
				/*
				 * Totali parte fissa
				 */
				if (st.getCodice().equalsIgnoreCase("CV") || st.getCodice().equalsIgnoreCase("CSL") || st.getCodice().equalsIgnoreCase("CARC") || st.getCodice().equalsIgnoreCase("CGG") || st.getCodice().equalsIgnoreCase("CCD") || st.getCodice().equalsIgnoreCase("AC") || st.getCodice().equalsIgnoreCase("CK")){
					totParteFissaEur += st.getParteFissa().doubleValue();
				}
				/*
				 * Totali parte variabile
				 */
				if (st.getCodice().equalsIgnoreCase("CV") || st.getCodice().equalsIgnoreCase("CRT") || st.getCodice().equalsIgnoreCase("CTS") || st.getCodice().equalsIgnoreCase("CRD") || st.getCodice().equalsIgnoreCase("CTR") ){
					totParteVariaEur += st.getParteVaria().doubleValue();
				}
				/*
				 * Percentuale di copertura dei costi che serve per calcoalre 
				 * le entrate teoriche
				 */
				if (st.getCodice().equals("COPERTURA"))
					totCoperturaPer = st.getTotale().doubleValue();
				/*
				 * Agevolazioni previste da regolamento
				 */
				if (st.getCodice().equalsIgnoreCase("APR"))
					totAgevolazioniEur = st.getTotale().doubleValue();
				/*
				 * Maggiori Entrate anno precedente
				 */
				if (st.getCodice().equalsIgnoreCase("MEAP"))
					totMaggioriEntrateAnnoPrecedenteEur = st.getTotale().doubleValue();
				/*
				 * Percentuali Utenze Domestiche Anno Precedente
				 */
				if (st.getCodice().equalsIgnoreCase("UDPERAP")){
					uteDomParteFissaAnnoPrecedentePer = st.getParteFissa().doubleValue();
					uteDomParteVariaAnnoPrecedentePer = st.getParteVaria().doubleValue();
					/*
					 * Percentuali Utenze NON Domestiche Anno Precedente
					 */
					uteNonDomParteFissaAnnoPrecedentePer = 100 - st.getParteFissa().doubleValue();
					uteNonDomParteVariaAnnoPrecedentePer = 100 - st.getParteVaria().doubleValue();
				}
//				/*
//				 * Percentuali Utenze NON Domestiche Anno Precedente
//				 */
//				if (st.getCodice().equalsIgnoreCase("UNDPERAP")){
//					uteNonDomParteFissaAnnoPrecedentePer = st.getParteFissa().doubleValue();
//					uteNonDomParteVariaAnnoPrecedentePer = st.getParteVaria().doubleValue();
//				}

			}
			/*
			 * Calcolo percentuali Costi parte fissa e varibile
			 */
			totCostiEur = totParteFissaEur + totParteVariaEur;
			totParteFissaPer = totParteFissaEur * 100 / totCostiEur;
			totParteVariaPer = totParteVariaEur * 100 / totCostiEur;
			/*
			 * Calcolo copertura dei costi e loro ripartizione in fissi e variabili
			 * in base alle percentuali sopra calcolate
			 */
			totPrevisioneEntrataEur = totCostiEur * totCoperturaPer / 100;
			previsioneEntrataParteFissaEur = totPrevisioneEntrataEur * totParteFissaPer / 100;
			previsioneEntrataParteVariaEur = totPrevisioneEntrataEur * totParteVariaPer / 100;
			/*
			 * Calcolo delle agevolazioni parte fissa e variabile
			 */
			agevolazioniParteFissaEur = totAgevolazioniEur * totParteFissaPer / 100;
			agevolazioniParteVariaEur = totAgevolazioniEur * totParteVariaPer / 100;
			/*
			 * Calcolo delle maggiori entrate anno precedente
			 */
			maggioriEntrateAnnoPrecedenteParteFissaEur = totMaggioriEntrateAnnoPrecedenteEur * totParteFissaPer / 100;
			maggioriEntrateAnnoPrecedenteParteVariaEur = totMaggioriEntrateAnnoPrecedenteEur * totParteVariaPer / 100;
			/*
			 * Calcolo Entrata Teorica
			 */
			entrataTeoricaParteFissaEur = previsioneEntrataParteFissaEur + agevolazioniParteFissaEur + maggioriEntrateAnnoPrecedenteParteFissaEur;
			entrataTeoricaParteVariaEur = previsioneEntrataParteVariaEur + agevolazioniParteVariaEur + maggioriEntrateAnnoPrecedenteParteVariaEur;
			totEntrataTeoricaEur = entrataTeoricaParteFissaEur + entrataTeoricaParteVariaEur;
			/*
			 * Calcolo Utenze Domestiche
			 */
			uteDomParteFissaEur = entrataTeoricaParteFissaEur * uteDomParteFissaAnnoPrecedentePer / 100;
			uteDomParteVariaEur = entrataTeoricaParteVariaEur * uteDomParteVariaAnnoPrecedentePer / 100;
			totUteDomEur = uteDomParteFissaEur + uteDomParteVariaEur;
			
			totUteDomAnnoPrecedentePer = totUteDomEur * 100 / totEntrataTeoricaEur;
			
			totUteDomAnnoCorrentePer = 100d;
			uteDomParteFissaAnnoCorrentePer = uteDomParteFissaEur * 100 / totUteDomEur;
			uteDomParteVariaAnnoCorrentePer = uteDomParteVariaEur * 100 / totUteDomEur;
			/*
			 * Calcolo utenze NON domestiche
			 */
			uteNonDomParteFissaEur = entrataTeoricaParteFissaEur * uteNonDomParteFissaAnnoPrecedentePer / 100;
			uteNonDomParteVariaEur = entrataTeoricaParteVariaEur * uteNonDomParteVariaAnnoPrecedentePer / 100;
			totUteNonDomEur = uteNonDomParteFissaEur + uteNonDomParteVariaEur;
			
			totUteNonDomAnnoPrecedentePer = totUteNonDomEur * 100 / totEntrataTeoricaEur;
			
			totUteNonDomAnnoCorrentePer = 100d;
			uteNonDomParteFissaAnnoCorrentePer = uteNonDomParteFissaEur * 100 / totUteNonDomEur;
			uteNonDomParteVariaAnnoCorrentePer = uteNonDomParteVariaEur * 100 / totUteNonDomEur;
			
			for (int i=0; i<lstBilancio.size(); i++){
				SetarBilancioAnnoCorr st = lstBilancio.get(i);
				if (st != null){
					if (st.getCodice().equalsIgnoreCase("SOMPER")){
						st.setParteFissa(new BigDecimal(totParteFissaPer) );
						st.setParteVaria(new BigDecimal(totParteVariaPer) );
						st.setTotale(new BigDecimal(100) );
					}
					if (st.getCodice().equalsIgnoreCase("SOMEUR")){
						st.setParteFissa(new BigDecimal(totParteFissaEur) );
						st.setParteVaria(new BigDecimal(totParteVariaEur) );
						st.setTotale(new BigDecimal(totCostiEur) );
					}
					if (st.getCodice().equalsIgnoreCase("PE")){
						st.setParteFissa(new BigDecimal(previsioneEntrataParteFissaEur) );
						st.setParteVaria(new BigDecimal(previsioneEntrataParteVariaEur) );
						st.setTotale(new BigDecimal(totPrevisioneEntrataEur) );
					}
					if (st.getCodice().equalsIgnoreCase("APR")){
						st.setParteFissa(new BigDecimal(agevolazioniParteFissaEur) );
						st.setParteVaria(new BigDecimal(agevolazioniParteVariaEur) );
						st.setTotale(new BigDecimal(totAgevolazioniEur) );
					}
					if (st.getCodice().equalsIgnoreCase("MEAP")){
						st.setParteFissa(new BigDecimal(maggioriEntrateAnnoPrecedenteParteFissaEur) );
						st.setParteVaria(new BigDecimal(maggioriEntrateAnnoPrecedenteParteVariaEur) );
						st.setTotale(new BigDecimal(totMaggioriEntrateAnnoPrecedenteEur) );
					}
					if (st.getCodice().equalsIgnoreCase("ET")){
						st.setParteFissa(new BigDecimal(entrataTeoricaParteFissaEur) );
						st.setParteVaria(new BigDecimal(entrataTeoricaParteVariaEur) );
						st.setTotale(new BigDecimal(totEntrataTeoricaEur) );
					}
					if (st.getCodice().equalsIgnoreCase("UD")){
						st.setParteFissa(new BigDecimal(uteDomParteFissaEur) );
						st.setParteVaria(new BigDecimal(uteDomParteVariaEur) );
						st.setTotale(new BigDecimal(totUteDomEur) );
					}
					if (st.getCodice().equalsIgnoreCase("UDPERAP")){
						st.setParteFissa(new BigDecimal(uteDomParteFissaAnnoPrecedentePer) );
						st.setParteVaria(new BigDecimal(uteDomParteVariaAnnoPrecedentePer) );
						st.setTotale(new BigDecimal(totUteDomAnnoPrecedentePer) );
					}
					if (st.getCodice().equalsIgnoreCase("UDPERAC")){
						st.setParteFissa(new BigDecimal(uteDomParteFissaAnnoCorrentePer) );
						st.setParteVaria(new BigDecimal(uteDomParteVariaAnnoCorrentePer) );
						st.setTotale(new BigDecimal(totUteDomAnnoCorrentePer) );
					}
					if (st.getCodice().equalsIgnoreCase("UND")){
						st.setParteFissa(new BigDecimal(uteNonDomParteFissaEur) );
						st.setParteVaria(new BigDecimal(uteNonDomParteVariaEur) );
						st.setTotale(new BigDecimal(totUteNonDomEur) );
					}
					if (st.getCodice().equalsIgnoreCase("UNDPERAP")){
						st.setParteFissa(new BigDecimal(uteNonDomParteFissaAnnoPrecedentePer) );
						st.setParteVaria(new BigDecimal(uteNonDomParteVariaAnnoPrecedentePer) );
						st.setTotale(new BigDecimal(totUteNonDomAnnoPrecedentePer) );
					}
					if (st.getCodice().equalsIgnoreCase("UNDPERAC")){
						st.setParteFissa(new BigDecimal(uteNonDomParteFissaAnnoCorrentePer) );
						st.setParteVaria(new BigDecimal(uteNonDomParteVariaAnnoCorrentePer) );
						st.setTotale(new BigDecimal(totUteNonDomAnnoCorrentePer) );
					}
				}
			}
		}
		
	}//-------------------------------------------------------------------------
	
	public void doSalvaBilAC(){
		logger.info(TariffeBean.class + ".doSalvaBilAC");

		if (lstBilancio != null && lstBilancio.size()>0){
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			for (int index=0; index<lstBilancio.size(); index++){
				SetarBilancioAnnoCorr setarBilancio = lstBilancio.get(index);
				dataIn.setObj(setarBilancio);
				tariffeService.updBilancio(dataIn);
			}
			
			super.addInfoMessage("operazione.success");
			
		}

	}//-------------------------------------------------------------------------
	
	public String goConsuntivoAP(){
		logger.info(TariffeBean.class + ".goConsuntivoAP");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		criteriaCoeff = new TariffeSearchCriteria();
		//if (criteriaCoeff != null){

			dataIn.setCriteriaCoeff(criteriaCoeff);
			
			lstConsuntivo = tariffeService.getConsuntivo(dataIn);
			if (lstConsuntivo != null)
				cnt = new Long(lstConsuntivo.size());
		//}
		
		return "tariffe.consuntivoAP";
	}//-------------------------------------------------------------------------

	public void onBlurConAP() {
		logger.info(TariffeBean.class + ".onBlurConAP");

		if (lstConsuntivo != null && lstConsuntivo.size()>0){
			
			aggiornaConAP();
		}
		
	}//-------------------------------------------------------------------------
	
	public void aggiornaConAP() {
		logger.info(TariffeBean.class + ".aggiornaConAP");

		if (lstConsuntivo != null && lstConsuntivo.size()>0){
			Iterator<SetarConsuntivoAnnoPrec> itCon = lstConsuntivo.iterator();
			
			Double totRsu = 0d;
			Double areePubblicheDaDetrarreKg = 0d;
			Double areePubblicheDaDetrarrePer = 0d;
			Double caricoUtenze = 0d;
			Double undKg = 0d;
			Double undPer = 0d;
			Double udKg = 0d;
			Double udPer = 0d;
			
			
//			Double indiceCorrezioneKgUND = 0d;
//			String occupantiNonResidenti = "";
//			String areaGeo = "";
//			String abitanti5 = "";
//			String ultimoAnnoApplicaTassa = "";
//			Double aliquotaEca = 0d;
//			Double addizionaleProvinciale = 0d; 
			
			while(itCon.hasNext()){
				SetarConsuntivoAnnoPrec sc = itCon.next();

				if (sc.getCodice().equalsIgnoreCase("TOTRSU"))
					totRsu = new BigDecimal( sc.getVal() ).doubleValue();
				if (sc.getCodice().equalsIgnoreCase("DAPDD")){
					areePubblicheDaDetrarreKg = new BigDecimal( sc.getVal() ).doubleValue(); 
					areePubblicheDaDetrarrePer = sc.getPer().doubleValue();
				}
				if (sc.getCodice().equalsIgnoreCase("UND")){
					undKg = new BigDecimal( sc.getVal() ).doubleValue();
				}
//				if (sc.getCodice().equalsIgnoreCase("ICKUND")){
//					indiceCorrezioneKgUND = sc.getPer().doubleValue();
//				}
//				if (sc.getCodice().equalsIgnoreCase("ONR")){
//					occupantiNonResidenti = sc.getVal();
//				}	
//				if (sc.getCodice().equalsIgnoreCase("AG")){
//					areaGeo = sc.getVal();
//				}
//				if (sc.getCodice().equalsIgnoreCase("ABI5")){
//					abitanti5 = sc.getVal();
//				}
//				if (sc.getCodice().equalsIgnoreCase("UATT")){
//					ultimoAnnoApplicaTassa = sc.getVal();
//				}
//				if (sc.getCodice().equalsIgnoreCase("ALIECA")){
//					aliquotaEca = sc.getPer().doubleValue(); 
//				}
//				if (sc.getCodice().equalsIgnoreCase("ADDPRO")){
//					addizionaleProvinciale = sc.getPer().doubleValue() ; 
//				}
				
			}
			
			areePubblicheDaDetrarreKg = totRsu * areePubblicheDaDetrarrePer / 100;
			caricoUtenze = totRsu - areePubblicheDaDetrarreKg;				
			//undEur = supTotCatPerKdSum * indiceCorrezioneKgUND>0?indiceCorrezioneKgUND:1;
			undPer = undKg / caricoUtenze * 100;
			udKg = caricoUtenze - undKg;
			udPer = udKg / caricoUtenze * 100;
					
			for (int index=0; index<lstConsuntivo.size(); index++){
				SetarConsuntivoAnnoPrec setarConsuntivo = lstConsuntivo.get(index);
				if (setarConsuntivo.getCodice().equalsIgnoreCase("TOTRSU")){
					setarConsuntivo.setVal( new BigDecimal( totRsu ).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
					setarConsuntivo.setPer( new BigDecimal( 0 ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
				}
				if (setarConsuntivo.getCodice().equalsIgnoreCase("DAPDD")){
					setarConsuntivo.setVal( new BigDecimal( areePubblicheDaDetrarreKg ).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
					setarConsuntivo.setPer( new BigDecimal( areePubblicheDaDetrarrePer ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
				}
				if (setarConsuntivo.getCodice().equalsIgnoreCase("ACU")){
					setarConsuntivo.setVal( new BigDecimal( caricoUtenze ).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
					setarConsuntivo.setPer( new BigDecimal( 0 ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
				}
				if (setarConsuntivo.getCodice().equalsIgnoreCase("UND")){
					setarConsuntivo.setVal( new BigDecimal( undKg ).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
					setarConsuntivo.setPer( new BigDecimal( undPer ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
				}
				if (setarConsuntivo.getCodice().equalsIgnoreCase("UD")){
					setarConsuntivo.setVal( new BigDecimal( udKg ).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
					setarConsuntivo.setPer( new BigDecimal( udPer ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
				}
//				if (setarConsuntivo.getCodice().equalsIgnoreCase("ICKUND")){
//					setarConsuntivo.setVal("");
//					setarConsuntivo.setPer( new BigDecimal( indiceCorrezioneKgUND ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
//				}
//				if (setarConsuntivo.getCodice().equalsIgnoreCase("ONR")){
//					setarConsuntivo.setVal( occupantiNonResidenti );
//					setarConsuntivo.setPer( new BigDecimal( 0 ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
//				}
//				if (setarConsuntivo.getCodice().equalsIgnoreCase("AG")){
//					setarConsuntivo.setVal( areaGeo );
//					setarConsuntivo.setPer( new BigDecimal( 0 ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
//				}
//				if (setarConsuntivo.getCodice().equalsIgnoreCase("ABI5")){
//					setarConsuntivo.setVal( abitanti5 );
//					setarConsuntivo.setPer( new BigDecimal( 0 ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
//				}
//				if (setarConsuntivo.getCodice().equalsIgnoreCase("UATT")){
//					setarConsuntivo.setVal( ultimoAnnoApplicaTassa );
//					setarConsuntivo.setPer( new BigDecimal( 0 ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
//				}
//				if (setarConsuntivo.getCodice().equalsIgnoreCase("ALIECA")){
//					setarConsuntivo.setVal("");
//					setarConsuntivo.setPer( new BigDecimal( aliquotaEca ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
//				}
//				if (setarConsuntivo.getCodice().equalsIgnoreCase("ADDPRO")){
//					setarConsuntivo.setVal("");
//					setarConsuntivo.setPer( new BigDecimal( addizionaleProvinciale ).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
//				}
				
			}
		}
		
	}//-------------------------------------------------------------------------

	public void doSalvaConAP(){
		logger.info(TariffeBean.class + ".doSalvaConAP");

		if (lstConsuntivo != null && lstConsuntivo.size()>0){
			
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			
			BigDecimal undPer = null;
			BigDecimal udPer = null;
			
			for (int index=0; index<lstConsuntivo.size(); index++){
				SetarConsuntivoAnnoPrec setarConsuntivo = lstConsuntivo.get(index);
				dataIn.setObj(setarConsuntivo);
				tariffeService.updConsuntivo(dataIn);
				
				/*
				 * Alla conferma si devono aggiornare le percentuali (SETAR_BILANCIO_ANNO_CORR.PARTE_VARIA)
				 * UDPERAP e UNDPERAP; non è necessario aggiornare tutto quello che ne consegue nella 
				 * tabella del bilancio perché l'utente deve comunque passare dal pulsante CALCOLA
				 * per avere le elaborazioni
				 */

				if (setarConsuntivo.getCodice().equalsIgnoreCase("UND")){
					undPer=setarConsuntivo.getPer();
				}
				if (setarConsuntivo.getCodice().equalsIgnoreCase("UD")){
					udPer=setarConsuntivo.getPer();
				}
			}
			/*
			 * recupero tutte le voci di bilancio cosi con un solo loop aggiorno
			 * UDPERAP e UNDPERAP e alla fine ricalcolo tutto e salvo
			 */
			criteriaCoeff = new TariffeSearchCriteria();
			dataIn.setCriteriaCoeff(criteriaCoeff);
			lstBilancio = tariffeService.getBilancio(dataIn);
			if (lstBilancio != null && lstBilancio.size()>0){
				Iterator<SetarBilancioAnnoCorr> itBil = lstBilancio.iterator();
				while(itBil.hasNext()){
					SetarBilancioAnnoCorr sbac = itBil.next();
					if (sbac.getCodice().equalsIgnoreCase("UDPERAP")){
						sbac.setParteVaria(udPer);
					}
					if (sbac.getCodice().equalsIgnoreCase("UNDPERAP")){
						sbac.setParteVaria(undPer);
					} 
				}
			}
			
			aggiornaBilAC();
			
			doSalvaBilAC();
			
			super.addInfoMessage("operazione.success");
			
		}

	}//-------------------------------------------------------------------------
	
	public void doResetPFUD() {
		logger.info(TariffeBean.class + ".doResetPFUD");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		TariffeSearchCriteria criteri = new TariffeSearchCriteria(); 
		criteri.setUtenzaTipo("UD");
		criteri.setCodice("UD");
		dataIn.setCriteriaCoeff(criteri);

		List<SetarBilancioAnnoCorr> lstBAC = tariffeService.getBilancio(dataIn);
		if (lstBAC != null && lstBAC.size()>0){
			SetarBilancioAnnoCorr sbac = lstBAC.get(0);
			bilancioCF = sbac.getParteFissa().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
			totGettito = sbac.getParteFissa().setScale(2, BigDecimal.ROUND_HALF_EVEN).add( new BigDecimal(bilancioCV) );
		}else{
			logger.info(TariffeBean.class + ".doResetPFUD: Costi Parte Fissa Bilancio Anno Corrente NON ESISTENTE");
		}
		
	}// ------------------------------------------------------------------------
	
	public void doResetPVUD() {
		logger.info(TariffeBean.class + ".doResetPVUD");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		TariffeSearchCriteria criteri = new TariffeSearchCriteria(); 
		criteri.setUtenzaTipo("UD");
		criteri.setCodice("UD");
		dataIn.setCriteriaCoeff(criteri);

		List<SetarBilancioAnnoCorr> lstBAC = tariffeService.getBilancio(dataIn);
		if (lstBAC != null && lstBAC.size()>0){
			SetarBilancioAnnoCorr sbac = lstBAC.get(0);
			bilancioCV = sbac.getParteVaria().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
			totGettito = sbac.getParteVaria().setScale(2, BigDecimal.ROUND_HALF_EVEN).add( new BigDecimal(bilancioCF) );
		}else{
			logger.info(TariffeBean.class + ".doResetPVUD: Costi Parte Variabile Bilancio Anno Corrente NON ESISTENTE");
		}
		
	}// ------------------------------------------------------------------------
	
	public void doResetPFUND() {
		logger.info(TariffeBean.class + ".doResetPFUND");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		TariffeSearchCriteria criteri = new TariffeSearchCriteria(); 
		criteri.setUtenzaTipo("UND");
		criteri.setCodice("UND");
		dataIn.setCriteriaCoeff(criteri);

		List<SetarBilancioAnnoCorr> lstBAC = tariffeService.getBilancio(dataIn);
		if (lstBAC != null && lstBAC.size()>0){
			SetarBilancioAnnoCorr sbac = lstBAC.get(0);
			bilancioCF = sbac.getParteFissa().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
			totGettito = sbac.getParteFissa().setScale(2, BigDecimal.ROUND_HALF_EVEN).add( new BigDecimal(bilancioCV) );
		}else{
			logger.info(TariffeBean.class + ".doResetPFUND: Costi Parte Fissa Bilancio Anno Corrente NON ESISTENTE");
		}
		
	}// ------------------------------------------------------------------------
	
	public void doResetPVUND() {
		logger.info(TariffeBean.class + ".doResetPVUND");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		TariffeSearchCriteria criteri = new TariffeSearchCriteria(); 
		criteri.setUtenzaTipo("UND");
		criteri.setCodice("UND");
		dataIn.setCriteriaCoeff(criteri);

		List<SetarBilancioAnnoCorr> lstBAC = tariffeService.getBilancio(dataIn);
		if (lstBAC != null && lstBAC.size()>0){
			SetarBilancioAnnoCorr sbac = lstBAC.get(0);
			bilancioCV = sbac.getParteVaria().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
			totGettito = sbac.getParteVaria().setScale(2, BigDecimal.ROUND_HALF_EVEN).add( new BigDecimal(bilancioCF) );
		}else{
			logger.info(TariffeBean.class + ".doResetPVUND: Costi Parte Variabile Bilancio Anno Corrente NON ESISTENTE");
		}
		
	}// ------------------------------------------------------------------------
	
	public void doValorizzaNuclei() {
		logger.info(TariffeBean.class + ".doValorizzaNuclei");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		TariffeSearchCriteria criteri = new TariffeSearchCriteria(); 
		criteri.setUtenzaTipo("UD");
		criteri.setCodice("UD");
		dataIn.setCriteriaCoeff(criteri);

		List<Object> lstObjs = tariffeService.getDistribuzioneComponenti(dataIn);
		totNumEntita = new BigDecimal(0);
		Hashtable<BigDecimal, BigDecimal> htTot = new Hashtable<BigDecimal, BigDecimal>();
		if (lstObjs != null && lstObjs.size()>0 && lstTariffe != null && lstTariffe.size()>0){
			BigDecimal totNuclei = new BigDecimal(0);
			
			for (int i=0; i<lstObjs.size(); i++){
				Object[] aryObj = (Object[])lstObjs.get(i);
				BigDecimal numComp = aryObj[0]!=null?(BigDecimal)aryObj[0]:new BigDecimal(0);
				BigDecimal numNuclei = aryObj[1]!=null?(BigDecimal)aryObj[1]:new BigDecimal(0);
				if (numComp.intValue() >= 6){
					/*
					 * sommo nuclei con numero componenti superiore o uguale a 6
					 */
					totNuclei = totNuclei.add(numNuclei);
					numComp = new BigDecimal(6);
					htTot.put(numComp, totNuclei);
				}else{
					htTot.put(numComp, numNuclei);
				}

				SetarTariffa st = null;
				for (int index=0; index<lstTariffe.size(); index++){
					st = (SetarTariffa)lstTariffe.get(index);
					String voce = st.getVoce().getDescrizione();
					if (voce.contains(numComp.toString())){
						if (numComp.intValue() >= 6){
							st.setNumEntita(totNuclei.setScale(0, BigDecimal.ROUND_HALF_EVEN));
						}else{
							st.setNumEntita(numNuclei.setScale(0, BigDecimal.ROUND_HALF_EVEN));
						}
					}
					//logger.info("n. comp: " + numComp + " tot. nuclei: " + totNuclei + " num. nuclei: " + numNuclei);
				}
			}
			/*
			 * calcolo il totale dei nuclei familiari 
			 */
			if (htTot !=  null && htTot.size()>0){
				Enumeration<BigDecimal> en = htTot.elements();
				while(en.hasMoreElements()){
					BigDecimal nnf = en.nextElement();
					//logger.info("numero nuclie familiari: " + nnf);
					totNumEntita = totNumEntita.add(nnf);
				}
			}
		}else{
			logger.info(TariffeBean.class + ".doValorizzaNuclei: Elenco Distribuzione Numero Componenti Famiglie VUOTO");
		}
		
	}// ------------------------------------------------------------------------
	
	public void doValorizzaSupTotAbit() {
		logger.info(TariffeBean.class + ".doValorizzaSupTotAbit");
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		offOnApply = true;
		lstSupTotAbit = new LinkedList<SupTotAbit>();
		
		TariffeSearchCriteria criteri = new TariffeSearchCriteria(); 
		criteri.setUtenzaTipo("UD");
		criteri.setCodice("UD");
		dataIn.setCriteriaCoeff(criteri);

		List<Object> lstObjs = tariffeService.getClassiTarsu(dataIn);
		lstClassiTarsu = new LinkedList<ClasseTarsu>();
		if (lstObjs != null && lstObjs.size()>0){
			Iterator<Object> itObjs = lstObjs.iterator();
			while(itObjs.hasNext()){
				Object obj = itObjs.next();
				ClasseTarsu ct = new ClasseTarsu();
				ct.setDescrizione((String)obj);
				lstClassiTarsu.add(ct);
			}
		}else{
			logger.info(TariffeBean.class + ".doValorizzaSupTotAbit: Elenco Classi TARSU VUOTO");
		}
		
	}// ------------------------------------------------------------------------
	
	public void onOffApplyCls(){
		logger.info(TariffeBean.class + ".onOffApplyCls");
		
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);

		/*
		 * Se almeno una checkbox viene selezionata allora abilito il pulsante APPLICA
		 */
		Boolean sel = false;
		if (lstClassiTarsu != null && lstClassiTarsu.size()>0){
			ClasseTarsu ct = null;
			Iterator<ClasseTarsu> itCt = lstClassiTarsu.iterator();
			while(itCt.hasNext()){
				ct = itCt.next();
				if (ct.getSel())
					sel = true;
			}
		}
		offOnApplyCls = !sel;
	}//-------------------------------------------------------------------------
	
	public void doValorizzaSupTotAbitApplyCls(){
		logger.info(TariffeBean.class + ".doValorizzaSupTotAbitApplyCls");
		
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		/*
		 * Compone il criterio per eseguirela query:
		 * and (ogg.des_cls_rsu = 'ABITAZIONE LOCALI' or ogg.des_cls_rsu = 'ABITAZIONE BOX')
		 */
		String where = "";
		/*
		 * Se il pulsante è abilitato significa che qualche classe tarsu è stata
		 * selezionata per cui:
		 */
		if (!offOnApplyCls){
			where = "and (";
			for (int index=0; index<lstClassiTarsu.size(); index++){
				ClasseTarsu ct = lstClassiTarsu.get(index);
				if (ct.getSel())
					where += "ogg.des_cls_rsu = '" + ct.getDescrizione().replaceAll("'", "''") + "' or "; 
			}
			where = where.substring(0, where.length()-4);
			where += ") ";
		}
		logger.info(TariffeBean.class + ".doValorizzaSupTotAbitApplyCls: " + where);
		/*
		 * esecuzione query
		 */
		TariffeSearchCriteria criteri = new TariffeSearchCriteria(); 
		criteri.setCodice(where);
		dataIn.setCriteriaCoeff(criteri);
		List<Object> lstObjs = tariffeService.getDistribuzioneSupTotTarsu(dataIn);
		lstSupTotAbit = new LinkedList<SupTotAbit>();
		if (lstObjs != null && lstObjs.size()>0){
			SupTotAbit sta = null;
			BigDecimal totNuclei = new BigDecimal(0);
			BigDecimal totSupTarsu = new BigDecimal(0);
			BigDecimal medSupTarsu = new BigDecimal(0);
			for (int i=0; i<lstObjs.size(); i++){
				sta = new SupTotAbit();
				Object[] aryObj = (Object[])lstObjs.get(i);
				BigDecimal numComp = aryObj[0]!=null?(BigDecimal)aryObj[0]:new BigDecimal(0);
				BigDecimal numNuclei = aryObj[1]!=null?(BigDecimal)aryObj[1]:new BigDecimal(0);
				BigDecimal supTarsuTot = aryObj[2]!=null?(BigDecimal)aryObj[2]:new BigDecimal(0);
				BigDecimal supTarsuMed = new BigDecimal(0);
				
				if (numComp.intValue() >= 6){
					/*
					 * sommo nuclei e sup tot poi alla fine faccio media
					 */
					totNuclei = totNuclei.add(numNuclei);
					totSupTarsu  = totSupTarsu.add(supTarsuTot);
				}else{
					sta.setNumComp(numComp.setScale(0).toString());
					sta.setNumNuclei(numNuclei.setScale(0).toString());
					sta.setSupTarsuTot(supTarsuTot.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
					supTarsuMed = supTarsuTot.divide( numNuclei, MathContext.DECIMAL64 );
					sta.setSupTarsuMed(supTarsuMed.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

					lstSupTotAbit.add(sta);
				}
			}
			if (totNuclei.compareTo(new BigDecimal(0)) == 1){ // totNuclei > 0
				medSupTarsu = totSupTarsu.divide(totNuclei, MathContext.DECIMAL64);
				sta = new SupTotAbit();
				sta.setNumComp("6 e oltre");
				sta.setNumNuclei(totNuclei.setScale(0).toString());
				sta.setSupTarsuTot(totSupTarsu.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
				sta.setSupTarsuMed(medSupTarsu.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
				lstSupTotAbit.add(sta);
			}
		}
		if (lstSupTotAbit.size()>0)
			offOnApply = false;
		
	}//-------------------------------------------------------------------------
	
	public void doValorizzaSupTotAbitApply(){
		logger.info(TariffeBean.class + ".doValorizzaSupTotAbitApplyCls");
		
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		
		if (lstTariffe != null && lstTariffe.size()>0 && lstSupTotAbit != null && lstSupTotAbit.size()>0){
			SetarTariffa st = null;
			SupTotAbit sta = null;
			for (int index=0; index<lstTariffe.size(); index++){
				st = (SetarTariffa)lstTariffe.get(index);
				String voce = st.getVoce().getDescrizione();
				boolean trovato = false;
				for (int indice=0; indice<lstSupTotAbit.size(); indice++){
					sta = (SupTotAbit)lstSupTotAbit.get(indice);
					String nc = sta.getNumComp().substring(0, 1);
					if (voce.contains(nc)){
						st.setSupTot(st.getNumEntita().multiply(new BigDecimal(sta.getSupTarsuMed()), MathContext.DECIMAL64));
						trovato = true;
					}
				}
				if (!trovato)
					st.setSupTot(new BigDecimal(0));
			}
		}
		
		offOnApplyCls = true;
		offOnApply = true;
		
	}//-------------------------------------------------------------------------
	
	public TariffeSearchCriteria getCriteriaCoeff() {
		return criteriaCoeff;
	}

	public void setCriteriaCoeff(TariffeSearchCriteria criteriaCoeff) {
		this.criteriaCoeff = criteriaCoeff;
	}

	public List<SetarCoeff> getLstCoeff() {
		return lstCoeff;
	}

	public void setLstCoeff(List<SetarCoeff> lstCoeff) {
		this.lstCoeff = lstCoeff;
	}

	public Long getCnt() {
		return cnt;
	}

	public void setCnt(Long cnt) {
		this.cnt = cnt;
	}

	public String getBilancioCF() {
		return bilancioCF;
	}

	public void setBilancioCF(String bilancioCF) {
		this.bilancioCF = bilancioCF;
	}

	public String getBilancioCV() {
		return bilancioCV;
	}

	public void setBilancioCV(String bilancioCV) {
		this.bilancioCV = bilancioCV;
	}

	public List<SetarTariffa> getLstTariffe() {
		return lstTariffe;
	}

	public void setLstTariffe(List<SetarTariffa> lstTariffe) {
		this.lstTariffe = lstTariffe;
	}

	public BigDecimal getTotNumEntita() {
		return totNumEntita;
	}

	public void setTotNumEntita(BigDecimal totNumEntita) {
		this.totNumEntita = totNumEntita;
	}

	public BigDecimal getTotSupTot() {
		return totSupTot;
	}

	public void setTotSupTot(BigDecimal totSupTot) {
		this.totSupTot = totSupTot;
	}

	public List<SetarTariffeElabUteDom> getLstTariffeUD() {
		return lstTariffeUD;
	}

	public void setLstTariffeUD(List<SetarTariffeElabUteDom> lstTariffeUD) {
		this.lstTariffeUD = lstTariffeUD;
	}

	public List<SetarTariffeElabUteNonDom> getLstTariffeUND() {
		return lstTariffeUND;
	}

	public void setLstTariffeUND(List<SetarTariffeElabUteNonDom> lstTariffeUND) {
		this.lstTariffeUND = lstTariffeUND;
	}

//	public String getOccupantiNonResidenti() {
//		return occupantiNonResidenti;
//	}
//
//	public void setOccupantiNonResidenti(String occupantiNonResidenti) {
//		this.occupantiNonResidenti = occupantiNonResidenti;
//	}

	public Long getStatNum() {
		return statNum;
	}

	public void setStatNum(Long statNum) {
		this.statNum = statNum;
	}

	public List<SetarBilancioAnnoCorr> getLstBilancio() {
		return lstBilancio;
	}

	public void setLstBilancio(List<SetarBilancioAnnoCorr> lstBilancio) {
		this.lstBilancio = lstBilancio;
	}

	public List<SetarConsuntivoAnnoPrec> getLstConsuntivo() {
		return lstConsuntivo;
	}

	public void setLstConsuntivo(List<SetarConsuntivoAnnoPrec> lstConsuntivo) {
		this.lstConsuntivo = lstConsuntivo;
	}

	public String getCodiceVoceBilancio() {
		return codiceVoceBilancio;
	}

	public void setCodiceVoceBilancio(String codiceVoceBilancio) {
		this.codiceVoceBilancio = codiceVoceBilancio;
	}

	public String getCodiceVoceConsuntivo() {
		return codiceVoceConsuntivo;
	}

	public void setCodiceVoceConsuntivo(String codiceVoceConsuntivo) {
		this.codiceVoceConsuntivo = codiceVoceConsuntivo;
	}

	public String getAbitanti() {
		return abitanti;
	}

	public void setAbitanti(String abitanti) {
		this.abitanti = abitanti;
	}

	public BigDecimal getTotGettito() {
		return totGettito;
	}

	public void setTotGettito(BigDecimal totGettito) {
		this.totGettito = totGettito;
	}

	public Double getQuf() {
		return quf;
	}

	public void setQuf(Double quf) {
		this.quf = quf;
	}

	public Double getQuv() {
		return quv;
	}

	public void setQuv(Double quv) {
		this.quv = quv;
	}

	public Double getSupTotAbitPerKaSum() {
		return supTotAbitPerKaSum;
	}

	public void setSupTotAbitPerKaSum(Double supTotAbitPerKaSum) {
		this.supTotAbitPerKaSum = supTotAbitPerKaSum;
	}

	public Double getNumNucleiFamPerKbSum() {
		return numNucleiFamPerKbSum;
	}

	public void setNumNucleiFamPerKbSum(Double numNucleiFamPerKbSum) {
		this.numNucleiFamPerKbSum = numNucleiFamPerKbSum;
	}

	public Double getQupf() {
		return qupf;
	}

	public void setQupf(Double qupf) {
		this.qupf = qupf;
	}

	public Double getQupv() {
		return qupv;
	}

	public void setQupv(Double qupv) {
		this.qupv = qupv;
	}

	public Double getSupTotCatPerKcSum() {
		return supTotCatPerKcSum;
	}

	public void setSupTotCatPerKcSum(Double supTotCatPerKcSum) {
		this.supTotCatPerKcSum = supTotCatPerKcSum;
	}

	public Double getSupTotCatPerKdSum() {
		return supTotCatPerKdSum;
	}

	public void setSupTotCatPerKdSum(Double supTotCatPerKdSum) {
		this.supTotCatPerKdSum = supTotCatPerKdSum;
	}

	public List<ClasseTarsu> getLstClassiTarsu() {
		return lstClassiTarsu;
	}

	public void setLstClassiTarsu(List<ClasseTarsu> lstClassiTarsu) {
		this.lstClassiTarsu = lstClassiTarsu;
	}

	public Boolean getOffOnApplyCls() {
		return offOnApplyCls;
	}

	public void setOffOnApplyCls(Boolean offOnApply) {
		this.offOnApplyCls = offOnApply;
	}

	public List<SupTotAbit> getLstSupTotAbit() {
		return lstSupTotAbit;
	}

	public void setLstSupTotAbit(List<SupTotAbit> lstSupTotAbit) {
		this.lstSupTotAbit = lstSupTotAbit;
	}

	public Boolean getOffOnApply() {
		return offOnApply;
	}

	public void setOffOnApply(Boolean offOnApply) {
		this.offOnApply = offOnApply;
	}



}
