package it.webred.fascicolo;

import it.escsolution.escwebgis.common.EnvBase;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.support.audit.AuditDBWriter;
import it.webred.fascicolo.act.RicercaVia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class FascicoloRunnerServlet extends HttpServlet {
	protected static final Logger log = Logger.getLogger("diogene.log");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            

	private static final long serialVersionUID = 9141744228661330709L;
	public static final String REQUEST_DATI_NAME = "DATI_FASCICOLO";
	private static final String JSP_VOID = "/fascicolo/void.jsp";
	private static final String JSP_ERROR = "/fascicolo/error.jsp";
	private static final String JSP_INDEX = "/fascicolo/index.jsp";
	private static final String JSP_RICERCA_VIA = "/fascicolo/cercavia.jsp";
	private String pathDatiDiogene = "";
	LinkedHashMap<String, FascicoloActionBean> actions;
	
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		String risorsa = JSP_VOID;
		String foglio = request.getParameter("ricerca_foglio");
		String particella = request.getParameter("ricerca_particella");
		String sub = request.getParameter("ricerca_sub");
		String filtroData = request.getParameter("filtroData");
		String situazione = request.getParameter("hSituazione");
		
		HttpSession ses = request.getSession();

		this.caricaPathDatiDiogene();
		
		try {
			String codNaz = null;
			Connection con = getConnectionDbTotale(request);
			Statement st = con.createStatement();
			ResultSet rsE = st.executeQuery("select uk_belfiore from ewg_tab_comuni");
			if (rsE.next())
				codNaz = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();
			con.close();

			String function = request.getParameter("AZ");
			log.debug(this.getClass().getName() + " "+ new Date().toString() + " : " +
					"function["+ function + "] " +
					"required by " + request.getRemoteUser()+ "-" + request.getRemoteAddr());
			initConfig(request);

			if (function == null || function.equals("")) {
				risorsa = caricamenu(request, response,this.getServletContext(),actions);
				request.getSession().setAttribute("fascicoliFabAbbilitati",actions);
			} else if (function.equals("STAMPA")) {
				stampa(request, response, codNaz, foglio, particella, sub,situazione, filtroData);
				return;
			} else if (function.equals("RICERCAVIA")) {
				RicercaVia ricercavia = new RicercaVia();
				// particella e nome via
				if (particella != null && !particella.equals(""))
					request.setAttribute(REQUEST_DATI_NAME, ricercavia.leggiDati(request, codNaz, foglio, particella,sub, situazione, filtroData));
				risorsa = JSP_RICERCA_VIA;
			} else {
				risorsa = getJspOut(function);
				request.setAttribute(REQUEST_DATI_NAME,getFascicoloActionRequestHandler(function).leggiDati(request, codNaz, foglio, particella, sub,situazione, filtroData));
				log.debug("Funzione:"+function+" Risorsa:"+risorsa);
			}
			request.setAttribute("situazione", situazione);
			request.getSession().setAttribute("PATH_DATI_DIOGENE",pathDatiDiogene + "/" + codNaz + "/");
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = risorsa + "|" + foglio + "|" + particella + "|" + sub + "|" + filtroData + "|" + situazione;
				EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
				CeTUser cetUser = eu.getUtente();
				AuditDBWriter auditWriter = new AuditDBWriter();
				auditWriter.auditMethod(cetUser.getCurrentEnte(),cetUser.getUsername(), cetUser.getSessionId(), this.getClass().getName(), "service", arguments, request.getAttribute(REQUEST_DATI_NAME), null);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
		} catch (Exception ex) {
			/*
			 * se si è avuta un'eccezione nella gestione della richiesta viene
			 * creato l'error view bean con la descrizione dell'eccezione, e
			 * selezionata la view d'errore
			 */
			risorsa = JSP_ERROR;
			String err = "";
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			ex.printStackTrace(new PrintStream(ba));
			request.setAttribute("ErrorMessage", ex.getMessage());
			request.setAttribute("ErrorTrace", ba.toString());
			log.debug(this.getClass().getName() + ": \n" + ba.toString());
			log.error(ex.getMessage(), ex);

		}
		// effettua la configurazione della risponse HTTP
		response.setContentType("text/html");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		// response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");

		RequestDispatcher rd;
		ServletContext sc = this.getServletContext();
		rd = sc.getRequestDispatcher(risorsa);
		log.debug(this.getClass().getName() + " forward=" + risorsa);
		rd.forward(request, response);

	}
	
	private void caricaPathDatiDiogene(){
	
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("dir.files.dati");

		try {
			ParameterService parameterService= (ParameterService)getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");

			AmKeyValueExt amKey = parameterService.getAmKeyValueExt(criteria);
			pathDatiDiogene = amKey.getValueConf();

		} catch (Exception e) {
			log.error("Path Dati Diogene non trovato!");
			pathDatiDiogene = "";
		}
	}

	private void stampa(HttpServletRequest request,
			HttpServletResponse response, String codNazionale, String foglio,
			String particella, String sub, String situazione, String filtroData)
			throws Exception {
		
		Document document = new Document(PageSize.A4.rotate());
		String subt = sub != null ? " subalterno " + sub : "";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		document.addTitle("Fascicolo Fabbricato");
		document.addSubject("foglio:" + foglio + " particella " + particella
				+ subt);
		document.addKeywords("foglio:" + foglio + " particella " + particella
				+ subt);
		document.addCreator("hiweb s.r.l.");
		document.addAuthor("DAN");
		document.addHeader("Expires", "0");

		writer.setPageEvent(new EndPageH());
		document.open();

		String[] functions = request.getParameterValues("TABELLE");
		for (int i = 0; i < functions.length; i++) {

			LinkedList<Object> dati = getFascicoloActionRequestHandler(
					functions[i]).stampaDati(request, codNazionale, foglio,
					particella, sub, situazione, filtroData);
			for (Object dato : dati) {
				// document.add(Chunk.NEWLINE);
				document.add(new Paragraph(" "));
				document.add((Element) dato);

			}
		}
		document.close();
		response.setContentType("application/pdf");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		// response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Content-Disposition",
				"inline; attachment; filename=\"" + new Date().getTime()
						+ ".pdf" + "\"");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();

	}

	private class EndPageH extends PdfPageEventHelper {

		public void onStartPage(PdfWriter writer, Document document) {
			PdfContentByte cb = writer.getDirectContent();
			cb.saveState();
			String text = "Pagina " + writer.getPageNumber();
			float textBase = document.bottom() - 20;
			try {
				cb.setFontAndSize(BaseFont.createFont("Helvetica",
						BaseFont.WINANSI, false), 6);
			} catch (Exception e) {
				e.printStackTrace();
			}

			cb.beginText();
			cb.setTextMatrix(document.left(), textBase);
			cb.showText(text);
			cb.endText();

		}

	}

	private String caricamenu(HttpServletRequest request,HttpServletResponse response, ServletContext servletContext,LinkedHashMap<String, FascicoloActionBean> actions) {
		
		StringBuffer htmlmenu = new StringBuffer();
		htmlmenu.append("<div  class=\"divMenu\">\n");
		htmlmenu.append("<strong>Fascicolo Fabbricato: </strong>\n");
		htmlmenu.append("<table width=\"400\" border=\"1\">\n");
		htmlmenu.append("  <tr align=\"left\" valign=\"top\">\n");
		htmlmenu.append("    <td><table id=\"filter\" align=center>\n");

		htmlmenu.append("      <tr>\n");
		htmlmenu.append("        <td class=\"TDmainLabel\"  nowrap width=\"30%\" align=\"right\"><span class=\"TXTmainLabel\">Situazione:</span></td>\n");
		htmlmenu.append("        <td  class=\"TDeditDBCOmboBox\"  width=\"70%\" align=\"left\"> <input type=\"radio\" id=\"rdoSituazione\" name=\"rdoSituazione\" value=\"ATTUALE\" class=\"INPDBComboBox\" checked=\"checked\" onclick=\"onOffElement('ATTUALE');\" />&nbsp;<span class=\"TXTmainLabel\">Solo Attuale</span></td>\n");
		htmlmenu.append("      </tr>\n");

		htmlmenu.append("      <tr>\n");
		htmlmenu.append("        <td class=\"TDmainLabel\"  nowrap  align=\"right\"><span class=\"TXTmainLabel\">&nbsp;<input type=\"hidden\" id=\"hSituazione\" name=\"hSituazione\" value=\"ATTUALE\"/></span></td>\n");
		htmlmenu.append("        <td  class=\"TDeditDBCOmboBox\"  align=\"left\"> <input type=\"radio\" id=\"rdoSituazione\" name=\"rdoSituazione\" value=\"COMPLETA\" class=\"INPDBComboBox\" onclick=\"onOffElement('COMPLETA');\" />&nbsp;<span class=\"TXTmainLabel\">Completa</span></td>\n");
		htmlmenu.append("      </tr>\n");

		// htmlmenu.append("      <tr >\n");
		// htmlmenu.append("        <td class=\"TDmainLabel\"  nowrap align=\"right\"><span class=\"TXTmainLabel\"> <input type=\"text\" id=\"filtroData\" name=\"filtroData\" value=\"\" readonly=\"true\" disabled=\"disabled\" size=\"8\"/> </span></td>\n");
		// htmlmenu.append("        <td  class=\"TDeditDBCOmboBox\"  align=\"left\" > <input type=\"radio\" id=\"rdoSituazione\" name=\"rdoSituazione\" value=\"ALLA_DATA\" class=\"INPDBComboBox\" onclick=\"onOffElement('ALLA_DATA');\" />&nbsp;<span class=\"TXTmainLabel\">Alla Data</span></td>\n");
		// htmlmenu.append("      </tr>\n");

		htmlmenu.append("      <tr>\n");
		htmlmenu.append("        <td class=\"TDmainLabel\"  nowrap align=\"right\"><span class=\"TXTmainLabel\">Foglio:</span></td>\n");
		htmlmenu.append("        <td  class=\"TDeditDBCOmboBox\"  align=\"left\"><input name=\"ricerca_foglio\"  id=\"ricerca_foglio\" type=\"text\" value=\"\" class=\"INPDBComboBox\"  style=\"width: 40px\"/></td>\n");
		htmlmenu.append("      </tr>\n");
		htmlmenu.append("      <tr>\n");
		htmlmenu.append("        <td class=\"TDmainLabel\"  nowrap align=\"right\"><span class=\"TXTmainLabel\">Particella: </span></td>\n");
		htmlmenu.append("        <td  class=\"TDeditDBCOmboBox\"  align=\"left\"><input name=\"ricerca_particella\" id=\"ricerca_particella\" type=\"text\" value=\"\" class=\"INPDBComboBox\"  style=\"width: 40px\"/></td>\n");
		htmlmenu.append("      </tr>\n");
		htmlmenu.append("      <tr>\n");
		htmlmenu.append("        <td class=\"TDmainLabel\" colspan='2' nowrap>\n");
		htmlmenu.append("        <div class=\"TXTmainLabel\" style=\"cursor: pointer; text-align:center; text-decoration:underline;\" onclick=\"cercaVia();\">cerca per via</div></td>\n");
		htmlmenu.append("      </tr>\n");
		htmlmenu.append("    </table></td>\n");
		htmlmenu.append("    <td class=\"TDmainLabel\"  nowrap><span class=\"TXTmainLabel\">\n");

		for (String key : actions.keySet()) {
			FascicoloActionBean b = actions.get(key);
			htmlmenu.append(" <div><input type=\"checkbox\" name=\"menuAction\" id=\"check"
					+ b.getAction()
					+ "\" value=\""
					+ b.getAction()
					+ "\"   class=\"INPDBComboBox\" />"
					+ b.getDescrizione()
					+ "</div>\n");
		}
		htmlmenu.append("  </tr>\n");
		htmlmenu.append("  <tr align=\"left\" valign=\"top\">\n");
		htmlmenu.append("    <td><div align=\"center\"><span class=\"TDmainLabel\">\n");
		htmlmenu.append("      <input name=\"ricerca\" type=\"button\" id=\"ricerca\" value=\"Ricerca\" onClick=\"ricerca()\"/> <input name=\"stampa\" type=\"button\" id=\"stampa\" value=\"Stampa\" onClick=\"stampaFascicolo()\"/> <input name=\"ricerca\" type=\"button\" id=\"reimposta\" value=\"Reimposta\" onClick=\"reimposta()\"/>\n");
		htmlmenu.append("    </span></div></td>\n");
		htmlmenu.append("    <td class=\"TDmainLabel\"  nowrap><input name=\"selTutto\" type=\"button\" id=\"selTutto\" onClick=\"selTutto()\" value=\"Seleziona tutto\" /></td>\n");
		htmlmenu.append("  </tr>\n");
		htmlmenu.append("</table>\n");
		htmlmenu.append("</div>\n");

		htmlmenu.append("<div style=\"align: right\">\n");
		htmlmenu.append("  <table align=center>\n");
		htmlmenu.append("  <tr align=\"left\" valign=\"top\" id=\"trDataFiltro\"  >\n");
		htmlmenu.append("    <td colspan=\"2\" nowrap  >\n");
		htmlmenu.append("		<span class=\"TDmainLabel\">Evidenzia i record validi in data:</span> <input type=\"text\" id=\"filtroData\" name=\"filtroData\" value=\"\" readonly=\"true\" size=\"8\"/>\n");
		htmlmenu.append("		<input type=\"button\" id=\"btnFiltroData\" onclick=\"filtraPerData($('#filtroData').val())\" disabled=\"true\" value=\"Applica\"/>\n");
		htmlmenu.append("    </td>\n");
		htmlmenu.append("  </tr>\n");
		htmlmenu.append("</table>\n");
		htmlmenu.append("</div>\n");

		for (String key : actions.keySet()) {
			FascicoloActionBean b = actions.get(key);
			htmlmenu.append("      <div  id=\"div" + b.getAction()
					+ "\" class=\"divContenitore\" titolo=\""
					+ b.getDescrizione() + "\"></div>\n");
		}

		request.setAttribute("menu", htmlmenu.toString());
		return JSP_INDEX;
	}

	public FascicoloActionRequestHandler getFascicoloActionRequestHandler(String function) throws Exception {
		
		if (function == null || function.trim().equals("")) {
			return null;
		}
		if (!actions.containsKey(function)){
			log.debug("actions size["+actions.size()+"] -FascicoloActionRequestHandler non trovata per funzione:"+function);
			return null;
		}
		FascicoloActionBean t = actions.get(function);
		String className = t.getExecutor();
		FascicoloActionRequestHandler ex = (FascicoloActionRequestHandler) Class.forName(className).newInstance();

		return ex;
	}

	public String getJspOut(String function) throws Exception {
		if (function == null || function.trim().equals("")) {
			return null;
		}
		if (!actions.containsKey(function)){
			log.debug("actions size["+actions.size()+"] - Jsp non trovata per funzione:"+function);
			
			return null;
		}
		
		return actions.get(function).getJsp();

	}

	private synchronized void initConfig(HttpServletRequest request) {
		if (actions==null || (actions!=null && actions.size()==0)){
			actions = new LinkedHashMap<String, FascicoloActionBean>();
			Statement st = null;
			ResultSet rs = null;
			Connection con = null;
			try {
				con = getConnectionDiogene(request);
				st = con.createStatement();
				rs = st.executeQuery("SELECT ID, DESCRIZIONE, CLASSE, ACTION_NAME, JSP_OUT from FASCICOLO_FAB_CONFIG WHERE ABILITATO = 1 ORDER BY ORDINE ");
				
				while (rs.next()) {
					String id = rs.getString("ID");
					String descrizione = rs.getString("DESCRIZIONE");
					String executor = rs.getString("CLASSE");
					String action = rs.getString("ACTION_NAME");
					String jsp = rs.getString("JSP_OUT");
	
					if (!actions.containsKey(action)){
						actions.put(action, new FascicoloActionBean(id,descrizione, executor, action, jsp));
						log.debug("Init - Inserita action:"+action);
					}
				}
				
				if(actions.size()==0)
					log.warn("Iniziazione Fascicolo Fabbricato:NESSUN DATO TROVATO");
				
	
			} catch (Exception e) {
				log.error("Errore Iniziazione Fascicolo Fabbricato:"+e.getMessage(),e);
			} finally {
				
				try {
					if (rs != null)
						rs.close();
					if (st != null)
						st.close();
					if (con != null)
						con.close();
				} catch (SQLException e) {
	
					log.error("Fascicolo Fabbricato-initConfig:"+e.getMessage(),e);
				}
			}
		}else
			log.info("Configurazione Iniziale già caricata - num "+actions.size()+" elementi");
		
	}

	protected Connection getConnectionDiogene(HttpServletRequest request)throws Exception {
		Context initContext = new InitialContext();
		
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		
		DataSource ds = (DataSource) initContext.lookup(es.getDataSource());
		Connection conn = ds.getConnection();
		return conn;
	}

	protected Connection getConnectionDbTotale(HttpServletRequest request)
			throws Exception {

		Context initContext = new InitialContext();
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		
		DataSource ds = (DataSource) initContext.lookup(es.getDataSource());
		Connection conn = ds.getConnection();
		return conn;
	}
	
	public Object getEjb(String ear, String module, String ejbName){
		EnvBase base = new EnvBase();
		return base.getEjb(ear, module, ejbName);
	}
}
