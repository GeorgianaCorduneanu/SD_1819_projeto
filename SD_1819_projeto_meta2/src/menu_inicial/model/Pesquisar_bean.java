package menu_inicial.model;

import menu_inicial.RMIServerInterface.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Pesquisar_bean {
    private RMIServerInterface server;
    private String resultado_pesquisa;

    public Pesquisar_bean(){
        String location_s = "rmi://localhost:7000/server";
        try {
            server = (RMIServerInterface) Naming.lookup(location_s);
        }
        catch(NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    public String pesquisar(int tipo, String nome){
        /* 14 - multicast pesquisa por album
           15 - multicast pesquisar por artista
           13 - multicast pesquisar por musica
         */
        String mensagem = tipo + ";" + nome;
        try {
            server.enviaStringAoMulticast(mensagem);
            resultado_pesquisa = server.recebe_multicast_socket();
            if(resultado_pesquisa==null)
                return resultado_pesquisa="Nao existe!";
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return resultado_pesquisa;
    }
}
