package mware_lib;

import java.io.Serializable;

public class NameServerRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4013542117088915246L;
	HostDescriptor hostDescriptor;
	Class classObject;
	String name;
	
	public NameServerRecord(HostDescriptor hostDescriptor, String name, Class c){
		this.classObject = c;
		this.hostDescriptor = hostDescriptor;
		this.name = name;
	}
	
	public HostDescriptor getHostDescriptor(){
		return hostDescriptor;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Class getClassObject(){
		return classObject;
	}

}
