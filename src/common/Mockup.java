package common;

import java.util.ArrayList;

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
    /* makieta ostatniego poprawnego ruchu */
    private MoveMockup lastMove;
    
    private ArrayList<Coordinate> deletedCheckers;

    public Mockup(final int numberState) {
        players = new PlayerMockup[2];
        fields = new FieldMockup[8][8];
        setGameStateFromNumber(numberState);
        deletedCheckers = new ArrayList<Coordinate>();
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
            game_state = GameStateMockup.WHITE_PLAYER_MOVE;
            break;
        case 2:
            game_state = GameStateMockup.BLACK_PLAYER_MOVE;
            break;
        case 3:
            game_state = GameStateMockup.WHITE_PLAYER_WON;
            break;
        case 4:
            game_state = GameStateMockup.BLACK_PLAYER_WON;
            break;
        case 5:
            game_state = GameStateMockup.WITHDRAW;
            break;
        case 6:
            game_state = GameStateMockup.WHITE_PLAYER_REPEAT_MOVE;
            break;
        case 7:
            game_state = GameStateMockup.BLACK_PLAYER_REPEAT_MOVE;
            break;
        default:
            throw new RuntimeException("gameState not recognized");
                
        }
    }

    /**
     * 
     * @return
     */
    public MoveMockup getLastMove() {
        return lastMove;
    }

    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void setLastMove(final int startX, final int startY, final int endX, final int endY) {
        this.lastMove = new MoveMockup(startX, startY, endX, endY);
        
    }
    
    public void addCoordinate(final Coordinate coordinateToadd) {
        deletedCheckers.add(coordinateToadd);
    }
    
    public ArrayList<Coordinate> getDeletedCheckers() {
        return deletedCheckers;
    }
    
    
    
}
