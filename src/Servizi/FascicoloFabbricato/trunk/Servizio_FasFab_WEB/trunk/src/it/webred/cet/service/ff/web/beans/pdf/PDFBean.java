package it.webred.cet.service.ff.web.beans.pdf;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.dettaglio.catasto.CatastoBean;
import it.webred.cet.service.ff.web.beans.dettaglio.compravendite.CompravenditeBean;
import it.webred.cet.service.ff.web.beans.dettaglio.datiCartografia.DatiCartografiaBean;
import it.webred.cet.service.ff.web.beans.dettaglio.datiTecnici.DatiTecniciBean;
import it.webred.cet.service.ff.web.beans.dettaglio.datiTecnici.TipologiaPRG;
import it.webred.cet.service.ff.web.beans.dettaglio.docfa.DocfaBean;
import it.webred.cet.service.ff.web.beans.dettaglio.documenti.DocfaPlanimetrieDatiCensuari;
import it.webred.cet.service.ff.web.beans.dettaglio.documenti.DocumentiBean;
import it.webred.cet.service.ff.web.beans.dettaglio.pregeo.PregeoBean;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.StatoOccupazioneBean;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto.DettLicenzeCommercioDTO;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto.DettLocazioniDTO;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto.DettResidentiDTO;
import it.webred.cet.service.ff.web.beans.dettaglio.tributi.TributiBean;
import it.webred.cet.service.ff.web.beans.filtro.FiltroBean;
import it.webred.cet.service.ff.web.beans.filtro.FiltroBean.TipoMezzoRisposta;
import it.webred.cet.service.ff.web.util.PermessiHandler;
import it.webred.cet.service.ff.web.util.TiffUtill;
import it.webred.cet.service.ff.web.util.Utility;
import it.webred.ct.aggregator.ejb.tributiFabbricato.dto.DatiIciDTO;
import it.webred.ct.aggregator.ejb.tributiFabbricato.dto.DatiTarsuDTO;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.concedilizie.ConcVisuraDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.docfa.dto.DatiGeneraliDocfaDTO;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.common.SitEnte;
import it.webred.ct.data.model.condono.CondonoPratica;
import it.webred.ct.data.model.cosap.SitTCosapTassa;
import it.webred.ct.data.model.datitecnicifabbricato.CertificazioneEnergetica;
import it.webred.ct.data.model.datitecnicifabbricato.CollaudoStatico;
import it.webred.ct.data.model.datitecnicifabbricato.DichiarazioneConformita;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;
import it.webred.ct.data.model.pregeo.PregeoInfo;
import it.webred.ct.service.ff.data.access.dettaglio.compravendite.dto.DatiCompravenditeDTO;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.access.risposte.GestRispostaService;
import it.webred.ct.service.ff.data.access.risposte.dto.RispostaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFRisposte;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.filesystem.provider.FileInfo;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;



public class PDFBean extends FFBaseBean implements Serializable {

	private String username;
	private String dataRichiesta;
	private String dataRif;
	private String idSoggRichiedente;
	private String soggRichiedente;
	private String idRich;
	
	// Dati relativi alla combo
	private boolean cbxCatasto;
	private boolean cbxDatiTecnici;
	private boolean cbxStatoOccupazione;
	private boolean cbxTributi;
	private boolean cbxDatiCartografici;
	private boolean cbxDocumenti;
	
	private String evadi;
	
	
	// Campi chiave
	private String sezione;
	private String particella;
	private String foglio;
	
	private String comune;

	private GestRispostaService rispostaService;
	private GestRichiestaService richService;
	private ParameterService paramService;
	private CommonService commonService;
	
	private String filename;
	
	/*public void doPrepareRich() {
		
		if (idSoggRichiedente!= null){
		FFSoggetti sogg= getSoggetto();
		soggRichiedente= sogg.getNome() + " " + sogg.getCognome()+ " "+ sogg.getDenomSoc(); 
		}
	}*/
	
	public void doPrepare() {
		
		logger.debug("inizio doPrepare()");
		
		
	}
	
