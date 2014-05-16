package model;

/**
 * Przechowuje informacje o graczu.
 */
public class Player {
	
	/** nazwa gracza */
    private String playerName;
    /** kolor pionkow */
    private CheckerColor playerColor;
    /** czy graczem jest komputer */
    private boolean isCpu;
    /** poziom trudnosci */
    private GameLevel gameLevel;
	/** flaga mowiaca o wygranej gracza */
    private boolean isVictory;

	/** 
	 * Konstruktor
	 */
    public Player(final CheckerColor playerColor, final boolean isCpu, 
    		final GameLevel gameLevel) {
		this.playerName = "CPU";
		this.playerColor = playerColor;
		this.isCpu = isCpu;
		this.gameLevel = gameLevel;
		this.isVictory = false;
    }

	/**
	 * Konstruktor
	 * 
	 * @param name player's name
	 */
    public Player(final String playerName, final CheckerColor playerColor, 
    		final boolean isCpu, final GameLevel gameLevel) {
		this.playerName = playerName.trim();
		this.playerColor = playerColor;
		this.isCpu = isCpu;
		this.gameLevel = gameLevel;
        this.isVictory = false;
    }

	/** 
	 * Zwraca imie gracza
	 * 
	 * @return playerName
	 */
    public String getName() {
        return playerName;
    }

	/**
	 * Ustawia imie gracza
	 * 
	 * @param name - nowa nazwa gracza
	 */
    public void setName(final String name) {
        playerName = name;
    }

	/**
	 * Ustawia flage zwyciestwa
	 * 
	 * @param isVictory
	 */
    public void setVictorious(final boolean isVictory) {
        this.isVictory = isVictory;
    }

    /**
	 * Sprawdza czy gracz wygral
	 * 
	 * @return isVictory
	 */
	public boolean isVictorious() {
        return isVictory;
    }
	
	/**
	 * Zwraca kolor pionkow gracza
	 * @return playerColor
	 */
	public CheckerColor getPlayerColor() {
		return playerColor;
	}
	
	/**
	 * Zwraca informacje czy gracz jest komputerem
	 * @return true jesli komputer
	 */
	public boolean isCpu() {
		return isCpu;
	}
	
	/**
	 * Zwraca poziom trudnosci gry.
	 * @return gameLevel
	 */
	public GameLevel getGameLevel() {
		return gameLevel;
	}

}
