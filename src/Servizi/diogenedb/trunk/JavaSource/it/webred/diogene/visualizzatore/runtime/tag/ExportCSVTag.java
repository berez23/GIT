package it.webred.diogene.visualizzatore.runtime.tag;

import it.webred.diogene.visualizzatore.runtime.ListPage;
import it.webred.diogene.visualizzatore.runtime.PageManager;
import it.webred.permessi.GestionePermessi;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Dan Petre
 * 
 */
public class ExportCSVTag extends ListRenderTag
{

	private Log		log		= LogFactory.getFactory().getInstance(this.getClass().getName());
	private String	style	= "text-align:left;margin:auto";

	public int doEndTag()
		throws JspException
	{

		JspWriter out = pageContext.getOut();
		try
		{
			Boolean canExport =  (Boolean)pageContext.getSession().getAttribute("canExport");
			if(canExport == null)
			{
				canExport = true; // TODO: MODIFICARE GESTIONE PERMESSI , PRIMA CONTROLLARE IL PERMESSO DI ESPORTARE 
					//	GestionePermessi.autorizzato(((HttpServletRequest) pageContext.getRequest()).getUserPrincipal(),
					//	pageContext.getServletContext().getServletContextName(),
					//	"Visualizzatore",
					//	"Export su File",true);
			}
			if(!canExport)
			{
				return (SKIP_PAGE);
			}
			HashMap<String, String> parametriRequest = getMapParameter();			
			ListPage lp = PageManager.getListPage(new Long(parametriRequest.get("idDvClasse")), "admin");// TODO
			log.debug("render lista " + lp.getTitle() + " :" + lp.getIdDvClasse());
			String sql = "select * from (" + lp.getSql() + ") where 1=1 ";			
			sql = creaSql(sql,parametriRequest,lp);
			log.debug("sql export =  " + sql);			
			pageContext.getRequest().setAttribute("sqlToExport",sql);
			pageContext.getRequest().setAttribute("idDvClasseToExport",parametriRequest.get("idDvClasse"));
			pageContext.getRequest().setAttribute("fileName",new Date().getTime()+"exportCSV.csv");
			RequestDispatcher rd = pageContext.getRequest().getRequestDispatcher("exportCSV.csv");
			try
			{
				rd.forward(pageContext.getRequest(), pageContext.getResponse());
			}
			catch (Exception e1)
			{
				throw new JspException(e1);
			}
			return (SKIP_PAGE);
		}
		catch (Exception e)
		{
			log.error("List Filter", e);
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(ba));
			pageContext.getRequest().setAttribute("ErrorMessage", e.getMessage());
			pageContext.getRequest().setAttribute("ErrorTrace", ba.toString());
			RequestDispatcher rd = pageContext.getRequest().getRequestDispatcher("error.jsp");
			try
			{
				rd.forward(pageContext.getRequest(), pageContext.getResponse());
			}
			catch (Exception e1)
			{
				throw new JspException(e);
			}
			return (SKIP_PAGE);

		}
		finally
		{

		}
		
	}

	

	public int doStartTag()
		throws JspException
	{

		return super.doStartTag();
	}

	public void release()
	{

		super.release();
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

}
