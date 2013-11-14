package mware_lib;

public class ObjectBroker { //- Front-End der Middleware -
public static ObjectBroker init(String serviceHost,
int listenPort) { ... }
// Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt
// der Middleware aus Anwendersicht sein.
// Parameter: Host und Port, bei dem die Dienste (Namensdienst)
//kontaktiert werden sollen.
public NameService getNameService() {...}
// Liefert den Namensdienst (Stellvetreterobjekt).
public void shutDown() {...}
// Beendet die Benutzung der Middleware in dieser Anwendung.
}