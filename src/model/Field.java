package model;

import common.CheckerMockup;
import common.FieldMockup;

class Field {
    /** pionek na tym polu */
    private Checker checker;
    /** czy pole jest zaznaczone */
    private boolean is_selected;
    /** pozycja x pola */
    private int positionX;
    /** pozycja y pola */
    private int positionY;

    Field(final int positionX, final int positionY) {
        this.checker = null;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Ustawia zadanego pionka na polu
     * @param checker pionek do ustawienia
     */
    void setChecker(Checker checker) {
        this.checker = checker;
    }

    /**
     * Zwraca pionek.
     * @return checker
     */
    Checker getChecker() {
        return checker;
    }

    /**
     * Kasuje pionek z pola
     * @return
     */
    void removeChecker()
    {
        this.checker = null;
    }

    /**
     * Tworzy makiete pola
     */
    FieldMockup getMockup() {
        return new FieldMockup(getCheckerMockup(), is_selected);
    }

    /**
     * Tworzy i zwraca makietę pionka
     */
    private CheckerMockup getCheckerMockup() {
        if(checker == null) {
            return CheckerMockup.EMPTY_FIELD;
        } else if (
                checker.getColor() == CheckerColor.BLACK &&
                checker.getType() == CheckerType.NORMAL) {
            return CheckerMockup.BLACK_CHECKER;
        } else if (
                checker.getColor() == CheckerColor.BLACK &&
                checker.getType() == CheckerType.QUEEN) {
            return CheckerMockup.BLACK_QUEEN;
        } else if (
                checker.getColor() == CheckerColor.WHITE &&
                checker.getType() == CheckerType.NORMAL) {
            return CheckerMockup.WHITE_CHECKER;
        } else if (
                checker.getColor() == CheckerColor.WHITE &&
                checker.getType() == CheckerType.QUEEN) {
            return CheckerMockup.WHITE_QUEEN;
        } else {
            throw new RuntimeException("unknown checker type detected");
        }
    }

    boolean isSelected() {
        return is_selected;
    }

    void select() {
        this.is_selected = true;
    }

    void unselect() {
        this.is_selected = false;
    }
    
    int getX() {
        return positionX;
    }
    
    int getY() {
        return positionY;
    }
    
    public Field clone() {
    	Field result = new Field(positionX, positionY);
    	if(checker != null) {    		
        	result.setChecker(checker.clone());
    	}
    	return result;
    }
    
    @Override
    public String toString() {
    	return checker == null? " " : checker.getColor() == CheckerColor.WHITE? "w" : "b";
    }
}
