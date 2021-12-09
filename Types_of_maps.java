package lab2.Program;

// Typ wyliczeniowy zawieraj¹cy rodzaje map.

public enum Types_of_maps 
{
	UNKNOWN("-------"),
	TOPOGRAPHIC("Topograficzna"),
	POLITICAL("Polityczna"),
	PHYSICAL("Fizyczna"),
	CLIMATIC("Klimatyczna"),
	ECONOMIC("Ekonomiczna"),
	ROAD("Drogowa");
	
	String typeOfMap;

	private Types_of_maps(String type_of_map) 
	{
		typeOfMap = type_of_map;
	}

	
	@Override
	public String toString() 
	{
		return typeOfMap;
	}
	
}
	

