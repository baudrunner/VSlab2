package mware_lib.stubs;

import java.util.ArrayList;

import mware_lib.RemoteCall_I;
import bank_access.AccountImplBase;
import bank_access.OverdraftException;

public class AccountImplBase_Stub extends AccountImplBase implements RemoteCall_I {
	
	private AccountImplBase localObject;
	
	public AccountImplBase_Stub(AccountImplBase aib){
		localObject = aib;
	}

	@Override
	public Object callMethod(String name, ArrayList<Object> params) {
		
		if(name.equals("transfer")){
			try {
				transfer((double)params.get(0));
				return null;
			} catch (OverdraftException e) {
				e.printStackTrace();
				return e;
			}
		}else if(name.equals("getBalance")){
			return getBalance();
		}else{
			return new IllegalArgumentException("Only methods 'void transfer(double amount)' and 'double getBalance()' supported by AccountImplBase)");
		}
		//return null;
	}

	@Override
	public void transfer(double amount) throws OverdraftException {
		localObject.transfer(amount);
	}

	@Override
	public double getBalance() {
		return localObject.getBalance();
	}

}
