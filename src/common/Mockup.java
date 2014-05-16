package common;

import java.util.Arrays;

/**
 * Makieta, tj. obiekt generowany przez model, który zawiera wszystkie informacje niezbędne 
 * widokowi do wyświetlenia stanu gry.
 */
public final class Mockup {
	
	/// opis planszy
	FieldMockup fields [][];
	
	/// stan gry
	GameStateMockup game_state;
	
	/// gracze (0 = bialy, 1 = czarny)
	PlayerMockup players[];
	
	
	public Mockup() {
		players = new PlayerMockup[2];
		fields = new FieldMockup[8][8];
	}

	/**
	 * @return the fields
	 */
	public final FieldMockup getField(int x, int y) {
		return fields[x][y];
	}

	/**
	 * @param fields the fields to set
	 */
	public final void setField(FieldMockup field, int x, int y) {
		this.fields[x][y] = field;
	}

	/**
	 * @return the game_state
	 */
	public final GameStateMockup getGameState() {
		return game_state;
	}

	/**
	 * @param game_state the game_state to set
	 */
	public final void setGameState(GameStateMockup game_state) {
		this.game_state = game_state;
	}

	/**
	 * @return the players
	 */
	public final PlayerMockup getPlayer(int player_num) {
		return players[player_num];
	}

	/**
	 * @param players the players to set
	 */
	public final void setPlayers(PlayerMockup player, int player_num) {
		this.players[player_num] = player;
	}

	/**
	 * tylko do debugowania
	 */
	@Override
	public String toString() {
		return "Mockup [fields=" + Arrays.toString(fields) + ", game_state="
				+ game_state + ", players=" + Arrays.toString(players) + "]";
	}
}
