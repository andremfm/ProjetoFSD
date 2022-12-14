import java.util.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;

public class Messages extends UnicastRemoteObject implements PrivateMessaging{

	private static Hashtable<String, UserInfo> presentUsers = new Hashtable<String, UserInfo>();
    private ArrayList<String>ListaMensagens = new ArrayList<String>();
	String msg;
    
    public Messages () throws RemoteException {
    	super();
    }

	public Vector<String> getUsers(String Username) {
		
		long actualTime = new Date().getTime();
		
		synchronized(this) {
			if (presentUsers.containsKey(Username)) {
				UserInfo newUser = presentUsers.get(Username);
				newUser.setLastSeen(actualTime);
			}
			else {
				UserInfo newUser = new UserInfo(Username, actualTime);
				presentUsers.put(Username,newUser);
			}
		}
		return getUsersList();
	}
	
	//Obter users ativos
	private Vector<String> getUsersList(){
		Vector<String> result = new Vector<String>();
		for (Enumeration<UserInfo> e = presentUsers.elements(); e.hasMoreElements(); ) {
			UserInfo element = e.nextElement();
			if (!element.timeOutPassed(180*1000)) {
				result.add(element.getUser());
			}
		}
		return result;
	}
	
	//Lista das 10 ultimas mensagens
    public ArrayList<String> getMsgs(){
		ArrayList<String> ultimas = new ArrayList<String>();

		if(ListaMensagens.size()<=10){
			return ListaMensagens;
		}else{
		for (int i=0; i<10; i++) {
			String a = ListaMensagens.get(i);
			ultimas.add(a);
		}
        return ultimas;
		}
    }
    
    //Adicionar mensagem AGENT_POST
	public void addMsg(String mensagem){
		ListaMensagens.add(0, mensagem);
	}   
	//ENVIAR MENSAGEM DIRETA RMI
	public String sendMessage(String name, String message) throws RemoteException {
		msg = name + ": " + message;
		System.out.println(msg);
		return msg;

	}
	//ENVIAR MENSAGEM DIRETA ASSINADA
	public String sendMessageSecure (String name, String message, String signature, PublicKey chavepub) throws RemoteException {
		
		byte[] sig = Base64.getDecoder().decode(signature);

		  try{
		  //Creating the MessageDigest object  
	      MessageDigest md = MessageDigest.getInstance("SHA-256");

	      //Passing data to the created MessageDigest Object
	      md.update(message.getBytes());
	      
	      //Compute the message digest
	      byte[] digest = md.digest();   
	      
	      //Creating a Signature object
	      Signature sign = Signature.getInstance("SHA256withDSA");
	      
	      //Initializing the signature
	      sign.initVerify(chavepub);
	      sign.update(digest);
	      
	      //Verifying the signature
	      boolean bool = sign.verify(sig);
	      
	      String msgass = name + ": " +message;
	      if(bool) {
	    	  
	         System.out.println(msgass + " *Mensagem Verificada*");   
	      } else {
	         System.out.println(msgass + " *Mensagem N??o Verificada*");
	      }
		   }catch(NoSuchAlgorithmException | InvalidKeyException | SignatureException e){
			System.exit(1);
		  }
		  		  return message;
	   }
}

class UserInfo {	
	private String user;
	private long lastSeen;

	public UserInfo(String user, long lastSeen) {
		this.user = user;
		this.lastSeen = lastSeen;
	}

	public String getUser () {
		return this.user;
	}

	public void setLastSeen(long time){
		this.lastSeen = time;
	}

	public boolean timeOutPassed(int timeout){
		boolean result = false;
		long timePassedSinceLastSeen = new Date().getTime() - this.lastSeen;
		if (timePassedSinceLastSeen >= timeout)
			result = true;
		return result;
	}
}

