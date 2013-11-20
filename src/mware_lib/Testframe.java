package mware_lib;

import java.util.ArrayList;

import bank_access.AccountImplBase;
import bank_access.AccountImplBase_Test;
import bank_access.OverdraftException;


public class Testframe {
	
	public static void main(String args[]){
		//NameService ns = new LocalNameService("141.22.87.152",14002);
		
		
		String konto = "kontoObjekt";
		
//		ObjectBroker objBroker = ObjectBroker.init("localhost",14002);
//		NameService nameSvc = objBroker.getNameService();
//		nameSvc.rebind((Object)new HostDescriptor("aaaadresse", 1234), "ID:5");
//		objBroker.shutDown();
		
		
		ObjectBroker objBroker2 = ObjectBroker.init("141.22.82.8",14009);
		NameService nameSvc2 = objBroker2.getNameService();
//		nameSvc2.rebind((Object)new HostDescriptor("aaaadresse", 1234), "34");
//		Object rawObjRef = nameSvc2.resolve("34");
//		AccountImplBase konto2 = AccountImplBase.narrowCast(rawObjRef); //liefert spezialisiertes Stellvertreterobjekt
		AccountImplBase_Test newtest = new AccountImplBase_Test();
		nameSvc2.rebind(newtest, "nameUnseresTollenErstenTestobjekts");
		Object rawObjRef = nameSvc2.resolve("nameUnseresTollenErstenTestobjekts");
		System.out.println( "Name des Objekts auf dem Server: "+((NameServerRecord)rawObjRef).getName());
		System.out.println( "HostDescriptor des Objekts auf dem Server: "+((NameServerRecord)rawObjRef).getHostDescriptor());
		AccountImplBase remoteKonto = AccountImplBase.narrowCast(rawObjRef); //liefert spezialisiertes Stellvertreterobjekt
		try {
			remoteKonto.transfer(50);
		} catch (OverdraftException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Aktueller Kontostand : " + remoteKonto.getBalance());
	//	double b = konto.getBalance();

		//ns.rebind("blabla", "name1");
	}

}
