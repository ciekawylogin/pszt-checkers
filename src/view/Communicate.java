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
            setMessage("Hello");
            break;
        case WHITE_PLAYER_MOVE:
            setMessage("White pieces move");
            break;
        case BLACK_PLAYER_MOVE:
            setMessage("Black pieces move");
            break;
        case WHITE_PLAYER_WON:
            setMessage("White pieces have won!");
            break;
        case BLACK_PLAYER_WON:
            setMessage("Black pieces have won!");
            break;
        case WITHDRAW:
            setMessage("Withdraw");
            break;
        case WHITE_PLAYER_REPEAT_MOVE:
            setMessage("White pieces move");
            break;
        case BLACK_PLAYER_REPEAT_MOVE:
            setMessage("Black pieces move");
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
