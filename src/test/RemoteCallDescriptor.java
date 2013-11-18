package test;

import java.io.Serializable;
import java.util.ArrayList;

public class RemoteCallDescriptor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3290977795824476953L;
	
	public String objName;
	public String method;
	public ArrayList<Object> params;
	
	public RemoteCallDescriptor(String objName, String method, ArrayList<Object> params){
		this.objName = objName;
		this.method = method;
		this.params = params;
	}
}
