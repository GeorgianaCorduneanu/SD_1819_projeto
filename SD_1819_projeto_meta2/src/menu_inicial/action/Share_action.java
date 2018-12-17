package menu_inicial.action;

import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import menu_inicial.model.Login_bean;
import menu_inicial.rest.DropBoxRestClient;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Share_action extends DropBoxRestClient implements SessionAware {
    private String musica,username;
    private Map<String, Object> session;
    private Login_bean login_bean;

    public String execute(){
        String aToken,id,mail;
        login_bean = getLogin_bean();
        OAuthService service = buildService();
        //aToken = login_bean.getAcessToken();//rip
        aToken = login_bean.getAcessTokenByUsername(username);
        Token accessToken = new Token(aToken,"");

        id = login_bean.getFileIDByMusic(musica);
        mail = getUserMail(service,accessToken);
        System.out.println("DEBUG: "+session.get("accessToken"));
        System.out.println("DEBUG: "+mail);
        shareWithEmail(service,(Token) session.get("accessToken"),mail,id);


        return "success";
    }

    public void setMusica(String musica) {
        this.musica = musica;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }
    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

}

