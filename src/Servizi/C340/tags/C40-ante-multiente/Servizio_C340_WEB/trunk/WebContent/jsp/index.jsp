<%

	double val = Math.random();
	String url = "jsp/protected/visualizzatore/listRicercaCatasto.faces?" + val;
	response.sendRedirect(url);
%>