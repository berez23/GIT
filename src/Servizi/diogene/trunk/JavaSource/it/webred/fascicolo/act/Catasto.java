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

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

  

public class Catasto extends FascicoloActionRequestHandler
{

	

	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		sub = levaZeri(sub);
		
		LinkedList<Object> ritorno = new LinkedList<Object>(); 
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/catasto.xml");
		String sqlDatiCatastali = "";
		String sqlCostituzioneCatastale = "";
		String sqlTitolarita = "";		
		String sqlaggregati = prop.getProperty("sqlaggregati");		
		
		if (situazione != null && situazione.equalsIgnoreCase("ATTUALE")){
			sqlDatiCatastali = prop.getProperty("sqlDatiCatastaliAttuali");
			sqlCostituzioneCatastale = prop.getProperty("sqlCostituzioneCatastaleAttuali");
			sqlTitolarita = prop.getProperty("sqlTitolaritaAttuali");		
		}else if (situazione != null && situazione.equalsIgnoreCase("COMPLETA")){
			sqlDatiCatastali = prop.getProperty("sqlDatiCatastali");
			sqlCostituzioneCatastale = prop.getProperty("sqlCostituzioneCatastale");
			sqlTitolarita = prop.getProperty("sqlTitolarita");		
		}else if (situazione != null && situazione.equalsIgnoreCase("ALLA_DATA")){
			sqlDatiCatastali = prop.getProperty("sqlDatiCatastali");
			sqlCostituzioneCatastale = prop.getProperty("sqlCostituzioneCatastale");
			sqlTitolarita = prop.getProperty("sqlTitolarita");		
		}
		else{
			sqlDatiCatastali = prop.getProperty("sqlDatiCatastali");
			sqlCostituzioneCatastale = prop.getProperty("sqlCostituzioneCatastale");
			sqlTitolarita = prop.getProperty("sqlTitolarita");	
		}
		
		if(sub != null && !sub.trim().equals(""))
		{
			sqlCostituzioneCatastale+=prop.getProperty("sqlCostituzioneCatastaleSub");
			sqlTitolarita += prop.getProperty("sqlTitolaritaSub");
		}
		sqlCostituzioneCatastale+=prop.getProperty("sqlCostituzioneCatastaleOrder");
		sqlTitolarita += prop.getProperty("sqlTitolaritaOrder");		
		
		Connection con = getConnectionDiogene(request);
		try
		{
			
			
			
			if(sub == null || sub.trim().equals(""))			{
				//ritorno.add(genericFPQuery(con, sqlDatiCatastali,codNazionale, foglio, particella, sub));
				
				RowSetDynaClass row = genericFPQuery(con, sqlDatiCatastali,codNazionale, foglio, particella, sub);
				
				List rows = row.getRows();
				ArrayList<DynaBean> alDynaCat = new ArrayList<DynaBean>();
				
				if (rows != null && rows.size()>0){
					Iterator<DynaBean> itCat = rows.iterator();
					while(itCat.hasNext()){
						DynaBean dyCat = itCat.next();
						if (dyCat != null){
							
							 String comune= (String)dyCat.get("cod_nazionale");
							 
							 String[] latLon = this.getLatitudeLongitude(request, foglio, particella, comune);
							 
							 dyCat.set("latitudine", latLon[0]);
							 dyCat.set("longitudine", latLon[1]);
								
							 alDynaCat.add(dyCat);
						}
					}
				}
				
				ritorno.add(alDynaCat);	
				
			}
			else
				ritorno.add(null);
			
			ritorno.add(genericFPQuery(con, sqlCostituzioneCatastale,codNazionale, foglio, particella,sub));
			ritorno.add(genericFPQuery(con, sqlTitolarita,codNazionale, foglio, particella,sub));
			
			if(sub == null || sub.trim().equals(""))
				ritorno.add(genericFPQuery(con, sqlaggregati,codNazionale, foglio, particella,sub));
			else
				ritorno.add(null);
			
			
			

			
		
		}
		finally
		{
			con.close();
		}		
		
