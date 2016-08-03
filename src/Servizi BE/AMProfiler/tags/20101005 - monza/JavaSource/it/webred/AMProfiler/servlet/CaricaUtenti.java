package it.webred.AMProfiler.servlet;

import it.webred.AMProfiler.beans.AmUser;

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

/**
 * Servlet implementation class for Servlet: SalvaUtente
 * 
 */
public class CaricaUtenti extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

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
	public CaricaUtenti() {
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
			ArrayList<AmUser> availUserList = BaseAction.listaUsers();

			request.setAttribute("userList", availUserList);
			request.getRequestDispatcher("/jsp/newUser.jsp").forward(request, response);
		}
		catch (Exception e) {
			if (e.toString().indexOf("ORA-00001") == -1) {
				BaseAction.toErrorPage(request, response, e);
			}
			else {
				request.setAttribute("giaPresente", "true");
				request.getRequestDispatcher("/jsp/newUser.jsp").forward(request, response);
			}
		}
		finally {
			BaseAction.chiudiConnessione(con, statment);
		}

	}

}