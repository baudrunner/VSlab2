package mware_lib;

import java.util.ArrayList;

import bank_access.AccountImplBase;


public class Testframe {
	
	public static void main(String args[]){
		//NameService ns = new LocalNameService("141.22.87.152",14002);
		
		
		String konto = "kontoObjekt";
		
		ObjectBroker objBroker = ObjectBroker.init("localhost",14002);
		NameService nameSvc = objBroker.getNameService();
		nameSvc.rebind((Object)new HostDescriptor("aaaadresse", 1234), "ID:5");
		objBroker.shutDown();
		
		
//		ObjectBroker objBroker2 = ObjectBroker.init("localhost",14002);
//		NameService nameSvc2 = objBroker.getNameService();
//		Object rawObjRef = nameSvc.resolve(KontoID);
//		AccountImplBase konto = AccountImplBase.narrowCast(rawObjRef);
//		//liefert spezialisiertes Stellvertreterobjekt
//		double b = konto.getBalance();

		//ns.rebind("blabla", "name1");
	}

}
