package model;

import java.util.ArrayList;

import common.Coordinate;
import common.Mockup;
import common.PlayerMockup;

public class Model implements AIGameInterface {
	static Model instance;
	
    /* rozmiar planszy */
    final static int BOARD_SIZE = 8;

    /* po ile rzedow pionkow rozstawic na starcie (dla wymiaru 8 jest to najczesciej
    3, dla wymiaru 10 - 4) */
    private final static int INITIAL_CHECKERS_ROWS = 3;

    /* odnosnik do planszy */
    static Board board;

    /* gracze */
    static public Player players[];

    /* numer gracza ktorego ruch byl ostatni */
    private int whoseWasLastMove;
    
    /* numer aktywnego gracze */
    static GameState gameState;

    /**
     * Konstruktor.
     */
    public Model() {
        board = new Board(Model.BOARD_SIZE, Model.INITIAL_CHECKERS_ROWS);
        players = new Player[2];
        whoseWasLastMove = -1;
        gameState = GameState.NOT_STARTED;
        instance = this;
    }
    
    /**
     * Metoda obslugi klikniecia wolana z kontrolera
     */
    public boolean processHumanMove(final int x, final int y) {
        //gameState = GameState.PLAYER_1_MOVE;
        
        
        if(isCheckerSelected(x, y)) {
            unselectChecker();
        } else if(isCurrentPlayerCheckerOnPosition(x, y)) {
            if(isAnyCheckerSelected()) {
                unselectChecker();
            }
            selectChecker(x, y);
        } else if(isAnyCheckerSelected()) {
            Coordinate startCoordinate = getSelectedCheckerCoordinate();
            Coordinate endCoordinate = new Coordinate(x, y);
            
            boolean isNotASingleMove = isMoveACapture(new Move(startCoordinate, endCoordinate));
            
            
            if(moveSelectedCheckerTo(x, y)) {
                setWhoseWasLastMove(0);
                
                isNotASingleMove &= checkCapturesFromPosition(x, y);
                if(!isNotASingleMove) {
                    changeActivePlayer();
                }
                
                return true;
            } else {
                gameState = GameState.PLAYER_1_MOVE_REPEAT_MOVE;
            }
        } else {
            
            // kliknieto puste pole, ignorujemy
            System.out.println("empty field clicked; ignoring");
            gameState = GameState.PLAYER_1_MOVE_REPEAT_MOVE;
        }
        return false;
        
    }

