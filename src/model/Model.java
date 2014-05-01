package model;

public class Model {

	/**
	 * @return true wtedy i tylko wtedy, gdy jakikolwiek pionek jest zaznaczony
	 */
	public final boolean isAnyCheckerSelected() {
		// @TODO write me
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Przemieszcza zaznaczony pionek na pole o współrzędnych (target_x, target_y).
	 * 
	 * Możliwe warianty wykonania ruchu:
	 * + jeżeli jakiś pionek jest zaznaczony, a docelowe pole znajduje się 1 pole 
	 * 	 naprzód (po skosie) od obecnej pozycji pionka (zwykły ruch), to pionek 
	 *   przesuwa się na zadane pole, następuje zmiana kolejki, a pionek zostaje 
	 *   odznaczony
	 * + jeżeli jakiś pionek jest zaznaczony, a docelowe pole znajduje się 2 pola 
	 *   naprzód od niego i między polem docelowym a obecnym znajduje się pionek 
	 *   przeciwnika (bicie), wówczas pionek przesuwa się 2 na pole docelowe, a pionek 
	 *   pzreciwnika zostaje usunięty dodatkowo przy biciu funkcja musi sprawdzić, 
	 *   czy jest możliwe kolejne bicie; 
	 *   jeżeli nie, to:
	 * ++ następuje zmiana kolejki
	 * ++ pionek zostaje odznaczony
	 *   jeżeli tak, to:
	 * ++ nie następuje zmiana kolejki
	 * ++ pionek nie zostaje odznaczony
	 * ++ w następnej kolejce pionek ma "wymuszone bicie", tj. gracz 
	 *    musi wykonać bicie tym pionkiem
	 * + jeżeli nie zachodzi żaden z powyższych przypadków, to ruch jest uznawany 
	 *   za niepoprawny; funkcja rzuca wyjątek, nie zostają wprowadzone żadne zmiany 
	 *   w modelu
	 * 
	 * @throws RuntimeException jeżeli ruch jest niedozwolony
	 * @param target_x współrzędna x docelowego pola
	 * @param target_y współrzędna y docelowego pola
	 */
	public final void moveSelectedCheckerTo(int target_x, int target_y) {
		// @TODO write me
		throw new UnsupportedOperationException("Not yet implemented");
		
	}
	
	/**
	 * Odznacza zaznaczonego obecnie pionka. Jeżeli odznaczenie jest niedozwolone
	 * (taka sytuacja zachodzi gdy gracz ma wymuszone bicie), to rzuca wyjątek
	 * 
	 * @throws RuntimeException jeżeli gracz ma wymuszone bicie
	 * @throws RuntimeException jeżeli żaden pionek nie jest zaznaczony
	 */
	public final void unselectChecker() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sprawdza, czy na danej pozycji znajduje się zaznaczony pionek.
	 * @param x współrzędna x pozycji do sprawdzenia
	 * @param y współrzędna y pozycji do sprawdzenia
	 * @return true jeżelili na danej pozycji znajduje się pionek i jest on zaznaczony
	 * 		   false jeżeli na danej pozycji nie ma pionka lub pionek jest niezaznaczony
	 */
	public boolean isCheckerSelected(int x, int y) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sprawdza, czy na zadanej pozycji znajduje się pionek aktywnego gracza 
	 * (gracz jest aktywny = 'jest jego ruch').
	 * 
	 * @param x współrzędna x pozycji do sprawdzenia
	 * @param y współrzędna y pozycji do sprawdzenia
	 * @return true jeżeli na polu (x, y) znajduje się pionek aktywnego gracza.
	 */
	public boolean isCurrentPlayerCheckerOnPosition(int x, int y) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Zaznacza pionek na pozycji (x, y).
	 * 
	 * @param x współrzędna x pionka do zaznaczenia
	 * @param y współrzędna y pionka do zaznaczenia
	 * 
	 * @throws RuntimeException jeżeli na pozycji (x, y) nie ma pionka aktywnego gracza
	 * @throws RuntimeException jeżeli jakiś pionek jest już zaznaczony
	 */
	public void selectChecker(int x, int y) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
		
	}

	/**
	 * Rozpoczyna nową grę, tj. tworzy planszę, ustawia pionki i ustawia białego gracza
	 * jako aktywnego.
	 */
	public void startGame() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
		
	}

}
 
