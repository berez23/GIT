package it.webred.cs.jsf.bean;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.DataModelCostanti.StatoTransizione;
import it.webred.cs.data.model.CsCfgAttr;
import it.webred.cs.data.model.CsCfgItStato;
import it.webred.cs.data.model.CsCfgItTransizione;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.utilities.CommonUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.NamingException;

public class IterDialogCambioStatoBean extends IterBaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<IterStatoAttrBean>  iterStatoAttrs;
	private List<IterStatoTransizioneBean>  statoSuccessivoAziones;
	private String datiExtLabel;
	private String statoDatiLabel;
	
	private long idCaso;
	private String opUsername;
	private String opRuolo;
	private String alertUrl;
	private boolean notificaSettoreSegnalante;
	
	private boolean datiRendered;
	private boolean datiExtRendered;
	private boolean operatorePanelRendered;
	private boolean enteRendered;
	private boolean settoreRendered;
	private boolean operatoreRendered;
	
	private long idSettore;
	private String nota;
	private String nomeStatoLastStep;
	
	private List<SelectItem> entes;
	private List<SelectItem> settores;
	private List<SelectItem> operatores;
	private Long enteSelezionato;
	private Long settoreSelezionato;
	private Long operatoreSelezionato;
	
	private CsItStep itLastStep;
	
	public void initialize(long idCaso, String opUsername, long idSettore, String opRuolo, String alertUrl, boolean notificaSettoreSegnalante) {
		try {
			this.idCaso = idCaso;
			this.opUsername = opUsername;
			this.opRuolo = opRuolo;
			this.alertUrl = alertUrl;
			this.notificaSettoreSegnalante = notificaSettoreSegnalante;
			this.idSettore = idSettore;
					
			this.datiRendered = false;
			this.datiExtRendered = false;
			this.operatorePanelRendered = false;
			this.enteRendered = false;
			this.settoreRendered = false;
			this.operatoreRendered = false;
				
			AccessTableIterStepSessionBeanRemote iterSessionBean = getIterSessioBean();
			
			IterDTO itDto = new IterDTO();
			fillEnte(itDto);
			itDto.setIdCaso(idCaso);
			itLastStep = iterSessionBean.getLastIterStepByCaso(itDto);
				
			if(itLastStep != null){
	
				this.nomeStatoLastStep = itLastStep.getCsCfgItStato().getNome();
				
				statoSuccessivoAziones = new LinkedList<IterStatoTransizioneBean>();
				
				itDto.setIdStato(itLastStep.getCsCfgItStato().getId());
				itDto.setOpRuolo(opRuolo);
				List<CsCfgItTransizione> listaTransizione =  iterSessionBean.getTransizionesByStatoRuolo(itDto);
				for (CsCfgItTransizione itTrans : listaTransizione )
					statoSuccessivoAziones.add( new IterStatoTransizioneBean( itTrans ) );
				
				initializeComboOperatore();
				
				//Reset attribute list
				iterStatoAttrs = new LinkedList<IterStatoAttrBean>();
				enteSelezionato = 0L;
				settoreSelezionato = 0L;
				operatoreSelezionato = 0L;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nell'inizializzazione", "Inizializzazione non riuscita!");
		}
	}
	
	protected void initializeComboOperatore(){
		try {
			AccessTableOperatoreSessionBeanRemote operatoreSessionBean = getOperatoreSessioBean();

			OperatoreDTO opDto = new OperatoreDTO();
			fillEnte(opDto);
			opDto.setIdSettore(idSettore);
			CsOSettore settoreOperatore = operatoreSessionBean
					.findSettoreById(opDto);

			caricaEnti();

			caricaSettori(settoreOperatore.getCsOOrganizzazione().getId());

			caricaOperatore(idSettore);

			enteSelezionato = 0L;
			settoreSelezionato = 0L;
			operatoreSelezionato = 0L;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nell'inizializzazione del campo operatore", "Inizializzazione combo cambio operatore non riuscita!");
		}
	}
	
	protected void initializeStatoAttr(long idStato){
		try {
			AccessTableIterStepSessionBeanRemote iterSessionBean = getIterSessioBean();
			
			IterDTO itDto = new IterDTO();
			fillEnte(itDto);
			itDto.setIdStato(idStato);
			CsCfgItStato itStato = iterSessionBean.findStatoById( itDto );
			
			datiExtLabel = itStato.getSezioneAttributiLabel();
			statoDatiLabel = itStato.getDatiLabel();
			
			iterStatoAttrs = new LinkedList<IterStatoAttrBean>();
			for( CsCfgAttr itStatoAttr : itStato.getCsCfgAttrs() )
				iterStatoAttrs.add( new IterStatoAttrBean(itStatoAttr) );
				
			datiRendered = true;
			if(iterStatoAttrs.size() > 0)
				datiExtRendered = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nell'inizializzazione degli attributi dello stato!", "Inizializzazione attributi non riuscita!");
		}
	}
	
	public boolean hasAttributi( Long idStatoSuccessivo ) throws NamingException {
		initializeStatoAttr( idStatoSuccessivo );
		return iterStatoAttrs.size() > 0;
	}
	
	public boolean salvaStato ( Long idStatoSuccessivo ){
		try {
			
			//se è "preso in carico" controllo che l'operatore abbia un tipo op associato 
			if(idStatoSuccessivo.equals(DataModelCostanti.IterStatoInfo.PRESO_IN_CARICO)) {
				AccessTableCasoSessionBeanRemote casoSessionBean = getCasoSessioBean();
				BaseDTO dto = new BaseDTO();
				fillEnte(dto);
				CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				dto.setObj(opSettore.getCsOOperatore().getId());
				dto.setObj2(opSettore.getCsOSettore().getId());
				CsOOperatoreTipoOperatore opTipoOp = casoSessionBean.findOperatoreTipoOperatoreByOpSettore(dto);
				if(opTipoOp == null) {
					addError("Salvataggio non effettuato", "La sua utenza non è associata a nessun tipo operatore per il settore scelto");
					return false;
				}
				
			}
			
			HashMap<Long, Object> attrNewStato = new HashMap<Long, Object>();
			
			for (IterStatoAttrBean it : iterStatoAttrs)
				attrNewStato.put(it.getIdAttr(), it.getValore());

			IterDTO itDto = new IterDTO();
			fillEnte(itDto);
			itDto.setIdCaso(idCaso);
			itDto.setIdStato(idStatoSuccessivo);
			itDto.setNomeOperatore(opUsername);
			itDto.setIdSettore(idSettore);
			itDto.setIdOpTo(operatoreSelezionato);
			itDto.setIdSettTo(settoreSelezionato);
			itDto.setIdOrgTo(enteSelezionato);
			itDto.setNota(nota);
			itDto.setAttrNewStato(attrNewStato);
			itDto.setAlertUrl(alertUrl);
			itDto.setNotificaSettoreSegnalante(notificaSettoreSegnalante);
			boolean bSaved = getIterSessioBean().addIterStep(itDto);
			if( !bSaved )
			{
				addError("Attenzione", "Errore durante il salvataggio");
			}
			
			// Reset Nota
			nota = "";
			
			return bSaved;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nel salvataggio", "Salvataggio non riuscito!");
		}
		
		return false;
	}

	public boolean checkRenderOperatore(Long idStatoSuccessivo) throws NamingException {
		
		for(IterStatoTransizioneBean itTrans : statoSuccessivoAziones )
		{
			if( itTrans.getIdStatoA().equals(idStatoSuccessivo))
			{
				enteRendered = !itTrans.getStatoOrganizzazione().equals(DataModelCostanti.StatoTransizione.Enum.IGNORA );  
				settoreRendered = !itTrans.getStatoSettore().equals(DataModelCostanti.StatoTransizione.Enum.IGNORA );
				operatoreRendered = !itTrans.getStatoOperatore().equals(DataModelCostanti.StatoTransizione.Enum.IGNORA );
				break;
			}
		}

		datiRendered = enteRendered || settoreRendered || operatoreRendered;
		
		operatorePanelRendered = datiRendered;
		
		return datiRendered;
	}
	
	protected boolean validaTransizioneEnte( IterStatoTransizioneBean itTrans ) {

		StatoTransizione.Enum eStato = itTrans.getStatoOrganizzazione(); 
		if( eStato.equals( StatoTransizione.Enum.OBBLIGATORIO ) )
		{
			if( !(enteSelezionato != null && enteSelezionato > 0L) )
			{
				addError("Attenzione", "Selezionare un ente");
				return false;
			}
		}
		else if( eStato.equals( StatoTransizione.Enum.IGNORA ) )
		{
			enteSelezionato = 0L;
		}
		
		return true;
	}
	
	protected boolean validaTransizioneSettore( IterStatoTransizioneBean itTrans ) {

		StatoTransizione.Enum eStato = itTrans.getStatoSettore();
		
		if( eStato.equals( StatoTransizione.Enum.OBBLIGATORIO ) )
		{
			if( !(settoreSelezionato != null && settoreSelezionato > 0L) ) 
			{
				addError("Attenzione", "Selezionare un settore");
				return false;
			}
		}
		else if( eStato.equals( StatoTransizione.Enum.IGNORA ) )
		{
			settoreSelezionato = 0L;
		}
		
		return true;
	}
	
	protected boolean validaTransizioneOperatore( IterStatoTransizioneBean itTrans ) {

		StatoTransizione.Enum eStato = itTrans.getStatoOperatore();

		if( eStato.equals( StatoTransizione.Enum.OBBLIGATORIO ) )
		{
			if( !(operatoreSelezionato != null && operatoreSelezionato > 0L) ) {
				addError("Attenzione", "Selezionare un operatore");
				return false;
			}
		}
		else if( eStato.equals( StatoTransizione.Enum.IGNORA ) )
		{
			operatoreSelezionato = 0L;
		}
		
		return true;
	}
	
	public boolean validaTransizione( Long idStatoSuccessivo ) {
		for( IterStatoTransizioneBean itTrans : statoSuccessivoAziones )
		{
			if( idStatoSuccessivo.equals( itTrans.getIdStatoA() ) )
			{
				boolean isValid = true;
				isValid &= validaTransizioneEnte( itTrans );
				isValid &= validaTransizioneSettore( itTrans );
				isValid &= validaTransizioneOperatore( itTrans );
			    return isValid;
			}
		}
	    
	    return true;
	}

	protected Object getValoreAttributo( Long id )
	{
		for( IterStatoAttrBean it : iterStatoAttrs )
			if( it.getIdAttr().equals(id) )
				return it.getValore();
		
		return null;
	}
	
	public boolean validaAttributi( Long idStatoSuccessivo ) throws NamingException {
		try {
			
			IterDTO itDto = new IterDTO();
			fillEnte(itDto);
			itDto.setIdStato(idStatoSuccessivo);
			CsCfgItStato itStato = getIterSessioBean().findStatoById( itDto );
			
			List<String> errorList = new LinkedList<String>();
			for (CsCfgAttr it : itStato.getCsCfgAttrs() ) {
				
				boolean bAttrObbl = !CommonUtils.isEmptyString ( it.getMessaggioObbligatorio() );
				
				Object attributoVal = getValoreAttributo(it.getId());
				if( bAttrObbl && attributoVal == null)
					errorList.add(it.getMessaggioObbligatorio());
			}
			
			if( errorList.size() > 0 ) {
				addError("Attenzione", CommonUtils.Join("<br/>", errorList.toArray()));
				return false;
			}
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nella validazione degli attributi", "Validazione attributi non riuscito!");
		}
		
		return true;
	}
	
	public void handleEnteChange() throws NamingException  {
		caricaSettori(enteSelezionato);
	}
	
	public void handleSettoreChange() throws NamingException  {
		caricaOperatore(settoreSelezionato);
	}
	
	protected void caricaEnti() throws NamingException {
		try {
			AccessTableConfigurazioneSessionBeanRemote confService = getConfigurazioneSessioBean();
		
			CeTBaseObject opDto = new CeTBaseObject();
			fillEnte(opDto);
			entes = new LinkedList<SelectItem>();
			for (CsOOrganizzazione it : confService.getOrganizzazioniBelfiore(opDto)) {
				entes.add(new SelectItem(it.getId(), it.getNome()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nel caricamento degli Enti", "Caricamento Enti non riuscito !");
		}
	}

	protected void caricaSettori(long enteId) throws NamingException {
		try {
			if( enteId != 0L ) {
				AccessTableOperatoreSessionBeanRemote operatoreSessionBean = getOperatoreSessioBean();
		
				OperatoreDTO opDto = new OperatoreDTO();
				fillEnte(opDto);
				opDto.setIdOrganizzazione(enteId);
				settores = new LinkedList<SelectItem>();
				for (CsOSettore it : operatoreSessionBean.findSettoreByOrganizzazione(opDto) ) {
					settores.add(new SelectItem( it.getId(), it.getNome()) );
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nel caricamento dei Settori", "Caricamento Settori non riuscito!");
		}
	}
	
	protected void caricaOperatore(long settoreId) throws NamingException {
		try {
			if (settoreId != 0L) {
				AccessTableOperatoreSessionBeanRemote operatoreSessionBean = getOperatoreSessioBean();
		
				OperatoreDTO opDto = new OperatoreDTO();
				fillEnte(opDto);
				opDto.setIdSettore(settoreId);
				operatores = new LinkedList<SelectItem>();
				AnagraficaService anagraficaService = (AnagraficaService) getEjb("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");
				for (CsOOperatoreSettore it : operatoreSessionBean.findOperatoreSettoreBySettore(opDto) ) {
					AmAnagrafica amAna = anagraficaService.findAnagraficaByUserName(it.getCsOOperatore().getUsername());
					operatores.add(new SelectItem( it.getCsOOperatore().getId(), amAna.getCognome() + " " + amAna.getNome()));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Errore nel caricamento degli Operatori", "Caricamento Operatori non riuscito!");
		}
	}
		
	public List<IterStatoAttrBean> getIterStatoAttrs() {
		return iterStatoAttrs;
	}

	public List<IterStatoTransizioneBean> getStatoSuccessivoAziones() {
		return statoSuccessivoAziones;
	}

	public String getDatiExtLabel() {
		return datiExtLabel;
	}

	public String getStatoDatiLabel() {
		return statoDatiLabel;
	}
	
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public List<SelectItem> getEntes() {
		return entes;
	}

	public List<SelectItem> getSettores() {
		return settores;
	}

	public List<SelectItem> getOperatores() {
		return operatores;
	}

	public Long getEnteSelezionato() {
		return enteSelezionato;
	}

	public void setEnteSelezionato(Long enteSelezionato) {
		this.enteSelezionato = enteSelezionato;
	}

	public Long getSettoreSelezionato() {
		return settoreSelezionato;
	}

	public void setSettoreSelezionato(Long settoreSelezionato) {
		this.settoreSelezionato = settoreSelezionato;
		
	}

	public Long getOperatoreSelezionato() {
		return operatoreSelezionato;
	}

	public void setOperatoreSelezionato(Long operatoreSelezionato) {
		this.operatoreSelezionato = operatoreSelezionato;
	}

	public boolean isDatiRendered() {
		return datiRendered;
	}

	public boolean isDatiExtRendered() {
		return datiExtRendered;
	}

	public boolean isOperatorePanelRendered() {
		return operatorePanelRendered;
	}
	
	public boolean isEnteRendered() {
		return enteRendered;
	}

	public boolean isSettoreRendered() {
		return settoreRendered;
	}

	public boolean isOperatoreRendered() {
		return operatoreRendered;
	}

	public String getNomeStatoLastStep() {
		return nomeStatoLastStep;
	}	
}
