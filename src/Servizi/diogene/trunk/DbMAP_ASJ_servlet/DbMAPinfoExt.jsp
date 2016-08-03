<%@page contentType="text/html; charset=iso-8859-1"
language="java"
import=" javax.naming.*, java.util.*, javax.servlet.*, java.sql.*, javax.sql.* "
%><%
int Recno;  // Recno to be displayed (if the query returns more than one row)(int type)
// Get Configuration parameters
String ConnectionURL=this.getServletConfig().getInitParameter("ConnectionURL");
String ConnectionDriver=this.getServletConfig().getInitParameter("ConnectionDriver");
String ConnectionUsername=this.getServletConfig().getInitParameter("ConnectionUsername");
String ConnectionPassword=this.getServletConfig().getInitParameter("ConnectionPassword");
String dataSource=this.getServletConfig().getInitParameter("dataSource");

// Get Request parameters
String InfoextTableName=request.getParameter("InfoextTableName");
if(InfoextTableName==null)InfoextTableName="INFOEXT";
String InfoextDetailTableName=request.getParameter("InfoextDetailTableName");
if(InfoextDetailTableName==null)InfoextDetailTableName=InfoextTableName+"_DETAIL";
String InfoextId=request.getParameter("InfoextId");
String ElemId=request.getParameter("ElemId");
String RecnoStr=request.getParameter("Recno");
String useCSS=request.getParameter("useCSS");

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
<title>DbMAP Infoext</title>
<%if (!"N".equalsIgnoreCase(useCSS)) {%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/DbMAPinfo.css" type="text/css">
<%}%>
</head>
<body bgcolor="#EEEEEE" text="#0000FF" link="#0000FF" vlink="#0000FF" alink="#0000FF">
<div id="logo"></div>
<%
boolean parameterError=false;
if(dataSource==null && ConnectionURL==null) parameterError=true;
if(InfoextTableName==null) parameterError=true;
if(InfoextDetailTableName==null) parameterError=true;
if(InfoextId==null) parameterError=true;
if(ElemId==null) parameterError=true;

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
	</table>
	<br>

	<b>Request parameters:</b><br>
	<table>
		<tr><td> - InfoextTableName: </td><td> [<i> <%=InfoextTableName%> </i>]</td></tr>
		<tr><td> - InfoextDetailTableName: </td><td> [<i> <%=InfoextDetailTableName%> </i>]</td></tr>
		<tr><td> - InfoextId: </td><td> [<i> <%=InfoextId%> </i>]</td></tr>
		<tr><td> - ElemId: </td><td> [<i> <%=ElemId%> </i>]</td></tr>
		<tr><td> - Recno (optional): </td><td> [<i> <%=RecnoStr%> </i>]</td></tr>
		<tr><td> - useCSS (optional): </td><td> [<i> <%=useCSS%> </i>]</td></tr>
	</table>
	</p>
