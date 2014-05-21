package model;

/**
 * Klasa reprezentujaca obecny stan gry.
 *
 */
enum GameState {
    // gra sie jeszcze nie rozpoczela - wyswietl ekran startowy
    NOT_STARTED(0),
    // gra w toku - czekamy na ruch gracza 1
    PLAYER_1_MOVE(1),
    // gra w toku - czekamy na ruch gracza 2
    PLAYER_2_MOVE(2),
    // wygral gracz 1 - wyswietl ekran informujacy o tym fakcie
    PLAYER_1_WON(3),
    // wygral gracz 2 - wyswietl ekran informujacy o tym fakcie
    PLAYER_2_WON(4),
    // remis
    WITHDRAW(5),
    // gra w toku - czekamy na ruch gracza 1 - nalezy powtorzyc ruch
    PLAYER_1_MOVE_REPEAT_MOVE(6),
    // gra w toku - czekamy na ruch gracza 2 - nalezy powtorzyc ruch
    PLAYER_2_MOVE_REPEAT_MOVE(7);
    
    private int number;
    
    GameState(final int number) {
        this.number = number;
    }

    int getValue() {
        return number;
    }
}
