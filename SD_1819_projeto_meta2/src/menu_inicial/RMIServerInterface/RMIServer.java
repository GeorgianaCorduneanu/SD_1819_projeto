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
	public boolean userMatchesPassword(String user, String password) throws RemoteException {
		System.out.println("Looking up " + user + "...");
		return users.containsKey(user) && users.get(user).equals(password);
	}

	//retorna todos os utilizadores que estiveram online
	public ArrayList<String> getAllUsers() {
		System.out.println("Looking up all users...");
		return new ArrayList<String>(users.keySet());	
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
		//recebe a: Utilizador Encontrado ; true|false
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

	public static void main(String[] args) throws RemoteException {
		RMIServerInterface s = new RMIServer();
		LocateRegistry.createRegistry(7000).rebind("server", s);
		System.out.println("Server ready...");
	}
}
