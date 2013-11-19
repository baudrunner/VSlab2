package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import tcp_advanced.Connection;

public class LocalNameService extends NameService{

	private	HostDescriptor host;
	private String NameServerAdress;
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

	@Override
	public void rebind(Object servant, String name) {
		ObjectOutputStream outToNameServer = null;
		Socket socket = null;
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
			outToNameServer = new ObjectOutputStream( socket.getOutputStream() );
			System.out.println("Verbindung vom LocalNameService zum Nameserver aufgebaut");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NameServerRecord record = new NameServerRecord(host, name,servant.getClass()); // Port, Ip und Name 
		try {
			outToNameServer.writeObject(record);
			outToNameServer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			socket.shutdownOutput();
			outToNameServer.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		remoteObjects.put(name, servant);
	}

	@Override
	public Object resolve(String name) {
		
		System.out.println("Resolve-Methode aufgerufen");
		Connection connection = null;
		try {
			connection = new Connection(new Socket( NameServerAdress, NameServerPort ));
			System.out.println("Connection Zum NameServer aufgebaut");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connection.send(name);
		System.out.println("Anfrage für Objekt gesendet");
		
		HostDescriptor objectHoster =  (HostDescriptor)connection.receive();
		if(objectHoster == null){
			System.out.println("objekt nicht vorhanden");
		}else{
			System.out.println("Objekt vorhanden");
		}
		
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

				conn = new Connection(sock);
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
