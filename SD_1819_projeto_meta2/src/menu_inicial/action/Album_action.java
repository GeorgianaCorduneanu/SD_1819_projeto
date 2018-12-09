package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Album_action extends ActionSupport implements SessionAware {
    private boolean listar_musica, pesquisar_musica, upload_musica, download_musica, partilhar_musica, criar_playlist, ver_playlist, privacidade_playlist, voltar;
    private boolean adicionar_musica, editar_musica, eliminar_musica;
    private Map<String, Object> session;
    public String execute(){
        if(pesquisar_musica)
            return "pesquisar_musica";
        else if(voltar)
            return "insuccess";
        else if(privacidade_playlist)
            return "privacidade_playlist";
        else if(listar_musica)
            return "listar_musica";
        else if(upload_musica)
            return "upload_musica";
        else if(download_musica)
            return "download_musica";
        else if(criar_playlist)
            return "criar_playlist";
        else if(ver_playlist)
            return "ver_playlist";
        else if(adicionar_musica)
            return "adicionar_musica";
        else if(eliminar_musica)
            return "eliminar_musica";
        else if(editar_musica){
            return "editar_musica";
        }
        return "insuccess";
    }
    public void setEliminar_musica(boolean eliminar_musica){
        this.eliminar_musica = eliminar_musica;
    }
    public void setEditar_musica(boolean editar_musica){
        this.editar_musica = editar_musica;
    }
    public void setAdicionar_musica(boolean adicionar_musica){
        this.adicionar_musica = adicionar_musica;
    }
    public void setPesquisar_musica(boolean pesquisar_musica){
        this.pesquisar_musica = pesquisar_musica;

    }
    public void setVoltar(boolean voltar){
        this.voltar = voltar;
    }
    public void setPrivacidade_playlist(boolean privacidade_playlist){
        this.privacidade_playlist = privacidade_playlist;
    }
    public void setVer_playlist(boolean ver_playlist){
        this.ver_playlist = ver_playlist;
    }
    public void setCriar_playlist(boolean criar_playlist){
        this.criar_playlist = criar_playlist;
    }
    public void setPartilhar_musica(boolean partilhar_musica){
        this.partilhar_musica = partilhar_musica;
    }
    public void setDownload_musica(boolean download_musica){
        this.download_musica = download_musica;
    }
    public void setUpload_musica(boolean upload_musica){
        this.upload_musica = upload_musica;
    }
    public void setListar_musica(boolean listar_musica){
        this.listar_musica = listar_musica;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
