package it.webred.fascicolo.act;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.webred.fascicolo.FascicoloActionRequestHandler;
import it.webred.fascicolo.act.Catasto;

public class RicercaVia extends FascicoloActionRequestHandler
{

	public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale, String sedime, String via, String civico, String situazione, String filtroData)
		throws Exception
	{

		LinkedList<Object> ritorno = new LinkedList<Object>();
		Properties prop = gedPropertiesReader("/it/webred/fascicolo/act/ricercaXvia.xml");
		String sql = prop.getProperty("sqlRicercaXvia");
		Connection con = getConnectionDiogene(request);
		try
		{
			ritorno.add(genericFPQuery(con, sql, codNazionale, sedime, "%"+via+"%", civico));
		}
		finally
		{
			con.close();
		}

		return ritorno;
	}

	@Override
	public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception
	{
		throw new Exception("Not implemented. Not Need to implement");

	}

}
