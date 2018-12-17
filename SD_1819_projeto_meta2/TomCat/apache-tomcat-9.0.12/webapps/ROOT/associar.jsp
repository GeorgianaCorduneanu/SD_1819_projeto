<%--
  Created by IntelliJ IDEA.
  User: gonca
  Date: 16/12/2018
  Time: 12:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Associar Musica</title>
</head>
<body>
    <s:form method="post" action="associar">
        <s:text name="Escreva o nome da musica: "/>
        <s:textfield name="musica" /><br>
        <s:text name="Filepath da Dropbox"/>
        <s:textfield name="filepath"/><br>
        <s:submit type="button" value="true" name="associa" label="Associar" formaction="associar"/>
        <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
    </s:form>
</body>
</html>
