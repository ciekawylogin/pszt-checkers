package common;

/**
 * Makieta, tj. obiekt generowany przez 
 * 
 *
 */
public final class Mockup {
	
	/// opis planszy
	FieldMockup fields [][];
	
	/// stan gry
	GameStateMockup game_state;
	
	/// gracze
	PlayerMockup players[];
	
	Mockup()
	{
		players = new PlayerMockup[2];
		fields = new FieldMockup[8][8];
	}
}
