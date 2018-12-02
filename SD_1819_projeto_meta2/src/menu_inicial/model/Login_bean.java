package menu_inicial.model;

import menu_inicial.RMIServerInterface.ServerRMI_I;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Login_bean {
    private ServerRMI_I server;
    private String username; // username and password supplied by the user
    private String password;

    public Login_bean() {
        try {
            server = (ServerRMI_I) Naming.lookup("server");
        }
        catch(NotBoundException|MalformedURLException|RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    public ArrayList<String> getAllUsers() throws RemoteException {
        return server.getAllUsers(); // are you going to throw all exceptions?
    }

    public boolean getUserMatchesPassword() throws RemoteException {
        return server.userMatchesPassword(this.username, this.password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
