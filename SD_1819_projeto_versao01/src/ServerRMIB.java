import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.*;
import java.rmi.server.*;
import java.util.concurrent.TimeUnit;

public class ServerRMIB extends UnicastRemoteObject implements ServerRMI_I, Serializable {
    private static String location_s;
    private static String server_ip = "localhost";
    private static int server_port = 7000;
    int multicast_port = 5000;
    private static String MULTICAST_ADDRESS = "224.3.2.1";
    static ClienteRMI_I cliente;

    public ServerRMIB() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws RemoteException {
        String nome = "msg";
        location_s = "rmi://" + server_ip + ":" + server_port + "/" + nome;
        System.getProperties().put("java.security.policy", "file:\\C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        //System.getProperties().put("java.security.policy", "file:\\C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        System.setSecurityManager(new RMISecurityManager());
        //encontrar servio
        check_servidor();

        //para escrever no cliente
        write_on_server();

    }

    public static void imprime(String a, String ip, int port) {
        System.out.print(">>> ");
        try {
            System.out.print(cliente.getUtilizador().getUsername() + ":" + cliente.getUtilizador().getPassword());
            cliente.say_hello_to_server(a, ip, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void check_servidor() {
        boolean check_principal = true;
        ServerRMI_I s_inter_principal = null;
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
        while (check_principal) {
            assert s_inter_principal != null;
            try {
                check_principal = s_inter_principal.check_server_p();
            } catch (IOException e) {
                System.out.println("*** Secundario Server Ativo ***");
                check_principal = false;
                break;
            }
            System.out.println("Principal Server Alive");
        }

    }

    public static void write_on_server() {
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
            do {
                imprime(frase, server_ip, server_port);
                try {
                    frase = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (frase.compareTo(":/stop") != 0);

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
        boolean check = true;
        Pacote_datagram pacote;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer;
        MulticastSocket socket = null;

        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            //mandar info a multicast buscar base de dados e adicionar registo
            System.out.println("Em Registar " + nome + " (" + c_i.getUtilizador().getUsername() + ") " + " verify: " + check);
            cliente = c_i;

            try {
                socket = new MulticastSocket();
                // create socket without binding it (only for sending)
                pacote = new Pacote_datagram(1, c_i);

                oos.writeObject(pacote);
                oos.flush();
                buffer = bos.toByteArray(); //passar o objeto Pacote_Datagram para byte

                socket.setLoopbackMode(true);//true quando envia
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicast_port);
                socket.send(packet);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Socket fechou");
                assert socket != null;
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //c_i.check_registar(check);
    }

    @Override
    public Pacote_datagram login(String nome, ClienteRMI_I c_i) throws RemoteException {
        boolean check = true;
        Pacote_datagram pacote,pacote2;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer;
        MulticastSocket socket = null;

        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            //mandar info a multicast buscar na base de dados e verificar login
            System.out.println("Em Login: " + nome + " (" + c_i.getUtilizador().getUsername() + ") " + " verify: " + check);
            cliente = c_i;
            try {
                socket = new MulticastSocket();
                // create socket without binding it (only for sending)
                //String message = "2;" + c_i.getUtilizador().getUsername() + ";" + c_i.getUtilizador().getPassword();
                pacote = new Pacote_datagram(2, c_i);

                oos.writeObject(pacote);
                oos.flush();
                buffer = bos.toByteArray();

                socket.setLoopbackMode(true);//true quando envia
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicast_port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("socket fechou");
                assert socket != null;
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        c_i.check_login(check);

        pacote2 = recebe_multicast_socket();

        if (pacote2 != null) {
            return pacote2;
        }
        return null;
    }

    @Override
    public void EnviaStringAoMulticast(String message, int n){
        byte[] buffer;
        MulticastSocket socket = null;
        try {
                socket =new MulticastSocket();
                buffer = message.getBytes();
                socket.setLoopbackMode(true);//true quando envia
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicast_port);
                TimeUnit.MILLISECONDS.sleep(75);
                socket.send(packet);
                
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }

    }
        @Override
    public boolean check_server_p() throws RemoteException {
        return true;
    }

    @Override
    public Pacote_datagram recebe_multicast_socket() throws RemoteException{
        MulticastSocket socket = null;
        Pacote_datagram pacote=null;
        // while(true) {
        try {
            socket = new MulticastSocket(multicast_port);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.setLoopbackMode(false);//false quando recebe

            socket.receive(packet);

            ByteArrayInputStream in = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            System.out.println(packet.getLength());
            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                try {
                    assert is != null;
                    pacote = (Pacote_datagram) is.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                assert pacote != null;
                return pacote;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert socket != null;
            socket.close();
        }
        return null;
    }
}