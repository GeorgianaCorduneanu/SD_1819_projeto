import java.io.Serializable;

public class Notificacao implements Serializable {
    private Utilizador u;
    private String data;
    private String mensagem;

    Notificacao(){}

    Notificacao(Utilizador u, String data){
        this.u = u;
        this.data = data;
    }
    Notificacao(Utilizador u, String data, String mensagem){
        this.u = u;
        this.data = data;
        this.mensagem = mensagem;
    }

    public Utilizador getU() {
        return u;
    }

    public void setU(Utilizador u) {
        this.u = u;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
