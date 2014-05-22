package common;

/**
 * Makieta, tj. obiekt generowany przez model, ktory zawiera wszystkie informacje niezbedne
 * widokowi do wyswietlenia stanu gry.
 */
public final class Mockup {
    /* opis planszy */
    private FieldMockup fields[][];
    /* stan gry */
    private GameStateMockup game_state = GameStateMockup.NOT_STARTED;
    /* gracze (0 = bialy, 1 = czarny) */
    private PlayerMockup players[];
    /* makieta ostatniego ruchu gracza 1 */
    private MoveMockup player1LastMove;
    /* makieta ostatniego ruchu gracza 2 */
    private MoveMockup player2LastMove;

    public Mockup(final int numberState) {
        players = new PlayerMockup[2];
        fields = new FieldMockup[8][8];
        setGameStateFromNumber(numberState);
    }

    /**
     * @return 
     */
    public final FieldMockup getField(int x, int y) {
        return fields[x][y];
    }

    /**
     * @param 
     */
    public final void setField(FieldMockup field, int x, int y) {
        this.fields[x][y] = field;
    }

    /**
     * @return 
     */
    public final GameStateMockup getGameState() {
        return game_state;
    }

    /**
     * @param
     */
    public final void setGameState(GameStateMockup game_state) {
        this.game_state = game_state;
    }

    /**
     * @return 
     */
    public final PlayerMockup getPlayer(int player_num) {
        return players[player_num];
    }

    /**
     * @param 
     */
    public final void setPlayers(PlayerMockup player, int player_num) {
        this.players[player_num] = player;
    }
    
    /**
     * Przypisuje numerowi dany stan gry
     * @param numberState
     */
    private void setGameStateFromNumber(final int numberState) {
        switch(numberState) {
        case 0:
            game_state = GameStateMockup.NOT_STARTED;
            break;
        case 1:
            game_state = GameStateMockup.PLAYER_1_MOVE;
            break;
        case 2:
            game_state = GameStateMockup.PLAYER_2_MOVE;
            break;
        case 3:
            game_state = GameStateMockup.PLAYER_1_WON;
            break;
        case 4:
            game_state = GameStateMockup.PLAYER_2_WON;
            break;
        case 5:
            game_state = GameStateMockup.WITHDRAW;
            break;
        case 6:
            game_state = GameStateMockup.PLAYER_1_MOVE_REPEAT_MOVE;
            break;
        case 7:
            game_state = GameStateMockup.PLAYER_2_MOVE_REPEAT_MOVE;
            break;
        default:
            throw new RuntimeException("gameState not recognized");
                
        }
    }

    /**
     * 
     * @return
     */
    public MoveMockup getPlayer2LastMove() {
        return player2LastMove;
    }

    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void setPlayer2LastMove(final int startX, final int startY, final int endX, final int endY) {
        this.player2LastMove = new MoveMockup(startX, startY, endX, endY);
        
    }

    /**
     * 
     * @return
     */
    public MoveMockup getPlayer1LastMove() {
        return player1LastMove;
    }

    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void setPlayer1LastMove(final int startX, final int startY, final int endX, final int endY) {
        this.player1LastMove = new MoveMockup(startX, startY, endX, endY);
        
    }
    
    
    
}
