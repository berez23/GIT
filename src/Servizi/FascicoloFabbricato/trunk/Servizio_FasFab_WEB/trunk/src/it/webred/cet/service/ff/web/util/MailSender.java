package it.webred.cet.service.ff.web.util;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFRisposte;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MailSender extends FFBaseBean{
	
	private ParameterService parameterService;
	private GestRichiestaService richiestaService;
	//private GestRispostaService rispostaService;

	
	public MailSender(ParameterService parameterService, GestRichiestaService richiestaService) {
		this.parameterService = parameterService;
		this.richiestaService = richiestaService;
	//	this.rispostaService = rispostaService;
	}

	
	public void sendEmail(FFRichieste rich, FFRisposte rispostaEvasa,String ente, String user, String dirFile) throws MailSendException
	{
		try
		{
			Utility utility = new Utility(parameterService);
			logger.debug(" MAIL SERVER = " + utility.getMailServer());
			logger.debug(" PORT SERVER = "+  utility.getPortMailServer());			
			logger.debug(" EMAIL TO = " + rich.getEMail());
			logger.debug(" EMAIL FROM = " + utility.getEmailFrom());

			Properties properties = new Properties();
			properties.put("mail.smtp.host", utility.getMailServer());
			properties.put("mail.smtp.port", utility.getPortMailServer());
			Session session = Session.getDefaultInstance(properties, null);
			
			MimeMessage message = new MimeMessage(session);
 			Multipart multipart = new MimeMultipart();
 			
			message.setFrom(new InternetAddress(utility.getEmailFrom()));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(rich.getEMail()));
			message.setSubject("Fascicolo Fabbricato - Richiesta numero " + rich.getIdRic());
			message.setSentDate(new Date());
			
			// Set the email message text
			MimeBodyPart messagePart = new MimeBodyPart();
			
			
			RichiestaDTO richDTO = new RichiestaDTO();
			
			FFSoggetti soggetto = new FFSoggetti();
			soggetto.setIdSogg(rich.getIdRic());
			
			richDTO.setSoggetto(soggetto);
			richDTO.setEnteId(ente);
			richDTO.setUserId(user);
			
			FFSoggetti sogg = richiestaService.getSoggetto(richDTO);
			
			String testo = " Risposta alla vostra richiesta effettuata il " + Utility.dateToString_ddMMyyyy(rich.getDtRic()) + " riferita a :<br />";
			if (rich.getSezione() != null && !rich.getSezione().equals(""))
				testo += "SEZIONE CATASTALE " + rich.getSezione() +  " - ";  
			testo += "FOGLIO CATASTALE " + rich.getFoglio() + " - PARTICELLA CATASTALE " + rich.getParticella();
			
			testo += "<br />";
			if(rich.getIdSoggRic()!=null)
			{
				//FFSoggetti soggRich = richiestaService.getSoggetto(rich.getIdSoggRic());
				
				RichiestaDTO richiestaDTO = new RichiestaDTO();
				
				FFSoggetti sogge = new FFSoggetti();
				sogge.setIdSogg(rich.getIdSoggRic());
				
				richiestaDTO.setSoggetto(sogge);
				richiestaDTO.setEnteId(ente);
				richiestaDTO.setUserId(user);
				
				FFSoggetti soggRich = richiestaService.getSoggetto(richiestaDTO);
				
				testo += " Effettuata da :<br />" ;
				testo += soggRich.getCognome() + " " + soggRich.getNome() + " codice fiscale " + soggRich.getCodFis();
			}
			
			testo += "<br /><br /> Testo risposta :<br />" + rispostaEvasa.getDesRis();
			messagePart.setContent(testo, "text/html");
			
			// Set the email attachment file
			MimeBodyPart attachmentPart = new MimeBodyPart();
			String pathFile = super.getPathDatiDiogeneEnte(parameterService) + File.separatorChar +  dirFile+ File.separatorChar + rich.getNomePdf() ; 
			logger.debug("PATH RICERCA FILE ALLEGATO E-MAIL: " + pathFile);
			if (pathFile!=null && pathFile!="")
			{
		        File tempPath = new File(pathFile);
		        if(tempPath.exists()) 
		        {
		            FileDataSource fds = new FileDataSource(pathFile);
		            attachmentPart.setDataHandler(new DataHandler(fds));
		            logger.debug("");
		            attachmentPart.setFileName(fds.getName());		  
					// Add attachment file
		            multipart.addBodyPart(attachmentPart);
		        }
			}
			
			multipart.addBodyPart(messagePart);
			message.setContent(multipart);
			
			// SEND MAIL
			logger.debug(" START Transport.send");
			Transport.send(message);
			logger.debug(" END Transport.send");
		}
		
		catch (Exception ex)
		{
			logger.error(ex.getMessage(),ex);
			throw new MailSendException();
		}
		
	}
}
