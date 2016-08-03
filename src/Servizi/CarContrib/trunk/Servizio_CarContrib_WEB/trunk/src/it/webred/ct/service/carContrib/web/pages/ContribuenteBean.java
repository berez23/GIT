package it.webred.ct.service.carContrib.web.pages;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.DatiAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.DatiPersonaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.bolliVeicoli.dto.RicercaBolliVeicoliDTO;
import it.webred.ct.data.access.basic.cartellasociale.dto.InterventoDTO;
import it.webred.ct.data.access.basic.cartellasociale.dto.RicercaCarSocDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.cosap.dto.RicercaSoggettoCosapDTO;
import it.webred.ct.data.access.basic.f24.dto.F24VersamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.RicercaF24DTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.access.basic.imu.dto.JsonDatiCatastoDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuBaseDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuConsulenzaDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuElaborazioneDTO;
import it.webred.ct.data.access.basic.imu.dto.ValImmobileDTO;
import it.webred.ct.data.access.basic.imu.dto.XmlF24DTO;
import it.webred.ct.data.access.basic.imu.dto.XmlFiglioDTO;
import it.webred.ct.data.access.basic.imu.dto.XmlImmobileDTO;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.access.basic.redditi.dto.KeySoggDTO;
import it.webred.ct.data.access.basic.redditi.dto.RedditiDicDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaSoggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.bolliVeicoli.BolloVeicolo;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;
import it.webred.ct.data.model.cosap.SitTCosapContrib;
import it.webred.ct.data.model.cosap.SitTCosapTassa;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.ici.VTIciSoggAll;
import it.webred.ct.data.model.redditi.RedDatiAnagrafici;
import it.webred.ct.data.model.redditi.RedDomicilioFiscale;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.service.carContrib.data.access.cc.IndiceNonAllineatoException;
import it.webred.ct.service.carContrib.data.access.cc.dto.RichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RisposteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.SoggettiCarContribDTO;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.cnc.dto.RicercaCncDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.IndirizzoIciTarsuDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.LocazioniDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.ParamAccessoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimImmobileDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimTerrenoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
import it.webred.ct.service.carContrib.data.access.ici.IciServiceCarContrib;
import it.webred.ct.service.carContrib.data.access.ici.dto.DatiIciDTO;
import it.webred.ct.service.carContrib.data.access.tarsu.dto.DatiTarsuDTO;
import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.service.carContrib.data.model.Risposte;
import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.service.carContrib.web.CarContribBaseBean;
import it.webred.ct.service.carContrib.web.Utility;
import it.webred.ct.service.carContrib.web.pages.fonti.AcquaCarContrib;
import it.webred.ct.service.carContrib.web.pages.fonti.MulteCarContrib;
import it.webred.ct.service.carContrib.web.pages.fonti.PratichePortaleCarContrib;
import it.webred.ct.service.carContrib.web.pages.fonti.RetteCarContrib;
import it.webred.ct.service.carContrib.web.pages.fonti.RuoliVersamentiCarContrib;
import it.webred.ct.service.carContrib.web.utils.FonteDTO;
import it.webred.ct.service.carContrib.web.utils.GestioneFonte;
import it.webred.ct.service.carContrib.web.utils.Permessi;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ContribuenteBean extends CarContribBaseBean
{
	private ComuneService comuneService;
	
	private final String F_TRIBUTI = "TRIBUTI";
	private final String F_COSAP = "COSAP/TOSAP";
	private final String F_CNC = "CNC";
	private final String F_REDDITI = "REDDITI";
	private final String F_ACQUA = "ACQUA NEW";
	private final String F_MULTE = "MULTE";
	private final String F_RETTE = "RETTE";
	private final String F_PRATICHE = "PRATICHE PORTALE";
	private final String F_DEMOGRAFIA = "DEMOGRAFIA";
	private final String F_F24 = "F24";
	private final String F_IMU = "IMU";
	private final String F_LOCAZIONI = "LOCAZIONI";
	private final String F_CATASTO = "CATASTO";
	private final String F_BOLLI_VEICOLI = "BOLLIVEICOLI";	// = AM_FONTE.CODICE
	
	private final String F_R_TARES = "RUOLO TARES";
	private final String F_R_TARSU = "RUOLO TARSU";
	private final String F_VER_TAR_BP = "VERS TAR POSTE";
	private final String F_VER_ICI_DM = "VERS ICI MISTERIALE";
	
	private FonteDTO tribTarsuFonte;
	private FonteDTO tribIciFonte;
	private FonteDTO cosapFonte;
	private FonteDTO cncFonte;
	private FonteDTO redditiFonte;
	private FonteDTO acquaFonte;
	private FonteDTO multeFonte;
	private FonteDTO retteFonte;
	private FonteDTO praticheFonte;
	private FonteDTO nucleoFonte;
	private FonteDTO carsocialeFonte;
	private FonteDTO imuFonte;
	private FonteDTO locazioniFonte;
	private FonteDTO catastoFonte;
	private FonteDTO bolliVeicoliFonte;
	
	
	private boolean visStoricoCatasto = false;
	private boolean preparaCartella ;
	private Long idRichiestaCartellaPrecedente;
	private SoggettoDTO soggCartellaPrecedente;
	
	private String padre;
	private String strDataRichiesta;
	private String strSoggettoRichiedente;
	private boolean cbxSezioneIci;
	private String motivoIci;
	private boolean cbxSezioneTarsu;
	private String motivoTarsu;
	private boolean cbxSezioneCosap;
	private String motivoCosap;
	private boolean cbxSezioneRedditi;
	private String motivoRedditi;
	private boolean cbxSezioneCnc;
	private String motivoCnc;
	private boolean cbxSezioneAcqua;
	private String motivoAcqua;
	private boolean cbxSezioneMulte;
	private String motivoMulte;
	private boolean cbxSezioneRette;
	private String motivoRette;
	private boolean cbxSezionePratiche;
	private String motivoPratiche;
	private boolean cbxSezioneCarSociale;
	private String motivoCarSociale;
	private boolean cbxSezioneRuoliVersamenti;
	private String motivoRuoliVersamenti;
	private boolean cbxSezioneImu;
	private String motivoImu;
	private boolean cbxSezionePatrimoniale;
	private String motivoPatrimoniale;
	
	private boolean returnToList;
	private boolean viewPDF;
	private boolean showButtonProduciPdf;
	private boolean showButtonProduciPdfPerNucleo;
	private String selectedTab;
	
	private ComponenteFamigliaDTO componenteNucleo;
	
	private static final long serialVersionUID = 1L;
	String VIS_IN_SCHEDA_ICI = "ICI";
	String VIS_IN_SCHEDA_TARSU = "TARSU";
	String[] COD_TIPI_TRIBUTO_TARSU = {"116","117", "127","113"};
	
	// IDENTIFICATICO UNIVOCO RICHIESTA CARTELLA
	private Long idRichiestaCartella;
	private String tipoAccessoCartella;
	// SOGGETTO x CARTELLA
	private SoggettoDTO soggettoCartella;
	private String idVarSoggetto;
	
	private String labelDescTipoSogg;
	
	//DATI PATRIMONIALI
	private String intestazioneSituazionePatrimoniale;
	private List<SitPatrimImmobileDTO> listaImmobili = new ArrayList<SitPatrimImmobileDTO>();
	private List<SitPatrimTerrenoDTO> listaTerreni = new ArrayList<SitPatrimTerrenoDTO>();
	private List<LocazioniDTO> listaLocazioni = new ArrayList<LocazioniDTO>();
	private List<BolloVeicolo> listaBolliVeicoli = new ArrayList<BolloVeicolo>();
	
	// DATI ICI
	private String annoRiferimentoICI;
	private String labelDatiSoggettoIci;
	private String indiceSoggettoTabellaIci;	
	
	private String intestazione;
	private String denominazione;
	private String indirizzo;
	private String descLabelIndirizzo;
	private String codFiscalePIVA;
	private String comuneResidenza;

	private String indirizzoICI;
	private String indirizzoAnagrafe;
	
	private List<String> listaIndirizziCatasto = new ArrayList<String>();
	private List<String> listaIndirizziSIT = new ArrayList<String>();
	private List<String> listaSoggettiCoinvolti = new ArrayList<String>();
	
	private SitTIciSogg soggettoIciDaVisualizzare;
	private String numeroSoggettiICI;
	
	
	private List<SitTIciSogg> listaSoggettiICI = new ArrayList<SitTIciSogg>();
	private List<DatiIciDTO> listaImmobiliDichiaratiICI = new ArrayList<DatiIciDTO>();
	private List<DatiImportiCncDTO> listaVersamenti = new ArrayList<DatiImportiCncDTO>();
	
	private DatiIciDTO immobileIci;
	private IndiciSoggettoDTO indiciSoggettoCartella;
	private SitTIciSogg soggettoIciSelezionato;
	
	// START OGGETTI X TARSU
	
	private List<SitTTarSogg> listaSoggettiTARSU = new ArrayList<SitTTarSogg>();

	private List<String> listaIndirizziCatastoTarsu = new ArrayList<String>();
	private List<String> listaIndirizziSITTarsu = new ArrayList<String>();
		
	private SitTTarSogg soggettoTarsuDaVisualizzare;
	private String numeroSoggettiTarsu;
	
	private String labelDatiSoggettoTarsu;
	private String indiceSoggettoTabellaTarsu;	
	
	private String denominazioneTarsu;
	private String indirizzoTarsu;
	private String descLabelIndirizzoTarsu;
	private String codFiscalePIVATarsu;
	private String comuneResidenzaTarsu;
	private String indirizzoPanelTarsu;
	
	private List<DatiTarsuDTO> listaImmobiliDichiaratiTarsu = new ArrayList<DatiTarsuDTO>();
	private List<DatiImportiCncDTO> listaVersamentiTarsu = new ArrayList<DatiImportiCncDTO>();
	//private List<SitPatrimImmobileDTO> listaImmobiliTarsu = new ArrayList<SitPatrimImmobileDTO>();
	//private List<LocazioniDTO> listaLocazioniTarsu = new ArrayList<LocazioniDTO>();
	
	private DatiTarsuDTO immobileTarsu;
	private SitTTarSogg soggettoTarsuSelezionato;
	
	private String indirizzoTARSU;
	private List<String> listaSoggettiCoinvoltiTarsu = new ArrayList<String>();
	
	// END OGGETTI X TARSU

	// START OGGETTI X COSAP
	
	private List<SitTCosapTassa> listaConcessioniCosap = new ArrayList<SitTCosapTassa>();
	
	private String labelDatiSoggettoCosap;
	private String indiceSoggettoTabellaCosap;
	private String denominazioneCosap;
	private String indirizzoCosap;
	private String descLabelIndirizzoCosap;
	private String codFiscalePIVACosap;
	private String comuneResidenzaCosap;
	
	private List<SitTCosapContrib> listaSoggettiCOSAP = new ArrayList<SitTCosapContrib>();
	private String numeroSoggettiCosap;
	private SitTCosapContrib soggettoCosapDaVisualizzare;
	private SitTCosapContrib soggettoCosapSelezionato;
	
	// END OGGETTI X COSAP
	
	// START OGGETTI X RUOLI CNC
	
	private String labelDatiSoggettoCnc; 
	private String indiceSoggettoTabellaCnc;

	private List<DatiImportiCncDTO> listaRuoliCNC = new ArrayList<DatiImportiCncDTO>();
	
	private String denominazioneCNC;
	private String indirizzoCNC;
	private String descLabelIndirizzoCNC;
	private String codFiscalePIVACNC;
	private String comuneResidenzaCNC;
	
	private List<VAnagrafica> listaSoggettiCNC = new ArrayList<VAnagrafica>();
	private String numeroSoggettiCNC;
	private VAnagrafica soggettoCNCDaVisualizzare;
	private VAnagrafica soggettoCNCSelezionato;
	
	// END OGGETTI X RUOLI CNC
	
	// START OGGETTI X REDDITI DICHIARATI
	private String labelDatiSoggettoRedditi;
	private String indiceSoggettoTabellaRedditi;

	private List<RedditiDicDTO> listaRedditi = new ArrayList<RedditiDicDTO>();
	
	private String denominazioneRedditi;
	private String indirizzoRedditi;
	private String descLabelIndirizzoRedditi;
	private String codFiscalePIVARedditi;
	private String domicilioFiscaleRedditi;
	
	//private RicercaSoggettoDTO ricercaSoggettoRedditi;
	private List<RedDatiAnagrafici> listaSoggettiRedditi = new ArrayList<RedDatiAnagrafici>();
	private String numeroSoggettiRedditi;
	private RedDatiAnagrafici soggettoRedditiDaVisualizzare;
	private RedDatiAnagrafici soggettoRedditiSelezionato;
	// END OGGETTI X REDDITI DICHIARATI
	
	private AcquaCarContrib acquaCarContrib;
	private MulteCarContrib multeCarContrib;
	private RetteCarContrib retteCarContrib;
	private PratichePortaleCarContrib pratichePortaleCarContrib;
	private RuoliVersamentiCarContrib ruoliVersCarContrib;
	
	// START OGGETTI X NUCLEO FAMILIARE
	private List<ComponenteFamigliaDTO> listaComponentiNucleo = new ArrayList<ComponenteFamigliaDTO>();
	
	private String cognomeNomeNucleo;
	private String comuneDtNascitaNucleo;
	private String codFiscaleNucleo;
	private String indirizzoResidenzaNucleo;
	private String dtInizioResidenzaNucleo;
	private String posizioneAnagraficaNucleo;
	
	private String numeroSoggettiNucleo;
	// END OGGETTI X NUCLEO FAMILIARE
	
	// START OGGETTI X CARTELLA SOCIALE
	private List<InterventoDTO> listaInterventiCarSociale = new ArrayList<InterventoDTO>();
	private String numeroInterventiCarSociale;
	// END OGGETTI X CARTELLA SOCIALE
	
	//START OGGETTI F24
	
	//END OGGETTI F24
	
	//START OGGETTI SALDO IMU
	private SaldoImuElaborazioneDTO datiElabImu;
	private SaldoImuConsulenzaDTO consulenzaImu;
	//END OGGETTI SALDO IMU

	
	public String ClosePanelCartellaComponenteNucleo()
	{
		soggettoCartella = soggCartellaPrecedente;
		soggCartellaPrecedente = null;
		
		//showButtonProduciPdfPerNucleo = true;
		idRichiestaCartella=idRichiestaCartellaPrecedente;
		idRichiestaCartellaPrecedente = null;
		viewPDF = false;
		
		soggettoIciDaVisualizzare = null;
		soggettoTarsuDaVisualizzare = null;
		soggettoCosapDaVisualizzare = null;
		soggettoRedditiDaVisualizzare = null;
		soggettoCNCDaVisualizzare= null;
		this.LoadCartellaContribuente(true);
		selectedTab="ajaxSei";
		
		return "goCartellaContribuente";
	}

	public void ShowCartellaComponenteNucleo()
	{
		Long idRichiesta = new Long(0);
		
		ParamAccessoDTO parms =  new ParamAccessoDTO();
		
		parms.setTipoSogg("F");
		parms.setNome(componenteNucleo.getPersona().getNome());
		parms.setCognome(componenteNucleo.getPersona().getCognome());			
		parms.setCodFis(componenteNucleo.getPersona().getCodfisc());
		parms.setDtNas(componenteNucleo.getPersona().getDataNascita());
		parms.setParIva("");
		parms.setDenom("");
		parms.setEnteId(super.getUserBean().getEnteID());
		parms.setUserId(super.getUserBean().getUsername());
		
		Set <SoggettoDTO> listaSoggetti= super.getGeneralService().searchSoggetto(parms);
		if (listaSoggetti==null || listaSoggetti.size()==0)
			logger.debug("NESSUN SOGGETTO TROVATO");
		else
		{
			logger.debug( " showCartellaComponenteNucleo idRichiesta = " + idRichiestaCartella.toString());
			
			RichiesteDTO richiestaDTO = new RichiesteDTO();
			
			Richieste richiesta = new Richieste();
			richiesta.setIdRic(idRichiestaCartella);
			
			richiestaDTO.setRich(richiesta);
			richiestaDTO.setEnteId(super.getUserBean().getEnteID());
			richiestaDTO.setUserId(super.getUserBean().getUsername());
			
			Richieste rich = super.getCarContribService().getRichiesta(richiestaDTO);
			if (rich!=null && rich.getNomePdf()!="")
			{
				SoggettiCarContribDTO soggettoCartella = new SoggettiCarContribDTO();
				SoggettiCarContrib soggetto = new SoggettiCarContrib();
				
				soggetto.setCodTipSogg("F");
				soggetto.setCodFis(componenteNucleo.getPersona().getCodfisc());
				soggetto.setCognome(componenteNucleo.getPersona().getCognome());
				soggetto.setNome(componenteNucleo.getPersona().getNome());
				soggetto.setDtNas(componenteNucleo.getPersona().getDataNascita());
				soggetto.setCodComNas(componenteNucleo.getPersona().getIdExtComuneNascita());
				soggetto.setParIva("");
				soggetto.setDenomSoc("");
				
				soggettoCartella.setSogg(soggetto);
				soggettoCartella.setEnteId(super.getUserBean().getEnteID());
				soggettoCartella.setUserId(super.getUserBean().getUsername());
				
				Richieste r =  super.getCarContribService().getRichiesta(richiestaDTO);
				
				RichiesteDTO richDTO = new RichiesteDTO();
				
				Richieste newRich = new Richieste();
				newRich.setCodTipRic(Utility.TipoRichiesta.CARTELLA.getTipoRichiesta());
				newRich.setCodTipProven(Utility.TipoProvenienza.INTERNA.getTipoProvenienza());
				newRich.setDtRic(new Date());
				newRich.setIdRic(null);
				newRich.setNumProt(null);
				newRich.setDtProt(null);
				newRich.setIdSoggRic(null);
				
				if (r.getIdSoggRic()!=null)
				{// E' STATO SPECIFICATO IL RICHIEDENTE
					newRich.setUserNameRic(null);
					newRich.setCodTipDocRicon(r.getCodTipDocRicon());
					newRich.setNumDocRicon(r.getNumDocRicon());
					newRich.setDtEmiDocRicon(r.getDtEmiDocRicon());
					newRich.setDesNotRic("");
					newRich.setIdSoggRic(r.getIdSoggRic());
				}
				else
				{// NON E' STATO SPECIFICATO IL RICHIEDENTE ALLORA IL RICHIEDENTE SARA' L'OPERATORE
					newRich.setUserNameRic(r.getUserNameRic());
					newRich.setCodTipDocRicon("");
					newRich.setNumDocRicon("");
					newRich.setDtEmiDocRicon(null);
					newRich.setDesNotRic("");
				}
				newRich.setEMail(r.getEMail());
				newRich.setNumTel(r.getNumTel());
				//Eredita la configurazione del padre
				newRich.setFlgStorico(this.visStoricoCatasto ? new Integer(0) : new Integer(1));
				
				richDTO.setRich(newRich);
				richDTO.setEnteId(super.getUserBean().getEnteID());
				richDTO.setUserId(super.getUserBean().getUsername());
				
				logger.debug(" showCartellaComponenteNucleo START insertOnlyRichiesta");
				idRichiesta =  super.getCarContribService().insertOnlyRichiesta(richDTO,soggettoCartella);
				logger.debug(" showCartellaComponenteNucleo END insertOnlyRichiesta");
			}
			
			logger.debug(" showCartellaComponenteNucleo NUOVO ID RICH. = " + idRichiesta.toString());
			
			if (listaSoggetti.isEmpty())
				 logger.debug("DATI INCONGRUENTI");
			else
			{
				soggCartellaPrecedente = soggettoCartella;
				soggettoCartella = listaSoggetti.iterator().next();
				idRichiestaCartellaPrecedente = idRichiestaCartella;
				idRichiestaCartella = idRichiesta;
				showButtonProduciPdfPerNucleo = true;
				viewPDF = false;
				//tipoAccessoCartella = Utility.TipoMezzoRisposta.CONSEGNA_A_MANO;
				
				soggettoIciDaVisualizzare = null;
				soggettoTarsuDaVisualizzare = null;
				soggettoCosapDaVisualizzare = null;
				soggettoRedditiDaVisualizzare = null;
				soggettoCNCDaVisualizzare= null;
				this.LoadCartellaContribuente(true);
			}
		}
	}
	
	public void setTipoAccessoCartella(String tipoAccessoCartella) {
		this.tipoAccessoCartella = tipoAccessoCartella;
	}

	public String getTipoAccessoCartella() {
		return tipoAccessoCartella;
	}

	private SoggettoDTO getSoggettoPerRicercaIndice()
	{
		if (soggettoCartella!=null)
		{
			SoggettoDTO r = new SoggettoDTO ();
			
			r.setEnteId(super.getUserBean().getEnteID());
			r.setUserId(super.getUserBean().getUsername());
			r.setTipoSogg(soggettoCartella.getTipoSogg());
			
			if (soggettoCartella.getTipoSogg().equals("F"))
			{// PERSONA FISICA
				r.setCodFis(soggettoCartella.getCodFis());
				r.setCognome(soggettoCartella.getCognome());
				r.setNome(soggettoCartella.getNome());
				r.setDtNas(soggettoCartella.getDtNas());
				r.setCodComNas(soggettoCartella.getCodComNas());
				r.setDesComNas(soggettoCartella.getDesComNas());
			}
			else
			{// PERSONA GIURIDICA
				r.setParIva(soggettoCartella.getParIva());
				r.setDenom(soggettoCartella.getDenom());
			}
			
			return r;
		}
		else
			return null;
	}
	
	private RicercaSoggettoIciDTO getSoggettoPerRicercaICI()
	{
		if (soggettoCartella!=null)
		{
			RicercaSoggettoIciDTO r = new RicercaSoggettoIciDTO ();
			
			r.setEnteId(super.getUserBean().getEnteID());
			r.setUserId(super.getUserBean().getUsername());
			r.setProvenienza(super.getProvenienzaDatiIci());
			r.setTipoSogg(soggettoCartella.getTipoSogg());
			
			if (soggettoCartella.getTipoSogg().equals("F"))
			{// PERSONA FISICA
				r.setCodFis(soggettoCartella.getCodFis());
				r.setCognome(soggettoCartella.getCognome());
				r.setNome(soggettoCartella.getNome());
				r.setDtNas(soggettoCartella.getDtNas());
				r.setCodComNas(soggettoCartella.getCodComNas());
				r.setDesComNas(soggettoCartella.getDesComNas());
			}
			else
			{// PERSONA GIURIDICA
				r.setParIva(soggettoCartella.getParIva());
				r.setDenom(soggettoCartella.getDenom());
			}
			
			return r;
		}
		else
			return null;
	}
	
	private RicercaLocazioniDTO getSoggettoPerRicercaLocazioni()
	{
		if (soggettoCartella!=null)
		{
			RicercaLocazioniDTO r = new RicercaLocazioniDTO ();
			
			r.setEnteId(super.getUserBean().getEnteID());
			r.setUserId(super.getUserBean().getUsername());
			r.setDtRif(null);
			
			if (soggettoCartella.getTipoSogg().equals("F"))
			// PERSONA FISICA
				r.setCodFis(soggettoCartella.getCodFis());
			else
			// PERSONA GIURIDICA
				r.setCodFis(soggettoCartella.getParIva());
			return r;
		}
		else
			return null;
	}
	
	private RicercaSoggettoDTO getSoggettoPerRicercaTARSU()
	{
		if (soggettoCartella!=null)
		{
			RicercaSoggettoTarsuDTO r = new RicercaSoggettoTarsuDTO ();
			
			r.setEnteId(super.getUserBean().getEnteID());
			r.setUserId(super.getUserBean().getUsername());
			r.setProvenienza(super.getProvenienzaDatiTarsu());
			r.setTipoSogg(soggettoCartella.getTipoSogg());
			
			if (soggettoCartella.getTipoSogg().equals("F"))
			{// PERSONA FISICA
				r.setCodFis(soggettoCartella.getCodFis());
				r.setCognome(soggettoCartella.getCognome());
				r.setNome(soggettoCartella.getNome());
				r.setDtNas(soggettoCartella.getDtNas());
				r.setCodComNas(soggettoCartella.getCodComNas());
				r.setDesComNas(soggettoCartella.getDesComNas());
			}
			else
			{// PERSONA GIURIDICA
				r.setParIva(soggettoCartella.getParIva());
				r.setDenom(soggettoCartella.getDenom());
			}
			
			return r;
		}
		else
			return null;
	}
	
	
	
	private SaldoImuBaseDTO getSoggettoPerSaldoImu()
	{
		if (soggettoCartella!=null)
		{
			SaldoImuBaseDTO r = new SaldoImuBaseDTO();
			
			r.setEnteId(super.getUserBean().getEnteID());
			r.setUserId(super.getUserBean().getUsername());
			if (this.soggettoCartella.getTipoSogg().equals("F"))
				// 	PERSONA FISICA
				r.setCodfisc(this.soggettoCartella.getCodFis());
			else
				// 	PERSONA GIURIDICA
				r.setCodfisc(this.soggettoCartella.getParIva());
			
			return r;
		}
		else
			return null;
	}
	
	private RicercaCarSocDTO getSoggettoPerRicercaCarSociale()
	{
		if (soggettoCartella!=null)
		{
			RicercaCarSocDTO r = new RicercaCarSocDTO();
			
			r.setEnteId(super.getUserBean().getEnteID());
			r.setUserId(super.getUserBean().getUsername());
			if (this.soggettoCartella.getTipoSogg().equals("F"))
				// 	PERSONA FISICA
				r.setCodFisc(this.soggettoCartella.getCodFis());
			else
				// 	PERSONA GIURIDICA
				r.setCodFisc(this.soggettoCartella.getParIva());
			
			return r;
		}
		else
			return null;
	}
	
	private RicercaSoggettoDTO getSoggettoPerRicerca()
	{
		logger.debug("soggettoCartella OBJ: " + soggettoCartella); 
		if (soggettoCartella!=null)
		{ 
			RicercaSoggettoDTO ricSoggetto = new RicercaSoggettoDTO ();
			
			ricSoggetto.setEnteId(super.getUserBean().getEnteID());
			ricSoggetto.setUserId(super.getUserBean().getUsername());
			ricSoggetto.setTipoSogg(soggettoCartella.getTipoSogg());
			
			if (soggettoCartella.getTipoSogg().equals("F"))
			{// PERSONA FISICA
				logger.debug("soggettoCartella PF ");
				ricSoggetto.setCodFis(soggettoCartella.getCodFis());
				ricSoggetto.setCognome(soggettoCartella.getCognome());
				ricSoggetto.setNome(soggettoCartella.getNome());
				ricSoggetto.setDtNas(soggettoCartella.getDtNas());
				ricSoggetto.setCodComNas(soggettoCartella.getCodComNas());
				ricSoggetto.setDesComNas(soggettoCartella.getDesComNas());
			}
			else
			{// PERSONA GIURIDICA
				logger.debug("soggettoCartella PG ");
				ricSoggetto.setParIva(soggettoCartella.getParIva());
				ricSoggetto.setDenom(soggettoCartella.getDenom());
			}
			
			return ricSoggetto;
		}
		else
			return null;
	}
	
	private Object getEntityBean()
	{   
		logger.debug("INDICE CORRELAZIONE ABILITATO: " + super.getAbilitaIndiceCorrelazione());
		if (super.getAbilitaIndiceCorrelazione().equals("1"))
		{
			RicercaSoggettoDTO ricSoggetto = this.getSoggettoPerRicerca();
			return super.getGeneralService().getEntityBean(ricSoggetto, soggettoCartella.getDesTblProv());
		}
		else
			return null;
	}
	
	public RicercaDTO getObjRicerca(Utility.TipoRicerca tipo)
	{
		RicercaDTO dati = new RicercaDTO();

		dati.setEnteId(super.getUserBean().getEnteID());
		dati.setUserId(super.getUserBean().getUsername());
	
		dati.setObjEntity(this.getEntityBean());	
		
		Object obj = new Object();
		logger.debug("TIPO RICERCA SOGGETTO: " + tipo); 
		switch (tipo)
		{
			case INDICE:
			{
				obj = this.getSoggettoPerRicercaIndice();
				break;
			}
			case ICI:
			{
				obj = this.getSoggettoPerRicercaICI();
				break;
			}
			case TARSU:
			{
				obj = this.getSoggettoPerRicercaTARSU();
			
				break;
			}		
			case LOCAZIONI:
			{
				obj = this.getSoggettoPerRicercaLocazioni();
				break;				
			}
			case REDDITI:
			{
				obj = this.getSoggettoPerRicerca();
				break;
			}
			case OTHER:
			{
				logger.debug("getObjRicerca - case OTHER" + tipo);
				obj = this.getSoggettoPerRicerca();
				break;
			}
		}
		
		dati.setObjFiltro(obj);
		logger.debug("OBJ ENTITY:  " + dati.getObjEntity()); 
		logger.debug("getObjRicerca - Fine  " ); 
		return dati;
	}
	

	
	public String getIntestazione() {
		return intestazione;
	}
	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
	}
	public void setLabelDatiSoggettoIci(String labelDatiSoggettoIci) {
		this.labelDatiSoggettoIci = labelDatiSoggettoIci;
	}
	public String getLabelDatiSoggettoIci() {
		return labelDatiSoggettoIci;
	}
	public String getIndiceSoggettoTabellaIci() {
		return indiceSoggettoTabellaIci;
	}
	public void setIndiceSoggettoTabellaIci(String indiceSoggettoTabellaIci) {
		this.indiceSoggettoTabellaIci = indiceSoggettoTabellaIci;
	}	
	public String getDenominazione() {
		return  denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getDescLabelIndirizzo() {
		return descLabelIndirizzo;
	}
	public void setDescLabelIndirizzo(String descLabelIndirizzo) {
		this.descLabelIndirizzo = descLabelIndirizzo;
	}
	public String getCodFiscalePIVA() {
		return codFiscalePIVA;
	}
	public void setCodFiscalePIVA(String codFiscalePIVA) {
		this.codFiscalePIVA = codFiscalePIVA;
	}
	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}
	public String getComuneResidenza() {
		return comuneResidenza;
	}
	public void setListaImmobiliDichiaratiICI(
			List<DatiIciDTO> listaImmobiliDichiaratiICI) {
		this.listaImmobiliDichiaratiICI = listaImmobiliDichiaratiICI;
	}
	public List<DatiIciDTO> getListaImmobiliDichiaratiICI() {
		return listaImmobiliDichiaratiICI;
	}
	public void setListaVersamenti(List<DatiImportiCncDTO> listaVersamenti) {
		this.listaVersamenti = listaVersamenti;
	}
	public List<DatiImportiCncDTO> getListaVersamenti() {
		return listaVersamenti;
	}
	public void setListaImmobili(List<SitPatrimImmobileDTO> listaImmobili) {
		this.listaImmobili= listaImmobili;
	}
	public List<SitPatrimImmobileDTO> getListaImmobili() {
		return listaImmobili;
	}
	public void setListaLocazioni(List<LocazioniDTO> listaLocazioni) {
		this.listaLocazioni = listaLocazioni;
	}
	public List<LocazioniDTO> getListaLocazioni() {
		return listaLocazioni;
	}
	public void setListaTerreni(List<SitPatrimTerrenoDTO> listaTerreni) {
		this.listaTerreni= listaTerreni;
	}
	public List<SitPatrimTerrenoDTO> getListaTerreni() {
		return listaTerreni;
	}
	public String getIndirizzoAnagrafe() {
		return indirizzoAnagrafe;
	}
	public void setIndirizzoAnagrafe(String indirizzoAnagrafe) {
		this.indirizzoAnagrafe = indirizzoAnagrafe;
	}
	public String getIndirizzoICI() {
		return indirizzoICI;
	}
	public void setIndirizzoICI(String indirizzoICI) {
		this.indirizzoICI = indirizzoICI;
	}
	public void setImmobileIci(DatiIciDTO immobileIci) {
		this.immobileIci = immobileIci;
	}
	public DatiIciDTO getImmobileIci() {
		return immobileIci;
	}
	public void setIdRichiestaCartella(Long idRichiestaCartella) {
		this.idRichiestaCartella = idRichiestaCartella;
	}
	public Long getIdRichiestaCartella() {
		return idRichiestaCartella;
	}
	public SoggettoDTO getSoggettoCartella() {
		return soggettoCartella;
	}
	public void setIdVarSoggetto(String idVarSoggetto) {
		this.idVarSoggetto = idVarSoggetto;
	}
	public String getIdVarSoggetto() {
		return idVarSoggetto;
	}
	public void setSoggettoCartella(SoggettoDTO soggettoCartella) {
		this.soggettoCartella = soggettoCartella;
	}
	public void setIndiciSoggettoCartella(IndiciSoggettoDTO indiciSoggettoCartella) {
		this.indiciSoggettoCartella = indiciSoggettoCartella;
	}
	public IndiciSoggettoDTO getIndiciSoggettoCartella() {
		return indiciSoggettoCartella;
	}
	public void setListaSoggettiICI(List<SitTIciSogg> listaSoggettiICI) {
		this.listaSoggettiICI = listaSoggettiICI;
	}
	public List<SitTIciSogg> getListaSoggettiICI() {
		return listaSoggettiICI;
	}
	public void setNumeroSoggettiICI(String numeroSoggettiICI) {
		this.numeroSoggettiICI = numeroSoggettiICI;
	}
	public String getNumeroSoggettiICI() {
		return numeroSoggettiICI;
	}
	public void setListaIndirizziCatasto(List<String> listaIndirizziCatasto) {
		this.listaIndirizziCatasto = listaIndirizziCatasto;
	}
	public List<String> getListaIndirizziCatasto() {
		return listaIndirizziCatasto;
	}
	public List<String> getListaIndirizziSIT() {
		return listaIndirizziSIT;
	}
	public void setListaIndirizziSIT(List<String> listaIndirizziSIT) {
		this.listaIndirizziSIT = listaIndirizziSIT;
	}
	public void setListaSoggettiCoinvolti(List<String> listaSoggettiCoinvolti) {
		this.listaSoggettiCoinvolti = listaSoggettiCoinvolti;
	}
	public List<String> getListaSoggettiCoinvolti() {
		return listaSoggettiCoinvolti;
	}
	public void setSoggettoIciSelezionato(SitTIciSogg soggettoIciSelezionato) {
		this.soggettoIciSelezionato = soggettoIciSelezionato;
	}
	public SitTIciSogg getSoggettoIciSelezionato() {
		return soggettoIciSelezionato;
	}
	public void setSoggettoIciDaVisualizzare(SitTIciSogg soggettoIciDaVisualizzare) {
		this.soggettoIciDaVisualizzare = soggettoIciDaVisualizzare;
	}
	public SitTIciSogg getSoggettoIciDaVisualizzare() {
		return soggettoIciDaVisualizzare;
	}
	public void setListaSoggettiTARSU(List<SitTTarSogg> listaSoggettiTARSU) {
		this.listaSoggettiTARSU = listaSoggettiTARSU;
	}
	public List<SitTTarSogg> getListaSoggettiTARSU() {
		return listaSoggettiTARSU;
	}
	public void setSoggettoTarsuDaVisualizzare(SitTTarSogg soggettoTarsuDaVisualizzare) {
		this.soggettoTarsuDaVisualizzare = soggettoTarsuDaVisualizzare;
	}
	public SitTTarSogg getSoggettoTarsuDaVisualizzare() {
		return soggettoTarsuDaVisualizzare;
	}
	public void setNumeroSoggettiTarsu(String numeroSoggettiTarsu) {
		this.numeroSoggettiTarsu = numeroSoggettiTarsu;
	}
	public String getNumeroSoggettiTarsu() {
		return numeroSoggettiTarsu;
	}
	public void setLabelDatiSoggettoTarsu(String labelDatiSoggettoTarsu) {
		this.labelDatiSoggettoTarsu = labelDatiSoggettoTarsu;
	}
	public String getLabelDatiSoggettoTarsu() {
		return labelDatiSoggettoTarsu;
	}
	public void setIndiceSoggettoTabellaTarsu(String indiceSoggettoTabellaTarsu) {
		this.indiceSoggettoTabellaTarsu = indiceSoggettoTabellaTarsu;
	}
	public String getIndiceSoggettoTabellaTarsu() {
		return indiceSoggettoTabellaTarsu;
	}
	public String getDenominazioneTarsu() {
		return denominazioneTarsu;
	}
	public void setDenominazioneTarsu(String denominazioneTarsu) {
		this.denominazioneTarsu = denominazioneTarsu;
	}
	public String getIndirizzoTarsu() {
		return indirizzoTarsu;
	}
	public void setIndirizzoTarsu(String indirizzoTarsu) {
		this.indirizzoTarsu = indirizzoTarsu;
	}
	public String getDescLabelIndirizzoTarsu() {
		return descLabelIndirizzoTarsu;
	}
	public void setDescLabelIndirizzoTarsu(String descLabelIndirizzoTarsu) {
		this.descLabelIndirizzoTarsu = descLabelIndirizzoTarsu;
	}
	public String getCodFiscalePIVATarsu() {
		return codFiscalePIVATarsu;
	}
	public void setCodFiscalePIVATarsu(String codFiscalePIVATarsu) {
		this.codFiscalePIVATarsu = codFiscalePIVATarsu;
	}
	public String getComuneResidenzaTarsu() {
		return comuneResidenzaTarsu;
	}
	public void setComuneResidenzaTarsu(String comuneResidenzaTarsu) {
		this.comuneResidenzaTarsu = comuneResidenzaTarsu;
	}
	public void setIndirizzoPanelTarsu(String indirizzoPanelTarsu) {
		this.indirizzoPanelTarsu = indirizzoPanelTarsu;
	}
	public String getIndirizzoPanelTarsu() {
		return indirizzoPanelTarsu;
	}
	public void setListaImmobiliDichiaratiTarsu(
			List<DatiTarsuDTO> listaImmobiliDichiaratiTarsu) {
		this.listaImmobiliDichiaratiTarsu = listaImmobiliDichiaratiTarsu;
	}
	public List<DatiTarsuDTO> getListaImmobiliDichiaratiTarsu() {
		return listaImmobiliDichiaratiTarsu;
	}
	public void setListaVersamentiTarsu(List<DatiImportiCncDTO> listaVersamentiTarsu) {
		this.listaVersamentiTarsu = listaVersamentiTarsu;
	}
	
	public List<DatiImportiCncDTO> getListaVersamentiTarsu() {
		return listaVersamentiTarsu;
	}
	public void setSoggettoTarsuSelezionato(SitTTarSogg soggettoTarsuSelezionato) {
		this.soggettoTarsuSelezionato = soggettoTarsuSelezionato;
	}
	public SitTTarSogg getSoggettoTarsuSelezionato() {
		return soggettoTarsuSelezionato;
	}
	public void setImmobileTarsu(DatiTarsuDTO immobileTarsu) {
		this.immobileTarsu = immobileTarsu;
	}
	public DatiTarsuDTO getImmobileTarsu() {
		return immobileTarsu;
	}
	public void setIndirizzoTARSU(String indirizzoTARSU) {
		this.indirizzoTARSU = indirizzoTARSU;
	}
	public String getIndirizzoTARSU() {
		return indirizzoTARSU;
	}
	public void setListaSoggettiCoinvoltiTarsu(
			List<String> listaSoggettiCoinvoltiTarsu) {
		this.listaSoggettiCoinvoltiTarsu = listaSoggettiCoinvoltiTarsu;
	}
	public List<String> getListaSoggettiCoinvoltiTarsu() {
		return listaSoggettiCoinvoltiTarsu;
	}
	public void setListaIndirizziCatastoTarsu(
			List<String> listaIndirizziCatastoTarsu) {
		this.listaIndirizziCatastoTarsu = listaIndirizziCatastoTarsu;
	}
	public List<String> getListaIndirizziCatastoTarsu() {
		return listaIndirizziCatastoTarsu;
	}
	
	public List<String> getListaIndirizziSITTarsu() {
		return listaIndirizziSITTarsu;
	}
	public void setListaIndirizziSITTarsu(List<String> listaIndirizziSITTarsu) {
		this.listaIndirizziSITTarsu = listaIndirizziSITTarsu;
	}
	public void setListaSoggettiCOSAP(List<SitTCosapContrib> listaSoggettiCOSAP) {
		this.listaSoggettiCOSAP = listaSoggettiCOSAP;
	}
	public List<SitTCosapContrib> getListaSoggettiCOSAP() {
		return listaSoggettiCOSAP;
	}
	public void setNumeroSoggettiCosap(String numeroSoggettiCosap) {
		this.numeroSoggettiCosap = numeroSoggettiCosap;
	}
	public String getNumeroSoggettiCosap() {
		return numeroSoggettiCosap;
	}
	public void setSoggettoCosapDaVisualizzare(SitTCosapContrib soggettoCosapDaVisualizzare) {
		this.soggettoCosapDaVisualizzare = soggettoCosapDaVisualizzare;
	}
	public SitTCosapContrib getSoggettoCosapDaVisualizzare() {
		return soggettoCosapDaVisualizzare;
	}
	public void setListaConcessioniCosap(
			List<SitTCosapTassa> listaConcessioniCosap) {
		this.listaConcessioniCosap = listaConcessioniCosap;
	}
	public List<SitTCosapTassa> getListaConcessioniCosap() {
		return listaConcessioniCosap;
	}
	public void setLabelDatiSoggettoCosap(String labelDatiSoggettoCosap) {
		this.labelDatiSoggettoCosap = labelDatiSoggettoCosap;
	}
	public String getLabelDatiSoggettoCosap() {
		return labelDatiSoggettoCosap;
	}
	public void setIndiceSoggettoTabellaCosap(String indiceSoggettoTabellaCosap) {
		this.indiceSoggettoTabellaCosap = indiceSoggettoTabellaCosap;
	}
	public String getIndiceSoggettoTabellaCosap() {
		return indiceSoggettoTabellaCosap;
	}
	public String getDenominazioneCosap() {
		return denominazioneCosap;
	}
	public void setDenominazioneCosap(String denominazioneCosap) {
		this.denominazioneCosap = denominazioneCosap;
	}
	public String getIndirizzoCosap() {
		return indirizzoCosap;
	}
	public void setIndirizzoCosap(String indirizzoCosap) {
		this.indirizzoCosap = indirizzoCosap;
	}
	public String getDescLabelIndirizzoCosap() {
		return descLabelIndirizzoCosap;
	}
	public void setDescLabelIndirizzoCosap(String descLabelIndirizzoCosap) {
		this.descLabelIndirizzoCosap = descLabelIndirizzoCosap;
	}
	public String getCodFiscalePIVACosap() {
		return codFiscalePIVACosap;
	}
	public void setCodFiscalePIVACosap(String codFiscalePIVACosap) {
		this.codFiscalePIVACosap = codFiscalePIVACosap;
	}
	public String getComuneResidenzaCosap() {
		return comuneResidenzaCosap;
	}
	public void setComuneResidenzaCosap(String comuneResidenzaCosap) {
		this.comuneResidenzaCosap = comuneResidenzaCosap;
	}
	public void setSoggettoCosapSelezionato(SitTCosapContrib soggettoCosapSelezionato) {
		this.soggettoCosapSelezionato = soggettoCosapSelezionato;
	}
	public List<DatiImportiCncDTO> getListaRuoliCNC() {
		return listaRuoliCNC;
	}
	public void setListaRuoliCNC(List<DatiImportiCncDTO> listaRuoliCNC) {
		this.listaRuoliCNC = listaRuoliCNC;
	}
	public String getDenominazioneCNC() {
		return denominazioneCNC;
	}
	public void setDenominazioneCNC(String denominazioneCNC) {
		this.denominazioneCNC = denominazioneCNC;
	}
	public String getIndirizzoCNC() {
		return indirizzoCNC;
	}
	public void setIndirizzoCNC(String indirizzoCNC) {
		this.indirizzoCNC = indirizzoCNC;
	}
	public String getDescLabelIndirizzoCNC() {
		return descLabelIndirizzoCNC;
	}
	public void setDescLabelIndirizzoCNC(String descLabelIndirizzoCNC) {
		this.descLabelIndirizzoCNC = descLabelIndirizzoCNC;
	}
	public String getCodFiscalePIVACNC() {
		return codFiscalePIVACNC;
	}
	public void setCodFiscalePIVACNC(String codFiscalePIVACNC) {
		this.codFiscalePIVACNC = codFiscalePIVACNC;
	}
	public String getComuneResidenzaCNC() {
		return comuneResidenzaCNC;
	}
	public void setComuneResidenzaCNC(String comuneResidenzaCNC) {
		this.comuneResidenzaCNC = comuneResidenzaCNC;
	}
	public List<VAnagrafica> getListaSoggettiCNC() {
		return listaSoggettiCNC;
	}
	public void setListaSoggettiCNC(List<VAnagrafica> listaSoggettiCNC) {
		this.listaSoggettiCNC = listaSoggettiCNC;
	}
	public String getNumeroSoggettiCNC() {
		return numeroSoggettiCNC;
	}
	public void setNumeroSoggettiCNC(String numeroSoggettiCNC) {
		this.numeroSoggettiCNC = numeroSoggettiCNC;
	}
	public VAnagrafica getSoggettoCNCDaVisualizzare() {
		return soggettoCNCDaVisualizzare;
	}
	public void setSoggettoCNCDaVisualizzare(VAnagrafica soggettoCNCDaVisualizzare) {
		this.soggettoCNCDaVisualizzare = soggettoCNCDaVisualizzare;
	}
	public VAnagrafica getSoggettoCNCSelezionato() {
		return soggettoCNCSelezionato;
	}
	public void setSoggettoCNCSelezionato(VAnagrafica soggettoCNCSelezionato) {
		this.soggettoCNCSelezionato = soggettoCNCSelezionato;
	}
	public SitTCosapContrib getSoggettoCosapSelezionato() {
		return soggettoCosapSelezionato;
	}
	public List<RedditiDicDTO> getListaRedditi() {
		return listaRedditi;
	}
	public void setListaRedditi(List<RedditiDicDTO> listaRedditi) {
		this.listaRedditi = listaRedditi;
	}
	public String getDenominazioneRedditi() {
		return denominazioneRedditi;
	}
	public void setDenominazioneRedditi(String denominazioneRedditi) {
		this.denominazioneRedditi = denominazioneRedditi;
	}
	public String getIndirizzoRedditi() {
		return indirizzoRedditi;
	}
	public void setIndirizzoRedditi(String indirizzoRedditi) {
		this.indirizzoRedditi = indirizzoRedditi;
	}
	public String getDescLabelIndirizzoRedditi() {
		return descLabelIndirizzoRedditi;
	}
	public void setDescLabelIndirizzoRedditi(String descLabelIndirizzoRedditi) {
		this.descLabelIndirizzoRedditi = descLabelIndirizzoRedditi;
	}
	public String getCodFiscalePIVARedditi() {
		return codFiscalePIVARedditi;
	}
	public void setCodFiscalePIVARedditi(String codFiscalePIVARedditi) {
		this.codFiscalePIVARedditi = codFiscalePIVARedditi;
	}
	public String getDomicilioFiscaleRedditi() {
		return domicilioFiscaleRedditi;
	}
	public void setDomicilioFiscaleRedditi(String domicilioFiscaleRedditi) {
		this.domicilioFiscaleRedditi = domicilioFiscaleRedditi;
	}
	public List<RedDatiAnagrafici> getListaSoggettiRedditi() {
		return listaSoggettiRedditi;
	}
	public void setListaSoggettiRedditi(List<RedDatiAnagrafici> listaSoggettiRedditi) {
		this.listaSoggettiRedditi = listaSoggettiRedditi;
	}
	public String getNumeroSoggettiRedditi() {
		return numeroSoggettiRedditi;
	}
	public void setNumeroSoggettiRedditi(String numeroSoggettiRedditi) {
		this.numeroSoggettiRedditi = numeroSoggettiRedditi;
	}
	public RedDatiAnagrafici getSoggettoRedditiDaVisualizzare() {
		return soggettoRedditiDaVisualizzare;
	}
	public void setSoggettoRedditiDaVisualizzare(
			RedDatiAnagrafici soggettoRedditiDaVisualizzare) {
		this.soggettoRedditiDaVisualizzare = soggettoRedditiDaVisualizzare;
	}
	public RedDatiAnagrafici getSoggettoRedditiSelezionato() {
		return soggettoRedditiSelezionato;
	}
	public void setSoggettoRedditiSelezionato(
			RedDatiAnagrafici soggettoRedditiSelezionato) {
		this.soggettoRedditiSelezionato = soggettoRedditiSelezionato;
	}
	public List<ComponenteFamigliaDTO> getListaComponentiNucleo() {
		return listaComponentiNucleo;
	}
	public void setListaComponentiNucleo(
			List<ComponenteFamigliaDTO> listaComponentiNucleo) {
		this.listaComponentiNucleo = listaComponentiNucleo;
	}
	public String getCognomeNomeNucleo() {
		return cognomeNomeNucleo;
	}
	public void setCognomeNomeNucleo(String cognomeNomeNucleo) {
		this.cognomeNomeNucleo = cognomeNomeNucleo;
	}

	public String getComuneDtNascitaNucleo() {
		return comuneDtNascitaNucleo;
	}
	public void setComuneDtNascitaNucleo(String comuneDtNascitaNucleo) {
		this.comuneDtNascitaNucleo = comuneDtNascitaNucleo;
	}
	public String getCodFiscaleNucleo() {
		return codFiscaleNucleo;
	}
	public void setCodFiscaleNucleo(String codFiscaleNucleo) {
		this.codFiscaleNucleo = codFiscaleNucleo;
	}
	public String getIndirizzoResidenzaNucleo() {
		return indirizzoResidenzaNucleo;
	}
	public void setIndirizzoResidenzaNucleo(String indirizzoResidenzaNucleo) {
		this.indirizzoResidenzaNucleo = indirizzoResidenzaNucleo;
	}
	public void setLabelDescTipoSogg(String labelDescTipoSogg) {
		this.labelDescTipoSogg = labelDescTipoSogg;
	}
	public String getLabelDescTipoSogg() {
		return labelDescTipoSogg;
	}
	public void setDtInizioResidenzaNucleo(String dtInizioResidenzaNucleo) {
		this.dtInizioResidenzaNucleo = dtInizioResidenzaNucleo;
	}
	public String getDtInizioResidenzaNucleo() {
		return dtInizioResidenzaNucleo;
	}
	public void setNumeroSoggettiNucleo(String numeroSoggettiNucleo) {
		this.numeroSoggettiNucleo = numeroSoggettiNucleo;
	}
	public String getNumeroSoggettiNucleo() {
		return numeroSoggettiNucleo;
	}
	public void setPosizioneAnagraficaNucleo(String posizioneAnagraficaNucleo) {
		this.posizioneAnagraficaNucleo = posizioneAnagraficaNucleo;
	}
	public String getPosizioneAnagraficaNucleo() {
		return posizioneAnagraficaNucleo;
	}
	public String getIndiceSoggettoTabellaCnc() {
		return indiceSoggettoTabellaCnc;
	}
	public void setIndiceSoggettoTabellaCnc(String indiceSoggettoTabellaCnc) {
		this.indiceSoggettoTabellaCnc = indiceSoggettoTabellaCnc;
	}
	public void setLabelDatiSoggettoCnc(String labelDatiSoggettoCnc) {
		this.labelDatiSoggettoCnc = labelDatiSoggettoCnc;
	}
	public String getLabelDatiSoggettoCnc() {
		return labelDatiSoggettoCnc;
	}
	public String getLabelDatiSoggettoRedditi() {
		return labelDatiSoggettoRedditi;
	}
	public void setLabelDatiSoggettoRedditi(String labelDatiSoggettoRedditi) {
		this.labelDatiSoggettoRedditi = labelDatiSoggettoRedditi;
	}
	public String getIndiceSoggettoTabellaRedditi() {
		return indiceSoggettoTabellaRedditi;
	}
	public void setIndiceSoggettoTabellaRedditi(String indiceSoggettoTabellaRedditi) {
		this.indiceSoggettoTabellaRedditi = indiceSoggettoTabellaRedditi;
	}
	public AcquaCarContrib getAcquaCarContrib() {
		return acquaCarContrib;
	}
	public void setAcquaCarContrib(AcquaCarContrib acquaCarContrib) {
		this.acquaCarContrib = acquaCarContrib;
	}
	public MulteCarContrib getMulteCarContrib() {
		return multeCarContrib;
	}
	public void setMulteCarContrib(MulteCarContrib multeCarContrib) {
		this.multeCarContrib = multeCarContrib;
	}
	public RetteCarContrib getRetteCarContrib() {
		return retteCarContrib;
	}
	public void setRetteCarContrib(RetteCarContrib retteCarContrib) {
		this.retteCarContrib = retteCarContrib;
	}
	public List<InterventoDTO> getListaInterventiCarSociale() {
		return listaInterventiCarSociale;
	}
	public void setListaInterventiCarSociale(List<InterventoDTO> listaInterventiCarSociale) {
		this.listaInterventiCarSociale = listaInterventiCarSociale;
	}
	public String getNumeroInterventiCarSociale() {
		return numeroInterventiCarSociale;
	}
	public void setNumeroInterventiCarSociale(String numeroInterventiCarSociale) {
		this.numeroInterventiCarSociale = numeroInterventiCarSociale;
	}
	// CARICA LA CARTELLA 	
	// preparaCartella=true--> significa che si proviene dalla funzione di preparazione cartella (se si produce il pdf, in automatico si inserisce la risposta)
	// preparaCartella=false--> significa che si NON proviene dalla funzione di preparazione cartella 
	public void LoadCartellaContribuente (boolean preparaCartella)
	{
		this.ClearAllFields();
		
		this.RecuperaRichiesta();
		
		this.preparaCartella =preparaCartella;
		if (soggettoCartella!=null)
		{
			logger.info("Gestione Fonte");
			
			this.GestioneFonte(); //Carico i flag per la visualizzazione delle fonti
			
			logger.info("Caricamento Intestazione");
			
			this.LoadIntestazioneCartella();
			
			logger.info("Caricamento Dati TARSU");
			
			if(tribTarsuFonte.isAbilitata()) this.LoadTabTARSU();
			
			logger.info("Caricamento Dati ICI");
			
			if(tribIciFonte.isAbilitata()) this.LoadTabICI();
			
			logger.info("Caricamento Dati CATASTO");
			
			if(locazioniFonte.isAbilitata() || catastoFonte.isAbilitata() || bolliVeicoliFonte.isAbilitata() ) this.LoadTabPatrimoniale();
			
			logger.info("Caricamento Dati COSAP");
			
			if(cosapFonte.isAbilitata())this.LoadTabCOSAP();
			
			logger.info("Caricamento Dati CNC");
			
			if(cncFonte.isAbilitata())this.LoadTabRuoliCNC();
			
			logger.info("Caricamento Dati REDDITI");
			
			if(redditiFonte.isAbilitata())this.LoadTabRedditi();
			
			logger.info("Caricamento Dati ACQUA");
			
			if(acquaFonte.isAbilitata()){
				acquaCarContrib.LoadTabACQUA(super.getUserBean().getEnteID(),
						super.getUserBean().getUsername(),
						soggettoCartella.getCodFis());
			}
			
			logger.info("Caricamento Dati MULTE");
			
			if(multeFonte.isAbilitata()){
				multeCarContrib.LoadTabMULTE(super.getUserBean().getEnteID(),
						super.getUserBean().getUsername(),
						soggettoCartella.getCodFis(),soggettoCartella.getNome(),
						soggettoCartella.getCognome());
			}
			
			logger.info("Caricamento Dati RETTE");
			
			if(retteFonte.isAbilitata()){
				retteCarContrib.LoadTabRETTE(super.getUserBean().getEnteID(),
						super.getUserBean().getUsername(),
						soggettoCartella.getCodFis(),soggettoCartella.getNome(),
						soggettoCartella.getCognome());
			}
			
			logger.info("Caricamento Dati PRATICHE");
			
			if(praticheFonte.isAbilitata()){
				pratichePortaleCarContrib.LoadTabPRATICHE(super.getUserBean().getEnteID(),
						super.getUserBean().getUsername(),
						soggettoCartella.getCodFis(),soggettoCartella.getNome(),
						soggettoCartella.getCognome());
			}
			
			logger.info("Caricamento Dati CAR.SOCIALE");
			
			if(carsocialeFonte.isAbilitata())this.LoadTabCarSociale();
			
			if(ruoliVersCarContrib.isFontiAbilitate()){
				ruoliVersCarContrib.LoadTabRuoliVersamenti(super.getUserBean().getEnteID(),
						super.getUserBean().getUsername(),
						soggettoCartella);
			}
			
			logger.info("Caricamento Dati IMU");
			
			if(imuFonte.isAbilitata())this.LoadTabConsulenzaImu();
			
			if (soggettoCartella.getTipoSogg().equals("F") 
				&& idVarSoggetto!=null
				&& nucleoFonte.isAbilitata())
				// solo SE idVarSoggetto è valorizzato cioè ha UN solo soggetto in listaIndiciSoggInAnagrafe
			{
				logger.info("Caricamento Dati NUCLEO FAMILIARE");
				this.LoadTabNucleoFamiliare();
			}
			else
				logger.info("NO NUCLEO FAM");
			
			
			
			
		}
		else
			logger.info("soggettoCartella == NULL");
	}
	
	// RECUPERA I DATI DELLA RICHIESTA
	private void RecuperaRichiesta()
	{
		RichiesteDTO richiestaDTO = new RichiesteDTO();
		
		Richieste richiesta = new Richieste();
		richiesta.setIdRic(idRichiestaCartella);
		
		richiestaDTO.setRich(richiesta);
		richiestaDTO.setEnteId(super.getUserBean().getEnteID());
		richiestaDTO.setUserId(super.getUserBean().getUsername());
		
		Richieste r =  super.getCarContribService().getRichiesta(richiestaDTO);
		
		if (r!=null)
		{
			strDataRichiesta =  Utility.dateToString_ddMMyyyy(r.getDtRic());
			if (r.getIdSoggRic()!=null)
			{
				SoggettiCarContribDTO soggCartDTO = new SoggettiCarContribDTO();
				
				SoggettiCarContrib soggetto = new SoggettiCarContrib();
				soggetto.setIdSogg(r.getIdSoggRic());
				
				soggCartDTO.setSogg(soggetto);
				soggCartDTO.setEnteId(super.getUserBean().getEnteID());
				soggCartDTO.setUserId(super.getUserBean().getUsername());				
				
				SoggettiCarContrib soggCartC = super.getCarContribService().getSoggetto(soggCartDTO);
				if (soggCartC!=null)
					strSoggettoRichiedente = soggCartC.getCognome() + " " + soggCartC.getNome();
			}
			else
			    strSoggettoRichiedente = " OPERATORE" ; //ALESSANDRA (viene utilizzato sulla pop-up di profilazione e l' viene mostrato l'operatore in una altra cella)
		}
	}
	
	// CARICA I DATI DELL'INTESTAZIONE
	private void LoadIntestazioneCartella()
	{
		indiciSoggettoCartella = super.getGeneralService().getIndiciSoggetto(this.getObjRicerca(Utility.TipoRicerca.INDICE), super.dataRiferimento);
		
		if (soggettoCartella.getTipoSogg().equals("F"))
		{// CERCO LA PERSONA FISICA NELL'ANAGRAFE
			labelDescTipoSogg="Codice Fiscale";
						
			List<String> listaIndiciSoggInAnagrafe =  indiciSoggettoCartella.getListaIdSoggAnagGen();

			if (listaIndiciSoggInAnagrafe==null || listaIndiciSoggInAnagrafe.size()==0)
			{
				logger.debug("IL SOGGETTO NON STA NELL'ANAGRAFE");
				intestazione = "SOGGETTO NON PRESENTE IN BANCA DATI DEMOGRAFIA  - " + this.soggettoCartella.getCognome() + " " + this.soggettoCartella.getNome();
			}	
			else
			{
				if (listaIndiciSoggInAnagrafe.size()==1)
				{
					RicercaSoggettoAnagrafeDTO rs = new RicercaSoggettoAnagrafeDTO();
					rs.setIdVarSogg(listaIndiciSoggInAnagrafe.get(0));
					idVarSoggetto=listaIndiciSoggInAnagrafe.get(0);
					rs.setEnteId(super.getUserBean().getEnteID());
					DatiPersonaDTO persona =  super.getAnagrafeService().getDatiPersonaById(rs);
					if (persona!=null)
					{
						String intest = persona.getPersona().getCognome() + " " + persona.getPersona().getNome();
						if (persona.getPersona().getDataNascita()!=null)
							intest += " - Nato il " + Utility.dateToString_ddMMyyyy(persona.getPersona().getDataNascita()) + " a ";
						else
							intest += " Nato a ";
						
						intest += persona.getDesComNas();
						if (persona.getSiglaProvNas()!=null && persona.getSiglaProvNas()!="")
							intest += " (" + persona.getSiglaProvNas()+ ")";
						
						intest += " - CF " + persona.getPersona().getCodfisc();
						
						soggettoCartella.setCodFis(persona.getPersona().getCodfisc());
						intestazione = intest;
					}
					else
						intestazione = "ERRORE - " + this.soggettoCartella.getCognome() + " " + this.soggettoCartella.getNome();
				}
				else
					intestazione= "SOGGETTO NON UNIVOCAMENTE IDENTIFICATO IN ANAGRAFE";
			}
		}
		else
		{// PERSONA GIURIDICA 
			labelDescTipoSogg = "Partita IVA";
			intestazione = this.soggettoCartella.getDenom() + " - P. IVA" + this.soggettoCartella.getParIva();
		}
		
		logger.debug("END LoadIntestazioneCartella");
	}
	
	// CARICA I DATI TAB ICI
	private void LoadTabICI()
	{
		
		RicercaSoggettoIciDTO soggettoICI = this.getDatiSoggettoICI();
		IciServiceCarContrib iciService = super.getIciServiceCarContrib();
		RicercaDTO ro = this.getObjRicerca(Utility.TipoRicerca.ICI);
		try{
			listaSoggettiICI = iciService.searchSoggettiCorrelatiIci(ro); 
		}catch(IndiceNonAllineatoException ie){
			this.addErrorMessage("cc.indice.disallineato", ie.getMessage());
		}
		
		numeroSoggettiICI = "0";
		
		if (listaSoggettiICI!=null && listaSoggettiICI.size()>0)
		{
			if (listaSoggettiICI.size()==1)
				numeroSoggettiICI = "1";
			else
				numeroSoggettiICI = "2";;
			
			if (soggettoIciDaVisualizzare==null || soggettoIciDaVisualizzare.getId()==null)
				// 	PRENDO L'ID DEL PRIMO SOGGETTO DELLA LISTA
				soggettoICI.setIdSoggIci(listaSoggettiICI.get(0).getId());
			else
				soggettoICI.setIdSoggIci(soggettoIciDaVisualizzare.getId());
			
			soggettoIciDaVisualizzare = super.getIciServiceCarContrib().getDatiIciSoggetto(soggettoICI);
			
			this.caricaDatiSoggettoICI();
			
			listaImmobiliDichiaratiICI = super.getIciServiceCarContrib().getDatiIci(soggettoICI, this.getIndiciSoggettoCartella());
			
			// RECUPERO LISTA VERSAMENTI ICI
			listaVersamenti = super.getIciServiceCarContrib().getVersamenti(soggettoICI, Utility.annoData(super.dataRiferimento));
			/*
			if (indiciSoggettoCartella.getListaIdSoggAnagCat()!=null && indiciSoggettoCartella.getListaIdSoggAnagCat().size()>0)
			{
				BigDecimal idSoggAnagCatasto = this.getIndiciSoggettoCartella().getListaIdSoggAnagCat().get(0);
			
				if (idSoggAnagCatasto!=null && idSoggAnagCatasto.intValue()>0)
				{
					//RECUPERO DATI IMMOBILI
					this.gestioneSezioneImmobiliICI(soggettoICI,idSoggAnagCatasto);
					
					//RECUPERO DATI TERRENI
					this.gestioneSezioneTerreniICI(soggettoICI,idSoggAnagCatasto);
				}
				else
					if (idSoggAnagCatasto==null)
						logger.debug("idSoggAnagCatasto NULLO");
					else
						logger.debug("idSoggAnagCatasto = " + idSoggAnagCatasto);
			}
			else
				logger.debug("IL SOGGETTO NON HA l'identificativo nell'anagrafe dei titolari catastali");

			// RECUPERO LISTA LOCAZIONI ICI
			listaLocazioni = this.caricaListaLocazioniSoggetto();
			//listaLocazioni = super.getLocazioniCarContribService().searchLocazioniBySogg(this.getObjRicerca(Utility.TipoRicerca.LOCAZIONI));
			 * 
			 */
		}
		else
			logger.debug("NESSUN SOGGETTO ICI TROVATO");
		
	}
	
	private void caricaDatiSoggettoICI()
	{
		if (soggettoIciDaVisualizzare!=null)
		{
			if (soggettoIciDaVisualizzare.getTipSogg().equals("F"))
			{
				this.setDenominazione(soggettoIciDaVisualizzare.getCogDenom() + " " + soggettoIciDaVisualizzare.getNome());

				this.setIndirizzo(soggettoIciDaVisualizzare.getDesComNsc() + " - " + Utility.dateToString_ddMMyyyy(soggettoIciDaVisualizzare.getDtNsc()));
				this.setCodFiscalePIVA(soggettoIciDaVisualizzare.getCodFisc());
				this.setDescLabelIndirizzo("Comune e Data di Nascita");
				this.setComuneResidenza(soggettoIciDaVisualizzare.getDesComRes());
			}
			else
			{
				this.setDenominazione(soggettoIciDaVisualizzare.getCogDenom());
				
				this.setIndirizzo(soggettoIciDaVisualizzare.getProvenienza());
				this.setCodFiscalePIVA(soggettoIciDaVisualizzare.getPartIva() + " - " + Utility.dateToString_ddMMyyyy(soggettoIciDaVisualizzare.getDtNsc()));
				this.setDescLabelIndirizzo("Data e Comune Sede");
				this.setComuneResidenza(soggettoIciDaVisualizzare.getDesComRes());
			}
		}
	}
	
	private List<LocazioniDTO> caricaListaLocazioniSoggetto(){
		List<LocazioniDTO> lista = new ArrayList<LocazioniDTO>();
		if(locazioniFonte.isAbilitata())
		 lista =  super.getLocazioniCarContribService().searchLocazioniBySogg(this.getObjRicerca(Utility.TipoRicerca.LOCAZIONI));
		return lista;
	}
	
	private List<BolloVeicolo> caricaListaBolliVeicoli(RicercaBolliVeicoliDTO rbv){
		List<BolloVeicolo> lista = new ArrayList<BolloVeicolo>();
		if(bolliVeicoliFonte.isAbilitata()){
		 lista =  super.getBolliVeicoliService().getBolliVeicoliByCFPI( rbv );
		}
		return lista;
	}//-------------------------------------------------------------------------
	
	/*
	private void gestioneSezioneImmobiliICI(RicercaSoggettoIciDTO ricSoggICI,BigDecimal idSoggCat)
	{
		RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
		rs.setEnteId(ricSoggICI.getEnteId());
		rs.setUserId(ricSoggICI.getUserId());
		rs.setIdSogg(idSoggCat);
		rs.setDtVal(Utility.newDate(this.recuperaAnnoRiferimentoICI(), 12, 31));
		rs.setDtRifA(Utility.newDate(this.recuperaAnnoRiferimentoICI(), 12, 31));
		rs.setDtRifDa(Utility.newDate(this.recuperaAnnoRiferimentoICI(), 1, 1));
		
		List<SitPatrimImmobileDTO> listaImm = new ArrayList<SitPatrimImmobileDTO>(); 

		//	IMMOBILI POSSEDUTI
		List<SitPatrimImmobileDTO> listaImmPosseduti = super.getCatastoCarContribService().getImmobiliPosseduti(rs, VIS_IN_SCHEDA_ICI);
			
		for (Iterator<SitPatrimImmobileDTO> i = listaImmPosseduti.iterator();i.hasNext();)
		{
			SitPatrimImmobileDTO imm= i.next();
			listaImm.add(imm);
		}
		
		// IMMOBILI CEDUTI
		List<SitPatrimImmobileDTO> listaImmCeduti = super.getCatastoCarContribService().getImmobiliCeduti(rs);
		for (Iterator<SitPatrimImmobileDTO> i = listaImmCeduti.iterator();i.hasNext();)
		{
			SitPatrimImmobileDTO imm= i.next();
			listaImm.add(imm);
		}
		
		listaImmobili = listaImm;
	}
	
	private void gestioneSezioneTerreniICI(RicercaSoggettoDTO ricSogg,BigDecimal idSoggCat)
	{
		RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
		rs.setEnteId(ricSogg.getEnteId());
		rs.setUserId(ricSogg.getUserId());
		rs.setIdSogg(idSoggCat);
		rs.setDtVal(Utility.newDate(this.recuperaAnnoRiferimentoICI(), 12, 31));
		rs.setDtRifA(Utility.newDate(this.recuperaAnnoRiferimentoICI(), 12, 31));
		rs.setDtRifDa(Utility.newDate(this.recuperaAnnoRiferimentoICI(), 1, 1));
		
		List<SitPatrimTerrenoDTO> listaT = new ArrayList<SitPatrimTerrenoDTO>();
		
		// TERRENI POSSEDUTI
		List<SitPatrimTerrenoDTO> listaTerrPosseduti = super.getCatastoCarContribService().getTerreniPosseduti(rs);
		if (listaTerrPosseduti!=null)
		{
			for (Iterator<SitPatrimTerrenoDTO> t = listaTerrPosseduti.iterator();t.hasNext();)
			{
				SitPatrimTerrenoDTO terr= t.next();
				listaT.add(terr);
			}
		}
		// TERRENI CEDUTI
		List<SitPatrimTerrenoDTO> listaTerrCeduti = super.getCatastoCarContribService().getTerreniCeduti(rs);
		if (listaTerrCeduti!=null)
		{
			for (Iterator<SitPatrimTerrenoDTO> t = listaTerrCeduti.iterator();t.hasNext();)
			{
				SitPatrimTerrenoDTO terr= t.next();
				listaT.add(terr);
			}
		}
		
		listaTerreni = listaT;
	}*/
	
	
	
	// Nasconde il pannello di scelta dei soggetti ICI e ricarica il tab dei dati ICI
	public String hideSoggettiICI ()
	{
		labelDatiSoggettoIci = " Sono mostrati i dati ICI riferiti al soggetto identificato nella lista dal numero " + indiceSoggettoTabellaIci;
		selectedTab="ajaxUno";
		if (soggettoIciSelezionato!=null) 
			soggettoIciDaVisualizzare= this.getSoggettoIciSelezionato();

		//alessandra
		this.LoadTabICI();
		
		soggettoIciSelezionato = null;
		
		return "goCartellaContribuente";
	}
	
	// Nasconde il pannello di scelta dei soggetti ICI e ricarica il tab dei dati ICI
	public void hideSoggettiIciPanel()
	{
		String i = indiceSoggettoTabellaIci;
		if (soggettoIciSelezionato!=null) {
			soggettoIciDaVisualizzare= this.getSoggettoIciSelezionato();
		}
		
		this.LoadTabICI();
		//this.LoadCartellaContribuente();

		labelDatiSoggettoIci = " Sono mostrati i dati ICI riferiti al soggetto identificato nella lista dal numero " + i;
		selectedTab="ajaxUnoPanel";

		soggettoIciSelezionato = null;
	}
	
	public void hideSoggettiIciPanelNucleoF()
	{
		String i = indiceSoggettoTabellaIci;
		if (soggettoIciSelezionato!=null) 
			soggettoIciDaVisualizzare= this.getSoggettoIciSelezionato();
		
		this.LoadTabICI();

		labelDatiSoggettoIci = " Sono mostrati i dati ICI riferiti al soggetto identificato nella lista dal numero " + i;
		selectedTab="ajaxUnoPanelNF";

		soggettoIciSelezionato = null;
	}
	
	// MOSTRA IL PANNELLO DI DETTAGLIO DEI DATI DICHIARAZIONE ICI
	public void showDettaglioICI ()
	{
		if (immobileIci!=null)
		{
			List<String> listaSoggetti = new ArrayList<String>();
			if (immobileIci.getListaSoggetti()!=null)
			{
				for (Iterator<VTIciSoggAll> s = this.immobileIci.getListaSoggetti().iterator();s.hasNext();)
				{
					VTIciSoggAll sogg= s.next();
					
					if (sogg.getTipSogg().equals("F"))
						listaSoggetti.add(sogg.getCogDenom() + " " + sogg.getNome() + " CF:" + sogg.getCodFisc() + " - " + sogg.getTipoSoggetto());
					else
						listaSoggetti.add(sogg.getCogDenom()+  " PI:" + sogg.getPartIva() + " - " + sogg.getTipoSoggetto());
				}
			}
			listaSoggettiCoinvolti = listaSoggetti;
			if (immobileIci.getIndirizzo()!=null)
				indirizzoICI = immobileIci.getIndirizzo().getDesIndirizzo();
			else
				indirizzoICI ="";
			
			if (this.immobileIci.getIndirizzoDaAnagrafe()!=null)
				indirizzoAnagrafe = this.immobileIci.getIndirizzoDaAnagrafe().getDesIndirizzo();
			else
				indirizzoAnagrafe="";
			
			List<String> listaInd = new ArrayList<String>();
			if (immobileIci.getListaIndirizziDaCatasto()!=null)
			{
				for (Iterator<IndirizzoIciTarsuDTO> i = immobileIci.getListaIndirizziDaCatasto().iterator();i.hasNext();)
				{
					listaInd.add(i.next().getDesIndirizzo());
				}
			}
			listaIndirizziCatasto = listaInd;
			
			List<String> listaIndSIT = new ArrayList<String>();
			if (immobileIci.getListaIndirizziSIT()!=null)
			{
				for (Iterator<IndirizzoIciTarsuDTO> i = immobileIci.getListaIndirizziSIT().iterator();i.hasNext();)
				{
					listaIndSIT.add(i.next().getDesIndirizzo());
				}
			}
			listaIndirizziSIT= listaIndSIT;
		}
	}
	
	private void LoadTabPatrimoniale(){
		
		// Intestazione Situazione Patrimoniale 
		if(!visStoricoCatasto)
			intestazioneSituazionePatrimoniale ="SITUAZIONE PATRIMONIALE ATTUALE";
		else
			intestazioneSituazionePatrimoniale =
				"SITUAZIONE PATRIMONIALE DAL " + "01/01/" + new Integer(Utility.annoData(Utility.addYear(this.dataRiferimento, -4))).toString();
		
		intestazioneSituazionePatrimoniale+=" E LOCAZIONI";
					
		RicercaSoggettoDTO soggettoCatasto = this.getDatiSoggettoCatasto();
		
		if (indiciSoggettoCartella!=null )
		{
			List<BigDecimal> lstIdsAnagCat = indiciSoggettoCartella.getListaIdSoggAnagCat();
			if (lstIdsAnagCat!=null && lstIdsAnagCat.size()>0)
			{
				BigDecimal idSoggAnagCatasto = lstIdsAnagCat.get(0);
				if (idSoggAnagCatasto!=null && idSoggAnagCatasto.intValue()>0)
					//RECUPERO DATI IMMOBILI TARSU
					this.gestioneSezioneImmobili(soggettoCatasto,idSoggAnagCatasto);
				
					//RECUPERO DATI TERRENI
					this.gestioneSezioneTerreni(soggettoCatasto,idSoggAnagCatasto);
			}
			else
				logger.debug("IL SOGGETTO NON HA l'identificativo nell'anagrafe dei titolari catastali");
		}
		else
			logger.debug(" getIndiciSoggettoCartella == NULL");

		// RECUPERO LISTA LOCAZIONI 
		listaLocazioni = this.caricaListaLocazioniSoggetto();	
		
		//BOLLI VEICOLI
		logger.info("Caricamento Bolli Veicoli Soggetto");
		RicercaBolliVeicoliDTO rbv = new RicercaBolliVeicoliDTO();
		rbv.setEnteId(soggettoCatasto.getEnteId());
		rbv.setUserId(soggettoCatasto.getUserId());
		rbv.setNome(soggettoCatasto.getNome());
		rbv.setCognome(soggettoCatasto.getCognome());
		rbv.setRagioneSociale(soggettoCatasto.getDenom());
		String cf = soggettoCatasto.getCodFis();
		String pi = soggettoCatasto.getParIva();
		if (cf != null && !cf.trim().equalsIgnoreCase(""))
			rbv.setCodiceFiscalePartitaIva(cf);
		if (pi != null && !pi.trim().equalsIgnoreCase(""))
			rbv.setCodiceFiscalePartitaIva(pi);
		listaBolliVeicoli = this.caricaListaBolliVeicoli(rbv);
		
	}
	
	private void LoadTabTARSU()
	{

			try{
				listaSoggettiTARSU = super.getTarsuCarContribService().searchSoggettiCorrelatiTarsu(this.getObjRicerca(Utility.TipoRicerca.TARSU));
			}catch(IndiceNonAllineatoException ie){
				this.addErrorMessage("cc.indice.disallineato", ie.getMessage());
			}
			numeroSoggettiTarsu = "0";
			
			if (listaSoggettiTARSU!=null && listaSoggettiTARSU.size()>0)
			{
				RicercaSoggettoTarsuDTO soggettoTARSU = this.getDatiSoggettoTARSU();
				
				if (listaSoggettiTARSU.size()==1)
					numeroSoggettiTarsu = "1";
				else
					numeroSoggettiTarsu = "2";
				
				logger.debug(" NUM SOGGETTI TARSU TROVATI = " + this.getListaSoggettiTARSU().size());
				
				if (soggettoTarsuDaVisualizzare==null || soggettoTarsuDaVisualizzare.getId()==null)
				{// PRENDO L'ID DEL PRIMO SOGGETTO DELLA LISTA
					soggettoTARSU.setIdSoggTarsu(this.getListaSoggettiTARSU().get(0).getId());
				}
				else
					soggettoTARSU.setIdSoggTarsu(soggettoTarsuDaVisualizzare.getId());
				
				soggettoTarsuDaVisualizzare = super.getTarsuCarContribService().getDatiTarsuSoggetto(soggettoTARSU);
				
				this.caricaDatiSoggettoTARSU(soggettoTarsuDaVisualizzare);
				// RECUPERO LISTA IMMOBILI TARSU
				listaImmobiliDichiaratiTarsu = super.getTarsuCarContribService().getDatiTarsu(soggettoTARSU, this.getIndiciSoggettoCartella());
				
				if (listaImmobiliDichiaratiTarsu==null || listaImmobiliDichiaratiTarsu.size()==0)
					logger.debug("LISTA IMMOBILI TARSU NON PRESENTE");
				
				// RECUPERO LISTA VERSAMENTI TARSU
				listaVersamentiTarsu = super.getTarsuCarContribService().getVersamenti(soggettoTARSU, Utility.annoData(super.dataRiferimento), this.COD_TIPI_TRIBUTO_TARSU);
				
				//Sposto blocco per il recupero dati patrimoniali in un'altra Tab
				/*
				if (indiciSoggettoCartella!=null )
				{
					if (indiciSoggettoCartella.getListaIdSoggAnagCat()!=null && indiciSoggettoCartella.getListaIdSoggAnagCat().size()>0)
					{
						BigDecimal idSoggAnagCatasto = this.getIndiciSoggettoCartella().getListaIdSoggAnagCat().get(0);
						if (idSoggAnagCatasto!=null && idSoggAnagCatasto.intValue()>0)
							//RECUPERO DATI IMMOBILI TARSU
							this.gestioneSezioneImmobiliTARSU(soggettoTARSU,idSoggAnagCatasto);
					}
					else
						logger.debug("IL SOGGETTO NON HA l'identificativo nell'anagrafe dei titolari catastali");
				}
				else
					logger.debug(" getIndiciSoggettoCartella == NULL");
	
				// RECUPERO LISTA LOCAZIONI TARSU
				listaLocazioniTarsu = this.caricaListaLocazioniSoggetto();
						//super.getLocazioniCarContribService().searchLocazioniBySogg(this.getObjRicerca(Utility.TipoRicerca.LOCAZIONI));
				 */			 
			}
			else
				logger.debug("NESSUN SOGGETTO TARSU TROVATO");
		
	}
	
