<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%@page import="it.escsolution.escwebgis.common.EscLogic"%>
<%@page import="java.io.*"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="org.apache.log4j.Logger"%>
<%!static Logger log = Logger.getLogger("diogene.log");%>

<%response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);
response.setHeader("Content-Type", "text/xml");

Context cont= new InitialContext();
DataSource theDataSource = null;
String param = request.getParameter("param");
String[] s = param.split("-");
String ente = s[0];
String idcat = s[1];
EnvSource es = new EnvSource(ente);
theDataSource = (DataSource)cont.lookup(es.getDataSource());
Connection cnn;

String fgl=null;
String par=null;
String sub=null;
String pkana=null;
String pkciv=null;
String idecogciv = null;
if(param.contains("ana"))
	pkana=s[3];
if(param.contains("civ"))
	pkciv=s[3];
if(param.contains("par")){
	fgl=s[3];
	par=s[4];
}
if(param.contains("sub")){
	fgl=s[3];
	par=s[4];
	sub=s[5];
}

%>

<%!
public String scriviPalette(String id, String descr, String rgb)
{
	  String RetVal="";
	  RetVal+="  <Color>";
	  RetVal+="  	<index>"+id+"</index>";
	  RetVal+="  	<rgb>"+rgb+"</rgb>";
	  RetVal+="  </Color>"; 
	  return RetVal;
}
%>

<%!

public boolean isFonteAbilitata(String ente, int codFonte){
	boolean abilitato = false;
	Statement st = null;
	ResultSet rs = null;
	Connection cnn = null;
	
	try {
		Context cont = new InitialContext();
		DataSource theDataSource = null;
		EnvSource es = new EnvSource(ente);
		theDataSource = (DataSource) cont.lookup(es.getDataSource());
		cnn = theDataSource.getConnection();

		st = cnn.createStatement();		
		rs = st.executeQuery("select * from am_fonte_comune fc " +
							 " where  fc.fk_am_fonte = '"+codFonte+"' and fk_am_comune = '"+ente.toUpperCase()+"'");
		
		if (rs.next()) 
			abilitato = true;
		
		//log.info(">>>>>>>>>>>>>>>wms>>>>>>>>>>>>>>>>>>>>>>Fonte " + codFonte + " abilitata per l'ente " + ente + " :" + abilitato);
		
	} catch (SQLException e) {
		log.error("Errore visualizzazione layer da catalogo", e);

	} catch (NamingException e) {
		log.error("Errore connessione", e);

	} finally {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (cnn != null)
				cnn.close();
		} catch (SQLException e) {
		}
	}
	
	return abilitato;
}

%>
	
