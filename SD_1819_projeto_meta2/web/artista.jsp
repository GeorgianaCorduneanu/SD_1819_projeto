<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 08/12/2018
  Time: 20:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Menu Artista</title>
</head>
<body>
    <s:if test="%{session.editor==true }">
        <s:form method="post">
            <s:submit type="button" value="true" name="pesquisar_artista" label="Pesquisar Artista" formaction="artista_editor"/><br>
            <s:submit type="button" value="true" name="adicionar_artista" label="Adicionar Artista" formaction="artista_editor"/><br>
            <s:submit type="button" value="true" name="eliminar_artista" label="Eliminar Artista" formaction="artista_editor"/><br>
            <s:submit type="button" value="true" name="editar_artista" label="Editar Artista" formaction="artista_editor"/><br>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
        </s:form>
    </s:if>
    <s:else>

    </s:else>

</body>
</html>