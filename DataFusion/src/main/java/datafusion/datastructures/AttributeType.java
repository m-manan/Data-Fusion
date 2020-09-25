package main.java.datafusion.datastructures;

/**
 * Rappresentazione della diverse tipologie di domini.
 * Ad esempio continuo o discreto.
 */
public enum AttributeType {
	CONTINUOUS, CATEGORICAL;

	public static AttributeType fromString(String str) {
		if (str.equalsIgnoreCase("continuous"))
			return CONTINUOUS;
		else if (str.equalsIgnoreCase("categorical"))
			return CATEGORICAL;
		else
			return null;
	}
}
