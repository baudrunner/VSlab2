package tcp_advanced;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Client {
	private Socket MySocket;	
	
	private ObjectInputStream In;
	private ObjectOutputStream Out;
	
	
	public Client(String host, int port) throws UnknownHostException, IOException {
		System.out.println("initializing Client...");
		MySocket = new Socket( host, port );
		
		Out = new ObjectOutputStream( MySocket.getOutputStream() );
		In = new ObjectInputStream(MySocket.getInputStream());
		System.out.println("Client initialized!");
	}
	
	public Object receive() throws IOException, ClassNotFoundException {
		System.out.println("CLIENT: receive(Object o)");
		return In.readObject();
	}
	
	public void send(Object o) throws IOException  {
		System.out.println("CLIENT: send(Object o)");
		Out.writeObject(o);
		Out.flush();
	}
	
	public void close() throws IOException {
		In.close();
		Out.close();
		MySocket.close();
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Client myClient = null;
		
		try{
			// Verbindung aufbauen
			myClient = new Client("localhost", 14002);
		}catch(Exception e){
			e.printStackTrace();
		}
	
		// Kommunikation
		myClient.send(new SerialTestClass(1, 2L, "gib ma ne Exception", new Date()));
		
		try {
			Object smsg = myClient.receive();
			if(smsg instanceof Throwable){
				try { throw (RuntimeException)smsg;} 
				catch (RuntimeException e) { e.printStackTrace(); }
			}
			System.out.println(myClient.receive());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Verbindung schliessen
		myClient.close();
	}

}
