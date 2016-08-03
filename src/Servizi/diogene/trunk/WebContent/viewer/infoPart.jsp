<%@page import="java.sql.*" %>
<%@page import="javax.naming.*" %>
<%@page import="javax.sql.DataSource" %>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.escsolution.escwebgis.common.EnvBase"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="it.webred.amprofiler.ejb.perm.LoginBeanService"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.escsolution.escwebgis.fascicolo.logic.FascFabbAppLogic"%>

<html>
<head>
<script language="JavaScript">
<!-- //
	function aprischeda(schName){
		<%--if(top.sx.rows =="55,30,*,58"){
	 		top.parent.parent.MenuCartelle.document.all('cart1').background="images/CartellaOff.jpg";
	 		top.parent.parent.MenuCartelle.document.all('cart2').background="images/CartellaOff.jpg";
			top.parent.parent.MenuCartelle.document.all('cart3').background="images/CartellaOff.jpg";
	 		top.parent.parent.MenuCartelle.document.all('cart1txt').className="txtnoselect";
	 		top.parent.parent.MenuCartelle.document.all('cart2txt').className="txtnoselect";
	 		top.parent.parent.MenuCartelle.document.all('cart3txt').className="txtnoselect";
	   	 	window.open(schName,"Dati");
		}else{--%>
			popDati = window.open(schName,"windowdati",'width=600, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
			popDati.focus();
			window.open('', '_self', '');
			window.close();

		<%--}--%>
	}
// -->
</script>
<%
	String entetable = request.getParameter("es");
	String[] arr = entetable.split("\\?");
	String currentEnte = arr[0];
	String tn = arr[1].replace("TableName=", "");
	String pkField = request.getParameter("IdentField");
	String pk = request.getParameter("ElemId");
	String layerName = ((String)request.getParameter("LayerName")).toUpperCase();
	System.out.println("Layer "+layerName);

	Context cont= new InitialContext();
	DataSource theDataSource = null;
	CeTUser cetUser = (CeTUser) request.getSession().getAttribute("user");
	if(cetUser == null){
		EnvBase base = new EnvBase();
		LoginBeanService service = (LoginBeanService) base.getEjb("AmProfiler", "AmProfilerEjb", "LoginBean");
		cetUser = new CeTUser();
		cetUser.setUsername(request.getUserPrincipal().getName());
		HashMap<String, String> permList = service.getPermissions(cetUser.getUsername(), currentEnte);
		cetUser.setPermList(permList);
		cetUser.setCurrentEnte(currentEnte);
		cetUser.setSessionId(request.getSession(false).getId());
		request.getSession().setAttribute("user", cetUser);
	}
	EnvUtente eu = new EnvUtente(cetUser, null, null);
	EnvSource es = new EnvSource(eu.getEnte());
	theDataSource = (DataSource)cont.lookup(es.getDataSource());
	Connection cnn = theDataSource.getConnection();
	
	eu.setDataSource("java:jdbc/" + es.getDataSource());

	PreparedStatement pst = null;
	ResultSet rs = null;
	String pk_particelle = "";
	String pk_civico_unico = "";
	String pk_par_catastali="";
	String keystr="";
	String foglio = "";
	String particella = "";
	boolean trovato = false;
	boolean infoCivico = false;
	boolean infoFascCivico = false;
	boolean infoCorrCivico = false;
	boolean infoDocfaProt = false;
	boolean infoZoneCensuarie = false;
	boolean infoCosap = false;
	boolean infoCatasto = false;
	boolean infoEcogCivico = false;
	boolean infoCrifAvgRedditi = false;
	boolean infoPregeoSuFoglio = false;
	boolean infoStatCategorie = false;
	boolean infoFascFabb = false;
	boolean infoDocfaSuParticella = false;
	boolean infoCened = false;
	
	try
	{
				
		if (layerName.equalsIgnoreCase("Civici") || layerName.equalsIgnoreCase("Civico Selezionato") || layerName.equalsIgnoreCase("CiviciAbitati")){
			infoCivico = true;
			trovato = true;
		}else if (layerName.equalsIgnoreCase("Fascicolo civico")){
			infoFascCivico = true;
			trovato = true;
		}else if (layerName.equalsIgnoreCase("Fascicolo fabbricato")){
			infoFascFabb = true;
			pst = cnn.prepareStatement("select * from " + tn + " where " + pkField + "=?");
			pst.setString(1, pk);
			rs = pst.executeQuery();
			if (rs.next()){
				trovato = true;
				pk_par_catastali = rs.getString("fk_par_catastali");
			}    	  
		}else if (layerName.equalsIgnoreCase("Correlazione civico")) {
			infoCorrCivico = true;
			trovato = true;
			pst = cnn.prepareStatement("select fk_civico from sit_civico_totale where id_dwh=? and fk_ente_sorgente=4 and prog_es=2");
			pst.setString(1, pk);
			rs = pst.executeQuery();
			if (rs.next()){
				trovato = true;
				pk_civico_unico = rs.getString("fk_civico");
			}    	  
		}else if (layerName.equalsIgnoreCase("STA01: UNITA ABITATIVE CON DOCFA") || layerName.equalsIgnoreCase("STA02: UNITA ABITATIVE CON DOCFA SOTTOCLASSATE")) {
			infoDocfaProt = true;
			trovato = true;
		} else if (layerName.equalsIgnoreCase("Civici-Cosap")) {
			infoCosap = true;
			trovato = true;
		} else if (layerName.equalsIgnoreCase("Particella Corrente") || layerName.equalsIgnoreCase("Fabbricati") || layerName.equalsIgnoreCase("Particelle")){
			infoCatasto = true;
			pst = cnn.prepareStatement("select * from " + tn + " where " + pkField + "=?");
			pst.setString(1, pk);
			rs = pst.executeQuery();
			if (rs.next()){
				trovato = true;
				pk_particelle = rs.getString("pk_particelle");
				
			}    	  
		} else if (layerName != null && layerName.trim().equalsIgnoreCase("STA04: ZONE CENSUARIE")) {
			infoZoneCensuarie = true;
			trovato = true;
		}else if(layerName!=null && layerName.trim().equalsIgnoreCase("Civici Ecografico")){
			infoEcogCivico = true;
			trovato=true;
		}else if(layerName!=null && layerName.trim().equalsIgnoreCase("Reddito medio per famiglia")){
			infoCrifAvgRedditi = true;
			trovato=true;
		}else if(layerName!=null && layerName.trim().equalsIgnoreCase("PREGEO PRESENTATI SU FOGLIO")){
			infoPregeoSuFoglio = true;
			trovato=true;
			System.out.println("Info pregeo "+infoPregeoSuFoglio);
		}else if(layerName!=null && layerName.trim().equalsIgnoreCase("CATEGORIE CATASTALI")){
			infoStatCategorie = true;
			trovato=true;
			System.out.println("Info statistica categorie "+infoStatCategorie);
		}else if(layerName!=null && layerName.trim().equalsIgnoreCase("STA05: DOCFA SU PARTICELLA")){
			infoDocfaSuParticella = true;
			trovato=true;
			System.out.println("Info DOCFA su Particella "+infoDocfaSuParticella);
		}
		else if(layerName!=null && layerName.trim().equalsIgnoreCase("CERTIFICAZIONI ENERGETICHE EDIFICI")){
			infoCened = true;
			String sql = "select foglio, particella from " + tn + " where " + pkField + "=?";
			pst = cnn.prepareStatement(sql);
			pst.setString(1, pk);
			//out.println(sql + " params: pk="+pk);
			rs = pst.executeQuery();
			if (rs.next()){
				foglio = rs.getString("FOGLIO");
				particella = rs.getString("PARTICELLA");
				trovato = true;
				//out.println("Info TROVATI SUB CENED " + trovato);
			}    	  
			//out.println("Info CERTIFICAZIONI ENERGETICHE EDIFICI " + infoCened);
		}
	}
	catch (SQLException e)
	{
		throw e;
	}
	finally
	{
		try { rs.close(); } catch (Exception ex) {}
		try { pst.close(); } catch (Exception ex) {}
		try { cnn.close(); } catch (Exception ex) {}
	}
	        
%>

</head>
<body>
<div id="divInfo">
	<input type="hidden" name="pk" value="<%=pk%>">

</div>
<div id="myhtml">

<% 
if (infoPregeoSuFoglio) {
%>
<form method="post" action="<%=request.getContextPath()%>/viewer/InfoPregeoSuFoglio.jsp" id="myform">
<input type="hidden" name="pk" value="<%=pk%>">
<input type="hidden" name="LAYER" value="<%=layerName%>">

</form>
<% 
}
%>
<% 
if (infoCrifAvgRedditi) {
%>
<form method="post" action="<%=request.getContextPath()%>/viewer/InfoAvgRedFam.jsp" id="myform">
<input type="hidden" name="pk" value="<%=pk%>">
<input type="hidden" name="LAYER" value="<%=layerName%>">

</form>
<% 
}
%>
<% 
if (infoDocfaProt) {
%>
<form method="post" action="<%=request.getContextPath()%>/viewer/InfoDocfaProt.jsp" id="myform">
<input type="hidden" name="pk" value="<%=pk%>">
<input type="hidden" name="LAYER" value="<%=layerName%>">

</form>
<% 
}
%>
<% 
if (infoDocfaSuParticella) {
%>
<form method="post" action="<%=request.getContextPath()%>/viewer/InfoDocfaProt.jsp" id="myform">
<input type="hidden" name="pk" value="<%=pk%>">
<input type="hidden" name="LAYER" value="<%=layerName%>">

</form>
<% 
}
%>
<% 
if (infoZoneCensuarie) {
%>

<form method="post" action="<%=request.getContextPath()%>/viewer/InfoZoneCensuarie.jsp" id="myform">
<input type="hidden" name="pk" value="<%=pk%>">
<input type="hidden" name="LAYER" value="<%=layerName%>">
</form>

<% 
}
%>
<% 
if (infoCened) {
%>

<form method="post" action="<%=request.getContextPath()%>/viewer/InfoCened.jsp" id="myform">
<input type="hidden" name="pk" value="<%=pk%>">
<input type="hidden" name="LAYER" value="<%=layerName%>">
<input type="hidden" name="FOGLIO" value="<%=foglio%>">
<input type="hidden" name="PARTICELLA" value="<%=particella%>">
</form>

<% 
}
%>
<% 
if (infoStatCategorie) {
%>

<form method="post" action="<%=request.getContextPath()%>/viewer/InfoStatCategorie.jsp" id="myform">
<input type="hidden" name="pk" value="<%=pk%>">
<input type="hidden" name="LAYER" value="<%=layerName%>">
</form>

<% 
}
%>
<% 
if (infoFascCivico) {
%>

<form id="formFascCiv" action="<%=request.getContextPath()%>/IndagineCivico" target="_parent" >

	<input type='hidden' name="ST" value="">
	<input type='hidden' name="OGGETTO_SEL" value="">
	<input type='hidden' name="RECORD_SEL" value="">
	<input type='hidden' name="ACT_PAGE" value="">
	<input type='hidden' name="AZIONE" value="">
	<input type='hidden' name="UC" value="111">
	<input type='hidden' name="BACK" value="">
	<input type="hidden" name="BACK_JS_COUNT" value="">

</form>
<% 
}
%>
<% 
if (infoCorrCivico) {
%>
<form id="formRicVelCiv" action="<%=request.getContextPath()%>/FastSearchCivici" target="_parent" >

	<input type='hidden' name="ST" value="">
	<input type='hidden' name="OGGETTO_SEL" value="">
	<input type='hidden' name="RECORD_SEL" value="">
	<input type='hidden' name="AZIONE" value="">
	<input type='hidden' name="UC" value="502">
	<input type='hidden' name="EXT" value="">
	<input type='hidden' name="BACK" value="">
	<input type="hidden" name="BACK_JS_COUNT" value="">

</form>
<% 
}
%>
<% 
if (infoEcogCivico) {
%>

<form id="formEcogCiv" action="<%=request.getContextPath()%>/EcograficoCivici" target="_parent" >

	<input type='hidden' name="ST" value="">
	<input type='hidden' name="OGGETTO_SEL" value="">
	<input type='hidden' name="RECORD_SEL" value="">
	<input type='hidden' name="ACT_PAGE" value="">
	<input type='hidden' name="AZIONE" value="">
	<input type='hidden' name="UC" value="117">
	<input type='hidden' name="BACK" value="">
	<input type="hidden" name="BACK_JS_COUNT" value="">

</form>
<% 
}
%>
</div>
<script type="text/javascript">

var popDati;
function submitFormInPopDati() {
			//alert(popDati);
			var div = popDati.document.getElementById('1');
			div.innerHTML = document.getElementById("myhtml").innerHTML;
			popDati.document.getElementById('myform').submit();
			popDati.focus();
			window.open('', '_self', '');
			window.close();
			
}
function passaForm() {
			var div = popDati.document.getElementById('2');
			div.innerHTML = document.getElementById("divInfo").innerHTML;
			popDati.focus();
}
function passaForm2() {
	
	popDati.focus();
	window.open('', '_self', '');
	window.close();
}
//FUNZIONI CIVICO 
function vaiFascCivico(codice, record_cliccato)
{
	try
	{
		document.getElementById('formFascCiv').OGGETTO_SEL.value = codice;
		document.getElementById('formFascCiv').RECORD_SEL.value = record_cliccato;
		document.getElementById('formFascCiv').ST.value = 33;
		document.getElementById('formFascCiv').submit();
				
	}
	catch (e)
	{
		alert(e);
	}
}
function vaiRicVelCivico(codice, record_cliccato){

		document.getElementById('formRicVelCiv').OGGETTO_SEL.value=codice;
		document.getElementById('formRicVelCiv').RECORD_SEL.value=record_cliccato;
		document.getElementById('formRicVelCiv').ST.value = 3;
		document.getElementById('formRicVelCiv').submit();
	}
<% 
	if (trovato) {
%>
		try
		{
			
			<% if (infoDocfaProt) { %>
				popDati = window.open("<%=request.getContextPath()%>/ewg/blank.jsp","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				setTimeout("submitFormInPopDati()",1200);
			<% } else if (infoCivico) { %>
				// NON PIU USATO vaiFascCivico('<%=request.getParameter("ElemId")%>', '1')
				vaiRicVelCivico('<%=pk_civico_unico%>', '1')
			<% } else if (infoFascCivico) { %>
				vaiFascCivico('<%=request.getParameter("ElemId")%>', '1')
			<% } else if (infoCorrCivico) { %>
				vaiRicVelCivico('<%=pk_civico_unico%>', '1')
			<% } else if (infoFascFabb) { 
				FascFabbAppLogic logic = new FascFabbAppLogic(eu);
				String url = logic.getUrlByFkParCatastali(pk_par_catastali);
				if(url.startsWith("ERR#")){
					url = url.substring(url.indexOf('#')+1); 
			%>
					alert(url);
					
			<%	}else{ 
					String[] s = url.split("#"); %>
					popDati = window.open("<%=s[3]%>","_self");
					
			<%	} 
		
			 } else if (infoCosap) { %> 
				popDati = window.open("<%=request.getContextPath()%>/viewer/InfoCosap.jsp?OGGETTO_SEL=<%=pk%>&LayerName=<%=layerName%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				// aspetto un attimo che la pagina sia caricata
				setTimeout("passaForm2()",1200);
			<% } else if (infoEcogCivico) { %> 
				popDati = window.open("<%=request.getContextPath()%>/viewer/InfoEcogCivici.jsp?OGGETTO_SEL=<%=pk%>&LayerName=<%=layerName%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				// aspetto un attimo che la pagina sia caricata
				setTimeout("passaForm2()",1200);
			<% } else if (infoCrifAvgRedditi) { %> 
				popDati = window.open("<%=request.getContextPath()%>/viewer/InfoAvgRedFam.jsp?OGGETTO_SEL=<%=pk%>&LayerName=<%=layerName%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				// aspetto un attimo che la pagina sia caricata
				setTimeout("passaForm2()",1200);
			<% } else if (infoPregeoSuFoglio) { %> 
				popDati = window.open("<%=request.getContextPath()%>/viewer/InfoPregeoSuFoglio.jsp?OGGETTO_SEL=<%=pk%>&LayerName=<%=layerName%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				// aspetto un attimo che la pagina sia caricata
				setTimeout("passaForm2()",1200);
			<% }else if (infoCened) { %> 
				popDati = window.open("<%=request.getContextPath()%>/viewer/InfoCened.jsp?OGGETTO_SEL=<%=pk%>&LayerName=<%=layerName%>&Foglio=<%=foglio%>&Particella=<%=particella%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				// aspetto un attimo che la pagina sia caricata
				setTimeout("passaForm2()",1200);
			<% }else if (infoStatCategorie) { %> 
				popDati = window.open("<%=request.getContextPath()%>/viewer/InfoStatCategorie.jsp?OGGETTO_SEL=<%=pk%>&LayerName=<%=layerName%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				// aspetto un attimo che la pagina sia caricata
				setTimeout("passaForm2()",1200);
			<% }  else if (infoZoneCensuarie) { %>
				popDati = window.open("<%=request.getContextPath()%>/ewg/blank.jsp","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				setTimeout("submitFormInPopDati()",1200);
			<% } else if (infoCatasto) { %>
				<% if (eu.getEnte().equals("F205")) {%>
					//popDati = window.open("<%=request.getContextPath()%>/viewer/InfoCatasto.jsp?OGGETTO_SEL=<%=pk_particelle%>&LayerName=<%=layerName%>","windowdati",'width=900, height=500, top=0, left=0, scrollbars=yes, resizable=yes');					
					popDati = window.open("<%=request.getContextPath()%>/CatastoGauss?DATASOURCE=jdbc/dbIntegrato&ST=3&UC=12&origine=MAPPA&OGGETTO_SEL=<%=pk_particelle%>&LAYER=<%=layerName%>","windowdati",'width=900, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				<% } else {%>
					//popDati = window.open("<%=request.getContextPath()%>/viewer/InfoCatasto.jsp?OGGETTO_SEL=<%=pk_particelle%>&LayerName=<%=layerName%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
					popDati = window.open("<%=request.getContextPath()%>/CatastoGauss?DATASOURCE=jdbc/dbIntegrato&ST=3&UC=12&origine=MAPPA&OGGETTO_SEL=<%=pk_particelle%>&LAYER=<%=layerName%>","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				<% } %>
				setTimeout("passaForm2()",1200);
			<% } else {%>			
				popDati = window.open("<%=request.getContextPath()%>/ewg/blank.jsp","windowdati",'width=550, height=500, top=0, left=0, scrollbars=yes, resizable=yes');
				// aspetto un attimo che la pagina sia caricata
				setTimeout("submitFormInPopDati()",1200);
			<% } %>
		}
		catch (e)
		{
		 	alert("Errore:"+e.message);
		}
	<% 
	} else {
	%>
		alert('Nella base dati non esistono dati collegati alla selezione effettuata');
		window.open('', '_self', '');
		window.close();
<%
	}
%>
</script>
</body>
</html>



