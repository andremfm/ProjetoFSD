import java.util.*;
import java.net.*;
import java.io.*;

public class Cliente {
	
	static final int DEFAULT_PORT = 2000;
	static final String DEFAULT_HOST="127.0.0.1"; 
	
	public static void main(String[] args) throws Exception {
		String servidor = DEFAULT_HOST;
		int porto = DEFAULT_PORT;
		int mnu;

		InetAddress serverAddress = InetAddress.getByName(servidor);

		Socket ligacao = null;

		ligacao = new Socket(serverAddress, porto);

		Scanner input = new Scanner(System.in);  
    	System.out.print("Username: ");
		String userName = input.nextLine();

		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(ligacao.getInputStream()));
			
			PrintWriter out = new PrintWriter(ligacao.getOutputStream(), true);
			
			String request = "SESSION_UPDATE_REQUEST" + ";;" + userName;
			
			out.println(request);
			out.flush();
			String msg = "";
			
			while(msg != null) {
				msg = in.readLine();
				if(msg != null)
					System.out.println(msg);
					
			}

			do{
				ligacao = new Socket(serverAddress, porto);

				in = new BufferedReader(new InputStreamReader(ligacao.getInputStream()));
			
				out = new PrintWriter(ligacao.getOutputStream(), true);

				Scanner menu = new Scanner(System.in);
				System.out.print("1 - Enviar mensagem; 2 - Refresh; 0 - Fechar: ");
				mnu = menu.nextInt();

				switch (mnu){
					case 1:
						Scanner input2 = new Scanner(System.in);  
    					System.out.print("Mensagem: ");
						String mensg = input2.nextLine();
						String request1 = "AGENT_POST" + ";;" + mensg;
						out.println(request1);
						out.flush();
						System.out.println("Mensagem enviada.");
					break;
					case 2:
						String request2 = "SESSION_UPDATE_REQUEST" + ";;" + userName;
						out.println(request2);
						out.flush();
						System.out.println(request2);
						String msg2 = "";

						while(msg2 != null) {
							msg2 = in.readLine();
							if(msg2 != null)
								System.out.println(msg2);
						}
						break;
					case 0:
						in.close();
						out.close();
						ligacao.close();
						break;
					default: System.out.println("ERRO!!!");
				}
			}while(mnu != 0);
			in.close();
			out.close();
			ligacao.close();
			System.out.println("\nTerminou a ligacao!");
		} catch (IOException e) {
			System.out.println("Erro ao comunicar com o servidor: "+e);
			System.exit(1);
		}
		
		}
		
	}
