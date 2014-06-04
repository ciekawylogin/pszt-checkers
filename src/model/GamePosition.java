package model;

import java.util.ArrayList;

public class GamePosition implements AIGameState {

	private Board board;
	private GameState gameState;
	private AIMove lastMove;
	private int value;
	
	public GamePosition(Board board, GameState gameState) {
		this.setGameState(gameState);
		this.setBoard(board);

		if(isMaxPlayerColor()) {
			this.setValue(-1000); 
		} else {
			this.setValue(1000);
		}
	}
	
	@Override
	public int getValue() {
		return value != 1000 && value != -1000? value: computeValue();
	}

	@Override
	public ArrayList<? extends AIMove> getMoves() {
		Board boardCopy = Model.board;
		GameState stateCopy = Model.gameState;
		
		Model.board = getBoard();
		Model.gameState = getGameState();
		
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
		if(hasMaxPlayerWon()) {
			return 400;
		}
		if(hasMinPlayerWon()) {
			return -400;
		}

        return getBoard().countCheckers(CheckerType.NORMAL, CheckerColor.WHITE) *  4 
           	 + getBoard().countCheckers(CheckerType.NORMAL, CheckerColor.BLACK) * -4
        	 + getBoard().countCheckers(CheckerType.QUEEN, CheckerColor.WHITE)  *  12
        	 + getBoard().countCheckers(CheckerType.QUEEN, CheckerColor.BLACK)  * -12
        	 + getBoard().countCheckersInLine(6, CheckerType.NORMAL, CheckerColor.WHITE) *  4
        	 + getBoard().countCheckersInLine(5, CheckerType.NORMAL, CheckerColor.WHITE) *  2 
        	 + getBoard().countCheckersInLine(0, CheckerType.NORMAL, CheckerColor.WHITE) *  1 
        	 + getBoard().countCheckersInLine(1, CheckerType.NORMAL, CheckerColor.BLACK) * -4
        	 + getBoard().countCheckersInLine(2, CheckerType.NORMAL, CheckerColor.BLACK) * -2
        	 + getBoard().countCheckersInLine(7, CheckerType.NORMAL, CheckerColor.BLACK) * -1
        	 + getBoard().countBlockedcheckers(CheckerColor.WHITE) * -2
        	 + getBoard().countBlockedcheckers(CheckerColor.BLACK) *  2;
	}

	@Override
	public void updateValueFromChild(int value) {
		if(isMaxPlayerColor() && value != 1000) {
			this.setValue(Math.max(this.getValue(), value));
		} else if(isMinPlayerColor() && value != -1000) {
			this.setValue(Math.min(this.getValue(), value));
		}
	}

	/**
	 * Czy aktywny gracz jest graczem maksymalizuj¹cym (w warcabach: bia³ym)?
	 */
	@Override
	public boolean isMaxPlayerColor() {
		if(getGameState() == GameState.PLAYER_1_MOVE 
		|| getGameState() == GameState.PLAYER_1_MOVE_REPEAT_MOVE) {
			return Model.players[0].getPlayerColor() == CheckerColor.WHITE;
		} else if(getGameState() == GameState.PLAYER_2_MOVE 
			   || getGameState() == GameState.PLAYER_2_MOVE_REPEAT_MOVE) {
			return Model.players[1].getPlayerColor() == CheckerColor.WHITE;
		} else {
			//throw new RuntimeException("Unknown player");
			return false;
		}
	}

	/**
	 * Czy aktywny gracz jest graczem minimalizuj¹cym (w warcabach: czarnym)?
	 */
	@Override
	public boolean isMinPlayerColor() {
		if(getGameState() == GameState.PLAYER_1_MOVE 
		|| getGameState() == GameState.PLAYER_1_MOVE_REPEAT_MOVE) {
			return Model.players[0].getPlayerColor() == CheckerColor.BLACK;
		} else if(getGameState() == GameState.PLAYER_2_MOVE 
			   || getGameState() == GameState.PLAYER_2_MOVE_REPEAT_MOVE) {
			return Model.players[1].getPlayerColor() == CheckerColor.BLACK;
		} else {
			//throw new RuntimeException("Unknown player");
			return false;
		}
	}

	/**
	 * czy gracz maksymalizuj¹cy (w warcabach: bia³y) wygra³?
	 */
	@Override
	public boolean hasMaxPlayerWon() {
		return (getGameState() == GameState.PLAYER_1_WON && Model.players[0].getPlayerColor() == CheckerColor.WHITE) 
			|| (getGameState() == GameState.PLAYER_2_WON && Model.players[1].getPlayerColor() == CheckerColor.WHITE); 
	}

	/**
	 * czy gracz minimalizuj¹cy (w warcabach: czarny) wygra³?
	 */
	@Override
	public boolean hasMinPlayerWon() {
		return (getGameState() == GameState.PLAYER_1_WON && Model.players[0].getPlayerColor() == CheckerColor.BLACK) 
			|| (getGameState() == GameState.PLAYER_2_WON && Model.players[1].getPlayerColor() == CheckerColor.BLACK); 
	}
	

	public boolean equals(Object object) {
		if(this == object) {
			return true;
		}
		if(!(object instanceof GamePosition)) {
			return false;
		}
		GamePosition gamePosition = (GamePosition) object;
		for(int i=0;i<8;++i) {
			for(int j=0;j<8;++j) {
				Checker first = getBoard().getField(i, j).getChecker(), 
						second = gamePosition.getBoard().getField(i, j).getChecker();
				if(first == null && second == null) {
					continue;
				}
				if(first == null || second == null
				|| first.getColor() != second.getColor()
				|| first.getType() != second.getType()) {
					return false;
				}
			}
		}
		return getGameState() == gamePosition.getGameState();
	}
	
	public int hashCode() {
		int result = 0;
		for(int i=0;i<8;++i) {
			for(int j=0;j<8;++j) {
				int fieldFactor = 1 << ((i+9*j));
				int checkerFactor = 0;
				Checker checker = getBoard().getField(i, j).getChecker();
				if(checker != null) {
					checkerFactor += 1;
					if(checker.getColor() == CheckerColor.WHITE) {
						checkerFactor += 2;
					}
					if(checker.getType() == CheckerType.QUEEN) {
						checkerFactor += 4;
					}
				}
				result += fieldFactor * checkerFactor;
			}
		}
		return result;
	}

	@Override
	public void setValue(int value) {
		this.value = value;
	}
	
	void setLastMove(AIMove lastMove) {
		this.lastMove = lastMove;
	}

	GameState getGameState() {
		return gameState;
	}

	void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	Board getBoard() {
		return board;
	}

	void setBoard(Board board) {
		this.board = board;
	}

	@Override
	public boolean isEndState() {
		return hasMinPlayerWon() || hasMaxPlayerWon();
	}

}
