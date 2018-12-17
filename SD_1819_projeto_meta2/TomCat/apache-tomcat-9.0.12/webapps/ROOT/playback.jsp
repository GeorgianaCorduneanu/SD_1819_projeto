<%--
  Created by IntelliJ IDEA.
  User: gonca
  Date: 17/12/2018
  Time: 04:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title>Playback</title>
</head>
<body>
<s:form method="post" action="play2">
    <s:text name="Escreva o nome da musica: "/>
    <s:textfield name="musica" /><br>
    <s:submit type="button" value="true" name="play2" label="Play" formaction="play2"/>
    <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
</s:form>
</body>
</html>
