package name_service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
	private ObjectInputStream In;
	private ObjectOutputStream Out;
	private InputStream inStream;
	private OutputStream outStream;
	private Socket socket;
	
	public Connection(Socket socket){
		this.socket = socket;
		try {
			outStream = socket.getOutputStream();
			Out = new ObjectOutputStream(outStream);
			Out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inStream = socket.getInputStream();
			In = new ObjectInputStream(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public Object receive() throws IOException{
		
		try {
			return In.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
//		catch (IOException e) {
//			e.printStackTrace();
//			throw e;
//		}
		
		return null;
	}
	
	public void send(Object message){
		try {
			Out.writeObject(message);
			Out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void close() throws IOException {
		In.close();
		Out.close();
	}
	
	public String getRemoteIP(){
		return socket.getInetAddress().toString().substring(1);//schneidet slash am anfang ab..
	}
	
	public int getRemotePort(){
		return socket.getPort();
	}
}