	public void createPDF() {
		
		// Recupero i dati del catasto
		HashMap datiMap = new HashMap();
		
		SitEnte ente= commonService.getEnte(null);
	
		if (ente != null)
			datiMap.put("comune", ente.getDescrizione());
		
        //String supReportPath = path + super.getEnte() + "//" + super.getGlobalConfig().get("dirTemplate")+ "//"  ;
		String templateReportPath = super.getDirFilesDati(paramService)  + "template" + File.separatorChar  + super.getGlobalConfig().get("dirTemplate")+ File.separatorChar ;
		logger.debug("PATH TEMPLATE PDF: " + templateReportPath); 
		datiMap.put("SUBREPORT_DIR", templateReportPath);
		datiMap.put("sezione", sezione);
		datiMap.put("foglio", foglio);
		datiMap.put("particella", particella);
		datiMap.put("dataRichiesta", dataRichiesta);
		datiMap.put("dataRif", dataRif);
		datiMap.put("idRichiesta", idRich);
		datiMap.put("soggRichiedente", soggRichiedente);
		datiMap.put("username", username);	
		datiMap.put("cbxCatasto", cbxCatasto);	
		datiMap.put("cbxStatoOccupazione", cbxStatoOccupazione);	
		datiMap.put("cbxTributi", cbxTributi);	
		datiMap.put("cbxDatiTecnici", cbxDatiTecnici);	
		datiMap.put("cbxDatiCartografici", cbxDatiCartografici);	
		
		if (cbxCatasto)
			loadCatastoData(datiMap);
		
		if (cbxStatoOccupazione)
			loadStatoOcc(datiMap);
		
		if (cbxTributi)
			loadTributi(datiMap);
		
		if (cbxDatiTecnici)
			loadDatiTecnici(datiMap);
		
		if (cbxDatiCartografici)
			loadDatiCartografici(datiMap);
		
        try {  
            FacesContext context = FacesContext.getCurrentInstance();  
            ExternalContext ext = context.getExternalContext();
           
            logger.debug("ID Rich ["+idRich+"]");
            
            /*
            Utility utl = new Utility(paramService);
            String path = utl.getDirFilesDati();
             
            String tplPath = path + super.getEnte() + "//" + super.getGlobalConfig().get("dirTemplate") + "//reportDettaglioFasFab.jasper"; 
             */ 
            String tplPath = templateReportPath + "reportDettaglioFasFab.jasper";
            JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
            JasperReport report = (JasperReport)JRLoader.loadObject(tplPath);
        	JasperPrint print = JasperFillManager.fillReport(report, datiMap); 
            
             filename = String.format("%012d", new Long(this.idRich)) + ".pdf";  

                      
             byte[] pdfBites = JasperExportManager.exportReportToPdf(print);
             
             //creo la cartella fascicoloFabbricato
             File theDir = new File(super.getPathDirEnteFascicoloFabbricato(paramService));
             if (!theDir.exists())
            	 theDir.mkdir();
             String path = super.getPathDirEnteFascicoloFabbricato(paramService) + File.separatorChar + filename;

             FileOutputStream fos = new FileOutputStream(new File(path));
             
             fos.write(pdfBites);
             fos.close();
             
             if (cbxDocumenti) {
            	 insertDocsInPdf(path);
             }             
             
             if ("true".equals(evadi)) {
            	 evadi(filename);
             }
             else {
            	 try {
            		 updateFileName(filename);
            	 }
            	 catch(Exception e) {}
             }
             
             
             FiltroBean fb = (FiltroBean) super.getBeanReference("filtroBean");
             fb.setNomePdf(filename);

             
             /*
             OutputStream os = resp.getOutputStream();
             os.write(pdfBites);
             os.flush();
             os.close();
             

             context.responseComplete();
             */
  
           }
           catch (Throwable t) {
        	  logger.error(t.getMessage(),t);
           	super.addErrorMessage("ff.errore", "");
           	
           }
	}
	
