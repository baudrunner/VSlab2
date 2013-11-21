package mware_lib;

import bank_access.AccountImplBase;
import bank_access.AccountImplBase_Test;
import bank_access.OverdraftException;

public class Testframe {
	
	public static void main(String args[]){	
		Testframe tf = new Testframe();
		tf.runTest();
	}
	
	public void runTest(){
		AccountImplBase_Test testkonto = new AccountImplBase_Test();
		
		ObjectBroker objBroker = ObjectBroker.init("lab29",14009);
		NameService nameSvc = objBroker.getNameService();
		nameSvc.rebind(testkonto, "testKonto1");
		Object remoteKontoRaw = nameSvc.resolve("testKonto1");
		AccountImplBase remoteKonto = AccountImplBase.narrowCast(remoteKontoRaw);
		
		try {
			remoteKonto.transfer(1000);
		} catch (OverdraftException e1) {
			e1.printStackTrace();
		}
		
		TestRunnable adder   = new TestRunnable(remoteKonto, true, 2, 200, 0);
		Thread autoAddThread = new Thread(adder);
		
		TestRunnable subber  = new TestRunnable(remoteKonto, false, 4, 100, 0);
		Thread autoSubThread = new Thread(subber);
		
		TestRunnable adder2   = new TestRunnable(remoteKonto, true, 2, 200, 0);
		Thread autoAddThread2 = new Thread(adder2);
		
		TestRunnable subber2  = new TestRunnable(remoteKonto, false, 4, 100, 0);
		Thread autoSubThread2 = new Thread(subber2);
		
		autoSubThread.start();
		autoAddThread.start();
		autoSubThread2.start();
		autoAddThread2.start();
		
		try {
	
			autoSubThread.join();
			autoAddThread.join();
			autoSubThread2.join();
			autoAddThread2.join();
			
			System.out.println("###################\n\nKontostand am Ende: " + remoteKonto.getBalance());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class TestRunnable implements Runnable{
		
		AccountImplBase konto;
		boolean addMoney;
		double ammount; 
		int cnt; 
		int usleep;
		
		public TestRunnable(AccountImplBase konto, boolean addMoney, double ammount, int cnt, int usleep){
			this.konto = konto;
			this.addMoney = addMoney;
			this.ammount = ammount;
			this.cnt = cnt;
			this.usleep = usleep;
		}

		@Override
		public void run() {
			
			int i = 0;
			while(i < cnt){
				if(addMoney){
					try {
						konto.transfer(ammount);
						System.out.println("Kontostand: " + konto.getBalance() );
					} catch (OverdraftException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						konto.transfer(ammount*-1.0);
						System.out.println("Kontostand: " + konto.getBalance() );
					} catch (OverdraftException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
			}
		}
	}

}
