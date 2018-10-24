import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClienteRMI extends UnicastRemoteObject implements ClienteRMI_I, Serializable {
    private Utilizador cliente_corrente;

    public ClienteRMI() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws IOException {
        String frase_crua=null;  //variavel para guardar input
        String frase_sem_espaco;
        String [] frase_chave_valor = null; //array para guardar cada par chave valor
        String location_s;
        String server_ip = "localhost";
        int server_port = 7000;
        String name = "msg";
        String txt = null;
        //definir plicy
        System.getProperties().put("java.security.policy", "file:\\C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        System.setProperty("java.rmi.server.hostname","192.168.56.1");
        System.setSecurityManager(new RMISecurityManager());

        //definir ip, porto do servidor e o nome
        location_s = "rmi://" + server_ip + ":" + server_port + "/"+ name;

        //ler o tipo de comando e valores
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        //definir clienteRMI e interfaceservidor RMI
        ClienteRMI cliente;
        ServerRMI_I server_i = null;

        try {
            server_i = (ServerRMI_I) Naming.lookup(location_s);
        } catch (NotBoundException e) {
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
                        txt = server_i.login(location_s, cliente);
                        System.out.println(txt);
                    }catch (IOException e) {
                        try {
                            server_i = (ServerRMI_I) Naming.lookup(location_s);
                            server_i.login(location_s, cliente);


                        } catch (NotBoundException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
                    break;
                case "registar":
                    try {
                        cliente = registar_cliente(frase_chave_valor[1], frase_chave_valor[2]);
                        //cliente.cliente_corrente = new Utilizador(frase_chave_valor[1], frase_chave_valor[2]);
                        System.out.println("A enviar para servidor");
                        try {
                            assert server_i != null;
                            server_i.subscribe(location_s, cliente);
                        }catch (IOException e) {
                            try {
                                server_i = (ServerRMI_I) Naming.lookup(location_s);
                                server_i.subscribe(location_s, cliente);

                            } catch (NotBoundException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Indique valor correto");
                    break;
            }
        }while(frase_crua != ":/stop");
    }

    public static ClienteRMI registar_cliente(String username, String passe){
        ClienteRMI cliente = null;
        System.out.println("*** Registar ***");
        try {
            cliente = new ClienteRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cliente.cliente_corrente = new Utilizador(username, passe);
        //System.out.println("username: " + cliente.getUtilizador().getUsername() + " : " + cliente.getUtilizador().getPassword());
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
        cliente.cliente_corrente = new Utilizador(username, passe);
        return cliente;
    }

    @Override
    public Utilizador getUtilizador() throws RemoteException {
        return cliente_corrente;
    }

    @Override
    public void say_hello_to_server(String a, String server_ip, int server_port) throws RemoteException {
        System.out.println(server_ip + ":" + server_port + ">>> " + a);
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
        if(!b)
            System.out.println("Ocorreu um erro, tente novamente");
    }

}