package net.skillbill.webred.pdf;

import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
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
public class PdfNotaServlet extends MuiBaseServlet {
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	Font descFieldFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD);
	Font valFieldFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL);
	public PdfNotaServlet() {
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
		try {
			
			Logger.log().info(this.getClass().getName(),
					"nota pdf request query String=" + req.getQueryString());
			setNota(req);
			//HashMap belfioreMap = (HashMap)MuiApplication.getMuiApplication().getServletContext().getAttribute(BELFIORE_VARNAME);
			MiDupNotaTras nota = (MiDupNotaTras) req.getAttribute("nota");
		    
		    List<MiDupTitolarita> titolaritaFavoreNota = (List<MiDupTitolarita>)req.getAttribute("titolaritaFavoreNota");
		    List<MiDupTitolarita> titolaritaControNota = (List<MiDupTitolarita>)req.getAttribute("titolaritaControNota");
		    List<MiDupTerreniInfo> terreniNota = (List<MiDupTerreniInfo>)req.getAttribute("terreniNota");
		    List<MiDupFabbricatiInfo> fabbricatiNota = (List<MiDupFabbricatiInfo>)req.getAttribute("fabbricatiNota");
		    List<MiDupSoggetti> soggettiNonCoinvolti = (List<MiDupSoggetti>)req.getAttribute("soggettiNonCoinvoltiNota");

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
			Paragraph titolo = new Paragraph("SUCCESSIONI - DETTAGLIO NOTA "+getProperty("numeroNotaTras",nota)+"/"+getProperty("annoNota",nota)+" valida dal "+getProperty("dataValiditaAtto",nota,true));
			titolo.font().setSize(14);
			titolo.font().setStyle(Font.HELVETICA);
			titolo.setAlignment(Paragraph.ALIGN_CENTER);
			document
					.add(titolo);
	        
			Paragraph notaDettRow = new Paragraph();
			notaDettRow.setAlignment(Paragraph.ALIGN_LEFT);
			addField("Tipo:","tipoNota",nota, notaDettRow);
			addField("Registro particolare n.",null,nota, notaDettRow);
			addField("Data presentazione Atto ","dataPresAtto",nota, notaDettRow,true);
			addField("Data in Diff. ","dataInDif",nota, notaDettRow,true);
			addField("Nominativo Rog.","cognomeNomeRog",nota, notaDettRow);
			addField("Cod. Fisc Rog.","codFiscRog",nota, notaDettRow);
			document
			.add(notaDettRow);

			notaDettRow = new Paragraph();
			notaDettRow.setAlignment(Paragraph.ALIGN_LEFT);
			addField("Sede Rog.","sedeRog",nota, notaDettRow);
			addField("Anno ","annoNota",nota, notaDettRow);
			addField("N. repertorio ","numeroRepertorio",nota, notaDettRow);
			addField("Data Validita Atto ","dataValiditaAtto",nota, notaDettRow,true);
			addField("Esito ","esitoNota",nota, notaDettRow);
			addField("Esito non registrato ","esitoNotaNonReg",nota, notaDettRow);
			addField("Data reg. Attività ","dataRegInAtti",nota, notaDettRow,true);
			addField("Rettifica ","flagRettifica",nota, notaDettRow);
			document
			.add(notaDettRow);

			//fabbricati
			Iterator<MiDupFabbricatiInfo> fabbricati = fabbricatiNota.iterator();
			int elemCount = 0;
			while (fabbricati.hasNext()) {
				MiDupFabbricatiInfo fabbricato = fabbricati.next();
				Paragraph fabbricatoDettRow = new Paragraph();
				fabbricatoDettRow.setAlignment(Paragraph.ALIGN_LEFT);
				addPair("Fabbricato n."+(elemCount+1),fabbricatoDettRow," ");
				document
				.add(fabbricatoDettRow);
				fabbricatoDettRow = new Paragraph();
				addPair("	",fabbricatoDettRow," ");
				addPair("Comune ",fabbricatoDettRow,MuiApplication.descComune);
				addPair("Sez.",fabbricatoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getSezioneUrbana());
				addPair("Fgl.",fabbricatoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getFoglio());
				addPair("Part.",fabbricatoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getNumero());
				addPair("Sub.",fabbricatoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getSubalterno());
				addField("ID Catast.","idCatastaleImmobile",fabbricato, fabbricatoDettRow);
				addPair("Graff.",fabbricatoDettRow,("1".equalsIgnoreCase( fabbricato.getFlagGraffato())?"S":"N"));
				addField("Cat.","categoria",fabbricato, fabbricatoDettRow);
				addField("mq.","mq",fabbricato, fabbricatoDettRow);
				addField("vani ","vani",fabbricato, fabbricatoDettRow);
				addField("mc.","mc",fabbricato, fabbricatoDettRow);
				document
				.add(fabbricatoDettRow);
				fabbricatoDettRow = new Paragraph();
				addPair("	",fabbricatoDettRow," ");
				addField("Ind.Tras.","TIndirizzo",fabbricato, fabbricatoDettRow);
				addField("Ind.Cat.","CIndirizzo",fabbricato, fabbricatoDettRow);
				addField("Sc.","TScala",fabbricato, fabbricatoDettRow);
				addField("Int.","TInterno1",fabbricato, fabbricatoDettRow);
				addField("Piano ","TPiano1",fabbricato, fabbricatoDettRow);
				addField("Edif.","TEdificio",fabbricato, fabbricatoDettRow);
				addField("Lotto ","TLotto",fabbricato, fabbricatoDettRow);
				addPair("Rendita ", fabbricatoDettRow,getRenditaImmobile(fabbricato));
				document
				.add(fabbricatoDettRow);
				elemCount++;
			}

			//terreni
			Iterator<MiDupTerreniInfo> terreni = terreniNota.iterator();
			elemCount = 0;
			while (terreni.hasNext()) {
				MiDupTerreniInfo terreno = terreni.next();
				Paragraph terrenoDettRow = new Paragraph();
				terrenoDettRow.setAlignment(Paragraph.ALIGN_LEFT);
				addPair("Terreno n."+(elemCount+1),terrenoDettRow," ");
				document
				.add(terrenoDettRow);
				terrenoDettRow = new Paragraph();
				addPair("	",terrenoDettRow," ");
				addPair("Comune ",terrenoDettRow,MuiApplication.descComune);
				addPair("Sez.",terrenoDettRow,terreno.getSezioneCensuaruia());
				addPair("Fgl.",terrenoDettRow,terreno.getFoglio());
				addPair("Part.",terrenoDettRow,terreno.getNumero());
				addPair("Sub.",terrenoDettRow,terreno.getSubalterno());
				addField("ID Catast.","idCatastaleImmobile",terreno, terrenoDettRow);
				addPair("Graff.",terrenoDettRow,"N");
				addField("Edif.","edificabilita",terreno, terrenoDettRow);
				addField("Ettari ","ettari",terreno, terrenoDettRow);
				addField("Are ","are",terreno, terrenoDettRow);
				addField("Centiarie","centiare",terreno, terrenoDettRow);
				document
				.add(terrenoDettRow);
				elemCount++;
			}

			//titolarita a favore
			Iterator<MiDupTitolarita> titolaritas = titolaritaFavoreNota.iterator();
			elemCount = 0;
			while (titolaritas.hasNext()) {
				MiDupTitolarita titolarita = titolaritas.next();
				MiDupSoggetti soggetto = titolarita.getMiDupSoggetti();
				Paragraph soggettoDettRow = new Paragraph();
				soggettoDettRow.setAlignment(Paragraph.ALIGN_LEFT);
				addPair("Soggetto a favore n."+(elemCount+1),soggettoDettRow," ");
				document
				.add(soggettoDettRow);
				soggettoDettRow = new Paragraph();
				addPair("	",soggettoDettRow," ");
				addField("Cod.Fisc.",(soggetto.isGiuridico()?"codiceFiscaleG":"codiceFiscale"),soggetto, soggettoDettRow);
				addField("Tipo ","tipoSoggetto",soggetto, soggettoDettRow);
				if(soggetto.isGiuridico()){
					addField("Denominazione ","denominazione",soggetto, soggettoDettRow);
					addField("Sede ","sede",soggetto, soggettoDettRow);
				}
				else{
					addField("Cognome ","cognome",soggetto, soggettoDettRow);
					addField("Nome ","nome",soggetto, soggettoDettRow);
					addPair("Sesso ",soggettoDettRow,(soggetto.isGiuridico()? "" :( "1".equals(soggetto.getSesso())?"M":"F") ));
					addField("Data Nasc.","dataNascita",soggetto, soggettoDettRow,true);
				}
				MiDupIndirizziSog indirizzo = null;
				try {
					indirizzo =	soggetto.getMiDupIndirizziSogs().iterator().next();
				} catch (Exception e) {
					indirizzo = null;
				}
				if(indirizzo != null){
					addPair("Indirizzo ",soggettoDettRow,indirizzo.getIndirizzo()+ " " +indirizzo.getComune()+ " " +indirizzo.getProvincia()+ " " +indirizzo.getCap());
				}
				document
				.add(soggettoDettRow);
				soggettoDettRow = new Paragraph();
				addPair("	",soggettoDettRow," ");
				MiDupFabbricatiInfo fabbricato = null;
				fabbricato = titolarita.getMiDupFabbricatiInfo();
				if(fabbricato != null){
					addPair("Fgl.",soggettoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getFoglio());
					addPair("Part.",soggettoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getNumero());
					addPair("Sub.",soggettoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getSubalterno());
				}
				MiDupTerreniInfo terreno = null;
				terreno = titolarita.getMiDupTerreniInfo();
				if(terreno != null){
					addPair("Fgl.",soggettoDettRow,terreno.getFoglio());
					addPair("Part.",soggettoDettRow,terreno.getNumero());
					addPair("Sub.",soggettoDettRow,terreno.getSubalterno());
				}
				CodiciDup codicedup = null;
				codicedup = titolarita.getCodiceDup();
				if(codicedup != null){
					addField("Diritto ","descrizione",codicedup, soggettoDettRow);
				}
				else{
					addPair("Diritto ",soggettoDettRow," ");
				}
				addField("Quota poss. ","quota",titolarita, soggettoDettRow);
				document
				.add(soggettoDettRow);
				elemCount++;
			}

			//titolarita contro
			titolaritas = titolaritaControNota.iterator();
			elemCount = 0;
			while (titolaritas.hasNext()) {
				MiDupTitolarita titolarita = titolaritas.next();
				MiDupSoggetti soggetto = titolarita.getMiDupSoggetti();
				Paragraph soggettoDettRow = new Paragraph();
				soggettoDettRow.setAlignment(Paragraph.ALIGN_LEFT);
				addPair("Soggetto contro n."+(elemCount+1),soggettoDettRow," ");
				document
				.add(soggettoDettRow);
				soggettoDettRow = new Paragraph();
				addPair("	",soggettoDettRow," ");
				addField("Cod.Fisc.",(soggetto.isGiuridico()?"codiceFiscaleG":"codiceFiscale"),soggetto, soggettoDettRow);
				addField("Tipo ","tipoSoggetto",soggetto, soggettoDettRow);
				if(soggetto.isGiuridico()){
					addField("Denominazione ","denominazione",soggetto, soggettoDettRow);
					addField("Sede ","sede",soggetto, soggettoDettRow);
				}
				else{
					addField("Cognome ","cognome",soggetto, soggettoDettRow);
					addField("Nome ","nome",soggetto, soggettoDettRow);
					addPair("Sesso ",soggettoDettRow,(soggetto.isGiuridico()? "" :( "1".equals(soggetto.getSesso())?"M":"F") ));
					addField("Data Nasc.","dataNascita",soggetto, soggettoDettRow,true);
				}
				MiDupIndirizziSog indirizzo = null;
				try {
					indirizzo =	soggetto.getMiDupIndirizziSogs().iterator().next();
				} catch (Exception e) {
					indirizzo = null;
				}
				if(indirizzo != null){
					addPair("Indirizzo ",soggettoDettRow,indirizzo.getIndirizzo()+ " " +indirizzo.getComune()+ " " +indirizzo.getProvincia()+ " " +indirizzo.getCap());
				}
				document
				.add(soggettoDettRow);
				soggettoDettRow = new Paragraph();
				addPair("	",soggettoDettRow," ");
				MiDupFabbricatiInfo fabbricato = null;
				fabbricato = titolarita.getMiDupFabbricatiInfo();
				if(fabbricato != null){
					addPair("Fgl.",soggettoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getFoglio());
					addPair("Part.",soggettoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getNumero());
					addPair("Sub.",soggettoDettRow,fabbricato.getMiDupFabbricatiIdentificas().iterator().next().getSubalterno());
				}
				MiDupTerreniInfo terreno = null;
				terreno = titolarita.getMiDupTerreniInfo();
				if(terreno != null){
					addPair("Fgl.",soggettoDettRow,terreno.getFoglio());
					addPair("Part.",soggettoDettRow,terreno.getNumero());
					addPair("Sub.",soggettoDettRow,terreno.getSubalterno());
				}
				CodiciDup codicedup = null;
				codicedup = titolarita.getCodiceDup();
				if(codicedup != null){
					addField("Diritto ","descrizione",codicedup, soggettoDettRow);
				}
				else{
					addPair("Diritto ",soggettoDettRow," ");
				}
				addField("Quota poss. ","quota",titolarita, soggettoDettRow);
				document
				.add(soggettoDettRow);
				elemCount++;
			}

			//soggetti non coinvolti
			Iterator<MiDupSoggetti> noncoinvolti = soggettiNonCoinvolti.iterator(); 
			elemCount = 0;
			while (noncoinvolti.hasNext()) {
				MiDupSoggetti soggetto = noncoinvolti.next();
				Paragraph soggettoDettRow = new Paragraph();
				soggettoDettRow.setAlignment(Paragraph.ALIGN_LEFT);
				addPair("Soggetto non coinvolto n."+(elemCount+1),soggettoDettRow," ");
				document
				.add(soggettoDettRow);
				soggettoDettRow = new Paragraph();
				addPair("	",soggettoDettRow," ");
				addField("Cod.Fisc.",(soggetto.isGiuridico()?"codiceFiscaleG":"codiceFiscale"),soggetto, soggettoDettRow);
				addField("Tipo ","tipoSoggetto",soggetto, soggettoDettRow);
				if(soggetto.isGiuridico()){
					addField("Denominazione ","denominazione",soggetto, soggettoDettRow);
					addField("Sede ","sede",soggetto, soggettoDettRow);
				}
				else{
					addField("Cognome ","cognome",soggetto, soggettoDettRow);
					addField("Nome ","nome",soggetto, soggettoDettRow);
					addPair("Sesso ",soggettoDettRow,(soggetto.isGiuridico()? "" :( "1".equals(soggetto.getSesso())?"M":"F") ));
					addField("Data Nasc.","dataNascita",soggetto, soggettoDettRow,true);
				}
				MiDupIndirizziSog indirizzo = null;
				try {
					indirizzo =	soggetto.getMiDupIndirizziSogs().iterator().next();
				} catch (Exception e) {
					indirizzo = null;
				}
				if(indirizzo != null){
					addPair("Indirizzo ",soggettoDettRow,indirizzo.getIndirizzo()+ " " +indirizzo.getComune()+ " " +indirizzo.getProvincia()+ " " +indirizzo.getCap());
				}
				document
				.add(soggettoDettRow);
				elemCount++;
			}

						
			// step 5: we close the document
	        document.close();
	        
	        out.flush();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			Logger.log().error(this.getClass().getName(), "error while pdf of nota",e);
			throw new ServletException(e);
		}
	}
	
	private String getRenditaImmobile(MiDupFabbricatiInfo fabbricato){
		String res = null;
		try {
			double resD = Integer.valueOf(fabbricato.getRenditaEuro()).intValue()/100; 
			res = "" +resD;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res = null;
		}
		return res;
	}
	
	private void addField(String desc,String property,Object nota, Paragraph notaDettRow) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		addField(desc,property,nota, notaDettRow,false);
	}

	private void addField(String desc,String property,Object nota, Paragraph notaDettRow,boolean isData) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String pValue = null;
		if(property != null){
			pValue = getProperty(property, nota, isData);
		}
		else{
			pValue = " ";
		}
		addPair(desc, notaDettRow, pValue);
	}

	private void addPair(String desc, Paragraph notaDettRow, String pValue) {
		Chunk chunk = new Chunk(desc,descFieldFont);
		notaDettRow.add(chunk);
		if(pValue != null){
			chunk = new Chunk(pValue,valFieldFont);
		}
		else{
			chunk = new Chunk( " ",valFieldFont);
		}
		
		notaDettRow.add(chunk);
		chunk = new Chunk("	",valFieldFont);
		notaDettRow.add(chunk);
	}

	protected String getProperty(String property, Object nota, boolean isData) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String pValue = BeanUtils.getProperty(nota,property);
		if(pValue != null && pValue.trim().length() >0){
			if(isData  && pValue.trim().length() == 8){
				pValue = pValue.substring(0,2)+"/"+pValue.substring(2,4)+"/"+pValue.substring(4);
			}
		}
		else{
			pValue =" ";
		}
		return pValue;
	}

	protected String getProperty(String property, Object nota) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return getProperty(property, nota,false);
	}

	private void setNota(HttpServletRequest req) {
		Long iidNota = Long.valueOf( req.getParameter("iidNota"));
		CodeBehindnoteDetailPage.storeNota(req, iidNota);
	}
}