package it.webred.fascicolo.act;

import it.webred.fascicolo.FascicoloActionRequestHandler;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.RowSetDynaClass;

public class Documenti extends FascicoloActionRequestHandler
{

	

	@Override
	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale,String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		foglio = levaZeri(foglio);
		particella = levaZeri(particella);
		sub = levaZeri(sub);
		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/documenti.xml");				
		Connection con = getConnectionDiogene(request);		
		int progressivoSQL = 0;
		try
		{
			while(true)
			{
				progressivoSQL++;
				String sql = prop.getProperty("sqlDoc"+progressivoSQL);
				if(sql == null)
					break;
				if(sub != null && !sub.trim().equals(""))
					sql += prop.getProperty("sqlDoc_sub"+progressivoSQL);
				if(prop.getProperty("sqlDoc_oreder"+progressivoSQL) != null)
					sql += prop.getProperty("sqlDoc_oreder"+progressivoSQL);
				RowSetDynaClass row = genericFPQuery(con, sql,null, foglio, particella,sub);
				ritorno.add(row);
		
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
		return new LinkedList<Object>();
	}
}
