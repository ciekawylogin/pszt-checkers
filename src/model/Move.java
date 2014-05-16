package model;

/**
 * Klasa reprezentujaca pojedynczy ruch gracza
 *
 */
public class Move {
	
	/** wspolrzedna X poczatkowego pola pionka */
    int startX;
    
    /** wspolrzedna Y poczatkowego pola pionka */
    int startY;
    
    /** wspolrzedna X koncowego pola pionka */
    int endX;
    
    /** wspolrzedna Y koncowego pola pionka */
    int endY;

    /**
     * Konstruktor.
     * 
     * @param startX - wspolrzedna X poczatkowego pola pionka
     * @param startY - wspolrzedna Y poczatkowego pola pionka
     * @param endX - wspolrzedna X koncowego pola pionka
     * @param endY - wspolrzedna Y koncowego pola pionka
     */
    Move(final int startX, final int startY, final int endX, final int endY) {
    	
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
}
