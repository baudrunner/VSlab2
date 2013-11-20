package mware_lib.skeletons;

import mware_lib.NameServerRecord;
import mware_lib.RemoteCallDescriptor;
import bank_access.ManagerImplBase;
import bank_access.OverdraftException;

public class ManagerImplBase_Skeleton extends ManagerImplBase {
	NameServerRecord remoteObject; 
	
	public ManagerImplBase_Skeleton(NameServerRecord remoteObject){
		this.remoteObject = remoteObject;
	}
	@Override
	public String createAccount(String owner, String branch) {
		
		Object resu = RemoteCaller.callMethod(remoteObject.getHostDescriptor(), new RemoteCallDescriptor(remoteObject.getName(), "createAccount", owner, branch));
		return (String)resu;
	}

}
