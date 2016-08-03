<%
	String url = "jsp/protected/welcome.faces";
	String es = request.getParameter("es");
	response.sendRedirect(url + (es!=null?("?es="+es):""));
%>
