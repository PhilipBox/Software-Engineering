/** \mainpage 201333186 Moon Ji-hwan


\section Doxygen Doxygen Exercise
- Exercise as JAVA code



\section About About this JAVA code

- Chat Chat & Whisper
- Understanding of multi-threaded socket programming.\n
- Analysis of well-made server-client applications.\n
- Learn (simple) GUI Java programming.\n


\section advenced Sign & Purpose

USER NAME
- Your own ID or whisper target ID to the server.

ACCEPTED_NAME
- Signals to activate chat rooms.

LOGIN
- A signal to notify all users of their status after login.

WSP_msgs
- Sends the ID of the whisper target, and server confirms the target.\n
After confirmation, send a message to the target.

MESSAGE
- Send your own ID and message, all users can see this message.

LOGOUT
- When you logout, the logout user ID and message are displayed.\n

*/

/**
 * @brief Package of Chat Program
 * @author Jihwan Moon
 * @date Nov, 2017
 * @version 1.0
 */
package chat;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


/**
 * @brief Client of Chat program
 * @details GUI JAVA Programming & Client function 
 * @author Jihwan Moon
 * @date Nov, 2017
 * @version 1.0
 */
public class Client {

	// in  : Data to receive  
	// out : Data to send
	BufferedReader in;
	PrintWriter out;
	
	/* Frame name : CHATTER
	 * text window size : 45
	 * messageField size : height(20), width(45)
	*/
	JFrame myframe = new JFrame("CHATTER");
	JTextField textWindow = new JTextField(45);
	JTextArea msgField = new JTextArea(20, 45);

	// text input field and message window are input to JFrame.
	
	/**
	 * @brief main method
	 * @param String[] args
	 * @return none
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.myframe.setVisible(true);
		client.run();
	}// end main method
	
	
	String whisper_receiver = ""; // whisper receiver 
	
	/**
	 * @brief Client method 
	 * @details Assign windows and button\n
	 * message input and print window\n
	 * @param none
	 * @return none
	 */
	public Client() {
		
		/* Assign windows and button
		 *  message input window : North position
		 * message print window : Center position 
		*/ 
		textWindow.setEditable(false);
		msgField.setEditable(false);
		myframe.getContentPane().add(textWindow, "North");
		myframe.getContentPane().add(new JScrollPane(msgField), "Center");
		myframe.pack();

		// When you type in the message window.
		textWindow.addActionListener(new ActionListener() {

			//Sends the message entered in the message input window to the server.
			/**
			 * - public void actionPerformed(ActionEvent e)\n
			 *  Sends the message entered in the message input window to the server.
			 */
			public void actionPerformed(ActionEvent e) {
				out.println(textWindow.getText());
				textWindow.setText("");
			}
		});
	}// end Client method


	/**
	 * @brief calculator funcion
	 * @details Input 'IP address' into IPaddress by using getIPaddress method\n
	 * Connect by using IP address and port number(1220)\n
	 * message received from readLine\n
	 * @param none
	 * @return none
	 * @throws IOException
	 */
	private void run() throws IOException {
		
		// Input 'IP address' into IPaddress by using getIPaddress method
		String IPaddress = getIPaddress();
		
		// Connect by using IP address and port number(1220)
		Socket mysocket = new Socket(IPaddress, 1220);
		
		// in  : Data to receive  
		// out : Data to send
		in = new BufferedReader(new InputStreamReader(mysocket.getInputStream()));
		out = new PrintWriter(mysocket.getOutputStream(), true);

		while (true) {
			
			// message received from readLine
			String msg_received = in.readLine();
			
			// [Whisper] send receiver_name and sender_name to server.
			if (msg_received.startsWith("USER_NAME")) {
				out.println(getUserName() + whisper_receiver);
			} 
			// [General case] send sender_name and use text window.
			if (msg_received.startsWith("ACCEPTED_NAME")) {
				textWindow.setEditable(true);
			}
			// [General case] inform When someone joined in CHATTER.
			if (msg_received.startsWith("LOGIN")) {
				msgField.append("< " + msg_received.substring(5) + " join CHATTER ! >\n");
			} 
			// [Whisper] print message for receiver. 
			if (msg_received.startsWith("WSP_msgs")) {
				msgField.append(msg_received.substring(8) + "\n");
			} 
			// [General case] print message for everyone.
			if (msg_received.startsWith("MESSAGE")) {
				msgField.append(msg_received.substring(8) + "\n");
			}
			// [General case] inform When someone has left from CHATTER.
			if (msg_received.startsWith("LOGOUT")) {
				msgField.append("<<< " + msg_received.substring(6) + " has left CHATTER >>> \n");
			}
		}// end while loop
	}// end run method

	
	/**
	 * @brief IP address input window
	 * @param none
	 * @return JOptionPane.showInputDialog(myframe, "Enter IP Address", "START CHATTER !", JOptionPane.QUESTION_MESSAGE)
	 */
	private String getIPaddress() {
		return JOptionPane.showInputDialog(myframe, "Enter IP Address", "START CHATTER !", JOptionPane.QUESTION_MESSAGE);
	}
	/**
	 * @brief User name input window
	 * @param none
	 * @return  JOptionPane.showInputDialog(myframe, "Enter your name in CHATTER", "CHOOSE NAME", JOptionPane.PLAIN_MESSAGE)
	 */
	private String getUserName() {
		return JOptionPane.showInputDialog(myframe, "Enter your name in CHATTER", "CHOOSE NAME", JOptionPane.PLAIN_MESSAGE);
	}
	
}//end Client class