package it.webred.ct.service.comma336.web.bean;

import it.webred.ct.data.access.aggregator.catastoDocfaConcedilizie.dto.SearchCriteriaDTO;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.ControlloClassamentoConsistenzaDTO;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.ElaborazioniDataIn;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.ElaborazioniDataOut;
import it.webred.ct.data.access.basic.c336.dto.C336CommonDTO;
import it.webred.ct.data.access.basic.c336.dto.C336GridAttribCatA2DTO;
import it.webred.ct.data.access.basic.c336.dto.C336PraticaDTO;
import it.webred.ct.data.access.basic.c336.dto.C336SkGenFabbricatoDTO;
import it.webred.ct.data.access.basic.c336.dto.C336SkUiuDTO;
import it.webred.ct.data.access.basic.c336.dto.C336TabValIncrClsA4A3DTO;
import it.webred.ct.data.access.basic.c336.dto.C336TabValIncrClsA5A6DTO;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.docfa.dto.DatiGeneraliDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.c336.C336GesPratica;
import it.webred.ct.data.model.c336.C336GridAttribCatA2;
import it.webred.ct.data.model.c336.C336Pratica;
import it.webred.ct.data.model.c336.C336SkCarGenFabbricato;
import it.webred.ct.data.model.c336.C336SkCarGenUiu;
import it.webred.ct.data.model.c336.C336TabValIncrClsA4A3;
import it.webred.ct.data.model.c336.C336TabValIncrClsA5A6;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.service.comma336.data.access.dto.DettGestionePraticaDTO;
import it.webred.ct.service.comma336.data.access.dto.DettInfoGeneraliPartDTO;
import it.webred.ct.service.comma336.data.access.dto.DettInfoGeneraliUiuDTO;
import it.webred.ct.service.comma336.data.access.dto.VerificheControlliPartDTO;
import it.webred.ct.service.comma336.web.Comma336BaseBean;
import it.webred.ct.service.comma336.web.bean.pagination.DataProviderImpl;
import it.webred.ct.service.comma336.web.bean.util.ButtonGesPraticaBean;
import it.webred.ct.service.comma336.web.bean.util.PermessiHandler;
import it.webred.ct.service.comma336.web.bean.util.UserBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlInputText;

public class DetailBean extends Comma336BaseBean {

	private static final long serialVersionUID = 1L;
	
	private String idUiu;
	private RicercaOggettoDTO ricUiu;
	private DettInfoGeneraliUiuDTO dtoInfoGenerali;
	private DettInfoGeneraliPartDTO dtoInfoGeneraliPart;
	private HtmlInputText txtRapporto;
	private String catUltimaUiu;
	private ControlloClassamentoConsistenzaDTO dtoVerificheUiu;
	
	private List<ControlloClassamentoConsistenzaDTO> lstClaCom; //Classi Compatibili
	private ControlloClassamentoConsistenzaDTO selClaCom;
	
	private List<ControlloClassamentoConsistenzaDTO> selLstClaZcDiverse;
	
	private List<VerificheControlliPartDTO> lstDtoVerifichePart;
	private DettGestionePraticaDTO dtoGesPratica;
	private ButtonGesPraticaBean btnPrendiInCarico=new ButtonGesPraticaBean();
	private ButtonGesPraticaBean btnSchede=new ButtonGesPraticaBean();
	private ButtonGesPraticaBean btnGriglie=new ButtonGesPraticaBean();
	private ButtonGesPraticaBean btnModifica=new ButtonGesPraticaBean();
	private ButtonGesPraticaBean btnChiudi=new ButtonGesPraticaBean();
	private ButtonGesPraticaBean btnAllegato=new ButtonGesPraticaBean();
	private boolean visSchede;
	private boolean visGriglie;
	private boolean visGriglieCat;
	private boolean visIndietro;
	private boolean loadFabbricato;
	private boolean visPnlGrigliaCat2;
		
