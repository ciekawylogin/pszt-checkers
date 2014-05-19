package model;

import java.util.ArrayList;
import common.GameStateMockup;
import common.Mockup;
import common.PlayerMockup;

public class Model {
    /* rozmiar planszy */
    private final static int BOARD_SIZE = 8;

    /* po ile rzedow pionkow rozstawic na starcie (dla wymiaru 8 jest to najczesciej
    3, dla wymiaru 10 - 4) */
    private final static int INITIAL_CHECKERS_ROWS = 3;

    /* odnosnik do planszy */
    private Board board;

    /* gracze */
    private Player players[];

    /* numer aktywnego gracze */
    private int active_player;

    /**
     * Konstruktor.
     */
    public Model() {
        this.board = new Board(Model.BOARD_SIZE, Model.INITIAL_CHECKERS_ROWS);
        this.players = new Player[2];
        this.active_player = 0;
    }

    /**
     * @return true wtedy i tylko wtedy, gdy jakikolwiek pionek jest zaznaczony
     */
    public final boolean isAnyCheckerSelected() {
        for(int x=0; x<8; ++x) {
            for(int y=0; y<8; ++y) {
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
    public final boolean moveSelectedCheckerTo(int targetX, int targetY) {
        Coordinate source_coordinate = getSelectedCheckerCoordinate();
        int sourceX = source_coordinate.getX();
        int sourceY = source_coordinate.getY();
        System.out.println("a " + players[active_player].isCpu());
        System.out.println("b " + players[active_player].getPlayerColor());
        System.out.println("c " + sourceX + " " + sourceY);
        System.out.println("d " + targetX + " " + targetY);
        Checker sourceChecker = board.getField(sourceX, sourceY).getChecker();
        boolean correctMove = true;;
        boolean forcedCapture = false;
        
        if(checkAllPossibleCaptures(sourceChecker.getColor(), null)) {
            forcedCapture = true;
        }
        
        if(board.getField(targetX, targetY).getChecker() == null) {
            if(sourceChecker.getType() == CheckerType.QUEEN) {
                correctMove = makeQueenMove(sourceX, sourceY, targetX, targetY);
            } else {
                correctMove = makeNormalCheckerMove(sourceX, sourceY, targetX, targetY);
            }
        } else {
            // docelowe pole jest zajete
            correctMove = false;
        }
        
        if(forcedCapture && correctMove) {
            correctMove = isMoveACapture(new Move(sourceX, sourceY, targetX, targetY));
        }
        
        unselectChecker();
        if(correctMove) {
            changeActivePlayer();
            
            ArrayList<Move> AIMoves = new ArrayList<Move>();
            while(isAITurn() && checkAllPossibleMoves(players[active_player].getPlayerColor(), AIMoves)) {
                makeAIMove(AIMoves);
            }
        }
        return correctMove;
    }

    /**
     * Wykonuje ruch zwyklego pionka, sprawdzajac jego poprawnosc
     *
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return true jesli ruch zostal wykonany
     */
    private boolean makeNormalCheckerMove(int sourceX, int sourceY, int targetX, int targetY) {
        if(targetX >= BOARD_SIZE || targetY >= BOARD_SIZE ||
                targetX < 0 || targetY < 0) {
            return false;
        }

        boolean normalMove = isNormalCheckerNormalMoveCorrect(sourceX, sourceY, targetX, targetY);
        boolean captureMove = isNormalCheckerCaptureMoveCorrect(sourceX, sourceY, targetX, targetY);
        boolean correctMove = normalMove || captureMove;
        if(correctMove) {
            Field oldField = board.getField(sourceX, sourceY);
            Checker checker = oldField.getChecker();
            oldField.removeChecker();
            Field newField = board.getField(targetX, targetY);
            newField.setChecker(checker);
            checker.setPositionOnBoard(targetX, targetY);
            checkQueenCondition(targetX, targetY);
        }
        if (captureMove) {
            int checkerToRemoveX = (targetX + sourceX) / 2;
            int checkerToRemoveY = (targetY + sourceY) / 2;
            board.getField(checkerToRemoveX, checkerToRemoveY).removeChecker();
        }
        return correctMove;
    }

    /**
     * Sprawdza czy zwykly pionek moze stac sie dama
     *
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     */
    private void checkQueenCondition(final int targetX, final int targetY) {
        if(targetY == 0 || targetY == BOARD_SIZE-1) {
            board.getField(targetX, targetY).getChecker().promote();
        }
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
    private boolean isMoveCorrect(int sourceX, int sourceY, int targetX, int targetY)
    {
        CheckerType type = board.getField(sourceX, sourceY).getChecker().getType();
        if(type == CheckerType.NORMAL) {
            return isNormalCheckerMoveCorrect(sourceX, sourceY, targetX, targetY);
        } else {
            return checkQueenMove(sourceX, sourceY, targetX, targetY, null);
        }
    }

    /**
     * Sprawdza czy ruch normalnego pionka jest poprawny
     *
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return true jesli ruch jest dozwolony
     */
    private boolean isNormalCheckerMoveCorrect(int sourceX, int sourceY, int targetX, int targetY) {
        return (board.getField(targetX, targetY).getChecker() == null) &&
            (
                isNormalCheckerCaptureMoveCorrect(sourceX, sourceY, targetX, targetY)
                ||
                isNormalCheckerNormalMoveCorrect(sourceX, sourceY, targetX, targetY)
            );
    }

    /**
     * Sprawdza czy dane bicie jest poprawne dla normalnego pionka
     *
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return
     */
    private boolean isNormalCheckerCaptureMoveCorrect(int sourceX, int sourceY, int targetX, int targetY) {
        boolean isTargetToTheLeft = targetX == sourceX - 2;
        boolean isTargetToTheRight = targetX == sourceX + 2;
        boolean isTargetToTheTop = targetY == sourceY - 2;
        boolean isTargetToTheBottom = targetY == sourceY + 2;

        int checkerToRemoveX = (targetX + sourceX) / 2;
        int checkerToRemoveY = (targetY + sourceY) / 2;

        Checker MovingChecker = board.getField(sourceX, sourceY).getChecker();
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
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return true jesli ruch jest dozwolony
     */
    private boolean isNormalCheckerNormalMoveCorrect(int sourceX, int sourceY, int targetX, int targetY) {
        Checker checker = board.getField(sourceX, sourceY).getChecker();
        CheckerColor color = checker.getColor();
        boolean isTargetToTheLeft = targetX == sourceX - 1;
        boolean isTargetToTheRight = targetX == sourceX + 1;
        boolean isTargetToTheTop = targetY == sourceY - 1;
        boolean isTargetToTheBottom = targetY == sourceY + 1;
        if(color == CheckerColor.WHITE) {
            return (isTargetToTheLeft && isTargetToTheBottom) || (isTargetToTheRight && isTargetToTheBottom);
        } else {
            return (isTargetToTheLeft && isTargetToTheTop) || (isTargetToTheRight && isTargetToTheTop);
        }
    }

    /**
     * Wykonuje dany ruch dla damy, sprawdzajac czy ten ruch jest poprawny
     *
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return true, jesli ruch zostal wykonany
     */
    private boolean makeQueenMove(int sourceX, int sourceY, int targetX, int targetY) {
        ArrayList<Coordinate> coordinatesToDelete = new ArrayList<>();
        boolean correctMove = checkQueenMove(sourceX, sourceY, targetX, targetY, coordinatesToDelete);

        if(correctMove) {
            setQueenOnPosition(sourceX, sourceY, targetX, targetY);
            
            // usuwanie potencjalnych ofiar ruchu
            removeCapturedCheckers(coordinatesToDelete);
            
        } else {
            coordinatesToDelete.clear();
        }
        return correctMove;
    }
    
    /**
     * Ustawia pionek damy na wskazanej pozycji usuwajac go ze starej pozycji.
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     */
    private void setQueenOnPosition(final int sourceX, final int sourceY, 
            final int targetX, final int targetY) {
        Field oldField = board.getField(sourceX, sourceY);
        Checker checker = oldField.getChecker();
        oldField.removeChecker();
        Field newField = board.getField(targetX, targetY);
        newField.setChecker(checker);
        checker.setPositionOnBoard(targetX, targetY);
    }
    
    /**
     * Usuwa zbite podczas ruchu pionki
     * @param coordinatesToDelete
     */
    private void removeCapturedCheckers(ArrayList<Coordinate> coordinatesToDelete) {
        for(Coordinate coordToDelete: coordinatesToDelete) {
            int x = coordToDelete.getX();
            int y = coordToDelete.getY();
            board.getField(x, y).removeChecker();
        }
        coordinatesToDelete.clear();
    }

    /**
     * Sprawdza czy ruch damy jest poprawny
     *
     * @param sourceX - wspolrzedna x poczatkowej pozycji
     * @param sourceY - wspolrzedna y poczatkowej pozycji
     * @param targetX - wspolrzedna x koncowej pozycji
     * @param targetY - pozycja y koncowej pozycji
     * @param coordinatesToDelete - tablica zbitych po drodze pionkow
     * @return true gdy ruch poprawny
     */
    private boolean checkQueenMove(final int sourceX, final int sourceY,
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        // sprawdz czy docelowe pole jest puste badz poza plansza
        if(board.getField(targetX, targetY).getChecker() != null &&
                targetX < BOARD_SIZE && targetY < BOARD_SIZE &&
                targetX >= 0 && targetY >= 0) {
            return false;
        }
        boolean isTargetToTheLeft = targetX < sourceX ? true : false;
        boolean isTargetToTheTop = targetY < sourceY ? true : false;

        // cel: lewy gorny rog
        if(isTargetToTheLeft && isTargetToTheTop) {
            return checkQueenMoveToTheLeftTopCorner(sourceX, sourceY, 
                    targetX, targetY, coordinatesToDelete);
            
        // cel: lewy dolny rog
        } else if(isTargetToTheLeft && !isTargetToTheTop) {
            return checkQueenMoveToTheLeftBottomCorner(sourceX, sourceY, 
                    targetX, targetY, coordinatesToDelete);
            
        // cel: prawy gorny rog
        } else if(!isTargetToTheLeft && isTargetToTheTop) {
            return checkQueenMoveToTheRightTopCorner(sourceX, sourceY, 
                    targetX, targetY, coordinatesToDelete);
            
        // cel: prawy dolny rog
        } else if(!isTargetToTheLeft && !isTargetToTheTop) {
            return checkQueenMoveToTheRightBottomCorner(sourceX, sourceY, 
                    targetX, targetY, coordinatesToDelete);
        }
        return false;
    }
    
    /**
     * Sprawdza czy ruch damy w kierunku lewego gornego rogu jest poprawny.
     * Jesli tak, zapisuje wspolrzedne zbitych po drodze pionkow
     * 
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     * @param coordinatesToDelete
     * @return true jesli ruch poprawny
     */
    private boolean checkQueenMoveToTheLeftTopCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        int x = sourceX -1;
        int y = sourceY -1;
        CheckerColor colorSource = board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x > targetX; --x, --y) {
            Checker temp = board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                return false;
            } else if(temp !=null && temp.getColor() != colorSource
                      && coordinatesToDelete != null) {
                coordinatesToDelete.add(new Coordinate (x, y));
            }
        }
        if(x == targetX && y == targetY) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy ruch damy w kierunku prawego gornego rogu jest poprawny.
     * Jesli tak, zapisuje wspolrzedne zbitych po drodze pionkow
     * 
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     * @param coordinatesToDelete
     * @return true jesli ruch poprawny
     */
    private boolean checkQueenMoveToTheRightTopCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        int x = sourceX +1;
        int y = sourceY -1;
        CheckerColor colorSource = board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x < targetX; ++x, --y) {
            Checker temp = board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                return false;
            } else if(temp !=null && temp.getColor() != colorSource
                      && coordinatesToDelete != null) {
                coordinatesToDelete.add(new Coordinate (x, y));
            }
        }
        if(x == targetX && y == targetY) {
            return true;
        }
        return false;
        
    }
    
