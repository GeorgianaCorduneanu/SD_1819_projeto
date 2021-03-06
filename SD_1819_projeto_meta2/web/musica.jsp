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
        <s:form method="post" action="musica">
            <s:if test="%{todasMusicas == null}">
                <s:action name="musica" executeResult="true"></s:action>
            </s:if>
            <s:else>
                <s:combobox list="listaMusicas" label="Selecione uma Musica" name="radioListaMusicas" emptyOption="false"  headerKey="-1" headerValue="--- Please Select ---"/><br>
                <s:submit type="button" value="true" name="botaoEliminar" label="Eliminar"/>
                <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/>
            </s:else>
        </s:form>
    </s:elseif>
    <s:elseif test="%{#session.login_bean.Opcao_menu == 'editar_musica'}">
        <s:form method="post" action="musica">
            <s:if test="%{todasMusicas == null}">
                <s:action name="musica" executeResult="true"></s:action>
            </s:if>
            <s:else>
                <s:radio list="{'Nome da Musica','Nome do Compositor', 'Duracao da Musica'}" name="radioMusicaEditar" multiple="false"/><br>
                <s:combobox list="listaMusicas" label="Selecione um utilizador" name="radioListaMusicas" emptyOption="false"  headerKey="-1" headerValue="--- Please Select ---"/>
                <s:textfield name="componenteMudar"/><br>
                <s:submit type="button" value="true" name="botaoEditar" label="Editar" />
                <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/>
            </s:else>
        </s:form>
    </s:elseif>
</body>
</html>