<%@ page language="java"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<%@page import="javax.naming.Context"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.webred.cet.permission.CeTUser"%>
<html>
<head>
<title>Informazioni Sul Civico</title>
<LINK rel="stylesheet"
	href="<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/styles/style.css"
	type="text/css">
<script src="../ewg/Scripts/dynamic.js" language="JavaScript"></script>

<script type="text/javascript">
function openView(percorso){
	nuova=window.open(percorso,"windowdati","width=550, height=500, statusbar=no,scrollbars=no");
	nuova.focus();
}

</script>

<%
    HttpSession sessione= request.getSession();
	String ricerca = ""; 
	ricerca = request.getParameter("ricerca");
    String pk = request.getParameter("pk");
    if (pk== null || pk.equals(""))
    	pk= (String)sessione.getAttribute("pk");
	String keystr="";
	String queryForKeyStr = null;
	boolean trovato = false;

	
	
	
	if (ricerca!=null && ricerca.equalsIgnoreCase("Civici")) {
		Context cont= new InitialContext();
		DataSource theDataSource = null;
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource = (DataSource)cont.lookup(es.getDataSource());
		Connection cnn = theDataSource.getConnection();
		  
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
				String query = "SELECT DISTINCT uk_civico FROM vista_civici WHERE pk_sequ_civico = ?";
				System.out.println("Eseguo "+ query);
				System.out.println("Parametro "+ pk);
				pst = cnn.prepareStatement(query);
				pst.setString(1, pk);
				rs = pst.executeQuery();
				if (rs.next())
				{
					
					keystr = rs.getString(1);
					trovato = true;
					sessione.setAttribute("pk", pk);
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
		
		
	}
	
	if (ricerca!=null && ricerca.equalsIgnoreCase("Anagrafe")) {
		
		Context cont= new InitialContext();
		DataSource theDataSource = null;
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		theDataSource =(DataSource)cont.lookup(es.getDataSourceIntegrato());
		Connection cnn = theDataSource.getConnection();
		  
		PreparedStatement pst = null;
		ResultSet rs = null;
		String pk_particelle = "";

		try
		{
				String q = "SELECT pk_anagrafe FROM persona_civici_shape_v WHERE pk_siticivi = ";
				String query = q + "?";
				System.out.println("Eseguo "+ query);
				System.out.println("Parametro "+ pk);
				pst = cnn.prepareStatement(query);
				pst.setString(1, pk);
				rs = pst.executeQuery();
				int contaChiavi = 0;
				if (rs.next())
				{
					
					keystr = rs.getString(1);
					while (rs.next()) {
						contaChiavi++;
						keystr += "," + rs.getString(1);
					}
					trovato = true;
					sessione.setAttribute("pk", pk);
				}
				//if (contaChiavi>999) 
					queryForKeyStr = q + "" + pk + "";
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
		
		
	}
	
	
%>

</head>

<body>


		
		<span class="extWinTXTTitle">

		</span>
		<table width="100%" class="extWinTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="extWinTDData" ><span class="extWinTXTData">Informazioni sul Civico</span>
							<div id=1>
								<form method="post" action="" id="myform">
								<% if (ricerca!=null && ricerca.equalsIgnoreCase("Civici")) {%>
						        <input type="radio" name="ricerca" checked value="Civici" />Dati Toponomastici 
						        &nbsp;<a href="javascript:openView('<%=request.getContextPath()%>/ToponomasticaCivici?DATASOURCE=jdbc/dbintegrato&ST=3&UC=11&origine=MAPPA&OGGETTO_SEL=<%=keystr%>')">Civici</a>
						        <br />
						        <%} else {%>
						        <input type="radio" name="ricerca"  value="Civici" />Dati Toponomastici<br />
						        <%} %>
						        <% if (ricerca!=null && ricerca.equalsIgnoreCase("Anagrafe")) {%>
						        <input type="radio" name="ricerca" checked value="Anagrafe" />Abitanti
						        &nbsp;<a href="javascript:openView('<%=request.getContextPath()%>/AnagrafeDwh?DATASOURCE=jdbc/diogene&ST=2&UC=45&origine=MAPPA&queryForKeyStr=SELECT pk_anagrafe FROM persona_civici_shape_v WHERE pk_siticivi =<%=pk%>')">Anagrafe</a>
						        <br/>
						        <%} else {%>
						        <input type="radio" name="ricerca"  value="Anagrafe" />Abitanti<br/>
						        <% }%> 
						        <input type="hidden" name="origine" value="<%=request.getParameter("origine") %>">
						       
						        
						        
									<div id=2>
										
									</div>
								<input type="submit" value="Conferma">
								
								
								
								</form>
								
							</div>			
				</td>
			</tr>
		</table>

<script type="text/javascript">

var popD;
function submitPopDati() {
			var div = popD.document.getElementById('1');
			div.innerHTML = document.getElementById("3").innerHTML;
			popD.document.getElementById('infoForm').submit();
			popD.focus();
			window.close();
}



<% 
	if (trovato) {
%>
		try
		{
				//popD = window.open("<%=request.getContextPath()%>/ewg/blank.jsp","windowInfo",'width=550, height=500, top=0, left=0, scrollbars=yes');
				//popD = window.open("<%=request.getContextPath()%>/viewer/InfoCivico.jsp","windowInfo",'width=550, height=500, top=0, left=0, scrollbars=yes');

				// aspetto un attimo che la pagina sia caricata
				//setTimeout("submitPopDati()",1200);
		}
		catch (e)
		{
		 	alert("Errore:"+e.message);
		}
	<% 
	} 
	if (ricerca!=null && !trovato) {
	%>
		alert('Nella base dati non esistono dati collegati alla selezione effettuata');
		window.close();
<%
	}
%>
</script>

</body>

</html>
