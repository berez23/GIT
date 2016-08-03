package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CodeBehinddocfaComunicazioneListPage extends AbstractPage {

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if(req.getAttribute("docfaComunicaziones") != null){
			req.getSession().setAttribute("docfaComunicaziones",req.getAttribute("docfaComunicaziones"));
		}
		else{
			req.setAttribute("docfaComunicaziones",req.getSession().getAttribute("docfaComunicaziones"));
		}
		return true;
	}

}
