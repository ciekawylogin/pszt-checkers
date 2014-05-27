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
    	if(!model.isAITurn())
    	{
    		return null;
    	}
    	
    	int AIPlayer = model.getActivePlayerNumber();
    	
        // mozliwe posuniecia w pierwszym ruchu
    	ArrayList<Move> possibleMoves = new ArrayList<Move>();
    	
    	model.checkAllPossibleMoves(model.getCurrentPlayerColor(), possibleMoves);

    	Move bestMove = null;
    	if(model.getCurrentPlayerColor() == CheckerColor.WHITE)
    	{
	    	int bestMoveValue = -1000; // duza ujemna liczba
	    	
	    	for(Move move: possibleMoves)
	    	{
	    		int moveValue = minMax(move, getMaxDepth(model), model);
	    		if(moveValue >= bestMoveValue)
	    		{
	    			bestMoveValue = moveValue;
	    			bestMove = move;
	    		}
	    	}
    	}
    	else // czarne
    	{
	    	int bestMoveValue = +1000; // duza dodatnia liczba
	    	
	    	for(Move move: possibleMoves)
	    	{
	    		int moveValue = minMax(move, getMaxDepth(model), model);
	    		if(moveValue <= bestMoveValue)
	    		{
	    			bestMoveValue = moveValue;
	    			bestMove = move;
	    		}
	    	}
    	}
        return bestMove;
    }

	private static int getMaxDepth(Model model) {
		if(model.getActivePlayer().getGameLevel() == GameLevel.EASY)
		{
			return 2;
		}
		else if(model.getActivePlayer().getGameLevel() == GameLevel.MEDIUM)
		{
			return 4;
		}
		else
		{
			return 6;
		}
	}

	private static int minMax(Move move, int depth, Model model) {
		System.out.println(" > MinMax("+depth+"), gameState = " + model.getGameState().name());
		int result = 0;
		boolean playerUnchanged = model.performMove(move);
		if(model.hasWhiteWon()) {
			result = +100;
		} else if (model.hasBlackWon()) {
			result = -100;
		} else if(depth == 0) {
			result = getGameStateValue(model);
		} else if (model.getCurrentPlayerColor() == CheckerColor.WHITE){
				int max = -1000;
		    	ArrayList<Move> possibleMoves = model.getPossibleMoves();
				for(Move nextMove: possibleMoves) {
					int moveValue = minMax(nextMove, depth-1, model);
					if(moveValue >= max)
					{
						max = moveValue;
					}
				}
				result = max;
		} else if (model.getCurrentPlayerColor() == CheckerColor.BLACK){
			int min = 1000;
	    	ArrayList<Move> possibleMoves = model.getPossibleMoves();
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
		System.out.println(" < MinMax("+depth+")");
		return result;
	}

	private static int getGameStateValue(Model model) {
		int whiteCheckers = model.countCheckers(CheckerType.NORMAL, CheckerColor.WHITE);
		int blackCheckers = model.countCheckers(CheckerType.NORMAL, CheckerColor.BLACK);

		int whiteQueens = model.countCheckers(CheckerType.QUEEN, CheckerColor.WHITE);
		int blackQueens = model.countCheckers(CheckerType.QUEEN, CheckerColor.BLACK);
		
		int value = whiteCheckers + whiteQueens * 5
				  - blackCheckers - blackQueens * 5;
		
		return value;
	}
    
}
