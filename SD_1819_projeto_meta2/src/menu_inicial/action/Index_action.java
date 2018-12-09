package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Index_action extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
        this.session.put("loggedout", true);
    }
}
