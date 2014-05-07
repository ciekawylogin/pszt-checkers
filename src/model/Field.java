package model;


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
	// @TODO dokonczyc
}
