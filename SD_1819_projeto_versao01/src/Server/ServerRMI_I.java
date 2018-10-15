package Server;

import Cliente.ClienteRMI_I;

import java.rmi.*;

public interface ServerRMI_I extends Remote {
    public void say_hello_to_cliente(String s) throws java.rmi.RemoteException;
    public void subscribe(String nome, ClienteRMI_I c_i) throws RemoteException;
}