		return ritorno;
	}

	
	public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String dataFiltro)
		throws Exception
	{
		LinkedList<Object> ritorno = new LinkedList<Object>();
		LinkedList<Object> dati = leggiDati(request, codNazionale,foglio,particella,sub,situazione,dataFiltro);
			       
		if(dati.get(0) != null)
		{
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100f);
			PdfPCell cell = nullsafeCellH("DATI CATASTALI");
			cell.setColspan(4);		
			table.addCell(cell);           
			table.addCell(nullsafeCellH("FOGLIO"));
			table.addCell(nullsafeCellH("PARTICELLA"));		
			table.addCell(nullsafeCellH("SUP. CENS."));
			table.addCell(nullsafeCellH("DATA FINE VAL."));
			for(BasicDynaBean d : (List<BasicDynaBean>)dati.get(0))
			{
				table.addCell(nullsafeCell(d.get("foglio")));
				table.addCell(nullsafeCell(d.get("particella")));
				table.addCell(nullsafeCell(d.get("superficie")));
				table.addCell(nullsafeCell(d.get("data_finef")));
			}
			ritorno.add(table);
		}
		if(dati.get(1) != null)
		{
			PdfPTable table = new PdfPTable(10);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f, 1f, 1f,1f, 1f, 1f,1f, 2f, 1f,1f };
			table.setWidths(widths2);			
			PdfPCell cell = nullsafeCellH("COSTITUZIONE CATASTALE");	
			cell.setColspan(10);
			table.addCell(cell);           
			table.addCell(nullsafeCellH("SUB."));
			table.addCell(nullsafeCellH("CAT."));
			table.addCell(nullsafeCellH("CLASSE"));
			table.addCell(nullsafeCellH("CONSISTENZA"));
			table.addCell(nullsafeCellH("RENDITA"));
			table.addCell(nullsafeCellH("SUP. U.I.U"));
			table.addCell(nullsafeCellH("PIANO"));
			table.addCell(nullsafeCellH("DAL - AL"));
			table.addCell(nullsafeCellH("ICI"));
			table.addCell(nullsafeCellH("TARSU"));
			for(BasicDynaBean d : (List<BasicDynaBean>)((RowSetDynaClass) dati.get(1)).getRows())
			{
				table.addCell(nullsafeCell(d.get("unimm")));
				table.addCell(nullsafeCell(d.get("categoria")));
				table.addCell(nullsafeCell(d.get("classe")));
				table.addCell(nullsafeCell(d.get("consistenza")));
				table.addCell(nullsafeCell(d.get("rendita")));
				table.addCell(nullsafeCell(d.get("sup_cat")));
				table.addCell(nullsafeCell(d.get("piano")));
				table.addCell(nullsafeCell(d.get("data_inizio_valf") + " - "+ d.get("data_fine_valf")));
				table.addCell(nullsafeCell(d.get("flag_ici")));
				table.addCell(nullsafeCell(d.get("flag_tarsu")));
			}
			ritorno.add(table);
		}
		
		if(dati.get(2) != null)
		{
			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100f);
			float[] widths2 = { 1f, 4f, 1f,2f, 1f};
			table.setWidths(widths2);				
			PdfPCell cell = nullsafeCellH("TITOLARITA'");
			cell.setColspan(5);		
			table.addCell(cell);           
			table.addCell(nullsafeCellH("SUB."));
			table.addCell(nullsafeCellH("SOGGETTO"));		
			table.addCell(nullsafeCellH("TITOLO"));
			table.addCell(nullsafeCellH("DAL - AL"));
			table.addCell(nullsafeCellH("PERC."));
			for(BasicDynaBean d : (List<BasicDynaBean>)((RowSetDynaClass) dati.get(2)).getRows())
			{
				table.addCell(nullsafeCell(d.get("unimm")));
				table.addCell(nullsafeCell(d.get("ragi_soci")+" "+d.get("cuaa")));
				table.addCell(nullsafeCell(d.get("tipo_titolo")));
				table.addCell(nullsafeCell(d.get("data_iniziof") + " - "+ d.get("data_finef")));
				table.addCell(nullsafeCell(d.get("perc_poss")));
			}
			ritorno.add(table);
		}
		
		return ritorno;
	}
	


}
