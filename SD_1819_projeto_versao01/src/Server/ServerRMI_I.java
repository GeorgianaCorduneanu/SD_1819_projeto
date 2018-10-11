package Server;

import java.rmi.*;

public interface ServerRMI_I extends Remote {
    public String sayHello() throws java.rmi.RemoteException;
    public String sayName() throws java.rmi.RemoteException;
}
