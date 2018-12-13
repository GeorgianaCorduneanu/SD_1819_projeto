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
/*Protocolo multicast
 1 - login
 2 - registo
 3 - dar permissoes
 4 - listar musicas
 5 - inserir uma musica
 6 - inserir um artista
 7 - inserir um album
 8 - eliminar uma musica
 9 - eliminar um album
 10 - eliminar artista
 11 - editar descricao
 12 - edirar data album
 13 - pesquisar uma musica
 14 - pesquisar album
 15 - pesquisar artista
 */
public class ClienteRMI extends UnicastRemoteObject implements ClienteRMI_I, Serializable {
    private static Utilizador cliente_corrente;
    private static  String server_ip = "localhost";
    private static int server_port = 7000;
    private static String name = "msg";
    private static String location_s = "rmi://" + server_ip + ":" + server_port + "/"+ name;
    private static ServerRMI_I server_i;


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
        String mensagem;

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
                            login_cliente(frase_chave_valor[0], frase_chave_valor[1]);
                            //}else
                            //System.out.printf("codigo: "+codigo);
                            break;
                        case 2://registar
                            cliente = registar_cliente(frase_chave_valor[0], frase_chave_valor[1]);
                            System.out.println("A enviar para servidor");
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
    private static void login_cliente(String username, String passe){
        ClienteRMI cliente = null;
        System.out.println("*** Login ***");
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
            else if(msg.equals("Erro ao fazer login")) {
                System.out.println(msg);
                return;
            }
            else
                cliente_corrente.setEditor(false);

            server_i.addClienteOnline(cliente);

            if (cliente_corrente.getEditor()) {
                menu_login_editor();
            }
            else {
                menu_login_normal();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("username: " + cliente.getUtilizador().getUsername() + " : " + cliente.getUtilizador().getPassword());
    }

    private static ClienteRMI registar_cliente(String username, String passe){
        ClienteRMI cliente = null;
        String mensagem;
        System.out.println("*** Registar ***");
        try {
            cliente = new ClienteRMI();
            cliente.cliente_corrente = new Utilizador(username, passe);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            mensagem = server_i.subscribe(location_s,cliente);
            if(mensagem.equals("Utilizador ja existente")) {
                System.out.println(mensagem);
            }
            else{
                server_i.addClienteOnline(cliente);
                if(cliente_corrente.getEditor()){
                    menu_login_editor();
                }
                else{
                    menu_login_normal();}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("username: " + cliente.getUtilizador().getUsername() + " : " + cliente.getUtilizador().getPassword());
        return cliente;
    }
    private static void menu_login_normal() throws IOException {
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
                   listar_musicas();
                   break;
               case 2: //consultar um album
                   break;
               case 3: //consultar artista
                   break;
               case 4: //upload de musica
                   break;
               case 5: //download de musica
                   break;
               case 6: //partilhar musica
                   break;
               case 7: //pesquisar musica
                   break;
               case 0:
                   System.out.println("Logout");
               default:
                   System.out.println("Insira uma opção válida!");
           }
       }while (opcao != 0);

    }
    private static void menu_login_editor() throws IOException {
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
                    + "[12]Listar utilizadores\n"
                    + "[0]Logout");

            opcao = reader.nextInt();
            int o, op;

            switch (opcao) {
                case 1: //listar as musicas
                    System.out.println("A listar musicas");
                    listar_musicas();
                    break;
                case 2: //gerir artistas
                    System.out.println("Pretende [1]adicionar, [2]editar ou [3]eliminar um artista [0]Sair");
                    o = sc.nextInt();
                    switch (o) {
                        case 1:
                            inserir_artista();
                            break;
                        case 2:
                            break;
                        case 3:
                            elimina_artista();
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("Introduza uma opção válida!");
                            break;
                    }

                    break;
                case 3: ///gerir album
                    System.out.println("Pretende [1]adicionar, [2]editar ou [3]eliminar uma album");
                    o = sc.nextInt();
                    switch (o) {
                        case 1:
                            inserir_album();
                            break;
                        case 2:
                            System.out.println("Pretende alterar a [1]descrição de um album ou [2]data de criação?\n");
                            op = sc.nextInt();
                            switch (op) {
                                case 1:
                                    editar_descricao_album(cliente_corrente.getUsername());
                                    break;
                                case 2:
                                    editar_data_album(cliente_corrente.getUsername());
                                    break;
                                default:
                                    System.out.println("Insira uma opção válida!");
                            }
                            break;
                        case 3:
                            elimina_album();
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
                            inserir_musica();
                            break;
                        case 2:
                            break;
                        case 3:
                            elimina_musica();
                            break;
                        default:
                            System.out.println("Introduza uma opção válida!");
                            break;
                    }
                    break;
                case 5: // privilégios de editor
                    escolheUser();
                    break;
                case 6: //detalhes do album numero 14
                    pesquisar(14);
                    break;
                case 7: //detalhe artista numero 15
                    pesquisar(15); /// a fazer
                    break;
                case 8: //upload de musica
                    break;
                case 9: //download de musica
                    break;
                case 10: //partilhar musica
                    break;
                case 11://pesquisar musica numero 13
                    pesquisar(13);
                    break;
                case 12:
                    Scanner opcao_lista = new Scanner(System.in);
                    listar(opcao_lista.nextInt());
                case 0:
                    System.out.println("Logout");
                    break;
                default:
                    System.out.println("Insira uma opção válida!");
            }
        }while (opcao != 0);
    }
    private static void listar(int tipo){
        ArrayList<String> lista_final=new ArrayList<>();
        String lista_recebe=null;
        String mensagem = "16;" + tipo;//
        try {
            server_i.enviaStringAoMulticast(mensagem);
            lista_recebe = server_i.recebe_multicast_socket();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(lista_recebe);
    }
    private static void pesquisar(int numero_protocolo){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String musica_pesquisar;
        String mensagem;
        System.out.println("Indique o nome a pesquisar: ");
        try {
            musica_pesquisar = reader.readLine();
            musica_pesquisar = musica_pesquisar.toLowerCase(); //passar todos os caracteres para minusculos
            mensagem = numero_protocolo + ";" + musica_pesquisar;
            server_i.enviaStringAoMulticast(mensagem);  //enviar pedido de detalhe de uma musica

            System.out.println(server_i.recebe_multicast_socket()); //receber info musica ou info erro
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void listar_musicas(){
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
    private static void inserir_artista(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;
        try {
            System.out.println("Nome_Artista;Compositor(true/false);Informacao");
            frase = reader.readLine();
            mensagem = "6;"+frase;
            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void inserir_album(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;
        try {
            System.out.println("Nome_Album;Descricao;Data_Lancamento(dd/mm/aaaa)");
            frase = reader.readLine();
            mensagem = "7;"+frase;
            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void inserir_musica(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;
        try {
            System.out.println("Nome_Musica;Compositor;Duracao");
            frase = reader.readLine();
            mensagem = "5;"+frase;
            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void editar_descricao_album(String username){
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
            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void editar_data_album(String username){
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
            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void elimina_musica(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;

        System.out.println("Qual o nome da música que pretende eliminar?");
        try{
            frase = reader.readLine();
            mensagem = "8;" +frase;

            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void elimina_album(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;

        System.out.println("Qual o nome do album que pretende eliminar?");
        try{
            frase = reader.readLine();
            mensagem = "9;" +frase;
            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    private static void elimina_artista(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String frase;
        String mensagem;

        System.out.println("Qual o nome do artista que pretende eliminar?");
        try{
            frase = reader.readLine();
            mensagem = "10;" +frase;
            server_i.enviaStringAoMulticast(mensagem);
            System.out.println(server_i.recebe_multicast_socket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void escolheUser() throws RemoteException {
        Utilizador user;
        Scanner scanner = new Scanner(System.in);
        String nome;
        String mensagem;
        int opcao;
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
        System.out.println("[0] Retirar privilegios [1] Dar privilegios [2] Sair");
        opcao = scanner.nextInt();
        if(opcao == 2)
            return;
        if(opcao == 1 || opcao == 0) {
            scanner.nextLine();
            System.out.println("Qual o username do utilizador?");
            nome = scanner.nextLine();
            mensagem = "3;" + nome + ";" + opcao;
        }
        else{
            System.out.println("Opcao errada");
            return;
        }
        assert server_i != null;
        server_i.enviaStringAoMulticast(mensagem);

        mensagem = server_i.recebe_multicast_socket();
        if(mensagem.equals("Feito")){
            System.out.println("Opcao para mensagem " + opcao);
            if(opcao == 1)
                server_i.notifica_cliente(nome, "NOTIFICACAO: Recebeu privilegios de editor de " + cliente_corrente.getUsername());
            else if(opcao == 0)
                server_i.notifica_cliente(nome, "NOTIFICACAO: Foram retirados os privilegios de editor de " + cliente_corrente.getUsername());
        }

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

    @Override
    public void notificacao(String mensagem) throws RemoteException {
        System.out.println(mensagem);
    }

}