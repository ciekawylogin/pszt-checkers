package model;

import java.util.ArrayList;
import model.Model;

abstract class AI {
    

    /**
     * Wykonanie ruchu gracza komputerowego
     * 
     * @param moves - tablica mozliwych ruchow
     */
    static Move makeAIMove(ArrayList<Move> moves) {
        
        int movesCount = moves.size();
        System.out.println("liczba dozwolonych ruchow: " + movesCount);
        int randomId = (int) Math.floor(Math.random() * movesCount);
        if(!moves.isEmpty()) {
            Move selectedMove = moves.get(randomId);
            System.out.println("wybrany ruch z ["+ selectedMove.getStartX() + ", " + selectedMove.getStartY() +
                               "] na [" + selectedMove.getEndX() + ", " + selectedMove.getEndY() + "]");
            //selectChecker(selectedMove.getStartX(), selectedMove.getStartY());
            //moveSelectedCheckerTo(selectedMove.getEndX(), selectedMove.getEndY());
            return selectedMove;
        }
        return null;
    }
    
}