<%!
public String scriviLayerWms(String ente, Connection Conn, String Descrizione, String Tabella, String ShapeType, String SQLDescr, String IdCat, String PLDecodeDescr, String PLEncode, String IdField, long Storicizzato, String campoCodice, String infoCol, String infoDescr)
{
  Statement ST=null;
  ResultSet RS=null;
  String RetVal="";
  
  try
  {
    ST=Conn.createStatement();
    //aggiungo parametro codut
	StringBuilder b = new StringBuilder(SQLDescr);
	b.replace(SQLDescr.lastIndexOf("FROM"), SQLDescr.lastIndexOf("FROM") + 4, ",codut FROM" );
	SQLDescr = b.toString();
	
	RS=ST.executeQuery(SQLDescr);
    while(RS.next())
    {
      
      String codut = RS.getString("CODUT");
	  RetVal+="  <layer>";
	  RetVal+="    <name>" + RS.getString("DESCRIZIONE").replaceAll(",","\\.").replaceAll("'", " ").replaceAll(">", "Magg.").replaceAll("<", "Min.") + "</name>";
	  if(Storicizzato==1)
	  	RetVal+="    <table>" + Tabella + " s, SITIDECO_CATALOG d</table>";
	  else RetVal+="    <table>" + Tabella + " s</table>";
	  RetVal+="    <shape_field>s.SHAPE</shape_field>";
	  if(Storicizzato==1)
	    RetVal+="    <where>d.codut = s." + campoCodice + " and d.codice = "+ RS.getString("CODICE") +" AND s.DATA_FINE_VAL=TO_DATE('99991231000000', 'YYYYMMDDHH24MISS')</where>";
	  else RetVal+="    <where>s." + campoCodice + " = '"+ codut +"'</where>";
	  
	    if (Descrizione.equalsIgnoreCase("STA04: ZONE CENSUARIE")) {
	   	  RetVal+="    <text_color>7</text_color>";
	   	  RetVal+="    <text_height>10</text_height>";
	   	  RetVal+="    <text_visible>Y</text_visible>";
	   	  RetVal+="    <InfoURL>../infoPart.jsp</InfoURL>";
	    } else if (Descrizione.equalsIgnoreCase("STA03: VALORE MEDIO RENDITA PER VANO")) {
	  	  RetVal+="    <text_label>s.VM_RENDITA_VANO</text_label>";
	  	  RetVal+="    <text_color>7</text_color>";
	  	  RetVal+="    <text_height>10</text_height>";
	  	  RetVal+="    <text_visible>Y</text_visible>";
	    } else if (Descrizione.equalsIgnoreCase("STA01: UNITA ABITATIVE CON DOCFA") ) { 
	  	  RetVal+="    <text_label>s.OCCORRENZE</text_label>";
	  	  RetVal+="    <text_color>7</text_color>";
	  	  RetVal+="    <text_height>10</text_height>";
	  	  RetVal+="    <text_visible>Y</text_visible>";    	
	  	  RetVal+="    <InfoURL>../infoPart.jsp</InfoURL>";
	    } else if (Descrizione.equalsIgnoreCase("STA02: UNITA ABITATIVE CON DOCFA SOTTOCLASSATE") ) { 
	    	  RetVal+="    <text_label>s.OCCORRENZE</text_label>";
	      	  RetVal+="    <text_color>7</text_color>";
	      	  RetVal+="    <text_height>10</text_height>";
	      	  RetVal+="    <text_visible>Y</text_visible>";    	
	      	  RetVal+="    <InfoURL>../infoPart.jsp</InfoURL>";
	    } else if (Descrizione.equalsIgnoreCase("PREGEO PRESENTATI SU FOGLIO") ) { 
	  	  	  RetVal+="    <text_label>s.OCCORRENZE</text_label>";
	    	  RetVal+="    <text_color>7</text_color>";
	    	  RetVal+="    <text_height>10</text_height>";
	    	  RetVal+="    <text_visible>Y</text_visible>";    	
	    	  RetVal+="    <InfoURL>../infoPart.jsp</InfoURL>";
	    }	else {
	    	if(infoCol != null){
		  		RetVal+="    <text_label>'"+ (infoDescr!=null?infoDescr:"") +" ' || s."+ infoCol +"</text_label>";
		  		RetVal+="    <text_visible>Y</text_visible>";
	    	}
		  	RetVal+="    <text_color>7</text_color>";
		  	RetVal+="    <text_height>10</text_height>";
		  	
	  	}
	  RetVal+="    <text_scale_min>1.5</text_scale_min>";
	  RetVal+="    <shape_type>" + ShapeType + "</shape_type>";
	  RetVal+="    <ident>" + IdField + "</ident>";
	  RetVal+="    <on>N</on>";
	  RetVal+="    <always_listed>Y</always_listed>";
	  RetVal+="    <apply_spatial_where>Y</apply_spatial_where>";
	  RetVal+="    <scaled_displist>Y</scaled_displist>";
	  if(ShapeType.equals("0"))
	  {
	    RetVal+="    <line_color>"+ RS.getString("COLORELINEA") +"</line_color>"; 
	    RetVal+="    <poly_fill_color>"+ RS.getString("COLORE") +"</poly_fill_color>";
	    RetVal+="    <poly_fill_style>"+ RS.getString("RIEMPIMENTO") +"</poly_fill_style>";
	  }
	  else{
	    RetVal+="    <line_color>"+ RS.getString("COLORELINEA") +"</line_color>";
	    RetVal+="    <line_thick>12</line_thick>";
	    RetVal+="    <point_style>3</point_style>";
	  }
	  RetVal+="  </layer>";
	  
    }
  }
  catch(SQLException e)
  {
    e.printStackTrace();
  }
  finally
  {
	  try {
		  if (RS!=null)
			  RS.close();
		  if (ST!=null)
			  ST.close();
	  } catch (SQLException e) { 
	  }
  }
  
  return RetVal;
}
%>

