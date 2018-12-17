package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.List;
import java.util.Map;

public class Artista_action extends ActionSupport implements SessionAware {
    private boolean adicionar_artista, eliminar_artista, editar_artista, botaoAdicionar, botaoEliminar, botaoMudar;
    private Map<String, Object> session;
    private String informacaoAdicional, nomeDoArtista, tipoCompositor, radioListaArtistas, radioTipoData, componenteMudar;
    private Login_bean login_bean;
    private String todosArtistas;
    private List<String> listaArtistas;

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
        }else if(radioListaArtistas != null && botaoEliminar) {
            mensagem = login_bean.eliminar(10, radioListaArtistas);
            if (mensagem.equals("Artista eliminado"))
                return SUCCESS;
        }else if(radioListaArtistas!=null && botaoMudar && radioTipoData!=null && componenteMudar!=""){
            if(radioTipoData.equals("Nome")){
                mensagem = "21;" + radioListaArtistas + ";" + componenteMudar;
                mensagem = login_bean.editar(1, mensagem);
                System.out.println(mensagem);
                if(mensagem.equals("Artista editado"))
                    return SUCCESS;
            }else if(radioTipoData.equals("Descricao")){
                mensagem = "22;" + radioListaArtistas + ";" + componenteMudar;
                mensagem = login_bean.editar(1, mensagem);
                System.out.println(mensagem);
                if(mensagem.equals("Artista editado"))
                    return SUCCESS;
            }
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

    public String getRadioTipoData() {
        return radioTipoData;
    }

    public void setRadioTipoData(String radioTipoData) {
        this.radioTipoData = radioTipoData;
    }

    public String getComponenteMudar() {
        return componenteMudar;
    }

    public void setComponenteMudar(String componenteMudar) {
        this.componenteMudar = componenteMudar;
    }

    public boolean isBotaoMudar() {
        return botaoMudar;
    }

    public void setBotaoMudar(boolean botaoMudar) {
        this.botaoMudar = botaoMudar;
    }

    public boolean isBotaoEliminar() {
        return botaoEliminar;
    }

    public void setBotaoEliminar(boolean botaoEliminar) {
        this.botaoEliminar = botaoEliminar;
    }

    public String getRadioListaArtistas() {
        return radioListaArtistas;
    }

    public void setRadioListaArtistas(String radioListaArtistas) {
        this.radioListaArtistas = radioListaArtistas;
    }

    /*public String getTodosArtistas() {
        return todosArtistas = login_bean.getTodosArtistas();
    }

    public void setTodosArtistas(String todosArtistas) {
        this.todosArtistas = todosArtistas;
    }*/

    public String getTodosArtistas() {
        return todosArtistas=login_bean.getTodosArtistas();
    }

    public void setTodosArtistas(String todosArtistas) {
        this.todosArtistas = todosArtistas;
    }

    /*public List<String> getListaArtistas() {
        return listaArtistas = login_bean.getLista_todos_utilizadores();
    }

    public void setListaArtistas(List<String> listaArtistas) {
        this.listaArtistas = listaArtistas;
    }*/

    public List<String> getListaArtistas() {
        return listaArtistas = login_bean.getLista_todos_utilizadores();
    }

    public void setListaArtistas(List<String> listaArtistas) {
        this.listaArtistas = listaArtistas;
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
