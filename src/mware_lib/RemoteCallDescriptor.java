package mware_lib;

import java.io.Serializable;
import java.util.ArrayList;

public class RemoteCallDescriptor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3290977795824476953L;
	
	private String objName;
	private String method;
	private ArrayList<Object> params;
	
	public RemoteCallDescriptor(String objName, String method, Object ... parameters){
		params = new ArrayList<Object>();
		for (Object o: parameters){
			params.add(o);
		}
		this.objName = objName;
		this.method = method;
	}

	public String getObjName() {
		return objName;
	}

	public String getMethod() {
		return method;
	}

	public ArrayList<Object> getParams() {
		return params;
	}
	
	
}
