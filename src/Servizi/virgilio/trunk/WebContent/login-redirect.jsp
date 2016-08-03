<%@page contentType="text/html; charset=iso-8859-1" language="java"
        session="true"%>
<!-- %@ page import="it.webred.permessi.GestionePermessi" % -->
<%
		/*
	    if(!GestionePermessi.isCastableUser(((HttpServletRequest) pageContext.getRequest()).getUserPrincipal()))
	    {
			response.sendRedirect(request.getContextPath() + "/login.secure");			
	    }
	    else
	    {	    	
			response.sendRedirect(request.getContextPath() + "/josso_login/");
	    }*/
		response.sendRedirect(request.getContextPath() + "/login.secure");
    
%>
