package it.webred.mui.http;

import it.webred.mui.MuiException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

public abstract class  AbstractPage implements CodeBehind {

	protected ServletContext ctx = null;
	public ServletContext getServletContext() {
		return ctx;
	}

	public void setServletContext(ServletContext ctx) {
		this.ctx = ctx;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public boolean control(HttpServletRequest req, HttpServletResponse resp,
			MuiBaseServlet servlet) throws ServletException,
			java.io.IOException{
		try{
			return controlImpl(req, resp, servlet);
		}
		catch (MuiException t){
			Logger.log().error(t.getCode(),t.getObj(),(t.getNestedThrowable() != null ?t.getNestedThrowable():t));
			req.setAttribute("Throwable",t);
			req.setAttribute("requestedUri","error.jsp");
			req.getRequestDispatcher("error.jsp").include(req,resp);
			return false;
		}
		catch (Throwable t){
			Logger.log().error(t.getMessage(),t);
			req.setAttribute("Throwable",t);
			req.setAttribute("requestedUri","error.jsp");
			req.getRequestDispatcher("error.jsp").include(req,resp);
			return false;
		}
	}
	
	protected abstract boolean controlImpl(HttpServletRequest req, HttpServletResponse resp,
			MuiBaseServlet servlet) throws ServletException,
			java.io.IOException ;

}
