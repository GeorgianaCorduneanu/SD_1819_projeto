package menu_inicial.action;

import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Index_action extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String url;
    private  boolean dbxauth,login,registo;
    private Login_bean login_bean;

    public String getUrl() {
        return url;
    }
    public String execute() {
        login_bean = getLogin_bean();
        if(dbxauth){
            OAuthService service = menu_inicial.rest.DropBoxRestClient.buildService();
            url = service.getAuthorizationUrl(null);
            return "dbxauth";
        }else if(login){
            return "login";
        }else if(registo){
            return "registo";
        }
        return "insuccess";
    }
    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }
    public void setDbxauth(boolean dbxauth) {
        this.dbxauth = dbxauth;
    }
    public void setLogin(boolean login) {
        this.login = login;
    }
    public void setRegisto(boolean registo) {
        this.registo = registo;
    }
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
        this.session.put("loggedout", true);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
