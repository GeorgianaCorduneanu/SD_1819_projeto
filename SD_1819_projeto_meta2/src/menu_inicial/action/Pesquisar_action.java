package menu_inicial.action;
import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;


public class Pesquisar_action extends ActionSupport implements SessionAware {
    private String pesquisar_tipo;
    private String resultado_pesquisa;
    private String string_pesquisar;
    private Login_bean login_bean;
    private Map<String, Object> session;
    private boolean voltar;

    @Override
    public String execute(){
        System.out.println(string_pesquisar);
        if(pesquisar_tipo==null)
            return "insuccess";
        if(pesquisar_tipo.equals("Album")) {
            pesquisar(1, string_pesquisar);
            return "album";
        }if(pesquisar_tipo.equals("Artista")) {
            pesquisar(2, string_pesquisar);
            return "artista";
        }if(pesquisar_tipo.equals("Musica")) {
            pesquisar(3, string_pesquisar);
            System.out.println("o que pesquisou: " + 3);
            return "musica";
        }if(voltar)
            return "insuccess";
        return "insuccess";
    }

    public String getResultado_pesquisa() {

        return this.resultado_pesquisa;
    }

    public String getString_pesquisar() {
        return string_pesquisar;
    }

    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }


    public void pesquisar(int tipo, String nome){
        if(tipo==1) //pesquisar album
            this.resultado_pesquisa = this.getLogin_bean().pesquisar(14, nome);
        else if(tipo==2){ //artista
            this.resultado_pesquisa = this.getLogin_bean().pesquisar(15, nome);
        }else if(tipo==3){//musica
            this.resultado_pesquisa = this.getLogin_bean().pesquisar(13, nome);
        }
        System.out.println("Resultado pesquisa: " + resultado_pesquisa);
    }
    public void setVoltar(boolean voltar){
        this.voltar = voltar;
    }
    public void setString_pesquisar(String string_pesquisar) {
        this.string_pesquisar = string_pesquisar;
    }

    public String getPesquisar_tipo() {
        return pesquisar_tipo;
    }

    public void setPesquisar_tipo(String pesquisar_tipo) {
        this.pesquisar_tipo = pesquisar_tipo;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
