package net.skillbill.webred.pdf;

import it.webred.mui.consolidation.ComunicazioneConverter;
import it.webred.mui.consolidation.DetrazioneManager;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupSoggetti;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.Session;
import org.pdfbox.exceptions.COSVisitorException;

/**
 * Servlet implementation class for Servlet: PdfComunicazioneICIServlet
 * 
 */
public class PdfComunicazioneICIServlet extends MuiBaseServlet {
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public PdfComunicazioneICIServlet() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see MuiBaseServlet#muiService(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void muiService(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		// un report ICI con due pagine "body"
		Ici qcsaf;
		try {
			Logger.log().info(PdfComunicazioneICIServlet.class.getName(), "comunicazione pdf request query String="+ req.getQueryString());			
			Long iidSoggetto = Long.valueOf(req.getParameter("iidSoggetto"));
			Session session = HibernateUtil.currentSession();
			MiDupSoggetti soggetto = (MiDupSoggetti) session.load(
					MiDupSoggetti.class, iidSoggetto);
//			MiConsComunicazione comunicazione = converter
//					.evalComunicazione(soggetto);
			DetrazioneManager dm = new DetrazioneManager(soggetto);
			try {
				dm.evalDetrazione();
			} catch (SQLException e) {
				throw new ServletException(e);
			} catch (NamingException e) {
				throw new ServletException(e);
			}
			MiConsComunicazione comunicazione = dm.getComunicazione();
			
			File tmpl = new File(getServletContext().getRealPath(
					"pdf/"+MuiApplication.belfiore+"_modelloIciCh.tmpl.pdf"));
			qcsaf = new Ici(tmpl, comunicazione.getMiConsOggettos().size());

			Map replaces = new HashMap();
			String codiceFiscale = comunicazione.getCodiceFiscale();
			for (int i = codiceFiscale.length(); i < 16; i++) {
				codiceFiscale += " ";
			}
			for (int i = 0; i < 16; i++) {
				setTemplateField(replaces, i, codiceFiscale.charAt(i));
			}
			setTemplateField(replaces, 16, comunicazione.getPrefisso());
			setTemplateField(replaces, 17, comunicazione.getTelefono());
			setTemplateField(replaces, 18, comunicazione.getCognome());
			setTemplateField(replaces, 27, comunicazione.getNome());
			
			setTemplateField(replaces, new int[]{19,20,21},comunicazione.getDataNascita());
			
			setTemplateField(replaces, 26, comunicazione.getComune());
			setTemplateField(replaces, 28, comunicazione.getProvincia());

			setTemplateField(replaces, new int[]{29,30},"1".equals(comunicazione.getSesso()),comunicazione.getSesso()!=null);
			
			setTemplateField(replaces, 22, comunicazione.getIndirizzo());
			setTemplateField(replaces, 23, comunicazione.getCap());
			setTemplateField(replaces, 31, comunicazione.getComune());
			setTemplateField(replaces, 32, comunicazione.getProvincia());
			setTemplateField(replaces, 24, comunicazione.getIndirizzoDomicilio());
			setTemplateField(replaces, 25, comunicazione.getCapDomicilio());
			setTemplateField(replaces, 33, comunicazione.getComuneDomicilio());
			setTemplateField(replaces, 34, comunicazione.getProvinciaDomicilio());
			
			setTemplateField(replaces, 35,comunicazione.getFlagRappresentanteLegale()); 
			setTemplateField(replaces, 36,comunicazione.getFlagCuratoreFallimentare()); 
			setTemplateField(replaces, 37,comunicazione.getFlagTutore()); 
			setTemplateField(replaces, 38,comunicazione.getFlagErede()); 
			setTemplateField(replaces, 39,comunicazione.getAltraNatura()); 
			String codiceFiscaleRL = comunicazione.getCodiceFiscaleRL();
			if(codiceFiscaleRL == null){
				codiceFiscaleRL = "";
			}
			for (int i = codiceFiscaleRL.length(); i < 16; i++) {
				codiceFiscaleRL += " ";
			}
			for (int i = 0; i < 9; i++) {
				setTemplateField(replaces, 40+i, codiceFiscaleRL.charAt(i));
			}
			for (int i = 9; i < 16; i++) {
				setTemplateField(replaces, 44+i, codiceFiscaleRL.charAt(i));
			}
			setTemplateField(replaces, 62, comunicazione.getPrefissoRL());
			setTemplateField(replaces, 63, comunicazione.getTelefonoRL());
			setTemplateField(replaces, 49, comunicazione.getCognomeRL());
			setTemplateField(replaces, 61, comunicazione.getNomeRL());

			setTemplateField(replaces, new int[]{50,51,52},comunicazione.getDataNascitaRL());

			setTemplateField(replaces, 60, comunicazione.getComuneRL());
			setTemplateField(replaces, 64, comunicazione.getProvinciaRL());
			setTemplateField(replaces, new int[]{65,66},"1".equals(comunicazione.getSessoRL()),(comunicazione.getCodiceFiscaleRL() != null && comunicazione.getSessoRL() != null));
			setTemplateField(replaces, 67, comunicazione.getIndirizzoRL());
			setTemplateField(replaces, 68, comunicazione.getCapRL());
			setTemplateField(replaces, 69, comunicazione.getComuneRL());
			setTemplateField(replaces, 70, comunicazione.getProvinciaRL());
			setTemplateField(replaces, 71, comunicazione.getIndirizzoDomicilioRL());
			setTemplateField(replaces, 72, comunicazione.getCapDomicilioRL());
			setTemplateField(replaces, 73, comunicazione.getComuneDomicilioRL());
			setTemplateField(replaces, 74, comunicazione.getProvinciaDomicilioRL());

			setTemplateField(replaces,75,comunicazione
					.getMiConsOggettos().size()+1);
			setTemplateField(replaces,new int[]{76,77,78},(Date)null);
			setTemplateField(replaces,79);
			
			// n-var in una botta sola nella pagina 0 (frontespizio)
			qcsaf.replaceVar(0, replaces);

			int npage = 1;
			for (Iterator<MiConsOggetto> iter = comunicazione
					.getMiConsOggettos().iterator(); iter.hasNext();) {
				MiConsOggetto oggetto = iter.next();
				replaces = new HashMap();
				setTemplateField(replaces,80,npage);
				for (int i = 81; i < 94; i++) {
					setTemplateField(replaces, i, codiceFiscale.charAt(i - 81));
				}
				setTemplateField(replaces, 100, codiceFiscale.charAt(13));
				setTemplateField(replaces, 110, codiceFiscale.charAt(14));
				setTemplateField(replaces, 111, codiceFiscale.charAt(15));
				
				setTemplateField(replaces, 94, oggetto.getCodiceVariazione());
				setTemplateField(replaces, 102, oggetto.getDescrizioneVariazione());
				
				//TODO: aggiungere il campo anno!!!
				setTemplateField(replaces, new int[]{99,101,162},oggetto.getDecorrenza());
				
				setTemplateField(replaces,95,oggetto.getFlagFabbricato());
				setTemplateField(replaces,96,oggetto.getFlagAreaFabbricabile());
				setTemplateField(replaces,97,oggetto.getFlagFabbricatoD());
				setTemplateField(replaces,98,oggetto.getFlagTerrenoAgricolo());

				setTemplateField(replaces,112,oggetto.getIndirizzo());

				setTemplateField(replaces,103,oggetto.getSezione());
				setTemplateField(replaces,104,oggetto.getFoglio());
				setTemplateField(replaces,105,oggetto.getParticella());
				setTemplateField(replaces,106,oggetto.getSubalterno());
				setTemplateField(replaces,107,oggetto.getNumeroProtocollo());
				setTemplateField(replaces,108,oggetto.getAnno());
				setTemplateField(replaces,109,oggetto.getCategoria());
				setTemplateField(replaces,113,oggetto.getClasse());
				
				setTemplateField(replaces,114,oggetto.getFlagRenditaDefinitiva());
				setTemplateField(replaces,119,oggetto.getFlagRenditaPresunta());
				setTemplateField(replaces,115,oggetto.getFlagValoreVenale());
				setTemplateField(replaces,120,oggetto.getFlagCostiContabili());
				
				setTemplateField(replaces,116,oggetto.getFlagRedditoDomenicale());
				//TODO: aggiungere decimali!!!
				setTemplateField(replaces,new int[]{117,118},oggetto.getRedditoEuro());

				setTemplateField(replaces,121,oggetto.getSezioneVar());
				setTemplateField(replaces,122,oggetto.getFoglioVar());
				setTemplateField(replaces,123,oggetto.getParticellaVar());
				setTemplateField(replaces,124,oggetto.getSubalternoVar());
				setTemplateField(replaces,125,oggetto.getNumeroProtocolloVar());
				setTemplateField(replaces,126,oggetto.getAnnoVar());
				setTemplateField(replaces,127,oggetto.getCategoriaVar());
				setTemplateField(replaces,128,oggetto.getClasseVar());
				
				setTemplateField(replaces,new int[]{136,137},oggetto.getPercentualePossesso());
				setTemplateField(replaces,135,oggetto.getFlagPossessoProprieta());
				setTemplateField(replaces,134,oggetto.getFlagPossessoUsufrutto());
				setTemplateField(replaces,133,oggetto.getFlagPossessoUso());
				setTemplateField(replaces,132,oggetto.getFlagPossessoDirittoAbitazione());
				setTemplateField(replaces,131,oggetto.getFlagPossessoSuperficie());
				setTemplateField(replaces,130,oggetto.getFlagPossessoLeasing());
				setTemplateField(replaces,129,oggetto.getAltroPossesso());
				
				setTemplateField(replaces,138,oggetto.getFlagAbitazionePrincipale());
				setTemplateField(replaces,139,oggetto.getFlagAbitazionePrincipaleNoMore());
				setTemplateField(replaces,140,oggetto.getContitolariAbitazionePrincipale());

				setTemplateField(replaces,new int[]{141,142},oggetto.getFlagInagibile(),oggetto.getFlagInagibile() != null);
				setTemplateField(replaces,new int[]{143,144},oggetto.getFlagAgricolturaDiretta(),oggetto.getFlagAgricolturaDiretta() != null);
				setTemplateField(replaces,new int[]{145,146},oggetto.getFlagImmobileEscluso(),oggetto.getFlagImmobileEscluso() != null);
				setTemplateField(replaces,new int[]{147,148},oggetto.getFlagRiduzioneLocazione(),oggetto.getFlagRiduzioneLocazione() != null);
				setTemplateField(replaces,new int[]{149,150},oggetto.getFlagStorico(),oggetto.getFlagStorico() != null);

				setTemplateField(replaces,151,oggetto.getFlagDetrazioneDelibera());
				setTemplateField(replaces,152,oggetto.getFlagDetrazioneDeliberaNM());
				setTemplateField(replaces,153,oggetto.getMembriNucleoFamiliare());
				setTemplateField(replaces,155,oggetto.getFlagPensionato());
				setTemplateField(replaces,156,oggetto.getFlagConiugePensionato());
				setTemplateField(replaces,157,oggetto.getFlagDisoccupato());
				setTemplateField(replaces,158,oggetto.getFlagInvalido());
				setTemplateField(replaces,159,oggetto.getFlagMobilita());
				setTemplateField(replaces,160,oggetto.getFlagInterinale());
				setTemplateField(replaces,161,oggetto.getFlagCococo());
				
				setTemplateField(replaces,154,npage+1);
				
				// n-var in una botta sola nella pagina n (dettaglio)
				qcsaf.replaceVar(npage, replaces);
				npage++;
			}

			// una var nella pagina 1 (prima body)

			ServletOutputStream out = res.getOutputStream();

			// ---------------------------------------------------------------
			// Set the output data's mime type
			// ---------------------------------------------------------------
			res.reset();
			res.resetBuffer();
			res.setContentType("application/pdf"); // MIME type for pdf doc

			// ------------------------------------------------------------
			// Content-disposition header - don't open in browser and
			// set the "Save As..." filename.
			// *There is reportedly a bug in IE4.0 which ignores this...
			// ------------------------------------------------------------
			//res.setHeader("Content-disposition", "attachment; filename="
			//		+ "ComunicazioneICI_"+comunicazione.getCodiceFiscale()+ ".pdf");

			// -----------------------------------------------------------------
			// PROXY_HOST and PROXY_PORT should be your proxy host and port
			// that will let you go through the firewall without
			// authentication.
			// Otherwise set the system properties and use
			// URLConnection.getInputStream().
			// -----------------------------------------------------------------
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			try {
				// Use Buffered Stream for reading/writing.
				bis = new BufferedInputStream(new FileInputStream(qcsaf
						.actual()));
				bos = new BufferedOutputStream(out);

				byte[] buff = new byte[2048];
				int bytesRead;

				// Simple read/write loop.
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}

			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}
		} catch (COSVisitorException e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
	}

	protected void setTemplateField(Map replaces, int campo, String valore, int padding) {
		if(valore == null){
			valore = "";
		}
		for (int i = valore.length(); i < padding; i++) {
			valore += " ";
		}
		setTemplateField( replaces, campo, valore);
	}

	protected void setTemplateField(Map replaces, int campo, String valore) {
		if(valore == null){
			valore = "";
		}
		replaces.put("_v"+campo, valore);
	}

	protected void setTemplateField(Map replaces, int campo, char valore) {
		setTemplateField(replaces, campo, String.valueOf(valore));
	}

	protected void setTemplateField(Map replaces, int campo, Boolean valore) {
		setTemplateField(replaces, campo, (Boolean.TRUE.equals(valore)  ? "XX":null));
	}

	protected void setTemplateField(Map replaces, int campo[], Boolean valore) {
		setTemplateField(replaces,campo,valore,null);
	}

	protected void setTemplateField(Map replaces, int campo[], Boolean valore, Boolean cond) {
		if (cond == null || cond) {
			if (Boolean.TRUE.equals(valore)) {
				setTemplateField(replaces, campo[0], "XX");
				setTemplateField(replaces, campo[1]);
			} else {
				setTemplateField(replaces, campo[0]);
				setTemplateField(replaces, campo[1], "XX");
			}
		}
		else{
			setTemplateField(replaces, campo[0]);
			setTemplateField(replaces, campo[1]);
		}
	}

	protected void setTemplateField(Map replaces, int campo, char valore[]) {
		setTemplateField(replaces, campo, String.valueOf(valore));
	}

	protected void setTemplateField(Map replaces, int campo) {
		setTemplateField(replaces, campo, (String)null);
	}

	protected void setTemplateField(Map replaces, int campo[], Date valore) {
		if(valore == null ){
			for (int i = 0; i < campo.length; i++) {
				setTemplateField(replaces,campo[i]);
			}
		}
		else{
			try {
				String date = MuiFornituraParser.dateParser
				.format(valore);
				setTemplateField(replaces, campo[0], new char[]{date.charAt(0) , date.charAt(1)});
				setTemplateField(replaces, campo[1], new char[]{date.charAt(2) , date.charAt(3)});
				if(campo.length > 2 ){
					setTemplateField(replaces, campo[2], new char[]{date.charAt(4) , date.charAt(5),date.charAt(6) , date.charAt(7)});
				}
			} catch (Throwable t) {
				Logger.log().error("error while mapping date", valore,t);
				for (int i = 0; i < campo.length; i++) {
					setTemplateField(replaces,campo[i]);
				}
			}
		}
	}
	protected void setTemplateField(Map replaces, int campo, Integer valore) {
		if(valore != null){
			setTemplateField(replaces,campo,String.valueOf(valore));
		}
		else{
			setTemplateField(replaces,campo);
		}
	}
	protected void setTemplateField(Map replaces, int campo[], BigDecimal valore) {
		if(valore == null ){
			for (int i = 0; i < campo.length; i++) {
				setTemplateField(replaces,campo[i]);
			}
		}
		else{
			int num = valore.intValue();
			valore = valore.subtract(new BigDecimal(num));
			int dec = valore.multiply(new BigDecimal(100)).intValue();
			setTemplateField(replaces, campo[0], num);
			setTemplateField(replaces, campo[1], dec);
		}
	}

	protected void setTemplateField(Map replaces, int campo, Date valore) {
		if(valore == null ){
			setTemplateField(replaces,campo);
		}
		else{
			try {
				String date = MuiFornituraParser.yearParser
				.format(valore);
				setTemplateField(replaces,campo,date);
			} catch (Throwable t) {
				Logger.log().error("error while mapping date", valore,t);
				setTemplateField(replaces,campo);
			}
		}
	}

}