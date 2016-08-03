<%
	double val = Math.random();
	String url = "jsp/protected/search/pratiche/search.faces?" + val;
	String es = request.getParameter("es");
	response.sendRedirect(url + (es!=null?("&es="+es):""));
%>