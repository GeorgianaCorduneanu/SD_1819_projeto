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
}
