package it.webred.diogene.visualizzatore.runtime.tag;

import it.webred.diogene.visualizzatore.runtime.DetailPage;
import it.webred.diogene.visualizzatore.runtime.PageManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Dan Petre
 *
 */
public class RedirectForwardTag extends BaseTag
{

	private Log	log	= LogFactory.getFactory().getInstance(this.getClass().getName());

	public int doEndTag() throws JspException
	{
		ServletRequest request = pageContext.getRequest();
		JspWriter out = pageContext.getOut();
		try
		{
			HashMap<String,String> newParameter = new HashMap<String,String>();
			String dvClasseLinkCompleto = request.getParameter("idDvClasseLink");
			Long idDvClasseLink =new Long(dvClasseLinkCompleto.substring(0,dvClasseLinkCompleto.indexOf("@")));
			Long idDvClasse =new Long(request.getParameter("idDvClasse"));
			DetailPage dp = PageManager.getDetailPage(idDvClasse, "admin", request.getParameter("paramLinckValues") );// TODO
			log.debug("crossLink " + dp.getIdDvClasse() +" params=" +request.getParameter("paramLinckValues"));
			//newParameter.put("paramLinckValues",request.getParameter("paramLinckValues"));
			newParameter.put("idDvClasse",idDvClasseLink.toString());
			int idxParams =Integer.parseInt(dvClasseLinkCompleto.substring(dvClasseLinkCompleto.indexOf("@")+1));



			for (Map.Entry <Long, LinkedHashMap<String, LinkedHashSet<String>>> lnk : dp.getPageLink().getKeys().entrySet())
			{
				//prendo id selezionato
				if(idDvClasseLink.equals(lnk.getKey())  )
				 {
						LinkedHashMap<String,LinkedHashSet<String>> lnkAttribute = lnk.getValue();
						int idx =0;
						for(Map.Entry<String,LinkedHashSet<String>> sottoLnk : lnkAttribute.entrySet())
						{
							if(idx == idxParams)
							{
								String nome = sottoLnk.getKey();
								String keyBuffer = "";
								for(String keys : sottoLnk.getValue())
								{
									keyBuffer += "'"+keys+"',";
								}
								if (keyBuffer.length() > 1)// ultima virgola
									keyBuffer = keyBuffer.substring(0, keyBuffer.length() - 1);
								
								newParameter.put("crossLinkParams",keyBuffer);
							}
							idx++;
						}
				}

			}
			pageContext.getRequest().setAttribute("forwardNavigazione",newParameter);
			pageContext.getRequest().setAttribute("forwardNavigazione",getMapParameter());;
			RequestDispatcher rd = pageContext.getRequest().getRequestDispatcher("list.jsp");
			try
			{

				rd.forward(pageContext.getRequest(), pageContext.getResponse());
			}
			catch (Exception e1)
			{
				throw new JspException(e1);
			}
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
		}
		finally
		{

		}
		return (SKIP_PAGE);

	}

	public int doStartTag() throws JspException
	{

		return super.doStartTag();
	}

	public void release()
	{

		super.release();
	}

}
