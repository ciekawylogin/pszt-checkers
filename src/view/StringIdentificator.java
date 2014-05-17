package view;

/**
 * Klasa wiazaca string pobierany z pol menu z identyfikatorem calkowitoliczbowym.
 * Dla czytelnosci i poprawnosci kodu.
 *
 */
enum StringIdentificator {
    
    BLACK_COLOR,
    WHITE_COLOR,
    EASY_LEVEL,
    MEDIUM_LEVEL,
    HARD_LEVEL,
    UNRECOGNIZED;
    
    /**
     * Metoda zwraca identyfikator stringa
     * 
     * @param event_class - klasa do zidentyfikowania
     * @return identyfikator
     */
    public static StringIdentificator getId(final String toRecognize) {
        if(toRecognize.equals("white")) {
            return WHITE_COLOR;
        } else if(toRecognize.equals("black")) {
            return BLACK_COLOR;
        } else if(toRecognize.equals("easy")) {
            return EASY_LEVEL;
        } else if(toRecognize.equals("medium")) {
            return MEDIUM_LEVEL;
        } else if(toRecognize.equals("hard")) {
            return HARD_LEVEL;
        } 
        return UNRECOGNIZED;
    }

}
