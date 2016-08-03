package it.webred.diogene.visualizzatore.runtime.tag;

import it.webred.diogene.visualizzatore.runtime.CampoRicercaBean;
import it.webred.diogene.visualizzatore.runtime.FilterElement;
import it.webred.diogene.visualizzatore.runtime.FilterPage;
import it.webred.diogene.visualizzatore.runtime.ListElement;
import it.webred.diogene.visualizzatore.runtime.ListPage;
import it.webred.diogene.visualizzatore.runtime.PageManager;
import it.webred.diogene.visualizzatore.runtime.paging.PageContext;
import it.webred.diogene.visualizzatore.runtime.paging.PageDefn;
import it.webred.diogene.visualizzatore.runtime.paging.SimplePagingDao;
import it.webred.permessi.GestionePermessi;
import it.webred.utils.Assertion;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
public class ListRenderTag extends BaseTag
{

	private Log		log		= LogFactory.getFactory().getInstance(this.getClass().getName());
	private String	style	= "text-align:left;margin:auto";

	public int doEndTag()
		throws JspException
	{

		JspWriter out = pageContext.getOut();
		try
		{
			HashMap<String, String> parametriRequest = getMapParameter();
			//crosslink
			if(pageContext.getRequest().getAttribute("forwardNavigazione") != null)
				parametriRequest = (HashMap<String, String>)pageContext.getRequest().getAttribute("forwardNavigazione");
			if(!permesso(parametriRequest.get("idDvClasse")))
				return (EVAL_PAGE);
			ListPage lp = PageManager.getListPage(new Long(parametriRequest.get("idDvClasse")), "admin");// TODO
			log.debug("render lista " + lp.getTitle() + " :" + lp.getIdDvClasse());

			int numberOfItemInPage = 20;
			if(!Assertion.isEmpty(parametriRequest.get("numberOfItemInPage")))
				numberOfItemInPage = Integer.parseInt(parametriRequest.get("numberOfItemInPage"));
			int currentListPage = 1;
			if(!Assertion.isEmpty(parametriRequest.get("currentListPage")))
				currentListPage = Integer.parseInt(parametriRequest.get("currentListPage"));
			int totalRecordNumber = -1;
			if(!Assertion.isEmpty(parametriRequest.get("totalRecordNumber")))
				totalRecordNumber = Integer.parseInt(parametriRequest.get("totalRecordNumber"));			
			
			

			String sql = "select * from (" + lp.getSql() + ") where 1=1 ";
			List<Object> params = new LinkedList<Object>();//non la uso (serviva per fare preparedst ma problemi per la export)
			sql = creaSql(sql,parametriRequest,lp);
			// eseguo ricerca
			PageDefn pd = new PageDefn(numberOfItemInPage);
			PageContext pc = new PageContext();

			pc.setPageNumber(currentListPage);
			pc.setSql(sql);
			pc.setSqlParams(params);
			pc.setDatabase("DWH");
			// uso cache totale record per non rifare la query
			if(totalRecordNumber>0)
				pc.setTotalRecordNumber(totalRecordNumber);
			new SimplePagingDao(pd, pc).executeQuery();
			
			
			//richiesta di riccardini
			//se un solo record vai in dettaglio
			// ma solo se non ci sono alias (se si vuol diire js salto in mapppa)
			if(pc.getTotalRecordNumber() == 1 &&  senzaJavaScript(lp))
			{
				Object rs = pc.getPageData().get(0);
				
				LinkedHashMap<String,String> valoriChivi = new LinkedHashMap<String,String>();
				int idx =0;
				//for (String aliasLink : lp.getListElement().iterator().next().getLinkParameterAlias())
				for (String aliasLink : lp.getIdDcColumnKey())
				{
					String nomeCampo = "chiaveLista[" + (idx) + "]";
					valoriChivi.put(nomeCampo,((HashMap) rs).get(aliasLink).toString());
					idx = idx + 1;					
				}
				valoriChivi.put("saltaLista","si");
				pageContext.getRequest().setAttribute("forzaChiaveLista",valoriChivi);
				RequestDispatcher rd = pageContext.getRequest().getRequestDispatcher("detail.jsp");
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

			
			// scorro i riecord trovati
			boolean almenounrecord = false;
			for (Object rs : pc.getPageData())
			{
				// intestazione tabella
				if (!almenounrecord)
				{

					out.println("<div style=\"" + style + "\">");
					out.println("<form name=\"listaDiogene\"  id=\"listaDiogene\" method=\"post\" action=\"detail.jsp\">");

					// render titolo
					out.println("<table class=\"tabellaLista\" border=0 align=\"center\">");
					out.println("<tr >");
					out.println("<th colspan=\"" + lp.getListElement().size() + "\">" + lp.getTitle() + "</th>");
					out.println("</tr>");

					out.println("<tr >");
					for (ListElement le : lp.getListElement())
					{
						out.println("<td nowrap class=\"headColonna\">" + le.getDescription() + "</td>");
					}
					out.println("</tr>");
				}
				almenounrecord = true;
				out.println("<tr style='cursor: pointer;' onmouseout=\"this.style.backgroundColor='transparent'\" onmouseover=\"this.style.backgroundColor='#CCC'\">");
				for (ListElement le : lp.getListElement())
				{
					// la presenza del alias mi fa capire se si tratta di un
					// campo normale o di un link JS
					if (le.getAlias() != null)
					{
						String paramLinckValues = "";
						for (String aliasLink : lp.getIdDcColumnKey())
						{
							paramLinckValues = paramLinckValues + "'" + ((HashMap) rs).get(aliasLink) + "',";
						}
						if (paramLinckValues.length() > 1)// ultima virgola
							paramLinckValues = paramLinckValues.substring(0, paramLinckValues.length() - 1);
						String valore = ((HashMap) rs).get(le.getAlias()) != null ? ((HashMap) rs).get(le.getAlias()).toString() : "&nbsp;";
						if(((HashMap) rs).get(le.getAlias()) != null && ((HashMap) rs).get(le.getAlias()) instanceof Date )
				 			{
								SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
								valore = dateFormat.format((Date)((HashMap) rs).get(le.getAlias()));
							}
						out.println("<td onclick=\"apriDettaglioDiogene(new Array(" + paramLinckValues + "))\">" + valore + "</td>");
					}
					else
					{
						String paramLinckValues = "";
						for (String aliasLink : le.getLinkParameterAlias())
						{
							paramLinckValues = paramLinckValues + "'" + ((HashMap) rs).get(aliasLink) + "',";
						}
						if (paramLinckValues.length() > 1)// ultima virgola
							paramLinckValues = paramLinckValues.substring(0, paramLinckValues.length() - 1);

						String viewLink = "link";
						if (le.getLinkImgUrl() != null && !le.getLinkImgUrl().trim().equals(""))
							viewLink = "<img src=\"" + le.getLinkImgUrl() + "\"/>";
						else if (le.getLinkText() != null)
							viewLink = le.getLinkText();

						out.println("<td align=\"center\" onclick=\"" + le.getLinkFunction() + "(" + paramLinckValues + ")\">" + viewLink + "</td>");
					}
				}
				out.println("</tr>");
			}
			// se nessun record messaggio errore e torono al filtro
			if (!almenounrecord)
			{
				log.debug("Nessun risultato trovato");
				pageContext.getRequest().setAttribute("errorMessage", "Nessun risultato trovato");
				RequestDispatcher rd = pageContext.getRequest().getRequestDispatcher("search.jsp");
				rd.forward(pageContext.getRequest(), pageContext.getResponse());
				return (SKIP_PAGE);
			}

			out.println("<tr >");
			out.println("	<td class=\"headColonna\" valign='middle' align=\"right\" colspan=\"" + lp.getListElement().size() + "\">");

			out.println("	<table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >");
			out.println("		<tr >");
			out.println("		<td class=\"footerTabella\" valign='middle' align=\"left\" >");
			// se vengo da salto da Mappa non faccio indietro ma chiudo la finestra
			if(!parametriRequest.containsKey(nomePersistenza) && parametriRequest.containsKey(nomeParametroMappaKeyExt))
				out.println("			<input type=\"button\" onClick=\"window.close();\" class=\"pulsante_scuro\" value=\"Chiudi\" > &nbsp; ");
			else
				out.println("			<input type=\"button\" onClick=\"document.getElementById('listaDiogene').action='back.jsp';document.getElementById('listaDiogene').submit();\" class=\"pulsante_scuro\" value=\"Indietro\" > &nbsp; ");
			out.println("		</td>");
			out.println("		<td class=\"footerTabella\" valign='middle' align=\"right\">");
			out.println("		Export: <a href=\"javascript:exportCSV();\"><img src=\"../img/ico_file_excel.png\" width=\"15\" height=\"14\" border=\"0\" /></a> &nbsp;");
			if(pc.getTotalRecordNumber() >= 20)
			{
				out.println("		Elementi per Pag. :<select name=\"numberOfItemInPageDo\" onChange=\"changeNumberOfItemInPageDo(this)\"> ");
				String selected = "";
				selected = numberOfItemInPage == 20 ? " selected=\"selected\" " : "";
				out.println("		<option value=\"20\" " + selected + ">20</option>");			
				if(pc.getTotalRecordNumber() >= 50)
				{
					selected = numberOfItemInPage == 50 ? " selected=\"selected\" " : "";
					out.println("		<option value=\"50\" " + selected + ">50</option>");
				}
				if(pc.getTotalRecordNumber() >= 100)
				{
					selected = numberOfItemInPage == 100 ? " selected=\"selected\" " : "";
					out.println("		<option value=\"100\" " + selected + ">100</option>");
				}
				out.println("		</select>");
			}
			out.println("		pag.&nbsp; "+currentListPage+" di " + pc.getTotalNumberOfPages()+ " ("+pc.getTotalRecordNumber()+" record)"); //demo
			if(currentListPage > 1)
				out.println("		<img src=\"../img/sx_arrow.png\" border=\"0\" width=22 height=22 style=\"cursor: pointer;\"  onclick=\"changePage(-1)\" alt=\"Scorri in dietro\"/>");
			if(currentListPage <  pc.getTotalNumberOfPages())
				out.println("		<img src=\"../img/dx_arrow.png\" border=\"0\" width=22 height=22 style=\"cursor: pointer;\"  onclick=\"changePage(1)\" alt=\"Scorri in dietro\"/>");
			out.println("		</td>");
			out.println("		</tr>");
			out.println("		</table>");
			String nomeCampo = "";
			int idx = 0;
			for (String aliasLink : lp.getIdDcColumnKey())
			{
				nomeCampo = "chiaveLista[" + (idx) + "]";
				out.println("		<input type=\"hidden\" id=\"" + nomeCampo + "\" name=\"" + nomeCampo + "\" value=\"\" /> ");
				idx = idx + 1;
			}
			writeHidden(out, parametriRequest);
			//scrivo campi hidden per gestione paginazione se è la prima volta
			if(Assertion.isEmpty(parametriRequest.get("numberOfItemInPage")))
				out.println("		<input type=\"hidden\" id=\"numberOfItemInPage\" name=\"numberOfItemInPage\" value=\""+numberOfItemInPage+"\" /> ");
			if(Assertion.isEmpty(parametriRequest.get("currentListPage")))
				out.println("		<input type=\"hidden\" id=\"currentListPage\" name=\"currentListPage\" value=\""+pc.getPageNumber()+"\" /> ");
			if(Assertion.isEmpty(parametriRequest.get("totalRecordNumber")))
				out.println("		<input type=\"hidden\" id=\"totalRecordNumber\" name=\"totalRecordNumber\" value=\""+pc.getTotalRecordNumber()+"\" /> ");			
			out.println("	</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</form>");
			out.println("</div>");
			out.println("		<script language=\"javascript1.1\">");
			out.println("		document.title='" + lp.getTitle() + "'");
			out.println("		function apriDettaglioDiogene(para)");
			out.println("		{");
			out.println("			for(i=0; i < para.length;i++)");
			out.println("			{");
			out.println("				document.getElementById(\"chiaveLista[\"+i+\"]\").value=para[i];");
			out.println("			}");
			out.println("			document.getElementById(\"listaDiogene\").action='detail.jsp';");
			out.println("			document.getElementById(\"listaDiogene\").submit();");
			out.println("		}");
			out.println("		function changeNumberOfItemInPageDo(para)");
			out.println("		{");
			out.println("			document.getElementById(\"numberOfItemInPage\").value=para.value;");
			out.println("			document.getElementById(\"listaDiogene\").action='list.jsp';");
			out.println("			document.getElementById(\"listaDiogene\").submit();");
			out.println("		}");
			out.println("		function exportCSV()");
			out.println("		{");
			
			
			Boolean canExport =  (Boolean)pageContext.getSession().getAttribute("canExport");
			if(canExport == null)
			{
				canExport = true; // 
						/* TODO : PRIMA CONTROLAVA IL PERMESSO 
						  GestionePermessi.autorizzato(((HttpServletRequest) pageContext.getRequest()).getUserPrincipal(),
						pageContext.getServletContext().getServletContextName(),
						"Visualizzatore",
						"Export su File",true);
						 */

			}
			if(canExport)
			{
				out.println("			document.getElementById(\"listaDiogene\").action='exportCSV.jsp';");
				out.println("			document.getElementById(\"listaDiogene\").submit();");
			}
			else
			{
				out.println("			alert('Non si dispone delle autorizzazioni necessarie per fare la export');");
			}
			out.println("		}");			
			out.println("		function changePage(para)");
			out.println("		{");
			out.println("			document.getElementById(\"currentListPage\").value=Number(document.getElementById(\"currentListPage\").value) + Number(para)");
			out.println("			document.getElementById(\"listaDiogene\").action='list.jsp';");
			out.println("			document.getElementById(\"listaDiogene\").submit();");
			out.println("		}");			
			out.println("		</script>");
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
		return (EVAL_PAGE);
	}


	private boolean senzaJavaScript(ListPage lp)
	{
		for (ListElement le : lp.getListElement())
		{
			// la presenza del alias mi fa capire se si tratta di un
			// campo normale o di un link JS
			if (le.getAlias() == null)
				return false;
		}
		return true;
	}


	protected String creaSql(String sql, HashMap<String, String> parametriRequest, ListPage lp)	
	throws Exception
	{
		FilterPage fp = PageManager.getFilterPage(new Long(parametriRequest.get("idDvClasse")), "admin");// TODO
		// aggiorno sql con parametri del filtro o chiavi crossLink
		if(parametriRequest.containsKey(nomePersistenza))
		{
			sql = sql +" and ( \n (";

			for (Map.Entry<String, String> entry : parametriRequest.entrySet())
			{
				if(entry.getKey().equals("crossLinkParams"))
				{
					
					String[] values = entry.getValue().split(",");
					for(int i=0;i<values.length;i++)
					{
						if(i>0)
							sql = sql +"  or (";
						String[] aliasKey = lp.getIdDcColumnKey();//alias key
						String[] aliasValue  = values[i].substring(1,values[i].length()-1).split("#");//chiave multipla
						if(aliasKey.length != aliasValue.length)
							throw new Exception("Impossibile creare il crossLink. Numero chiavi valore errato");
						for(int k=0;k<aliasKey.length;k++)
						{
							sql = sql +aliasKey[k]+" = '"+aliasValue[k]+"' and ";
							//p.add(new CampoRicercaBean(aliasKey[k], aliasValue[k], "=", "it.webred.diogene.visualizzatore.runtime.tag.CrossLinkType"));
							//params.add(aliasValue[k]);
						}
						if (sql.endsWith("and ") )// ultimo and
							sql = sql.substring(0, sql.length() - 4);
						sql = sql +" )";
					}
					
				}
			}
			sql = sql +" \n )";
		}
		else if(parametriRequest.containsKey(nomeParametroMappaKeyExt))
		{
			sql = sql +" and "+lp.getAliasExtKey()+" = '"+parametriRequest.get(nomeParametroMappaKeyExt).trim()+"'";
		}
		else
		{
			LinkedHashSet<CampoRicercaBean> parametriValorizzati = getMapParametriRicercaValorizzati(parametriRequest, fp);
			int idx = 0;
			for (CampoRicercaBean crb : parametriValorizzati)
			{
				idx = idx + 1;
				if (crb.getJavaType() != null && crb.getJavaType().equals("java.lang.String"))
					sql = sql + " and " + crb.getNomeAlias() + " ";
				else
					sql = sql + " and " + crb.getNomeAlias();
				sql = sql + " " + crb.getValoreOperatore() + " ";
				
				
				//sql = sql + "?";
				
				if (crb.isStringType())
				{
					String val = crb.getValoreCampo().replaceAll("'","''");
					if (crb.isStringType() && crb.getValoreOperatore().equalsIgnoreCase("like"))
					{
						val = "%" + val + "%";
					}
					val = val.toUpperCase();
					//params.add(val);					
					sql = sql + "'"+val+"'";
					log.debug("param String  [" + idx + "]=" + val);					
				}
				else if (crb.isDateType())
				{
					//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					//params.add(new java.sql.Date(sdf.parse(crb.getValoreCampo().trim()).getTime()));
					String val = " to_date('"+crb.getValoreCampo()+"','dd/mm/yyyy') ";
					sql = sql + val;
					log.debug("param Date  [" + idx + "]=" + val);
				}
				else
				{
					//params.add(crb.getValoreCampo());
					String val = crb.getValoreCampo();
					sql = sql + "'"+val+"'";
					log.debug("param Object  [" + idx + "]=" + val);
				}
			}
		}
		log.debug("sql lista =  " + sql);
		return sql;
	}


	/**
	 * Campi ricerca da Form
	 * @param parametriRequest
	 * @return
	 */
	protected LinkedHashSet<CampoRicercaBean> getMapParametriRicercaValorizzati(HashMap<String, String> parametriRequest, FilterPage fp)
	{
		LinkedHashSet<CampoRicercaBean> p = new LinkedHashSet<CampoRicercaBean>();
		int idx = 0;
		String nomeCampo = null;
		String nomeOperatore = null;
		for (FilterElement fe : fp.getFilterElement())
		{
			nomeCampo = "ricerca[" + idx + "]";
			nomeOperatore = "operatore[" + idx + "]";
			if (parametriRequest.get(nomeCampo) != null && !parametriRequest.get(nomeCampo).equals(""))
			{
				p.add(new CampoRicercaBean(fe.getAlias(), parametriRequest.get(nomeCampo), parametriRequest.get(nomeOperatore), fe.getJavaType()));
			}
			idx = idx + 1;
		}
		return p;
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
