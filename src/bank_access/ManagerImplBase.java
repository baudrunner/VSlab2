package bank_access;

import java.util.ArrayList;

import name_service.NameServerRecord;
import mware_lib.RemoteCall_I;

public abstract class ManagerImplBase implements RemoteCall_I {
	public abstract String createAccount(String owner, String branch);
	public static ManagerImplBase narrowCast(Object rawObjectRef) {
		return new ManagerImplBase_Skeleton((NameServerRecord)rawObjectRef);
	}
	
	@Override
	public Object callMethod(String name, ArrayList<Object> params) {
		if(name.equals("createAccount")){
			return createAccount((String)params.get(0), (String)params.get(1));
		}else{
			return new IllegalArgumentException("Only method 'String createAccount(String owner, String branch)' supported by ManagerImplBase)");
		}
		//return null;
	}
}