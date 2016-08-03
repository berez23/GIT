<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="./css/style.css" rel="stylesheet" type="text/css" />
    <title>Login Page</title>
  </head>
  <body>
    <div align="center">
      <form action='j_security_check' method='post' style="width: 200px">
          <font size='5' color='blue'>AMProfiler Login</font><hr>
          <table>
            <tr>
              <th align="right">UserName:</th>
              <td><input type='text' name='j_username'></td>
            </tr>
            <tr>
              <th align="right">Password:</th>
              <td><input type='password' name='j_password' size='8'></td>
            </tr>
          </table>
          <br>
          <input type='submit' value='login'>
      </form>
    </div>
  </body>
</html>