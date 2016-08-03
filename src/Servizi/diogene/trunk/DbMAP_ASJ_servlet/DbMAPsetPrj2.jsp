<%@page contentType="text/html; charset=iso-8859-1"
        language="java"%>
<%
session.setAttribute("XMLprj", request.getParameter("xml_project"));
%>
<html>

<head>
<meta http-equiv="Content-Language" content="it">

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>DbMAP ASJ Web Preview</title>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<div align="center">
  <center>

<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
    <td height="5" bgcolor="#808080"></td>
    <td height="5" width="5" bgcolor="#808080"></td>
  </tr>
  <tr>
    <td height="5" width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td height="5" bgcolor="#C0C0C0" align="center">
    <img border="0" src="<%=request.getContextPath()%>/logo.gif" width="64" height="46"></td>
    <td height="5" width="5" bgcolor="#808080">&nbsp;</td>
    <td height="5" bgcolor="#C0C0C0" align="center"><b>DbMAP ASJ Web Preview</b></td>
    <td height="5" width="5" bgcolor="#808080"></td>
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
    <td bgcolor="#C0C0C0" rowspan="2">
    <applet code="DbMAP.satellites.sitapplet_layers.class" codebase="<%=request.getContextPath()%>/" archive="DbMAP_ASJ_applets.jar" width="150" height="100%" name="layers">
              <param name="backcolor" value="192 192 192">
              <param name="forecolor" value="0 0 0">
        </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td bgcolor="#C0C0C0" width="100%" height="100%">
    <applet code="DbMAP.sitapplet_main.class" codebase="<%=request.getContextPath()%>/" archive="DbMAP_ASJ_applets.jar" width="100%" height="100%">
              <param name="satelliteapplet1" value="status1">
              <param name="satelliteapplet2" value="status2">
              <param name="satelliteapplet3" value="layers">
              <param name="satelliteapplet4" value="command">
              <param name="satelliteapplet5" value="scale">
              <param name="satelliteapplet6" value="selector">
              <param name="satelliteapplet7" value="browser">
              <param name="satelliteapplet8" value="editor">
              <param name="project" value="DbMAPgetPrj.jsp">
        </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
  </tr>
  <tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.ext.sitapplet_scale.class" codebase="<%=request.getContextPath()%>/" name="scale" width="100%" height="20" archive="DbMAP_ASJ_applets.jar">
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
    <td align="center" bgcolor="#C0C0C0"><applet code="DbMAP.satellites.sitapplet_editor.class" codebase="<%=request.getContextPath()%>/" archive="DbMAP_ASJ_applets.jar" width="84" height="24" name="editor">
			<param name="backcolor" value="192 192 192" />
            <param name="forecolor" value="0 0 0">
			<param name="appletReference" value="window.opener.document.editor" />
            <param name="insert_multi" value="Y">
		</applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td align="center" bgcolor="#C0C0C0"><applet code="DbMAP.satellites.sitapplet_command.class" codebase="<%=request.getContextPath()%>/" name="command" width="320" height="32" archive="DbMAP_ASJ_applets.jar">
            <param name="backcolor" value="192 192 192">
            <param name="forecolor" value="0 0 0">
			<param name="RELOAD" value="Y" />
        </applet><applet code="DbMAP.satellites.sitapplet_selector.class" codebase="<%=request.getContextPath()%>/" name="selector" width="32" height="32" archive="DbMAP_ASJ_applets.jar">
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
  <tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td colspan="3" align="center" bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.sitapplet_selection_browser.class" codebase="<%=request.getContextPath()%>/" name="browser" width="100%" height="80" archive="DbMAP_ASJ_applets.jar">
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
  <tr>
    <td width="5" bgcolor="#808080" bordercolor="#FF0000">&nbsp;</td>
    <td align="left" valign="top" bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.sitapplet_statbar.class" codebase="<%=request.getContextPath()%>/" archive="DbMAP_ASJ_applets.jar" width="100%" height="15" name="status1">
              <param name="backcolor" value="192 192 192">
              <param name="forecolor" value="0 0 0">
		      <param name="showStatus" value="N">
            </applet></td>
    <td width="5" bgcolor="#808080">&nbsp;</td>
    <td align="left" valign="top" bgcolor="#C0C0C0">
    <applet code="DbMAP.satellites.sitapplet_statbar.class" codebase="<%=request.getContextPath()%>/" archive="DbMAP_ASJ_applets.jar" width="100%" height="15" name="status2">
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