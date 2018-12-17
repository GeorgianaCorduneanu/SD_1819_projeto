package menu_inicial.action;

import java.util.Map;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

public class DropboxAuthRedirect_action extends ActionSupport implements SessionAware {
    String code;
    Token acessToken;
    String verifica;
    private Login_bean login_bean;
    private Map<String, Object> session;
    public String execute(){
        setCode(code);
        if(code==null){
            return "insuccess";
        }else {
            OAuthService service = menu_inicial.rest.DropBoxRestClient.buildService();
            //setCode(code);
            Verifier verifier = new Verifier(code);
            String mail;
            acessToken = service.getAccessToken(null,verifier);
            mail = menu_inicial.rest.DropBoxRestClient.getUserMail(service,acessToken);
            if(session.get("username")==null){
                String email = menu_inicial.rest.DropBoxRestClient.getUserMail(service,acessToken);
                System.out.println("DEBUGGGGGGGGGG:"+email);
                login_bean = this.getLogin_bean();
                String resposta = login_bean.verificaMail1(email);
                String [] resposta1 = resposta.split(";");
                System.out.println("DEBUGGGGGGGGGGGGGG____"+resposta1[0]);
                switch(resposta1[0]){
                    case "true"://user existe
                        session.put("username",resposta1[1]);
                        session.put("editor",true);
                        return "success1";
                    case "false"://user nao existe
                        return "login";
                    default:
                        return "insuccess";
                }
            }else {
                String resposta = this.getLogin_bean().guardaAcessKey(acessToken.getToken(),session.get("username").toString(),mail);
                String[] resposta1 = resposta.split(";");
                switch(resposta1[0]){
                    case "success":
                        return "success";
                    case "false":
                        return "login";
                    default:
                        return "insuccess";
                }
            }
        }
    }
    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            setLogin_bean(new Login_bean());
            return (Login_bean) session.get("login_bean");

    }
    public void setLogin_bean(Login_bean login_bean){
        this.session.put("login_bean",login_bean);
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setSession(Map<String,Object> session){
        this.session = session;
    }
}
