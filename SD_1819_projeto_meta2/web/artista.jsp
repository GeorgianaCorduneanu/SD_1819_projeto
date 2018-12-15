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
    <s:if test="%{#session.login_bean.Opcao_menu == 'adicionar_artista'}">
        <s:form method="post" action="artista">
            <s:text name="Nome do Artista: "/>
            <s:textfield name="nomeDoArtista"/><br>
            <s:radio list="{'Compositor', 'Nao Compositor'}" name="tipoCompositor" multiple="false"/><br>
            <s:text name="Informacao Adicional"/>
            <s:textfield name="informacaoAdicional"/>
            <s:submit type="button" label="Adicionar" value="true" name="botaoAdicionar"/>
            <s:submit type="button" label="Voltar" value="voltar" formaction="menu"/>
        </s:form>
    </s:if>
    <s:elseif test="%{#session.login_bean.Opcao_menu == 'eliminar_artista'}">
        <s:form method="post" action="artista">
            <s:if test="%{todosArtistas == null}">
                <s:action name="artista" executeResult="true"></s:action>
            </s:if>
            <s:else>
                <s:combobox list="listaArtistas" label="Selecione um utilizador" name="radioListaArtistas" emptyOption="false"  headerKey="-1" headerValue="--- Please Select ---"/><br>
                <s:submit type="button" value="true" name="botaoEliminar" label="Eliminar"/>
                <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/>
            </s:else>
        </s:form>
    </s:elseif>
    <s:elseif test="%{#session.login_bean.Opcao_menu == 'editar_artista'}">
        <s:form method="post" action="artista">
            <s:if test="%{todosArtistas == null}">
                <s:action name="artista" executeResult="true"/>
            </s:if>
            <s:else>
                <s:radio list="{'Nome','Descricao'}" name="radioTipoData"/><br>
                <s:combobox list="listaArtistas" label="Selecione um utilizador" name="radioListaArtistas" emptyOption="false"  headerKey="-1" headerValue="--- Please Select ---"/>
                <s:textfield name="componenteMudar"/><br>
                <s:submit type="button" value="true" name="botaoMudar" label="Editar"/>
                <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/>
            </s:else>
        </s:form>
    </s:elseif>
</body>
</html>