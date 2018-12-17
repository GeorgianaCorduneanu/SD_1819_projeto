<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 10/12/2018
  Time: 20:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <s:if test="%{# session.login_bean.Opcao_menu == 'artista' }">
        <s:form method="post">
            <s:submit type="button" value="true" name="adicionar_artista" label="Adicionar Artista" formaction="artista"/><br>
            <s:submit type="button" value="true" name="eliminar_artista" label="Eliminar Artista" formaction="artista"/><br>
            <s:submit type="button" value="true" name="editar_artista" label="Editar Artista" formaction="artista"/><br>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
        </s:form>
    </s:if>
    <s:elseif test="%{# session.login_bean.Opcao_menu == 'album' }">
        <s:form method="post">
            <s:submit type="button" value="true" name="adicionar_album" label="Adicionar Album" formaction="album"/><br>
            <s:submit type="button" value="true" name="eliminar_album" label="Eliminar Album" formaction="album"/><br>
            <s:submit type="button" value="true" name="editar_album" label="Editar Album" formaction="album"/><br>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
        </s:form>
    </s:elseif>
    <s:elseif test="%{# session.login_bean.Opcao_menu == 'musica' || session.editor==true}">
        <s:form method="post">
            <s:submit type="button" value="true" name="adicionar_musica" label="Adicionar Musica" formaction="musica"/><br>
            <s:submit type="button" value="true" name="eliminar_musica" label="Eliminar Musica" formaction="musica"/><br>
            <s:submit type="button" value="true" name="editar_musica" label="Editar Musica" formaction="musica"/><br>
            <s:submit type="button" value="true" name="upload_musica" label="Fazer Upload" formaction="musica"/><br>
            <s:submit type="button" value="true" name="download_musica" label="Fazer Download" formaction="musica"/><br>
            <s:submit type="button" value="true" name="partilhar_musica" label="Partilhar uma Musica" formaction="musica"/><br>
           <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
        </s:form>
    </s:elseif>
    <s:else>

        <s:form method="post">
            <s:submit type="button" value="true" name="upload_musica" label="Fazer Upload" formaction="musica"/><br>
            <s:submit type="button" value="true" name="download_musica" label="Fazer Download" formaction="musica"/><br>
            <s:submit type="button" value="true" name="partilhar_musica" label="Partilhar uma Musica" formaction="musica"/><br>
            <s:submit type="button" value="true" name="criar_playlist" label="Criar Playlist" formaction="musica"/><br>
            <s:submit type="button" value="true" name="ver_playlist" label="Ver Playlist" formaction="musica"/><br>
            <s:submit type="button" value="true" name="editar_playlist" label="Editar PLaylist" fomraction="musica"/><br>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
        </s:form>
    </s:else>
</body>
</html>
