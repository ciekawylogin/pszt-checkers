package model;

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
}
