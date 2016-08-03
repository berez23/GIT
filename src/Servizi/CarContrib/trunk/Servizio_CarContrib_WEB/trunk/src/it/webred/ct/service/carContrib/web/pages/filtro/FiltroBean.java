package it.webred.ct.service.carContrib.web.pages.filtro;

import it.webred.ct.service.carContrib.data.access.cc.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;
import it.webred.ct.service.carContrib.data.access.cc.dto.RichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RisposteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.SoggettiCarContribDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.ParamAccessoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.service.carContrib.data.model.Risposte;
import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.service.carContrib.web.CarContribBaseBean;
import it.webred.ct.service.carContrib.web.Utility;
import it.webred.ct.service.carContrib.web.beans.FiltroRichieste;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.utils.UtilityScambioPortale;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.net.ftp.FTPClient;

public class FiltroBean extends CarContribBaseBean{

	private FiltroRichieste richiestaSelezionata;
	private Risposte rispostaEvasa;
	
	private String msgErrore;
	
	private String nomePdf;
	
	private Date dataEvasione;
	private String operatore;
	private String mezzoRisposta;
	private String notaOperatore;
	private String testoRisposta;
	private boolean inviaPerEmail;
	private boolean showEmailCheck;
	private boolean disabilitaEmailCheck;
	
	public void setRichiestaSelezionata(FiltroRichieste richiestaSelezionata) {
		this.richiestaSelezionata = richiestaSelezionata;
	}
	public FiltroRichieste getRichiestaSelezionata() {
		return richiestaSelezionata;
	}
	public void setRispostaEvasa(Risposte rispostaEvasa) {
		this.rispostaEvasa = rispostaEvasa;
	}
	public Risposte getRispostaEvasa() {
		return rispostaEvasa;
	}
	public String getMsgErrore() {
		return msgErrore;
	}
	public void setNomePdf(String nomePdf) {
		this.nomePdf = nomePdf;
	}
	public String getNomePdf() {
		return nomePdf;
	}
	public Date getDataEvasione() {
		return dataEvasione;
	}
	public void setDataEvasione(Date dataEvasione) {
		this.dataEvasione = dataEvasione;
	}
	public String getOperatore() {
		return operatore;
	}
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
	public String getMezzoRisposta() {
		return mezzoRisposta;
	}
	public void setMezzoRisposta(String mezzoRisposta) {
		this.mezzoRisposta = mezzoRisposta;
	}
	public String getNotaOperatore() {
		return notaOperatore;
	}
	public void setNotaOperatore(String notaOperatore) {
		this.notaOperatore = notaOperatore;
	}
	public String getTestoRisposta() {
		return testoRisposta;
	}
	public void setTestoRisposta(String testoRisposta) {
		this.testoRisposta = testoRisposta;
	}
	public boolean isInviaPerEmail() {
		return inviaPerEmail;
	}
	public void setInviaPerEmail(boolean inviaPerEmail) {
		this.inviaPerEmail = inviaPerEmail;
	}
	public void setShowEmailCheck(boolean showEmailCheck) {
		this.showEmailCheck = showEmailCheck;
	}
	public boolean isShowEmailCheck() {
		return showEmailCheck;
	}
	
	
	public boolean isDisabilitaEmailCheck() {
		return disabilitaEmailCheck;
	}
	public void setDisabilitaEmailCheck(boolean disabilitaEmailCheck) {
		this.disabilitaEmailCheck = disabilitaEmailCheck;
	}
	public List<SelectItem> getCodiciRisposta() {
		
		List<SelectItem> codiciRisposta =  new ArrayList<SelectItem>();
		
		CeTBaseObject cetObj = new CeTBaseObject();
		cetObj.setEnteId(super.getUserBean().getEnteID());
		cetObj.setUserId(super.getUserBean().getUsername());
		
		List<CodiciTipoMezzoRisposta> lista  =  super.getCarContribService().getListaCodiciRisp(cetObj);
		
		for(CodiciTipoMezzoRisposta c:lista)
		{
			SelectItem s = new SelectItem(c.getCodice(), c.getDescr());
			codiciRisposta.add(s);
		}
		
		return codiciRisposta;
	}

