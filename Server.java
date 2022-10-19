import java.net.*;
import java.io.*;

public class Server {
	static int DEFAULT_PORT=2000;
	
	public static void main(String[] args) throws Exception {
		int port=DEFAULT_PORT;
		Messages messages = new Messages();
		
		ServerSocket servidor = null;
		
		servidor = new ServerSocket(port);
		
		System.out.println("Servidor a' espera de ligacoes no porto " + port);
		
		while(true) {
			try {

				Socket ligacao = servidor.accept();


				Handler atendedor = new Handler(ligacao, messages);
				atendedor.start();

			} catch (IOException e) {
				System.out.println("Erro na execucao do servidor: "+e);
				System.exit(1);
			}
		}
	}
	
}