package test;

import mware_lib.LocalNameService;
import mware_lib.NameService;

public class Testframe {
	
	public static void main(String args[]){
		NameService ns = new LocalNameService("141.22.89.212",14002);
		//ns.rebind("blabla", "name1");
	}

}
