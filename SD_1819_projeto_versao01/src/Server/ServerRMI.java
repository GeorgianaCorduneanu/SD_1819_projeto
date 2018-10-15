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

public class ServerRMI extends UnicastRemoteObject implements ServerRMI_I {
    private static String location_s;
    static ClienteRMI_I cliente;
    public ServerRMI() throws RemoteException{super();}

    public static void main(String args[]){
        String frase;
        String server_ip = "127.0.0.1";
        int server_port = 7000;
        String nome = "msg";
        location_s = "rmi://" + server_ip + ":" + server_port + "/" + nome;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

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
        System.out.println("Subscribing " + nome);
        cliente = c_i;
    }
}