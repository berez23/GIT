package it.webred.fascicolo.act;

import it.webred.fascicolo.FascicoloActionRequestHandler;
import it.webred.utils.GenericTuples;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.sql.RowSet;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.commons.beanutils.WrapDynaBean;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

  

public class Ici extends FascicoloActionRequestHandler
{

	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		particella = levaZeri(particella);
		sub = levaZeri(sub);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/ici.xml");
		String sqlsqlIci = prop.getProperty("sqlIci");
		
		if (situazione != null && situazione.equalsIgnoreCase("ATTUALE")){
			sqlsqlIci = prop.getProperty("sqlIciAttuali");
		}else if (situazione != null && situazione.equalsIgnoreCase("COMPLETA")){
			sqlsqlIci = prop.getProperty("sqlIci");
		}else if (situazione != null && situazione.equalsIgnoreCase("ALLA_DATA")){
			sqlsqlIci = prop.getProperty("sqlIci");
		}
		
		
		if(sub != null && !sub.trim().equals(""))
			sqlsqlIci += prop.getProperty("sqlIciSub");
		
		if (situazione != null && situazione.equalsIgnoreCase("ATTUALE")){
			sqlsqlIci += prop.getProperty("sqlIciOrderAttuali");;
		}else if (situazione != null && situazione.equalsIgnoreCase("COMPLETA")){
			sqlsqlIci += prop.getProperty("sqlIciOrder");
		}else if (situazione != null && situazione.equalsIgnoreCase("ALLA_DATA")){
			sqlsqlIci += prop.getProperty("sqlIciOrder");
		}
		
		Connection con = getConnectionDbTotale(request);
		try
		{
			RowSetDynaClass row = genericFPQuery(con, sqlsqlIci,null, foglio, particella,sub);
			
			if (situazione != null && situazione.equalsIgnoreCase("ATTUALE")){
				List rows = row.getRows();
				ArrayList<DynaBean> alDynaIci = new ArrayList<DynaBean>();
				
				if (rows != null && rows.size()>0){
					String annoMax = "-1";
					String annoCur ="0";
					String subMax = "-1";
					String subCur = "";
					
					Iterator<DynaBean> itIci = rows.iterator();
					while(itIci.hasNext()){
						DynaBean dyIci = itIci.next();
						if (dyIci != null){
							
							 foglio= (String)dyIci.get("foglio");
							 particella= (String)dyIci.get("particella");
							 String comune= (String)dyIci.get("fk_comuni");
							 
							 String[] latLon = this.getLatitudeLongitude(request, foglio, particella, comune);
							 
							 dyIci.set("latitudine", latLon[0]);
							 dyIci.set("longitudine", latLon[1]);
							
							annoCur = (String)dyIci.get("den_riferimento");
							subCur = (String)dyIci.get("subalterno");
							
							if ( subMax != null && subCur != null && subMax.trim().equalsIgnoreCase(subCur.trim()) ){
								
								if ( annoMax != null && annoCur != null && annoMax.equals(annoCur) ){
									alDynaIci.add(dyIci);
								}
							}else{
								annoMax = annoCur;
								subMax = subCur;
								
								alDynaIci.add(dyIci);
							}
						}							
					}
				}
				
				ritorno.add(alDynaIci);
			}else{
				
				List rows = row.getRows();
				ArrayList<DynaBean> alDynaIci = new ArrayList<DynaBean>();
				
				if (rows != null && rows.size()>0){
					Iterator<DynaBean> itIci = rows.iterator();
					while(itIci.hasNext()){
						DynaBean dyIci = itIci.next();
						if (dyIci != null){
							
							 foglio= (String)dyIci.get("foglio");
							 particella= (String)dyIci.get("particella");
							 String comune= (String)dyIci.get("fk_comuni");
							 
							 String[] latLon = this.getLatitudeLongitude(request, foglio, particella, comune);
							 
							 dyIci.set("latitudine", latLon[0]);
							 dyIci.set("longitudine", latLon[1]);
								
							 alDynaIci.add(dyIci);
						}
					}
				}
				
				ritorno.add(alDynaIci);				
			}

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
		LinkedList<Object> dati = leggiDati(request, codNazionale,foglio,particella,sub,situazione, filtroData);
			       
		if(dati.get(0) != null)
		{
			PdfPTable table = new PdfPTable(11);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f};
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("ICI");
			cell.setColspan(11);		
			table.addCell(cell);           
			table.addCell(nullsafeCellH("ANNO RIF"));
			table.addCell(nullsafeCellH("COMUNE"));
			table.addCell(nullsafeCellH("FOGLIO"));
			table.addCell(nullsafeCellH("PARTICELLA"));
			table.addCell(nullsafeCellH("SUBALTERNO"));
			table.addCell(nullsafeCellH("CATEGORIA"));
			table.addCell(nullsafeCellH("PROVENIENZA"));
			table.addCell(nullsafeCellH("CLASSE"));
			table.addCell(nullsafeCellH("RENDITA"));
			table.addCell(nullsafeCellH("STORICO"));
			table.addCell(nullsafeCellH("PRIMA CASA"));						

			for(BasicDynaBean d : (List<BasicDynaBean>)dati.get(0))
			{
				table.addCell(nullsafeCell(d.get("den_riferimento")));
				table.addCell(nullsafeCell(d.get("comune")));
				table.addCell(nullsafeCell(d.get("foglio")));
				table.addCell(nullsafeCell(d.get("particella")));	
				table.addCell(nullsafeCell(d.get("subalterno")));	
				table.addCell(nullsafeCell(d.get("categoria")));
				table.addCell(nullsafeCell(d.get("provenienza")));
				table.addCell(nullsafeCell(d.get("classe")));
				table.addCell(nullsafeCell(d.get("rendita_catastale")));
				table.addCell(nullsafeCell(d.get("immobile_storico")));
				table.addCell(nullsafeCell(d.get("abitazione_principale")));
			}
			ritorno.add(table);
		}	
		return ritorno;
	}
}
