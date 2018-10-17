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
    private static String server_ip = "localhost";
    private static int server_port = 7000;
    private static String name = "msg";

    public ClienteRMI() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws IOException {
        String frase_crua=null;  //variavel para guardar input
        String frase_sem_espaco;
        String [] frase_chave_valor = null; //array para guardar cada par chave valor

        //definir ip, porto do servidor e o nome
        location_s = "rmi://" + server_ip + ":" + server_port + "/"+ name;

        //ler o tipo de comando e valores
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        //definir clienteRMI e interfaceservidor RMI
        ClienteRMI cliente = null;
        ServerRMI_I server_i = null;

        try {
            server_i = (ServerRMI_I) Naming.lookup(location_s);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        do {
            //procurar o servidor a falar

            System.out.println("*** Inserir Comando ***");
            try {
                frase_crua = reader.readLine();
                frase_sem_espaco = frase_crua.replace(" ","");
                frase_chave_valor = frase_sem_espaco.split(";");
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (frase_chave_valor[0]) {
                case "login":
                    cliente = login_cliente(frase_chave_valor[1], frase_chave_valor[2]);
                    try {
                        server_i.login(location_s, (ClienteRMI_I) cliente);
                    }catch (IOException e) {
                        try {
                            server_i = (ServerRMI_I) Naming.lookup(location_s);
                            server_i.login(location_s, (ClienteRMI_I) cliente);


                        } catch (NotBoundException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
                case "registar":
                    try {
                        cliente = registar_cliente(frase_chave_valor[1], frase_chave_valor[2]);
                        System.out.println("a enviar para segvidor");
                        try {
                            server_i.subscribe(location_s, (ClienteRMI_I) cliente);
                        }catch (IOException e) {
                            try {
                                server_i = (ServerRMI_I) Naming.lookup(location_s);
                                server_i.subscribe(location_s, (ClienteRMI_I) cliente);

                            } catch (NotBoundException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        }                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Indique valor correto");
                    break;
            }
        }while(frase_crua != ":/stop");
    }

    public static ClienteRMI registar_cliente(String username, String passe) throws RemoteException {
        ClienteRMI cliente = null;
        System.out.println("*** Registar ***");
        try {
            cliente = new ClienteRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            cliente.setNome(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            cliente.setPasse(passe);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cliente;
    }
    public static ClienteRMI login_cliente(String username, String passe) throws RemoteException {
        ClienteRMI cliente = null;
        System.out.println("*** Login ***");
        try {
            cliente = new ClienteRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            cliente.setNome(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            cliente.setPasse(passe);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cliente;
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

    @Override
    public void check_registar(boolean b) throws RemoteException {
        if(b)
            System.out.println("Registado com sucesso!");
        else
            System.out.println("Ocorreu um erro, tente novamente");
    }

    @Override
    public void check_login(boolean b) throws RemoteException {
        if(b)
            System.out.println("Login com sucesso");
        else
            System.out.println("Ocorreu um erro, tente novamente");
    }

    @Override
    public String tooString() throws RemoteException {
        try {
            return getNome() + " : " + getPasse();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

}