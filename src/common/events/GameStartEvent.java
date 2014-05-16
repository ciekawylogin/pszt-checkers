package common.events;

import model.CheckerColor;
import model.GameLevel;

/**
 * Klasa reprezentuje zdarzenie wcisniecia przycisku rozpoczecia gry
 */
public final class GameStartEvent extends GameEvent {

	/** poziom trudnosci */
	private GameLevel gameLevel;
	/** kolor pionkow ludzkiego gracza */
	private CheckerColor playerColor;
	/** nazwa ludzkiego gracza */
	private String playerName;
	
	/**
	 * Konstruktor
	 * @param playerName - nazwa gracza
	 * @param playerColor - kolor pionkow ludzkiego gracza
	 * @param gameLevel - poziom trudnosci gry
	 */
	public GameStartEvent(final String playerName, final CheckerColor playerColor, final GameLevel gameLevel) {
		this.playerName = playerName;
		this.playerColor = playerColor;
		this.gameLevel = gameLevel;
	}
	
	
	/**
	 * Zwraca nazwe gracza
	 * @return playerName
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Zwraca kolor pionkow 
	 * @return checkerColor
	 */
	public CheckerColor getCheckerColor() {
		return playerColor;
	}
	
	/**
	 * Zwraca poziom trudnosci gry
	 * @return gameLevel
	 */
	public GameLevel getGameLevel() {
		return gameLevel;
	}
}