<%=
  "<DbMAP_asj_Project>" +
	"<ServerURL>http://"+request.getServerName()+":"+ request.getServerPort()+"/DbMAP_ASJ_servlet/"+ ente +"/DbMAPservlet?function=</ServerURL>" +
		"<InfoURL>http://miami:8888/siticatasto/sitiinfo.jsp</InfoURL>" +
		"<Palette>" +
			"<BackgroundColor>" +
				"<rgb>191 191 191</rgb>" +
			"</BackgroundColor>" +
			"<HiliteColor>" +
				"<rgb>255 0 0</rgb>" +
			"</HiliteColor>" +
			"<HiliteColor2>" +
				"<rgb>150 77 27</rgb>" +
			"</HiliteColor2>"
%>
			
<% 

  cnn = theDataSource.getConnection();
  Statement ST3=null;
  ResultSet RS3=null;
  
  try
  {
    ST3=cnn.createStatement();
    RS3=ST3.executeQuery("SELECT * FROM PGT_SQL_PALETTE");
    while(RS3.next())
    {

%>
<%=scriviPalette(RS3.getString("ID"), RS3.getString("DESCR"), RS3.getString("RGB"))%>
<%     
    }
  }catch(SQLException e)
  {
	  log.error("Errore visualizzazione layer da catalogo",e);

  }
  finally
  {
	  try {
		  if (RS3!=null)
			  RS3.close();
		  if (ST3!=null)
			  ST3.close();
		  if (cnn!=null)
			  cnn.close();
	  } catch (SQLException e) { 
	  }  
  }
%>
			
<%=			"</Palette>" +
			
			"<default_view_fixed>" +
				"<x1>1469402.3321737435</x1>" +
				"<y1>5083109.670447513</y1>" +
				"<x2>1472482.642600353</x2>" +
				"<y2>5084543.457325367</y2>" +
			"</default_view_fixed>"
%>

			<%if(pkana != null || pkciv != null || fgl != null){%>
			<Layer>
				<name>Selezione</name>
				<InfoURL>../infoPart.jsp</InfoURL>
				<%if(pkana != null){%>
				<table>PERSONA_CIVICI_SHAPE_V pcs, sit_d_persona p, SITICIVI c, SITIDSTR s</table>
				<shape_field>c.SHAPE</shape_field>
				<where>p.id_ext = '<%=pkana%>' and p.id = pcs.pk_anagrafe and pcs.pk_siticivi = c.pkid_civi and s.pkid_stra = c.PKID_STRA</where>
				<shape_type>1</shape_type>
				<text_label>s.PREFISSO || ' ' || s.NOME ||' ,' ||c.CIVICO</text_label> 
				<ident>c.PKID_CIVI</ident>
				<%}else if(pkciv != null){%>
				<table>SITICIVI, SITIDSTR</table>
				<shape_field>shape</shape_field>
				<where>pkid_civi = <%=pkciv%> and sitidstr.pkid_stra = SITICIVI.PKID_STRA</where>
				<shape_type>1</shape_type>
				<text_label>SITIDSTR.PREFISSO || ' ' || SITIDSTR.NOME ||' ,' ||SITICIVI.CIVICO</text_label>
				<%}else if(fgl != null){%>
				<table>CAT_PARTICELLE_GAUSS_2</table>
				<shape_field>GEOMETRY</shape_field>
				<%if(par == null){%>
				<where>layer='PARTICELLE' AND FOGLIO = <%=fgl%> and data_fine_val = TO_DATE ('99991231', 'YYYYMMDD')</where>
				<%}%>
				<%if(par != null && sub == null){%>
				<where>layer='PARTICELLE' AND FOGLIO = <%=fgl%> AND PARTICELLA = '<%=par%>' and data_fine_val = TO_DATE ('99991231', 'YYYYMMDD')</where>
				<%}%>
				<%if(par != null && sub != null){%>
				<where>layer='PARTICELLE' AND FOGLIO = <%=fgl%> AND PARTICELLA = '<%=par%>' AND SUB = <%=sub%> and data_fine_val = TO_DATE ('99991231', 'YYYYMMDD')</where>
				<%}%>     
				<shape_type>0</shape_type> 
				<%}%>  
				<always_listed>Y</always_listed>
				<line_color>119</line_color>
				<line_thick>2</line_thick>
				<text_color>119</text_color>
				<text_height>12</text_height>
				<text_flags>0</text_flags>
				<text_font>SansSerif</text_font>
				<point_style>1</point_style>
				<poly_fill_color>119</poly_fill_color>
				<poly_fill_style>1</poly_fill_style>
				<poly_fill_xor>N</poly_fill_xor>
			</Layer>
			<%}%> 
			  
