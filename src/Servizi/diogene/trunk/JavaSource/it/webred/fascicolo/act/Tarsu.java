package it.webred.fascicolo.act;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.aggregator.ejb.tributiFabbricato.TarsuFabbricatoService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.fascicolo.FascicoloActionRequestHandler;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

  

public class Tarsu extends FascicoloActionRequestHandler
{

	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		particella = levaZeri(particella);
		sub = levaZeri(sub);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/tarsu.xml");
		String sqlsqlTarsu = prop.getProperty("sqlTarsu");
		
		if (situazione != null && situazione.equalsIgnoreCase("ATTUALE")){
			sqlsqlTarsu = prop.getProperty("sqlTarsuAttuali");
		}else if (situazione != null && situazione.equalsIgnoreCase("COMPLETA")){
			sqlsqlTarsu = prop.getProperty("sqlTarsu");
		}else if (situazione != null && situazione.equalsIgnoreCase("ALLA_DATA")){
			sqlsqlTarsu = prop.getProperty("sqlTarsu");
		}
		
		if(sub != null && !sub.trim().equals(""))
			sqlsqlTarsu += prop.getProperty("sqlTarsuSub");
		sqlsqlTarsu += prop.getProperty("sqlTarsuOrder");
		Connection con = getConnectionDbTotale(request);
		try
		{
			RowSetDynaClass row = genericFPQuery(con, sqlsqlTarsu,null, foglio, particella,sub);
			
			List rows = row.getRows();
			ArrayList<DynaBean> alDynaTarsu = new ArrayList<DynaBean>();
			
			if (rows != null && rows.size()>0){
				Iterator<DynaBean> itTarsu = rows.iterator();
				while(itTarsu.hasNext()){
					DynaBean dyTarsu = itTarsu.next();
					if (dyTarsu != null){
						
						
						 foglio= (String)dyTarsu.get("foglio");
						 particella= (String)dyTarsu.get("particella");
						 String comune= (String)dyTarsu.get("fk_comuni");
						 
						 String[] latLon = this.getLatitudeLongitude(request, foglio, particella, comune);
						 
						  dyTarsu.set("latitudine", latLon[0]);
						  dyTarsu.set("longitudine", latLon[1]);
					
						  
					/*	 GenericTuples.T2<String,String> coord = null;
							try {
								coord = getLatitudeLongitude(con, foglio, particella, comune);
							} catch (Exception e) {
							}
							if (coord!=null) {
								
								dyTarsu.set("latitudine", coord.firstObj);
								dyTarsu.set("longitudine", coord.secondObj);
							}
							else {
								dyTarsu.set("latitudine", "0");
								dyTarsu.set("longitudine", "0");
							}*/
							
							alDynaTarsu.add(dyTarsu);
					}
				}
			}
			
			ritorno.add(alDynaTarsu);	
			
			//ritorno.add(row);
		}
		finally
		{
			con.close();
		}		
		
		return ritorno;
	}
	
	public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
	throws Exception
	{
		LinkedList<Object> ritorno = new LinkedList<Object>();
		LinkedList<Object> dati = leggiDati(request, codNazionale,foglio,particella,sub,situazione,filtroData);
			       
		if(dati.get(0) != null)
		{
			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,1f,1f,1f,1f,1f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("TARSU");
			cell.setColspan(7);		
			table.addCell(cell);
			table.addCell(nullsafeCellH("DATA"));
			table.addCell(nullsafeCellH("COMUNE"));
			table.addCell(nullsafeCellH("FOGLIO"));
			table.addCell(nullsafeCellH("PARTICELLA"));
			table.addCell(nullsafeCellH("SUBALTERNO"));
			table.addCell(nullsafeCellH("SUPERFICIE TOTALE"));
			table.addCell(nullsafeCellH("PROVENIENZA"));
					

			for(BasicDynaBean d : (List<BasicDynaBean>)dati.get(0))
			{
				table.addCell(nullsafeCell(d.get("data_ini_ogge")+" - "+d.get("data_fine_ogge")));
				table.addCell(nullsafeCell(d.get("comune")));
				table.addCell(nullsafeCell(d.get("foglio")));
				table.addCell(nullsafeCell(d.get("particella")));	
				table.addCell(nullsafeCell(d.get("subalterno")));	
				table.addCell(nullsafeCell(d.get("superficie")));
				table.addCell(nullsafeCell(d.get("provenienza")));
			}
			ritorno.add(table);
		}	
		return ritorno;
	}

}
