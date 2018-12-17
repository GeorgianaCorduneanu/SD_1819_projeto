/**
 * Raul Barbosa 2014-11-07
 */
package menu_inicial.RMIServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface RMIServerInterface extends Remote {
	boolean userMatchesPassword(String user, String password) throws RemoteException;
	String subscribe(String nome, String password) throws RemoteException;
	String login(String nome, String passowrd) throws RemoteException;
	void enviaStringAoMulticast(String message) throws RemoteException;
	String recebe_multicast_socket() throws RemoteException;
	void addClienteOnline(String nome, String password) throws RemoteException;
   // List<String> listar(int tipo);
    String gerirPrivilegios(String nome, int tipo) throws RemoteException;
	void eliminarNOtificacao(String user) throws RemoteException;
	String verNotificacao(String user) throws RemoteException;
	void inserirNotificacao(String user, String notificacao) throws RemoteException;
		//String pesquisar(int tipo, String nome) throws RemoteException;
}
