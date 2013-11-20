package bank_access;

import mware_lib.NameServerRecord;
import mware_lib.skeletons.AccountImplBase_Skeleton;

public abstract class AccountImplBase {
	public abstract void transfer(double amount) throws OverdraftException;
	public abstract double getBalance();
	public static AccountImplBase narrowCast(Object rawObjectRef) {
		return new AccountImplBase_Skeleton((NameServerRecord)rawObjectRef);
	}
}