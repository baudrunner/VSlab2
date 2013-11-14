package mware_lib;

public abstract class NameService {
	//- Schnittstelle zum Namensdienstes -
	public abstract void rebind(Object servant, String name);
	// Meldet ein Objekt (servant) beim Namensdienst an.
	// Eine eventuell schon vorhandene Objektreferenz gleichen Namens
	// soll überschrieben werden.
	public abstract Object resolve(String name);
	// Liefert eine generische Objektreferenz zu einem Namen. (vgl. unten)
	}