	@PostConstruct
	public void initService() {
		visIndietro = true;
		ricUiu = new RicercaOggettoDTO();
		this.fillEnte(ricUiu);
		setDtoInfoGenerali(new DettInfoGeneraliUiuDTO());
		setDtoGesPratica(new DettGestionePraticaDTO() );
		btnPrendiInCarico=new ButtonGesPraticaBean();
		btnModifica=new ButtonGesPraticaBean();
		btnChiudi=new ButtonGesPraticaBean();
		btnAllegato = new ButtonGesPraticaBean();
		btnSchede = new ButtonGesPraticaBean();
		btnGriglie = new ButtonGesPraticaBean();
		visPnlGrigliaCat2 = true;
		catUltimaUiu = null;
	}

	public String goDetail() {
		doCaricaInfoGeneraliDto();
		doCaricaVerificheDto();
		doCaricaTestataPratica();
		doCaricaGesPraticaDto();
		return "c336.immobile.detail";
	}

	public RicercaOggettoDTO getRicUiu() {
		return ricUiu;
	}

	private void doCaricaInfoGeneraliDto() {
		try {
			setDtoInfoGeneraliPart(new DettInfoGeneraliPartDTO());
			dtoInfoGenerali = new DettInfoGeneraliUiuDTO();
			setSearchParams();
			
			if(loadFabbricato){
				setDtoInfoGeneraliPart(c336CommonService.getDettInfoGeneraliParticella(ricUiu));
				catUltimaUiu = null;
			}else{
				dtoInfoGenerali = c336CommonService.getDettInfoGeneraliImmobile(ricUiu);
				List<Sitiuiu> uius = dtoInfoGenerali.getListaSitiuiu();
				if(uius.size()>0){
					Date dataMax = uius.get(0).getId().getDataFineVal();
					catUltimaUiu = uius.get(0).getCategoria();
					int i = 1;
					while(i<uius.size()){
						if(dataMax.before(uius.get(i).getId().getDataFineVal())){
							dataMax = uius.get(i).getId().getDataFineVal();
							catUltimaUiu = uius.get(i).getCategoria();
						}
					i++;
					}
					
				}
			}
			
		} catch (Throwable t) {
			super.addErrorMessage("immobile.detail.error", t.getMessage());
			logger.error("doCaricaInfoGeneraliDto - " +t.getMessage(),t);
		}
	}
	
	private void doCaricaVerificheDto() {
		try {
			dtoVerificheUiu  = null;
			setLstDtoVerifichePart(new ArrayList<VerificheControlliPartDTO>());
			setSearchParams();
			
			if(loadFabbricato){
				lstDtoVerifichePart = c336CommonService.getVerificheControlliFabbricato(ricUiu);
				
			}else{
				//Recupero il docfa più recente
				List<DatiGeneraliDocfaDTO> listadocfa = dtoInfoGenerali.getListaDocfa();
				if(listadocfa!=null && listadocfa.size()>0){
					DatiGeneraliDocfaDTO ultimo = listadocfa.get(0);
					for(DatiGeneraliDocfaDTO docfa:listadocfa){
						Date dataCorr = docfa.getDataVariazione();
						if(dataCorr.after(ultimo.getDataVariazione()))
							ultimo = docfa;
					}
					
					RicercaOggettoDocfaDTO rod = new RicercaOggettoDocfaDTO();
					fillEnte(rod);
					rod.setProtocollo(ultimo.getProtocollo());
					rod.setFornitura(ultimo.getDataFornitura());
					rod.setFoglio(ricUiu.getFoglio());
					rod.setParticella(ricUiu.getParticella());
					rod.setUnimm(ricUiu.getSub());
					
					//Richiama il metodo del CT Service per il recupero dei controlli sul classamento
					dtoVerificheUiu = elaborazioniService.getControlliClassConsistenzaByDocfaUiu(rod);
					if(dtoVerificheUiu!= null && ultimo !=null)
							dtoVerificheUiu.setDataVariazione(ultimo.getDataVariazione());
				}
			}
			
		} catch (Throwable t) {
			super.addErrorMessage("immobile.detail.error", t.getMessage());
			logger.error("doCaricaVerificheDto - " +t.getMessage(),t);
		}
	}
	
