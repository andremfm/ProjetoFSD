import java.net.*;
import java.io.*;
import java.util.*;

public class Thread extends Thread {
	Socket ligacao;
	Messages messages;
	BufferedReader in;
	PrintWriter out;
	
	public GetPresencesRequestHandler(Socket ligacao, Messages messages) {
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
			
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
}