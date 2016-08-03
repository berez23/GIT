/**
 * @author tux
 * $Id: DynamicParameter.java,v 1.2.32.1 2010/12/03 13:51:52 filippo Exp $
 */

package it.webred.sqlcatalog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import com.lowagie.text.html.HtmlEncoder;

public class DynamicParameter extends BaseTag
{
	private Logger log = Logger.getLogger("diogene.log");

	private String command;

	private String styleClass;

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public String getStyleClass()
	{
		return styleClass;
	}

	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}

	public int doStartTag(HttpServletRequest request)
			throws JspException
	{
		Connection con = null;
		Statement stmt = null;
		JspWriter out = pageContext.getOut();
		try
		{
			out.println("<input type='hidden' name='command' value='" + command + "'/>");
			// table
			out.println("<table class=\"TXTmainLabel\">");
			// th
			out.println("<thead><tr><th colspan=\"2\">Esecuzione catalogo " + command + "</th></tr></thead><tbody>");
			// tr
			con = getConnection(request);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT NOME,TIPO FROM CATALOGOSQL_PARAMS WHERE IDCATALOGOSQL = " + command + " ORDER BY IDPARAMS");
			String values[] = pageContext.getRequest().getParameterValues("valore");
			int idx = 0;
			while (rs.next())
			{
				String nome = HtmlEncoder.encode(rs.getString("NOME"));
				String val = "";
				if (values != null && values.length >= idx && values[idx] != null)
					val = values[idx++];
				String input = "<input type=\"text\" name=\"valore\" value=\"" + val + "\">";
				out.println("<tr class=\"odd\"><td  nowrap>" + nome + "</td><td>" + input + "</td></tr>");

			}
			rs.close();
			out.println("<tr class=\"odd\"><td colspan=\"2\" >");
			out.println("	<div align=\"center\">");
			out.println("		<input name=\"executeSql\" type=\"submit\" id=\"executeSql\" value=\"Esegui\" onclick=\"wait()\"> &nbsp; ");
			out.println("		<input name=\"indietro\" type=\"button\" id=\"indietro\" value=\"Indietro\" onclick=\"document.location='index.jsp'\">");
			out.println(" </div></td></tr>");
			out.println("</tbody></table>");

			return SKIP_BODY;
		}
		catch (Exception e)
		{
			log.error("Creazione parametri dinamici", e);
			throw new JspException(e);
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
			}
			try
			{
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException e)
			{
			}
		}

	}

	public int doEndTag()
			throws JspException
	{
		return SKIP_PAGE;
	}	


}
