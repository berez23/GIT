<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->

<html>
	<head>
		<title>Welcome</title>
	</head>
	
	<SCRIPT>
	function apri(url,tipo){
	if(tipo=="1"){
			finestra=window.open(url,'dati','statusbar=NO scrollbars=NO height=500 width=550');
			finestra.focus();
		}else{
			finestra=window.open(url,'mostramappa','statusbar=NO scrollbars=NO height=600 width=800');
			finestra.focus();
		}
	}
	</SCRIPT>
	<body>
		<a href="javascript:apri('<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/TributiOggettiTARSU?ST=1&UC=7','1')">Oggetti TARSU</a>
	<center><img src="../ewg/images/tarsu.jpg"></img></center>
<center><a  href="javascript:apri('../eiv/EscIntranetView.jsp','2')" >Apri Mappa</a> </center>  
	</body>
	
</html>
