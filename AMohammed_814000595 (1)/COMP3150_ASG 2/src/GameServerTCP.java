	
//This program runs on one machine 

import java.io.*;
import java.util.*;
import java.net.*;

public class GameServerTCP {
	
	public static void main(String args[]) {
		
		//declare a TCP socket object and initialize it to null
        ServerSocket welcomeSocket=null;
        //create the port number
        int port = 6790;
        
        try {
        	//Create the TCP server socket
        	welcomeSocket=new ServerSocket(port);
        	System.out.println("TCP server created on port = "+port);
        } catch (IOException ex) {
            //will be executed when the server cannot be created
            System.out.println("Error: The server with port="+port+" cannot be created");
        }
        
        System.out.print("/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */\n"
                + "\t\t\t\t WHAT YUH KNOW!!!\n"
                + "/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */\n");
       
        final int set =10;
        Queue <String> QuestionQueue1 = new LinkedList<String>();//Queue of questions to be used
        Queue <String> QuestionQueueAns= new LinkedList<String>();//Answers to the questions asked
        /*
         * Extra Feature is hint purchases
         */
        Queue <String> QuestionQueueHints= new LinkedList<String>();//hints able to be purchased. Cost increases exponentially with every hint given 
        
        //1
        QuestionQueue1.add("Question #:1\nWhat does pvc stand for?\nA: Plenty Volume Control\nB: Polymer vinyl Chlorine\nC: Poly(V) Chloride\nD: Polyvinyl Chloride\n#");
        QuestionQueueAns.add("D");
        QuestionQueueHints.add("PVC is a Polymer, has a smooth vinyl like feel and has a Chloride component");
        //2
        QuestionQueue1.add("Question #:2\nHow much percent alcohol is there in pure H2O? (number only dont include % symbol)\n#");
        QuestionQueueAns.add("0");
        QuestionQueueHints.add("The functional group for any alcohol is the R-OH group./nWater or H-O-H or H2O has its oxygen atom fully bonded to 2H's therefore is water alcoholic in nature?");
        //3
        QuestionQueue1.add("Question #:3\nHow many 5 cent pieces are in $38.00\n#");
        QuestionQueueAns.add("760");
        QuestionQueueHints.add("(20) 5 cent pieces are in $1 TTD");
        //4
        QuestionQueue1.add("Question #:4\nWhat is the smallest unit of matter?\nA: Element\nB: Atom\nC: Molecule\nD: Electron\n#");
        QuestionQueueAns.add("B");
        QuestionQueueHints.add("Matter is made up of very small particles invisible to the naked eye called ______");
        //5
        QuestionQueue1.add("Question #:5\nWhat is gold?\nA: Element\nB: Atom\nC: Molecule\nD: Electron\n#");
        QuestionQueueAns.add("A");
        QuestionQueueHints.add("Gold has the chemical symbol of Au on the periodic table and periodic table contains a list of what what?");
        //6
        QuestionQueue1.add("Question #:6\nWhich of the following is correct?\nA: Protons and Neutrons are electrically charged\nB: Protons and Electrons are electrically charged\nC: Electrons and Neutrons are electrically charged\nD: Neutrons, Electrons and Protons are electrically charged\n#");
        QuestionQueueAns.add("B");
        QuestionQueueHints.add("So a quick joke: A neutron walks into a bar and orders a drink, he then asks the bartender, \"How much do I owe ya.\" the bartender replies\"For you..No Charge!\"");
        //7
        QuestionQueue1.add("Question #:7\nWhere is \"Sauce Doubles\" Located\n#");
        QuestionQueueAns.add("curepe");
        QuestionQueueHints.add("Where can you get a taxi, after deboarding a maxi on the bus route, to go Caroni in Trinidad");
        //8
        QuestionQueue1.add("Question #:8\nWhere is What is the pitch lake made out of?\n#");
        QuestionQueueAns.add("Asphalt");
        QuestionQueueHints.add("No,its not pitch");
        //9
        QuestionQueue1.add("Question #:9\nHow many national songs does TnT have\n#");
        QuestionQueueAns.add("9");
        QuestionQueueHints.add("Sometimes the answer we seek is really right in front of us");
        //10
        QuestionQueue1.add("Question #:10\nnWhere is Paria Waterfall located in Trinidad\n#");
        QuestionQueueAns.add("Blanchisseuse");
        QuestionQueueHints.add("Blanc--sse--e");
        
        
        //---------------------------------------IMPORTANT NOTE TO SELF------------------------------------
        //check if the queue for each client remains unaffected after client plays wins and members of the queue are removed
        //If so make a temp copy to use within the while(true) of the server section
        
        //*******CHECKED and thus needed a temp copy of queue just remember to empty it when client quits to save memory 

        String clientResponse =null;
        //start an endless loop to listen for clients
        
        while(true){
        	Socket connectionSocket = null;
        	try {//wait for connection (listening socket) 
        		System.out.println("[Game Server TCP] waiting for the incoming request ...");
        		connectionSocket = welcomeSocket.accept();//passes the connection from listening the socket to an auxiliary socket
        		
        	}
        	 catch (IOException e) {
                 System.out.println("Error: cannot accept client request. Exit program");
                 return;
             }
        	
        	try {//Guts of the game is built within here-----------------------------------------------------------------------------------------------
        		
        		Queue <String> QuestionQueuetemp=new LinkedList<>(QuestionQueue1);//Temp copy of queue of questions to be used
        		
        	    Queue <String> QuestionQueueAnstemp=new LinkedList<>(QuestionQueueAns);//Temp copy of queue of answers to the questions asked
        	    
        	    Queue <String> ExtraFeatureQueue=new LinkedList<>(QuestionQueueHints);//Temp queue of hints to be used 
        	    
        		int score=0, correctQuestions=0, count=0, pass=0, hint_counter=1, hint_cost=0;
        	    String question,ans,hint;
        	    
        		PrintStream toClient = new PrintStream(connectionSocket.getOutputStream());//Print to client
                InputStreamReader inputStream = new InputStreamReader(connectionSocket.getInputStream()); 
                BufferedReader inFromClient = new BufferedReader(inputStream);//To read input from client
                
                BasicChoice(connectionSocket);//Ask if they wish to Continue or Quit
                clientResponse = inFromClient.readLine();//Check if they want to Continue or Quit
                //System.out.println(clientResponse);//test to see what the client was sending
                
                while(!( clientResponse.equalsIgnoreCase("Q") ) && !( QuestionQueuetemp.isEmpty() )) {
                	
                	if(clientResponse.equalsIgnoreCase("Y")) {
                		count++;
                		//System.out.println(count);
                		
                		if(count<=set) {
                			toClient.println("[ATTEMPT 1]---------------\n");                			
                			pass=1;//first pass where correct answers are worth 10pts and questions can be added back into the queue
                		}
                		else {
                			pass=2;//second pass where correct answers are worth 5pts 
                			toClient.println("[ATTEMPT 2]----------------\nOk since you got some wrong, were gonna ask those again at a 5 pts each\nGOOD LUCK!\n");
                		} 
                		//System.out.println("Pass "+pass);//test to see if passes switching in correct flow
                		
                		question = QuestionQueuetemp.poll();//stores the question from the top of queue in here and removes it from queue
                		toClient.println("SCORE:"+score+" pts\nPress the 'H' key for a hint..Cost: "+ExtraFeature(hint_counter)+" pts\n"+question);//tells the client the question and to enter H to purchase a hint 
                		
                		clientResponse = inFromClient.readLine();//the client's answer to question 
                		ans = QuestionQueueAnstemp.poll();//stores the answer from the top of queue in here and removes it from queue
                		hint=ExtraFeatureQueue.poll();//holds the hint in case it is purchased
                		//poll returned and removed the head of the linkedlist 
                		//they can be re-added into queues and be automatically added to back of the queue if the person gets it wrong preparation for the second pass
                		
                		while(clientResponse.length()==0) {//ensure that if player pushes enter without entering anything it asks for their answer once again
                			toClient.println("Error: No Response was detected\nAnswer:\n#");
                        	clientResponse = inFromClient.readLine();
                		}
                		
                		int check = Check(ans,clientResponse);
                		int flag=0;//to prevent consecutive billing or requesting the same hint if hint was already purchased 
             
                		if(pass==1) {//first attempt
                			
                			while(check==3) {//path of the hint or extra feature
                				hint_cost= ExtraFeature(hint_counter,score);
                				//System.out.println(hint_cost);//hint cost wasnt sending the -1 for some reason ---------fixed
                				
                				if(hint_cost == -1) {
                					toClient.println("............Sorry but you currently dont have enough funds to purchase this feature\nAnswer: \n#");
                					
                				}else if(flag==0 && hint_cost!=-1) {
                					score=score-hint_cost;//remove the cost of purchase 
                					toClient.println("HINT PURCHASED...\nHINT:"+hint+"\nAnswer: \n#");
                					
                					//Need to prevent player from being billed for same question after a hint was already purchased
                					flag=1;
                					hint_counter+=1;//every time hint goes through cost of next hint increases
                				}else {
                					toClient.println("HINT ALREADY PURCHASED\nAnswer: \n#");
                				}
                				
                				clientResponse = inFromClient.readLine();//the client's answer to question after given hint 
                				//System.out.println(clientResponse);//test here cause a client wasn't able to send anything from here onwards to server-------fixed
                				while(clientResponse.length()==0) {//ensure that if player pushes enter without entering anything it asks for their answer once again
                        			toClient.println("Error: No Response was detected\nAnswer:\n#");
                                	clientResponse = inFromClient.readLine();
                        		}
            					check=Check(ans,clientResponse);//checks the answer
            					
                			}
                			
                			if(check == 0 ) {//path of the correct answer 
                    			score+=10;
                    			toClient.println("CORRECT!!!!!!!!!\nWELLDONE KEEP AT IT!\nSCORE:"+score+" pts\n");
                    		}
                    		
                    		if( check == 1 ) {//path of the incorrect answer
                    			toClient.println("Aww no sorry that is incorrect. Doh fret, yuh have a second chance next round\nKEEP AT IT!!\nSCORE:"+score+"pts\n");
                    			//Re-enter incorrect questions, their answers and hint into queues
                    			QuestionQueuetemp.add(question);
                    			QuestionQueueAnstemp.add(ans);
                    			ExtraFeatureQueue.add(hint);
                    		}	
                    		
                			
                		}
                		
                		if(pass == 2) {
                			while(check==3) {//path of the hint or extra feature
                				hint_cost= ExtraFeature(hint_counter,score);
                				//System.out.println(hint_cost);//hint cost wasnt sending the -1 for some reason ---------fixed
                				
                				if(hint_cost == -1) {
                					toClient.println("............Sorry but you currently dont have enough funds to purchase this feature\nAnswer: \n#");
                					
                				}else if(flag==0 && hint_cost!=-1) {
                					score=score-hint_cost;//remove the cost of purchase 
                					toClient.println("HINT PURCHASED...\nHINT:"+hint+"\nAnswer: \n#");
                					
                					//Need to prevent player from being billed for same question after a hint was already purchased
                					flag=1;
                					hint_counter+=1;//every time hint goes through cost of next hint increases
                				}else {
                					toClient.println("HINT ALREADY PURCHASED\nAnswer: \n#");
                				}
                				
                				clientResponse = inFromClient.readLine();//the client's answer to question after given hint 
                				//System.out.println(clientResponse);//test here cause a client wasn't able to send anything from here onwards to server-------fixed
                				
                				while(clientResponse.length()==0) {//ensure that if player pushes enter without entering anything it asks for their answer once again
                        			toClient.println("Error: No Response was detected\nAnswer:\n#");
                                	clientResponse = inFromClient.readLine();
                        		}
                				check=Check(ans,clientResponse);//checks the answer
                			}
                			
                			if(check == 0 ) {//path of the correct answer 
                    			score+=5;
                    			toClient.println("CORRECT!!!!!!!!!\nWELLDONE KEEP AT IT!\nSCORE:"+score+" pts");
                    		}
                    		
                    		if( check == 1 ) {//path of the incorrect answer
                    			toClient.println("Aww no sorry that is incorrect.\nKEEP AT IT THO, YOU GOT THIS!!\nSCORE:"+score+" pts\n");
                    			//Do not add back questions
                    		}
                    		
                		}
                		
                    }//end of continue section 
                	
                    else 
                    	toClient.print("Error: INCORRECT INPUT\n");
                	
                    if(! QuestionQueuetemp.isEmpty()) {
                    	BasicChoice(connectionSocket);
                    	clientResponse = inFromClient.readLine();
                    }else
                    	toClient.println("HURRAY!! CONGRATULATIONS ON MAKING IT TO THE END");
                    
                	
                    
            	}//end of "while queues are not empty and client didn't want to quit"
                
                QuestionQueuetemp.removeAll(QuestionQueuetemp);
        	   	QuestionQueueAnstemp.removeAll(QuestionQueuetemp);
        	   	ExtraFeatureQueue.removeAll(ExtraFeatureQueue);
                toClient.println("\n\n\n----------------------------------GAME OVER-----------------------------------\nSCORE:"+score+"\nThank you for playing...GOODBYE\n#");
        		//continue;
        	}catch (Exception e){
        		System.out.print("[Game Server TCP] The server cannot send the message");
        	}//GUTS of game ends here-------------------------------------------------------------------------------------------------------------
        	
        
        }//ends server loop 
        	
	}
    public static int Check(String ans, String clientResponse) {//Checks if the answer is correct or wrong or hint request returns 0-correct or 1-wrong or 3- hint request
    	if(clientResponse.equalsIgnoreCase(ans)) {
    		return 0;
    	}
    	if(clientResponse.equalsIgnoreCase("H") ){
    		return 3;
    	}
    	else {
    		return 1;
    	}
    	
    }
         
	public static void BasicChoice(Socket connectionSocket) throws IOException { //Simply asks the client if they want to continue with questions or quit
		PrintStream outToClient = new PrintStream(connectionSocket.getOutputStream());		
		String message=("READY TO GO AGAIN?!\tCONTINUE = Y\t\tQUIT= Q\n#");
		outToClient.println(message);
		
		
	}
	
	
	public static int ExtraFeature(int count) {//Return the cost of hints using only the hint_counter as the passed parameters
		int cost= (int) Math.pow(3,count);//cost starts at 3^1 then 3^2 etc etc
		
			return cost;	
	}
	
	public static int ExtraFeature(int count, int points) {//Return the cost of hints or -1 if infeasible to purchase uses the hint_counter and points as the passed parameters
		int cost= (int) Math.pow(3,count);//cost starts at 3^1 then 3^2 etc etc
		if(points<cost) {
			
			return -1;
		}
		return cost;
	}
	
}