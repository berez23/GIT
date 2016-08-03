<%

	String url = "jsp/protected/home.faces?";
	
	String fgl = (String) request.getParameter("fgl");
	String par = (String) request.getParameter("par");
	String cat = (String) request.getParameter("cat");
	if(fgl!=null)
		url+="fgl="+fgl;
	if(par!=null)
		url+="&par="+par;
	if(cat!=null)
		url+="&cat="+cat;
	
	response.sendRedirect(url);
%>