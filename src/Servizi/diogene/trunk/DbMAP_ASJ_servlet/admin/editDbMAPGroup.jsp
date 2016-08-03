<%@page import="java.util.*, DbMAP.utils.ExternalUrl"
%>
<jsp:useBean id="service" scope="request" class="DbMAP.management.beans.ServiceBean" />
<jsp:useBean id="DbMAP_ADMIN_MSG" scope="request" class="java.lang.String" />
<html>
<head>
<title>Edit DbMAP Service</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/admin.css">
<script language="JavaScript">
<!-- //
function serviceNameChanged(newName) {
  var frm = document.frm;
  if (frm.serverURL.value == "" &&
      frm.catalogURL.value == "" &&
      frm.infoURL.value == "" &&
      frm.infoExtURL.value == "" )
  {
    frm.serverURL.value = "/" + newName;
    frm.catalogURL.value = "/" + newName;
    frm.infoURL.value = "/" + newName;
    frm.infoExtURL.value = "/" + newName;
  }
}

function deleteService() {
  if (window.confirm("This operation will DELETE this service, are you sure?"))
  {
    document.frm.elements["function"].value = "saveDbMAPGroup";
    document.frm.operation.value = "DELETE";
    document.frm.target="_self";
    document.frm.submit();
  }
}

function testConnection()
{
    document.frm.elements["function"].value = "testConnection";
    document.frm.target="_blank";
    document.frm.submit();
}

function saveService()
{
    document.frm.elements["function"].value = "saveDbMAPGroup";
    document.frm.target="_self";
    document.frm.submit();
}


function dbTypeChanged(db) {
  var item = db.options[db.selectedIndex];
  var frm = document.frm;

  switch (item.value) {
    case "ORACLE_LOCATOR":
    case "ORACLE_SPATIAL":
      frm.connectionDriver.value = "oracle.jdbc.OracleDriver";
      frm.connectionURL.value = "jdbc:oracle:thin:@localhost:1521:sid";
      frm.connectionUsername.value = "scott";
      break;
    case "POSTGIS":
      frm.connectionDriver.value = "org.postgresql.Driver";
      frm.connectionURL.value = "jdbc:postgresql://localhost:5432/database";
      frm.connectionUsername.value = "postgres";
      break;
    case "MYSQLGIS":
      frm.connectionDriver.value = "com.mysql.jdbc.Driver";
      frm.connectionURL.value = "jdbc:mysql://localhost:3306/database";
      frm.connectionUsername.value = "root";
      break;
    default:
      frm.connectionDriver.value = "";
      frm.connectionURL.value = "";
      frm.connectionUsername.value = "";
      break;
  }
}

	function addNewProxyedUrl()
	{
    document.frm.elements["function"].value = "addProxyedUrl";
    document.frm.target="_self";
    document.frm.submit();
	}

	function deleteProxyedUrl(index)
	{
    document.frm.elements["function"].value = "deleteProxyedUrl";
				document.frm.elements["allowedUrl"+index].value = "";
    document.frm.target="_self";
    document.frm.submit();
	}

// -->
</script>
</head>
<body>
<div id="errore"><%=DbMAP_ADMIN_MSG%></div>
<form name="frm" method="post" action="saveDbMAP.jsp">
<input type="hidden" name="function" value="saveDbMAPGroup">
<input type="hidden" name="operation" value="<%=service.getOperation()%>">
<input type="hidden" name="proxyedToBeManaged" value="">
<%
if(service.getServiceID() != null && service.getInfoExtURL() == null)
{
	service.setInfoExtURL("/"+service.getServiceID()+"/DbMAPinfoExt");
}
%>
<table align="center" cellspacing="2">
  <tr>
    <td colspan="4" class="sectionTitle">Service</td>
  </tr>
  <tr>
    <td align="right" valign="top" rowspan="5" class="formLabels">Service id:</td>
    <td valign="top" rowspan="5">
<%    if (service.getServiceID() == null) {%>
        <input name="serviceID" type="text" value="" onchange="serviceNameChanged(this.value)">
<%    } else {%>
        <input name="serviceID" type="text" value="<%=service.getServiceID()%>"  onchange="serviceNameChanged(this.value)" <%=("EDIT".equals(service.getOperation()) ? "readonly" : "")%>>
<%    }%>
    </td>
    <td align="right" class="formLabels">Server URL:</td>
    <td>
      <%=request.getContextPath()%>
