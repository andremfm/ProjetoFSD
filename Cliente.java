import java.util.*;
import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.security.*;

public class Cliente {
	
	static final int DEFAULT_PORT = 2000;
	static final String DEFAULT_HOST="127.0.0.1";
	
	public static void main(String[] args) throws Exception {
		//Verificar se insere os argumentos necessários
		if(args.length != 1){
			System.out.println("Tem de inserir: java Cliente <<Endereço do servidor>>");
			System.exit(-1);
		}


		String servidor = DEFAULT_HOST;
		int porto = DEFAULT_PORT;
		int mnu;
		String SERVICE_NAME="/PrivateMessaging";
		int porta;
		String username2;
		String ip;
		PrivateKey privKey;
		PublicKey chavepub;

		InetAddress serverAddress = InetAddress.getByName(args[0]);

		Socket ligacao = null;
		ligacao = new Socket(serverAddress, porto);

		Scanner input = new Scanner(System.in);  
    	System.out.print("Username: ");
		String userName = input.nextLine();
		System.out.print("\n");
		System.out.println("Suporte RMI?(S/N): ");
		String rmi = input.nextLine();

		//Creating KeyPair generator object
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
		      
		//Initializing the key pair generator
		keyPairGen.initialize(2048);
		      
		//Generate the pair of keys
		KeyPair pair = keyPairGen.generateKeyPair();
		      
		//Getting the private key from the key pair
		privKey = pair.getPrivate();
		      
		//Get PublicKey
		chavepub = pair.getPublic();
		byte[] byteChavePub = chavepub.getEncoded();
		      
		//Veridicar se o Cliente suporta RMI
		if(rmi.equals("S") || rmi.equals("s")){
			System.out.println("IP: ");
			ip = input.nextLine();
			System.out.println("Porta: ");
			porta = input.nextInt();
			MessagesServer ms = new MessagesServer();
			ms.createMessages(porta, ip);
			username2 = userName + " | RMI: Sim" + " | IP: "+ ip + " | Porta: " +  porta + "| Chave Publica: " + byteChavePub;
			
		}else{
			username2 = userName + " | RMI: Não";
		}
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(ligacao.getInputStream()));
			
			PrintWriter out = new PrintWriter(ligacao.getOutputStream(), true);
			String request = "SESSION_UPDATE_REQUEST" + ";;" + username2;
			
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
				//Criar menu com as várias opções
				do{
				Scanner menu = new Scanner(System.in);
				System.out.print("\n****** MENU ******");
				System.out.print("\nEscolha um numero:");
				System.out.print("\n1 - Enviar mensagem; 2 - Refresh; 3 - Enviar Mensagem Privada; 4 - Enviar Mensagem Assinada; 0 - Fechar: \n");
				mnu = menu.nextInt();
				
				switch (mnu){
					//AGENT POST
					case 1:
						Scanner inputNewMsg = new Scanner(System.in);  
						System.out.print("Mensagem: ");
						String NewMsg = inputNewMsg.nextLine();
						String request1 = "AGENT_POST" + ";;" + NewMsg;
						out.println(request1);
						System.out.println(in.readLine());
						out.flush();
						break;
					//SESSION UPDATE REQUEST
					case 2:
						String request2 = "SESSION_UPDATE_REQUEST" + ";;" + username2;
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
					//MENSAGEM PRIVADA RMI	
					case 3:
						Scanner inputcase3 = new Scanner(System.in); 
						System.out.println("IP de destino: ");
						String ipdest = inputcase3.nextLine();
						System.out.println("Porta de destino: ");
						int portadest = inputcase3.nextInt(); 
						Scanner inputcase3msg = new Scanner(System.in);
						System.out.println("Mensagem: ");
						String msgpriv = inputcase3msg.nextLine();
						PrivateMessaging pm = (PrivateMessaging) LocateRegistry.getRegistry(ipdest, portadest).lookup(SERVICE_NAME);
						pm.sendMessage(userName, msgpriv);
						break;
					//MENSAGEM PRIVADA RMI ASSINADA
					case 4:
						Scanner inputcase4 = new Scanner(System.in); 
						System.out.println("IP de destino: ");
						String ipdest1 = inputcase4.nextLine();
						System.out.println("Porta de destino: ");
						int portadest1 = inputcase4.nextInt(); 
						Scanner input1 = new Scanner(System.in);
						System.out.println("Mensagem: ");
						String msgass = input1.nextLine();
						
						//Creating the MessageDigest object  
					    MessageDigest md = MessageDigest.getInstance("SHA-256");
					      
					    //Passing data to the created MessageDigest Object
					    md.update(msgass.getBytes());
					      
					    //Compute the message digest
					    byte[] digest = md.digest();
					      
					    //Creating a Signature object
					    Signature sign = Signature.getInstance("SHA256withDSA");
					      
					    //Initialize the signature
					    sign.initSign(privKey);
					      
					    //Adding digest to the signature
					    sign.update(digest);
					      
					    //Calculating the signature
					    byte[] signature = sign.sign();

						String sig = Base64.getEncoder().encodeToString(signature);
					      
					    PrivateMessaging pms = (PrivateMessaging) LocateRegistry.getRegistry(ipdest1, portadest1).lookup(SERVICE_NAME);
					    pms.sendMessageSecure(userName, msgass, sig, chavepub);
						
						break;
					//FECHAR SISTEMA
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
