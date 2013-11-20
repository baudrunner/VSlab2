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
		Object resu = RemoteCaller.callMethod(remoteObject.getHostDescriptor(), new RemoteCallDescriptor(remoteObject.getName(), "transfer", amount));
		
		if(resu instanceof OverdraftException){
			throw (OverdraftException)resu;
		}
	}

	@Override
	public double getBalance() {
		Object resu = RemoteCaller.callMethod(remoteObject.getHostDescriptor(), new RemoteCallDescriptor(remoteObject.getName(), "getBalance"));
		return (double) resu;
	}

}
