package mware_lib;

import java.util.ArrayList;


public class Testframe {
	
	public static void main(String args[]){
		//NameService ns = new LocalNameService("141.22.87.152",14002);
		
		
		String konto = "kontoObjekt";
		
		ObjectBroker objBroker = ObjectBroker.init("141.22.87.152",14002);
		NameService nameSvc = objBroker.getNameService();
		nameSvc.rebind((Object) konto, "ID:5");
		objBroker.shutDown();
		
		
		
		//ns.rebind("blabla", "name1");
	}

}
