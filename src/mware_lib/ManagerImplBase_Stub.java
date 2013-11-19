package mware_lib;

import java.util.ArrayList;

import bank_access.ManagerImplBase;
import bank_access.OverdraftException;

public class ManagerImplBase_Stub extends ManagerImplBase implements RemoteCall_I {
	
	private ManagerImplBase localObject;
	
	public ManagerImplBase_Stub(ManagerImplBase mib){
		localObject = mib;
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

	@Override
	public String createAccount(String owner, String branch) {
		return localObject.createAccount(owner, branch);
	}

}
