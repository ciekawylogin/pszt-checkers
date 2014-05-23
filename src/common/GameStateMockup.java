package common;

public enum GameStateMockup {
    
    // gra sie jeszcze nie rozpoczela - wyswietl ekran startowy
    NOT_STARTED,
    // gra w toku - czekamy na ruch gracza bialego
    WHITE_PLAYER_MOVE,
    // gra w toku - czekamy na ruch gracza czarnego
    BLACK_PLAYER_MOVE,
    // wygral gracz bialy - wyswietl ekran informujacy o tym fakcie
    WHITE_PLAYER_WON,
    // wygral gracz czarny - wyswietl ekran informujacy o tym fakcie
    BLACK_PLAYER_WON,
    // remis
    WITHDRAW,
    // gra w toku - czekamy na ruch gracza bialego - nalezy powtorzyc ruch
    WHITE_PLAYER_REPEAT_MOVE,
    // gra w toku - czekamy na ruch gracza czarnego - nalezy powtorzyc ruch
    BLACK_PLAYER_REPEAT_MOVE
    
    
    
}
