package model;

import common.FieldMockup;


public class Field {
	/* pionek na tym polu */
	private Checker checker;
	
	Field() {
		this.checker = null;
	}
	
	/**
	 * Ustawia zadanego pionka na polu
	 * @param checker pionek do ustawienia
	 */
	public void setChecker(Checker checker) {
		this.checker = checker;
	}
	
	/**
	 * Zwraca pionek.
	 * @return checker
	 */
	public Checker getChecker() {
        return checker;
    }
	
	/**
	 * Kasuje pionek z pola
	 */
	public void removeChecker()
	{
		this.checker = null;
	}
	
	/**
	 * Tworzy i zwraca makietę zadanego pola
	 */
	public FieldMockup getMockup() {
		if(checker == null) {
			return FieldMockup.EMPTY_FIELD;
		} else if (
				checker.getColor() == CheckerColor.BLACK && 
				checker.getType() == CheckerType.NORMAL) {
			return FieldMockup.BLACK_CHECKER;
		} else if (
				checker.getColor() == CheckerColor.BLACK && 
				checker.getType() == CheckerType.QUEEN) {
			return FieldMockup.BLACK_QUEEN;
		} else if (
				checker.getColor() == CheckerColor.WHITE && 
				checker.getType() == CheckerType.NORMAL) {
			return FieldMockup.WHITE_CHECKER;
		} else if (
				checker.getColor() == CheckerColor.WHITE && 
				checker.getType() == CheckerType.QUEEN) {
			return FieldMockup.WHITE_QUEEN;
		}
		else
		{
			throw new RuntimeException("unknown checker type detected");
		}
	}
	// @TODO dokonczyc
}
