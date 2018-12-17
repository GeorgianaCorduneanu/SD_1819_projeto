package Classes_para_RMI;

import java.io.Serializable;

public class Critica implements Serializable{
    private String justificacao;
    private int pontuacao;
    private String username;
    Critica(){}
    Critica(String justificacao, int pontuacao, String username){
        this.justificacao = justificacao;
        this.pontuacao = pontuacao;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJustificacao() {
        return justificacao;
    }

    public void setJustificacao(String justificacao) {
        this.justificacao = justificacao;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
}
