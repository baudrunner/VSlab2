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
	Connection connection = null;

	public LocalNameService(String serviceHost, int listenPort) {
		
		NameServerAdress = serviceHost;
		NameServerPort = listenPort;
		try {
			servSocket = new ServerSocket(0);
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
		
		if(servant instanceof AccountImplBase){
			remoteObjects.put(name, new AccountImplBase_Stub((AccountImplBase)servant));
		}else if(servant instanceof ManagerImplBase){
			remoteObjects.put(name, new ManagerImplBase_Stub((ManagerImplBase)servant));
		}else if(servant instanceof TransactionImplBase){
			remoteObjects.put(name, new TransactionImplBase_Stub((TransactionImplBase)servant));
		}
		
		connection.send(record);
	}

	@Override
	public Object resolve(String name) {
		
		System.out.println("Resolve-Methode aufgerufen");
		connection.send(name);
		System.out.println("Anfrage für Objekt gesendet");
		
		NameServerRecord targetRecord = null;
		try {
			targetRecord = (NameServerRecord)connection.receive();
		} catch (IOException e) {
			e.printStackTrace();
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
				System.out.println("LocalNameService: waiting for remote method call...");
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