	public String getDescMezzoRisposta() {
		
		//logger.info("rispostaEvasa.getCodTipMezRis() = " + rispostaEvasa.getCodTipMezRis());
		
		if(rispostaEvasa.getCodTipMezRis()==null || rispostaEvasa.getCodTipMezRis().equals("0") || rispostaEvasa.getCodTipMezRis().equals(""))
			return "-";
		
		CodiciTipoMezzoRispostaDTO codiceDTO = new CodiciTipoMezzoRispostaDTO();
		CodiciTipoMezzoRisposta c = new CodiciTipoMezzoRisposta();
		c.setCodice(rispostaEvasa.getCodTipMezRis());
		
		codiceDTO.setCodice(c);
		codiceDTO.setEnteId(super.getUserBean().getEnteID());
		codiceDTO.setUserId(super.getUserBean().getUsername());
		
		CodiciTipoMezzoRisposta cod =super.getCarContribService().getDescCodiciRisp(codiceDTO);
		if (cod==null) 
			return "-";
		else
			return cod.getDescr();
	}

	public String goFiltroRichieste()
	{
		richiestaSelezionata = new FiltroRichieste();
		rispostaEvasa = new Risposte();
		msgErrore = "";
		nomePdf = "";
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		FiltroDataModel filtro=(FiltroDataModel)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "filtroDataModel");
		filtro.setCriteria(new FiltroRichiesteSearchCriteria());
		filtro.controllaPermessi();
		filtro.setShowTableRusult(false);
		
