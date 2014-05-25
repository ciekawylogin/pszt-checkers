package model;

import java.util.ArrayList;

import common.Coordinate;

/**
 * Statyczna klasa zawierajaca metody dla dam.
 *
 */
abstract class Queen {
    
    static ArrayList<Coordinate> deletedCheckers = new ArrayList<Coordinate>();
    
    /**
     * Sprawdza mozliwe bicia w kierunku prawego dolnego rogu
     * @param checkerOnField
     * @param coordinatesToCapture
     */
    private static boolean checkCapturesToTheBottomRightCorner(final Checker checkerOnField, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int x = checkerOnField.getX()+1;
        int y = checkerOnField.getY()+1;
        
        for(; x < Model.BOARD_SIZE && y < Model.BOARD_SIZE; ++x, ++y) {
            checkMove(checkerOnField.getX(), checkerOnField.getY(), 
                    x, y, coordinatesToCapture);
        }
        if(coordinatesToCapture != null && coordinatesToCapture.size() != 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza mozliwe bicia w kierunku prawego gornego rogu
     * @param checkerOnField
     * @param coordinatesToCapture
     */
    private static boolean checkCapturesToTheTopRightCorner(final Checker checkerOnField, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int x = checkerOnField.getX()+1;
        int y = checkerOnField.getY()-1;
        
        for(; x < Model.BOARD_SIZE && y >= 0; ++x, --y) {
            checkMove(checkerOnField.getX(), checkerOnField.getY(), 
                    x, y, coordinatesToCapture);
        }
        if(coordinatesToCapture != null && coordinatesToCapture.size() != 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza mozliwe bicia w kierunku lewego dolnego rogu
     * @param checkerOnField
     * @param coordinatesToCapture
     */
    private static boolean checkCapturesToTheBottomLeftCorner(final Checker checkerOnField, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int x = checkerOnField.getX()-1;
        int y = checkerOnField.getY()+1;
        
        for(; x >= 0 && y < Model.BOARD_SIZE; --x, ++y) {
            checkMove(checkerOnField.getX(), checkerOnField.getY(), 
                    x, y, coordinatesToCapture);
        }
        if(coordinatesToCapture != null && coordinatesToCapture.size() != 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza mozliwe bicia w kierunku lewego gornego rogu
     * @param checkerOnField
     * @param coordinatesToCapture
     */
    private static boolean checkCapturesToTheTopLeftCorner(final Checker checkerOnField, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int x = checkerOnField.getX()-1;
        int y = checkerOnField.getY()-1;
        
        for(; x >= 0 && y >=0; --x, --y) {
            checkMove(checkerOnField.getX(), checkerOnField.getY(), 
                    x, y, coordinatesToCapture);
        }
        if(coordinatesToCapture != null && coordinatesToCapture.size() != 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy zwykly pionek moze stac sie dama
     *
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     */
    static void checkQueenCondition(final int targetX, final int targetY) {
        if(targetY == 0 || targetY == Model.BOARD_SIZE-1) {
            Model.board.getField(targetX, targetY).getChecker().promote();
        }
    }
    
    /**
     * Wykonuje dany ruch dla damy, sprawdzajac czy ten ruch jest poprawny
     *
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return ArrayList<Coordinate> - lista zbitych pionkow, jesli ruch zostal wykonany (null jesli ruch byl bledny)
     */
    static ArrayList<Coordinate> makeMove(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, final boolean forcedCapture) {
        ArrayList<Coordinate> coordinatesToDelete = new ArrayList<>();
        boolean correctMove = checkMove(sourceX, sourceY, targetX, targetY, coordinatesToDelete);
        if(forcedCapture) {
            correctMove &= isMoveACapture(new Move(sourceX, sourceY, targetX, targetY, -1));
        };

        if(correctMove) {
            setOnPosition(sourceX, sourceY, targetX, targetY);
            
            
            // usuwanie potencjalnych ofiar ruchu
            removeCapturedCheckers(coordinatesToDelete);
            return coordinatesToDelete;
            
        } else {
            coordinatesToDelete.clear();
        }
        return null;
    }
    
    /**
     * Ustawia pionek damy na wskazanej pozycji usuwajac go ze starej pozycji.
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     */
    private static void setOnPosition(final int sourceX, final int sourceY, 
            final int targetX, final int targetY) {
        Field oldField = Model.board.getField(sourceX, sourceY);
        Checker checker = oldField.getChecker();
        oldField.removeChecker();
        Field newField = Model.board.getField(targetX, targetY);
        newField.setChecker(checker);
        checker.setPositionOnBoard(targetX, targetY);
    }
    
    /**
     * Usuwa zbite podczas ruchu pionki
     * @param coordinatesToDelete
     */
    private static void removeCapturedCheckers(ArrayList<Coordinate> coordinatesToDelete) {
        for(Coordinate coordToDelete: coordinatesToDelete) {
            int x = coordToDelete.getX();
            int y = coordToDelete.getY();
            //Model.board.getField(x, y).removeChecker();
            deletedCheckers.add(new Coordinate(x, y));
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
    static boolean checkMove(final int sourceX, final int sourceY,
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        // sprawdz czy docelowe pole jest puste badz poza plansza
        if(Model.board.getField(targetX, targetY).getChecker() != null &&
                targetX < Model.BOARD_SIZE && targetY < Model.BOARD_SIZE &&
                targetX >= 0 && targetY >= 0) {
            return false;
        }
        boolean isTargetToTheLeft = targetX < sourceX ? true : false;
        boolean isTargetToTheTop = targetY < sourceY ? true : false;

        // cel: lewy gorny rog
        if(isTargetToTheLeft && isTargetToTheTop) {
            return checkMoveToTheLeftTopCorner(sourceX, sourceY, 
                    targetX, targetY, coordinatesToDelete);
            
        // cel: lewy dolny rog
        } else if(isTargetToTheLeft && !isTargetToTheTop) {
            return checkMoveToTheLeftBottomCorner(sourceX, sourceY, 
                    targetX, targetY, coordinatesToDelete);
            
        // cel: prawy gorny rog
        } else if(!isTargetToTheLeft && isTargetToTheTop) {
            return checkMoveToTheRightTopCorner(sourceX, sourceY, 
                    targetX, targetY, coordinatesToDelete);
            
        // cel: prawy dolny rog
        } else if(!isTargetToTheLeft && !isTargetToTheTop) {
            return checkMoveToTheRightBottomCorner(sourceX, sourceY, 
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
    private static boolean checkMoveToTheLeftTopCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        int x = sourceX -1;
        int y = sourceY -1;
        
        if(x < 0 || y < 0) {
            return false;
        }
        
        CheckerColor colorSource = Model.board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x > targetX && y > targetY; --x, --y) {
            Checker temp = Model.board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                
                if(coordinatesToDelete != null && coordinatesToDelete.size() != 0
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getX() -1 == x
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getY() -1 == y) {
                    coordinatesToDelete.clear();
                }
                
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
    private static boolean checkMoveToTheRightTopCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        int x = sourceX +1;
        int y = sourceY -1;
        
        if(x >= Model.BOARD_SIZE || y < 0) {
            return false;
        }
        
        CheckerColor colorSource = Model.board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x < targetX && y > targetY; ++x, --y) {
            Checker temp = Model.board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                
                if(coordinatesToDelete != null && coordinatesToDelete.size() != 0
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getX() +1 == x
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getY() -1 == y) {
                    coordinatesToDelete.clear();
                }
                
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
    private static boolean checkMoveToTheRightBottomCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        int x = sourceX +1;
        int y = sourceY +1;
        
        if(x >= Model.BOARD_SIZE || y >= Model.BOARD_SIZE) {
            return false;
        }
        
        CheckerColor colorSource = Model.board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x < targetX && y < targetY; ++x, ++y) {
            Checker temp = Model.board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                
                if(coordinatesToDelete != null && coordinatesToDelete.size() != 0
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getX() +1 == x
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getY() +1 == y) {
                    coordinatesToDelete.clear();
                }
                
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
    private static boolean checkMoveToTheLeftBottomCorner(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, ArrayList<Coordinate> coordinatesToDelete) {
        
        int x = sourceX -1;
        int y = sourceY +1;
        
        if(x < 0 || y >= Model.BOARD_SIZE) {
            return false;
        }
        
        CheckerColor colorSource = Model.board.getField(sourceX, sourceY).getChecker().getColor();
        
        for(; x > targetX && y < targetY; --x, ++y) {
            Checker temp = Model.board.getField(x, y).getChecker();
            if(temp != null && temp.getColor() == colorSource) {
                
                if(coordinatesToDelete != null && coordinatesToDelete.size() != 0
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getX() -1 == x
                        && coordinatesToDelete.get(coordinatesToDelete.size()-1).getY() +1 == y) {
                    coordinatesToDelete.clear();
                }
                
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
     * Sprawdza czy ruch jest biciem za pomoca damy
     * @param moveToCheck
     * @return true jesli jest to bicie damy
     */
    static boolean isMoveACapture(final Move moveToCheck) {
        ArrayList<Coordinate> coordinatesToDelete = new ArrayList<Coordinate>();
        checkMove(moveToCheck.getStartX(), moveToCheck.getStartY(),
                moveToCheck.getEndX(), moveToCheck.getEndY(), coordinatesToDelete);
        if(coordinatesToDelete.size() != 0) {
            coordinatesToDelete = null;
            return true;
        }
        
        coordinatesToDelete = null;
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie dla dam danego koloru.
     * Jesli sa mozliwe bicia, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param color
     * @param coordinatesToCapture
     * @return true jesli bicie jest mozliwe
     */
    static boolean checkPossibleCaptures(final CheckerColor color, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        Checker checkerOnField;
        boolean isCapturePossible = false;
        
        for(Field[] rows : Model.board.getFields()) {
            
            for(Field field : rows) {
                checkerOnField = field.getChecker();
                if(checkerOnField!= null && checkerOnField.getType() == CheckerType.QUEEN && 
                        checkerOnField.getColor() == color) {
                    
                    isCapturePossible |= 
                            checkPossibleCapturesFromQueen(checkerOnField, coordinatesToCapture);
                    
                    }
                }
                
        }
        
        
        return isCapturePossible;
    }
    
    /**
     * Sprawdza czy sa mozliwe bicia dla konkretnej damy.
     * Jesli tak, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param sourceChecker
     * @param coordinatesToCapture
     * @return
     */
    static boolean checkPossibleCapturesFromQueen(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        boolean isCapturePossible = false;
        
        // cel: prawy dolny rog
        isCapturePossible |= checkCapturesToTheBottomRightCorner(sourceChecker, 
                coordinatesToCapture);
        
        // cel: prawy gorny rog
        isCapturePossible |= checkCapturesToTheTopRightCorner(sourceChecker, 
                coordinatesToCapture);
        
        // cel: lewy dolny rog
        isCapturePossible |= checkCapturesToTheBottomLeftCorner(sourceChecker, 
                coordinatesToCapture);
        
        // cel: lewy gorny rog
        isCapturePossible |= checkCapturesToTheTopLeftCorner(sourceChecker, 
                coordinatesToCapture);
        
        return isCapturePossible;
        
        
    }

}