    /**
     * @return true wtedy i tylko wtedy, gdy jakikolwiek pionek jest zaznaczony
     */
    private final boolean isAnyCheckerSelected() {
        for(int x = 0; x < BOARD_SIZE; ++x) {
            for(int y = 0; y < BOARD_SIZE; ++y) {
                if(board.getField(x, y).isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Przemieszcza zaznaczony pionek na pole o wspolrzednych (targetX, targetY)
     *
     * Mozliwe warianty wykonania ruchu:
     * + jezeli jakis pionek jest zaznaczony, a docelowe pole znajduje sie 1 pole
     *   naprzod (po skosie) od obecnej pozycji pionka (zwykly ruch), to pionek
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
     * @param targetX wspolrzedna x docelowego pola
     * @param targetY wspolrzedna y docelowego pola
     * @return true jezeli ruch jest dozwolony i zostac wykonany
     *            false jezeli ruch jest niedozwolony; w takim wypadku zadne zmiany nie zostaja
     *            wprowadzone do modelu
     */
    private final boolean moveSelectedCheckerTo(final int targetX, final int targetY) {
    	
    	if(!areCoordinatesOnBoard( targetX, targetY)) {
            return false;
    	}
    	
        Coordinate source_coordinate = getSelectedCheckerCoordinate();
        int sourceX = source_coordinate.getX();
        int sourceY = source_coordinate.getY();
        //System.out.println("a " + players[active_player].isCpu());
        //System.out.println("b " + players[active_player].getPlayerColor());
        //System.out.println("c " + sourceX + " " + sourceY);
        //System.out.println("d " + targetX + " " + targetY);
        Checker sourceChecker = board.getField(sourceX, sourceY).getChecker();
        boolean correctMove = true;
        boolean forcedCapture = false;
        
        ArrayList<Coordinate> coordinatesToCapture = new ArrayList<Coordinate>();
        if(checkAllPossibleCaptures(sourceChecker.getColor(), coordinatesToCapture)) {
            forcedCapture = true;
        }
        coordinatesToCapture = null;
        
        if(board.getField(targetX, targetY).getChecker() == null) {
            if(sourceChecker.getType() == CheckerType.QUEEN) {
                correctMove = Queen.makeMove(sourceX, sourceY, targetX, targetY, forcedCapture);
            } else {
                correctMove = NormalChecker.makeMove(sourceX, sourceY, targetX, targetY, forcedCapture);
            }
        } else {
            // docelowe pole jest zajete
            correctMove = false;
        }
        unselectChecker();
        if(correctMove) {
        	saveCorrectMove(sourceX, sourceY, targetX, targetY);
        }
        return correctMove;
    }
    
    /**
     * Ustawia ostatni poprawny ruch dla aktywnego gracza
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     */
    private void saveCorrectMove(final int sourceX, final int sourceY, 
            final int targetX, final int targetY) {
        Player player = getActivePlayer() == 0 ? players[0] : players[1];        
        player.setLastMove(new Move(sourceX, sourceY, targetX, targetY));
    }
    
    /**
     * Wykonanie ruchu gracza komputerowego.
     * @return jesli ruch poprawny
     */
    public boolean makeAIMove() {
//    	ArrayList<Coordinate> queenDeletedCheckers = Queen.deletedCheckers;
//    	ArrayList<Coordinate> normalDeletedCheckers = NormalChecker.deletedCheckers;
    	
    	ArrayList<Move> AIMoves = new ArrayList<Move>();
    	boolean isNotASingleMove = true;
    	boolean isMoveCorrect = false;
        while(!isMoveCorrect && isAITurnPossible(AIMoves)) {
        	AI ai = new AI(new GamePosition(board, gameState));
        	
        	GameLevel level = getPlayer(0).getGameLevel();
        	int miliseconds = 
        			level == GameLevel.EASY?    500:
        			level == GameLevel.MEDIUM? 1500:
        		  /*level == GameLevel.HARD*/  3000;
        	
        	ai.buildTree(miliseconds);
        	Move selectedMove = (Move)ai.getBestMove();
        	
            selectChecker(selectedMove.getStartX(), selectedMove.getStartY());
            Coordinate startCoordinate = getSelectedCheckerCoordinate();
            Coordinate endCoordinate = new Coordinate(selectedMove.getEndX(), selectedMove.getEndY());
            
            isNotASingleMove = isMoveACapture(new Move(startCoordinate, endCoordinate));
            
            if(moveSelectedCheckerTo(selectedMove.getEndX(), selectedMove.getEndY())) {
                isNotASingleMove &= checkCapturesFromPosition(selectedMove.getEndX(), selectedMove.getEndY());
                
                isMoveCorrect = true;
                setWhoseWasLastMove(1);
                
                
                if(!isNotASingleMove) {
                    changeActivePlayer();
                }
                
            }
            AIMoves.clear();
        }
        
        
//      Queen.deletedCheckers = queenDeletedCheckers;
//    	NormalChecker.deletedCheckers = normalDeletedCheckers;
    	
        
        return !isNotASingleMove;
    	
        
	}

	private boolean areCoordinatesOnBoard(final int x, final int y) {
    	
    	return !(x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE);
    }

    /**
     * Ogolna metoda sprawdzajaca czy dany ruch jest poprawny dla pionka dowolnego typu
     *
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return
     */
    private boolean isMoveCorrect(final int sourceX, final int sourceY, 
            final int targetX, final int targetY)
    {
        CheckerType type = board.getField(sourceX, sourceY).getChecker().getType();
        if(type == CheckerType.NORMAL) {
            return NormalChecker.isMoveCorrect(sourceX, sourceY, targetX, targetY);
        } else {
            return Queen.checkMove(sourceX, sourceY, targetX, targetY, null);
        }
    }

    /**
     * Znajduje zaznaczony pionek
     */
    private final Coordinate getSelectedCheckerCoordinate() {
        for(int x=0; x<8; ++x) {
            for(int y=0; y<8; ++y) {
                if(board.getField(x, y).isSelected()) {
                    return new Coordinate(x, y);
                }
            }
        }
        throw new RuntimeException("haven't found any selected checker");
    }

    /**
     * Odznacza zaznaczonego obecnie pionka. Jezeli odznaczenie jest niedozwolone
     * (taka sytuacja zachodzi gdy gracz ma wymuszone bicie), to rzuca wyjatek
     *
     * @throws RuntimeException jezeli gracz ma wymuszone bicie
     * @throws RuntimeException jezeli żaden pionek nie jest zaznaczony
     */
    private final void unselectChecker() {
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
     * Zmiana gracza wykonujacego ruch.
     */
    private final void changeActivePlayer() {
        gameState = getActivePlayer() == 1 ? GameState.PLAYER_1_MOVE : GameState.PLAYER_2_MOVE;
    }
    
    /**
     * Przypisuje czyj byl ostatni wykonany ruch
     * @param i
     */
    private final void setWhoseWasLastMove(final int i) {
        whoseWasLastMove = i;
    }

    /**
     * Sprawdza, czy na danej pozycji znajduje sie zaznaczony pionek
     * @param x wspolrzedna x pozycji do sprawdzenia
     * @param y wspolrzedna y pozycji do sprawdzenia
     * @return true jezeli na danej pozycji znajduje sie pionek i jest on zaznaczony
     *            false jezeli na danej pozycji nie ma pionka lub pionek jest niezaznaczony
     */
    private final boolean isCheckerSelected(final int x, final int y) {
    	if(areCoordinatesOnBoard(x, y)) {
    		Field field = board.getField(x, y);
            return field.isSelected();
    	}
        return false;
    }

    /**
     * Sprawdza, czy na zadanej pozycji znajduje sie pionek aktywnego gracza
     * (gracz jest aktywny = 'jest jego ruch')
     *
     * @param x wspolrzedna x pozycji do sprawdzenia
     * @param y wspolrzedna y pozycji do sprawdzenia
     * @return true jezeli na polu (x, y) znajduje sie pionek aktywnego gracza
     */
    private final boolean isCurrentPlayerCheckerOnPosition(final int x, final int y) {

    	if(!areCoordinatesOnBoard(x, y)) {
            return false;
    	}
    	
        Checker checker = board.getField(x, y).getChecker();
        if(checker == null) {
            return false;
        }
        
        if(getActivePlayer() == 0 && checker.getColor() == players[0].getPlayerColor()
                || getActivePlayer() == 1 && checker.getColor() == players[1].getPlayerColor() ) {
            return true;
        }
        return false;
        
  
    }

    /**
     * Zaznacza pionek na pozycji (x, y)
     *
     * @param x wspolrzedna x pionka do zaznaczenia
     * @param y wspolrzedna y pionka do zaznaczenia
     */
    private final void selectChecker(final int x, final int y) {
        board.getField(x, y).select();
    }

    /**
     * Rozpoczyna nowa gre, tj. tworzy plansze, ustawia pionki i ustawia bialego gracza
     * jako aktywnego
     */
    public final void startGame(final String playerName, 
            final GameLevel gameLevel, final CheckerColor checkerColor) {

        players[0] = new Player("player", checkerColor, false, gameLevel);
        players[1] = new Player("CPU", CheckerColor.getOppositeColor(checkerColor), true, null);
        gameState = players[0].getPlayerColor() == CheckerColor.WHITE ? 
                GameState.PLAYER_1_MOVE : GameState.PLAYER_2_MOVE;
        // rozstaw pionki
        board.clean();
        board.setUp();
    }

    /**
     * Generuje makiete
     * @return makieta obecnego stanu gry
     * @see #Mockup
     */
    public final Mockup getMockup() {
        Mockup mockup = new Mockup(getGameStateMockupValue());
        for(int i = 0; i < BOARD_SIZE;++i)
            for(int j = 0; j < BOARD_SIZE;++j)
                mockup.setField(board.getField(i, j).getMockup(), i, j);
        mockup.setPlayers(PlayerMockup.HUMAN_PLAYER, 0);
        mockup.setPlayers(PlayerMockup.AI_PLAYER, 1);
        
        Move move = null;
        // ostatni ruch nalezal do gracza ludzkiego
        if(whoseWasLastMove == 0) {
            move = players[0].getLastMove();
            
        // ostatni ruch nalezal do cpu
        } else {
            move = players[1].getLastMove();
        }
        
        if(move != null) {
            mockup.setLastMove(move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        }
        
        for(Coordinate coordinate : Queen.deletedCheckers) {
            mockup.addCoordinate(coordinate);
        }
        
        for(Coordinate coordinate : NormalChecker.deletedCheckers) {
            mockup.addCoordinate(coordinate);
        }
        /*
        for(Coordinate c: mockup.getDeletedCheckers()) {
           System.out.println("DELETED: "+c.getX()+" "+c.getY());
        }
        
        if(mockup.getLastMove() != null) {
            System.out.println("mockup: "+mockup.getLastMove().getStartX()+" "+mockup.getLastMove().getStartY()
                    +" "+mockup.getLastMove().getEndX()+ " "+mockup.getLastMove().getEndY());
        }*/
        Queen.deletedCheckers.clear();
        NormalChecker.deletedCheckers.clear();
        
        return mockup;
    }
    
    /**
     * Przetlumaczenie wartosci z GameState na GameStateMockup
     * @return
     */
    private int getGameStateMockupValue() {
        
        if(players[0].getPlayerColor() == CheckerColor.WHITE 
                || gameState == GameState.WITHDRAW || gameState == GameState.NOT_STARTED) {
            return gameState.getValue();
        } else if (gameState == GameState.PLAYER_1_MOVE 
                || gameState == GameState.PLAYER_1_MOVE_REPEAT_MOVE || gameState == GameState.PLAYER_1_WON) {
            return gameState.getValue()+1;
            
        } else {
            return gameState.getValue()-1;
            
        }
        
    }

    /**
     * Sprawdza warunek konca gry
     * @return wygrany zawodnik
     */
    public Player checkIfAnyPlayerWon() {
        // sprawdzenie czy pierwszy gracz wygral
        if(hasPlayerWon(players[0])) {
            gameState = GameState.PLAYER_1_WON;
            return getPlayer(0);
        // sprawdzenie czy drugi gracz wygral
        } else if(hasPlayerWon(players[1])) {
            gameState = GameState.PLAYER_2_WON;
            return getPlayer(1);
        }
        return null;
    }

    /**
     * Sprawdza czy nastapil warunek remisu
     * @return true jesli remis
     */
    public boolean checkIfWithdraw() {
        // sprawdzenie czy jeden i drugi gracz nie maja mozliwosci ruchu
        
        if( !checkAllPossibleMoves(CheckerColor.BLACK, null) && 
                !checkAllPossibleMoves(CheckerColor.WHITE, null)) {
            gameState = GameState.WITHDRAW;
            return true;
        }
        
        return false;
    }

    /**
     * Sprawdza czy podany w parametrze gracz wygral rozgrywke
     *
     * @param player - gracz dla ktorego sprawdzamy warunek zwyciestwa
     * @return true jesli gracz wygral
     */
    private boolean hasPlayerWon(final Player player) {
        CheckerColor color = player.getPlayerColor();

        // sprawdzenie czy przeciwnik ma mozliwosc ruchu (moze rowniez nie miec pionkow)
        if(checkAllPossibleMoves(CheckerColor.getOppositeColor(color), null) ) {
            return false;
        }

        return true;
    }

    /**
     * Zwraca rozmiar planszy
     * @return BOARD_SIZE
     */
    public static int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Zwraca konkretnego gracza
     * @return player(index)
     */
    private Player getPlayer(final int i) {
        return players[i];
    }

   
    /**
     * Sprawdza czy gracz posiada dozwolony ruch i wpisuje liste ruchow
     * 
     * @param color - sprawdzany kolor pionkow
     * @param result - tablica ruchow do uzupelnienia
     * @return true jesli istnieje chociaz 1 dozwolony ruch
     */
    private boolean checkAllPossibleMoves(CheckerColor color, ArrayList<Move> result) {
        boolean isAnyPossibleMove = false;
        for(int y=0; y<BOARD_SIZE; ++y) {
            for(int x=0; x<BOARD_SIZE; ++x) {
                if(board.getField(x, y).getChecker() != null &&
                   board.getField(x, y).getChecker().getColor() == color) {
                    isAnyPossibleMove |= checkAllPossibleMovesFromPosition(x, y, result);
                }
            }
        };
        return isAnyPossibleMove;
    }

   
    /**
     * Sprawdza czy istnieje dozwolony ruch z danego pola i wpisuje ich liste
     * 
     * @param sourceX - wspolrzedna x pola sprawdzanego
     * @param sourceY - wspolrzedna y pola sprawdzanego
     * @param result - tablica ruchow do uzupelnienia
     * @return true jesli istnieje chociaz 1 dozwolony ruch
     */
    private boolean checkAllPossibleMovesFromPosition(int sourceX, int sourceY, ArrayList<Move> result) {
        boolean isAnyPossibleMove = false;
        for(int targetX=0; targetX<BOARD_SIZE; ++targetX) {
            for(int targetY=0; targetY<BOARD_SIZE; ++targetY) {
                if(isMoveCorrect(sourceX, sourceY, targetX, targetY)) {
                    if(result != null) {
                        result.add(new Move(sourceX, sourceY, targetX, targetY));
                    }
                    isAnyPossibleMove = true;
                }
            }
        }
        return isAnyPossibleMove;
    }
    
    /**
     * Sprawdza czy podany ruch jest biciem.
     * @param moveToCheck
     * @return true jesli ruch jest biciem
     */
    private boolean isMoveACapture(final Move moveToCheck) {
        Checker sourceChecker = board.getField(moveToCheck.getStartX(), 
                moveToCheck.getStartY()).getChecker();
        
        // jesli pionek jest dama
        if(sourceChecker != null && sourceChecker.getType() == CheckerType.QUEEN) {
            return Queen.isMoveACapture(moveToCheck);
            
        // jesli pionek jest zwyklym pionkiem
        } else if(sourceChecker !=null) {
            return NormalChecker.isMoveACapture(moveToCheck);
        }
        
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie dla pionkow danego koloru.
     * Jesli sa mozliwe bicia, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param color
     * @param coordinatesToCapture
     * @return true jesli bicie mozliwe
     */
    private boolean checkAllPossibleCaptures(final CheckerColor color, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        boolean isPossibleCapture = NormalChecker.checkPossibleCaptures(color, coordinatesToCapture);
        isPossibleCapture |= Queen.checkPossibleCaptures(color, coordinatesToCapture);
        return isPossibleCapture;
    }

    /**
     * Sprawdza czy w obecnej turze ruch nalezy do komputera
     * @return true jesli tura komputera
     */
    public boolean isAITurn() {
        return players[getActivePlayer()].isCpu();
    }
    
    /**
     * Sprawdza czy w obecnej turze jest mozliwy ruch komputera
     * @return true jesli tura komputera i ma sie gdzie ruszyc
     */
    public boolean isAITurnPossible(ArrayList<Move>AIMoves) {
        return isAITurn() && checkAllPossibleMoves(players[getActivePlayer()].getPlayerColor(), AIMoves);
    }
    
    private int getActivePlayer() {
		return gameState == GameState.PLAYER_1_MOVE || gameState == GameState.PLAYER_1_MOVE_REPEAT_MOVE?
				0: 1;
	}

	/**
     * Sprawdza czy mozliwe jest bicie z danej pozycji
     * @param x
     * @param y
     * @return true jesli bicie jest mozliwe
     */
    private boolean checkCapturesFromPosition(final int x, final int y) {
        
        boolean isCapturePossible = false;
        Checker checker = board.getField(x, y).getChecker();
        ArrayList<Coordinate> list = new ArrayList<Coordinate>();
        if(checker != null && checker.getType() == CheckerType.NORMAL) {
            isCapturePossible |= 
                    NormalChecker.checkPossibleCapturesFromChecker(checker, null);
        } else if(checker != null){
            isCapturePossible |= 
                    Queen.checkPossibleCapturesFromQueen(checker, list);
        }
        
        return isCapturePossible;
    }

    public ArrayList<Move> getPossibleMoves() {
    	ArrayList<Move> possibleMoves = new ArrayList<Move>();
    	ArrayList<Coordinate> captures = new ArrayList<Coordinate>();
    	checkAllPossibleMoves(getCurrentPlayerColor(), possibleMoves);
    	if(checkAllPossibleCaptures(getCurrentPlayerColor(), captures)) {
        	ArrayList<Move> result = new ArrayList<Move>();
    		for(Move move: possibleMoves) {
    			if(isMoveACapture(move)) {
    				result.add(move);
    			}
    		}
        	return result;
    	}
    	return possibleMoves;
    }
    
	@Override
	public AIGameState getCurrentState() {
		throw new RuntimeException();
	}
	
	public CheckerColor getCurrentPlayerColor() {
		return players[getActivePlayer()].getPlayerColor();
	}

	public void performMove(Move move) {
		//System.out.println(move);
		selectChecker(move.getStartX(), move.getStartY());
        boolean isNotASingleMove = isMoveACapture(move);        
        moveSelectedCheckerTo(move.getEndX(), move.getEndY());
        isNotASingleMove &= checkCapturesFromPosition(move.getEndX(), move.getEndY());
        if(!isNotASingleMove) {
            changeActivePlayer();
        }
        checkIfAnyPlayerWon();
        checkIfWithdraw();
        
        Queen.deletedCheckers.clear();
        NormalChecker.deletedCheckers.clear();
        
        //saveCorrectMove(move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
       // unselectChecker();
	}
}
