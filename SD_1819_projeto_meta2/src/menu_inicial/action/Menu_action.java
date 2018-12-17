package menu_inicial.action;

import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import menu_inicial.rest.DropBoxRestClient;


import java.util.Map;

public class Menu_action extends ActionSupport implements SessionAware {
    private boolean musica, artista, album, pesquisar, privilegios, dbxauth, associar, share, play;
    private String pesquisar_tipo;
    private Map<String, Object> session;
    private Login_bean login_bean;
    private String url;

    public String getUrl() {
        return url;
    }


    public String execute() {
        login_bean = getLogin_bean();
        if (musica) {
            login_bean.setOpcao_menu("musica");
            return SUCCESS;
        }
        if (album) {
            login_bean.setOpcao_menu("album");
            return SUCCESS;
        }
        if (artista) {
            login_bean.setOpcao_menu("artista");
            return SUCCESS;
        }
        if (privilegios) {
            return "privilegios";
        }
        if (pesquisar) {
            return "pesquisar";
        }
        if (dbxauth) {
            OAuthService service = menu_inicial.rest.DropBoxRestClient.buildService();
            url = service.getAuthorizationUrl(null);
            return "dbxauth";
        }
        if (associar) {
            return "associar";
        }
        if (share) {
            return "share";
        }
        if (play){
            return "play";
        }
        return "insuccess";
    }


    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }

    public void setPrivilegios(boolean privilegios) {
        this.privilegios = privilegios;
    }
    public void setDbxauth(boolean dbxauth) {
        this.dbxauth = dbxauth;
    }
    public void setShare(boolean share) {
        this.share = share;
    }
    public void setAssociar(boolean associar) {
        this.associar = associar;
    }

    public void setMusica(boolean musica){
        this.musica = musica;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setArtista(boolean artista){
        this.artista = artista;
    }
    public void setPlay(boolean play){
        this.play = play;
    }
    public void setAlbum(boolean album){
        this.album = album;
    }

    public void setPesquisar_tipo(String pesquisar_tipo) {
        this.pesquisar_tipo = pesquisar_tipo;
    }

    public void setPesquisar(boolean pesquisar){this.pesquisar=pesquisar;}

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
