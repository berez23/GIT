package it.webred.diogene.visualizzatore.runtime.tag;

import javax.servlet.RequestDispatcher;
import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Dan Petre
 * 
 */
public class RedirectBackTag extends BaseTag
{

	private Log	log	= LogFactory.getFactory().getInstance(this.getClass().getName());


	public int doEndTag() throws JspException
	{
		String pagina = "detail.jsp";
		if(pageContext.getRequest().getParameter(nomePersistenza) == null)
			pagina ="search.jsp";
		RequestDispatcher rd = pageContext.getRequest().getRequestDispatcher(pagina);
		try
		{
			pageContext.getRequest().setAttribute("backNavigazione",true);
			rd.forward(pageContext.getRequest(), pageContext.getResponse());
		}
		catch (Exception e1)
		{
			throw new JspException(e1);
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
