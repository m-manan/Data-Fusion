package main.java.datafusion.datastructures;

/**
 * Rappresentazione dei possibili domini che un attributo può assumere.
 * Ad esempio String, Float, ecc.
 */
public enum AttributeDataType {
	FLOAT;

	public static AttributeDataType fromString(String str) {
		if (str.equalsIgnoreCase("float"))
			return FLOAT;
		else
			return null;
	}
}
