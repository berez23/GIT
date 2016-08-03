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

  

public class Docfa extends FascicoloActionRequestHandler
{

	

	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/docfa.xml");
		String sqDocfa = prop.getProperty("sqlDocfa");
		
		if(sub != null && !sub.trim().equals(""))
			sqDocfa += prop.getProperty("sqlDocfaSub");
		sqDocfa += prop.getProperty("sqlDocfaOrder");
		String sqDocfaVie = prop.getProperty("sqlDocfaVie");
		Connection con = getConnectionDiogene(request);
		try
		{
			RowSetDynaClass row = genericFPQuery(con, sqDocfa,null, foglio, particella,sub);
			for(BasicDynaBean b: (List<BasicDynaBean>)row.getRows())
			{
				if(b.get("subalterno") == null || ((String)b.get("subalterno")).trim().equals(""))
					b.set("subalterno", "0");
				String vie = "";
				
				for(BasicDynaBean v: (List<BasicDynaBean>)genericFPQuery(con, sqDocfaVie,codNazionale, (String)b.get("foglio"), (String)b.get("particella"),(String)b.get("subalterno")).getRows())
				{
					vie +=v.get("nome")+" "+v.get("civico")+"@";
				}
				
				if(vie.lastIndexOf('@')>=0)
					vie = vie.substring(0, vie.lastIndexOf('@')-1);
				vie = vie.replaceAll("@", "<br/>");
				b.set("listavie", vie);
			}
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
			PdfPTable table = new PdfPTable(13);
			table.setWidthPercentage(100f);
			float[] widths2 = { 2f, 2f, 3f,1f, 1f, 1f, 2f, 1f, 1f, 1f, 3f, 3f, 3f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("DOCFA");
			cell.setColspan(13);		
			table.addCell(cell);           
			table.addCell(nullsafeCellH("PROTOCOLLO"));
			table.addCell(nullsafeCellH("DATA PROTOCOLLO"));
			table.addCell(nullsafeCellH("CAUSALE"));
			table.addCell(nullsafeCellH("SOPR."));
			table.addCell(nullsafeCellH("VAR."));
			table.addCell(nullsafeCellH("COST."));
			table.addCell(nullsafeCellH("OPERAZIONE"));
			table.addCell(nullsafeCellH("FOGL."));
			table.addCell(nullsafeCellH("PART."));
			table.addCell(nullsafeCellH("SUB."));
			table.addCell(nullsafeCellH("DICHIARANTE"));
			table.addCell(nullsafeCellH("INDIRIZZO PARTICELLA")); 
			table.addCell(nullsafeCellH("PROFESSIONISTA"));
			for(BasicDynaBean d : (List<BasicDynaBean>)((RowSetDynaClass) dati.get(0)).getRows())
			{
				table.addCell(nullsafeCell(d.get("protocollo")));
				table.addCell(nullsafeCell(d.get("data_registrazionef")));
				table.addCell(nullsafeCell(d.get("causale")));
				table.addCell(nullsafeCell(d.get("soppressione")));
				table.addCell(nullsafeCell(d.get("variazione")));
				table.addCell(nullsafeCell(d.get("costituzione")));
				table.addCell(nullsafeCell(d.get("operazione")));
				table.addCell(nullsafeCell(d.get("foglio")));
				table.addCell(nullsafeCell(d.get("particella")));
				table.addCell(nullsafeCell(d.get("subalterno")));
				table.addCell(nullsafeCell(d.get("dichiarante")));
				String listaVie = d.get("listavie").toString();
				listaVie = listaVie.replaceAll("<br/>", "\n");
				table.addCell(nullsafeCell(listaVie));
				table.addCell(nullsafeCell(d.get("tec_cognome") +" "+d.get("tec_nome")));
			}
			ritorno.add(table);
		}	
		return ritorno;
	}

}
