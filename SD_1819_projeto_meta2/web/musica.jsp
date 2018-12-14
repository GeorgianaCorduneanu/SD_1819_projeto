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
    <s:if test="%{#session.login_bean.Opcao_menu == 'adicionar_musica'}">
        <s:form method="post" action="musica">
            <s:text name="Nome da Musica: "/>
            <s:textfield name="nomeDaMusica"/><br>
            <s:text name="Nome do Compositor: "/>
            <s:textfield name="nomeDoCompositor"/><br>
            <s:text name="Durancao da Musica: "/>
            <s:textfield name="duracaoDaMusica"/><br>
            <s:submit type="button" label="Adicionar"/>
            <s:submit type="button" label="Voltar" value="voltar" formaction="menu"/>
        </s:form>
    </s:if>
    <s:elseif test="%{#session.login_bean.Opcao_menu == 'eliminar_musica'}">

    </s:elseif>
    <s:elseif test="%{#session.login_bean.Opcao_menu == 'editar_musica'}">

    </s:elseif>
</body>
</html>