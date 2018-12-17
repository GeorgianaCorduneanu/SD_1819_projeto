package menu_inicial.action;

import menu_inicial.rest.DropBoxRestClient;
import org.apache.struts2.interceptor.SessionAware;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import menu_inicial.model.Login_bean;
import menu_inicial.rest.DropBoxRestClient;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;
public class Play_action extends DropBoxRestClient implements SessionAware{
    private String musica,url;
    private Map<String, Object> session;
    private Login_bean login_bean;

    public String execute(){
        String path,aToken,id,folder;
        String [] url_parte,partes;
        login_bean = getLogin_bean();
        OAuthService service = buildService();

        aToken = login_bean.getAcessTokenByUsername(session.get("username").toString());
        Token accessToken = new Token(aToken,"");

        id = login_bean.getFileIDByMusic(musica);
        System.out.println("---DEBUG: "+ id);
        path = getFilePathUsingID(service,accessToken,id);
        url_parte = path.split(":");
        if(url_parte[0].equals("https")){
            url = path;
            return "redirect";

        }else {
            partes = path.split("/");
            int n = partes.length;
            folder = "";
            String file = partes[n-1];
            for(int i=0;i<n-1;i++){
                if(i==n-1){
                    folder+=partes[i];
                }
                else{
                    folder+=partes[i];
                    folder+="/";
                }
            }
            url = "https://www.dropbox.com/home"+folder+"?preview="+file;
            System.out.println("DEBUG URL:"+ url);
            return "redirect";
        }
    }
    public void setMusica(String musica) {
        this.musica = musica;
    }
    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
