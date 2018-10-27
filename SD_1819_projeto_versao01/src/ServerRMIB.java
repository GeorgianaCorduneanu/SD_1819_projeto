import java.io.*;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerRMIB extends UnicastRemoteObject implements ServerRMI_I, Serializable {
    private static String location_s;
    private static String server_ip = "localhost";
    private static int server_port = 7000;
    private int multicast_port = 5000;
    private static String MULTICAST_ADDRESS = "224.3.2.1";
    private static ClienteRMI_I cliente;
    private static String nome = "msg";


    private ServerRMIB() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws RemoteException {
        location_s = "rmi://" + server_ip + ":" + server_port + "/" + nome;
        System.getProperties().put("java.security.policy", "file:\\C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        //System.getProperties().put("java.security.policy", "file:\\C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        System.setSecurityManager(new RMISecurityManager());
        //encontrar servio
        check_servidor();

        //para escrever no cliente
        write_on_server();

    }

    private static void imprime(String a, String ip, int port) {
        System.out.print(">>> ");
        try {
            System.out.print(cliente.getUtilizador().getUsername() + ":" + cliente.getUtilizador().getPassword());
            cliente.say_hello_to_server(a, ip, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void check_servidor() {
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

    private static void write_on_server() {
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
            assert frase != null;
            do {
                imprime(frase, server_ip, server_port);
                try {
                    frase = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!frase.equals(":/stop"));

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
        MulticastSocket socket = null;

        //mandar info a multicast buscar base de dados e adicionar registo
        System.out.println("Em Registar " + nome + " (" + c_i.getUtilizador().getUsername() + ") " + " verify: " + check);
        cliente = c_i;

        try {
            socket = new MulticastSocket();
            String message = "1;" + c_i.getUtilizador().getUsername() + ";" + c_i.getUtilizador().getPassword();
            byte[] buffer = message.getBytes();
            // create socket without binding it (only for sending)

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

        //c_i.check_registar(check);
    }

    @Override
    public Pacote_datagram login(String nome, ClienteRMI_I c_i) throws RemoteException {
        boolean check = true;
        Pacote_datagram pacote = null;
        MulticastSocket socket = null;
        String[] msg_cortada;
        try {
            //mandar info a multicast buscar na base de dados e verificar login
            System.out.println("Em Login: " + nome + " (" + c_i.getUtilizador().getUsername() + ") " + " verify: " + check);
            cliente = c_i;
            try {
                socket = new MulticastSocket();
                // create socket without binding it (only for sending)
                String message = "2;" + c_i.getUtilizador().getUsername() + ";" + c_i.getUtilizador().getPassword();
                byte[] buffer = message.getBytes();

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


        //c_i.check_login(check);
        System.out.println("Antes de receber a mensagem");
        String msg = recebe_multicast_socket();
        System.out.println("Depois de receber a mensagem");
        System.out.println("Mensagem enviada para login: " + msg);
        msg_cortada = msg.split(";");
        for (String item : msg_cortada) {
            System.out.print("\t" + item);
        }
        if (msg.equals("Erro ao fazer login")) {
            pacote = new Pacote_datagram(2, cliente);
            pacote.setMessage(409);
            return pacote;
        } else if (msg_cortada[1].equals("true")) {
            cliente.change_edir(true);
            System.out.println("Estou em set a true " + cliente.getUtilizador().getEditor());
            pacote = new Pacote_datagram(2, cliente);
            pacote.setMessage(200);
        } else if (msg_cortada[1].equals("false")) {
            cliente.change_edir(false);
            pacote = new Pacote_datagram(2, cliente);
            pacote.setMessage(200);
        }
        return pacote;
    }

    @Override
    public void enviaStringAoMulticast(String message) {
        byte[] buffer;
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();
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
            assert socket != null;
            socket.close();
        }

    }

    @Override
    public boolean check_server_p() throws RemoteException {
        return true;
    }

    @Override
    public String recebe_multicast_socket() {
        MulticastSocket socket = null;
        Pacote_datagram pacote = null;
        // while(true) {
        try {
            socket = new MulticastSocket(multicast_port);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.setLoopbackMode(false);//false quando recebe
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println(message);
            System.out.println(packet.getLength());
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert socket != null;
            socket.close();
        }
        return null;
    }

    @Override
    public ArrayList<Musica> recebe_musicas(int inicio, int fim) throws RemoteException {
        ArrayList<Musica> lista_musica = new ArrayList<>();
        String mensagem = "4;" + inicio + ";" + fim;
        enviaStringAoMulticast(mensagem);
        MulticastSocket socket = null;

        //c_i.check_login(check);
        System.out.println("Antes de receber a mensagem");
        try {
            socket = new MulticastSocket(multicast_port);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.setLoopbackMode(false);//false quando recebe
            socket.receive(packet);
            ByteArrayInputStream b_i = new ByteArrayInputStream(buffer);
            ObjectInputStream is = new ObjectInputStream(b_i);

            lista_musica = (ArrayList<Musica>) is.readObject();
            //Musica musica = (Musica)is.readObject();
//            System.out.println("MAIS UM TESTE"+is.readObject().getClass());
            System.out.println("Recebeu do Multicast");
            //lista_musica.add(musica);
            return lista_musica;
            //lista_musica = new String(packet.getData(), 0, packet.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            assert socket != null;
            socket.close();
        }
        /*System.out.println("A enviar para cliente");
        try {
            System.out.println("Entrou em try");
            socket = new MulticastSocket(multicast_port);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.setLoopbackMode(false);//false quando recebe
            socket.receive(packet);

            ByteArrayInputStream b_i = new ByteArrayInputStream(buffer);
            ObjectInputStream is = new ObjectInputStream(b_i);
            lista_musica = (ArrayList<Musica>)is.readObject();

            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Antes de enviar a musica");
        return lista_musica;
    }*/
        return lista_musica;
    }
}