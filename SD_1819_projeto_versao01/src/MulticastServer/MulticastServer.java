package MulticastServer;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.rmi.*;


public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 5000;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastServerEnvia sender = new MulticastServerEnvia();
        sender.start();
    }

    public MulticastServer() {

        super("Server " + (long) (Math.random() * 1000));
    }

    public void run(){
        MulticastSocket socket= null;
        System.out.println("O servidor Multicast nr "+this.getName() + " está a correr!!");
        String [] mensagem_cortada;
        Connection connection = getConnection(); //obter a conexão com a base de dados

        int id;
        try {
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.setLoopbackMode(false);//false quando recebe
                socket.receive(packet);

                String message = new String(packet.getData(),0,packet.getLength());
                mensagem_cortada = message.split(";");

                id = Integer.parseInt(mensagem_cortada[0]);
                switch (id){
                    case 1: // registo
                        System.out.println("Registo! A enviar utilizador para arraylist!\n"+"Utilizador: "+mensagem_cortada[1] + ", passoword: "+mensagem_cortada[2]);
                        //Aqui confirmam-se duplos e adicionam-se os registos na bd
                        break;
                    case 2: //login
                        //aqui confirmam-se os dados do utilizador e sqldatabase
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
    }
}


class MulticastServerEnvia extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 5000;

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            Scanner keyboardScanner = new Scanner(System.in);
            while (true) {
                String message = keyboardScanner.nextLine();
                byte[] buffer = message.getBytes();
                socket.setLoopbackMode(true);//true quando envia
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }

    }
}