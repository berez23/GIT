package net.skillbill.webred.pdf;

import it.webred.docfa.model.Docfa;
import it.webred.docfa.model.DocfaUIUTitolareBean;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.codebehind.CodeBehinddocfaDetailPage;
import it.webred.mui.http.codebehind.CodeBehindnoteDetailPage;
import it.webred.mui.model.CodiciDup;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupIndirizziSog;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTerreniInfo;
import it.webred.mui.model.MiDupTitolarita;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Servlet implementation class for Servlet: PdfNotaServlet
 * 
 */
public class PdfDocfaServlet extends MuiBaseServlet
{
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	Font	descFieldFont	= FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD);
	Font	valFieldFont	= FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL);

	public PdfDocfaServlet()
	{
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see MuiBaseServlet#muiService(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void muiService(HttpServletRequest req, HttpServletResponse res)
		throws ServletException,
		java.io.IOException
	{
		// un report ICI con due pagine "body"
		try
		{

			Logger.log().info(this.getClass().getName(), "docfa pdf request query String=" + req.getQueryString());
			setDocfa(req);



			List<MiDupTitolarita> titolaritaFavoreNota = (List<MiDupTitolarita>) req.getAttribute("titolaritaFavoreNota");
			List<MiDupTitolarita> titolaritaControNota = (List<MiDupTitolarita>) req.getAttribute("titolaritaControNota");
			List<MiDupTerreniInfo> terreniNota = (List<MiDupTerreniInfo>) req.getAttribute("terreniNota");
			List<MiDupFabbricatiInfo> fabbricatiNota = (List<MiDupFabbricatiInfo>) req.getAttribute("fabbricatiNota");
			List<MiDupSoggetti> soggettiNonCoinvolti = (List<MiDupSoggetti>) req.getAttribute("soggettiNonCoinvoltiNota");
			
			Docfa docfaMy = (Docfa) req.getAttribute("docfa");

			

			// step 1: creation of a document-object
			Document document = new Document(PageSize.A4.rotate());

			ServletOutputStream out = res.getOutputStream();

			// ---------------------------------------------------------------
			// Set the output data's mime type
			// ---------------------------------------------------------------
			res.reset();
			res.resetBuffer();
			res.setContentType("application/pdf"); // MIME type for pdf doc
			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file

			PdfWriter.getInstance(document, out);

			// step 3: we open the document
			document.open();

			// step 4: we add some content
			Paragraph titolo = new Paragraph("DOCFA - DETTAGLIO DOCFA " + getProperty("protocollo", docfaMy) + "-" + getProperty("fornitura", docfaMy) );
			titolo.font().setSize(14);
			titolo.font().setStyle(Font.HELVETICA);
			titolo.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(titolo);

			Paragraph docfaDettRow = new Paragraph();
			docfaDettRow.setAlignment(Paragraph.ALIGN_LEFT);
			addField("Data Registrazione: ", "dataRegistrazione", docfaMy, docfaDettRow,true);
			addField("Data Variazione: ", "dataVariazione", docfaMy, docfaDettRow,true);
			addField("Causale:", "causale", docfaMy, docfaDettRow);

			document.add(docfaDettRow);

			docfaDettRow = new Paragraph();
			docfaDettRow.setAlignment(Paragraph.ALIGN_LEFT);
			addField("Sopressione: ", "soppressione", docfaMy, docfaDettRow);
			addField("Variazione: ", "variazione", docfaMy, docfaDettRow);
			addField("Nuove: ", "costituzione", docfaMy, docfaDettRow);
			addField("Deriv spe.: ", "derivSpe", docfaMy, docfaDettRow);
			document.add(docfaDettRow);
			
			// rifermenti temporali fabbricato
			List listaDocfaParteUnof = (List)req.getAttribute("listaDocfaParteUno");			
			if(listaDocfaParteUnof != null && listaDocfaParteUnof.size()>0)
			{
				Iterator itF = listaDocfaParteUnof.iterator();
				if(itF.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("RIFERIMENTI TEMPORALI FABBRICATO:", p, " ");
					document.add(p);
				}
				while (itF.hasNext())
				{
					Docfa parteUno = (Docfa)itF.next();
					Paragraph fabbricatoDettRow = new Paragraph();
					fabbricatoDettRow.setAlignment(Paragraph.ALIGN_LEFT);
					if(parteUno.getAnnoCostruzione().equals("1900"))
						addPair("Anno Costruzione :", fabbricatoDettRow, "antec. 1942");
					else
						addPair("Anno Costruzione :", fabbricatoDettRow, "      "+parteUno.getAnnoCostruzione());
					
					if(parteUno.getAnnoRistrutturazione().equals("0"))
						addPair("	Anno Ristrutturazione Totale: ", fabbricatoDettRow, "    ");
					else
						addPair("	Anno Ristrutturazione Totale: ", fabbricatoDettRow, parteUno.getAnnoRistrutturazione());
					document.add(fabbricatoDettRow);
	
				}
			}
			
			// LISTA INTESTATI
			List listaIntestati = (List)req.getAttribute("listaDocfaIntestati");			
			if(listaIntestati != null && listaIntestati.size()>0)
			{
				Iterator itInt = listaIntestati.iterator();
				if(itInt.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("LISTA INTESTATI:", p, " ");
					document.add(p);
				}
				while (itInt.hasNext())
				{
					Docfa docfaInt = (Docfa)itInt.next();
					Paragraph intestatiDettRow = new Paragraph();
					intestatiDettRow.setAlignment(Paragraph.ALIGN_LEFT);
					intestatiDettRow.setIndentationLeft(20);
					addPair("Denominazione: ", intestatiDettRow, docfaInt.getDenominazione());
					addPair("	Codice Fiscale: ", intestatiDettRow, docfaInt.getCodiceFiscale());
					addPair("	Partita Iva: ", intestatiDettRow, docfaInt.getPartitaIva());
					document.add(intestatiDettRow);
				}			
			}
			// LISTA DICHIARANTI
			List listaDichiaranti = (List)req.getAttribute("listaDocfaDichiaranti");
			if(listaDichiaranti != null && listaDichiaranti.size()>0)
			{
				Iterator itDic = listaDichiaranti.iterator();
				if(itDic.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("LISTA DICHIARANTI:", p, " ");
					document.add(p);
				}
				while (itDic.hasNext())
				{
					Docfa docfaInt = (Docfa)itDic.next();
					Paragraph dichiarantiDettRow = new Paragraph();
					dichiarantiDettRow.setIndentationLeft(20);
					dichiarantiDettRow.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("Cognome: ", dichiarantiDettRow, docfaInt.getCognome());
					addPair("	Nome: ", dichiarantiDettRow, docfaInt.getNome());
					addPair("	Indirizzo: ", dichiarantiDettRow, docfaInt.getIndirizzoDichiarante());
					addPair("	Luogo: ", dichiarantiDettRow, docfaInt.getLuogo());
					document.add(dichiarantiDettRow);
				}	
			}

			// LISTA UIU
			List listaDocfaUiu = (List)req.getAttribute("listaDocfaUiu");
			if(listaDocfaUiu != null && listaDocfaUiu.size()>0)
			{
				Iterator itUiu = listaDocfaUiu.iterator();
				if(itUiu.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("LISTA UIU:", p, " ");
					document.add(p);
				}
				while (itUiu.hasNext())
				{
					Docfa docfaUiu = (Docfa)itUiu.next();
					Paragraph uiuDettRow = new Paragraph();
					uiuDettRow.setAlignment(Paragraph.ALIGN_LEFT);				
					uiuDettRow.setIndentationLeft(20);
					addField("Graffati:", "presenzaGraffati", docfaUiu, uiuDettRow);
					addField("	Tipo: ", "tipo", docfaUiu, uiuDettRow);
					addField("	Foglio: ", "foglio", docfaUiu, uiuDettRow);
					addField("	Particella: ", "particella", docfaUiu, uiuDettRow);
					addField("	Subalterno: ", "subalterno", docfaUiu, uiuDettRow);
					String ind = "";
					Iterator itInd = docfaUiu.getIndPart().iterator();
					while (itInd.hasNext())
					{
						ind +=itInd.next()+ " ";
					}						
					addPair("	Indirizzo: ", uiuDettRow, ind);
					document.add(uiuDettRow);
					//intestatari
					List listaDocfaIntestatari = docfaUiu.getElencoTitolari();					
					if(listaDocfaIntestatari != null && listaDocfaIntestatari.size()>0)
					{
						Iterator itInte = listaDocfaIntestatari.iterator();
						if(itInte.hasNext())
						{
							Paragraph p = new Paragraph();
							p.setAlignment(Paragraph.ALIGN_LEFT);
							addPair("			LISTA INTESTATARI :", p, " ");
							document.add(p);
						}
						while (itInte.hasNext())
						{
							DocfaUIUTitolareBean docfaInte = (DocfaUIUTitolareBean)itInte.next();
							Paragraph inteDettRow = new Paragraph();
							inteDettRow.setAlignment(Paragraph.ALIGN_LEFT);	
							inteDettRow.setIndentationLeft(40);
							addPair("Denominazione:", inteDettRow, docfaInte.getDenominazione()+" "+docfaInte.getNome());
							if(docfaInte.getCodiceFiscale() != null && !docfaInte.getCodiceFiscale().equals("-"))
							{
								addField("	C.F.: ", "codiceFiscale", docfaInte, inteDettRow);
								addField("	Data Inizio: ", "dataNascita", docfaInte, inteDettRow,true);
								addField("	Comune Nascita: ", "descrComuneNascita", docfaInte, inteDettRow);
								addField("	Provincia Nascita: ", "provinciaNascita", docfaInte, inteDettRow);
								addField("	Residenza: ", "indirizzoResidenza", docfaInte, inteDettRow);								
							}
							else
							{
								addField("	P.I.: ", "partitaIva", docfaInte, inteDettRow);
								addField("	Sede: ", "subalterno", docfaInte, inteDettRow);
								addField("	SedeLegale: ", "subalterno", docfaInte, inteDettRow);
							}
							addField("-", "capResidenza", docfaInte, inteDettRow);
							addField(" ", "comuneResidenza", docfaInte, inteDettRow);
							addField(" ", "provinciaResidenza", docfaInte, inteDettRow);														
							addField("	% Possesso: ", "percentualePossesso", docfaInte, inteDettRow);
							document.add(inteDettRow);
						}	
					}
				}	
			}			

			
			
			// BENI COMUNI NON CENSIBILI
			List listaDocfabeniNonCens = (List)req.getAttribute("listaDocfabeniNonCens");
			if(listaDocfabeniNonCens != null && listaDocfabeniNonCens.size()>0)
			{
				Iterator itBnc = listaDocfabeniNonCens.iterator();
				if(itBnc.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("BENI COMUNI NON CENSIBILI:", p, " ");
					document.add(p);
				}
				while (itBnc.hasNext())
				{
					Docfa docfaBnc = (Docfa)itBnc.next();
					Paragraph bncDettRow = new Paragraph();
					bncDettRow.setIndentationLeft(20);
					bncDettRow.setAlignment(Paragraph.ALIGN_LEFT);
					addField("Foglio: ", "foglio", docfaBnc, bncDettRow);
					addField("	Particella: ", "particella", docfaBnc, bncDettRow);
					addField("	Sub.: ", "subalterno", docfaBnc, bncDettRow);
					addField("	Foglio 2: ", "foglio2", docfaBnc, bncDettRow);
					addField("	Particella 2: ", "particella2", docfaBnc, bncDettRow);
					addField("	Sub. 2: ", "subalterno2", docfaBnc, bncDettRow);
					addField("	Foglio 3: ", "foglio3", docfaBnc, bncDettRow);
					addField("	Particella 3: ", "particella3", docfaBnc, bncDettRow);
					addField("	Sub. 3: ", "subalterno3", docfaBnc, bncDettRow);
					document.add(bncDettRow);
				}	
			}			
			
			// ANNOTAZIONI
			List listaDocfaAnnotazioni = (List)req.getAttribute("listaDocfaAnnotazioni");
			if(listaDocfaAnnotazioni != null && listaDocfaAnnotazioni.size()>0)
			{
				Iterator itAno = listaDocfaAnnotazioni.iterator();
				if(itAno.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("ANNOTAZIONI:", p, " ");
					document.add(p);
				}
				while (itAno.hasNext())
				{
					Docfa ano = (Docfa)itAno.next();
					Paragraph bncDettRow = new Paragraph();
					bncDettRow.setIndentationLeft(20);
					bncDettRow.setAlignment(Paragraph.ALIGN_LEFT);
					addField(" ", "annotazioni", ano, bncDettRow);
					document.add(bncDettRow);
				}	
			}
			
			// DATI CENSUARI
			List listaDocfaDatiCensuari = (List)req.getAttribute("listaDocfaDatiCensuari");
			if(listaDocfaDatiCensuari != null && listaDocfaDatiCensuari.size()>0)
			{
				Iterator itDc = listaDocfaDatiCensuari.iterator();
				if(itDc.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("DATI CENSUARI:", p, " ");
					document.add(p);
				}
				while (itDc.hasNext())
				{
					Docfa docfaBnc = (Docfa)itDc.next();
					Paragraph dcDettRow = new Paragraph();
					dcDettRow.setIndentationLeft(20);
					dcDettRow.setAlignment(Paragraph.ALIGN_LEFT);
					addField("Graffati: ", "presenzaGraffati", docfaBnc, dcDettRow);
					addField("	Foglio: ", "foglio", docfaBnc, dcDettRow);
					addField("	Particella: ", "particella", docfaBnc, dcDettRow);
					addField("	Sub.: ", "subalterno", docfaBnc, dcDettRow);
					addField("	Classe: ", "classe", docfaBnc, dcDettRow);
					addField("	Zona: ", "zona", docfaBnc, dcDettRow);
					addField("	Categoria: ", "categoria", docfaBnc, dcDettRow);
					addField("	Consistenza: ", "consistenza", docfaBnc, dcDettRow);
					addField("	Superficie: ", "superfice", docfaBnc, dcDettRow);
					addField("	Rendita: ", "rendita", docfaBnc, dcDettRow);
					addField("	Ident. Imm.: ", "identificativoImm", docfaBnc, dcDettRow);
					document.add(dcDettRow);
				}	
			}				


			// LISTA PARTE UNO
			List listaDocfaParteUno = (List)req.getAttribute("listaDocfaParteUno");
			if(listaDocfaParteUno != null && listaDocfaParteUno.size()>0)
			{
				Iterator itp1 = listaDocfaParteUno.iterator();
				if(itp1.hasNext())
				{
					Paragraph p = new Paragraph();
					p.setAlignment(Paragraph.ALIGN_LEFT);
					addPair("LISTA PARTE UNO:", p, " ");
					document.add(p);
				}
				while (itp1.hasNext())
				{
					Docfa docfap1 = (Docfa)itp1.next();
					Paragraph p1DettRow = new Paragraph();
					p1DettRow.setIndentationLeft(20);
					p1DettRow.setAlignment(Paragraph.ALIGN_LEFT);
					addField("Foglio: ", "foglio", docfap1, p1DettRow);
					addField("	Particella: ", "numero", docfap1, p1DettRow);
					addField("	Anno Cost.: ", "annoCostruzione", docfap1, p1DettRow);
					addField("	Anno Rist.: ", "annoRistrutturazione", docfap1, p1DettRow);
					addField("	Posiz. Fab.: ", "posizFabbr", docfap1, p1DettRow);
					addField("	Tipo Acces.: ", "tipoAccesso", docfap1, p1DettRow);

					document.add(p1DettRow);
				}	
			}	

			// step 5: we close the document
			document.close();

			out.flush();

		}
		catch (Throwable e)
		{
			// TODO Auto-generated catch block
			Logger.log().error(this.getClass().getName(), "error while pdf of docfa", e);
			throw new ServletException(e);
		}
	}

	private String getRenditaImmobile(MiDupFabbricatiInfo fabbricato)
	{
		String res = null;
		try
		{
			double resD = Integer.valueOf(fabbricato.getRenditaEuro()).intValue() / 100;
			res = "" + resD;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			res = null;
		}
		return res;
	}

	private void addField(String desc, String property, Object nota, Paragraph notaDettRow)
		throws IllegalAccessException,
		InvocationTargetException,
		NoSuchMethodException
	{
		addField(desc, property, nota, notaDettRow, false);
	}

	private void addField(String desc, String property, Object nota, Paragraph notaDettRow, boolean isData)
		throws IllegalAccessException,
		InvocationTargetException,
		NoSuchMethodException
	{
		String pValue = null;
		if (property != null)
		{
			pValue = getProperty(property, nota, isData);
		}
		else
		{
			pValue = " ";
		}
		addPair(desc, notaDettRow, pValue);
	}

	private void addPair(String desc, Paragraph notaDettRow, String pValue)
	{
		Chunk chunk = new Chunk(desc, descFieldFont);
		notaDettRow.add(chunk);
		if (pValue != null)
		{
			chunk = new Chunk(pValue, valFieldFont);
		}
		else
		{
			chunk = new Chunk(" ", valFieldFont);
		}

		notaDettRow.add(chunk);
		chunk = new Chunk("	", valFieldFont);
		notaDettRow.add(chunk);
	}

	protected String getProperty(String property, Object nota, boolean isData)
		throws IllegalAccessException,
		InvocationTargetException,
		NoSuchMethodException
	{
		String pValue = BeanUtils.getProperty(nota, property);
		if (pValue != null && pValue.trim().length() > 0)
		{
			if (isData && pValue.trim().length() == 8)
			{
				pValue = pValue.substring(0, 2) + "/" + pValue.substring(2, 4) + "/" + pValue.substring(4);
			}
		}
		else
		{
			pValue = " ";
		}
		return pValue;
	}

	protected String getProperty(String property, Object nota)
		throws IllegalAccessException,
		InvocationTargetException,
		NoSuchMethodException
	{
		return getProperty(property, nota, false);
	}

	private void setDocfa(HttpServletRequest req)
	{
		String docfaKey = req.getParameter("idFornitura") + "-" + req.getParameter("protocollo");
		CodeBehinddocfaDetailPage.storeDocfa(req, docfaKey);
	}
}