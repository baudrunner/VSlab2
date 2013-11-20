package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import mware_lib.stubs.AccountImplBase_Stub;
import mware_lib.stubs.ManagerImplBase_Stub;
import mware_lib.stubs.TransactionImplBase_Stub;
import cash_access.TransactionImplBase;
import bank_access.AccountImplBase;
import bank_access.ManagerImplBase;
import tcp_advanced.Connection;

public class LocalNameService extends NameService{

	private	HostDescriptor host;
	private String NameServerAdress;
	ServerSocket servSocket; //auf diesem Socket werden Befehle (fuer z.B. methodenaufrufe) entgegengenommen
	int serverListenPort = 14003;
	int NameServerPort;
	HashMap<String, RemoteCall_I> remoteObjects = new HashMap<String, RemoteCall_I>();

	public LocalNameService(String serviceHost, int listenPort) {
		
		NameServerAdress = serviceHost;
		NameServerPort = listenPort;
		// TODO Auto-generated constructor stub
		try {
			servSocket = new ServerSocket(serverListenPort);
			//host = new HostDescriptor(servSocket.getLocalSocketAddress().toString(), serverListenPort);
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
			host = new HostDescriptor(socket.getLocalAddress().toString().substring(1), serverListenPort); 
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
		
		
		if(servant instanceof AccountImplBase){
			remoteObjects.put(name, new AccountImplBase_Stub((AccountImplBase)servant));
		}else if(servant instanceof ManagerImplBase){
			remoteObjects.put(name, new ManagerImplBase_Stub((ManagerImplBase)servant));
		}else if(servant instanceof TransactionImplBase){
			remoteObjects.put(name, new TransactionImplBase_Stub((TransactionImplBase)servant));
		}
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
		
		NameServerRecord targetRecord =  (NameServerRecord)connection.receive();
		if(targetRecord == null){
			System.out.println("objekt nicht vorhanden");
		}else{
			System.out.println("Objekt vorhanden auf Adresse:" + targetRecord.getClassObject() );
			
			
			
		}
		
		// TODO Auto-generated method stub
		
		
		return targetRecord;
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
			
			//while(true){
				//TODO: hier aufrufe von einzelnen verbindungen entgegennehmen unmarshallen und auf dem lokalen objekt die aktionen ausfueheren
				System.out.println("LocalNameService: waiting for remote method call...");
				RemoteCallDescriptor rcd = (RemoteCallDescriptor)conn.receive();
				System.out.println(rcd.getMethod() + " on objekt " + rcd.getObjName() + " should be called.."); 
				
				
				remoteObjects.get(rcd.getObjName());
				
				
				RemoteCall_I rco = remoteObjects.get(rcd.getObjName());
				Object returnVal = rco.callMethod(rcd.getMethod(), rcd.getParams());
				conn.send(returnVal);
				
				System.out.println("parameter:");
				while(!rcd.getParams().isEmpty()){
					System.out.println(rcd.getParams().remove(0));
				}
				System.out.println("##########");
			//}
			
		}

	}

	
	
}
