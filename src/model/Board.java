package model;

import static model.CheckerColor.WHITE;

import model.Field;
import model.CheckerColor;
import model.CheckerType;

public class Board {
    // pola planszy
    Field fields[][];

    // @see #Model.BOARD_SIZE
    final int SIZE;

    // @see #Model.INITIAL_CHECKERS_ROWS
    final int INIT_ROWS;

    /**
     * Konstruktor.
     *
     * @param size - ilosc kolumn
     * @param init_rows - ilosc wierszy
     */
    Board(final int size, final int init_rows) {
        this.SIZE = size;
        this.INIT_ROWS = init_rows;
        fields = new Field[SIZE][SIZE];

        for(int i=0; i<8; ++i) {
            for(int j=0; j<8; ++j) {
                fields[i][j] = new Field();
            }
        }
    }

    /**
     * Rozstawia pionki na planszy
     */
    public void setUp() {
        // zmienna pomocnicza
        Field field;

        for(int x=0; x<SIZE; ++x) {
            for(int y=0; y<SIZE; ++y) {
                field = fields[x][y];
                if(isWhiteStartingPosition(x, y)) {
                    field.setChecker(new Checker(CheckerColor.WHITE, CheckerType.NORMAL));
                } else if(isBlackStartingPosition(x, y)) {
                    field.setChecker(new Checker(CheckerColor.BLACK, CheckerType.NORMAL));
                }
            }
        }
    }

    /**
     * Sprawdza, czy dana pozycja jest pozycja startowa dla bialego pionka.
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     * @return true jezeli na polu na poczatku gry ma stac bialy pionek
     */
    private boolean isWhiteStartingPosition(final int x, final int y) {
        return y < INIT_ROWS && isFieldAccessible(x, y);
    }

    /**
     * Sprawdza, czy dana pozycja jest pozycja startowa dla czarnego pionka.
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     * @return true jezeli na polu na poczatku gry ma stac czarny pionek
     */
    private boolean isBlackStartingPosition(final int x, final int y) {
        return SIZE - y <= INIT_ROWS && isFieldAccessible(x, y);
    }

    /**
     * Sprawdza, czy pole jest dostepne dla pionkow (zazwyczaj oznaczone jako jasne pole).
     *
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     * @return true jezeli na polu moze stac pionek
     */
    private boolean isFieldAccessible(final int x, final int y) {
        return (x + y) % 2 == 0;
    }

    public Field getField(int x, int y) {
        // TODO Auto-generated method stub
        return fields[x][y];
    }
}
