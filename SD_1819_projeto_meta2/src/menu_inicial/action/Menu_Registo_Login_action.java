package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Menu_Registo_Login_action extends ActionSupport implements SessionAware {    private Map<String, Object> session;

    public String execute(){
        return null;
    }

    @Override
    public void setSession(Map<String, Object> map) {
    }
}
