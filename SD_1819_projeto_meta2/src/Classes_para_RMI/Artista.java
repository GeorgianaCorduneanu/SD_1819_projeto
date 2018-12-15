package Classes_para_RMI;

import java.io.Serializable;
import java.util.ArrayList;

public class Artista implements Serializable {
    private String nome_artista;
    private Boolean compositor;
    private String informacao;
    private ArrayList<Utilizador> lista_utilizadores_editar_artista;

    Artista(){}

    Artista(String nome_artista){
        this.nome_artista = nome_artista;
        compositor=false;
        informacao=null;
    }
    Artista(String nome_artista, Boolean compositor){
        this.compositor = compositor;
        this.nome_artista = nome_artista;
        this.informacao = null;
    }
    Artista(String nome_artista, Boolean compositor, String informacao){
        this.nome_artista = nome_artista;
        this.compositor = compositor;
        this.informacao = informacao;
    }

    public String getNome_artista() {
        return nome_artista;
    }

    public void setNome_artista(String nome_artista) {
       this.nome_artista = nome_artista;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public Boolean getCompositor() {
        return compositor;
    }

    public void setCompositor(Boolean compositor) {
        this.compositor = compositor;
    }
}
