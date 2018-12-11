<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 08/12/2018
  Time: 22:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Menu Musica</title>
</head>
<body>
    <s:if test="%{# session.editor == true }">
        <s:form method="post">
            <s:submit type="button" value="true" name="adicionar_musica" label="Adicionar Musica" formaction="musica"/><br>
            <s:submit type="button" value="true" name="eliminar_musica" label="Eliminar Musica" formaction="musica"/><br>
            <s:submit type="button" value="true" name="editar_musica" label="Editar Musica" formaction="musica"/><br>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
            <s:submit type="button" value="true" name="upload_musica" label="Fazer Upload" formaction="album_editor"/><br>
            <s:submit type="button" value="true" name="download_musica" label="Fazer Download" formaction="album_editor"/><br>
            <s:submit type="button" value="true" name="partilhar_musica" label="Partilhar uma Musica" formaction="album_editor"/><br>

        </s:form>
    </s:if>
</body>
</html>