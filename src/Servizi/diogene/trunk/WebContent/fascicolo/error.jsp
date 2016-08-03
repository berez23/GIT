    <TABLE width="100%" align="center" >
	<TR >
		<td bgcolor="#66CC66">
           Errore:<a href="javascript:hiddeunhide();">
		<%
		out.println(request.getAttribute("ErrorMessage"));
		%></a>    
		</td>
	</TR>
	<tr id="errore" style="display:none">
		<td ><PRE><%out.println(request.getAttribute("ErrorTrace"));%></PRE> 
		</td>
	</tr>
</TABLE>