    /**
     * Sprawdza czy ruch damy w kierunku prawego dolnego rogu jest poprawny.
     * Jesli tak, zapisuje wspolrzedne zbitych po drodze pionkow
     * 
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     * @param coordinatesToDelete
     * @return true jesli ruch poprawny
     */
    private boolean checkQueenMoveToTheRightBottomCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        int x = sourceX +1;
        int y = sourceY +1;
        CheckerColor colorSource = board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x < targetX; ++x, ++y) {
            Checker temp = board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                return false;
            } else if(temp !=null && temp.getColor() != colorSource
                      && coordinatesToDelete != null) {
                coordinatesToDelete.add(new Coordinate (x, y));
            }
        }
        if(x == targetX && y == targetY) {
            return true;
        }
        return false;
        
    }
    
    /**
     * Sprawdza czy ruch damy w kierunku lewego dolnego rogu jest poprawny.
     * Jesli tak, zapisuje wspolrzedne zbitych po drodze pionkow
     * 
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     * @param coordinatesToDelete
     * @return true jesli ruch poprawny
     */
    private boolean checkQueenMoveToTheLeftBottomCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        int x = sourceX -1;
        int y = sourceY +1;
        CheckerColor colorSource = board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x > targetX; --x, ++y) {
            Checker temp = board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                return false;
            } else if(temp !=null && temp.getColor() != colorSource
                      && coordinatesToDelete != null) {
                coordinatesToDelete.add(new Coordinate (x, y));
            }
        }
        if(x == targetX && y == targetY) {
            return true;
        }
        return false;
        
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
     * Zmiana gracza wykonujacego ruch.
     */
    public final void changeActivePlayer() {
        active_player = active_player == 1 ? 0 : 1;
    }

    /**
     * Sprawdza, czy na danej pozycji znajduje sie zaznaczony pionek
     * @param x wspolrzedna x pozycji do sprawdzenia
     * @param y wspolrzedna y pozycji do sprawdzenia
     * @return true jezeli na danej pozycji znajduje sie pionek i jest on zaznaczony
     *            false jezeli na danej pozycji nie ma pionka lub pionek jest niezaznaczony
     */
    public final boolean isCheckerSelected(int x, int y) {
        Field field = board.getField(x, y);
        return field.isSelected();
    }

    /**
     * Sprawdza, czy na zadanej pozycji znajduje sie pionek aktywnego gracza
     * (gracz jest aktywny = 'jest jego ruch')
     *
     * @param x wspolrzedna x pozycji do sprawdzenia
     * @param y wspolrzedna y pozycji do sprawdzenia
     * @return true jezeli na polu (x, y) znajduje sie pionek aktywnego gracza
     */
    public final boolean isCurrentPlayerCheckerOnPosition(int x, int y) {

        Checker checker = board.getField(x, y).getChecker();
        if(checker == null) {
            return false;
        }
        
        if(active_player == 0 && checker.getColor() == players[0].getPlayerColor()
                || active_player == 1 && checker.getColor() == players[1].getPlayerColor() ) {
            return true;
        }
        return false;
        
  
    }

    /**
     * Zaznacza pionek na pozycji (x, y)
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
     * jako aktywnego
     */
    public final void startGame(final String playerName, 
            final GameLevel gameLevel, final CheckerColor checkerColor) {

        players[0] = new Player("player", checkerColor, false, gameLevel);
        players[1] = new Player("CPU", CheckerColor.getOppositeColor(checkerColor), true, null);
        // rozstaw pionki
        board.setUp();
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
        // sprawdzenie czy pierwszy gracz wygral
        if(hasPlayerWon(players[0])) {
            return getPlayer(0);
        // sprawdzenie czy drugi gracz wygral
        } else if(hasPlayerWon(players[1])) {
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
        for(int x=0; x<BOARD_SIZE; ++x) {
            for(int y=0; y<BOARD_SIZE; ++y) {
                if(board.getField(x, y).getChecker() != null &&
                   board.getField(x, y).getChecker().getColor() == color) {
                    if(result != null) {
                        checkAllPossibleMovesFromPosition(x, y, result);
                    }
                    
                    isAnyPossibleMove = true;
                }
            }
        }
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
        for(int targetX=0; targetX<8; ++targetX) {
            for(int targetY=0; targetY<8; ++targetY) {
                if(isMoveCorrect(sourceX, sourceY, targetX, targetY)) {
                    System.out.println("dodaje: " + sourceX + " " + sourceY + " " + targetX + " " + targetY);
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
            return isMoveAQueenCapture(moveToCheck);
            
        // jesli pionek jest zwyklym pionkiem
        } else if(sourceChecker !=null) {
            return isMoveANormalCheckerCapture(moveToCheck);
        }
        return false;
    }
    
    /**
     * Sprawdza czy ruch jest biciem za pomoca zwyklego pionka
     * @param moveToCheck
     * @return true jesli jest to bicie zwyklego pionka
     */
    private boolean isMoveANormalCheckerCapture(final Move moveToCheck) {
        boolean isTargetToTheLeft = moveToCheck.getEndX() == moveToCheck.getStartX() - 2;
        boolean isTargetToTheRight = moveToCheck.getEndX() == moveToCheck.getStartX() + 2;
        boolean isTargetToTheTop = moveToCheck.getEndY() == moveToCheck.getStartY() - 2;
        boolean isTargetToTheBottom = moveToCheck.getEndY() == moveToCheck.getStartY() + 2;
        Checker sourceChecker = board.getField(moveToCheck.getStartX(),
                moveToCheck.getStartY()).getChecker();
        
        // sprawdzenie dla normalnych pionkow
        if(isTargetToTheLeft && isTargetToTheTop && 
                isPossibleNormalCheckerCaptureToTheLeftTopCorner(sourceChecker, null)) {
            return true;
            
        } else if(isTargetToTheRight && isTargetToTheTop && 
                isPossibleNormalCheckerCaptureToTheRightTopCorner(sourceChecker, null)) {
            return true;
            
        } else if(isTargetToTheLeft && isTargetToTheBottom && 
                isPossibleNormalCheckerCaptureToTheLeftBottomCorner(sourceChecker, null)) {
            return true;
            
        } else if(isTargetToTheRight && isTargetToTheBottom && 
                isPossibleNormalCheckerCaptureToTheRightBottomCorner(sourceChecker, null)) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy ruch jest biciem za pomoca damy
     * @param moveToCheck
     * @return true jesli jest to bicie damy
     */
    private boolean isMoveAQueenCapture(final Move moveToCheck) {
        ArrayList<Coordinate> coordinatesToDelete = new ArrayList<Coordinate>();
        checkQueenMove(moveToCheck.getStartX(), moveToCheck.getStartY(),
                moveToCheck.getEndX(), moveToCheck.getEndY(), coordinatesToDelete);
        if(coordinatesToDelete.size() != 0) {
            coordinatesToDelete = null;
            return true;
        }
        
        coordinatesToDelete = null;
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
        
        boolean isPossibleCapture = checkPossibleNormalCheckerCaptures(color, coordinatesToCapture);
        isPossibleCapture |= checkPossibleQueenCaptures(color, coordinatesToCapture);
        
        return isPossibleCapture;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie dla zwyklych pionkow danego koloru.
     * Jesli sa mozliwe bicia, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param color
     * @param coordinatesToCapture
     * @return true jesli bicie mozliwe
     */
    private boolean checkPossibleNormalCheckerCaptures(final CheckerColor color, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        Field fields [][];
        fields = board.getFields();
        Checker checkerOnField;
        boolean isPossibleCapture = false;
        
        for(Field[] boardRow : fields) {
            for(Field field : boardRow) {
                checkerOnField = field.getChecker();
                if(checkerOnField != null && checkerOnField.getColor() == color && 
                        checkPossibleCapturesFromNormalChecker(checkerOnField, coordinatesToCapture)) {
                    isPossibleCapture = true;
                    
                }
            }
        }
        
        return isPossibleCapture;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie dla dam danego koloru.
     * Jesli sa mozliwe bicia, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param color
     * @param coordinatesToCapture
     * @return true jesli bicie jest mozliwe
     */
    private boolean checkPossibleQueenCaptures(final CheckerColor color, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        Checker checkerOnField;
        
        for(Field[] rows : board.getFields()) {
            
            for(Field field : rows) {
                checkerOnField = field.getChecker();
                if(checkerOnField!= null && checkerOnField.getType() == CheckerType.QUEEN && 
                        checkerOnField.getColor() == color) {
                    
                    for(Field[] rowsToCheck : board.getFields()) {
                        
                        for(Field fieldToCheck : rowsToCheck) {
                            if(fieldToCheck.getChecker() == null) {
                                
                                checkQueenMove(checkerOnField.getX(), checkerOnField.getY(), 
                                        fieldToCheck.getX(), fieldToCheck.getY(), coordinatesToCapture);
                            }
                            
                            
                        }
                    }
                }
                
                
            }
        }
        
        if(coordinatesToCapture!= null && coordinatesToCapture.size() != 0) {
            return true;
        }
        
        
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe jakiekolwiek bicie z danego zwyklego pionka.
     * Jesli sa mozliwe bicia, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param sourceChecker
     * @return true jesli bicie mozliwe
     */
    private boolean checkPossibleCapturesFromNormalChecker(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        if(sourceChecker == null) {
            return false;
        }
        // sprawdzenie lewego gornego rogu
        if(isPossibleNormalCheckerCaptureToTheLeftTopCorner(sourceChecker, coordinatesToCapture)) {
            return true;
        }
        // sprawdzenie prawego gornego rogu
        if(isPossibleNormalCheckerCaptureToTheRightTopCorner(sourceChecker, coordinatesToCapture)) {
            return true;
        }
        // sprawdzenie lewego dolnego rogu
        if(isPossibleNormalCheckerCaptureToTheLeftBottomCorner(sourceChecker, coordinatesToCapture)) {
            return true;
        }
        // sprawdzenie prawego dolnego rogu
        if(isPossibleNormalCheckerCaptureToTheRightBottomCorner(sourceChecker, coordinatesToCapture)) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie pionka w kierunku gornego lewego rogu.
     * Jesli jest mozliwe bicie, wpisuje do tablicy wspolrzedne pionka mozliwego do zbicia
     * @param sourceChecker
     * @param coordinatesToCapture
     * @return
     */
    private boolean isPossibleNormalCheckerCaptureToTheLeftTopCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() - 2;
        int targetY = sourceChecker.getY() + 2;
        
        if(targetX < 0 || targetY >= BOARD_SIZE) {
            return false;
        }
        Checker neighbourChecker = board.getField(targetX+1, targetY-1).getChecker();
        Field targetField = board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField != null) {
            if(coordinatesToCapture != null) {
                coordinatesToCapture.add(new Coordinate (targetX, targetY));
            }
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie pionka w kierunku gornego prawego rogu.
     * Jesli jest mozliwe bicie, wpisuje do tablicy wspolrzedne pionka mozliwego do zbicia
     * @param sourceChecker
     * @param coordinatesToCapture
     * @return true jesli bicie mozliwe
     */
    private boolean isPossibleNormalCheckerCaptureToTheRightTopCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() + 2;
        int targetY = sourceChecker.getY() + 2;
        
        if(targetX >= BOARD_SIZE || targetY >= BOARD_SIZE) {
            return false;
        }
        Checker neighbourChecker = board.getField(targetX-1, targetY-1).getChecker();
        Field targetField = board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField != null) {
            if(coordinatesToCapture != null) {
                coordinatesToCapture.add(new Coordinate (targetX, targetY));
            }
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie pionka w kierunku dolnego lewego rogu.
     * Jesli jest mozliwe bicie, wpisuje do tablicy wspolrzedne pionka mozliwego do zbicia
     * @param sourceChecker
     * @param coordinatesToCapture
     * @return true jesli bicie mozliwe
     */
    private boolean isPossibleNormalCheckerCaptureToTheLeftBottomCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() - 2;
        int targetY = sourceChecker.getY() - 2;
        
        if(targetX < 0 || targetY < 0) {
            return false;
        }
        Checker neighbourChecker = board.getField(targetX+1, targetY+1).getChecker();
        Field targetField = board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField != null) {
            if(coordinatesToCapture != null) {
                coordinatesToCapture.add(new Coordinate (targetX, targetY));
            }
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie pionka w kierunku dolnego prawego rogu.
     * Jesli jest mozliwe bicie, wpisuje do tablicy wspolrzedne pionka mozliwego do zbicia
     * @param sourceChecker
     * @param coordinatesToCapture
     * @return true jesli bicie mozliwe
     */
    private boolean isPossibleNormalCheckerCaptureToTheRightBottomCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() + 2;
        int targetY = sourceChecker.getY() - 2;
        
        if(targetX >= BOARD_SIZE || targetY < 0) {
            return false;
        }
        Checker neighbourChecker = board.getField(targetX-1, targetY+1).getChecker();
        Field targetField = board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField != null) {
            if(coordinatesToCapture != null) {
                coordinatesToCapture.add(new Coordinate (targetX, targetY));
            }
            return true;
        }
        return false;
    }

    /**
     * Sprawdza czy w obecnej turze ruch nalezy do komputera
     * @return true jesli tura komputera
     */
    private boolean isAITurn() {
        return players[active_player].isCpu();
    }

    /**
     * Wykonanie ruchu gracza komputerowego
     * 
     * @param moves - tablica mozliwych ruchow
     */
    private void makeAIMove(ArrayList<Move> moves) {
        
        int movesCount = moves.size();
        System.out.println("liczba dozwolonych ruchow: " + movesCount);
        int randomId = (int) Math.floor(Math.random() * movesCount);
        if(!moves.isEmpty()) {
            Move selectedMove = moves.get(randomId);
            System.out.println("wybrany ruch z ["+ selectedMove.getStartX() + ", " + selectedMove.getStartY() +
                               "] na [" + selectedMove.getEndX() + ", " + selectedMove.getEndY() + "]");
            selectChecker(selectedMove.getStartX(), selectedMove.getStartY());
            moveSelectedCheckerTo(selectedMove.getEndX(), selectedMove.getEndY());
        }
    }
}
