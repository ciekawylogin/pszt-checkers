package model;

import common.CheckerMockup;
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
	 * Tworzy makietę pola
	 */
	public FieldMockup getMockup() {
		return new FieldMockup(getCheckerMockup(), false);
	}
	
	/**
	 * Tworzy i zwraca makietę pionka
	 */
	public CheckerMockup getCheckerMockup() {
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
		}
		else
		{
			throw new RuntimeException("unknown checker type detected");
		}
	}
	// @TODO dokonczyc
}
