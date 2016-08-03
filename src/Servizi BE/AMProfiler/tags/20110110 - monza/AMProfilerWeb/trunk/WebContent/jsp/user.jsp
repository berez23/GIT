<div align="left"
	style="position:absolute;
	top:0;
	width:150px;
	height:70px;
	border-bottom:1px solid #000000;
	overflow:hidden;
	color: #000000;
	padding:5px;
	z-index: 100;
	right: 0">
	
<%String pathWebApp = "http://"+request.getServerName()+":"+ request.getServerPort() +  request.getContextPath();%>

<script type="text/javascript">
   function caricaUtenti() {	
    var browser=navigator.appName;
    if(browser.indexOf("Microsoft")==-1)
    	window.open("CaricaUtenti","Gestione Utenti","width=600,height=600,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");
    else 
    	window.open("CaricaUtenti","Gestione_Utenti","width=600,height=600,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");		
   }
	
  function cambiaPassword(uN) {
    var browser=navigator.appName;
    if(browser.indexOf("Microsoft") == -1)
    	window.open("<%=pathWebApp%>/SalvaUtente?mode=pwd&userName=" + uN,"Carica Utenti","width=600,height=220,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");
    else
    	window.open("<%=pathWebApp%>/SalvaUtente?mode=pwd&userName=" + uN,"Carica_Utenti","width=600,height=220,left=300,top=0,scrollbars=yes,resizable=yes,status=yes");	
  }
</script>

  <p align="left">UTENTE:<font color="red"> <%=request.getUserPrincipal().getName()%></font><br>
  	<a href="<%=pathWebApp%>/Logout" >Cambia utente</a> <br>
  	<a href="javascript:void(0);" onclick="cambiaPassword('<%=request.getUserPrincipal().getName()%>')">Cambia password</a> <br>
  
  	<%if ((String) request.getAttribute("gestPerm") != null){%> 
      <a href="javascript:void(0);" onclick="caricaUtenti()" >Gestione Utenti</a> 
    <%}%>
  </p>
</div>
