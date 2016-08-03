package it.webred.ct.service.segnalazioniqualificate.web.bean.gestione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import it.webred.ct.data.access.basic.segnalazionequalificata.*;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.*;
import it.webred.ct.data.model.segnalazionequalificata.*;
import it.webred.ct.service.segnalazioniqualificate.web.bean.*;
import it.webred.ct.service.segnalazioniqualificate.web.bean.util.CommonDataBean;
import it.webred.ct.service.segnalazioniqualificate.web.bean.util.UserBean;

public class GestionePraticaBean extends SegnalazioniQualificateBaseBean {
	private static final long serialVersionUID = 1L;

	private long idPratica;
	private PraticaSegnalazioneDTO dto;
	private HashMap<String,List<String>> selAmbiti;
	
	public final static String MODE_INSERIMENTO = "I";
	public final static String MODE_MODIFICA = "M";
	public final static String DESC_MODE_INSERIMENTO = "Inserimento";
	public final static String DESC_MODE_MODIFICA = "Modifica";
	
	public final static String SOGG_GIURIDICO = "G";
	public final static String SOGG_FISICO = "P";
	
	public final static String S_ISTRUTTORIA_COMUNALE_IN_CORSO = "0";
	public final static String S_SEGNALAZIONE_IN_CORSO_AdE = "1";
	public final static String S_ITER_CONCLUSO = "2";
	
	private String mode;
	
	@PostConstruct
	public void initService() {
		resetData();
	}
	
	public void resetData(){
		dto = new PraticaSegnalazioneDTO();
		dto.setEnteId(this.getCurrentEnte());
		SOfPratica pratica = new SOfPratica();
		pratica.setOperatoreId(super.getUser().getUsername());
		pratica.setAccTipoSogg(SOGG_FISICO);
		dto.setPratica(pratica);
		selAmbiti = new HashMap<String,List<String>>();
	}
	
