package model;

/**
 * White checker class
 */
public class WhiteChecker extends Checker {
    /**
     * WhiteChecker konstruktor
     */
    public WhiteChecker() {
        super(CheckerColor.WHITE);
    }

    /**
     * WhiteChecker konstruktor
     *
     * @param pos position on board
     * @param type type of checker
     */
    public WhiteChecker(final int position, final CheckerType type) {
        super(position, type);
    }

    /**
     * WhiteChecker konstruktor kopiujacy
     *
     * @param checker- reference to checker
     */
    public WhiteChecker(final Checker checker) {
        super(checker);
    }
}
