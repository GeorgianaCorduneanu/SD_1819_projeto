package Server;

import Cliente.ClienteRMI;
import Cliente.ClienteRMI_I;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.Scanner;

public class ServerRMIB extends UnicastRemoteObject implements ServerRMI_I {
    private static String location_s;
    static ClienteRMI_I cliente;
    public ServerRMIB() throws RemoteException{super();}

    public static void main(String args[]) throws RemoteException {
        String frase;
        boolean check_principal = true;
        String server_ip = "localhost";
        int server_port = 7000;
        String nome = "msg";
        location_s = "rmi://" + server_ip + ":" + server_port + "/" + nome;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        ServerRMI_I s_inter_principal=null;

        //encontrar servio
        try {
            s_inter_principal = (ServerRMI_I) Naming.lookup(location_s);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        while(check_principal){
            assert s_inter_principal != null;
            try {
                check_principal = s_inter_principal.check_server_p();
            }catch (IOException e){
                System.out.println("*** Secundario Server Ativo ***");
                check_principal = false;
                break;
            }
            System.out.println("Principal Server Alive");
        }
        //para escrever no cliente
        try {
            ServerRMI server = new ServerRMI();
            Naming.rebind(location_s, server);
            System.out.println("Hello Server ready!!");
            frase = reader.readLine();
            do{
                imprime(frase, server_ip, server_port);
                frase = reader.readLine();
            }while(frase.compareTo(":/stop") != 0);

        } catch (RemoteException e) {
            System.out.println("Exception on main ServerRMI ->>> " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Exception on main ServerRMI ->>> " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception on main ServerRMI ->>> " + e.getMessage());
        }
    }
    public static void imprime(String a, String ip, int port){
        System.out.print(">>> ");
        try {
            System.out.print(cliente.getNome() + ":"+cliente.getPasse());
            cliente.say_hello_to_server(a, ip, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void say_hello_to_cliente(String s) throws RemoteException {
        System.out.println(">>> " + s);
    }

    @Override
    public void subscribe(String nome, ClienteRMI_I c_i) throws RemoteException {
        boolean check=true;
        //mandar info a multicast buscar base de dados e adicionar registo
        System.out.println("Em Registar " + nome + " (" + c_i.tooString() + ") " + " verify: " + check);
        cliente = c_i;
        c_i.check_registar(check);
    }

    @Override
    public void login(String nome, ClienteRMI_I c_i) throws RemoteException {
        boolean check = true;
        //mandar info a multicast buscar na base de dados e verificar login
        System.out.println("Em Login: " + nome + " (" + c_i.tooString() + ") " + " verify: " + check);
        cliente = c_i;
        c_i.check_login(check);
    }

    @Override
    public boolean check_server_p() throws RemoteException {
        return true;
    }


}