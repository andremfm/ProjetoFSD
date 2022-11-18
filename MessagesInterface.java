import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;


public interface MessagesInterface extends Remote {

	public Vector<String> getMessages(String nome, PrivateMessaging pm) throws RemoteException;

}