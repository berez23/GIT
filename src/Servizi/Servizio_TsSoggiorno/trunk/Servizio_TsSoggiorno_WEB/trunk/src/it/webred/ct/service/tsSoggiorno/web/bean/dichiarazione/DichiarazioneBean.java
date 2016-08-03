package it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione;

import it.webred.ct.service.tsSoggiorno.data.access.AnagraficaService;
import it.webred.ct.service.tsSoggiorno.data.access.DichiarazioneService;
import it.webred.ct.service.tsSoggiorno.data.access.dto.*;
import it.webred.ct.service.tsSoggiorno.data.model.*;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.anagrafica.*;
import it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione.export.*;
import it.webred.ct.service.tsSoggiorno.web.bean.util.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class DichiarazioneBean extends TsSoggiornoBaseBean implements
		DichiarazioneDataProvider {

	private final String statoBozza = "BOZZA";
	private final String statoChiusa = "CHIUSA";

	private DichiarazioneSearchCriteria criteria = new DichiarazioneSearchCriteria();
	private String codFiscale;
	private String idSocieta;
	private String idStruttura;
	private List<SelectItem> listaPeriodi = new ArrayList<SelectItem>();
	private List<SelectItem> listaPeriodiNuovaDich = new ArrayList<SelectItem>();
	private List<SelectItem> listaClassi = new ArrayList<SelectItem>();
	private List<SelectItem> listaClassiStruttura = new ArrayList<SelectItem>();
	private List<SelectItem> listaStrutture = new ArrayList<SelectItem>();
	private List<SelectItem> listaSocieta = new ArrayList<SelectItem>();

	// DICH
	private Boolean[] stepArray;
	private String currentStep;
	private String idSelezionato;
	private String progRifiutoSel;
	private IsDichiarazione dichiarazione;
	private List<RifiutoDTO> listaRifiuti;
	private String tipoPagamento="MAV";
	private IsStrutturaSnap snap;
	private String periodoDescr;
	private String strutturaDescr;
	private String classeDescr;
	private IsSocieta societa;
	private StrutturaDTO struttura;
	private boolean anteprimaStampa;
	private boolean anteprimaStampaIter;
	private List<ModuloDTO> listaModuli1;
	private List<ModuloDTO> listaModuli2;
	private List<ModuloDTO> listaModuli3;
	private List<ModuloDTO> listaModuli4;
	private IsImpostaDovuta impDovuta;
	private IsImpostaIncassata impIncassata;
	private BigDecimal impostaInSospeso;
	private BigDecimal impostaInSospesoPrec;
	private BigDecimal totIncassato;
	private BigDecimal totSospeso;
	private BigDecimal totVersare;
	private BigDecimal valTariffa;
	
	private BigDecimal impLordaComp;
	
	private BigDecimal totRifiuti;
	private BigDecimal impRifiuti;
	
	private boolean visEsenzioni = false;
	private BigDecimal impEsenzioni;
	
	private boolean visRiduzioniE = false;
	private BigDecimal totRiduzioniE;
	private BigDecimal impRiduzioniE;
	
	private boolean visRiduzioniF1 = false;
	private BigDecimal totRiduzioniF1;
	private BigDecimal impRiduzioniF1;
	
	private boolean visRiduzioniF2 = false;
	private BigDecimal totRiduzioniF2;
	private BigDecimal impRiduzioniF2;
	
	private BigDecimal impPagAnticipati;
	
	private String listaModuliRow;
	private String listaModuliStep;
	
	private List<IsSanzione> listaSanzioni;
	private List<IsRimborso> listaRimborsi;
	

	@PostConstruct
	public void init() {
		
		this.stepArray = new Boolean[7];
		
		if (codFiscale == null) {
			UserBean user = (UserBean) getBeanReference("userBean");
			codFiscale = user.getUsername();
		}
		doLoadNewDichiarazione();
		
		

	}

	public List<DichiarazioneDTO> getDichiarazioniByRange(int start,
			int rowNumber) {
		try {

			loadSocieta();
			loadClassi();
			loadPeriodi();
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			criteria.setCodFiscale(codFiscale);
			criteria.setStart(start);
			criteria.setRowNumber(rowNumber);
			dataIn.setObj(criteria);
			return super.getDichiarazioneService().getDichiarazioniByCriteria(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
		return new ArrayList<DichiarazioneDTO>();
	}

	public int getDichiarazioniCount() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			criteria.setCodFiscale(codFiscale);
			dataIn.setObj(criteria);
			return super.getDichiarazioneService().getDichiarazioniCountByCriteria(dataIn)
					.intValue();

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
		return 0;
	}

	public void doLoadNewDichiarazione() {
		try {
			
			listaRimborsi = null;
			listaSanzioni = null;

			loadSocieta();
			loadClassi();
			loadPeriodi();
			dichiarazione = new IsDichiarazione();
			snap = new IsStrutturaSnap();
			idSelezionato = null;
			if (listaSocieta.size() > 0) {
				if (idSocieta != null)
					snap.setFkIsSocieta(new BigDecimal(idSocieta));
				else
					snap.setFkIsSocieta((BigDecimal) listaSocieta.get(0).getValue());
				doLoadSocieta();
				doLoadStrutture();
				if (listaStrutture.size() > 0) {
					if (idStruttura != null)
						snap.setFkIsStruttura(new BigDecimal(idStruttura));
					else
						snap.setFkIsStruttura((BigDecimal) listaStrutture
								.get(0).getValue());
					doLoadStruttura();
					doLoadClassiStruttura();
					if (listaClassiStruttura.size() > 0)
						snap.setFkIsClasse((String) listaClassiStruttura.get(0)
								.getValue());
				}
			} else {
				listaStrutture = new ArrayList<SelectItem>();
				listaClassiStruttura = new ArrayList<SelectItem>();
			}
			

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doLoadNewSteps() {
		try {
			
			
			SocietaBean sBean = (SocietaBean)super.getBeanReference("societaBean");
			sBean.setIdSelezionato(struttura.getStruttura().getFkIsSocieta().longValue());
			sBean.doLoadSocieta();

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);

			// caricamento dati anagrafici
			for (SelectItem item : listaPeriodi) {
				if (item.getValue().equals(
						dichiarazione.getFkIsPeriodo().longValue()))
					periodoDescr = item.getLabel();
			}
			
			
			strutturaDescr = struttura.getStruttura().getDescrizione();
			snap.setIndirizzoStrut(struttura.getStruttura().getIndirizzo());
			snap.setNumCivStrut(struttura.getStruttura().getNumCiv());
			snap.setDescrizioneStrut(struttura.getStruttura().getDescrizione());
			snap.setEmailStrut(struttura.getStruttura().getEmail());
			
			snap.setRagSoc(struttura.getSocieta().getRagSoc());
			snap.setCf(struttura.getSocieta().getCf());
			snap.setPi(struttura.getSocieta().getPi());
			snap.setFkIsSocieta(struttura.getStruttura().getFkIsSocieta());
			snap.setIndirizzoSede(struttura.getSocieta().getIndirizzoSede());
			snap.setNumeroCivSede(struttura.getSocieta().getNumeroCivSede());
			snap.setComuneSede(struttura.getSocieta().getComuneSede());
			snap.setSiglaProvSede(struttura.getSocieta().getSiglaProvSede());
			snap.setStatoEsteroSede(struttura.getSocieta().getStatoEsteroSede());
			
			
			snap.setDescrizioneTitolo(sBean.getSocietaSogg().getDescTitolo());
			
			dataIn.setCodFiscale(codFiscale);
			IsSoggetto sogg = super.getAnagraficaService().getSoggettoByCodFis(dataIn);
			snap.setFkIsSoggetto(new BigDecimal(sogg.getId()));
			snap.setCognome(sogg.getCognome());
			snap.setNome(sogg.getNome());
			snap.setCodfisc(sogg.getCodfisc());
			snap.setComuneNasc(sogg.getComuneNasc());
			snap.setSiglaProvNasc(sogg.getSiglaProvNasc());
			snap.setStatoEsteroNasc(sogg.getStatoEsteroNasc());
			snap.setDtNasc(sogg.getDtNasc());
			snap.setComuneRes(sogg.getComuneRes());
			snap.setSiglaProvRes(sogg.getSiglaProvRes());
			snap.setStatoEsteroRes(sogg.getStatoEsteroRes());
			snap.setIndirizzoRes(sogg.getIndirizzoRes());
			snap.setNumeroCivRes(sogg.getNumeroCivRes());
			snap.setCapRes(sogg.getCapRes());
			snap.setCell(sogg.getCell());
			snap.setFax(sogg.getFax());
			snap.setTel(sogg.getTel());
			snap.setEmail(sogg.getEmail());
			snap.setUsrIns(sogg.getCodfisc());
			snap.setDtIns(new Date());
			for (IsClassiStruttura cs : struttura.getClassi()) {
				if (cs.getId().getFkIsClasse().equals(snap.getFkIsClasse())) {
					classeDescr = cs.getDescrizione();
					snap.setDescrizioneClasseStrut(cs.getDescrizione());
				}
			}
			dichiarazione.setUsrIns(sogg.getCodfisc());
			dichiarazione.setDtIns(new Date());

			dichiarazione.setIntegrativa(new BigDecimal(0));

			listaRifiuti = new ArrayList<RifiutoDTO>();
			
			dataIn.setId(dichiarazione.getFkIsPeriodo().longValue());
			listaModuli1 = super.getDichiarazioneService().getNewModuliVersato(dataIn);

			dataIn.setId(dichiarazione.getFkIsPeriodo().longValue());
			listaModuli2 = super.getDichiarazioneService().getNewModuliEsenzioni(dataIn);

			dataIn.setId(dichiarazione.getFkIsPeriodo().longValue());
			listaModuli3 = super.getDichiarazioneService().getNewModuliRiduzioni(dataIn);

			dataIn.setId(dichiarazione.getFkIsPeriodo().longValue());
			listaModuli4 = super.getDichiarazioneService().getNewModuliPagAnticipati(dataIn);

			impDovuta = new IsImpostaDovuta();
			impDovuta.setImporto(new BigDecimal(0));
			impDovuta.setImportoMesiPrec(new BigDecimal(0));
			impIncassata = new IsImpostaIncassata();
			impIncassata.setImporto(new BigDecimal(0));
			impIncassata.setImportoMesiPrec(new BigDecimal(0));
			
			
			// cerco la dichiarazione precedente per prendere il campo
			// "residuo da mesi precedenti"
			dataIn.setId(snap.getFkIsStruttura().longValue());
			dataIn.setId2(snap.getFkIsClasse());
			dataIn.setId3(dichiarazione.getFkIsPeriodo().longValue());
			IsDichiarazione dPrec = super.getDichiarazioneService()
					.getDichiarazionePrecByPeriodoStrClasse(dataIn);
			if (dPrec != null) {
				impDovuta.setImportoMesiPrec(dPrec.getTotResiduo());
				// se è ancora in bozza non permetto la creazione nuova
				if (dPrec.getStato().equals(statoBozza)) {
					super.addErrorMessage("dich.precbozza", null);
					PageBean bean = (PageBean) getBeanReference("pageBean");
					bean.setPageCenter("/jsp/protected/content/dich.xhtml");
				}
			}

			// controllo se già esiste
			IsDichiarazione d = super.getDichiarazioneService().getDichiarazioneByPeriodoStrClasse(dataIn);
			if (d != null && d.getStato().equals(statoBozza)) {
				super.addErrorMessage("dich.presente", null);
				PageBean bean = (PageBean) getBeanReference("pageBean");
				bean.setPageCenter("/jsp/protected/content/dich.xhtml");
			} else if (d != null && d.getStato().equals(statoChiusa)) {
				super.addWarningMessage("dich.integrativa");
				dichiarazione.setIntegrativa(new BigDecimal(1));
				
			}
			
			if(dichiarazione.getIntegrativaInt()==0){
				
				dataIn = new DataInDTO();
				fillEnte(dataIn);
				dataIn.setId(snap.getFkIsStruttura().longValue());
				dataIn.setId2(snap.getFkIsClasse());
				dataIn.setId3(dichiarazione.getFkIsPeriodo().longValue());
				
				listaRimborsi = super.getDichiarazioneService().getRimborsiByPeriodoStrClasse(dataIn);
				listaSanzioni = super.getDichiarazioneService().getSanzioniByPeriodoStrClasse(dataIn);
			}else{
				listaRimborsi = null;
				listaSanzioni = null;
			}

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}
	
	

	public void doLoadDichiarazione() {
		try {
			
			DichiarazioneService dichiarazioneService = super.getDichiarazioneService();

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(new Long(idSelezionato));
			DichiarazioneDTO dichDto = dichiarazioneService.getDichiarazioneById(dataIn);
			dichiarazione = dichDto.getDichiarazione();
			snap = dichDto.getStrutturaSnap();

			periodoDescr = dichDto.getPeriodo().getDescrizione();
			strutturaDescr = dichDto.getStrutturaSnap().getDescrizioneStrut();
			classeDescr = dichDto.getStrutturaSnap().getDescrizioneClasseStrut();
			
			listaSanzioni = dichDto.getListaSanzioni();
			listaRimborsi = dichDto.getListaRimborsi();

			listaModuli1 = dichiarazioneService.getLoadModuliVersato(dataIn);
			listaModuli2 = dichiarazioneService.getLoadModuliEsenzioni(dataIn);
			listaModuli3 = dichiarazioneService.getLoadModuliRiduzioni(dataIn);
			listaModuli4 = dichiarazioneService.getLoadModuliPagAnticipati(dataIn);

			impDovuta = dichiarazioneService
					.getImpostaDovutaByDichiarazione(dataIn);
			impIncassata = dichiarazioneService
					.getImpostaIncassataByDichiarazione(dataIn);

			listaRifiuti = dichiarazioneService.getListaRifiutiByIdDich(dataIn);
			
			//Carico la società
			SocietaBean sBean = (SocietaBean)super.getBeanReference("societaBean");
			sBean.setIdSelezionato(snap.getFkIsSocieta().longValue());
			sBean.doLoadSocieta();
			
		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}
	
	public void doSaveAnagrafica(){
		
		AnagraficaBean aBean = (AnagraficaBean)super.getBeanReference("anagraficaBean");
		SocietaBean sBean = (SocietaBean)super.getBeanReference("societaBean");
		
		sBean.setShowMessage(false);
		aBean.setShowMessage(false);
			
		//Devo fare l'update dello snap
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCodFiscale(codFiscale);
		
		IsSoggetto sogg = aBean.getSoggetto();
		
		sogg.setCognome(snap.getCognome());
		sogg.setNome(snap.getNome());
		sogg.setComuneNasc(snap.getComuneNasc());
		sogg.setSiglaProvNasc(snap.getSiglaProvNasc());
		sogg.setStatoEsteroNasc(snap.getStatoEsteroNasc());
		sogg.setDtNasc(snap.getDtNasc());
		sogg.setComuneRes(snap.getComuneRes());
		sogg.setSiglaProvRes(snap.getSiglaProvRes());
		sogg.setStatoEsteroRes(snap.getStatoEsteroRes());
		sogg.setIndirizzoRes(snap.getIndirizzoRes());
		sogg.setNumeroCivRes(snap.getNumeroCivRes());
		sogg.setCapRes(snap.getCapRes());
		sogg.setCell(snap.getCell());
		sogg.setFax(snap.getFax());
		sogg.setTel(snap.getTel());
		sogg.setEmail(snap.getEmail());
		
		aBean.setSoggetto(sogg);
		sBean.getSocietaSogg().setDescTitolo(snap.getDescrizioneTitolo());
		
		aBean.doUpdateSoggetto();
		sBean.doSaveSocieta();
		
		if(this.idSelezionato!=null){
			snap.setDtMod(new Date());
			snap.setUsrMod(sogg.getCodfisc()); 
		}
	
		sBean.setShowMessage(true);
		aBean.setShowMessage(true);
	}

	public boolean doCheckDati() {

		boolean validita = true;
		
		boolean[] verifica = {true,true,true,true,true,true,true};
		
		if(this.currentStep!=null){
			int step = new Integer(currentStep).intValue();
			
			for(int i = step; i<verifica.length; i++)
				verifica[i]=false;
		}
			int totRifiuti=this.getTotaleGiorniRifiuto();
			int totRifiutiEsenzioni=this.getTotGiorniEsenzioni()+totRifiuti;
			int totRifEseRid= this.getTotGiorniRiduzioni()+totRifiutiEsenzioni;
		
			
			if(verifica[1]){
				validita = this.doCheckStep2(validita);
			}
			
			if(verifica[2]){
				validita = this.doCheckStep3(validita, totRifiuti);
			}
			
			if(verifica[3]){
				validita = this.doCheckStep4(validita, totRifiutiEsenzioni);
			}
			
			if(verifica[4]){
				validita = this.doCheckStep5(validita, totRifEseRid);
			}
			
			if(verifica[5]){
				validita = this.doCheckStep6(validita);
			}
			
			if(this.getNumOspitiModuliFirma()==0 && this.getNumOspitiFirma()>0 && this.currentStep!=null){
				String msg = "Attenzione: Anomalia al Passo 2 (IMPOSTA) - OSPITI CHE RIFIUTANO IL PAGAMENTO CON FIRMA DEL DOCUMENTO: " +
						"\n Si è scelto di non inserire i moduli di rifiuto.";
				
				FacesContext facesContext = FacesContext.getCurrentInstance();
				PageBean pageBean = (PageBean) getBeanReference("pageBean");
				
				String sPage = null;
				
				if(!validita){
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, ""));
					sPage = "/jsp/protected/content/dich"+currentStep+".xhtml";
					
				}else{
					if(new Integer(currentStep)<this.stepArray.length){
						facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg + "\n Per inserire i nominativi degli ospiti che rifiutano con firma tornare al Passo 2.", ""));
						sPage = "/jsp/protected/content/dich"+(new Integer(currentStep).intValue()+1)+".xhtml";
					}
					
					
				}
				
				if(new Integer(currentStep)<this.stepArray.length){
					if(this.idSelezionato!=null)
						pageBean.setPageCenterDati(sPage);
					else
						pageBean.setPageCenter(sPage);
				}
			}
			
			//Incremento lo step corrente, per il passo successivo
			if(validita){
				Integer cs = new Integer(currentStep)+1;
				this.setCurrentStep(cs.toString());
			}
			
		return validita;
	}

	
	private boolean doCheckStep2(boolean validita){
		
		//controlli step 1
		if(dichiarazione.getPresenze()==null || 
			(dichiarazione.getPresenze()!=null && dichiarazione.getPresenze().compareTo(BigDecimal.ZERO)<0)){
			
			super.addWarningMessage("validita.ggPresenze");
			//pageBean.setPageCenterDati(currentPage);
			validita = false;	
		}
		
		if(dichiarazione.getArrivi()==null || 
				(dichiarazione.getArrivi()!=null && dichiarazione.getArrivi().compareTo(BigDecimal.ZERO)<0)){
				
				super.addWarningMessage("validita.ggArrivi");
				//pageBean.setPageCenterDati(currentPage);
				validita = false;	
			}
		
		if(dichiarazione.getArrivi()!=null && dichiarazione.getPresenze()!=null && 
				(dichiarazione.getArrivi().compareTo(dichiarazione.getPresenze())>0 ||
				 dichiarazione.getPresenze().compareTo(BigDecimal.ZERO)<0)){
			
			super.addWarningMessage("validita.arrPresenze");
			//pageBean.setPageCenterDati(currentPage);
			validita = false;	
		}
		
		int totRifiuti = 0;
		int nOspFirma =0;
		for (ModuloDTO dto : listaModuli1) {
			IsModuloOspiti mo = dto.getOspiti();
			
			if (dto.getDati().getId()==11){ 
				if(dto.getOspiti().getNOspiti() == null) dto.getOspiti().setNOspiti(new BigDecimal(0));
				
				nOspFirma = dto.getOspiti().getNOspiti().intValue();
			}
			
			totRifiuti += mo.getNSoggiorni()!=null ? mo.getNSoggiorni().intValue() : 0 ;
			
			if (mo.getNSoggiorni() != null && mo.getNOspiti() != null && mo.getNSoggiorni().intValue() < mo.getNOspiti().intValue()) {
				super.addWarningMessage("validita.ngiorni");
				//pageBean.setPageCenterDati(currentPage);
				validita = false;
			}
			
			if ((mo.getNSoggiorni() != null && mo.getNSoggiorni().intValue() >0) && (mo.getNOspiti() != null && mo.getNOspiti().intValue()<=0)) {
				super.addWarningMessage("validita.ngiorniZero");
				//pageBean.setPageCenterDati(currentPage);
				validita = false;
			}
		}
		
		//Il numero di giorni di soggiorno dei rifiuti <= num.presenze
		if(dichiarazione.getPresenze()!=null && dichiarazione.getPresenze().intValue()<totRifiuti){
			super.addWarningMessage("validita.rifiuti");
			validita = false;
		}
		
		boolean rifiutiNonCompilati = false;
		boolean rifiutoValReqCompilato = true;
		boolean gruppoAllReqCompilato = true;
		boolean dateSoggiornoOrd = true;
		boolean dateSoggiornoRange = true;
		Date[] periodo = getIntervalloPeriodoDich();
		for(RifiutoDTO dto : listaRifiuti){
			
			if(!dto.isRifiutoValorizzato())
				rifiutiNonCompilati = true;
			//else{
			if(!dto.isRifiutoValReq())
				rifiutoValReqCompilato = false;
			
			if(!dto.isDateSoggiornoInRange(periodo[0], periodo[1]))
				dateSoggiornoRange = false;
			
			if(!dto.isDateSoggiornoOrdinate())
				dateSoggiornoOrd=false;
			
			if(!dto.isGruppoValReq())
				gruppoAllReqCompilato = false;
			//}
		}
		
		if(!dateSoggiornoRange){
			SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
			String msg = "Attenzione: le date di inizio e fine soggiorno devono essere comprese nell'intervallo del Periodo di Riferimento:  ("+SDF.format(periodo[0])+" - "+SDF.format(periodo[1])+")";
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, ""));
			//pageBean.setPageCenterDati(currentPage);
			validita = false;
		}
		
		if(!dateSoggiornoOrd){
			super.addWarningMessage("validita.dateSoggiorno");
			//pageBean.setPageCenterDati(currentPage);
			validita = false;
		}
		
		if(!rifiutoValReqCompilato || rifiutiNonCompilati){
			super.addWarningMessage("validita.modRifiutiRequired");
			//pageBean.setPageCenterDati(currentPage);
			validita = false;
		}
		
		if(!gruppoAllReqCompilato){
			super.addWarningMessage("validita.modRifiutiGruppoRequired");
			//pageBean.setPageCenterDati(currentPage);
			validita = false;
		}
		
		//Se inserisco almeno una dichiarazione, devo inserirle tutte
		if(this.getNumOspitiModuliFirma()>0 && this.getNumOspitiFirma()!=this.getNumOspitiModuliFirma()){
			String msg = "Attenzione: Il totale degli ospiti per cui è stato compilata la dichiarazione di rifiuto al pagamento (="+getNumOspitiModuliFirma()+") " +
					"non è coerente con il valore dichiarato nel campo 'n.ospiti' (="+getNumOspitiFirma()+").";
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, ""));
			//pageBean.setPageCenterDati(currentPage);
			validita = false;
		}
		
		String currentPage = "/jsp/protected/content/dich2.xhtml";
		PageBean pageBean = (PageBean) getBeanReference("pageBean");
		
		if(!validita && this.idSelezionato!=null)
			pageBean.setPageCenterDati(currentPage);
		else if(!validita && this.idSelezionato==null)
			pageBean.setPageCenter(currentPage);
		
		return validita;
		
	}
	
	private int getTotaleGiorniRifiuto(){
		int totRifiuti = 0;
		
		for (ModuloDTO dto : listaModuli1) {
			IsModuloOspiti mo = dto.getOspiti();
			totRifiuti += mo.getNSoggiorni()!=null ? mo.getNSoggiorni().intValue() : 0 ;
		}
		return totRifiuti;
	}
	
	private int getTotGiorniEsenzioni(){
		
		int totggEsenzioni = 0;
		
		for (ModuloDTO dto : listaModuli2) {
			IsModuloOspiti mo = dto.getOspiti();
			
			totggEsenzioni += (mo.getNSoggiorni()!=null ?  mo.getNSoggiorni().intValue() : 0 );
		}
			
		return totggEsenzioni;
	}
	
	private int getTotGiorniRiduzioni(){
		
		int totggF1 = 0;
		int totggF2 = 0;
		int totggE = 0;
		
		for (ModuloDTO dto : listaModuli3) {
			List<IsModuloEventi> listame = dto.getEventi();
			
			for(IsModuloEventi me: listame){
				
				if(dto.getDati().getFkIsTabModulo().equals("IS_MODULO_OSPITI_RIDUZ"))
					totggE += me.getNSoggiorni()!=null ? me.getNSoggiorni().intValue() : 0 ;
				else{ 
					
					int ggSogg = me.getNSoggiorni()!=null ? me.getNSoggiorni().intValue() : 0 ;
					
					if(dto.getDati().getFkIsTabModulo().equals("IS_RIDUZ_CONGRESSI"))
						totggF1 += ggSogg;			
					else if(dto.getDati().getFkIsTabModulo().equals("IS_RIDUZ_CONGRESSI2"))
						totggF2 += ggSogg;	
				}
			}
		}
		
		return  totggF1+totggF2+totggE;
	}
	
	private boolean doCheckStep3(boolean validita, int ggRifiuto){
		
		int totggEsenzioni = 0;
		
		for (ModuloDTO dto : listaModuli2) {
			IsModuloOspiti mo = dto.getOspiti();
			
			totggEsenzioni += (mo.getNSoggiorni()!=null ?  mo.getNSoggiorni().intValue() : 0 );
			
			if (mo.getNSoggiorni() != null
					&& mo.getNOspiti() != null
					&& mo.getNSoggiorni().intValue() < mo.getNOspiti().intValue()) {
				super.addWarningMessage("validita.ngiorni");
				validita = false;
			}
			
			if (mo.getNSoggiorni() != null && mo.getNSoggiorni().intValue()<0) {
				super.addWarningMessage("validita.ggSoggiorno");
				validita = false;
			}
			
			if (mo.getNOspiti() != null && mo.getNOspiti().intValue()<0) {
				super.addWarningMessage("validita.nnOspiti");
				validita = false;
			}
			
		}
		
		if(totggEsenzioni > dichiarazione.getPresenze().intValue() - ggRifiuto){
			super.addWarningMessage("validita.esenzioni");
			validita = false;
		}
		
			
		String currentPage = "/jsp/protected/content/dich3.xhtml";
		PageBean pageBean = (PageBean) getBeanReference("pageBean");
		
		if(!validita && this.idSelezionato!=null)
			pageBean.setPageCenterDati(currentPage);
		else if(!validita && this.idSelezionato==null)
			pageBean.setPageCenter(currentPage);
		
		return validita;
		
	}
	
	private boolean doCheckStep4(boolean validita, int totRifEse){
		
		DichiarazioneService dichiarazioneService = super.getDichiarazioneService();
		
		int totggF1 = 0;
		int totggF2 = 0;
		int totggE = 0;
		int totRiduzioni=0;
		
		for (ModuloDTO dto : listaModuli3) {
			List<IsModuloEventi> listame = dto.getEventi();
			
			for(IsModuloEventi me: listame){
				if (me.getNSoggiorni() != null
						&& me.getNOspiti() != null
						&& me.getNSoggiorni().intValue() < me.getNOspiti()
								.intValue()) {
					super.addWarningMessage("validita.ngiorni");
					validita = false;
				}
				
				if (me.getNSoggiorni() != null && me.getNSoggiorni().intValue()<0) {
					super.addWarningMessage("validita.ggSoggiorno");
					validita = false;
				}
				
				if (me.getNOspiti() != null && me.getNOspiti().intValue()<0) {
					super.addWarningMessage("validita.nnOspiti");
					validita = false;
				}
				
			
					
				if(dto.getDati().getFkIsTabModulo().equals("IS_MODULO_OSPITI_RIDUZ")){
				
					totggE += me.getNSoggiorni()!=null ? me.getNSoggiorni().intValue() : 0 ;
					
				}else{ 
					
					DataInDTO dataIn = new DataInDTO();
					fillEnte(dataIn);
					dataIn.setId2(dto.getDati().getFkIsTabModulo());
					
					BigDecimal percRid = dichiarazioneService.getRiduzioneByCodModulo(dataIn);
					double perc = percRid!=null ? percRid.doubleValue()/100 : 1 ;
					
					int ggSogg = me.getNSoggiorni()!=null ? me.getNSoggiorni().intValue() : 0 ;
					
					double val = ggSogg * this.getValTariffa().intValue() * perc;
					double val5 = val * 0.05d;
					double pagato = me.getField2()!=null && !me.getField2().equals("") ? new BigDecimal(me.getField2()).doubleValue() : 0d ;
					
					if(dto.getDati().getFkIsTabModulo().equals("IS_RIDUZ_CONGRESSI")){
						
						totggF1 += ggSogg;
						
						if(ggSogg < 50 && ggSogg>0){
							super.addWarningMessage("validita.ggSoggF1");
							validita = false;
						}
							
						//Totale imposta già assolta deve essere non inferiore al n.giorni*tariffa*%riduzione (-5%)
						//Il valore max non è previsto: il congresso può aver pagato, ma è possibile che i partecipanti non abbiano poi pernottato --> imposta>> del valore calcolato
						if(pagato < (val-val5)){
							super.addWarningMessage("validita.impostaF1");
							validita = false;
						}
					
					}else if(dto.getDati().getFkIsTabModulo().equals("IS_RIDUZ_CONGRESSI2")){
						
						totggF2 += me.getNSoggiorni()!=null ? me.getNSoggiorni().intValue() : 0 ;
						
						if(pagato > (val + val5) || pagato < (val-val5)){
							super.addWarningMessage("validita.impostaF2");
							validita = false;
						}
						
					}
				}
				
			}
		}
		
	
		totRiduzioni = totggF1+totggF2+totggE;
		
		if(totRiduzioni > dichiarazione.getPresenze().intValue() - totRifEse){
			super.addWarningMessage("validita.riduzioni");
			validita = false;
		}
		
		//Imposto la pagina di errore (la stessa di provenienza)
		String currentPage = "/jsp/protected/content/dich4.xhtml";
		PageBean pageBean = (PageBean) getBeanReference("pageBean");
		
		if(!validita && this.idSelezionato!=null)
			pageBean.setPageCenterDati(currentPage);
		else if(!validita && this.idSelezionato==null)
			pageBean.setPageCenter(currentPage);
		
		return validita;
		
	}
	
	private boolean doCheckStep5(boolean validita, int totRifEseRid){
		
		PageBean pageBean = (PageBean) getBeanReference("pageBean");
		
		int totggSogg = 0;
		
		for (ModuloDTO dto : listaModuli4) {
			List<IsModuloEventi> listame = dto.getEventi();
			for(IsModuloEventi me: listame){
				
				if (me.getNSoggiorni() != null && me.getNSoggiorni().intValue()<0) {
					super.addWarningMessage("validita.ggSoggiorno");
					validita = false;
				}
				
				if (me.getNOspiti() != null && me.getNOspiti().intValue()<0) {
					super.addWarningMessage("validita.nnOspiti");
					validita = false;
				}
				
				if (me.getNSoggiorni() != null && me.getNOspiti() != null && 
						me.getNSoggiorni().intValue() < me.getNOspiti().intValue()) {
					super.addWarningMessage("validita.ngiorni");
					//pageBean.setVisDati(true);
					validita = false;
					/*if (idSelezionato == null) {
						pageBean.setPageCenter("/jsp/protected/content/dich.xhtml");
					}*/
				}
				
				
				int ggSogg = me.getNSoggiorni()!=null ? me.getNSoggiorni().intValue() : 0 ;
				if(ggSogg < 50 && ggSogg>0){
					super.addWarningMessage("validita.ggSoggPA");
					validita = false;
				}
				totggSogg += ggSogg;
				
			}
		}
		
		
		if(totggSogg > dichiarazione.getPresenze().intValue() - totRifEseRid){
			super.addWarningMessage("validita.pagAnticipati");
			validita = false;
		}
		
		
		//Imposto la pagina di errore (la stessa di provenienza)
		String currentPage = "/jsp/protected/content/dich5.xhtml";
		
		if(!validita && this.idSelezionato!=null)
			pageBean.setPageCenterDati(currentPage);
		else if(!validita && this.idSelezionato==null)
			pageBean.setPageCenter(currentPage);
		
		
		return validita;
		
	}
	
	public boolean doCheckStep6(boolean validita){
		
		if(dichiarazione.getIntegrativaInt()==1 && dichiarazione.getPresenze().intValue()==0){
			//Non effettuo controlli
		}else if(impDovuta.getImporto()!=null && impIncassata.getImporto()!=null && 
				impDovuta.getImporto().doubleValue() < impIncassata.getImporto().doubleValue()){
			super.addWarningMessage("validita.impInc2Dovuta1");
			validita=false;
		}
		
		if((impIncassata.getImporto()!=null && impIncassata.getImporto().doubleValue()<0) ||
		   (impIncassata.getImportoMesiPrec()!=null && impIncassata.getImportoMesiPrec().doubleValue() < 0)){
			super.addWarningMessage("validita.impostaIncassata");
			validita=false;
		}
		
		if(impDovuta.getImportoMesiPrec()!=null && impIncassata.getImportoMesiPrec()!=null && 
				impDovuta.getImporto().doubleValue() < impIncassata.getImporto().doubleValue()){
			super.addWarningMessage("validita.impInc5Dovuta4");
			validita=false;
		}
		
		
		//Imposto la pagina di errore (la stessa di provenienza)
		String currentPage = "/jsp/protected/content/dich6.xhtml";
		PageBean pageBean = (PageBean) getBeanReference("pageBean");
		if(!validita && this.idSelezionato!=null)
			pageBean.setPageCenterDati(currentPage);
		else if(!validita && this.idSelezionato==null)
			pageBean.setPageCenter(currentPage);
		
		return validita;
	}
	
	public void doSaveDichiarazione() {
		try {
			
			DichiarazioneService dichiarazioneService = super.getDichiarazioneService();
			
			boolean validita = doCheckDati();

			if(validita){
				
				this.doSaveAnagrafica();
				
				DataInDTO dataIn = new DataInDTO();
				fillEnte(dataIn);
				doValoriCalcolati();
				if (idSelezionato == null) {
					// nuovo
					dataIn.setObj(snap);
					snap = dichiarazioneService.saveStrutturaSnap(dataIn);
					dichiarazione.setFkIsStrutturaSnap(new BigDecimal(snap.getId()));
					dichiarazione.setTotIncassato(totIncassato);
					dichiarazione.setTotResiduo(totSospeso);
					dichiarazione.setValoreTariffa(valTariffa);
					dichiarazione.setCompensazione(dichiarazione.getValRimborso()); //TODO:verificare
					dataIn.setObj(dichiarazione);
					dichiarazione = dichiarazioneService.saveDichiarazione(dataIn);
					// imp dovuta
					impDovuta.setUsrIns(codFiscale);
					impDovuta.setDtIns(new Date());
					impDovuta.setFkIsDichiarazione(new BigDecimal(dichiarazione
							.getId()));
					impDovuta.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
					dataIn.setObj(impDovuta);
					dichiarazioneService.saveImpostaDovuta(dataIn);
					// imp incassata
					impIncassata.setUsrIns(codFiscale);
					impIncassata.setDtIns(new Date());
					impIncassata.setFkIsDichiarazione(new BigDecimal(dichiarazione
							.getId()));
					impIncassata.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
					dataIn.setObj(impIncassata);
					dichiarazioneService.saveImpostaIncassata(dataIn);
					// versato
					for (ModuloDTO dto : listaModuli1) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo.getNOspiti() != null || mo.getNSoggiorni() != null) {
							mo.setUsrIns(codFiscale);
							mo.setDtIns(new Date());
							mo.setFkIsDichiarazione(new BigDecimal(dichiarazione
									.getId()));
							mo.setFkIsModuloDati(new BigDecimal(dto.getDati()
									.getId()));
							mo.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
							dataIn.setObj(mo);
							dichiarazioneService.saveModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								me.setUsrIns(codFiscale);
								me.setDtIns(new Date());
								me.setFkIsDichiarazione(new BigDecimal(
										dichiarazione.getId()));
								me.setFkIsModuloDati(new BigDecimal(dto.getDati()
										.getId()));
								me.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
								dataIn.setObj(me);
								dichiarazioneService.saveModuloEventi(dataIn);
							}
						}
					}
					//Lista Rifiuti
					for(RifiutoDTO dto : listaRifiuti){
						dto.getRifiuto().setFkIsDichiarazione(new BigDecimal(dichiarazione.getId()));
						dataIn.setObj(dto);
						dichiarazioneService.saveRifiuto(dataIn);
					}
					
					// esenzioni
					for (ModuloDTO dto : listaModuli2) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo.getNOspiti() != null || mo.getNSoggiorni() != null) {
							mo.setUsrIns(codFiscale);
							mo.setDtIns(new Date());
							mo.setFkIsDichiarazione(new BigDecimal(dichiarazione
									.getId()));
							mo.setFkIsModuloDati(new BigDecimal(dto.getDati()
									.getId()));
							mo.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
							dataIn.setObj(mo);
							dichiarazioneService.saveModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								me.setUsrIns(codFiscale);
								me.setDtIns(new Date());
								me.setFkIsDichiarazione(new BigDecimal(
										dichiarazione.getId()));
								me.setFkIsModuloDati(new BigDecimal(dto.getDati()
										.getId()));
								me.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
								dataIn.setObj(me);
								dichiarazioneService.saveModuloEventi(dataIn);
							}
						}
					}
					// riduzioni
					for (ModuloDTO dto : listaModuli3) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo.getNOspiti() != null || mo.getNSoggiorni() != null) {
							mo.setUsrIns(codFiscale);
							mo.setDtIns(new Date());
							mo.setFkIsDichiarazione(new BigDecimal(dichiarazione
									.getId()));
							mo.setFkIsModuloDati(new BigDecimal(dto.getDati()
									.getId()));
							mo.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
							dataIn.setObj(mo);
							dichiarazioneService.saveModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								me.setUsrIns(codFiscale);
								me.setDtIns(new Date());
								me.setFkIsDichiarazione(new BigDecimal(
										dichiarazione.getId()));
								me.setFkIsModuloDati(new BigDecimal(dto.getDati()
										.getId()));
								me.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
								dataIn.setObj(me);
								dichiarazioneService.saveModuloEventi(dataIn);
							}
						}
					}
					// pag anticipati
					for (ModuloDTO dto : listaModuli4) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo.getNOspiti() != null || mo.getNSoggiorni() != null) {
							mo.setUsrIns(codFiscale);
							mo.setDtIns(new Date());
							mo.setFkIsDichiarazione(new BigDecimal(dichiarazione
									.getId()));
							mo.setFkIsModuloDati(new BigDecimal(dto.getDati()
									.getId()));
							mo.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
							dataIn.setObj(mo);
							dichiarazioneService.saveModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								me.setUsrIns(codFiscale);
								me.setDtIns(new Date());
								me.setFkIsDichiarazione(new BigDecimal(
										dichiarazione.getId()));
								me.setFkIsModuloDati(new BigDecimal(dto.getDati()
										.getId()));
								me.setFkIsPeriodo(dichiarazione.getFkIsPeriodo());
								dataIn.setObj(me);
								dichiarazioneService.saveModuloEventi(dataIn);
							}
						}
					}
				} else {
					// update
					dataIn.setObj(snap);
					dichiarazioneService.updateStrutturaSnap(dataIn);
					dichiarazione.setFkIsStrutturaSnap(new BigDecimal(snap.getId()));
					dichiarazione.setTotIncassato(totIncassato);
					dichiarazione.setTotResiduo(totSospeso);
					dichiarazione.setValoreTariffa(valTariffa);
					dichiarazione.setUsrMod(codFiscale);
					dichiarazione.setDtMod(new Date());
					dataIn.setObj(dichiarazione);
					dichiarazioneService.updateDichiarazione(dataIn);
					
					// imp dovuta
					impDovuta.setUsrMod(codFiscale);
					impDovuta.setDtMod(new Date());
					dataIn.setObj(impDovuta);
					dichiarazioneService.updateImpostaDovuta(dataIn);
					// imp incassata
					impIncassata.setUsrMod(codFiscale);
					impIncassata.setDtMod(new Date());
					dataIn.setObj(impIncassata);
					dichiarazioneService.updateImpostaIncassata(dataIn);
					// azzero eventi per ripopolari
					dataIn.setId(new Long(idSelezionato));
					dichiarazioneService.deleteModuloEventiByDich(dataIn);
					// versato
					for (ModuloDTO dto : listaModuli1) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo.getNOspiti() != null || mo.getNSoggiorni() != null) {
							mo.setUsrMod(codFiscale);
							mo.setDtMod(new Date());
							dataIn.setObj(mo);
							dichiarazioneService.updateModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								if (me.getId() != null) {
									me.setUsrMod(codFiscale);
									me.setDtMod(new Date());
									dataIn.setObj(me);
									dichiarazioneService.updateModuloEventi(dataIn);
								} else {
									me.setUsrIns(codFiscale);
									me.setDtIns(new Date());
									me.setFkIsDichiarazione(new BigDecimal(
											dichiarazione.getId()));
									me.setFkIsModuloDati(new BigDecimal(dto
											.getDati().getId()));
									me.setFkIsPeriodo(dichiarazione
											.getFkIsPeriodo());
									dataIn.setObj(me);
									dichiarazioneService.saveModuloEventi(dataIn);
								}
							}
						}
					}
					//Lista Rifiuti
					for(RifiutoDTO dto : listaRifiuti)
						dto.getRifiuto().setFkIsDichiarazione(new BigDecimal(dichiarazione.getId()));
					dataIn.setId(dichiarazione.getId());
					dataIn.setObj(listaRifiuti);
					dichiarazioneService.updateListaRifiuti(dataIn);
					
					// esenzioni
					for (ModuloDTO dto : listaModuli2) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo.getNOspiti() != null || mo.getNSoggiorni() != null) {
							mo.setUsrMod(codFiscale);
							mo.setDtMod(new Date());
							dataIn.setObj(mo);
							dichiarazioneService.updateModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								if (me.getId() != null) {
									me.setUsrMod(codFiscale);
									me.setDtMod(new Date());
									dataIn.setObj(me);
									dichiarazioneService.updateModuloEventi(dataIn);
								} else {
									me.setUsrIns(codFiscale);
									me.setDtIns(new Date());
									me.setFkIsDichiarazione(new BigDecimal(
											dichiarazione.getId()));
									me.setFkIsModuloDati(new BigDecimal(dto
											.getDati().getId()));
									me.setFkIsPeriodo(dichiarazione
											.getFkIsPeriodo());
									dataIn.setObj(me);
									dichiarazioneService.saveModuloEventi(dataIn);
								}
							}
						}
					}
					// riduzioni
					for (ModuloDTO dto : listaModuli3) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo != null
								&& (mo.getNOspiti() != null || mo.getNSoggiorni() != null)) {
							mo.setUsrMod(codFiscale);
							mo.setDtMod(new Date());
							dataIn.setObj(mo);
							dichiarazioneService.updateModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								if (me.getId() != null) {
									me.setId(null);
									me.setUsrMod(codFiscale);
									me.setDtMod(new Date());
									dataIn.setObj(me);
									dichiarazioneService.saveModuloEventi(dataIn);
								} else {
									me.setUsrIns(codFiscale);
									me.setDtIns(new Date());
									me.setFkIsDichiarazione(new BigDecimal(
											dichiarazione.getId()));
									me.setFkIsModuloDati(new BigDecimal(dto
											.getDati().getId()));
									me.setFkIsPeriodo(dichiarazione
											.getFkIsPeriodo());
									dataIn.setObj(me);
									dichiarazioneService.saveModuloEventi(dataIn);
								}
							}
						}
					}
					// pag anticipati
					for (ModuloDTO dto : listaModuli4) {
						IsModuloOspiti mo = dto.getOspiti();
						if (mo != null
								&& (mo.getNOspiti() != null || mo.getNSoggiorni() != null)) {
							mo.setUsrMod(codFiscale);
							mo.setDtMod(new Date());
							dataIn.setObj(mo);
							dichiarazioneService.updateModuloOspiti(dataIn);
						}
						for (IsModuloEventi me : dto.getEventi()) {
							if (me.getEvento() != null || me.getNOspiti() != null
									|| me.getNSoggiorni() != null
									|| me.getField1() != null
									|| me.getField2() != null) {
								if (me.getId() != null) {
									me.setId(null);
									me.setUsrMod(codFiscale);
									me.setDtMod(new Date());
									dataIn.setObj(me);
									dichiarazioneService.saveModuloEventi(dataIn);
								} else {
									me.setUsrIns(codFiscale);
									me.setDtIns(new Date());
									me.setFkIsDichiarazione(new BigDecimal(
											dichiarazione.getId()));
									me.setFkIsModuloDati(new BigDecimal(dto
											.getDati().getId()));
									me.setFkIsPeriodo(dichiarazione
											.getFkIsPeriodo());
									dataIn.setObj(me);
									dichiarazioneService.saveModuloEventi(dataIn);
								}
							}
						}
					}
				}
				criteria = new DichiarazioneSearchCriteria();
	
				super.addInfoMessage("salvataggio.ok");
				
				if(dichiarazione.getStato().equals(statoChiusa))
					super.addInfoMessage("chiusura.ok");
			
			}
		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doDeleteDichiarazione() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(new Long(idSelezionato));
			super.getDichiarazioneService().deleteDichiarazioneById(dataIn);
			criteria = new DichiarazioneSearchCriteria();

			super.addInfoMessage("salvataggio.ok");
		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doStampaDichiarazione() {
		try {

			DichiarazioneService dichiarazioneService = super.getDichiarazioneService();
			
			//carico e preparo parametri
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsConfig> listaParam = dichiarazioneService.getConfig(dataIn);
			HashMap<String, String> hashParam = new HashMap<String, String>();
			IsConfig logoComune = null;
			for(IsConfig conf: listaParam){
				if(conf.getChiave().equals("LOGO_COMUNE"))
					logoComune = conf;
				hashParam.put(conf.getChiave(), conf.getValore());
			}
			
			Document document = new Document(PageSize.A4, 60, 60, 80, 60);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			document.open();

			// header e footer
			if (anteprimaStampa) {
				anteprimaStampa = false;
				// add watermark
				DichiarazionePDFPageEventFS d = new DichiarazionePDFPageEventFS();
				d.setHashParametri(hashParam);
				d.setLogo(logoComune);
				writer.setPageEvent(d);
			} else {
				DichiarazionePDFPageEvent d = new DichiarazionePDFPageEvent();
				d.setHashParametri(hashParam);
				d.setLogo(logoComune);
				writer.setPageEvent(d);
			}
			
			// load dati dichiarazione
			DichiarazioneDTO dichDto = new DichiarazioneDTO();
			if (idSelezionato != null && !anteprimaStampaIter) {
				dataIn.setId(new Long(idSelezionato));
				dichDto = dichiarazioneService.getDichiarazioneById(dataIn);
				listaModuli1 = dichiarazioneService
						.getLoadModuliVersato(dataIn);
				listaModuli2 = dichiarazioneService
						.getLoadModuliEsenzioni(dataIn);
				listaModuli3 = dichiarazioneService
						.getLoadModuliRiduzioni(dataIn);
				listaModuli4 = dichiarazioneService
						.getLoadModuliPagAnticipati(dataIn);

				impDovuta = dichiarazioneService
						.getImpostaDovutaByDichiarazione(dataIn);
				impIncassata = dichiarazioneService
						.getImpostaIncassataByDichiarazione(dataIn);
			} else {
				doValoriCalcolati();
				dichDto.setDichiarazione(dichiarazione);
				dichDto.setStrutturaSnap(snap);
				dichDto.setPeriodo(new IsPeriodo());
				dichDto.getPeriodo().setDescrizione(periodoDescr);
				dichDto.setListaRimborsi(listaRimborsi);
				dichDto.setListaSanzioni(listaSanzioni);
				anteprimaStampaIter=false;
			}

			// prepare export
			DichiarazionePDF pdfEx = new DichiarazionePDF();
			pdfEx.setDichDTO(dichDto);
			pdfEx.setListaModuli1(listaModuli1);
			pdfEx.setListaModuli2(listaModuli2);
			pdfEx.setListaModuli3(listaModuli3);
			pdfEx.setListaModuli4(listaModuli4);
			pdfEx.setImpDovuta(impDovuta);
			pdfEx.setImpIncassata(impIncassata);
			dataIn.setId(dichDto.getStrutturaSnap().getFkIsSocieta()
					.longValue());
			//IsSocietaSogg ss = anagraficaService.getSocietaSoggById(dataIn);
			pdfEx.setDescrTitoloSoc(dichDto.getStrutturaSnap().getDescrizioneTitolo());
			pdfEx.setHashParametri(hashParam);
			
			document = pdfEx.getPDFDocument(document);

			document.close();

			if (baos != null) {
				String nomePdf = dichDto.getPeriodo().getDescrizione()
						+ " - "
						+ dichDto.getStrutturaSnap().getDescrizioneStrut()
						+ "_"
						+ dichDto.getStrutturaSnap()
								.getDescrizioneClasseStrut();
				if (dichDto.getDichiarazione().getIntegrativa().intValue() == 1)
					nomePdf += " INTEGR.";
				FacesContext faces = FacesContext.getCurrentInstance();
				HttpServletResponse response = (HttpServletResponse) faces
						.getExternalContext().getResponse();

				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");
				response.addHeader("Content-disposition",
						"attachment;filename=\"" + nomePdf + ".pdf");

				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				baos.flush();

				response.setContentLength(baos.size());
				out.close();
				faces.responseComplete();
			}

		} catch (Throwable t) {
			super.addErrorMessage("elaborazione.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doStampaReportPeriodo() {
		try {

			Document document = new Document(PageSize.A4.rotate(), 0, 0, 60, 60);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			document.open();

			// load dati dichiarazioni
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(new Long(1));
			dataIn.setId3(new Long(4));
			List<DichiarazioneDTO> lista = super.getDichiarazioneService().getDichiarazioniByPeriodoDalAl(dataIn);

			// prepare export
			ReportPeriodoPDF pdfEx = new ReportPeriodoPDF();
			pdfEx.setListaDichDTO(lista);
			pdfEx.setDal("Novembre 2012");
			pdfEx.setAl("Febbraio 2013");
			document = pdfEx.getPDFDocument(document);

			document.close();

			if (baos != null) {
				String nomePdf = "Report Periodo";
				FacesContext faces = FacesContext.getCurrentInstance();
				HttpServletResponse response = (HttpServletResponse) faces
						.getExternalContext().getResponse();

				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");
				response.addHeader("Content-disposition",
						"attachment;filename=\"" + nomePdf + ".pdf");

				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				baos.flush();

				response.setContentLength(baos.size());
				out.close();
				faces.responseComplete();
			}

		} catch (Throwable t) {
			super.addErrorMessage("elaborazione.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doChiudiDichiarazione() {
		try {
			
			DichiarazioneService dichiarazioneService = super.getDichiarazioneService();

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(new Long(idSelezionato));
			DichiarazioneDTO dichDto = dichiarazioneService.getDichiarazioneById(dataIn);
			IsDichiarazione dich = dichDto.getDichiarazione();
			dich.setStato(statoChiusa);
			dataIn.setObj(dich);
			dichiarazioneService.updateDichiarazione(dataIn);
			super.addInfoMessage("chiusura.ok");

		} catch (Throwable t) {
			super.addErrorMessage("elaborazione.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}
	
	public void doUpLoadDichiarazione(Long idDocumento){
		try {
			
			DichiarazioneService dichiarazioneService = super.getDichiarazioneService();
			
			BigDecimal idFile = new BigDecimal(idDocumento);
						
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(new Long(idSelezionato));
			DichiarazioneDTO dichDto = dichiarazioneService.getDichiarazioneById(dataIn);
			IsDichiarazione dich = dichDto.getDichiarazione();
			dich.setFkDocumento(idFile);
			dataIn.setObj(dich);
			dichiarazioneService.updateDichiarazione(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("elaborazione.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}
	
	public void doDownloadDichiarazione(){
		//TODO 
		
		DichiarazioneService dichiarazioneService = super.getDichiarazioneService();
		
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(new Long(idSelezionato));
			DichiarazioneDTO dichDto = dichiarazioneService.getDichiarazioneById(dataIn);
			
			if (baos != null) {
				String nomePdf = dichDto.getPeriodo().getDescrizione()
						+ " - "
						+ dichDto.getStrutturaSnap().getDescrizioneStrut()
						+ "_"
						+ dichDto.getStrutturaSnap()
								.getDescrizioneClasseStrut();
				if (dichDto.getDichiarazione().getIntegrativa().intValue() == 1)
					nomePdf += " INTEGR.";
				FacesContext faces = FacesContext.getCurrentInstance();
				HttpServletResponse response = (HttpServletResponse) faces
						.getExternalContext().getResponse();
	
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");
				response.addHeader("Content-disposition",
						"attachment;filename=\"" + nomePdf + ".pdf");
	
				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				baos.flush();
	
				response.setContentLength(baos.size());
				out.close();
				faces.responseComplete();
			}
		
		} catch (Throwable t) {
			super.addErrorMessage("elaborazione.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doAddRowEventi() {
		if ("THREE".equals(listaModuliStep))
			listaModuli3.get(new Integer(listaModuliRow)).getEventi()
					.add(new IsModuloEventi());
		if ("FOUR".equals(listaModuliStep))
			listaModuli4.get(new Integer(listaModuliRow)).getEventi()
					.add(new IsModuloEventi());
	}

	public void doDeleteRowEventi() {
		if ("THREE".equals(listaModuliStep)) {
			int size = listaModuli3.get(new Integer(listaModuliRow))
					.getEventi().size();
			listaModuli3.get(new Integer(listaModuliRow)).getEventi()
					.remove(size - 1);
		}
		if ("FOUR".equals(listaModuliStep)) {
			int size = listaModuli4.get(new Integer(listaModuliRow))
					.getEventi().size();
			listaModuli4.get(new Integer(listaModuliRow)).getEventi()
					.remove(size - 1);
		}
	}

	public void doValoriCalcolati() {
		
		
		/*----------------------------------------CALCOLO RIEPILOGO----------------------------------*/
		
		//Imposta totale lorda per periodo di competenza
		impLordaComp = (dichiarazione.getPresenze()!=null ? dichiarazione.getPresenze().multiply(this.getValTariffa()) : new BigDecimal(0));
		
		//Esenzioni
		BigDecimal totEsenti = BigDecimal.ZERO;
		for (ModuloDTO dto : listaModuli2) {
			if (dto.getOspiti().getNSoggiorni() == null)
				dto.getOspiti().setNSoggiorni(new BigDecimal(0));
			totEsenti = totEsenti.add(dto.getOspiti().getNSoggiorni());
			visEsenzioni = true;
		}
		dichiarazione.setEsenti(totEsenti);
		impEsenzioni = totEsenti.multiply(this.getValTariffa());
		
		//Rifiuti
		totRifiuti = BigDecimal.ZERO;
		for (ModuloDTO dto : listaModuli1) {
			if (dto.getOspiti().getNSoggiorni() == null)
				dto.getOspiti().setNSoggiorni(new BigDecimal(0));
			totRifiuti = totRifiuti.add(dto.getOspiti().getNSoggiorni());
		}
		impRifiuti = totRifiuti.multiply(this.getValTariffa());
		
		//Riduzioni
		totRiduzioniE  = BigDecimal.ZERO;
		impRiduzioniE  = BigDecimal.ZERO;
		totRiduzioniF1 = BigDecimal.ZERO;
		impRiduzioniF1 = BigDecimal.ZERO;
		totRiduzioniF2 = BigDecimal.ZERO;
		impRiduzioniF2 = BigDecimal.ZERO;
		
		for (ModuloDTO dto : listaModuli3) {
			if (dto.getEventi() != null){
				for(IsModuloEventi ev : dto.getEventi()){
					
					if(ev.getNSoggiorni() == null) ev.setNSoggiorni(new BigDecimal(0));
					
					if(ev.getField2() == null || ev.getField2().equals("")) ev.setField2("0");
					
					if(dto.getDati().getFkIsTabModulo().equals("IS_MODULO_OSPITI_RIDUZ")){
						totRiduzioniE = totRiduzioniE.add(ev.getNSoggiorni());
						visRiduzioniE = true;
					}else if (dto.getDati().getFkIsTabModulo().equals("IS_RIDUZ_CONGRESSI")){
						visRiduzioniF1 = true;
						totRiduzioniF1 = totRiduzioniF1.add(ev.getNSoggiorni());
						impRiduzioniF1 = impRiduzioniF1.add(new BigDecimal(ev.getField2()));
				   }else if(dto.getDati().getFkIsTabModulo().equals("IS_RIDUZ_CONGRESSI2")){
					   visRiduzioniF2 = true;
					   totRiduzioniF2 = totRiduzioniF2.add(ev.getNSoggiorni());
					   impRiduzioniF2 = impRiduzioniF2.add(new BigDecimal(ev.getField2()));
				   }
				}
			}
		}
		
		impRiduzioniE = totRiduzioniE.multiply(new BigDecimal(0.5).multiply(valTariffa)); // giorni di soggiorno x 50% tariffa
		
		//Pagamenti Anticipati
		impPagAnticipati= BigDecimal.ZERO;
		for (ModuloDTO dto : listaModuli4) {
			if (dto.getEventi() != null){
				for(IsModuloEventi ev : dto.getEventi()){
					
					if(ev.getField2() == null || ev.getField2().equals("")) ev.setField2("0");
			
					impPagAnticipati = impPagAnticipati.add(new BigDecimal(ev.getField2()));
				}
			}
		}
		
		
		if (dichiarazione.getPresenze() != null)
			dichiarazione.setAssoggImposta(new BigDecimal(dichiarazione.getPresenze().doubleValue() - dichiarazione.getEsenti().longValue()));
		
		
		long valImpDovuta = impLordaComp.longValue() - impEsenzioni.longValue() - 
							impRifiuti.longValue() -  impRiduzioniE.longValue() -
							impRiduzioniF1.longValue() - impRiduzioniF2.longValue() - impPagAnticipati.longValue();
		
		this.impDovuta.setImporto(new BigDecimal(valImpDovuta));
						 
		
		/*----------------------------------------CALCOLO (C)----------------------------------*/
		
		//(3)=(1-2)
		if (impDovuta.getImporto() != null && impIncassata.getImporto() != null)
			impostaInSospeso = impDovuta.getImporto().subtract(impIncassata.getImporto());
		else
			impostaInSospeso = new BigDecimal(0);
		//(6)=(4-5)
		if (impDovuta.getImportoMesiPrec() != null && impIncassata.getImportoMesiPrec() != null)
			impostaInSospesoPrec = impDovuta.getImportoMesiPrec().subtract(impIncassata.getImportoMesiPrec());
		else
			impostaInSospesoPrec = new BigDecimal(0);
		//(9)=(2+5)
		if (impIncassata.getImporto() != null && impIncassata.getImportoMesiPrec() != null)
			totIncassato = impIncassata.getImporto().add(impIncassata.getImportoMesiPrec());
		else
			totIncassato = new BigDecimal(0);
		//(3+6)
		if (impostaInSospeso != null && impostaInSospesoPrec != null)
			totSospeso = impostaInSospeso.add(impostaInSospesoPrec);
		else
			totSospeso = new BigDecimal(0);

		//(9-7+8)
		this.calcolaSanzioniRimborsi();
		if(totIncassato!=null && dichiarazione.getValSanzione()!=null && dichiarazione.getValRimborso() !=null)
			totVersare = (totIncassato.add(dichiarazione.getValSanzione())).subtract(dichiarazione.getValRimborso());
		else
			totVersare = new BigDecimal(0);
		

	}
	
	private void calcolaSanzioniRimborsi(){
		
		DichiarazioneService dichiarazioneService = super.getDichiarazioneService();
		
		if(dichiarazione.getIntegrativaInt()==0){
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(snap.getFkIsStruttura().longValue());
			dataIn.setId2(snap.getFkIsClasse());
			dataIn.setId3(dichiarazione.getFkIsPeriodo().longValue());
			dataIn.setObj(this.idSelezionato); //Se è diverso da null esclude dal computo la dichiarazione corrente.
			
			BigDecimal totSanzioni =  dichiarazioneService.getTotSanzioniByPeriodoStrClasse(dataIn);
			BigDecimal totSanzioniDich = dichiarazioneService.getTotSanzioniDichByPeriodoStrClasse(dataIn);
			
			listaSanzioni = listaSanzioni==null ? dichiarazioneService.getSanzioniByPeriodoStrClasse(dataIn) : listaSanzioni;
			listaRimborsi = listaRimborsi==null ? dichiarazioneService.getRimborsiByPeriodoStrClasse(dataIn) : listaRimborsi;
			
			if(totSanzioni!=null && totSanzioniDich!=null && totSanzioni.compareTo(totSanzioniDich)>0)
				dichiarazione.setValSanzione(totSanzioni.subtract(totSanzioniDich));
			else
				dichiarazione.setValSanzione(BigDecimal.ZERO);
			
			BigDecimal totRimborsi =  dichiarazioneService.getTotRimborsiByPeriodoStrClasse(dataIn);
			BigDecimal totRimborsiDich = dichiarazioneService.getTotRimborsiDichByPeriodoStrClasse(dataIn);
			
			if(totRimborsi!=null && totRimborsiDich!=null && totRimborsi.compareTo(totRimborsiDich)>0)
				dichiarazione.setValRimborso(totRimborsi.subtract(totRimborsiDich));
			else
				dichiarazione.setValRimborso(BigDecimal.ZERO);
		}else{
			//Nella dichiarazione integrativa non valuto sanzioni/rimborsi
			dichiarazione.setValSanzione(BigDecimal.ZERO);
			dichiarazione.setValRimborso(BigDecimal.ZERO);
			
			listaSanzioni = null;
			listaRimborsi = null;
			
		}
	}

	public void doLoadSocieta() {
		
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setId(snap.getFkIsSocieta().longValue());
		societa = super.getAnagraficaService().getSocietaById(dataIn);
		doLoadStrutture();
		if (listaStrutture.size() > 0) {
			snap.setFkIsStruttura((BigDecimal) listaStrutture.get(0).getValue());
			doLoadStruttura();
			doLoadClassiStruttura();
			if (listaClassiStruttura.size() > 0)
				snap.setFkIsClasse((String) listaClassiStruttura.get(0)
						.getValue());
		}
	}

	public void doLoadStruttura() {
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setId(snap.getFkIsStruttura().longValue());
		struttura = super.getAnagraficaService().getStrutturaById(dataIn);
		doLoadClassiStruttura();
		snap.setFkIsClasse((String) listaClassiStruttura.get(0).getValue());
	}

	private void doLoadStrutture() {
		listaStrutture = new ArrayList<SelectItem>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		DichiarazioneSearchCriteria crit = new DichiarazioneSearchCriteria();
		crit.setIdSocieta(snap.getFkIsSocieta().longValue());
		dataIn.setObj(crit);
		List<StrutturaDTO> lista = super.getAnagraficaService()
				.getStrutturaByCriteria(dataIn);
		for (StrutturaDTO s : lista) {
			listaStrutture.add(new SelectItem(new BigDecimal(s.getStruttura()
					.getId()), s.getStruttura().getDescrizione()));
		}
	}

	private void doLoadClassiStruttura() {
		listaClassiStruttura = new ArrayList<SelectItem>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setId(snap.getFkIsStruttura().longValue());
		List<IsClassiStruttura> lista = super.getAnagraficaService()
				.getClassiByStruttura(dataIn);
		for (IsClassiStruttura s : lista) {
			listaClassiStruttura.add(new SelectItem(s.getId().getFkIsClasse(),
					s.getDescrizione()));
		}
	}

	public BigDecimal getImpLordaComp() {
		return impLordaComp;
	}

	public void setImpLordaComp(BigDecimal impLordaComp) {
		this.impLordaComp = impLordaComp;
	}

	public BigDecimal getImpEsenzioni() {
		return impEsenzioni;
	}

	public void setImpEsenzioni(BigDecimal impEsenzioni) {
		this.impEsenzioni = impEsenzioni;
	}

	public BigDecimal getTotRiduzioniE() {
		return totRiduzioniE;
	}

	public void setTotRiduzioniE(BigDecimal totRiduzioniE) {
		this.totRiduzioniE = totRiduzioniE;
	}

	public BigDecimal getImpRiduzioniE() {
		return impRiduzioniE;
	}

	public void setImpRiduzioniE(BigDecimal impRiduzioniE) {
		this.impRiduzioniE = impRiduzioniE;
	}

	public BigDecimal getTotRiduzioniF1() {
		return totRiduzioniF1;
	}

	public void setTotRiduzioniF1(BigDecimal totRiduzioniF1) {
		this.totRiduzioniF1 = totRiduzioniF1;
	}

	public BigDecimal getImpRiduzioniF1() {
		return impRiduzioniF1;
	}

	public void setImpRiduzioniF1(BigDecimal impRiduzioniF1) {
		this.impRiduzioniF1 = impRiduzioniF1;
	}

	public BigDecimal getTotRiduzioniF2() {
		return totRiduzioniF2;
	}

	public void setTotRiduzioniF2(BigDecimal totRiduzioniF2) {
		this.totRiduzioniF2 = totRiduzioniF2;
	}

	public BigDecimal getImpRiduzioniF2() {
		return impRiduzioniF2;
	}

	public void setImpRiduzioniF2(BigDecimal impRiduzioniF2) {
		this.impRiduzioniF2 = impRiduzioniF2;
	}

	public BigDecimal getImpPagAnticipati() {
		return impPagAnticipati;
	}

	public void setImpPagAnticipati(BigDecimal impPagAnticipati) {
		this.impPagAnticipati = impPagAnticipati;
	}

	private void loadSocieta() {
		listaSocieta = new ArrayList<SelectItem>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCodFiscale(codFiscale);
		List<SocietaDTO> lista = super.getAnagraficaService().getSocietaByCodFis(dataIn);
		for (SocietaDTO s : lista) {
			listaSocieta.add(new SelectItem(new BigDecimal(s.getSocieta()
					.getId()), s.getSocieta().getRagSoc()));
		}
	}

	private void loadClassi() {
		if (listaClassi == null || listaClassi.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsClasse> lista = super.getAnagraficaService().getClassi(dataIn);
			for (IsClasse c : lista) {
				listaClassi.add(new SelectItem(c.getCodice(), c
						.getDescrizione()));
			}
		}
	}

	private void loadPeriodi() {
		
		DichiarazioneService dichiarazioneService = super.getDichiarazioneService();
		
		if (listaPeriodi == null || listaPeriodi.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsPeriodo> lista = dichiarazioneService.getPeriodi(dataIn);
			for (IsPeriodo c : lista) {
				listaPeriodi.add(new SelectItem(c.getId(), c.getDescrizione()));
			}
		}
		if (listaPeriodiNuovaDich == null || listaPeriodiNuovaDich.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsPeriodo> lista = dichiarazioneService
					.getPeriodiNuovaDich(dataIn);
			for (IsPeriodo c : lista) {
				listaPeriodiNuovaDich.add(new SelectItem(c.getId(), c
						.getDescrizione()));
			}
		}
	}

	public DichiarazioneSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(DichiarazioneSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public List<SelectItem> getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List<SelectItem> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List<SelectItem> getListaStrutture() {
		return listaStrutture;
	}

	public void setListaStrutture(List<SelectItem> listaStrutture) {
		this.listaStrutture = listaStrutture;
	}

	public List<SelectItem> getListaSocieta() {
		return listaSocieta;
	}

	public void setListaSocieta(List<SelectItem> listaSocieta) {
		this.listaSocieta = listaSocieta;
	}

	public List<SelectItem> getListaPeriodi() {
		return listaPeriodi;
	}

	public void setListaPeriodi(List<SelectItem> listaPeriodi) {
		this.listaPeriodi = listaPeriodi;
	}

	public IsSocieta getSocieta() {
		return societa;
	}

	public void setSocieta(IsSocieta societa) {
		this.societa = societa;
	}

	public StrutturaDTO getStruttura() {
		return struttura;
	}

	public void setStruttura(StrutturaDTO struttura) {
		this.struttura = struttura;
	}

	public List<SelectItem> getListaClassiStruttura() {
		return listaClassiStruttura;
	}

	public void setListaClassiStruttura(List<SelectItem> listaClassiStruttura) {
		this.listaClassiStruttura = listaClassiStruttura;
	}

	public String getIdSocieta() {
		return idSocieta;
	}

	public void setIdSocieta(String idSocieta) {
		this.idSocieta = idSocieta;
	}

	public String getIdStruttura() {
		return idStruttura;
	}

	public void setIdStruttura(String idStruttura) {
		this.idStruttura = idStruttura;
	}

	public String getPeriodoDescr() {
		return periodoDescr;
	}

	public void setPeriodoDescr(String periodoDescr) {
		this.periodoDescr = periodoDescr;
	}

	public String getStrutturaDescr() {
		return strutturaDescr;
	}

	public void setStrutturaDescr(String strutturaDescr) {
		this.strutturaDescr = strutturaDescr;
	}

	public String getClasseDescr() {
		return classeDescr;
	}

	public void setClasseDescr(String classeDescr) {
		this.classeDescr = classeDescr;
	}

	public List<ModuloDTO> getListaModuli2() {
		return listaModuli2;
	}

	public void setListaModuli2(List<ModuloDTO> listaModuli2) {
		this.listaModuli2 = listaModuli2;
	}

	public List<ModuloDTO> getListaModuli3() {
		return listaModuli3;
	}

	public void setListaModuli3(List<ModuloDTO> listaModuli3) {
		this.listaModuli3 = listaModuli3;
	}

	public List<ModuloDTO> getListaModuli4() {
		return listaModuli4;
	}

	public void setListaModuli4(List<ModuloDTO> listaModuli4) {
		this.listaModuli4 = listaModuli4;
	}

	public String getListaModuliRow() {
		return listaModuliRow;
	}

	public void setListaModuliRow(String listaModuliRow) {
		this.listaModuliRow = listaModuliRow;
	}

	public String getIdSelezionato() {
		return idSelezionato;
	}

	public void setIdSelezionato(String idSelezionato) {
		this.idSelezionato = idSelezionato;
	}

	public String getListaModuliStep() {
		return listaModuliStep;
	}

	public void setListaModuliStep(String listaModuliStep) {
		this.listaModuliStep = listaModuliStep;
	}

	public IsDichiarazione getDichiarazione() {
		return dichiarazione;
	}

	public void setDichiarazione(IsDichiarazione dichiarazione) {
		this.dichiarazione = dichiarazione;
	}

	public IsStrutturaSnap getSnap() {
		return snap;
	}

	public void setSnap(IsStrutturaSnap snap) {
		this.snap = snap;
	}

	public List<ModuloDTO> getListaModuli1() {
		return listaModuli1;
	}

	public void setListaModuli1(List<ModuloDTO> listaModuli1) {
		this.listaModuli1 = listaModuli1;
	}

	public IsImpostaDovuta getImpDovuta() {
		return impDovuta;
	}

	public void setImpDovuta(IsImpostaDovuta impDovuta) {
		this.impDovuta = impDovuta;
	}

	public IsImpostaIncassata getImpIncassata() {
		return impIncassata;
	}

	public void setImpIncassata(IsImpostaIncassata impIncassata) {
		this.impIncassata = impIncassata;
	}

	public BigDecimal getImpostaInSospeso() {
		return impostaInSospeso;
	}

	public void setImpostaInSospeso(BigDecimal impostaInSospeso) {
		this.impostaInSospeso = impostaInSospeso;
	}

	public BigDecimal getImpostaInSospesoPrec() {
		return impostaInSospesoPrec;
	}

	public void setImpostaInSospesoPrec(BigDecimal impostaInSospesoPrec) {
		this.impostaInSospesoPrec = impostaInSospesoPrec;
	}

	public BigDecimal getTotIncassato() {
		return totIncassato;
	}

	public void setTotIncassato(BigDecimal totIncassato) {
		this.totIncassato = totIncassato;
	}

	public BigDecimal getTotSospeso() {
		return totSospeso;
	}

	public void setTotSospeso(BigDecimal totSospeso) {
		this.totSospeso = totSospeso;
	}

	public List<SelectItem> getListaPeriodiNuovaDich() {
		return listaPeriodiNuovaDich;
	}

	public void setListaPeriodiNuovaDich(List<SelectItem> listaPeriodiNuovaDich) {
		this.listaPeriodiNuovaDich = listaPeriodiNuovaDich;
	}

	public String getStatobozza() {
		return statoBozza;
	}

	public String getStatochiusa() {
		return statoChiusa;
	}

	public boolean isAnteprimaStampa() {
		return anteprimaStampa;
	}

	public void setAnteprimaStampa(boolean anteprimaStampa) {
		this.anteprimaStampa = anteprimaStampa;
	}

	public String getTipoPagamento() {
		
		if(dichiarazione.getPagBonifico()!=null)
			tipoPagamento = "BONIFICO";
		if(dichiarazione.getPagMav()!=null)
			tipoPagamento = "MAV";
		if(dichiarazione.getPagVersamento()!=null)
			tipoPagamento = "CONTANTI";
		
		//Se la dichiarazione è integrativa (e non è in modifica), carico per default la modalità di pagamento della dichiarazione originaria
		if(dichiarazione.getIntegrativa().intValue()==1 && idSelezionato == null){
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId3(dichiarazione.getFkIsPeriodo().longValue());
			dataIn.setId2(snap.getFkIsClasse());
			dataIn.setId(snap.getFkIsStruttura().longValue());
			
			IsDichiarazione dichOrig =  super.getDichiarazioneService().getDichiarazioneByPeriodoStrClasse(dataIn);
			if(dichOrig!=null){
				if(dichOrig.getPagBonifico()!=null)
					tipoPagamento = "BONIFICO";
				if(dichOrig.getPagMav()!=null)
					tipoPagamento = "MAV";
				if(dichOrig.getPagVersamento()!=null)
					tipoPagamento = "CONTANTI";
			}
				
		}
		
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getLb_pagMAV() {
		return this.getMessage("label.pagamento.MAV");
	}

	public String getLb_pagCONTANTI() {
		return this.getMessage("label.pagamento.CONTANTI");
	}

	public String getLb_pagBONIFICO() {
		return this.getMessage("label.pagamento.BONIFICO");
	}

	public void clearEstremiPagamento(){
		this.dichiarazione.setPagMav(null);
		this.dichiarazione.setPagVersamento(null);
		this.dichiarazione.setPagBonifico(null);
	}

	public BigDecimal getTotVersare() {
		return totVersare;
	}

	public void setTotVersare(BigDecimal totVersare) {
		this.totVersare = totVersare;
	}
	
	public void calcolaValoriActionListener(ActionEvent actionEvent) {
	  
	    this.doValoriCalcolati();
	}
	
	private int getNumOspitiModuliFirma(){
		int nOspitiMod = 0;
		for(RifiutoDTO dto : listaRifiuti)
			nOspitiMod += dto.getNumOspiti();
		
		return nOspitiMod;
	}
	
	private int getNumOspitiFirma(){
		int nOspFirma = 0;
		for (ModuloDTO dto : listaModuli1) {
			if (dto.getDati().getId()==11){ 
				if(dto.getOspiti().getNOspiti() == null) dto.getOspiti().setNOspiti(new BigDecimal(0));
				
				 nOspFirma = dto.getOspiti().getNOspiti().intValue();
			}
		}
		
		return nOspFirma;
	}
	
	public void listaRifiutiActionListener(ActionEvent actionEvent) {
		
		int numOspitiModuli = this.getNumOspitiModuliFirma();
		
		int nOspFirma = this.getNumOspitiFirma();
				
		if(nOspFirma<=0)
			listaRifiuti = new ArrayList<RifiutoDTO>();
		
		//Confronto la lunghezza ed eventualmente aggiungo, se il numero è >
		if(nOspFirma > numOspitiModuli){
			int index = listaRifiuti.size()>0 ? listaRifiuti.get(listaRifiuti.size()-1).getIntId()+1 : 0; //Prendo l'ultimo progressivo inserito
			for(int i=numOspitiModuli ; i<nOspFirma; i++){
				if(idSelezionato!=null)
					listaRifiuti.add(new RifiutoDTO(new BigDecimal(idSelezionato),Integer.toString(index)));
				else
					listaRifiuti.add(new RifiutoDTO(Integer.toString(index)));
				
				index++;
			}
		}
		
		if(nOspFirma < numOspitiModuli){
			int i=listaRifiuti.size()-1;
			while(getNumOspitiModuliFirma()>nOspFirma && i>=0){
				RifiutoDTO rem = listaRifiuti.get(i);
				if(!rem.isRifiutoValorizzato()) 
					listaRifiuti.remove(rem);
				i--;
			}	
		}		 
	}
	

	public BigDecimal getValTariffa() {
		
		if(dichiarazione.getValoreTariffa()!=null)
			valTariffa = dichiarazione.getValoreTariffa();
		else{
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId3(dichiarazione.getFkIsPeriodo().longValue());
			dataIn.setId2(snap.getFkIsClasse());
			valTariffa = super.getDichiarazioneService().getTariffaByPeriodoClasse(dataIn);
		}
			
		return valTariffa;
	}

	public void setValTariffa(BigDecimal valTariffa) {
		this.valTariffa = valTariffa;
	}

	public BigDecimal getTotRifiuti() {
		return totRifiuti;
	}

	public void setTotRifiuti(BigDecimal totRifiuti) {
		this.totRifiuti = totRifiuti;
	}

	public boolean isVisEsenzioni() {
		return visEsenzioni;
	}

	public void setVisEsenzioni(boolean visEsenzioni) {
		this.visEsenzioni = visEsenzioni;
	}

	public boolean isVisRiduzioniE() {
		return visRiduzioniE;
	}

	public void setVisRiduzioniE(boolean visRiduzioniE) {
		this.visRiduzioniE = visRiduzioniE;
	}

	public boolean isVisRiduzioniF1() {
		return visRiduzioniF1;
	}

	public void setVisRiduzioniF1(boolean visRiduzioniF1) {
		this.visRiduzioniF1 = visRiduzioniF1;
	}

	public boolean isVisRiduzioniF2() {
		return visRiduzioniF2;
	}

	public void setVisRiduzioniF2(boolean visRiduzioniF2) {
		this.visRiduzioniF2 = visRiduzioniF2;
	}

	public BigDecimal getImpRifiuti() {
		return impRifiuti;
	}

	public void setImpRifiuti(BigDecimal impRifiuti) {
		this.impRifiuti = impRifiuti;
	}

	public List<RifiutoDTO> getListaRifiuti() {
		return listaRifiuti;
	}

	public void setListaRifiuti(List<RifiutoDTO> listaRifiuti) {
		this.listaRifiuti = listaRifiuti;
	}

	public String getProgRifiutoSel() {
		return progRifiutoSel;
	}

	public void setProgRifiutoSel(String progRifiutoSel) {
		this.progRifiutoSel = progRifiutoSel;
	}
	
	public void rimuoviRifiuto(){
		if(progRifiutoSel!=null){
			boolean rimosso = false;
			int i = listaRifiuti.size()-1;
			while(!rimosso && i>=0){
				RifiutoDTO dto = listaRifiuti.get(i);
				if(dto.getId().equals(progRifiutoSel)){
					rimosso = listaRifiuti.remove(dto);
					//Aggiorno il numero di ospiti - Elimino un ospite dal contatore
					if(this.getNumOspitiModuliFirma()==this.getNumOspitiFirma()-1)
						this.aggiornoNOspitiConFirma(-(dto.getNumOspiti()));	
				}
				i--;
			}
		
		}
	}
	
	
	
	//numOspiti positivo (aggiungo) o negativo (rimuovo)
	public void aggiornoNOspitiConFirma(int numOspiti){
		for (ModuloDTO dto : listaModuli1) {
			if (dto.getDati().getId()==11){ 
				if(dto.getOspiti().getNOspiti() == null) dto.getOspiti().setNOspiti(new BigDecimal(0));
				
				int nOspFirma = dto.getOspiti().getNOspiti().intValue() + numOspiti;
				dto.getOspiti().setNOspiti(new BigDecimal(nOspFirma));
			}
		}
	}
	
	public void addComponenteGruppo(){
		
		RifiutoDTO dto = this.getRifiutoByProgSel();
		if(dto!=null){
			dto.doAddComponenteGruppo();
			//this.removeRifiuti(1);
			if(this.getNumOspitiModuliFirma()>this.getNumOspitiFirma())
				this.aggiornoNOspitiConFirma(1);
		}
	}
	
	public void removeLastComponenteGruppo(){
		
		RifiutoDTO dto = this.getRifiutoByProgSel();
		if(dto!=null){
			dto.doDeleteLastComponenteGruppo();
			if(this.getNumOspitiModuliFirma()==this.getNumOspitiFirma()-1)
				this.aggiornoNOspitiConFirma(-1);
		}
	}
	
	public RifiutoDTO getRifiutoByProgSel(){
		RifiutoDTO dtoSel = null;
		if(this.progRifiutoSel!=null){
			boolean trovato = false;
			int i=0;
			while(i<listaRifiuti.size() && !trovato){
				RifiutoDTO dto = listaRifiuti.get(i);
				if(dto.getId().equals(this.progRifiutoSel)){
					trovato = true;
					dtoSel = dto;
					
				}
			}
		}
		return dtoSel;
	}
	
	public void doAddRifiuto(){
		int index = listaRifiuti.size()>0 ? listaRifiuti.get(listaRifiuti.size()-1).getIntId()+1 : 0; //Prendo l'ultimo progressivo inserito
		if(idSelezionato!=null)
			listaRifiuti.add(new RifiutoDTO(new BigDecimal(idSelezionato),Integer.toString(index)));
		else
			listaRifiuti.add(new RifiutoDTO(Integer.toString(index)));
		
		if(this.getNumOspitiModuliFirma()>this.getNumOspitiFirma())
			this.aggiornoNOspitiConFirma(1);
	}
	
	public void removeRifiuti(int numRemove){
		
		int i=listaRifiuti.size()-1;
		while(numRemove>0 && i>=0){
			RifiutoDTO rem = listaRifiuti.get(i);
			if(!rem.isRifiutoValorizzato() && !rem.getId().equals(this.progRifiutoSel)){
				listaRifiuti.remove(rem);
				numRemove--;
			}
			i--;
		}	
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
		
		Integer cs = new Integer(currentStep);
		
		for(int i=0; i<stepArray.length; i++){
			if(i==cs-1)
				stepArray[i]=true;
			else
				stepArray[i]=false;
		}
		
		
		
	}
	
	public Date[] getIntervalloPeriodoDich(){
		DataInDTO dataIn =  new DataInDTO();
		fillEnte(dataIn);
		dataIn.setId(dichiarazione.getFkIsPeriodo().longValue());
		
		IsPeriodo periodo = super.getDichiarazioneService().getPeriodoById(dataIn);
		
		Date[] range = {periodo.getDataDal(),periodo.getDataAl()};
		
		return range;
		
	}

	public Boolean[] getStepArray() {
		return stepArray;
	}

	public void setStepArray(Boolean[] stepArray) {
		this.stepArray = stepArray;
	}

	public List<IsRimborso> getListaRimborsi() {
		return listaRimborsi;
	}

	public void setListaRimborsi(List<IsRimborso> listaRimborsi) {
		this.listaRimborsi = listaRimborsi;
	}

	public List<IsSanzione> getListaSanzioni() {
		return listaSanzioni;
	}

	public void setListaSanzioni(List<IsSanzione> listaSanzioni) {
		this.listaSanzioni = listaSanzioni;
	}

	public boolean isAnteprimaStampaIter() {
		return anteprimaStampaIter;
	}

	public void setAnteprimaStampaIter(boolean anteprimaStampaIter) {
		this.anteprimaStampaIter = anteprimaStampaIter;
	}

	
}
