package mware_lib;

import java.io.Serializable;

public class HostDescriptor implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 234926084169402590L;
	private String adress;
	private int port;
	
	public HostDescriptor(String adress, int port){
		this.adress = adress;
		this.port = port;
	}
	
}
