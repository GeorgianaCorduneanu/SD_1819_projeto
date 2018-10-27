import java.io.*;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MulticastServer extends Thread implements Serializable {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 5000;
    private Connection connection;
    private ArrayList<Utilizador> user = new ArrayList<>();
    private ArrayList<Artista> lista_artistas = new ArrayList<>();
    private ArrayList<Album> lista_album = new ArrayList<>();
    private ArrayList<Musica> lista_musica = new ArrayList<>();
    private String ficheiro_utilizador;
    private String ficheiro_musicas;
    private String ficheiro_album;
    private String ficheiro_artista;
    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    private MulticastServer() {

        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println("O servidor Multicast nr " + this.getName() + " está a correr!!");
        Pacote_datagram pacote = null;
        String[] mensagem_cortada;
        String mensagem;
        Utilizador u;
        Utilizador v_ut;
        ficheiro_utilizador = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin";
        ficheiro_album = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\album.bin";
        ficheiro_artista = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\artista.bin";
        ficheiro_musicas = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\musica.bin";

       // write_obj_user();

        //this.connection = getConnection(); //obter a conexão com a base de dados
        read_obj();
        if(!user.isEmpty()) {
            for (Utilizador anUser : user) {
                if (anUser.getUsername().equals("gi"))
                    user.get(user.indexOf(anUser)).setEditor(true);
                System.out.println("Utilizador: " + anUser.getUsername() + " | password: " + anUser.getPassword() + "| Editor: " + anUser.getEditor());
            }
           write_obj_user();
            read_obj();
            for (Utilizador anUser : user) {
                System.out.println("Utilizador: " + anUser.getUsername() + " | password: " + anUser.getPassword() + "| Editor: " + anUser.getEditor());
            }
        }
        System.out.println("Listas vazias");
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
                String message = new String(packet.getData(),0,packet.getLength());
                System.out.println("Recebeu a mensagem: " + message);
                mensagem_cortada =message.split(";");

                switch (mensagem_cortada[0]) {
                    case "1": // registo
                        u = new Utilizador(mensagem_cortada[1], mensagem_cortada[2]);
                        //Aqui confirmam-se duplos e adicionam-se os registos na bd
                        //inserir dados -> insere_dados(String[] mensagem, int numeto_tabela) type void
                        //insere_dados(mensagem_cortada, 1);
                        /*if(!verifica_utilizador(mensagem_cortada[1])) { //caso o utilizador já exista nao adiciona a lista
                            System.out.println("Utilzador nao existente");
                            if (user.isEmpty())
                                u.setEditor(true);

                            //System.out.println("teste: "+pacote.getCliente().getUtilizador().getEditor());
                            user.add(u);
                            write_obj_user();
                            //pacote.setMessage(201);
                            enviaServerRMI("Utilizador criado com sucesso");
                            break;
                        }*/
                        if(user.isEmpty()) {
                            u = new Utilizador(u.getUsername(), u.getPassword(), true);
                            user.add(u);
                            write_obj_user();
                            break;
                        }else if(!user.isEmpty()){
                            if(!verifica_utilizador(mensagem_cortada[1]))
                                user.add(u);
                            write_obj_user();
                            break;
                        }
                        System.out.println("Utilizador ja existente");
                      //  pacote.setMessage(409);
                        enviaServerRMI("Utilizador ja existente");

                        break;
                    case "2": //login
                        //aqui confirmam-se os dados do utilizador
                        v_ut = verifica_utilizador_login(mensagem_cortada[1],mensagem_cortada[2]);

                        if (v_ut != null) { //verifica se o utilizador está na array list
                            mensagem = "Login bem sucedido ;" + v_ut.getEditor();
                            System.out.println("Utilizador Encontrado ; " + v_ut.getEditor());
                          // pacote.setMessage(202);
                            enviaServerRMI(mensagem);
                        } else {
                            System.out.println("Utilizador não encontrado!");
                           // pacote.setMessage(302);
                            enviaServerRMI("Erro ao fazer login");
                        }
                        break;
                    case "3": //dar permissao de editor a outro utilizador
                        System.out.println("A encontrar user na base de dados");
                        Utilizador ut = new Utilizador(mensagem_cortada[1], null);
                       // System.out.println("Username a dar permissao: "+pacote.getCliente().getUtilizador());
                        encontraUtilizadorEditor(ut);
                        write_obj_user();
                        break;
                    case "4":  //pesquisar musicas
                        System.out.println("A encontrar musicas na base de dados");
                        if(lista_musica.isEmpty()) {
                            enviaServerRMI("Nao existem musicas");
                            break;
                        }
                        envia_musicas_server_RMI(Integer.parseInt(mensagem_cortada[1]), Integer.parseInt(mensagem_cortada[2]));
                        break;
                    case "5": //inserir uma musica
                        System.out.println("A gravar a musica na base de dados");
                        if(inserir_musica_lista(mensagem_cortada[1], mensagem_cortada[2], mensagem_cortada[3]))
                            enviaServerRMI("Musica adicionada");
                        else
                            enviaServerRMI("Musica existente");
                        write_obj_user();
                        break;
                    default:
                        System.out.println("Nenhuma das opcoes: " + "|" + mensagem_cortada[0] + "|");
                    }

                }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("nao podes fechar este socket");
            assert socket != null;
            socket.close();
        }
    }
    private boolean inserir_musica_lista(String nome, String compositor, String duracao){
        for (Musica m:lista_musica) {
            if(m.getDuracao().equals(duracao) && m.getCompositor().equals(compositor) && m.getNome_musica().equals(duracao)){
                System.out.println("Musica ja existente");
                return false;
            }

        }
        Musica aux = new Musica(nome, compositor, duracao);
        lista_musica.add(aux);
        return true;
    }
    private void write_obj_user() {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro_utilizador);
            FileOutputStream fos_artista = new FileOutputStream(ficheiro_artista);
            FileOutputStream fos_album = new FileOutputStream(ficheiro_album);
            FileOutputStream fos_musica = new FileOutputStream(ficheiro_musicas);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            ObjectOutputStream oos_album = new ObjectOutputStream(fos_album);
            ObjectOutputStream oos_musica = new ObjectOutputStream(fos_musica);
            ObjectOutputStream oos_artista = new ObjectOutputStream(fos_artista);
            oos_album.flush();
            oos_artista.flush();
            oos.flush();
            oos_musica.flush();
            oos.writeObject(user);
            oos_album.writeObject(lista_album);
            oos_musica.writeObject(lista_musica);
            oos_artista.writeObject(lista_artistas);
            oos.close();
            oos_album.close();
            oos_artista.close();
            oos_musica.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void read_obj(){
        try{
            FileInputStream fis = new FileInputStream(ficheiro_utilizador);
            FileInputStream fis_artista = new FileInputStream(ficheiro_artista);
            FileInputStream fis_musica = new FileInputStream(ficheiro_musicas);
            FileInputStream fis_album = new FileInputStream(ficheiro_album);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ObjectInputStream ois_artista = new ObjectInputStream(fis_artista);
            ObjectInputStream ois_musica = new ObjectInputStream(fis_musica);
            ObjectInputStream ois_album = new ObjectInputStream(fis_album);
            user = (ArrayList<Utilizador>) ois.readObject();
            lista_album = (ArrayList<Album>)ois_album.readObject();
            lista_artistas = (ArrayList<Artista>)ois_artista.readObject();
            lista_musica = (ArrayList<Musica>)ois_musica.readObject();
            ois.close();
            ois_album.close();
            ois_artista.close();
            ois_musica.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ficheiro de objetos está Vazio");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Utilizador verifica_utilizador_login(String username, String password){ //verifica se o utilizador está na array list
        for(Utilizador utilizador : user){
            if(utilizador.getUsername().equals(username) && utilizador.getPassword().equals(password)){
                return utilizador;
            }
        }
        return null;
    }
    private boolean verifica_utilizador(String username){ //verifica se o utilizador está na array list
        for(Utilizador utilizador : user){
            if(utilizador.getUsername().equals(username)){
                    return true;
                }
            }
        return false;
    }

    private void encontraUtilizadorEditor(Utilizador u){
        for (Utilizador anUser : user) {
            if (anUser.getUsername().equals(u.getUsername())) {
                anUser.setEditor(true);
            }
        }
    }
    private void envia_musicas_server_RMI(int inicio, int fim){
        MulticastSocket socket= null;
        ArrayList<Musica> sublista;

        try {
            socket = new MulticastSocket();
            ByteArrayOutputStream b_a = new ByteArrayOutputStream();
            try {
                ObjectOutputStream out = new ObjectOutputStream(b_a);
                if(lista_musica.size() == 1) {
                    out.writeObject(lista_musica.get(0));
                    System.out.println(lista_musica.get(0).getNome_musica());
                }else if(fim>lista_musica.size() && lista_musica.size()!=0){
                    fim = lista_musica.size();
                    sublista = new ArrayList<>(lista_musica.subList(inicio, fim-1));
                    for (Musica m: sublista) {
                        System.out.println(m.getNome_musica() + " : " + m.getCompositor());
                    }
                    out.writeObject(sublista);
                }
                else{
                    sublista = new ArrayList<>(lista_musica.subList(inicio, fim-1));
                    out.writeObject(sublista);
                }
                byte [] buffer = b_a.toByteArray();
                try {
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group,5000);
                    socket.send(packet);
                    socket.close();
                    System.out.println("Enviou pacote com musica");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void enviaServerRMI(String message) {
        MulticastSocket socket=null;
            try {
                socket = new MulticastSocket();
                byte[] buffer = message.getBytes();
                socket.setLoopbackMode(true);//true quando envia
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                //System.out.println("sending to rmi server: " + message);
                TimeUnit.MILLISECONDS.sleep(75);
                socket.send(packet);
                System.out.println("Mensagem enviada msg: " + message);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                assert socket != null;
                socket.close();
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


