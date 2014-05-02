package model;

import model.Field;
import model.CheckerColor;
import model.CheckerType;

public class Board {
	/// pola planszy
	Field fields[][];
	
	/// @see #Model.BOARD_SIZE
	final int SIZE;
	
	/// @see #Model.INITIAL_CHECKERS_ROWS
	final int INIT_ROWS;
	
	Board(int size, int init_rows) {
		this.SIZE = size;
		this.INIT_ROWS = init_rows;
		fields = new Field[SIZE][SIZE];
	}

	/**
	 * Rozstawia pionki na planszy
	 */
	public void setUp() {
		// Dawidczyk by mnie zajebal xD
		for(int x=0; x<SIZE; ++x)
		{
			for(int y=0; y<SIZE; ++y)
			{
				Field field = fields[x][y];
				if(isWhiteStartingPosition(x, y))
				{
					field.setChecker(new Checker(CheckerColor.WHITE, CheckerType.NORMAL));
				} else if(isBlackStartingPosition(x, y))
				{
					field.setChecker(new Checker(CheckerColor.BLACK, CheckerType.NORMAL));
				} 
			}
		}
		
	}

	/**
	 * Sprawdza, czy dana pozycja jest pozycją startową dla białego pionka.
	 * @param x współrzędna x
	 * @param y współrzędna y
	 * @return true jeżeli na polu na początku gry ma stać biały pionek
	 */
	private boolean isWhiteStartingPosition(int x, int y) {
		return y < INIT_ROWS && isFieldAccessible(x, y);
	}
	
	/**
	 * Sprawdza, czy dana pozycja jest pozycją startową dla czarnego pionka.
	 * @param x współrzędna x
	 * @param y współrzędna y
	 * @return true jeżeli na polu na początku gry ma stać czarny pionek
	 */
	private boolean isBlackStartingPosition(int x, int y) {
		return SIZE - y >= INIT_ROWS && isFieldAccessible(x, y);
	}

	/**
	 * Sprawdza, czy pole jest dostępne dla pionków (zazwyczaj oznaczone jako jasne pole).
	 * 
	 * @param x współrzędna x
	 * @param y współrzędna y
	 * @return true jeżeli na polu może stać pionek
	 */
	private boolean isFieldAccessible(int x, int y) {
		return (x + y) % 2 == 0;
	}
	
	
	
	// @TODO dokonczyc
}
