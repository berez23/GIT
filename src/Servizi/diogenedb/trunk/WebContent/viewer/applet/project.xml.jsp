<%response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);


String cn=request.getParameter("cn")!=null&&!request.getParameter("cn").trim().equals("")?request.getParameter("cn"):null;
String fgl=request.getParameter("fgl")!=null&&!request.getParameter("fgl").trim().equals("")?request.getParameter("fgl"):null;
String par=request.getParameter("par")!=null&&!request.getParameter("par").trim().equals("")?request.getParameter("par"):null;
String sub=request.getParameter("sub")!=null&&!request.getParameter("sub").trim().equals("")?request.getParameter("sub"):null;
String pkana=request.getParameter("pkana")!=null&&!request.getParameter("pkana").trim().equals("")?request.getParameter("pkana"):null;
String pkciv=request.getParameter("pkciv")!=null&&!request.getParameter("pkciv").trim().equals("")?request.getParameter("pkciv"):null;



%>

<?xml version="1.0" encoding="UTF-8"?>
<!-- Created by DbMAP ASJ Viewer/Author by ABACO s.r.l. (http://www.abacogroup.com) -->

<DbMAP_asj_Project>

	<ServerURL><%="http://"+request.getServerName()+":"+ request.getServerPort()%>/DbMAP_ASJ_servlet<%=request.getContextPath()%>/DbMAPservlet?function=</ServerURL>
	<InfoURL><%="http://"+request.getServerName()+":"+ request.getServerPort()%>/DbMAP_ASJ_servlet<%=request.getContextPath()%>/DbMAPinfo</InfoURL>

	<Palette>
		<BackgroundColor>
			<rgb>255 255 255</rgb>
		</BackgroundColor>
		<HiliteColor>
			<rgb>255 255 0</rgb>
		</HiliteColor>
		<HiliteColor2>
			<rgb>150 77 27</rgb>
		</HiliteColor2>
	</Palette>


	<default_view_computed>
<%if(fgl != null){%>
	<table>DUAL</table> 
	  <shape_field>INGOMBRO_PARTICELLA(<%=cn==null?"NULL":"'"+cn+"'"%>,<%=fgl==null?"NULL":fgl%>,<%=par==null?"NULL":"'"+par+"'"%>,<%=sub==null?"NULL":"'"+sub+"'"%>,NULL)</shape_field> 
	  <where /> 
	 <!--  <zoom_factor_max>2.2</zoom_factor_max> -->
<%}else{ %>
		<table>SITICOML S</table>
		<shape_field>SHAPE</shape_field>
		<where>LPAD(S.ISTATP,3,'0') || LPAD(S.ISTATC,3,0) = '015146'</where>
 <%}%> 
	
  </default_view_computed>
	<Layer>
		<name>Foto</name>
		<table>SITIFOTW</table>
		<shape_field>SHAPE</shape_field>
		<where>*</where>
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
	</Layer>

	<Layer>
		<name>Fogli</name>
		
		<table>SITIFGLC B</table>
		<shape_field>B.SHAPE</shape_field>
		<where></where>
		<shape_type>0</shape_type>
		<always_listed>Y</always_listed>
		<scaled_displist>Y</scaled_displist>
		<line_color>160</line_color>
		<line_thick>2</line_thick>
		<text_label>B.FOGLIO</text_label>
		<text_color>160</text_color>
		<text_height>10</text_height>
		<text_visible>N</text_visible>
		<text_font>SansSerif</text_font>
	</Layer>

	<Layer>
		<name>Particelle</name>
		
		<table>CAT_PARTICELLE_GAUSS</table>
		<shape_field>GEOMETRY</shape_field>
		<where>layer='PARTICELLE'</where>
		<shape_type>0</shape_type>
		<ident>PK_PARTICELLE</ident>
		<scale_min>0.42237384377543336</scale_min>
		<always_listed>Y</always_listed>
		<line_color>7</line_color>
		<line_thick>1</line_thick>
		<text_label>'Foglio ' || foglio || ' part. ' || PARTICELLA</text_label>
		<text_color>1</text_color>
		<text_height>10</text_height>
		<text_visible>N</text_visible>
		<text_font>SansSerif</text_font>		
		<poly_fill_color>11</poly_fill_color>
 		<poly_fill_style>1</poly_fill_style>
		<poly_fill_xor>N</poly_fill_xor>
		<predefined_search>
			<name>FoglioParticella</name>
			<description>Ricerca foglio / particella</description>
			<whereTemplate>FOGLIO ='_x_' and PARTICELLA = '_y_'</whereTemplate>
			<predefined_search_param>
				<name>_x_</name>
				<description>Foglio</description>
				<prompt>Immettere Foglio</prompt>
			</predefined_search_param>
			<predefined_search_param>
				<name>_y_</name>
				<description>Particella</description>
				<prompt>Immettere Particella</prompt>
			</predefined_search_param>
		</predefined_search>
	</Layer>

	<Layer>
		<name>Fabbricati</name>
		<InfoURL>SaltaInMappa.jsp</InfoURL>
		<table>CAT_PARTICELLE_GAUSS</table>
		<shape_field>GEOMETRY</shape_field>
		<where>layer='FABBRICATI'</where>
		<shape_type>0</shape_type>
		<ident>PK_PARTICELLE</ident>
		<scale_min>0.42237384377543336</scale_min>
		<always_listed>Y</always_listed>
		<line_color>7</line_color>
		<line_thick>1</line_thick>
		<text_label>'Foglio ' || foglio || ' part. ' || PARTICELLA</text_label>
		<text_color>1</text_color>
		<text_height>10</text_height>
		<text_visible>N</text_visible>
		<text_font>SansSerif</text_font>
		<poly_fill_color>41</poly_fill_color>
		<poly_fill_style>1</poly_fill_style>
		<poly_fill_xor>N</poly_fill_xor>
	</Layer>

	<Layer>
		<name>Civici</name>
		
		<table>(SELECT SITICIVI.COD_NAZIONALE, SITICIVI.DATA_FINE_VAL, PKID_CIVI, SHAPE,SITIDSTR.PREFISSO || ' ' || SITIDSTR.NOME VIA, siticivi.civico  FROM SITICIVI, SITIDSTR WHERE  sitidstr.pkid_stra = SITICIVI.PKID_STRA)</table>
		<shape_field>SHAPE</shape_field>
		<where>DATA_FINE_VAL =to_date('31/12/9999','dd/mm/yyyy')</where>
		<shape_type>1</shape_type>
		<ident>PKID_CIVI</ident>
		<scale_min>1.5</scale_min>
		<always_listed>Y</always_listed>
		<line_color>151</line_color>
		<line_thick>1</line_thick>
		<text_label>via ||' ,' ||CIVICO</text_label>
		<text_color>2</text_color>
		<text_height>12</text_height>
		<text_flags>0</text_flags>
		<text_font>SansSerif</text_font>
		<text_scale_min>1.5</text_scale_min>
		<point_style>-1</point_style>
		<point_image>gifs\red_point.gif</point_image>
		<point_image_thumbnail></point_image_thumbnail>
		<poly_fill_color>133</poly_fill_color>
		<poly_fill_style>1</poly_fill_style>
		<poly_fill_xor>N</poly_fill_xor>
	</Layer>

<%if(fgl != null)
{
	
	
	String wherepartcor = "COMUNE='"+cn+"' AND LPAD(TRIM(FOGLIO),6,'0')=LPAD(TRIM('"+fgl+"'),6,'0') ";
	if(par != null) 
		wherepartcor += " and LPAD(TRIM(PARTICELLA),5,'0')=LPAD(TRIM('"+par+"'),5,'0') ";
	wherepartcor += " and layer='PARTICELLE'";
			%>
				<Layer>
				  <name>Particella Corrente</name> 
				  <table>CAT_PARTICELLE_GAUSS</table>
				  <shape_field>GEOMETRY</shape_field>
					<where><%=wherepartcor%></where>
			
					<shape_type>0</shape_type>
					<ident>PK_PARTICELLE</ident>
					<scale_min>0.42237384377543336</scale_min>
					<always_listed>Y</always_listed>
					<line_color>1</line_color>
					<line_thick>1</line_thick>
					<text_label>'Foglio ' || foglio || ' part. ' || PARTICELLA</text_label>
					<text_color>2</text_color>
					<text_height>10</text_height>
					<text_visible>Y</text_visible>
					<text_font>SansSerif</text_font>
					<poly_fill_color>1</poly_fill_color>
					<poly_fill_style>8</poly_fill_style>
					<poly_fill_xor>N</poly_fill_xor>
				    
				  </Layer>
	 	  	
<%}%>

<%if(pkciv != null){%>
	<Layer>
		<name>Civico Selezionato</name>
		
		<table>(SELECT SITICIVI.COD_NAZIONALE, SITICIVI.DATA_FINE_VAL, PKID_CIVI, SHAPE,SITIDSTR.PREFISSO || ' ' || SITIDSTR.NOME VIA, siticivi.civico  FROM SITICIVI, SITIDSTR WHERE  sitidstr.pkid_stra = SITICIVI.PKID_STRA)</table>
		<shape_field>SHAPE</shape_field>
		<where>DATA_FINE_VAL =to_date('31/12/9999','dd/mm/yyyy') and PKID_CIVI = <%=pkciv %></where>
		<shape_type>1</shape_type>
		<ident>PKID_CIVI</ident>
		<scale_min>1.5</scale_min>
		<always_listed>Y</always_listed>
		<line_color>90</line_color>
		<line_thick>1</line_thick>
		<poly_fill_color>90</poly_fill_color>
		<poly_fill_style>7</poly_fill_style>
		<poly_fill_xor>N</poly_fill_xor>
	</Layer>
<%}%>
</DbMAP_asj_Project>
