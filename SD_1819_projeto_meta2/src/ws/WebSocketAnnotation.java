package ws;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import jdk.nashorn.internal.ir.annotations.Ignore;
import menu_inicial.RMIServerInterface.RMIServerInterface;
import menu_inicial.model.Login_bean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws") //isto e o que define o caminho de forma dinamica aqui tambem derivase que e uma classe servidor diz ao aplication server materializa-se num servidor websocket
//so se tem que se verificar o value para que o cliente possa chegar ao webserver
public class WebSocketAnnotation{
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private String username;
    private Session session;
    private RMIServerInterface server;
    private static Set<WebSocketAnnotation> users1 = new CopyOnWriteArraySet<>();

    public WebSocketAnnotation() {
        String location_s = "rmi://localhost:7000/server";

        try {
            server = (RMIServerInterface) Naming.lookup(location_s);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //isto esta a ser feito con contrutor da classe pois cada ligacao que e estabelecia independente cria uma nova instancia de uma classe dedicada para cada cliente que se liga a ele
        //se nao fosse assim o segundo cliente teria o mesmo nome que primeiro
        //define-se o nome do user
        //a variavel e atomic integer que tem um metodo getanincrement e ele incrementa 1
        /*setSession(ActionContext.getContext().getSession());
        username = (String)session_struts.get("username");
        System.out.println(username);*/
        /*Map<String, Object> session_map = ActionContext.getContext().getSession();
        System.out.println(ActionContext.getContext().getSession());*/
        //username = (String)session_map.get("username");

        //System.out.println(session_map + " : " + ActionContext.getContext().get("session"));
        //users1.add(this);
        /*HttpSession session = ServletActionContext.getRequest().getSession(false);
        username = (String)session.getAttribute("username");*/
    }
    //estes 4 metodos sao uma correspondencia direta as do cliente mas nao tem que se ir ao websocket
    //declaram-se os 4 metodos e splicam-se as respetivas anotacoes para cada evento

    @OnOpen
    public void start(Session session) {//esta session nao tem nada a ver com o struts ou o srvlets
        //e uma classe diferente com um rpopostio diferente
        //quando a ligacao e estabelecida e a sessao e passada por argumento
        //guarda.se a sessao num dos atributos da classe
        this.session = session;

        //Login_bean login_bean = new Login_bean();
        //System.out.println(login_bean.getUsername());
        //HttpSession session_s = ServletActionContext.getRequest().getSession(false);
        /*System.out.println(session_s);
        System.out.println(session_s.getAttribute("username"));*/
        /*System.out.println();
        System.out.println(ActionContext.getContext().getSession());
        System.out.println(session.getBasicRemote());
        System.out.println(session.getContainer());
        System.out.println(session.getId());
        System.out.println(session.getPathParameters().keySet());
        System.out.println(session.getUserProperties());*/
        System.out.println(session.getOpenSessions());
        System.out.println(session.getUserProperties());
       // String message = "*" + username + "* connected.";
       // sendMessage(message); //trata de enviar a astring para o cliente
    }

    @OnClose
    public void end() {
        //fecha-se os recursos que devem ser fechados
    	// clean up once the WebSocket connection is closed
        try {
            server.eliminarNOtificacao(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void receiveMessage(String message) {
        //recebe a tring enviada a partir do cliente
        //e automaticamente dispultado uando o cliente envia a mensagem tal como e feito em sendmessage
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
        System.out.println("recebeu algo: " + message);
        String [] recebe = message.split(";");
        if(recebe[0].equals("login")){
            this.username = recebe[1];
            users1.add(this);
            //fazer para receber notificacoes antigas
            try {
                this.session.getBasicRemote().sendText(username + "Esta na ws");
                String historicoNotificacao = server.verNotificacao(username);
                String [] notificacaoHistoricoSeparadas = historicoNotificacao.split(":");
                for(String item:notificacaoHistoricoSeparadas){
                    this.session.getBasicRemote().sendText(item + "\n");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(recebe[0].equals("privilegios")){
            System.out.println("recebeu privileios");
            try {
                for (WebSocketAnnotation item : users1) {
                    //caso o utilizador esteja online
                    System.out.println("Caso o utilizador esteja onliine");
                    if (item.getUsername().equals(recebe[2])) {
                        item.getSession().getBasicRemote().sendText(recebe[1] + "\n");

                        System.out.println(item.getUsername());
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(recebe[0].equals("critica")){

        }
    }

    public Set<WebSocketAnnotation> getUsers1() {
        return users1;
    }

    public void setUsers1(Set<WebSocketAnnotation> users1) {
        WebSocketAnnotation.users1 = users1;
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

    public Session getSession(){
        return this.session;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setSession(Session session){
        this.session = session;
    }
}
