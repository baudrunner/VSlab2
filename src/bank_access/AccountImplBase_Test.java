package bank_access;

public class AccountImplBase_Test extends AccountImplBase {

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

}
