<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 08/12/2018
  Time: 19:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Menu Album Utilizador Editor</title>
</head>
<body>
    <%--<c:choose>
        <c:if test="${session.editor == true}">--%>
        <s:form method="post">
            <s:submit type="button" value="true" name="adicionar_album" label="Adicionar Album" formaction="album"/><br>
            <s:submit type="button" value="true" name="eliminar_album" label="Eliminar Album" formaction="album"/><br>
            <s:submit type="button" value="true" name="editar_album" label="Editar Album" formaction="album"/><br>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
        </s:form>
</body>
</html>
