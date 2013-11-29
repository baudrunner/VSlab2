package mware_lib;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalNameService extends NameService{

	public static int threadIDcntr = 0;
	ArrayList<RemoteMethodCallListener> rmlcList = new ArrayList<RemoteMethodCallListener>();
	ArrayList<Thread> threadList = new ArrayList<Thread>();
	RemoteListener remoteListenerThreadRunnable;
	Thread remoteListenerThread;
	boolean shutdown = false;
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
			remoteListenerThreadRunnable = remoteListener;
			Thread t = new Thread(remoteListener);
			remoteListenerThread = t;
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void rebind(Object servant, String name) {
		
		NameServerRecord record = new NameServerRecord(host, name); // Port, Ip und Name 
		Object[] recordOa = new Object[]{name, record}; // Port, Ip und Name 
		remoteObjects.put(name, (RemoteCall_I)servant);
		connMutex.lock();
		connection.send(recordOa);
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

			while(!shutdown){
				
				try {
					System.out.println("waiting for someone to connect...");
					Socket s = serSock.accept();
					System.out.println("...someone connected!");
					RemoteMethodCallListener rmcl = new RemoteMethodCallListener(s);
					rmlcList.add(rmcl);
					Thread t = new Thread(rmcl);
					threadList.add(t);
					System.out.println("spawning Thread...");
					t.start();
				} catch (IOException e) {
					if(shutdown){
						System.out.println("shutdown...");
					}else{
						e.printStackTrace();
					}
				} 
			}
		}
		
		public void stop() {
			shutdown = true;
			try {
				serSock.close();
			} catch (IOException e) {
				e.printStackTrace();
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
					if(shutdown){
						System.out.println("System wird heruntergefahren");
					}else{
						e.printStackTrace();
					}
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
		
		public void stop() {
			shutdown = true;
			try {
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	public void shutdown() {
		
		System.out.println("shutdown methode von LocalNameService aufgerufen");
		
		remoteListenerThreadRunnable.stop();
		
		try {
			remoteListenerThread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		shutdown = true;
		System.out.println("fordere threads zum terminieren auf...");
		for(RemoteMethodCallListener o: rmlcList){
			o.stop();
		}
		
		System.out.println("warte darauf dass alle threads terminieren...");
		for(Thread t: threadList){
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Alle listenerThreads beendet!");
		
	}

	
	
	
	
}
