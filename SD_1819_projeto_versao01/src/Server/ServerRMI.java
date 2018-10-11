package Server;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Scanner;

public class ServerRMI extends UnicastRemoteObject implements ServerRMI_I{
    private static String location_s = "rmi://localhost:7000/msg";
    public ServerRMI() throws RemoteException{super();}

    public static void main(String args[]){
        try {
            ServerRMI s = new ServerRMI();
            Naming.rebind(location_s, s);
            System.out.println("Hello server ready");
        } catch (RemoteException e) {
            System.out.println("Exception in Server RMI main: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Exception in Server RMI main: " + e.getMessage());
        }

    }

    @Override
    public String sayHello() throws RemoteException {
        String frase;
        Scanner sc = new Scanner(System.in);
        System.out.print(">>>");
        frase = sc.nextLine();
        return frase;
    }
    public String sayName() throws RemoteException{
        return location_s;
    }
}
