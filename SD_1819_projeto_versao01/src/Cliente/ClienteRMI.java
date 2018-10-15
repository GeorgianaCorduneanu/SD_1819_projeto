package Cliente;

import Server.ServerRMI_I;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClienteRMI extends UnicastRemoteObject implements ClienteRMI_I {
    private static String location_s;
    private static String nome_cliente;
    private static String passe_cliente;
    private static String server_ip = "127.0.0.1";
    private static int server_port = 7000;
    private static String name = "msg";

    public ClienteRMI() throws RemoteException {
        super();
    }

    public static void main(String args[]) {
        //definir ip, porto do servidor e o nome
        location_s = "rmi://" + server_ip + ":" + server_port + "/"+ name;

        //definir informacoes do cliente
        ClienteRMI cliente = null;
        try {
            cliente = definir_cliente();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            //procurar por um ip e um porto no registry com o nome
            ServerRMI_I server_i = (ServerRMI_I)Naming.lookup(location_s);
            //enviar confirmacao ao cliente de que se ligou ao servidor
            server_i.subscribe(location_s, (ClienteRMI_I)cliente);
            System.out.println("Client sent subscrition to server");
        } catch (NotBoundException e) {
            System.out.println("Exception on ClienteRMI main ->>> " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Exception on ClienteRMI main ->>> " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Exception on ClienteRMI main ->>> " + e.getMessage());
        }
    }
    //criat thread para poder ler a partir de servidor RMI
    class Th_read extends Thread{

    }
    //criar thread para poder escrever através do servidor RMI
    //ou para dar comandos
    class Th_write extends Thread{

    }


    @Override
    public String getNome() throws RemoteException {
        return this.nome_cliente;
    }

    @Override
    public void say_hello_to_server(String a, String server_ip, int server_port) throws RemoteException {
        System.out.println(server_ip + ":" + server_port + ">>> " + a);
    }

    @Override
    public String getPasse() throws RemoteException {
        return this.passe_cliente;
    }

    @Override
    public void setNome(String a) throws RemoteException {
        this.nome_cliente = a;
    }

    @Override
    public void setPasse(String a) throws RemoteException {
        this.passe_cliente = a;
    }

    public static ClienteRMI definir_cliente() throws RemoteException {
        ClienteRMI cliente = null;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        try {
            cliente = new ClienteRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Your name: ");
        try {
            cliente.setNome(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Your password");
        try {
            cliente.setPasse(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cliente;
    }
}