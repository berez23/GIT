package it.webred.fascicolo.act;

import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.IndirizzoConcessioneDTO;
import it.webred.fascicolo.FascicoloActionRequestHandler;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

  

public class ConcessioniCivico extends FascicoloActionRequestHandler
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
		
		log.debug("DETTAGLIO CONCESSIONI EDILIZIE CIVICI - DATA RIF: ["+filtroData+"]" );
		
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
		
		List<ConcessioneDTO> listaConcEdiCivici=concessioniEdilizieService.getDatiConcCiviciDelFabbricato(ro);
		
		for(ConcessioneDTO conc : listaConcEdiCivici)
			ritorno.add(conc);
		
		return ritorno;
	}
	
	public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
	throws Exception
	{
		LinkedList<Object> ritorno = new LinkedList<Object>();
		LinkedList<Object> dati = leggiDati(request, codNazionale,foglio,particella,sub,situazione,filtroData);
			       
		if(dati != null)
		{
			PdfPTable table = new PdfPTable(8);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,1f,1f,1f,1f,1f,1f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("CONCESSIONI AL CIVICO");
			cell.setColspan(8);		
			table.addCell(cell);
			table.addCell(nullsafeCellH("ANNO"));
			table.addCell(nullsafeCellH("PROG."));
			table.addCell(nullsafeCellH("NUMERO"));
			table.addCell(nullsafeCellH("OGGETTO"));
			table.addCell(nullsafeCellH("SOGGETTI"));
			table.addCell(nullsafeCellH("IMMOBILI"));
			table.addCell(nullsafeCellH("INDIRIZZI"));
			table.addCell(nullsafeCellH("PROV."));

			for(Object d : dati)
			{
				
				ConcessioneDTO c = (ConcessioneDTO)d;
				
				table.addCell(nullsafeCell(c.getProgAnno()));
				table.addCell(nullsafeCell(c.getProgNumero()));
				table.addCell(nullsafeCell(c.getConcNumero()));
				table.addCell(nullsafeCell(c.getOggetto()));
				table.addCell(nullsafeCell(c.getListaSoggettiHtml()));
				table.addCell(nullsafeCell(c.getStringaImmobili()));
				
				
				List<IndirizzoConcessioneDTO> cind = c.getListaIndirizzi();
				String lstIndirizzi = "";
				for(IndirizzoConcessioneDTO ind : cind)
					lstIndirizzi+= ind.getIndirizzo()+" "+ind.getCivico();
					
				table.addCell(nullsafeCell(lstIndirizzi));
				table.addCell(nullsafeCell(c.getProvenienza()));
			}
			ritorno.add(table);
		}	
		return ritorno;
	}

}
