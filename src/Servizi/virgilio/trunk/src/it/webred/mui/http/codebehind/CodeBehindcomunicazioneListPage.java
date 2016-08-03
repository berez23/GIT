package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CodeBehindcomunicazioneListPage extends AbstractPage {

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if(req.getAttribute("miConsComunicaziones") != null){
			req.getSession().setAttribute("miConsComunicaziones",req.getAttribute("miConsComunicaziones"));
		}
		else{
			req.setAttribute("miConsComunicaziones",req.getSession().getAttribute("miConsComunicaziones"));
		}
		return true;
	}

}
