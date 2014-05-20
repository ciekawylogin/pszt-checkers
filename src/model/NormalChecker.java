package model;
import java.util.ArrayList;

abstract class NormalChecker {
    
    /**
     * Sprawdza czy ruch jest biciem za pomoca zwyklego pionka
     * @param moveToCheck
     * @return true jesli jest to bicie zwyklego pionka
     */
    static boolean isMoveACapture(final Move moveToCheck) {
        boolean isTargetToTheLeft = moveToCheck.getEndX() == moveToCheck.getStartX() - 2;
        boolean isTargetToTheRight = moveToCheck.getEndX() == moveToCheck.getStartX() + 2;
        boolean isTargetToTheTop = moveToCheck.getEndY() == moveToCheck.getStartY() - 2;
        boolean isTargetToTheBottom = moveToCheck.getEndY() == moveToCheck.getStartY() + 2;
        Checker sourceChecker = Model.board.getField(moveToCheck.getStartX(),
                moveToCheck.getStartY()).getChecker();
        
        if(isTargetToTheLeft && isTargetToTheTop && 
                isPossibleCaptureToTheLeftTopCorner(sourceChecker, null)) {
            return true;
            
        } else if(isTargetToTheRight && isTargetToTheTop && 
                isPossibleCaptureToTheRightTopCorner(sourceChecker, null)) {
            return true;
            
        } else if(isTargetToTheLeft && isTargetToTheBottom && 
                isPossibleCaptureToTheLeftBottomCorner(sourceChecker, null)) {
            return true;
            
        } else if(isTargetToTheRight && isTargetToTheBottom && 
                isPossibleCaptureToTheRightBottomCorner(sourceChecker, null)) {
            return true;
        }
        return false;
    }
    
    /**
     * Sprawdza czy jest mozliwe bicie dla zwyklych pionkow danego koloru.
     * Jesli sa mozliwe bicia, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param color
     * @param coordinatesToCapture
     * @return true jesli bicie mozliwe
     */
    static boolean checkPossibleCaptures(final CheckerColor color, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        Field fields [][];
        fields = Model.board.getFields();
        Checker checkerOnField;
        boolean isPossibleCapture = false;
        
        for(Field[] boardRow : fields) {
            for(Field field : boardRow) {
                checkerOnField = field.getChecker();
                if(checkerOnField != null && checkerOnField.getColor() == color && 
                        checkPossibleCapturesFromChecker(checkerOnField, coordinatesToCapture)) {
                    isPossibleCapture = true;
                    
                }
            }
        }
        return isPossibleCapture;
    }
    
