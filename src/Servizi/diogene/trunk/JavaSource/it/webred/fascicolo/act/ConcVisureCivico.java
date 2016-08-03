package it.webred.fascicolo.act;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcVisuraDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.IndirizzoConcessioneDTO;
import it.webred.fascicolo.FascicoloActionRequestHandler;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

  

public class ConcVisureCivico extends FascicoloActionRequestHandler
{

	
	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		particella = levaZeri(particella);
		sub = levaZeri(sub);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		//Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/concessioni.xml");
		//String sqlConcessioni = prop.getProperty("sqlConcessioni");
		
		log.debug("DETTAGLIO CONCESSIONI EDILIZIE ARCHIVIO STORICO VISURE - CIVICI - DATA RIF: ["+filtroData+"]" );
		
		CeTUser utente = (CeTUser) request.getSession().getAttribute("user");
		
		String enteId = utente.getCurrentEnte();
		String userId = utente.getUsername();
		String sessionId =utente.getSessionId();
		
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setSub(null);
		//ro.setDtVal(filtroData);
		
		ro.setEnteId(enteId);
		ro.setUserId(userId);
		
		ConcessioniEdilizieService concessioniEdilizieService = (ConcessioniEdilizieService)getEjb("CT_Service", "CT_Service_Data_Access", "ConcessioniEdilizieServiceBean");
		
		List<ConcVisuraDTO> listaConcEdiCivici=concessioniEdilizieService.getVisureCiviciDelFabbricato(ro);
		
		for(ConcVisuraDTO conc : listaConcEdiCivici)
			ritorno.add(conc);
		
		return ritorno;
	}
	
	public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
	throws Exception
	{
		LinkedList<Object> ritorno = new LinkedList<Object>();
		LinkedList<Object> dati = leggiDati(request, codNazionale,foglio,particella,sub,situazione,filtroData);
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		if(dati != null)
		{
			PdfPTable table = new PdfPTable(8);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,1f,1f,1f,1f,1f,1f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("CONCESSIONI (ARCHIVIO STORICO VISURE) AL CIVICO");
			cell.setColspan(8);		
			table.addCell(cell);
			table.addCell(nullsafeCellH("TIPO ATTO"));
			table.addCell(nullsafeCellH("INTESTATARIO"));
			table.addCell(nullsafeCellH("NUM.ATTO"));
			table.addCell(nullsafeCellH("INDIRIZZO"));
			table.addCell(nullsafeCellH("CIVICO"));
			table.addCell(nullsafeCellH("NUM.PROT.GEN"));
			table.addCell(nullsafeCellH("NUM.PROT.SETT."));
			table.addCell(nullsafeCellH("DATA DOC."));
			
			for(Object d : dati)
			{
				
				ConcVisuraDTO c = (ConcVisuraDTO)d;
				
				table.addCell(nullsafeCell(c.getTipoAtto()+"-"+c.getDescTipoAtto()));
				table.addCell(nullsafeCell(c.getNomeIntestatario()));
				table.addCell(nullsafeCell(c.getNumeroAtto()));
				table.addCell(nullsafeCell(c.getPrefisso()+" "+c.getNomeVia()));
				table.addCell(nullsafeCell(c.getCivicoSub()));
				table.addCell(nullsafeCell(c.getNumProtGen()));
				table.addCell(nullsafeCell(c.getNumProtSett()));
				table.addCell(nullsafeCell(SDF.format(c.getDataDoc())));
				
			}
			ritorno.add(table);
		}	
		return ritorno;
	}

}
