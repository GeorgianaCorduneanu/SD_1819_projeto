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

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<html>
<head>
    <title>Registo_Index</title>
</head>
<body>
<s:form method="post">
    <s:text name="Username" />
    <s:textfield name="username" /><br>
    <s:text name="Password"/>
    <s:password name="password" /><br>
    <s:text name="Confirmacao da password"/>
    <s:password name="conf_password"/><br>
    <s:submit type="button" label="VOLTAR" formaction="index"/>
    <s:submit type="button" label="REGISTAR" formaction="registo"/>
</s:form>

</body>
</html>
