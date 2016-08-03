package it.webred.AMProfiler.servlet;

import it.webred.AMProfiler.beans.AmGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * Servlet implementation class for Servlet: SalvaUtente
 * 
 */
public class SalvaUtente extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int numGiorniVal = 90; // default
	public static int numGiorniAnteScad = 5; // default

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public SalvaUtente() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		numGiorniVal = Integer.parseInt(config.getInitParameter("numGiorniVal"));
		numGiorniAnteScad = Integer.parseInt(config.getInitParameter("numGiorniAnteScad"));
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection con = null;
		Statement statment = null;
		try {
			String mode = request.getParameter("mode");
			String userName = request.getParameter("userName");

			request.setAttribute("mode", mode);
			request.setAttribute("showPwd", true);
			request.setAttribute("showGroup", !"pwd".equals(mode));

			if (mode != null) {
				if (!"new".equals(mode)) {
					request.setAttribute("uN", userName);
				}
			}

			if ("Salva".equals(request.getParameter("submit"))) {
				if ("new".equals(mode)) {

					// Salva utente
					if (validate(request)) {
						BaseAction.SalvaUtente(request);
						request.setAttribute("mode", "vis");
						request.setAttribute("uN", userName);
					}

					// Salva Gruppi
					BaseAction.SalvaGruppi(request);
				}

				if ("vis".equals(mode)) {
					
					// Salva utente
					String password = request.getParameter("password");
					String password2= request.getParameter("password2");

					if( StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(password2) && validate(request) ) {
						BaseAction.SalvaUtente(request);
					}

					// Salva Gruppi
					BaseAction.SalvaGruppi(request);
				}

				if ("pwd".equals(mode)) {

					// Salva utente
					if (validate(request)) {
						BaseAction.SalvaUtente(request);
					}

					boolean isPwdScaduta = request.getParameter("pwdScaduta") != null && new Boolean(request.getParameter("pwdScaduta")).booleanValue();
					boolean stessoUtente = isPwdScaduta || request.getUserPrincipal().getName().equals(request.getAttribute("uN"));
					boolean doLogout = stessoUtente && request.getAttribute("salvaOk") != null;
					request.getRequestDispatcher("/jsp/updUser.jsp?doLogout=" + doLogout + (request.getAttribute("salvaOk") != null && isPwdScaduta ? "&soloMsg=true" : "")).forward(request, response);
					return;
				}
			}

			// Carica Gruppi
			ArrayList<AmGroup> gruppi = BaseAction.listaTuttiGruppi(userName);
			request.setAttribute("gruppi", gruppi);

			request.getRequestDispatcher("/jsp/updUser.jsp").forward(request, response);
		}
		catch (Exception e) {
			if (e.toString().indexOf("ORA-00001") == -1) {
				BaseAction.toErrorPage(request, response, e);
			}
			else {
				request.setAttribute("giaPresente", "true");
				request.getRequestDispatcher("/jsp/updUser.jsp").forward(request, response);
			}
		}
		finally {
			BaseAction.chiudiConnessione(con, statment);
		}

	}

	private boolean validate(HttpServletRequest request) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		if (userName == null || userName.trim().equals("")) {
			request.setAttribute("uN", "");
			return false;
		}

		if (password.length() == 0) {
			request.setAttribute("uN", userName);
			request.setAttribute("noPwd", "true");
			return false;
		}
		if (!password.equals(password2)) {
			request.setAttribute("uN", userName);
			request.setAttribute("pwdError", "true");
			return false;
		}
		return true;
	}

}