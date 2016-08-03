<%@page contentType="text/html; charset=iso-8859-1" language="java"
            session="true" %>
    <%
    	request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    %>
