package it.webred.AMProfiler.servlet;

import it.webred.AMProfiler.beans.AmApplication;
import it.webred.AMProfiler.beans.CodificaPermessi;
import it.webred.permessi.GestionePermessi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: CaricaMenu
 * 
 */
public class CaricaMenu extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public CaricaMenu() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Principal user = request.getUserPrincipal();

			boolean gestPerm = GestionePermessi.autorizzato(user,
					CodificaPermessi.NOME_APP, CodificaPermessi.ITEM_MAPPING,
					CodificaPermessi.GESTIONE, true);
			boolean gestServizi = gestPerm;

			Connection con = null;
			Statement statment = null;
			try {
				
				ArrayList listaApplUtente = GestionePermessi.getApplications(
						user, true);

				con = BaseAction.apriConnessione();
				statment = con.createStatement();
		  		
		  		ResultSet rsc = statment
						.executeQuery("select distinct belfiore, descrizione from am_comune");
				Hashtable<String, String> decodeComuni = new Hashtable<String, String>();
				while (rsc.next()) {
					decodeComuni.put(rsc.getString("belfiore"), rsc
							.getString("descrizione"));
				}
				rsc.close();

				ResultSet rs = statment
						.executeQuery("select distinct APP_TYPE from am_application where APP_TYPE !='AMProfiler' order by APP_TYPE");
				ArrayList<String> intestazioneTab = new ArrayList<String>();
				intestazioneTab.add("Ente");
				while (rs.next()) {
					intestazioneTab.add(rs.getString("APP_TYPE"));
				}
				rs.close();
				// controllo quante applicazioni ho trovato
				if (intestazioneTab.size() != 1) {
					request.setAttribute("intestazioneTab", intestazioneTab);

					LinkedHashMap<String, LinkedHashMap> mappa = new LinkedHashMap();
					String sql = "SELECT a.*, c.FK_AM_COMUNE, i.URL "
							+ "FROM am_application a, am_instance i, am_instance_comune c "
							+ "WHERE i.fk_am_application (+)LIKE a.NAME AND c.FK_AM_INSTANCE (+)LIKE i.NAME "
							+ "AND a.NAME != 'AMProfiler' order by c.FK_AM_COMUNE, a.APP_CATEGORY, a.NAME";
					rs = statment.executeQuery(sql);
					HashMap<String, Boolean> permessoDiAccessoAdComune = new HashMap<String, Boolean>();

					while (rs.next()) {
						String comune = rs.getString("FK_AM_COMUNE");
						if (mappa.get(comune) == null) {
							permessoDiAccessoAdComune.put(comune, false);
							LinkedHashMap mappaEnte = new LinkedHashMap();
							for (Object nomeApp : intestazioneTab) {

								/*
								 * if(((String)nomeApp).toLowerCase().endsWith("ente"
								 * )) { AmApplication a=new AmApplication();
								 * a.setName(ente); a.setUrl(ente);
								 * a.setEnte(ente); a.setTipo_app(ente);
								 * ArrayList appo=new ArrayList(); appo.add(a);
								 * mappaEnte
								 * .put(((String)nomeApp).toLowerCase(),appo);
								 * 
								 * } else
								 */
								mappaEnte.put(((String) nomeApp).toLowerCase(),
										new ArrayList());
							}
							mappa.put(comune, mappaEnte);
						}

						AmApplication ap = new AmApplication();
						ap.setName(rs.getString("NAME"));
						ap.setUrl(rs.getString("URL"));
						ap.getComune().setBelfiore(comune);
						ap.setTipo_app(rs.getString("APP_TYPE").toLowerCase());
						ap.setCat_app(rs.getString("APP_CATEGORY"));
						// controllo se l'utente può accedere all'applicazione
						if (listaApplUtente.contains(ap.getName())) {
							permessoDiAccessoAdComune.put(comune, true);
							ap.setAccessoAutorizzato(true);
						}

						((List) mappa.get(comune).get(ap.getTipo_app()))
								.add(ap);

					}

					if (gestPerm) {
						request.setAttribute("gestPerm", "true");
					} else {
						// tolgo dalla mappa gli enti per i quali l'utente non
						// ha nessun diritto di accesso
						for (Iterator it = permessoDiAccessoAdComune.keySet()
								.iterator(); it.hasNext();) {
							String name = (String) it.next();
							if (!(Boolean) permessoDiAccessoAdComune.get(name)) {
								mappa.remove(name);
								// System.out.println("rimossa "+ name );
							}
						}
					}

					request.setAttribute("listaApplicazioni", mappa);
					request.setAttribute("myMenu", getMyMenu(mappa,
							decodeComuni, gestPerm, gestServizi));
				}

				request.getRequestDispatcher("/jsp/index.jsp").forward(request,
						response);
			} catch (Throwable e) {
				BaseAction.toErrorPage(request, response, e);
			} finally {
				BaseAction.chiudiConnessione(con, statment);
			}
		} catch (Exception e) {
			BaseAction.toErrorPage(request, response, e);
		}

	}

	private String getMyMenu(LinkedHashMap<String, LinkedHashMap> mappa,
			Hashtable<String, String> decodeComuni, boolean gestPermessi,
			boolean gestServizi) {
		StringBuffer out = new StringBuffer();

		out.append("<div class=\"TabView\" id=\"TabView1\" >\n");
		out.append("<div class=\"Tabs\" id=\"Tabs\"> ");
		for (String key : mappa.keySet()) {
			if (key != null)
				out.append("<a> " + /*decodeComuni.get(key) +*/ " </a>");
		}
		out.append("<select NAME=comuni onchange=\"javascript:TabView.switchTab(0, this.selectedIndex);\">");
		for (String key : mappa.keySet()) {
			if (key != null)
				out.append("<option >" + decodeComuni.get(key) + "</option>");
		}
		out.append("</select>");
		out.append("</div><div class=\"Pages\">\n");

		for (String key : mappa.keySet()) {

			StringBuffer appAcq = new StringBuffer();
			StringBuffer appVis = new StringBuffer();
			StringBuffer appVert = new StringBuffer();
			StringBuffer appAltri = new StringBuffer();
			LinkedHashMap app = mappa.get(key);

			for (Object k : app.keySet()) {
				ArrayList<AmApplication> l = (ArrayList<AmApplication>) app
						.get(k);
				for (AmApplication a : l) {
					if (a.getCat_app().equals("1")) {
						String href = null;

						if (a.isAccessoAutorizzato()) {
							if (a.getUrl() == null
									|| a.getUrl().trim().equals("")) {
								href = "<a href=\"javascript:void(0);\" onclick=\"alert('URL non impostata! Contattare il supporto tecnico');\">";
							} else {
								href = "<a href=\"" + a.getUrl()
										+ "\" target=\"_blank\" >";
							}
						}
						appAcq
								.append("			<div style=\"width:150px;text-align:center; float:left; margin-left:10px\">\n");
						appAcq
								.append("				<div style=\"width:150px;text-align:center;clear:left; \">\n");
						if (href != null) {
							appAcq.append(href);
						}
						appAcq.append("				<img border=\"0\" src=\"jsp/img/"
								+ a.getTipo_app().toLowerCase()
								+ ".gif\" width=\"150\" height=\"150\" />\n");
						if (href != null) {
							appAcq.append("</a>");
						}
						appAcq.append("				</div>\n");
						appAcq
								.append("				<div style=\"width:150px;text-align:center;clear:left;\">");
						if (href != null) {
							appAcq.append(href);
						}
						appAcq.append(a.getName());
						if (href != null) {
							appAcq.append("</a>");
						}

						if (gestPermessi) {
							appAcq
									.append("<a href=\"CaricaPermessi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/keys.gif\" ></img></a>");
						}

						if (gestServizi) {
							appAcq
									.append("<a href=\"CaricaServizi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/services.png\" ></img></a>");
						}

						appAcq.append("</div>\n");
						appAcq.append("			</div>\n");
					} else if (a.getCat_app().equals("2")) {
						String href = null;

						if (a.isAccessoAutorizzato()) {
							if (a.getUrl() == null
									|| a.getUrl().trim().equals("")) {
								href = "<a href=\"javascript:void(0);\" onclick=\"alert('URL non impostata! Contattare il supporto tecnico');\">";
							} else {
								href = "<a href=\"" + a.getUrl()
										+ "\"  target=\"_blank\" >";
							}
						}
						appVis
								.append("			<div style=\"width:150px;text-align:center; float:left; margin-left:10px\">\n");
						appVis
								.append("				<div style=\"width:150px;text-align:center;clear:left; \">\n");
						if (href != null) {
							appVis.append(href);
						}
						appVis.append("				<img border=\"0\" src=\"jsp/img/"
								+ a.getTipo_app().toLowerCase()
								+ ".gif\" width=\"150\" height=\"150\" />\n");
						if (href != null) {
							appVis.append("</a>");
						}
						appVis.append("				</div>\n");
						appVis
								.append("				<div style=\"width:150px;text-align:center;clear:left;\">");
						if (href != null) {
							appVis.append(href);
						}
						appVis.append(a.getName());
						if (href != null) {
							appVis.append("</a>");
						}

						if (gestPermessi) {
							appVis
									.append("<a href=\"CaricaPermessi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/keys.gif\" ></img></a>");
						}

						if (gestServizi) {
							appVis
									.append("<a href=\"CaricaServizi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/services.png\" ></img></a>");
						}

						appVis.append("</div>\n");
						appVis.append("			</div>\n");
					} else if (a.getCat_app().equals("3")) {
						String href = null;

						if (a.isAccessoAutorizzato()) {
							if (a.getUrl() == null
									|| a.getUrl().trim().equals("")) {
								href = "<a href=\"javascript:void(0);\" onclick=\"alert('URL non impostata! Contattare il supporto tecnico');\">";
							} else {
								href = "<a href=\"" + a.getUrl()
										+ "\"  target=\"_blank\" >";
							}
						}
						appVert
								.append("			<div style=\"width:150px;text-align:center; float:left; margin-left:10px\">\n");
						appVert
								.append("				<div style=\"width:150px;text-align:center;clear:left; \">\n");
						if (href != null) {
							appVert.append(href);
						}
						appVert.append("				<img border=\"0\" src=\"jsp/img/"
								+ a.getTipo_app().toLowerCase()
								+ ".gif\" width=\"150\" height=\"150\" />\n");
						if (href != null) {
							appVert.append("</a>");
						}
						appVert.append("				</div>\n");
						appVert
								.append("				<div style=\"width:150px;text-align:center;clear:left;\">");
						if (href != null) {
							appVert.append(href);
						}
						appVert.append(a.getName());
						if (href != null) {
							appVert.append("</a>");
						}

						if (gestPermessi) {
							appVert
									.append("<a href=\"CaricaPermessi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/keys.gif\" ></img></a>");
						}

						if (gestServizi) {
							appVert
									.append("<a href=\"CaricaServizi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/services.png\" ></img></a>");
						}

						appVert.append("</div>\n");
						appVert.append("			</div>\n");
					} else {
						appAltri.append("&nbsp;");
						String href = null;
						if (a.isAccessoAutorizzato()) {
							if (a.getUrl() == null
									|| a.getUrl().trim().equals("")) {
								href = "&nbsp;<a href=\"javascript:void(0);\" onclick=\"alert('URL non impostata! Contattare il supporto tecnico');\">";
							} else {
								href = "<a href=\"" + a.getUrl()
										+ "\"  target=\"_blank\" >";
							}
						}
						if (href != null) {
							appAltri.append(href);
						}
						appAltri.append(a.getName());
						if (href != null) {
							appAltri.append("</a>");
						}
						if (gestPermessi) {
							appAltri
									.append("<a href=\"CaricaPermessi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/keys.gif\" ></img></a>");
						}

						if (gestServizi) {
							appAltri
									.append("<a href=\"CaricaServizi?application="
											+ a.getName()
											+ "&appType="
											+ a.getTipo_app()
											+ "\"><img style=\"border:none;\" src=\"img/services.png\" ></img></a>");
						}

						appAltri.append("&nbsp;");
					}

				}

			}

			out
					.append("<div class=\"Page\"><table width=\"100%\" border=\"1\" align=\"center\" class=\"griglia\" > \n");

			String descrizioneSezione = null;
			String descAltriApp = null;
			if (key == null) {
				descrizioneSezione = "&nbsp;";
				descAltriApp = "UTILITY";
			} else {
				descrizioneSezione = "ENTE:" + key;
				descAltriApp = "Altri Applicativi";
			}
			out
					.append("<tr><th colspan=\"2\" nowrap=\"nowrap\" class=\"divTableTitle\" id=\"codiceEnte\">"
							+ descrizioneSezione + " </th></tr>\n");

			if (appAcq != null && appAcq.toString().length() > 1) {
				out.append("  <tr >\n");
				out
						.append("    <td width=\"1%\" nowrap=\"nowrap\">Processi di acquisizione<br /> e trattamento dati </td>\n");
				out.append("    <td align=\"left\" >\n");
				out.append(appAcq);
				out.append("	 </td>\n");
				out.append("  </tr>\n");
			}
			if (appVis != null && appVis.toString().length() > 1) {
				out.append("  <tr >\n");
				out
						.append("    <td width=\"1%\" nowrap=\"nowrap\">Visualizzazione<br /> fonti dati  </td>\n");
				out.append("    <td align=\"left\" >\n");
				out.append(appVis);
				out.append("	 </td>\n");
				out.append("  </tr>\n");
			}
			if (appVert != null && appVert.toString().length() > 1) {
				out.append("  <tr >\n");
				out
						.append("    <td width=\"1%\" nowrap=\"nowrap\">Applicativi verticali </td>\n");
				out.append("    <td align=\"left\" >\n");
				out.append(appVert);
				out.append("	 </td>\n");
				out.append("  </tr>\n");
			}
			if (appAltri != null && appAltri.toString().length() > 1) {
				out.append("  <tr >\n");
				out.append("    <td width=\"1%\" nowrap=\"nowrap\">"
						+ descAltriApp + "</td>\n");
				out.append("    <td align=\"left\" >\n");
				out.append(appAltri);
				out.append("	 </td>\n");
				out.append("  </tr>\n");
			}
			out.append("</table></div>");
		}
		out.append("</div></div>");
		// se piu enti
		if (mappa.size() > 1) {
			out.append("<script>init();</script>");
		} else {
			out.append("\n<script>\n");
			out.append("var t = document.getElementById(\"Tabs\");\n");
			out
					.append("document.getElementById(\"codiceEnte\").innerHTML  += \" - \"+ t.innerHTML ;\n");
			out.append("t.style.display = \"none\";\n");
			out.append("</script>\n");
		}
		return out.toString();
	}
}