package mware_lib;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalNameService extends NameService{

	public static int threadIDcntr = 0;
	private	HostDescriptor host;
	private String NameServerAdress;
	ServerSocket servSocket; //auf diesem Socket werden Befehle (fuer z.B. methodenaufrufe) entgegengenommen
	int NameServerPort;
	HashMap<String, RemoteCall_I> remoteObjects = new HashMap<String, RemoteCall_I>();
	Connection connection = null;
    Lock connMutex;

	public LocalNameService(String serviceHost, int listenPort) {
		
		NameServerAdress = serviceHost;
		NameServerPort = listenPort;
		try {
			servSocket = new ServerSocket(0);
			connMutex = new ReentrantLock(true);
			System.out.println("Port mit der LocalNameService gestartet wird" + servSocket.getLocalPort());
			Socket sockToNameServer = new Socket(NameServerAdress, NameServerPort);
			connection = new Connection(sockToNameServer); //direkte verbindung zum NameServer
			host = new HostDescriptor(sockToNameServer.getLocalAddress().toString().substring(1), servSocket.getLocalPort());
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
		
		NameServerRecord record = new NameServerRecord(host, name); // Port, Ip und Name 
		remoteObjects.put(name, (RemoteCall_I)servant);
		connMutex.lock();
		connection.send(record);
		connMutex.unlock();
	}

	@Override
	public Object resolve(String name) {
		
		System.out.println("Resolve-Methode aufgerufen");
		connMutex.lock();
		connection.send(name);
		System.out.println("Anfrage für Objekt gesendet");
		
		NameServerRecord targetRecord = null;
		try {
			targetRecord = (NameServerRecord)connection.receive();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			connMutex.unlock();
		}
		
		if(targetRecord == null){
			System.out.println("objekt nicht vorhanden");
		}else{
			System.out.println("objekt vorhanden");
		}
		
		System.out.println("Resolve Rueckgabewert targetRecord ausgabe von getName: " + targetRecord.getName());
		System.out.println("Resolve Rueckgabewert targetRecord ausgabe von HostDescriptor : " + targetRecord.getHostDescriptor());
		System.out.println("Resolve Rueckgabewert targetRecord : " + targetRecord);
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
					System.out.println("spawning Thread...");
					t.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	}
	
	private class RemoteMethodCallListener implements Runnable{
				
		int threadID;
		Connection conn;
		
		public RemoteMethodCallListener(Socket sock){
				conn = new Connection(sock);
				threadID = threadIDcntr++;
		}

		@Override
		public void run() {
			
			//while(true){
				System.out.println("RemoteMethodCallListener["+ threadID +"]: waiting for remote method call...");
				RemoteCallDescriptor rcd = null;
				try {
					rcd = (RemoteCallDescriptor)conn.receive();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(rcd.getMethod() + " on objekt " + rcd.getObjName() + " should be called.."); 
				
				remoteObjects.get(rcd.getObjName());
								
				RemoteCall_I rco = remoteObjects.get(rcd.getObjName());
				Object returnVal = rco.callMethod(rcd.getMethod(), rcd.getParams());
				if(returnVal instanceof Exception){
					System.err.println("Fehler beim Aufruf von " + rcd.getMethod() + " auf Object " + rcd.getObjName() + " -> sende Exception an aufrufenden Host..");
				}
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
