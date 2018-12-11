package menu_inicial.action;
import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Pesquisar_bean;
import java.util.Map;


public class Pesquisar_action extends ActionSupport {
    private String pesquisar_tipo;
    private String resultado_pesquisa;
    private String string_pesquisar;
    private Pesquisar_bean pesquisar_bean;

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
            return "musica";
        }
        return "insuccess";
    }

    public String getResultado_pesquisa() {
        return resultado_pesquisa;
    }

    public String getString_pesquisar() {
        return string_pesquisar;
    }

    public Pesquisar_bean getPesquisar_bean() {
        return pesquisar_bean;
    }

    public void setPesquisar_bean(Pesquisar_bean pesquisar_bean) {
        this.pesquisar_bean = pesquisar_bean;
    }

    public void pesquisar(int tipo, String nome){
        if(tipo==1) //pesquisar album
            resultado_pesquisa = this.getPesquisar_bean().pesquisar(14, nome);
        else if(tipo==2){ //artista
            resultado_pesquisa = this.getPesquisar_bean().pesquisar(15, nome);
        }else if(tipo==3){//musica
            resultado_pesquisa = this.getPesquisar_bean().pesquisar(13, nome);
        }
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
}