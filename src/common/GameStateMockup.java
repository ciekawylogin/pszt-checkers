package common;

public enum GameStateMockup {
    // gra sie jeszcze nie rozpoczela - wyswietl ekran startowy
    NOT_STARTED,
    // gra w toku - czekamy na ruch gracza 1
    PLAYER_1_MOVE,
    // gra w toku - czekamy na ruch gracza 2
    PLAYER_2_MOVE,
    // wygral gracz 1 - wyswietl ekran informujacy o tym fakcie
    PLAYER_1_WON,
    // wygral gracz 2 - wyswietl ekran informujacy o tym fakcie
    PLAYER_2_WON
}