	public void doCaricaGesPraticaDto() {
		logger.debug("doCaricaGesPraticaDto");
		
		try {
			
			dtoGesPratica = c336CommonService.getDatiGestionePratica(getKeyRicerca());
			String userGesAttuale="";
			if (dtoGesPratica.getDatiPratica().getPratica().getIdPratica() !=null ) {
				if (dtoGesPratica.getDatiPratica().getPratica().getCodStato().equals(C336PraticaDTO.COD_STATO_ISTRUTTORIA_IN_CORSO))
					userGesAttuale = dtoGesPratica.getDatiPratica().getGesAttualePratica().getId().getUserName();
				// TIPO pratica 
				if (dtoGesPratica.getDatiPratica().getPratica().getTipoPratica().equals(C336PraticaDTO.TIPO_PRATICA_RICLASSIFICAZIONE)){
					visSchede=true;
					visGriglie = true;
				}else{
					visSchede=false;
					visGriglie=false;
				}
				
			} else {
				DataProviderImpl dataProv = (DataProviderImpl)this.getBeanReference("dataProviderImpl");
				SearchCriteriaDTO criteri = dataProv.getCriteria();
				if (criteri.getTipoRicerca() !=null && !criteri.getTipoRicerca().equals("")) {
					visSchede=false;
					visGriglie=false;
				}else{
					visSchede=true;
					visGriglie=true;
				}
			}
			
			attivaLinkGesPra(dtoGesPratica.getDatiPratica().getPratica().getCodStato(), userGesAttuale);
	
		} catch (Throwable t) {
			super.addErrorMessage("immobile.detail.error", t.getMessage());
			logger.error("doCaricaGesPraticaDto - " +t.getMessage(),t);
		}
	}
	
	public void doCaricaTestataPratica() {
		this.getLogger().debug("doCaricaTestataPratica");
		try {
			dtoGesPratica = c336CommonService.getDatiBasePratica(getKeyRicerca());
		} catch (Throwable t) {
			super.addErrorMessage("immobile.detail.error", t.getMessage());
			logger.error("doCaricaTestataPratica - " +t.getMessage(),t);
		}
	}
	
	public boolean isVisIndietro() {
		return visIndietro;
	}

	public void setVisIndietro(boolean visIndietro) {
		this.visIndietro = visIndietro;
	}

	public void setDtoInfoGenerali(DettInfoGeneraliUiuDTO dtoInfoGenerali) {
		this.dtoInfoGenerali = dtoInfoGenerali;
	}

	public DettInfoGeneraliUiuDTO getDtoInfoGenerali() {
		return dtoInfoGenerali;
	}

	public void setIdUiu(String idUiu) {
		this.idUiu = idUiu;	
	}

	public String getIdUiu() {
		return idUiu;
	}

	public DettGestionePraticaDTO getDtoGesPratica() {
		return dtoGesPratica;
	}

	public ControlloClassamentoConsistenzaDTO getSelClaCom() {
		return selClaCom;
	}

	public void setSelClaCom(ControlloClassamentoConsistenzaDTO selClaCom) {
		this.selClaCom = selClaCom;
	}

	public void setDtoGesPratica(DettGestionePraticaDTO dtoGesPratica) {
		this.dtoGesPratica = dtoGesPratica;
	}
	
	public ButtonGesPraticaBean getBtnPrendiInCarico() {
		return btnPrendiInCarico;
	}

	public void setBtnPrendiInCarico(ButtonGesPraticaBean btnPrendiInCarico) {
		this.btnPrendiInCarico = btnPrendiInCarico;
	}

	public ButtonGesPraticaBean getBtnModifica() {
		return btnModifica;
	}

	public void setBtnModifica(ButtonGesPraticaBean btnModifica) {
		this.btnModifica = btnModifica;
	}

	public ButtonGesPraticaBean getBtnChiudi() {
		return btnChiudi;
	}

