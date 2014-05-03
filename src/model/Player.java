package model;

/**
 * Przechowuje informacje o graczu.
 */
public class Player {
	/** nazwa gracza */
    private String playerName;
    
	/** flaga mowiaca o wygranej */
    private boolean isVictory;

	/** 
	 * Konstruktor
	 */
    public Player() {
		playerName = "player";
    }

	/**
	 * Konstruktor
	 * 
	 * @param name player's name
	 */
    public Player(final String name) {
        playerName = name.trim();
        isVictory = false;
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

}
