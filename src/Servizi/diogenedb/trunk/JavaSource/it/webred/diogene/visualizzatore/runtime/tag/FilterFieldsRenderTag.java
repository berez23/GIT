package it.webred.diogene.visualizzatore.runtime.tag;

import it.webred.diogene.visualizzatore.runtime.FilterElement;
import it.webred.diogene.visualizzatore.runtime.FilterPage;
import it.webred.diogene.visualizzatore.runtime.PageManager;
import it.webred.utils.Replace;
import it.webred.web.validator.DateValidation;
import it.webred.web.validator.NumberValidation;
import it.webred.web.validator.ValidationType;
import it.webred.web.validator.ValidatorsHolder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

/**
 * @author Dan Petre
 * 
 */
public class FilterFieldsRenderTag extends BaseTag
{

	private Log		log		= LogFactory.getFactory().getInstance(this.getClass().getName());
	private String	style	= "text-align:left;margin:auto";

	public int doEndTag()
		throws JspException
	{

		JspWriter out = pageContext.getOut();
		ServletRequest request = pageContext.getRequest();
		try
		{
			String idDvClasseRequest = request.getParameter("idDvClasse");
			try
			{
				if (idDvClasseRequest == null || !GenericValidator.isLong(idDvClasseRequest))
					return (EVAL_PAGE);
				if(!permesso(idDvClasseRequest))
					return (EVAL_PAGE);
			}
			catch (Exception e)
			{
				return (EVAL_PAGE);
			}

			FilterPage fp = PageManager.getFilterPage(new Long(idDvClasseRequest), "admin");// TODO
			log.debug("render ricerca " + fp.getIdDvClasse());

			out.println("<div style=\"" + style + "\">");

			out.println("<form name=\"filtroDiogene\"  id=\"filtroDiogene\" method=\"post\" action=\"list.jsp\" onSubmit=\"return validateFiltroDiogene(this);\">");
			out.println("<table class=\"tabellaRicerca\" align=\"center\" cellpading=0 >");
			out.println("<tr><th colspan='3'>" + fp.getTitle() + "</th></tr>");
			int idx = 0;
			String nomeCampo = "";
			ValidatorsHolder validator = new ValidatorsHolder();
			for (FilterElement fe : fp.getFilterElement())
			{

				String nomeLabel = fe.getDescription();
				nomeCampo = "ricerca[" + (idx) + "]";
				String nomeOperatore = "operatore[" + idx + "]";
				String valoreCampo = request.getParameter(nomeCampo) != null ? Replace.forHTMLTag(request.getParameter(nomeCampo)) : "";
				String valoreOperatore = request.getParameter(nomeOperatore) != null ? request.getParameter(nomeOperatore) : "";
				out.println("<tr>");
				out.println("	<td nowrap>");
				out.println("		<label >" + nomeLabel + "</label>");
				out.println("	</td>");

				out.println("	<td>");
				out.println("		<select name=\"" + nomeOperatore + "\"  >");
				String selected = "";
				for (Map.Entry<String, String> item : fe.getOperators().entrySet())
				{
					String chiave = item.getKey();
					Object valore = item.getValue();
					selected = valoreOperatore.equals(chiave) ? " selected=\"selected\" " : "";
					out.println("\t\t<option value=\"" + chiave + "\" " + selected + ">" + valore + "</option>");
				}
				out.println("		</select> ");
				out.println("	</td>");

				out.println("	<td>");
				if (fe.getListValues() == null)
				{
					out.println("<input type=\"text\" name=\"" + nomeCampo + "\" value=\"" + valoreCampo + "\" /><br />");
				}
				else
				{

					out.println("<select name=\"" + nomeCampo + "\"  > ");
					for (Map.Entry<String, String> item : fe.getListValues().entrySet())
					{
						String chiave = item.getKey();
						Object valore = item.getValue();
						selected = valoreCampo.equals(chiave) ? " selected=\"selected\" " : "";
						out.println("\t<option value=\"" + chiave + "\" " + selected + ">" + valore + "</option>");
					}
					out.println("</select>");
				}
				out.println("	</td>");
				out.println("<tr>");
				if (fe.getJavaType() != null)
				{
					if (fe.getJavaType().endsWith("Date"))
						validator.addValidation(new DateValidation("filtroDiogene", nomeCampo, fe.getDescription()));
					else if (fe.getJavaType().equals("java.math.BigDecimal"))
						validator.addValidation(new NumberValidation("filtroDiogene", nomeCampo, fe.getDescription(), ValidationType.FLOAT));
				}
				idx = idx + 1;
			}
			out.println("<tr>");
			out.println("	<td>&nbsp;</td><td colspan='2'>");
			out.println("\t\t <input type=\"hidden\" name=\"idDvClasse\" value=\"" + fp.getIdDvClasse() + "\" />");
			out.println("\t\t <input type=\"submit\" class=\"pulsante\" value=\"Ricerca\" >");
			out.println("\t\t <input type=\"reset\" class=\"pulsante\" value=\"Reset\" >");

			out.println("\t\t <script language=\"javascript1.1\">");
			out.println("\t\t document.title='" + fp.getTitle() + "'");
			
			if (pageContext.getRequest().getAttribute("errorMessage") != null)
				out.println("\t\t\talert ('" + pageContext.getRequest().getAttribute("errorMessage") + "')");
			out.println("\t\t\t</script>");
			String jsvalidazione = validator.getValidateScript();
			if(jsvalidazione != null && jsvalidazione.length() > 0)
			{
				out.println(jsvalidazione);
			}
			else
			{
				out.println("<script>\nfunction validateFiltroDiogene(formobj)\n{\nreturn true;\n}\n</script>\n");
			}
			out.println("	</td>");
			out.println("</tr>");
			out.println("</table>");

			out.println("</form>");
			out.println("</div>");
			// out.println("<div id=\"diogeneAttendiDiv\" style=\"position:
			// absolute; top:0; left:0; width:100%; height: 100%;
			// background-color: transparent; background-image:
			// url('img/loading.gif') ;background-repeat: no-repeat;
			// background-position:center; cursor: wait; z-index: 1001;
			// visibility: hidden \"/>");
		}
		catch (Exception e)
		{
			log.error("Tag Filter", e);
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
		return (EVAL_PAGE);
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
