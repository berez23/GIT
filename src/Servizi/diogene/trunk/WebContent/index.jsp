<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->

<html>
	<head>
		<title>Welcome</title>
	</head>
	<script>
	function vai(azione,st,uc){
		document.mainform.action = azione
		document.mainform.ST.value=st;
		document.mainform.UC.value=uc;
		document.mainform.submit();
	}
	
		window.open("main.jsp","","width=550,height=500");
		window.close();
	
	</script>
</html>
