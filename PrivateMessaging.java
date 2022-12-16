import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrivateMessaging extends Remote
{
	public String sendMessage(String name, String message) throws RemoteException;
	public String sendMessageSecure(String name, String message, String signature) throws RemoteException;

}