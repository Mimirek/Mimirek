/*	Autor: Wiktor Syska
 *  Indeks: 259123
 *  Data: Grudzieñ 2021
 */

package lab3.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import lab2.Program.Maps;
import lab2.Program.MapsException;

public class GroupOfMaps implements java.io.Serializable
{

private static final long serialVersionUID = 1L;

private String name;
private GroupType type;
private Collection<Maps> collection;


public GroupOfMaps(GroupType type, String name) throws MapsException
	{
		setName(name);
		if (type == null)
		{
			throw new MapsException("Nieprawid³owy typ kolekcji.");
		}
		this.type = type;
		collection = this.type.createCollection();
	}


	public GroupOfMaps(String type_name, String name) throws MapsException
	{
		setName(name);
		GroupType type = GroupType.find(type_name);
		if (type == null)
		{
			throw new MapsException("Nieprawid³owy typ kolekcji.");
		}
		this.type = type;
		collection = this.type.createCollection();
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public void setName(String name) throws MapsException
	{
		if ((name == null) || name.equals(""))
			throw new MapsException("Nazwa grupy musi byæ okreœlona.");
		this.name = name;
	}
	
	
	public GroupType getType()
	{
		return type;
	}

	
	public void setType(GroupType type) throws MapsException 
	{
		if (type == null)
		{
			throw new MapsException("Typ kolekcji musi byæ okreœlny.");
		}
		if (this.type == type)
			return;
		Collection<Maps> oldCollection = collection;
		collection = type.createCollection();
		this.type = type;
		for (Maps maps : oldCollection)
			collection.add(maps);
	}
	
	
	public void setType(String type_name) throws MapsException
	{
		for(GroupType type : GroupType.values())
		{
			if (type.toString().equals(type_name))
			{
				setType(type);
				return;
			}
		}
		throw new MapsException("Nie ma takiego typu kolekcji.");
	}
	
	
	public boolean add(Maps a)
	{
		return collection.add(a);
	}
	
	public Iterator<Maps> interator()
	{
		return collection.iterator();
	}
	
	public int size()
	{
		return collection.size();
	}
	
	
	public void sortName() throws MapsException
	{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
		{
			throw new MapsException("Kolekcje typu SET nie mog¹ byæ sortowane."); 
		}
		Collections.sort((List<Maps>) collection, new Comparator<Maps>() {
			
			@Override
			public int compare(Maps m1, Maps m2)
			{
				int last = m1.getName().compareTo(m2.getName());
		        return last;
			}    
		});
	}
	
	public void sortWidth() throws MapsException
	{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
		{
			throw new MapsException("Kolekcje typu Set nie mog¹ byæ stosowane.");
		}
		Collections.sort((List<Maps>) collection, new Comparator<Maps>()
			{
				@Override
				public int compare(Maps m1, Maps m2)
				{
					if (m1.getWidth() < m2.getWidth())
						return -1;
					if (m1.getWidth() > m2.getWidth())
						return 1;
					return 0;
				}
			});
	}
	
	public void sortHeight() throws MapsException
	{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
		{
			throw new MapsException("Kolekcje typt SET nie mog¹ byæ sortowanie");
		}
		Collections.sort((List<Maps>) collection, new Comparator<Maps>()
			{
				@Override
				public int compare(Maps m1, Maps m2) {
					if (m1.getHeight() < m2.getHeight())
						return -1;
					if (m1.getHeight() > m2.getHeight())
						return 1;
					return 0;
				}
	
			});
	}
	
	public void sortType() throws MapsException  
	{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
		{
			throw new MapsException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		}
		Collections.sort((List<Maps>) collection, new Comparator<Maps>()
				{
					@Override
					public int compare(Maps m1, Maps m2)
					{
						return m1.getType().toString().compareTo(m2.getType().toString());
					}
				});
	}
	@Override 
	public String toString()
	{
		return name + "  [" + type + "]";
	}
	
	
	public static void printToFile(PrintWriter writer, GroupOfMaps group)
	{
		writer.println(group.getName());
		writer.println(group.getType());
		for (Maps person : group.collection)
			Maps.printToFile(writer, person);
	}
	
	
	public static void printToFile(String file_name, GroupOfMaps group) throws MapsException 
	{
		try (PrintWriter writer = new PrintWriter(file_name)) {
			printToFile(writer, group);
		} catch (FileNotFoundException e){
			throw new MapsException("Nie odnaleziono pliku " + file_name);
		}
	}
	
	
	public static GroupOfMaps readFromFile(BufferedReader reader) throws MapsException
	{
		try 
		{
			String group_name = reader.readLine();
			String type_name = reader.readLine();
			GroupOfMaps groupOfMaps = new GroupOfMaps(type_name, group_name);

			Maps maps;
			while((maps = Maps.readFromFile(reader)) != null)
				groupOfMaps.collection.add(maps);
			return groupOfMaps;
		} catch(IOException e)
		
		{
			throw new MapsException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}	
	}
	
	
	public static GroupOfMaps readFromFile(String file_name) throws MapsException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
			return GroupOfMaps.readFromFile(reader);
		} catch (FileNotFoundException e){
			throw new MapsException("Nie odnaleziono pliku " + file_name);
		} catch(IOException e){
			throw new MapsException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}	
	}
	
	
}



