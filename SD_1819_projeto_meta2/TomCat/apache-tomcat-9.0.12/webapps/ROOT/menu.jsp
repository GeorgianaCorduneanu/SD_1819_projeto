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
    <s:if test="%{# session.editor == true }">
        <s:form method="post">
            <s:submit type="button" value="true" name="musica" label="MUSICA & PLAYLIST" formaction="menu"/>
            <s:submit type="button" value="true" name="artista" label="ARTISTA" formaction="menu"/>
            <s:submit type="button" value="true" name="album" label="ALBUM" formaction="menu"/>
            <s:submit type="button" value="true" name="pesquisar" label="PESQUISAR" formaction="menu"/>
            <s:submit type="button" value="true" name="privilegios" label="PRIVILEGIOS" formaction="menu"/>
            <s:submit type="button" value="true" name="play" label="PLAY MUSICA" formaction="menu"/>
            <s:submit type="button" value="true" name="share" label="PARTILHAR MUSICA" formaction="menu"/>
            <s:submit type="button" value="true" name="associar" label="ASSOCIAR MUSICA" formaction="menu"/>
            <s:submit type="button" value="true" name="dbxauth" label="DROPBOX" formaction="menu"/>
            <s:submit type="button" name="logout" label="LOGOUT" formaction="index"/>
        </s:form>
    </s:if>
    <s:elseif test="%{ #session.editor == false}">
        <s:form method="post">
            <s:submit type="button" value="true" name="musica" label="MUSICA & PLAYLIST" formaction="menu"/>
            <s:submit type="button" value="true" name="pesquisar" label="PESQUISAR" formaction="menu"/>
            <s:submit type="button" value="true" name="upload" label="UPLOAD MUSICA" formaction="menu"/>
            <s:submit type="button" value="true" name="download" label="DOWNLOAD MUSICA" formaction="menu"/>
            <s:submit type="button" value="true" name="dropboxauth" label="DROPBOX" formaction="menu"/>
            <s:submit type="button" name="logout" label="LOGOUT" formaction="index"/>
        </s:form>
    </s:elseif>
</body>
</html>
