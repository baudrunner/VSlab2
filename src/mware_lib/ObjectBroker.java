package mware_lib;

public class ObjectBroker { //- Front-End der Middleware -
	

	private  String serviceHost;
	private  int listenPort;
	private static ObjectBroker instance = null;
	LocalNameService localNameService = null;
	
	private ObjectBroker (String serviceHost,int listenPort) {
		this.serviceHost = serviceHost;
		this.listenPort = listenPort;
	}

	/**
	 *  Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt
	 * der Middleware aus Anwendersicht sein.
	 * Parameter: Host und Port, bei dem die Dienste (Namensdienst)
	 * kontaktiert werden sollen.
	 */
	public static ObjectBroker init(String serviceHost,int listenPort) {
		if(instance == null){
			instance = new ObjectBroker(serviceHost, listenPort);
		}
		return instance;
	}
	
	public NameService getNameService() {// Liefert den Namensdienst (Stellvetreterobjekt).
		if(localNameService == null){
			localNameService = new LocalNameService(serviceHost, listenPort);
		}
		return localNameService;
	}


	public void shutDown() {
		
	}
	// Beendet die Benutzung der Middleware in dieser Anwendung.

}