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
	
	/// po ile rzedow pionkow rozstawic na starcie (dla wymiaru 8 jest to najczesciej
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
		players[0] = new Player("player", CheckerColor.WHITE, false, null);
		players[1] = new Player("CPU", CheckerColor.BLACK, true, GameLevel.EASY);
		this.active_player = 0;
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
	 * Przemieszcza zaznaczony pionek na pole o wspolrzednych (target_x, target_y).
	 * 
	 * Mozliwe warianty wykonania ruchu:
	 * + jezeli jakis pionek jest zaznaczony, a docelowe pole znajduje sie 1 pole 
	 * 	 naprzod (po skosie) od obecnej pozycji pionka (zwykly ruch), to pionek 
	 *   przesuwa sie na zadane pole, nastepuje zmiana kolejki, a pionek zostaje 
	 *   odznaczony; UWAGA: zwykly ruch jest dozwolony tylko wtedy, gdy gracz nie
	 *   ma zadnego bicia
	 * + jezeli jakis pionek jest zaznaczony, a docelowe pole znajduje sie 2 pola 
	 *   naprzod od niego i miedzy polem docelowym a obecnym znajduje sie pionek 
	 *   przeciwnika (bicie), wowczas pionek przesuwa sie 2 na pole docelowe, a pionek 
	 *   pzreciwnika zostaje usuniety dodatkowo przy biciu funkcja musi sprawdzic, 
	 *   czy jest mozliwe kolejne bicie; 
	 *   jezeli nie, to:
	 * ++ nastepuje zmiana kolejki
	 * ++ pionek zostaje odznaczony
	 *   jezeli tak, to:
	 * ++ nie nastepuje zmiana kolejki
	 * ++ pionek nie zostaje odznaczony
	 * ++ w nastepnej kolejce pionek zostaje zablokowany, tj. gracz nie moze go odznaczyc,
	 *    musi wykonac bicie tym pionkiem
	 * + jezeli nie zachodzi zaden z powyzszych przypadkow, to ruch jest uznawany 
	 *   za niepoprawny; funkcja rzuca wyjatek, nie zostajz wprowadzone zadne zmiany 
	 *   w modelu
	 * 
	 * @param target_x wspolrzedna x docelowego pola
	 * @param target_y wspolrzedna y docelowego pola
	 * @return true jezeli ruch jest dozwolony i zostac� wykonany
	 * 		   false jezeli ruch jest niedozwolony; w takim wypadku zadne zmiany nie zostaja
	 * 		   wprowadzone do modelu
	 */
	public final boolean moveSelectedCheckerTo(int target_x, int target_y) {
		Coordinate source_coordinate = getSelectedCheckerCoordinate();
		int source_x = source_coordinate.getX();
		int source_y = source_coordinate.getY();
		System.out.println("a" + players[active_player].isCpu());
		System.out.println("b" + players[active_player].getPlayerColor());
		System.out.println("c" + source_x + " " + source_y);
		System.out.println("d" + target_x + " " + target_y);
		CheckerType checkerType = board.getField(source_x, source_y).getChecker().getType();
		boolean correctMove;
		if(board.getField(target_x, target_y).getChecker() == null)
		{
			if(checkerType == CheckerType.QUEEN) {
				correctMove = makeQueenMove(source_x, source_y, target_x, target_y);
			} else {
				correctMove = makeNormalCheckerMove(source_x, source_y, target_x, target_y);
			}
		}
		else
		{
			// docelowe pole jest zajete
			correctMove = false;
		}
		unselectChecker();
		if(correctMove)
		{
			changeActivePlayer();
			while(isAITurn())
			{
				makeAIMove();
			}
		}
		return correctMove;
	}
	
	/**
	 * Wykonuje ruch zwyklego pionka, sprawdzajac jego poprawnosc
	 * 
	 * @param source_x - wspolrzedna X poczatkowej pozycji
	 * @param source_y - wspolrzedna Y poczatkowej pozycji
	 * @param target_x - wspolrzedna X koncowej pozycji
	 * @param target_y - wspolrzedna Y koncowej pozycji
	 * @return true jesli ruch zostal wykonany
	 */
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

	/**
	 * Sprawdza czy zwykly pionek moze stac sie dama
	 * 
	 * @param target_x - wspolrzedna X koncowej pozycji
	 * @param target_y - wspolrzedna Y koncowej pozycji
	 */
	private void checkQueenCondition(final int target_x, final int target_y) {
		if(target_y == 0 || target_y == BOARD_SIZE-1) {
			board.getField(target_x, target_y).getChecker().promote();
		}
	}

	/**
	 * Ogolna metoda sprawdzajaca czy dany ruch jest poprawny dla pionka dowolnego typu
	 * 
	 * @param source_x - wspolrzedna X poczatkowej pozycji
	 * @param source_y - wspolrzedna Y poczatkowej pozycji
	 * @param target_x - wspolrzedna X koncowej pozycji
	 * @param target_y - wspolrzedna Y koncowej pozycji
	 * @return
	 */
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
	
	/**
	 * Sprawdza czy ruch normalnego pionka jest poprawny
	 * 
	 * @param source_x - wspolrzedna X poczatkowej pozycji
	 * @param source_y - wspolrzedna Y poczatkowej pozycji
	 * @param target_x - wspolrzedna X koncowej pozycji
	 * @param target_y - wspolrzedna Y koncowej pozycji
	 * @return true jesli ruch jest dozwolony
	 */
	private boolean isNormalCheckerMoveCorrect(int source_x, int source_y, int target_x, int target_y) {
		return (board.getField(target_x, target_y).getChecker() == null) &&
			( 
				isNormalCheckerCaptureMoveCorrect(source_x, source_y, target_x, target_y)
				|| 
				isNormalCheckerNormalMoveCorrect(source_x, source_y, target_x, target_y)
			);
	}
	
	/**
	 * Sprawdza czy dane bicie jest poprawne dla normalnego pionka
	 * 
	 * @param source_x - wspolrzedna X poczatkowej pozycji
	 * @param source_y - wspolrzedna Y poczatkowej pozycji
	 * @param target_x - wspolrzedna X koncowej pozycji
	 * @param target_y - wspolrzedna Y koncowej pozycji
	 * @return
	 */
	private boolean isNormalCheckerCaptureMoveCorrect(int source_x, int source_y, int target_x, int target_y) {
		boolean isTargetToTheLeft = target_x == source_x - 2;
		boolean isTargetToTheRight = target_x == source_x + 2;
		boolean isTargetToTheTop = target_y == source_y - 2;
		boolean isTargetToTheBottom = target_y == source_y + 2;

		int checkerToRemoveX = (target_x + source_x) / 2;
		int checkerToRemoveY = (target_y + source_y) / 2; 

		Checker MovingChecker = board.getField(source_x, source_y).getChecker();
		Checker CheckerToRemove = board.getField(checkerToRemoveX, checkerToRemoveY).getChecker();
		
		return((isTargetToTheLeft  && isTargetToTheTop)
			|| (isTargetToTheLeft  && isTargetToTheBottom)
			|| (isTargetToTheRight && isTargetToTheTop)
			|| (isTargetToTheRight && isTargetToTheBottom))
			&& MovingChecker != null && CheckerToRemove != null 
			&& CheckerToRemove.getColor() != MovingChecker.getColor();
	}

	/**
	 * Sprawdza czy dany ruch zwyklego pionka jest poprawny
	 * 
	 * @param source_x - wspolrzedna X poczatkowej pozycji
	 * @param source_y - wspolrzedna Y poczatkowej pozycji
	 * @param target_x - wspolrzedna X koncowej pozycji
	 * @param target_y - wspolrzedna Y koncowej pozycji
	 * @return true jesli ruch jest dozwolony
	 */
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

	/**
	 * Wykonuje dany ruch dla damy, sprawdzajac czy ten ruch jest poprawny
	 * 
	 * @param source_x - wspolrzedna X poczatkowej pozycji
	 * @param source_y - wspolrzedna Y poczatkowej pozycji
	 * @param target_x - wspolrzedna X koncowej pozycji
	 * @param target_y - wspolrzedna Y koncowej pozycji
	 * @return true, jesli ruch zostal wykonany
	 */
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
		if(board.getField(target_x, target_y).getChecker() != null &&
				target_x < BOARD_SIZE && target_y < BOARD_SIZE &&
				target_x >= 0 && target_y >= 0) {
			return false;
		}
		
		Checker checker = board.getField(source_x, source_y).getChecker();
		CheckerColor color = checker.getColor();
		boolean isTargetToTheLeft = target_x < source_x ? true : false;
		boolean isTargetToTheTop = target_y < source_y ? true : false;
		
		int x = board.getField(source_x, source_y).getChecker().getPositionX();
		int y = board.getField(source_x, source_y).getChecker().getPositionY();
		CheckerColor colorSource = board.getField(x, y).getChecker().getColor();
		
		
		// cel: lewy gorny rog
		if(isTargetToTheLeft && isTargetToTheTop) {
			--x;
			--y;
			for(; x > target_x; --x, --y) {
				
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == colorSource) {
					return false;
				} else if(temp !=null && temp.getColor() != colorSource
						&& coordinatesToDelete != null) {
					coordinatesToDelete.add(new Coordinate (x, y));
				}
				
			}
			
			if(x == target_x && y == target_y) {
				return true;
			}
			
		// cel: lewy dolny rog
		} else if(isTargetToTheLeft && !isTargetToTheTop) {
			--x;
			++y;
			for(; x > target_x; --x, ++y) {
				
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == colorSource) {
					return false;
				} else if(temp !=null && temp.getColor() != colorSource
						&& coordinatesToDelete != null) {
					coordinatesToDelete.add(new Coordinate (x, y));
				}
				
			}
			
			if(x == target_x && y == target_y) {
				return true;
			}
			
			
		// cel: prawy gorny rog
		} else if(!isTargetToTheLeft && isTargetToTheTop) {
			++x;
			--y;
			for(; x < target_x; ++x, --y) {
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == colorSource) {
					return false;
				} else if(temp !=null && temp.getColor() != colorSource
						&& coordinatesToDelete != null) {
					coordinatesToDelete.add(new Coordinate (x, y));
				}
				
			}
			
			if(x == target_x && y == target_y) {
				return true;
			}
			
			
		// cel: prawy dolny rog
		} else if(!isTargetToTheLeft && !isTargetToTheTop) {
			++x;
			++y;
			for(; x < target_x; ++x, ++y) {
				Checker temp = board.getField(x, y).getChecker();
				
				if(temp != null && temp.getColor() == colorSource) {
					return false;
				} else if(temp !=null && temp.getColor() != colorSource
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
	 * Odznacza zaznaczonego obecnie pionka. Jezeli odznaczenie jest niedozwolone
	 * (taka sytuacja zachodzi gdy gracz ma wymuszone bicie), to rzuca wyjatek
	 * 
	 * @throws RuntimeException jezeli gracz ma wymuszone bicie
	 * @throws RuntimeException jezeli żaden pionek nie jest zaznaczony
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

	public final void changeActivePlayer()
	{
		if(active_player == 1)
		{
			active_player = 0;
		}
		else
		{
			active_player = 1;
		}
	}
	
	/**
	 * Sprawdza, czy na danej pozycji znajduje sie zaznaczony pionek.
	 * @param x wspolrzedna x pozycji do sprawdzenia
	 * @param y wspolrzedna y pozycji do sprawdzenia
	 * @return true jezeli na danej pozycji znajduje sie pionek i jest on zaznaczony
	 * 		   false jezeli na danej pozycji nie ma pionka lub pionek jest niezaznaczony
	 */
	public final boolean isCheckerSelected(int x, int y) {
		Field field = board.getField(x, y);
		return field.isSelected();
	}

	/**
	 * Sprawdza, czy na zadanej pozycji znajduje sie pionek aktywnego gracza 
	 * (gracz jest aktywny = 'jest jego ruch').
	 * 
	 * @param x wspolrzedna x pozycji do sprawdzenia
	 * @param y wspolrzedna y pozycji do sprawdzenia
	 * @return true jezeli na polu (x, y) znajduje sie pionek aktywnego gracza.
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
	 * @param x wspolrzedna x pionka do zaznaczenia
	 * @param y wspolrzedna y pionka do zaznaczenia
	 * 
	 * @throws RuntimeException jezeli na pozycji (x, y) nie ma pionka aktywnego gracza
	 * @throws RuntimeException jezeli jakis pionek jest juz zaznaczony
	 */
	public final void selectChecker(int x, int y) {
		
		board.getField(x, y).select();
	}

	/**
	 * Rozpoczyna nowa gre, tj. tworzy plansze, ustawia pionki i ustawia bialego gracza
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
	 * Sprawdza warunek konca gry
	 * @return wygrany zawodnik
	 */
	public Player checkIfAnyPlayerWon() {
		if(hasPlayer1Won()) {
			return getPlayer(0);
		} else if(hasPlayer2Won()) {
			return getPlayer(1);
		}
		return null;
	}
	
	/**
	 * Sprawdza czy nastapil warunek remisu
	 * @return true jesli remis
	 */
	public boolean checkIfWithdraw() {
		//TODO
		return false;
		
	}

	/**
	 * Sprawdza, czy gracz 1 wygral
	 * @return true wtedy i tylko wtedy, gdy spelnione sa oba ponizsze warunki:
	 * 	+ Gracz 2 jest aktywny
	 *  + Gracz 2 nie ma zadnego dozwolonego ruchu
	 */
	public boolean hasPlayer1Won() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Sprawdza, czy gracz 2 wygral
	 * @return true wtedy i tylko wtedy, gdy spelnione sa oba ponizsze warunki:
	 * 	+ Gracz 1 jest aktywny
	 *  + Gracz 1 nie ma zadnego dozwolonego ruchu
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
	 * Zwraca tablice z graczami
	 * @return players
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Zwraca konkretnego gracza
	 * @return player(index)
	 */
	public Player getPlayer(final int i) {
		return players[i];
	}

	/**
	 * Zwraca liste wszystkich dozwolonych ruchow dla danego gracza
	 */
	public ArrayList<Move> getAllPossibleMoves(CheckerColor color) {
		ArrayList<Move> result = new ArrayList<Move>();
		for(int x=0; x<8; ++x) {
			for(int y=0; y<8; ++y) {
				if(board.getField(x, y).getChecker() != null && 
				   board.getField(x, y).getChecker().getColor() == color) {
					result.addAll(getAllPossibleMovesFromPosition(x, y));
				}
			}
		}
		return result;
	}

	/**
	 * Zwraca liste dozwolonych ruchow z danego pola
	 * @param x
	 * @param y
	 * @return
	 */
	private ArrayList<Move> getAllPossibleMovesFromPosition(int source_x, int source_y) {
		ArrayList<Move> result = new ArrayList<Move>();
		for(int target_x=0; target_x<8; ++target_x) {
			for(int target_y=0; target_y<8; ++target_y) {
				if(isMoveCorrect(source_x, source_y, target_x, target_y)) {
					System.out.println("dodaje: " + source_x + " " + source_y + " " + target_x + " " + target_y);
					result.add(new Move(source_x, source_y, target_x, target_y));
				}
			}
		}
		return result;
	}

	public boolean isAITurn() {
		return players[active_player].isCpu();
	}

	public void makeAIMove() {
		ArrayList<Move> moves = getAllPossibleMoves(players[active_player].getPlayerColor());
		int movesCount = moves.size();
		System.out.println("liczba dozwolonych ruchow: " + movesCount);
		int randomId = (int) Math.floor(Math.random() * movesCount);
		Move selectedMove = moves.get(randomId);
		System.out.println("wybrany ruch z ["+ selectedMove.startX + ", " + selectedMove.startY +
						   "] na [" + selectedMove.endX + ", " + selectedMove.endY + "]");
		selectChecker(selectedMove.startX, selectedMove.startY);
		moveSelectedCheckerTo(selectedMove.endX, selectedMove.endY);
	}
	
	
	
}
 
