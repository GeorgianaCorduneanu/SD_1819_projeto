package Classes_para_RMI;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private String nome_album, descricao, data_lancamento;
    private ArrayList<Musica> lista_musicas;
    private ArrayList<Utilizador> lista_utilizadores_editar_album;
    private ArrayList<Critica> listaCriticas;
    private int pontuacaoMedia;
    Album(){}

    Album(String nome_album, String descricao, String data_lancamento){
        this.nome_album = nome_album;
        this.data_lancamento = data_lancamento;
        this.descricao = descricao;
        this.pontuacaoMedia = 0;
        this.listaCriticas = new ArrayList<>();
    }
    private void calculaPontuacaoMedia(){
        int soma = 0;
        for(Critica item:listaCriticas){
            soma += item.getPontuacao();
        }
        setPontuacaoMedia(soma/listaCriticas.size());
    }
    public void add(Critica c){
        listaCriticas.add(c);
        calculaPontuacaoMedia();
    }

    public ArrayList<Critica> getListaCriticas() {
        return listaCriticas;
    }

    public void setListaCriticas(ArrayList<Critica> listaCriticas) {
        this.listaCriticas = listaCriticas;
    }

    public int getPontuacaoMedia() {
        return pontuacaoMedia;
    }

    public void setPontuacaoMedia(int pontuacaoMedia) {
        this.pontuacaoMedia = pontuacaoMedia;
    }

    public void remover_musica(Musica m){
        for(Musica item:lista_musicas){
            if(item.equals(m)){
                lista_musicas.remove(item);
                return;
            }
        }
        System.out.println("Musica inexistente");
    }

    public void setData_lancamento(String data_lancamento) {
        this.data_lancamento = data_lancamento;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void add_musica(Musica m){
        lista_musicas.add(m);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao, Utilizador u) {
/*
        if(u.getEditor()){
            if(lista_utilizadores_editar_album.contains(u) == false)
                lista_utilizadores_editar_album.add(u);

        }*/
        this.descricao = descricao;
    }

    public String getData_lancamento() {
        return data_lancamento;
    }

    public void setData_lancamento(String data_lancamento, Utilizador u) {
        /*if(u.getEditor()){
            if(lista_utilizadores_editar_album.contains(u)==false)
                lista_utilizadores_editar_album.add(u);

        }*/
        this.data_lancamento = data_lancamento;
    }

    public String getNome_album() {
        return nome_album;
    }

    public void setNome_album(String nome_album) {
        /*if(u.getEditor()){
            if(lista_utilizadores_editar_album.add(u)==false)
                lista_utilizadores_editar_album.add(u);
            this.nome_album = nome_album;
        }*/
        this.nome_album = nome_album;
    }

}
