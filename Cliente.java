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
		System.out.print("\n");
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(ligacao.getInputStream()));
			
			PrintWriter out = new PrintWriter(ligacao.getOutputStream(), true);
			
			String request = "SESSION_UPDATE_REQUEST" + ";;" + userName;
			
			System.out.println("----------------------------------------------------");

			out.println(request);
			out.flush();
			
			String msg = "";
			
			while(!msg.equals("END_OF_MESSAGE")){
				msg = in.readLine();
				if(!msg.equals("END_OF_MESSAGE"))
					System.out.println(msg);
			}

			System.out.println("----------------------------------------------------");

				do{
				Scanner menu = new Scanner(System.in);
				System.out.print("\n****** MENU ******");
				System.out.print("\nEscolha um numero:");
				System.out.print("\n1 - Enviar mensagem; 2 - Refresh; 0 - Fechar: ");
				mnu = menu.nextInt();

				switch (mnu){
					case 1:
					Scanner inputNewMsg = new Scanner(System.in);  
    				System.out.print("Mensagem: ");
					String NewMsg = inputNewMsg.nextLine();
					String request1 = "AGENT_POST" + ";;" + NewMsg;
					out.println(request1);
					//System.out.println("\nMensagem enviada.\n");
					System.out.println(in.readLine());
					out.flush();
					break;
					case 2:
						String request2 = "SESSION_UPDATE_REQUEST" + ";;" + userName;
						out.println(request2);

						System.out.println("----------------------------------------------------");

						String msgRefresh = "";
						while(!msgRefresh.equals("END_OF_MESSAGE")) {
							msgRefresh = in.readLine();
							if(!msgRefresh.equals("END_OF_MESSAGE"))
								System.out.println(msgRefresh);
						}

						System.out.println("----------------------------------------------------");

						out.flush();
						break;
					case 0:
						out.flush();
						in.close();
						out.close();
						ligacao.close();
						break;
					default: System.out.println("ERRO!!!");
				}
			}while(mnu != 0);
			ligacao.close();
			System.out.println("\nTerminou a ligacao!");
		} catch (IOException e) {
			System.out.println("Erro ao comunicar com o servidor: "+e);
			System.exit(1);
		}
		
		}
		
	}
