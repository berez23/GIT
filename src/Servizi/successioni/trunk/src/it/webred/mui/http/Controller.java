package it.webred.mui.http;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;

import java.io.Serializable;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

/**
 * Title: MUI - Description: $Id: Controller.java,v 1.5 2006/05/31 08:01:07
 * franci Exp $ $Source:
 * /usr/local/cvsroot/MUI-WEB/src/it/webred/mui/http/Controller.java,v $
 * Copyright: Copyright (c) 2004 Company: $Author: dan $ $Date: 2006/05/31
 * 08:01:07 $
 *
 * @author : Ing Francesco Ciacca
 * @version $Revision: 1.1 $
 *
 */

public class Controller implements MuiHttpConstants, Serializable {

	private static Map<String, CodeBehind> _codeBehinds = new HashMap<String, CodeBehind>();

	private static FieldConverter _fc = new FieldConverter();

	private long _startTime = System.currentTimeMillis();

	public Controller() {

	}

	public static void prefetch(){
		getCodeBehindForJsp("uploadList.jsp",null);
		getCodeBehindForJsp("noteDetail.jsp",null);
		getCodeBehindForJsp("dapEval.jsp",null);
		getCodeBehindForJsp("docfaDapEval.jsp",null);
	}
	/**
	 * calculate elapsed time
	 *
	 * @param timeInSeconds
	 *            elapsed time in millisecond
	 */
	public String calcElapsed() {
		long timeInMilliSeconds = System.currentTimeMillis() - _startTime;
		long timeInSeconds = timeInMilliSeconds / 1000;
		long hours, minutes;
		hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);
		String elapsed = null;
		if (hours < 10) {
			elapsed = "0" + hours + ":";
		} else {
			elapsed = hours + ":";
		}
		if (minutes < 10) {
			elapsed = elapsed + "0" + minutes;
		} else {
			elapsed = elapsed + minutes;
		}
		return elapsed;
	}

	/**
	 *
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws java.io.IOException
	 */
	public void control(HttpServletRequest req, HttpServletResponse resp,
			MuiBaseServlet servlet, ServletContext ctx) throws ServletException,
			java.io.IOException, MuiException {
		try {
			Principal user = null;
			if ((user = req.getUserPrincipal()) != null) {
				if (req.getSession().getAttribute(MUI_USERPROFILE_SES) == null) {
					req.getSession().setAttribute(MUI_USERPROFILE_SES, user);
				}
				req.setAttribute(USER_REQ, user.getName());
				req.setAttribute(CONNECTION_TIME_REQ, calcElapsed());
			}
			try {
				req.setAttribute(DBTIME_REQ, MuiApplication.getMuiApplication()
						.getDbTime());
			} catch (SQLException e) {
				throw new ServletException(e);
			} catch (NamingException e) {
				throw new ServletException(e);
			}

			Boolean controlled = (Boolean) req
					.getAttribute(CONTROLLER_PASS_REQ);
			if (controlled == null || !controlled.booleanValue()) {
				String requestedUri = (String) req.getAttribute("requestedUri");
				if (requestedUri == null || requestedUri.trim().equals("")) {
					requestedUri = req.getRequestURI();
				}
				if (requestedUri == null || requestedUri.trim().equals("")) {
					requestedUri = "index.jsp";
				}
				int lastPathSepIndex = requestedUri.lastIndexOf("/");
				String requestedUriFull = requestedUri;
				if (lastPathSepIndex != -1) {
					requestedUri = requestedUri.substring(lastPathSepIndex + 1);
				}
				if (requestedUri == null || requestedUri.trim().equals("")) {
					requestedUri = "index.jsp";
				}
				Logger.log().debug(MAIN_URI_REQ + " requestedUri:",
						requestedUri);
				if (requestedUriFull.toLowerCase().indexOf(
						FORMPOST_DISCRIMINATOR.toLowerCase()) == -1) {
					CodeBehind codeBehind = getCodeBehindForJsp(requestedUri,ctx);
					if (codeBehind != null) {
						if (!codeBehind.control(req, resp, servlet)) {
							return;
						}
					}
				}
				req.setAttribute(CONTROLLER_PASS_REQ, new Boolean(true));
				req.setAttribute("FieldConverter", _fc);
				Logger.log().debug(" browser is:", req.getHeader("User-Agent"));
				if (req.getHeader("User-Agent") != null) {
					if (req.getHeader("User-Agent").toUpperCase().indexOf(
							"FIREFOX") != -1) {
						req.setAttribute("BROWSER", "FIREFOX");
					} else {
						req.setAttribute("BROWSER", "INTERNET EXPLORER");
					}
				} else {
					req.setAttribute("BROWSER", "INTERNET EXPLORER");
				}
				req.setAttribute(MAIN_URI_REQ, requestedUri);
				if (!isNonHtmlExport(req)) {
					resp.setContentType("text/html");
				}
				resp.setHeader("Pragma", "No-cache");
				resp.setHeader("Cache-Control", "No-cache");
				resp.setHeader("Expires", "Thu, 01 Jan 1970 01:00:00 CET");
				resp.setHeader("Server",
						"SkillBill Web Application Server (SWAS) v.2.0");
				if (requestedUri.toLowerCase().indexOf(
						POPUP_DISCRIMINATOR.toLowerCase()) != -1) {
					req.getRequestDispatcher(POPUP_TEMPLATE).include(req, resp);
				} else if (requestedUriFull.toLowerCase().indexOf(
						NOTEMPLATE_DISCRIMINATOR.toLowerCase()) != -1) {
					Logger.log()
							.info("servlet.muiService(req, resp);", servlet);
					servlet.muiService(req, resp);
				} else if (isNonHtmlExport(req)) {
					servlet.muiService(req, resp);
				} else {
					resp.setContentType("text/html");
					resp.setCharacterEncoding("UTF-8");
					resp
							.getWriter()
							.write(
									"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
					req.getRequestDispatcher(TEMPLATE).include(req, resp);
				}
			} else {
				servlet.muiService(req, resp);
			}
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ex) {
			}
		}

	}

	public static boolean isNonHtmlExport(HttpServletRequest req) {
		return (req.getParameter("d-1588-e") != null && !"4"
				.equalsIgnoreCase(req.getParameter("d-1588-e")));
	}

	public static boolean isDisplayTagNavReq(HttpServletRequest req) {
		Enumeration params = req.getParameterNames();
		for (; params.hasMoreElements();) {
			String name = (String) params.nextElement();
			if (name.startsWith("d-") && name.endsWith("-p")) {
				return true;
			}
		}
		return false;
	}

	private static CodeBehind getCodeBehindForJsp(String page,ServletContext ctx) {
		boolean isJsp = page.toLowerCase().endsWith(".jsp");
		CodeBehind codeBehind = null;
		if (isJsp) {
			page = page.substring(0, page.toLowerCase().lastIndexOf(".jsp"));
			codeBehind = _codeBehinds.get(page);
			if (codeBehind == null) {
				try {
					codeBehind = (CodeBehind) Class.forName(
							"it.webred.mui.http.codebehind.CodeBehind" + page
									+ "Page").newInstance();
					codeBehind.init();
					_codeBehinds.put(page, codeBehind);
				} catch (ClassNotFoundException cnfe) {

				} catch (IllegalAccessException iae) {

				} catch (InstantiationException iae) {

				}
			}
		}
		if (codeBehind != null) {
			codeBehind.setServletContext(ctx);
		}
		return codeBehind;
	}

}
/**
 * History: $Log: Controller.java,v $
 * History: Revision 1.1  2007/05/16 07:45:40  dan
 * History: *** empty log message ***
 * History:
 * History: Revision 1.4  2007/05/24 15:26:09  ciacca
 * History: inserito supporto file XML
 * History:
 * History: Revision 1.1.1.1  2007/02/15 08:44:08  dan
 * History: no message
 * History:
 * History: Revision 1.10  2007/01/16 09:49:49  franci
 * History: dap rilascio seconda offerta webred
 * History:
 * History: Revision 1.9  2006/10/23 12:14:36  franci
 * History:
 * History: inserito abbozzo DAP
 * History:
 * History: Revision 1.8  2006/09/12 23:57:28  franci
 * History: prima di rilascio
 * History:
 * History: Revision 1.7  2006/07/24 17:05:01  franci
 * History: pre-collaudo
 * History:
 * History: Revision 1.6  2006/06/17 08:43:09  franci
 * History:  rilascio 20060617
 * History: Revision 1.5 2006/05/31 08:01:07 franci ok
 *
 * Revision 1.4 2006/05/26 14:59:27 franci log caricamento!!!!
 *
 * Revision 1.3 2006/05/25 07:51:46 franci ricerche
 *
 * Revision 1.2 2006/05/11 07:09:07 franci prime correzioni da installazione
 * comune milano
 *
 * Revision 1.1.1.1 2006/05/04 09:45:45 franci Importing from Scratch
 *
 *
 */
