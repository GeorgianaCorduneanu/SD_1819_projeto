package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Artista_action extends ActionSupport implements SessionAware {
    private boolean adicionar_artista, eliminar_artista, editar_artista, botaoAdicionar;
    private Map<String, Object> session;
    private String informacaoAdicional, nomeDoArtista, tipoCompositor;
    private Login_bean login_bean;

    public String execute(){
        String mensagem;
        login_bean = getLogin_bean();
        if(nomeDoArtista!=null && tipoCompositor!=null && botaoAdicionar){
            if(tipoCompositor.equals("Compositor")){
                mensagem = login_bean.inserirArtista(nomeDoArtista, true, informacaoAdicional);
            }else{
                mensagem = login_bean.inserirArtista(nomeDoArtista, false, informacaoAdicional);
            }
            if(mensagem.equals("Artista Adicionado"))
                return SUCCESS;
        }else if(adicionar_artista){
            mensagem = "adicionar_artista";
            login_bean.setOpcao_menu(mensagem);
            return mensagem;
        }else if(eliminar_artista) {
            mensagem = "eliminar_artista";
            login_bean.setOpcao_menu(mensagem);
            return mensagem;
        }else if(editar_artista) {
            mensagem = "editar_artista";
            login_bean.setOpcao_menu(mensagem);
            return mensagem;
        }
        return "insuccess";
    }

    public boolean isBotaoAdicionar() {
        return botaoAdicionar;
    }

    public void setBotaoAdicionar(boolean botaoAdicionar) {
        this.botaoAdicionar = botaoAdicionar;
    }

    public String getNomeDoArtista() {
        return nomeDoArtista;
    }

    public void setNomeDoArtista(String nomeDoArtista) {
        this.nomeDoArtista = nomeDoArtista;
    }

    public String getTipoCompositor() {
        return tipoCompositor;
    }

    public void setTipoCompositor(String tipoCompositor) {
        this.tipoCompositor = tipoCompositor;
    }

    public String getInformacaoAdicional() {
        return informacaoAdicional;
    }

    public void setInformacaoAdicional(String informacaoAdicional) {
        this.informacaoAdicional = informacaoAdicional;
    }

    public boolean isAdicionar_artista() {
        return adicionar_artista;
    }

    public void setAdicionar_artista(boolean adicionar_artista) {
        this.adicionar_artista = adicionar_artista;
    }

    public boolean isEliminar_artista() {
        return eliminar_artista;
    }

    public void setEliminar_artista(boolean eliminar_artista) {
        this.eliminar_artista = eliminar_artista;
    }

    public boolean isEditar_artista() {
        return editar_artista;
    }

    public void setEditar_artista(boolean editar_artista) {
        this.editar_artista = editar_artista;
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
