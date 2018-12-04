/**
 * Raul Barbosa 2014-11-07
 */
package menu_inicial.RMIServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIServerInterface extends Remote {
	boolean userMatchesPassword(String user, String password) throws RemoteException;
	ArrayList<String> getAllUsers() throws RemoteException;
}
