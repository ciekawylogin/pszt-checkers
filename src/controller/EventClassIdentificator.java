package controller;

import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameFinishEvent;
import common.events.GameStartEvent;
import common.events.ProgramQuitEvent;

/**
 * Klasa wiazaca klase eventu z przystepniejszym dla czytelnika identyfikatorem.
 * Dla czytelnosci kodu.
 *
 */
public enum EventClassIdentificator {
    
    FIELD_CLICK,
    GAME_START,
    GAME_FINISH,
    PROGRAM_QUIT,
    UNRECOGNIZED;
    
    /**
     * Metoda zwraca identyfikator klasy eventu
     * 
     * @param event_class - klasa do zidentyfikowania
     * @return identyfikator
     */
    public static EventClassIdentificator getId(Class<? extends GameEvent> event_class) {
        if(event_class == FieldClickEvent.class) {
            return FIELD_CLICK;
        } else if(event_class == GameStartEvent.class) {
            return GAME_START;
        } else if(event_class == GameFinishEvent.class) {
            return GAME_FINISH;
        } else if(event_class == ProgramQuitEvent.class) {
            return PROGRAM_QUIT;
        }
        return UNRECOGNIZED;
    }

}
