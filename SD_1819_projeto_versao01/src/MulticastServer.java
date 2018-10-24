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
    private static ArrayList<Utilizador> lista_user = new ArrayList<>();

    public static void main(String[] args) {
        read_obj();
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastServerEnvia sender = new MulticastServerEnvia();
        sender.start();
    }

    public MulticastServer() {

        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println("O servidor Multicast nr " + this.getName() + " está a correr!!");
        String[] mensagem_cortada;
        Utilizador u;
        //this.connection = getConnection(); //obter a conexão com a base de dados
        for(int i=0;i<lista_user.size();i++) {
            System.out.println("Utilizador: "+lista_user.get(i).getUsername()+" | password: "+lista_user.get(i).getPassword());
        }

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

                String message = new String(packet.getData(), 0, packet.getLength());
                mensagem_cortada = message.split(";");
                u = new Utilizador(mensagem_cortada[1], mensagem_cortada[2]);
                id = Integer.parseInt(mensagem_cortada[0]);
                switch (id) {
                    case 1: // registo
                        //Aqui confirmam-se duplos e adicionam-se os registos na bd
                        //inserir dados -> insere_dados(String[] mensagem, int numeto_tabela) type void
                        insere_dados(mensagem_cortada, 1);
                        break;
                    case 2: //login
                        //aqui confirmam-se os dados do utilizador e sqldatabase
                        System.out.println("A verificar se dados sao corretos");
                        if(verifica_login(u)){
                            System.out.println("Login verificado com sucesso");
                            //avisar o utilizador que foi verificado com sucesso
                        }else{
                            System.out.println("Palavra passe ou username incorreto");
                            //avisar o utilizador que username ou pass estao erradas
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
    public void write_obj() {
        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lista_user);
            oos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    private static boolean read_obj(){
        try{
            FileInputStream fis = new FileInputStream("C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            lista_user = (ArrayList<Utilizador>) ois.readObject();
            ois.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ficheiro de objetos está Vazio");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void insere_dados(String [] mensagem, int numero_tabela){
        Utilizador u = new Utilizador(mensagem[1], mensagem[2]);
        //criar um statement para inserir dados na bd
        //Statement statement;
       //try {
           // statement = connection.prepareStatement();
        //} catch (SQLException e) {
           // e.printStackTrace();
        //
        //insere-se o codigo em sql
        // statement.executeUpdate(Select nome from);

        System.out.println(mensagem[1] + " : " + mensagem[2]);
        if(verifica_utilizador_existente(u)){
            //avisa utilizador que já existe utilizador com esse username
            System.out.println("JA EXISTE UTILIZADOR");
            return;
        }
        if(lista_user.isEmpty()) //caso seja o primeiro a ser adicionado
            u.setEditor(true);
        lista_user.add(u);
        write_obj();
        //enviar mensagem para cliente a dizer que a operacao foi feita com sucesso
        System.out.println("Operacao efetuada com sucesso");

    }
    private boolean verifica_login(Utilizador u){
        for(Utilizador item:lista_user){
            if(item.getUsername().equals(u.getUsername()) && item.getPassword().equals(u.getPassword()))
                return true;
        }
        return false;
    }
    private boolean verifica_utilizador_existente(Utilizador u){
        for(Utilizador item: lista_user){
            if(item.getUsername().equals(u.getUsername() )){
                return true;
            }
        }
        return false;
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







    /*public Connection getConnection(){
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