<%    if (service.getServerURL() == null) {%>
        <input name="serverURL" type="text" value="">/DbMAPservlet
<%    } else {%>
        <input name="serverURL" type="text" size="30" value="<%=service.getServerURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="right" class="formLabels">Database catalog URL:</td>
    <td>
      <%=request.getContextPath()%>
<%    if (service.getCatalogURL() == null) {%>
        <input name="catalogURL" type="text" value="">/DbMAPservletCAT
<%    } else {%>
        <input name="catalogURL" type="text" size="30" value="<%=service.getCatalogURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="right" class="formLabels">Info page URL:</td>
    <td>
      <%=request.getContextPath()%>
<%    if (service.getInfoURL() == null) {%>
        <input name="infoURL" type="text" value="">/DbMAPinfo
<%    } else {%>
        <input name="infoURL" type="text" size="30" value="<%=service.getInfoURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="right" class="formLabels">Info page (jsp or class):</td>
    <td>
<%    if (service.getInfoPage() == null) {%>
        <input name="infoPage" type="text" size="40" value="/DbMAPinfo.jsp">
<%    } else {%>
        <input name="infoPage" type="text" size="40" value="<%=service.getInfoPage()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="right" class="formLabels">InfoExt page URL:</td>
    <td>
      <%=request.getContextPath()%>
<%    if (service.getInfoExtURL() == null) {%>
        <input name="infoExtURL" type="text" value="">/DbMAPinfoExt
<%    } else {%>
        <input name="infoExtURL" type="text" size="30" value="<%=service.getInfoExtURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td colspan="4" class="sectionTitle">Server extra features</td>
  </tr>

  <tr>
    <td align="right" class="formLabels">Allow shapefiles <input type="checkbox" name="allowShapefiles" value="Y" <%=("Y".equalsIgnoreCase(service.getAllowShapefiles()) ? "checked" : "")%>></td>
    <td align="right" class="formLabels">Allow updates <input type="checkbox" name="allowUpdates" value="Y" <%=("Y".equalsIgnoreCase(service.getAllowUpdates()) ? "checked" : "")%>></td>
    <td align="right" class="formLabels">Allow pocket export <input type="checkbox" name="allowPocket" value="Y" <%=("Y".equalsIgnoreCase(service.getAllowPocket()) ? "checked" : "")%>></td>
    <td align="right" class="formLabels">Allow custom SELECTs <input type="checkbox" name="allowCustomSelects" value="Y" <%=("Y".equalsIgnoreCase(service.getAllowCustomSelects()) ? "checked" : "")%>></td>

<!--    <td align="right" class="formLabels">Allow execute <input type="checkbox" name="allowExecute" value="Y" <%=("Y".equalsIgnoreCase(service.getAllowExecute()) ? "checked" : "")%>></td>-->
<!--    <td>&nbsp;</td> -->
  </tr>

  <tr>
    <td align="right" class="formLabels">Allow shapefiles Import <input type="checkbox" name="allowShapefilesImport" value="Y" <%=("Y".equalsIgnoreCase(service.getAllowShapefilesImport()) ? "checked" : "")%>></td>
    <td align="right" class="formLabels" colspan="3">Shapefile import temporary path <input type="text" name="uploadTempPath" value="<%=service.getUploadTempPath()!=null?service.getUploadTempPath():""%>"></td>
	</tr>

  <tr>
    <td colspan="4" class="sectionTitle">Raster parameters</td>
  </tr>

  <tr>
    <td align="right" class="formLabels">JPEG quality:</td>
    <td>
<%    if (service.getJpegQuality() == null) {%>
        <input name="jpegQuality" type="text" value="0.5">
<%    } else {%>
        <input name="jpegQuality" type="text" value="<%=service.getJpegQuality()%>">
<%    }%>
    </td>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td align="right" class="formLabels">ECW background color:</td>
    <td>
<%    if (service.getEcwBackColor() == null) {%>
        <input name="ecwBackColor" type="text" value="0 0 0">
<%    } else {%>
        <input name="ecwBackColor" type="text" value="<%=service.getEcwBackColor()%>">
<%    }%>
    </td>
    <td colspan="2">&nbsp;</td>
  </tr>

  <tr>
    <td align="right" class="formLabels">ECW path:</td>
    <td>
<%  if (service.getEcwPath() == null) {%>
      <input name="ecwPath" type="text" value="">
<%  } else {%>
      <input name="ecwPath" type="text" value="<%=service.getEcwPath()%>">
<%  }%>
    </td>
    <td align="right" class="formLabels">RMI ECW URL:</td>
    <td>
<%    if (service.getRmiEcwURL() == null) {%>
        <input name="rmiEcwURL" type="text" size="40" value="">
<%    } else {%>
        <input name="rmiEcwURL" type="text" size="40" value="<%=service.getRmiEcwURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="right" class="formLabels">TIFF path:</td>
    <td>
<%    if (service.getTiffPath() == null) {%>
        <input name="tiffPath" type="text" value="">
<%    } else {%>
        <input name="tiffPath" type="text" value="<%=service.getTiffPath()%>">
<%    }%>
    </td>
    <td align="right" class="formLabels">RMI TIFF URL:</td>
    <td>
<%    if (service .getRmiTifURL() == null) {%>
        <input name="rmiTifURL" type="text" size="40" value="">
<%    } else {%>
        <input name="rmiTifURL" type="text" size="40" value="<%=service.getRmiTifURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="right" class="formLabels">DWG path:</td>
    <td>
<%    if (service.getDwgPath() == null) {%>
        <input name="dwgPath" type="text" value="">
<%    } else {%>
        <input name="dwgPath" type="text" value="<%=service.getDwgPath()%>">
<%    }%>
    </td>
    <td align="right" class="formLabels">RMI DWG URL:</td>
    <td>
<%    if (service.getRmiDwgURL() == null) {%>
        <input name="rmiDwgURL" type="text" size="40" value="">
<%    } else {%>
        <input name="rmiDwgURL" type="text" size="40" value="<%=service.getRmiDwgURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td colspan="4" class="sectionTitle">Database connection</td>
  </tr>

  <tr>
    <td align="right" class="formLabels">Database type:</td>
    <td>
      <select name="spatialDatabaseType" onchange="dbTypeChanged(this)">
        <option value="">Select a DB type</option>
        <option value="ORACLE_SPATIAL" <%=("ORACLE_SPATIAL".equals(service.getSpatialDatabaseType()) ? "selected" : "")%>>Oracle Spatial</option>
        <option value="ORACLE_LOCATOR" <%=("ORACLE_LOCATOR".equals(service.getSpatialDatabaseType()) ? "selected" : "")%>>Oracle Locator</option>
        <option value="POSTGIS" <%=("POSTGIS".equals(service.getSpatialDatabaseType()) ? "selected" : "")%>>PostGIS</option>
        <option value="MYSQLGIS" <%=("MYSQLGIS".equals(service.getSpatialDatabaseType()) ? "selected" : "")%>>MySQLGIS</option>
        <option value="SDA" <%=("SDA".equals(service.getSpatialDatabaseType()) ? "selected" : "")%>>Abaco SDA</option>
      </select>
    </td>
    <td align="right" class="formLabels">DataSource:</td>
    <td>
<%    if (service.getDataSource() == null) {%>
        <input name="dataSource" type="text" value="">
<%    } else {%>
        <input name="dataSource" type="text" value="<%=service.getDataSource()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="right" class="formLabels">Database description (SDA only):</td>
    <td>
      <select name="spatialDatabaseDescription">
        <option value="">Select a DB description (SDA type only)</option>
        <option value="HsqldbStandaloneConfig" <%=("HsqldbStandaloneConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>Hsqldb Standalone</option>
        <option value="MSAccessConfig" <%=("MSAccessConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>MS Access</option>
        <option value="MysqlConfig" <%=("MysqlConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>MySql</option>
        <option value="OracleConfig" <%=("OracleConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>Oracle</option>
        <option value="PostgreSQL_JDBCConfig" <%=("PostgreSQL_JDBCConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>PostgreSQL JDBC</option>
        <option value="PostgreSQL_ODBCConfig" <%=("PostgreSQL_ODBCConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>PostgreSQL ODBC</option>
        <option value="SQLServer_JDBCConfig" <%=("SQLServer_JDBCConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>SQLServer JDBC</option>
        <option value="SQLServerConfig" <%=("SQLServerConfig".equals(service.getSpatialDatabaseDescription()) ? "selected" : "")%>>SQLServer ODBC</option>
      </select>
    </td>

    <td align="center" colspan="2">OR</td>
  </tr>

  <tr>
    <td align="right" class="formLabels">SRID:</td>
    <td>
<%    if (service.getSRID() == null) {%>
        <input name="SRID" type="text" value="">
<%    } else {%>
        <input name="SRID" type="text" value="<%=service.getSRID()%>">
<%    }%>
    </td>
    <td align="right" class="formLabels">Connection driver:</td>
    <td>
<%    if (service.getConnectionDriver() == null) {%>
        <input name="connectionDriver" type="text" size="40" value="">
<%    } else {%>
        <input name="connectionDriver" type="text" size="40" value="<%=service.getConnectionDriver()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="center" colspan="2">&nbsp;</td>
    <td align="right" class="formLabels">Connection URL:</td>
    <td>
<%    if (service.getConnectionURL() == null) {%>
        <input name="connectionURL" type="text" size="40" value="">
<%    } else {%>
        <input name="connectionURL" type="text" size="40" value="<%=service.getConnectionURL()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="center" colspan="2">&nbsp;</td>
    <td align="right" class="formLabels">Connection Username:</td>
    <td>
<%    if (service.getConnectionUsername() == null) {%>
        <input name="connectionUsername" type="text" size="40" value="">
<%    } else {%>
        <input name="connectionUsername" type="text" size="40" value="<%=service.getConnectionUsername()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="center" colspan="2">&nbsp;</td>
    <td align="right" class="formLabels">Connection Password:</td>
    <td>
<%    if (service.getConnectionPassword() == null) {%>
        <input name="connectionPassword" type="password" size="40" value="">
<%    } else {%>
        <input name="connectionPassword" type="password" size="40" value="<%=service.getConnectionPassword()%>">
<%    }%>
    </td>
  </tr>

  <tr>
    <td align="center" colspan="2">&nbsp;</td>
    <td align="right" class="formLabels">Retype connection Password:</td>
    <td>
<%    if (service.getConnectionPassword() == null) {%>
        <input name="connectionPassword2" type="password" size="40" value="">
<%    } else {%>
        <input name="connectionPassword2" type="password" size="40" value="<%=service.getConnectionPassword()%>">
<%    }%>
    </td>
  </tr>
<tr>
    <td align="center" colspan="3">&nbsp;</td>
    <td align="right"><input type="button" value="Test Connection" onclick="testConnection()"></td>
</tr>

  <tr>
    <td colspan="4" class="sectionTitle">Allowed external URLs:</td>
  </tr>
		<tr>
		 <td colspan=4>
		 <center><table border = "0" width="80%">
				<tr><th class="small" width="70%">URL</th><th class="small">Username</th><th class="small">Password</th></tr>
<%
Vector proxyedUrls=service.getProxyedServlets();
if (proxyedUrls.size()==0)
{
%><tr>
<td colspan="3">No allowed URL configured&nbsp;</td>
</tr><%
}
else
{
for (int i=0;i<proxyedUrls.size();i++)
{
	ExternalUrl currentUrl=(ExternalUrl) proxyedUrls.elementAt(i);
String theUrl=currentUrl.getUrl();
String theUsr=currentUrl.getUsername()!=null?currentUrl.getUsername():"";
String thePwd=currentUrl.getPassword()!=null?currentUrl.getPassword():"";
%><tr>
<td><input type="text" value="<%=theUrl%>" name="allowedUrl<%=i%>" style="width:100%"></td>
<td><input type="text" value="<%=theUsr%>" name="allowedUser<%=i%>" style="width:100%"></td>
<td><input type="password" value="<%=thePwd%>" name="allowedPass<%=i%>" style="width:100%"></td>
<td><input type="button" value="X" onclick="deleteProxyedUrl(<%=i%>)"></td>
</tr><%
}
}
%>
		 </table>
<input type="button" value="Add new URL" onclick="addNewProxyedUrl()">
</center>
		 </td>
		</tr>

  <tr><td colspan="4">&nbsp;</td></tr>
  <tr><td colspan="4">&nbsp;</td></tr>
  <tr><td colspan="4">&nbsp;</td></tr>

  <tr>
    <td align="center" colspan="4">
<!--      <input type="submit" value="  OK  ">-->
      <input type="button" value="  OK  " onclick="saveService()">
      <!--input type="reset" value="Reset"-->
      <input type="button" value="Cancel" onclick="window.location.href='index.jsp'">
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" value="Delete this service" onclick="deleteService()">
    </td>
  </tr>
</table>
</form>
</body>
</html>