		return "goFiltroRichieste";
	}
	
	public String returnListaRicerca()
	{
		return "goFiltroRichieste";
	}
	
	public String SelectRichiesta()
	{
		if (richiestaSelezionata!=null && richiestaSelezionata.getRichiesta()!=null)
		{
			if (richiestaSelezionata.getRichiesta().getIdSoggettoRichiedente()!=null 
					&& richiestaSelezionata.getRichiesta().getIdSoggettoRichiedente()!="")
			{
				SoggettiCarContribDTO soggCartDTO = new SoggettiCarContribDTO();
				
				SoggettiCarContrib soggetto = new SoggettiCarContrib();
				soggetto.setIdSogg(new Long(richiestaSelezionata.getRichiesta().getIdSoggettoRichiedente()));
				
				soggCartDTO.setSogg(soggetto);
				soggCartDTO.setEnteId(super.getUserBean().getEnteID());
				soggCartDTO.setUserId(super.getUserBean().getUsername());	
				
				richiestaSelezionata.getRichiesta().setSoggettoRichiedente(super.getCarContribService().getSoggetto(soggCartDTO));
			}
			if (richiestaSelezionata.getRichiesta().getRichEvasa().equals("1"))
			{// RICHIESTA EVASA
				RichiesteDTO richiestaDTO = new RichiesteDTO();
				
				Richieste richiesta = new Richieste();
				richiesta.setIdRic(new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()));
				
				richiestaDTO.setRich(richiesta);
				richiestaDTO.setEnteId(super.getUserBean().getEnteID());
				richiestaDTO.setUserId(super.getUserBean().getUsername());
				
				rispostaEvasa =  super.getCarContribService().getRisposta(richiestaDTO);
				
				if(richiestaSelezionata.getRichiesta().getCodTipMezRis() != null){
					mezzoRisposta = richiestaSelezionata.getRichiesta().getCodTipMezRis();
					abilitaEmail();
				}
				
				msgErrore = "";
				return "goCartellaEvasa";
			}
			else
			{// RICHIESTA NON EVASA
				dataEvasione = new Date();
				notaOperatore = richiestaSelezionata.getRichiesta().getNote();
				testoRisposta = "";
				nomePdf = richiestaSelezionata.getRichiesta().getNomePdf();
				if (richiestaSelezionata.getRichiesta().getCodTipMezRis()!= null)
					mezzoRisposta = richiestaSelezionata.getRichiesta().getCodTipMezRis();
				else
					mezzoRisposta="";
				
				inviaPerEmail =false;
			    showEmailCheck=Utility.isValidEmailAddress(richiestaSelezionata.getRichiesta().getEmail());
			    abilitaEmail();
				msgErrore = "";
				return "goCartellaNonEvasa";			
			}
		}
		else
			return null;
	}
	
	public String ReturnRichiesta()
	{// RICHIESTA NON EVASA
		
		RichiesteDTO richiestaDTO = new RichiesteDTO();
		
		Richieste richiesta = new Richieste();
		richiesta.setIdRic(new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()));
		
		richiestaDTO.setRich(richiesta);
		richiestaDTO.setEnteId(super.getUserBean().getEnteID());
		richiestaDTO.setUserId(super.getUserBean().getUsername());
		
		Richieste r =  super.getCarContribService().getRichiesta(richiestaDTO);
		
		dataEvasione = new Date();
		notaOperatore = r.getDesNotRic();
		testoRisposta = "";
		nomePdf = r.getNomePdf();
		msgErrore = "";
		
		
		return "goCartellaNonEvasa";			
	}

	public void ApriCartella()
	{
		msgErrore = "";
		
		ParamAccessoDTO parms =  new ParamAccessoDTO();
		
		parms.setTipoSogg(richiestaSelezionata.getRichiesta().getCodTipoSogg());
		parms.setNome(richiestaSelezionata.getRichiesta().getNome());
		parms.setCognome(richiestaSelezionata.getRichiesta().getCognome());			
		parms.setCodFis(richiestaSelezionata.getRichiesta().getCodFiscale());
		parms.setDtNas(richiestaSelezionata.getRichiesta().getDataNascita());
		parms.setParIva(richiestaSelezionata.getRichiesta().getPartIva());
		parms.setDenom(richiestaSelezionata.getRichiesta().getDenominazione());
		parms.setEnteId(super.getUserBean().getEnteID());
		parms.setUserId(super.getUserBean().getUsername());
		
		Set <SoggettoDTO> listaSoggetti = super.getGeneralService().searchSoggetto(parms);
		
		if (listaSoggetti.isEmpty())
			 msgErrore = "DATI INCONGRUENTI";
		else
			super.LoadSoggettoInContribuenteBean(listaSoggetti.iterator().next(),new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()),mezzoRisposta,false,Utility.TipoBeanPadre.CHIUDI, false, richiestaSelezionata.getRichiesta().getStorico());
	}

	public void MostraPdf() throws IOException
	{
		Utility.ShowFile(super.getPathFilesPdf() + "//" + nomePdf + ".pdf");
	}
	
	public String CancellaPdf()
	{
		String pathPdf = super.getPathFilesPdf() + "//" + nomePdf + ".pdf";
		Utility.DeleteFile(pathPdf);
		
		this.updateNamePdfFile("");
		
		nomePdf = null;
		msgErrore = "";
		
		return "goCartellaNonEvasa";
	}
	
	public String RespingiRichiesta() throws Exception
	{
		if (testoRisposta==null||testoRisposta.equals(""))
		{
			msgErrore= " Testo risposta obbligatoria";
			return "goCartellaNonEvasa";
		}
		msgErrore = "";
		
		RichiesteDTO richiestaDTO = new RichiesteDTO();
		
		Richieste richiesta = new Richieste();
		richiesta.setIdRic(new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()));
		
		richiestaDTO.setRich(richiesta);
		richiestaDTO.setEnteId(super.getUserBean().getEnteID());
		richiestaDTO.setUserId(super.getUserBean().getUsername());

		if(nomePdf!=null && nomePdf!="")
		{
	      	RichiesteDTO richDTO = new RichiesteDTO();
	      	Richieste rich = new Richieste();
	      	rich.setIdRic(new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()));
	      	rich.setNomePdf(null);
	      	
	      	richDTO.setRich(rich);
	      	richDTO.setEnteId(super.getUserBean().getEnteID());
	      	richDTO.setUserId(super.getUserBean().getUsername());
	      	
			super.getCarContribService().updateFilePdfRichiesta(richDTO);
			
			String pathPdf= super.getPathFilesPdf() + "//" + nomePdf + ".pdf";
			File pdf = new File(pathPdf);
			if(pdf.delete()) logger.info(" CANCELLAZIONE PDF ESEGUITA ");
			
			nomePdf=null;
		}
		
		this.insertRichiesta(true,"40","Richiesta respinta",notaOperatore);
		
		if(inviaPerEmail)
		{// invio email con "Richiesta Cartella Contribuente n. xxxx RESPINTA" e nota operatore
			this.sendEmailRespinta();
		}
		
		super.addInfoMessage("cc.filtro.richiestarespinta");
		return "goCartellaEvasa";
	}
	
	public String EvadiRichiesta() throws Exception
	{
		logger.info("in EvadiRichiesta() - MEZZO RISPOSTA: " + mezzoRisposta);
		if ((nomePdf==null||nomePdf.equals("")) &&
			(testoRisposta.equals("")))
		{
			msgErrore= " File pdf e/o Testo Risposta obbligatori";
			return "goCartellaNonEvasa";
		}
		
		if(mezzoRisposta.equals("0"))
		{// mezzo risposta non selezionato
			logger.info("Mezzo risposta non selezionato.");
			msgErrore = "Mezzo risposta non selezionato.";
			return "goCartellaNonEvasa";
		}
		
		try {
		
			msgErrore = "";
			String macroStato = "";String descStato = "";
			switch (new Integer(mezzoRisposta)){
				case 118:
				{
					macroStato = "60";
					descStato = "Pronta per consegna";
					break;
				}
				case 119:
				{
					macroStato = "70";
					descStato = "Evasa";
					break;
				}
				case 120:
				{
					macroStato = "70";
					descStato = "Evasa";
					break;
				}
				case 122:
				{
					macroStato = "70";
					descStato = "Evasa";
					break;
				}
				case 121:
				{
					macroStato = "80";
					descStato = "Spedita";
					break;
				}			
			}
			
			this.insertRichiesta(false,macroStato,descStato,testoRisposta);
			
			boolean emailInviata =false;
			
			if (mezzoRisposta.equals("121"))
			{// MEZZO RISPOSTA == Email --> invio email + allegato
				this.sendEmail("Testo risposta :<br />" + rispostaEvasa.getDesRis(),true);
				emailInviata = true;
			}
			else
			{
				if (richiestaSelezionata.getRichiesta().getTipoProvenienza().equals("W"))
				{// RICHIESTA DA PORTALE
					switch (new Integer(mezzoRisposta)){
						case 118:
						{// Ritiro di persona 
							// Invia una email senza allegato avvertendo che la cartella è pronta per la consegna 
							// NON INVIA LA CARTELLA CONTRIBUENTE in allegato
							this.sendEmail("Testo risposta :<br /> Cartella pronta per la consegna.", false);
							emailInviata = true;					
							break;
						}
						case 119:
						case 120:
						{// Ritiro Tramite messo comunale o Tramite posta ordinaria 
							// Invia una email senza allegato avvertendo che la cartella è stata prodotta e verrà spedita con <modalità richiesta> al più presto 
							// NON INVIA LA CARTELLA CONTRIBUENTE in allegato
							String testo = "Testo risposta :<br /> La cartella è stata prodotta e ";
							CodiciTipoMezzoRispostaDTO codiceDTO = new CodiciTipoMezzoRispostaDTO();
							CodiciTipoMezzoRisposta c = new CodiciTipoMezzoRisposta();
							c.setCodice(mezzoRisposta);
							
							codiceDTO.setCodice(c);
							codiceDTO.setEnteId(super.getUserBean().getEnteID());
							codiceDTO.setUserId(super.getUserBean().getUsername());
							
							CodiciTipoMezzoRisposta cod =super.getCarContribService().getDescCodiciRisp(codiceDTO);
							if (cod==null) 
								testo = " Sarà spedita quanto prima.";
							else
								testo = " Sarà spedita tramite " + cod.getDescr() + " quanto prima.";
							
							this.sendEmail(testo, false);
							emailInviata = true;
							break;
						}
						case 122:
						{// Ritiro da portale
							// Nessuna email viene inviata
							// Il sistema avverte l'operatore che ha evaso la richiesta  che è necessario caricare il pdf prodotto sul portale
							/*
							if (nomePdf!=null && !nomePdf.equals(""))
								msgErrore="E' necessario caricare il file pdf della cartella contribuente sul portale.";
							*/
							break;
						}			
					}				
				}
				if (!emailInviata && inviaPerEmail)
				{ //se non si è ancora inviata l'email e si seleziona checkbox "INVIA PER E-MAIL" --> invio email + allegato
					String testoRisposta="";
					if (rispostaEvasa.getDesRis() != null && ! rispostaEvasa.getDesRis().equals(""))
						testoRisposta = rispostaEvasa.getDesRis();
					this.sendEmail(this.testoRisposta,true);
				}
			}
		
		}catch(Throwable t) {
			super.addErrorMessage("cc.errore","");	
			t.printStackTrace();
			return "filtro.richiesta.fasNonEvaso";
		}
		
		super.addInfoMessage("cc.filtro.richiestaevasa");
		return "goCartellaEvasa";
	}
	
	private void insertRichiesta(boolean respinto,String macroStato,String descStato, String testoRisposta) throws Exception
	{
		RisposteDTO rispDTO = new RisposteDTO();
		
		Risposte risp = new Risposte();
		risp.setUserName(super.getUserBean().getUsername());
		risp.setIdRic(new Long (richiestaSelezionata.getRichiesta().getIdRichiesta()));
		risp.setDtRis(dataEvasione);
		risp.setDesRis(testoRisposta);
		risp.setDesNotUser(notaOperatore);
		risp.setCodTipMezRis(mezzoRisposta);
		if (respinto)
			risp.setRespinto("1");
		else
			risp.setRespinto("0");
		
		rispDTO.setRisp(risp);
		rispDTO.setUserId(super.getUserBean().getUsername());
		rispDTO.setEnteId(super.getUserBean().getEnteID());
		
		super.getCarContribService().insertRisposta(rispDTO);
	
		RichiesteDTO richiestaDTO = new RichiesteDTO();
		
		Richieste richiesta = new Richieste();
		richiesta.setIdRic(new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()));
		
		richiestaDTO.setRich(richiesta);
		richiestaDTO.setEnteId(super.getUserBean().getEnteID());
		richiestaDTO.setUserId(super.getUserBean().getUsername());
		
		rispostaEvasa =  super.getCarContribService().getRisposta(richiestaDTO);

		richiestaSelezionata.getRichiesta().setNomePdf(nomePdf);
				
		if (richiestaSelezionata.getRichiesta().getTipoProvenienza().equals("W"))
		{//CAMBIO DI STATO NEL FILE XML DEL PORTALE
			// sia in caso di evasa che di respinta
			String nomefileStato = richiestaSelezionata.getRichiesta().getNumeroProtocollo()+"_change_stato.xml";
			String nomefileLockStato = richiestaSelezionata.getRichiesta().getNumeroProtocollo()+"_change_stato.lck";
			Long idPratica=new Long(richiestaSelezionata.getRichiesta().getNumeroProtocollo());
			Long idRichiesta=new Long(richiestaSelezionata.getRichiesta().getIdRichiesta());
			String ftpHost = super.getHostFtpScambioPortale();
			String ftpUser = super.getUserFtpScambioPortale();
			String ftpPwd = super.getPwdFtpScambioPortale();
			String ftpDir = super.getDirFtpScambioPortale();
			
			if (ftpHost!=null && !ftpHost.equals(""))
			{
				FTPClient ftpClient = UtilityScambioPortale.openConnectionFTP(ftpHost, ftpUser, ftpPwd);
				
				if (ftpClient==null || !ftpClient.isConnected())
					logger.info("Impossibile connettersi al sito ftp");
				else
				{
					if(ftpClient.changeWorkingDirectory(ftpDir))
					{
						UtilityScambioPortale.writeXmlFileChangeStato(ftpClient, ftpDir + "/stati", nomefileStato, nomefileLockStato, 
								idPratica, idRichiesta, macroStato,"", descStato, testoRisposta);
						//Per mezzo risposta=ritiro tramite portale --> si fa l'upload del pdf sul portale 
						if (nomePdf!=null && !nomePdf.equals("")) {
							logger.info("Chiamata per UPLOAD FTP file pdf");
							String pathPdf= super.getPathFilesPdf() + "//" + nomePdf + ".pdf";
							File filePdf = new File(pathPdf);
							String nomefilePdfPortale = richiestaSelezionata.getRichiesta().getNumeroProtocollo()+ ".pdf";
							ftpClient.changeWorkingDirectory(ftpDir+ "/certificati");
							UtilityScambioPortale.writePdfCartella(ftpClient,filePdf, nomefilePdfPortale);
						}
							
					}
					else
						logger.info(" Directory FTP non present");
					
					UtilityScambioPortale.closeConnectionFTP(ftpClient);
				}
			}
			else
				logger.info(" Host FTP non definito");
		}
	}
	
	private void sendEmail(String testo, boolean sendAttach)
	{
		try
		{
			String idRichiesta = richiestaSelezionata.getRichiesta().getIdRichiesta();
			
			RichiesteDTO richiestaDTO = new RichiesteDTO();

			Richieste r = new Richieste();
			r.setIdRic(new Long(idRichiesta));
			
			richiestaDTO.setRich(r);
			richiestaDTO.setEnteId(super.getUserBean().getEnteID());
			richiestaDTO.setUserId(super.getUserBean().getUsername());
			Richieste rich =  super.getCarContribService().getRichiesta(richiestaDTO);
			
			if (rich!=null && r.getEMail()!="")
			{
				Properties properties = new Properties();
				properties.put("mail.smtp.host", super.getMailServer());
				properties.put("mail.smtp.port", super.getPortMailServer());
				Session session = Session.getDefaultInstance(properties, null);
				
				MimeMessage message = new MimeMessage(session);
	 			Multipart multipart = new MimeMultipart();
	 			
				message.setFrom(new InternetAddress(super.getEmailFrom()));
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(rich.getEMail()));
				message.setSubject("Cartella contribuente Richiesta numero " + (rich.getCodTipProven().equals("W")?rich.getNumProt():rich.getIdRic()));
				message.setSentDate(new Date());
				
				MimeBodyPart messagePart = new MimeBodyPart();
				
				String testoMail = " Risposta alla vostra richiesta cartella contribuente effettuata il " + Utility.dateToString_ddMMyyyy(rich.getDtRic()) + " riferita a :<br />";
				
				SoggettiCarContribDTO soggCartDTO = new SoggettiCarContribDTO();
				
				SoggettiCarContrib soggetto = new SoggettiCarContrib();
				soggetto.setIdSogg(rich.getIdSoggCar());
				
				soggCartDTO.setSogg(soggetto);
				soggCartDTO.setEnteId(super.getUserBean().getEnteID());
				soggCartDTO.setUserId(super.getUserBean().getUsername());
				
				SoggettiCarContrib sogg = super.getCarContribService().getSoggetto(soggCartDTO);
				
				if (sogg!=null)
				{
					if(sogg.getCodTipSogg().equals("F"))
						testoMail += sogg.getCognome() + " " + sogg.getNome() + " codice fiscale " + sogg.getCodFis();
					else
						testoMail += sogg.getDenomSoc() + " partita IVA " + sogg.getParIva();
					
					testoMail += "<br />";
				}
				
				if(rich.getIdSoggRic()!=null)
				{
					SoggettiCarContribDTO soggettoCartDTO = new SoggettiCarContribDTO();
					
					SoggettiCarContrib sogg1 = new SoggettiCarContrib();
					sogg1.setIdSogg(rich.getIdSoggRic());
					
					soggettoCartDTO.setSogg(sogg1);
					soggettoCartDTO.setEnteId(super.getUserBean().getEnteID());
					soggettoCartDTO.setUserId(super.getUserBean().getUsername());
					
					SoggettiCarContrib soggRich = super.getCarContribService().getSoggetto(soggettoCartDTO);
					if (soggRich!=null)
					{
						//testoMail += " Effettuata da :<br />" + rich.getUserNameRic();
						testoMail += " Effettuata da :<br />";
						testoMail += soggRich.getCognome() + " " + soggRich.getNome() + " codice fiscale " + soggRich.getCodFis();
					}
				}
				
				testoMail += "<br /><br /> ";
				testoMail += testo;
				messagePart.setContent(testoMail, "text/html");
				
				if (sendAttach)
				{
					// Set the email attachment file
					MimeBodyPart attachmentPart = new MimeBodyPart();
					String pathFile = super.getPathFilesPdf() + "//"  + rich.getNomePdf() + ".pdf";
					if (pathFile!=null && pathFile!="")
					{
				        File tempPath = new File(pathFile);
				        if(tempPath.exists()) 
				        {
				            FileDataSource fds = new FileDataSource(pathFile);
				            attachmentPart.setDataHandler(new DataHandler(fds));
				            attachmentPart.setFileName(fds.getName());		  
							// Add attachment file
				            multipart.addBodyPart(attachmentPart);
				        }
					}
				}
				multipart.addBodyPart(messagePart);
				message.setContent(multipart);
				
				Transport.send(message);
			}
		}
		catch (MessagingException e) {
			super.addErrorMessage("cc.sendMail", null);
			e.printStackTrace();
		}		
		catch (Exception ex)
		{
		}
	}

	private void sendEmailRespinta()
	{
		try
		{   
			String idRichiesta = richiestaSelezionata.getRichiesta().getIdRichiesta();
			String numProt = richiestaSelezionata.getRichiesta().getNumeroProtocollo();
			String provenienza = richiestaSelezionata.getRichiesta().getTipoProvenienza();
		
			RichiesteDTO richiestaDTO = new RichiesteDTO();

			Richieste r = new Richieste();
			r.setIdRic(new Long(idRichiesta));
			
			richiestaDTO.setRich(r);
			richiestaDTO.setEnteId(super.getUserBean().getEnteID());
			richiestaDTO.setUserId(super.getUserBean().getUsername());
			Richieste rich =  super.getCarContribService().getRichiesta(richiestaDTO);
			
			if (rich!=null && r.getEMail()!="")
			{
				Properties properties = new Properties();
				properties.put("mail.smtp.host", super.getMailServer());
				properties.put("mail.smtp.port", super.getPortMailServer());
				Session session = Session.getDefaultInstance(properties, null);
				
				MimeMessage message = new MimeMessage(session);
	 			Multipart multipart = new MimeMultipart();
	 			
				message.setFrom(new InternetAddress(super.getEmailFrom()));
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(rich.getEMail()));
				message.setSubject("Cartella contribuente Richiesta numero " + (provenienza.equals("W")?numProt:idRichiesta));
				message.setSentDate(new Date());
				
				MimeBodyPart messagePart = new MimeBodyPart();
				
				String testoMail = " Richiesta cartella contribuente n. " + (provenienza.equals("W")?numProt:idRichiesta) + " RESPINTA <br />";
				testoMail += "<br /> Note risposta :<br />" + notaOperatore;
				messagePart.setContent(testoMail, "text/html");
				multipart.addBodyPart(messagePart);
				message.setContent(multipart);
				
				Transport.send(message);
			}
		}
		catch (MessagingException e) {
			super.addErrorMessage("cc.sendMail", null);
			e.printStackTrace();
		}		
		catch (Exception ex)
		{
		}
	}
	
	private void updateNamePdfFile(String nomeFile)
	{
      	RichiesteDTO richDTO = new RichiesteDTO();
      	Richieste rich = new Richieste();
      	rich.setIdRic(new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()));
      	rich.setNomePdf(nomeFile);
      	
      	richDTO.setRich(rich);
      	richDTO.setEnteId(super.getUserBean().getEnteID());
      	richDTO.setUserId(super.getUserBean().getUsername());
      	
      	super.getCarContribService().updateFilePdfRichiesta(richDTO);
	}
	
	public void abilitaEmail() {
		disabilitaEmailCheck = false;
        if (mezzoRisposta.equals("121"))
        	disabilitaEmailCheck=true;
          
    }

	
}
