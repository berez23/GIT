package it.webred.cet.service.ff.web.beans.dettaglio.tributi;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.dettaglio.catasto.CatastoBean;
import it.webred.cet.service.ff.web.util.Utility;
import it.webred.ct.aggregator.ejb.tributiFabbricato.dto.DatiIciDTO;
import it.webred.ct.aggregator.ejb.tributiFabbricato.dto.DatiTarsuDTO;
import it.webred.ct.aggregator.ejb.tributiFabbricato.IciFabbricatoService;
import it.webred.ct.aggregator.ejb.tributiFabbricato.TarsuFabbricatoService;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.cosap.CosapService;
import it.webred.ct.data.access.basic.cosap.dto.DatiCosapDTO;
import it.webred.ct.data.access.basic.cosap.dto.RicercaOggettoCosapDTO;
import it.webred.ct.data.access.basic.indice.ricerca.IndiceCorrelazioneService;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.model.cosap.SitTCosapTassa;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TributiBean extends DatiDettaglio implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private IciFabbricatoService iciFabbService;
	private TarsuFabbricatoService tarsuFabbService;
	private CosapService cosapService;
	private ParameterService parameterService;
	private IndiceCorrelazioneService indService;
	
	List<DatiIciDTO> listaIci = new ArrayList<DatiIciDTO>();
	List<DatiTarsuDTO> listaTarsu = new ArrayList<DatiTarsuDTO>(); 
	List<SitTCosapTassa> listaCosap = new ArrayList<SitTCosapTassa>();
	
	
	List<DatiIciDTO> listaIciAnnoCorrente = new ArrayList<DatiIciDTO>();
	List<DatiIciDTO> listaIciAnniPrec = new ArrayList<DatiIciDTO>();
	List<DatiTarsuDTO> listaTarsuUI = new ArrayList<DatiTarsuDTO>();
	List<DatiCosapDTO> listaCosapDettaglio= new ArrayList<DatiCosapDTO>();
	
	private List<IndirizzoDTO> listaIndCatastali;

	private String annoRiferIci;
	private boolean showLabelFPS;
	
	private String numeroDocumento;
    private String annoDocumento;
    private String codiceImmobile;
    private String tipoOccupazione;
    private String dtIniValiditaStr;
    private String dtFineValiditaStr;
	
	
	public IciFabbricatoService getIciFabbService() {
		return iciFabbService;
	}
	public void setIciFabbService(IciFabbricatoService iciFabbService) {
		this.iciFabbService = iciFabbService;
	}
	public TarsuFabbricatoService getTarsuFabbService() {
		return tarsuFabbService;
	}
	public void setTarsuFabbService(TarsuFabbricatoService tarsuFabbService) {
		this.tarsuFabbService = tarsuFabbService;
	}
	public CosapService getCosapService() {
		return cosapService;
	}
	public void setCosapService(CosapService cosapService) {
		this.cosapService = cosapService;
	}
	public ParameterService getParameterService() {
		return parameterService;
	}
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	public List<DatiIciDTO> getListaIci() {
		return listaIci;
	}
	public void setListaIci(List<DatiIciDTO> listaIci) {
		this.listaIci = listaIci;
	}
	public List<DatiTarsuDTO> getListaTarsu() {
		return listaTarsu;
	}
	public void setListaTarsu(List<DatiTarsuDTO> listaTarsu) {
		this.listaTarsu = listaTarsu;
	}
	
	public IndiceCorrelazioneService getIndService() {
		return indService;
	}
	public void setIndService(IndiceCorrelazioneService indService) {
		this.indService = indService;
	}
	public List<SitTCosapTassa> getListaCosap() {
		return listaCosap;
	}
	public void setListaCosap(List<SitTCosapTassa> listaCosap) {
		this.listaCosap = listaCosap;
	}
	public List<DatiIciDTO> getListaIciAnnoCorrente() {
		return listaIciAnnoCorrente;
	}
	public void setListaIciAnnoCorrente(List<DatiIciDTO> listaIciAnnoCorrente) {
		this.listaIciAnnoCorrente = listaIciAnnoCorrente;
	}
	public List<DatiIciDTO> getListaIciAnniPrec() {
		return listaIciAnniPrec;
	}
	public void setListaIciAnniPrec(List<DatiIciDTO> listaIciAnniPrec) {
		this.listaIciAnniPrec = listaIciAnniPrec;
	}
	public List<DatiTarsuDTO> getListaTarsuUI() {
		return listaTarsuUI;
	}
	public void setListaTarsuUI(List<DatiTarsuDTO> listaTarsuUI) {
		this.listaTarsuUI = listaTarsuUI;
	}
	public void setAnnoRiferIci(String annoRiferIci) {
		this.annoRiferIci = annoRiferIci;
	}
	public String getAnnoRiferIci() {
		return annoRiferIci;
	}
	public void setShowLabelFPS(boolean showLabelFPS) {
		this.showLabelFPS = showLabelFPS;
	}
	public boolean isShowLabelFPS() {
		return showLabelFPS;
	}
		
	public void doSwitch()
	{
		logger.debug("TRIBUTI - DATA RIF: ["+super.getDataRifStr()+"]" );
		Date dataRif =super.getDataRif();
		Utility utility = new Utility(parameterService);
			
		RicercaOggettoDTO ro = new RicercaOggettoDTO();
		ro.setSezione(super.getSezione());
		ro.setFoglio(super.getFoglio());
		ro.setParticella(super.getParticella());
		ro.setSub(null);
		ro.setDtRif(dataRif);
		ro.setProvenienza(utility.getProvenienzaDatiIci());
		
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		logger.debug("doSwitch START getDatiIciFabbricato");
		listaIci = iciFabbService.getDatiIciFabbricato(ro);
		logger.debug("doSwitch END getDatiIciFabbricato");
		
		ro = new RicercaOggettoDTO();
		ro.setSezione(super.getSezione());
		ro.setFoglio(super.getFoglio());
		ro.setParticella(super.getParticella());
		ro.setSub(null);
		ro.setProvenienza(utility.getProvenienzaDatiTarsu());
		ro.setDtRif(dataRif);
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		logger.debug("doSwitch START getDatiTarsuFabbricato");
		listaTarsu = tarsuFabbService.getDatiTarsuFabbricato(ro);
		logger.debug("doSwitch END getDatiTarsuFabbricato");
	
		
		//  Lista COSAP
		RicercaIndiceDTO ricInd = new RicercaIndiceDTO();
		ricInd.setDestFonte("14");
		ricInd.setDestProgressivoEs("2");
		KeyFabbricatoDTO keyFabb = new KeyFabbricatoDTO();
		keyFabb.setCodNazionale(super.getCodNazionale());
		keyFabb.setSezione(super.getSezione());
		keyFabb.setFoglio(super.getFoglio());
		keyFabb.setParticella(super.getParticella());
		ricInd.setObj(keyFabb);
		ricInd.setEnteId(super.getEnte());
		ricInd.setUserId(super.getUsername());
		Hashtable<String, List<Object>> ht=indService.getCorrelazioniFabbricatoFromFabbricato(ricInd, false);
		List<Object> objListLc = ht.get(IndiceCorrelazioneService.LISTA_CORRELAZIONI_FABBRICATO);
		if (objListLc != null) {
			List<String> listaId = new ArrayList<String>();
			for (Object o : objListLc) {
				SitTCosapTassa tassa = (SitTCosapTassa)o;
			  /*
				RicercaOggettoCosapDTO rc = new RicercaOggettoCosapDTO();
				rc.setEnteId(super.getEnte());
				rc.setUserId(super.getUsername());
				rc.setId(tassa.getId());
				SitTCosapTassa tassaFull = cosapService.getDatiOggettoById(rc);
				if (tassaFull!=null) {
					listaCosap.add(tassaFull);
					logger.debug("DATA INIZIO/FINE VAL TARIFFA: " + tassaFull.getDtIniValiditaTariffaStr() + "/" + tassaFull.getDtFinValiditaTariffaStr());
				}
				*/	
				listaId.add(tassa.getId());
			}
			RicercaOggettoCosapDTO rc = new RicercaOggettoCosapDTO();
			rc.setEnteId(super.getEnte());
			rc.setUserId(super.getUsername());
			rc.setListaId(listaId);
			rc.setDataRif(dataRif);
			listaCosap = cosapService.getDatiSintesiOggettiByListaID(rc);
			
		}	
		
	}
	
	public void showOggettiIciCivici()
	{
		
		logger.debug("DETTAGLIO ICI CIVICI - DATA RIF: ["+super.getDataRifStr()+"]" );
		Utility utility = new Utility(parameterService);
		
		RicercaOggettoDTO ro = new RicercaOggettoDTO();
		ro.setSezione(super.getSezione());
		ro.setFoglio(super.getFoglio());
		ro.setParticella(super.getParticella());
		ro.setSub(null);
		ro.setDtRif(super.getDataRif());
		ro.setProvenienza(utility.getProvenienzaDatiIci());
		
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		List<DatiIciDTO> tmpList = iciFabbService.getDatiIciCiviciDelFabbricato(ro);
		
		if (tmpList==null || tmpList.size()==0)
			logger.debug("******* showOggettiIciCivici tmpList NULL");
		else
			logger.debug("******* showOggettiIciCivici tmpList SIZE = " + tmpList.size());
		
		this.sceltaListaAnno(tmpList);
		
		loadIndCatastali();
		
		showLabelFPS = false;
	}
	
	public void showDettaglioICI()
	{
		logger.debug("DETTAGLIO ICI - DATA RIF: ["+super.getDataRifStr()+"]" );
		Utility utility = new Utility(parameterService);
		
		RicercaOggettoDTO ro = new RicercaOggettoDTO();
		ro.setSezione(super.getSezione());
		ro.setFoglio(super.getFoglio());
		ro.setParticella(super.getParticella());
		ro.setSub(super.getSub());
		ro.setDtRif(super.getDataRif());
		ro.setProvenienza(utility.getProvenienzaDatiIci());
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		List<DatiIciDTO> tmpList = new ArrayList<DatiIciDTO>();
		
		tmpList = iciFabbService.getDatiIciUI(ro);
			
		if (tmpList==null || tmpList.size()==0)
			logger.debug("******* showDettaglioICI tmpList NULL");
		else
			logger.debug("******* showDettaglioICI tmpList SIZE = " + tmpList.size());
		
		this.sceltaListaAnno(tmpList);
		
		loadIndCatastali();
		
		showLabelFPS = true;
	}
	
	private void loadIndCatastali(){
		
		//Carico la lista di Indirizzi Catastali
		
			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			
			roc.setDtVal(super.getDataRif());
			roc.setFoglio(this.getFoglio());
			roc.setParticella(this.getParticella());
			roc.setEnteId(super.getEnte());
			roc.setUserId(super.getUsername());
			roc.setCodEnte(super.getEnte());
			
			try {
			
				CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");				
				setListaIndCatastali(catastoService.getLocalizzazioneCatastaleDescr(roc));
			
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		
	}
	
	public void showOggettiTarsuCivici() 
	{
		logger.debug("OGGETTI TARSU CIVICI - DATA RIF: ["+super.getDataRifStr()+"]" );
		Utility utility = new Utility(parameterService);
		
		RicercaOggettoDTO ro = new RicercaOggettoDTO();
		ro.setSezione(super.getSezione());
		ro.setFoglio(super.getFoglio());
		ro.setParticella(super.getParticella());
		ro.setProvenienza(utility.getProvenienzaDatiTarsu());
		ro.setDtRif(super.getDataRif());
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		listaTarsuUI = tarsuFabbService.getDatiTarsuCiviciDelFabbricato(ro);
		
		this.loadIndCatastali();
		
		showLabelFPS = false;
	}
	
	public void showDettaglioTarsu()
	{
	
		logger.debug("DETTAGLIO TARSU DATA RIF" + super.getDataRif());
		Utility utility = new Utility(parameterService);
		
		RicercaOggettoDTO ro = new RicercaOggettoDTO();
		ro.setSezione(super.getSezione());
		ro.setFoglio(super.getFoglio());
		ro.setParticella(super.getParticella());
		ro.setSub(super.getSub());
		ro.setProvenienza(utility.getProvenienzaDatiTarsu());
		ro.setDtRif(super.getDataRif());
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		listaTarsuUI = tarsuFabbService.getDatiTarsuUI(ro);
		
		loadIndCatastali();
		
		showLabelFPS = true;
	}
	
	private void sceltaListaAnno(List<DatiIciDTO> tmpList)
	{
		listaIciAnnoCorrente.clear();
		listaIciAnniPrec.clear();
		
		for(DatiIciDTO d:tmpList)
		{
			if (d.getAnnoRifConfr().equals(annoRiferIci))
				listaIciAnnoCorrente.add(d);
			else
				listaIciAnniPrec.add(d);
		}
	}
	
	public void showDettaglioCosap()
	{
	DateFormat sf= new SimpleDateFormat("dd/MM/yyyy");
		
		RicercaOggettoCosapDTO rc = new RicercaOggettoCosapDTO();
		rc.setEnteId(super.getEnte());
		rc.setUserId(super.getUsername());
		rc.setNumeroDocumento(numeroDocumento);
		rc.setAnnoDocumento(annoDocumento);
		rc.setCodiceImmobile(codiceImmobile);
		rc.setTipoOccupazione(tipoOccupazione);
		try{
			if (dtIniValiditaStr != null)
				rc.setDtIniValidita(sf.parse(dtIniValiditaStr));
			if (dtFineValiditaStr != null)
				rc.setDtFineValidita(sf.parse(dtFineValiditaStr));
		}
		catch(ParseException e){
			
		}
		
		listaCosapDettaglio= cosapService.getDettaglioCosap(rc);
		
		showLabelFPS = true;
	}
	public List<DatiCosapDTO> getListaCosapDettaglio() {
		return listaCosapDettaglio;
	}
	public void setListaCosapDettaglio(List<DatiCosapDTO> listaCosapDettaglio) {
		this.listaCosapDettaglio = listaCosapDettaglio;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getAnnoDocumento() {
		return annoDocumento;
	}
	public void setAnnoDocumento(String annoDocumento) {
		this.annoDocumento = annoDocumento;
	}
	public String getCodiceImmobile() {
		return codiceImmobile;
	}
	public void setCodiceImmobile(String codiceImmobile) {
		this.codiceImmobile = codiceImmobile;
	}
	public String getTipoOccupazione() {
		return tipoOccupazione;
	}
	public void setTipoOccupazione(String tipoOccupazione) {
		this.tipoOccupazione = tipoOccupazione;
	}
	public String getDtIniValiditaStr() {
		return dtIniValiditaStr;
	}
	public void setDtIniValiditaStr(String dtIniValiditaStr) {
		this.dtIniValiditaStr = dtIniValiditaStr;
	}
	public String getDtFineValiditaStr() {
		return dtFineValiditaStr;
	}
	public void setDtFineValiditaStr(String dtFineValiditaStr) {
		this.dtFineValiditaStr = dtFineValiditaStr;
	}
	public List<IndirizzoDTO> getListaIndCatastali() {
		return listaIndCatastali;
	}
	public void setListaIndCatastali(List<IndirizzoDTO> listaIndCatastali) {
		this.listaIndCatastali = listaIndCatastali;
	}
		

}
