package model;

/**
 * Przechowuje informacje o graczu
 */
public class Player {
    // nazwa gracza
    private String playerName;
    // kolor pionkow
    private CheckerColor playerColor;
    // czy graczem jest komputer
    private boolean isCpu;
    // poziom trudnosci
    private GameLevel gameLevel;
    /* ostatni poprawny ruch gracza */
    private Move lastMove;

    /**
     * Konstruktor
     */
    Player(final CheckerColor playerColor, final boolean isCpu, final GameLevel gameLevel) {
        this.playerName = "CPU";
        this.playerColor = playerColor;
        this.isCpu = isCpu;
        this.gameLevel = gameLevel;
    }

    /**
     * Konstruktor
     *
     * @param name player's name
     */
    Player(final String playerName, final CheckerColor playerColor,
            final boolean isCpu, final GameLevel gameLevel) {
        this.playerName = playerName.trim();
        this.playerColor = playerColor;
        this.isCpu = isCpu;
        this.gameLevel = gameLevel;
    }

    /**
     * Zwraca imie gracza
     *
     * @return playerName
     */
    String getName() {
        return playerName;
    }

    /**
     * Ustawia imie gracza
     *
     * @param name - nowa nazwa gracza
     */
    void setName(final String name) {
        playerName = name;
    }

    /**
     * Zwraca kolor pionkow gracza
     * @return playerColor
     */
    CheckerColor getPlayerColor() {
        return playerColor;
    }
    
    /**
     * Ustawia kolor pionkow gracza
     * @param playerColor - kolor do ustawienia
     */
    void setPlayerColor(final CheckerColor playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * Zwraca informacje czy gracz jest komputerem
     * @return true jesli komputer
     */
    boolean isCpu() {
        return isCpu;
    }

    /**
     * Zwraca poziom trudnosci gry
     * @return gameLevel
     */
    GameLevel getGameLevel() {
        return gameLevel;
    }

    /**
     * Zwraca ostatni poprawnie wykonany ruch gracza.
     * @return
     */
    Move getLastMove() {
        return lastMove;
    }

    /**
     * Ustawia ostatni poprawny ruch gracza
     * @param lastMove
     */
    void setLastMove(final Move lastMove) {
        this.lastMove = lastMove;
    }
}
