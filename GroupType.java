/*	Autor: Wiktor Syska
 *  Indeks: 259123
 *  Data: Grudzieñ 2021
 */
package lab3.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;

import lab2.Program.Maps;
import lab2.Program.MapsException;

public enum GroupType
{
	VECTOR("Lista   (klasa Vector)"), 
	ARRAY_LIST("Lista   (klasa ArrayList)"), 
	LINKED_LIST("Lista   (klasa LinkedList)"),
	HASH_SET("Zbiór   (klasa HashSet)"),
	TREE_SET("Zbiór   (klasa TreeSet)");
	
	String typeName;

	private GroupType(String type_name)
	{
		typeName = type_name;
	}
	
	
	@Override
	public String toString() 
	{
		return typeName;
	}
	
	
	public static GroupType find(String type_name)
	{
		for(GroupType type : values())
		{
			if (type.typeName.equals(type_name))
			{
				return type;
			}
		}
		return null;
	}
	
	
	
	public Collection<Maps> createCollection() throws MapsException {
		switch (this) 
		{
		case VECTOR:      return new Vector<Maps>();
		case ARRAY_LIST:  return new ArrayList<Maps>();
		case HASH_SET:    return new HashSet<Maps>();
		case LINKED_LIST: return new LinkedList<Maps>();
		case TREE_SET:    return new TreeSet<Maps>();
		default:          throw new MapsException("Podany typ kolekcji nie zosta³ zaimplementowany.");
		}
	}
			
}  // koniec klasy enum GroupType

