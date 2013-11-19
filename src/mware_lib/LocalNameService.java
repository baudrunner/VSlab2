package mware_lib;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import tcp_advanced.Connection;

public class LocalNameService extends NameService{
	
	private ObjectOutputStream OutToNameServer;
	private	HostDescriptor host;
	private String NameServerAdress;
	Socket socket;
	ServerSocket servSocket; //auf diesem Socket werden Befehle (fuer z.B. methodenaufrufe) entgegengenommen
	int serverListenPort = 14003;
	int NameServerPort = 14009;
	HashMap<String, Object> remoteObjects = new HashMap<String, Object>();

	public LocalNameService(String serviceHost, int listenPort) {
		
		NameServerAdress = serviceHost;
		host = new HostDescriptor(serviceHost, listenPort);
		// TODO Auto-generated constructor stub
		try {
			servSocket = new ServerSocket(serverListenPort);
			RemoteListener remoteListener = new RemoteListener(servSocket);
			Thread t = new Thread(remoteListener);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void send(Object o) throws IOException  {
		System.out.println("CLIENT: send(Object o)");
		OutToNameServer.writeObject(o);
		OutToNameServer.flush();
		
	}
	
	public void close() throws IOException {
		socket.shutdownOutput();
		OutToNameServer.close();
		socket.close();
	}
	// TODO: vielleicht noch ein close für den Socket machen
	

	@Override
	public void rebind(Object servant, String name) {
		try {
			socket = new Socket( NameServerAdress, NameServerPort );
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("LocalNameService: making object " + servant + " under name:" + name);
		try {
			OutToNameServer = new ObjectOutputStream( socket.getOutputStream() );
			System.out.println("Verbindung vom LocalNameService zum Nameserver aufgebaut");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NameServerRecord record = new NameServerRecord(host, name,servant.getClass()); // Port, Ip und Name 
		try {
			send(record); // sende neuen Eintrag zum NameServer
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		remoteObjects.put(name, servant);
	}

	@Override
	public Object resolve(String name) {
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	private class RemoteListener implements Runnable{
		
		ServerSocket serSock;
		
		public RemoteListener(ServerSocket s){
			serSock = s;
		}

		@Override
		public void run() {
			
			while(true){
				
				try {
					System.out.println("waiting for someone to connect...");
					Socket s = serSock.accept();
					System.out.println("...someone connected!");
					RemoteMethodCallListener rmcl = new RemoteMethodCallListener(s);
					Thread t = new Thread(rmcl);
					t.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 
			}
			
		}

	}
	
	private class RemoteMethodCallListener implements Runnable{
		
		Connection conn;
		
		public RemoteMethodCallListener(Socket sock){
			try {
				conn = new Connection(sock);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			//TODO: hier aufrufe von einzelnen verbindungen entgegennehmen unmarshallen und auf dem lokalen objekt die aktionen ausfueheren
			System.out.println("LocalNameService: waiting for remote method call...");
			RemoteCallDescriptor rcd = (RemoteCallDescriptor)conn.receive();
			System.out.println(rcd.method + " on objekt " + rcd.objName + " should be called.."); 
			
			Object o = remoteObjects.get(rcd.objName);
			
			
			System.out.println("parameter:");
			while(!rcd.params.isEmpty()){
				System.out.println(rcd.params.remove(0));
			}
			System.out.println("##########");
		}

	}

	
	
}
