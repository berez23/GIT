package it.webred.gitland.web.svt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MapServlet extends HttpServlet{

	private static final long serialVersionUID = -4872019749212473933L;

	public MapServlet() {
	}//-------------------------------------------------------------------------
	
	protected void service (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String lottoIdAsiMap = request.getParameter("lottoIdAsiMap");
		
		System.out.println(request.getSession(false).getId());
		
		request.getSession(false).setAttribute("lottoIdAsiMap", lottoIdAsiMap);
		
	}//-------------------------------------------------------------------------
	
	

}
