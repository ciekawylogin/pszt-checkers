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
     * @param position - pozycja na planszy
     * @param type - rodzaj pionka
     */
    Checker(final int positionX, final int positionY, final CheckerColor color, final CheckerType type) {
        this.type = type;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
    }
    
    /**
     * Konstruktor.
     *
     * @param position - pozycja na planszy
     * @param type - rodzaj pionka
     */
    Checker(final int positionX, final int positionY, final CheckerColor color) {
        this.type = CheckerType.NORMAL;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
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
     * Zwraca pozycje x pionka
     *
     * @return positionX
     */
    int getX() {
        return positionX;
    }

    /**
     * Zwraca pozycje y pionka
     *
     * @return positionY
     */
    int getY() {
        return positionY;
    }

    /**
     * Zwraca kolor pionka.
     *
     * @return color
     */
    CheckerColor getColor() {
        return color;
    }

    /**
     * Awans pionka na dame
     */
    void promote() {
        if (color == CheckerColor.WHITE && positionY == 7) {
            type = CheckerType.QUEEN;
        } else if (color == CheckerColor.BLACK && positionY == 0) {
            type = CheckerType.QUEEN;
        }
    }

    /**
     * cofniecie awansu
     */
	public void unpromote() {
		if(type == CheckerType.NORMAL)
		{
			throw new RuntimeException();
		}
		type = CheckerType.NORMAL;
	}
}