	public void setBtnChiudi(ButtonGesPraticaBean btnChiudi) {
		this.btnChiudi = btnChiudi;
	}

	public ButtonGesPraticaBean getBtnAllegato() {
		return btnAllegato;
	}

	public ButtonGesPraticaBean getBtnGriglie() {
		return btnGriglie;
	}

	public void setBtnGriglie(ButtonGesPraticaBean btnGriglie) {
		this.btnGriglie = btnGriglie;
	}

	public void setBtnAllegato(ButtonGesPraticaBean btnAllegato) {
		this.btnAllegato = btnAllegato;
	}

	
	public ButtonGesPraticaBean getBtnSchede() {
		return btnSchede;
	}

	public void setBtnSchede(ButtonGesPraticaBean btnSchede) {
		this.btnSchede = btnSchede;
	}

	public boolean isVisSchede() {
		return visSchede;
	}

	public void setVisSchede(boolean visSchede) {
		this.visSchede = visSchede;
	}

	private void setSearchParams(){
		String[] param = idUiu.split("@");
		this.fillEnte(ricUiu);
		ricUiu.setFoglio(param[0]);
		ricUiu.setParticella(param[1]);
		if(param.length==3)
			ricUiu.setSub(param[2]);
	}
		
	public void doPrendiInCaricoPratica() {
		C336Pratica pra =null;
		if (dtoGesPratica.getDatiPratica().getPratica() == null ||dtoGesPratica.getDatiPratica().getPratica().getIdPratica()==null)
			pra =inserisciPratica();
		else {
			pra = dtoGesPratica.getDatiPratica().getPratica();
			cambiaGestorePratica();
		}
		C336PraticaDTO praDto = new C336PraticaDTO();
		this.fillEnte(praDto);
		praDto.setPratica(pra);
		dtoGesPratica.setDatiPratica(praDto);
		//gestione attuale 
		C336GesPratica ges = praticaService.getGesAttualePratica(praDto);
		praDto.setGesAttualePratica(ges);
		//lista operatori
		List<C336GesPratica> listaOperatori  = praticaService.getListaOperatoriPratica(praDto);
		dtoGesPratica.setListaOperatoriPratica(listaOperatori);
		
		attivaLinkGesPra(C336PraticaDTO.COD_STATO_ISTRUTTORIA_IN_CORSO, ges.getId().getUserName());
			
			
		
	}
	public void cambiaGestorePratica() {
		C336PraticaDTO praDto = dtoGesPratica.getDatiPratica();
		this.getLogger().debug("cambiaGestorePratica--> idPratica/userId: " + praDto.getPratica().getIdPratica()+"/" + this.getUser().getUsername() );
		praDto.setUserId(this.getUser().getUsername());
		praticaManagerService.cambiaGestionePratica(praDto);
	}
	
	public C336Pratica inserisciPratica() {
		//inserimento pratica
		C336PraticaDTO praDto = new C336PraticaDTO();
		this.fillEnte(praDto);
		this.getLogger().debug("inserisciPratica--> userId: " + new UserBean().getUsername() );
		UserBean usr = new UserBean();
		praDto.setUserId(usr.getUsername());
		praDto.getPratica().setUserNameIni(usr.getUsername()); 
		praDto.getPratica().setFoglio(ricUiu.getFoglio());
		praDto.getPratica().setParticella(ricUiu.getParticella());
		praDto.getPratica().setSub(getValoreSub());
		praDto.getPratica().setCodStato(C336PraticaDTO.COD_STATO_ISTRUTTORIA_IN_CORSO);
		praDto.getPratica().setDtIni(new Date());
		//valorizzazione tipo pratica
		DataProviderImpl dataProv = (DataProviderImpl)this.getBeanReference("dataProviderImpl");
		SearchCriteriaDTO criteri = dataProv.getCriteria();
		if (criteri.getTipoRicerca() ==null || criteri.getTipoRicerca().equals("")) 
			praDto.getPratica().setTipoPratica(C336PraticaDTO.TIPO_PRATICA_RICLASSIFICAZIONE);
		else {
			if (criteri.getTipoRicerca().equals("maiDic") ) {
				praDto.getPratica().setTipoPratica(C336PraticaDTO.TIPO_PRATICA_FABBR_MAI_DIC);
			}else
				praDto.getPratica().setTipoPratica(C336PraticaDTO.TIPO_PRATICA_FABBR_EX_RURALE);
		}
		//inserimento pratica	
		C336Pratica pra = praticaManagerService.nuovaPratica(praDto) ;
		this.getLogger().debug("pratica inserita --> id: " + pra.getIdPratica() );
		return pra;
		
	}
	
