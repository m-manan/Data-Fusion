package main.java.datafusion.datastructures;

import java.lang.String;

/** 
 * Un attributo è una proprietà di un record (ad esempio una colonna nel caso relazionale).
 * Questa classe rappresenta un attributo generico con un nome ed un valore,
 * inoltre tiene traccia del dominio di tale attributo. 
 * 
 */
@SuppressWarnings("rawtypes")
public class Attribute implements Comparable {

	/**
	 * Nome dell'attributo (identificatore)
	 */
	protected String name;

	/**
	 * Valore "grezzo" dell'attributo
	 */
	protected String rawValue;

	/**
	 * Dominio dell'attributo (Strings, Floats, ecc.)
	 */
	protected AttributeDataType dataType;

	/**
	 * Dimensione del dominio (categorical o continuous)
	 */
	protected AttributeType type;

	/**
	 * Costruttore di default (imposta nome e valore)
	 * 
	 * @param name     - nome dell'attributo
	 * @param rawValue - valore dell'attributo
	 */
	public Attribute(String name, String rawValue) {
		this.name = name;
		this.rawValue = rawValue;
	}

	/**
	 * Compara due attributi, gli attributi con lo stesso nome sono ordinati in base al valore,
	 * attributi con nomi diversi sono ordinati in base al loro nome
	 * 
	 * @param o 	- l'attributo con cui effettuare il confronto
	 * @return 		- ordine parziale degli attributi
	 */
	@Override
	public int compareTo(Object o) {
		if (o instanceof Attribute) {
			Attribute other = (Attribute) o;
			if (this.name.equals(other.getName()))
				return this.rawValue.compareTo(other.getRawValue());
			else
				return this.name.compareTo(other.getName());
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Ritorna il nome dell'attributo
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Ritorna il valore dell'attributo
	 * 
	 * @return rawValue
	 */
	public String getRawValue() {
		return rawValue;
	}

	/**
	 * Ritorna un hashcode dell'attributo
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		return (this.name + this.rawValue).hashCode();
	}

	/**
	 * Controlla l'uguaglianza tra due attributi.
	 * Due attributi sono uguali se hanno stesso nome e stesso valore
	 * 
	 * @param obj 	- l'attributo con cui effettuare il confronto
	 * @return true sse i due attributi sono uguali
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Attribute) && this.name.equals(((Attribute) obj).getName())
				&& this.rawValue.equals(((Attribute) obj).getRawValue())
				&& this.dataType.equals(((Attribute) obj).getDataType());
	}

	/**
	 * Ritorno il dominio dell'attributo
	 * 
	 * @return dataType
	 */
	public AttributeDataType getDataType() {
		return dataType;
	}

	/**
	 * Ritorna la "dimensione" del dominio
	 * 
	 * @return type
	 */
	public AttributeType getType() {
		return type;
	}

	/**
	 * Dato un gruppo di attributi, controlla se sono tutti dello stesso tipo, 
	 * in tal caso ritorna il tipo, altrimenti ritorna null.
	 * 
	 * @param attributes 	- Il gruppo di attributi
	 * @return attributeDataType
	 */
	public static AttributeDataType getDataType(Iterable<Attribute> attributes) {
		boolean first = true;
		AttributeDataType attributeDataType = null;
		for (Attribute a : attributes) {
			AttributeDataType next = a.getDataType();
			if (first)
				attributeDataType = next;
			else if (!next.equals(attributeDataType)) {
				System.err.println(
						"[Attribute.getDataType] ERROR: the collection of attributes passed into getDataType do not have a common data type.");
				return null;
			}
			first = false;
		}
		return attributeDataType;
	}

	/**
	 * Dato un gruppo di attributi, controlla se hanno tutti domini dello stesso tipo, 
	 * in tal caso ritorna il tipo, altrimenti ritorna null.
	 * 
	 * @param attributes 	- Il gruppo di attributi
	 * @return attributeType
	 */
	public static AttributeType getType(Iterable<Attribute> attributes) {
		boolean first = true;
		AttributeType attributeType = null;
		for (Attribute a : attributes) {
			AttributeType next = a.getType();
			if (first)
				attributeType = next;
			else if (!next.equals(attributeType)) {
				System.err.println(
						"[Attribute.getType] ERROR: the collection of attributes passed into getType do not have a common data type.");
				return null;
			}
			first = false;
		}
		return attributeType;
	}
}
