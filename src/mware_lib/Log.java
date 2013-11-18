package mware_lib;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {

	FileWriter writer;
	File logfile;
	private static Log instance;
	
	private Log() throws IOException{
		logfile = new File("lab3_log" + new Date()+ ".txt");
		writer = new FileWriter(logfile ,true);// falls die Datei bereits existiert
        									   // werden die Bytes an das Ende der Datei geschrieben
	}
	  
	public void append(String txt){
    
	     try {
	       // Text wird in den Stream geschrieben
	       writer.write(new Date() + txt);
	       
	       // Platformunabhängiger Zeilenumbruch
	       writer.write(System.getProperty("line.separator"));
   
	       writer.flush();
	      
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	
	public void quitLogger(){
		
		// Schliesst den Stream
	    try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Log getInstance(){
		if(Log.instance == null){
			try {
				instance = new Log();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return instance;
	}

}