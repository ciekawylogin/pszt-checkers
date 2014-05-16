package common.events;

import model.Player;

/**
 * Klasa reprezentuje zdarzenie zakonczenia gry
 */
public class GameFinishEvent extends GameEvent {

	private boolean isWithdraw;
	private Player victoriusPlayer;
	
	/**
	 * Zwraca wygranego gracza. W przypadku remisu- null.
	 * @return victoriousPlayer
	 */
	public Player getVictoriusPlayer() {
		return victoriusPlayer;
	}
	
	/**
	 * Sprawdza czy w rozgrywce padl remis
	 * @return isWithraw
	 */
	public boolean isWithdraw() {
		return isWithdraw;
	}
}



