package name_service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RemoteObject;
import java.util.HashMap;

import tcp_advanced.Connection;
import tcp_advanced.Server;
import mware_lib.HostDescriptor;
import mware_lib.NameServerRecord;


public class NameServer {

	private ServerSocket MySvrSocket;
	
	static int serverListenPort = 14009;
	HashMap<String, NameServerRecord> remoteObjects = new HashMap<String, NameServerRecord>();

	
	public NameServer(int i) throws IOException {
		System.out.println("initializing NameServer...");
		MySvrSocket = new ServerSocket(serverListenPort);
		System.out.println("Server initialized! listening on port " + serverListenPort);
	}

	public Connection getConnection() throws IOException {
		return new Connection(MySvrSocket.accept());
	}

	public static void main(String[] args) throws IOException {
		
		NameServer nameSerer = new NameServer(serverListenPort);
		nameSerer.run();
		
	}
	
	
	private void run(){
		
		while(true){
			// Auf Verbindungsanfrage warten.
			System.out.println("SERVER: waiting for client to connect...");
			Connection myConnection = null;
			try {
				myConnection = getConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			System.out.println("SERVER: waiting for message from client...");
			Object cmsg = myConnection.receive();
			System.out.println("received: " + cmsg);
			
			if(cmsg instanceof NameServerRecord){
				remoteObjects.put(((NameServerRecord) cmsg).getName(), (NameServerRecord)cmsg);
				System.out.println("Neues Objekt zum NameServer hinzugefuegt Klasse ist: " + ((NameServerRecord) cmsg).getClassObject());
			}else if(cmsg instanceof String){
					myConnection.send(remoteObjects.get((String)cmsg));
			}
			
			try {
				myConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
