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
	private CheckerColor checkerColor;
	/** nazwa ludzkiego gracza */
	private String playerName;
	
	
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
		return checkerColor;
	}
	
	/**
	 * Zwraca poziom trudnosci gry
	 * @return gameLevel
	 */
	public GameLevel getGameLevel() {
		return gameLevel;
	}
}


