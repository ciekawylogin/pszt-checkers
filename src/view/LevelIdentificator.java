package view;

/**
 * Klasa wiazaca poziom trudnosci pobierany z pola menu z identyfikatorem calkowitoliczbowym.
 * Dla czytelnosci i poprawnosci kodu.
 *
 */
enum LevelIdentificator {
    
    EASY_LEVEL,
    MEDIUM_LEVEL,
    HARD_LEVEL,
    UNRECOGNIZED;
    
    /**
     * Metoda zwraca identyfikator poziomu trudnosci
     * 
     * @param toRecognize - poziom trudnosci do zidentyfikowania
     * @return identyfikator
     */
    public static LevelIdentificator getId(final String toRecognize) {
        
        if(toRecognize.equals("easy")) {
            return EASY_LEVEL;
        } else if(toRecognize.equals("medium")) {
            return MEDIUM_LEVEL;
        } else if(toRecognize.equals("hard")) {
            return HARD_LEVEL;
        } 
        return UNRECOGNIZED;
    }

}
