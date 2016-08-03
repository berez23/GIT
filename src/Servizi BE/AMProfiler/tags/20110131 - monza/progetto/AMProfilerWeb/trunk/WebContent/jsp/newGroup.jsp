<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<html>
	<head>
  	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  	<link href="<%="http://"+request.getServerName()+":"+ request.getServerPort()%>/<%=request.getContextPath()%>/css/newstyle.css" rel="stylesheet" type="text/css" />
  	<title>AMProfiler - Nuovo Gruppo</title>
  	<%String pathWebApp = "http://"+request.getServerName()+":"+ request.getServerPort() +  request.getContextPath();%>
	</head>
	
	<body>
		<script>

		</script>		

		<div id="centrecontent"><!--centre content goes here --> 
			<br>
			<form name="userForm" method="POST" action="<%=pathWebApp%>/NuovoGruppo" >

				<table width="90%" border="0" cellspacing="5" align="center">
          <tr>
            <th align="center">Nuovo Gruppo</th>
            <td><input id="nuovoGruppo" type="text" name="nuovoGruppo" /></td>
          </tr>

          <c:if test="${msgNuovoGruppo != null}">
          <tr>
            <td colspan="2">
             <font color="red" style="font-size: 12px; font-weight: bold;">
               <c:out value="${msgNuovoGruppo}"></c:out>
             </font>
            </td>
          </tr>
          </c:if> 

          <tr><td><br/></td></tr>
          <tr>
            <th align="left">Gruppi Esistenti</th>
            <th align="left">Enti (*)</th>
          </tr>          
          <tr>
            <td align="left">
              <div id="listagruppi" style="width: 200px">
                <br/>
                <select NAME="gruppi" disabled="disabled" size="10" multiple style="width: 200px">
                  <c:forEach var="gruppo" items="${gruppi}">
                    <c:if test="${gruppo.checked == true}">
                      <option selected value=" <c:out value="${gruppo.name}" />">
                        <c:out value="${gruppo.name}" />
                      </option>
                    </c:if>
                    <c:if test="${gruppo.checked != true}">
                      <option value=" <c:out value="${gruppo.name}" />">
                        <c:out value="${gruppo.name}" />
                      </option>
                    </c:if>
                  </c:forEach>
                </select>
              </div>
            </td>
            <td align="left">
              <div id="listaenti" style="width: 200px">
                <br/>
                <select NAME="ente" size="10" style="width: 200px">
                  <c:forEach var="comune" items="${comuni}">
                      <option value="<c:out value="${comune.belfiore}"/>">
                        <c:out value="${comune.descrizione}" />
                      </option>
                  </c:forEach>
                </select>
              </div>
            </td>
          </tr>
          
          <tr>
			      <td align="center" colspan="2">
		      		<input type="submit" name="submit" value="Salva">
			      	<input type="button" value="Chiudi" onclick="window.close();">
			      </td>
			    </tr>
				</table>

        <input name="urlReferer" type="hidden" value="${urlReferer}"></input>

			</form>
		</div>
	</body>
</html>
