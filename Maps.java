//Autor: Wiktor Syska
package lab2.Program;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;







public class Maps implements java.io.Serializable
{
	
	
	private static final long serialVersionUID = 1L;
	private String name;
	private int width;
	private int height;
	private int scale;
	private Types_of_maps type;
 
	
	public Maps(String Name) throws MapsException 
	{
		setName(Name);
		type = Types_of_maps.UNKNOWN;
	}
	
	public String getName() 
	{
		return name;
	}

	
	public void setName(String Name) throws MapsException
	{
		if ((Name == null) || Name.equals(""))
			throw new MapsException("Pole <Nazwa> musi byæ wype³nione.");
		this.name = Name;
	}
	
	public Types_of_maps getType()
	{
		return type;
	}

	
	public void setType(Types_of_maps type)
	{
		this.type = type;
	}
	
	
	public void setType(String type_of_map) throws MapsException 
	{
		if (type_of_map == null || type_of_map.equals(""))
		{  			this.type = Types_of_maps.UNKNOWN;
			return;
		}
		for(Types_of_maps type : Types_of_maps.values()){
			if (type.typeOfMap.equals(type_of_map)) {
				this.type = type;
				return;
			}
		}
		throw new MapsException("Nie ma takiego rodzaju mapy.");
	}
	
	
	public int getWidth() 
	{
		return width;
	}

	
	public void setWidth(int Width) throws MapsException 
	{
		if (Width <= 10 || Width >= 4000)
			throw new MapsException("Szerokoœæ mapy powinna mieœciæ siê przedziale [10 - 4000].");
		this.width = Width;
	}
	
	
	public int getHeight() 
	{
		return height;
	}

	
	public void setHeight(int Height) throws MapsException 
	{
		if (Height <= 10 || Height >= 4000)
			throw new MapsException("Wysokoœæ mapy powinna mieœciæ siê przedziale [10 - 4000].");
		this.height = Height;
	}	
	
	public int getScale() 
	{
		return scale;
	}

		
	public void setScale(int Scale) throws MapsException 
	{
		if (Scale <= 1 || Scale >= 2000000000)
			throw new MapsException("Maksymalna skala mapy dla tego programu wynosi 1:2000000000");
		this.scale = Scale;
		
	}
	
		
		
		public static void printToFile(PrintWriter writer, Maps map)
		{
			writer.println(map.name + " " + map.height + 
					" " + map.width + " " + map.scale + " " + map.type);
		}
		
		
		public static void printToFile(String file_name, Maps map) throws MapsException 
		{
			try (PrintWriter writer = new PrintWriter(file_name)) {
				printToFile(writer, map);
			} catch (FileNotFoundException e){
				throw new MapsException("Nie odnaleziono pliku " + file_name);
			}
		}
		
		
		public static Maps readFromFile(BufferedReader reader) throws MapsException
		{
			try 
			{
				String line = reader.readLine();
				String[] txt = line.split(" ");
				Maps map = new Maps(txt[0]);
				map.setHeight(Integer.parseInt(txt[1]));
				map.setWidth(Integer.parseInt(txt[2]));
				map.setScale(Integer.parseInt(txt[3]));
				map.setType(txt[4]);
				return map;
			} catch(IOException e){
				throw new MapsException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
			}	
		}
		

		public static Maps readFromFile(String file_name) throws MapsException {
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
				return Maps.readFromFile(reader);
			} catch (FileNotFoundException e){
				throw new MapsException("Nie odnaleziono pliku " + file_name);
			} catch(IOException e){
				throw new MapsException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
			}	
		}
		
		public static void main(String args[]) {
			System.out.print("Witaj");
			
		}

		public boolean equals() {
			
			return false;
		}

		
		public void compareTo(Maps m1, Maps m2) {
			
			
		}

		
		
		public interface Serializable {
			
			public String toStirng();
			public int hashCode();
			public boolean equals();
			public void compareTo(Maps m1, Maps m2);
		}

}
