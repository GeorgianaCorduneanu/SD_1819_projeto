import java.rmi.*;
import java.util.ArrayList;

public interface ServerRMI_I extends Remote {
   // public void say_hello_to_cliente(String s) throws java.rmi.RemoteException;
    String subscribe(String nome, ClienteRMI_I c_i) throws RemoteException;
    String login(String nome, ClienteRMI_I c_i) throws RemoteException;

    void enviaStringAoMulticast(String message) throws RemoteException;

    boolean check_server_p() throws RemoteException;
    String recebe_multicast_socket() throws RemoteException;
    ArrayList<Musica> recebe_musicas(int inicio, int fim) throws RemoteException;
    void addClienteOnline(ClienteRMI_I c_i) throws RemoteException;
    ArrayList<ClienteRMI_I> clientes_online() throws RemoteException;
    void notifica_cliente(String nome, String mensagem) throws RemoteException;
}
