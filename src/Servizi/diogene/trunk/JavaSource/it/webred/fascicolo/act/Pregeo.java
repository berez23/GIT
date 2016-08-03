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

  

public class Pregeo extends FascicoloActionRequestHandler
{

	

	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
//		particella = levaZeri(particella);
		sub = levaZeri(sub);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/pregeo.xml");
		String sqlPregeo = prop.getProperty("sqlPregeo");
		
//		if(sub != null && !sub.trim().equals(""))
//			sqlConcessioni += prop.getProperty("sqlConcessioniSub");
		sqlPregeo += prop.getProperty("sqlPregeoOrder");
		Connection con = getConnectionDiogene(request);
		try
		{
			RowSetDynaClass row = genericFPQuery(con, sqlPregeo, null, foglio, particella, null);
			ritorno.add(row);
		}
		finally
		{
			if (con != null)
				con.close();
		}		
		
		return ritorno;
	}
	
	public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
	throws Exception
	{
		LinkedList<Object> ritorno = new LinkedList<Object>();
		LinkedList<Object> dati = leggiDati(request, codNazionale, foglio, particella, sub, situazione, filtroData);
			       
		if(dati.get(0) != null)
		{
			PdfPTable table = new PdfPTable(8);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,1f,1f,1f,1f,1f,1f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("PREGEO");
			cell.setColspan(8);		
			table.addCell(cell);
			table.addCell(nullsafeCellH("DATA"));
			table.addCell(nullsafeCellH("CODICE"));
			table.addCell(nullsafeCellH("DENOMINAZIONE"));
			table.addCell(nullsafeCellH("TIPO TECNICO"));
			table.addCell(nullsafeCellH("TECNICO"));
			table.addCell(nullsafeCellH("FOGLIO"));
			table.addCell(nullsafeCellH("PARTICELLA"));
			table.addCell(nullsafeCellH("PDF"));

			for(BasicDynaBean d : (List<BasicDynaBean>)((RowSetDynaClass) dati.get(0)).getRows())
			{

				table.addCell(nullsafeCell(d.get("data_pregeo")));
				table.addCell(nullsafeCell(d.get("codice_pregeo")));
				table.addCell(nullsafeCell(d.get("denominazione")));
				table.addCell(nullsafeCell(d.get("tipo_tecnico")));
				table.addCell(nullsafeCell(d.get("tecnico")));
				table.addCell(nullsafeCell(d.get("foglio")));
				table.addCell(nullsafeCell(d.get("particella")));
				table.addCell(nullsafeCell(d.get("nome_file_pdf")));
			}
			ritorno.add(table);
		}	
		return ritorno;
	}

}
