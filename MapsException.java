//Autor:Dr in¿. Pawe³ Rogaliñski
//Edit: Wiktor Syska

package lab2.Program;

/*
 * Klasa PersonException jest klas¹ wyj¹tków dedykowan¹ do zg³aszania b³êdów 
 * wystêpuj¹cych przy operacjach na obiektach klasy Person.
 */
public class MapsException extends Exception {

	private static final long serialVersionUID = 1L;

	public MapsException(String message) {
		super(message);
	}
	
} // koniec klasy PersonException
