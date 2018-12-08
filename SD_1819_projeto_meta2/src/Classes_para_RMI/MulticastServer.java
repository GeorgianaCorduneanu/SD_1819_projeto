package Classes_para_RMI;

import com.sun.org.apache.xpath.internal.operations.Mult;

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
        String[] mensagem_cortada;
        String mensagem;
        Utilizador u;
        Utilizador v_ut;
        /*ficheiro_utilizador = "C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\data.bin";
        ficheiro_artista = "C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\artista.bin";
        ficheiro_album = "C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\album.bin";
        ficheiro_musicas = "C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\musica.bin";*/

        ficheiro_utilizador = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_meta2\\data.bin";
        ficheiro_album = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_meta2\\album.bin";
        ficheiro_artista = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_meta2\\artista.bin";
        ficheiro_musicas = "C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_meta2\\musica.bin";

        // write_obj_user();

        //this.connection = getConnection(); //obter a conexão com a base de dados
        read_obj();
        if (!user.isEmpty()) {
            System.out.println("----USERS----");
            for (Utilizador anUser : user) {
                //if (anUser.getUsername().equals("gi"))
                  //  user.get(user.indexOf(anUser)).setEditor(true);
                System.out.println("Utilizador: " + anUser.getUsername() + " | password: " + anUser.getPassword() + "| Editor: " + anUser.getEditor());
            }
            //write_obj_user();
            //read_obj();
            for (Utilizador anUser : user) {
                System.out.println("Utilizador: " + anUser.getUsername() + " | password: " + anUser.getPassword() + "| Editor: " + anUser.getEditor());
            }
        }
        if (!lista_artistas.isEmpty()) {
            System.out.println("----ARTISTAS----");
            for (Artista art : lista_artistas) {
                System.out.println("Nome Artista: " + art.getNome_artista() + "| compositor: " + art.getCompositor() + "| Informacao: " + art.getInformacao());
            }
        }
        if (!lista_musica.isEmpty()) {
            System.out.println("----MUSICAS----");
            for (Musica m : lista_musica) {
                System.out.println("Nome musica: " + m.getNome_musica() + "| compositor: " + m.getCompositor() + "| Duracao(min): " + m.getDuracao());
            }
        }
        if (!lista_album.isEmpty()) {
            System.out.println("----ALBUNS----");
            for (Album alb : lista_album) {
                System.out.println("Nome ALBUNS: " + alb.getNome_album() + "| descricao: " + alb.getDescricao() + "| Data de Lançamento: " + alb.getData_lancamento());
            }
        }
        // System.out.println("Listas vazias");
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

                System.out.println(packet.getLength());
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Recebeu a mensagem: " + message);
                mensagem_cortada = message.split(";");

                switch (mensagem_cortada[0]) {
                    case "1": // registo
                        u = new Utilizador(mensagem_cortada[1], mensagem_cortada[2]);
                        //Aqui confirmam-se duplos e adicionam-se os registos na bd
                        //inserir dados -> insere_dados(String[] mensagem, int numeto_tabela) type void
                        //insere_dados(mensagem_cortada, 1);
                        if (user.isEmpty()) {  //caso a lista esteja vazia
                            u = new Utilizador(u.getUsername(), u.getPassword(), true);
                            user.add(u);
                            write_obj_user();
                            enviaServerRMI("Primeiro utilizador");
                            break;
                        } else if(!verifica_utilizador(mensagem_cortada[1])){ //caso nao encontre o utilizado
                            user.add(u);
                            write_obj_user();
                            enviaServerRMI("Utilizador nao existente");
                            break;
                        }

                        System.out.println("Utilizador ja existente");
                        //  pacote.setMessage(409);
                        enviaServerRMI("Utilizador ja existente");

                        break;
                    case "2": //login
                        //aqui confirmam-se os dados do utilizador
                        v_ut = verifica_utilizador_login(mensagem_cortada[1], mensagem_cortada[2]);

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
                        if(encontraUtilizadorEditor(ut, mensagem_cortada[2]))
                            enviaServerRMI("Feito");
                        else
                            enviaServerRMI("Nao feito");
                        write_obj_user();
                        break;
                    case "4":  //listar musicas
                        System.out.println("A encontrar musicas na base de dados");
                        if (lista_musica.isEmpty()) {
                            enviaServerRMI("Nao existem musicas");
                            break;
                        }
                        envia_musicas_server_RMI(Integer.parseInt(mensagem_cortada[1]), Integer.parseInt(mensagem_cortada[2]));
                        break;
                    case "5": //inserir uma musica
                        System.out.println("A gravar a musica na base de dados");
                        if (inserir_musica_lista(mensagem_cortada[1], mensagem_cortada[2], mensagem_cortada[3]))
                            enviaServerRMI("Musica adicionada");
                        else
                            enviaServerRMI("Musica existente");
                        write_obj_user();
                        break;
                    case "6"://inserir um artista
                        System.out.println("A gravar utilizador na base de dados");
                        boolean bol;
                        bol = mensagem_cortada[2].equals("true") || mensagem_cortada[2].equals("True") || mensagem_cortada[2].equals("TRUE");

                        if (inserir_artista_lista(mensagem_cortada[1], bol, mensagem_cortada[3])) {
                            enviaServerRMI("Artista Adicionado");
                        } else
                            enviaServerRMI("Artista já existente");
                        write_obj_user();
                        break;
                    case "7"://inserir um album
                        System.out.println("A gravar album na base de dados");
                        if (inserir_album_lista(mensagem_cortada[1], mensagem_cortada[2], mensagem_cortada[3])) {
                            enviaServerRMI("Album adicionado");
                        } else
                            enviaServerRMI("Album já existente");
                        write_obj_user();
                        break;
                    case "8"://elimina uma musica
                        System.out.println("A eliminar uma música da base de dadso");
                        if (elimina_musica_list(mensagem_cortada[1])) {
                            enviaServerRMI("Musica eliminada");
                        } else
                            enviaServerRMI("Musica não encontrada");
                        write_obj_user();
                        break;
                    case "9"://elimina album
                        System.out.println("A eliminar um album da base de dados");
                        if (elimina_album_list(mensagem_cortada[1])) {
                            enviaServerRMI("Album eliminado");
                        } else
                            enviaServerRMI("Album não encontrado");
                        write_obj_user();
                        break;
                    case "10"://elimina artista
                        System.out.println("A eliminar um artista da base de dados");
                        if (elimina_artista_list(mensagem_cortada[1])) {
                            enviaServerRMI("Artista eliminado");
                        } else
                            enviaServerRMI("Artista não encontrado");
                        write_obj_user();
                        break;
                    case "11"://edita descricao album
                        if(edita_descricao_album(mensagem_cortada[1],mensagem_cortada[2],mensagem_cortada[3])){
                            enviaServerRMI("A editar descricao dum album");
                        }else
                            enviaServerRMI("Upps algo errado!");
                        write_obj_user();
                        break;
                    case "12"://edita data album
                        if(edita_data_album(mensagem_cortada[1],mensagem_cortada[2],mensagem_cortada[3])){
                            enviaServerRMI("A editar data dum album");
                        }else
                            enviaServerRMI("Upps algo errado!");
                        write_obj_user();
                        break;
                    case "13"://pesquisar uma musica
                        enviaServerRMI(pesquisar(13,mensagem_cortada[1]));
                        break;
                    case "14": //pesquisar album
                        enviaServerRMI(pesquisar(14, mensagem_cortada[1]));
                    case "15":
                        enviaServerRMI(pesquisar(15, mensagem_cortada[1]));
                    default:
                        System.out.println("Nenhuma das opcoes: " + "|" + mensagem_cortada[0] + "|");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("nao podes fechar este socket");
            assert socket != null;
            socket.close();
        }
    }

    private String pesquisar(int protocolo, String nome){
            String mensagem_erro= null;
            switch (protocolo) {
                case 13: //para musica
                    for (Musica item : lista_musica) {
                        if (item.getNome_musica().toLowerCase().equals(nome)) //pesquisar nome em minusculo
                            return item.getNome_musica() + " -> " + item.getCompositor() + " : " + item.getDuracao();
                    }
                    mensagem_erro =  "Musica inexistente";
                    break;
                case 14: //para album
                    for(Album item:lista_album){
                        if(item.getNome_album().toLowerCase().equals(nome))
                            return item.getNome_album() + " -> " + item.getData_lancamento() + " : " + item.getDescricao();
                    }
                    mensagem_erro =  "Album inexistente";
                    break;
                case 15: //para artista
                    for(Artista item:lista_artistas){
                        if(item.getNome_artista().toLowerCase().equals(nome))
                            return item.getNome_artista() + " -> " + item.getCompositor() + " : " + item.getInformacao();
                    }
                    mensagem_erro = "Artista inexistente";
                default:
                    break;
            }
            return mensagem_erro;
    }
    private boolean inserir_musica_lista(String nome, String compositor, String duracao) {
        for (Musica m : lista_musica) {
            if (m.getCompositor().equals(compositor) && m.getNome_musica().equals(nome)) {
                System.out.println("Musica ja existente");
                return false;
            }
        }
        Musica aux = new Musica(nome, compositor, duracao);
        lista_musica.add(aux);
        return true;
    }

    private boolean inserir_album_lista(String nome, String descricao, String data) {
        for (Album a : lista_album) {
            if (a.getNome_album().equals(nome)) {
                System.out.println("Musica ja existente");
                return false;
            }
        }
        Album aux = new Album(nome, descricao, data);
        lista_album.add(aux);
        return true;
    }

    private boolean inserir_artista_lista(String nome, boolean compositor, String descricao) {
        for (Artista a : lista_artistas) {
            if (a.getNome_artista().equals(nome)) {
                System.out.println("Artista já existente");
                return false;
            }
        }
        Artista aux = new Artista(nome, compositor, descricao);
        lista_artistas.add(aux);
        return true;
    }

    private boolean edita_descricao_album(String nome, String descricao, String nome_user) {
        int i = 0;
        Utilizador aux = null;
        for (Utilizador u : user) {
            if (u.getUsername().equals(nome_user)) {
                aux = u;
            }
            System.out.println("USERS: "+u.getUsername());
        }
        for (Album a : lista_album) {
            if (a.getNome_album().equals(nome)) {
                lista_album.get(i).setDescricao(descricao, aux);
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean edita_data_album(String nome, String data, String nome_user) {
        int i = 0;
        Utilizador aux = null;
        for (Utilizador u : user) {
            if (u.getUsername().equals(nome_user)) {
                aux = u;
            }
        }
            for (Album a : lista_album) {
                if (a.getNome_album().equals(nome)) {
                    lista_album.get(i).setData_lancamento(data, aux);
                    return true;
                }
                i++;
            }

            return false;
        }


    private boolean elimina_musica_list(String nome){
        int i = 0;
        for (Musica m : lista_musica) {
            if (m.getNome_musica().equals(nome)) {
                lista_musica.remove(i);
                return true;
            }

            i++;
        }
        return false;
    }
        private boolean elimina_album_list (String nome){
            int i = 0;
            for (Album a : lista_album) {
                if (a.getNome_album().equals(nome)) {
                    lista_album.remove(i);
                    return true;
                }

                i++;
            }
            return false;
        }
        private boolean elimina_artista_list (String nome){
            int i = 0;
            for (Artista a : lista_artistas) {
                if (a.getNome_artista().equals(nome)) {
                    lista_artistas.remove(i);
                    return true;
                }
                i++;
            }
            return false;
        }

        private void write_obj_user () {
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

        private void read_obj () {
            try {
                FileInputStream fis = new FileInputStream(ficheiro_utilizador);
                FileInputStream fis_artista = new FileInputStream(ficheiro_artista);
                FileInputStream fis_musica = new FileInputStream(ficheiro_musicas);
                FileInputStream fis_album = new FileInputStream(ficheiro_album);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ObjectInputStream ois_artista = new ObjectInputStream(fis_artista);
                ObjectInputStream ois_musica = new ObjectInputStream(fis_musica);
                ObjectInputStream ois_album = new ObjectInputStream(fis_album);
                user = (ArrayList<Utilizador>) ois.readObject();
                lista_album = (ArrayList<Album>) ois_album.readObject();
                lista_artistas = (ArrayList<Artista>) ois_artista.readObject();
                lista_musica = (ArrayList<Musica>) ois_musica.readObject();
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

        private Utilizador verifica_utilizador_login (String username, String password) { //verifica se o utilizador está na array list
            for (Utilizador utilizador : user) {
                if (utilizador.getUsername().equals(username) && utilizador.getPassword().equals(password)) {
                    return utilizador;
                }
            }
            return null;
        }
        private boolean verifica_utilizador (String username){ //verifica se o utilizador está na array list
            for (Utilizador utilizador : user) {
                if (utilizador.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        }

        private boolean encontraUtilizadorEditor (Utilizador u, String privilegio){
            for (Utilizador anUser : user) {
                if (anUser.getUsername().equals(u.getUsername())) {
                    if(privilegio.equals("1"))
                       anUser.setEditor(true);
                    else
                        anUser.setEditor(false);
                    return true;
                }
            }
            return false;
        }
        private void envia_musicas_server_RMI ( int inicio, int fim){
            MulticastSocket socket = null;
            ArrayList<Musica> sublista;

            try {
                socket = new MulticastSocket();
                ByteArrayOutputStream b_a = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(b_a);
                    if (lista_musica.size() == 1) {
                        out.writeObject(lista_musica.get(0));
                        System.out.println(lista_musica.get(0).getNome_musica());
                    } else if (fim > lista_musica.size() && lista_musica.size() != 0) {
                        fim = lista_musica.size();
                        sublista = new ArrayList<>(lista_musica.subList(inicio, fim));
                        for (Musica m : sublista) {
                            System.out.println(m.getNome_musica() + " : " + m.getCompositor());
                        }
                        out.writeObject(sublista);
                    } else {
                        sublista = new ArrayList<>(lista_musica.subList(inicio, fim));
                        out.writeObject(sublista);
                    }
                    byte[] buffer = b_a.toByteArray();
                    try {
                        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 5000);
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
        private void enviaServerRMI (String message){
            MulticastSocket socket = null;
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


