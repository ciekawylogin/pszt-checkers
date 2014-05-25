package model;

import java.util.ArrayList;

import common.Coordinate;

/**
 * Klasa reprezentujaca pojedynczy ruch gracza
 */
class Move {
    // wspolrzedna X poczatkowego pola pionka
    private int startX;

    // wspolrzedna Y poczatkowego pola pionka
    private int startY;

    // wspolrzedna X koncowego pola pionka
    private int endX;

    // wspolrzedna Y koncowego pola pionka
    private int endY;
    
    // ktory gracz wykonal ruch? (-1 = brak danych i nieistotne)
    private int player;

    /// Pozycje zbitych pionkow (o ile jakies byly zbite)
    ArrayList<Coordinate> capturedCheckers;
    
    /// Pozycje zbitych damek (o ile jakies byly zbite)
    ArrayList<Coordinate> capturedQueens;

    /**
     * Konstruktor.
     *
     * @param startX - wspolrzedna X poczatkowego pola pionka
     * @param startY - wspolrzedna Y poczatkowego pola pionka
     * @param endX - wspolrzedna X koncowego pola pionka
     * @param endY - wspolrzedna Y koncowego pola pionka
     * @param player 
     * @param capturedCheckers
     * @param capturedQueens
     */
    Move(final int startX, final int startY, final int endX, final int endY, int player, ArrayList<Coordinate> capturedCheckers,  ArrayList<Coordinate> capturedQueens) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.player = player;
        this.capturedCheckers = capturedCheckers;
        this.capturedQueens = capturedQueens;
    }
    
    /**
     * Konstruktor
     * @param startCoordinate - wspolrzedne polozenia poczatkowego
     * @param endCoordinate - wspolrzedne polozenia koncowego
     */
    Move(final Coordinate startCoordinate, final Coordinate endCoordinate) {
        startX = startCoordinate.getX();
        startY = startCoordinate.getY();
        endX = endCoordinate.getX();
        endY = endCoordinate.getY();
        player = -1;
    }

    Move(final int startX, final int startY, final int endX, final int endY, int player) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.player = player;
    }

	int getStartX() {
        return startX;
    }

    int getStartY() {
        return startY;
    }
    
    int getEndX() {
        return endX;
    }
    
    int getEndY() {
        return endY;
    }
    
    public Move reverse()
    {
    	return new Move(endX, endY, startX, startY, player);
    }
    
    public String toString() {
    	return "move: ("+startX+", "+startY+") -> ("+endX+", "+endY+")\n";
    }

	int getPlayer() {
		return player;
	}

	private void setPlayer(int player) {
		this.player = player;
	}
}
