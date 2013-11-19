package mware_lib;

import java.io.Serializable;

public class NameServerRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4013542117088915246L;
	HostDescriptor adress;
	String name;
	
	public NameServerRecord(HostDescriptor adress, String name){
		this.adress = adress;
		this.name = name;
	}
	
	public HostDescriptor getAdress(){
		return adress;
	}
	public String getName(){
		return this.name;
	}

}
