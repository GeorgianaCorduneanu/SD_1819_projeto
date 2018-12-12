package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Menu_action extends ActionSupport implements SessionAware {
    private boolean musica, artista, album, pesquisar, dar_privilegios;
    private String pesquisar_tipo;
    private Map<String, Object> session;
    private Login_bean login_bean;

    public String execute(){
        login_bean = getLogin_bean();
        if(musica) {
            login_bean.setOpcao_menu("musica");
            return SUCCESS;
        }if(album) {
            login_bean.setOpcao_menu("album");
            return SUCCESS;
        }if(artista) {
            login_bean.setOpcao_menu("artista");
            return SUCCESS;
        }if(dar_privilegios){
            login_bean.setOpcao_menu("dar_privilegios");
        }if(pesquisar) {
            login_bean.setOpcao_menu("pesquisar");
            return "pesquisar";
        }
        return "insuccess";
    }
    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }
    public void setDar_privilegios(boolean dar_privilegios) {
        this.dar_privilegios = dar_privilegios;

    }
    public void setPesquisar_tipo(String pesquisar_tipo) {
        this.pesquisar_tipo = pesquisar_tipo;
    }

    public void setMusica(boolean musica){
        this.musica = musica;
    }

    public void setArtista(boolean artista){
        this.artista = artista;
    }

    public void setAlbum(boolean album){
        this.album = album;
    }

    public void setPesquisar(boolean pesquisar){this.pesquisar=pesquisar;}

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
