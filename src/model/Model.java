package model;

import java.util.ArrayList;
import java.util.Collection;

import common.FieldMockup;
import common.GameStateMockup;
import common.Mockup;
import common.PlayerMockup;

public class Model {

	/// rozmiar planszy
	private final static int BOARD_SIZE = 8;
	
	/// po ile rzędów pionków rozstawić na starcie (dla wymiaru 8 jest to najczęściej
	/// 3, dla wymiaru 10 - 4)
	private final static int INITIAL_CHECKERS_ROWS = 3;
	
	/// odnosnik do planszy
	private Board board;
	
	/// gracze
	private Player players[];
	
	/// numer aktywnego gracze
	private int active_player;
	
	public Model() {
		this.board = new Board(Model.BOARD_SIZE, Model.INITIAL_CHECKERS_ROWS);
		this.players = new Player[2];
	}
	
	/**
	 * @return true wtedy i tylko wtedy, gdy jakikolwiek pionek jest zaznaczony
	 */
	public final boolean isAnyCheckerSelected() {
		for(int x=0; x<8; ++x)
		{
			for(int y=0; y<8; ++y)
			{
				if(board.getField(x, y).isSelected())
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Przemieszcza zaznaczony pionek na pole o współrzędnych (target_x, target_y).
	 * 
	 * Możliwe warianty wykonania ruchu:
	 * + jeżeli jakiś pionek jest zaznaczony, a docelowe pole znajduje się 1 pole 
	 * 	 naprzód (po skosie) od obecnej pozycji pionka (zwykły ruch), to pionek 
	 *   przesuwa się na zadane pole, następuje zmiana kolejki, a pionek zostaje 
	 *   odznaczony; UWAGA: zwykły ruch jest dozwolony tylko wtedy, gdy gracz nie
	 *   ma żadnego bicia
	 * + jeżeli jakiś pionek jest zaznaczony, a docelowe pole znajduje się 2 pola 
	 *   naprzód od niego i między polem docelowym a obecnym znajduje się pionek 
	 *   przeciwnika (bicie), wówczas pionek przesuwa się 2 na pole docelowe, a pionek 
	 *   pzreciwnika zostaje usunięty dodatkowo przy biciu funkcja musi sprawdzić, 
	 *   czy jest możliwe kolejne bicie; 
	 *   jeżeli nie, to:
	 * ++ następuje zmiana kolejki
	 * ++ pionek zostaje odznaczony
	 *   jeżeli tak, to:
	 * ++ nie następuje zmiana kolejki
	 * ++ pionek nie zostaje odznaczony
	 * ++ w następnej kolejce pionek zostaje zablokowany, tj. gracz nie może go odznaczyć,
	 *    musi wykonać bicie tym pionkiem
	 * + jeżeli nie zachodzi żaden z powyższych przypadków, to ruch jest uznawany 
	 *   za niepoprawny; funkcja rzuca wyjątek, nie zostają wprowadzone żadne zmiany 
	 *   w modelu
	 * 
	 * @param target_x współrzędna x docelowego pola
	 * @param target_y współrzędna y docelowego pola
	 * @return true jeżeli ruch jest dozwolony i został wykonany
	 * 		   false jeżeli ruch jest niedozwolony; w takim wypadku żadne zmiany nie zostają
	 * 		   wprowadzone do modelu
	 */
	public final boolean moveSelectedCheckerTo(int target_x, int target_y) {
		Coordinate source_coordinate = getSelectedCheckerCoordinate();
		int source_x = source_coordinate.getX();
		int source_y = source_coordinate.getY();
		CheckerType checkerType = board.getField(source_x, source_y).getChecker().getType();
		if(board.getField(target_x, target_y).getChecker() == null)
		{
			if(checkerType == CheckerType.QUEEN) {
				return makeQueenMove(source_x, source_y, target_x, target_y);
			} else {
				return makeNormalCheckerMove(source_x, source_y, target_x, target_y);
			}
		}
		else
		{
			// docelowe pole jest zajete
			return false;
		}
	}
	

	private boolean makeNormalCheckerMove(int source_x, int source_y, int target_x, int target_y) {
		boolean normalMove = isNormalCheckerNormalMoveCorrect(source_x, source_y, target_x, target_y);
		boolean captureMove = isNormalCheckerCaptureMoveCorrect(source_x, source_y, target_x, target_y);
		boolean correctMove = normalMove || captureMove;
		if(correctMove) {
			Field oldField = board.getField(source_x, source_y);
			Checker checker = oldField.getChecker();
			oldField.removeChecker();

			Field newField = board.getField(target_x, target_y);
			newField.setChecker(checker);
			checker.setPositionOnBoard(target_x, target_y);
			checkQueenCondition(target_x, target_y);
		}
		if (captureMove) {
			int checkerToRemoveX = (target_x + source_x) / 2;
			int checkerToRemoveY = (target_y + source_y) / 2; 
			
			board.getField(checkerToRemoveX, checkerToRemoveY).removeChecker();
		}
			
		return correctMove;
	}

	private void checkQueenCondition(final int target_x, final int target_y) {
		if(target_y == 0 || target_y == BOARD_SIZE) {
			board.getField(target_x, target_y).getChecker().promote();
		}
	}

	private boolean isMoveCorrect(int source_x, int source_y, int target_x, int target_y)
	{
		CheckerType type = board.getField(source_x, source_y).getChecker().getType();
		if(type == CheckerType.NORMAL)
		{
			return isNormalCheckerMoveCorrect(source_x, source_y, target_x, target_y);
		}
		else
		{
			return isQueenCheckerMoveCorrect(source_x, source_y, target_x, target_y, null);
		}
	}
	
	private boolean isNormalCheckerMoveCorrect(int source_x, int source_y, int target_x, int target_y) {
		return (board.getField(target_x, target_y).getChecker() == null) &&
			( 
				isNormalCheckerCaptureMoveCorrect(source_x, source_y, target_x, target_y)
				|| 
				isNormalCheckerNormalMoveCorrect(source_x, source_y, target_x, target_y)
			);
	}
	
	private boolean isNormalCheckerCaptureMoveCorrect(int source_x, int source_y, int target_x, int target_y) {
		boolean isTargetToTheLeft = target_x == source_x - 2;
		boolean isTargetToTheRight = target_x == source_x + 2;
		boolean isTargetToTheTop = target_y == source_y - 2;
		boolean isTargetToTheBottom = target_y == source_y + 2;
		
		return (isTargetToTheLeft  && isTargetToTheTop)
			|| (isTargetToTheLeft  && isTargetToTheBottom)
			|| (isTargetToTheRight && isTargetToTheTop)
			|| (isTargetToTheRight && isTargetToTheBottom);
	}

	private boolean isNormalCheckerNormalMoveCorrect(int source_x, int source_y, int target_x, int target_y) {
		Checker checker = board.getField(source_x, source_y).getChecker();
		CheckerColor color = checker.getColor();
		boolean isTargetToTheLeft = target_x == source_x - 1;
		boolean isTargetToTheRight = target_x == source_x + 1;
		boolean isTargetToTheTop = target_y == source_y - 1;
		boolean isTargetToTheBottom = target_y == source_y + 1;
		if(color == CheckerColor.WHITE) {
			return (isTargetToTheLeft && isTargetToTheBottom) || (isTargetToTheRight && isTargetToTheBottom);
		} else {
			return (isTargetToTheLeft && isTargetToTheTop) || (isTargetToTheRight && isTargetToTheTop);
		}
	}

	private boolean makeQueenMove(int source_x, int source_y, int target_x, int target_y) {
			ArrayList<Coordinate> coordinatesToDelete = new ArrayList<>();
		
			boolean correctMove = isQueenCheckerMoveCorrect(source_x, source_y, target_x, target_y,
					coordinatesToDelete);
			
			if(correctMove) {
				Field oldField = board.getField(source_x, source_y);
				Checker checker = oldField.getChecker();
				oldField.removeChecker();

				Field newField = board.getField(target_x, target_y);
				newField.setChecker(checker);
				checker.setPositionOnBoard(target_x, target_y);


				// usuwanie potencjalnych ofiar ruchu 
				for(int i=0; i< coordinatesToDelete.size(); ++i) {
					int x = coordinatesToDelete.get(i).getX();
					int y = coordinatesToDelete.get(i).getY();
					board.getField(x, y).removeChecker();
				}
				
				
				
			}
				
			return correctMove;
	}

	/**
	 * Sprawdza czy ruch damy jest poprawny.
	 * 
	 * @param source_x - wspolrzedna x poczatkowej pozycji
	 * @param source_y - wspolrzedna y poczatkowej pozycji
	 * @param target_x - wspolrzedna x koncowej pozycji
	 * @param target_y - pozycja y koncowej pozycji
	 * @param coordinatesToDelete - tablica zbitych po drodze pionkow
	 * @return true gdy ruch poprawny
	 */
	private boolean isQueenCheckerMoveCorrect(final int source_x, final int source_y,
			final int target_x, final int target_y, ArrayList<Coordinate> coordinatesToDelete) {
		
		// sprawdz czy docelowe pole jest puste
		if(board.getField(target_x, target_y).getChecker() != null) {
			return false;
		}
		
		Checker checker = board.getField(source_x, source_y).getChecker();
		CheckerColor color = checker.getColor();
		boolean isTargetToTheLeft = target_x < source_x ? true : false;
		boolean isTargetToTheTop = target_y < source_y ? true : false;
		
		int x = board.getField(source_x, source_y).getChecker().getPositionX();
		int y = board.getField(source_x, source_y).getChecker().getPositionY();
		
		
		// cel: lewy gorny rog
		if(isTargetToTheLeft && isTargetToTheTop && color == CheckerColor.BLACK) {
			--x;
			++y;
			for(; x >= target_x; --x, ++y) {
				
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == CheckerColor.BLACK) {
					return false;
				} else if(temp !=null && temp.getColor() == CheckerColor.WHITE
						&& coordinatesToDelete != null) {
					coordinatesToDelete.add(new Coordinate (x, y));
				}
				
			}
			
			if(x == target_x && y == target_y) {
				return true;
			}
			
		// cel: lewy dolny rog
		} else if(isTargetToTheLeft && !isTargetToTheTop && color == CheckerColor.WHITE) {
			--x;
			--y;
			for(; x >= target_x; --x, --y) {
				
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == CheckerColor.WHITE) {
					return false;
				} else if(temp !=null && temp.getColor() == CheckerColor.BLACK
						&& coordinatesToDelete != null) {
					coordinatesToDelete.add(new Coordinate (x, y));
				}
				
			}
			
			if(x == target_x && y == target_y) {
				return true;
			}
			
			
		// cel: prawy gorny rog
		} else if(!isTargetToTheLeft && isTargetToTheTop && color == CheckerColor.BLACK) {
			++x;
			--y;
			for(; x <= target_x; ++x, --y) {
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == CheckerColor.BLACK) {
					return false;
				} else if(temp !=null && temp.getColor() == CheckerColor.WHITE
						&& coordinatesToDelete != null) {
					coordinatesToDelete.add(new Coordinate (x, y));
				}
				
			}
			
			if(x == target_x && y == target_y) {
				return true;
			}
			
			
		// cel: prawy dolny rog
		} else if(!isTargetToTheLeft && !isTargetToTheTop && color == CheckerColor.WHITE) {
			++x;
			++y;
			for(; x <= target_x; ++x, ++y) {
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == CheckerColor.WHITE) {
					return false;
				} else if(temp !=null && temp.getColor() == CheckerColor.BLACK
						&& coordinatesToDelete != null) {
					coordinatesToDelete.add(new Coordinate (x, y));
				}
			}
			
			if(x == target_x && y == target_y) {
				return true;
			}
			
		}
		
