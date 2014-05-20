package model;

import model.Field;
import model.CheckerColor;
import model.CheckerType;

class Board {
    // pola planszy
    private static Field fields[][];

    // @see #Model.BOARD_SIZE
    private final int SIZE;

    // @see #Model.INITIAL_CHECKERS_ROWS
    private final int INIT_ROWS;

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

        for(int i = 0; i < Model.BOARD_SIZE; ++i) {
            for(int j = 0; j < Model.BOARD_SIZE; ++j) {
                fields[j][i] = new Field(j, i);
            }
        }
    }

    /**
     * Rozstawia pionki na planszy
     */
    void setUp() {
        // zmienna pomocnicza
        Field field;

        for(int y = 0; y < SIZE; ++y) {
            for(int x = 0; x < SIZE; ++x) {
                field = fields[x][y];
                if(isWhiteStartingPosition(x, y)) {
                    field.setChecker(new Checker(x, y, CheckerColor.WHITE, CheckerType.NORMAL));
                } else if(isBlackStartingPosition(x, y)) {
                    field.setChecker(new Checker(x, y, CheckerColor.BLACK, CheckerType.NORMAL));
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

    Field getField(int x, int y) {
        // TODO Auto-generated method stub
        return fields[x][y];
    }
    
    Field[][] getFields() {
        return fields;
    }
}
