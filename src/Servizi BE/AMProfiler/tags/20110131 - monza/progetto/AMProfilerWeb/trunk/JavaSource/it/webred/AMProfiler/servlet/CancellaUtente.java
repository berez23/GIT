package it.webred.AMProfiler.servlet;

import it.webred.AMProfiler.beans.AmUser;
import it.webred.AMProfiler.exception.AMException;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: CancellaUtente
 * 
 */
public class CancellaUtente extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public CancellaUtente() {
		super();
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
		try {
			if (validate(request)) {
				BaseAction.CancellaUtente(request);
			}

			ArrayList<AmUser> availUserList = BaseAction.listaUsers();
			request.setAttribute("userList", availUserList);
			
			request.getRequestDispatcher("/jsp/newUser.jsp").forward(request, response);
		}
		catch (AMException e) {
			request.setAttribute("noCancellazioneUtente", "true");
			request.setAttribute("msgCancellazioneUtente", e.getMessage());
		}
		catch (Exception e) {
			request.setAttribute("noCancellazioneUtente", "true");
			request.setAttribute("msgCancellazioneUtente", "Errore non previsto nella cancellazione utente");
		}

	}

	private boolean validate(HttpServletRequest request) {
		if (request.getParameter("userName") == null || request.getParameter("userName").trim().equals(""))
			return false;
		return true;
	}
}