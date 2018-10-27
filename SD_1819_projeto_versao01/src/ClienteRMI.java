import com.sun.security.ntlm.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class ClienteRMI extends UnicastRemoteObject implements ClienteRMI_I, Serializable {
    private static Utilizador cliente_corrente;
    private static  String server_ip = "localhost";
    private static int server_port = 7000;
    private static String name = "msg";

    private ClienteRMI() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws IOException {
        String frase_crua=null;  //variavel para guardar input
        String frase_sem_espaco;
        String [] frase_chave_valor = null; //array para guardar cada par chave valor
        String location_s;
        String txt;
        String codigo =null;
        Pacote_datagram pacote;
        //definir plicy
        System.getProperties().put("java.security.policy", "file:\\C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        //System.getProperties().put("java.security.policy", "file:\\C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
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
            assert frase_chave_valor != null;
            switch (frase_chave_valor[0]) {
                case "login":
                    cliente = login_cliente(frase_chave_valor[1], frase_chave_valor[2]);
                    System.out.println("A enviar para servidor");
                    try {
                        assert server_i != null;
                        //txt = server_i.login(location_s, cliente);
                        pacote = server_i.login(location_s, cliente);
                        System.out.println(pacote.getMessage().get_Message(pacote.getMessage().getNumber()));
                        //pacote =server_i.login(location_s, cliente);
                       // codigo = pacote.getMessage().get_Message(pacote.getMessage().getNumber());
                        //System.out.println(server_i.recebe_multicast_socket()); //receber mensagem de multicast ou nao
                       // System.out.println("Devia ter enviado mensagem");
                       // System.out.println(codigo);
                        //System.out.println("TESTE: "+pacote.getCliente().getUtilizador().getEditor());
                       // if(codigo.equals("ACCEPTED") && !pacote.getCliente().getUtilizador().getEditor()){ //tipo de menu
                            //menu_login_normal();
                       // }
                        //else if(pacote.getCliente().getUtilizador().getEditor()){
                        if(pacote.getMessage().getNumber() == 409){
                            break;
                        }
                        cliente_corrente = pacote.getCliente().getUtilizador();
                        System.out.println(pacote.getMessage().getNumber());
                        System.out.println(cliente_corrente.getUsername() + " : " + cliente_corrente.getPassword() + " : " + cliente_corrente.getEditor());
                        //check_editor();
                        if(cliente_corrente.getEditor())
                            menu_login_editor(server_i);
                        else
                            menu_login_normal(server_i);
                       //}else
                            //System.out.printf("codigo: "+codigo);
                    }catch (IOException e) {
                       /* try {
                            pacote =server_i.login(location_s, cliente);
                            codigo = pacote.getMessage().get_Message(pacote.getMessage().getNumber());
                           // System.out.println(codigo);
                            if(codigo.equals("ACCEPTED")){
                                menu_login_normal();
                            }
                           // System.out.println(server_i.recebe_multicast_socket()); //receber mensagem de multicast ou nao
                          //  System.out.println("Devia ter enviado mensagem");
                        } catch (NotBoundException e1) {
                            e1.printStackTrace();
                        }*/
                       e.printStackTrace();
                        break;
                    }
                    break;
                case "registar":
                    try {
                        cliente = registar_cliente(frase_chave_valor[1], frase_chave_valor[2]);
                        System.out.println("A enviar para servidor");
                        try {
                            assert server_i != null;
                            server_i.subscribe(location_s, cliente);
                          //  System.out.println(server_i.recebe_multicast_socket()); //receber mensagem de multicast ou nao
                           // System.out.println("Devia ter enviado mensagem");
                        }catch (IOException e) {
                            try {
                                server_i = (ServerRMI_I) Naming.lookup(location_s);
                                server_i.subscribe(location_s, cliente);
                                System.out.println("Enviou!");
                               // System.out.println(server_i.recebe_multicast_socket()); //receber mensagem de confirmacao ou nao do multicast

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
        }while(!":/stop".equals(frase_crua));
    }

    private static ClienteRMI registar_cliente(String username, String passe){
        ClienteRMI cliente = null;
        System.out.println("*** Registar ***");
        try {
            cliente = new ClienteRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assert cliente != null;
        cliente.cliente_corrente = new Utilizador(username, passe);
        //System.out.println("username: " + cliente.getUtilizador().getUsername() + " : " + cliente.getUtilizador().getPassword());
        return cliente;
    }

    private static ClienteRMI login_cliente(String username, String passe) {
        ClienteRMI cliente = null;
        System.out.println("*** Login ***");
        try {
            cliente = new ClienteRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assert cliente != null;
        cliente.cliente_corrente = new Utilizador(username, passe);
        return cliente;
    }
    public static void menu_login_normal(ServerRMI_I server_i) throws IOException {
        Scanner reader = new Scanner(System.in);
       /* if( System.getProperty( "os.name" ).startsWith( "Window" ) )
            Runtime.getRuntime().exec("cls");
        else
            Runtime.getRuntime().exec("clear");
*/
        System.out.println("-----------Menu-----------\n"
                +"[1]Pesquisar música\n"
                +"[2]Consultar detalhes album\n"
                +"[3]Consultar detalhes artista\n"
                +"[4]Upload de música\n"
                +"[5]Download de música\n"
                +"[6]Partilhar uma música\n"
                +"[7]Logout");

        int opcao = reader.nextInt();
        switch (opcao) {
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                System.out.println("Insira uma opção válida!");
        }

    }
    private static void menu_login_editor(ServerRMI_I server_i) throws IOException {
        Scanner reader = new Scanner(System.in);
        /*if( System.getProperty( "os.name" ).startsWith( "Window" ) )
            Runtime.getRuntime().exec("cls");
        else
            Runtime.getRuntime().exec("clear");*/
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------Menu de Editor-----------\n"
                +"[1]Pesquisar música\n"
                +"[2]Gerir artistas\n"
                +"[3]Gerir álbuns\n"
                +"[4]Gerir músicas\n"
                +"[5]Dar privilégios de editor a um utilizador\n"
                +"[6]Consultar detalhes album\n"
                +"[7]Consultar detalhes artista\n"
                +"[8]Upload de música\n"
                +"[9]Download de música\n"
                +"[10]Partilhar uma música\n"
                +"[11]Logout");

        int opcao = reader.nextInt();
        int o;
        switch (opcao){
            case 1: //pesquisar musicas
                System.out.println("A procurar musicas");
                pesquisar_musicas(server_i);
                break;
            case 2: //gerir artistas
                System.out.printf("Pretende [1]adicionar, [2]editar ou [3]eliminar um artista");
                o = sc.nextInt();
                switch (o){
                    case 1:
                        inserir_artista(server_i);
                        break;
                    case 2:
                        break;
                    case 3:
                        elimina_artista(server_i);
                        break;
                    default:
                        System.out.println("Introduza uma opção válida!");
                        break;
                }

                break;
            case 3: ///gerir album
                System.out.printf("Pretende [1]adicionar, [2]editar ou [3]eliminar uma album");
                o = sc.nextInt();
                switch (o){
                    case 1:
                        inserir_album(server_i);
                        break;
                    case 2:
                        break;
                    case 3:
                        elimina_album(server_i);
                        break;
                    default:
                        System.out.println("Introduza uma opção válida!");
                        break;
                }
                break;
            case 4: //gerir musica
                System.out.printf("Pretende [1]adicionar, [2]editar ou [3]eliminar uma musica");
                o = sc.nextInt();
                switch (o){
                    case 1:
                        inserir_musica(server_i);
                        break;
                    case 2:
                        break;
                    case 3:
                        elimina_musica(server_i);
                        break;
                    default:
                        System.out.println("Introduza uma opção válida!");
                        break;
                }
                break;
            case 5: // privilégios de editor
                escolheUser();
                break;
            case 6: //detalhes do album
                consultar_album();
                break;
            case 7: //detalhe artista
                consultar_artista();
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            default:
                System.out.println("Insira uma opção válida!");
        }
    }
    private static void pesquisar_musicas(ServerRMI_I server_i){
        System.out.println("Dentro pesquisar musicas");
        ArrayList<Musica> lista_musica = null;
        try {
            System.out.println("Antes de receber as musicas");
            lista_musica = server_i.recebe_musicas(0, 5);
            System.out.println("Recebeu as musicas");
            if(lista_musica == null) {
                System.out.println("Nao existem musicas");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(lista_musica != null){
            for (Musica m: lista_musica) {
                System.out.println(m.getNome_musica() + " : " + m.getCompositor() + " : " + m.getDuracao());
            }
        }

    }
    private static void inserir_artista(ServerRMI_I s_i){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;
        try {
            System.out.println("Nome_Artista;Compositor(true/false);Informacao");
            frase = reader.readLine();
            mensagem = "6;"+frase;
            s_i.enviaStringAoMulticast(mensagem);
            System.out.println(s_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void inserir_album(ServerRMI_I s_i){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;
        try {
            System.out.println("Nome_Album;Descricao;Data_Lancamento(dd/mm/aaaa)");
            frase = reader.readLine();
            mensagem = "7;"+frase;
            s_i.enviaStringAoMulticast(mensagem);
            System.out.println(s_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void inserir_musica(ServerRMI_I s_i){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;
        try {
            System.out.println("Nome_Musica;Compositor;Duracao");
            frase = reader.readLine();
            mensagem = "5;"+frase;
            s_i.enviaStringAoMulticast(mensagem);
            System.out.println(s_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void elimina_musica(ServerRMI_I s_i){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;

        System.out.println("Qual o nome da música que pretende eliminar?");
        try{
            frase = reader.readLine();
            mensagem = "8;" +frase;

            s_i.enviaStringAoMulticast(mensagem);
            System.out.println(s_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void elimina_album(ServerRMI_I s_i){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;

        System.out.println("Qual o nome do album que pretende eliminar?");
        try{
            frase = reader.readLine();
            mensagem = "9;" +frase;
            s_i.enviaStringAoMulticast(mensagem);
            System.out.println(s_i.recebe_multicast_socket());
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    private static void elimina_artista(ServerRMI_I s_i){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;

        System.out.println("Qual o nome do artista que pretende eliminar?");
        try{
            frase = reader.readLine();
            mensagem = "10;" +frase;
            s_i.enviaStringAoMulticast(mensagem);
            System.out.println(s_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void consultar_album(){}
    private static void consultar_artista(){}

    private static void escolheUser() throws RemoteException {
        Utilizador user;
        Scanner scanner = new Scanner(System.in);
        String nome;
        String mensagem;
        int n =1;// numero chave que diz ao metodo envia que é para enviar o nome
        String location_s = "rmi://" + server_ip + ":" + server_port + "/"+ name;
        ServerRMI_I server_i = null;
        try {
            server_i = (ServerRMI_I) Naming.lookup(location_s);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        System.out.println("Qual o username do utilizador?");
        nome = scanner.nextLine();
        mensagem = "3;"+nome;
        assert server_i != null;
        server_i.enviaStringAoMulticast(mensagem);

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

    @Override
    public void change_edir(boolean b) throws RemoteException {
        cliente_corrente.setEditor(b);
    }

}