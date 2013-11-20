package cash_access;

import mware_lib.NameServerRecord;
import mware_lib.skeletons.TransactionImplBase_Skeleton;

/*
 * Anmerkungen: Die Middlware kann in den ImplBase­Klassen weitere Methoden und Interfaces 
	implementieren. Die Anwendungen kennen und benutzen aber nur die oben definierten Klassen und Methoden
	Die Methode narrowCast soll ein Stellvertreterobjekt zu einer generischen 
	Objektreferenz liefern und ist jeweils von der Middleware zu implementieren.
 */

public abstract class TransactionImplBase {
	public abstract void deposit(String accountID, double amount) throws InvalidParamException;
	public abstract void withdraw(String accountID, double amount) throws InvalidParamException, OverdraftException;
	public abstract double getBalance(String accountID) throws InvalidParamException;
	public static TransactionImplBase narrowCast(Object rawObjectRef) {
		System.out.println("TransactionImplBase -> Narrowcast");
		return new TransactionImplBase_Skeleton((NameServerRecord)rawObjectRef);
	}
}