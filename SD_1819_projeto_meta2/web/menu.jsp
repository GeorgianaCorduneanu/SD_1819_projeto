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
    <script type="text/javascript">
        var websocket = null;
        window.onload = function () {
            connect('ws://' + window.location.host +  '/SD_1819_projeto_meta2/ws');
        }
        function connect(host) {
            if('WebSocket' in window){
                websocket = new WebSocket(host)
            }else if('MozWebSocket' in window){
                websocket = new MozWebSocket(host);
            }else{
                //caso nenhum deles nao funcionar e porque o browser nao suporta websocket
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }
            websocket.onclose = onClose;
            websocket.onmessage = onMessage;
            websocket.onopen = onOpen;
            websocket.onerror = onError;
        }
        function onClose(event) {
            writeToHistory('WebSocket closed (code ' + event.code + ').');
            console.log(event.reason);
        }
        function onOpen(event) {
            var username = "<%=(String) session.getAttribute("username")%>";
            websocket.send("login;" + username);
        }
        function onMessage(message) {
            writeToHistory(message.data);
        }
        function onError(event) {
            writeToHistory('WebSocket error.');
        }
        function doSend() {
            var message = 'notificacao'
            websocket.send(message);
        }
        function writeToHistory(text) {
            //acrexcenta conteudo a div
            var history = document.getElementById('history');
            var line = document.createElement('p');
            line.style.wordWrap = 'break-word';
            line.innerHTML = text;
            history.appendChild(line); //append do elemento a div
            history.scrollTop = history.scrollHeight;
        }
    </script>
</head>
<body>
    <s:if test="%{# session.editor == true }">
        <s:form method="post">
            <s:submit type="button" value="true" name="musica" label="MUSICA" formaction="menu"/>
            <s:submit type="button" value="true" name="artista" label="ARTISTA" formaction="menu"/>
            <s:submit type="button" value="true" name="album" label="ALBUM" formaction="menu"/>
            <s:submit type="button" value="true" name="pesquisar" label="PESQUISAR" formaction="menu"/>
            <s:submit type="button" value="true" name="privilegios" label="PRIVILEGIOS" formaction="menu"/>
            <s:submit type="button" name="logout" label="LOGOUT" formaction="index"/>
        </s:form>
    </s:if>
    <s:elseif test="%{ #session.editor == false}">
        <s:form method="post">
            <s:submit type="button" value="true" name="musica" label="MUSICA" formaction="menu"/>
            <s:submit type="button" value="true" name="pesquisar" label="PESQUISAR" formaction="menu"/>
            <s:submit type="button" name="logout" label="LOGOUT" formaction="index"/>
        </s:form>
    </s:elseif>
</body>
<div>
    <div id="container"><div id="history"></div></div>
</div>
</html>
