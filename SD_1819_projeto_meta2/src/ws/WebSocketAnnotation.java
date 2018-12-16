package ws;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws") //isto e o que define o caminho de forma dinamica aqui tambem derivase que e uma classe servidor diz ao aplication server materializa-se num servidor websocket
//so se tem que se verificar o value para que o cliente possa chegar ao webserver
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    private static final Set<WebSocketAnnotation> users1 = new CopyOnWriteArraySet<>();


    public WebSocketAnnotation() {
        //isto esta a ser feito con contrutor da classe pois cada ligacao que e estabelecia independente cria uma nova instancia de uma classe dedicada para cada cliente que se liga a ele
        //se nao fosse assim o segundo cliente teria o mesmo nome que primeiro
        //define-se o nome do user
        //a variavel e atomic integer que tem um metodo getanincrement e ele incrementa 1
        username = "User" + sequence.getAndIncrement();
        users1.add(this);
    }
    //estes 4 metodos sao uma correspondencia direta as do cliente mas nao tem que se ir ao websocket
    //declaram-se os 4 metodos e splicam-se as respetivas anotacoes para cada evento

    @OnOpen
    public void start(Session session) {//esta session nao tem nada a ver com o struts ou o srvlets
        //e uma classe diferente com um rpopostio diferente
        //quando a ligacao e estabelecida e a sessao e passada por argumento
        //guarda.se a sessao num dos atributos da classe
        this.session = session;
        String message = "*" + username + "* connected.";
        sendMessage(message); //trata de enviar a astring para o cliente
    }

    @OnClose
    public void end() {
        //fecha-se os recursos que devem ser fechados
    	// clean up once the WebSocket connection is closed
    }

    @OnMessage
    public void receiveMessage(String message) {
        //recebe a tring enviada a partir do cliente
        //e automaticamente dispultado uando o cliente envia a mensagem tal como e feito em sendmessage
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
        String upperCaseMessage = message.toUpperCase();
        sendMessage("[" + username + "] " + upperCaseMessage); //trata a string que o cliente enviou para o servidor
    }
    
    @OnError
    public void handleError(Throwable t) {
        //podese fazer fecha de recursos
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
    	try {
			//this.session.getBasicRemote().sendText(text);  //getbasicremote danos uma referencia que tem varios metodos que nos permite enviar mensagens para o cliente
		    //o metodo on message e dispultado automaticamente no cliente do metodo onmessage quando isto e feito
            //permitenos enviar objetos mas nao e limpa pois precisamos de ter outra classe a parte que vai fazer encoding e decoding
            //
    	    this.session.getBasicRemote().sendText(text);
    	} catch (IOException e) {

			// clean up once the WebSocket connection is closed
			try {
			    //quando der erro faz-se isto
                //caso se queira envoar um texto e a socket tenha sido fecha ele da erro e vem aqui
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
}
