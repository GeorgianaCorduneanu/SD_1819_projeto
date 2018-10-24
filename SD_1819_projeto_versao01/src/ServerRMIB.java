import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.*;
import java.rmi.server.*;

public class ServerRMIB extends UnicastRemoteObject implements ServerRMI_I {
    private static String location_s;
    private static String server_ip = "127.0.0.1";
    private static int server_port = 7000;
    private static String MULTICAST_ADDRESS;
    static ClienteRMI_I cliente;
    private ServerRMIB() throws RemoteException{super();}

    public static void main(String args[]) throws RemoteException {
        String MULTICAST_ADDRESS = "224.3.2.1";
        String nome = "msg";
        location_s = "rmi://" + server_ip + ":" + server_port + "/" + nome;
        System.getProperties().put("java.security.policy", "file:\\C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\out\\production\\SD_1819_projeto_versao01\\policy.all");
        System.setSecurityManager(new RMISecurityManager());
        //encontrar servio
        check_servidor();

        //para escrever no cliente
        write_on_server();

    }
    private static void imprime(String a, String ip, int port){
        System.out.print(">>> ");
        try {
            System.out.print(cliente.getUtilizador().getUsername() + ":"+cliente.getUtilizador().getPassword());
            cliente.say_hello_to_server(a, ip, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private static void check_servidor(){
        boolean check_principal = true;
        ServerRMI_I s_inter_principal=null;
        try {
            s_inter_principal = (ServerRMI_I) Naming.lookup(location_s);
        } catch (NotBoundException e) {
            System.out.println(e);
            return;
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

    }
    private static void write_on_server(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase = null;
        try {
            ServerRMIB server = new ServerRMIB();
            Naming.rebind(location_s, server);
            System.out.println("Hello Server ready!!");
            try {
                frase = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            do{
                imprime(frase, server_ip, server_port);
                try {
                    frase = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(frase.compareTo(":/stop") != 0);

        } catch (RemoteException e) {
            System.out.println("Exception on main ServerRMI ->>> " + e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void say_hello_to_cliente(String s) throws RemoteException {
        System.out.println(">>> " + s);
    }*/

    @Override
    public void subscribe(String nome, ClienteRMI_I c_i) throws RemoteException {
        boolean check=true;
        int multicast_port = 5000;
        String MULTICAST_ADDRESS = "224.3.2.1";
        //mandar info a multicast buscar base de dados e adicionar registo
        System.out.println("Em Registar " + nome + " (" + c_i.getUtilizador().getUsername() + ") " + " verify: " + check);
        cliente = c_i;

        envia_para_multicast(1,c_i, multicast_port);

        c_i.check_registar(check);
    }
    private void envia_para_multicast(int i, ClienteRMI_I c_i, int multicast_port){
        try (MulticastSocket socket = new MulticastSocket()) {
            // create socket without binding it (only for sending)
            String message =  i + ";" + c_i.getUtilizador().getUsername() + ";" + c_i.getUtilizador().getPassword();
            byte[] buffer = message.getBytes();

            socket.setLoopbackMode(true);//true quando envia
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicast_port);
            socket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(String nome, ClienteRMI_I c_i) throws RemoteException {
        boolean check = true;
        int multicast_port = 5000;
        //mandar info a multicast buscar na base de dados e verificar login
        System.out.println("Em Login: " + nome + " (" + c_i.getUtilizador().getUsername() + ") " + " verify: " + check);
        cliente = c_i;

        envia_para_multicast(2,c_i, multicast_port);
        c_i.check_login(check);
    }

    @Override
    public boolean check_server_p() throws RemoteException {
        return true;
    }


}