<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 17/12/2018
  Time: 01:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>


<html>
<head>
    <title>Criticar Album</title>
</head>
<body>
    <s:form method="post" action="critica">
        <s:if test="%{todosAlbuns == null}">
            <s:action name="critica" executeResult="true"></s:action>
        </s:if>
        <s:else>
            <s:combobox list="listaTodosAlbuns" label="Selecione um utilizador" name="radioListaAlbuns" emptyOption="false"  headerKey="-1" headerValue="--- Please Select ---"/><br>
            <s:text name="Critica"/>
            <s:textfield name="criticaFeita"/><br>
            <s:text name="Pontuacao"/>
            <s:textfield name="pontuacaoFeita" size="1" type="number"/><br>
            <s:submit type="button" value="true" name="botaoCriticar" label="Criticar"/>
            <s:submit type="button" value="true" name="voltar" label="VOLTAR" formaction="menu"/>
        </s:else>
    </s:form>
</body>
</html>
