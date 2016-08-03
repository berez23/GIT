package it.webred.diogene.visualizzatore.runtime.tag;

import it.webred.permessi.GestionePermessi;
import it.webred.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * @author Dan Petre
 *
 */
public class MenuRenderTag extends BaseTag
{

	private Log									log					= LogFactory.getFactory().getInstance(this.getClass().getName());


	public int doEndTag()
		throws JspException
	{
		JspWriter out = pageContext.getOut();
		Session session = null;
		try
		{
			if (pageContext.getRequest().getParameter(nomeParametroMappaKeyExt) != null && !pageContext.getRequest().getParameter(nomeParametroMappaKeyExt).trim().equals(""))
				return (EVAL_PAGE);
			String user = ((HttpServletRequest) pageContext.getRequest()).getUserPrincipal() == null ? "" : ((HttpServletRequest) pageContext.getRequest()).getUserPrincipal().getName();
			if (StringUtils.isEmpty(user))
				user = "guest";
			StringBuffer htmlMenu = (StringBuffer)pageContext.getSession().getAttribute("htmlMenu");
			if (htmlMenu == null || htmlMenu.length() < 1)
			{
				GestionePermessi.clearCache(user);
				List idClassiPermesse = (List)pageContext.getSession().getAttribute("idClassiPermesse");
				if(idClassiPermesse == null)
					idClassiPermesse = new ArrayList<String>();
				htmlMenu = new StringBuffer();
				Configuration c = new Configuration().configure("hibernate.cfg.xml");
				session = c.buildSessionFactory().openSession();
				SQLQuery qryProgetto = session.createSQLQuery("SELECT  p.ID, p.NAME, (select count(id) from dv_classe where dv_classe.fk_progetto = p.id) figili FROM DV_PROGETTO p ORDER BY p.SORT_ORDER");
				List listProgetto = qryProgetto.list();
				htmlMenu.append("<div id=\"mainMenu1\" >\n");
				htmlMenu.append("\t <ul id=\"menuList1\"> \n");
				for (int i = 0; i < listProgetto.size(); i++)
				{
					Object[] rowPrg = (Object[]) listProgetto.get(i);
					htmlMenu.append("\t<li>\n");
					htmlMenu.append("\t		<a class=\"actuator\" >" + rowPrg[1] + "</a>\n");// progetto
					if (new Integer(rowPrg[2].toString()).intValue() > 0)// figli
					{
						htmlMenu.append("\t 	<ul class=\"menu\"> \n");
						elaboraSottoClassi(session, null, (BigDecimal) rowPrg[0], htmlMenu,pageContext,idClassiPermesse);
						htmlMenu.append("\t		</ul> \n");
					}
					htmlMenu.append("\t</li>\n");
				}
				htmlMenu.append("\t <li>	<a class=\"actuator\" onClick=\"javascript:window.open('../viewer/applet/index.jsp','map','width=800,height=600,top=0, left=0,toolbar=no,status=yes,resizable=yes'); \">Consulta Mappa </a> </li>\n");
				htmlMenu.append("\t <li>	<a class=\"actuator\" href=\"../\">Exit</a> </li>\n");
				htmlMenu.append("\t </ul> \n");
				htmlMenu.append("</div>\n");

				htmlMenu.append("\t<form id=\"myMenuForm\" action=\"search.jsp\" method=\"post\"><input type=\"hidden\" name=\"idDvClasse\" id=\"idDvClasse\" value=\"\"></form>\n");
				htmlMenu.append("\t\t <script language=\"javascript1.1\">\n");
				htmlMenu.append("\t\t function goMyMenu(myId){\n");
				htmlMenu.append("\t\t document.getElementById('myMenuForm').idDvClasse.value=myId; \n");
				htmlMenu.append("\t\t document.getElementById('myMenuForm').submit(); \n");
				htmlMenu.append("\t\t }\n");
				htmlMenu.append("\t\t </script>\n");
				pageContext.getSession().setAttribute("idClassiPermesse",idClassiPermesse);
			}
			out.println(htmlMenu);
		}
		catch (Exception e)
		{
			log.error("Menu", e);
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

			if (session != null)
				session.close();
		}
		return (EVAL_PAGE);

	}

	private void elaboraSottoClassi(Session session, BigDecimal idDvClasse, BigDecimal fk_progetto, StringBuffer htmlMenu, PageContext pageContext, List<String> idClassePermesse)
		throws Exception
	{
		String idQry = (idDvClasse == null ? " WHERE c.fk_dv_classe is null " : " WHERE c.fk_dv_classe = " + idDvClasse);
		SQLQuery qryClasse = session.createSQLQuery("SELECT   c.ID, c.NAME, c.fk_dv_classe, c.fk_progetto, f.fk_dv_user_entity, "
				+ " (select count(id) from dv_classe where dv_classe.fk_dv_classe = C.id) figli, "
				+ " dce.name nomeenita "
				+ " FROM dv_classe c, dv_format_classe f,  dc_entity dce, dv_user_entity due  "
				+ idQry + " and c.FK_PROGETTO =  "
				+ fk_progetto
				+ " and c.id = f.fk_dv_classe(+)  AND due.ID = f.fk_dv_user_entity  AND due.fk_dc_entity = dce.id " + " ORDER BY c.sort_order");
		List listClasse = qryClasse.list();
		for (int i = 0; i < listClasse.size(); i++)
		{
			Object[] rowClasse = (Object[]) listClasse.get(i);
			htmlMenu.append("\t\t	 <li> \n");

			if (new Integer(rowClasse[5].toString()).intValue() > 0)// figli
			{
				htmlMenu.append("\t\t 		<a class=\"actuator\">" + rowClasse[1] + "</a>");
				htmlMenu.append("\t\t 		<ul class=\"menu\"> \n");
				elaboraSottoClassi(session, (BigDecimal) rowClasse[0], fk_progetto, htmlMenu,pageContext,idClassePermesse);
				if ((i + 1) == listClasse.size())
					htmlMenu.append("\t\t 		<li style=\"border: 1px solid ; padding: 0pt; position: absolute; visibility: hidden; display: block; width: 2px;\"></li>");
				htmlMenu.append("\t\t 		</ul> \n");
			}
			else
			{
				String href = "";
				if (rowClasse[4] != null)
				{

					/* boolean puo = GestionePermessi.operazioneSuEntita(((HttpServletRequest) pageContext.getRequest()).getUserPrincipal(),
							rowClasse[6].toString(),
							pageContext.getServletContext().getServletContextName(),
							GestionePermessi.READ_ENTITY);
					*/
					boolean puo =true;
					// TODO: MODIFICARE GESTIONE PERMESSI
					if(puo)
					{
						href = "onClick=\"goMyMenu('" + rowClasse[0] + "');\"";
						htmlMenu.append("\t\t 		<a   " + href + ">" + rowClasse[1] + "</a>");
						idClassePermesse.add(rowClasse[0].toString());
					}
					else
					{
						href = "onClick=\"alert('Non si dipone dei diritti necessari per accedere alla funzione');\"";
						htmlMenu.append("\t\t 		<a   " + href + "><s>" + rowClasse[1] + "</s></a>");
					}
				}

			}
			htmlMenu.append("\t	 </li> \n");

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

	public static void svuotaCache()
	{
		//deprecata da quando il menu sta in sessione

	}




}
