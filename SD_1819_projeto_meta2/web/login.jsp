<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 02/12/2018
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>  <!-- tag s fica associado a /struts-tags-->
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!--%@ page contentType="text/html;charset=UTF-8" language="java" %-->
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Login_Login</title>
</head>
<body>
    <c:choose>
        <jsp:useBean id="session" scope="request"/>
        <c:when test="${session.loggedin == true}">
            <p>Welcome, ${session.username}. Say HEY to someone.</p>
        </c:when>
        <c:otherwise>
            <p>Welcome, anonymous user. Say HEY to someone.</p>
        </c:otherwise>
    </c:choose>

<p><a href="<s:url action="index" />">Start</a></p>
</body>
</html> --%>
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
