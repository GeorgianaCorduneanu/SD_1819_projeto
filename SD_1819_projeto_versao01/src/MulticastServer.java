import java.io.*;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

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
        String[] mensagem_cortada;
        boolean v_ut;
        //this.connection = getConnection(); //obter a conexão com a base de dados
        read_obj();
        for(int i=0;i<user.size();i++) {
            System.out.println("Utilizador: "+user.get(i).getUsername()+" | password: "+user.get(i).getPassword());
        }
        int id;
        try {
            socket = new MulticastSocket(PORT); //create socket and bindint to port
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            socket.setTimeToLive(ttl); //definir o numero de routers a saltar

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.setLoopbackMode(false);//false quando recebe
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                mensagem_cortada = message.split(";");

                id = Integer.parseInt(mensagem_cortada[0]);
                switch (id) {
                    case 1: // registo
                        System.out.println("Registo! A enviar utilizador para arraylist!\n" + "Utilizador: " + mensagem_cortada[1] + ", passoword: " + mensagem_cortada[2]);
                        //Aqui confirmam-se duplos e adicionam-se os registos na bd
                        //inserir dados -> insere_dados(String[] mensagem, int numeto_tabela) type void
                        //insere_dados(mensagem_cortada, 1);
                        user.add(new Utilizador(mensagem_cortada[1],mensagem_cortada[2]));
                        write_obj_user();
                        break;
                    case 2: //login
                        //aqui confirmam-se os dados do utilizador
                        v_ut=verifica_utilizador(mensagem_cortada[1],mensagem_cortada[2]);
                        if(v_ut==true) { //verifica se o utilizador está na array list
                            System.out.printf("Utilizador Encontrado!");
                            enviaServerRMI(socket);
                        }else{
                            System.out.printf("Utilizador não encontrado");
                        }
                        break;
                }
                //System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
    public void write_obj_user() {
        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin");
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
            FileInputStream fis = new FileInputStream("C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin");
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
    public boolean verifica_utilizador(String username, String password){ //verifica se o utilizador está na array list
        for(Utilizador utilizador : user){
            if(utilizador.getUsername().equals(username)){
                if(utilizador.getPassword().equals((password))){
                    return true;
                }
            }
        }
        return false;
    }

    public void enviaServerRMI(MulticastSocket socket) {
        try {
            String message = "Login bem sucedido! ";
            byte[] buffer = message.getBytes();
            socket.setLoopbackMode(true);//true quando envia
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            socket.close();
        }*/

    }
}
    /*
    public void insere_dados(String [] mensagem, int numero_tabela){
        Utilizador u;
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
                /*            escrever nos ficheiros de objeto       */
                //caso o ficheiro esteja vazio escreve-se Utilizador(mensagem[1], mensagem[2], true) para dar privilegios
                //caso contrario
                u = new Utilizador(mensagem[1], mensagem[2]);
                //escrever no ficheiro
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


