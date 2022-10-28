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
			
			System.out.println("Aceitou ligacao de cliente no endereco " + ligacao.getInetAddress() + " na porta " + ligacao.getPort());

			while (true)
			{
			String response;
			msg = in.readLine();
			System.out.println("Request=" + msg);

			StringTokenizer tokens = new StringTokenizer(msg, ";;");
			String metodo = tokens.nextToken();
			System.out.println("aqui2");
			if (metodo.equals("SESSION_UPDATE_REQUEST")) {
				response = "SESSION_UPDATE\n\n";
				String users = tokens.nextToken();
				response += "Utilizadores: ";
				Vector<String> userList = messages.getUsers(users);
				for (Iterator<String> it = userList.iterator(); it.hasNext();){
					String next = it.next();
					response += next + ";";
				}
				
				response += "\n\nMensagens: ";
				ArrayList<String> msgList = messages.getMsgs();
				for(Iterator<String> it = msgList.iterator(); it.hasNext();){
					String next = it.next();
					response += "\n" +  next;
				}
				response += "\nEND_OF_MESSAGE";
						
				System.out.println(response);
				out.println(response);
				out.flush();
				System.out.println("aqui3");
				

			}else if(metodo.equals("AGENT_POST")){
				response ="AGENT_POST";
				String mensagens = tokens.nextToken();
				messages.addMsg(mensagens);
				response += mensagens;
				System.out.println(response);


			}else{
				out.println("Method not found.");
			}
			System.out.println("aqui");
			}
			/*out.flush();
			in.close();
			out.close();
			ligacao.close();*/
			
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
	}
}