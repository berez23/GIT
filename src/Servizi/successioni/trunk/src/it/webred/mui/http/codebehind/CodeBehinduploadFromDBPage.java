package it.webred.mui.http.codebehind;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.input.MuiInvalidInputDataException;
import it.webred.mui.inputdb.MuiFornituraDB2TXTTransformer;
import it.webred.mui.model.MiDupFornitura;
import it.webred.successioni.output.ExportComunicazioniFornitura;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.skillbill.logging.Logger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;

public class CodeBehinduploadFromDBPage extends AbstractPage {

	static final DateFormat dateformatDB = new SimpleDateFormat("yyyy-MM-dd");

	   protected boolean controlImpl(HttpServletRequest req, HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
			
		String esporta = req.getParameter("esporta");
		String coniugato = req.getParameter("coniugato");
		if (req.getMethod().equalsIgnoreCase("post") || "S".equalsIgnoreCase(esporta)) {
			Connection conn;
			try {
				/*
				Context cont = new InitialContext();
				Context datasourceContext = (Context) cont.lookup("java:comp/env");

				DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
				conn = theDataSource.getConnection();
				*/
				
				conn = MuiApplication.getMuiApplication().getConnection();
				
			} catch (Exception e) {
				Logger.log().error(this.getClass().getName(),"Error retrieving connection! ",e);
				throw new ServletException(e);
			} 

			//    Da passare nel formato YYYY-MM-DD
			/*String dataInizio = req.getParameter("data_inizio"); 
			String dataFine = req.getParameter("data_fine"); */

			String anno = req.getParameter("anno");
			String mese = req.getParameter("mese");

			Logger.log().info(this.getClass().getName(), "Anno: " + anno);
			Logger.log().info(this.getClass().getName(), "Mese: " + mese);
			boolean estrazioneAnnuale =  (mese == null || mese.trim().length() == 0);
			
			String dataInizio;
			String dataFine;
			Calendar now = Calendar.getInstance();
			if (anno == null || anno.trim().length() == 0) {
				anno = "" + now.get(Calendar.YEAR);				
			}
			now.set(Calendar.YEAR, Integer.parseInt(anno));
			if (estrazioneAnnuale) {
				now.set(Calendar.MONTH, 0);
				now.set(Calendar.DAY_OF_MONTH, 1);
				dataInizio = dateformatDB.format(now.getTime());
				
				now.set(Calendar.YEAR, Integer.parseInt(anno) + 1);
				now.add(Calendar.DAY_OF_YEAR, -1); // Setto all'ultimo giorno del mese precedente
				dataFine = dateformatDB.format(now.getTime());
			} else {
				now.set(Calendar.MONTH, Integer.parseInt(mese) - 1);
				now.set(Calendar.DAY_OF_MONTH, 1);
				dataInizio = dateformatDB.format(now.getTime());

				now.set(Calendar.MONTH, Integer.parseInt(mese));
				now.set(Calendar.DAY_OF_MONTH, 1);
				now.add(Calendar.DAY_OF_YEAR, -1); // Setto all'ultimo giorno del mese precedente
				dataFine = dateformatDB.format(now.getTime());
			}

			try {
				Logger.log().info(this.getClass().getName(), "DataInizio: " + dataInizio);
				Logger.log().info(this.getClass().getName(), "DataFine: " + dataFine);
				final MuiFornituraParser parser = new MuiFornituraParser();
//						fileXML.deleteOnExit();

				HashMap mesi = new HashMap(12);
				mesi.put("01", "Gennaio");
				mesi.put("02", "Febbraio");
				mesi.put("03", "Marzo");
				mesi.put("04", "Aprile");
				mesi.put("05", "Maggio");
				mesi.put("06", "Giugno");
				mesi.put("07", "Luglio");
				mesi.put("08", "Agosto");
				mesi.put("09", "Settembre");
				mesi.put("10", "Ottobre");
				mesi.put("11", "Novembre");
				mesi.put("12", "Dicembre");

				String filenamePost = it.webred.utils.DateFormat.dateToString(new java.util.Date(), "yyyyMMdd");
				if("S".equalsIgnoreCase(coniugato))
					filenamePost += "Successioni_Coniugati_";
				else
					filenamePost += "Successioni_Celibi_";
					
				String filename = estrazioneAnnuale ?
						filenamePost + dataInizio.substring(0, 4) :
						filenamePost + mesi.get(dataInizio.substring(5, 7))+ "_" + dataInizio.substring(0, 4);

				final java.io.File fileTXT = java.io.File.createTempFile(filename, "txt");
//						fileTXT.deleteOnExit();
//						item.write(fileTXT);


				if ("S".equalsIgnoreCase(esporta)) {
/** INIZIO - CODICE PER SCARICARE IL FILE .TXT NEL NUOVO FORMATO */

					Logger.log().info(this.getClass().getName(), "Chiamata esportazione dati");

					Long iidFornitura = Long.valueOf( req.getParameter("iidFornitura"));
					Logger.log().info(this.getClass().getName(), "iidFornitura: " + iidFornitura);
					Session session = HibernateUtil.currentSession();
					String dataPresentazione = null;
					MiDupFornitura fornitura = null;
					try {
						fornitura = (MiDupFornitura)session.load(MiDupFornitura.class,iidFornitura);
						// ------------------------------------------------------------
						// Content-disposition header - don't open in browser and
						// set the "Save As..." filename.
						// *There is reportedly a bug in IE4.0 which ignores this...
						// ------------------------------------------------------------

						dataInizio = MuiFornituraDB2TXTTransformer.parseStringDateFromDDMMYYYY(fornitura.getDataIniziale());
						dataFine = MuiFornituraDB2TXTTransformer.parseStringDateFromDDMMYYYY(fornitura.getDataFinale());

						Logger.log().info(this.getClass().getName(), "Nuova dataInizio: " + dataInizio);
						Logger.log().info(this.getClass().getName(), "Nuova dataFine: " + dataFine);
						dataPresentazione = MuiFornituraDB2TXTTransformer.parseDateToYYYYMMDD(fornitura.getDataCaricamento());
						Logger.log().info(this.getClass().getName(), "DataPresentazione recuperata da Fornitura: " + dataPresentazione);
						
						anno = dataInizio.substring(0, 4);
						String meseInizio = dataInizio.substring(5, 7);
						String meseFine = dataFine.substring(5, 7);
						filename = it.webred.utils.DateFormat.dateToString(new java.util.Date(), "yyyyMMdd");

						if("S".equalsIgnoreCase(coniugato))
							filename += "Successioni_Coniugati_";
						else
							filename += "Successioni_Celibi_";
							
						filename = meseInizio.equalsIgnoreCase(meseFine) ?
								filename + mesi.get(meseInizio)+ "_" + dataInizio.substring(0, 4) :
									filename + anno;

						Logger.log().info(this.getClass().getName(), "filename: " + filename);

					} finally {
						HibernateUtil.closeSession();
					}
					
					Logger.log().info(this.getClass().getName(), "Chiamata a exportComunicazioni.readDataFromDB");
					ExportComunicazioniFornitura exportComunicazioni = new ExportComunicazioniFornitura();
					Date switchOffDate = MuiFornituraParser.dateParser.parse("30042006");
					boolean isAfter2006 = fornitura != null && fornitura.getDataFinaleDate() != null ? 
											fornitura.getDataFinaleDate().after(switchOffDate) : 
											false;
					StringBuffer sbOut = exportComunicazioni.readDataFromDB("" + iidFornitura, dataPresentazione, coniugato, conn, isAfter2006);
				    String filenameWithExt = filename + ".txt";
					resp.setContentType("application/x-download");
			        resp.setHeader("Content-disposition", "attachment;  filename=\"" + filenameWithExt + "\"");

			        BufferedOutputStream bos = null;
			        try {
			           bos = new BufferedOutputStream(resp.getOutputStream());           
			           bos.write(sbOut.toString().getBytes());
			        }
			        catch (IOException ex) {
			           ex.printStackTrace();
			        }
			        finally {
			           if (bos != null) {
			             bos.close();
			           }
			        }
/** FINE - CODICE PER SCARICARE IL FILE .TXT NEL NUOVO FORMATO */
					
				} else {
					MuiFornituraDB2TXTTransformer transformer = new MuiFornituraDB2TXTTransformer();
/** INIZIO - CODICE PER CARICARE LA FORNITURA CON LA CLASSE MuiFornituraParser */
					Logger.log().info(this.getClass().getName(), "Inizio recupero dati");
					transformer.readDataSuccFromDB(dataInizio, dataFine, new FileWriter(fileTXT), conn);
				    parser.setInput(new FileInputStream(fileTXT));
					Logger.log().info(this.getClass().getName(), "Inizio parse dei dati");
					parser.parse(true);
					Logger.log().info(this.getClass().getName(), "Parse ultima linea dei dati");
					parser.parseLastLine(parser.getLastLine(fileTXT));
					Logger.log().info(this.getClass().getName(), "Creazione Thread");
					Thread t = new Thread() {
						public void run() {
							parser.parseNotes();
							fileTXT.delete();
						}
					};
					Logger.log().info(this.getClass().getName(), "Start Thread");
					t.start();
	/** FINE - CODICE PER CARICARE LA FORNITURA CON LA CLASSE MuiFornituraParser */
					
	/** INIZIO - CODICE PER SCARICARE IL FILE .TXT GENERATO, SENZA CARICARLO */
/*
					StringBuffer sbOut = transformer.readDataSuccFromDB(dataInizio, dataFine, null, conn);
				    String filenameWithExt = filename + ".txt";
			        resp.setContentType("application/x-download");
			        resp.setHeader("Content-disposition", "attachment;  filename=\"" + filenameWithExt + "\"");

			        BufferedOutputStream bos = null;
			        try {
			           bos = new BufferedOutputStream(resp.getOutputStream());           
			           bos.write(sbOut.toString().getBytes());
			        }
			        catch (IOException ex) {
			           ex.printStackTrace();
			        }
			        finally {
			           if (bos != null) {
			             bos.close();
			           }
			        }
*/
/** FINE - CODICE PER SCARICARE IL FILE .TXT GENERATO, SENZA CARICARLO */
				}
				

			} catch (MuiInvalidInputDataException e) {
				Logger.log().error(this.getClass().getName(),"Lancio MuiInvalidInputDataException");
				throw e;
			} catch (it.webred.mui.MuiException e) {
				Logger.log().error(this.getClass().getName(),"Lancio MuiException");
				throw e;
			} catch (Exception e) {
				Logger.log().error(this.getClass().getName(), "error", e);
				Logger.log().error(this.getClass().getName(),"Lancio ServletException");
				throw new ServletException(e);
			}

			req.setAttribute("requestedUri", "uploadResultFromDB.jsp");
			req.getRequestDispatcher("uploadResultFromDB.jsp").include(req, resp);
			Logger.log().info(this.getClass().getName(), "Return FALSE");
			return false;
		}
		Logger.log().info(this.getClass().getName(), "Return TRUE");
		return true;
	}
}
