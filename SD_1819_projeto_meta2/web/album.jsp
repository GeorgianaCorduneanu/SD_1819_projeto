<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 08/12/2018
  Time: 19:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Menu Album Utilizador Editor</title>
</head>
<body>
    <%--<c:choose>
        <c:if test="${session.editor == true}">--%>
        <s:if test="%{#session.editor == false}">
            <s:form method="post">
                <s:submit type="button" value="true" name="listar_musica" label="Listar Musicas" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="pesquisar_musica" label="Pesquisar Musica" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="upload_musica" label="Fazer Upload" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="download_musica" label="Fazer Download" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="partilhar_musica" label="Partilhar uma Musica" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="criar_playlist" label="Criar uma Playlist" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="ver_playlist" label="Ver Playlists" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="privacidade_playlist" label="Definir privacidade de Playlist" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="adicionar_musica" label="Adicionar Musica" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="eliminar_musica" label="Eliminar Musica" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="editar_musica" label="Editar uma Musica" formaction="album_editor"/><br>
                <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
            </s:form>
        </s:if>
        <s:else>
            <s:form method="post">
                <s:submit type="button" value="true" name="listar_musica" label="Listar Musicas" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="pesquisar_musica" label="Pesquisar Musica" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="upload_musica" label="Fazer Upload" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="download_musica" label="Fazer Download" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="partilhar_musica" label="Partilhar uma Musica" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="criar_playlist" label="Criar uma Playlist" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="ver_playlist" label="Ver Playlists" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="privacidade_playlist" label="Definir privacidade de Playlist" formaction="album_normal"/><br>
                <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
            </s:form>
        </s:else>
</body>
</html>
