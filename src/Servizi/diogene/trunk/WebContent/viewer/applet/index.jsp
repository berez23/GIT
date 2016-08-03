<%response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);

String cn=request.getParameter("cn")!=null&&!request.getParameter("cn").trim().equals("")?request.getParameter("cn"):null;
String fgl=request.getParameter("fgl")!=null&&!request.getParameter("fgl").trim().equals("")?request.getParameter("fgl"):null;
String par=request.getParameter("par")!=null&&!request.getParameter("par").trim().equals("")?request.getParameter("par"):null;
String sub=request.getParameter("sub")!=null&&!request.getParameter("sub").trim().equals("")?request.getParameter("sub"):null;
String pkana=request.getParameter("pkana")!=null&&!request.getParameter("pkana").trim().equals("")?request.getParameter("pkana"):null;
String pkciv=request.getParameter("pkciv")!=null&&!request.getParameter("pkciv").trim().equals("")?request.getParameter("pkciv"):null;

String qry_stringa = "";
if(cn != null) qry_stringa += "&cn="+URLEncoder.encode(cn,"UTF-8");
if(fgl != null) qry_stringa += "&fgl="+ URLEncoder.encode(fgl,"UTF-8") ;
if(par != null) qry_stringa += "&par="+ URLEncoder.encode(par,"UTF-8") ;
if(sub != null) qry_stringa += "&sub="+ URLEncoder.encode(sub,"UTF-8") ;
if(pkana != null) qry_stringa += "&pkana="+URLEncoder.encode(pkana,"UTF-8");
if(pkciv != null) qry_stringa += "&pkciv="+URLEncoder.encode(pkciv,"UTF-8");
if(qry_stringa.length()>1) qry_stringa = "?"+qry_stringa.substring(1);

%>
<%@page import="java.net.URLEncoder"%>
<html>

<head>
<meta http-equiv="Content-Language" content="it">

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Visualizzazione cartografica</title>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<div align="center">
  <center>

<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
  <!-- <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr>
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td height="5" bgcolor="#C0C0C0" align="center">
    <img border="0" src="logo.gif" width="141" height="70"></td>
    <td height="5" width="5" bgcolor="#808080">&nbsp;</td>
    <td height="6" bgcolor="#C0C0C0" align="center"><b>Data Viewer</b><br>
    <b>Visualizzazione cartografica</b></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr>
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr> -->
  <tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td bgcolor="#C0C0C0" rowspan="2" width="25%">
    <applet code="DbMAP.satellites.sitapplet_layers.class" codebase="./" archive="DbMAP_ASJ_applets.jar" width="100%" height="100%" name="layers">
              <param name="backcolor" value="192 192 192">
              <param name="forecolor" value="0 0 0">
        </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td bgcolor="#C0C0C0" width="75%" height="100%">
    <applet code="DbMAP.sitapplet_main.class" codebase="./" archive="DbMAP_ASJ_applets.jar" width="100%" height="100%" name="miapp">
              <param name="satelliteapplet1" value="status1">
              <param name="satelliteapplet2" value="status2">
              <param name="satelliteapplet3" value="layers">
              <param name="satelliteapplet4" value="command">
              <param name="satelliteapplet5" value="scale">
              <param name="satelliteapplet6" value="selector">
              <!-- param name="satelliteapplet7" value="browser" -->
              <param name="satelliteapplet7" value="editor">
              <param name="project" value="project.xml.jsp<%=qry_stringa%>">
        </applet></td>
      <td width="5" bgcolor="#808080">&nbsp;</td>
  </tr>
  <tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.sitapplet_scalimetro.class" codebase="./" name="scale" width="100%" height="20" archive="DbMAP_ASJ_applets.jar">
              <param name="backcolor" value="255 255 255">
              <param name="forecolor" value="0 0 0">
            </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
  </tr>
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr>
  <tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td align="center" bgcolor="#C0C0C0"><applet code="DbMAP.satellites.sitapplet_editor.class" codebase="./" archive="DbMAP_ASJ_applets.jar" width="84" height="24" name="editor">
			<param name="backcolor" value="192 192 192" />
            <param name="forecolor" value="0 0 0">
			<param name="appletReference" value="window.opener.document.editor" />
		</applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td align="center" bgcolor="#C0C0C0"><applet code="DbMAP.satellites.sitapplet_command.class" codebase="./" name="command" width="320" height="32" archive="DbMAP_ASJ_applets.jar">
            <param name="backcolor" value="192 192 192">
            <param name="forecolor" value="0 0 0">
			<param name="RELOAD" value="Y" />
			<param name="BUT_INFO" value="Y" />
        </applet><applet code="DbMAP.satellites.sitapplet_selector.class" codebase="./" name="selector" width="32" height="32" archive="DbMAP_ASJ_applets.jar">
            <param name="backcolor" value="192 192 192">
            <param name="forecolor" value="0 0 0">
        </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
  </tr>
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr>
  <!--<tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td colspan="3" align="center" bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.sitapplet_selection_browser.class" codebase="./" name="browser" width="100%" height="80" archive="DbMAP_ASJ_applets.jar">
              <param name="backcolor" value="192 192 192">
              <param name="forecolor" value="0 0 0">
            </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
  </tr>
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr> -->
  <tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td align="left" valign="top" bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.sitapplet_statbar.class" codebase="./" archive="DbMAP_ASJ_applets.jar" width="100%" height="15" name="status1">
              <param name="backcolor" value="192 192 192">
              <param name="forecolor" value="0 0 0">
		      <param name="showStatus" value="N">
            </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td align="left" valign="top" bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.sitapplet_statbar.class" codebase="./" archive="DbMAP_ASJ_applets.jar" width="100%" height="15" name="status2">
              <param name="backcolor" value="192 192 192">
              <param name="forecolor" value="0 0 0">
		      <param name="showCoords" value="N">
            </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
  </tr>
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr>
</table>

  </center>
</div>

</body>

</html>