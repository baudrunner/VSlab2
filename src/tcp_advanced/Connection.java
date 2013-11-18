package tcp_advanced;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
	private ObjectInputStream In;
	private ObjectOutputStream Out;
	
	public Connection(Socket socket) throws IOException {
		In = new ObjectInputStream(socket.getInputStream());
		Out = new ObjectOutputStream(socket.getOutputStream());		
	}
	
	public Object receive(){
		
		try {
			return In.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void send(Object message) throws IOException {
		Out.writeObject(message);
	}
	
	public void close() throws IOException {
		In.close();
		Out.close();
	}
}
