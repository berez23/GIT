<%@page import="java.util.*, java.net.*, DbMAP.management.*"%>
<%String contextPath = request.getContextPath();%>
<html>
<head>
<title>Deployed servlets</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/admin.css">
<script language="JavaScript">
<!-- //
function saveAll() {
  var msg = "WARNING: you may need to restart your web application!";

  if (window.confirm(msg))
    window.location.href="<%=request.getContextPath()%>/admin/DbMAPManagement?function=saveAllChanges";
}
// -->
</script>
</head>
<jsp:useBean id="servlets" scope="request" class="java.util.TreeMap" />
<jsp:useBean id="groups" scope="request" class="java.util.TreeMap" />
<body>
<center><h1>Services</h1></center>
<table class="LISTA" align="center">
  <tr>
    <th>Service name</th>
    <th>Service components</th>
  </tr>
<%boolean needsSave = false;
  Iterator iter = groups.keySet().iterator();
  ServletGroup group;
  while (iter.hasNext()) {
    group = (ServletGroup)groups.get(iter.next());
    needsSave = needsSave || group.isChanged() || group.isDeleted();%>
    <tr>
      <td <%=(group.isDeleted() ? "class=\"deleted\"" : group.isChanged() ? "class=\"changed\"" : "")%>>
<%      if (group.isDeleted()) {%>
          <b><%=group.getName()%></b>
<%      } else {%>
          <a href="<%=request.getContextPath()%>/admin/DbMAPManagement?function=editDbMAPGroup&groupID=<%=URLEncoder.encode(group.getName())%>"><%=group.getName()%></a>
<%      }%>
      </td>
      <td <%=(group.isDeleted() ? "class=\"deleted\"" : group.isChanged() ? "class=\"changed\"" : "")%>>
        <table class="LISTA" align="center">
          <tr>
            <th>Description</th>
            <th>Mapped on</th>
          </tr>
<%          if (group.getServer() != null) {%>
              <tr>
                <td><%=nvl(group.getServer().getDescription())%></td>
                <td><%=contextPath%><%=group.getServer().getUrlPattern()%></td>
              </tr>
<%          }%>
<%          if (group.getCatalog() != null) {%>
              <tr>
                <td><%=nvl(group.getCatalog().getDescription())%></td>
                <td><%=contextPath%><%=group.getCatalog().getUrlPattern()%></td>
              </tr>
<%          }%>
<%          if (group.getInfo() != null) {%>
              <tr>
                <td><%=nvl(group.getInfo().getDescription())%></td>
                <td><%=contextPath%><%=group.getInfo().getUrlPattern()%></td>
              </tr>
<%          }%>
        </table>
      </td>
    </tr>
<%}%>
</table>
<br>
<center>
<%if (needsSave) {%>
    <input type="button" style="background: #ff5555; color: #ffffff; font-weight: bold" value="Save all changes" onclick="saveAll()">
<%}%>
<input type="button" value="New service" onclick="window.location.href='<%=request.getContextPath()%>/admin/DbMAPManagement?function=editDbMAPGroup'">
</center>

<center><h1>Standalone servlets</h1></center>
<table class="LISTA" align="center">
  <tr>
    <th>Servlet Name</th>
    <th>Servlet class</th>
    <th>Mapped on</th>
  </tr>
<%iter = servlets.keySet().iterator();
  ServletDef def;
  while (iter.hasNext()) {
    def = (ServletDef)servlets.get(iter.next());%>
    <tr>
      <td><%=def.getName()%></td>
      <td><%=def.getClazz()%></td>
      <td><%=contextPath%><%=def.getUrlPattern()%></td>
    </tr>
<%}%>
</table>
</body>
</html>

<%!
private String nvl(String string) {
  return string == null ? "" : string;
}
%>
