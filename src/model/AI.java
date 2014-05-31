package model;

import java.util.ArrayList;

import common.Tree;
import common.Tree.Node;
import common.Tree.TreeLevel;

class AI {

	/// Kolejne mo¿liwe stany gry, korzeñ = obecny stan gry
	private Tree<AIGameState> possibleStates;
    
	/// ile poziomow wglab zbadalismy?
	private int treeDepth;
	
	/**
	 * Inicjuje procedure obliczen ai
	 * @param state obecny stan gry
	 */
	public AI(AIGameState state)
	{
		possibleStates = new Tree<AIGameState>(state);
		treeDepth = 0;
	}
	
	/**
	 * Bada jeden poziom drzewa rekurencji
	 */
	public void buildNextTreeLevel() {
		TreeLevel<AIGameState> states = possibleStates.getTreeLevel(treeDepth);
		for(Node<AIGameState> node: states) {
			AIGameState state = node.getData();
			ArrayList<? extends AIMove> moves = state.getMoves();
			for(AIMove move: moves) {
				AIGameState newState = move.applyTo(state);
				node.addChild(newState);
			}
		}
		treeDepth++;
	}
	
	public void updateValues() {
		for(int i=treeDepth; i>0; --i) {
			TreeLevel<AIGameState> states = possibleStates.getTreeLevel(i);
			for(Node<AIGameState> node: states) {
				int value = node.isLeaf()? node.getData().computeValue(): node.getData().getValue();
				node.getParent().getData().updateValueFromChild(value);
			}
		}
	}

	public AIMove getBestMove() {
		TreeLevel<AIGameState> states = possibleStates.getTreeLevel(1);
		AIGameState bestState = null;
		int bestStateValue = -1000;
		for (Node<AIGameState> stateNode: states) {
			AIGameState state = stateNode.getData();
			int stateValue = -state.getValue(); // TODO: zrobic uniwerslanie, tj +/- w zaleznosci od tego ktory gracz
			if(stateValue > bestStateValue) {
				bestState = state;
				bestStateValue = stateValue;
			}
		}
		return bestState.getLastMove();
	}
	
	
	
}
