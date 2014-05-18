package view;

/**
 * Klasa wiazaca kolor pionkow pobierany z pola menu z identyfikatorem calkowitoliczbowym.
 * Dla czytelnosci i poprawnosci kodu.
 *
 */
public enum ColorIdentificator {
    
    BLACK_COLOR,
    WHITE_COLOR,
    UNRECOGNIZED;
    
    /**
     * Metoda zwraca identyfikator koloru
     * 
     * @param toRecognize - kolor do zidentyfikowania
     * @return identyfikator
     */
    public static ColorIdentificator getId(final String toRecognize) {
        
        if(toRecognize.equals("black")) {
            return BLACK_COLOR;
        } else if(toRecognize.equals("white")) {
            return WHITE_COLOR;
        } 
       
        return UNRECOGNIZED;
    }

}