<%=			
			"<Layer>" +
				"<name>Foto</name>" +
				"<table>SITIFOTW</table>" +
				"<shape_field>SHAPE</shape_field>" +
				"<where>*</where>" +
				"<shape_type>4</shape_type>" +
				"<always_listed>Y</always_listed>" +
				"<scaled_displist>Y</scaled_displist>" +
				"<text_label>FILENAME</text_label>" +
				"<text_visible>N</text_visible>" +
				"<text_font>SansSerif</text_font>"  +
				"<poly_fill_color>80</poly_fill_color>" +
				"<poly_fill_style>1</poly_fill_style>" + 
				"<poly_fill_xor>N</poly_fill_xor>"+ 
				"<union_type>ECW</union_type>"+
			"</Layer>" 
%>

			<% if( "I921".equals(ente) ){ %>
					<Layer> 
						<name>Foto 2005</name> 
						<table>SITIFOTW_PATH</table> 
						<shape_field>SHAPE</shape_field> 
						<where>FILENAME like '%062005%'</where> 
						<shape_type>4</shape_type> 
						<always_listed>Y</always_listed> 
						<scaled_displist>Y</scaled_displist> 
						<text_label>FILENAME</text_label> 
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 1997</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%061997%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
			<%}%>

			<% if( "E617".equals(ente) ){ %>
					<Layer> 
						<name>r2</name> 
						<table>SITIFOTW_PATH</table> 
						<shape_field>SHAPE</shape_field> 
						<where>FILENAME like '%r2%'</where> 
						<shape_type>4</shape_type> 
						<always_listed>Y</always_listed> 
						<scaled_displist>Y</scaled_displist> 
						<text_label>FILENAME</text_label> 
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>r10</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%r10%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
			<%}%>

			<% if( "C618".equals(ente) || "C072".equals(ente) || "C208".equals(ente) || "C925".equals(ente) || "G170".equals(ente) || "G869".equals(ente) || "H410".equals(ente) || "H630".equals(ente) || "L380".equals(ente) || "L494".equals(ente) ){ %>
					<Layer> 
						<name>Foto 2007</name> 
						<table>SITIFOTW_PATH</table> 
						<shape_field>SHAPE</shape_field> 
						<where>FILENAME like '%012007%'</where> 
						<shape_type>4</shape_type> 
						<always_listed>Y</always_listed> 
						<scaled_displist>Y</scaled_displist> 
						<text_label>FILENAME</text_label> 
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 2009</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%042009%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
			<%}%>

			<%if("C349".equals(ente)){%>
					<Layer> 
						<name>Foto 2007</name> 
						<table>SITIFOTW_PATH</table> 
						<shape_field>SHAPE</shape_field> 
						<where>FILENAME like '%012007%'</where> 
						<shape_type>4</shape_type> 
						<always_listed>Y</always_listed> 
						<scaled_displist>Y</scaled_displist> 
						<text_label>FILENAME</text_label> 
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto lug2011</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%072011%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto ago2011</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%082011%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
			<%}%>	
			
			<%if("L872".equals(ente)){%>
					<Layer> 
						<name>Foto 2005</name> 
						<table>SITIFOTW_PATH</table> 
						<shape_field>SHAPE</shape_field> 
						<where>FILENAME like '%2005%'</where> 
						<shape_type>4</shape_type> 
						<always_listed>Y</always_listed> 
						<scaled_displist>Y</scaled_displist> 
						<text_label>FILENAME</text_label> 
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 2006</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%2006%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 2007</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%2007%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 2008</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%2008%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>					
					<Layer>
						<name>Foto 2009</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%2009%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 2010</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%2010%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 2011</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%2011%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>
					<Layer>
						<name>Foto 2012</name>
						<table>SITIFOTW_PATH</table>
						<shape_field>SHAPE</shape_field>
						<where>FILENAME like '%2012%'</where>
						<shape_type>4</shape_type>
						<always_listed>Y</always_listed>
						<scaled_displist>Y</scaled_displist>
						<text_label>FILENAME</text_label>
						<text_visible>N</text_visible>
						<text_font>SansSerif</text_font>
						<poly_fill_color>80</poly_fill_color>
						<poly_fill_style>1</poly_fill_style>
						<poly_fill_xor>N</poly_fill_xor>
						<union_type>ECW</union_type>
						<on>N</on>
					</Layer>					
			<%}%>
			
	          <% if( "C361".equals(ente) || "F138".equals(ente) || "D615".equals(ente)){ %>
	            <Layer> 
                  <name>PIANO ASI</name> 
                  <table>SITIFOTW_PATH</table> 
                  <shape_field>SHAPE</shape_field> 
                  <where>FILENAME like '%071990%'</where> 
                  <shape_type>4</shape_type> 
                  <always_listed>Y</always_listed> 
                  <scaled_displist>Y</scaled_displist> 
                  <text_label>FILENAME</text_label> 
                  <text_visible>N</text_visible>
                  <text_font>SansSerif</text_font>
                  <poly_fill_color>80</poly_fill_color>
                  <poly_fill_style>1</poly_fill_style>
                  <poly_fill_xor>N</poly_fill_xor>
                  <union_type>ECW</union_type>
                  <on>N</on>
	            </Layer>
	            <Layer>
                  <name>PIANO ASI-VARIANTE</name>
                  <table>SITIFOTW_PATH</table>
                  <shape_field>SHAPE</shape_field>
                  <where>FILENAME like '%061991%'</where>
                  <shape_type>4</shape_type>
                  <always_listed>Y</always_listed>
                  <scaled_displist>Y</scaled_displist>
                  <text_label>FILENAME</text_label>
                  <text_visible>N</text_visible>
                  <text_font>SansSerif</text_font>
                  <poly_fill_color>80</poly_fill_color>
                  <poly_fill_style>1</poly_fill_style>
                  <poly_fill_xor>N</poly_fill_xor>
                  <union_type>ECW</union_type>
                  <on>N</on>
	            </Layer>
			  <%}%>
	
	          <% if( "A717".equals(ente)){ %>
	            <Layer> 
                  <name>PIANO ASI 1</name> 
                  <table>SITIFOTW_PATH</table> 
                  <shape_field>SHAPE</shape_field> 
                  <where>FILENAME like '%200403_1%'</where> 
                  <shape_type>4</shape_type> 
                  <always_listed>Y</always_listed> 
                  <scaled_displist>Y</scaled_displist> 
                  <text_label>FILENAME</text_label> 
                  <text_visible>N</text_visible>
                  <text_font>SansSerif</text_font>
                  <poly_fill_color>80</poly_fill_color>
                  <poly_fill_style>1</poly_fill_style>
                  <poly_fill_xor>N</poly_fill_xor>
                  <union_type>ECW</union_type>
                  <on>N</on>
	            </Layer>
	            <Layer>
                  <name>PIANO ASI 2</name>
                  <table>SITIFOTW_PATH</table>
                  <shape_field>SHAPE</shape_field>
                  <where>FILENAME like '%200403_2%'</where>
                  <shape_type>4</shape_type>
                  <always_listed>Y</always_listed>
                  <scaled_displist>Y</scaled_displist>
                  <text_label>FILENAME</text_label>
                  <text_visible>N</text_visible>
                  <text_font>SansSerif</text_font>
                  <poly_fill_color>80</poly_fill_color>
                  <poly_fill_style>1</poly_fill_style>
                  <poly_fill_xor>N</poly_fill_xor>
                  <union_type>ECW</union_type>
                  <on>N</on>
	            </Layer>
			<%}%>
	
	
	          <% if( "H703".equals(ente)){ %>
	            <Layer> 
                  <name>PIANO ASI 2</name> 
                  <table>SITIFOTW_PATH</table> 
                  <shape_field>SHAPE</shape_field> 
                  <where>FILENAME like '%200001_1%'</where> 
                  <shape_type>4</shape_type> 
                  <always_listed>Y</always_listed> 
                  <scaled_displist>Y</scaled_displist> 
                  <text_label>FILENAME</text_label> 
                  <text_visible>N</text_visible>
                  <text_font>SansSerif</text_font>
                  <poly_fill_color>80</poly_fill_color>
                  <poly_fill_style>1</poly_fill_style>
                  <poly_fill_xor>N</poly_fill_xor>
                  <union_type>ECW</union_type>
                  <on>N</on>
	            </Layer>
	            <Layer>
                  <name>PIANO ASI 3</name>
                  <table>SITIFOTW_PATH</table>
                  <shape_field>SHAPE</shape_field>
                  <where>FILENAME like '%200001_2%'</where>
                  <shape_type>4</shape_type>
                  <always_listed>Y</always_listed>
                  <scaled_displist>Y</scaled_displist>
                  <text_label>FILENAME</text_label>
                  <text_visible>N</text_visible>
                  <text_font>SansSerif</text_font>
                  <poly_fill_color>80</poly_fill_color>
                  <poly_fill_style>1</poly_fill_style>
                  <poly_fill_xor>N</poly_fill_xor>
                  <union_type>ECW</union_type>
                  <on>N</on>
	            </Layer>
	            <Layer>
                  <name>TERMOVALORIZZATORE</name>
                  <table>SITIFOTW_PATH</table>
                  <shape_field>SHAPE</shape_field>
                  <where>FILENAME like '%200001_3%'</where>
                  <shape_type>4</shape_type>
                  <always_listed>Y</always_listed>
                  <scaled_displist>Y</scaled_displist>
                  <text_label>FILENAME</text_label>
                  <text_visible>N</text_visible>
                  <text_font>SansSerif</text_font>
                  <poly_fill_color>80</poly_fill_color>
                  <poly_fill_style>1</poly_fill_style>
                  <poly_fill_xor>N</poly_fill_xor>
                  <union_type>ECW</union_type>
                  <on>N</on>
	            </Layer>
			<%}%>
			
			<%boolean ecograficoAbilitato = this.isFonteAbilitata(ente, 29);
			if(ecograficoAbilitato){%>
				
			  <Layer>
				<name>Civici Ecografico</name>
				<InfoURL>../infoPart.jsp</InfoURL>
				<table>EC_TOP_CIVICI B</table>
				<shape_field>SHAPE</shape_field>
				<where>1=1</where>
				<shape_type>1</shape_type>
				<text_label>DESCR_STRADA || ', ' || DESCRIZIONE_CIVICO</text_label> 
				<always_listed>Y</always_listed>
				<line_color>3</line_color>
				<line_thick>9</line_thick>
				<text_color>3</text_color>
				<text_height>12</text_height>
				<text_flags>0</text_flags>
				<text_scale_min>1.5</text_scale_min>
				<text_font>SansSerif</text_font>
				<point_style>3</point_style>
				<poly_fill_color>3</poly_fill_color>
				<poly_fill_style>1</poly_fill_style>
				<poly_fill_xor>N</poly_fill_xor>
			</Layer>
			<Layer>
				<name>Strade Ecografico</name>
				<InfoURL>../infoPart.jsp</InfoURL>
				<table>EC_TOP_TRATTI_STRADALI B</table>
				<shape_field>B.SHAPE</shape_field>
				<where>1=1</where>
				<shape_type>1</shape_type>
				<text_label></text_label> 
				<always_listed>Y</always_listed>
				<line_color>5</line_color>
				<line_thick>9</line_thick>
				<text_color>3</text_color>
				<text_height>12</text_height>
				<text_flags>0</text_flags>
				<text_font>SansSerif</text_font>
				<point_style>3</point_style>
				<poly_fill_color>5</poly_fill_color>
				<poly_fill_style>1</poly_fill_style>
				<poly_fill_xor>N</poly_fill_xor>
			</Layer>
			<Layer>
				<name>Fabbricati Ecografico</name>
				<InfoURL>../infoPart.jsp</InfoURL>
				<table>EC_FAB_FABBRICATI B</table>
				<shape_field>B.SHAPE</shape_field>
				<where>1=1</where>
				<shape_type>1</shape_type>
				<ident>ID</ident>
				<always_listed>Y</always_listed>
				<scaled_displist>Y</scaled_displist>
				<text_label></text_label> 
				<line_color>1</line_color>
				<line_thick>9</line_thick>
				<text_color>3</text_color>
				<text_height>12</text_height>
				<text_flags>0</text_flags>
				<text_font>SansSerif</text_font>
				<text_scale_min>1.5</text_scale_min>
				<point_style>3</point_style>
				<poly_fill_color>1</poly_fill_color>
				<poly_fill_style>1</poly_fill_style>
				<poly_fill_xor>N</poly_fill_xor>
			</Layer>	    
			
			<%}%>
			
