<%@page contentType="text/html; charset=iso-8859-1"
language="java"
import=" javax.naming.*, java.util.*, javax.servlet.*, java.sql.*, javax.sql.* "
%><%
/*
--Parameters--

---View Info---
TableName: table from where data must be read.
IdentField: identifier field of the table
ElemId: value of the identifier field (used to build the where clause)
where: (optional) an additional optional where to be used in conjunction with the identifier where.
stmt: (optional) select statement to be executed to obtain data to show (must contain a ? (placeholeder) to be binded with the value of ElemId)

---Edit Info---
TableName: table from where data must be read.
IdentField: identifier field of the table
ElemId: value of the identifier field (used to build the where clause)
where: (optional) an additional optional where to be used in conjunction with the identifier where.
EDITING=U (or UG to disable the editing of alphanumeric data)
submitURL: url where to post when submit is pressed (usually the servlet url, without parameters)
-servlet specific parameters-
function=updateext
GeometryField: Name of the geometry field to set with SDAString (optional)
SDAString: SDA string of the geometry to write (optional) if not set and GeometryField is set then GeometryField is set to null

---Delete Info---
TableName: table from where data must be read.
IdentField: identifier field of the table
ElemId: value of the identifier field (used to build the where clause)
where: (optional) an additional optional where to be used in conjunction with the identifier where.
EDITING=D
submitURL: url where to post when submit is pressed (usually the servlet url, without parameters)
-servlet specific parameters-
function=updateext

---Insert Info---
TableName: table from where data must be read.
EDITING=I
submitURL: url where to post when submit is pressed (usually the servlet url, without parameters)
-servlet specific parameters-
function=updateext
GeometryField: Name of the geometry field to set with SDAString (optional)
SDAString: SDA string of the geometry to write (optional) if not set and GeometryField is set then GeometryField is set to null

-NOTE-
Please note that all the others parameters will be submitted to the submitURL as they are received.
You can use additional parameters to specify the shapefield and its new value and also other custom parameters.
All the table fields will be submitted to the submitURL with their new values.
Each field is identified with a parameter name in the form of field_XXX (where XXX is the field name).
The callerPage parameter is also sent to submitURL and it can be used to reopen the page when an error occours.
In this case you have to pass all the parameters you've received to the callerPage.
You can also specify an errorString parameter. This is shown in the page.
*/

int Recno;  // Recno to be displayed (if the query returns more than one row)(int type)

if(request.getParameter("storeSDAString")!=null&&request.getParameter("storeSDAString").equalsIgnoreCase("Y"))
{
session.setAttribute("SDAString",request.getParameter("SDAString"));
}

// Get Configuration parameters
String ConnectionURL=this.getServletConfig().getInitParameter("ConnectionURL");
String ConnectionDriver=this.getServletConfig().getInitParameter("ConnectionDriver");
String ConnectionUsername=this.getServletConfig().getInitParameter("ConnectionUsername");
String ConnectionPassword=this.getServletConfig().getInitParameter("ConnectionPassword");
String dataSource=this.getServletConfig().getInitParameter("dataSource");
String allowCustomSelects=this.getServletConfig().getInitParameter("allowCustomSelects");

// Get Request parameters
String TableName=request.getParameter("TableName");
String IdentField=request.getParameter("IdentField");
String ElemId=request.getParameter("ElemId");
String RecnoStr=request.getParameter("Recno");
String editing=request.getParameter("EDITING");
String submitURL=request.getParameter("submitURL");
String customStatement=request.getParameter("stmt");
String optionalWhere=request.getParameter("where");

// Check the received parameter
if (ConnectionDriver==null)
	ConnectionDriver="oracle.jdbc.OracleDriver";
if (RecnoStr==null || RecnoStr.equals(""))
	Recno=1;
else
	Recno=Integer.valueOf(RecnoStr).intValue();
%>
<html>
<head>
<title>DbMAP Info</title>
<%if (!"N".equalsIgnoreCase(request.getParameter("useCSS"))) {%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/DbMAPinfo.css" type="text/css">
<%}%>
</head>
<body bgcolor="#EEEEEE" text="#0000FF" link="#0000FF" vlink="#0000FF" alink="#0000FF">
<div id="logo"></div>
<%
if(request.getParameter("errorString")!=null)
{
%>
<p>
<%=request.getParameter("errorString")%>
</p>
<%
}
%>
<%
boolean parameterError=false;
if(dataSource==null && ConnectionURL==null) parameterError=true;
if(TableName==null) parameterError=true;
if((IdentField==null || ElemId==null)&&(editing==null||!editing.equals("I"))) parameterError=true;

