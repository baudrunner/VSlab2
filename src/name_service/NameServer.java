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
		NameServer nameServer = new NameServer(serverListenPort);
		nameServer.listen();
	}
	
	public void listen(){
		while(true){
			try {
				NameServerListener nlr = new NameServerListener(getConnection());
				Thread listenerThread = new Thread(nlr);
				listenerThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class NameServerListener implements Runnable{
		
		private Connection clientConnection; 
		
		public NameServerListener(Connection clientConn){
			clientConnection = clientConn;
		}

		@Override
		public void run() {
			
			while(true){
				System.out.println("SERVER: waiting for message from client...");
				Object cmsg = null;
				try {
					cmsg = clientConnection.receive();
				} catch (IOException e) {
					System.err.println("Client " + clientConnection.getRemoteIP() +":"+ clientConnection.getRemotePort() + " hat Verbindung beendet!");
					//e.printStackTrace();
					break;
				}
				
				System.out.println("received: " + cmsg);
				if(cmsg instanceof NameServerRecord){ //Host meldet Objekt unter Name an
					remoteObjects.put(((NameServerRecord) cmsg).getName(), (NameServerRecord)cmsg);
					System.out.println("Neues Objekt zum NameServer hinzugefuegt");
				}else if(cmsg instanceof String){//Host moechte Eintrag von Objekt mit name 'cmsg'
					System.out.println("suche im Verzeichnis nach Objekt mit Name '" + (String)cmsg + "' ...");
					Object orderedObject = remoteObjects.get((String)cmsg);
					if(orderedObject == null){
						System.err.println("KeinObjekt mit Name '" + (String)cmsg + "' gefunden");
					}else{
						System.out.println("returning " + orderedObject);
					}
					clientConnection.send(remoteObjects.get((String)cmsg));
				}
			}			
		}

	}
	
}
