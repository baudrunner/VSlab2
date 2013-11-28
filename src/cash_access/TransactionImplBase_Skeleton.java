package cash_access;

import name_service.NameServerRecord;
import mware_lib.RemoteCallDescriptor;
import mware_lib.RemoteCaller;

public class TransactionImplBase_Skeleton extends TransactionImplBase{
	
	NameServerRecord remoteObject; 
	public TransactionImplBase_Skeleton(NameServerRecord remoteObject){	
		this.remoteObject = remoteObject;
	}
	

	@Override
	public void deposit(String accountID, double amount) throws InvalidParamException {
		
		Object resu = RemoteCaller.callMethod(remoteObject.getHostDescriptor(), new RemoteCallDescriptor(remoteObject.getName(), "deposit", accountID, amount));
		
		if(resu instanceof InvalidParamException){
			throw (InvalidParamException)resu;
		}
	}

	@Override
	public void withdraw(String accountID, double amount) throws InvalidParamException, OverdraftException {
		
		Object resu = RemoteCaller.callMethod(remoteObject.getHostDescriptor(), new RemoteCallDescriptor(remoteObject.getName(), "withdraw", accountID, amount));
		
		if(resu instanceof InvalidParamException){
			throw (InvalidParamException)resu;
		} else if(resu instanceof OverdraftException){
			throw (OverdraftException)resu;
		}
		
	}

	@Override
	public double getBalance(String accountID) throws InvalidParamException {
		
		Object resu = RemoteCaller.callMethod(remoteObject.getHostDescriptor(), new RemoteCallDescriptor(remoteObject.getName(), "getBalance", accountID));
		
		if(resu instanceof InvalidParamException){
			throw (InvalidParamException)resu;
		}else{
			return (double)resu;
		}
		
	}

}
