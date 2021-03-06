package menu_inicial.RMIServerInterface;
//package rmiserver;

import Classes_para_RMI.Album;
import Classes_para_RMI.Artista;
import Classes_para_RMI.Musica;
import Classes_para_RMI.Utilizador;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
	private static final long serialVersionUID = 20141107L;
	//private HashMap<Utilizador, String> users;
	//private Utilizador lista_utilizadores;
	private HashMap<String, String> users;
	private static String MULTICAST_ADDRESS = "224.3.2.1";
	private int PORT = 5000;


	public RMIServer() throws RemoteException {
		super();
		users = new HashMap<String, String>();
		users.put("bender", "rodriguez"); // static users and passwords, to simplify the example
		users.put("fry",    "philip");
		users.put("leela",  "turanga");
		users.put("homer",  "simpson");
		users.put("gi",     "gi");
	}
	//retorna verdadeira quando a pass esta certa para o username
	@Override
	public boolean userMatchesPassword(String user, String password) throws RemoteException {
		System.out.println("Looking up " + user + "...");
		return users.containsKey(user) && users.get(user).equals(password);
	}

	//retorna todos os utilizadores que estiveram online
	@Override
	public void inserirNotificacao(String user, String notificacao){
		String mensagem = "23;" + user + ";" + notificacao;
		try {
			System.out.println("Mensagem para inserir notificacao:" + mensagem);
			enviaStringAoMulticast(mensagem);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

    @Override
    public String getListaPessoasCriticaram(String nomeAlbum) throws RemoteException {
        String mensagem = "27;" + nomeAlbum;
        enviaStringAoMulticast(mensagem);
        mensagem = recebe_multicast_socket();
        return mensagem;
    }

    @Override
    public String getPontuacaoMedia(String nomeAlbum) throws RemoteException {
        String mensagem = "28;" + nomeAlbum;

        enviaStringAoMulticast(mensagem);
        mensagem = recebe_multicast_socket();
        return mensagem;
    }

    @Override
    public String criticarAlbum(String nomeAlbum, String critica, String pontuacao, String username) throws RemoteException {
        String mensagem = "26;" + nomeAlbum + ";" + critica + ";" + pontuacao + ";" + username;
	    enviaStringAoMulticast(mensagem);
	    mensagem = recebe_multicast_socket();
	    return mensagem;
    }

    @Override
	public void eliminarNOtificacao(String user){
		String mensagem = "24;" + user;
		try {
			enviaStringAoMulticast(mensagem);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String verNotificacao(String user){
		String mensagem = "25;" + user;
		try {
			enviaStringAoMulticast(mensagem);
			mensagem = recebe_multicast_socket();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return mensagem;
	}

	@Override
	public String subscribe(String nome, String password) throws RemoteException {
		boolean check = true;
		MulticastSocket socket = null;

		//mandar info a multicast buscar base de dados e adicionar registo
		try {
			socket = new MulticastSocket();
			String message = "1;" + nome + ";" + password;
			byte[] buffer = message.getBytes();
			// create socket without binding it (only for sending)

			socket.setLoopbackMode(true);//true quando envia
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
			socket.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Socket fechou");
			assert socket != null;
			socket.close();
		}

		//c_i.check_registar(check);
		//recebe a: Utilizador nao existente
		//recebe b: Utilizador ja existente
		//recebe c: Primeiro utilizador
		return recebe_multicast_socket();
	}

	@Override
	public String login(String nome, String passowrd) throws RemoteException {
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket();
			// create socket without binding it (only for sending)
			String message = "2;" + nome + ";" + passowrd;
			byte[] buffer = message.getBytes();

			socket.setLoopbackMode(true);//true quando envia
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			assert socket != null;
			socket.close();
		}
		//recebe a: Login bem sucedido ;true|false
		//recebe b: Erro ao fazer login
		String mensagem=recebe_multicast_socket();
        System.out.println(mensagem);
		return mensagem;
	}

	@Override
	public void enviaStringAoMulticast(String message) throws RemoteException {
		byte[] buffer;
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket();
			buffer = message.getBytes();
			socket.setLoopbackMode(true);//true quando envia
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
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
	public String recebe_multicast_socket() throws RemoteException {
        MulticastSocket socket = null;
        // while(true) {
        try {
            socket = new MulticastSocket(PORT);
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
	public void addClienteOnline(String nome, String password) throws RemoteException {
		if(users.isEmpty()){
			users.put(nome, password);
			return;
		}
		if(users.containsKey(nome))
			return;
		users.put(nome, password);
	}

    @Override
    public String gerirPrivilegios(String nome, int tipo) throws RemoteException {
        //switch 3 de multicast
        //opcao 1 dar privilegios
        //opcao 0 retirar privilegios
        String mensagem = "3;" + nome + ";" + tipo;
        try {
            enviaStringAoMulticast(mensagem);
            mensagem = recebe_multicast_socket();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return mensagem;
    }

    public static void main(String[] args) throws RemoteException {
		RMIServerInterface s = new RMIServer();
		LocateRegistry.createRegistry(7000).rebind("server", s);
		System.out.println("Server ready...");
	}
}