<%=			

		"<Layer>" +
				"<name>Fogli</name>" +
				"<InfoURL>../infoFogli.jsp</InfoURL>" +
				"<table>SITIFGLC B, SITICOMU C</table>" +
				"<shape_field>B.SHAPE</shape_field>" +
				"<where>B.COD_NAZIONALE = C.COD_NAZIONALE AND C.CODI_FISC_LUNA in (select uk_belfiore from ewg_tab_comuni)</where>" +
				"<shape_type>0</shape_type>" +
				"<always_listed>Y</always_listed>" +
				"<scaled_displist>Y</scaled_displist>" +
				"<line_color>5</line_color>" +
				"<line_thick>2</line_thick>" +
				"<text_label>B.FOGLIO</text_label>" +
				"<text_scale_min>0.5</text_scale_min>"+
				"<text_color>1</text_color>" +
				"<text_height>30</text_height>" +
				"<text_visible>Y</text_visible>" +
				"<text_font>SansSerif</text_font>" +
			"</Layer>" +
			"<Layer>" +
				"<name>Particelle</name>" +
				"<InfoURL>../infoPart.jsp</InfoURL>" +
				"<table>CAT_PARTICELLE_GAUSS_2</table>" +
				"<shape_field>GEOMETRY</shape_field>" +
				"<where>COMUNE in (select COD_NAZIONALE from sit_ente E , SITICOMU C  WHERE E.CODENT = C.CODI_FISC_LUNA ) and layer='PARTICELLE' and data_fine_val = TO_DATE ('99991231', 'YYYYMMDD')</where>" +
				"<shape_type>0</shape_type>" +
				"<ident>PK_PARTICELLE</ident>" +
				"<scale_min>0.42237384377543336</scale_min>" +
				"<always_listed>Y</always_listed>" +
				"<line_color>7</line_color>" +
				"<line_thick>1</line_thick>" +
				"<text_label>PARTICELLA</text_label>" +
				"<text_scale_min>1.5</text_scale_min>"+
				"<text_color>7</text_color>" +
				"<text_height>15</text_height>" +
				"<text_visible>Y</text_visible>" +
				"<text_font>SansSerif</text_font>" +
				"<poly_fill_color>11</poly_fill_color>" +
				"<poly_fill_style>8</poly_fill_style>" +
				"<poly_fill_xor>N</poly_fill_xor>" +
				"<predefined_search>" +
					"<name>FoglioParticella</name>" +
					"<description>Ricerca foglio / particella</description>" +
					"<whereTemplate>FOGLIO ='_x_' and PARTICELLA = '_y_'</whereTemplate>" +
					"<predefined_search_param>" +
						"<name>_x_</name>" +
						"<description>Foglio</description>" +
						"<prompt>Immettere Foglio</prompt>" +
					"</predefined_search_param>" +
					"<predefined_search_param>" +
						"<name>_y_</name>" +
						"<description>Particella</description>" +
						"<prompt>Immettere Particella</prompt>" +
					"</predefined_search_param>" +
				"</predefined_search>" +
			"</Layer>" +
			"<Layer>" +
				"<name>Fabbricati</name>" +
				"<InfoURL>../infoPart.jsp</InfoURL>" +
				"<table>CAT_PARTICELLE_GAUSS_2</table>" +
				"<shape_field>GEOMETRY</shape_field>" +
				"<where>COMUNE in (select COD_NAZIONALE from sit_ente E , SITICOMU C  WHERE E.CODENT = C.CODI_FISC_LUNA ) and layer='FABBRICATI' and data_fine_val = TO_DATE ('99991231', 'YYYYMMDD')</where>" +
				"<shape_type>0</shape_type>" +
				"<ident>PK_PARTICELLE</ident>" +
				"<scale_min>0.42237384377543336</scale_min>" +
				"<always_listed>Y</always_listed>" +
				"<line_color>7</line_color>" +
				"<line_thick>1</line_thick>" +
				"<text_label>'Foglio ' || FOGLIO || ' part. ' || PARTICELLA</text_label>" +
				"<text_color>1</text_color>" +
				"<text_height>10</text_height>" +
				"<text_visible>N</text_visible>" +
				"<text_font>SansSerif</text_font>" +
				"<poly_fill_color>51</poly_fill_color>" +
				"<poly_fill_style>1</poly_fill_style>" +
				"<poly_fill_xor>N</poly_fill_xor>" +
			"</Layer>" +
			"<Layer>" +
				"<name>Civici</name>" +
				"<InfoURL>../infoPart.jsp</InfoURL>" +
				"<table>(SELECT SITICIVI.COD_NAZIONALE, SITICIVI.DATA_FINE_VAL, PKID_CIVI, SHAPE,SITIDSTR.PREFISSO || ' ' || SITIDSTR.NOME VIA, siticivi.civico  FROM SITICIVI, SITIDSTR, SITICOMU WHERE  sitidstr.pkid_stra = SITICIVI.PKID_STRA and SITICIVI.COD_NAZIONALE = SITICOMU.COD_NAZIONALE AND SITICOMU.CODI_FISC_LUNA in (select uk_belfiore from ewg_tab_comuni) and siticivi.DATA_FINE_VAL =to_date('31/12/9999','dd/mm/yyyy'))</table>" +
				"<shape_field>SHAPE</shape_field>" +
				"<where>1=1</where>" +
				"<shape_type>1</shape_type>" +
				"<ident>PKID_CIVI</ident>" +
				"<scale_min>1.5</scale_min>" +
				"<always_listed>Y</always_listed>" +
				"<scaled_displist>Y</scaled_displist>" +
				"<line_color>7</line_color>" +
				"<line_thick>9</line_thick>" +
				"<text_label>via ||' ,' ||CIVICO</text_label>" +
				"<text_color>143</text_color>" +
				"<text_height>12</text_height>" +
				"<text_flags>0</text_flags>" +
				"<text_font>SansSerif</text_font>" +
				"<text_scale_min>1.5</text_scale_min>" +
				"<point_style>3</point_style>" +
				"<poly_fill_color>7</poly_fill_color>" +
				"<poly_fill_style>1</poly_fill_style>" +
				"<poly_fill_xor>N</poly_fill_xor>" +
			"</Layer>"