	public void doPreparaGesIstr() {
		this.getLogger().debug("doPreparaGesIstr");
		
	}
	
	public void doSalvaGesIstr() {
		if (dtoGesPratica.getDatiPratica().getFlNotificatoTit().booleanValue())
			dtoGesPratica.getDatiPratica().getPratica().setFlInviataNotificaTitolare("S");
		else	
			dtoGesPratica.getDatiPratica().getPratica().setFlInviataNotificaTitolare("N");
		if (dtoGesPratica.getDatiPratica().getFlNotificatoAdt().booleanValue())
			dtoGesPratica.getDatiPratica().getPratica().setFlInviataNotificaAdt("S");
		else	
			dtoGesPratica.getDatiPratica().getPratica().setFlInviataNotificaAdt("N");
		C336Pratica pra = praticaManagerService.modificaPratica(dtoGesPratica.getDatiPratica());
		dtoGesPratica.getDatiPratica().setPratica(pra);
		C336CommonDTO dto = new C336CommonDTO();
		dto.setEnteId(ricUiu.getEnteId());
		dto.setCodIrreg(dtoGesPratica.getDatiPratica().getPratica().getCodIrregAccertata());
		dtoGesPratica.getDatiPratica().setDesIrregolarita(praticaService.getDesIrregolarita(dto));
	
	}
	
	public void doChiudiPratica() {
		//chiudere la pratica 
		dtoGesPratica.getDatiPratica().getPratica().setCodStato(C336PraticaDTO.COD_STATO_ISTRUTTORIA_CONCLUSA);
		dtoGesPratica.getDatiPratica().getPratica().setDtFin(new Date());
		dtoGesPratica.getDatiPratica().getPratica().setUserNameFin(new UserBean().getUsername()); 
		C336PraticaDTO praUpd =	praticaManagerService.chiudiIstrPratica(dtoGesPratica.getDatiPratica());
		//aggiormanento storico operatori
		List<C336GesPratica> listaOperatori  = praticaService.getListaOperatoriPratica(praUpd);
		dtoGesPratica.setListaOperatoriPratica(listaOperatori);
		//
		dtoGesPratica.setDatiPratica(praUpd);
		//nascondo pulsanti gestione
		attivaLinkGesPra(C336PraticaDTO.COD_STATO_ISTRUTTORIA_CONCLUSA, null);
	}
	
	private void attivaLinkGesPra (String codStato, String userGesAttuale)  {
		this.getLogger().debug("attivaLinkGesPra -- stato/userGesAtt/usr Abilitato: " + codStato + "/" + userGesAttuale +"/" + (new UserBean()).getUsername());
		if (!PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_GESTIONE_PRATICHE)){
			btnPrendiInCarico.attiva(false);
			btnAllegato.attiva(false);
			btnModifica.attiva(false);
			btnSchede.attiva(false);
			btnGriglie.attiva(false);
			btnChiudi.attiva(false);
			return;
		}
		
		if (codStato==null ||
		   (codStato !=null &&	codStato.equals("") ) ||
		   (codStato !=null && codStato.equals(C336PraticaDTO.COD_STATO_NON_ESAMINATA)) ){
			btnPrendiInCarico.attiva(true);
			btnAllegato.attiva(false);
			btnModifica.attiva(false);
			btnSchede.attiva(false);
			btnGriglie.attiva(false);
			btnChiudi.attiva(false);
			return;
		}
		
