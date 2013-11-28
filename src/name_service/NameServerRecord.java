package name_service;

import java.io.Serializable;

public class NameServerRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4013542117088915246L;
	HostDescriptor hostDescriptor;
	String name;
	
	public NameServerRecord(HostDescriptor hostDescriptor, String name){
		this.hostDescriptor = hostDescriptor;
		this.name = name;
	}
	
	public HostDescriptor getHostDescriptor(){
		return hostDescriptor;
	}
	
	public String getName(){
		return this.name;
	}

}
