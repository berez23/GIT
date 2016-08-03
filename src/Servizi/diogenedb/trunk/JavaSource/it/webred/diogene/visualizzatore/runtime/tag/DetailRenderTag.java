package it.webred.diogene.visualizzatore.runtime.tag;

import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.visualizzatore.runtime.DetailElement;
import it.webred.diogene.visualizzatore.runtime.DetailPage;
import it.webred.diogene.visualizzatore.runtime.DetailTableElement;
import it.webred.diogene.visualizzatore.runtime.ListPage;
import it.webred.diogene.visualizzatore.runtime.PageManager;
import it.webred.utils.Replace;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Dan Petre
 *
 */
public class DetailRenderTag extends BaseTag
{

	private Log		log		= LogFactory.getFactory().getInstance(this.getClass().getName());
	private String	style	= "text-align:left;margin:auto;";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	//@SuppressWarnings("unchecked")
	public int doEndTag()
		throws JspException
	{

		JspWriter out = pageContext.getOut();
		Connection con = null;
		PreparedStatement pstm = null;
		Statement stmStorico = null;
		try
		{

			HashMap<String, String> parametriRequest = getMapParameter();
			//se ho saltato la lista perche un solo record
			if(pageContext.getRequest().getAttribute("forzaChiaveLista") != null)
			{
				parametriRequest.putAll((LinkedHashMap<String,String>)pageContext.getRequest().getAttribute("forzaChiaveLista"));

			}
			if(!permesso(parametriRequest.get("idDvClasse")))
				return (EVAL_PAGE);
			ListPage lp = PageManager.getListPage(new Long(parametriRequest.get("idDvClasse")), "admin");// TODO

			String sql = "select * from (" + lp.getSql() + ") where 1=1 ";
			for (String nomeAlias : lp.getIdDcColumnKey())
			{
				sql = sql + " and " + nomeAlias + " = ";
				sql = sql + "?";
			}

			log.debug("sql dettaglio =  " + sql);
			con = DataJdbcConnection.getConnectionn("DWH");
			pstm = con.prepareStatement(sql);
			int idx = 0;
			String nomeCampo = "";
			for (String nomeAlias : lp.getIdDcColumnKey())
			{
				nomeCampo = "chiaveLista[" + (idx) + "]";
				log.debug("param dettail  [" + idx + "]=" + parametriRequest.get(nomeCampo));
				idx = idx + 1;
				pstm.setObject(idx, parametriRequest.get(nomeCampo));

			}
			ResultSet rs = pstm.executeQuery();
			if (rs.next())
			{

				String paramLinckValues = "";
				for (String aliasLink : lp.getIdDcColumnKey())
				{
					paramLinckValues = paramLinckValues + rs.getObject(aliasLink) + "#";
				}
				if (paramLinckValues.length() > 1)// ultima #
					paramLinckValues = paramLinckValues.substring(0, paramLinckValues.length() - 1);

				DetailPage dp = PageManager.getDetailPage(new Long(parametriRequest.get("idDvClasse")), "admin", paramLinckValues);// TODO
				log.debug("dettaglio " + dp.getIdDvClasse());

				out.println("<div style=\"" + style + "\">");
				out.println("<form name=\"dettaglioDiogene\"  id=\"dettaglioDiogene\" method=\"post\" action=\"list.jsp\" >");
				// inizio tabella principale
				out.println("<table   width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");

				//titlo
				out.println("  <tr><td width=\"90%\" valign=\"top\">");

				out.println("			<table class=\"tabellaDettaglio\" width=\"90%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
				out.println("    			<tr><td class=\"titoloDettaglio\" valign='top' align=\"center\" >"+dp.getTitle()+"</td></tr>");
				out.println("    		</table>");





				// ciclo tabelle in dp

				for (DetailTableElement dte : dp.getPageElement())
				{
					out.println("		<table class=\"tabellaDettaglio\" width=\"90%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
					if (dte.getTitle() != null && !dte.getTitle().trim().equals(""))
						out.println("			<tr><th colspan=\"3\" class=\"headColonna\">" + dte.getTitle() + "</th></tr>");
					for (LinkedHashSet<DetailElement> listaDem : dte.getPageElement())
					{
						out.println("			<tr>");
						for (DetailElement dem : listaDem)
						{
							if (dem.getHtmlDATA() != null)
							{
								out.println("				<td>" + dem.getHtmlDATA() + "</td>");
								break;
							}
							String nome = "&nbsp;";
							String valore = "&nbsp;";
							if (dem != null)
							{
								nome = dem.getDescription() != null ? dem.getDescription() : "&nbsp;";
								valore = rs.getObject(dem.getAlias()) != null ? rs.getObject(dem.getAlias()).toString() : "&nbsp;";
								if(rs.getObject(dem.getAlias()) != null && rs.getObject(dem.getAlias()) instanceof Date )
					 			{

									valore = dateFormat.format((Date)rs.getObject(dem.getAlias()));
								}
							}
							out.println("				<td width=\"33%\" align=\"left\" valign=\"top\" ><strong>" + nome + "</strong>: " + valore + " </td>");
						}
						out.println("			</tr>");
					}
					out.println("		</table>");
				}

				// fine td tabelle dp

				// navigazione


				out.println("			<table class=\"tabellaDettaglio\" width=\"90%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
				out.println("    		<tr>");
				out.println("				<td class=\"footerTabella\" valign='top' align=\"center\" >");

				//controlo se ho saltato la lista. Se si uso la funzione back
				if(parametriRequest.get("saltaLista") != null && !parametriRequest.get("saltaLista").trim().equals(""))
				{
					// se vengo da salto da Mappa non faccio indietro ma chiudo la finestra
					if(!parametriRequest.containsKey(nomePersistenza) && parametriRequest.containsKey(nomeParametroMappaKeyExt))
						out.println("			<input type=\"button\" onClick=\"window.close();\" class=\"pulsante_scuro\" value=\"Chiudi\" > &nbsp; ");
					else
						out.println("					<input type=\"button\" onClick=\"document.getElementById('dettaglioDiogene').action='back.jsp';document.getElementById('dettaglioDiogene').submit();\" class=\"pulsante_scuro\" value=\"Indietro\" >");
				}
				else
					out.println("					<input type=\"button\" onClick=\"document.getElementById('dettaglioDiogene').action='list.jsp';document.getElementById('dettaglioDiogene').submit();\" class=\"pulsante_scuro\" value=\"Indietro\" >");
				out.println("				</td>");
				out.println("    		</tr>");
				out.println("    		</table>");

				out.println("  </td><td width=\"10%\" valign=\"top\">");

				//storico
				if(lp.getAliasExtKey() != null && lp.getAliasInizioValidita() != null && lp.getAliasFineValidita() != null)
				{
					String aliasExtkey = lp.getAliasExtKey();
					String aliasInizioValidita = lp.getAliasInizioValidita();
					String aliasFineValidita = lp.getAliasFineValidita();
					String sqlStorico = "select ";
					//N chiavi
					for (String nomeAlias : lp.getIdDcColumnKey())
					{
						sqlStorico +=  nomeAlias + " ,";
					}
					sqlStorico+=aliasInizioValidita+" ,";
					sqlStorico+=aliasFineValidita+" ";

					sqlStorico += " from (" + lp.getSql() + ") where "+aliasExtkey+"="+"'"+rs.getObject(lp.getAliasExtKey()).toString()+"'";
					log.debug("Sql Storico: "+sqlStorico);
					stmStorico = con.createStatement();
					ResultSet rsStoricoExtKey = stmStorico.executeQuery(sqlStorico);


					out.println("			<table  valign=\"top\" class=\"tabellaCrossLink\" width=\"90%\" border=\"0\" cellspacing=\"2\" cellpadding=\"1\">");
					out.println("			  <tr><th nowrap>Navigazione Storica</th></tr>");
					out.println("			  <tr>");
					out.println("				<td nowrap>");
					out.println("			    <select onChange=\"eval(this.value)\" name=\"storicoKeyValue\">");

					while(rsStoricoExtKey.next())
					{
						String storicoKeyValue = "";
						String paramLinckValuesControllo = "";
						for (String nomeAlias : lp.getIdDcColumnKey())
						{
							storicoKeyValue+="'"+rsStoricoExtKey.getObject(nomeAlias)+"',";
							paramLinckValuesControllo = paramLinckValuesControllo + rsStoricoExtKey.getObject(nomeAlias) + "#";
						}
						storicoKeyValue=storicoKeyValue.substring(0,storicoKeyValue.length()-1);//ultima virgola
						paramLinckValuesControllo = paramLinckValuesControllo.substring(0, paramLinckValuesControllo.length() - 1);//ultima #
						String valore = " ";
						String selected = "  ";
						if(paramLinckValuesControllo.equals(paramLinckValues))
							selected = " selected ";
						if(rsStoricoExtKey.getObject(lp.getAliasInizioValidita()) != null)
							valore = dateFormat.format((Date)rsStoricoExtKey.getObject(lp.getAliasInizioValidita()));
						if(rsStoricoExtKey.getObject(lp.getAliasFineValidita()) != null)
							valore += " - "+dateFormat.format((Date)rsStoricoExtKey.getObject(lp.getAliasFineValidita()));
						out.println("			    	<option "+selected+" value=\"apriStoricoDettaglioDiogene(new Array(" + storicoKeyValue + "))\" >"+valore+"</option>");
					}
					out.println("			    </select> ");

					out.println("			    </td>");
					out.println("			  </tr>");
					rsStoricoExtKey.close();

				}
				//  crosslink
				if(dp.getPageLink() != null && dp.getPageLink().getKeys().size() >0)
				{
					out.println("			<table  valign=\"top\" class=\"tabellaCrossLink\" width=\"90%\" border=\"0\" cellspacing=\"2\" cellpadding=\"1\">");
					out.println("			  <tr><th nowrap>Link Correlati</th></tr>");
					for (Map.Entry<Long, LinkedHashMap<String, LinkedHashSet<String>>> lnk : dp.getPageLink().getKeys().entrySet())
					{
						Long idDvClasseLink = lnk.getKey();

						LinkedHashMap<String, LinkedHashSet<String>> lnkAttribute = lnk.getValue();
						int idxLink = 0;

							for (Map.Entry<String, LinkedHashSet<String>> sottoLnk : lnkAttribute.entrySet())
							{
								String nome = sottoLnk.getKey();
								int numeroRecord = sottoLnk.getValue().size();
								out.println("			  <tr>");
								out.println("				<td nowrap>");

								if(permesso(idDvClasseLink.toString()))
								{
									if (numeroRecord > 0)
										out.println("					<a href=\"javascript:goMyCrossLink('" + idDvClasseLink + "@"+idxLink+"')\">");
									out.println("						" + nome + " ( " + numeroRecord + " )");
									if (numeroRecord > 0)
										out.println("					</a>");
								}
								else
								{
									out.println("					<s>"+nome+"</s>");
								}
								out.println("			    </td>");
								out.println("			  </tr>");
								idxLink++;
							}


					}
					out.println("			</table>");
				}


				out.println("  </td></tr>");
				// fine

				out.println("</table>");

				writeHidden(out, parametriRequest);
				out.println("<input type=\"hidden\" name=\"paramLinckValues\" id=\"paramLinckValues\" value=\""+Replace.forHTMLTag(paramLinckValues)+"\" />");
				out.println("<input type=\"hidden\" name=\"idDvClasseLink\" id=\"idDvClasseLink\" value=\"\" />");
				out.println("</form>");
				out.println("</div>");

				out.println("<script language=\"javascript1.1\">");
				out.println("document.title='" + dp.getTitle() + "'");
				out.println("function goMyCrossLink(myid){");
				out.println("	document.getElementById('dettaglioDiogene').idDvClasseLink.value=myid;");
				out.println("	document.getElementById('dettaglioDiogene').action='crossLink.jsp';");
				out.println("	document.getElementById('dettaglioDiogene').submit();");
				out.println("}");
				out.println("		function apriStoricoDettaglioDiogene(para)");
				out.println("		{");
				out.println("			for(i=0; i < para.length;i++)");
				out.println("			{");
				out.println("				document.getElementById(\"chiaveLista[\"+i+\"]\").value=para[i];");
				out.println("			}");
				out.println("			document.getElementById(\"dettaglioDiogene\").action='detail.jsp';");
				out.println("			document.getElementById(\"dettaglioDiogene\").submit();");
				out.println("		}");
				out.println("</script>");

			}
			else
			{
				out.println("Impossibile accedere al dettaglio.");
			}

		}
		catch (Exception e)
		{
			log.error("Detail Filter", e);
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

			try
			{
				if (stmStorico != null)
					stmStorico.close();
				if (pstm != null)
					pstm.close();
				if (con != null && !con.isClosed())
					con.close();
			}
			catch (Exception e)
			{
			}

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
