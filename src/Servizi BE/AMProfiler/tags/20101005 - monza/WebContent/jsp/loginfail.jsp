<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%-- <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Login Error!</title>
    <link href="./css/style.css" rel="stylesheet" type="text/css" />
  </head>
  <body>
    <div align="center" style="margin-top: 50px">
      <font size='4' color='red' >
        The username and password validation error.<br/>
        Click <a href='<%= response.encodeURL("/AMProfiler/index.html") %>'>here</a> to retry login
      </font>
    </div>
  </body>
</html>