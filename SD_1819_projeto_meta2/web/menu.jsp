<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 08/12/2018
  Time: 15:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Menu</title>
</head>
<body>
    <s:form method="post">
        <s:submit type="button" value="true" name="musica" label="MUSICA" formaction="menu"/>
        <s:submit type="button" value="true" name="artista" label="ARTISTA" formaction="menu"/>
        <s:submit type="button" value="true" name="album" label="ALBUM" formaction="menu"/>
        <s:submit type="button" name="logout" label="LOGOUT" formaction="index"/>
    </s:form>
</body>
</html>
