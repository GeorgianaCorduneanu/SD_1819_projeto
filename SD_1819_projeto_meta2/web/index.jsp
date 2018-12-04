<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 02/12/2018
  Time: 11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!--%@ page contentType="text/html;charset=UTF-8" language="java" % -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<html>
  <head>
    <title>Login_Index</title>
  </head>
  <body>
      <s:form action="login" method="post">
          <s:text name="username" />
          <s:textfield name="username" /><br>
          <s:text name="password"/>
          <s:password name="password" /><br>
          <s:submit />
      </s:form>

  </body>
</html>