%>

<% 

  cnn = theDataSource.getConnection();

  Statement ST=null;
  ResultSet RS=null;
  Statement ST2=null;
  ResultSet RS2=null;
  
  try
  {
    ST=cnn.createStatement();
    //RS=ST.executeQuery("SELECT ID_FIELD, TABELLA, DESCRIZIONE, SQLDESCR, SHAPE_TYPE, SQLTOOLTIPS, PLDECODE_DESCR, PLENCODE, STORICIZZATO,ID_CAT, CODE_FIELD FROM SITICATALOG WHERE ID_CAT = "+ idcat +" order by DESCRIZIONE");
    RS=ST.executeQuery("SELECT * FROM (" +
    		"SELECT   id_field, tabella, descrizione, sqldescr, shape_type, sqltooltips,pldecode_descr, plencode, storicizzato, id_cat, code_field, null as infocol, null as infodescr " +
    		    "FROM siticatalog WHERE ID_CAT = "+ idcat + " " +
    		"UNION " +
    		"SELECT   name_col_id, name_table, des_layer, sql_deco, shape_type, null,pldecode_descr, plencode, 0, id, name_col_tema, name_col_info, descr_col_info " +
    		    "FROM pgt_sql_layer " +
    		    "WHERE flg_publish = 'Y' AND id = "+ idcat + ")" +
    		" ORDER BY DESCRIZIONE");
    while(RS.next())
    {
        String ShapeType="0";
        if(RS.getString("SHAPE_TYPE").equalsIgnoreCase("LINE"))
          ShapeType="3";
        else if(RS.getString("SHAPE_TYPE").equalsIgnoreCase("POINT"))
          ShapeType="1";

%>

<%=scriviLayerWms(ente, cnn, RS.getString("DESCRIZIONE"), RS.getString("TABELLA"), ShapeType, RS.getString("SQLDESCR"), RS.getString("ID_CAT"), RS.getString("PLDECODE_DESCR"), RS.getString("PLENCODE"), RS.getString("ID_FIELD"), RS.getLong("STORICIZZATO"), RS.getString("CODE_FIELD"), RS.getString("INFOCOL"), RS.getString("INFODESCR"))%>

<%
    }
%>

<%="</DbMAP_asj_Project>"%>

<%     
  }
  catch(SQLException e)
  {
	  log.error("Errore visualizzazione layer da catalogo",e);

  }
  finally
  {
	  try {
		  if (RS!=null)
			  RS.close();
		  if (ST!=null)
			  ST.close();
		  if (RS2!=null)
			  RS2.close();
		  if (ST2!=null)
			  ST2.close();
		  if (cnn!=null)
			  cnn.close();
	  } catch (SQLException e) { 
	  }  
  }
%>