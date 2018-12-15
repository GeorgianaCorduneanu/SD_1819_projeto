package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.List;
import java.util.Map;

public class Album_action extends ActionSupport implements SessionAware {
    private boolean adicionar_album, eliminar_album, editar_album, botao_adicionar, botaoEliminar;
    private String nomeDoAlbum, descricaoDoAlbum, dia, mes, ano, todosAlbuns, radioListaAlbuns;
    private Map<String, Object> session;
    private Login_bean login_bean;
    private List<String> listaTodosAlbuns;

    public String execute(){
        login_bean = getLogin_bean();
        String mensagem;
        if(nomeDoAlbum!=null && descricaoDoAlbum!=null && dia!=null && mes!=null && ano!=null && botao_adicionar){
            String data = dia + "/" + mes + "/" + ano;
            mensagem =login_bean.inserirAlbum(nomeDoAlbum, descricaoDoAlbum, data);
            if(mensagem.equals("Album adicionado"))
                return SUCCESS;
        }else if(radioListaAlbuns!=null && botaoEliminar){
            mensagem = login_bean.eliminar(9,radioListaAlbuns);
            System.out.println(radioListaAlbuns);
            if(mensagem.equals("Album eliminado"))
                return SUCCESS;
        }else if(adicionar_album) {
            mensagem = "adicionar_album";
            login_bean.setOpcao_menu(mensagem);
            return mensagem;
        }else if(eliminar_album) {
            mensagem = "eliminar_album";
            login_bean.setOpcao_menu(mensagem);
            return mensagem;
        }else if(editar_album) {
            mensagem = "editar_album";
            login_bean.setOpcao_menu(mensagem);
            return mensagem;
        }
        return "insuccess";
    }

    public String getRadioListaAlbuns() {
        return radioListaAlbuns;
    }

    public void setEditar_album(boolean editar_album) {
        this.editar_album = editar_album;
    }

    public void setRadioListaAlbuns(String radioListaAlbuns) {
        this.radioListaAlbuns = radioListaAlbuns;
    }

    public List<String> getListaTodosAlbuns() {
        return listaTodosAlbuns = login_bean.getLista_todos_utilizadores();
    }

    public void setListaTodosAlbuns(List<String> listaTodosAlbuns) {
        this.listaTodosAlbuns = listaTodosAlbuns;
    }

    public boolean isBotaoEliminar() {
        return botaoEliminar;
    }

    public void setBotaoEliminar(boolean botaoEliminar) {
        this.botaoEliminar = botaoEliminar;
    }

    public String getTodosAlbuns() {
        return todosAlbuns = login_bean.getTodosAlbuns();
    }

    public void setTodosAlbuns(String todosAlbuns) {
        this.todosAlbuns = todosAlbuns;
    }

    public boolean isBotao_adicionar() {
        return botao_adicionar;
    }

    public void setBotao_adicionar(boolean botao_adicionar) {
        this.botao_adicionar = botao_adicionar;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getNomeDoAlbum() {
        return nomeDoAlbum;
    }

    public void setNomeDoAlbum(String nomeDoAlbum) {
        this.nomeDoAlbum = nomeDoAlbum;
    }

    public String getDescricaoDoAlbum() {
        return descricaoDoAlbum;
    }

    public void setDescricaoDoAlbum(String descricaoDoAlbum) {
        this.descricaoDoAlbum = descricaoDoAlbum;
    }

    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }

    public boolean isAdicionar_album() {
        return adicionar_album;
    }

    public boolean isEliminar_album() {
        return eliminar_album;
    }

    public boolean isEditar_album() {
        return editar_album;
    }

    public void setAdicionar_album(boolean adicionar_album){
        this.adicionar_album=adicionar_album;
    }

    public void setEliminar_album(boolean eliminar_album) {
        this.eliminar_album = eliminar_album;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
