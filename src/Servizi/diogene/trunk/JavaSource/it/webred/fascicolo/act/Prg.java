package it.webred.fascicolo.act;

import it.webred.fascicolo.FascicoloActionRequestHandler;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;


public class Prg extends FascicoloActionRequestHandler
{


	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		particella = levaZeri(particella);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/prg.xml");
		String sqlPrg = prop.getProperty("sqlPrg");
		Connection con = getConnectionDiogene(request);
		try
		{
			RowSetDynaClass row = genericFPQueryDouble(con, sqlPrg,codNazionale, foglio, particella);
			ritorno.add(row);
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
			PdfPTable table = new PdfPTable(8);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,1f,2f,3f,1f,1f,1f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("PRG");
			cell.setColspan(6);		
			table.addCell(cell);           
			table.addCell(nullsafeCellH("FOGLIO"));
			table.addCell(nullsafeCellH("PARTICELLA"));	
			table.addCell(nullsafeCellH("DEST FUNZ"));
			table.addCell(nullsafeCellH("LEGENDA"));
			table.addCell(nullsafeCellH("AREA PART"));
			table.addCell(nullsafeCellH("AREA PRG"));
			//table.addCell(nullsafeCellH("AREA INT"));
			//table.addCell(nullsafeCellH("DAL - AL"));	

					

			for(BasicDynaBean d : (List<BasicDynaBean>)((RowSetDynaClass) dati.get(0)).getRows())
			{
				table.addCell(nullsafeCell(d.get("foglio")));
				table.addCell(nullsafeCell(d.get("particella")));
				table.addCell(nullsafeCell(d.get("dest_funz")));
				table.addCell(nullsafeCell(d.get("legenda")));
				table.addCell(nullsafeCell(d.get("area_part")));
				table.addCell(nullsafeCell(d.get("area_prg")));
				//table.addCell(nullsafeCell(d.get("area_int")));
				//table.addCell(nullsafeCell(d.get("data_inizio") + " - "+ d.get("data_fine")));
			}
			ritorno.add(table);
		}	
		return ritorno;
	}

}