	public void resetEsitoAdE(){
		dto.getPratica().setAdeDataFine(null);
		dto.getPratica().setAdeEsito(null);
		dto.getPratica().setFlgLiqQuote(null);
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	private void caricaPraticaById(){
		SegnalazioniDataIn dataIn = this.getInitRicercaParams();
		dataIn.getRicercaPratica().setIdPra(idPratica);
		dto = segnalazioneService.getPraticaById(dataIn);
		dto.setDescAccComuNasc(this.getDescEnte(dto.getPratica().getAccComuNasc()));
	}
	
	public String goSalvaChiusura() {
		dto.getPratica().setAccDataFine(super.getCurrentDate());
		if(dto.getPratica().getAccAzione().equalsIgnoreCase("SEGNALAZIONE ADE")){
			dto.getPratica().setAccStato(S_SEGNALAZIONE_IN_CORSO_AdE);
			dto.getPratica().setAdeStato("ATTESA DI IMMISSIONE");
		}else
			dto.getPratica().setAccStato(S_ITER_CONCLUSO);
		
		modificaPratica();
		return goDetail();
	}
	
	public String goGestionePratica(){
		getLogger().info("CARICAMENTO PRATICA DA MODIFICARE: "+ idPratica);
		caricaPraticaById();
		popolaSelAmbiti();
		return "segnalazioni.modifica";
	}
	
	public String goGestioneInfoAdE(){
		getLogger().info("CARICAMENTO PRATICA (ADE): "+ idPratica);
		caricaPraticaById();
		return "segnalazioni.ade.modifica";
	}
	
	private void popolaSelAmbiti(){
		dto.getAmbitiAccertamento();
		CommonDataBean common = (CommonDataBean)this.getBeanReference("commonDataBean");
		List<AmbitiAccertamentoSpecieDTO> ambitiSpecie = common.getAmbitiAccertamento();
		for(AmbitiAccertamentoSpecieDTO aas : ambitiSpecie){
			String specie = aas.getSpecie();
			List<String> selAmbitiSpecie = new ArrayList<String>();
			for(SelectItem ambito : aas.getAmbiti()){
				if(dto.getAmbitiAccertamento().contains(ambito.getValue().toString())){
					getLogger().info("Ambito Pre-Selezionato: " + ambito.getValue().toString());
					selAmbitiSpecie.add(ambito.getValue().toString());
					}
			}
			selAmbiti.put(specie, selAmbitiSpecie);
		}
	}
	
	public String goChiusuraPratica(){
		SegnalazioniDataIn dataIn = this.getInitRicercaParams();
		dataIn.getRicercaPratica().setIdPra(idPratica);
		getLogger().info("CARICAMENTO PRATICA IN CHIUSURA: "+ idPratica);
		dto = segnalazioneService.getPraticaById(dataIn);
		dto.setDescAccComuNasc(this.getDescEnte(dto.getPratica().getAccComuNasc()));
		return "segnalazioni.chiusura";
	}
	
	public String goSalvaModifiche() {
		dto.setAmbitiAccertamento(new ArrayList<String>());
		for( List<String> es : selAmbiti.values())
			dto.getAmbitiAccertamento().addAll(es);
		
		modificaPratica();
		return goDetail();
	}
	
	public String goSalvaModificheAde() {
		if(dto.getPratica().getAdeStato().equalsIgnoreCase("CONCLUSA"))
			dto.getPratica().setAccStato(S_ITER_CONCLUSO);
		modificaPratica();
		return goDetail();
	}
	
	public String goDetail(){
		DetailBean det = (DetailBean)this.getBeanReference("praticaDetailBean");
		det.initService();
		det.setIdPratica(idPratica);
		return det.goDetail();
	}
	
	public String goNew() {
		initService();
		return "segnalazioni.apertura";
	}
	
	public String goApriNew(){
		String msg = "pratica.crea";
		try
		{
			inserisciPratica();
			super.addInfoMessage(msg);
			getLogger().info("Pratica Inserita con ID:["+idPratica+"]");
			return goDetail();
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
			return "";
		}		
	}
		
	
	public void clearAccComuneNascita(){
		dto.getPratica().setAccComuNasc(null);
	}
	
	public void clearSoggAcc(){
			dto.getPratica().setAccCodiFisc(null);
			dto.getPratica().setAccCognome(null);
			dto.getPratica().setAccNome(null);
			dto.getPratica().setAccComuNasc(null);
			dto.getPratica().setAccDataNasc(null);
			dto.getPratica().setAccDenominazione(null);
			dto.getPratica().setAccCodiPiva(null);
	}
	
	/*public String salva() {
		String esito = "success";		
		
		if (mode.equalsIgnoreCase(MODE_INSERIMENTO)) 
			inserisciPratica();
		else
			modificaPratica();

		return esito;
	}*/
	
	private void inserisciPratica() {
		
		dto.getPratica().setAccStato(S_ISTRUTTORIA_COMUNALE_IN_CORSO);
		getLogger().info("Inserimento nuova pratica di segnalazione...");
		getLogger().info("'"+dto.getPratica().getAccComuNasc()+"'");
		this.fillEnte(dto);
		idPratica =  segnalazioneService.creaPraticaSegnalazione(dto);
		
	}
	
	private void modificaPratica() {
		String msg = "pratica.modifica";
	
		try{
			getLogger().info("Modfica pratica di segnalazione...");
			idPratica = dto.getPratica().getId();
			this.getLogger().info("Modifica pratica in corso: "+ idPratica );
			this.fillEnte(dto);
			segnalazioneService.modificaPraticaSegnalazione(dto);
			
			super.addInfoMessage(msg);
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			t.printStackTrace();
		}
	}

	public PraticaSegnalazioneDTO getDto() {
		return dto;
	}

	public void setDto(PraticaSegnalazioneDTO dto) {
		this.dto = dto;
	}

	public long getIdPratica() {
		return idPratica;
	}

	
	public void setIdPratica(long idPratica) {
		this.idPratica = idPratica;
	}

	public void setSelAmbiti(HashMap<String,List<String>> selAmbiti) {
		this.selAmbiti = selAmbiti;
		dto.setAmbitiAccertamento(new ArrayList<String>());
		for( List<String> es : selAmbiti.values())
			dto.getAmbitiAccertamento().addAll(es);
	}

	public HashMap<String,List<String>> getSelAmbiti() {
		return selAmbiti;
	}
	
	public boolean isValidEmailAddress(){
		String aEmailAddress = dto.getPratica().getResEmail();
	    if (aEmailAddress == null) return false;
	    boolean result = true;
	    try {
	      InternetAddress emailAddr = new InternetAddress(aEmailAddress);
	      if ( ! hasNameAndDomain(aEmailAddress) ) {
	        result = false;
	        throw new AddressException();
	        
	      }
	    }
	    catch (AddressException ex){
	    	super.addErrorMessage("email.validation.error","");
	    	result = false;
	    }
	    return result;
	  }

	  private boolean hasNameAndDomain(String aEmailAddress){
	    String[] tokens = aEmailAddress.split("@");
	    return 
	     tokens.length == 2 && tokens[0].length()> 0 && tokens[1].length()> 0;
	     
	  }
	

}
