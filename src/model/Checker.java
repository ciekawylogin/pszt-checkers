package model;

public class Checker {
    // typ pionka (zwykly lub krolowa)
    private CheckerType type;

    // kolor pionka (bialy lub czarny)
    private final CheckerColor color;

    // pozycja X pionka
    private int positionX;

    // pozycja Y pionka
    private int positionY;

    /**
     * Konstruktor.
     *
     * @param color - tworzony kolor
     * @param type - tworzony typ
     */
    Checker(final CheckerColor color, final CheckerType type) {
        this.type = type;
        this.color = color;
        positionX = -1;
        positionY = -1;
    }

    /**
     * Konstruktor.
     *
     * @param color - tworzony kolor
     */
    Checker(final CheckerColor color) {
        this.type = CheckerType.NORMAL;
        this.color = color;
        positionX = -1;
        positionY = -1;
    }

    /**
     * Konstruktor.
     *
     * @param position - pozycja na planszy
     * @param type - rodzaj pionka
     */
    public Checker(final int position, final CheckerType type) {
        this.type = CheckerType.NORMAL;
        this.color = CheckerColor.WHITE;
        positionX = position % 8;
        positionY = position / 8;
    }

    /**
     * Konstruktor kopiujacy.
     *
     * @param checkerToCopy
     */
    Checker(final Checker checkerToCopy) {
        positionX = checkerToCopy.positionX;
        positionY = checkerToCopy.positionY;
        color = checkerToCopy.color;
        type = checkerToCopy.type;
    }

    /**
     * Zwraca typ pionka
     *
     * @return
     */
    public CheckerType getType() {
        return type;
    }

    /**
     * Ustawienie pozycji pionka
     *
     * @param x - pozycja x
     * @param y - pozycja y
     */
    public void setPositionOnBoard(final int x, final int y) {
        positionX = x;
        positionY = y;
    }

    /**
     * Zwraca numer pola na ktorym znajduje sie pionek
     *
     * @return position on board
     */
    public int getPositionOnBoard() {
        return positionY * 8 + positionX;
    }

    /**
     * Zwraca pozycje x pionka
     *
     * @return positionX
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Zwraca pozycje y pionka
     *
     * @return positionY
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Zwraca kolor pionka.
     *
     * @return color
     */
    public CheckerColor getColor() {
        return color;
    }

    /**
     * Awans pionka na dame
     */
    public void promote() {
        if (color == CheckerColor.WHITE && positionY == 7) {
            type = CheckerType.QUEEN;
        } else if (color == CheckerColor.BLACK && positionY == 0) {
            type = CheckerType.QUEEN;
        }
    }
}
