package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;
import sun.awt.windows.ThemeReader;
import ws.WebSocketAnnotation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Privilegios_action extends ActionSupport implements SessionAware {
    private String privilegios_tipo;
    private Login_bean login_bean;
    private Map<String, Object> session;
    private String todos_utilizadores;
    private List<String> lista_todos_utilizadores;
    private boolean mudar;
    private String utilizador_privilegio;

    public String execute(){
        login_bean = getLogin_bean();
        if(utilizador_privilegio!=null && privilegios_tipo!=null && mudar) {
            System.out.println(utilizador_privilegio + " : " + privilegios_tipo + " : " + mudar);
            if(privilegios_tipo.equals("Dar Privilegios")){
                login_bean.gerirPrivilegios(utilizador_privilegio, 1);
            }else if(privilegios_tipo.equals("Remover Privilegios")){
                login_bean.gerirPrivilegios(utilizador_privilegio, 0);
            }

            WebSocketAnnotation ws = new WebSocketAnnotation();
            Set<WebSocketAnnotation> listaUsers = ws.getUsers1();

            for(WebSocketAnnotation item:listaUsers){
                if(item.getUsername().equals(utilizador_privilegio)) {
                    String mensagem = "privilegios;Os teus privilegios foram mudados";
                    System.out.println("Antes de enviar para inserir na notificacao: " + mensagem);
                    login_bean.inserirNotificacao(utilizador_privilegio, mensagem);
                    try {
                        item.getSession().getBasicRemote().sendText(mensagem);
                        return SUCCESS;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(mensagem);
                }
            }
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String mensagem;
            if(privilegios_tipo.equals("Dar Privilegios"))
                mensagem = "Teus privilegios foram mudados, parabens es editor [ " + sdf.format(cal.getTime()) + " ]" + ", " + login_bean.getUsername();
            else
                mensagem = "Teus privilegios foram mudados, lamento ja nao es editor [ " + sdf.format(cal.getTime()) + " ]" + ", " + login_bean.getUsername();
            System.out.println("Antes de enviar para inserir na notificacao: " + mensagem + ", " + login_bean.getUsername());
            login_bean.inserirNotificacao(utilizador_privilegio, mensagem);
            return SUCCESS;
        }
        return "insuccess";
    }


    public List<String> getLista_todos_utilizadores() {
        System.out.println(login_bean.getTodos_utilizadores());
        return login_bean.getLista_todos_utilizadores();
    }

    public void setLista_todos_utilizadores(List<String> lista_todos_utilizadores) {
        this.lista_todos_utilizadores = lista_todos_utilizadores;
    }

    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }

    public String getPrivilegios_tipo() {
        return privilegios_tipo;
    }

    public void setPrivilegios_tipo(String privilegios_tipo) {
        this.privilegios_tipo = privilegios_tipo;
    }

    public String getTodos_utilizadores() {
        todos_utilizadores = login_bean.getTodos_utilizadores();
        System.out.println(todos_utilizadores);
        return todos_utilizadores;
    }

    public void setTodos_utilizadores(String todos_utilizadores) {
        this.todos_utilizadores = login_bean.getTodos_utilizadores();
    }

    public String getUtilizador_privilegio() {
        return utilizador_privilegio;
    }

    public void setUtilizador_privilegio(String utilizador_privilegio){
        this.utilizador_privilegio = utilizador_privilegio;
    }

    public boolean isMudar() {
        return mudar;
    }

    public void setMudar(boolean mudar) {
        this.mudar = mudar;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
