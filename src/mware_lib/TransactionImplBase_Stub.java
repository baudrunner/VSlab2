package mware_lib;

import java.util.ArrayList;

import bank_access.ManagerImplBase;
import cash_access.InvalidParamException;
import cash_access.OverdraftException;
import cash_access.TransactionImplBase;

public class TransactionImplBase_Stub extends TransactionImplBase implements RemoteCall_I {

	private TransactionImplBase localObject;
	
	public TransactionImplBase_Stub(TransactionImplBase tib){
		localObject = tib;
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

	@Override
	public void deposit(String accountID, double amount) throws InvalidParamException {
		localObject.deposit(accountID, amount);
	}

	@Override
	public void withdraw(String accountID, double amount) throws InvalidParamException, OverdraftException {
		localObject.withdraw(accountID, amount);
	}

	@Override
	public double getBalance(String accountID) throws InvalidParamException {
		return localObject.getBalance(accountID);
	}

}
