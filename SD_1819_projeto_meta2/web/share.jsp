<%--
  Created by IntelliJ IDEA.
  User: gonca
  Date: 17/12/2018
  Time: 01:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title>Partilha</title>
</head>
<body>
    <s:form method="post" action="share">
        <s:text name="Nome da musica: "/>
        <s:textfield name="musica" /><br>
        <s:text name="Nome do utilizador"/>
        <s:textfield name="username"/><br>
        <s:submit type="button" value="true" name="partilha" label="Partilhar" formaction="share"/>
        <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
    </s:form>
</body>
</html>
