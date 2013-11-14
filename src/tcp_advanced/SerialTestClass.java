package tcp_advanced;

import java.io.Serializable;
import java.util.Date;;

public class SerialTestClass implements Serializable{
	
	private static final long serialVersionUID = -909135063379965088L;
	
	public int i;
	public long l;
	public String s;
	public Date timestamp;
	
	public SerialTestClass(int i, long l, String s, Date timestamp) {
		super();
		this.i = i;
		this.l = l;
		this.s = s;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "SerialTestClass [i=" + i + ", l=" + l + ", s=" + s
				+ ", timestamp=" + timestamp + "]";
	}
	
	
	
}
