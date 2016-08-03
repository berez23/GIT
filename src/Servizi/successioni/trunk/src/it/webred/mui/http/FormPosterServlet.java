package it.webred.mui.http;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

/**
 * Servlet implementation class for Servlet: FormPosterServlet
 *
 */
 public class FormPosterServlet extends it.webred.mui.http.MuiBaseServlet implements javax.servlet.Servlet {
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public FormPosterServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void muiService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Logger.log().info("muiService;",this);
		String form_action = request.getPathInfo().substring(1);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("<html>");
		response.getWriter().write("<body>");
		response.getWriter().write("<form name=\"myform\" id=\"myform\" method=\"post\" action=\"../"+form_action+"\">");
		for (Iterator iter = request.getParameterMap().entrySet().iterator(); iter.hasNext();) {
			java.util.Map.Entry element = (java.util.Map.Entry) iter.next();
			String paramName = (String)element.getKey();
			String[] paramValues = (String[])element.getValue();
			for (int i = 0; i < paramValues.length; i++) {
				response.getWriter().write("<input type=\"hidden\" name=\""+paramName+"\" value=\""+paramValues[i]+"\" />");
			}
		}
		response.getWriter().write("</form>\n");
		response.getWriter().write("<script language=\"JavaScript\">\n");
		response.getWriter().write("document.getElementById('myform').submit();\n");
		response.getWriter().write("</script>\n");
		response.getWriter().write("</body>");
		response.getWriter().write("</html>");
		response.getWriter().flush();
	}  	
	
}