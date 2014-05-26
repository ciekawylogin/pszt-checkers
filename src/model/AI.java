package model;

import java.util.ArrayList;

abstract class AI {
    /**
     * Wykonanie ruchu gracza komputerowego
     * 
     * @param model - odnosnik do modelu
     * @param AIMoves 
     * @return ruch w postaci listy pojedynczych posuniec 
     *  (wlasciwy ruch moze skladac sie z kilku mniejszych -> bicie wielokrotne)
     */
    static Move computeAIMove(Model model, ArrayList<Move> AIMoves) {
        // mozliwe posuniecia w pierwszym ruchu
    	ArrayList<Move> possibleMoves = new ArrayList<Move>();
    	
    	model.checkAllPossibleMoves(model.getCurrentPlayerColor(), possibleMoves);
    	int numberOfPossibleMoves = possibleMoves.size();
    	Move bestMove = null;
    	int bestMoveValue = -1000; // duza ujemna liczba
    	
    	for(Move move: possibleMoves)
    	{
    		int moveValue = minMax(move, getMaxDepth(), model);
    		if(moveValue > bestMoveValue)
    		{
    			bestMoveValue = moveValue;
    			bestMove = move;
    		}
    	}
        return bestMove;
    }

	private static int getMaxDepth() {
		return 2;
	}

	private static int minMax(Move move, int depth, Model model) {
		int result = 0;
		boolean playerUnchanged = model.performMove(move);
		if(model.getGameState() == GameState.PLAYER_1_WON) {
			result = +100;
		} else if (model.getGameState() == GameState.PLAYER_2_WON) {
			result = -100;
		} else if(depth == 0) {
			result = getGameStateValue();
		} else if (model.getGameState() == GameState.PLAYER_1_MOVE
				   ||  model.getGameState() == GameState.PLAYER_1_MOVE_REPEAT_MOVE){
				int max = -1000;
		    	ArrayList<Move> possibleMoves = new ArrayList<Move>();
				for(Move nextMove: possibleMoves) {
					int moveValue = minMax(nextMove, depth-1, model);
					if(moveValue > max)
					{
						max = moveValue;
					}
				}
				result = max;
		} else if (model.getGameState() == GameState.PLAYER_2_MOVE
			   ||  model.getGameState() == GameState.PLAYER_2_MOVE_REPEAT_MOVE){
			int min = 1000;
	    	ArrayList<Move> possibleMoves = new ArrayList<Move>();
			for(Move nextMove: possibleMoves) {
				int moveValue = minMax(nextMove, depth-1, model);
				if(moveValue < min)
				{
					min = moveValue;
				}
			}
			result = min;
		}
		if(!playerUnchanged)
		{
			model.changeActivePlayer();
		}
		model.undoLastMove();
		model.removeLastMoveFromHistory();
		return result;
	}

	private static int getGameStateValue() {
		return 0;
	}
    
}