/*	private void gestioneSezioneImmobiliTARSU(RicercaSoggettoTarsuDTO ricSoggTarsu,BigDecimal idSoggCat)
	{
		RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
		
		rs.setEnteId(ricSoggTarsu.getEnteId());
		rs.setUserId(ricSoggTarsu.getUserId());
		rs.setIdSogg(idSoggCat);
		rs.setDtRifDa(Utility.newDate(Utility.annoData(Utility.addYear(this.dataRiferimento, -4)), 1, 1));
		rs.setDtRifA(this.dataRiferimento);
		
		listaImmobiliTarsu = super.getCatastoCarContribService().getImmobiliPosseduti(rs, this.VIS_IN_SCHEDA_TARSU);
	}*/

	private void gestioneSezioneImmobili(RicercaSoggettoDTO ricSogg,BigDecimal idSoggCat)
	{
		RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
		
		rs.setEnteId(ricSogg.getEnteId());
		rs.setUserId(ricSogg.getUserId());
		rs.setIdSogg(idSoggCat);
		if(!visStoricoCatasto)
			rs.setDtVal(this.dataRiferimento);
		else{
			rs.setDtRifDa(Utility.newDate(Utility.annoData(Utility.addYear(this.dataRiferimento, -4)), 1, 1));
			rs.setDtRifA(this.dataRiferimento);
		}
		
		listaImmobili = super.getCatastoCarContribService().getImmobiliPosseduti(rs, this.VIS_IN_SCHEDA_TARSU);
	}
	
	private void gestioneSezioneTerreni(RicercaSoggettoDTO ricSogg,BigDecimal idSoggCat)
	{
		RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
		rs.setEnteId(ricSogg.getEnteId());
		rs.setUserId(ricSogg.getUserId());
		rs.setIdSogg(idSoggCat);
		
		List<SitPatrimTerrenoDTO> listaT = new ArrayList<SitPatrimTerrenoDTO>();
		
		rs.setDtVal(this.dataRiferimento); //Ricerca Terreni Posseduti non utilizza le altre date
		
		// TERRENI POSSEDUTI
		List<SitPatrimTerrenoDTO> listaTerrPosseduti = super.getCatastoCarContribService().getTerreniPosseduti(rs);
		if (listaTerrPosseduti!=null)
		{
			for (Iterator<SitPatrimTerrenoDTO> t = listaTerrPosseduti.iterator();t.hasNext();)
			{
				SitPatrimTerrenoDTO terr= t.next();
				listaT.add(terr);
			}
		}
			
	   if(visStoricoCatasto){
			//Range dati per recuperare la lista di terreni ceduti
			rs.setDtRifDa(Utility.newDate(Utility.annoData(Utility.addYear(this.dataRiferimento, -4)), 1, 1));
			rs.setDtRifA(this.dataRiferimento);
			// TERRENI CEDUTI
			List<SitPatrimTerrenoDTO> listaTerrCeduti = super.getCatastoCarContribService().getTerreniCeduti(rs);
			if (listaTerrCeduti!=null)
			{
				for (Iterator<SitPatrimTerrenoDTO> t = listaTerrCeduti.iterator();t.hasNext();)
				{
					SitPatrimTerrenoDTO terr= t.next();
					listaT.add(terr);
				}
			}
	   }
		
		listaTerreni = listaT;
	}
	
	private void caricaDatiSoggettoTARSU(SitTTarSogg soggInfo)
	{
		if (soggInfo!=null)
		{
			if (soggInfo.getTipSogg().equals("F"))
			{
				denominazioneTarsu = soggInfo.getCogDenom() + " " + soggInfo.getNome();
				indirizzoTarsu= soggInfo.getDesComNsc() + " - " + Utility.dateToString_ddMMyyyy(soggInfo.getDtNsc());
				codFiscalePIVATarsu = soggInfo.getCodFisc();
				descLabelIndirizzoTarsu = "Comune e Data di Nascita";
				comuneResidenzaTarsu = soggInfo.getDesComRes();
			}
			else
			{
				denominazioneTarsu =soggInfo.getCogDenom();
				indirizzoTarsu =soggInfo.getProvenienza();
				codFiscalePIVATarsu = soggInfo.getPartIva() + " - " + Utility.dateToString_ddMMyyyy(soggInfo.getDtNsc());
				descLabelIndirizzoTarsu = "Data e Comune Sede";
				comuneResidenzaTarsu = soggInfo.getDesComRes();
			}
		}
	}
	
	// Nasconde il pannello di scelta dei soggetti TARSU e ricarica il tab dei dati TARSU
	public String hideSoggettiTARSU ()
	{
		labelDatiSoggettoTarsu = " Sono mostrati i dati TARSU riferiti al soggetto identificato nella lista dal numero " + indiceSoggettoTabellaTarsu;
		selectedTab="ajaxDue";
		logger.debug( " labelDatiSoggettoTarsu == " + labelDatiSoggettoTarsu);
		if (soggettoTarsuSelezionato!=null)
			soggettoTarsuDaVisualizzare = soggettoTarsuSelezionato;

		soggettoTarsuSelezionato=null;
		
		//RICARICO TAB TARSU
		//alessandra
		this.LoadTabTARSU();
		
		return "goCartellaContribuente";
	}
	
	public void hideSoggettiTarsuPanel()
	{
		String i = indiceSoggettoTabellaTarsu;

		if (soggettoTarsuSelezionato!=null)
			soggettoTarsuDaVisualizzare = soggettoTarsuSelezionato;

		soggettoTarsuSelezionato=null;
		
		//RICARICO TAB TARSU
		this.LoadTabTARSU();
		
		labelDatiSoggettoTarsu = " Sono mostrati i dati TARSU riferiti al soggetto identificato nella lista dal numero " + i;
		selectedTab="ajaxDuePanel";
	}
	
	// MOSTRA IL PANNELLO DI DETTAGLIO DEI DATI DICHIARAZIONE TARSU
	public void showDettaglioTARSU ()
	{
		List<String> listaSoggetti = new ArrayList<String>();
		if (immobileTarsu.getListaSoggetti()!=null)
		{
			for (Iterator<SoggettoTarsuDTO> s = this.getImmobileTarsu().getListaSoggetti().iterator();s.hasNext();)
			{
				SoggettoTarsuDTO sogg= s.next();
				if (sogg.getSoggetto().getTipSogg().equals("F"))
					listaSoggetti.add(sogg.getSoggetto().getCogDenom() + " " + sogg.getSoggetto().getNome() + " CF:" + sogg.getSoggetto().getCodFisc() + " - " + sogg.getTitolo());
				else
					listaSoggetti.add(sogg.getSoggetto().getCogDenom()+  " PI:" + sogg.getSoggetto().getPartIva() + " - " + sogg.getTitolo());
			}
		}
		listaSoggettiCoinvoltiTarsu=listaSoggetti;
		
		indirizzoPanelTarsu = immobileTarsu.getIndirizzo().getDesIndirizzo();
		
		List<String> listaInd = new ArrayList<String>();
		if (this.getImmobileTarsu().getListaIndirizziDaCatasto()!=null)
		{
			for (Iterator<IndirizzoIciTarsuDTO> i = this.getImmobileTarsu().getListaIndirizziDaCatasto().iterator();i.hasNext();)
			{
				listaInd.add(i.next().getDesIndirizzo());
			}
		}
		listaIndirizziCatastoTarsu=listaInd;
		//indirizzi SIT
		List<String> listaIndSIT = new ArrayList<String>();
		if (this.getImmobileTarsu().getListaIndirizziSIT()!=null)
		{
			for (Iterator<IndirizzoIciTarsuDTO> i = this.getImmobileTarsu().getListaIndirizziSIT().iterator();i.hasNext();)
			{
				listaIndSIT.add(i.next().getDesIndirizzo());
			}
		}
		listaIndirizziSITTarsu=listaIndSIT;
	}

	// CARICA I DATI TAB COSAP
	private void LoadTabCOSAP()
	{
		//RECUPERO DATI SOGGETTI COSAP
		RicercaSoggettoCosapDTO soggettoCOSAP = this.getDatiSoggettoCOSAP();
		
		listaSoggettiCOSAP = super.getCosapCarContribService().searchSoggettoCosap(soggettoCOSAP);
		
		numeroSoggettiCosap="0";
		
		if (listaSoggettiCOSAP!=null && listaSoggettiCOSAP.size()>0)
		{
			if (listaSoggettiCOSAP.size()==1)
				numeroSoggettiCosap="1";
			else
				numeroSoggettiCosap ="2";
			
			logger.debug(" NUM SOGGETTI COPAS TROVATI = " + listaSoggettiCOSAP.size());
			
			if (soggettoCosapDaVisualizzare==null || soggettoCosapDaVisualizzare.getId()==null)
			// 	PRENDO L'ID DEL PRIMO SOGGETTO DELLA LISTA
				soggettoCOSAP.setIdSoggCosap(this.getListaSoggettiCOSAP().get(0).getId());
			else
				soggettoCOSAP.setIdSoggCosap(soggettoCosapDaVisualizzare.getId());

			soggettoCosapDaVisualizzare = super.getCosapCarContribService().getDatiCosapSoggetto(soggettoCOSAP);
			
			this.caricaDatiSoggettoCOSAP(soggettoCosapDaVisualizzare);
			
			listaConcessioniCosap = super.getCosapCarContribService().getDatiCosap(soggettoCOSAP, Utility.addYear(super.dataRiferimento,-1));
		}
		else
			logger.debug("NESSUN SOGGETTO COSAP TROVATO");

		logger.debug("+++++++++++++++ END LETTURA COSAP");
	}
	
	private void caricaDatiSoggettoCOSAP(SitTCosapContrib soggInfo)
	{
		if (soggInfo!=null)
		{
			if (soggInfo.getTipoPersona().equals("F"))
			{
				denominazioneCosap = soggInfo.getCogDenom() + " " + soggInfo.getNome();
				indirizzoCosap = soggInfo.getDescComuneNascita() + " - " + Utility.dateToString_ddMMyyyy(soggInfo.getDtNascita());
				codFiscalePIVACosap = soggInfo.getCodiceFiscale();
				descLabelIndirizzoCosap= "Comune e Data di Nascita";
				comuneResidenzaCosap =soggInfo.getDescComuneResidenza();
			}
			else
			{
				denominazioneCosap=soggInfo.getCogDenom();
				indirizzoCosap =soggInfo.getProvenienza();
				codFiscalePIVACosap= soggInfo.getPartitaIva() + " - " + Utility.dateToString_ddMMyyyy(soggInfo.getDtNascita());
				descLabelIndirizzoCosap="Data e Comune Sede";
				comuneResidenzaCosap=soggInfo.getDescComuneResidenza();
			}
		}
	}

	// Nasconde il pannello di scelta dei soggetti COSAP e ricarica il tab dei dati COSAP
	public String hideSoggettiCOSAP ()
	{
		labelDatiSoggettoCosap = " Sono mostrati i dati COSAP riferiti al soggetto identificato nella lista dal numero " + indiceSoggettoTabellaCosap;
		selectedTab="ajaxTre";
		if (soggettoCosapSelezionato!=null)
			soggettoCosapDaVisualizzare =this.getSoggettoCosapSelezionato();

		soggettoCosapSelezionato=null;
		
		//RICARICO TAB
		//alessandra
		//this.LoadCartellaContribuente();
		//alessandra
		this.LoadTabCOSAP();
		
		return "goCartellaContribuente";
	}
	
	public void hideSoggettiCosapPanel ()
	{
		String i = indiceSoggettoTabellaCosap;
		
		if (soggettoCosapSelezionato!=null)
			soggettoCosapDaVisualizzare =this.getSoggettoCosapSelezionato();

		soggettoCosapSelezionato=null;
		
		//RICARICO TAB
		this.LoadTabCOSAP();
		
		labelDatiSoggettoCosap = " Sono mostrati i dati COSAP riferiti al soggetto identificato nella lista dal numero " + i;
		selectedTab="ajaxTrePanel";
	}
	
	//CARICA I DATI TAB RUOLI CNC
	private void LoadTabRuoliCNC()
	{		
		logger.debug("--------------------  START LoadTabRuoliCNC");
		
		RicercaCncDTO soggettoCNC = this.getDatiSoggettoCNC();
		 
		listaSoggettiCNC = super.getCncCarContribService().getAnagraficaDebitore(soggettoCNC);
		
		numeroSoggettiCNC = "0";
		
		if (listaSoggettiCNC!=null && listaSoggettiCNC.size()>0)
		{
			if (listaSoggettiCNC.size()==1)
				numeroSoggettiCNC ="1";
			else
				numeroSoggettiCNC ="2";
			
			logger.debug(" NUM SOGGETTI CNC TROVATI = " + listaSoggettiCNC.size());
			
			if (soggettoCNCDaVisualizzare==null || soggettoCNCDaVisualizzare.getId()==null)
				soggettoCNCDaVisualizzare = listaSoggettiCNC.get(0);
		
			this.caricaDatiSoggettoCNC(soggettoCNCDaVisualizzare);
			
			// ******* START RECUPERO DICHIARAZIONI CNC
			logger.debug("******* START RECUPERO DICHIARAZIONI CNC");
			listaRuoliCNC = super.getCncCarContribService().getDatiCNC(soggettoCNC, Utility.annoData(super.dataRiferimento));
			logger.debug("******* END RECUPERO DICHIARAZIONI CNC");
			// ******* END RECUPERO DICHIARAZIONI CNC
		}
		else
			logger.debug("NESSUN RUOLO CNC TROVATO");
		
		logger.debug("--------------------  END LoadTabRuoliCNC");
	}
	
	private void caricaDatiSoggettoCNC(VAnagrafica soggCNC)
	{
		if (soggCNC!=null)
		{
			if (soggCNC.getCodFiscale()!=null && soggCNC.getCodFiscale()!="")
			{
				denominazioneCNC = soggCNC.getCognome() + " " + soggCNC.getNome();
				indirizzoCNC = soggCNC.getIndirizzoAt() + " - " + Utility.formatStringDate(soggCNC.getDtNascita());
				codFiscalePIVACNC=soggCNC.getCodFiscale();
				descLabelIndirizzoCNC="Comune e Data di Nascita";
				comuneResidenzaCNC=soggCNC.getDtValidDomicFiscale();
			}
			else
			{
				denominazioneCNC=soggCNC.getDenominazione();
				indirizzoCNC=soggCNC.getIndirizzoSoc();
				codFiscalePIVACNC=soggCNC.getPartitaIva() + " - " + Utility.formatStringDate(soggCNC.getDtNascita());
				descLabelIndirizzoCosap="Data e Comune Sede";
				comuneResidenzaCosap=soggCNC.getDtValidDomicFiscale();
			}
		}
	}
	
	// Nasconde il pannello di scelta dei soggetti CNC e ricarica il tab dei dati
	public String hideSoggettiCNC ()
	{
		labelDatiSoggettoCnc = " Sono mostrati i dati CNC riferiti al soggetto identificato nella lista dal numero " + indiceSoggettoTabellaCnc;
		selectedTab="ajaxQuattro";
		if (soggettoCNCSelezionato!=null)
			soggettoCNCDaVisualizzare = this.getSoggettoCNCSelezionato();

		soggettoCNCSelezionato = null;
		
		//RICARICO TAB CNC
		//alessandra
		//this.LoadCartellaContribuente();
		this.LoadTabRuoliCNC();
		
		return "goCartellaContribuente";
	}
	
	public void hideSoggettiCncPanel()
	{
		String i = indiceSoggettoTabellaCnc;
		
		if (soggettoCNCSelezionato!=null)
			soggettoCNCDaVisualizzare = this.getSoggettoCNCSelezionato();

		soggettoCNCSelezionato = null;
		
		//RICARICO TAB CNC
		this.LoadTabRuoliCNC();
		
		labelDatiSoggettoCnc = " Sono mostrati i dati CNC riferiti al soggetto identificato nella lista dal numero " + i;
		selectedTab="ajaxQuattroPanel";
	}
	
	// CARICA I DATI TAB CARTELLA SOCIALE
	private void LoadTabCarSociale()
	{
		RicercaCarSocDTO ric = this.getSoggettoPerRicercaCarSociale();
		listaInterventiCarSociale = super.getCarSocialeCarContribService().getInterventiByCF(ric);
		for (Iterator<InterventoDTO> it = listaInterventiCarSociale.iterator();it.hasNext();)
		{
			InterventoDTO i = it.next();			
			i.setDtInizioValStr(Utility.dateToString_ddMMyyyy(i.getDtInizioVal()));
			i.setDtFineValStr(Utility.dateToString_ddMMyyyy(i.getDtFineVal()));
			if (i.getImportoErogato() != null){
				i.setImportoErogatoStr(StringUtility.DF.format(i.getImportoErogato()));
			}
		}
		
		numeroInterventiCarSociale = "0";
		if (listaInterventiCarSociale != null && listaInterventiCarSociale.size() > 0)
		{
			numeroInterventiCarSociale = "1";
		}	
	}
	
	// CARICA I DATI TAB CONSULENZA IMU
	private void LoadTabConsulenzaImu()
	{
		SaldoImuBaseDTO search = this.getSoggettoPerSaldoImu();
		
		consulenzaImu = super.getImuService().getConsulenza(search);
		if(consulenzaImu!=null){
			datiElabImu = super.getImuService().getJsonDTOUltimaElaborazione(search);
			logger.debug("Dati input Consulenza SaldoImu per il soggetto "+search.getCodfisc()+" "+(datiElabImu!=null ? "trovata" : "non trovata"));
		}
		logger.debug("Consulenza SaldoImu per il soggetto "+search.getCodfisc()+" "+(consulenzaImu!=null ? "trovata" : "non trovata"));
		
	}
	

	
	
	// CARICA I DATI TAB REDDITI DICHIARATI
	private void LoadTabRedditi()
	{
		//listaSoggettiRedditi = super.getRedditiCarContribService().searchSoggettiCorrelatiRedditi(this.getObjRicerca(Utility.TipoRicerca.OTHER));
		RicercaDTO ric = this.getObjRicerca(Utility.TipoRicerca.REDDITI);
		listaSoggettiRedditi = super.getRedditiCarContribService().searchSoggettiCorrelatiRedditi(ric);
		numeroSoggettiRedditi ="0";
		
		if (listaSoggettiRedditi!=null && listaSoggettiRedditi.size()>0)
		{
			RicercaSoggettoDTO soggettoRedditi = this.getDatiSoggettoRedditi();
			
			if (listaSoggettiRedditi.size()==1)
				numeroSoggettiRedditi="1";
			else
				numeroSoggettiRedditi="2";
			
			logger.debug("NUM SOGGETTI REDDITI TROVATI = " + listaSoggettiRedditi.size());
			
			if (soggettoRedditiDaVisualizzare==null || soggettoRedditiDaVisualizzare.getIdeTelematico()==null)
				// 	PRENDO IL PRIMO SOGGETTO DELLA LISTA
				soggettoRedditiDaVisualizzare =listaSoggettiRedditi.get(0);
			
			KeySoggDTO key = new KeySoggDTO();
			key.setEnteId(super.getUserBean().getEnteID());
			key.setUserId(super.getUserBean().getUsername());
			if (soggettoRedditi.getTipoSogg().equals("F"))
				key.setCodFis(soggettoRedditi.getCodFis());
			else
				key.setCodFis(soggettoRedditi.getParIva());
			key.setIdeTelematico(soggettoRedditiDaVisualizzare.getIdeTelematico());
		
			this.caricaDatiSoggettoRedditi(key,soggettoRedditi);
			
			listaRedditi = super.getRedditiCarContribService().getRedditiByKey(key);
		}
		else
			logger.debug("NESSUN REDDITI DICHIARATO TROVATO");
	}
	
	private void caricaDatiSoggettoRedditi(KeySoggDTO key,RicercaSoggettoDTO soggInfo)
	{
		RedDatiAnagrafici soggRedditi = super.getRedditiCarContribService().getSoggettoByKey(key);
		
		if (soggRedditi!=null)
		{
			if (soggInfo.getTipoSogg().equals("F"))
			{
				denominazioneRedditi = soggRedditi.getCognome() + " " + soggRedditi.getNome();
				indirizzoRedditi= soggRedditi.getDesComuneNascita() + " " + Utility.formatStringDate(soggRedditi.getDataNascita());
				codFiscalePIVARedditi=soggInfo.getCodFis();
				descLabelIndirizzoRedditi="Comune e Data di Nascita";
			}
			else
			{
				denominazioneRedditi=soggRedditi.getDenominazione();
				indirizzoRedditi=soggRedditi.getDesComuneNascita();
				codFiscalePIVARedditi=soggInfo.getParIva();
				descLabelIndirizzoRedditi="Data e Comune Sede";
			}
		}
		RedDomicilioFiscale domicilioRedditi = super.getRedditiCarContribService().getDomicilioByKey(key);
		if (domicilioRedditi!=null)
		{
			String indirizzo = domicilioRedditi.getIndirizzoAttuale();
			if(domicilioRedditi.getCap()!=null&&domicilioRedditi.getCap()!="")
				indirizzo += " - " + domicilioRedditi.getCap();
			
			domicilioFiscaleRedditi=domicilioRedditi.getDesComuneDomFiscaleAttuale() + " / " +  domicilioRedditi.getDesComuneDomFiscaleDic();
		}
	}

	// Nasconde il pannello di scelta dei redditi
	public String hideSoggettiRedditi()
	{
		labelDatiSoggettoRedditi = " Sono mostrati i dati Redditi riferiti al soggetto identificato nella lista dal numero " + indiceSoggettoTabellaRedditi;
		selectedTab="ajaxCinque";
		if (soggettoRedditiSelezionato!=null)
			soggettoRedditiDaVisualizzare = soggettoRedditiSelezionato;

		soggettoRedditiSelezionato= null;
		
		//RICARICO TAB
		//alessandra
		//this.LoadCartellaContribuente();
		this.LoadTabRedditi();
		
		return "goCartellaContribuente";
	}
	
	public void hideSoggettiRedditiPanel()
	{
		String i = indiceSoggettoTabellaRedditi;
		
		if (soggettoRedditiSelezionato!=null)
			soggettoRedditiDaVisualizzare = soggettoRedditiSelezionato;

		soggettoRedditiSelezionato= null;
		
		//RICARICO TAB
		this.LoadTabRedditi();
		
		labelDatiSoggettoRedditi = " Sono mostrati i dati Redditi riferiti al soggetto identificato nella lista dal numero " + i;
		selectedTab="ajaxCinquePanel";
	}
	
	// Carica i dato del nucleo familiare 
	private void LoadTabNucleoFamiliare()
	{
		RicercaSoggettoAnagrafeDTO rs = new RicercaSoggettoAnagrafeDTO();
		
		rs.setIdVarSogg(idVarSoggetto);
		rs.setDtRif(null);
		rs.setEnteId(super.getUserBean().getEnteID());
		
		DatiPersonaDTO persona =  super.getAnagrafeService().getDatiPersonaById(rs);
		
		numeroSoggettiNucleo ="0";
		
		if (persona!=null)
		{
			numeroSoggettiNucleo = "1";
			
			rs.setIdSogg(persona.getPersona().getIdExt());
			
			cognomeNomeNucleo = persona.getPersona().getCognome() + " " + persona.getPersona().getNome();
			
			String dtNasc = "";
			if (persona.getPersona().getDataNascita()!=null)
				dtNasc = Utility.dateToString_ddMMyyyy(persona.getPersona().getDataNascita());

			if (persona.getSiglaProvNas()!=null  && persona.getSiglaProvNas()!="")
				comuneDtNascitaNucleo = persona.getDesComNas()+ " (" + persona.getSiglaProvNas()+ ")   "+ dtNasc;
			else
				comuneDtNascitaNucleo = persona.getDesComNas() + "   " + dtNasc;
			
			codFiscaleNucleo =persona.getPersona().getCodfisc();

			if (persona.getPersona().getDataInizioResidenza()!=null)
				dtInizioResidenzaNucleo = Utility.dateToString_ddMMyyyy(persona.getPersona().getDataInizioResidenza());
			else
				dtInizioResidenzaNucleo = "-";
			
			indirizzoResidenzaNucleo = persona.getIndirizzoResidenza();
			
			posizioneAnagraficaNucleo = persona.getPersona().getPosizAna();

			List<ComponenteFamigliaDTO> listaComponenti =super.getAnagrafeService().getListaCompFamiglia(rs); 
			
			if(listaComponenti != null && listaComponenti.size() > 0){
				for (Iterator<ComponenteFamigliaDTO> c = listaComponenti.iterator();c.hasNext();)
				{
					ComponenteFamigliaDTO comp= c.next();
					
					comp.setDtNasStr(Utility.dateToString_ddMMyyyy(comp.getPersona().getDataNascita()));
					String desLuogoNascita ="";
					if(comp.getDesComNas()!=null &&  comp.getDesComNas()!="")
					{
						desLuogoNascita =  comp.getDesComNas();
						if(comp.getDesProvNas()!=null &&  comp.getDesProvNas()!="")
							desLuogoNascita += " ("+ comp.getDesProvNas() + ")";
					}
					comp.setDesLuogoNascita(desLuogoNascita);
				}
			}
			
			listaComponentiNucleo = listaComponenti;
			if (listaComponentiNucleo==null || listaComponentiNucleo.size()==0 )
				logger.debug(" NESSUN COMPONENTE NUCLEO FAMILIARE TROVATO");
		}
		else
			logger.debug("NESSUNA PERSONA TROVATA x NUCLEO FAMILIARE");
	}

	public RicercaSoggettoIciDTO getDatiSoggettoICI() {
		
		RicercaSoggettoIciDTO ricSoggICI = new RicercaSoggettoIciDTO();
		ricSoggICI.setEnteId(super.getUserBean().getEnteID());
		ricSoggICI.setUserId(super.getUserBean().getUsername());
		
		ricSoggICI.setProvenienza(super.getProvenienzaDatiIci());
		ricSoggICI.setTipoSogg(soggettoCartella.getTipoSogg());
		
		if (soggettoCartella.getTipoSogg().equals("F"))
		{// PERSONA FISICA
			ricSoggICI.setCodFis(soggettoCartella.getCodFis());
			ricSoggICI.setCognome(soggettoCartella.getCognome());
			ricSoggICI.setNome(soggettoCartella.getNome());
			ricSoggICI.setDtNas(soggettoCartella.getDtNas());
			ricSoggICI.setCodComNas(soggettoCartella.getCodComNas());
			ricSoggICI.setDesComNas(soggettoCartella.getDesComNas());
		}
		else
		{// PERSONA GIURIDICA
			ricSoggICI.setParIva(soggettoCartella.getParIva());
			ricSoggICI.setDenom(soggettoCartella.getDenom());
		}
		
		return ricSoggICI;
	}
	
	public RicercaSoggettoDTO getDatiSoggettoCatasto() {
		
		RicercaSoggettoDTO ricSogg = new RicercaSoggettoTarsuDTO();
		ricSogg.setEnteId(super.getUserBean().getEnteID());
		ricSogg.setUserId(super.getUserBean().getUsername());
		ricSogg.setProvenienza(super.getProvenienzaDatiTarsu());
		ricSogg.setTipoSogg(this.soggettoCartella.getTipoSogg());
		
		if (soggettoCartella.getTipoSogg().equals("F"))
		{// PERSONA FISICA
			ricSogg.setCodFis(soggettoCartella.getCodFis());
			ricSogg.setCognome(soggettoCartella.getCognome());
			ricSogg.setNome(soggettoCartella.getNome());
			ricSogg.setDtNas(soggettoCartella.getDtNas());
			ricSogg.setCodComNas(soggettoCartella.getCodComNas());
			ricSogg.setDesComNas(soggettoCartella.getDesComNas());
		}
		else
		{// PERSONA GIURIDICA
			ricSogg.setParIva(soggettoCartella.getParIva());
			ricSogg.setDenom(soggettoCartella.getDenom());
		}
		
		return ricSogg;
	}
	
	public RicercaSoggettoTarsuDTO getDatiSoggettoTARSU() {
		
		RicercaSoggettoTarsuDTO ricSoggTarsu = new RicercaSoggettoTarsuDTO();
		ricSoggTarsu.setEnteId(super.getUserBean().getEnteID());
		ricSoggTarsu.setUserId(super.getUserBean().getUsername());
		ricSoggTarsu.setProvenienza(super.getProvenienzaDatiTarsu());
		ricSoggTarsu.setTipoSogg(this.soggettoCartella.getTipoSogg());
		
		if (soggettoCartella.getTipoSogg().equals("F"))
		{// PERSONA FISICA
			ricSoggTarsu.setCodFis(soggettoCartella.getCodFis());
			ricSoggTarsu.setCognome(soggettoCartella.getCognome());
			ricSoggTarsu.setNome(soggettoCartella.getNome());
			ricSoggTarsu.setDtNas(soggettoCartella.getDtNas());
			ricSoggTarsu.setCodComNas(soggettoCartella.getCodComNas());
			ricSoggTarsu.setDesComNas(soggettoCartella.getDesComNas());
		}
		else
		{// PERSONA GIURIDICA
			ricSoggTarsu.setParIva(soggettoCartella.getParIva());
			ricSoggTarsu.setDenom(soggettoCartella.getDenom());
		}
		
		return ricSoggTarsu;
	}
	
	public RicercaSoggettoCosapDTO getDatiSoggettoCOSAP() {
		
		RicercaSoggettoCosapDTO ricSoggCosap = new RicercaSoggettoCosapDTO();
		ricSoggCosap.setEnteId(super.getUserBean().getEnteID());
		ricSoggCosap.setUserId(super.getUserBean().getUsername());
		ricSoggCosap.setTipoSogg(soggettoCartella.getTipoSogg());
		
		if (soggettoCartella.getTipoSogg().equals("F"))
		{// PERSONA FISICA
			ricSoggCosap.setCodFis(soggettoCartella.getCodFis());
			ricSoggCosap.setCognome(soggettoCartella.getCognome());
			ricSoggCosap.setNome(soggettoCartella.getNome());
			ricSoggCosap.setDtNas(soggettoCartella.getDtNas());
			ricSoggCosap.setCodComNas(soggettoCartella.getCodComNas());
			ricSoggCosap.setDesComNas(soggettoCartella.getDesComNas());
		}
		else
		{// PERSONA GIURIDICA
			ricSoggCosap.setParIva(soggettoCartella.getParIva());
			ricSoggCosap.setDenom(soggettoCartella.getDenom());
		}
		
		return ricSoggCosap;
	}
	
	public RicercaCncDTO getDatiSoggettoCNC() {

		RicercaCncDTO ricSoggCNC = new RicercaCncDTO();
		ricSoggCNC.setEnteId(super.getUserBean().getEnteID());
		ricSoggCNC.setUserId(super.getUserBean().getUsername());

		if (this.soggettoCartella.getTipoSogg().equals("F"))
			// 	PERSONA FISICA
			ricSoggCNC.setCodFis(this.soggettoCartella.getCodFis());
		else
			// 	PERSONA GIURIDICA
			ricSoggCNC.setCodFis(this.soggettoCartella.getParIva());
		
		return ricSoggCNC;
	}

	public RicercaSoggettoDTO getDatiSoggettoRedditi() {
		
		RicercaSoggettoDTO ricSoggRedditi = new RicercaSoggettoDTO();
		ricSoggRedditi.setEnteId(super.getUserBean().getEnteID());
		ricSoggRedditi.setUserId(super.getUserBean().getUsername());
		ricSoggRedditi.setTipoSogg(this.soggettoCartella.getTipoSogg());
		
		if (this.soggettoCartella.getTipoSogg().equals("F"))
		{// PERSONA FISICA
			ricSoggRedditi.setCodFis(this.soggettoCartella.getCodFis());
			ricSoggRedditi.setCognome(this.soggettoCartella.getCognome());
			ricSoggRedditi.setNome(this.soggettoCartella.getNome());
			ricSoggRedditi.setDtNas(this.soggettoCartella.getDtNas());
			ricSoggRedditi.setCodComNas(this.soggettoCartella.getCodComNas());
			ricSoggRedditi.setDesComNas(this.soggettoCartella.getDesComNas());
		}
		else
		{// PERSONA GIURIDICA
			ricSoggRedditi.setParIva(this.soggettoCartella.getParIva());
			ricSoggRedditi.setDenom(this.soggettoCartella.getDenom());
		}
		
		return ricSoggRedditi;
	}
	
	public String getAnnoRiferimentoICI() {
		return new Integer(this.recuperaAnnoRiferimentoICI()).toString();
	}

	private int recuperaAnnoRiferimentoICI()
	{
		Date myAnno = super.dataRiferimento;
		if (!(Utility.meseData(myAnno)==12 && Utility.giornoData(myAnno)==31))
			myAnno = Utility.addYear(myAnno, -1);

		return Utility.annoData(myAnno);
	}
	
	private void ClearAllFields()
	{
		this.ClearCampiIci();
		this.ClearCampiTarsu();
		this.ClearCampiCosap();
		this.ClearCampiCnc();
		this.ClearCampiRedditi();
		this.ClearCampiNucleoFamiliare();
		this.ClearCampiCartellaSociale();
		this.ClearCampiSaldoImu();
		this.ClearCampiPatrimoniale();
		
		acquaCarContrib = new AcquaCarContrib();
		multeCarContrib = new MulteCarContrib();
		retteCarContrib = new RetteCarContrib();
		pratichePortaleCarContrib = new PratichePortaleCarContrib();
		ruoliVersCarContrib = new RuoliVersamentiCarContrib();
		
		idVarSoggetto = null;
		
		cbxSezioneIci = true;
		cbxSezioneTarsu=true;
		cbxSezioneCosap=true;
		cbxSezioneRedditi=true;
		cbxSezioneAcqua=true;
		cbxSezioneMulte=true;
		cbxSezioneRette=true;
		cbxSezionePratiche=true;
		cbxSezioneCnc=true;
		cbxSezioneCarSociale=true;
		cbxSezioneRuoliVersamenti=true;
		cbxSezioneImu=true;
		cbxSezionePatrimoniale = true;
		motivoIci="";
		motivoTarsu="";
		motivoRedditi="";
		motivoCnc="";
		motivoCosap="";
		motivoAcqua="";
		motivoRette="";
		motivoMulte="";
		motivoPratiche="";
		motivoCarSociale="";
		motivoRuoliVersamenti="";
		motivoImu="";
		motivoPatrimoniale="";
	}
	
	private void ClearCampiPatrimoniale(){
		intestazioneSituazionePatrimoniale = "";
		listaImmobili = new ArrayList<SitPatrimImmobileDTO>();
		listaTerreni = new ArrayList<SitPatrimTerrenoDTO>();
		listaLocazioni = new ArrayList<LocazioniDTO>();
		indiciSoggettoCartella = new IndiciSoggettoDTO();
	}
	
	private void ClearCampiIci()
	{
		labelDatiSoggettoIci = "";
		indiceSoggettoTabellaIci = "";
		intestazione="";
		denominazione = "";
		indirizzo= "";
		descLabelIndirizzo= "";
		codFiscalePIVA= "";
		comuneResidenza="";
		indirizzoICI= "";
		indirizzoAnagrafe = "";
		
		listaIndirizziCatasto = new ArrayList<String>();
		listaSoggettiCoinvolti = new ArrayList<String>();
		listaSoggettiICI = new ArrayList<SitTIciSogg>();
		listaImmobiliDichiaratiICI = new ArrayList<DatiIciDTO>();
		listaVersamenti = new ArrayList<DatiImportiCncDTO>();
		
	}

	private void ClearCampiTarsu()
	{
		indiceSoggettoTabellaTarsu = "";
		labelDatiSoggettoTarsu = "";
		
		listaSoggettiTARSU = new ArrayList<SitTTarSogg>();
		listaIndirizziCatastoTarsu = new ArrayList<String>();
		soggettoTarsuDaVisualizzare = new SitTTarSogg();
		numeroSoggettiTarsu = "0";

		denominazioneTarsu ="";
		indirizzoTarsu= "";
		descLabelIndirizzoTarsu= "";
		codFiscalePIVATarsu ="";
		comuneResidenzaTarsu = "";
		indirizzoPanelTarsu="";
		listaImmobiliDichiaratiTarsu = new ArrayList<DatiTarsuDTO>();
		listaVersamentiTarsu = new ArrayList<DatiImportiCncDTO>();
		//listaImmobiliTarsu = new ArrayList<SitPatrimImmobileDTO>();
		//listaLocazioniTarsu = new ArrayList<LocazioniDTO>();
		immobileTarsu = new DatiTarsuDTO();
		soggettoTarsuSelezionato = new SitTTarSogg();
		indirizzoTARSU = "";
		listaSoggettiCoinvoltiTarsu = new ArrayList<String>();
	}
	
	private void ClearCampiCosap()
	{
		indiceSoggettoTabellaCosap = "";
		labelDatiSoggettoCosap = "";
		
		listaConcessioniCosap = new ArrayList<SitTCosapTassa>();
		
		denominazioneCosap = "";
		indirizzoCosap = "";
		descLabelIndirizzoCosap = "";
		codFiscalePIVACosap = "";
		comuneResidenzaCosap = "";
		
		listaSoggettiCOSAP = new ArrayList<SitTCosapContrib>();
		numeroSoggettiCosap = "";;
		soggettoCosapSelezionato = new SitTCosapContrib();
		
	}
	
	private void ClearCampiCnc()
	{
		labelDatiSoggettoCnc = "";
		indiceSoggettoTabellaCnc = "";
		listaRuoliCNC = new ArrayList<DatiImportiCncDTO>();
		
		denominazioneCNC = "";
		indirizzoCNC  = "";
		descLabelIndirizzoCNC  = "";
		codFiscalePIVACNC  = "";
		comuneResidenzaCNC  = "";
		
		listaSoggettiCNC = new ArrayList<VAnagrafica>();
		numeroSoggettiCNC = "";
		soggettoCNCSelezionato = new VAnagrafica();
	}
	
	private void ClearCampiRedditi()
	{
		labelDatiSoggettoRedditi = "";
		indiceSoggettoTabellaRedditi = "";
		
		denominazioneRedditi = "";
		indirizzoRedditi="";
		descLabelIndirizzoRedditi="";
		labelDescTipoSogg="";
		codFiscalePIVARedditi="";
		domicilioFiscaleRedditi="";
		listaRedditi = new ArrayList<RedditiDicDTO>();
	}
	
	private void ClearCampiNucleoFamiliare()
	{
		numeroSoggettiNucleo="0";
		cognomeNomeNucleo = "";
		dtInizioResidenzaNucleo= "";
		comuneDtNascitaNucleo= "";
		codFiscaleNucleo= "";
		indirizzoResidenzaNucleo= "";
		posizioneAnagraficaNucleo= "";
		listaComponentiNucleo = new ArrayList<ComponenteFamigliaDTO>();
	}
	
	private void ClearCampiCartellaSociale()
	{
		numeroInterventiCarSociale="0";
		listaInterventiCarSociale = new ArrayList<InterventoDTO>();
	}
	
	
	private void ClearCampiSaldoImu()
	{
		
		datiElabImu = null;
		consulenzaImu = null;
	}


	// TORNA ALLA PAGINA DI RICERCA
	public String goCartellaRicerca()
	{
		String res="goRicerca";
		return res ;
	}

	// ************************ PDF

	public void CreateAndSavePdfPerNucleo()
	{
		try
		{
			ByteArrayOutputStream baos = null; 
			baos = this.CreatePDF();
			
			if (baos!=null)
				this.SavePdfFile(baos);

			viewPDF= true;
			showButtonProduciPdfPerNucleo=false;
		}
		catch(Exception ex)
		{
			logger.error(" ERRORE funzione CreateAndSavePdf " + ex.getMessage());
		}		
	}
	
	
	public void CreateAndSavePdf()
	{
		try
		{
			ByteArrayOutputStream baos = null; 
			baos = this.CreatePDF();
			
			if (baos!=null)
				this.SavePdfFile(baos);

			this.viewPDF= true;
			this.showButtonProduciPdf=false;
		}
		catch(Exception ex)
		{
			logger.error(" ERRORE funzione CreateAndSavePdf " + ex.getMessage(),ex);
		}
	}
	
	private ByteArrayOutputStream CreatePDF() throws DocumentException, IOException {
    	
		String soggetto = "";
		
		if (soggettoCartella.getTipoSogg().equals("F"))
			soggetto =soggettoCartella.getCognome()+" "+soggettoCartella.getNome();
		else
			soggetto = soggettoCartella.getDenom();

    	Document document = new Document(PageSize.A3.rotate(), 10, 10, 10, 10);
	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    	PdfWriter writer = PdfWriter.getInstance(document, baos);
    	document.open();

		document.addTitle("Posizione Contribuente di " + soggetto);
		document.addSubject("Posizione Contribuente di " + soggetto);
		document.addAuthor(super.getUserBean().getEnteID());
		document.addCreator(super.getUserBean().getUsername());
		
		Paragraph titolo = new Paragraph();
		this.addEmptyLine(titolo, 1);
		titolo.add(new Paragraph("NUMERO RICHIESTA " + idRichiestaCartella.toString() + " DEL " + strDataRichiesta, new Font(Font.FontFamily.TIMES_ROMAN, 30,Font.BOLD)));		
		this.addEmptyLine(titolo, 1);
		titolo.add(this.insertTitoloScheda("Data di stampa : " + Utility.dateToString_ddMMyyyy(new Date())));
		this.addEmptyLine(titolo, 1);
		titolo.add(new Paragraph("POSIZIONE CONTRIBUENTE", new Font(Font.FontFamily.TIMES_ROMAN, 30,Font.BOLD)));
		this.addEmptyLine(titolo, 1);
		titolo.add(new Paragraph(getValueProperties("NOTA_PDF"), new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.ITALIC)));
		this.addEmptyLine(titolo, 3);
		
		document.add(titolo);
		
		if(catastoFonte.isAbilitata() || locazioniFonte.isAbilitata() || bolliVeicoliFonte.isAbilitata() )
			this.addSezionePatrimoniale(document);
		
		if(tribTarsuFonte.isAbilitata())
			this.addSezioneIci(document);
			
		if(tribTarsuFonte.isAbilitata())
			this.addSezioneTarsu(document);
		
		if(ruoliVersCarContrib.isFontiAbilitate())
			ruoliVersCarContrib.addSezionePdf(document,cbxSezioneRuoliVersamenti,motivoRuoliVersamenti);
		
		if(cosapFonte.isAbilitata())
			this.addSezioneCosap(document);
		
		if(cncFonte.isAbilitata())
			this.addSezioneCnc(document);
		
		if(redditiFonte.isAbilitata())
			this.addSezioneRedditi(document);
		
		if(acquaCarContrib != null && acquaFonte.isAbilitata())
			acquaCarContrib.addSezionePdf(document, cbxSezioneAcqua, motivoAcqua, acquaFonte);
		
		if(multeCarContrib != null && multeFonte.isAbilitata())
			multeCarContrib.addSezionePdf(document, cbxSezioneMulte, motivoMulte, multeFonte);
		
		if(retteCarContrib != null && retteFonte.isAbilitata())
			retteCarContrib.addSezionePdf(document, cbxSezioneRette, motivoRette, retteFonte);
		
		if(pratichePortaleCarContrib != null && praticheFonte.isAbilitata())
			pratichePortaleCarContrib.addSezionePdf(document, cbxSezionePratiche, motivoPratiche, praticheFonte);
		
		if(carsocialeFonte.isAbilitata())
			this.addSezioneCarSociale(document);
		
		if(imuFonte.isAbilitata())
			this.addSezioneImu(document);
		
		this.addSezioneRisposta(document);
    	document.close();

    	return baos;
    }
	
	public void ShowFile() throws IOException
	{ 
		String pathPdf = super.getPathFilesPdf() + File.separator + String.format("%012d", idRichiestaCartella)+ ".pdf";
	
		Utility.doDownload(pathPdf);
	}
	
	private void SavePdfFile (ByteArrayOutputStream baos){

		try
		{
			String nomeFile = String.format("%012d", idRichiestaCartella) ;
			
			String pathDir = super.getPathFilesPdf();
			
            File tempDir = new File(pathDir);
            if(!tempDir.exists()) {
                tempDir.mkdirs();
                logger.debug("PDF creata cartella per pdf " + tempDir.getAbsolutePath());
            }

            String pathFile = tempDir.getAbsolutePath() +"\\" + nomeFile + ".pdf";
            
            Utility.DeleteFile(pathFile);
            
	      	OutputStream f2 = new FileOutputStream(pathFile);
	      	baos.writeTo(f2);
	      	f2.close();
	      	logger.debug("PDF Creato e salvato file pdf =" + pathFile);
	      	
			this.updateNamePdfFile(nomeFile);

	      	// SOLO SE ACCESSO DA FUNZIONE DI PREPARAZIONE CARTELLA--> SI INSERISCE LA RISPOSTA
			if (preparaCartella){
				logger.debug("Inserisce anche la risposta");
				this.insertRisposta();
			}
			else
				logger.debug("NON Inserisce la risposta");
	    		
		}
		catch (IOException e)
		{
			logger.debug("ERRORE SavePdfFile = " + e.getMessage());
		}
	}
	
	private void updateNamePdfFile(String nomeFile)
	{
      	RichiesteDTO richDTO = new RichiesteDTO();
      	Richieste rich = new Richieste();
      	rich.setIdRic(idRichiestaCartella);
      	rich.setNomePdf(nomeFile);
      	
      	richDTO.setRich(rich);
      	richDTO.setEnteId(super.getUserBean().getEnteID());
      	richDTO.setUserId(super.getUserBean().getUsername());
      	
      	super.getCarContribService().updateFilePdfRichiesta(richDTO);
	}
	
	private void insertRisposta()
	{
      	// INSERIMENTO SUL DB NUOVA RISPOSTA
      	RisposteDTO rispDTO = new RisposteDTO();
      	Risposte risp = new Risposte();
      	
      	risp.setIdRic(this.idRichiestaCartella);
      	risp.setUserName(super.getUserBean().getUsername());
      	risp.setDtRis(new Date());
      	
      	risp.setCodTipMezRis(tipoAccessoCartella);
      	risp.setDesRis("");
      	risp.setDesNotUser("");
      	
      	rispDTO.setRisp(risp);
      	rispDTO.setEnteId(super.getUserBean().getEnteID());
      	rispDTO.setUserId(super.getUserBean().getUsername());
      	
      	super.getCarContribService().insertRisposta(rispDTO);
	}
	
	private void addSezioneRisposta(Document document) throws DocumentException {
		
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD);
		Font italicFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.ITALIC);

		Paragraph parRisposta = new Paragraph();
		this.addEmptyLine(parRisposta, 1);
		
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setMinimumHeight(20);
		table.getDefaultCell().setPaddingLeft(20);

		CeTBaseObject cetObj = new CeTBaseObject();
		cetObj.setEnteId(super.getUserBean().getEnteID());
		cetObj.setUserId(super.getUserBean().getUsername());
		
		PdfPCell myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("COMUNE DI " + super.getCommonService().getEnte(cetObj).getDescrizione().toUpperCase(),boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(myCell);
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("Ricevuta dell'avvenuta consegna della cartella fiscale", italicFont));
		myCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(myCell);		

		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("SI DICHIARA CHE",boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		
		parRisposta.add(table);
		this.addEmptyLine(parRisposta, 2);
		
		table = new PdfPTable(3);
		table.setWidthPercentage(100);
		table.setWidths(new float[] {33,33,34});
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setPaddingLeft(20);
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("COGNOME",boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);		
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("NOME",boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("CODICE FISCALE",boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("",boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);		
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("",boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("",boldFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		parRisposta.add(table);
		
		this.addEmptyLine(parRisposta, 10);
		parRisposta.add(new Paragraph("HA RICEVUTO IN DATA " + Utility.dateToString_ddMMyyyy(new Date()) + " LA CARTELLA FISCALE RELATIVA A :", boldFont));
		this.addEmptyLine(parRisposta, 2);
		
		if (soggettoCartella.getTipoSogg().equals("F"))
		{
			table = new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setWidths(new float[] {33,33,34});
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.getDefaultCell().setPaddingLeft(20);
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase("COGNOME",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);		
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase("NOME",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase("CODICE FISCALE",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase(soggettoCartella.getCognome(),italicFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);		
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase(soggettoCartella.getNome(),italicFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase(soggettoCartella.getCodFis(),italicFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);
		}
		else
		{
			table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new float[] {50,50});
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.getDefaultCell().setPaddingLeft(20);
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase("DENOMINAZIONE",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);		
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase("PARTITA IVA",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase(soggettoCartella.getDenom(),italicFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);		
			
			myCell = table.getDefaultCell();
			myCell.setPhrase(new Phrase(soggettoCartella.getParIva(),italicFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(myCell);
		}
		
		parRisposta.add(table);
		
		this.addEmptyLine(parRisposta, 3);

		table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[] {50,50});
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setPaddingLeft(50);
		table.getDefaultCell().setPaddingRight(50);
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("L'incaricato",italicFont));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		
		myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase("Il ricevente",italicFont));
		myCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(myCell);
		
		parRisposta.add(table);
		
		document.add(parRisposta);

	}
	
	private void addSezioneCnc(Document document) throws DocumentException {
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);

		Paragraph paragrafoCnc = new Paragraph();
		
		paragrafoCnc.add(this.insertTitoloScheda("SCHEDA CNC"));
		
		if (!cbxSezioneCnc) {
			paragrafoCnc.add(new Phrase(motivoCnc,normalSmallFont));
			document.add(paragrafoCnc);
			document.newPage();
			return;
		}
		
		if(cncFonte.isAbilitataCC()){
			paragrafoCnc.add(cncFonte.getStrDataAgg());
			this.addEmptyLine(paragrafoCnc, 1);
			paragrafoCnc.add(new Phrase(cncFonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoCnc, 2);
			
			// Codice per creazione pdf del CNC
			// Da provare quando ci saranno i dati
			/*
			RicercaCncDTO soggettoCNC = this.getDatiSoggettoCNC();
			VAnagrafica soggCnc = new VAnagrafica();
			if (soggettoCNCDaVisualizzare==null || soggettoCNCDaVisualizzare.getId()==null)
			{
				List<VAnagrafica> listaCNC = super.getCncCarContribService().getAnagraficaDebitore(soggettoCNC);
				if (listaCNC!=null && listaCNC.size()>0)
					soggCnc = listaCNC.get(0);
			}
			else
				soggCnc = soggettoCNCDaVisualizzare;
			
			if(soggettoCNC!=null)
			{
				paragrafoCnc.add(new Phrase("Dati anagrafici dichiarati",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				//String residenza = "";
				// RESIDENZA ?	
				// Sezione Dati Anagrafici dichiarati
				//paragrafoCnc.add(this.insertSezioneDatiAnagrafici(soggCnc.getCognome(),
				//		soggCnc.getNome(),soggCnc.getDtNascita(),
				//		"",residenza,soggCnc.getCodFiscale(),
				//		soggCnc.getDenominazione(),soggCnc.getPartitaIva()));
				
				this.addEmptyLine(paragrafoCnc, 2);
				List<DatiImportiCncDTO> listaCncPdf = new ArrayList<DatiImportiCncDTO>();
				if(listaRuoliCNC==null || listaRuoliCNC.size()==0)
					listaCncPdf = super.getCncCarContribService().getDatiCNC(soggettoCNC, super.annoData(super.dataRiferimento));
				else
					listaCncPdf = listaRuoliCNC;
				
				if(listaCncPdf!=null && listaCncPdf.size()>0)
				{
					paragrafoCnc.add(new Paragraph("Dati autorizzazioni", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					
					PdfPTable tableDatiCnc = new PdfPTable(4);
					tableDatiCnc.setWidths(new float[] {10,40,25,25});
					tableDatiCnc.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiCnc.setWidthPercentage(100);
					tableDatiCnc.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					tableDatiCnc.getDefaultCell().setMinimumHeight(20);
					
					PdfPCell c1 = new PdfPCell(new Phrase("ANNO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableDatiCnc.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("TRIBUTO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableDatiCnc.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("ISCRITTO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableDatiCnc.addCell(c1);				
	
					c1 = new PdfPCell(new Phrase("RISCOSSO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableDatiCnc.addCell(c1);
					
					for (Iterator<DatiImportiCncDTO> d = listaCncPdf.iterator();d.hasNext();)
					{
						DatiImportiCncDTO cnc= d.next();
						
						PdfPCell cellTassa = new PdfPCell(new Phrase(this.formattaStringa(cnc.getAnnoRif()),normalSmallFont));
						cellTassa.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableDatiCnc.addCell(cellTassa);
						
						cellTassa = new PdfPCell(new Phrase(this.formattaStringa(cnc.getCodTipoTributo() + " - " + cnc.getDesTipoTributo()),normalSmallFont));
						cellTassa.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableDatiCnc.addCell(cellTassa);
						
						cellTassa = new PdfPCell(new Phrase(this.formattaStringa(cnc.getImpTotRuolo().toString()),normalSmallFont));
						cellTassa.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableDatiCnc.addCell(cellTassa);
						
						cellTassa = new PdfPCell(new Phrase(this.formattaStringa(cnc.getImpTotRiscosso().toString()),normalSmallFont));
						cellTassa.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableDatiCnc.addCell(cellTassa);					
					}
					
					paragrafoCnc.add(tableDatiCnc);
					
					this.addEmptyLine(paragrafoCnc, 2);
				}
				else
				{
					paragrafoCnc.add(new Paragraph("Lista CNC non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoCnc, 2);				
				}
			}
			*/
			
			paragrafoCnc.add(new Paragraph("Nessun dato CNC da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
		}else
			this.addMotivoFonteDisabilitataCC(paragrafoCnc, cncFonte.getDescrizione());
		
		document.add(paragrafoCnc);
		
		document.newPage();
	}
	
	private void addSezioneCosap(Document document) throws DocumentException {
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		Paragraph paragrafoCosap = new Paragraph();
		
		paragrafoCosap.add(this.insertTitoloScheda("SCHEDA COSAP"));
		
		if (!cbxSezioneCosap) {
			paragrafoCosap.add(new Phrase(motivoCosap,normalSmallFont));
			document.add(paragrafoCosap);
			document.newPage();
			return;
		}
		if(cosapFonte.isAbilitataCC()){
			paragrafoCosap.add(cosapFonte.getStrDataAgg());
			this.addEmptyLine(paragrafoCosap, 1);
			paragrafoCosap.add(new Phrase(cosapFonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoCosap, 2);
			
			SitTCosapContrib soggettoCosap = new SitTCosapContrib();
			if (soggettoCosapDaVisualizzare==null || soggettoCosapDaVisualizzare.getId()==null)
			{
				List<SitTCosapContrib> listaSoggettiCosapPdf = super.getCosapCarContribService().searchSoggettoCosap(this.getDatiSoggettoCOSAP());
				if (listaSoggettiCosapPdf!=null && listaSoggettiCosapPdf.size()>0)
					soggettoCosap = listaSoggettiCosapPdf.get(0);
			}
			else
				soggettoCosap = soggettoCosapDaVisualizzare;
		
		
			if (soggettoCosap!=null && soggettoCosap.getId()!=null)
			{
				paragrafoCosap.add(new Phrase("Dati anagrafici dichiarati",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				String residenza = "";
				
				if (soggettoCartella.getTipoSogg().equals("F"))
				{
					if (soggettoCosap.getDescComuneResidenza()!=null && soggettoCosap.getDescComuneResidenza().trim()!="")
						residenza = soggettoCosap.getDescComuneResidenza().trim();
					if (soggettoCosap.getIndirizzo()!=null && soggettoCosap.getIndirizzo().trim()!="")
						residenza = soggettoCosap.getIndirizzo().trim() + "   " + residenza;
					if (soggettoCosap.getCapResidenza()!=null && soggettoCosap.getCapResidenza().trim()!="")
						residenza += " - " + soggettoCosap.getCapResidenza().trim();
				}			
				// Sezione Dati Anagrafici dichiarati
				paragrafoCosap.add(this.insertSezioneDatiAnagrafici(soggettoCosap.getCogDenom(),
						soggettoCosap.getNome(),soggettoCosap.getDtNascita(),
						soggettoCosap.getDescComuneNascita(),residenza,soggettoCosap.getCodiceFiscale(),
						soggettoCosap.getCogDenom(),soggettoCosap.getPartitaIva()));
				this.addEmptyLine(paragrafoCosap, 2);
				
				List<SitTCosapTassa> listaConcCosapPdf = new ArrayList<SitTCosapTassa>();
				if (listaConcessioniCosap==null)
				{
					RicercaSoggettoCosapDTO soggCosap = this.getDatiSoggettoCOSAP();
					soggCosap.setIdSoggCosap(soggettoCosap.getId());
					listaConcCosapPdf = super.getCosapCarContribService().getDatiCosap(soggCosap, Utility.addYear(super.dataRiferimento,-1));
				}
				else
					listaConcCosapPdf = listaConcessioniCosap;
				
				if (listaConcCosapPdf!=null && listaConcCosapPdf.size()>0)
				{
					paragrafoCosap.add(new Paragraph("Dati autorizzazioni", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					
					PdfPTable tableConces = new PdfPTable(9);
					tableConces.setWidths(new float[] {8,8,12,14,20,6,12,4,6});
					tableConces.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableConces.setWidthPercentage(100);
					tableConces.getDefaultCell().setMinimumHeight(20);
					
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "DATA RICH.");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "DATA INIZIO");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "OCCUPAZIONE");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "ANNO-NUM.DOC.");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "INDIRIZZO");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "TARIFFA");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "VAL. TARIFFA");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "CONS.");
					this.addCellaIntestazione(tableConces, boldSmallFont, Element.ALIGN_CENTER, "CANONE");
					
					for (Iterator<SitTCosapTassa> d = listaConcCosapPdf.iterator();d.hasNext();)
					{
						SitTCosapTassa tassa= d.next();
						
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getDtRichiestaStr()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getDtIniValiditaStr()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getTipoOccupazione()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getAnnoDocumento()+"-"+tassa.getNumeroDocumento()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getIndirizzo()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getTariffaPerUnitaStr()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getDtIniValiditaTariffaStr() + "-"+  tassa.getDtFinValiditaTariffaStr()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getUnitaMisuraQuantita() + " " + tassa.getQuantita()));
						this.addCella(tableConces, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(tassa.getImportoCanoneStr()));
						
					}
	
					paragrafoCosap.add(tableConces);
					
					this.addEmptyLine(paragrafoCosap, 2);
				}
				else
				{
					paragrafoCosap.add(new Paragraph("Nessun dato COSAP da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoCosap, 2);
				}
			}
			else
			{
				paragrafoCosap.add(new Paragraph("Nessun dato COSAP da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoCosap, 2);
			}	
		}else
			this.addMotivoFonteDisabilitataCC(paragrafoCosap, this.cosapFonte.getDescrizione());
		
		document.add(paragrafoCosap);
		
		document.newPage();
	}
	
	private void addMotivoFonteDisabilitataCC(Paragraph paragrafo, String fonte){
		String motivo = "Fonte "+fonte+" non configurata per la visualizzazione nel PDF";
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.ITALIC);
		font.setColor(BaseColor.RED);
		this.addEmptyLine(paragrafo, 1);
		paragrafo.add(new Paragraph(motivo,font));
		this.addEmptyLine(paragrafo, 2);
	}
	
	private void addSezioneRedditi(Document document) throws DocumentException {
		
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.NORMAL);

		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		Paragraph paragrafoRedditi = new Paragraph();
		
		paragrafoRedditi.add(this.insertTitoloScheda("SCHEDA REDDITI"));
				
		if (!cbxSezioneRedditi) {
			paragrafoRedditi.add(new Phrase(motivoRedditi,normalSmallFont));
			document.add(paragrafoRedditi);
			document.newPage();
			return;
		}
		
		if(redditiFonte.isAbilitataCC()){
			paragrafoRedditi.add(redditiFonte.getStrDataAgg());
			this.addEmptyLine(paragrafoRedditi, 1);
			paragrafoRedditi.add(new Phrase(redditiFonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoRedditi, 2);
			
			RicercaSoggettoDTO soggettoRedditi = this.getDatiSoggettoRedditi();
	
			RedDatiAnagrafici soggReddito = new RedDatiAnagrafici();
			
			if (soggettoRedditiDaVisualizzare==null || soggettoRedditiDaVisualizzare.getIdeTelematico()==null)
			{
				List<RedDatiAnagrafici> listaSoggettiRedditiPdf = super.getRedditiCarContribService().searchSoggettiCorrelatiRedditi(this.getObjRicerca(Utility.TipoRicerca.OTHER));
				if (listaSoggettiRedditiPdf!=null && listaSoggettiRedditiPdf.size()>0)
					soggReddito = listaSoggettiRedditiPdf.get(0);
			}
			else // PRENDO L'ID DEL SOGG. REDDITI SELEZIONATO
				soggReddito = soggettoRedditiDaVisualizzare;
	
			if (soggReddito!=null && soggReddito.getIdeTelematico()!=null)
			{
				KeySoggDTO key = new KeySoggDTO();
				key.setEnteId(super.getUserBean().getEnteID());
				key.setUserId(super.getUserBean().getUsername());
				if (soggettoRedditi.getTipoSogg().equals("F"))
					key.setCodFis(soggettoRedditi.getCodFis());
				else
					key.setCodFis(soggettoRedditi.getParIva());
				key.setIdeTelematico(soggReddito.getIdeTelematico());
	
				// ******* START RECUPERO ACQUISIZIONE REDDITI
				List<RedditiDicDTO> listaRedditi = new ArrayList<RedditiDicDTO>();
				if (this.getListaRedditi()==null)
					listaRedditi = super.getRedditiCarContribService().getRedditiByKey(key);
				else
					listaRedditi = this.getListaRedditi();
						
				if (listaRedditi!=null && listaRedditi.size()>0)
				{
					paragrafoRedditi.add(new Phrase("Dati del contribuente",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					
					RedDatiAnagrafici soggRedditi = super.getRedditiCarContribService().getSoggettoByKey(key);
					
					String cognome= "-";
					String nome= "-";
					String dataNascita = "-";
					String comuneNascita="-";
					
					if (soggRedditi!=null)
					{
						cognome = soggRedditi.getCognome();
						nome = soggRedditi.getNome();
						comuneNascita = soggRedditi.getDesComuneNascita();
						dataNascita= Utility.formatStringDate(soggRedditi.getDataNascita());
					}
					
					String indirizzo = "";
					RedDomicilioFiscale domicilioRedditi = super.getRedditiCarContribService().getDomicilioByKey(key);
					if (domicilioRedditi!=null)
					{
						indirizzo = domicilioRedditi.getIndirizzoAttuale();
						if(domicilioRedditi.getCap()!=null&&domicilioRedditi.getCap()!="")
							indirizzo += " - " + domicilioRedditi.getCap();
					}
					
					// 	Sezione Dati Anagrafici dichiarati
					PdfPTable tableDatiSoggetto = new PdfPTable(4);
					tableDatiSoggetto.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.setWidthPercentage(100);
					tableDatiSoggetto.setWidths(new float[] {20,35,10,35});
					tableDatiSoggetto.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					tableDatiSoggetto.getDefaultCell().setMinimumHeight(30);
	
					PdfPCell myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase("COGNOME",boldFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase(cognome,normalFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase("NOME",boldFont));					
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
	
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase(nome,normalFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase("NATO IL",boldFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
	
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase(dataNascita,normalFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase("A",boldFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase(comuneNascita,normalFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase("RESIDENZA",boldFont));					
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase(indirizzo,normalFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					myCell.setColspan(3);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase("CODICE FISCALE",boldFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					myCell.setColspan(1);
					tableDatiSoggetto.addCell(myCell);
					
					myCell = tableDatiSoggetto.getDefaultCell();
					myCell.setPhrase(new Phrase(soggettoRedditi.getCodFis(),normalFont));
					myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					myCell.setColspan(3);
					tableDatiSoggetto.addCell(myCell);
					
					paragrafoRedditi.add(tableDatiSoggetto);
					
					this.addEmptyLine(paragrafoRedditi, 2);
					
					paragrafoRedditi.add(new Paragraph("Redditi dichiarati", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					
					PdfPTable tableRedditi = new PdfPTable(3);
					tableRedditi.setWidths(new float[] {50,30,20});
					tableRedditi.setWidthPercentage(100);
					tableRedditi.getDefaultCell().setMinimumHeight(30);
					
					this.addCellaIntestazione(tableRedditi, boldSmallFont, Element.ALIGN_CENTER, "DESCRIZIONE QUADRO");
					this.addCellaIntestazione(tableRedditi, boldSmallFont, Element.ALIGN_CENTER, "VALORE CONTABILE");
					this.addCellaIntestazione(tableRedditi, boldSmallFont, Element.ALIGN_CENTER, "ANNO IMPOSTA");
					
					for (Iterator<RedditiDicDTO> d = listaRedditi.iterator();d.hasNext();)
					{
						RedditiDicDTO redditi= d.next();
						
						this.addCella(tableRedditi, normalSmallFont, Element.ALIGN_LEFT, redditi.getDesQuadro().trim());
						this.addCella(tableRedditi, normalSmallFont, Element.ALIGN_RIGHT, redditi.getValoreContabileF().trim());
						this.addCella(tableRedditi, normalSmallFont, Element.ALIGN_CENTER, redditi.getId().getAnnoImposta().trim());
						
					}
					
					paragrafoRedditi.add(tableRedditi);
					
					this.addEmptyLine(paragrafoRedditi, 2);
				}
				else
				{
					paragrafoRedditi.add(new Paragraph("Nessun reddito dichiarato da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoRedditi, 2);
				}
			}
			else
			{
				this.addEmptyLine(paragrafoRedditi, 1);			
				paragrafoRedditi.add(new Paragraph("Nessun reddito dichiarato da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			}
			
		}else
			this.addMotivoFonteDisabilitataCC(paragrafoRedditi, this.redditiFonte.getDescrizione());
		
		document.add(paragrafoRedditi);
		
		document.newPage();
	}
	
	private void addCellaIntestazione(PdfPTable table, Font font, int align, String label){
		
		PdfPCell c1 = table.getDefaultCell();
		c1.setPhrase(new Phrase(label,font));
		c1.setHorizontalAlignment(align);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		c1.setColspan(1);
		table.addCell(c1);
		
	}
	
	private void addCella(PdfPTable table, Font font,int align,String desc){
		
		PdfPCell cellInt = table.getDefaultCell();
		cellInt.setPhrase(new Phrase(desc,font));
		cellInt.setHorizontalAlignment(align);
		cellInt.setBackgroundColor(BaseColor.WHITE);
		cellInt.setColspan(1);
		table.addCell(cellInt);
	}
	
	
	private void addSezioneImu(Document document)throws DocumentException{
		
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		Paragraph paragrafoImu = new Paragraph();
		paragrafoImu.add(this.insertTitoloScheda("SCHEDA CONSULENZE SALDO IMU"));
		
		if (!cbxSezioneImu) {
			paragrafoImu.add(new Phrase(motivoImu,normalSmallFont));
			document.add(paragrafoImu);
			document.newPage();
			return;
		}
		
		if(imuFonte.isAbilitataCC()){
			this.addEmptyLine(paragrafoImu, 2);
			if(consulenzaImu!=null){
				this.addSezioneImuDatiElab(paragrafoImu);
				this.addSezioneImuStorico(paragrafoImu);
			}else
			{
				paragrafoImu.add(new Paragraph("Nessuna consulenza imu da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoImu, 2);
			}
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafoImu, this.imuFonte.getDescrizione());
		
		document.add(paragrafoImu);
		document.newPage();
	}
	
	private void addSezioneImuStorico(Paragraph paragrafoImu) throws DocumentException {
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.NORMAL);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		if(consulenzaImu!=null){
			String intestazione = "DETTAGLIO CONSULENZA fornita il "+SDF.format(this.consulenzaImu.getDtConsulenza());
			paragrafoImu.add(new Phrase(intestazione,new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
			
			DatiAnagrafeDTO dichiarante  = this.consulenzaImu.getDichiarante();
			List<XmlImmobileDTO> lstImm = this.consulenzaImu.getLstImmobili();
			List<XmlF24DTO> lstf24 = this.consulenzaImu.getLstF24();
		
			//Inserire Dati Dichiarante
			if(dichiarante!=null){
				this.addEmptyLine(paragrafoImu, 2);
				paragrafoImu.add(new Phrase("Dichiarante",new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
			
				PdfPTable tableDatiDich = new PdfPTable(4);
				tableDatiDich.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiDich.setWidths(new float[] {20,35,10,35});
				tableDatiDich.setWidthPercentage(100);
				tableDatiDich.getDefaultCell().setMinimumHeight(30);
				
				int align = Element.ALIGN_LEFT;
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "COGNOME");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getCog());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "NOME");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getNom());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "CODICE FISCALE");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getCodFisc());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "SESSO");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getSesso());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "NATO A");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getComNas()+"("+dichiarante.getProvNas()+")");
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "IBAN");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getIban());
			
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "RESIDENZA");
				
				String indirizzo = "";
				if(dichiarante.getIndRes()!=null && dichiarante.getIndRes().length()>0)
					indirizzo = dichiarante.getIndRes();
				indirizzo += dichiarante.getComRes()!=null ? ", "+dichiarante.getComRes() : "";
				indirizzo += dichiarante.getProvRes()!=null ? "("+dichiarante.getProvRes()+")" : "";
					
				PdfPCell cellInt = tableDatiDich.getDefaultCell();
				cellInt.setPhrase(new Phrase(indirizzo,normalFont));
				cellInt.setHorizontalAlignment(align);
				cellInt.setBackgroundColor(BaseColor.WHITE);
				cellInt.setColspan(3);
				tableDatiDich.addCell(cellInt);
				
				paragrafoImu.add(tableDatiDich);
				this.addEmptyLine(paragrafoImu, 2);
			}

			//Inserisco il paragrafo Catastale
			
			if (lstImm!=null && lstImm.size()>0)
			{	
				paragrafoImu.add(new Paragraph("Dati catastali", new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
				this.addEmptyLine(paragrafoImu, 1);
				
				//Inserisco lista immobili
				if((lstImm!=null && lstImm.size()>0)){
				
					PdfPTable tableCatastoF = new PdfPTable(9);
					tableCatastoF.setWidths(new float[] {40,3,11,11,3,3,10,4,5});
					tableCatastoF.setWidthPercentage(100);
					tableCatastoF.getDefaultCell().setMinimumHeight(20);
					
					int align = Element.ALIGN_CENTER;
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Dati Immobile principale e pertinenze");
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Aliq.");
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Acconto");
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Terr.Agricolo");
					
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Tasso");
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Cont.");
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Figli");
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"IACP");
					this.addCellaIntestazione(tableCatastoF,boldSmallFont,align,"Note");
					
					for (Iterator<XmlImmobileDTO> it = lstImm.iterator();it.hasNext();)
					{
						XmlImmobileDTO i= it.next();
						
							//Aggiungo lista immobili
							PdfPTable tableVal = new PdfPTable(10);
							tableVal.setWidths(new float[] {6,12,12,12,8,9,10,11,10,10});
							tableVal.setWidthPercentage(20);
							
							tableVal.getDefaultCell().setMinimumHeight(20);
							
							align = Element.ALIGN_CENTER;
							PdfPCell cellInt = tableVal.getDefaultCell();
							cellInt.setPhrase(new Phrase(i.getCatDescrizione(),normalSmallFont));
							cellInt.setHorizontalAlignment(Element.ALIGN_LEFT);
							cellInt.setBackgroundColor(BaseColor.WHITE);
							cellInt.setBorder(Rectangle.NO_BORDER);
							cellInt.setColspan(10);
							tableVal.addCell(cellInt);
							
							cellInt.setBorder(Rectangle.BOX);
							
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Tipo");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Rendita");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Rival.");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Valore");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Mesi");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "% Poss");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Storico");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Imposta");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Detr.");
							this.addCellaIntestazione(tableVal, boldSmallFont,align, "Dovuto");
							
							ValImmobileDTO[]  vals = i.getListaValori();
							for(ValImmobileDTO v : vals){
								if(!v.isVuoto()){
									 addCella(tableVal,normalSmallFont,Element.ALIGN_LEFT, v.getCod());
									 addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getRendita()!=null ?  " " +DF.format(v.getRendita()) : "");
									 addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getRenditaRivalutata()!=null ? " " +DF.format(v.getRenditaRivalutata()) : "");
									 addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getValore()!=null ? " " +DF.format(v.getValore()):"");
									 addCella(tableVal,normalSmallFont,Element.ALIGN_CENTER, v.getMesiPoss());	
									 addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getQuotaPoss()!=null ? v.getQuotaPoss().toString() : "");
									 addCella(tableVal,normalSmallFont,Element.ALIGN_CENTER, v.getStorico().equalsIgnoreCase("S")? "SI" : "NO");
									 addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getDovuto()!=null ? " " +DF.format(v.getDovuto()):"");
									 addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getDetrazione()!=null ? " " +DF.format(v.getDetrazione()):"");
									 addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getDovutoMenoDetrazione()!=null ? " " +DF.format(v.getDovutoMenoDetrazione()):"");
								}
							}
							
							cellInt = tableVal.getDefaultCell();
							cellInt.setPhrase(new Phrase("Somma Rendite Rival.:",boldSmallFont));
							cellInt.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellInt.setBackgroundColor(BaseColor.WHITE);
							cellInt.setColspan(3);
							cellInt.setBorder(Rectangle.NO_BORDER);
							tableVal.addCell(cellInt);
							
							cellInt = tableVal.getDefaultCell();
							cellInt.setPhrase(new Phrase(i.getRenditaImmobileRiv()!=null ? "  " +DF.format(i.getRenditaImmobileRiv()):"",normalSmallFont));
							cellInt.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellInt.setBackgroundColor(BaseColor.WHITE);
							cellInt.setBorder(Rectangle.NO_BORDER);
							cellInt.setColspan(2);
							tableVal.addCell(cellInt);
							
							cellInt = tableVal.getDefaultCell();
							cellInt.setPhrase(new Phrase("Dovuto Quota Stato:",boldSmallFont));
							cellInt.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellInt.setBackgroundColor(BaseColor.WHITE);
							cellInt.setBorder(Rectangle.NO_BORDER);
							cellInt.setColspan(3);
							tableVal.addCell(cellInt);
							
							cellInt = tableVal.getDefaultCell();
							cellInt.setPhrase(new Phrase(i.getDovutoStato()!=null ? "  " +DF.format(i.getDovutoStato()):"",normalSmallFont));
							cellInt.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellInt.setBackgroundColor(BaseColor.WHITE);
							cellInt.setBorder(Rectangle.NO_BORDER);
							cellInt.setColspan(2);
							tableVal.addCell(cellInt);
							
							cellInt = tableVal.getDefaultCell();
							cellInt.setPhrase(new Phrase("Dovuto Quota Comune:",boldSmallFont));
							cellInt.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellInt.setBackgroundColor(BaseColor.WHITE);
							cellInt.setBorder(Rectangle.NO_BORDER);
							cellInt.setColspan(8);
							tableVal.addCell(cellInt);
							
							cellInt = tableVal.getDefaultCell();
							cellInt.setPhrase(new Phrase(i.getDovutoComune()!=null ? "  " +DF.format(i.getDovutoComune()):"",normalSmallFont));
							cellInt.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellInt.setBackgroundColor(BaseColor.WHITE);
							cellInt.setBorder(Rectangle.NO_BORDER);
							cellInt.setColspan(2);
							tableVal.addCell(cellInt);
							
							cellInt.setBorder(Rectangle.BOX);
							
							tableCatastoF.addCell(tableVal);
							
							this.addCella(tableCatastoF,normalSmallFont,Element.ALIGN_RIGHT, i.getAliquota().toString());
							tableCatastoF.addCell(this.getTabAcconto(i));
							if(i.isVuotoTerrAgr()) 
								this.addCella(tableCatastoF,normalSmallFont,Element.ALIGN_CENTER, "-");
							else
								tableCatastoF.addCell(this.getTabTerrAgricolo(i));
							this.addCella(tableCatastoF,normalSmallFont,Element.ALIGN_RIGHT, i.getTasso().toString());
							this.addCella(tableCatastoF,normalSmallFont,Element.ALIGN_CENTER, i.getNumContitolari());
							tableCatastoF.addCell(this.getTabFigli(i));
							tableCatastoF.addCell(this.getTabIACP(i));
							String flag = i.isTerremotato() ? " Comune terremotato" : "";
							flag += i.isVariazione() ? " Immobile variato" : "";
							this.addCella(tableCatastoF,normalSmallFont,Element.ALIGN_LEFT, flag);
							
					}
					
					paragrafoImu.add(new Phrase("Lista Immobili",new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD)));
					paragrafoImu.add(tableCatastoF);
					this.addEmptyLine(paragrafoImu, 2);
				
				}
			
			}
			else
			{
				paragrafoImu.add(new Paragraph("Dati catastali non presenti", new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD)));
				this.addEmptyLine(paragrafoImu, 2);
			}	
			
			
			//Inserire Dati Anagrafe (Nucleo Familiare)
			if (lstf24!=null && lstf24.size()>0)
			{
				paragrafoImu.add(new Phrase("Lista F24",new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
				
				PdfPTable tableF24 = new PdfPTable(16);
				tableF24.setWidths(new float[] {3,4,4,3,3,14,5,3,5,5,5,5,5,5,5,5});
				tableF24.setWidthPercentage(100);
				tableF24.getDefaultCell().setMinimumHeight(30);
				int align = Element.ALIGN_CENTER;
				
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Ravv.");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Imm.Var.");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Acconto");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Saldo");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "n.Imm.");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Tributo");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Rateazione");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Anno");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Dov.Scad.");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Pag.Scad.");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Da Ravv.");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Sanzione");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Interessi");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Tasso");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Detrazione");
				this.addCellaIntestazione(tableF24, boldSmallFont,align, "Imp.Debito");
				
				for (Iterator<XmlF24DTO> it = lstf24.iterator(); it.hasNext();)
				{
					XmlF24DTO i = it.next();
					
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.isFlgRav()? "SI" : "NO");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.isFlgImmVar()? "SI" : "NO");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.isFlgAcconto()? "SI" : "NO");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.isFlgSaldo()? "SI" : "NO");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getNumImm());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, i.getCodTributo()+" - "+i.getDescTributo());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getFlgRateazione());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getAnnoRif());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, "  " + DF.format(i.getDovutoScadenza().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, "  " + DF.format(i.getPagatoScadenza().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, "  " + DF.format(i.getImpDaRavvedere().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, "  " + DF.format(i.getSanzione().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, "  " + DF.format(i.getInteressi().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT,  DF.format(i.getTasso().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, "  " + DF.format(i.getDetrazione().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, "  " + DF.format(i.getImpDebito().doubleValue()));
					
				}
				paragrafoImu.add(tableF24);
				this.addEmptyLine(paragrafoImu, 2);
				
			}else{
				paragrafoImu.add(new Paragraph("Dati F24 non presenti", new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD)));
				this.addEmptyLine(paragrafoImu, 2);
			}
			
			
		}else
		{
			paragrafoImu.add(new Paragraph("Dati consulenza IMU non presenti", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			this.addEmptyLine(paragrafoImu, 2);
		}
			
	}
	
	private PdfPTable getTabAcconto(XmlImmobileDTO v) throws DocumentException{
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.NORMAL);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		PdfPTable tableAcc = new PdfPTable(3);
		tableAcc.setWidths(new float[] {22,39,39});
		tableAcc.setWidthPercentage(100);
		tableAcc.getDefaultCell().setMinimumHeight(20);
		
		tableAcc.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_CENTER, "");
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "Detraz.");
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "Dovuto");
	
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "S:");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDetrazioneAccStato()!=null ?  "  " +DF.format(v.getDetrazioneAccStato()) : "");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDovutoAccStato()!=null ?  "  " +DF.format(v.getDovutoAccStato()) : "");
		
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "C:");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDetrazioneAccComune()!=null ?  "  " +DF.format(v.getDetrazioneAccComune()) : "");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDovutoAccComune()!=null ?  "  " +DF.format(v.getDovutoAccComune()) : "");
		
		return tableAcc;
	}
	
	private PdfPTable getTabTerrAgricolo(XmlImmobileDTO v) throws DocumentException{
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.NORMAL);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		PdfPTable tableAcc = new PdfPTable(3);
		tableAcc.setWidths(new float[] {22,39,39});
		tableAcc.setWidthPercentage(100);
		
		tableAcc.getDefaultCell().setMinimumHeight(20);
		tableAcc.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_CENTER, "");
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "Detraz.");
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "Dovuto");
	
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "S:");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDetrTerrAgrStato()!=null ?  "  " +DF.format(v.getDetrTerrAgrStato()) : "");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_CENTER, "-");
		
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "C:");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDetrTerrAgrComune()!=null ?  "  " +DF.format(v.getDetrTerrAgrComune()) : "");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_CENTER, "-");
		
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_RIGHT, "Tot:");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDetrTerrAgr()!=null ?  "  " +DF.format(v.getDetrTerrAgr()) : "");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_RIGHT, v.getDovutoTerrAgr()!=null ?  "  " +DF.format(v.getDovutoTerrAgr()) : "");
		
		return tableAcc;
	}
	
	private PdfPTable getTabIACP(XmlImmobileDTO v) throws DocumentException{
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.NORMAL);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		PdfPTable tableAcc = new PdfPTable(2);
		tableAcc.setWidths(new float[] {70,30});
		tableAcc.setWidthPercentage(100);
		tableAcc.getDefaultCell().setMinimumHeight(20);
		tableAcc.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_LEFT, "Tot.");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_CENTER, v.getImmTotali());
		
		this.addCella(tableAcc,boldSmallFont,Element.ALIGN_LEFT, "Ass.");
		this.addCella(tableAcc,normalSmallFont,Element.ALIGN_CENTER, v.getImmAssegnati());
			
		return tableAcc;
	}

	
	private PdfPTable getTabFigli(XmlImmobileDTO v) throws DocumentException{
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.NORMAL);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		PdfPTable tableAcc = new PdfPTable(2);
		tableAcc.setWidths(new float[] {50,50});
		tableAcc.setWidthPercentage(100);
		tableAcc.getDefaultCell().setMinimumHeight(20);
		
		PdfPCell cellInt = tableAcc.getDefaultCell();
		cellInt.setPhrase(new Phrase(v.getNumFigli(),normalSmallFont));
		cellInt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellInt.setBackgroundColor(BaseColor.WHITE);
		cellInt.setBorder(Rectangle.NO_BORDER);
		cellInt.setColspan(10);
		tableAcc.addCell(cellInt);
		
		cellInt.setBorder(Rectangle.BOX);
		
		if(v.getNumFigli()!=null && !"0".equalsIgnoreCase(v.getNumFigli())){
			
			this.addCellaIntestazione(tableAcc, boldSmallFont,Element.ALIGN_CENTER, "Mesi Detr.");
			this.addCellaIntestazione(tableAcc, boldSmallFont,Element.ALIGN_CENTER, "% Detr.");
			
			for(XmlFiglioDTO f : v.getLstFigli()){
				
				if(f.isPresente()){
					
					this.addCella(tableAcc,normalSmallFont,Element.ALIGN_CENTER, f.getMesiDetrazione());
					this.addCella(tableAcc,normalSmallFont,Element.ALIGN_CENTER, f.getPercDetrazione());
					
				}
				
			}
		
		}
		
		return tableAcc;
	}

	
	
	private void addCelleCatastoImuINT(PdfPTable tableCatasto, Font boldSmallFont,Font normalSmallFont, boolean imm ){
		
	    int align = Element.ALIGN_CENTER;
	
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Dati Immobile principale e pertinenze");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Sez");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"F / P / S");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Partita");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Dat.Val. Imm.");
		
		if(imm)
			this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Cat.");
		
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Cls");
		
		if(imm)
			this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Cons.");
		
		if(!imm){
			
			this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Redd. Domin.");
			this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Redd. Agr.");
			this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Area");
			this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Qualità");
		}
		
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Indirizzo");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Soggetto");		
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Titolo");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Reg.");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Tasso");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Aliq.");
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Detr.");
	}
	
	private void addCelleCatastoImu(PdfPTable tableCatasto, Font boldSmallFont,Font normalSmallFont, JsonDatiCatastoDTO i, boolean imm ) throws DocumentException{
		
		DecimalFormat DF = new DecimalFormat("0.00");
		
		PdfPTable tableVal = new PdfPTable(4);
		tableVal.setWidths(new float[] {20,35,20,25});
		tableVal.setWidthPercentage(20);
		tableVal.getDefaultCell().setMinimumHeight(20);
		
		int align = Element.ALIGN_CENTER;
		PdfPCell cellInt = tableVal.getDefaultCell();
		cellInt.setPhrase(new Phrase(i.getTxtTipologia(),normalSmallFont));
		cellInt.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellInt.setBackgroundColor(BaseColor.WHITE);
		cellInt.setColspan(4);
		cellInt.setBorder(Rectangle.NO_BORDER);
		tableVal.addCell(cellInt);
		
		cellInt.setBorder(Rectangle.BOX);
		
		this.addCellaIntestazione(tableVal, boldSmallFont,align, "Tipo");
		this.addCellaIntestazione(tableVal, boldSmallFont,align, "Rendita");
		this.addCellaIntestazione(tableVal, boldSmallFont,align, "Mesi");
		this.addCellaIntestazione(tableVal, boldSmallFont,align, "% Poss");
		
		ValImmobileDTO[]  vals = i.getValori();
		for(ValImmobileDTO v : vals){
			if(!v.isVuoto()){
				this.addCella(tableVal,normalSmallFont,Element.ALIGN_LEFT, v.getCod());
				this.addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getRendita()!=null ? "  " +DF.format(v.getRendita()) : "");
				this.addCella(tableVal,normalSmallFont,Element.ALIGN_CENTER, v.getMesiPoss());	
				this.addCella(tableVal,normalSmallFont,Element.ALIGN_RIGHT, v.getQuotaPoss().toString());
			}
		}
		
		tableCatasto.addCell(tableVal);
		
		align = Element.ALIGN_LEFT;
		
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, i.getSez());
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_CENTER, i.getFoglio()+" / "+i.getNum()+" / "+i.getSub());
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, i.getPartita());
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, "da " + i.getDataInizio() +" a " +i.getDataFine());
		
		if(imm)
			this.addCella(tableCatasto, normalSmallFont, Element.ALIGN_CENTER, i.getCat());
		
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_CENTER, i.getClasse());
		
		if(imm)
			this.addCella(tableCatasto, normalSmallFont, Element.ALIGN_CENTER, i.getConsistenza());
			/*this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Ordine");
			this.addCella(tableCatasto, normalSmallFont, Element.ALIGN_LEFT, i.getOrdImmobile().toString());*/
		
		if(!imm){
			
			this.addCella(tableCatasto, normalSmallFont, Element.ALIGN_RIGHT, i.getRedditoDominicale());
			this.addCella(tableCatasto, normalSmallFont, Element.ALIGN_RIGHT, i.getRedditoAgrario());
			this.addCella(tableCatasto, normalSmallFont, Element.ALIGN_RIGHT, i.getArea());
			this.addCella(tableCatasto, normalSmallFont, Element.ALIGN_LEFT, i.getQualita());
		}
		
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, i.getUbi());
	
		String txtSogg = i.getDenom() +", c.f. " + i.getCodFisc();
		if(i.getDtNas()!=null) txtSogg += " nato il " + i.getDtNas();
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, txtSogg);
		/*this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Sede");
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, i.getSede());
		this.addCellaIntestazione(tableCatasto,boldSmallFont,align,"Prov.");
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, i.getProvCat());*/
		
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_LEFT, i.getTit() +" da "+i.getDataInizioTit()+" a " + i.getDataFineTit());
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_CENTER, i.getRegime());
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_RIGHT, i.getTasso());
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_RIGHT, i.getAliquota());
		this.addCella(tableCatasto,normalSmallFont,Element.ALIGN_RIGHT, i.getDetrazione());
	}
	
	
	private void addSezioneImuDatiElab(Paragraph paragrafoImu) throws DocumentException {
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.NORMAL);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		if(datiElabImu!=null){
			paragrafoImu.add(new Phrase("DATI INPUT CONSULENZA",new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
			
		
			List<DatiAnagrafeDTO> tabAnagrafe = datiElabImu.getTabAnagrafe();
			DatiAnagrafeDTO dichiarante = datiElabImu.getDichiarante();
		
			//Inserire Dati Dichiarante
			if(dichiarante!=null){
				this.addEmptyLine(paragrafoImu, 2);
				paragrafoImu.add(new Phrase("Dichiarante",new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
			
				PdfPTable tableDatiDich = new PdfPTable(4);
				tableDatiDich.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableDatiDich.setWidths(new float[] {20,35,10,35});
				tableDatiDich.setWidthPercentage(100);
				tableDatiDich.getDefaultCell().setMinimumHeight(30);
				
				int align = Element.ALIGN_LEFT;
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "COGNOME");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getCog());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "NOME");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getNom());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "CODICE FISCALE");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getCodFisc());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "SESSO");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getSesso());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "NATO IL");
				this.addCella(tableDatiDich, normalFont, align, Utility.dateToString_ddMMyyyy(dichiarante.getDatNas()));
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "A");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getComNas()+"("+dichiarante.getProvNas()+")");
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "IBAN");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getIban());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "COD.BELFIORE");
				this.addCella(tableDatiDich, normalFont, align, dichiarante.getBelfiore());
				
				this.addCellaIntestazione(tableDatiDich, boldFont,align, "RESIDENZA");
				String indirizzo = "";
				if(dichiarante.getIndRes()!=null && dichiarante.getIndRes().length()>0)
					indirizzo = dichiarante.getIndRes();
				indirizzo += dichiarante.getComRes()!=null  && dichiarante.getComRes().length()>0? ", "+dichiarante.getComRes() : "";
				indirizzo += dichiarante.getProvRes()!=null && dichiarante.getProvRes().length()>0? "("+dichiarante.getProvRes()+")" : "";
					
				PdfPCell cellInt = tableDatiDich.getDefaultCell();
				cellInt.setPhrase(new Phrase(indirizzo,normalFont));
				
				cellInt.setHorizontalAlignment(align);
				cellInt.setBackgroundColor(BaseColor.WHITE);
				cellInt.setColspan(3);
				tableDatiDich.addCell(cellInt);
				
				paragrafoImu.add(tableDatiDich);
				this.addEmptyLine(paragrafoImu, 2);
			}
			
			//Inserire Dati Anagrafe (Nucleo Familiare)
			if (tabAnagrafe!=null && tabAnagrafe.size()>0)
			{
				paragrafoImu.add(new Phrase("Dati nucleo familiare",new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
				
				PdfPTable tableAnag = new PdfPTable(6);
				tableAnag.setWidths(new float[] {16,17,17,16,17,16});
				tableAnag.setWidthPercentage(100);
				tableAnag.getDefaultCell().setMinimumHeight(30);
				int align = Element.ALIGN_CENTER;
				
				this.addCellaIntestazione(tableAnag, boldFont,align, "Cod.Fiscale");
				this.addCellaIntestazione(tableAnag, boldFont,align, "Cognome");
				this.addCellaIntestazione(tableAnag, boldFont,align, "Nome");
				this.addCellaIntestazione(tableAnag, boldFont,align, "Data Nasc.");
				this.addCellaIntestazione(tableAnag, boldFont,align, "Comune Nasc.");
				this.addCellaIntestazione(tableAnag, boldFont,align, "Prov.Nasc.");
				
				for (Iterator<DatiAnagrafeDTO> it = tabAnagrafe.iterator();it.hasNext();)
				{
					DatiAnagrafeDTO i = it.next();
					this.addCella(tableAnag, normalFont, Element.ALIGN_LEFT, i.getCodFisc());
					this.addCella(tableAnag, normalFont, Element.ALIGN_LEFT, i.getCog());
					this.addCella(tableAnag, normalFont, Element.ALIGN_LEFT, i.getNom());
					this.addCella(tableAnag, normalFont, Element.ALIGN_LEFT, Utility.dateToString_ddMMyyyy(i.getDatNas()));
					this.addCella(tableAnag, normalFont, Element.ALIGN_LEFT, i.getComNas());
					this.addCella(tableAnag, normalFont, Element.ALIGN_LEFT, i.getProvNas());
				}
				paragrafoImu.add(tableAnag);
				this.addEmptyLine(paragrafoImu, 2);
				
			}else{
				paragrafoImu.add(new Paragraph("Dati nucleo familiare non presenti", new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD)));
				this.addEmptyLine(paragrafoImu, 2);
			}
			
			//Inserisco il paragrafo Catastale
			List<JsonDatiCatastoDTO> tabCatastoImm = datiElabImu.getTabCatastoImm();
			List<JsonDatiCatastoDTO> tabCatastoTerr = datiElabImu.getTabCatastoTerr();
			
			//Inserire Dati Catasto
			if ((tabCatastoImm!=null && tabCatastoImm.size()>0) || (tabCatastoTerr!=null && tabCatastoTerr.size()>0))
			{	
				paragrafoImu.add(new Paragraph("Dati catastali", new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD)));
				this.addEmptyLine(paragrafoImu, 1);
				
				//Inserisco lista immobili
				if((tabCatastoImm!=null && tabCatastoImm.size()>0)){
				
					PdfPTable tableCatastoF = new PdfPTable(15);
					tableCatastoF.setWidths(new float[] {15,3,8,5,5,3,3,3,14,14,12,3,3,3,5});
					tableCatastoF.setWidthPercentage(100);
					tableCatastoF.getDefaultCell().setMinimumHeight(20);
					
				    this.addCelleCatastoImuINT(tableCatastoF, boldSmallFont, normalSmallFont, true);
					
					for (Iterator<JsonDatiCatastoDTO> it = tabCatastoImm.iterator();it.hasNext();)
					{
						JsonDatiCatastoDTO i= it.next();
						
							//Aggiungo lista immobili
							int align = Element.ALIGN_LEFT;
							this.addCelleCatastoImu(tableCatastoF, boldSmallFont, normalSmallFont, i, true);
							
						
					}
					
					paragrafoImu.add(new Phrase("Lista Immobili",new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD)));
					paragrafoImu.add(tableCatastoF);
					this.addEmptyLine(paragrafoImu, 2);
				
				}
			
				//Inserisco lista terreni
				if((tabCatastoTerr!=null && tabCatastoTerr.size()>0)){
					
					PdfPTable tableCatastoT = new PdfPTable(17);
					                          
					tableCatastoT.setWidths(new float[] {15,3,8,5,5,3,4,4,3,7,10,10,8,3,3,3,5});
					tableCatastoT.setWidthPercentage(100);
					tableCatastoT.getDefaultCell().setMinimumHeight(20);
					
					 this.addCelleCatastoImuINT(tableCatastoT, boldSmallFont, normalSmallFont, false);
					
					for (Iterator<JsonDatiCatastoDTO> it = tabCatastoTerr.iterator();it.hasNext();)
					{
						JsonDatiCatastoDTO i= it.next();
						
							//Aggiungo lista comuni
							int align = Element.ALIGN_LEFT;	
							this.addCelleCatastoImu(tableCatastoT, boldSmallFont, normalSmallFont, i,false);
							
						
						}
						
					paragrafoImu.add(new Phrase("Lista Terreni",new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD)));
					paragrafoImu.add(tableCatastoT);
					this.addEmptyLine(paragrafoImu, 2);
					
				}
				
				
			}
			else
			{
				paragrafoImu.add(new Paragraph("Dati catastali non presenti", new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD)));
				this.addEmptyLine(paragrafoImu, 2);
			}	
			
		}else
		{
			paragrafoImu.add(new Paragraph("Dati input consulenza IMU non presenti", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			this.addEmptyLine(paragrafoImu, 2);
		}
		
		
	}
	


	
	
	private void addSezioneCarSociale(Document document) throws DocumentException {
		
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.NORMAL);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		Paragraph paragrafoCarSociale = new Paragraph();
		
		paragrafoCarSociale.add(this.insertTitoloScheda("SCHEDA CARTELLA SOCIALE"));
				
		if (!cbxSezioneCarSociale) {
			paragrafoCarSociale.add(new Phrase(motivoCarSociale,normalSmallFont));
			document.add(paragrafoCarSociale);
			document.newPage();
			return;
		}
		
		this.addEmptyLine(paragrafoCarSociale, 2);
				
		if (listaInterventiCarSociale!=null && listaInterventiCarSociale.size()>0)
		{
			paragrafoCarSociale.add(new Phrase("Elenco Interventi",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			
			PdfPTable tableCarSociale = new PdfPTable(5);
			tableCarSociale.setWidths(new float[] {35,12,12,12,29});
			tableCarSociale.setWidthPercentage(100);
			tableCarSociale.getDefaultCell().setMinimumHeight(30);
			int align = Element.ALIGN_CENTER;
			this.addCellaIntestazione(tableCarSociale,boldFont,align,"INTERVENTO");
			this.addCellaIntestazione(tableCarSociale,boldFont,align,"DATA INIZIO");
			this.addCellaIntestazione(tableCarSociale,boldFont,align,"DATA FINE");
			this.addCellaIntestazione(tableCarSociale,boldFont,align,"IMPORTO INTERVENTO");
			this.addCellaIntestazione(tableCarSociale,boldFont,align,"ENTE EROGANTE");
			
			for (Iterator<InterventoDTO> it = listaInterventiCarSociale.iterator();it.hasNext();)
			{
				InterventoDTO i= it.next();
				
				this.addCella(tableCarSociale,normalFont,Element.ALIGN_LEFT, i.getDescrizione().trim());
				this.addCella(tableCarSociale,normalFont,Element.ALIGN_LEFT, i.getDtInizioValStr().trim());
				this.addCella(tableCarSociale,normalFont,Element.ALIGN_LEFT, i.getDtFineValStr().trim());
				this.addCella(tableCarSociale,normalFont,Element.ALIGN_RIGHT, i.getImportoErogatoStr());
				this.addCella(tableCarSociale,normalFont,Element.ALIGN_LEFT, i.getDescComuneErogante());
		
			}
			
			paragrafoCarSociale.add(tableCarSociale);
			
			this.addEmptyLine(paragrafoCarSociale, 2);
		}
		else
		{
			paragrafoCarSociale.add(new Paragraph("Nessun intervento da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			this.addEmptyLine(paragrafoCarSociale, 2);
		}		
		
		document.add(paragrafoCarSociale);
		
		document.newPage();
	}

	
    private void addSezioneTarsu(Document document) throws DocumentException {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Paragraph paragrafoTarsu = new Paragraph();
		paragrafoTarsu.add(this.insertTitoloScheda("SCHEDA TARSU"));
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		if (!cbxSezioneTarsu) {
			paragrafoTarsu.add(new Phrase(motivoTarsu,normalSmallFont));
			document.add(paragrafoTarsu);
			document.newPage();
			return;
		}
		
		if(tribTarsuFonte.isAbilitataCC()){
			paragrafoTarsu.add(tribTarsuFonte.getStrDataAgg());
			this.addEmptyLine(paragrafoTarsu, 1);
			paragrafoTarsu.add(new Phrase(tribTarsuFonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoTarsu, 2);
			
			SitTTarSogg soggettoTarsu = new SitTTarSogg();
			if (soggettoTarsuDaVisualizzare==null || soggettoTarsuDaVisualizzare.getId()==null)
			{
				try{
					List<SitTTarSogg> listaSoggettiTarsuPdf = super.getTarsuCarContribService().searchSoggettiCorrelatiTarsu(this.getObjRicerca(Utility.TipoRicerca.TARSU));
					if (listaSoggettiTarsuPdf!=null && listaSoggettiTarsuPdf.size()>0)
						soggettoTarsu = listaSoggettiTarsuPdf.get(0);
				}catch(IndiceNonAllineatoException ie){
					this.addErrorMessage("cc.indice.disallineato", ie.getMessage());
				}
			}
			else
				soggettoTarsu = soggettoTarsuDaVisualizzare;
	
			if (soggettoTarsu!=null && soggettoTarsu.getId()!=null)
			{
				paragrafoTarsu.add(new Phrase("Dati anagrafici dichiarati",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				String residenza = "";
				
				if (soggettoCartella.getTipoSogg().equals("F"))
				{
					if (soggettoTarsu.getDesComRes()!=null && soggettoTarsu.getDesComRes().trim()!="")
						residenza = soggettoTarsu.getDesComRes().trim();
					if (soggettoTarsu.getDesIndRes()!=null && soggettoTarsu.getDesIndRes().trim()!="")
						residenza = soggettoTarsu.getDesIndRes().trim() + "   " + residenza;
					if (soggettoTarsu.getSiglaProvRes()!=null && soggettoTarsu.getSiglaProvRes().trim()!="")
						residenza += " (" + soggettoTarsu.getSiglaProvRes().trim()+ ")";
				}			
				// Sezione Dati Anagrafici dichiarati
				paragrafoTarsu.add(this.insertSezioneDatiAnagrafici(soggettoTarsu.getCogDenom(),
						soggettoTarsu.getNome(),soggettoTarsu.getDtNsc(),
						soggettoTarsu.getDesComNsc(),residenza,soggettoTarsu.getCodFisc(),
						soggettoTarsu.getCogDenom(),soggettoTarsu.getPartIva()));
				this.addEmptyLine(paragrafoTarsu, 2);
				
				List<DatiTarsuDTO> listaImmobiliDichiaratiTarsuPdf = new ArrayList<DatiTarsuDTO>();
	
				if (this.getListaImmobiliDichiaratiTarsu()==null)
				{
					RicercaSoggettoTarsuDTO soggettoTARSU = this.getDatiSoggettoTARSU();
					soggettoTARSU.setIdSoggTarsu(soggettoTarsu.getId());
					listaImmobiliDichiaratiTarsuPdf = super.getTarsuCarContribService().getDatiTarsu(soggettoTARSU, this.getIndiciSoggettoCartella());
				}
				else
					listaImmobiliDichiaratiTarsuPdf = this.getListaImmobiliDichiaratiTarsu();
				
				if (listaImmobiliDichiaratiTarsuPdf!=null && listaImmobiliDichiaratiTarsuPdf.size()>0)
				{
					paragrafoTarsu.add(new Paragraph("Unità immobiliari dichiarate", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					
					PdfPTable tableImmobili = new PdfPTable(8);
					tableImmobili.setWidths(new float[] {7,10,17,17,8,8,7,27});
					tableImmobili.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableImmobili.setWidthPercentage(100);
					tableImmobili.getDefaultCell().setMinimumHeight(20);
					
					int align = Element.ALIGN_CENTER;
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"SEZIONE");
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"F-P-S");
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"DESCRIZIONE CLASSE");
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"DESCRIZIONE TIPO");
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"INIZIO POSSESSO");
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"FINE POSSESSO");
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"SUPERF. TOTALE");
					this.addCellaIntestazione(tableImmobili,boldSmallFont,align,"INDIRIZZO TARSU");
					
					for (Iterator<DatiTarsuDTO> d = listaImmobiliDichiaratiTarsuPdf.iterator();d.hasNext();)
					{
						DatiTarsuDTO immobile= d.next();
						
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(immobile.getSezione()));
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(immobile.getDescFPS()));
						this.addCella(tableImmobili, new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL), Element.ALIGN_CENTER, immobile.getDesClasse());
						this.addCella(tableImmobili, new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL), Element.ALIGN_LEFT,   immobile.getDesTipOgg());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER, immobile.getDtIniPos());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER, immobile.getDtFinPos());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_RIGHT,  immobile.getSupTot().toString());
						
						String indirizzo = "";
						if (immobile.getIndirizzo()!=null && immobile.getIndirizzo().getDesIndirizzo()!="")
							indirizzo = immobile.getIndirizzo().getDesIndirizzo();
						
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_LEFT, indirizzo);
					}
					
					paragrafoTarsu.add(tableImmobili);
					
					this.addEmptyLine(paragrafoTarsu, 1);
				}
				else
				{
					paragrafoTarsu.add(new Paragraph("Lista unità immobiliari dichiarate non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoTarsu, 1);
				}
				
			
				// START RECUPERO LISTA VERSAMENTI TARSU
				if (listaVersamentiTarsu!=null && listaVersamentiTarsu.size() > 0 )
				{
					paragrafoTarsu.add(new Paragraph("Lista versamenti", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
	
					PdfPTable tableVersamenti = new PdfPTable(4);
					tableVersamenti.setWidths(new float[] {25,25,25,25});
					tableVersamenti.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableVersamenti.setWidthPercentage(100);
					tableVersamenti.getDefaultCell().setMinimumHeight(20);
					
					this.addCellaIntestazione(tableVersamenti, boldSmallFont, Element.ALIGN_CENTER, "ANNO");
					this.addCellaIntestazione(tableVersamenti, boldSmallFont, Element.ALIGN_CENTER, "VERSATO");
					this.addCellaIntestazione(tableVersamenti, boldSmallFont, Element.ALIGN_CENTER, "ISCRITTO A RUOLO");
					this.addCellaIntestazione(tableVersamenti, boldSmallFont, Element.ALIGN_CENTER, "RISCOSSO");
					
					for (DatiImportiCncDTO importo: listaVersamentiTarsu)
					{					
						this.addCella(tableVersamenti, normalSmallFont, Element.ALIGN_CENTER, importo.getAnnoRif());
						this.addCella(tableVersamenti, normalSmallFont, Element.ALIGN_RIGHT,  importo.getImpTotVer()!=null?importo.getImpTotVer().toString():"");
						this.addCella(tableVersamenti, normalSmallFont, Element.ALIGN_RIGHT, importo.getImpTotRuolo()!=null?importo.getImpTotRuolo().toString():"");
						this.addCella(tableVersamenti, normalSmallFont, Element.ALIGN_RIGHT, importo.getImpTotRiscosso()!=null?importo.getImpTotRiscosso().toString():"");
					}
					paragrafoTarsu.add(tableVersamenti);
					this.addEmptyLine(paragrafoTarsu, 1);
				}
				else
				{
					paragrafoTarsu.add(new Paragraph("Lista versamenti non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoTarsu, 1);
				}
				// END RECUPERO LISTA VERSAMENTI TARSU
				
				/*
				paragrafoTarsu.add(new Paragraph(intestazioneSituazionePatrimonialeTarsu, new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoTarsu, 1);
				// START RECUPERO LISTA IMMOBILI
				if (listaImmobiliTarsu!=null && listaImmobiliTarsu.size() > 0 )
				{
					paragrafoTarsu.add(new Paragraph("Lista immobili", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
	
					PdfPTable tableImm = new PdfPTable(12);
					tableImm.setWidths(new float[] {5,9,5,6,9,9,9,6,5,9,9,19});
					tableImm.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableImm.setWidthPercentage(100);
					tableImm.getDefaultCell().setBorder(0);
					tableImm.getDefaultCell().setMinimumHeight(20);
					
					PdfPCell c1 = new PdfPCell(new Phrase("SEZ.",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("F/P/S",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("CAT.",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("CLASSE",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("RENDITA",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("INIZIO POSS.",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("FINE POSS.",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("QUOTA",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("SUP.",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("SUP.TARSU C340",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("LOCATO A:",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("DICHIARANTI TARSU",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					for (SitPatrimImmobileDTO dto: listaImmobiliTarsu)
					{					
						PdfPCell cell = new PdfPCell(new Phrase(dto.getSezione(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitImmobile().getId().getFoglio()+" / "+dto.getDatiTitImmobile().getId().getParticella()+" / "+dto.getDatiTitImmobile().getId().getUnimm(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getCategoria(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getClasse(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getRenditaF(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitImmobile().getDataInizio()!=null?sdf.format(dto.getDatiTitImmobile().getDataInizio()):"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitImmobile().getId().getDataFine()!=null?sdf.format(dto.getDatiTitImmobile().getId().getDataFine()):"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getPercPossF()+"%",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getSuperficieCatF(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getSuperficieTarsuF(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase("Info non disponibile",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						tableImm.addCell(cell);
						
						String dichiaranti = "";
						if(dto.getListaDichiarantiTarsu()!=null){
							for(String d: dto.getListaDichiarantiTarsu()){
								dichiaranti += "," + d;
							}
						}
						cell = new PdfPCell(new Phrase(dichiaranti.length()>1?dichiaranti.substring(1):"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						tableImm.addCell(cell);
					}
					paragrafoTarsu.add(tableImm);
					this.addEmptyLine(paragrafoTarsu, 1);
				}
				else
				{
					paragrafoTarsu.add(new Paragraph("Lista immobili non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoTarsu, 1);
				}
				// END RECUPERO LISTA IMMOBILI
				
				//RECUPERO LISTA LOCAZIONI (con informazioni TARSU)
				this.addLocazioniTarsu(paragrafoTarsu);
				*/
			}
			else
			{
				this.addEmptyLine(paragrafoTarsu, 1);			
				paragrafoTarsu.add(new Paragraph("Nessun dato TARSU da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			}
		
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafoTarsu, this.tribIciFonte.getDescrizione());
		
		document.add(paragrafoTarsu);
		
		document.newPage();
	}
    
    
    private void addSezionePatrimoniale(Document document) throws DocumentException {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Paragraph paragrafoPatri = new Paragraph();
		paragrafoPatri.add(this.insertTitoloScheda("SCHEDA SITUAZIONE PATRIMONIALE E LOCAZIONI"));
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL);
		
		if (!cbxSezionePatrimoniale) {
			paragrafoPatri.add(new Phrase(motivoPatrimoniale,normalSmallFont));
			document.add(paragrafoPatri);
			document.newPage();
			return;
		}
		
		if(catastoFonte.isAbilitataCC()){
			
			paragrafoPatri.add(catastoFonte.getStrDataAgg());
			this.addEmptyLine(paragrafoPatri, 1);
			paragrafoPatri.add(new Phrase(catastoFonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoPatri, 2);
	
			paragrafoPatri.add(new Paragraph(intestazioneSituazionePatrimoniale, new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD)));
			this.addEmptyLine(paragrafoPatri, 1);
			// START RECUPERO LISTA IMMOBILI
			if (listaImmobili!=null && listaImmobili.size() > 0 )
			{
				paragrafoPatri.add(new Paragraph("Lista immobili", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));

				PdfPTable tableImm = new PdfPTable(13);
				tableImm.setWidths(new float[] {4,9,4,6,6,9,8,5,8,4,8,6,23});
				tableImm.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableImm.setWidthPercentage(100);
				tableImm.getDefaultCell().setMinimumHeight(20);
				
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "SEZ.");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "F/P/S");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "CAT.");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "CLASSE");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "RENDITA");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "INIZIO POSS.");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "FINE POSS.");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "QUOTA");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "TITOLO");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "SUP.");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "SUP.TARSU C340");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "LOCATO");
				this.addCellaIntestazione(tableImm, boldSmallFont, Element.ALIGN_CENTER, "DICHIARANTI TARSU");
				
				for (SitPatrimImmobileDTO dto: listaImmobili)
				{				
					
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, dto.getSezione());
					
					String fps = dto.getDatiTitImmobile().getId().getFoglio()+" / "
								+dto.getDatiTitImmobile().getId().getParticella()+" / "
							    +dto.getDatiTitImmobile().getId().getUnimm();
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, fps);
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, dto.getCategoria());
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, dto.getClasse());
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_RIGHT, dto.getRenditaF());
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiTitImmobile().getDataInizio()!=null?sdf.format(dto.getDatiTitImmobile().getDataInizio()):"");
					String dataFine = dto.getDatiTitImmobile().getId().getDataFine()!=null?sdf.format(dto.getDatiTitImmobile().getId().getDataFine()):"";
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, dataFine.equals("31/12/9999") ? "ATTUALE" : dataFine);
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, dto.getPercPossF()+"%");
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, dto.getDescTitolo()!=null ? dto.getDescTitolo() : "");
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_RIGHT, dto.getSuperficieCatF());
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_RIGHT, dto.getSuperficieTarsuF());
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_CENTER, "-");
					
					String dichiaranti = "";
					if(dto.getListaDichiarantiTarsu()!=null){
						for(String d: dto.getListaDichiarantiTarsu()){
							dichiaranti += "," + d;
						}
					}
					this.addCella(tableImm, normalSmallFont, Element.ALIGN_LEFT, dichiaranti.length()>1?dichiaranti.substring(1):"");
				}
				paragrafoPatri.add(tableImm);
				this.addEmptyLine(paragrafoPatri, 1);
			}
			else
			{
				paragrafoPatri.add(new Paragraph("Lista immobili non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoPatri, 1);
			}
			// END RECUPERO LISTA IMMOBILI
			
			// START RECUPERO LISTA TERRENI
			if (listaTerreni!=null && listaTerreni.size() > 0 )
			{
				paragrafoPatri.add(new Paragraph("Lista terreni", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));

				PdfPTable tableTerr = new PdfPTable(12);
				tableTerr.setWidths(new float[] {4,9,10,6,5,10,10,9,8,7,10,12});
				tableTerr.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableTerr.setWidthPercentage(100);
				tableTerr.getDefaultCell().setMinimumHeight(20);
				
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "SEZ.");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "F/P");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "QUALITA'");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "CLASSE");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "SUP.");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "RED. DOMINICALE");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "RED. AGRARIO");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "INIZIO POSS.");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "FINE POSS.");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "QUOTA");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "TITOLO");
				this.addCellaIntestazione(tableTerr, boldSmallFont, Element.ALIGN_CENTER, "LOCATO");
				
				for (SitPatrimTerrenoDTO dto: listaTerreni)
				{		
					
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiTitTerreno().getSezione());
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiTitTerreno().getFoglio()+" / "+dto.getDatiTitTerreno().getParticella());
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_LEFT, dto.getDatiTitTerreno().getQualita()!=null?dto.getDatiTitTerreno().getQualita().toString():"");
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiTitTerreno().getClasse());
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_RIGHT, dto.getSuperficieF());
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_RIGHT, dto.getRedditoDominicaleF());
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_RIGHT, dto.getRedditoAgrarioF());
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiTitTerreno().getDtIniPos()!=null?sdf.format(dto.getDatiTitTerreno().getDtIniPos()):"");
					
					String dataFine = dto.getDatiTitTerreno().getDtFinPos()!=null?sdf.format(dto.getDatiTitTerreno().getDtFinPos()):"";
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, dataFine.equals("31/12/9999") ? "ATTUALE" : dataFine);
					
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, StringUtility.DF.format(dto.getDatiTitTerreno().getPercPoss())+"%");
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiTitTerreno().getTitolo());
					this.addCella(tableTerr, normalSmallFont, Element.ALIGN_CENTER, "-");
		
				}
				paragrafoPatri.add(tableTerr);
				this.addEmptyLine(paragrafoPatri, 1);
			}
			else
			{
				paragrafoPatri.add(new Paragraph("Lista terreni non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoPatri, 1);
			}
			// END RECUPERO LISTA TERRENI
				
		}else this.addMotivoFonteDisabilitataCC(paragrafoPatri, this.catastoFonte.getDescrizione());
	
		//RECUPERO LISTA LOCAZIONI (con informazioni TARSU)
		this.addLocazioni(paragrafoPatri);
		
		this.addBolliVeicoli(paragrafoPatri);
	
		document.add(paragrafoPatri);
		
		document.newPage();
	}//-------------------------------------------------------------------------
    
    /*
    private void addLocazioniIci(Paragraph paragrafoIci) throws DocumentException{
       	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		paragrafoIci.add(new Paragraph("Lista locazioni", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
		
		if(locazioniFonte.isAbilitataCC()){
	    	// START RECUPERO LISTA LOCAZIONI
			if (listaLocazioni!=null && listaLocazioni.size() > 0 )
			{
				PdfPTable tableLoc = new PdfPTable(7);
				tableLoc.setWidths(new float[] {20,10,10,10,20,15,15});
				tableLoc.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableLoc.setWidthPercentage(100);
				tableLoc.getDefaultCell().setBorder(0);
				tableLoc.getDefaultCell().setMinimumHeight(20);
				
				PdfPCell c1 = new PdfPCell(new Phrase("ID CONTRATTO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("DATA INIZIO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("DATA FINE",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("F/P/S",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("INDIRIZZO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("COMUNE",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("TIPO SOGGETTO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				for (LocazioniDTO dto: listaLocazioni)
				{					
					PdfPCell cell = new PdfPCell(new Phrase(dto.getDatiOggLocazione().getNumero()+"-"+dto.getDatiOggLocazione().getSerie()+"-"+dto.getDatiOggLocazione().getAnno()+"-"+dto.getDatiOggLocazione().getUfficio(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getDatiOggLocazione().getDataInizio()!=null?sdf.format(dto.getDatiOggLocazione().getDataInizio()):"",normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getDatiOggLocazione().getDataFine()!=null?sdf.format(dto.getDatiOggLocazione().getDataFine()):"",normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getFps(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getIndirizzo(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getComune(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getTipoSoggetto(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
				}
				paragrafoIci.add(tableLoc);
				this.addEmptyLine(paragrafoIci, 1);
			}
			else
			{
				paragrafoIci.add(new Paragraph("Lista locazioni non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			}
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafoIci, this.locazioniFonte.getDescrizione());
		// END RECUPERO LISTA LOCAZIONI
    }

    private void addLocazioniTarsu(Paragraph paragrafoTarsu) throws DocumentException{
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		paragrafoTarsu.add(new Paragraph("Lista locazioni", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
		
		if(locazioniFonte.isAbilitataCC()){
		
			// START RECUPERO LISTA LOCAZIONI
			if (listaLocazioniTarsu!=null && listaLocazioniTarsu.size() > 0 )
			{
		
				PdfPTable tableLoc = new PdfPTable(9);
				tableLoc.setWidths(new float[] {15,10,10,10,15,10,10,10,10});
				tableLoc.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableLoc.setWidthPercentage(100);
				tableLoc.getDefaultCell().setBorder(0);
				tableLoc.getDefaultCell().setMinimumHeight(20);
				
				PdfPCell c1 = new PdfPCell(new Phrase("ID CONTRATTO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("DATA INIZIO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("DATA FINE",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("F/P/S",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("INDIRIZZO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("COMUNE",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("SUP. CATASTALE",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("SUP. DICHIARATA TARSU",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				c1 = new PdfPCell(new Phrase("TIPO SOGGETTO",boldSmallFont));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableLoc.addCell(c1);
				
				for (LocazioniDTO dto: listaLocazioniTarsu)
				{					
					PdfPCell cell = new PdfPCell(new Phrase(dto.getDatiOggLocazione().getNumero()+"-"+dto.getDatiOggLocazione().getSerie()+"-"+dto.getDatiOggLocazione().getAnno()+"-"+dto.getDatiOggLocazione().getUfficio(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getDatiOggLocazione().getDataInizio()!=null?sdf.format(dto.getDatiOggLocazione().getDataInizio()):"",normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getDatiOggLocazione().getDataFine()!=null?sdf.format(dto.getDatiOggLocazione().getDataFine()):"",normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getFps(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getIndirizzo(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getComune(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getSupCat(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getSupTarsu(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					tableLoc.addCell(cell);
					
					cell = new PdfPCell(new Phrase(dto.getTipoSoggetto(),normalSmallFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableLoc.addCell(cell);
				}
				paragrafoTarsu.add(tableLoc);
				this.addEmptyLine(paragrafoTarsu, 1);
			}
			else
			{
				paragrafoTarsu.add(new Paragraph("Lista locazioni non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			}
			// END RECUPERO LISTA LOCAZIONI
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafoTarsu, this.locazioniFonte.getDescrizione());
    }*/
    
  private void addLocazioni(Paragraph paragrafo) throws DocumentException{
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		this.addEmptyLine(paragrafo, 1);
		paragrafo.add(new Paragraph("LOCAZIONI", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
		if(locazioniFonte.isAbilitataCC()){
			
			paragrafo.add(locazioniFonte.getStrDataAgg());
			this.addEmptyLine(paragrafo, 1);
			paragrafo.add(new Phrase(locazioniFonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafo, 2);
		
		
			// START RECUPERO LISTA LOCAZIONI
			if (listaLocazioni!=null && listaLocazioni.size() > 0 )
			{
				
				paragrafo.add(new Paragraph("Lista locazioni", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable tableLoc = new PdfPTable(9);
				tableLoc.setWidths(new float[] {15,10,10,10,15,10,10,10,10});
				tableLoc.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableLoc.setWidthPercentage(100);
				tableLoc.getDefaultCell().setMinimumHeight(20);
				
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "ID CONTRATTO");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "DATA INIZIO");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "DATA FINE");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "F/P/S");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "INDIRIZZO");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "COMUNE");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "SUP. CATASTALE");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "SUP. DICHIARATA TARSU");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "TIPO SOGGETTO");
						
				for (LocazioniDTO dto: listaLocazioni)
				{					
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_RIGHT, dto.getDatiOggLocazione().getNumero()+"-"+dto.getDatiOggLocazione().getSerie()+"-"+dto.getDatiOggLocazione().getAnno()+"-"+dto.getDatiOggLocazione().getUfficio());
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiOggLocazione().getDataInizio()!=null?sdf.format(dto.getDatiOggLocazione().getDataInizio()):"");
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getDatiOggLocazione().getDataFine()!=null?sdf.format(dto.getDatiOggLocazione().getDataFine()):"");
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getFps());
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getIndirizzo());
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getComune());
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_RIGHT,  dto.getSupCat());
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_RIGHT,  dto.getSupTarsu());
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getTipoSoggetto());
				}
				paragrafo.add(tableLoc);
				this.addEmptyLine(paragrafo, 1);
			}
			else
			{
				paragrafo.add(new Paragraph("Lista locazioni non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			}
			// END RECUPERO LISTA LOCAZIONI
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafo, this.locazioniFonte.getDescrizione());
    }//-------------------------------------------------------------------------
  
  	private void addBolliVeicoli(Paragraph paragrafo) throws DocumentException{
  	
  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
  		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
  		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		this.addEmptyLine(paragrafo, 1);
		paragrafo.add(new Paragraph("BOLLI VEICOLI", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
		if (bolliVeicoliFonte.isAbilitataCC()){
			
			paragrafo.add(bolliVeicoliFonte.getStrDataAgg());
			this.addEmptyLine(paragrafo, 1);
			paragrafo.add(new Phrase(bolliVeicoliFonte.getNota(), normalSmallFont));
			this.addEmptyLine(paragrafo, 2);
		
		
			// START RECUPERO LISTA BOLLI VEICOLI
			if (listaBolliVeicoli!=null && listaBolliVeicoli.size() > 0 )
			{
				
				paragrafo.add(new Paragraph("Lista Bolli Veicoli", new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD)));
				
				PdfPTable tableLoc = new PdfPTable(13);
				tableLoc.setWidths(new float[] {10,10,10,5,5,5,5,5,5,10,5,10,15});
				tableLoc.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableLoc.setWidthPercentage(100);
				tableLoc.getDefaultCell().setMinimumHeight(20);
				
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "TARGA");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "USO");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "DESTINAZIONE");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "PORTATA");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "CC");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "ALIM.");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "MASSA RIM.");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "N. POSTI");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "KW");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "DT PRIMA IMMAT.");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "N. ASSI");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "SIGLA EURO");
				this.addCellaIntestazione(tableLoc, boldSmallFont, Element.ALIGN_CENTER, "CODICE TELAIO");
						
				for (BolloVeicolo dto: listaBolliVeicoli)
				{					
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_RIGHT, dto.getTarga() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getUso() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getDestinazione() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getPortata().toString() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getCilindrata().toString() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getAlimentazione() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_RIGHT,  dto.getMassaRimorchiabile().toString() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_RIGHT,  dto.getNumeroPosti().toString() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getKw().toString() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getDtPrimaImmatricolazione()!=null?sdf.format( dto.getDtPrimaImmatricolazione() ):"" );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getNumeroAssi().toString() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getCodSiglaEuro() );
					this.addCella(tableLoc, normalSmallFont, Element.ALIGN_CENTER, dto.getCodiceTelaio() );
				}
				paragrafo.add(tableLoc);
				this.addEmptyLine(paragrafo, 1);
			}
			else
			{
				paragrafo.add(new Paragraph("Lista Bolli Veicoli non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			}
			// END RECUPERO LISTA BOLLI VEICOLI
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafo, this.bolliVeicoliFonte.getDescrizione());
  	}//-------------------------------------------------------------------------
    
    private void addSezioneIci(Document document) throws DocumentException {
			
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Paragraph paragrafoIci = new Paragraph();
		
		paragrafoIci.add(this.insertTitoloScheda("SCHEDA ICI"));
		
		Font boldSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL);
		
		if (!cbxSezioneIci) {
			paragrafoIci.add(new Phrase(motivoIci,normalSmallFont));
			document.add(paragrafoIci);
			document.newPage();
			return;
		}
		
		if(tribIciFonte.isAbilitataCC()){
			paragrafoIci.add(tribIciFonte.getStrDataAgg());
			this.addEmptyLine(paragrafoIci, 1);
			paragrafoIci.add(new Phrase(tribIciFonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoIci, 2);
			
			RicercaSoggettoIciDTO soggettoICI = this.getDatiSoggettoICI();
			SitTIciSogg soggettoIci = new SitTIciSogg();
			
			if (soggettoIciDaVisualizzare==null || soggettoIciDaVisualizzare.getId()==null)
			{
				try{
					List<SitTIciSogg> listaSoggettiICIPdf = super.getIciServiceCarContrib().searchSoggettiCorrelatiIci(this.getObjRicerca(Utility.TipoRicerca.ICI));
					if (listaSoggettiICIPdf!=null && listaSoggettiICIPdf.size()>0)
						soggettoIci = listaSoggettiICIPdf.get(0);
				}catch(IndiceNonAllineatoException ie){
					this.addErrorMessage("cc.indice.disallineato", ie.getMessage());
				}
			}
			else // PRENDO L'ID DEL SOGG. ICI SELEZIONATO
				soggettoIci = soggettoIciDaVisualizzare;
			
			if (soggettoIci!=null && soggettoIci.getId()!=null)
			{
				paragrafoIci.add(new Phrase("Dati anagrafici dichiarati",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			
				String residenza = "";
				
				if (soggettoCartella.getTipoSogg().equals("F"))
				{
					if (soggettoIci.getDesComRes()!=null && soggettoIci.getDesComRes().trim()!="")
						residenza = soggettoIci.getDesComRes().trim();
					if (soggettoIci.getDesIndRes()!=null && soggettoIci.getDesIndRes().trim()!="")
						residenza = soggettoIci.getDesIndRes().trim() + "   " + residenza;
					if (soggettoIci.getSiglaProvRes()!=null && soggettoIci.getSiglaProvRes().trim()!="")
						residenza += " (" + soggettoIci.getSiglaProvRes().trim()+ ")";
				}
				
				// Sezione Dati Anagrafici dichiarati
				paragrafoIci.add(this.insertSezioneDatiAnagrafici(soggettoIci.getCogDenom(),
						soggettoIci.getNome(),soggettoIci.getDtNsc(),
						soggettoIci.getDesComNsc(),residenza,soggettoIci.getCodFisc(),
						soggettoIci.getCogDenom(),soggettoIci.getPartIva()));
				this.addEmptyLine(paragrafoIci, 2);
	
				soggettoICI.setIdSoggIci(soggettoIci.getId());
				
				// START RECUPERO LISTA IMMOBILI ICI
				List<DatiIciDTO> listaImmobiliDichiaratiICIPdf = new ArrayList<DatiIciDTO>();
				if (this.getListaImmobiliDichiaratiICI()==null)
					listaImmobiliDichiaratiICIPdf = super.getIciServiceCarContrib().getDatiIci(soggettoICI, this.getIndiciSoggettoCartella());
				else
					listaImmobiliDichiaratiICIPdf = this.getListaImmobiliDichiaratiICI();
				
				if (listaImmobiliDichiaratiICIPdf!=null && listaImmobiliDichiaratiICIPdf.size()>0)
				{
					paragrafoIci.add(new Paragraph("Unità immobiliari dichiarate", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					
					PdfPTable tableImmobili = new PdfPTable(15);
					tableImmobili.setWidths(new float[] {6,11,5,5,10,6,6,6,8,4,5,7,5,8,8});
					
					tableImmobili.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableImmobili.setWidthPercentage(100);
					tableImmobili.getDefaultCell().setMinimumHeight(20);
					
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "SEZ.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "F-P-S");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "ANNO DEN.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "ANNO RIF.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "NUM. DEN.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "TIPO DEN.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "CAT.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "CLASSE");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "VALORE");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "TIPO");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "IMM. STOR.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "% POSS.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "MESI POSS.");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "ACQ/CESS");
					this.addCellaIntestazione(tableImmobili, boldSmallFont, Element.ALIGN_CENTER, "AB. PRINC.");
			
					for (Iterator<DatiIciDTO> d = listaImmobiliDichiaratiICIPdf.iterator();d.hasNext();)
					{
						DatiIciDTO immobile= d.next();
						
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER, this.formattaStringa(immobile.getSezione()));
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,this.formattaStringa(immobile.getDescFPS()));
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getAnnoDenuncia());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getAnnoRif());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getNumeroDenuncia());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getTipoDenuncia());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getCategoria());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getClasse());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_RIGHT,immobile.getValImmobileF().toString());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getTipologiaCatastale());
						String flag = "No";
						if (immobile.getFlImmStorico()!=null && immobile.getFlImmStorico().equals("1"))
							flag="Si";
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,flag);
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER, immobile.getPerPossessoF()  .toString());
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,immobile.getMesiPossesso().toString());
	
						flag= "No";
						if (immobile.getFlAcq()!=null && immobile.getFlAcq().equals("1"))
							flag="Si";
						if (immobile.getFlCss()!=null && immobile.getFlCss().equals("1"))
							flag+="/Si";
						else
							flag+="/No";
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,flag);
						
						flag = "No";
						if (immobile.getFlAbitPrinc()!=null && immobile.getFlAbitPrinc().equals("1"))
							flag="Si";
						this.addCella(tableImmobili, normalSmallFont, Element.ALIGN_CENTER,flag);
						
						PdfPCell cellImmobili = new PdfPCell(new Phrase(" INDIRIZZO     ",boldSmallFont));
						cellImmobili.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cellImmobili.setColspan(2);
						cellImmobili.setFixedHeight(20);
						cellImmobili.setBackgroundColor(BaseColor.LIGHT_GRAY);
						tableImmobili.addCell(cellImmobili);
						
						cellImmobili = new PdfPCell(new Phrase(immobile.getIndirizzo().getDesIndirizzo(),normalSmallFont));
						cellImmobili.setHorizontalAlignment(Element.ALIGN_LEFT);
						cellImmobili.setFixedHeight(2);
						cellImmobili.setColspan(13);
						tableImmobili.addCell(cellImmobili);
					}
					
					paragrafoIci.add(tableImmobili);
					
					this.addEmptyLine(paragrafoIci, 2);
				}
				else
				{
					paragrafoIci.add(new Paragraph("Lista unità immobiliari dichiarate non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoIci, 2);
				}
				// END RECUPERO LISTA IMMOBILI ICI
				
				// START RECUPERO LISTA VERSAMENTI ICI
				List<DatiImportiCncDTO> listaVersamentiPdf = new ArrayList<DatiImportiCncDTO>();
				if (this.getListaVersamenti()==null)
					listaVersamentiPdf = super.getIciServiceCarContrib().getVersamenti(soggettoICI, Utility.annoData(super.dataRiferimento));
				else
					listaVersamentiPdf = this.getListaVersamenti();
				
				if (listaVersamentiPdf!=null && listaVersamentiPdf.size() > 0 )
				{
					paragrafoIci.add(new Paragraph("Lista versamenti", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
	
					PdfPTable tableVersamenti = new PdfPTable(2);
					tableVersamenti.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableVersamenti.setWidthPercentage(100);
					tableVersamenti.getDefaultCell().setMinimumHeight(20);
					
					this.addCellaIntestazione(tableVersamenti, boldSmallFont, Element.ALIGN_CENTER, "ANNO");
					this.addCellaIntestazione(tableVersamenti, boldSmallFont, Element.ALIGN_CENTER, "VERSATO");
									
					for (Iterator<DatiImportiCncDTO> d = listaVersamentiPdf.iterator();d.hasNext();)
					{
						DatiImportiCncDTO importo= d.next();
						
						this.addCella(tableVersamenti, normalSmallFont, Element.ALIGN_CENTER,importo.getAnnoRif());
						this.addCella(tableVersamenti, normalSmallFont, Element.ALIGN_RIGHT,importo.getImpTotVer().toString());
					}
					paragrafoIci.add(tableVersamenti);
					this.addEmptyLine(paragrafoIci, 1);
				}
				else
				{
					paragrafoIci.add(new Paragraph("Lista versamenti non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoIci, 1);
				}
				// END RECUPERO LISTA VERSAMENTI ICI
				
				/*
				paragrafoIci.add(new Paragraph("Situazione patrimoniale e locazioni al 31/12/"+getAnnoRiferimentoICI()+":", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoIci, 1);
				// START RECUPERO LISTA IMMOBILI
				if (listaImmobili!=null && listaImmobili.size() > 0 )
				{
					paragrafoIci.add(new Paragraph("Lista immobili", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
	
					PdfPTable tableImm = new PdfPTable(8);
					tableImm.setWidths(new float[] {10,10,20,10,10,15,15,10});
					tableImm.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableImm.setWidthPercentage(100);
					tableImm.getDefaultCell().setBorder(0);
					tableImm.getDefaultCell().setMinimumHeight(20);
					
					PdfPCell c1 = new PdfPCell(new Phrase("SEZIONE",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("F/P/S",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("CATEGORIA",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("CLASSE",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("RENDITA",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("INIZIO POSSESSO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("FINE POSSESSO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("QUOTA",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableImm.addCell(c1);
					
					for (SitPatrimImmobileDTO dto: listaImmobili)
					{					
						PdfPCell cell = new PdfPCell(new Phrase(dto.getSezione(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitImmobile().getId().getFoglio()+" / "+dto.getDatiTitImmobile().getId().getParticella()+" / "+dto.getDatiTitImmobile().getId().getUnimm(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getCategoria(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getClasse(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getRenditaF(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitImmobile().getDataInizio()!=null?sdf.format(dto.getDatiTitImmobile().getDataInizio()):"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitImmobile().getId().getDataFine()!=null?sdf.format(dto.getDatiTitImmobile().getId().getDataFine()):"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getPercPossF()+"%",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableImm.addCell(cell);
					}
					paragrafoIci.add(tableImm);
					this.addEmptyLine(paragrafoIci, 1);
				}
				else
				{
					paragrafoIci.add(new Paragraph("Lista immobili non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoIci, 1);
				}
				// END RECUPERO LISTA IMMOBILI
				
				// START RECUPERO LISTA TERRENI
				if (listaTerreni!=null && listaTerreni.size() > 0 )
				{
					paragrafoIci.add(new Paragraph("Lista terreni", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
	
					PdfPTable tableTerr = new PdfPTable(10);
					tableTerr.setWidths(new float[] {10,10,10,10,10,10,10,10,10,10});
					tableTerr.setHorizontalAlignment(Element.ALIGN_LEFT);
					tableTerr.setWidthPercentage(100);
					tableTerr.getDefaultCell().setBorder(0);
					tableTerr.getDefaultCell().setMinimumHeight(20);
					
					PdfPCell c1 = new PdfPCell(new Phrase("SEZIONE",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("F/P",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("QUALITA'",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("CLASSE",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("SUPERFICIE",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("REDDITO DOMINICALE",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("REDDITO AGRARIO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("LOCATO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("INIZIO POSSESSO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					c1 = new PdfPCell(new Phrase("FINE POSSESSO",boldSmallFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tableTerr.addCell(c1);
					
					for (SitPatrimTerrenoDTO dto: listaTerreni)
					{					
						PdfPCell cell = new PdfPCell(new Phrase(dto.getDatiTitTerreno().getSezione(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitTerreno().getFoglio()+" / "+dto.getDatiTitTerreno().getParticella(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitTerreno().getQualita()!=null?dto.getDatiTitTerreno().getQualita().toString():"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitTerreno().getClasse(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getSuperficieF(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getRedditoDominicaleF(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getRedditoAgrarioF(),normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase("-",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitTerreno().getDtIniPos()!=null?sdf.format(dto.getDatiTitTerreno().getDtIniPos()):"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
						
						cell = new PdfPCell(new Phrase(dto.getDatiTitTerreno().getDtFinPos()!=null?sdf.format(dto.getDatiTitTerreno().getDtFinPos()):"",normalSmallFont));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						tableTerr.addCell(cell);
					}
					paragrafoIci.add(tableTerr);
					this.addEmptyLine(paragrafoIci, 1);
				}
				else
				{
					paragrafoIci.add(new Paragraph("Lista terreni non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
					this.addEmptyLine(paragrafoIci, 1);
				}
				// END RECUPERO LISTA TERRENI
				
				//RECUPERO LISTA LOCAZIONI (con informazioni ICI)
				this.addLocazioniIci(paragrafoIci);
				*/
			}
			else
			{
				this.addEmptyLine(paragrafoIci, 1);			
				paragrafoIci.add(new Paragraph("Nessun dato ICI da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
			}
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafoIci, this.tribIciFonte.getDescrizione());
		
		document.add(paragrafoIci);
		document.newPage();
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	private PdfPTable insertTitoloScheda(String titolo)
	{
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setMinimumHeight(40);
		
		PdfPCell myCell = table.getDefaultCell();
		myCell.setPhrase(new Phrase(titolo,new Font(Font.FontFamily.TIMES_ROMAN, 26,Font.BOLD)));
		myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(myCell);
		
		return table;
	}
	
	private PdfPTable insertSezioneDatiAnagrafici(String cognome,String nome,Date dtNascita,String comuneNascita,
			String residenza, String codFiscale, String denominazione, String pIVA) throws DocumentException
	{
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.NORMAL);
		
		PdfPTable tableDatiSoggetto = new PdfPTable(4);
		tableDatiSoggetto.setHorizontalAlignment(Element.ALIGN_LEFT);
		tableDatiSoggetto.setWidths(new float[] {20,35,10,35});
		tableDatiSoggetto.setWidthPercentage(100);
		tableDatiSoggetto.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableDatiSoggetto.getDefaultCell().setMinimumHeight(30);

		if (soggettoCartella.getTipoSogg().equals("F"))
		{// CERCO LA PERSONA FISICA NELL'ANAGRAFE

			PdfPCell myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("COGNOME",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(cognome,normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("NOME",boldFont));					
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);

			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(nome,normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("NATO IL",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);

			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(Utility.dateToString_ddMMyyyy(dtNascita),normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("A",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(comuneNascita,normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("RESIDENZA",boldFont));					
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(residenza,normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			myCell.setColspan(3);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("CODICE FISCALE",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			myCell.setColspan(1);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(codFiscale,normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			myCell.setColspan(3);
			tableDatiSoggetto.addCell(myCell);
		}
		else
		{// PERSONA GIURIDICA 

			PdfPCell myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("DENOMINAZIONE",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(denominazione,normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			myCell.setColspan(3);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase("PARTITA IVA",boldFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			myCell.setColspan(1);
			tableDatiSoggetto.addCell(myCell);
			
			myCell = tableDatiSoggetto.getDefaultCell();
			myCell.setPhrase(new Phrase(pIVA,normalFont));
			myCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			myCell.setColspan(3);
			tableDatiSoggetto.addCell(myCell);
		}
		
		return tableDatiSoggetto;
	}

	// ************************ PDF
	
	private String formattaStringa(String strIn)
	{
		if (strIn!=null && strIn!="")
			return strIn.trim();
		else
			return "";
	}
	public void setComuneService(ComuneService comuneService) {
		this.comuneService = comuneService;
	}
	public ComuneService getComuneService() {
		return comuneService;
	}
	
	
	private void GestioneFonte()
	{
		
		GestioneFonte fonte = new GestioneFonte(comuneService, super.getFontiService(), super.getCarContribService(), super.getPdfFontiDefault()); 
		
		tribIciFonte = fonte.getDatiFonte(this.F_TRIBUTI,"ICI");
		tribTarsuFonte = fonte.getDatiFonte(this.F_TRIBUTI,"TARSU");
		cosapFonte = fonte.getDatiFonte(this.F_COSAP,null );
		cncFonte = fonte.getDatiFonte(this.F_CNC,null );
		redditiFonte = fonte.getDatiFonte(this.F_REDDITI,null );
		acquaFonte = fonte.getDatiFonte(this.F_ACQUA,null );
		multeFonte = fonte.getDatiFonte(this.F_MULTE,null );
		retteFonte = fonte.getDatiFonte(this.F_RETTE,null );
		praticheFonte = fonte.getDatiFonte(this.F_PRATICHE,null );
		nucleoFonte = fonte.getDatiFonte(this.F_DEMOGRAFIA,null );
		imuFonte = fonte.getDatiFonte(this.F_IMU,null );
		locazioniFonte = fonte.getDatiFonte(this.F_LOCAZIONI,null );
		catastoFonte = fonte.getDatiFonte(this.F_CATASTO, null);
		bolliVeicoliFonte = fonte.getDatiFonte(this.F_BOLLI_VEICOLI, null);
		
		ruoliVersCarContrib.setF24Fonte(fonte.getDatiFonte(this.F_F24,null));
		ruoliVersCarContrib.setrTaresFonte(fonte.getDatiFonte(this.F_R_TARES,null));
		ruoliVersCarContrib.setrTarsuFonte(fonte.getDatiFonte(this.F_R_TARSU,null));
		ruoliVersCarContrib.setVerIciDMFonte(fonte.getDatiFonte(this.F_VER_ICI_DM,null));
		ruoliVersCarContrib.setVerTarsuBPFonte(fonte.getDatiFonte(this.F_VER_TAR_BP,null));
		
		carsocialeFonte = new FonteDTO();
		carsocialeFonte.setAbilitata(isCarSocialeFontePermesso());
		logger.debug("CARTELLA SOCIALE = " + carsocialeFonte.isAbilitata());
	}
	

	public void resetBean(){
		setSoggettoIciDaVisualizzare(null);
		setSoggettoTarsuDaVisualizzare(null);
		setSoggettoCosapDaVisualizzare(null);
		setSoggettoRedditiDaVisualizzare(null);
		setSoggettoCNCDaVisualizzare(null);
		setSoggCartellaPrecedente(null);
		setIdRichiestaCartellaPrecedente(null);
		setIntestazione(null);
	}
	
	private boolean isCarSocialeFontePermesso() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();		
		CeTUser user = (CeTUser)request.getSession().getAttribute("user");
		if (user == null)
			return false;
		return Permessi.controlla(user, Permessi.PERMESSO_VIS_CARTELLA_SOCIALE);
	}
	
	public boolean isCbxSezionePratiche() {
		return cbxSezionePratiche;
	}
	public void setCbxSezionePratiche(boolean cbxSezionePratiche) {
		this.cbxSezionePratiche = cbxSezionePratiche;
	}
	public String getMotivoPratiche() {
		return motivoPratiche;
	}
	public void setMotivoPratiche(String motivoPratiche) {
		this.motivoPratiche = motivoPratiche;
	}
	public PratichePortaleCarContrib getPratichePortaleCarContrib() {
		return pratichePortaleCarContrib;
	}
	public void setPratichePortaleCarContrib(
			PratichePortaleCarContrib pratichePortaleCarContrib) {
		this.pratichePortaleCarContrib = pratichePortaleCarContrib;
	}
	
	public SaldoImuElaborazioneDTO getDatiElabImu() {
		return datiElabImu;
	}
	public void setDatiElabImu(SaldoImuElaborazioneDTO datiElabImu) {
		this.datiElabImu = datiElabImu;
	}
	public boolean isCbxSezioneImu() {
		return cbxSezioneImu;
	}
	public void setCbxSezioneImu(boolean cbxSezioneImu) {
		this.cbxSezioneImu = cbxSezioneImu;
	}
	public String getMotivoImu() {
		return motivoImu;
	}
	public void setMotivoImu(String motivoImu) {
		this.motivoImu = motivoImu;
	}
	public SaldoImuConsulenzaDTO getConsulenzaImu() {
		return consulenzaImu;
	}
	public void setConsulenzaImu(SaldoImuConsulenzaDTO consulenzaImu) {
		this.consulenzaImu = consulenzaImu;
	}
	public FonteDTO getTribTarsuFonte() {
		return tribTarsuFonte;
	}
	public void setTribTarsuFonte(FonteDTO tribTarsuFonte) {
		this.tribTarsuFonte = tribTarsuFonte;
	}
	public FonteDTO getTribIciFonte() {
		return tribIciFonte;
	}
	public void setTribIciFonte(FonteDTO tribIciFonte) {
		this.tribIciFonte = tribIciFonte;
	}
	public FonteDTO getCosapFonte() {
		return cosapFonte;
	}
	public void setCosapFonte(FonteDTO cosapFonte) {
		this.cosapFonte = cosapFonte;
	}
	public FonteDTO getCncFonte() {
		return cncFonte;
	}
	public void setCncFonte(FonteDTO cncFonte) {
		this.cncFonte = cncFonte;
	}
	public FonteDTO getRedditiFonte() {
		return redditiFonte;
	}
	public void setRedditiFonte(FonteDTO redditiFonte) {
		this.redditiFonte = redditiFonte;
	}
	public FonteDTO getAcquaFonte() {
		return acquaFonte;
	}
	public void setAcquaFonte(FonteDTO acquaFonte) {
		this.acquaFonte = acquaFonte;
	}
	public FonteDTO getMulteFonte() {
		return multeFonte;
	}
	public void setMulteFonte(FonteDTO multeFonte) {
		this.multeFonte = multeFonte;
	}
	public FonteDTO getRetteFonte() {
		return retteFonte;
	}
	public void setRetteFonte(FonteDTO retteFonte) {
		this.retteFonte = retteFonte;
	}
	public FonteDTO getPraticheFonte() {
		return praticheFonte;
	}
	public void setPraticheFonte(FonteDTO praticheFonte) {
		this.praticheFonte = praticheFonte;
	}
	public FonteDTO getNucleoFonte() {
		return nucleoFonte;
	}
	public void setNucleoFonte(FonteDTO nucleoFonte) {
		this.nucleoFonte = nucleoFonte;
	}
	public FonteDTO getCarsocialeFonte() {
		return carsocialeFonte;
	}
	public void setCarsocialeFonte(FonteDTO carsocialeFonte) {
		this.carsocialeFonte = carsocialeFonte;
	}
	public FonteDTO getImuFonte() {
		return imuFonte;
	}
	public void setImuFonte(FonteDTO imuFonte) {
		this.imuFonte = imuFonte;
	}
	public FonteDTO getLocazioniFonte() {
		return locazioniFonte;
	}
	public void setLocazioniFonte(FonteDTO locazioniFonte) {
		this.locazioniFonte = locazioniFonte;
	}
	public SoggettoDTO getSoggCartellaPrecedente() {
		return soggCartellaPrecedente;
	}
	public void setSoggCartellaPrecedente(SoggettoDTO soggCartellaPrecedente) {
		this.soggCartellaPrecedente = soggCartellaPrecedente;
	}
	public Long getIdRichiestaCartellaPrecedente() {
		return idRichiestaCartellaPrecedente;
	}
	public void setIdRichiestaCartellaPrecedente(Long idRichiestaCartellaPrecedente) {
		this.idRichiestaCartellaPrecedente = idRichiestaCartellaPrecedente;
	}
	public String getPadre() {
		return padre;
	}
	public void setPadre(String padre) {
		this.padre = padre;
	}
	public FonteDTO getCatastoFonte() {
		return catastoFonte;
	}
	public void setCatastoFonte(FonteDTO catastoFonte) {
		this.catastoFonte = catastoFonte;
	}
	public String getIntestazioneSituazionePatrimoniale() {
		return intestazioneSituazionePatrimoniale;
	}
	public void setIntestazioneSituazionePatrimoniale(
			String intestazioneSituazionePatrimoniale) {
		this.intestazioneSituazionePatrimoniale = intestazioneSituazionePatrimoniale;
	}
	public boolean isVisStoricoCatasto() {
		return visStoricoCatasto;
	}
	public void setVisStoricoCatasto(boolean visStoricoCatasto) {
		this.visStoricoCatasto = visStoricoCatasto;
	}
	public String getStrDataRichiesta() {
		return strDataRichiesta;
	}
	public void setStrDataRichiesta(String strDataRichiesta) {
		this.strDataRichiesta = strDataRichiesta;
	}
	public String getStrSoggettoRichiedente() {
		return strSoggettoRichiedente;
	}
	public void setStrSoggettoRichiedente(String strSoggettoRichiedente) {
		this.strSoggettoRichiedente = strSoggettoRichiedente;
	}
	public boolean isCbxSezioneIci() {
		return cbxSezioneIci;
	}
	public void setCbxSezioneIci(boolean cbxSezioneIci) {
		this.cbxSezioneIci = cbxSezioneIci;
	}
	public boolean isCbxSezioneTarsu() {
		return cbxSezioneTarsu;
	}
	public void setCbxSezioneTarsu(boolean cbxSezioneTarsu) {
		this.cbxSezioneTarsu = cbxSezioneTarsu;
	}
	public boolean isCbxSezioneCosap() {
		return cbxSezioneCosap;
	}
	public void setCbxSezioneCosap(boolean cbxSezioneCosap) {
		this.cbxSezioneCosap = cbxSezioneCosap;
	}
	public boolean isCbxSezioneRedditi() {
		return cbxSezioneRedditi;
	}
	
	public boolean isCbxSezionePatrimoniale() {
		return cbxSezionePatrimoniale;
	}
	public void setCbxSezionePatrimoniale(boolean cbxSezionePatrimoniale) {
		this.cbxSezionePatrimoniale = cbxSezionePatrimoniale;
	}
	public String getMotivoPatrimoniale() {
		return motivoPatrimoniale;
	}
	public void setMotivoPatrimoniale(String motivoPatrimoniale) {
		this.motivoPatrimoniale = motivoPatrimoniale;
	}
	public void setCbxSezioneRedditi(boolean cbxSezioneRedditi) {
		this.cbxSezioneRedditi = cbxSezioneRedditi;
	}
	public boolean isCbxSezioneCnc() {
		return cbxSezioneCnc;
	}
	public void setCbxSezioneCnc(boolean cbxSezioneCnc ) {
		this.cbxSezioneCnc = cbxSezioneCnc ;
	}
	public boolean isCbxSezioneCarSociale() {
		return cbxSezioneCarSociale;
	}
	public void setCbxSezioneCarSociale(boolean cbxSezioneCarSociale) {
		this.cbxSezioneCarSociale = cbxSezioneCarSociale;
	}
	public boolean isCbxSezioneAcqua() {
		return cbxSezioneAcqua;
	}
	public void setCbxSezioneAcqua(boolean cbxSezioneAcqua) {
		this.cbxSezioneAcqua = cbxSezioneAcqua;
	}
	public boolean isCbxSezioneRette() {
		return cbxSezioneRette;
	}
	public void setCbxSezioneRette(boolean cbxSezioneRette) {
		this.cbxSezioneRette = cbxSezioneRette;
	}
	public boolean isCbxSezioneMulte() {
		return cbxSezioneMulte;
	}
	public void setCbxSezioneMulte(boolean cbxSezioneMulte) {
		this.cbxSezioneMulte = cbxSezioneMulte;
	}
	public boolean isCbxSezioneRuoliVersamenti() {
		return cbxSezioneRuoliVersamenti;
	}
	public void setCbxSezioneRuoliVersamenti(boolean cbxSezioneRuoliVersamenti) {
		this.cbxSezioneRuoliVersamenti = cbxSezioneRuoliVersamenti;
	}
	public String getMotivoRuoliVersamenti() {
		return motivoRuoliVersamenti;
	}
	public void setMotivoRuoliVersamenti(String motivoRuoliVersamenti) {
		this.motivoRuoliVersamenti = motivoRuoliVersamenti;
	}
	public RuoliVersamentiCarContrib getRuoliVersCarContrib() {
		return ruoliVersCarContrib;
	}
	public void setRuoliVersCarContrib(RuoliVersamentiCarContrib ruoliVersCarContrib) {
		this.ruoliVersCarContrib = ruoliVersCarContrib;
	}
	public String getMotivoIci() {
		return motivoIci;
	}
	public void setMotivoIci(String motivoIci) {
		this.motivoIci = motivoIci;
	}
	public String getMotivoTarsu() {
		return motivoTarsu;
	}
	public void setMotivoTarsu(String motivoTarsu) {
		this.motivoTarsu = motivoTarsu;
	}
	public String getMotivoCosap() {
		return motivoCosap;
	}
	public void setMotivoCosap(String motivoCosap) {
		this.motivoCosap = motivoCosap;
	}
	public String getMotivoRedditi() {
		return motivoRedditi;
	}
	public void setMotivoRedditi(String motivoRedditi) {
		this.motivoRedditi = motivoRedditi;
	}
	public String getMotivoCnc() {
		return motivoCnc;
	}
	public void setMotivoCnc(String motivoCnc) {
		this.motivoCnc = motivoCnc;
	}
	public String getMotivoCarSociale() {
		return motivoCarSociale;
	}
	public void setMotivoCarSociale(String motivoCarSociale) {
		this.motivoCarSociale = motivoCarSociale;
	}
	public String getMotivoAcqua() {
		return motivoAcqua;
	}
	public void setMotivoAcqua(String motivoAcqua) {
		this.motivoAcqua = motivoAcqua;
	}
	public String getMotivoMulte() {
		return motivoMulte;
	}
	public void setMotivoMulte(String motivoMulte) {
		this.motivoMulte = motivoMulte;
	}
	public String getMotivoRette() {
		return motivoRette;
	}
	public void setMotivoRette(String motivoRette) {
		this.motivoRette = motivoRette;
	}
	
	public boolean isPreparaCartella() {
		return preparaCartella;
	}
	public void setPreparaCartella(boolean preparaCartella) {
		this.preparaCartella = preparaCartella;
	}
	public boolean getReturnToList() {
		return returnToList;
	}
	public void setReturnToList(boolean returnToList) {
		this.returnToList = returnToList;
	}
	public boolean isShowButtonProduciPdf() {
		return showButtonProduciPdf;
	}
	public void setShowButtonProduciPdf(boolean showButtonProduciPdf) {
		this.showButtonProduciPdf = showButtonProduciPdf;
	}
	public boolean isShowButtonProduciPdfPerNucleo() {
		return showButtonProduciPdfPerNucleo;
	}
	public void setShowButtonProduciPdfPerNucleo(
			boolean showButtonProduciPdfPerNucleo) {
		this.showButtonProduciPdfPerNucleo = showButtonProduciPdfPerNucleo;
	}
	public boolean isViewPDF() {
		return viewPDF;
	}
	public void setViewPDF(boolean viewPDF) {
		this.viewPDF = viewPDF;
	}

	public String getSelectedTab() {
		return selectedTab;
	}
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}
	public ComponenteFamigliaDTO getComponenteNucleo() {
		return componenteNucleo;
	}
	public void setComponenteNucleo(ComponenteFamigliaDTO componenteNucleo) {
		this.componenteNucleo = componenteNucleo;
	}

	public FonteDTO getBolliVeicoliFonte() {
		return bolliVeicoliFonte;
	}

	public void setBolliVeicoliFonte(FonteDTO bolliVeicoliFonte) {
		this.bolliVeicoliFonte = bolliVeicoliFonte;
	}

	public List<BolloVeicolo> getListaBolliVeicoli() {
		return listaBolliVeicoli;
	}

	public void setListaBolliVeicoli(List<BolloVeicolo> listaBolliVeicoli) {
		this.listaBolliVeicoli = listaBolliVeicoli;
	}
}
