<%

	double val = Math.random();
	String url = "jsp/protected/visualizzatore/listRicercaCatasto.faces?" + val;
	String es = request.getParameter("es");
	response.sendRedirect(url + (es!=null?("&es="+es):""));
%>