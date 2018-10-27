
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteRMI_I extends Remote {
    Utilizador getUtilizador() throws RemoteException;
    void say_hello_to_server(String a, String ip, int port) throws java.rmi.RemoteException;
    void check_registar(boolean b) throws RemoteException;
    void check_login(boolean b) throws RemoteException;
    void change_edir(boolean b) throws RemoteException;
}
