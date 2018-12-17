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
    <s:if test="%{#session.login_bean.Opcao_menu == 'adicionar_album'}">
        <s:form method="post" action="album">
            <s:text name="Nome do Album: "/>
            <s:textfield name="nomeDoAlbum"/><br>
            <s:text name="Descricao do Album: "/>
            <s:textfield name="descricaoDoAlbum"/><br>
            <s:text name = "dia&emsp;&emsp;"/>
            <s:text name = "mes&emsp;&emsp;"/>
            <s:text name = "ano"/><br>
            <s:textfield name="dia" size="1"/>
            <s:textfield name="mes" size="1"/>
            <s:textfield name="ano" size="1"/><br>

            <s:submit type="button" label="Adicionar" value="true" name="botao_adicionar"/>
            <s:submit type="button" label="Voltar" value="voltar" formaction="menu"/>
        </s:form>
    </s:if>
    <s:elseif test="%{#session.login_bean.Opcao_menu == 'eliminar_album'}">
        <s:form method="post" action="album">
            <s:if test="%{todosAlbuns == null}">
                <s:action name="album" executeResult="true"></s:action>
            </s:if>
            <s:else>
                <s:combobox list="listaTodosAlbuns" label="Selecione um utilizador" name="radioListaAlbuns" emptyOption="false"  headerKey="-1" headerValue="--- Please Select ---"/><br>
                <s:submit type="button" value="true" name="botaoEliminar" label="Eliminar"/>
                <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/>
            </s:else>
        </s:form>
    </s:elseif>
    <s:elseif test="%{#session.login_bean.Opcao_menu == 'editar_album'}">

    </s:elseif>
</body>
</html>
