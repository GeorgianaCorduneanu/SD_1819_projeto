package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class Album_action extends ActionSupport implements SessionAware {
    private boolean adicionar_album, eliminar_album, editar_album, botao_adicionar;
    private String nomeDoAlbum, descricaoDoAlbum, dia, mes, ano;
    private Map<String, Object> session;
    private Login_bean login_bean;

    public String execute(){
        login_bean = getLogin_bean();
        String mensagem;
        if(nomeDoAlbum!=null && descricaoDoAlbum!=null && dia!=null && mes!=null && ano!=null && botao_adicionar){
            String data = dia + "/" + mes + "/" + ano;
            mensagem =login_bean.inserirAlbum(nomeDoAlbum, descricaoDoAlbum, data);
            if(mensagem.equals("Album adicionado"))
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
    public void setAdicionar_album(boolean adicionar_album){
        this.adicionar_album=adicionar_album;
    }
    public void setEliminar_album(boolean adicionar_album){
        this.adicionar_album=adicionar_album;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
