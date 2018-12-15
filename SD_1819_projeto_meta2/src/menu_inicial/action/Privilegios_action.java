package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;
import sun.awt.windows.ThemeReader;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
