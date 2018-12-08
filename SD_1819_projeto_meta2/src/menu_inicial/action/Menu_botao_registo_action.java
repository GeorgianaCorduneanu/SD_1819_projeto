package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Menu_botao_registo_action extends ActionSupport implements SessionAware {
    public String execute(){
        return SUCCESS;
    }
    @Override
    public void setSession(Map<String, Object> map) {

    }
}
