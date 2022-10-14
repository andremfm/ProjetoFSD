import java.net.*;
import java.io.*;
import java.util.*;

public class Thread extends Thread {
	Socket ligacao;
	BufferedReader in;
	PrintWriter out;
	
	public GetPresencesRequestHandler(Socket ligacao) {
		this.ligacao = ligacao;
		
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
			
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
}