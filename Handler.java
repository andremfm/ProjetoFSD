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
		try {
			System.out.println("Aceitou ligacao de cliente no endereco " + ligacao.getInetAddress() + " na porta " + ligacao.getPort());

			String response;
			String msg = in.readLine();
			System.out.println("Request=" + msg);

			StringTokenizer tokens = new StringTokenizer(msg);
			String metodo = tokens.nextToken();

			if (metodo.equals("SESSION_UPDATE_REQUEST")) {
				response = "SESSION_UPDATE\n";
				String users = tokens.nextToken();

				//Criar metodo getUsers
				Vector<String> userList = messages.getUsers;
				for (Iterator<String> it = userList.iterator(); it.hasNext();){
					String next = it.next();
					response += next + ";";
				}
				System.out.println(response);
				out.println(response);
				
				String mensagens = tokens.nextToken();

				//criar metodo getMsgs
				Vector<String> msgList = messages.getMsgs;
				for(Iterator<String> it = msgList.iterator(); it.hasNext();){
					String next = it.next();
					response += next + "\n";
				}
				System.out.println(response);
				out.println(response);

			}else
				out.println("Method not found.");

			out.flush();
			in.close();
			out.close();
			ligacao.close();
			
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
}
}