package it.webred.mui.http;

import it.webred.mui.MuiException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CodeBehind extends MuiHttpConstants {
	boolean control(HttpServletRequest req, HttpServletResponse resp,
			MuiBaseServlet servlet) throws MuiException,ServletException,
			java.io.IOException;
	void init();
	ServletContext getServletContext();
	void setServletContext(ServletContext ctx);
}
