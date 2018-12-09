package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Musica_action extends ActionSupport implements SessionAware {
    private boolean pesquisar_musica, adicionar_musica, eliminar_musica, editar_musica;
    private Map<String, Object> session;

    public String execute(){
        if(pesquisar_musica)
            return "pesquisar_musica";
        else if(adicionar_musica)
            return "adicionar_musica";
        else if(eliminar_musica)
            return "eliminar_musica";
        else if(editar_musica)
            return "editar_musica";
        return "insuccess";
    }
    public void setEditar_musica(boolean editar_musica){
        this.editar_musica = editar_musica;
    }
    public void setEliminar_artista(boolean eliminar_musica){
        this.eliminar_musica=eliminar_musica;
    }
    public void setAdicionar_artista(boolean adicionar_musica){
        this.adicionar_musica = adicionar_musica;
    }
    public void setPesquisar_artista(boolean pesquisar_musica){
        this.pesquisar_musica = pesquisar_musica;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
