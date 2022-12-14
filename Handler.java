import java.net.*;
import java.io.*;
import java.util.*;

public class Handler extends Thread {
	Socket ligacao;
	Messages messages;
	BufferedReader in;
	PrintWriter out;
	
	public Handler(Socket ligacao, Messages messages) {
		this.ligacao = ligacao;
		this.messages = messages;
		
		try {
			this.in = new BufferedReader (new InputStreamReader(ligacao.getInputStream()));
			this.out = new PrintWriter(ligacao.getOutputStream());
		} catch(IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}	
	}
	
	public void run() {   
		String msg;
		try {
			
			System.out.println("Aceitou ligacao de cliente no endereco " + ligacao.getInetAddress() + " na porta " + ligacao.getPort() + "\n");

			while (true)
			{
			String response;
			msg = in.readLine();
			if (msg == null)
				break;
			System.out.println("Request=" + msg);

			StringTokenizer tokens = new StringTokenizer(msg, ";;");
			String metodo = tokens.nextToken();
			if (metodo.equals("SESSION_UPDATE_REQUEST")) {
				response = "SESSION_UPDATE\n\n";
				String users = tokens.nextToken();
				response += "UTILIZADORES: ";
				Vector<String> userList = messages.getUsers(users);
				for (Iterator<String> it = userList.iterator(); it.hasNext();){
					String next = it.next();
					response +="\n" + next + "; ";
				}
				
				response += "\n\nMENSAGENS: ";
				ArrayList<String> msgList = messages.getMsgs();
				for(Iterator<String> it = msgList.iterator(); it.hasNext();){
					String next = it.next();
					response += "\n" +  next;
				}
				response += "\nEND_OF_MESSAGE";
						
				System.out.println(response);
				out.println(response);
				out.flush();
				

			}else if(metodo.equals("AGENT_POST")){
				response ="AGENT_POST ";
				String mensagens = tokens.nextToken();
				messages.addMsg(mensagens);
				response += mensagens;
				System.out.println(response);
				out.println("Mensagem Enviada!");
				out.flush();


			}else{
				out.println("Method not found.");
			}
			}
			
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
	}
}