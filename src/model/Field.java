package model;

import common.CheckerMockup;
import common.FieldMockup;


public class Field {
	/** pionek na tym polu */
	private Checker checker;
	/** czy pole jest zaznaczone */
	private boolean is_selected;
	
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
	 * @return 
	 */
	public void removeChecker()
	{
		this.checker = null;
	}
	
	/**
	 * Tworzy makietę pola
	 */
	public FieldMockup getMockup() {
		return new FieldMockup(getCheckerMockup(), is_selected);
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

	public boolean isSelected() {
		return is_selected;
	}

	public void select() {
		this.is_selected = true;
	}
	
	public void unselect() {
		this.is_selected = false;
	}
	
}
