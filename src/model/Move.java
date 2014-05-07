package model;

import java.util.ArrayList;

public class Move {
	
	/** poczatkowe pole pionka */
    int start;
    
    /** koncowe pole pionka */
    int end;
    
    /** ilosc bic */
    int captures;
    
    /* flaga kasowania */
    boolean toDelete = false;
    
    /* Tablica historii planszy */
    ArrayList<Board> boards;

    /**
     * Konstruktor
     *
     * @param start - poczatkowa pozycja pionka
     * @param end - koncowa pozycja pionka
     * @param model - tablica po wykonaniu ruchu
     */
    Move(final int start, final int end, final Board model) {
    	boards = new ArrayList<>();
        boards.add(model);
    	
        this.start = start;
        this.end = end;
    }


    //TODO
}
