package model;

import java.util.ArrayList;

public class GamePosition implements AIGameState {

	Board board;
	GameState gameState;
	AIMove lastMove;
	int value;
	
	public GamePosition(Board board, GameState gameState) {
		this.gameState = gameState;
		this.board = board;

		if(gameState == GameState.PLAYER_1_MOVE 
		|| gameState == GameState.PLAYER_1_MOVE_REPEAT_MOVE) {
			this.value = -1000; 
		} else {
			this.value = 1000;
		}
	}
	
	@Override
	public int getValue() {
		return value;
	}

	@Override
	public ArrayList<? extends AIMove> getMoves() {
		Board boardCopy = Model.board;
		GameState stateCopy = Model.gameState;
		
		Model.board = board;
		Model.gameState = gameState;
		
		ArrayList<? extends AIMove> possibleMoves = Model.instance.getPossibleMoves();
		
		Model.board = boardCopy;
		Model.gameState = stateCopy;
		return possibleMoves;
	}

	@Override
	public AIMove getLastMove() {
		return lastMove; 
	}

	@Override
	public int computeValue() {
		if(gameState == GameState.PLAYER_1_WON) {
			return 100;
		}
		if(gameState == GameState.PLAYER_2_WON) {
			return -100;
		}
        return board.countCheckers(CheckerType.NORMAL, CheckerColor.WHITE) * 1 
           	 + board.countCheckers(CheckerType.NORMAL, CheckerColor.BLACK) * -1
        	 + board.countCheckers(CheckerType.QUEEN, CheckerColor.WHITE) * 5
        	 + board.countCheckers(CheckerType.QUEEN, CheckerColor.BLACK) * -5; 
	}

	@Override
	public void updateValueFromChild(int value) {
		if(gameState == GameState.PLAYER_1_MOVE 
		|| gameState == GameState.PLAYER_1_MOVE_REPEAT_MOVE) {
			this.value = Math.max(this.value, value);
		} else {
			this.value = Math.min(this.value, value);
		}
	}
	
}
