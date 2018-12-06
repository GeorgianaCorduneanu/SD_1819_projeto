package menu_inicial.model;

import menu_inicial.RMIServerInterface.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Login_bean {
    private RMIServerInterface server;
    private String username; // username and password supplied by the user
    private String password;

    public Login_bean() {
        String location_s = "rmi://localhost:7000/server";
        try {
            server = (RMIServerInterface) Naming.lookup(location_s);
        }
        catch(NotBoundException|MalformedURLException|RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    public ArrayList<String> getAllUsers() throws RemoteException {
        return server.getAllUsers(); // are you going to throw all exceptions?
    }

    /*public boolean getUserMatchesPassword() throws RemoteException {
        return server.userMatchesPassword(this.username, this.password);
    }*/

    public int getUserMatchesPassword() throws RemoteException {
        String mensagem = server.login(this.username, this.password);
        //String mensagem_cortada[] = mensagem.split(";");
        System.out.println(mensagem + "\nErro ao fazer login");
        if (mensagem.equals("Erro ao fazer login"))
            return 0;
        else if(mensagem.equals("Utilizador Encontrado ; false"))
            return 1;
        else if(mensagem.equals("Utilizador Encontrado ; true"))
            return 2;

        return 0;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
