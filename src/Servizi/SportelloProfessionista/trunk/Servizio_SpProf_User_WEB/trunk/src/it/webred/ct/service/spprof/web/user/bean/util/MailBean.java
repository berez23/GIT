package it.webred.ct.service.spprof.web.user.bean.util;

import it.webred.amprofiler.ejb.user.UserService;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.application.ApplicationService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.EdificioDTO;
import it.webred.ct.service.spprof.data.access.dto.UiuDTO;
import it.webred.ct.service.spprof.data.access.dto.UnitaVolDTO;
import it.webred.ct.service.spprof.data.model.SSpUnitaVol;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.interventi.DatiBean;
import it.webred.ct.service.spprof.web.user.bean.interventi.UiuBean;
import it.webred.ct.service.spprof.web.user.bean.interventi.UnitaVolBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MailBean extends SpProfBaseBean {

	protected ParameterService parameterService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");

	protected UserService userService = (UserService) getEjb("AmProfiler", "AmProfilerEjb", "UserServiceBean");

	String idIntervento;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public void sendMailIntervento() throws MessagingException {

		try {

			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("dir.files.dati");
			AmKeyValueExt extPath = parameterService.getAmKeyValueExt(criteria);
			criteria.setComune(getEnte());
			criteria.setKey("mail.server");
			AmKeyValueExt extServer = parameterService
					.getAmKeyValueExt(criteria);
			criteria.setKey("mail.server.port");
			AmKeyValueExt extPort = parameterService.getAmKeyValueExt(criteria);
			criteria.setKey("email.admin");
			AmKeyValueExt extMailAdmin = parameterService
					.getAmKeyValueExt(criteria);
			criteria.setKey("email.spprof.testo.invio");
			UserBean uBean = (UserBean) getBeanReference("userBean");
			criteria.setInstance(uBean.getInstance());
			AmKeyValueExt extTesto = parameterService
					.getAmKeyValueExt(criteria);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String testoUtente = "Comunicazione proveniente dal portale GIT: Servizio Sportello del Professionista\n\n"
					+ extTesto.getValueConf() + "\n\nSaluti\n\n" + sdf.format(new Date());
			String testoAdmin = "Comunicazione proveniente dal portale GIT: Servizio Sportello del Professionista\n\n"
					+ "L'utente " + getUsername() + " ha inviato un intervento" + "\n\nSaluti\n\n" + sdf.format(new Date());

			//INVIO MAIL AD UTENTE
			String[] recipients = { userService.getUserByName(getUsername())
					.getEmail() };
		
			String file = extPath.getValueConf() + "dettaglioIntervento_"
					+ idIntervento + ".pdf";

			// create the pdf
			dettaglioInterventoExportToPdf(file);

			// Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.host", extServer.getValueConf());
			props.put("mail.smtp.port", extPort.getValueConf());

			// create some properties and get the default Session
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(false);

			// create a message
			Message msg = new MimeMessage(session);

			// set the from and to address
			InternetAddress addressFrom = new InternetAddress(
					extMailAdmin.getValueConf());
			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				addressTo[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			// create and fill the first message part
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(testoUtente, "text/plain");

			// create the second message part
			MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			FileDataSource fds = new FileDataSource(file);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);

			// Setting the Subject and Content Type
			msg.setSubject("Sportello del Professionista: dettaglio invio intervento: "
					+ idIntervento);
			msg.setContent(mp);
			Transport.send(msg);
			
			//INVIO MAIL AD ADMIN
			// create a message
			msg = new MimeMessage(session);

			// set the from and to address
			msg.setFrom(addressFrom);

			addressTo[0] = addressFrom;
			
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			// create and fill the first message part
			mbp1 = new MimeBodyPart();
			mbp1.setContent(testoAdmin, "text/plain");

			// create the second message part
			mbp2 = new MimeBodyPart();

			// attach the file to the message
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());

			// create the Multipart and add its parts to it
			mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);

			// Setting the Subject and Content Type
			msg.setSubject("Sportello del Professionista: dettaglio invio intervento: "
					+ idIntervento + " Utente: " + getUsername());
			msg.setContent(mp);
			Transport.send(msg);

			// eliminazione pdf temp
			File pdfTemp = new File(file);
			pdfTemp.delete();

			super.addInfoMessage("sendMail");

		} catch (Exception e) {
			logger.error(e);
			super.addErrorMessage("sendMail.error", e.getMessage());
		}
	}

	public void dettaglioInterventoExportToPdf(String file) {

		String titolo = "Dettaglio Intervento Id: " + idIntervento;

		try {

			Document document = new Document(PageSize.A4, 10, 10, 10, 10);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			PdfWriter.getInstance(document, baos);
			document.open();

			document.addTitle("pdf");
			document.addSubject("pdf");
			document.addAuthor("Servizio Sportello Professionista");
			document.addCreator("Servizio Sportello Professionista");

			this.addTitolo(document, titolo);
			DatiBean dBean = (DatiBean) getBeanReference("datiBean");
			dBean.setIdIntervento(idIntervento);
			dBean.setCaricaSolo(true);
			dBean.doDettaglio();
			this.addDettaglioIntervento(document, dBean);
			this.addEdifici(document, dBean.getListaEdifici());
			this.addEdificiMinori(document, dBean.getListaEdificiMinori());

			document.close();

			if (baos != null)
				this.writePdf(baos, file);
		} catch (Throwable t) {
			super.addErrorMessage("export.pdf.error", t.getMessage());
			logger.error(t);
		}

	}

	private void addTitolo(Document document, String titolo)
			throws DocumentException {

		Paragraph tit = new Paragraph();
		this.addEmptyLine(tit, 1);
		Paragraph intestazione = new Paragraph(titolo, new Font(
				Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD));
		intestazione.setAlignment(Element.ALIGN_CENTER);
		tit.add(intestazione);
		this.addEmptyLine(tit, 3);

		document.add(tit);
	}

	private void addDettaglioIntervento(Document document, DatiBean bean)
			throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

		// INIZIO TABELLA DETTAGLIO
		PdfPTable tableDett = new PdfPTable(2);
		tableDett.setWidthPercentage(60);
		tableDett.setWidths(new float[] { 50, 50 });
		tableDett.getDefaultCell().setMinimumHeight(20);

		tableDett.addCell(getCell(tableDett, "Numero posti auto", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getnPostiAuto(), normalFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Numero box auto", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getnBoxAuto(), normalFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Numero passi carrai previsti",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getnPassiCarrai(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Numero accessi carrai previsti",
				boldFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getnAccessiCarrai(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Numero concessione", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getConcessioneNum(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Numero progressivo", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getProgressivoNum(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Anno progressivo", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getProgressivoAnno(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Data protocollo", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, (bean.getProtocolloData()!=null?sdf.format(bean.getProtocolloData()):""),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Numero protocollo", boldFont,
				BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getProtocolloNumero(),
				normalFont, BaseColor.WHITE, new Integer(0)));

		tableDett.addCell(getCell(tableDett, "Note", boldFont, BaseColor.WHITE,
				new Integer(0)));

		tableDett.addCell(getCell(tableDett, bean.getNote(), normalFont,
				BaseColor.WHITE, new Integer(0)));

		document.add(tableDett);
		// FINE TABELLA DETTAGLIO

		Paragraph p = new Paragraph();
		this.addEmptyLine(p, 1);
		document.add(p);

		this.addEmptyLine(p, 1);
		document.add(p);
	}

	private void addEdifici(Document document, List<EdificioDTO> listaEd)
			throws DocumentException {

		UnitaVolBean uvBean = (UnitaVolBean) getBeanReference("unitaVolBean");
		UiuBean uaBean = (UiuBean) getBeanReference("uiuBean");

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

		// INIZIO TABELLA EDIFICI
		for (EdificioDTO i : listaEd) {

			PdfPTable tableEd = new PdfPTable(6);
			tableEd.setWidthPercentage(100);
			tableEd.setWidths(new float[] { 15, 25, 20, 15, 15, 20 });
			tableEd.getDefaultCell().setMinimumHeight(20);

			tableEd.addCell(getCell(tableEd, "Edificio", boldFont,
					BaseColor.GRAY, null, new Integer(6)));

			tableEd.addCell(getCell(tableEd, "Coordinate", boldFont,
					BaseColor.LIGHT_GRAY, null));

			tableEd.addCell(getCell(tableEd, "Civico", boldFont,
					BaseColor.LIGHT_GRAY, null));

			tableEd.addCell(getCell(tableEd, "Tipologia", boldFont,
					BaseColor.LIGHT_GRAY, null));

			tableEd.addCell(getCell(tableEd, "Destinazione d'uso", boldFont,
					BaseColor.LIGHT_GRAY, null));

			tableEd.addCell(getCell(tableEd, "Destinazione d'uso prevalente",
					boldFont, BaseColor.LIGHT_GRAY, null));

			tableEd.addCell(getCell(tableEd, "Note", boldFont,
					BaseColor.LIGHT_GRAY, null));

			tableEd.addCell(getCell(tableEd, i.getParticella().getFoglio()
					+ ", " + i.getParticella().getParticella().toString(),
					normalFont, BaseColor.WHITE, null));

			tableEd.addCell(getCell(tableEd, i.getEdificio().getViaPrefisso()
					+ " " + i.getEdificio().getViaNome() + ", "
					+ i.getEdificio().getCivicoNumero(), normalFont,
					BaseColor.WHITE, null));

			tableEd.addCell(getCell(tableEd, i.getTipologia().getDescr(),
					normalFont, BaseColor.WHITE, null));

			tableEd.addCell(getCell(tableEd, i.getDestUrb(), normalFont,
					BaseColor.WHITE, null));

			tableEd.addCell(getCell(tableEd, (i.getEdificio().getFkSpDestUrb()
					.equals("MIX") ? i.getDestUrbPrev() : ""), normalFont,
					BaseColor.WHITE, null));

			tableEd.addCell(getCell(tableEd, i.getEdificato().getNote(),
					normalFont, BaseColor.WHITE, null));

			document.add(tableEd);

			// INIZIO TABELLA UNITA VOLUMETRICHE
			uvBean.setIdEdificio(new Long(i.getEdificato().getIdSpCedificato())
					.toString());
			uvBean.setCaricaSolo(true);
			uvBean.doLista();

			if (uvBean.getListaUnitaVol().size() > 0) {
				PdfPTable tableUv = new PdfPTable(8);
				tableUv.setWidthPercentage(100);
				tableUv.setWidths(new float[] { 5, 15, 15, 15, 15, 10, 10, 15 });
				tableUv.getDefaultCell().setMinimumHeight(20);

				tableUv.addCell(getCell(tableUv,
						"Unità volumetriche associate all'edificio", boldFont,
						BaseColor.GRAY, null, new Integer(8)));

				tableUv.addCell(getCell(tableUv, "Id", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUv.addCell(getCell(tableUv, "Altezza", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUv.addCell(getCell(tableUv,
						"Superficie d'impronta già sgrondata", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUv.addCell(getCell(tableUv,
						"Superficie lorda di pavimento complessiva", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUv.addCell(getCell(tableUv, "Volume", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUv.addCell(getCell(tableUv, "Piani fuori terra", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUv.addCell(getCell(tableUv, "Piani entro terra", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUv.addCell(getCell(tableUv, "Note", boldFont,
						BaseColor.LIGHT_GRAY, null));

				for (UnitaVolDTO uvdto : uvBean.getListaUnitaVol()) {
					
					SSpUnitaVol uv = uvdto.getUnitaVol();
					tableUv.addCell(getCell(tableUv,
							new Long(uv.getIdSpUnitaVol()).toString(),
							normalFont, BaseColor.WHITE, null));

					tableUv.addCell(getCell(tableUv,
							uv.getAltezza() != null ? uv.getAltezza()
									.toString() : "", normalFont,
							BaseColor.WHITE, null));

					tableUv.addCell(getCell(tableUv,
							uv.getSupDbt() != null ? uv.getSupDbt().toString()
									: "", normalFont, BaseColor.WHITE, null));

					tableUv.addCell(getCell(tableUv, uv.getSlp() != null ? uv
							.getSlp().toString() : "", normalFont,
							BaseColor.WHITE, null));

					tableUv.addCell(getCell(tableUv,
							uv.getVolume() != null ? uv.getVolume().toString()
									: "", normalFont, BaseColor.WHITE, null));

					tableUv.addCell(getCell(tableUv,
							uv.getPianiFt() != null ? uv.getPianiFt()
									.toString() : "", normalFont,
							BaseColor.WHITE, null));

					tableUv.addCell(getCell(tableUv,
							uv.getPianiEt() != null ? uv.getPianiEt()
									.toString() : "", normalFont,
							BaseColor.WHITE, null));

					tableUv.addCell(getCell(tableUv, uv.getNote(), normalFont,
							BaseColor.WHITE, null));
				}

				document.add(tableUv);
			}
			// FINE TABELLA UNITA VOLUMETRICHE

			// INIZIO TABELLA UNITA ABITATIVE
			uaBean.setIdEdificio(new Long(i.getEdificato().getIdSpCedificato())
					.toString());
			uaBean.setCaricaSolo(true);
			uaBean.doLista();

			if (uaBean.getListaUiu().size() > 0) {
				PdfPTable tableUa = new PdfPTable(6);
				tableUa.setWidthPercentage(100);
				tableUa.setWidths(new float[] { 5, 20, 25, 15, 15, 20 });
				tableUa.getDefaultCell().setMinimumHeight(20);

				tableUa.addCell(getCell(tableUa,
						"Unità abitative associate all'edificio", boldFont,
						BaseColor.GRAY, null, new Integer(6)));

				tableUa.addCell(getCell(tableUa, "Id", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUa.addCell(getCell(tableUa, "Numero unità per categoria",
						boldFont, BaseColor.LIGHT_GRAY, null));

				tableUa.addCell(getCell(tableUa, "Categoria", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUa.addCell(getCell(tableUa, "Classe", boldFont,
						BaseColor.LIGHT_GRAY, null));

				tableUa.addCell(getCell(tableUa, "Unita volumetrica collegata",
						boldFont, BaseColor.LIGHT_GRAY, null));

				tableUa.addCell(getCell(tableUa, "Note", boldFont,
						BaseColor.LIGHT_GRAY, null));

				for (UiuDTO ua : uaBean.getListaUiu()) {

					tableUa.addCell(getCell(tableUa, new Long(ua.getUiu()
							.getIdSpUiu()).toString(), normalFont,
							BaseColor.WHITE, null));

					tableUa.addCell(getCell(tableUa,
							ua.getUiu().getNumUiu() != null ? ua.getUiu()
									.getNumUiu().toString() : "", normalFont,
							BaseColor.WHITE, null));

					tableUa.addCell(getCell(tableUa, ua.getUiu().getCategoria()
							+ "-" + ua.getCategoriaDescr(), normalFont,
							BaseColor.WHITE, null));

					tableUa.addCell(getCell(tableUa, ua.getUiu().getClasse(),
							normalFont, BaseColor.WHITE, null));

					tableUa.addCell(getCell(tableUa, ua.getUiu()
							.getFkSpUnitaVol() != null ? ua.getUiu()
							.getFkSpUnitaVol().toString() : "", normalFont,
							BaseColor.WHITE, null));

					tableUa.addCell(getCell(tableUa, ua.getUiu().getNote(),
							normalFont, BaseColor.WHITE, null));
				}

				document.add(tableUa);
			}
			// FINE TABELLA UNITA ABITATIVE

			Paragraph p = new Paragraph();
			this.addEmptyLine(p, 1);
			document.add(p);
		}
		// FINE TABELLA EDIFICI

		Paragraph p = new Paragraph();
		this.addEmptyLine(p, 1);
		document.add(p);

		this.addEmptyLine(p, 1);
		document.add(p);
	}

	private void addEdificiMinori(Document document, List<EdificioDTO> listaEd)
			throws DocumentException {

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

		// INIZIO TABELLA EDIFICI MINORI
		PdfPTable tableEd = new PdfPTable(4);
		tableEd.setWidthPercentage(100);
		tableEd.setWidths(new float[] { 15, 50, 15, 20 });
		tableEd.getDefaultCell().setMinimumHeight(20);

		tableEd.addCell(getCell(tableEd, "Lista Edifici Minori", boldFont,
				BaseColor.GRAY, null, new Integer(4)));

		tableEd.addCell(getCell(tableEd, "Coordinate", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableEd.addCell(getCell(tableEd, "Tipologia", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableEd.addCell(getCell(tableEd, "Precario", boldFont,
				BaseColor.LIGHT_GRAY, null));

		tableEd.addCell(getCell(tableEd, "Note", boldFont,
				BaseColor.LIGHT_GRAY, null));

		for (EdificioDTO i : listaEd) {

			tableEd.addCell(getCell(tableEd, i.getParticella().getFoglio()
					+ ", " + i.getParticella().getParticella().toString(),
					normalFont, BaseColor.WHITE, null));

			tableEd.addCell(getCell(tableEd, i.getTipologiaMin().getDescr(),
					normalFont, BaseColor.WHITE, null));

			String precario = "NO";
			if (i.getEdificioMinore().getPrecario().equals(new BigDecimal(1)))
				precario = "SI";
			tableEd.addCell(getCell(tableEd, precario, normalFont,
					BaseColor.WHITE, null));

			tableEd.addCell(getCell(tableEd, i.getEdificato().getNote(),
					normalFont, BaseColor.WHITE, null));
		}

		document.add(tableEd);
		// FINE TABELLA EDIFICI MINORI

		Paragraph p = new Paragraph();
		this.addEmptyLine(p, 1);
		document.add(p);

		this.addEmptyLine(p, 1);
		document.add(p);
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border) {

		return getCell(table, ((phrase == null || phrase.contains("null")) ? ""
				: phrase), font, color, border, new Integer(1));
	}

	private PdfPCell getCell(PdfPTable table, String phrase, Font font,
			BaseColor color, Integer border, Integer colSpan) {

		PdfPCell c1 = table.getDefaultCell();
		c1.setPhrase(new Phrase(phrase, font));
		if (border != null)
			c1.setBorder(border.intValue());
		if (colSpan != null)
			c1.setColspan(colSpan.intValue());
		c1.setBackgroundColor(color);

		return c1;
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private void writePdf(ByteArrayOutputStream baos, String file)
			throws IOException {

		File fout = new File(file);

		FileOutputStream out = new FileOutputStream(fout);
		baos.writeTo(out);
		baos.flush();
		baos.close();
		out.close();

	}

	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

}
