package common;

public class MoveMockup {
    
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
    public MoveMockup(final int startX, final int startY, final int endX, final int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }
    
    public int getEndX() {
        return endX;
    }
    
    public int getEndY() {
        return endY;
    }

}
