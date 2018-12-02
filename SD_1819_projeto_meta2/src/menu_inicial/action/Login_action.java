package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Login_action extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username = null, password = null;

    @Override
    public String execute() {
        // any username is accepted without confirmation (should check using RMI)
        if(this.username != null && !username.equals("") && this.password != null/**/) {
            this.getLogin_bean().setUsername(this.username);
            this.getLogin_bean().setPassword(this.password);
            /*if(this.getheybea()getusermarchespassword tratar de remote exception*/
            session.put("username", username);
            session.put("loggedin", true); // this marks the user as logged in
            return SUCCESS;
        }
        else
            return LOGIN;
    }

    public void setUsername(String username) {
        this.username = username; // will you sanitize this input? maybe use a prepared statement?
    }

    public void setPassword(String password) {
        this.password = password; // what about this input?
    }

    public Login_bean getLogin_bean() {
        if(!session.containsKey("login_bean"))
            this.setLogin_bean(new Login_bean());
        return (Login_bean) session.get("heyBean");
    }

    public void setLogin_bean(Login_bean login_bean) {
        this.session.put("login_bean", login_bean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
