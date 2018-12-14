<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 10/12/2018
  Time: 20:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>Pesquisar</title>
</head>
<body>
    <s:form method="post" action="pesquisar">
        <s:label name="pesquisar_por" label="Pesquisar por: "/>
        <s:text name="Escreva o nome: "/>
        <s:radio list="{'Artista', 'Musica', 'Album'}" name="pesquisar_tipo" multiple="false"/><br>
        <s:textfield name="string_pesquisar" /><br>
        <s:if test="%{resultado_pesquisa != null}">
            <s:property value="Resultado_pesquisa"/><br><%--aqui chama a funcao getResultado_pesquisa--%>
        </s:if>
        <s:submit type="button" value="true" name="pesquisar" label="Pesquisar" onclick="fazer_pesquisa()"/><br>
        <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>
    </s:form>
</body>
</html>
