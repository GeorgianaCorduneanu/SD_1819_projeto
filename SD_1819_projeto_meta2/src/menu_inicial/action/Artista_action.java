package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Artista_action extends ActionSupport implements SessionAware {
    private boolean pesquisar_artista, adicionar_artista, eliminar_artista, editar_artista;
    private Map<String, Object> session;

    public String execute(){
        if(pesquisar_artista || (boolean)session.get("editor"))
            return "pesquisar_artista";
        else if(adicionar_artista)
            return "adicionar_artista";
        else if(eliminar_artista)
            return "eliminar_artista";
        else if(editar_artista)
            return "editar_artista";
        return "insuccess";
    }
    public void setEditar_artista(boolean editar_artista){
        this.editar_artista = editar_artista;
    }
    public void setEliminar_artista(boolean eliminar_artista){
        this.eliminar_artista=eliminar_artista;
    }
    public void setAdicionar_artista(boolean adicionar_artista){
        this.adicionar_artista = adicionar_artista;
    }
    public void setPesquisar_artista(boolean pesquisar_artista){
        this.pesquisar_artista = pesquisar_artista;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
