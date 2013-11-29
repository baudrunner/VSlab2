package bank_access;

import java.util.ArrayList;

import mware_lib.NameServerRecord;
import mware_lib.RemoteCall_I;

public class AccountImplBase_Test extends AccountImplBase implements RemoteCall_I{

	double saldo = 0;
	@Override
	public void transfer(double amount) throws OverdraftException {
		System.out.println("AccountImplBase_Test Methode transfer aufgerufen mit : " + amount);
		saldo = saldo + amount;
	}

	@Override
	public double getBalance() {
		System.out.println("AccountImplBase_Test Methode getBalance aufgerufen");
		return saldo;
	}
	
	public static AccountImplBase_Skeleton narrowCast(Object rawObjectRef) {
		return new AccountImplBase_Skeleton((NameServerRecord)rawObjectRef);
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
	}

}
