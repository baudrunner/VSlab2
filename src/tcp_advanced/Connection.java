package tcp_advanced;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
	private ObjectInputStream In;
	private ObjectOutputStream Out;
	
	public Connection(Socket socket) throws IOException {
		In = new ObjectInputStream(socket.getInputStream());
		Out = new ObjectOutputStream(socket.getOutputStream());		
	}
	
	public Object receive() throws IOException, ClassNotFoundException {
		return In.readObject();
	}
	
	public void send(Object message) throws IOException {
		Out.writeObject(message);
	}
	
	public void close() throws IOException {
		In.close();
		Out.close();
	}
}
