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
    private static String location_s = "rmi://" + server_ip + ":" + server_port + "/"+ name;


    private ClienteRMI() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws IOException {
        //definir plicy
        //System.getProperties().put("java.security.policy", "file:\\C:\\Users\\gonca\\Desktop\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        System.getProperties().put("java.security.policy", "file:\\C:\\Users\\ginjo\\Documents\\SD_1819_projeto\\SD_1819_projeto_versao01\\src\\policy.all");
        System.setProperty("java.rmi.server.hostname","192.168.56.1");
        System.setSecurityManager(new RMISecurityManager());
        menu_inicial();
    }
    private static void menu_inicial() throws MalformedURLException {
        Scanner sc = new Scanner(System.in);
        String frase;
        String[] frase_chave_valor;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        //definir clienteRMI e interfaceservidor RMI
        ClienteRMI cliente;
        ServerRMI_I server_i = null;
        int opcao=0;


        do {
            try {
                server_i = (ServerRMI_I) Naming.lookup(location_s);
                System.out.println("-----------Menu Inicial-----------\n"
                        + "[1]Login\n"
                        + "[2]Registar\n"
                        + "[3]Sair\n");

                opcao = sc.nextInt();
                System.out.println("Nome Passe");
                try {
                    frase = reader.readLine();
                    frase_chave_valor = frase.split(" ");
                    switch (opcao) {
                        case 1: //login
                            login_cliente(frase_chave_valor[0], frase_chave_valor[1], server_i);
                            //}else
                            //System.out.printf("codigo: "+codigo);
                            break;
                        case 2://registar
                            try {
                                cliente = registar_cliente(frase_chave_valor[1], frase_chave_valor[2]);
                                System.out.println("A enviar para servidor");
                                server_i.subscribe(location_s,cliente);
                                //  System.out.println(server_i.recebe_multicast_socket()); //receber mensagem de multicast ou nao
                                // System.out.println("Devia ter enviado mensagem");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }

        }while(opcao != 0);
    }
    private static void login_cliente(String username, String passe, ServerRMI_I server_i){
        ClienteRMI cliente = null;
        System.out.println("*** Registar ***");
        String msg;
        String [] msg_split;
        try {
            cliente = new ClienteRMI();
            cliente.cliente_corrente = new Utilizador(username, passe);
            msg = server_i.login(location_s, cliente);
            System.out.println("A enviar para servidor: " + msg);
            msg_split = msg.split(";");
            if(msg_split[msg_split.length-1].equals("true"))
                cliente_corrente.setEditor(true);
            else
                cliente_corrente.setEditor(false);

            if (cliente_corrente.getEditor())
                menu_login_editor(server_i);
            else
                menu_login_normal(server_i);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("username: " + cliente.getUtilizador().getUsername() + " : " + cliente.getUtilizador().getPassword());
    }

    private static ClienteRMI registar_cliente(String username, String passe){
        ClienteRMI cliente = null;
        System.out.println("*** Registar ***");
        try {
            cliente = new ClienteRMI();
            cliente.cliente_corrente = new Utilizador(username, passe);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //System.out.println("username: " + cliente.getUtilizador().getUsername() + " : " + cliente.getUtilizador().getPassword());
        return cliente;
    }
    private static void menu_login_normal(ServerRMI_I server_i) throws IOException {
        Scanner reader = new Scanner(System.in);
       /* if( System.getProperty( "os.name" ).startsWith( "Window" ) )
            Runtime.getRuntime().exec("cls");
        else
            Runtime.getRuntime().exec("clear");
*/
       int opcao;
       do {
           System.out.println("-----------Menu-----------\n"
                   + "[1]Listar músicas\n"
                   + "[2]Consultar detalhes album\n"
                   + "[3]Consultar detalhes artista\n"
                   + "[4]Upload de música\n"
                   + "[5]Download de música\n"
                   + "[6]Partilhar uma música\n"
                   + "[7]Pesquisar uma musica\n"
                   + "[0]Logout");

           opcao = reader.nextInt();
           switch (opcao) {
               case 1:
                   break;
               case 2: //consultar um album
                   consultar_album();
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
               case 0:
                   System.out.println("Logout");
               default:
                   System.out.println("Insira uma opção válida!");
           }
       }while (opcao != 0);

    }
    private static void menu_login_editor(ServerRMI_I server_i) throws IOException {
        Scanner reader = new Scanner(System.in);
        /*if( System.getProperty( "os.name" ).startsWith( "Window" ) )
            Runtime.getRuntime().exec("cls");
        else
            Runtime.getRuntime().exec("clear");*/
        int opcao;
        Scanner sc = new Scanner(System.in);
        do {//manter o login aberto a nao ser que peça para sair
            System.out.println("-----------Menu de Editor-----------\n"
                    + "[1]Listar música\n"
                    + "[2]Gerir artistas\n"
                    + "[3]Gerir álbuns\n"
                    + "[4]Gerir músicas\n"
                    + "[5]Dar privilégios de editor a um utilizador\n"
                    + "[6]Consultar detalhes album\n"
                    + "[7]Consultar detalhes artista\n"
                    + "[8]Upload de música\n"
                    + "[9]Download de música\n"
                    + "[10]Partilhar uma música\n"
                    + "[11]Pesquisar uma musica\n"
                    + "[0]Logout");

            opcao = reader.nextInt();
            int o, op;

            switch (opcao) {
                case 1: //pesquisar musicas
                    System.out.println("A procurar musicas");
                    pesquisar_musicas(server_i);
                    break;
                case 2: //gerir artistas
                    System.out.printf("Pretende [1]adicionar, [2]editar ou [3]eliminar um artista");
                    o = sc.nextInt();
                    switch (o) {
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
                    switch (o) {
                        case 1:
                            inserir_album(server_i);
                            break;
                        case 2:
                            System.out.println("Pretende alterar a [1]descrição de um album ou [2]data de criação?\n");
                            op = sc.nextInt();
                            switch (op) {
                                case 1:
                                    editar_descricao_album(server_i, cliente_corrente.getUsername());
                                    break;
                                case 2:
                                    editar_data_album(server_i, cliente_corrente.getUsername());
                                    break;
                                default:
                                    System.out.printf("Insira uma opção válida!");
                            }

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
                    switch (o) {
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
                case 0:
                    System.out.println("Logout");
                    break;
                default:
                    System.out.println("Insira uma opção válida!");
            }
        }while (opcao != 0);
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
    private static void editar_descricao_album(ServerRMI_I s_i, String username){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase,frase2;
        String mensagem;

        System.out.println("Qual o nome do album que pretende editar? ");
        try{
            frase = reader.readLine();
            mensagem = "11;"+frase;
            System.out.println("Qual a nova descrição do album?");
            frase2= reader.readLine();
            mensagem = mensagem+";" + frase2;
            mensagem = mensagem+";" + username;
            s_i.enviaStringAoMulticast(mensagem);
            System.out.println(s_i.recebe_multicast_socket());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void editar_data_album(ServerRMI_I s_i, String username){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase,frase2;
        String mensagem;

        System.out.println("Qual o nome do album que pretende editar? ");
        try{
            frase = reader.readLine();
            mensagem = "12;"+frase;
            System.out.println("Qual a nova data do album?");
            frase2= reader.readLine();
            mensagem = mensagem+";" + frase2;
            mensagem = mensagem + ";" + username;
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