package model;

/**
 * Klasa reprezentujaca ruch gracza
 */
public enum MoveState {
    // Ruch poprawny
    CORRECT,
    // Ruch poprawny, ale wymagane bicie
    DOCAPTURE,
    // Ruch niepoprawny
    INCORRECT,
    // Po biciu nastepuje kolejne bicie
    MULTICAPTURE;
}
