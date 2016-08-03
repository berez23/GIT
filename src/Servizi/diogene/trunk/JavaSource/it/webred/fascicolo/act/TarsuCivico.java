package it.webred.fascicolo.act;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.aggregator.ejb.tributiFabbricato.dto.DatiTarsuDTO;
import it.webred.ct.aggregator.ejb.tributiFabbricato.TarsuFabbricatoService;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.fascicolo.FascicoloActionRequestHandler;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class TarsuCivico extends FascicoloActionRequestHandler
{

	
	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		particella = levaZeri(particella);
		sub = levaZeri(sub);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		
		log.debug("OGGETTI TARSU CIVICI - DATA RIF: ["+filtroData+"]" );
		
		CeTUser utente = (CeTUser) request.getSession().getAttribute("user");
		
		String enteId = utente.getCurrentEnte();
		String userId = utente.getUsername();
		String sessionId =utente.getSessionId();
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("provenienza.dati.tarsu");
		criteria.setComune(enteId);
		criteria.setSection("param.comune");
		String provenienza = this.doGetListaKeyValue(criteria);
		
		RicercaOggettoDTO ro = new RicercaOggettoDTO();
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setSub(null);
		ro.setProvenienza(provenienza);
		if(filtroData!=null)
			ro.setDtRif(new Date(filtroData));
		ro.setEnteId(enteId);
		ro.setUserId(userId);
		
		TarsuFabbricatoService tarsuService = (TarsuFabbricatoService)getEjb("CT_Service", "CT_Service_Aggregator_EJB", "TarsuFabbricatoServiceBean");
		
		List<DatiTarsuDTO> listaTarsuUI = tarsuService.getDatiTarsuCiviciDelFabbricato(ro);
		
		
		for(DatiTarsuDTO tar : listaTarsuUI)
			ritorno.add(tar);
		
		return ritorno;
	}
	
	public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
	throws Exception
	{
		LinkedList<Object> ritorno = new LinkedList<Object>();
		LinkedList<Object> dati = leggiDati(request, codNazionale,foglio,particella,sub,situazione,filtroData);
			       
		if(dati != null)
		{
			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,1f,1f,1f,1f,1f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("TARSU AL CIVICO");
			cell.setColspan(7);		
			table.addCell(cell);
			table.addCell(nullsafeCellH("INDIRIZZO"));
			table.addCell(nullsafeCellH("DATA"));
			table.addCell(nullsafeCellH("FOGLIO"));
			table.addCell(nullsafeCellH("PARTICELLA"));
			table.addCell(nullsafeCellH("SUBALTERNO"));
			table.addCell(nullsafeCellH("SUP.TOT"));
			table.addCell(nullsafeCellH("PROVENIENZA"));
		

			for(Object d : dati)
			{
				
				DatiTarsuDTO c = (DatiTarsuDTO)d;
				
				table.addCell(nullsafeCell(c.getIndirizzo().getDesIndirizzo()));
				table.addCell(nullsafeCell(c.getDtIniPos()+" - "+c.getDescrDtFinPos()));
				table.addCell(nullsafeCell(c.getFoglio()));
				table.addCell(nullsafeCell(c.getParticella()));
				table.addCell(nullsafeCell(c.getSub()));
				table.addCell(nullsafeCell(c.getSupTot()));
				table.addCell(nullsafeCell(c.getProvenienza()));
			
			}
			ritorno.add(table);
		}	
		return ritorno;
	}
	
	private String doGetListaKeyValue(ParameterSearchCriteria criteria)
	{
		
		
		try
		{
			ParameterService parameterService= (ParameterService)getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
			if (parameterService==null)
			{
				log.debug(" Utility_doGetListaKeyValue_parameterService == NULL");
				return "";
			}
			
			List<AmKeyValueDTO> l = parameterService.getListaKeyValue(criteria);
			if (l!=null && l.size()>0)
				return ((AmKeyValueDTO)l.get(0)).getAmKeyValueExt().getValueConf();
			else
			{
				log.debug(" LISTA RITORNO DA parameterService.getListaKeyValue VUOTA");
				return "";
			}
		}
		catch (Exception ex)
		{
			log.error(" doGetListaKeyValue ERRORE = " + ex.getMessage(),ex);
			return "";
		}

	}

}
