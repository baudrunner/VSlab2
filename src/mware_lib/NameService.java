package mware_lib;

public abstract class NameService { 	//- Schnittstelle zum Namensdienstes 
	
	/**	
	 *  Meldet ein Objekt (servant) beim Namensdienst an.
	 *  Eine eventuell schon vorhandene Objektreferenz gleichen Namens
	 *  soll überschrieben werden.
	 * @param servant
	 * @param name
	 */
	public abstract void rebind(Object servant, String name);

	
	
	/**
	 *  Liefert eine generische Objektreferenz zu einem Namen. (vgl. unten)
	 * @param name
	 * @return
	 */
	public abstract Object resolve(String name);
	
	}
