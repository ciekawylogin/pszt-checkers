package model;


import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import common.Tree;
import common.Tree.Node;
import common.Tree.TreeLevel;

class AI {
	/// Kolejne mo¿liwe stany gry, korzeñ = obecny stan gry
	private Tree<AIGameState> possibleStates;
    
	/// ile poziomow wglab zbadalismy?
	private int treeDepth;
	
	/// zbior sprawdzonych stanow
	private Map<AIGameState, AIGameState> visitedStates;
	
	/**
	 * Inicjuje procedure obliczen ai
	 * @param state obecny stan gry
	 */
	public AI(AIGameState state)
	{
		possibleStates = new Tree<AIGameState>(state);
		treeDepth = 0;
		visitedStates = new HashMap<AIGameState, AIGameState>();
	}
	
	public int alphaBeta(Node<AIGameState> node, int depth, int alpha, int beta) {
		AIGameState state = node.getData();
		if(depth == 0 || state.isEndState()) {
			return state.computeValue();
		}
		ArrayList<? extends AIMove> moves = state.getMoves();
		if(state.isMinPlayerColor()) {
			for(AIMove move: moves) {
				AIGameState newState = move.applyTo(state);
				Node<AIGameState> newNode = node.addChild(newState);
				beta = Math.min(beta, alphaBeta(newNode, depth-1, alpha, beta));
				if(alpha >= beta) {
					break;
				}
			}
			state.setValue(beta);
			return beta;
		} else if(state.isMaxPlayerColor()) {
			for(AIMove move: moves) {
				AIGameState newState = move.applyTo(state);
				Node<AIGameState> newNode = node.addChild(newState);
				alpha = Math.max(alpha, alphaBeta(newNode, depth-1, alpha, beta));
				if(alpha >= beta) {
					break;
				}
			}
			state.setValue(alpha);
			return alpha;
		} else {
			throw new RuntimeException("unknown player");
		}
	}

	public class MaxStateComparator implements Comparator<Node<AIGameState>> {
	    @Override
	    public int compare(Node<AIGameState> o1, Node<AIGameState> o2) {
	        return o1.getData().getValue() - o2.getData().getValue();
	    }
	}

	public class MinStateComparator implements Comparator<Node<AIGameState>> {
	    @Override
	    public int compare(Node<AIGameState> o1, Node<AIGameState> o2) {
	        return-o1.getData().getValue() + o2.getData().getValue();
	    }
	}
	
	public void buildTree(int maxMiliseconds) {
		long start = System.currentTimeMillis();
		
		boolean isMax = possibleStates.getRoot().getData().isMaxPlayerColor();
		Comparator<Node<AIGameState>> comparator = isMax? new MaxStateComparator(): new MinStateComparator();
		
		final int initLevels = 4 + maxMiliseconds / 1000;
		final int stepLevels = 3;
		final int positionsToCheck = 50;
		// czy gracz jest maksymalizujacy?
		alphaBeta(possibleStates.getRoot(), initLevels, -1000, +1000);
		treeDepth = initLevels;
		
		if(possibleStates.getRoot().getChildren().size() == 1) {
			return; // tylko 1 mozliwy ruch, nie ma sie co meczyc
		}

		System.out.print((System.currentTimeMillis() - start) + " milisekund");
		
		int i=0;
		while(true) {
			PriorityQueue<Node<AIGameState>> deepestLevel = new PriorityQueue<Node<AIGameState>>(200, comparator);
			for(Node<AIGameState> node: possibleStates.getTreeLevel(treeDepth)) {
				deepestLevel.add(node);
			}
			
			while(true) {
				long milisecondsElapsed = System.currentTimeMillis() - start;
				
				if(milisecondsElapsed >= maxMiliseconds) {
					System.out.println(i + " " + treeDepth);
					return;
				}
				
				Node<AIGameState> node = deepestLevel.poll();
				if(node == null) {
					break;
				}
				alphaBeta(node, stepLevels, -1000, +1000);
				propagateValue(node);
				i++;
			}
			treeDepth += stepLevels;
		}
	}

	private void propagateValue(Node<AIGameState> node) {
		Node<AIGameState> currentNode = node;
		while(currentNode.getParent() != null) {
			int value = currentNode.getData().getValue();
			currentNode.getParent().getData().updateValueFromChild(value);
			currentNode = currentNode.getParent();
		}
	}

	public AIMove getBestMove() {
		TreeLevel<AIGameState> states = possibleStates.getTreeLevel(1);
		AIGameState bestState = null;
		AIGameState currentState = possibleStates.getRoot().getData();
		if(currentState.isMaxPlayerColor()) {
			// gracz maksymalizujacy
			int bestStateValue = -1000;
			for (Node<AIGameState> stateNode: states) {
				AIGameState state = stateNode.getData();
				int stateValue = state.getValue(); 
				if(stateValue != 1000 && stateValue > bestStateValue) {
					bestState = state;
					bestStateValue = stateValue;
				}
			}
		} else {
			// gracz minimalizujacy
			int bestStateValue = +1000;
			for (Node<AIGameState> stateNode: states) {
				AIGameState state = stateNode.getData();
				int stateValue = state.getValue(); 
				if(stateValue == 1000 || stateValue == -1000)
				{
					continue;
				}
				if(stateValue != -1000 && stateValue < bestStateValue) {
					bestState = state;
					bestStateValue = stateValue;
				}
			}
		}
		return bestState.getLastMove();
	}
	
	
	
}
