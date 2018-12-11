package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Menu_action extends ActionSupport implements SessionAware {
    private boolean musica, artista, album, pesquisar;
    private String pesquisar_tipo;
    private Map<String, Object> session;

    public String execute(){
        if(musica) {
            session.put("opcao_menu", "musica");
            return SUCCESS;
        }if(album) {
            session.put("opcao_menu", "album");
            return SUCCESS;
        }if(artista) {
            session.put("opcao_menu", "artista");
            return SUCCESS;
        }if(pesquisar) {
            session.put("opcao_menu", "pesquisar");
            return "pesquisar";
        }
        return "insuccess";
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