		return false;
	}
	

	/**
	 * Znajduje zaznaczony pionek
	 */
	public final Coordinate getSelectedCheckerCoordinate() {
		for(int x=0; x<8; ++x) {
			for(int y=0; y<8; ++y) {
				if(board.getField(x, y).isSelected()) {
					return new Coordinate(x, y);
				}
			}
		}
		throw new RuntimeException("hm");
	}
	
	/**
	 * Odznacza zaznaczonego obecnie pionka. Jeżeli odznaczenie jest niedozwolone
	 * (taka sytuacja zachodzi gdy gracz ma wymuszone bicie), to rzuca wyjątek
	 * 
	 * @throws RuntimeException jeżeli gracz ma wymuszone bicie
	 * @throws RuntimeException jeżeli żaden pionek nie jest zaznaczony
	 */
	public final void unselectChecker() {
		for(int x=0; x<8; ++x) {
			for(int y=0; y<8; ++y) {
				if(board.getField(x, y).isSelected()) {
					board.getField(x, y).unselect();
					return;
				}
			}
		}
		throw new RuntimeException("unselect requested, but no checker was selected");
	}

	/**
	 * Sprawdza, czy na danej pozycji znajduje się zaznaczony pionek.
	 * @param x współrzędna x pozycji do sprawdzenia
	 * @param y współrzędna y pozycji do sprawdzenia
	 * @return true jeżelili na danej pozycji znajduje się pionek i jest on zaznaczony
	 * 		   false jeżeli na danej pozycji nie ma pionka lub pionek jest niezaznaczony
	 */
	public final boolean isCheckerSelected(int x, int y) {
		Field field = board.getField(x, y);
		return field.isSelected();
	}

	/**
	 * Sprawdza, czy na zadanej pozycji znajduje się pionek aktywnego gracza 
	 * (gracz jest aktywny = 'jest jego ruch').
	 * 
	 * @param x współrzędna x pozycji do sprawdzenia
	 * @param y współrzędna y pozycji do sprawdzenia
	 * @return true jeżeli na polu (x, y) znajduje się pionek aktywnego gracza.
	 */
	public final boolean isCurrentPlayerCheckerOnPosition(int x, int y) {
		Checker checker = board.getField(x, y).getChecker();
		return checker != null && (
			   (checker.getColor().isWhite() && active_player == 0) ||
			   (checker.getColor().isBlack() && active_player == 1));
	}

	/**
	 * Zaznacza pionek na pozycji (x, y).
	 * 
	 * @param x współrzędna x pionka do zaznaczenia
	 * @param y współrzędna y pionka do zaznaczenia
	 * 
	 * @throws RuntimeException jeżeli na pozycji (x, y) nie ma pionka aktywnego gracza
	 * @throws RuntimeException jeżeli jakiś pionek jest już zaznaczony
	 */
	public final void selectChecker(int x, int y) {
		board.getField(x, y).select();
	}

	/**
	 * Rozpoczyna nową grę, tj. tworzy planszę, ustawia pionki i ustawia białego gracza
	 * jako aktywnego.
	 */
	public final void startGame() {
		// rozstaw pionki
		board.setUp();
		
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Not yet implemented");
		
	}

	/**
	 * Generuje makiete
	 * @return makieta obecnego stanu gry
	 * @see #Mockup
	 */
	public final Mockup getMockup() {
		Mockup mockup = new Mockup(); // wypelnij mnie
		mockup.setGameState(GameStateMockup.PLAYER_1_MOVE);
		for(int i=0;i<8;++i)
			for(int j=0;j<8;++j)
				mockup.setField(board.getField(i, j).getMockup(), i, j);
		mockup.setPlayers(PlayerMockup.HUMAN_PLAYER, 0);
		mockup.setPlayers(PlayerMockup.AI_PLAYER, 1);
		return mockup;
		
	}

	/**
	 * Sprawdza, czy gracz 1 wygrac�
	 * @return true wtedy i tylko wtedy, gdy spelnione są oba poniższe warunki:
	 * 	+ Gracz 2 jest aktywny
	 *  + Gracz 2 nie ma żadnego dozwolonego ruchu
	 */
	public boolean hasPlayer1Won() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Sprawdza, czy gracz 2 wygrał
	 * @return true wtedy i tylko wtedy, gdy spelnione są oba poniższe warunki:
	 * 	+ Gracz 1 jest aktywny
	 *  + Gracz 1 nie ma żadnego dozwolonego ruchu
	 *  
	 *  @TODO copy-paste programming - moze przedefiniowac interfejs?
	 */
	public boolean hasPlayer2Won() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Zwraca rozmiar planszy.
	 * @return BOARD_SIZE
	 */
	public static int getBoardSize() {
		return BOARD_SIZE;
	}

	/**
	 * Zwraca liste wszystkich dozwolonych ruchow dla danego gracza
	 */
	public ArrayList<Move> getAllPossibleMoves(CheckerColor color) {
		ArrayList<Move> result = new ArrayList<Move>();
		for(int x=0; x<8; ++x) {
			for(int y=0; y<8; ++y) {
				if(board.getField(x, y).getChecker().getColor() == color) {
					result.addAll(getAllPossibleMovesFromPosition(x, y));
				}
			}
		}
		return result;
	}

	/**
	 * Zwraca listę dozwolonych ruchół z danego pola
	 * @param x
	 * @param y
	 * @return
	 */
	private ArrayList<Move> getAllPossibleMovesFromPosition(int source_x, int source_y) {
		ArrayList<Move> result = new ArrayList<Move>();
		for(int target_x=0; target_x<8; ++target_x) {
			for(int target_y=0; target_y<8; ++target_y) {
				if(true) {
					//result.add();
				}
			}
		}
		return result;
	}
	
	
}
 
