package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class Login_action extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username = null, password = null;
    boolean editor=false;

    @Override
    public String execute() {
        // any username is accepted without confirmation (should check using RMI)
        if(this.username != null && !username.equals("") && this.password != null) {
                this.getLogin_bean().setUsername(this.username);
                this.getLogin_bean().setPassword(this.password);
                //if(this.getLogin_bean().getUserMatchesPassword()) {/*tratar de remote exception*/

            int aux = 0;//0-nao existe 1-utilizador sem privilegios 2-utilizador com privilegios
            try {
                aux = this.getLogin_bean().getUserMatchesPassword();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if(aux==1 || aux==2) {
                    session.put("username", username);
                    if(aux==1)
                        session.put("editor", false);
                    else if(aux==2)
                        session.put("editor", true);
                    session.put("loggedin", true); // this marks the user as logged in
                    System.out.println("Encontrou");
                    return SUCCESS;
                }
        }
        return "insuccess";
    }

    public void setUsername(String username) {
        this.username = username; // will you sanitize this input? maybe use a prepared statement?
    }

    public void setPassword(String password) {
        this.password = password; // what about this input?
    }

    public Login_bean getLogin_bean() {
        //verificar se alguém já tem a sessao iniciada
        if(!session.containsKey("login_bean"))
            this.setLogin_bean(new Login_bean());
        return (Login_bean) session.get("login_bean");
    }

    public void setLogin_bean(Login_bean login_bean) {
        this.session.put("login_bean", login_bean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
/*

    <c:forEach items="${login_bean.login}" var="value">
        <c:out value="${value}" /><br>
        <jsp:useBean id="login_bean" scope="request" type="menu_inicial.model.Login_bean"/>
    <c:forEach items="${login_bean.allUsers}" var="value">
        <c:out value="${value}" /><br>
    </c:forEach>
    */