package model;

/**
 * Black checker class
 */
class BlackChecker extends Checker {
    /**
     * BlackChecker konstruktor
     */
    BlackChecker() {
        super(CheckerColor.WHITE);
    }

    /**
     * BlackChecker konstruktor
     *
     * @param pos position on board
     * @param type type of checker
     */
    BlackChecker(final int position, final CheckerType type) {
        super(position, type);
    }
}
