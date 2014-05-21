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
        case PLAYER_1_MOVE:
            setMessage("Ruch gracza 1");
            break;
        case PLAYER_2_MOVE:
            setMessage("Ruch gracza 2");
            break;
        case PLAYER_1_WON:
            setMessage("Gracz 1 wygral");
            break;
        case PLAYER_2_WON:
            setMessage("Gracz 2 wygral");
            break;
        case WITHDRAW:
            setMessage("Remis");
            break;
        case PLAYER_1_MOVE_REPEAT_MOVE:
            setMessage("Ruch gracza 1 - prosze powtorzyc ruch");
            break;
        case PLAYER_2_MOVE_REPEAT_MOVE:
            setMessage("Ruch gracza 2 - prosze powtorzyc ruch");
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
