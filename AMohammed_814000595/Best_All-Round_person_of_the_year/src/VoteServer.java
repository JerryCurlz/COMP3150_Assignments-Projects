
/*A.Mohammed
 * 814000595
 * The programs were tested on a single computer using "localhost"
 */



import java.util.ArrayList;
import java.util.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class VoteServer {
	

	public static void main(String[] args) throws IOException {
		System.out.print("\t\t*****************\n"
				+"Voting fot the Best all-round person of the year here!!!!! \n"
				+ "\t\t*****************\n\n");
		//declare the UDP server socket
		DatagramSocket serverSocket = null;
		int port;
		port =5555;
		//create the UDP server with a specific port number
		serverSocket = new DatagramSocket(port);
		
		int IVC;//Invalid Vote Counter
		int VVC;//Valid Vote Counter
		Candidate c1 = new Candidate ("Peter Singh");
		Candidate c2 = new Candidate ("Ricardo Allen");
		Candidate c3 = new Candidate ("Winston Alibocas");
		Candidate c4 = new Candidate ("Linda Jenkins");
		Candidate c5 = new Candidate ("Marlene Williams");
		
		ArrayList <Candidate> candidates = new ArrayList <Candidate>();
		candidates.add(c1);
		candidates.add(c2);
		candidates.add(c3);
		candidates.add(c4);
		candidates.add(c5);
		
		//prepare a packet structure for received packets
		int datalength = 1024; //Must be large enough otherwise message will be truncated
		byte[] receiveData = new byte [datalength];//buffer or array of bytes
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		//Scanner input = new Scanner(System.in);
		//System.out.println("Enter the name of the candidate you wish to vote for: ");
		//String name = input.nextLine();
		
		IVC=0; 
		VVC=0;
		while(true){ 
			//wait for incoming packets
			serverSocket.receive(receivePacket);
			//Extract the received packet data
			InetAddress IPAddress = receivePacket.getAddress();
			int clientPort = receivePacket.getPort();
			String name = new String(receivePacket.getData());
			
			//we must trim the message to remove the empty bytes. Observe the output
			name=name.trim();
			name= name.replace(" ","");//removes all excess space 
			name= name.replace("\t","");//removes all tab-ed spaces
			
			//send response back to client host
			byte[] sendData = new byte [datalength];
			String responseMessage;
			
			/*
			 * Function code to show how a 
			 * hacker computer can be prevented from voting.
			 */
			
			//*REMOVE THIS ***************************************************************
			
			String IP = IPAddress.toString();//Replace IPAddress.toString(); 
			//with the IP address you wish to actually block
			//used above just to show how it would work
			if(IP.equals(receivePacket.getAddress().toString())) {
				
				//Need to verify that the message 
				//came from the client you wish to block
				responseMessage="Your machine is barred from voting...";
				sendData = responseMessage.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
				serverSocket.send(sendPacket);
				System.out.println("HACKER WITH IP "+IP+" HAS BEEN BARRED");
				continue;//skips everything else below and awaits a new connection
				
			}
			///*///remove this comment to test the client blocker function*******************
			
			Candidate verify = LookUp(name, candidates);
			
			if(verify == null) {
				IVC++;
				responseMessage="Invalid Candidtate...";
			}else {
				verify.count++;
				VVC++;
				responseMessage="Vote Successful...";
			}
			
			sendData = responseMessage.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
			serverSocket.send(sendPacket);
			
			if(VVC == 25)
				break;		
			
		}//end the loop that counts the VVC == 25
		
		System.out.println("Invalid Votes: "+IVC
						  +"\nValid votes: "+VVC+"\n");
		
		Iterator<Candidate> iter = candidates.iterator();
		while(iter.hasNext()) {
			Candidate test = (Candidate)iter.next();
			System.out.println("NAME: "+test.name+"\t\tVOTES: "+test.count);//Server information 	
		}
		
		Candidate winner = Winner(candidates);
		System.out.println("THE WINNER IS "+winner.name+" with "+winner.count+" votes");
		
				
	}
	
	public static Candidate LookUp (String name, ArrayList<Candidate> candidates) {
		Iterator<Candidate> iter = candidates.iterator();
		while(iter.hasNext()) {
			Candidate test = (Candidate)iter.next();
			String nameTest= test.name;
			nameTest = nameTest.replace(" ","");
			if(name.compareToIgnoreCase(nameTest)==0) {
				return test;
			}
		}
		
		return null;
	}
	
	public static Candidate Winner(ArrayList<Candidate> candidates) {
		int max_count=0;
		String name="";
		Iterator<Candidate> iter = candidates.iterator();
		while(iter.hasNext()) {
			Candidate test = (Candidate)iter.next();
			if(test.count>max_count) {
				max_count=test.count;
				name = test.name;
				name= name.replace(" ","");//removes all excess space 
				name= name.replace("\t","");//removes all tab-ed spaces
				
			}
		}
		
		return LookUp(name,candidates);
	}
	

}
