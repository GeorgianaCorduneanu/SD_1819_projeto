package Cliente;

import Server.ServerRMI;
import Server.ServerRMI_I;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClienteRMI{
    public static void main(String args[]){
        try {

            ServerRMI_I s = (ServerRMI_I) Naming.lookup("rmi://localhost:7000/msg");
            String message=null;
            while(message != ":/stop") {
                message = s.sayHello();
                System.out.println(s.sayName() + " <<< " + message);
            }
           /*String message = s.sayHello();
           System.out.println(message);*/

        } catch (NotBoundException e) {
            System.out.println("Exception in Cliente RMI main: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Exception in Cliente RMI main: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Exception in Cliente RMI main: "+ e.getMessage());
        }

    }
}