if (parameterError)
{
%>
	<p>
	<br>
	ERROR : One or more required parameters are null:
	<br><br><br>
	<b>Configuration parameters:</b><br>
	<table>
		<tr>
		<td> - ConnectionURL: </td><td> [<i> <%=ConnectionURL%> </i>] </td>
		</tr>
		<tr>
		<td> - ConnectionUsername: </td><td> [<i> <%=ConnectionUsername%> </i>] </td>
		</tr>
		<tr>
		<td> - ConnectionPassword: </td><td> [<i> <%=(ConnectionPassword==null?"":"*****")%> </i>] </td>
		</tr>
		<tr>
		<td> - ConnectionDriver: </td><td> [<i> <%=ConnectionDriver%> </i>] </td>
		</tr>
		<tr>
		<td> - dataSource: </td><td> [<i> <%=dataSource%> </i>] </td>
		</tr>
		<tr>
		<td> - allowCustomSelects: </td><td> [<i> <%=allowCustomSelects%> </i>] </td>
		</tr>
	</table>
	<br>

	<b>Request parameters:</b><br>
	<table>
		<tr><td> - TableName: </td><td> [<i> <%=TableName%> </i>]</td></tr>
		<tr><td> - IdentField (optional when EDITING=I): </td><td> [<i> <%=IdentField%> </i>]</td></tr>
		<tr><td> - ElemId (optional when EDITING=I): </td><td> [<i> <%=ElemId%> </i>]</td></tr>
		<tr><td> - where (optional): </td><td> [<i> <%=optionalWhere%> </i>]</td></tr>
		<tr><td> - stmt (optional and only when EDITING is null): </td><td> [<i> <%=ElemId%> </i>]</td></tr>
		<tr><td> - EDITING (optional)[I/U/UG/D]: </td><td> [<i> <%=editing%> </i>]</td></tr>
		<tr><td> - Recno (optional): </td><td> [<i> <%=RecnoStr%> </i>]</td></tr>
		<tr><td> - submitURL (required for editing):</td><td> [<i> <%=submitURL%> </i>]</td></tr>
	</table>
	</p>
<%
}
else if(customStatement!=null && "N".equalsIgnoreCase(allowCustomSelects))
{
%>
	<p>
	<br>
	ERROR : A custom statement is specified and allowCustomStatements is set to N
	<br><br><br>
<%
}
else
{
	Connection conn=null;
	try
	{
		if (dataSource==null)
			conn=getConnection(ConnectionURL,ConnectionDriver,ConnectionUsername,ConnectionPassword);
		else
			conn=getConnection(dataSource);
	}
	catch(Exception ex)
	{
		%><br>Connection error:<br><b> &lt;&lt; <%=ex%> &gt;&gt; </b><%
		conn=null;
	}

	if (conn!=null)
	{
		String SQL;
		PreparedStatement Ps=null;
		Statement St=null;
		ResultSet Rs=null;
		ResultSetMetaData RsMd;
		int NumOfCols;
		int NumOfRows;
		int i, j;
		boolean HasElements=false;  // Specifies whether the recordset has at least one element
		Vector Expressions=new Vector(0), Prompts=new Vector(0);
		String FieldList="*";// all the fields are displayed, using the default prompt

    if(editing!=null)
      customStatement=null;

		// Get the IdentField type in order to execute the correct SQL statement
		if(editing!=null&&editing.equals("I"))
		{
		  SQL="SELECT " + FieldList + " FROM " + TableName;
		}
		else
		{
		  if(customStatement!=null)
				SQL=customStatement;
		  else
		  {
		    boolean StringColumn=columnIsString(conn,IdentField,TableName);
				if (StringColumn)
				  SQL="SELECT " + FieldList + " FROM " + TableName + " WHERE " + IdentField + " = '" + ElemId + "'";
				else
				  SQL="SELECT " + FieldList + " FROM " + TableName + " WHERE " + IdentField + " = " + ElemId;
				if(optionalWhere!=null)
				  SQL+=" AND ("+optionalWhere+")";
		  }
		}
		try
		{
      if(customStatement==null)
      {
			  St=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			  Rs=St.executeQuery(SQL);
      }
      else
      {
        Ps=conn.prepareStatement(SQL,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        Ps.setString(1,ElemId);
        Rs=Ps.executeQuery();
      }
		}
		catch (SQLException ex)
		{
			%><br>Error reading Data Table [ <%=TableName%> ] :<br><br><b> &lt;&lt; <%=ex%> &gt;&gt; </b><br><br><%
      if(Rs!=null) Rs.close();
      if(St!=null) St.close();
      if(Ps!=null) Ps.close();
			Rs=null;
			St=null;
      Ps=null;
		}
		if (Rs!=null)
		{
			if(editing!=null&&editing.equals("I"))
				HasElements=true;
			else
				HasElements=Rs.next();
		}

		if (HasElements)
		{
			NumOfRows=0;
			if(editing!=null&&editing.equals("I"))
			{
			}
			else
			{
				Rs.beforeFirst();
				while (Rs.next())
				{
					NumOfRows++;
				}
				if (Recno>NumOfRows) Recno=NumOfRows;
				if (Recno<=0) Recno=1;
				Rs.absolute(Recno);
			}
			RsMd = Rs.getMetaData();
		%>
		<%
			if(editing!=null)
			{%>
				<form METHOD="POST" action="<%=submitURL%>">
			<%}%>
			<table id="griglia" <%=("N".equalsIgnoreCase(request.getParameter("useCSS")) ? "border=\"1\" cellspacing=\"0\" cellpadding=\"1\" bordercolor=\"#000000\"" : "")%>>
<%
			NumOfCols=RsMd.getColumnCount();
		  int rowi=0;
			for (i=1;i<=NumOfCols;i++)
			{
				if (TypeAllowed(RsMd.getColumnType(i)))
				{
rowi++;%>

				<tr <%=("N".equalsIgnoreCase(request.getParameter("useCSS")) ? "" : rowi % 2 == 0 ? "class=\"even\"": "class=\"odd\"")%>>
					<td <%=("N".equalsIgnoreCase(request.getParameter("useCSS")) ? "" : "class=\"tdSx\"")%>><%=RsMd.getColumnName(i)%></td>
<%
					if(editing==null)
					{%>
						<td><%=getColumnString(request,Rs,RsMd,i)%>&nbsp;</td>
					<%}
					else
					{%>
						<td><input type="TEXT" name="<%=(editing.equals("UG")?"view_":"")%>field_<%=RsMd.getColumnName(i)%>" size=<%=(RsMd.getColumnDisplaySize(i)<=80?RsMd.getColumnDisplaySize(i):80)%> value="<%=getColumnString(request,Rs,RsMd,i)%>" <%=(editing.equalsIgnoreCase("D")||editing.equalsIgnoreCase("UG")?"DISABLED":"")%>></td>
					<%}%>
				</tr>
				<%
				}
			}
%>
			</table>
		<%
			if(NumOfRows>1)
			{%>
				<table border="0" cellpadding="1">
				<tr><td align="center" colspan="2"><%
				if (Recno>1)
				{
					%><a href="<%=javax.servlet.http.HttpUtils.getRequestURL(request).toString()%>?TableName=<%=TableName%>&IdentField=<%=IdentField%>&ElemId=<%=ElemId%><%=(optionalWhere==null?"":"&where="+java.net.URLEncoder.encode(optionalWhere))%><%=(customStatement==null?"":"&stmt="+java.net.URLEncoder.encode(customStatement))%>&Recno=<%=Recno-1%>">&lt;</a><%
				}%>
					<%=Recno%> / <%=NumOfRows%><%
				if (Recno<NumOfRows)
				{
					%><a href="<%=javax.servlet.http.HttpUtils.getRequestURL(request).toString()%>?TableName=<%=TableName%>&IdentField=<%=IdentField%>&ElemId=<%=ElemId%><%=(optionalWhere==null?"":"&where="+java.net.URLEncoder.encode(optionalWhere))%><%=(customStatement==null?"":"&stmt="+java.net.URLEncoder.encode(customStatement))%>&Recno=<%=Recno+1%>">&gt;</a><%
				}%>
				</td></tr>
				</table><%
			}
			if(editing!=null)
			{
				Enumeration params=request.getParameterNames();
				while(params.hasMoreElements())
				{
					String paramName=(String)params.nextElement();
					String paramValue=request.getParameter(paramName);
					if(paramName!=null&&paramName.equals("SDAString")&&paramValue!=null&&paramValue.equals("*STORED*"))
						paramValue=(String)session.getAttribute("SDAString");
					if(paramName!=null&&paramValue!=null&&!paramName.startsWith("field_")&&!paramName.equals("callerPage"))
					{
						%><input type="HIDDEN" name="<%=paramName%>" value="<%=paramValue%>"><%
					}
				}
%>
					<input type="HIDDEN" name="callerPage" value="<%=javax.servlet.http.HttpUtils.getRequestURL(request).toString()%>">
					<INPUT TYPE="SUBMIT" value="<%=(editing.equalsIgnoreCase("D")?"DELETE":"Ok")%>" <%=(NumOfRows>1?"DISABLED":"")%>>
					</form>
<%
			}
		}
		else
		{
			if (Rs!=null && !HasElements)
			{
			%><br><b>No rows for this query:</b><br><br><%=SQL%><%
			}
		}
		if (Rs!=null) Rs.close();
		if (St!=null) St.close();
		if (Ps!=null) Ps.close();
	}
	if (conn!=null) conn.close();
}
%>
</body>
</html>

<script language="javascript">
try {
window.resizeBy(
  (document.getElementById("griglia").clientWidth*1.5) - document.body.clientWidth,
  document.body.scrollHeight - document.body.clientHeight,
  0);
window.resizeBy(
  (document.getElementById("griglia").clientWidth*1.5) - document.body.clientWidth,
  document.body.scrollHeight - document.body.clientHeight,
  0);
window.focus();
}
catch(er) {
}
</script>

<%!

// return the connection from a database URL
private Connection getConnection (String TheUrl, String DriverToUse, String Username, String Password) throws SQLException,InstantiationException,ClassNotFoundException,IllegalAccessException
{
	Driver driver=(Driver)Class.forName(DriverToUse).newInstance();
	Connection conn=DriverManager.getConnection(TheUrl,Username,Password);
	return conn;
}

// return the connection from a dataSource
private Connection getConnection (String dataSource) throws NamingException,SQLException
{
	InitialContext ctx=new InitialContext();
	DataSource ds=(DataSource)ctx.lookup(dataSource);
	return ds.getConnection();
}

// return true if the field can be displayed, false otherwise
private boolean TypeAllowed (int ColType)
{
	if ( ColType == Types.BIGINT      ||
		ColType == 16/*Types.BOOLEAN(only from 1.4)*/     ||
		ColType == Types.CHAR        ||
		ColType == Types.DATE        ||
		ColType == Types.DECIMAL     ||
		ColType == Types.DOUBLE      ||
		ColType == Types.DOUBLE      ||
		ColType == Types.FLOAT       ||
		ColType == Types.INTEGER     ||
		ColType == Types.LONGVARCHAR ||
		ColType == Types.NUMERIC     ||
		ColType == Types.SMALLINT    ||
		ColType == Types.TIME        ||
		ColType == Types.TIMESTAMP   ||
		ColType == Types.VARCHAR
	)
		return true;
	else
		return false;
}

private String getColumnString(HttpServletRequest request,ResultSet rs,ResultSetMetaData rsmd,int columnIndex) throws SQLException
{
	String retVal="";
	String columnName=rsmd.getColumnName(columnIndex);
	if(request.getParameter("field_"+columnName)!=null)
		return request.getParameter("field_"+columnName);
	if(rs.isAfterLast()||rs.isBeforeFirst())
		return retVal;
	int colType=rsmd.getColumnType(columnIndex);
	try
	{
		switch(colType)
		{
		case 16/*Types.BOOLEAN(only from 1.4)*/:
		  retVal=new Boolean(rs.getBoolean(columnIndex)).toString();
		  break;
		case Types.SMALLINT:
		  retVal=new Short(rs.getShort(columnIndex)).toString();
		  break;
		case Types.DECIMAL:
		case Types.NUMERIC:
		  retVal=rs.getBigDecimal(columnIndex).toString();
		  break;
		case Types.INTEGER:
		  retVal=new Integer(rs.getInt(columnIndex)).toString();
		  break;
		case Types.DOUBLE:
		case Types.FLOAT:
		  retVal=new Double(rs.getDouble(columnIndex)).toString();
		  break;
		case Types.BIGINT:
		  retVal=new Long(rs.getLong(columnIndex)).toString();
		  break;
		case Types.DATE:
		  retVal=rs.getDate(columnIndex).toString();
		  break;
		case Types.TIME:
		  retVal=rs.getTime(columnIndex).toString();
		  break;
		case Types.TIMESTAMP:
		  retVal=rs.getTimestamp(columnIndex).toString();
		  break;
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
		case Types.CHAR:
		default:
		  retVal=rs.getString(columnIndex);
		  if(rs.wasNull()) retVal="";
		  break;
		}
	}catch(Exception e){retVal="";}
	return retVal;
}


private boolean columnIsString(Connection conn,String columnName,String tableName)
{
	Statement PsTemp=null;
	ResultSet RsTemp=null;
	ResultSetMetaData RsMdTemp=null;
	String SQLTemp=null;
	int ColumnType;
	boolean StringColumn=true;

	try
	{
		SQLTemp="SELECT " + columnName + " FROM " + tableName;
		PsTemp=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		RsTemp=PsTemp.executeQuery(SQLTemp);

		RsMdTemp=RsTemp.getMetaData();
		ColumnType=RsMdTemp.getColumnType(1); //only one column is returned
		switch (ColumnType)
		{
		case 1  : //CHAR
		case -1 : //LONGVARCHAR
		case 12 : //VARCHAR
			StringColumn=true;
			break;
		default:
			StringColumn=false;
			break;
		}
		RsTemp.close();
		PsTemp.close();
	}
	catch(Exception e)
	{
	}
	return StringColumn;
}

%>