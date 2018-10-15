package Cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteRMI_I extends Remote {
    public String getNome() throws RemoteException;
    public void say_hello_to_server(String a, String ip, int port) throws java.rmi.RemoteException;
    public String getPasse() throws RemoteException;
    public void setNome(String a) throws RemoteException;
    public void setPasse(String a) throws RemoteException;
}
