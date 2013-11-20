package mware_lib.skeletons;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import tcp_advanced.Connection;
import mware_lib.NameServerRecord;
import mware_lib.RemoteCallDescriptor;
import bank_access.AccountImplBase;
import bank_access.OverdraftException;

public class AccountImplBase_Skeleton extends AccountImplBase {
	
	NameServerRecord remoteObject; 
	
	public AccountImplBase_Skeleton(NameServerRecord remoteObject){
		
		this.remoteObject = remoteObject;
		
	}

	@Override
	public void transfer(double amount) throws OverdraftException {
		Connection conn = null;
		try {
			conn = new Connection(new Socket(remoteObject.getHostDescriptor().getAdress(), remoteObject.getHostDescriptor().getPort()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(amount);
		conn.send(new RemoteCallDescriptor(remoteObject.getName(), "transfer", params));
		Object returnVal = conn.receive();

	}

	@Override
	public double getBalance() {
		Connection conn = null;
		try {
			conn = new Connection(new Socket(remoteObject.getHostDescriptor().getAdress(), remoteObject.getHostDescriptor().getPort()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Object> params = new ArrayList<Object>();
		conn.send(new RemoteCallDescriptor(remoteObject.getName(), "getBalance", params));
		Object returnVal = conn.receive();
		return (double) returnVal;
	}

}
