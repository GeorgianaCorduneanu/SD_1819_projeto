package menu_inicial.model;

import menu_inicial.RMIServerInterface.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Registo_bean {
    private RMIServerInterface server;
    private String username; // username and password supplied by the user
    private String password;

    public Registo_bean() {
        String location_s = "rmi://localhost:7000/server";
        try {
            server = (RMIServerInterface) Naming.lookup(location_s);
        }
        catch(NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }
    public int regista_utilizador(){
        try {
            String mensagem=server.subscribe(this.username, this.password);
            if(mensagem.equals("Utilizador nao existente"))
                return 1;
            else if(mensagem.equals(" Utilizador ja existente"))
                return 0;
            else if(mensagem.equals("Primeiro utilizador")){
                return 2;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUserMatchesPassword() throws RemoteException {
        String mensagem = server.login(this.username, this.password);
        //String mensagem_cortada[] = mensagem.split(";");
        System.out.println(mensagem + "\nErro ao fazer login");
        if (mensagem.equals("Erro ao fazer login"))
            return 0;
        else if(mensagem.equals("Login bem sucedido ;false"))
            return 1;
        else if(mensagem.equals("Login bem sucedido ;true"))
            return 2;
        else if(server==null)
            return 3;

        return 0;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