	private void insertDocsInPdf(String path) throws Exception {		
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, new FileOutputStream(path.replace(".pdf", "_TEMP2.pdf")));
        document.open();
        PdfReader reader = new PdfReader(path);
        int n = reader.getNumberOfPages();
        for (int page = 0; page < n;) {
        	if (page == n - 1) {
        		writeDocsPdf(path);
        		PdfReader reader1 = new PdfReader(path.replace(".pdf", "_TEMP1.pdf"));
        		int n1 = reader1.getNumberOfPages();
                for (int page1 = 0; page1 < n1;) {
                	copy.addPage(copy.getImportedPage(reader1, ++page1));
                }
                copy.freeReader(reader1);
                reader1.close();
        	}
            copy.addPage(copy.getImportedPage(reader, ++page));
        }
        copy.freeReader(reader);
        reader.close();
        document.close();
        if (new File(path).delete() && new File(path.replace(".pdf", "_TEMP1.pdf")).delete()) {
        	new File(path.replace(".pdf", "_TEMP2.pdf")).renameTo(new File(path));
        }
	}
	
	private void writeDocsPdf(String path) throws Exception {
		DocumentiBean documentiBean = (DocumentiBean)super.getBeanReference("documentiBean");
		setParams(documentiBean);
		documentiBean.doSwitch();
		
		List<DocfaPlanimetrieDatiCensuari> listaDocfaPlanimetrieUiu = documentiBean.getListaDocfaPlanimetrieUiu();
		List<DocfaPlanimetrie> listaDocfaPlanimetrieFab = documentiBean.getListaDocfaPlanimetrieFab();
		List<PlanimetriaComma340DTO> listaC340PlanimetrieUiu = documentiBean.getListaC340PlanimetrieUiu();
		List<PlanimetriaComma340DTO> listaC340PlanimetrieFab = documentiBean.getListaC340PlanimetrieFab();
		
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(document, new FileOutputStream(path.replace(".pdf", "_TEMP1_BASE.pdf")));
		Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.NORMAL);
		Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
		
		document.open();
		int pag = 0;
		String planFileName = null;
		
		document.newPage();
		pag++;
		Paragraph par = new Paragraph(new Phrase(4, "PLANIMETRIE DOCFA RELATIVE ALLE UNITÀ IMMOBILIARI", bold));
		document.add(par);
		
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		
		String text = null;
		int conta = listaDocfaPlanimetrieUiu == null ? 0 : listaDocfaPlanimetrieUiu.size();
		if (conta == 0) {
			text = "Non è presente nessuna planimetria DOCFA relativa alle unità immobiliari";
		} else if (conta == 1) {
			text = "È presente 1 planimetria DOCFA relativa alle unità immobiliari";
		} else {
			text = "Sono presenti " + conta + " planimetrie DOCFA relative alle unità immobiliari";
		}
		par = new Paragraph(new Phrase(4, text, normal));
		document.add(par);
		
		if (conta > 0) {
			for (DocfaPlanimetrieDatiCensuari ddc : listaDocfaPlanimetrieUiu) {
				pag++;
				DocfaPlanimetrie plan = ddc.getDocfaPlanimetrie();
				planFileName = path.replace(".pdf", "_TEMP_PLAN_" + pag + ".pdf");
				String nomePlan = plan.getId().getNomePlan() + "." + plan.getId().getPadProgressivo() + ".tif";
				String dirPlan = super.getGlobalConfig().get("dirPlanimetrie");
				String pathFile = super.getPathDatiDiogeneEnte(paramService) + File.separatorChar + dirPlan + File.separatorChar + plan.getFornituraStr().substring(0, 6) +  File.separatorChar + nomePlan;
				writePlan(pathFile, planFileName);
			}
		}

		document.newPage();
		pag++;
		par = new Paragraph(new Phrase(4, "ELABORATI PLANIMETRICI DOCFA", bold));
		document.add(par);
		
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		
		text = null;
		conta = listaDocfaPlanimetrieFab == null ? 0 : listaDocfaPlanimetrieFab.size();
		if (conta == 0) {
			text = "Non è presente nessun elaborato planimetrico DOCFA";
		} else if (conta == 1) {
			text = "È presente 1 elaborato planimetrico DOCFA";
		} else {
			text = "Sono presenti " + conta + " elaborati planimetrici DOCFA";
		}
		par = new Paragraph(new Phrase(4, text, normal));
		document.add(par);
		
		if (conta > 0) {
			for (DocfaPlanimetrie plan : listaDocfaPlanimetrieFab) {
				pag++;
				planFileName = path.replace(".pdf", "_TEMP_PLAN_" + pag + ".pdf");
				String nomePlan = plan.getId().getNomePlan() + "." + plan.getId().getPadProgressivo() + ".tif";
				String dirPlan = super.getGlobalConfig().get("dirPlanimetrie");
				String pathFile = super.getPathDatiDiogeneEnte(paramService) + File.separatorChar + dirPlan + File.separatorChar + plan.getFornituraStr().substring(0, 6) +  File.separatorChar + nomePlan;
				writePlan(pathFile, planFileName);
			}
		}
		
		document.newPage();
		pag++;
		par = new Paragraph(new Phrase(4, "PLANIMETRIE COMMA 340 RELATIVE ALLE UNITÀ IMMOBILIARI", bold));
		document.add(par);
		
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		
		text = null;
		conta = listaC340PlanimetrieUiu == null ? 0 : listaC340PlanimetrieUiu.size();
		if (conta == 0) {
			text = "Non è presente nessuna planimetria comma 340 relativa alle unità immobiliari";
		} else if (conta == 1) {
			text = "È presente 1 planimetria comma 340 relativa alle unità immobiliari";
		} else {
			text = "Sono presenti " + conta + " planimetrie comma 340 relative alle unità immobiliari";
		}
		par = new Paragraph(new Phrase(4, text, normal));
		document.add(par);
		
		if (conta > 0) {
			for (PlanimetriaComma340DTO plan : listaC340PlanimetrieUiu) {
				pag++;
				planFileName = path.replace(".pdf", "_TEMP_PLAN_" + pag + ".pdf");
				String pathFile = plan.getLink();
				writePlan(pathFile, planFileName);
			}
		}
		
		document.newPage();
		pag++;
		par = new Paragraph(new Phrase(4, "ELABORATI PLANIMETRICI COMMA 340", bold));
		document.add(par);
		
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		
		text = null;
		conta = listaC340PlanimetrieFab == null ? 0 : listaC340PlanimetrieFab.size();
		if (conta == 0) {
			text = "Non è presente nessun elaborato planimetrico comma 340";
		} else if (conta == 1) {
			text = "È presente 1 elaborato planimetrico comma 340";
		} else {
			text = "Sono presenti " + conta + " elaborati planimetrici comma 340";
		}
		par = new Paragraph(new Phrase(4, text, normal));
		document.add(par);
		
		if (conta > 0) {
			for (PlanimetriaComma340DTO plan : listaC340PlanimetrieFab) {
				pag++;
				planFileName = path.replace(".pdf", "_TEMP_PLAN_" + pag + ".pdf");
				String pathFile = plan.getLink();
				writePlan(pathFile, planFileName);
			}
		}
		
		document.close();
		
		Document document1 = new Document();
		PdfCopy copy = new PdfCopy(document1, new FileOutputStream(path.replace(".pdf", "_TEMP1.pdf")));
        document1.open();
        PdfReader reader = new PdfReader(path.replace(".pdf", "_TEMP1_BASE.pdf"));
        int n = reader.getNumberOfPages();
        int nTot = 1;
        for (int page = 0; page < n;) {
            copy.addPage(copy.getImportedPage(reader, ++page));
            nTot++;
        	while (new File(path.replace(".pdf", "_TEMP_PLAN_" + nTot + ".pdf")).exists()) {
        		PdfReader docReader = new PdfReader(path.replace(".pdf", "_TEMP_PLAN_" + nTot + ".pdf"));
        		int docN = docReader.getNumberOfPages();
        		for (int docPage = 0; docPage < docN;) {
        			copy.addPage(copy.getImportedPage(docReader, ++docPage));
        		}
        		copy.freeReader(docReader);
        		docReader.close();
        		new File(path.replace(".pdf", "_TEMP_PLAN_" + nTot + ".pdf")).delete();
        		nTot++;
        	}
        }
        copy.freeReader(reader);
        reader.close();
        document1.close();
        new File(path.replace(".pdf", "_TEMP1_BASE.pdf")).delete();
	}
	
	private void writePlan(String pathFile, String planFileName) throws Exception {
		boolean isViewNoWatermark = PermessiHandler.controlla(super.getCeTUser(), PermessiHandler.PERMESSO_ELIMINA_WATERMARK);		
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		FileOutputStream fos = null;
		try {
			File image = new File(pathFile);
			is = new FileInputStream(image);
			List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, !isViewNoWatermark, false);
			baos = TiffUtill.jpgsTopdf(jpgs, false, 4);					
			fos = new FileOutputStream(planFileName);
			baos.writeTo(fos);
		} catch (Exception e) {
			throw e;
		} finally {
			is.close();
			baos.close();
			fos.close();
		}		
	}
	
	private void updateFileName(String filename) throws Exception {
        RichiestaDTO richDTO = new RichiestaDTO();
   	 
    	FFRichieste rich = new FFRichieste();
    	rich.setIdRic(new Long(idRich));
    	rich.setNomePdf(filename != null? filename.replace(".pdf", ""): null);
    	 
    	richDTO.setEnteId(super.getEnte());
    	richDTO.setUserId(super.getUsername());
    	
    	richDTO.setRichiesta(rich);
    	richService.updateFilePdfRichiesta(richDTO);
	}
	
	private void evadi(String filename) {
		try {
			
			updateFileName(filename);
			
			RispostaDTO rispDTO = new RispostaDTO();
			
			FFRisposte risp = new FFRisposte();
			/*
			UserBean u = new UserBean();
			CeTUser user =u.getUser();
			*/
			
			risp.setUserName(super.getUsername());
			
			risp.setIdRic(new BigDecimal(idRich));
			risp.setDtRis(new Date());
			risp.setDesRis("");
			risp.setDesNotUser("");
			risp.setCodTipMezRis(new Integer(TipoMezzoRisposta.CONSEGNA_A_MANO.ordinal()).toString());
			
			rispDTO.setUserId(super.getUsername());
			rispDTO.setEnteId(super.getEnte());
			rispDTO.setRisposta(risp);
	
			rispostaService.insertRisposta(rispDTO);
			
			
			

			super.addInfoMessage("ff.filtro.richiestaevasa");
			
		}
		catch(Throwable t) {
			super.addErrorMessage("ff.errore","");	
		}
			
	}
	
	private void loadCatastoData(HashMap datiMap) {
		CatastoBean cb = (CatastoBean) super.getBeanReference("catastoBean");
		CompravenditeBean compBean = (CompravenditeBean) super.getBeanReference("compravenditeBean");
		PregeoBean pregBean = (PregeoBean) super.getBeanReference("pregeoBean");
		DocfaBean docfaBean = (DocfaBean) super.getBeanReference("docfaBean");
		
		setParams(cb);
		setParams(compBean);
		setParams(pregBean);
		setParams(docfaBean);
		
		cb.doSwitch();
		compBean.setCompravenditeParticella("");
		pregBean.setListaDatiPregeo("");
		docfaBean.setDocfaParticella("");
		
		
		List<Sitiuiu>  listSiti = cb.getListaUiu();
		datiMap.put("sitiUIU", new JRBeanCollectionDataSource(listSiti));
		
		List<Sititrkc> listaTerr = cb.getListaTerreni();
		datiMap.put("terreniList", new JRBeanCollectionDataSource(listaTerr));
		
		List<DatiGeneraliDocfaDTO> listaDocfaParticella= docfaBean.getListaDocfaParticella();
		datiMap.put("datiDocfa", new JRBeanCollectionDataSource(listaDocfaParticella));
		
		List<PregeoInfo> listaPregeo= pregBean.getListaPregeo();
		datiMap.put("datiPregeo", new JRBeanCollectionDataSource(listaPregeo));
		
		List<DatiCompravenditeDTO> listaCompravenditeParticella= compBean.getListaCompravenditeParticella();
		datiMap.put("datiCompravenditeImmobili", new JRBeanCollectionDataSource(listaCompravenditeParticella));
		
		List<DatiCompravenditeDTO> listaCompravenditeParticellaTerreno= compBean.getListaCompravenditeParticellaTerreno();
		datiMap.put("datiCompravenditeTerreni", new JRBeanCollectionDataSource(listaCompravenditeParticellaTerreno));
		
	}
	
	private void loadDatiTecnici(HashMap datiMap) {
		DatiTecniciBean datiTecniciBean = (DatiTecniciBean) super.getBeanReference("datiTecniciBean");
		
	    setParams(datiTecniciBean);
	    
	    datiTecniciBean.doSwitch();
	    
	    /*24-10-2013 - La visualizzazione delle informazioni dei cataloghi è stata spostata nel pannello DATI CARTOGRAFICI
	    /* List<TipologiaPRG> listaPRG= datiTecniciBean.getListaPRG();
	    datiMap.put("datiPRG", new JRBeanCollectionDataSource(listaPRG));
	    */
	    
	    List<ConcessioneDTO> listaConcessioni= datiTecniciBean.getListaConcessioni();
	    datiMap.put("datiConcessioni", new JRBeanCollectionDataSource(listaConcessioni));
	    
	    List<ConcVisuraDTO> listaConcessioniVisure= datiTecniciBean.getListaConcVisure();
	    datiMap.put("datiConcessioniVisure", new JRBeanCollectionDataSource(listaConcessioniVisure));
	    
	    List<CertificazioneEnergetica> listaCertEnergetiche = datiTecniciBean.getListaCertEnergetiche();
	    datiMap.put("datiCertEnergetiche", new JRBeanCollectionDataSource(listaCertEnergetiche));
	    
		List<DichiarazioneConformita> listaDichConform = datiTecniciBean.getListaDichConform();
		datiMap.put("datiDichConformita", new JRBeanCollectionDataSource(listaDichConform));
		
		List<CollaudoStatico> listaCollaudoStatico = datiTecniciBean.getListaCollaudoStatico();
		datiMap.put("datiCollaudoStatico", new JRBeanCollectionDataSource(listaCollaudoStatico));
		
		List<CondonoPratica> listaCondono= datiTecniciBean.getListaCondono();
		datiMap.put("datiCondono", new JRBeanCollectionDataSource(listaCondono));
	    
	 
	}
	
	private void loadStatoOcc(HashMap datiMap) {
		StatoOccupazioneBean sOccBean = (StatoOccupazioneBean) super.getBeanReference("statoOccupazioneBean");
		
	    setParams(sOccBean);
	    
	    sOccBean.doSwitch();
	    List<DettLocazioniDTO> dettLocList =  sOccBean.getDettLocazioni();
	    datiMap.put("datiLocazione", new JRBeanCollectionDataSource(dettLocList));
 	    List<DettResidentiDTO> dettResList = sOccBean.getDettResidenti();
 	    datiMap.put("datiResidenti", new JRBeanCollectionDataSource(dettResList));
 	   List<DettLicenzeCommercioDTO> dettLicenzeCommercio= sOccBean.getDettLicenzeCommercio();
 	  datiMap.put("datiLicenze", new JRBeanCollectionDataSource(dettLicenzeCommercio));
	}
	
	private void loadTributi(HashMap datiMap) {
		TributiBean tributiBean = (TributiBean) super.getBeanReference("tributiBean");
		
	    setParams(tributiBean);
	    
	    tributiBean.doSwitch();
	    List<DatiIciDTO> listaIci = tributiBean.getListaIci();
	    datiMap.put("datiIci", new JRBeanCollectionDataSource(listaIci));
		List<DatiTarsuDTO> listaTarsu = tributiBean.getListaTarsu(); 
		 datiMap.put("datiTarsu", new JRBeanCollectionDataSource(listaTarsu));
		List<SitTCosapTassa> listaCosap = tributiBean.getListaCosap();
	    datiMap.put("datiCosap", new JRBeanCollectionDataSource(listaCosap));
 	    
	}
	
	private void loadDatiCartografici(HashMap datiMap) {
		DatiCartografiaBean cartBean = (DatiCartografiaBean) super.getBeanReference("datiCartografiaBean");
	    setParams(cartBean);
	    
	    cartBean.doSwitch();
	    
	    List<TipologiaPRG> lista = cartBean.getListaPRG();
	    datiMap.put("datiCartografici", new JRBeanCollectionDataSource(lista));
	}
	
	
	private void setParams(DatiDettaglio dati) {
		dati.setFoglio(foglio);
		dati.setParticella(particella);
		dati.setSezione(sezione);
		
		Date dataRifD=null;
		SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		
		Date oggi= new Date();
		String strOggi= df.format(oggi);
		
		if (!strOggi.equals(dataRif))
			{
			try{
				 dataRifD= df.parse(dataRif);
			
			
			logger.debug("DATA RIF: " + dataRifD);
			}
			catch(ParseException e){
				logger.error("errore parse dataRif "+ e.getMessage(),e);
			}
		}
		dati.setDataRif(dataRifD);
	}
	
	// Metodi get e set
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDataRichiesta() {
		//logger.debug("Set D ["+dataRichiesta+"]");
		return dataRichiesta;
	}
	public void setDataRichiesta(String dataRichiesta) {
		//logger.debug("Set D ["+dataRichiesta+"]");
		this.dataRichiesta = dataRichiesta;
	}
	public String getSoggRichiedente() {
		return soggRichiedente;
	}
	public void setSoggRichiedente(String soggRichiedente) {
		this.soggRichiedente = soggRichiedente;
	}
	public String getIdRich() {
		return idRich;
	}
	public void setIdRich(String idRich) {
		this.idRich = idRich;
	}
	public boolean isCbxCatasto() {
		return cbxCatasto;
	}
	public void setCbxCatasto(boolean cbxCatasto) {
		this.cbxCatasto = cbxCatasto;
	}
	public boolean isCbxDatiTecnici() {
		return cbxDatiTecnici;
	}
	public void setCbxDatiTecnici(boolean cbxDatiTecnici) {
		this.cbxDatiTecnici = cbxDatiTecnici;
	}
	public boolean isCbxStatoOccupazione() {
		return cbxStatoOccupazione;
	}
	public void setCbxStatoOccupazione(boolean cbxStatoOccupazione) {
		this.cbxStatoOccupazione = cbxStatoOccupazione;
	}
	public boolean isCbxTributi() {
		return cbxTributi;
	}
	public void setCbxTributi(boolean cbxTributi) {
		this.cbxTributi = cbxTributi;
	}
	
	
	public boolean isCbxDatiCartografici() {
		return cbxDatiCartografici;
	}

	public void setCbxDatiCartografici(boolean cbxDatiCartografici) {
		this.cbxDatiCartografici = cbxDatiCartografici;
	}

	public boolean isCbxDocumenti() {
		return cbxDocumenti;
	}

	public void setCbxDocumenti(boolean cbxDocumenti) {
		this.cbxDocumenti = cbxDocumenti;
	}

	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getFoglio() {
		//logger.debug("Get F ["+foglio+"]");
		return foglio;
	}
	public void setFoglio(String foglio) {
		//logger.debug("Set F ["+foglio+"]");
		this.foglio = foglio;
	}

	public String getEvadi() {
		return evadi;
	}

	public void setEvadi(String evadi) {
		this.evadi = evadi;
	}

	public GestRispostaService getRispostaService() {
		return rispostaService;
	}

	public void setRispostaService(GestRispostaService rispostaService) {
		this.rispostaService = rispostaService;
	}

	public ParameterService getParamService() {
		return paramService;
	}

	public void setParamService(ParameterService paramService) {
		this.paramService = paramService;
	}
	
	public GestRichiestaService getRichService() {
		return richService;
	}

	public void setRichService(GestRichiestaService richService) {
		this.richService = richService;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void doDownloadPDF() {
       
		String path = super.getPathDirEnteFascicoloFabbricato(paramService) + File.separatorChar + filename;
		path += (path.endsWith(".pdf") ? "" : ".pdf");
        HttpServletResponse resp = (HttpServletResponse) super.getResponse();  
        resp.setContentType("application/pdf");  
        //String filename = String.format("%012d", new Long(this.idRich)) + ".pdf";  

        resp.addHeader("Content-Disposition", "attachment; filename="  + filename + (filename.endsWith(".pdf") ? "" : ".pdf"));
        
        OutputStream os = null;
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        
        try {
        	os = resp.getOutputStream();
			fis = new FileInputStream(new File(path));
			while ( fis.read(buffer) != -1) {
				os.write(buffer);
			}
			
            os.flush();
			
		} catch (Throwable e) {			
			logger.error(e.getMessage(),e);
		}
		finally {
			try {
				os.close();
			}
			catch(Throwable t) {}
			
			try {
				fis.close();
			}
			catch(Throwable t) {}
		}
        
		FacesContext context = FacesContext.getCurrentInstance(); 
		context.responseComplete();

	}
	
	public void doCancellaPdf()
	{
		logger.debug("File Name ["+filename+"] - ID ["+idRich+"]");
		
		String path = super.getPathDirEnteFascicoloFabbricato(paramService) + File.separatorChar + filename;
		path += (path.endsWith(".pdf") ? "" : ".pdf");
		Utility.DeleteFile(path);
		
		
		try {
			updateFileName(null);
			FiltroBean fb = (FiltroBean) super.getBeanReference("filtroBean");
			fb.setNomePdf(null);
			
		}
		catch (Throwable t) {
			
			logger.error(t.getMessage(),t);
		}
		
		path = null;
		
		//return "filtro.richiesta.fasNonEvaso";
	}


	
	public String getIdSoggRichiedente() {
		return idSoggRichiedente;
	}

	public void setIdSoggRichiedente(String idSoggRichiedente) {
		this.idSoggRichiedente = idSoggRichiedente;
	}

	public String getDataRif() {
		return dataRif;
	}

	public void setDataRif(String dataRif) {
		this.dataRif = dataRif;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

}
