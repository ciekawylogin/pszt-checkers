package common.events;

import model.Player;

/**
 * Klasa reprezentuje zdarzenie zakonczenia gry
 */
public class GameFinishEvent extends GameEvent {
    private boolean isWithdraw;
    private Player victoriusPlayer;

    /**
     * Konstruktor
     * @param isWithdraw - czy w rozgrywce padl remis
     * @param victoriousPlayer - referencja na gracza ktory wygral, 
     * null - jesli nie ma wygranego
     */
    public GameFinishEvent(final boolean isWithdraw, final Player victoriousPlayer) {
        this.isWithdraw = isWithdraw;
        this.victoriusPlayer = victoriousPlayer;
    }

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
