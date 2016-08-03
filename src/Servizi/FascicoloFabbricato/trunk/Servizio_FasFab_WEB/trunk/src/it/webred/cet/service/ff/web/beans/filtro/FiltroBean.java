package it.webred.cet.service.ff.web.beans.filtro;

import it.webred.cet.permission.CeTUser;
import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.cet.service.ff.web.UserBean;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.service.ff.data.access.common.FFCommonService;
import it.webred.ct.service.ff.data.access.common.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.access.risposte.GestRispostaService;
import it.webred.ct.service.ff.data.access.risposte.dto.RispostaDTO;
import it.webred.ct.service.ff.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFRisposte;
import it.webred.ct.service.ff.data.model.FFSoggetti;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.utils.UtilityScambioPortale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
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


public class FiltroBean extends FFBaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum TipoMezzoRisposta
	{
		EMAIL,
		CONSEGNA_A_MANO,
		RITIRO_PORTALE
	}
	
	private FFCommonService commonService;
	private GestRichiestaService richiestaService;
	private GestRispostaService rispostaService;
	private ParameterService parameterService;

	private FiltroRichieste richiestaSelezionata;
	private FFRisposte rispostaEvasa;	
	
	private List<FiltroRichieste> listaFiltro;

	private String nomePdf;
	private Date dataEvasione;
	private String operatore;
	private String mezzoRisposta;
	private String notaOperatore;
	private String testoRisposta;
	private boolean inviaPerEmail;
	private boolean showEmailCheck;
	private boolean disabilitaEmailCheck;
	
	private String idRichiesta;
	
	public FFCommonService getCommonService() {
		return commonService;
	}
	public void setCommonService(FFCommonService commonService) {
		this.commonService = commonService;
	}
	public GestRichiestaService getRichiestaService() {
		return richiestaService;
	}
	public void setRichiestaService(GestRichiestaService richiestaService) {
		this.richiestaService = richiestaService;
	}
	public GestRispostaService getRispostaService() {
		return rispostaService;
	}
	public void setRispostaService(GestRispostaService rispostaService) {
		this.rispostaService = rispostaService;
	}
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	public ParameterService getParameterService() {
		return parameterService;
	}
	public String getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public FiltroRichieste getRichiestaSelezionata() {
		return richiestaSelezionata;
	}
	public void setRichiestaSelezionata(FiltroRichieste richiestaSelezionata) {
		this.richiestaSelezionata = richiestaSelezionata;
	}
	public FFRisposte getRispostaEvasa() {
		return rispostaEvasa;
	}
	public void setRispostaEvasa(FFRisposte rispostaEvasa) {
		this.rispostaEvasa = rispostaEvasa;
	}
	public List<FiltroRichieste> getListaFiltro() {
		return listaFiltro;
	}
	public void setListaFiltro(List<FiltroRichieste> listaFiltro) {
		this.listaFiltro = listaFiltro;
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
	public String getMezzoRisposta() {
		return mezzoRisposta;
	}
	public void setMezzoRisposta(String mezzoRisposta) {
		this.mezzoRisposta = mezzoRisposta;
	}
	public boolean isInviaPerEmail() {
		return inviaPerEmail;
	}
	public void setInviaPerEmail(boolean inviaPerEmail) {
		this.inviaPerEmail = inviaPerEmail;
	}
	public boolean isShowEmailCheck() {
		return showEmailCheck;
	}
	public void setShowEmailCheck(boolean showEmailCheck) {
		this.showEmailCheck = showEmailCheck;
	}
	public boolean isDisabilitaEmailCheck() {
		return disabilitaEmailCheck;
	}
	public void setDisabilitaEmailCheck(boolean disabilitaEmailCheck) {
		this.disabilitaEmailCheck = disabilitaEmailCheck;
	}
	public void setNomePdf(String nomePdf) {
		this.nomePdf = nomePdf;
		logger.debug("PDF ["+nomePdf+"]");
	}
	public String getNomePdf() {
		return nomePdf;
	}
	public String doFiltroRichieste() throws FileNotFoundException, IOException
	{
		return "filtro.richiesta.filtro";
	}

	public String doSelectRichiesta()
	{
		if (richiestaSelezionata!=null && richiestaSelezionata.getRichiesta()!=null)
		{
			if (richiestaSelezionata.getRichiesta().getIdSoggettoRichiedente()!=null 
					&& richiestaSelezionata.getRichiesta().getIdSoggettoRichiedente()!="")
			{				
				RichiestaDTO richiestaDTO = new RichiestaDTO();
				
				FFSoggetti sogge = new FFSoggetti();
				sogge.setIdSogg(new Long(richiestaSelezionata.getRichiesta().getIdSoggettoRichiedente()));
				
				richiestaDTO.setSoggetto(sogge);
				richiestaDTO.setEnteId(super.getEnte());
				richiestaDTO.setUserId(super.getUsername());
				richiestaSelezionata.getRichiesta().setSoggettoRichiedente(richiestaService.getSoggetto(richiestaDTO));
			}
			if (richiestaSelezionata.getRichiesta().getRichEvasa().equals("1"))
			{// RICHIESTA EVASA
				
				RispostaDTO rispDTO = new RispostaDTO();
				FFRisposte risp = new FFRisposte();
				risp.setIdRic(new BigDecimal(richiestaSelezionata.getRichiesta().getIdRichiesta()));
				
				rispDTO.setRisposta(risp);
				rispDTO.setEnteId(super.getEnte());
				rispDTO.setUserId(super.getUsername());
				
				rispostaEvasa =  rispostaService.getRispostaByIdRichiesta(rispDTO);
				if(richiestaSelezionata.getRichiesta().getCodTipoMezzoRisposta() != null){
					mezzoRisposta = richiestaSelezionata.getRichiesta().getCodTipoMezzoRisposta();
					abilitaEmail();
				}
				
				return "filtro.richiesta.fasEvaso";
			}
			else
			{// RICHIESTA NON EVASA
				dataEvasione = new Date();
				notaOperatore = richiestaSelezionata.getRichiesta().getNote();
				testoRisposta = "";
				nomePdf= richiestaSelezionata.getRichiesta().getNomePdf();
				inviaPerEmail =false;
				
				showEmailCheck=true;//isValidEmailAddress(richiestaSelezionata.getRichiesta().getEmail());
				if(richiestaSelezionata.getRichiesta().getCodTipoMezzoRisposta() != null){
					mezzoRisposta = richiestaSelezionata.getRichiesta().getCodTipoMezzoRisposta();
					abilitaEmail();
				}
				
				return "filtro.richiesta.fasNonEvaso";			
			}
			
		}
		else
			return "filtro.richiesta.filtro";
	}
	
	public void doApriCartella()
	{
		
	}
	
	public List<SelectItem> getCodiciRisposta() {
		
		List<SelectItem> codiciRisposta =  new ArrayList<SelectItem>();
		
		CeTBaseObject cetObj = new CeTBaseObject();
		cetObj.setEnteId(super.getEnte());
		cetObj.setUserId(super.getUsername());
		
		List<CodiciTipoMezzoRisposta> lista  =  commonService.getListaCodiciRisp(cetObj);
		
		for(CodiciTipoMezzoRisposta c:lista)
		{
			SelectItem s = new SelectItem(c.getCodice(), c.getDescr());
			codiciRisposta.add(s);
		}
		
		return codiciRisposta;
	}
	
	public String getDescMezzoRisposta() {
		
		//logger.debug("rispostaEvasa.getCodTipMezRis() = " + rispostaEvasa.getCodTipMezRis());
		
		if(rispostaEvasa.getCodTipMezRis()==null || rispostaEvasa.getCodTipMezRis().equals("0") || rispostaEvasa.getCodTipMezRis().equals(""))
			return "-";
		
		CodiciTipoMezzoRispostaDTO codiceDTO = new CodiciTipoMezzoRispostaDTO();
		CodiciTipoMezzoRisposta c = new CodiciTipoMezzoRisposta();
		c.setCodice(rispostaEvasa.getCodTipMezRis());
		
		codiceDTO.setCodice(c);
		codiceDTO.setEnteId(super.getEnte());
		codiceDTO.setUserId(super.getUsername());
		
		CodiciTipoMezzoRisposta cod = commonService.getDescCodiciRisp(codiceDTO);
		if (cod==null) 
			return "-";
		else
			return cod.getDescr();
	}
	
	public List<SelectItem> getUsersNames() {
		
		UserBean u = new UserBean();
		CeTUser user =u.getUser();

		List<SelectItem> userNameSelect =  new ArrayList<SelectItem>();
		
		CeTBaseObject cetObj = new CeTBaseObject();
		cetObj.setEnteId(user.getCurrentEnte());
		cetObj.setUserId(user.getUsername());

		List<String> listaUserName  = commonService.getDistinctUserName(cetObj);

		for(String userName: listaUserName)
		{
			userNameSelect.add(new SelectItem(userName, userName));
		}
		
		return userNameSelect;
	}
	
	public String respingiRichiesta() throws Exception
	{
		if (testoRisposta==null||testoRisposta.equals(""))
		{
			super.addErrorMessage("ff.filtro.incompleti","");	
			return "filtro.richiesta.fasNonEvaso";
		}

		if(nomePdf!=null && nomePdf!="")
		{
	      	RichiestaDTO richDTO = new RichiestaDTO();
	      	FFRichieste rich = new FFRichieste();
	      	rich.setIdRic(new Long(richiestaSelezionata.getRichiesta().getIdRichiesta()));
	      	rich.setNomePdf(null);
	      	
	      	richDTO.setRichiesta(rich);
	      	richDTO.setEnteId(super.getEnte());
	      	richDTO.setUserId(super.getUsername());
	      	
			richiestaService.updateFilePdfRichiesta(richDTO);
			
			String pathPdf= super.getPathDirFascicoloFabbricato(parameterService) + File.separatorChar + (nomePdf.endsWith(".pdf")? nomePdf: nomePdf + ".pdf");
			File pdf = new File(pathPdf);
			if(pdf.delete()) logger.debug(" CANCELLAZIONE PDF ESEGUITA ");
			
			nomePdf=null;
		}
		
		this.insertRichiesta(true,"40","Richiesta respinta",testoRisposta);
		
		if(inviaPerEmail || mezzoRisposta.equals("121"))
		{// invio email con "Richiesta Cartella Contribuente n. xxxx RESPINTA" e nota operatore
			this.sendEmailRespinta();
		}
		
		super.addInfoMessage("ff.filtro.richiestarespinta");
		return "filtro.richiesta.fasEvaso";
	}
	
	public String doEvadiRichiesta()
	{
		
		try {
		
			if ((nomePdf==null||nomePdf.equals("")) &&
				(testoRisposta.equals("")))
			{
				super.addErrorMessage("ff.filtro.incompleti","");	
				return "filtro.richiesta.fasNonEvaso";
			}
			
			if(mezzoRisposta.equals("0"))
			{// mezzo risposta non selezionato
				logger.debug("Mezzo risposta non selezionato.");
				super.addErrorMessage("ff.filtro.mezzorisp","");
				return "filtro.richiesta.fasNonEvaso";
			}
			
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
							this.sendEmail("Testo risposta :<br /> Fascicolo pronto per la consegna.", false);
							emailInviata = true;					
							break;
						}
						case 119:
						case 120:
						{// Ritiro Tramite messo comunale o Tramite posta ordinaria 
							// Invia una email senza allegato avvertendo che la cartella è stata prodotta e verrà spedita con <modalità richiesta> al più presto 
							// NON INVIA LA CARTELLA CONTRIBUENTE in allegato
							String testo = "Testo risposta :<br /> Il fascicolo è stato prodotto e ";
							CodiciTipoMezzoRispostaDTO codiceDTO = new CodiciTipoMezzoRispostaDTO();
							CodiciTipoMezzoRisposta c = new CodiciTipoMezzoRisposta();
							c.setCodice(mezzoRisposta);
							
							codiceDTO.setCodice(c);
							codiceDTO.setEnteId(super.getEnte());
							codiceDTO.setUserId(super.getUsername());
							
							CodiciTipoMezzoRisposta cod = commonService.getDescCodiciRisp(codiceDTO);
							if (cod==null) 
								testo = " Sarà spedito quanto prima.";
							else
								testo = " Sarà spedito tramite " + cod.getDescr() + " quanto prima.";
							
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
			super.addErrorMessage("ff.errore","");	
			logger.error(t.getMessage(),t);
			return "filtro.richiesta.fasNonEvaso";
		}
		
		super.addInfoMessage("ff.filtro.richiestaevasa");
		return "filtro.richiesta.fasEvaso";

	}
	
	private void insertRichiesta(boolean respinto,String macroStato,String descStato, String testoRisp) throws Exception
	{
		RispostaDTO rispDTO = new RispostaDTO();
		
		FFRisposte risp = new FFRisposte();
		risp.setUserName(super.getUsername());
		risp.setIdRic(new BigDecimal (richiestaSelezionata.getRichiesta().getIdRichiesta()));
		risp.setDtRis(dataEvasione);
		risp.setDesRis(testoRisposta);
		risp.setDesNotUser(notaOperatore);
		risp.setCodTipMezRis(mezzoRisposta);
		if (respinto)
			risp.setRespinto("1");
		else
			risp.setRespinto("0");
		
		rispDTO.setRisposta(risp);
		rispDTO.setUserId(super.getUsername());
		rispDTO.setEnteId(super.getEnte());
		
		rispostaEvasa = rispostaService.insertRisposta(rispDTO);

		logger.debug(" ID RISP INSERT = " + rispostaEvasa.getIdRis());
		
		richiestaSelezionata.getRichiesta().setNomePdf(nomePdf);
				
		if (richiestaSelezionata.getRichiesta().getTipoProvenienza().equals("W"))
		{//CAMBIO DI STATO NEL FILE XML DEL PORTALE
			// sia in caso di evasa che di respinta
			String nomefileStato = richiestaSelezionata.getRichiesta().getNumeroProtocollo()+"_change_stato.xml";
			String nomefileLockStato = richiestaSelezionata.getRichiesta().getNumeroProtocollo()+"_change_stato.lck";
			Long idPratica=new Long(richiestaSelezionata.getRichiesta().getNumeroProtocollo());
			Long idRichiesta=new Long(richiestaSelezionata.getRichiesta().getIdRichiesta());
			String ftpHost = super.getHostFtpScambioPortale(parameterService);
			String ftpUser = super.getUserFtpScambioPortale(parameterService);
			String ftpPwd = super.getPwdFtpScambioPortale(parameterService);
			String ftpDir = super.getDirFtpScambioPortale(parameterService);
			
			if (ftpHost!=null && !ftpHost.equals(""))
			{
				FTPClient ftpClient = UtilityScambioPortale.openConnectionFTP(ftpHost, ftpUser, ftpPwd);
				
				if (ftpClient==null || !ftpClient.isConnected())
					logger.debug("Impossibile connettersi al sito ftp");
				else
				{
					if(ftpClient.changeWorkingDirectory(ftpDir))
					{
						UtilityScambioPortale.writeXmlFileChangeStato(ftpClient, ftpDir + "/stati", nomefileStato, nomefileLockStato, 
								idPratica, idRichiesta, macroStato,"", descStato, testoRisp);
						if(nomePdf!=null && !nomePdf.equals("")){
							//per ogni mezzo risp si fa cmq l'upload del pdf sul portale 
							logger.debug("Chiamata per UPLOAD FTP file pdf");
							String pathPdf= super.getPathDirFascicoloFabbricato(parameterService) + File.separatorChar + (nomePdf.endsWith(".pdf")? nomePdf: nomePdf + ".pdf");
							File filePdf = new File(pathPdf);
							String nomefilePdfPortale = richiestaSelezionata.getRichiesta().getNumeroProtocollo()+ ".pdf";
							ftpClient.changeWorkingDirectory(ftpDir+ "/certificati");
							UtilityScambioPortale.writePdfCartella(ftpClient,filePdf, nomefilePdfPortale);
					}
							
					}
					else
						logger.debug(" Directory FTP non present");
					
					UtilityScambioPortale.closeConnectionFTP(ftpClient);
				}
			}
			else
				logger.debug(" Host FTP non definito");
		}
	}

	private void sendEmail(String testo, boolean sendAttach)
	{
		try
		{
			String idRichiesta = richiestaSelezionata.getRichiesta().getIdRichiesta();
			
			RichiestaDTO richiestaDTO = new RichiestaDTO();

			FFRichieste r = new FFRichieste();
			r.setIdRic(new Long(idRichiesta));
			
			richiestaDTO.setRichiesta(r);
			richiestaDTO.setEnteId(super.getEnte());
			richiestaDTO.setUserId(super.getUsername());
			FFRichieste rich =  richiestaService.getRichiesta(richiestaDTO);
			
			if (rich!=null && r.getEMail()!="")
			{
				Properties properties = new Properties();
				properties.put("mail.smtp.host", super.getMailServer(parameterService));
				properties.put("mail.smtp.port", super.getPortMailServer(parameterService));
				Session session = Session.getDefaultInstance(properties, null);
				
				MimeMessage message = new MimeMessage(session);
	 			Multipart multipart = new MimeMultipart();
	 			
				message.setFrom(new InternetAddress(super.getEmailFrom(parameterService)));
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(rich.getEMail()));
				message.setSubject("Fascicolo Fabbricato richiesta numero " + (rich.getCodTipProven().equals("W")?rich.getNumProt():rich.getIdRic()));
				message.setSentDate(new Date());
				
				MimeBodyPart messagePart = new MimeBodyPart();
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String testoMail = " Risposta alla vostra richiesta fascicolo fabbricato effettuata il " + sdf.format(rich.getDtRic()) + " riferita a :<br />";
				if(richiestaSelezionata.getRichiesta().getSezione() != null)
					testoMail += "  Sezione: " + richiestaSelezionata.getRichiesta().getSezione();
				if(richiestaSelezionata.getRichiesta().getFoglio() != null)
					testoMail += "  Foglio: " + richiestaSelezionata.getRichiesta().getFoglio();
				if(richiestaSelezionata.getRichiesta().getParticella() != null)
					testoMail += "  Particella: " + richiestaSelezionata.getRichiesta().getParticella();
				testoMail += "<br />";
				
				if(rich.getIdSoggRic()!=null)
				{
					RichiestaDTO richDTO = new RichiestaDTO();
					FFSoggetti ffSogg = new FFSoggetti();
					ffSogg.setIdSogg(rich.getIdSoggRic());
					richDTO.setSoggetto(ffSogg);
					richDTO.setUserId(getUsername());
					richDTO.setEnteId(getEnte());
					FFSoggetti soggRich = richiestaService.getSoggetto(richDTO);
					if (soggRich!=null)
					{
						testoMail += " Effettuata da:<br />";
						testoMail += soggRich.getCognome() + " " + soggRich.getNome() + " Codice fiscale: " + soggRich.getCodFis();
					}
				}
				
				testoMail += "<br /><br /> ";
				testoMail += testo;
				messagePart.setContent(testoMail, "text/html");
				
				if (sendAttach)
				{
					// Set the email attachment file
					MimeBodyPart attachmentPart = new MimeBodyPart();
					String pathFile = super.getPathDirFascicoloFabbricato(parameterService) + File.separatorChar  + rich.getNomePdf() + ".pdf";
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
			super.addErrorMessage("ff.sendMail", null);
			logger.error(e.getMessage(),e);
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
		
			RichiestaDTO richiestaDTO = new RichiestaDTO();

			FFRichieste r = new FFRichieste();
			r.setIdRic(new Long(idRichiesta));
			
			richiestaDTO.setRichiesta(r);
			richiestaDTO.setEnteId(super.getEnte());
			richiestaDTO.setUserId(super.getUsername());
			FFRichieste rich =  richiestaService.getRichiesta(richiestaDTO);
			
			if (rich!=null && r.getEMail()!="")
			{
				Properties properties = new Properties();
				properties.put("mail.smtp.host", super.getMailServer(parameterService));
				properties.put("mail.smtp.port", super.getPortMailServer(parameterService));
				Session session = Session.getDefaultInstance(properties, null);
				
				MimeMessage message = new MimeMessage(session);
	 			Multipart multipart = new MimeMultipart();
	 			
				message.setFrom(new InternetAddress(super.getEmailFrom(parameterService)));
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(rich.getEMail()));
				message.setSubject("Fascicolo Fabbricato Richiesta numero " + (provenienza.equals("W")?numProt:idRichiesta));
				message.setSentDate(new Date());
				
				MimeBodyPart messagePart = new MimeBodyPart();
				
				String testoMail = " Richiesta fascicolo fabbricato n. " + (provenienza.equals("W")?numProt:idRichiesta) + " RESPINTA <br />";
				testoMail += "<br /> Testo risposta :<br />" + testoRisposta;
				messagePart.setContent(testoMail, "text/html");
				multipart.addBodyPart(messagePart);
				message.setContent(multipart);
				
				Transport.send(message);
			}
		}
		catch (MessagingException e) {
			super.addErrorMessage("ff.sendMail", null);
			logger.error(e.getMessage(),e);
		}		
		catch (Exception ex)
		{
		}
	}
	
	public String goToList() {
		return "fab.filtro.dettaglio.back";
	}
	
	private boolean isValidEmailAddress(String aEmailAddress){
		
		if (aEmailAddress==null || aEmailAddress=="") return false;
		boolean result = true;
		try 
		{
			String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern;
			Matcher matcher;
			
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(aEmailAddress);

			return matcher.matches();
		}
		catch (Exception ex){
			result = false;
		}
		
		return result;
	}
	
	public void abilitaEmail() {
		disabilitaEmailCheck = false;
        if (mezzoRisposta.equals("121"))
        	disabilitaEmailCheck=true;
          
    }
}
