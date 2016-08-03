/**
 * @author tux
 * $Id: SqlCatalog.java,v 1.3.36.1.2.3 2011/07/18 14:52:15 g.maccherani Exp $
 */

package it.webred.sqlcatalog;


import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.support.audit.AuditDBWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class SqlCatalog extends HttpServlet
{

	private static final long serialVersionUID = -8147275649930119295L;
	public static final int MAX_ROW = 10000;
	private static final Logger log = Logger.getLogger(SqlCatalog.class.getName());

	public void init()
			throws ServletException
	{

	}

	private Connection getConnection(HttpServletRequest request)
			throws Exception
	{

		Context initContext = new InitialContext();
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		DataSource ds = (DataSource)initContext.lookup(es.getDataSourceIntegrato());
		Connection conn = ds.getConnection();
		return conn;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		request.getSession().removeAttribute("results");
		request.getSession().removeAttribute("resultsSchema");
		String message = null;
		Connection con = null;

		PreparedStatement pstm = null;
		String sqlFormatted = "";
		try
		{
			String elencovalori[] = request.getParameterValues("valore");
			String command = request.getParameter("command");
			if (command != null && !command.trim().equals(""))
			{
				con = getConnection(request);
				pstm = con.prepareStatement("select c.sqlcommand  from catalogosql c where c.idcatalogosql = ?");
				pstm.setInt(1, Integer.parseInt(command.trim()));
				ResultSet rs_qry = pstm.executeQuery();
				if (!rs_qry.next())
					throw new Exception("Errore nel catalogo. Contattare l'assistenza segnalando il malfunzionamento del catalogo " + command.trim());
				String sql = rs_qry.getString("sqlcommand");
				rs_qry.close();
				log.debug("SqlCatalog orig sql: " + sql);
				// rimuovo eventuali commenti sql
				int fromindex = 0;
				while (sql.indexOf("/*",fromindex) > -1)
				{
					if(sql.indexOf("/*",fromindex) != sql.indexOf("/*+",fromindex)) {
						sql = sql.substring(0, sql.indexOf("/*",fromindex)) + sql.substring(sql.indexOf("*/",fromindex) + 2);
					    fromindex = sql.indexOf("*/",fromindex) +2;
					}
					else {
						fromindex = sql.indexOf("*/", sql.indexOf("/*+",fromindex)+3)+2;
					}
				}
				sql += "\n ";
				while (sql.indexOf("--") > -1)
				{
					sql = sql.substring(0, sql.indexOf("--")) + sql.substring(sql.indexOf("\n", sql.indexOf("--")));
				}
				pstm.cancel();
				pstm = con.prepareStatement("select *  from catalogosql_params  where idcatalogosql = ? ORDER BY IDPARAMS");
				pstm.setInt(1, Integer.parseInt(command.trim()));
				ResultSet rs_para = pstm.executeQuery();
				pstm.cancel();
				
				if(sql.contains("[1]") && sql.contains("[/1]")){
					//Cerco e gestisco i tag parametri nel formato [1] [/1], [2] [/2], ecc... 
					sqlFormatted = sql;
					sqlFormatted = sqlFormatted.replaceAll("\\r", " ");
					sqlFormatted = sqlFormatted.replaceAll("\\n", " ");
					if (elencovalori != null){
						for (int i = 0; i < elencovalori.length; i++) {
								
							String expr = "\\["+(i+1)+"\\](.+?)\\[/"+(i+1)+"\\]";
							Pattern pattern = Pattern.compile(expr);
							Matcher matcher = pattern.matcher(sqlFormatted);
							if (matcher.find()) {
								
								//se il parametro non è definito elimino tutta la parte dentro i tag
								if (elencovalori[i].equals("")){
									sqlFormatted = matcher.replaceAll(" ");
								}
								else {
									//se il parametro è definito rimuovo solo i tag
									sqlFormatted = sqlFormatted.replaceAll("\\["+(i+1)+"\\]", " ");
									sqlFormatted = sqlFormatted.replaceAll("\\[/"+(i+1)+"\\]", " ");
								}
							}
						}
					}
				}else{
					// scorporo la group by 
					String group = "";
					if (sql.toLowerCase().lastIndexOf("group by") > -1)
					{
						group = sql.substring(sql.toLowerCase().lastIndexOf("group by"));
						sql = sql.substring(0, sql.toLowerCase().lastIndexOf("group by"));
					}
					// scorporo eventuale order
					String order = "";
					if (sql.toLowerCase().lastIndexOf("order") > -1)
					{
						order = sql.substring(sql.toLowerCase().lastIndexOf("order"));
						sql = sql.substring(0, sql.toLowerCase().lastIndexOf("order"));
					}

					
					if (sql.toLowerCase().lastIndexOf("where") > -1 && elencovalori != null && elencovalori.length > 0)
						sql = sql.substring(0, sql.toLowerCase().lastIndexOf("where") + 5) + " 1=1 and " + sql.substring(sql.toLowerCase().lastIndexOf("where") + 5);

					// Pattern.compile("\\s(and|or)\\s+\\S+?\\s*(=|<|>|<=|>=|<>|\\slike\\s)\\s*\\?");
					Pattern p = Pattern.compile("\\s(and|or).*?(\\sand|\\sor|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

					int finposition = 0;
					if (elencovalori != null)
						for (int i = 0; i < elencovalori.length; i++)
						{
							Matcher m = p.matcher(sql);
							m.find(finposition);
							boolean trovato = true;
							while(m.group(0).indexOf("?") <0) {
								trovato =true;
								if (m.find(m.start(2)-1)) continue; else trovato=false; break;
							}
							if (elencovalori[i].equals(""))
							{

								sql = sql.substring(0, m.start(1)) + " /* " + sql.substring(m.start(1), m.start(2)) + " */ " + sql.substring(m.start(2));

							}
							if (trovato)
							finposition = m.start(2)-1;
						}
					//		while (sql.indexOf("/*") > -1)
					//		{
					//			if(sql.indexOf("/*") != sql.indexOf("/*+"))
					//				sql = sql.substring(0, sql.indexOf("/*")) + sql.substring(sql.indexOf("*/") + 2);
					//		}
					sql += " " + order;
					sql += " " + group;
					sqlFormatted = sql;
					sqlFormatted = sqlFormatted.replaceAll("\\r", " ");
					sqlFormatted = sqlFormatted.replaceAll("\\n", " ");
				}
				log.debug("SqlCatalog new  sql: " + sqlFormatted);
				pstm = con.prepareStatement(sqlFormatted);
				int k = 0;
				if (elencovalori != null)
					for (int i = 0; i < elencovalori.length; i++)
					{
						rs_para.next();
						if (elencovalori[i] != null && !elencovalori[i].equals(""))
						{
							String val = elencovalori[i];
							log.info("SqlCatalog: parametro[" + i + "] " + val + " tipo" + rs_para.getString("tipo"));
							callPreparedStatementSetMethod(pstm, rs_para.getString("tipo"), ++k, val, rs_para.getInt("uselike") < 1 ? false : true);
						}
					}

				//Statement s = con.createStatement();
				//s.executeUpdate("ALTER SESSION SET optimizer_goal= rule");

				ResultSet rs = pstm.executeQuery();

				int cnt = 0;
				try
				{
					ResultSetMetaData metaData = rs.getMetaData();
					ArrayList rows = new ArrayList();
					while (rs.next() && (cnt++ < MAX_ROW))
					{

						LinkedHashMap bean = new LinkedHashMap();
						for (int i = 1; i <= metaData.getColumnCount(); i++)
						{
							bean.put(metaData.getColumnName(i), rs.getObject(metaData.getColumnName(i)));
						}
						rows.add(bean);
					}
					if (rows.size() == MAX_ROW)
						message = "ATTENZIONE: sono visualizzati soli i primi " + MAX_ROW + " record. <br> Se si ha la reale necessità di lavorare su tutti i record contattare il supporto.";

					request.getSession().setAttribute("results", rows);
					if (rows.size() <= 100)
					{
						int columnCount = metaData.getColumnCount();
						String col[] = new String[columnCount];
						for (int i = 1; i <= columnCount; i++)
						{
							col[i - 1] = metaData.getColumnName(i);
						}
						request.getSession().setAttribute("resultsSchema", col);
					}		

					
					/*INIZIO AUDIT*/
					try {
						Object[] arguments = new Object[1];
						arguments[0] = command;
						EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
						CeTUser cetUser = eu.getUtente();
						AuditDBWriter auditWriter = new AuditDBWriter();
						auditWriter.auditMethod(cetUser.getCurrentEnte(),cetUser.getUsername(), cetUser.getSessionId(), this.getClass().getName(), "doPost", arguments, rows, null);
					} catch (Throwable e) {				
						log.debug("ERRORE nella scrittura dell'audit", e);
					}
					/*FINE AUDIT*/
				}
				catch (OutOfMemoryError oom)
				{
					message = "La query ha prodotto troppi record per poter essere visualizzati.";

				}
				rs.close();
				pstm.cancel();

			}

		}
		catch (Exception e)
		{
			log.error("ERRORE nella Query: " + sqlFormatted);
			e.printStackTrace();
			message = ("Error executing the SQL statement: \n " + e.getMessage()).replaceAll("\n", "<br>");
		}
		finally
		{
			if (pstm != null)
			{
				try
				{
					pstm.close();
				}
				catch (SQLException e)
				{

				}
			}
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (SQLException e)
				{

				}
			}
		}
		if (message != null)
			request.setAttribute("message", message);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sqlcatalog/sqlRis.jsp");
		dispatcher.forward(request, response);

	}

	private void callPreparedStatementSetMethod(PreparedStatement st, String tipo, int column, String value, boolean uselike)
			throws IllegalArgumentException, SQLException
	{

		if (tipo.equalsIgnoreCase("stringa"))
		{
			if (value == null || value.equalsIgnoreCase("null2"))
			{
				st.setNull(column, Types.CHAR);
			}
			else
			{
				if (uselike)
					st.setString(column, "%" + value + "%");
				else
					st.setString(column, value);
			}
		}
		else if (tipo.equalsIgnoreCase("intero"))
		{
			try
			{
				if (value == null || value.trim().equalsIgnoreCase("null2"))
				{
					st.setNull(column, Types.NUMERIC);
				}
				else
				{
					st.setInt(column, Integer.parseInt(value.trim()));
				}
			}
			catch (NumberFormatException nfe)
			{
				throw new IllegalArgumentException(value + " non è un intero valido.");
			}

		}

		else if (tipo.equalsIgnoreCase("decimale"))
		{
			try
			{
				if (value == null || value.trim().equalsIgnoreCase("null2"))
				{
					st.setNull(column, Types.NUMERIC);
				}
				else
				{
					st.setBigDecimal(column, new BigDecimal(value.trim()));
				}
			}
			catch (NumberFormatException nfe)
			{
				throw new IllegalArgumentException(value + " non è un deciamle valido.");
			}

		}
		else if (tipo.equals("data"))
		{
			try
			{
				if (value == null || value.trim().equalsIgnoreCase("null2"))
				{
					st.setNull(column, Types.DATE);
				}
				else
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					st.setDate(column, new java.sql.Date(sdf.parse(value.trim()).getTime()));
				}
			}
			catch (Exception nfe)
			{
				throw new IllegalArgumentException(value + " non è una data valida.");
			}

		}
		else
			throw new IllegalArgumentException("Tipo " + tipo + " sconosciuto. Contattare il supporto!");

	}

}
