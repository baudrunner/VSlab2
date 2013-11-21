package mware_lib.skeletons;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import tcp_advanced.Connection;
import mware_lib.HostDescriptor;
import mware_lib.RemoteCallDescriptor;

public class RemoteCaller {

	public static Object callMethod(HostDescriptor hd, RemoteCallDescriptor rcd){
		
		Connection conn = null;
		try {
			conn = new Connection(new Socket(hd.getAdress(), hd.getPort()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		conn.send(rcd);
		
		try {
			return conn.receive();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
