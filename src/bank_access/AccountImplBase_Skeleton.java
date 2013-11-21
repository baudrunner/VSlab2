package bank_access;

import mware_lib.NameServerRecord;
import mware_lib.RemoteCallDescriptor;
import mware_lib.RemoteCaller;

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
