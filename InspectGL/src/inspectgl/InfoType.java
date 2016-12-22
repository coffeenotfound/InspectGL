package inspectgl;

import java.util.Vector;

public class InfoType {
	private static final Vector<InfoType> registeredTypes = new Vector<InfoType>();
	
	private final String name;
	
	public InfoType(String parName) {
		name = parName;
	}
	
	public boolean isSame(String parType) {
		return name.equalsIgnoreCase(parType);
	}
	
	/** returns a mutable list of registered info types. the list must not be changed */
	public static Vector<InfoType> getInfoTypes() {
		return registeredTypes;
	}
	
	public static InfoType parse(String parInfoType) {
		for(int i = 0; i < registeredTypes.size(); i++) {
			if(registeredTypes.get(i).isSame(parInfoType)) return registeredTypes.get(i);
		}
		return null;
	}
	
	public static void register(InfoType parType) {
		registeredTypes.add(parType);
	}
}
