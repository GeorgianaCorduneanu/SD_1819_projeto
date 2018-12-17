package menu_inicial.action;

import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import menu_inicial.model.Login_bean;
import menu_inicial.rest.DropBoxRestClient;
import org.apache.struts2.interceptor.SessionAware;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class Associar_action extends DropBoxRestClient implements SessionAware {
    private Login_bean login_bean;
    private Map<String, Object> session;
    private String todas_musicas,dropmusic_associar,musica,filepath;
    private List<String> lista_todas_musicas;


    public String execute() {
        String fileID;
        String aToken;
        login_bean = getLogin_bean();

        OAuthService service = buildService();
        aToken = login_bean.getAcessToken();
        Token acessToken = new Token(aToken,"");

        fileID = getFileId(service,acessToken,filepath);
        System.out.println(musica+";"+fileID);
        return login_bean.guardaMusicaID(musica, fileID);
    }
    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }
    public List<String> getLista_todas_musicas() {
        System.out.println(login_bean.getTodos_utilizadores());
        return login_bean.getLista_todos_utilizadores();
    }

    public void setLista_todas_musicas(List<String> lista_todas_musicas) {
        this.lista_todas_musicas = lista_todas_musicas;
    }
    public String getTodas_musicas() {
        todas_musicas = login_bean.getTodasMusicas();
        System.out.println(todas_musicas);
        return todas_musicas;
    }

    public void setTodos_utilizadores(String todas_musicas) {
        this.todas_musicas = login_bean.getTodos_utilizadores();
    }
    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public void setDropmusic_associar(String dropmusic_associar) {
        this.dropmusic_associar = dropmusic_associar;
    }

    public void setMusica(String musica) {
        this.musica = musica;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
