package chat;
import java.io.*;
import java.net.*;
import java.util.*;


/**
 * @brief Server of Chat program
 * @details Server function 
 * @author Jihwan Moon
 * @date Nov, 2017
 * @version 1.0
 */
public class Server {

   /* 
    * port number is '1220'.
    * user_names : variable to store name .
    * writers    : variable to print for all users .
   */
   private static final int PORT_NUMBER = 1220;
   private static ArrayList<String> user_names = new ArrayList<String>();
   private static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

	/**
	 * @brief main method
	 * @param String[] args
	 * @return none
	 * @throws Exception
	 * @details \n
	 * - ServerSocket OPEN\n
	 * - private static class Manage extends Thread \n
	 * in 	  : data received\n
	 * out	  : data to send \n
	 * id_name  : user id\n
	 * mySocket : to store connection from client\n\n
	 * 
	 * - public void run()\n
	 * chat process method\n
	 */
   public static void main(String[] args) throws Exception {
      System.out.println("[ Now, The chatting program server start ! ]");
      
      // ServerSocket OPEN
      ServerSocket welcomeSocket = new ServerSocket(PORT_NUMBER);
      try {
         while (true) {
            // waiting for connection ( server - client )
            new Manage(welcomeSocket.accept()).start();
         }
      } finally {
    	  welcomeSocket.close();
      }
   } //end main method


   private static class Manage extends Thread {

	  /*
	   * in 	  : data received
	   * out	  : data to send 
	   * id_name  : user id
	   * mySocket : to store connection from client
	   */
	  private BufferedReader in; 
	  private PrintWriter out; 
      private String id_name;
      private Socket mySocket; 

      
      public Manage(Socket socket) {
         this.mySocket = socket;
      }
      
      // chat process method
      public void run() {
         try {
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            out = new PrintWriter(mySocket.getOutputStream(), true);

            while (true) {
               // message to client ( request user name )
               // and store user name to id_name
               out.println("USER_NAME");
               id_name = in.readLine();

               // make sure the name exists,
               // if it exists, re-enter it or save the name to name.
               synchronized (user_names) {
                  if (!user_names.contains(id_name)) {
                	  user_names.add(id_name);
                     break;
                  }
               }// end synchronized 
            }//end while loop

            
            
            // request that open the message window to client(user).
            out.println("ACCEPTED_NAME");
            writers.add(out);
            
            // At the first time, information about whisper format and window.
            out.println("MESSAGE" + " ========================================================");
            out.println("MESSAGE" + " < WHISPER FUNCTION FORMAT > : Typed text + /wsp + user_name");
            out.println("MESSAGE" + " EX)  This is whisper only for you!!  /wsp  joy ");
            out.println("MESSAGE" + " ==================== " + id_name + " 's WINDOW ====================");

            // login information about all of users
            for (int i = 0; i < writers.size(); i++) {
               if (!id_name.equals(user_names.get(i)))
                  out.println("LOGIN" + user_names.get(i));
            }
            for (PrintWriter writer : writers) {
               writer.println("LOGIN" + id_name);
            }

            
            while (true) {
               // read messages from user
               String input = in.readLine();
               
               // if client exit the program, then repetition also exit.
               if (input == null) {
                  return;
               }

               // check whisper ( my protocol : /wsp )
               String sign[] = input.split(" /wsp ");
               
               // whisper check flag : initial value is '0'
               // if whisper is received, then whisper_check convert to '1'
               int whisper_check = 0;
               
               // [whisper case]
               if (sign.length > 1) { 
                  for (int i = 0; i < writers.size(); i++) {
                     
                	 // ERROR Case : whisper to myself
                     if(sign[1].equals(id_name) && i == 0) {
                        out.println("WSP_msgs" + "<ERROR> You can't send whisper to myself !");
                        whisper_check = 1;
                        break;
                     }
                     
                     // find whisper target name
                     if (sign[1].equals(user_names.get(i))) {
                    	// send message to target(only)
                        writers.get(i).println("WSP_msgs" + "�뼳�뼳[ whisper from " + id_name + " ] : " + sign[0]);
                        out.println("WSP_msgs" + "�뼳�뼳[ whisper to " + sign[1] + " ] : " + sign[0]);
                        writers.get(i).flush();
                        whisper_check = 1;
                        break;
                     }
                  }
                  // ERROR case : invalid user name
                  if (whisper_check == 0)
                     out.println("WSP_msgs" + "<ERROR> " + sign[1] + " is invalid user name(id) !");
              
               // [General Case] : not whisper situation.
               } else {
                  for (PrintWriter writer : writers) {
                     writer.println("MESSAGE �뼳" + id_name + " : " + input);
                  }
               }//end else
            }//end while loop
         } catch (IOException e) {
            System.out.println(e);
         }
         
         finally {
            if (id_name != null) {
            	// inform information about logout user name(id)
            	for (PrintWriter writer : writers) {
                  writer.println("LOGOUT " + id_name);
               }
               user_names.remove(id_name);
            }
            
            if (out != null) {
               writers.remove(out);
            }
            
            try {
            	// logout client's connection close.
            	mySocket.close();
            } catch (IOException e) {
            }
         }//end finally
      }//end run method
   }//end Manage method
}//end Server class
