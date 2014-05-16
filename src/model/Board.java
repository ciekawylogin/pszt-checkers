package model;

import static model.CheckerColor.BLACK;
import static model.CheckerColor.WHITE;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	/** tablica z czarymi pionkami */
	//private ArrayList<BlackChecker> blackCheckers;
	/** tablica z bialymi pionkami */
    //private ArrayList<WhiteChecker> whiteCheckers;
    /* tablica z graczami */
    //private ArrayList<Player> players;
    
    /*  aktualny kolor pionka majacy prawo do ruchu */
    private CheckerColor actualMoveColor = WHITE;
	
    /**
     * Konstruktor domyslny.
     */
    Board() {
		this.SIZE = 8;
		this.INIT_ROWS = 8;
		fields = new Field[SIZE][SIZE];
		
		//blackCheckers = new ArrayList<>();
        //whiteCheckers = new ArrayList<>();
        //players = new ArrayList<>();
	}
    
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
		
		//blackCheckers = new ArrayList<>();
        //whiteCheckers = new ArrayList<>();
        //players = new ArrayList<>();
	}

	/**
	 * Rozstawia pionki na planszy
	 */
	public void setUp() {
		
		// zmienna pomocnicza
		Field field;

		for(int x=0; x<SIZE; ++x)
		{
			for(int y=0; y<SIZE; ++y)
			{
				field = fields[x][y];
				
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
		return SIZE - y >= INIT_ROWS && isFieldAccessible(x, y);
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
	
	/**
	 * Zwraca gracza
	 * 
	 * @param idx indeks gracza
	 * @return
	 */
	//public Player getPlayer(final int idx) {
    //    return players.get(idx);
    //}
	
	/**
	 * Zwraca tablice bialych pionkow
	 * 
	 * @return whiteCheckers
	 */
	//public ArrayList<WhiteChecker> getWhiteCheckersArray() {
    //    return whiteCheckers;
    //}

	/**
	 * Zwraca tablice czarnych pionkow
	 * 
	 * @return blackCheckers
	 */
    //public ArrayList<BlackChecker> getBlackCheckersArray() {
    //    return blackCheckers;
    //}
    
    /**
     * Zwraca pionek z planszy
     * 
     * @param idx - indeks pionka
     * @return Checker
     */
    //public Checker getChecker(final int idx) {
    //	return fields[idx % 8][idx / 8].getChecker();
    //}
    
    /**
     * Ustawia pionek na planszy
     * 
     * @param checker - pionek do ustawienia
     * @param idx - index pola do wstawienia
     */
    //public void setChecker(final Checker checker, final int idx) {
    //    fields[idx % 8][idx / 8].setChecker(checker);
    //}

    /**
     * Usuwa pionek z planszy
     *
     * @param checker - pionek do usuniecia
     */
	/*
    private void deleteChecker(final Checker checker) {
    	int x = checker.getPositionOnBoard() % 8;
    	int y = checker.getPositionOnBoard() / 8;
    	
        fields[x][y].setChecker(null);
        
        switch (checker.getColor()) {
            case WHITE:
                whiteCheckers.remove((WhiteChecker) checker);
                break;
                
            case BLACK:
                blackCheckers.remove((BlackChecker) checker);
                break;
                
            default:
            	throw new RuntimeException("Nierozpoznany pionek");
        }
    }
    */
    
    /**
     * Sprawdzenie czy dany kolor ma mozliwosc ruchu.
     *
     * @param color - kolor do sprawdzenia
     * @return true - jesli znaleziono mozliwosc
     */
	/*
    boolean hasMove(final CheckerColor color) {
        switch (color) {
            case WHITE:
                for (Iterator<WhiteChecker> it = whiteCheckers.iterator(); it.hasNext();) {
                    Checker i = it.next();
                    if (hasMove(i)) {
                        return true;
                    }
                }
                break;
                
            case BLACK:
                for (Iterator<BlackChecker> it = blackCheckers.iterator(); it.hasNext();) {
                    Checker i = it.next();
                    if (hasMove(i)) {
                        return true;
                    }
                }
                
            default:
            	throw new RuntimeException("Nierozpoznany pionek");
        }
        return false;
    }
    */
    
    /**
     * Sprawdza, czy pionek moze wykonac ruch
     *
     * @param checker - pionek do sprawdzenia
     * @return true - jesli truch mozliwy
     */
	/*
    boolean hasMove(final Checker checker) {
    	
        int x = checker.getPositionX();
        int y = checker.getPositionY();
        
        CheckerColor color = checker.getColor();

        switch (checker.getType()) {
        
            case NORMAL:
                if (color == CheckerColor.BLACK) {
                	
                	//TODO


                } else {
                	
                    //TODO
                } 
                break;

            case QUEEN:

            	//TODO
        }
        return false;
    }
    */
    
    /**
     * Sprawdza, czy pionek moze wykonac bicie
     * 
     * @param checker - pionek do sprawdzenia
     * @return true - jesli bicie jest mozliwe
     */
	/*
    boolean hasCaptures(final Checker checker) {
    	//TODO
    	return false;
    }
    */
    
    /**
     * Wykonanie ruchu.
     */
    void makeMove() {
        //TODO

    }
	
	
	
	// @TODO dokonczyc
}
