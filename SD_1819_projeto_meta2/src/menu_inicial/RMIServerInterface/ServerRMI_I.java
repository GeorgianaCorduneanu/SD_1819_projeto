package menu_inicial.RMIServerInterface;

import java.rmi.*;
import java.util.ArrayList;

public interface ServerRMI_I extends Remote {
   // public void say_hello_to_cliente(String s) throws java.rmi.RemoteException;
    void enviaStringAoMulticast(String message) throws RemoteException;

    boolean check_server_p() throws RemoteException;
    String recebe_multicast_socket() throws RemoteException;
    public boolean userMatchesPassword(String user, String password) throws RemoteException;
    public ArrayList<String> getAllUsers() throws RemoteException;
    void notifica_cliente(String nome, String mensagem) throws RemoteException;
}
