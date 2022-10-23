import java.util.*;
import java.net.*;
import java.io.*;

public class Cliente {
	
	static final int DEFAULT_PORT = 2000;
	static final String DEFAULT_HOST="127.0.0.1"; 
	
	public static void main(String[] args) throws Exception {
		String servidor = DEFAULT_HOST;
		int porto = DEFAULT_PORT;
		
		/*if (args.length != 1) {
			System.out.println("Erro: use java presencesClient <ip>");
			System.exit(-1);
		}
		
		InetAddress serverAddress = null;
		
		try {
			serverAddress = InetAddress.getByName(args[0}]);
		} catch (UnknownHostException u) {
			System.out.println ("Erro");
		}
		
		Socket ligacao = null;
		
		try {
			ligacao = new Socket (serverAddress, args[1]);
		} catch (IOException e) {
			System.out.println ("Erro");
		}*/

		InetAddress serverAddress = InetAddress.getByName(servidor);

		Socket ligacao = null;

		ligacao = new Socket(serverAddress, porto);

		Scanner input = new Scanner(System.in);  
    	System.out.println("Username: ");
		String userName = input.nextLine();

		Scanner input2 = new Scanner(System.in);  
    	System.out.println("Mensagem: ");
		String mensg = input2.nextLine();
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(ligacao.getInputStream()));
			
			PrintWriter out = new PrintWriter(ligacao.getOutputStream(), true);
			
			String request = "SESSION_UPDATE_REQUEST" + ";;" + userName + ";;" + mensg;
			
			out.println(request);
			
			String msg = "";
			
			while(msg != null) {
				msg = in.readLine();
				if(msg != null)
					System.out.println(msg);
					
			}
			
			ligacao.close();
			System.out.println("Terminou a ligacao!");
		} catch (IOException e) {
			System.out.println("Erro ao comunicar com o servidor: "+e);
			System.exit(1);
		}
		
		}
		
	}