    /**
     * Sprawdza czy jest mozliwe jakiekolwiek bicie z danego zwyklego pionka.
     * Jesli sa mozliwe bicia, wpisuje do tablicy wspolrzedne pionkow mozliwych do zbicia
     * @param sourceChecker
     * @return true jesli bicie mozliwe
     */
    private static boolean checkPossibleCapturesFromChecker(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        
        if(sourceChecker == null) {
            return false;
        }
        // sprawdzenie lewego gornego rogu
        if(isPossibleCaptureToTheLeftTopCorner(sourceChecker, coordinatesToCapture)) {
            return true;
        }
        // sprawdzenie prawego gornego rogu
        if(isPossibleCaptureToTheRightTopCorner(sourceChecker, coordinatesToCapture)) {
            return true;
        }
        // sprawdzenie lewego dolnego rogu
        if(isPossibleCaptureToTheLeftBottomCorner(sourceChecker, coordinatesToCapture)) {
            return true;
        }
        // sprawdzenie prawego dolnego rogu
        if(isPossibleCaptureToTheRightBottomCorner(sourceChecker, coordinatesToCapture)) {
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
    private static boolean isPossibleCaptureToTheLeftTopCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() - 2;
        int targetY = sourceChecker.getY() - 2;
        
        if(targetX < 0 || targetY < 0) {
            return false;
        }
        Checker neighbourChecker = Model.board.getField(targetX+1, targetY+1).getChecker();
        Field targetField = Model.board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField.getChecker() == null) {
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
    private static boolean isPossibleCaptureToTheRightTopCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() + 2;
        int targetY = sourceChecker.getY() - 2;
        
        if(targetX >= Model.BOARD_SIZE || targetY < 0) {
            return false;
        }
        Checker neighbourChecker = Model.board.getField(targetX-1, targetY+1).getChecker();
        Field targetField = Model.board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField.getChecker() ==  null) {
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
    private static boolean isPossibleCaptureToTheLeftBottomCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() - 2;
        int targetY = sourceChecker.getY() + 2;
        
        if(targetX < 0 || targetY >= Model.BOARD_SIZE) {
            return false;
        }
        Checker neighbourChecker = Model.board.getField(targetX+1, targetY-1).getChecker();
        Field targetField = Model.board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField.getChecker() ==  null) {
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
    private static boolean isPossibleCaptureToTheRightBottomCorner(final Checker sourceChecker, 
            ArrayList<Coordinate> coordinatesToCapture) {
        int targetX = sourceChecker.getX() + 2;
        int targetY = sourceChecker.getY() + 2;
        
        if(targetX >= Model.BOARD_SIZE || targetY >= Model.BOARD_SIZE) {
            return false;
        }
        Checker neighbourChecker = Model.board.getField(targetX-1, targetY-1).getChecker();
        Field targetField = Model.board.getField(targetX, targetY);
        if(neighbourChecker != null && neighbourChecker.getColor() != sourceChecker.getColor()
                && targetField.getChecker() ==  null) {
            if(coordinatesToCapture != null) {
                coordinatesToCapture.add(new Coordinate (targetX, targetY));
            }
            return true;
        }
        return false;
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
    static boolean isMoveCorrect(final int sourceX, final int sourceY, 
            final int targetX, final int targetY) {
        return (Model.board.getField(targetX, targetY).getChecker() == null) &&
            (
                isCaptureMoveCorrect(sourceX, sourceY, targetX, targetY)
                ||
                isNormalMoveCorrect(sourceX, sourceY, targetX, targetY)
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
    static boolean isCaptureMoveCorrect(final int sourceX, final int sourceY, 
            final int targetX, final int targetY) {
        boolean isTargetToTheLeft = targetX == sourceX - 2;
        boolean isTargetToTheRight = targetX == sourceX + 2;
        boolean isTargetToTheTop = targetY == sourceY - 2;
        boolean isTargetToTheBottom = targetY == sourceY + 2;

        int checkerToRemoveX = (targetX + sourceX) / 2;
        int checkerToRemoveY = (targetY + sourceY) / 2;

        Checker MovingChecker = Model.board.getField(sourceX, sourceY).getChecker();
        Checker CheckerToRemove = Model.board.getField(checkerToRemoveX, checkerToRemoveY).getChecker();

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
    static boolean isNormalMoveCorrect(final int sourceX, final int sourceY, 
            final int targetX, final int targetY) {
        Checker checker = Model.board.getField(sourceX, sourceY).getChecker();
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
     * Wykonuje ruch zwyklego pionka, sprawdzajac jego poprawnosc
     *
     * @param sourceX - wspolrzedna X poczatkowej pozycji
     * @param sourceY - wspolrzedna Y poczatkowej pozycji
     * @param targetX - wspolrzedna X koncowej pozycji
     * @param targetY - wspolrzedna Y koncowej pozycji
     * @return true jesli ruch zostal wykonany
     */
    static boolean makeMove(final int sourceX, final int sourceY, 
            final int targetX, final int targetY, final boolean forcedCapture) {
        if(targetX >= Model.BOARD_SIZE || targetY >= Model.BOARD_SIZE ||
                targetX < 0 || targetY < 0) {
            return false;
        }

        boolean normalMove = NormalChecker.isNormalMoveCorrect(sourceX, sourceY, targetX, targetY);
        boolean captureMove = NormalChecker.isCaptureMoveCorrect(sourceX, sourceY, targetX, targetY);
        boolean correctMove = normalMove || captureMove;
        
        if(forcedCapture) {
            correctMove &= isMoveACapture(new Move(sourceX, sourceY, targetX, targetY));
        };
        
        if(correctMove) {
            Field oldField = Model.board.getField(sourceX, sourceY);
            Checker checker = oldField.getChecker();
            oldField.removeChecker();
            Field newField = Model.board.getField(targetX, targetY);
            newField.setChecker(checker);
            checker.setPositionOnBoard(targetX, targetY);
            Queen.checkQueenCondition(targetX, targetY);
        }
        if (captureMove) {
            int checkerToRemoveX = (targetX + sourceX) / 2;
            int checkerToRemoveY = (targetY + sourceY) / 2;
            Model.board.getField(checkerToRemoveX, checkerToRemoveY).removeChecker();
        }
        return correctMove;
    }

}
