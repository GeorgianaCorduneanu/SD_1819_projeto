import javax.sound.midi.SysexMessage;
import java.io.*;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MulticastServer extends Thread implements Serializable {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 5000;
    private Connection connection;
    ArrayList<Utilizador> user = new ArrayList<>();

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    public MulticastServer() {

        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println("O servidor Multicast nr " + this.getName() + " está a correr!!");
        Pacote_datagram pacote = null;

        boolean v_ut;
        //this.connection = getConnection(); //obter a conexão com a base de dados
        read_obj();
        for(int i=0;i<user.size();i++) {
            System.out.println("Utilizador: "+user.get(i).getUsername()+" | password: "+user.get(i).getPassword());
        }
        try {


            while (true) {
                socket = new MulticastSocket(PORT);
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                socket.joinGroup(group);
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.setLoopbackMode(false);//false quando recebe
                System.out.println("Waiting to receive...");
                socket.receive(packet);

                ByteArrayInputStream in = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                System.out.println(packet.getLength());
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    pacote = (Pacote_datagram) is.readObject();
                    System.out.println(pacote.getCliente().getUtilizador().getUsername() + " : " + pacote.getCliente().getUtilizador().getPassword());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }catch (EOFException e){
                    System.out.println("Chegou ao fim");
                    is.close();
                }

                assert pacote != null;
                if (400 != pacote.getFuncao()) {
                    System.out.println("Message received");

                    switch (pacote.getFuncao()) {
                        case 1: // registo
                            //Aqui confirmam-se duplos e adicionam-se os registos na bd
                            //inserir dados -> insere_dados(String[] mensagem, int numeto_tabela) type void
                            //insere_dados(mensagem_cortada, 1);
                            if(!verifica_utilizador(pacote.getCliente().getUtilizador())) { //caso o utilziador já exista nao adiciona a lista
                                System.out.println("Utilzador nao existente");
                                if (user.isEmpty())
                                    pacote.getCliente().getUtilizador().setEditor(true);
                                user.add(pacote.getCliente().getUtilizador());
                                write_obj_user();
                                pacote.setMessage(201);
                                enviaServerRMI(pacote);
                                break;
                            }
                            System.out.println("Utilizador ja existente");
                            pacote.setMessage(409);
                            enviaServerRMI(pacote);
                            break;
                        case 2: //login
                            //aqui confirmam-se os dados do utilizador
                            v_ut = verifica_utilizador(pacote.getCliente().getUtilizador());
                            String msg;
                            if (v_ut) { //verifica se o utilizador está na array list
                                System.out.println("Utilizador Encontrado!");
                                pacote.setMessage(202);
                                enviaServerRMI(pacote);
                            } else {
                                System.out.println("Utilizador não encontrado!");
                                pacote.setMessage(302);
                                enviaServerRMI(pacote);
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("nao podes fechar este socket");
            socket.close();
        }
    }
    public void write_obj_user() {
        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\data.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public void read_obj(){
        try{
            //FileInputStream fis = new FileInputStream("C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin");
            FileInputStream fis = new FileInputStream("C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            user = (ArrayList<Utilizador>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ficheiro de objetos está Vazio");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean verifica_utilizador(Utilizador u){ //verifica se o utilizador está na array list
        for(Utilizador utilizador : user){
            if(utilizador.getUsername().equals(u.getUsername()) && utilizador.getUsername().equals(u.getUsername()))
                return true;
        }
        return false;
    }

    public void enviaServerRMI(Pacote_datagram pacote) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            MulticastSocket socket=null;
            try {
                oos.writeObject(pacote);
                oos.flush();
                socket = new MulticastSocket();
                byte[] buffer = bos.toByteArray();
                //socket.setLoopbackMode(true);//true quando envia
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                //System.out.println("sending to rmi server: " + message);
                TimeUnit.MILLISECONDS.sleep(50);
                socket.send(packet);
                System.out.println("Mensagem enviada msg: " + pacote.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    /*
    public void insere_dados(String [] mensagem, int numero_tabela){
        //criar um statement para inserir dados na bd
        Statement statement;
       //try {
           // statement = connection.prepareStatement();
        //} catch (SQLException e) {
           // e.printStackTrace();
        }//
        switch (numero_tabela){
            case 1: //registo
                //insere-se o codigo em sql
                // statement.executeUpdate(Select nome from);
                System.out.println(mensagem[1] + " : " + mensagem[2]);
                break;
            case 2: //artista
                //insere-se o codigo em sql
                // statement.executeUpdate();

                break;
            case 3: //musica
                //insere-se o codigo em sql
                // statement.executeUpdate();
                break;
                default:
                    System.out.println("Not a correct number for table");
                  break;
        }
    }

    public Connection getConnection(){
        Connection  connection = null;
        String bd_hostname = "127.0.0.1";
        String bd_name = "base_dados_2018_bd";
        String bd_port = "3306";
        String bd_user = "root";
        String bd_password = "bd";
        // register your mysql jdbc driver with the java application
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String connection_url = "jdbc:mysql://"+ bd_hostname + ":"+ bd_port +"/" + bd_name + "?useSSL=false&autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String schema;
        //aqui fazemos a conexao com a base de dados
        try {
            //fazer a conexão do servidor base de dados com java
            //connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/base_dados_2018_bd?useSSL=false&autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","bd");
            connection = DriverManager.getConnection(connection_url, bd_user, bd_password);
            System.out.println("Successful connection with server");
            //obter a base de dados que tem no servidor
            //schema = connection.getSchema(); //nao sei porque nao fucniona com schema, acho que
            //e porque esta versao do java nao suporta shema
            //mas funciona com getCatalog()
            schema = connection.getCatalog();
            System.out.println("Successful connection - Schema: " + schema);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }*/


