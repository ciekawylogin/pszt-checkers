package model;

/**
 * White checker class
 */
class WhiteChecker extends Checker {
    /**
     * WhiteChecker konstruktor
     */
    WhiteChecker() {
        super(CheckerColor.WHITE);
    }

    /**
     * WhiteChecker konstruktor
     *
     * @param pos position on board
     * @param type type of checker
     */
    WhiteChecker(final int position, final CheckerType type) {
        super(position, type);
    }
}
