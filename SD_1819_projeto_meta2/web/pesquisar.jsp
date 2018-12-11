<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 10/12/2018
  Time: 20:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Pesquisar</title>
</head>
<body>
    <s:form method="post" action="pesquisar">
        <s:label name="pesquisar_por" label="Pesquisar por: "/>
        <s:radio list="{'Artista', 'Musica', 'Album'}" name="pesquisar_tipo" multiple="false"/><br>
        <s:text name="Escreva o titulo"/>
        <s:textfield name="string_pesquisar" /><br>
       <%-- <s:property value="Pesquisar_action.resultado_pesquisa"/>--%>
        <s:submit type="button" value="true" name="pesquisar" label="Pesquisar" onclick="fazer_pesquisa()"/><br>
        <s:submit type="button" value="true" name="voltar" label="VOLTAR"/><br>
    </s:form>
</body>
</html>
