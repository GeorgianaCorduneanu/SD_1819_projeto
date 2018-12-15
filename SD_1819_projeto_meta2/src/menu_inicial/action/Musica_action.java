package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.List;
import java.util.Map;

public class Musica_action extends ActionSupport implements SessionAware {
    private boolean adicionar_musica, eliminar_musica, editar_musica, botaoEliminar;
    private String nomeDaMusica, nomeDoCompositor, duracaoDaMusica, radioListaMusicas, todasMusicas;
    private Map<String, Object> session;
    private Login_bean login_bean;
    private List<String> listaMusicas;

    public String execute(){
        login_bean = getLogin_bean();
        String mensagemDevolve;
        if(nomeDaMusica!=null && nomeDoCompositor!=null && duracaoDaMusica !=null){
            mensagemDevolve = login_bean.inserir_musica(nomeDaMusica, nomeDoCompositor, duracaoDaMusica);
            if(mensagemDevolve.equals("Musica adicionada"))
                return SUCCESS;
        }else if(radioListaMusicas!=null && botaoEliminar){
            mensagemDevolve = login_bean.eliminar(8,radioListaMusicas);
            if(mensagemDevolve.equals("Musica eliminada"))
                return SUCCESS;
        }else if(adicionar_musica) {
            mensagemDevolve = "adicionar_musica";
            login_bean.setOpcao_menu(mensagemDevolve);
            System.out.println(mensagemDevolve);
            return mensagemDevolve;
        }else if(eliminar_musica) {
            mensagemDevolve = "eliminar_musica";
            login_bean.setOpcao_menu(mensagemDevolve);
            System.out.println(mensagemDevolve);

            return mensagemDevolve;
        }else if(editar_musica) {
            mensagemDevolve = "editar_musica";
            login_bean.setOpcao_menu(mensagemDevolve);
            System.out.println(mensagemDevolve);
            return mensagemDevolve;
        }
        return "insuccess";
    }

    public String getTodasMusicas() {
        todasMusicas = login_bean.getTodasMusicas();
        System.out.println(todasMusicas);
        return todasMusicas;
    }

    public void setTodasMusicas(String todasMusicas) {
        this.todasMusicas = todasMusicas;
    }

    public boolean isBotaoEliminar() {
        return botaoEliminar;
    }

    public void setBotaoEliminar(boolean botaoEliminar) {
        this.botaoEliminar = botaoEliminar;
    }

    public String getRadioListaMusicas() {
        return radioListaMusicas;
    }

    public void setRadioListaMusicas(String radioListaMusicas) {
        this.radioListaMusicas = radioListaMusicas;
    }

    public String getNomeDaMusica() {
        return nomeDaMusica;
    }

    public List<String> getListaMusicas() {
        return listaMusicas=login_bean.getLista_todos_utilizadores();
    }

    public void setListaMusicas(List<String> listaMusicas) {
        this.listaMusicas = listaMusicas;
    }

    public void setNomeDaMusica(String nomeDaMusica) {
        this.nomeDaMusica = nomeDaMusica;
    }

    public String getNomeDoCompositor() {
        return nomeDoCompositor;
    }

    public void setNomeDoCompositor(String nomeDoCompositor) {
        this.nomeDoCompositor = nomeDoCompositor;
    }

    public String getDuracaoDaMusica() {
        return duracaoDaMusica;
    }

    public void setDuracaoDaMusica(String duracaoDaMusica) {
        this.duracaoDaMusica = duracaoDaMusica;
    }

    public boolean isAdicionar_musica() {
        return adicionar_musica;
    }

    public void setAdicionar_musica(boolean adicionar_musica) {
        this.adicionar_musica = adicionar_musica;
    }

    public boolean isEliminar_musica() {
        return eliminar_musica;
    }

    public void setEliminar_musica(boolean eliminar_musica) {
        this.eliminar_musica = eliminar_musica;
    }

    public boolean isEditar_musica() {
        return editar_musica;
    }

    public void setEditar_musica(boolean editar_musica) {
        this.editar_musica = editar_musica;
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
