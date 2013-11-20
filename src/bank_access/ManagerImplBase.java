package bank_access;

import mware_lib.NameServerRecord;
import mware_lib.skeletons.ManagerImplBase_Skeleton;

public abstract class ManagerImplBase {
	public abstract String createAccount(String owner, String branch);
	public static ManagerImplBase narrowCast(Object rawObjectRef) {
		return new ManagerImplBase_Skeleton((NameServerRecord)rawObjectRef);
	}
}