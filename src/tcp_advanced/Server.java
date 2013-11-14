package tcp_advanced;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

public class Server {
	private ServerSocket MySvrSocket;
	
	public Server(int listenPort) throws IOException {
		System.out.println("initializing Server...");
		MySvrSocket = new ServerSocket(listenPort);		
		System.out.println("Server initialized! listening on port " + listenPort);
	}
	
	public Connection getConnection() throws IOException {
		return new Connection(MySvrSocket.accept());
	}
	
	public void shutdown() throws IOException {
		MySvrSocket.close();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Server theServer = new Server(14002);

		// Auf Verbindungsanfrage warten.
		System.out.println("SERVER: waiting for client to connect...");
		Connection myConnection = theServer.getConnection();		
		
		// Kommunikation
		try {
			System.out.println("SERVER: waiting for message from client...");
			Object cmsg = myConnection.receive();
			System.out.println("received: " + cmsg);
			if(cmsg instanceof SerialTestClass && ((SerialTestClass) cmsg).s.equals("gib ma ne Exception") ){
				try{ Object o = null; o.toString();}
				catch(Exception e){ myConnection.send(e); };
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//myConnection.send(new SerialTestClass(3, 5L, "testantwort vom Server", new Date()));
		
		// Verbindung schliessen
		myConnection.close();

		// Server runterfahren
		theServer.shutdown();
	}
}