		if (codStato.equals(C336PraticaDTO.COD_STATO_ISTRUTTORIA_IN_CORSO)) {
			UserBean usr = new UserBean();
			if (!usr.getUsername().equals(userGesAttuale)) {
				btnPrendiInCarico.attiva(true);
				btnAllegato.attiva(false);
				btnModifica.attiva(false);
				btnChiudi.attiva(false);	
				btnSchede.attiva(false);
				btnGriglie.attiva(false);
			}else {
				btnPrendiInCarico.attiva(false);
				btnAllegato.attiva(true);
				btnModifica.attiva(true);
				btnChiudi.attiva(true);	
				btnSchede.attiva(true);	
				btnGriglie.attiva(true);
			}
				
		}
		
		if (codStato.equals(C336PraticaDTO.COD_STATO_ISTRUTTORIA_CONCLUSA)) {
			btnAllegato.attiva(false);
			btnPrendiInCarico.attiva(false);
			btnModifica.attiva(false);
			btnChiudi.attiva(false);	
			btnSchede.attiva(false);
			btnGriglie.attiva(false);
		}
	}
	
	public void doSalvaSkFabbr() {
		this.getLogger().debug("doSalvaSkFabbr-- inizio");
		C336PraticaDTO pra = new C336PraticaDTO();
		pra.setEnteId(this.getCurrentEnte());
		pra.getPratica().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		C336SkGenFabbricatoDTO skFabbrDto = new C336SkGenFabbricatoDTO();
		skFabbrDto.setEnteId(this.getCurrentEnte());
		skFabbrDto.setSchedaFabbr(dtoGesPratica.getSchedaFabbricato());
		skFabbrDto.getSchedaFabbr().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		C336SkCarGenFabbricato sk = praticaService.getSkGeneraleFabbricato(pra);
		if (sk==null) {
			this.getLogger().debug("insert scheda fab");
			praticaManagerService.nuovaSkFabbricato(skFabbrDto);	
		}
		else{
			this.getLogger().debug("modifica scheda fab");
			praticaManagerService.modificaSkFabbricato(skFabbrDto);
		}
			
		this.getLogger().debug("doSalvaSkFabbr-- fine"); 
	}
	
	public void doSalvaSkUiu() {
		this.getLogger().debug("doSalvaSkUiu-- inizio");
		C336PraticaDTO pra = new C336PraticaDTO();
		pra.setEnteId(this.getCurrentEnte());
		pra.getPratica().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		C336SkUiuDTO skUiuDto = new C336SkUiuDTO();
		skUiuDto.setEnteId(this.getCurrentEnte());
		skUiuDto.setSchedaUI(dtoGesPratica.getSchedaUiu());
		skUiuDto.getSchedaUI().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		C336SkCarGenUiu sk = praticaService.getSkGeneraleUiu(pra);
		if (sk==null) {
			praticaManagerService.nuovaSkUiu(skUiuDto);	
		}
		else{
			praticaManagerService.modificaSkUiu(skUiuDto);
		}
			
		this.getLogger().debug("doSalvaSkUiu-- fine"); 
	}
	
	public void doSalvaGrigliaA4A3(){
		this.getLogger().debug("doSalvaGrigliaA3A4-- inizio");
		C336PraticaDTO pra = new C336PraticaDTO();
		pra.setEnteId(this.getCurrentEnte());
		pra.getPratica().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		
		C336TabValIncrClsA4A3DTO dto = new C336TabValIncrClsA4A3DTO();
	
		dto.setEnteId(this.getCurrentEnte());
		dto.setGriglia(dtoGesPratica.getTabellaValutIncrA4A3());
		dto.getGriglia().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		C336TabValIncrClsA4A3 sk = praticaService.getTabValutIncrClasseA4A3(pra);
		if (sk==null) {
			praticaManagerService.nuovaTabValutIncrClasseA4A3(dto);	
		}
		else{
			praticaManagerService.modificaTabValutIncrClasseA4A3(dto);
		}
			
		this.getLogger().debug("doSalvaGrigliaA3A4-- fine"); 
	}
	
	public void doSalvaGrigliaA5A6(){
		C336PraticaDTO pra = new C336PraticaDTO();
		pra.setEnteId(this.getCurrentEnte());
		pra.getPratica().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		
		C336TabValIncrClsA5A6DTO dto = new C336TabValIncrClsA5A6DTO();
	
		dto.setEnteId(this.getCurrentEnte());
		dto.setGriglia(dtoGesPratica.getTabellaValutIncrA5A6());
		dto.getGriglia().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		C336TabValIncrClsA5A6 sk = praticaService.getTabValutIncrClasseA5A6(pra);
		if (sk==null) {
			praticaManagerService.nuovaTabValutIncrClasseA5A6(dto);	
		}
		else{
			praticaManagerService.modificaTabValutIncrClasseA5A6(dto);
		}
			
		this.getLogger().debug("doSalvaGrigliaA5A6-- fine"); 
	}
	
	public void doSalvaGrigliaAttribA2(){
		C336PraticaDTO pra = new C336PraticaDTO();
		pra.setEnteId(this.getCurrentEnte());
		pra.getPratica().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		
		C336GridAttribCatA2DTO dto = new C336GridAttribCatA2DTO();
	
		dto.setEnteId(this.getCurrentEnte());
		dto.setGriglia(dtoGesPratica.getGrigliaAttribA2());
		dto.getGriglia().setIdPratica(dtoGesPratica.getDatiPratica().getPratica().getIdPratica());
		C336GridAttribCatA2 sk = praticaService.getGridAttribCat2(pra);
		if (sk==null) {
			praticaManagerService.nuovaGridAttribCat2(dto);	
		}
		else{
			praticaManagerService.modificaGridAttribCat2(dto);
		}
			
		this.getLogger().debug("doSalvaGrigliaAttribA2-- fine"); 
	}
	
	@SuppressWarnings("unchecked")
	public void showClassamento(){
		try{
			ElaborazioniDataIn dataIn = new ElaborazioniDataIn();
			
			if(this.txtRapporto!=null)
				selClaCom.setRapportoParam(this.txtRapporto.getValue());
			else
				selClaCom.setRapportoParam(null);
			
			dataIn.setObj1(selClaCom);
			fillEnte(dataIn);
			ElaborazioniDataOut dataOut = elaborazioniService.getClassamentoCompatibile(dataIn);
			this.setSelClaCom((ControlloClassamentoConsistenzaDTO)dataOut.getObj1());
			this.setLstClaCom((List<ControlloClassamentoConsistenzaDTO>)dataOut.getObj2());
			
		} catch (Throwable t) {
			super.addErrorMessage("immobile.detail.classamento.compatibile.error", t.getMessage());
			this.getLogger().debug(t.getMessage());
			logger.error(t.getMessage(), t);
		}
	}

	
	public void setDtoInfoGeneraliPart(DettInfoGeneraliPartDTO dtoInfoGeneraliPart) {
		this.dtoInfoGeneraliPart = dtoInfoGeneraliPart;
	}

	public DettInfoGeneraliPartDTO getDtoInfoGeneraliPart() {
		return dtoInfoGeneraliPart;
	}
	
	public boolean isLoadFabbricato() {
		return loadFabbricato;
	}

	public void setLoadFabbricato(boolean loadFabbricato) {
		this.loadFabbricato = loadFabbricato;
	}
	public ControlloClassamentoConsistenzaDTO getDtoVerificheUiu() {
		return dtoVerificheUiu;
	}

	public void setDtoVerificheUiu(ControlloClassamentoConsistenzaDTO dtoVerificheUiu) {
		this.dtoVerificheUiu = dtoVerificheUiu;
	}

	public void setLstDtoVerifichePart(List<VerificheControlliPartDTO> lstDtoVerifichePart) {
		this.lstDtoVerifichePart = lstDtoVerifichePart;
	}

	public List<VerificheControlliPartDTO> getLstDtoVerifichePart() {
		return lstDtoVerifichePart;
	}

	public void setLstClaCom(List<ControlloClassamentoConsistenzaDTO> lstClaCom) {
		this.lstClaCom = lstClaCom;
	}

	public List<ControlloClassamentoConsistenzaDTO> getLstClaCom() {
		return lstClaCom;
	}

	public void setTxtRapporto(HtmlInputText txtRapporto) {
		this.txtRapporto = txtRapporto;
	}

	public HtmlInputText getTxtRapporto() {
		return txtRapporto;
	}

	public void showClassamentoDefault(){
		this.txtRapporto = null;
		this.showClassamento();
	}

	public void setVisGriglie(boolean visGriglie) {
		this.visGriglie = visGriglie;
	}

	public boolean isVisGriglie() {
		return visGriglie;
	}

	public void setVisPnlGrigliaCat2(boolean visPnlGrigliaCat2) {
		this.visPnlGrigliaCat2 = visPnlGrigliaCat2;
	}

	public boolean isVisPnlGrigliaCat2() {
		C336GridAttribCatA2 griglia = this.dtoGesPratica.getGrigliaAttribA2();
		this.visPnlGrigliaCat2 = isNo(griglia.getValCorpiAccessori()) &&
								 isNo(griglia.getValAltRidotta()) &&
								 isNo(griglia.getValSerIgienicoEsterno())&&
								 isNo(griglia.getValUiuAccessoBallatoio()) &&
								 isNo(griglia.getValUiuSeminterrate());
		//getLogger().debug("Visualizza secondo pannello: "+visPnlGrigliaCat2);
		return visPnlGrigliaCat2;
	}
	
	
	private boolean isNo(String val){
		return val==null || (val!=null && val.equals("N"));
	}


	public void setCatUltimaUiu(String catUltimaUiu) {
		this.catUltimaUiu = catUltimaUiu;
	}

	public String getCatUltimaUiu() {
		//getLogger().debug("CategoriaUIU: "+ catUltimaUiu);
		return catUltimaUiu;
	}


	public boolean isVisGriglieCat() {
		if(catUltimaUiu!=null && 
				   (catUltimaUiu.equals("A04")||
					catUltimaUiu.equals("A03")||
					catUltimaUiu.equals("A05")||
					catUltimaUiu.equals("A06")))
				visGriglieCat = true;
			else 
				visGriglieCat = false;
		
		return visGriglieCat;
	}
	
	
	public void valueChangedGrigliaCat2P1(){
			if(!this.isVisPnlGrigliaCat2())
				resetGrigliaCat2P2();
	}
	
	public void resetGrigliaCat2P2(){
		this.dtoGesPratica.getGrigliaAttribA2().setValCarAndrone(null);
		this.dtoGesPratica.getGrigliaAttribA2().setValNumUiuPiano(null);
		this.dtoGesPratica.getGrigliaAttribA2().setValSupMediaUiu(null);
		
	}
	
	public String getValoreSub() {
		String sub= ricUiu.getSub();
		if (sub != null) {
			if (sub.equals("") || sub.equals("0000"))
				sub = null;
		}
		return sub;
	}
	
	public RicercaOggettoDTO getKeyRicerca() {
		RicercaOggettoDTO ro = new RicercaOggettoDTO();
		ro.setEnteId(this.getCurrentEnte());
		ro.setUserId(this.getUser().getUsername());
		ro.setSezione(ricUiu.getSezione() );
		ro.setFoglio(ricUiu.getFoglio());
		ro.setParticella(ricUiu.getParticella());
		ro.setSub(getValoreSub());
		return ro;
	}

	
	public List<ControlloClassamentoConsistenzaDTO> getSelLstClaZcDiverse() {
		return selLstClaZcDiverse;
	}

	public void setSelLstClaZcDiverse(List<ControlloClassamentoConsistenzaDTO> selLstClaZcDiverse) {
		this.selLstClaZcDiverse = selLstClaZcDiverse;
	}
}
