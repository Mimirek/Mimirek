//Autor:Dr in�. Pawe� Rogali�ski
//Edit: Wiktor Syska

package lab2.Program;

/*
 * Klasa PersonException jest klas� wyj�tk�w dedykowan� do zg�aszania b��d�w 
 * wyst�puj�cych przy operacjach na obiektach klasy Person.
 */
public class MapsException extends Exception {

	private static final long serialVersionUID = 1L;

	public MapsException(String message) {
		super(message);
	}
	
} // koniec klasy PersonException
