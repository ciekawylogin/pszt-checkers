package model;

/**
 * Black checker class
 */
public class BlackChecker extends Checker {
	
	/**
     * BlackChecker konstruktor
     */
    public BlackChecker() {
        super(CheckerColor.WHITE);
    }

    /**
     * BlackChecker konstruktor
     *
     * @param pos position on board
     * @param type type of checker
     */
    public BlackChecker(final int position, final CheckerType type) {
        super(position, type);
    }

    /**
     * BlackChecker konstruktor kopiujacy
     *
     * @param checker- reference to checker
     */
    public BlackChecker(final Checker checker) {
        super(checker);
    }

}
