import java.net.*;
import java.util.Scanner;
import java.io.*;

/**
 * This program is one end of a simple command-line interface chat program.
 * It acts as a client which makes a connection to a CLChatServer program.  
 * The computer to connect to can be given as a command line argument.  If
 * it is not, then the program prompts the user for computer name or IP and
 * for port number.  If a computer is specified on the command line, a port
 * number can also be specified as the second command-line argument; if no
 * second argument is specified, the default port number is used.
 * Once a connection has been established, the two ends of the connection
 * each send a HANDSHAKE string to the other, so that both ends can verify
 * that the program on the other end is of the right type.  Then the connected 
 * programs alternate sending messages to each other.  The client always sends 
 * the first message.  The user on either end can close the connection by 
 * entering the string "quit" when prompted for a message.  Note that the first 
 * character of any string sent over the connection must be 0 or 1; this
 * character is interpreted as a command.
 */
class CLChatClient {

	/**
	 * Port number on server, if none is specified on the command line.
	 */
	static final String DEFAULT_PORT = "1728";

	/**
	 * Handshake string. Each end of the connection sends this  string to the 
	 * other just after the connection is opened.  This is done to confirm that 
	 * the program on the other side of the connection is a CLChat program.
	 */
	static final String HANDSHAKE = "CLChat";

	/**
	 * This character is prepended to every message that is sent.
	 */
	static final char MESSAGE = '0';

	/**
	 * This character is sent to the connected program when the user quits.
	 */
	static final char CLOSE = '1';



	public static void main(String[] args) {

		String computer;  // The computer where the server is running,
						  // as specified on the command line.  It can
						  // be either an IP number or a domain name.

		String portStr;   // Port number as a string.
		int port;         // The port on which the server listens.

		Socket connection;      // For communication with the server.

		BufferedReader incoming;  // Stream for receiving data from server.
		PrintWriter outgoing;     // Stream for sending data to server.
		String messageOut;        // A message to be sent to the server.
		String messageIn;         // A message received from the server.

		Scanner userInput;        // A wrapper for System.in, for reading
								  // lines of input from the user.


		/* First, get the computer and port number. */

		if (args.length == 0) {
			Scanner stdin = new Scanner(System.in);
			System.out.print("Enter computer name or IP address: ");
			computer = stdin.nextLine();
			System.out.print("Enter port, or press return to use default:");
			portStr = stdin.nextLine();
			if (portStr.length() == 0)
				portStr = DEFAULT_PORT;
		}
		else {
			computer = args[0];
			if (args.length == 1)
				portStr = DEFAULT_PORT;
			else
				portStr = args[1];
		}
		try {
			port= Integer.parseInt(portStr);
			if (port <= 0 || port > 65535)
				throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			System.out.println("Illegal port number, " + args[1]);
			return;
		}

		
		/* Open a connection to the server.  Create streams for 
         communication and exchange the handshake. */

		try {
			System.out.println("Connecting to " + computer + " on port " + port);
			connection = new Socket(computer,port);
			incoming = new BufferedReader(
					new InputStreamReader(connection.getInputStream()) );
			outgoing = new PrintWriter(connection.getOutputStream());
			outgoing.println(HANDSHAKE);  // Send handshake to server.
			outgoing.flush();
			messageIn = incoming.readLine();  // Receive handshake from server.
			if (! messageIn.equals(HANDSHAKE) ) {
				throw new IOException("Connected program is not CLChat!");
			}
			System.out.println("Connected.  Enter your first message.");
		}
		catch (Exception e) {
			System.out.println("An error occurred while opening connection.");
			System.out.println(e.toString());
			return;
		}

		/* Exchange messages with the other end of the connection until one side or 
		   the other closes the connection.  This client program sends the first message.
		   After that,  messages alternate strictly back and forth. */

		try {
			userInput = new Scanner(System.in);
			System.out.println("NOTE: Enter 'quit' to end the program.\n");
			while (true) {
				System.out.print("SEND:      ");
				messageOut = userInput.nextLine();
				if (messageOut.equalsIgnoreCase("quit"))  {
						// User wants to quit.  Inform the other side
						// of the connection, then close the connection.
					outgoing.println(CLOSE);
					outgoing.flush();
					connection.close();
					System.out.println("Connection closed.");
					break;
				}
				outgoing.println(MESSAGE + messageOut);
				outgoing.flush();
				if (outgoing.checkError()) {
					throw new IOException("Error occurred while transmitting message.");
				}
				System.out.println("WAITING...");
				messageIn = incoming.readLine();
				if (messageIn.length() > 0) {
						// The first character of the message is a command. If 
						// the command is CLOSE, then the connection is closed.  
						// Otherwise, remove the command character from the 
						// message and proceed.
					if (messageIn.charAt(0) == CLOSE) {
						System.out.println("Connection closed at other end.");
						connection.close();
						break;
					}
					messageIn = messageIn.substring(1);
				}
				System.out.println("RECEIVED:  " + messageIn);
			}
		}
		catch (Exception e) {
			System.out.println("Sorry, an error has occurred.  Connection lost.");
			System.out.println(e.toString());
			System.exit(1);
		}

	}  // end main()



} //end class CLChatClient