<%
}
else
{

	//Connect to database
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


	//retrieve the select statement
	String SelectStmt=null;
	String header=null;
	String footer=null;
	int recPerPage=0;
	PreparedStatement ExtPs=null;
	ResultSet ExtRs=null;
	if(conn!=null)
	{
		try
		{
					ExtPs=conn.prepareStatement("SELECT * FROM "+InfoextTableName+" WHERE ID=?",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ExtPs.setString(1,InfoextId);
					ExtRs=ExtPs.executeQuery();
					if(ExtRs.next())
				  {
						SelectStmt=ExtRs.getString("SELECT_STMT");
						recPerPage=ExtRs.getInt("REC_PER_PAGE");
						header=ExtRs.getString("CONTENT_HEADER");
						footer=ExtRs.getString("CONTENT_FOOTER");
				  }
				  else
				  {
		  			%><br>Error:<br><b> &lt;&lt; <%="no record found in table "+InfoextTableName+" for ID="+InfoextId+"."%> &gt;&gt; </b><%
				  }

		}
		catch(Exception ex)
		{
			%><br>Error:<br><b> &lt;&lt; <%=ex%> &gt;&gt; </b><%
			conn=null;
		}
		if(ExtRs!=null) ExtRs.close();
		if(ExtPs!=null) ExtPs.close();
	}

	if (SelectStmt!=null)
	{
		PreparedStatement Ps=null;
		ResultSet Rs=null;
		ResultSetMetaData RsMd;
		int NumOfCols;
		int NumOfRows;
		int i, j;
		boolean HasElements=false;  // Specifies whether the recordset has at least one element
		try
		{
      Ps=conn.prepareStatement(SelectStmt,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      Ps.setString(1,ElemId);
      Rs=Ps.executeQuery();
		}
		catch (SQLException ex)
		{
			%><br>Error reading Data <br>[ <%=SelectStmt%> ] :<br><br><b> &lt;&lt; <%=ex%> &gt;&gt; </b><br><br><%
		}

		if (Rs!=null)
		{
			HasElements=Rs.next();
		}

		if(HasElements)
		{
			NumOfRows=0;
			Rs.beforeFirst();
			while (Rs.next())
			{
				NumOfRows++;
			}
			if (Recno>NumOfRows) Recno=NumOfRows;
			if (Recno<=0) Recno=1;
			Rs.absolute(Recno);
			RsMd = Rs.getMetaData();
%>
		  <%=header!=null?header:""%>
			<table id="griglia" <%=("N".equalsIgnoreCase(useCSS) ? "border=\"1\" cellspacing=\"0\" cellpadding=\"1\" bordercolor=\"#000000\"" : "")%>>
<%
			NumOfCols=RsMd.getColumnCount();
		  int rowi=0;
		  for(int rn=0;rn<=recPerPage;rn++)
		  {
				if(recPerPage>0)
				{
%>
						<tr <%=("N".equalsIgnoreCase(useCSS) ? "" : rowi % 2 == 0 ? "class=\"even\"": "class=\"odd\"")%>>
<%
						rowi++;
				}
				for (i=1;i<=NumOfCols;i++)
				{
					if (TypeAllowed(RsMd.getColumnType(i)))
					{
						if(recPerPage==0)
						{
				  		rowi++;
%>
						  <tr <%=("N".equalsIgnoreCase(useCSS) ? "" : rowi % 2 == 0 ? "class=\"even\"": "class=\"odd\"")%>>
<%
						}
						if(rn==0)
						{
%>
							<td <%=("N".equalsIgnoreCase(useCSS) ? "" : "class=\"tdSx\"")%>><%=RsMd.getColumnName(i)%></td>
<%
						}
						if(rn>0||recPerPage==0)
						{
%>
							<td><%=getColumnString(request,conn,Rs,RsMd,i,InfoextId,InfoextTableName,InfoextDetailTableName,useCSS)%>&nbsp;</td>
<%
						}
						if(recPerPage==0)
						{
%>
							</tr>
<%
						}
					}
				}
				if(recPerPage>0)
				{
%>
						</tr>
<%
				}
				if(rn>0)
					if(!Rs.next())
						rn=recPerPage;
		  }
%>
			</table>
<%
			if(NumOfRows>1)
			{
%>
				<table border="0" cellpadding="1">
				<tr>
				  <td align="center" colspan="2">
<%
				if (Recno>1)
				{
					%><a href="DbMAPinfoExt?InfoextTableName=<%=InfoextTableName%>&InfoextDetailTableName=<%=InfoextDetailTableName%>&InfoextId=<%=InfoextId%>&ElemId=<%=ElemId%>&Recno=<%=Recno-Math.max(recPerPage,1)%>&useCSS=<%="N".equalsIgnoreCase(useCSS)?"N":"Y"%>">&lt;</a><%
				}
%>
					<%=Recno%><%=recPerPage<=0?"":"-"+String.valueOf(Math.min(Recno+recPerPage-1,NumOfRows))%> / <%=NumOfRows%>
<%
				if (Recno+Math.max(recPerPage,1)<=NumOfRows)
				{
					%><a href="DbMAPinfoExt?InfoextTableName=<%=InfoextTableName%>&InfoextDetailTableName=<%=InfoextDetailTableName%>&InfoextId=<%=InfoextId%>&ElemId=<%=ElemId%>&Recno=<%=Recno+Math.max(recPerPage,1)%>&useCSS=<%="N".equalsIgnoreCase(useCSS)?"N":"Y"%>">&gt;</a><%
				}
%>
				</td></tr>
				</table>
<%
			}

		}
		else if (Rs!=null && !HasElements)
		{
			%><br><b>No rows for this query:</b><br><br><%=SelectStmt%><%
		}
		if (Rs!=null) Rs.close();
		if (Ps!=null) Ps.close();
	}
	if (conn!=null) conn.close();
  %><%=footer!=null?footer:""%><%
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

private String getColumnString(HttpServletRequest request,Connection conn,ResultSet rs,ResultSetMetaData rsmd,int columnIndex,String InfoextId,String InfoextTableName,String InfoextDetailTableName,String useCSS) throws SQLException
{
	String retVal="";
	String columnName=rsmd.getColumnName(columnIndex);
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

	if(!retVal.equals(""))//is it a link field??
	{
		PreparedStatement psd=null;
		ResultSet rsd=null;
		try
		{
		  String sql="SELECT * FROM "+InfoextDetailTableName+" WHERE ID_MAIN=? AND FIELD_NAME=?";
		  psd=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  psd.setString(1,InfoextId);
		  psd.setString(2,columnName);
		  rsd=psd.executeQuery();
		  if(rsd.next())
			{
				String url="DbMAPinfoExt?InfoextTableName="+InfoextTableName+"&InfoextDetailTableName="+InfoextDetailTableName+"&InfoextId="+rsd.getString("ID_MAIN_LINK")+"&ElemId="+retVal+"&useCSS="+("N".equalsIgnoreCase(useCSS)?"N":"Y");

				String linkText=rsd.getString("DESCRIPTION");
				if(linkText==null)
				{
				  linkText=retVal;
				  retVal="";
				}
				else retVal+=" ";

				retVal+="<a href=\""+url+"\">"+linkText+"</a>";
			}
		}
		catch(Exception e)
		{
		  e.printStackTrace();
		}
		if(rsd!=null) rsd.close();
		if(psd!=null) psd.close();
	}

	return retVal;
}
%>