package it.webred.indice;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.webred.cet.permission.CeTUser;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import it.webred.cet.permission.CeTUser;

public class IndiceCorrServlet extends EscServlet {

	private Properties sqlProps;
	private Properties mapProps;
	
	public void init() throws ServletException {
		
		log.debug("IndiceCorrServlet init");
	}
	
	
	private void caricaProps(HttpServletRequest request) {
		try {		
	     if (sqlProps == null) {
			  InputStream is = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/sql_Indice.properties");
			  sqlProps = new Properties();
			  sqlProps.load(is);
	     }
	     
	     if (mapProps == null) {
			  InputStream is = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/mapping.properties");
			  mapProps = new Properties();
			  mapProps.load(is);
	     }
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
		
	public void  EseguiServizio(HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {

		super.EseguiServizio(request, response);
		HttpSession session = request.getSession();
		session.removeAttribute("indiceCorrList");

		caricaProps(request);
		String fonte = request.getParameter("fonte");
		String progr = request.getParameter("progr");
		String tipo = request.getParameter("tipo");
		String idOrig = request.getParameter("idOrig");
	
		log.debug("Fonte ["+fonte+"] - Progr ["+progr+"] - Tipo ["+tipo+"] - IdOrig ["+idOrig+"]");
		
		IndiceCorrLogic logic = new IndiceCorrLogic(getEnvUtente(request), sqlProps, mapProps);
		
		List<DatiFonte> result = new ArrayList<DatiFonte>();
		
		CeTUser user= (CeTUser) request.getSession().getAttribute("user");

		String action = request.getParameter("action");

		if (action == null || action.equals("")) {
			if ("1".equals(tipo))
			   result = logic.getSoggettiCorr(fonte, progr, tipo, idOrig, user);
			if ("2".equals(tipo))
				   result = logic.getVieCorr(fonte, progr, tipo, idOrig, user);
			if ("3".equals(tipo))
				   result = logic.getCivicoCorr(fonte, progr, tipo, idOrig, user);
			if ("4".equals(tipo))
				   result = logic.getOggettiCorr(fonte, progr, tipo, idOrig, user);
			if ("5".equals(tipo))
				   result = logic.getFabbricatiCorr(fonte, progr, tipo, idOrig, user);
		}
		else {
			if ("1".equals(tipo))
				   result = logic.getSoggettiCorrByUnico(fonte, tipo, idOrig, user);	
			if ("2".equals(tipo))
				   result = logic.getVieCorrByUnico(fonte, tipo, idOrig, user);	
			if ("3".equals(tipo))
				   result = logic.getCiviciCorrByUnico(fonte, tipo, idOrig, user);	
			if ("4".equals(tipo))
				   result = logic.getOggettiCorrByUnico(fonte, tipo, idOrig, user);	
			if ("5".equals(tipo))
				   result = logic.getFabbricatiCorrByUnico(fonte, tipo, idOrig, user);	
		}
		
		session.setAttribute("indiceCorrList", result);
		
		PulsantiNavigazione nav = new PulsantiNavigazione();
		
		try {
			this.chiamaPagina(request,response,"indice/listaCorr.jsp",nav);
		}
		catch(Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	

	


}
