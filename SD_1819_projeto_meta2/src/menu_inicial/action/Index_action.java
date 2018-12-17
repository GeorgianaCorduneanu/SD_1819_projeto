package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Index_action extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private Login_bean login_bean;


    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
        this.session.put("loggedout", true);
    }
}
