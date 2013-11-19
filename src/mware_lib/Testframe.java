package mware_lib;

import java.util.ArrayList;

import bank_access.AccountImplBase;


public class Testframe {
	
	public static void main(String args[]){
		//NameService ns = new LocalNameService("141.22.87.152",14002);
		
		
		String konto = "kontoObjekt";
		
//		ObjectBroker objBroker = ObjectBroker.init("localhost",14002);
//		NameService nameSvc = objBroker.getNameService();
//		nameSvc.rebind((Object)new HostDescriptor("aaaadresse", 1234), "ID:5");
//		objBroker.shutDown();
		
		
		ObjectBroker objBroker2 = ObjectBroker.init("localhost",14002);
		NameService nameSvc2 = objBroker2.getNameService();
		nameSvc2.rebind((Object)new HostDescriptor("aaaadresse", 1234), "34");
		Object rawObjRef = nameSvc2.resolve("34");
		AccountImplBase konto2 = AccountImplBase.narrowCast(rawObjRef);
		
		
		//liefert spezialisiertes Stellvertreterobjekt
	//	double b = konto.getBalance();

		//ns.rebind("blabla", "name1");
	}

}
