package it.webred.ct.service.carContrib.web.pages.fonti;

import it.webred.ct.data.access.basic.f24.F24Service;
import it.webred.ct.data.access.basic.f24.dto.F24VersamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.RicercaF24DTO;
import it.webred.ct.data.access.basic.ruolo.RuoloDataIn;
import it.webred.ct.data.access.basic.ruolo.tares.RTaresService;
import it.webred.ct.data.access.basic.ruolo.tares.dto.RuoloTaresDTO;
import it.webred.ct.data.access.basic.ruolo.tarsu.RTarsuService;
import it.webred.ct.data.access.basic.ruolo.tarsu.dto.RataDTO;
import it.webred.ct.data.access.basic.ruolo.tarsu.dto.RuoloTarsuDTO;
import it.webred.ct.data.access.basic.versamenti.bp.dto.VersamentoTarBpDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.VersIciDmService;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDataIn;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.VersamentoIciDmDTO;
import it.webred.ct.data.model.ruolo.tares.SitRuoloTares;
import it.webred.ct.data.model.ruolo.tarsu.SitRuoloTarsu;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.web.utils.FonteDTO;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;



import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class RuoliVersamentiCarContrib extends FonteBaseCarContrib{

	private FonteDTO f24Fonte;
	private FonteDTO rTaresFonte;
	private FonteDTO rTarsuFonte;
	private FonteDTO verTarsuBPFonte;
	private FonteDTO verIciDMFonte;
	
	private boolean fontiAbilitate;
	private boolean datiPresenti;
	
	private List<F24VersamentoDTO> listaVersamentiF24 = new ArrayList<F24VersamentoDTO>();
	private List<VersamentoIciDmDTO> listaVersamentiIciDm = new ArrayList<VersamentoIciDmDTO>();
	private List<RuoloTaresDTO> listaRuoliTares = new ArrayList<RuoloTaresDTO>();
	private List<RuoloTarsuDTO> listaRuoliTarsu = new ArrayList<RuoloTarsuDTO>();
	
	private F24Service f24Service;
	private VersIciDmService iciDMService;
	private RTaresService rTaresService;
	private RTarsuService rTarsuService;
	
	private RuoloTaresDTO selRTares;
	private RuoloTarsuDTO selRTarsu;
	
	public boolean isFontiAbilitate(){
		return  (this.f24Fonte!=null && this.f24Fonte.isAbilitata())||
				(this.rTaresFonte!=null && this.rTaresFonte.isAbilitata())||
				(this.rTarsuFonte!=null && this.rTarsuFonte.isAbilitata())||
				(this.verIciDMFonte!=null && this.verIciDMFonte.isAbilitata())||
				(this.verTarsuBPFonte!=null && this.verTarsuBPFonte.isAbilitata());
	}
	
	public boolean isDatiPresenti(){
		return (listaVersamentiF24!=null && listaVersamentiF24.size()>0)||
			   (listaVersamentiIciDm!=null && listaVersamentiIciDm.size()>0)||
			   (listaRuoliTares!=null && listaRuoliTares.size()>0)||
			   (listaRuoliTarsu!=null && listaRuoliTarsu.size()>0);
	}
	
	
	
	// CARICA I DATI TAB Ruoli e Versamenti
	public void LoadTabRuoliVersamenti(String ente, String username, SoggettoDTO soggettoCartella)
	{
		this.LoadSezioneF24(ente,username,soggettoCartella);
		this.LoadSezioneVersIciDM(ente, username, soggettoCartella);
		this.LoadSezioneRTares(ente, username, soggettoCartella);
		this.LoadSezioneRTarsu(ente, username, soggettoCartella);
	}
	
	private void LoadSezioneF24(String ente, String username, SoggettoDTO soggettoCartella){
		try{
			if(f24Service == null){
				Context ctx = new InitialContext();
				f24Service = (F24Service) getEjb("CT_Service","CT_Service_Data_Access" , "F24ServiceBean"); 
			}
			
			String cf_piva = this.getSoggettoCF_PIVA(soggettoCartella);
			if(cf_piva!=null){
				RicercaF24DTO search = new RicercaF24DTO();
				search.setEnteId(ente);
				search.setUserId(username);
				search.setCf(cf_piva);
				listaVersamentiF24 = f24Service.getListaVersamentiByCF(search);
			}
			logger.debug("Numero Versamenti F24 per il soggetto "+cf_piva+": "+listaVersamentiF24.size());
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void LoadSezioneVersIciDM(String ente, String username,SoggettoDTO soggettoCartella){
		try{
			if(iciDMService == null){
				Context ctx = new InitialContext();
				iciDMService = (VersIciDmService) getEjb("CT_Service","CT_Service_Data_Access" , "VersIciDmServiceBean");
			}
			
			String cf_piva = this.getSoggettoCF_PIVA(soggettoCartella);
			if(cf_piva!=null){
				IciDmDataIn dataIn = new IciDmDataIn();
				dataIn.setEnteId(ente);
				dataIn.setUserId(username);	
				dataIn.setCf(cf_piva);
				listaVersamentiIciDm = iciDMService.getListaVersamentiByCodFis(dataIn);
			}
			logger.debug("Numero Versamenti ICI D.M. per il soggetto "+cf_piva+": "+listaVersamentiIciDm.size());
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void LoadSezioneRTares(String ente, String username,SoggettoDTO soggettoCartella){
		try{
			if(rTaresService == null){
				Context ctx = new InitialContext();
				rTaresService = (RTaresService) getEjb("CT_Service","CT_Service_Data_Access" , "RTaresServiceBean");
				
			}
			
			String cf_piva = this.getSoggettoCF_PIVA(soggettoCartella);
			if(cf_piva!=null){
				RuoloDataIn dataIn = new RuoloDataIn();
				dataIn.setEnteId(ente);
				dataIn.setUserId(username);	
				dataIn.setCf(cf_piva);
				listaRuoliTares = rTaresService.getListaRuoliByCodFis(dataIn);
			}
			logger.debug("Numero Ruoli Tares per il soggetto "+cf_piva+": "+listaRuoliTares.size());
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
	}
	
	
	private void LoadSezioneRTarsu(String ente, String username,SoggettoDTO soggettoCartella){
		try{
			if(rTarsuService == null){
				Context ctx = new InitialContext();
				rTarsuService = (RTarsuService) getEjb("CT_Service","CT_Service_Data_Access" , "RTarsuServiceBean");
			}
			
			String cf_piva = this.getSoggettoCF_PIVA(soggettoCartella);
			if(cf_piva!=null){
				RuoloDataIn dataIn = new RuoloDataIn();
				dataIn.setEnteId(ente);
				dataIn.setUserId(username);	
				dataIn.setCf(cf_piva);
				dataIn.setRicercaVersamenti(this.verTarsuBPFonte.isAbilitata());
				listaRuoliTarsu = rTarsuService.getListaRuoliByCodFis(dataIn);
			}
			logger.debug("Numero Ruoli Tarsu per il soggetto "+cf_piva+": "+listaRuoliTarsu.size());
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
	}
	
	
	private String getSoggettoCF_PIVA(SoggettoDTO soggettoCartella)
	{
		if (soggettoCartella!=null)
		{
			if (soggettoCartella.getTipoSogg().equals("F"))
				// 	PERSONA FISICA
				return soggettoCartella.getCodFis();
			else
				// 	PERSONA GIURIDICA
				return soggettoCartella.getParIva();
		}
		else
			return null;
	}
	
	public F24Service getF24Service() {
		return f24Service;
	}

	public void setF24Service(F24Service f24Service) {
		this.f24Service = f24Service;
	}
	
	
	public void addSezionePdf(Document document, boolean cbx, String motivo) throws DocumentException{
	
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		Paragraph paragrafoGen = new Paragraph();
		
		paragrafoGen.add(this.insertTitoloScheda("SCHEDA RUOLI e VERSAMENTI"));
				
		if (!cbx) {
			paragrafoGen.add(new Phrase(motivo,normalSmallFont));
			document.add(paragrafoGen);
			document.newPage();
			return;
		}
		
		document.add(paragrafoGen);
		if(rTarsuFonte.isAbilitata())
			this.addSezioneRTarsu(document, this.rTarsuFonte);
		
		if(this.rTaresFonte.isAbilitata())
			this.addSezioneRTares(document, this.rTaresFonte);
		
		if(this.verIciDMFonte.isAbilitata())
			this.addSezioneIciDM(document,this.verIciDMFonte);
		
		if(this.f24Fonte.isAbilitata())
			this.addSezioneF24(document,this.f24Fonte);
		
		document.newPage();
		
	}
	
	private void addSezioneRTarsu(Document document, FonteDTO fonte) throws DocumentException {
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL);
		
		Paragraph paragrafo = new Paragraph();
		
		if(fonte.isAbilitataCC()){
			
			paragrafo.add(fonte.getStrDataAgg());
			paragrafo.add(new Phrase(fonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafo, 1);
			
			if(this.verTarsuBPFonte.isAbilitataCC()){
				paragrafo.add(verTarsuBPFonte.getStrDataAgg());
				paragrafo.add(new Phrase(verTarsuBPFonte.getNota(),normalSmallFont));
				this.addEmptyLine(paragrafo, 2);
			}
			
					
			if (listaRuoliTarsu!=null && listaRuoliTarsu.size()>0)
			{
				paragrafo.add(new Phrase("Ruolo Tarsu",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable tableBase = new PdfPTable(1);
				tableBase.setWidths(new float[] {100});
				tableBase.setWidthPercentage(100);
				tableBase.getDefaultCell().setMinimumHeight(30);
				
				
				for (Iterator<RuoloTarsuDTO> it = listaRuoliTarsu.iterator();it.hasNext();)
				{	
					PdfPTable table = new PdfPTable(1);
					table.setWidths(new float[] {100});
					table.setWidthPercentage(100);
					table.getDefaultCell().setBorder(0);
					table.getDefaultCell().setMinimumHeight(30);
					int align = Element.ALIGN_CENTER;
					
					PdfPTable tableRuolo = new PdfPTable(4);
					tableRuolo.setWidths(new float[] {15,55,15,15});
					tableRuolo.setWidthPercentage(50);
					tableRuolo.getDefaultCell().setBorder(0);
					table.getDefaultCell().setMinimumHeight(10);
					align = Element.ALIGN_LEFT;
					
					RuoloTarsuDTO i= it.next();
					SitRuoloTarsu r = i.getRuolo();
					
					this.addCella(tableRuolo,boldFont,align,"ANNO:");
					this.addCella(tableRuolo,normalSmallFont,align, r.getAnno());
					
					this.addCella(tableRuolo,boldFont,align,"TOT.NETTO:");
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_RIGHT, r.getTotNetto()!=null ? DF.format(r.getTotNetto().doubleValue()): "");
					
					this.addCella(tableRuolo,boldFont,align,"TIPO:");
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_LEFT, "A".equalsIgnoreCase(r.getTipo())? "Principale" : "Suppletivo");
					
					this.addCella(tableRuolo,boldFont,align,"ADD.ECA:");
					String addECA =r.getAddEca()!=null ? (DF.format(r.getAddEca())+(r.getPercEca()!=null ? " ("+DF.format(r.getPercEca())+"%)" : "")) : "";
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_RIGHT, addECA);
					
					this.addCella(tableRuolo,boldFont,align,"CONTRIBUENTE:");
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_LEFT, r.getNominativoContrib()+" c.f. "+r.getCodfisc());
					
					this.addCella(tableRuolo,boldFont,align,"Magg.ECA");
					String maggECA =r.getMaggEca()!=null ? (DF.format(r.getMaggEca())+(r.getPercMaggEca()!=null ? " ("+DF.format(r.getPercMaggEca())+"%)" : "")) : "";
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_RIGHT, maggECA);
					
					this.addCella(tableRuolo,boldFont,align,"INDIRIZZO:");
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_LEFT, r.getIndirizzo()+" "+r.getComune()+
											(r.getProv()!=null ? " ("+r.getProv()+")" : "")+
											(r.getEstero()!=null ? " "+r.getEstero() : ""));
			
					this.addCella(tableRuolo,boldFont,align,"Trib.Prov.");
					String tribProv =r.getTribProv()!=null ? (DF.format(r.getTribProv())+(r.getPercTribProv()!=null ? " ("+DF.format(r.getPercTribProv())+"%)" : "")) : "";
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_RIGHT, tribProv);
					
					this.addCella(tableRuolo,boldFont,align,"ACCONTO:");
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_LEFT, r.getAccontoTarsuAnno()!=null ? DF.format(r.getAccontoTarsuAnno().doubleValue()) : "");
					
					this.addCella(tableRuolo,boldFont,align,"Totale");
					this.addCella(tableRuolo,normalSmallFont,Element.ALIGN_RIGHT, r.getTotale()!=null ? DF.format(r.getTotale().doubleValue()): "");
					
					//Aggiungo il blocco con la cella RUOLO
					table.addCell(tableRuolo);
					
					if(i.getRate()!=null && i.getRate().size()>0){
						
						//Aggiungo il blocco con la cella RATE
						PdfPCell cellInt = table.getDefaultCell();
						cellInt.setColspan(table.getNumberOfColumns());
						cellInt.setHorizontalAlignment(align);
						cellInt.setBackgroundColor(BaseColor.WHITE);
						cellInt.addElement(new Phrase("Lista Rate",new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD)));
						table.addCell(cellInt);
												
						PdfPTable tableRata = new PdfPTable(6);
						tableRata.setWidths(new float[] {10,10,10,30,20,20});
						tableRata.setWidthPercentage(50);
						tableRata.getDefaultCell().setMinimumHeight(30);
						align = Element.ALIGN_CENTER;
						
						this.addCellaIntestazione(tableRata,boldFont,align,"Num.");
						this.addCellaIntestazione(tableRata,boldFont,align,"Data Scadenza");
						this.addCellaIntestazione(tableRata,boldFont,align,"Importo");
						this.addCellaIntestazione(tableRata,boldFont,align,"Desc.Importo");
						this.addCellaIntestazione(tableRata,boldFont,align,"V campo");
						this.addCellaIntestazione(tableRata,boldFont,align,"Cod.Rend.Auto");
						
						for(RataDTO rata : i.getRate()){
							this.addCella(tableRata,normalSmallFont,Element.ALIGN_CENTER, "0".equals(rata.getProg()) ? "Unica Soluzione" : rata.getProg());
							this.addCella(tableRata,normalSmallFont,Element.ALIGN_CENTER, rata.getDataScadenza()!=null ? SDF.format(rata.getDataScadenza()) : "");
							this.addCella(tableRata,normalSmallFont,Element.ALIGN_RIGHT, rata.getImpBollettino()!=null ? DF.format(rata.getImpBollettino()) : "");
							this.addCella(tableRata,normalSmallFont,Element.ALIGN_LEFT, rata.getTotLettere());
							this.addCella(tableRata,normalSmallFont,Element.ALIGN_CENTER, rata.getvCampo());
							this.addCella(tableRata,normalSmallFont,Element.ALIGN_CENTER, rata.getCodRendAuto());
						}
						
						//Aggiungo il blocco con la cella RATE
						table.addCell(tableRata);
					}
					
					//Inserisco le informazioni sui versamenti, se la fonte è abilitata
					if(this.verTarsuBPFonte.isAbilitata())
						this.addSezioneVersTarBP(i,table);
					
					tableBase.addCell(table);

				}
				paragrafo.add(tableBase);
				
				this.addEmptyLine(paragrafo, 1);
				
			}else{
				paragrafo.add(new Paragraph("Ruolo Tarsu non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafo, 1);
			}	
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafo, fonte.getDescrizione());
		
		document.add(paragrafo);
	}
	
	
	private void addSezioneVersTarBP(RuoloTarsuDTO i, PdfPTable table) throws DocumentException{
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL);
		int align = Element.ALIGN_CENTER;
		
		if(this.verTarsuBPFonte.isAbilitataCC()){
			if(i.getListaVersBp()!=null && i.getListaVersBp().size()>0){
				
				PdfPCell cellInt = table.getDefaultCell();
				cellInt.setColspan(table.getNumberOfColumns());
				cellInt.setHorizontalAlignment(align);
				cellInt.setBackgroundColor(BaseColor.WHITE);
				cellInt.addElement(new Phrase("Lista Versamenti Postali",new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD)));
				table.addCell(cellInt);
				
				PdfPTable tableBP = new PdfPTable(8);
				tableBP.setWidths(new float[] {10,10,20,10,10,10,10,20});
				tableBP.setWidthPercentage(100);
				tableBP.getDefaultCell().setMinimumHeight(30);
				
				this.addCellaIntestazione(tableBP,boldFont,align,"Rata Rif.");
				this.addCellaIntestazione(tableBP,boldFont,align,"V campo");
				this.addCellaIntestazione(tableBP,boldFont,align,"Tipo Documento");
				this.addCellaIntestazione(tableBP,boldFont,align,"Importo");
				this.addCellaIntestazione(tableBP,boldFont,align,"Data Pagam.");
				this.addCellaIntestazione(tableBP,boldFont,align,"Data Accred.");
				this.addCellaIntestazione(tableBP,boldFont,align,"CC Beneficiario");
				this.addCellaIntestazione(tableBP,boldFont,align,"Tipo");
				
				for(VersamentoTarBpDTO v : i.getListaVersBp()){
					
					this.addCella(tableBP, normalSmallFont, align, "0".equals(v.getNumRataRif()) ? "Unica Soluzione" : v.getNumRataRif());
					this.addCella(tableBP, normalSmallFont, align, v.getNumBollettino());
					this.addCella(tableBP, normalSmallFont, align, v.getDescTipoDoc());
					this.addCella(tableBP, normalSmallFont, align, v.getImporto());
					this.addCella(tableBP, normalSmallFont, align, v.getDtAccettazione()!=null ? SDF.format(v.getDtAccettazione()) : "");
					this.addCella(tableBP, normalSmallFont, align, v.getDtAccredito()!=null ? SDF.format(v.getDtAccredito()) : "");
					this.addCella(tableBP, normalSmallFont, align, v.getCcBeneficiario());
					this.addCella(tableBP, normalSmallFont, align, v.getTipoDoc());
				}
				
				table.addCell(tableBP);
			}else{
				PdfPCell cellInt = table.getDefaultCell();
				cellInt.setHorizontalAlignment(align);
				cellInt.setBackgroundColor(BaseColor.WHITE);
				cellInt.addElement(new Phrase("Versamenti Postali non Presenti",normalSmallFont));
				table.addCell(cellInt);
			}
		}else{
			String motivo = "Fonte "+this.verTarsuBPFonte.getDescrizione()+" non configurata per la visualizzazione nel PDF";
			Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.ITALIC);
			font.setColor(BaseColor.RED);
			
			PdfPCell cellInt = table.getDefaultCell();
			cellInt.setHorizontalAlignment(align);
			cellInt.setBackgroundColor(BaseColor.WHITE);
			cellInt.addElement(new Phrase(motivo,font));
			table.addCell(cellInt);
		}
		
	}
	
	private void addSezioneRTares(Document document, FonteDTO fonte) throws DocumentException {
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		Paragraph paragrafo = new Paragraph();
		
		if(fonte.isAbilitataCC()){
			
			paragrafo.add(fonte.getStrDataAgg());
			paragrafo.add(new Phrase(fonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafo, 2);
					
			if (listaRuoliTares!=null && listaRuoliTares.size()>0)
			{
				paragrafo.add(new Phrase("Ruolo Tares",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable table = new PdfPTable(13);
				table.setWidths(new float[] {4,6,19,17,5,6,6,6,6,6,6,6,7});
				table.setWidthPercentage(100);
				table.getDefaultCell().setMinimumHeight(30);
				int align = Element.ALIGN_CENTER;
				
				this.addCellaIntestazione(table,boldFont,align,"Anno");
				this.addCellaIntestazione(table,boldFont,align,"Tipo");
				this.addCellaIntestazione(table,boldFont,align,"Contribuente");
				this.addCellaIntestazione(table,boldFont,align,"Indirizzo");
				this.addCellaIntestazione(table,boldFont,align,"n.Imm.");
				
				this.addCellaIntestazione(table,boldFont,align,"Acconto");
				
				this.addCellaIntestazione(table,boldFont,align,"Tot.Netto");
				this.addCellaIntestazione(table,boldFont,align,"IVA");
				this.addCellaIntestazione(table,boldFont,align,"Trib.Prov.");
				
				this.addCellaIntestazione(table,boldFont,align,"Totale");
				this.addCellaIntestazione(table,boldFont,align,"Quota Ente");
				this.addCellaIntestazione(table,boldFont,align,"Quota Stato");
				
				this.addCellaIntestazione(table,boldFont,align,"Fattura");
				
				
				for (Iterator<RuoloTaresDTO> it = listaRuoliTares.iterator();it.hasNext();)
				{
					RuoloTaresDTO i= it.next();
					SitRuoloTares r = i.getRuolo();
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, r.getAnno());
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, "A".equalsIgnoreCase(r.getTipo())? "Principale" : "Suppletivo");
					this.addCella(table,normalSmallFont,Element.ALIGN_LEFT, r.getNominativoContrib()+" c.f. "+r.getCodfisc());
					this.addCella(table,normalSmallFont,Element.ALIGN_LEFT, r.getIndirizzo()+" "+r.getComune()+
											(r.getProv()!=null ? " ("+r.getProv()+")" : "")+
											(r.getEstero()!=null ? " "+r.getEstero() : ""));
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, r.getNimm().toString());
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, r.getOldTot()!=null ?  DF.format(r.getOldTot().doubleValue()): "");
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, r.getTotNetto()!=null ? DF.format(r.getTotNetto().doubleValue()): "");
					String addIVA =r.getAddIva()!=null ? (DF.format(r.getAddIva())+(r.getPercIva()!=null ? " ("+DF.format(r.getPercIva())+"%)" : "")) : "";
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, addIVA);
					String tribProv =r.getTribProv()!=null ? (DF.format(r.getTribProv())+(r.getPercTribProv()!=null ? " ("+DF.format(r.getPercTribProv())+"%)" : "")) : "";
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, tribProv);
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, r.getTotale()!=null ? DF.format(r.getTotale().doubleValue()): "");
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, r.getTotaleEnte()!=null ? DF.format(r.getTotaleEnte().doubleValue()): "");
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, r.getTotaleStato()!=null ? DF.format(r.getTotaleStato().doubleValue()): "");
					this.addCella(table,normalSmallFont,Element.ALIGN_LEFT, "n." +r.getNumFatt()+" del "+SDF.format(r.getDataFatt()));
				}
				paragrafo.add(table);
				
				this.addEmptyLine(paragrafo, 2);
				
			}else{
				paragrafo.add(new Paragraph("Ruolo Tares non presente", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafo, 2);
			}	
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafo, fonte.getDescrizione());
		
		document.add(paragrafo);
	}
	
	
	
	private void addSezioneIciDM(Document document, FonteDTO fonte) throws DocumentException {
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		
		Paragraph paragrafoIci = new Paragraph();
		
		if(fonte.isAbilitataCC()){
		
			paragrafoIci.add(fonte.getStrDataAgg());
			paragrafoIci.add(new Phrase(fonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoIci, 2);
					
			if (listaVersamentiIciDm!=null && listaVersamentiIciDm.size()>0)
			{
				paragrafoIci.add(new Phrase("Elenco Versamenti Ici",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable table = new PdfPTable(14);
				table.setWidths(new float[] {6,3,11,3,8,8,8,8,8,8,8,5,3,6});
				table.setWidthPercentage(100);
				table.getDefaultCell().setMinimumHeight(30);
				int align = Element.ALIGN_CENTER;
				
				this.addCellaIntestazione(table,boldFont,align,"Data Versamento");
				this.addCellaIntestazione(table,boldFont,align,"Cod. Ente");
				this.addCellaIntestazione(table,boldFont,align,"Contribuente");
				this.addCellaIntestazione(table,boldFont,align,"Anno Rif.");
				this.addCellaIntestazione(table,boldFont,align,"Imp.Versato");
				this.addCellaIntestazione(table,boldFont,align,"Imp.Terr.");
				this.addCellaIntestazione(table,boldFont,align,"Imp.Aree Fabb.");
				this.addCellaIntestazione(table,boldFont,align,"Imp.Ab.Princ.");
				this.addCellaIntestazione(table,boldFont,align,"Imp.Altri Fabb.");
				this.addCellaIntestazione(table,boldFont,align,"Detr.Ab.Princ.");
				this.addCellaIntestazione(table,boldFont,align,"Acc./Saldo");
				this.addCellaIntestazione(table,boldFont,align,"Ravv.");
				this.addCellaIntestazione(table,boldFont,align,"Num. Imm.");
				this.addCellaIntestazione(table,boldFont,align,"Comune Imm.");
				
				for (Iterator<VersamentoIciDmDTO> it = listaVersamentiIciDm.iterator();it.hasNext();)
				{
					VersamentoIciDmDTO i= it.next();
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, SDF.format(i.getDtVersamento()));
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, i.getCodEnte());
					this.addCella(table,normalSmallFont,Element.ALIGN_LEFT, i.getCfVersante());
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, i.getAnnoImposta());
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpVersato().doubleValue()));
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpTerrAgricoli().doubleValue()));
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpAreeFabbricabili().doubleValue()));
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpAbitazPrincipale().doubleValue()));
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpAltriFabbricati().doubleValue()));
					this.addCella(table,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpDetrazione().doubleValue()));
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, i.getDesAccSaldo());
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, i.getDesRavvedimento());
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, i.getNumFabb().toString());
					this.addCella(table,normalSmallFont,Element.ALIGN_CENTER, i.getComuneImm());
					
				}
				paragrafoIci.add(table);
				
				this.addEmptyLine(paragrafoIci, 2);
				
			}else{
				paragrafoIci.add(new Paragraph("Nessun versamento ICI da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoIci, 2);
			}	
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafoIci, fonte.getDescrizione());
		
		document.add(paragrafoIci);
	}
	
	
	private void addSezioneF24(Document document, FonteDTO fonte) throws DocumentException {
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat DF = new DecimalFormat("0.00");
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font normalSmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
		Paragraph paragrafoF24 = new Paragraph();
		if(fonte.isAbilitataCC()){
		
			paragrafoF24.add(fonte.getStrDataAgg());
			paragrafoF24.add(new Phrase(fonte.getNota(),normalSmallFont));
			this.addEmptyLine(paragrafoF24, 2);
					
			if (listaVersamentiF24!=null && listaVersamentiF24.size()>0)
			{
				paragrafoF24.add(new Phrase("Elenco Versamenti F24",new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				
				PdfPTable tableF24 = new PdfPTable(21);
				tableF24.setWidths(new float[] {4,7,7,5.5f,3,3,8,11,8,5.5f,8,3,3,3,3,3,3,3,3,3,3});
				tableF24.setWidthPercentage(100);
				tableF24.getDefaultCell().setMinimumHeight(30);
				int align = Element.ALIGN_CENTER;
				
				this.addCellaIntestazione(tableF24,boldFont,align,"Imposta");
				this.addCellaIntestazione(tableF24,boldFont,align,"Fornitura");
				this.addCellaIntestazione(tableF24,boldFont,align,"Ripartizione");
				this.addCellaIntestazione(tableF24,boldFont,align,"Bonifico");
				this.addCellaIntestazione(tableF24,boldFont,align,"Prog");
				this.addCellaIntestazione(tableF24,boldFont,align,"Riga");
				this.addCellaIntestazione(tableF24,boldFont,align,"Ente");
				this.addCellaIntestazione(tableF24,boldFont,align,"Contribuente");
				this.addCellaIntestazione(tableF24,boldFont,align,"Altro Sogg.");
				this.addCellaIntestazione(tableF24,boldFont,align,"Riscoss.");
				this.addCellaIntestazione(tableF24,boldFont,align,"Tributo");
				this.addCellaIntestazione(tableF24,boldFont,align,"Anno Rif.");
				this.addCellaIntestazione(tableF24,boldFont,align,"Credito");
				this.addCellaIntestazione(tableF24,boldFont,align,"Debito");
				this.addCellaIntestazione(tableF24,boldFont,align,"Detr.");
				this.addCellaIntestazione(tableF24,boldFont,align,"Acconto");
				this.addCellaIntestazione(tableF24,boldFont,align,"Saldo");
				this.addCellaIntestazione(tableF24,boldFont,align,"Ravv.");
				this.addCellaIntestazione(tableF24,boldFont,align,"Imm.Var.");
				this.addCellaIntestazione(tableF24,boldFont,align,"num.Imm.");
				this.addCellaIntestazione(tableF24,boldFont,align,"Rateaz.");
				
				for (Iterator<F24VersamentoDTO> it = listaVersamentiF24.iterator();it.hasNext();)
				{
					F24VersamentoDTO i= it.next();
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, i.getDescTipoImposta());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, SDF.format(i.getDtFornitura())+" - "+i.getProgFornitura().toString());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, SDF.format(i.getDtRipartizione())+" - "+i.getProgRipartizione());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, SDF.format(i.getDtBonifico()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getProgDelega().toString());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getProgRiga().toString());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, i.getDescTipoEnteRd() +" "+ i.getDescCodEnteRd()+":"+i.getCodEnteRd()+" Cod.CAB:"+i.getCab());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, i.getCf());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, (i.getCf2()!=null? i.getCf2()+"("+i.getDescTipoCf2()+")":""));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, SDF.format(i.getDtRiscossione()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, i.getCodTributo()+"-"+i.getDescTipoTributo());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getAnnoRif().toString());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpCredito().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getImpDebito().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_RIGHT, DF.format(i.getDetrazione().doubleValue()));
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getAcconto().compareTo(new BigDecimal(0))==0? "NO" : "SI");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getSaldo().compareTo(new BigDecimal(0))==0? "NO" : "SI");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getRavvedimento().compareTo(new BigDecimal(0))==0? "NO" : "SI");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getVarImmIciImu().compareTo(new BigDecimal(0))==0? "NO" : "SI");
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_CENTER, i.getNumFabbIciImu().toString());
					this.addCella(tableF24,normalSmallFont,Element.ALIGN_LEFT, i.getRateazione().toString());
					
				}
				paragrafoF24.add(tableF24);
				
				this.addEmptyLine(paragrafoF24, 2);
				
			}else{
				paragrafoF24.add(new Paragraph("Nessun versamento da visualizzare", new Font(Font.FontFamily.TIMES_ROMAN, 24,Font.BOLD)));
				this.addEmptyLine(paragrafoF24, 2);
			}	
		}else 
			this.addMotivoFonteDisabilitataCC(paragrafoF24, fonte.getDescrizione());
		
		document.add(paragrafoF24);
	}

	@Override
	public void ClearCampiTAB() {
		listaVersamentiF24 = new ArrayList<F24VersamentoDTO>();
		listaVersamentiIciDm = new ArrayList<VersamentoIciDmDTO>();
	}


	public FonteDTO getF24Fonte() {
		return f24Fonte;
	}


	public void setF24Fonte(FonteDTO f24Fonte) {
		this.f24Fonte = f24Fonte;
	}


	public FonteDTO getrTaresFonte() {
		return rTaresFonte;
	}


	public void setrTaresFonte(FonteDTO rTaresFonte) {
		this.rTaresFonte = rTaresFonte;
	}


	public FonteDTO getrTarsuFonte() {
		return rTarsuFonte;
	}


	public void setrTarsuFonte(FonteDTO rTarsuFonte) {
		this.rTarsuFonte = rTarsuFonte;
	}


	public FonteDTO getVerTarsuBPFonte() {
		return verTarsuBPFonte;
	}


	public void setVerTarsuBPFonte(FonteDTO verTarsuBPFonte) {
		this.verTarsuBPFonte = verTarsuBPFonte;
	}


	public FonteDTO getVerIciDMFonte() {
		return verIciDMFonte;
	}


	public void setVerIciDMFonte(FonteDTO verIciDMFonte) {
		this.verIciDMFonte = verIciDMFonte;
	}


	public List<F24VersamentoDTO> getListaVersamentiF24() {
		return listaVersamentiF24;
	}


	public void setListaVersamentiF24(List<F24VersamentoDTO> listaVersamentiF24) {
		this.listaVersamentiF24 = listaVersamentiF24;
	}

	public List<VersamentoIciDmDTO> getListaVersamentiIciDm() {
		return listaVersamentiIciDm;
	}

	public void setListaVersamentiIciDm(
			List<VersamentoIciDmDTO> listaVersamentiIciDm) {
		this.listaVersamentiIciDm = listaVersamentiIciDm;
	}

	public List<RuoloTaresDTO> getListaRuoliTares() {
		return listaRuoliTares;
	}

	public void setListaRuoliTares(List<RuoloTaresDTO> listaRuoliTares) {
		this.listaRuoliTares = listaRuoliTares;
	}

	public List<RuoloTarsuDTO> getListaRuoliTarsu() {
		return listaRuoliTarsu;
	}

	public void setListaRuoliTarsu(List<RuoloTarsuDTO> listaRuoliTarsu) {
		this.listaRuoliTarsu = listaRuoliTarsu;
	}

	public RuoloTaresDTO getSelRTares() {
		return selRTares;
	}

	public void setSelRTares(RuoloTaresDTO selRTares) {
		this.selRTares = selRTares;
	}

	public RuoloTarsuDTO getSelRTarsu() {
		return selRTarsu;
	}

	public void setSelRTarsu(RuoloTarsuDTO selRTarsu) {
		this.selRTarsu = selRTarsu;
	}
	
	
	
}
