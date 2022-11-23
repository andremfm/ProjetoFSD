import java.lang.SecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MessagesServer {
	
	String SERVICE_NAME="/PrivateMessaging";
	int porta;

	private void bindRMI(Messages messages) throws RemoteException {

		System.getProperties().put( "java.security.policy", "./server.policy");

		if( System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try { 
			LocateRegistry.createRegistry(porta);
		} catch( RemoteException e) {
			
		}
		try {
		  LocateRegistry.getRegistry(porta).rebind(SERVICE_NAME, messages);
		  } catch( RemoteException e) {
		  	System.out.println("Registry not found");
		  }
	}

	public MessagesServer() {
		super();
	}
	
	public void createMessages(int porta) {

		this.porta = porta;
		
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