package it.webred.AMProfiler.servlet;

import it.webred.AMProfiler.beans.AmComune;
import it.webred.AMProfiler.beans.AmGroup;
import it.webred.AMProfiler.exception.AMException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * Servlet implementation class for Servlet: NuovoGruppo
 * 
 */
public class NuovoGruppo extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public NuovoGruppo() {
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
			
			if( "Salva".equals(request.getParameter("submit")) )
			{
				if( validate(request))
					BaseAction.NuovoGruppo(request);
			}
			
			ArrayList<AmGroup> gruppi = BaseAction.listaTuttiGruppi(null);
			request.setAttribute("gruppi", gruppi);

			ArrayList<AmComune> comuni = BaseAction.listaComuni();
			request.setAttribute("comuni", comuni);

			request.getRequestDispatcher("/jsp/newGroup.jsp").forward(request, response);
		}
		catch (AMException e) {
			request.setAttribute("msgNuovoGruppo", e.getMessage());
			request.getRequestDispatcher("/jsp/newGroup.jsp").forward(request, response);
		}
		catch (Exception e) {
			BaseAction.toErrorPage(request, response, e);
		}
	}
	
	private boolean validate(HttpServletRequest request )
	{
		String nuovoGruppo = request.getParameter("nuovoGruppo");
		String ente = request.getParameter("enti");
		
		boolean isGruppoOk = StringUtils.isNotEmpty(nuovoGruppo);
		boolean isEnteOk = StringUtils.isNotEmpty(ente);
		   
		if( !isGruppoOk && !isEnteOk )
			request.setAttribute("msgNuovoGruppo", "Specificare il nome del nuovo gruppo e l'ente di appartenza");

		if( isGruppoOk && !isEnteOk )
			request.setAttribute("msgNuovoGruppo", "Specificare l'ente");

		if( !isGruppoOk && isEnteOk )
			request.setAttribute("msgNuovoGruppo", "Specificare il nome del nuovo gruppo");

		return isGruppoOk && isEnteOk;
	}
}