package name_service;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NameServer {

	private ServerSocket MySvrSocket;
	
	static int serverListenPort;
	HashMap<String, Object> remoteObjects = new HashMap<String, Object>();
	Lock mutex;

	
	public NameServer(int i) throws IOException {
		mutex = new ReentrantLock(true);
		System.out.println("initializing NameServer...");
		MySvrSocket = new ServerSocket(serverListenPort);
		System.out.println("Server initialized! listening on port " + serverListenPort);
	}

	public Connection getConnection() throws IOException {
		return new Connection(MySvrSocket.accept());
	}

	public static void main(String[] args) throws IOException {
		if(args.length==1) {
			serverListenPort=Integer.valueOf(args[0]);
		}else {
            System.out.println("Bitte Port als Parameter uebergeben");
            return;
		}
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
				if(cmsg instanceof String){//Host moechte Eintrag von Objekt mit name 'cmsg'
					System.out.println("suche im Verzeichnis nach Objekt mit Name '" + (String)cmsg + "' ...");
					mutex.lock();
					Object orderedObject = remoteObjects.get((String)cmsg);
					mutex.unlock();
					if(orderedObject == null){
						System.err.println("KeinObjekt mit Name '" + (String)cmsg + "' gefunden");
					}else{
						System.out.println("returning " + orderedObject);
					}
					clientConnection.send(remoteObjects.get((String)cmsg));
				}else{
					mutex.lock();
					Object[] arraycmsg = (Object[]) cmsg;
					remoteObjects.put( (String)arraycmsg[0], arraycmsg[1]);
					mutex.unlock();
					System.out.println("Neues Objekt zum NameServer hinzugefuegt");
				}
			}			
		}

	}
	
}
