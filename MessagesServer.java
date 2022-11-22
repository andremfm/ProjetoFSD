import java.lang.SecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MessagesServer {
	
	String SERVICE_NAME="/PrivateMessaging";

	private void bindRMI(Messages messages) throws RemoteException {
		
		System.getProperties().put( "java.security.policy", "./server.policy");

		if( System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try { 
			LocateRegistry.createRegistry(1099);
		} catch( RemoteException e) {
			
		}
		try {
		  LocateRegistry.getRegistry("127.0.0.1",1099).rebind(SERVICE_NAME, messages);
		  } catch( RemoteException e) {
		  	System.out.println("Registry not found");
		  }
	}

	public MessagesServer() {
		super();
	}
	
	public void createMessages() {
		
		Messages messages = null;
		try {
			messages = new Messages();
		} catch (RemoteException e1) {
			System.err.println("unexpected error...");
			e1.printStackTrace();
		}
		
		try {
			bindRMI(messages);
		} catch (RemoteException e1) {
			System.err.println("erro ao registar o stub...");
			e1.printStackTrace();
		}
		
	}
}