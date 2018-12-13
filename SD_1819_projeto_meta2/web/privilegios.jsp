<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 12/12/2018
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>Privilegios</title>
</head>
<body>
    <s:form method="post" action="privilegios">
        <s:if test="%{todos_utilizadores == null}">
            <s:action name="privilegios" executeResult="true"></s:action>
        </s:if>
        <s:else>
            <s:radio list="{'Dar Privilegios', 'Remover Privilegios'}" name="privilegios_tipo" multiple="false"/><br>
            <s:combobox list="lista_todos_utilizadores" label="Selecione um utilizador" name="lista_todos_utilizadores" emptyOption="false"  headerKey="-1" headerValue="--- Please Select ---"/>
            <s:submit type="button" value="true" name="mudar" label="Mudar"/><br>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/><br>

        </s:else>
   </s:form>
</body>
</html>
