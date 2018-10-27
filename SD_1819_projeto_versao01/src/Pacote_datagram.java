import java.io.Serializable;

public class Pacote_datagram  implements Serializable {
    private int funcao;
    private ClienteRMI_I cliente;
    private Http_message message;
    /*
    200 - ok
    201 - created
    202 - accepted
    203 - non-authorised information
     */

    Pacote_datagram(int funcao, ClienteRMI_I cliente){
        this.funcao = funcao;
        this.cliente = cliente;
        this.message = new Http_message(200);
    }

    public Http_message getMessage() {
        return message;
    }

    public void setMessage(int number) {
        this.message.setNumber(number);
    }

    public ClienteRMI_I getCliente() {
        return cliente;
    }

    public void setCliente(ClienteRMI_I cliente) {
        this.cliente = cliente;
    }

    public int getFuncao() {
        return funcao;
    }

    public void setFuncao(int funcao) {
        this.funcao = funcao;
    }
}
