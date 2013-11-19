package mware_lib;

import java.util.ArrayList;

public interface RemoteCall_I{
		
	public Object callMethod(String name, ArrayList<Object> params);
}
