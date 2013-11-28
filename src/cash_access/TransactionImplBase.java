package cash_access;

import java.util.ArrayList;

import name_service.NameServerRecord;
import mware_lib.RemoteCall_I;

/*
 * Anmerkungen: Die Middlware kann in den ImplBase­Klassen weitere Methoden und Interfaces 
	implementieren. Die Anwendungen kennen und benutzen aber nur die oben definierten Klassen und Methoden
	Die Methode narrowCast soll ein Stellvertreterobjekt zu einer generischen 
	Objektreferenz liefern und ist jeweils von der Middleware zu implementieren.
 */

public abstract class TransactionImplBase implements RemoteCall_I{
	public abstract void deposit(String accountID, double amount) throws InvalidParamException;
	public abstract void withdraw(String accountID, double amount) throws InvalidParamException, OverdraftException;
	public abstract double getBalance(String accountID) throws InvalidParamException;
	public static TransactionImplBase narrowCast(Object rawObjectRef) {
		System.out.println("TransactionImplBase -> Narrowcast");
		return new TransactionImplBase_Skeleton((NameServerRecord)rawObjectRef);
	}
	
	@Override
	public Object callMethod(String name, ArrayList<Object> params) {
		if(name.equals("deposit")){
			try {
				deposit((String)params.get(0), (double)params.get(1));
				return null;
			} catch (InvalidParamException e) {
				e.printStackTrace();
				return e;
			}
		}else if(name.equals("withdraw")){
			try {
				withdraw((String)params.get(0),(double)params.get(1));
				return null;
			} catch (InvalidParamException e) {
				e.printStackTrace();
				return e;
			} catch (OverdraftException e) {
				e.printStackTrace();
				return e;
			}
		}else if(name.equals("getBalance")){
			try {
				return getBalance((String)params.get(0));
			} catch (InvalidParamException e) {
				e.printStackTrace();
				return e;
			}
		}else{
			return new IllegalArgumentException("Only methods 'void deposit(String accountID, double amount)' , 'void withdraw(String accountID, double amount)' and 'double getBalance(String accountID)' supported by TransactionImplBase)");
		}
		//return null;
	}
}