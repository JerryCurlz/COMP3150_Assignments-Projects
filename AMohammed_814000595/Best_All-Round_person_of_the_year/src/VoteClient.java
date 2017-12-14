
/*A.Mohammed
 * 814000595
 * The programs were tested on a single computer using "localhost"
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class VoteClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.print("\t\t****************************************\n"
				+"Greatings Voter Start Voting for your candidate by entering their name\n"
				+ "\t\t****************************************\n");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		
		//create the server host name
		String serverHostName = "localhost";
		
		//create the sever address 
		InetAddress serverIPAddress =null;
		  
		
		//create the port number
		int serverPortNumber = 5555;//same as the server
		
		//create the client socket that will be used to contact the UDP server
		DatagramSocket clientSocket = new DatagramSocket ();
				
		//find the IP address of the sever from its name 
		serverIPAddress = InetAddress.getByName(serverHostName);
		
		/** send a packet to the UDP server **/
		//System.out.print("Please enter the candidate name you wish to vote for: ");
	    //String message = inFromUser.readLine();
		
		ArrayList<String> candidates = new ArrayList <String>(25);//Automated testing of various invalid names and names with
																  //a combination of lower and uppercase letters as well as leading and trailing spaces. 
		candidates.add("Pe te R SingH");//1
		candidates.add("Expecto Pat TRONUS");//2
		candidates.add("Pe tra Singh");//3
		candidates.add("Linda Jenkins");//4
		candidates.add("Li	nda Je	nk	ins");//5
		candidates.add("WI n s t	o	n 	Al	i	bocas");//6
		candidates.add("Data Base");//7
		candidates.add("Drop the base");//8
		candidates.add("Ricardo Allen");//9
		candidates.add("R i car			d	o A	l	l	e	n");//10
		candidates.add("Whiney the poo");//11
		candidates.add("Odessy");//12
		candidates.add("Blind date");//13
		candidates.add("Ma	 rl	ene 	W	i	lli	a	ms");//14
		candidates.add("Win				ston Al iboc as");//15
		candidates.add("Alibo Wi n s t o n cas");//16
		candidates.add("Alibo Jenkins");//17
		candidates.add("Peter Singh");//18
		candidates.add("Peter Sin	g	h");//19
		candidates.add("P Singh eter");//20
		candidates.add("Peter Parker");//21
		candidates.add("Silver Server");//22
		candidates.add("Ricardo Allen");//23
		candidates.add("Ricardo AlLEn	");//24
		candidates.add("Marlene Willia	ms      				");//25
	
		int count=0, n;
		String response;
		Random rand = new Random();
		
		while(true) {
			n = rand.nextInt(24) + 0;
			String message = candidates.get(n);
		    //prepare the buffer that will be used to send the message 
		    byte[] sendData = new byte [message.length() * 8];
		    sendData = message.getBytes();
		    DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, serverIPAddress, serverPortNumber);
		    clientSocket.send(sendPacket);
		    
		    /** get response from server **/
		    byte[] receiveData = new byte[1024];
		    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);
		    response = new String(receivePacket.getData());
		    response.trim();
		    System.out.println("FROM SERVER: " +response );
		    
		    if(response.contains("Vote Successful...")){//count the valid entries to know when to end client 	
		    	count++;
		    	
		    }else
		    	if(response.contains("Your machine is barred from voting...")) {
		    		
			    	break;
		    	}
		    
		    if(count==25)//terminate 
		    	break;
		    
		}
		
	    clientSocket.close();
	    
	}
	
	static boolean isTrue(String eq) {
		String cheese = "Vote Successful...";
		for (int i = 0; i < cheese.length();i++){
			if (cheese.charAt(i) != eq.charAt(i)) return false;
		}
		return true;
	}

}
