import java.rmi.*;

public interface ServerRMI_I extends Remote {
   // public void say_hello_to_cliente(String s) throws java.rmi.RemoteException;
    void subscribe(String nome, ClienteRMI_I c_i) throws RemoteException;
    String login(String nome, ClienteRMI_I c_i) throws RemoteException;
    boolean check_server_p() throws RemoteException;
}
