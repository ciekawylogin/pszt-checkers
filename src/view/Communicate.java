package view;

import common.GameStateMockup;

/**
 * Klasa komunikatow do wyswietlenia w panelu gry.
 *
 */
class Communicate {
    
    private String message;
    
    /**
     * Konstruktor
     * @param gameState
     */
    Communicate(final GameStateMockup gameState) {
        
        switch(gameState) {
        case NOT_STARTED:
            setMessage("Witaj");
            break;
        case WHITE_PLAYER_MOVE:
            setMessage("Ruch gracza bialego");
            break;
        case BLACK_PLAYER_MOVE:
            setMessage("Ruch gracza czarnego");
            break;
        case WHITE_PLAYER_WON:
            setMessage("Gracz bialy wygral");
            break;
        case BLACK_PLAYER_WON:
            setMessage("Gracz czarny wygral");
            break;
        case WITHDRAW:
            setMessage("Remis");
            break;
        case WHITE_PLAYER_REPEAT_MOVE:
            setMessage("Ruch gracza bialego - prosze powtorzyc ruch");
            break;
        case BLACK_PLAYER_REPEAT_MOVE:
            setMessage("Ruch gracza czarnego - prosze powtorzyc ruch");
            break;
        default:
            throw new RuntimeException("unrecognized gameState");
    
        }
        
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
