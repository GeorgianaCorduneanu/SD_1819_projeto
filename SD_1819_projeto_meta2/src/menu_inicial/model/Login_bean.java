package menu_inicial.model;

import menu_inicial.RMIServerInterface.RMIServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Login_bean {
    private RMIServerInterface server;
    private String username; // username and password supplied by the user
    private String password;
    private String resultado_pesquisa;
    private String opcao_menu;
    private String todos_utilizadores;
    private List<String> lista_todos_utilizadores;
  //  private List<String> lista_users;

    public Login_bean() {
        String location_s = "rmi://localhost:7000/server";
        //lista_users = listar(3);
        try {
            server = (RMIServerInterface) Naming.lookup(location_s);
        }
        catch(NotBoundException|MalformedURLException|RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    public List<String> getLista_todos_utilizadores() {
        return lista_todos_utilizadores;
    }

    public String getTodos_utilizadores(){
        if(todos_utilizadores == null)
            todos_utilizadores =listar(3);
        getLista_users();
        return todos_utilizadores;
    }

    public void getLista_users() {
        ArrayList<String> lista_todos_utilizadores = new ArrayList<>();
        String [] mensagem_cortada_recebida =todos_utilizadores.split(":");
        for(int i=0 ; i<mensagem_cortada_recebida.length ; i++){
            lista_todos_utilizadores.add(mensagem_cortada_recebida[i]);
        }
        this.lista_todos_utilizadores = lista_todos_utilizadores;
    }

    private String listar(int tipo){
        //0 album
        //1 artista
        //2 musica
        //3 utilizadores
        //4 playlist
        String mensagem = "16;" + tipo;//
        try {
            server.enviaStringAoMulticast(mensagem);
            todos_utilizadores = server.recebe_multicast_socket();
            System.out.println("Dentro de listar, todos utilizadores" + todos_utilizadores);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
/*
        mensagem_cortada_recebida =lista_recebe.split(":");
        for(int i=0 ; i<mensagem_cortada_recebida.length ; i++){
            lista_final.add(mensagem_cortada_recebida[i]);
        }
        System.out.println(lista_recebe);
        return lista_final;*/
        return todos_utilizadores;
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
        else if(mensagem.equals("Login bem sucedido ;false"))
            return 1;
        else if(mensagem.equals("Login bem sucedido ;true"))
            return 2;

        return 0;
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
    public String getOpcao_menu(){
        return this.opcao_menu;
    }
    public void setOpcao_menu(String opcao_menu){
        this.opcao_menu = opcao_menu;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }
}
