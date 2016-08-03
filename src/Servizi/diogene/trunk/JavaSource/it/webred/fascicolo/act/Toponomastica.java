package it.webred.fascicolo.act;

import it.webred.fascicolo.FascicoloActionRequestHandler;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

  

public class Toponomastica extends FascicoloActionRequestHandler
{

	

	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		particella = levaZeri(particella);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/toponomastica.xml");
		String sqlToponomastica = prop.getProperty("sqlToponomastica");
		Connection con = getConnectionDbTotale(request);
		try
		{
			RowSetDynaClass row = genericFPQuery(con, sqlToponomastica,null, foglio, particella,null);
			
			List rows = row.getRows();
			ArrayList<DynaBean> alDynaTop = new ArrayList<DynaBean>();
			
			if (rows != null && rows.size()>0){
				Iterator<DynaBean> itTop = rows.iterator();
				while(itTop.hasNext()){
					DynaBean dyTop = itTop.next();
					if (dyTop != null){
						
						 String comune= (String)dyTop.get("cod_nazionale");
						 
						 String[] latLon = this.getLatitudeLongitude(request, foglio, particella, comune);
						 dyTop.set("latitudine", latLon[0]);
						 dyTop.set("longitudine", latLon[1]);
							
						 alDynaTop.add(dyTop);
					}
				}
			}
			
			ritorno.add(alDynaTop);	
			
			
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
			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,8f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("TOPONOMASTICA");
			cell.setColspan(3);		
			table.addCell(cell);           
			table.addCell(nullsafeCellH("SEDIME"));
			table.addCell(nullsafeCellH("NOME VIA"));
			table.addCell(nullsafeCellH("CIVICO"));

					

			for(BasicDynaBean d : (List<BasicDynaBean>)dati.get(0))
			{
				table.addCell(nullsafeCell(d.get("sedime")));
				table.addCell(nullsafeCell(d.get("nome_via")));
				table.addCell(nullsafeCell(d.get("descrizione_civico")));	
			}
			ritorno.add(table);
		}	
		return ritorno;
	}

}
