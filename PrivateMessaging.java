import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrivateMessaging extends Remote
{
	public String sendMessage(String name, String message) throws RemoteException;
}